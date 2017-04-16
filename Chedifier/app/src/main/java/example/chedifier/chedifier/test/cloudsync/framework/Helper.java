package example.chedifier.chedifier.test.cloudsync.framework;

import java.util.List;

/**
 * Created by Administrator on 2017/4/2.
 */

public class Helper {

    public static boolean addListWithoutDuplicate(List list, Object o){
        if(list.contains(o)){
            return false;
        }

        return list.add(o);
    }

}
