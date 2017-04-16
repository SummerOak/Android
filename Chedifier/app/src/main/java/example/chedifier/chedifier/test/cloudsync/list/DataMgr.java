package example.chedifier.chedifier.test.cloudsync.list;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Administrator on 2017/3/29.
 */

public class DataMgr {

    private static final String TAG = "DataMgr";

    public DataMgr(){

    }

    private ArrayList<SyncData> mData = new ArrayList<>();
    private ArrayList<SyncData> mModifies = new ArrayList<>();
    private ArrayList<SyncData> mAdds = new ArrayList<>();
    private ArrayList<SyncData> mDeletes = new ArrayList<>();

    public ArrayList<SyncData> getData(){
        return mData;
    }

    public SyncData getHead(){
        return mData.size() > 0 ? mData.get(0):null;
    }

    public SyncData getData(int pos){
        if(0<=pos&&pos<mData.size()){
            return mData.get(pos);
        }

        return null;
    }

    public void setData(Collection<SyncData> data){
        mAdds.clear();
        mModifies.clear();
        mDeletes.clear();
        mData.clear();

        if(data != null){
            mData.addAll(data);
        }
    }

    public void afterSync(){
        for(int i=0;i<mData.size();i++){
            mData.get(i).updateDirtyOrder(false);
        }

        mAdds.clear();
        mModifies.clear();
        mDeletes.clear();
    }

    public void copyFrom(ArrayList<SyncData> data){
        mModifies.clear();
        mAdds.clear();
        mDeletes.clear();
        mData.clear();

        if(data != null){
            for(int i=0;i<data.size();i++){
                mData.add(data.get(i).clone());

                if(i > 0){
                    mData.get(i-1).next = mData.get(i);
                }
            }
        }
    }

    public SyncData getTail(){
        return mData.size()>0?mData.get(mData.size()-1):null;
    }

    public void add(SyncData data){
        SyncData tail = getTail();
        if(tail != null){
            tail.next = data;
        }

        data.next = null;

        mData.add(data);
        mAdds.add(data);
    }

    public void delete(SyncData data){
        int indexOfData = mData.indexOf(data)-1;
        if(indexOfData >= 0){
            SyncData pre = mData.get(indexOfData);
            if(pre != null){
                pre.next = data.next;
            }
        }
        mData.remove(data);

        mModifies.remove(data);
        mAdds.remove(data);
        mDeletes.add(data);
    }

    //data have already changed!
    public boolean swapData(int pos1,int pos2){

        Log.i(TAG,"swap " + pos1 + " " + pos2);

        if(pos1 == pos2){
            return false;
        }

        SyncData data = getData(pos2);
        SyncData data2 = getData(pos2+1);
        SyncData prePos2 = getData(pos2-1);
        if(prePos2 != null){
            prePos2.updateNext(data);
            mModifies.add(prePos2);
        }

        SyncData prePos1 = null;
        if(pos1 > pos2){
            prePos1 = getData(pos1);
        }else{
            prePos1 = getData(pos1-1);
        }

        if(prePos1 != null){
            prePos1.updateNext(data.next);
            mModifies.add(prePos1);
        }

        data.updateNext(data2);
        mModifies.add(data);

        return false;
    }

    public void logout(){
        String s = "";
        for(int i=0;i<mData.size();i++){
            s += String.valueOf(mData.get(i).id);
        }
        Log.i(TAG,s);
    }
}
