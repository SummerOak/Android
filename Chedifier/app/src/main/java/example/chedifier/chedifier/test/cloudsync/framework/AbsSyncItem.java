package example.chedifier.chedifier.test.cloudsync.framework;

import example.chedifier.chedifier.test.cloudsync.Bookmark;

/**
 * Created by Administrator on 2017/4/6.
 */

public abstract class AbsSyncItem{

    public enum DIRTY_TYPE{
        CLEAN,
        ADD,
        MODIFY,
        DELETE
    }

    private DIRTY_TYPE dirty = DIRTY_TYPE.CLEAN;

    public boolean updateDirtyType(DIRTY_TYPE type){
        switch (type){
            case ADD:
                dirty = type;
                break;
            case DELETE:
                dirty = type;
                break;
            case MODIFY:
                if(dirty == DIRTY_TYPE.CLEAN || dirty == DIRTY_TYPE.MODIFY){
                    dirty = type;
                    break;
                }
                return false;
            case CLEAN:
                dirty = type;
                break;
        }

        return true;
    }

    public boolean similarTo(AbsSyncItem item){
        return false;
    }

    public DIRTY_TYPE getDirty(){
        return dirty;
    }

    public abstract AbsSyncItem syncClone();

}
