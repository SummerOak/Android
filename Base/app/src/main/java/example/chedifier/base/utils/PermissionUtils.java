package example.chedifier.base.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chedifier on 2016/3/28.
 */
public class PermissionUtils {

    public static final int PERMISSION_REQCODE_1 = 1;

    public static int checkSdcardPermission(Activity activity, String[] permission){
        List<String> permissionNeedReq = new ArrayList<>();
        for(int i=0;i<permission.length;i++){

            boolean hasT = (ContextCompat.checkSelfPermission(activity,permission[i])
                    == PackageManager.PERMISSION_GRANTED);
            if(!hasT){
                permissionNeedReq.add(permission[i]);
            }

        }

        if(permissionNeedReq.size() > 0){

            String[] p = new String[permissionNeedReq.size()];
            permissionNeedReq.toArray(p);

            ActivityCompat.requestPermissions(activity,
                    p,
                    PERMISSION_REQCODE_1);

            return PERMISSION_REQCODE_1;
        }

        return 0;
    }

}
