package example.chedifier.chedifier.test.cloudsync.list;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/31.
 */

public class SyncResolver {

    private List<SyncData> mCAdd;
    private List<SyncData> mCDel;
    private List<SyncData> mCModify;
    private List<SyncData> mLAdd;
    private List<SyncData> mLDel;
    private List<SyncData> mLModify;

    private List<SyncData> mCloudUpdateClosure = new ArrayList<>();
//    private List<SyncData>

    public SyncResolver(List<SyncData> cAdd,List<SyncData> cDel,List<SyncData> cModify,
                        List<SyncData> lAdd,List<SyncData> lDel,List<SyncData> lModify){

        mCAdd = cAdd;
        mCDel = cDel;
        mCModify = cModify;
        mLAdd = lAdd;
        mLDel = lDel;
        mLModify = lModify;


    }

//    private void processDeletes(){
//        if(mCDel != null){
//            for(SyncData del:mCDel){
//                mLocalHead = deleteIfNeeded(mLocalHead,del);
//            }
//        }
//        if(mLDel != null){
//            for(SyncData del:mLDel){
//                mCloudHead = deleteIfNeeded(mCloudHead,del);
//            }
//        }
//    }

    private SyncData deleteIfNeeded(SyncData head,SyncData del){
        SyncData ret = head;
        if(del == null){
            return ret;
        }

        SyncData p = head;
        SyncData pre = null;
        while (p != null) {
            if(p.id == del.id && del.last_modify > p.last_modify){
                if(pre != null){
                    pre.next = p.next;
                }else {
                    ret = p.next;
                }

                p = p.next;
                continue;
            }

            pre = p;
            p = p.next;
        }

        return ret;
    }

}
