package example.chedifier.chedifier.bdls;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by chedifier on 2016/12/21.
 */
public class BDLSActionHandler implements BDLSContentView.IOnClickEvent{

    private Context mContext;

    public BDLSActionHandler(Context context){
        mContext = context;
    }

    @Override
    public void onClickEvent(int event, Object data) {
        switch (event){
            case EVENT_ENTER_CLICK:
            case EVENT_INPUT_EDIT_CLICK:{
                openSearchView();
                break;
            }

            case EVENT_PORTAL_CLICK:{
                if(data instanceof BDLSPortalModel){
                    stepInPortal((BDLSPortalModel)data);
                }
                break;
            }
        }
    }

    private void openSearchView(){
        Intent intent = new Intent("com.uc.search.action.INPUT");
        intent.setPackage("com.UCMobile");

        mContext.startActivity(intent);
        ((Activity)mContext).finish();
    }

    private void stepInPortal(BDLSPortalModel portal){

        boolean toUC = true;

        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setPackage(toUC?"com.UCMobile":"com.android.browser");
        intent.setData(Uri.parse(portal.mUrl));
        intent.putExtra("source", "bdls");
        intent.putExtra("policy", "UCM_NO_NEED_BACK|UCM_CURRENT_WINDOW");

        mContext.startActivity(intent);
        ((Activity)mContext).finish();
    }
}
