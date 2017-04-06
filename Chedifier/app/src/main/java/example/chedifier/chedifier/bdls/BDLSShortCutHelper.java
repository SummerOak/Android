package example.chedifier.chedifier.bdls;

import android.content.Intent;

/**
 * Created by chedifier on 2016/12/21.
 */
public class BDLSShortCutHelper {

    public static final String TITLE = "百度";

    public static final String ACTION = "fkldjslnknvkljdiosuioejklmdkfjkdlsj";

    public static Intent generateShortCutIntent(){

        Intent intent = new Intent();

        intent.setAction(ACTION);

        return intent;
    }

}
