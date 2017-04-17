package example.chedifier.chedifier.test.cloudsync;

import example.chedifier.chedifier.test.cloudsync.framework.ConflictResolver;

/**
 * Created by Administrator on 2017/4/1.
 */

public class BookmarkConflictResolver extends ConflictResolver<Bookmark> {

    @Override
    public Bookmark resolveCloudAdd(Bookmark cloud, Bookmark localSimilar) {
        if(cloud.item_type == Bookmark.ITEM_TYPE.DIRECTORY
                && localSimilar.item_type == Bookmark.ITEM_TYPE.DIRECTORY){

            cloud.order_info = new OrderInfoResolver(cloud.order_info,localSimilar.order_info).resolve();
        }
        return cloud;
    }

    @Override
    public Bookmark resolveCloudDeleteConflict(Bookmark cloud, Bookmark local) {
        return cloud.last_modify>local.last_modify?cloud:local;
    }

    @Override
    public Bookmark resolveModifyConflict(Bookmark cloud, Bookmark local) {
        Bookmark last = cloud.last_modify>local.last_modify?cloud:local;
        if(cloud.item_type == Bookmark.ITEM_TYPE.DIRECTORY){
            last.order_info = new OrderInfoResolver(cloud.order_info,local.order_info).resolve();
        }

        return last;
    }

    @Override
    public Bookmark resolveLocalDeleteConflict(Bookmark cloud, Bookmark local) {
        return cloud.last_modify>local.last_modify?cloud:local;
    }
}
