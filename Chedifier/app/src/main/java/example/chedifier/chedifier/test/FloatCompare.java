package example.chedifier.chedifier.test;

import android.util.Log;

/**
 * Created by Administrator on 2017/4/10.
 */

public class FloatCompare {

    float m = 61f;

    public void test(){
        compare(m);
    }

    public void compare(float t) {
        if(m == t){

        }else{
            Log.i("FloatCompare","!!!!!! m != t");
        }
        m = t;
    }
}
