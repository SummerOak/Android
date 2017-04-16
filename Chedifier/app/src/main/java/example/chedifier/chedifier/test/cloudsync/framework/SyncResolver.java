package example.chedifier.chedifier.test.cloudsync.framework;

import java.util.List;

/**
 * Created by Administrator on 2017/4/1.
 */

public class SyncResolver<T extends AbsSyncItem> {

    private List<T> cAdds;
    private List<T> cModifies;
    private List<T> cDeletes;
    private List<T> lAdds;
    private List<T> lModifies;
    private List<T> lDeletes;

    public SyncResolver(Patch<T> cPatch, Patch lPatch){

        this.cAdds = cPatch.getAdd();
        this.cModifies = cPatch.getUpdates();
        this.cDeletes = cPatch.getDelete();

        this.lAdds = lPatch.getAdd();
        this.lModifies = lPatch.getUpdates();
        this.lDeletes = lPatch.getDelete();

    }

    public void resolve(ConflictResolver<T> resolver,ISyncCallBack<T> callBack){
        resolveCloud(resolver,callBack);
        resolveLocal(callBack);
    }

    private void resolveCloud(ConflictResolver<T> resolver,ISyncCallBack<T> callBack){
        resolveCloudAdd(resolver,callBack);
        resolveCloudModify(resolver,callBack);
        resolveCloudDelete(resolver,callBack);
    }

    private void resolveCloudAdd(ConflictResolver<T> resolver,ISyncCallBack<T> callBack){
        for(T ca:cAdds){

            for(T la:lAdds){
                if(la.similarTo(ca)){
                    T last = resolver.resolveCloudAdd(ca,la);
                    lAdds.remove(last);
                    callBack.updateLocal((T) last.syncClone());
                    callBack.updateCloud((T) last.syncClone());
                    break;
                }
            }

            callBack.addLocal((T)ca.syncClone());
        }
    }

    private void resolveCloudModify(ConflictResolver<T> resolver,ISyncCallBack<T> callBack){
        for(int i=0;i<cModifies.size();i++){
            T cModify = cModifies.get(i);
            int idx = lModifies.indexOf(cModify);
            if(idx != -1){
                T lModify = lModifies.get(idx);
                T last = resolver.resolveModifyConflict(cModify,lModify);
                callBack.updateLocal((T)last.syncClone());
                callBack.updateCloud((T)last.syncClone());

                lModifies.remove(lModify);
                continue;
            }

            idx = lDeletes.indexOf(cModify);
            if(idx != -1){
                T lDelete = lDeletes.get(idx);
                T last = resolver.resolveLocalDeleteConflict(cModify,lDelete);
                if(last == cModify){
                    callBack.addLocal((T)last.syncClone());
                    lDeletes.remove(lDelete);
                }
                continue;
            }

            callBack.updateLocal(cModify);
        }
    }

    private void resolveCloudDelete(ConflictResolver<T> resolver,ISyncCallBack<T> callBack){
        for(int i=0;i<cDeletes.size();i++){
            T cDelete = cDeletes.get(i);
            int idx = lModifies.indexOf(cDelete);
            if(idx != -1){
                T lModify = lModifies.get(idx);
                T last = resolver.resolveCloudDeleteConflict(cDelete,lModify);
                if(last == cDelete){
                    callBack.deleteLocal(last);
                }else if(last == lModify){
                    callBack.addCloud((T)last.syncClone());// TODO what will sdk deal this?
                }
                lModifies.remove(last);
                continue;
            }

            callBack.deleteLocal(cDelete);
        }
    }

    private void resolveLocal(ISyncCallBack<T> callBack){
        for(int i=0;i<lAdds.size();i++){
            callBack.addCloud((T)lAdds.get(i).syncClone());
        }

        for(int i=0;i<lModifies.size();i++){
            callBack.updateCloud((T)lModifies.get(i).syncClone());
        }

        for(int i=0;i<lDeletes.size();i++){
            callBack.deleteCloud((T)lDeletes.get(i).syncClone());
        }
    }

    public interface ISyncCallBack<T extends AbsSyncItem>{
        boolean addCloud(T data);
        boolean deleteCloud(T data);
        boolean updateCloud(T data);
        boolean addLocal(T data);
        boolean deleteLocal(T data);
        boolean updateLocal(T data);
    }

}
