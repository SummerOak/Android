package example.chedifier.chedifier.test.cloudsync.framework;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/6.
 */

public class Patch<T extends AbsSyncItem> {

    private ArrayList<T> mModifies = new ArrayList<>();
    private ArrayList<T> mAdds = new ArrayList<>();
    private ArrayList<T> mDeletes = new ArrayList<>();

    public boolean add(T b){
        if(b.updateDirtyType(AbsSyncItem.DIRTY_TYPE.ADD)){
            Helper.addListWithoutDuplicate(mAdds,b);
            return true;
        }

        return false;
    }

    public List<T> getAdd(){
        return mAdds;
    }

    public boolean delete(T b){
        if (b.updateDirtyType(AbsSyncItem.DIRTY_TYPE.DELETE)) {
            mModifies.remove(b);
            mAdds.remove(b);
            Helper.addListWithoutDuplicate(mDeletes,b);
            return true;
        }
        return false;
    }

    public List<T> getDelete(){
        return mDeletes;
    }

    public boolean update(T b){
        if(b.updateDirtyType(AbsSyncItem.DIRTY_TYPE.MODIFY)){
            Helper.addListWithoutDuplicate(mModifies,b);
            return true;
        }

        return false;
    }

    public List<T> getUpdates(){
        return mModifies;
    }

    public void clear(){
        for(T b:mAdds){
            b.updateDirtyType(AbsSyncItem.DIRTY_TYPE.CLEAN);
        }
        for(T b:mModifies){
            b.updateDirtyType(AbsSyncItem.DIRTY_TYPE.CLEAN);
        }
        for(T b:mDeletes){
            b.updateDirtyType(AbsSyncItem.DIRTY_TYPE.CLEAN);
        }

        mAdds.clear();
        mModifies.clear();
        mDeletes.clear();
    }

    public Patch<T> copy(){
        Patch<T> patch = new Patch<>();
        patch.mAdds.addAll(mAdds);
        patch.mDeletes.addAll(mDeletes);
        patch.mModifies.addAll(mModifies);

        return patch;
    }


}
