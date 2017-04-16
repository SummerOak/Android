package example.chedifier.chedifier.bdls;

import android.app.Activity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chedifier on 2016/12/21.
 */
public class BDLSActivity extends Activity {

    protected final String TAG = getClass().getSimpleName();

    private BDLSContentView mContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContentView = new BDLSContentView(this);
        setContentView(mContentView.getView());

        mContentView.setOnClickEventListener(new BDLSActionHandler(this));
        mContentView.updatePortals(getBottomPortals());
    }

    private List<BDLSPortalModel> getBottomPortals(){
        List<BDLSPortalModel> portals = new ArrayList<>();

        portals.add(BDLSPortalConfiguration.queryPortal(BDLSPortalConfiguration.PORTALS.POST_BAR));
        portals.add(BDLSPortalConfiguration.queryPortal(BDLSPortalConfiguration.PORTALS.NEWS    ));
        portals.add(BDLSPortalConfiguration.queryPortal(BDLSPortalConfiguration.PORTALS.PICTURES));
        portals.add(BDLSPortalConfiguration.queryPortal(BDLSPortalConfiguration.PORTALS.ANSWERS ));
        portals.add(BDLSPortalConfiguration.queryPortal(BDLSPortalConfiguration.PORTALS.BAIKE   ));
        portals.add(BDLSPortalConfiguration.queryPortal(BDLSPortalConfiguration.PORTALS.LIBRARY ));
        portals.add(BDLSPortalConfiguration.queryPortal(BDLSPortalConfiguration.PORTALS.HAO123  ));
        portals.add(BDLSPortalConfiguration.queryPortal(BDLSPortalConfiguration.PORTALS.VIDEOS  ));
        portals.add(BDLSPortalConfiguration.queryPortal(BDLSPortalConfiguration.PORTALS.BOARD   ));

        return portals;
    }

}
