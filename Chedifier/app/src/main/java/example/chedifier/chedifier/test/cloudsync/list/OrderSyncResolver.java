package example.chedifier.chedifier.test.cloudsync.list;

import android.support.v4.util.Pair;
import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

/**
 * Created by Administrator on 2017/3/30.
 */

public class OrderSyncResolver {

    private static final String TAG = "OrderSyncResolver";

    private SyncData mCloudHead;
    private SyncData mLocalHead;

    private Map<Long,SyncData> mlId2Datas;
    private Map<Long,SyncData> mcId2Datas;

    private Map<Long,Long> mResult = new HashMap<>();
    private Map<Long,Long> mResult2 = new HashMap<>();
    private Map<Long,Boolean> mResultUsingCloud = new HashMap<>();

    private Map<Long,Pair<SyncData,SyncData>> mOCL = new HashMap<>();
    private Map<SyncData,Pair<Long,SyncData>> mCOL = new HashMap<>();
    private Map<SyncData,Pair<Long,SyncData>> mLOC = new HashMap<>();

    //add fake head&tail to the table
    private final SyncData L_HEAD = new SyncData(SyncData.ID_HEAD);
    private final SyncData L_TAIL = new SyncData(SyncData.ID_TAIL);

    private final SyncData C_HEAD = new SyncData(SyncData.ID_HEAD);
    private final SyncData C_TAIL = new SyncData(SyncData.ID_TAIL);

    public OrderSyncResolver(SyncData cloudHead, SyncData localHead){
        mCloudHead = cloudHead;
        mLocalHead = localHead;

        preProcess();
    }

    private void preProcess(){
        Log.i(TAG,"preProcess >>>");
        C_HEAD.next = mCloudHead;
        SyncData p = C_HEAD;
        while(p.next != null){
            Log.i(TAG,"preProcess1 " + p.id + "("+(p.dirtyOrder?1:0)+")"+ " -> " + p.next.id + "("+(p.next.dirtyOrder?1:0)+")");
            p = p.next;
        }
        p.next = C_TAIL;

        Log.i(TAG,"4");

        L_HEAD.next = mLocalHead;
        p = L_HEAD;
        while(p.next != null){
            Log.i(TAG,"preProcess2 " + p.id + "("+(p.dirtyOrder?1:0)+")"+ " -> " + p.next.id+ "("+(p.next.dirtyOrder?1:0)+")");
            p = p.next;
        }
        p.next = L_TAIL;

        Log.i(TAG,"3");

        mlId2Datas = buildMap(L_HEAD);
        mcId2Datas = buildMap(C_HEAD);
        Log.i(TAG,"2");

        SyncData ch = mCloudHead==null?C_TAIL:mCloudHead;
        SyncData lh = mLocalHead==null?L_TAIL:mLocalHead;
        mOCL.put(SyncData.ID_HEAD,new Pair<SyncData, SyncData>(ch,lh));
        mCOL.put(ch,new Pair<Long, SyncData>(SyncData.ID_HEAD,lh));
        mLOC.put(lh,new Pair<Long, SyncData>(SyncData.ID_HEAD,ch));

        Log.i(TAG,"1");

        updateTable(mCloudHead,false);
        updateTable(mLocalHead,true);

        Log.i(TAG,"preProcess <<<");
    }


    public SyncData resolve() throws Exception {
        Log.i(TAG,"resolve step1 >>>");
        // step1: if cloud next same with local next ,use one of it
        Iterator<Map.Entry<Long,Pair<SyncData,SyncData>>> iterator = mOCL.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<Long,Pair<SyncData,SyncData>> entry = iterator.next();
            SyncData c = entry.getValue().first;
            SyncData l = entry.getValue().second;

            if(c != null && l != null && c.id == l.id){
                if(setNextWithAdjust(entry.getKey(),c.id,true)){
                }else{
                    logoutTable();
                    throw new Exception("failed in step1.");
                }
            }
        }

        Log.i(TAG,"resolve step2 >>>");
        //step2: cloud add or just modify in cloud use cloud data
        SyncData p = C_HEAD;
        while(p != null && p.id != SyncData.ID_TAIL && p.next != null){
            SyncData c = mOCL.get(p.id).first;

            SyncData localP = mlId2Datas.get(p.id);
            if(p.dirtyOrder && (localP == null || !localP.dirtyOrder)){
                if(setNextWithAdjust(p.id,c.id,true)){
                }else{
                    logoutTable();
                    throw new Exception("failed in step2.");
                }
            }

            p = p.next;
        }

        Log.i(TAG,"resolve step3 >>>");
        //step3:
        processLocalUpdate();
        processCloudUpdate();

        return adjustDirty(buildResultList());
    }

    private void processCloudUpdate() throws Exception{
        SyncData p = C_HEAD;
        while(p != null && p.id != SyncData.ID_TAIL){
            if(getNext(p.id) == SyncData.ID_NONE){
                SyncData oc = mcId2Datas.get(p.id);
                SyncData ol = mlId2Datas.get(p.id);

                if(ol == null){
                    throw new Exception("cloud add item ,must have been processed in step 2!");
                }

                Pair<SyncData,SyncData> cl = mOCL.get(p.id);

                if(oc.last_modify >= ol.last_modify){
                    if(!setNextWithAdjust(p.id,cl.first.id,true)){
                        if(!setNextWithAdjust(p.id,cl.second.id,false)){
                            throw new Exception("failed in step3.");
                        }
                    }
                }else{
                    if(!setNextWithAdjust(p.id,cl.second.id,false)){
                        if(!setNextWithAdjust(p.id,cl.first.id,true)){
                            throw new Exception("failed in step3.");
                        }
                    }
                }
            }

            p = p.next;
        }
    }

    private void processLocalUpdate() throws Exception {

        Stack<SyncData> lAdd = new Stack<>();

        SyncData p = L_HEAD;
        while(p != null && p.id != SyncData.ID_TAIL){
            if(getNext(p.id) == SyncData.ID_NONE){
                SyncData oc = mcId2Datas.get(p.id);
                SyncData ol = mlId2Datas.get(p.id);

                if(oc == null){// local add,process in revert order
                    lAdd.push(p);
                    p = p.next;
                    continue;
                }

                Pair<SyncData,SyncData> cl = mOCL.get(p.id);

                // local and cloud both have update
                if(ol.last_modify >= oc.last_modify){ // local update is new than cloud, try
                    if(!setNextWithAdjust(p.id,cl.second.id,false)){
                        if(!setNextWithAdjust(p.id,cl.first.id,true)){
                            logoutTable();
                            throw new Exception("failed while processLocalUpdate.");
                        }
                    }
                }else{
                    if(!setNextWithAdjust(p.id,cl.first.id,true)){
                        if(!setNextWithAdjust(p.id,cl.second.id,false)){
                            logoutTable();
                            throw new Exception("failed while processLocalUpdate.");
                        }
                    }
                }
            }

            p = p.next;
        }

        //insert local add item in front of currently list
        while(!lAdd.isEmpty()){
            p = lAdd.pop();

            long paid = ancestorOf(p.id);
            long aid = ancestorOf(mOCL.get(p.id).second.id);
            if(paid == aid){
                logoutTable();
                throw new Exception("failed while processLocalUpdate local add.");
            }

            if(aid == SyncData.ID_HEAD){
                SyncData head = getEffectiveHead();
                if(head != null){
                    setNext(p.id, head.next.id,mResultUsingCloud.get(head.id));
                    setNext(head.id,p.id,false);
                }

            }else{
                setNext(p.id,aid,false);
            }
        }
    }

    private long ancestorOf(long id){
        while(mResult2.get(id) != null){
            id = mResult2.get(id);
        }

        return id;
    }

    private SyncData getEffectiveHead(){
        Boolean usingCloud = mResultUsingCloud.get(SyncData.ID_HEAD);
        if(usingCloud == null){
            return null;
        }

        return usingCloud?C_HEAD:L_HEAD;
    }

    private void setNext(long id,long next,boolean usingCloud){
        mResult.put(id,next);
        mResult2.put(next,id);

        if(next == SyncData.ID_NONE){
            mResultUsingCloud.remove(id);
        }else{
            mResultUsingCloud.put(id,usingCloud);
        }
    }
    private long getNext(long id){
        Long r = mResult.get(id);
        if(r != null){
            return r;
        }
        return SyncData.ID_NONE;
    }

    private boolean setNextWithAdjust(long id, long next_id, boolean usingCloud) throws Exception {
        long cur = getNext(id);
        if(cur != SyncData.ID_NONE){
            if(cur != next_id){
                return false;
            }else{
                return true;
            }
        }

        if(ancestorOf(next_id) == id){
            return false;
        }

        setNext(id,next_id,usingCloud);

        if(usingCloud){
            Pair<Long,SyncData> pair = mLOC.get(mcId2Datas.get(next_id));
            if(pair != null && pair.second != null){
                if(!setNextWithAdjust(pair.first,pair.second.id,usingCloud)){
                    setNext(id,SyncData.ID_NONE,usingCloud);//revert
                    return false;
                }
            }
        }else{
            Pair<Long,SyncData> pair = mCOL.get(mlId2Datas.get(next_id));
            if(pair != null && pair.second != null){
                if(!setNextWithAdjust(pair.first,pair.second.id,usingCloud)){
                    setNext(id,SyncData.ID_NONE,usingCloud);//revert
                    return false;
                }
            }
        }

        return true;
    }

    private void updateTable(SyncData head,boolean local){

        SyncData p = head;
        while(p != null && p.next != null){
            SyncData c = null;
            SyncData co = mcId2Datas.get(p.id);
            if(co != null && co.next != null){
                c = mcId2Datas.get(co.next.id);
            }

            SyncData l = null;
            SyncData lo = mlId2Datas.get(p.id);
            if(lo != null && lo.next != null){
                l = mlId2Datas.get(lo.next.id);
            }

            mOCL.put(p.id,new Pair<SyncData, SyncData>(c,l));

            if(c != null){
                mCOL.put(c,new Pair<Long, SyncData>(p.id,l));
            }

            if(l != null){
                mLOC.put(l,new Pair<Long, SyncData>(p.id,c));
            }

            p = p.next;
        }
    }

    private SyncData buildResultList() throws Exception {
        Log.i(TAG,"buildResultList>>>");

        Iterator<Map.Entry<Long,Long>> itr = mResult.entrySet().iterator();
        while(itr.hasNext()){
            Map.Entry<Long,Long> entry = itr.next();
            long id = entry.getKey();
            long next_id = entry.getValue();

            if(id == SyncData.ID_TAIL){
                continue;
            }

            SyncData data = getResultData(id);

            if(next_id == SyncData.ID_TAIL){
                data.next = null;
                continue;
            }

            SyncData nextData = getResultData(next_id);
            if(data == null){
                logout();
                throw new Exception("build reuslt list failed");
            }

            data.next = nextData;
        }

        SyncData head = getEffectiveHead();
        if(!checkResult(head,mOCL.size())){
            logout();
            throw new Exception("build reuslt list failed");
        }

        return head.next;
    }

    private SyncData adjustDirty(SyncData head){
        SyncData p = head;
        while(p != null){
            p.updateDirtyOrder(!mResultUsingCloud.get(p.id));
            p = p.next;
        }

        return head;
    }

    private boolean checkResult(SyncData head,int size){
        int count = 0;
        while(head != null && count <= size){
            head=head.next;
            count++;
        }

        if(count == size){
            return true;
        }

        return false;
    }

    private SyncData getResultData(long id){
        Boolean usingCloud = mResultUsingCloud.get(id);
        if(usingCloud == null){
            return null;
        }

        return usingCloud?mcId2Datas.get(id):mlId2Datas.get(id);
    }

    private Map<Long,SyncData> buildMap(SyncData head){
        Map<Long,SyncData> m = new HashMap<>();
        while(head != null){
            m.put(head.id,head);
            head = head.next;
        }

        return m;
    }

    private void logout(){
        logoutTable();
        logoutResult();
    }

    private void logoutTable(){
        Iterator<Map.Entry<Long,Pair<SyncData,SyncData>>> iterator = mOCL.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<Long,Pair<SyncData,SyncData>> entry = iterator.next();
            SyncData c = entry.getValue().first;
            SyncData l = entry.getValue().second;

            long oid = entry.getKey();
            boolean dc = mcId2Datas.get(oid)==null?false:mcId2Datas.get(oid).dirtyOrder;
            boolean dl = mlId2Datas.get(oid)==null?false:mlId2Datas.get(oid).dirtyOrder;

            Log.i(TAG,oid + "("+(dc?1:0)+"" + (dl?1:0)+")" + (c==null?"?":c.id) + "" + (l==null?"?":l.id));
        }
    }

    private void logoutResult(){

        Map<Long,Long> t = new HashMap<>();
        t.putAll(mResult);

        String s = "";
        SyncData h = getEffectiveHead();
        while(h != null && t.containsKey(h.id)){
            s += h.id;
            t.remove(h.id);
            h = h.next;
        }

        s += "#";

        Iterator<Map.Entry<Long,Long>> itr = t.entrySet().iterator();
        while(itr.hasNext()){
            Map.Entry<Long,Long> entry = itr.next();
            s += entry.getKey() + " -> " + entry.getValue();
        }

        Log.i(TAG,s);
    }


}
