package example.chedifier.chedifier.test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chedifier on 2016/12/12.
 */
public class OOMTest {

    private static List<Long> sData = new ArrayList<Long>();

    public static void startThread(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                while(true){
                    sData.add(new Long(System.currentTimeMillis()));
                }

            }
        }).start();
    }

}
