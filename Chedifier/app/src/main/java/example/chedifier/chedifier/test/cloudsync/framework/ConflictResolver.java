package example.chedifier.chedifier.test.cloudsync.framework;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/1.
 */

public abstract class ConflictResolver<T extends AbsSyncItem> {


    /**
     * cloud add
     * @param cloudAdd
     * @param localSimilar
     * @return cloud if add without
     */
    public abstract T resolveCloudAdd(T cloudAdd, T localSimilar);

    /**
     * cloud delete & local modify
     * @param cloud
     * @param local
     * @return return cloud if use cloud(do delete opt) or local ignore delete in cloud
     */
    public abstract T resolveCloudDeleteConflict(T cloud,T local);

    /**
     * cloud modify & local modify
     * @param cloud
     * @param local
     * @return final data witch may contains both cloud modify and local modify
     */
    public abstract T resolveModifyConflict(T cloud,T local);

    /**
     * cloud modify & local delete
     * @param cloud
     * @param local
     * @return return cloud if ignore local delete or local ignore cloud update
     */
    public abstract T resolveLocalDeleteConflict(T cloud,T local);

}
