package example.chedifier.chedifier.test.cloudsync.list;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.woxthebox.draglistview.DragListView;
import com.woxthebox.draglistview.swipe.ListSwipeHelper;
import com.woxthebox.draglistview.swipe.ListSwipeItem;

import java.util.ArrayList;

import example.chedifier.chedifier.R;

/**
 * Created by Administrator on 2017/3/29.
 */

public class CloudSyncController implements View.OnClickListener{

    private static final String TAG = "CloudSyncController";

    private Context mContext;

    private LinearLayout mContent;

    private Button mSyncDown;
    private Button mSyncUp;

    private DragListView mCloudListView;
    private ItemAdapter mCloudAdapter;
    private DragListView mLocalListView;
    private ItemAdapter mLocalAdapter;
    private TextView mAddCloud;
    private TextView mAddLocal;

    private DragListView mResultListView;
    private ItemAdapter mResultAdapter;

    private DataMgr mCloudDataMgr = new DataMgr();
    private DataMgr mLocalDataMgr = new DataMgr();
    private DataMgr mResultDataMgr = new DataMgr();

    public CloudSyncController(Context context){
        mContext = context;

        initView();
    }

    private void initView(){
        if(mContent == null){
            mContent = (LinearLayout)View.inflate(mContext, R.layout.cloud_sync_test,null);
            mContent.setOrientation(LinearLayout.VERTICAL);

            mCloudListView = (DragListView)mContent.findViewById(R.id.cloud_list);
            mCloudListView.setCanDragHorizontally(false);
            mCloudListView.setDragListListener(new DragListView.DragListListenerAdapter() {

                @Override
                public void onItemDragEnded(int fromPosition, int toPosition) {
                    Log.i(TAG,"onItemDragEnded " + fromPosition + " to " + toPosition);
                    if (fromPosition != toPosition) {
                        mCloudDataMgr.logout();
                        mCloudDataMgr.swapData(fromPosition,toPosition);
                    }
                }
            });

            mCloudListView.setSwipeListener(new ListSwipeHelper.OnSwipeListenerAdapter() {
                @Override
                public void onItemSwipeEnded(ListSwipeItem item, ListSwipeItem.SwipeDirection swipedDirection) {
                    super.onItemSwipeEnded(item, swipedDirection);
                    if (swipedDirection != ListSwipeItem.SwipeDirection.NONE) {
                        SyncData data = (SyncData) item.getTag();
                        mCloudDataMgr.delete(data);
                        mCloudAdapter.notifyDataSetChanged();
                    }
                }
            });
            mCloudAdapter = new ItemAdapter(mCloudDataMgr);
            mCloudListView.setLayoutManager(new LinearLayoutManager(mContext));
            mCloudListView.setAdapter(mCloudAdapter,true);

            mLocalListView = (DragListView)mContent.findViewById(R.id.local_list);
            mLocalListView.setCanDragHorizontally(false);
            mLocalListView.setDragListListener(new DragListView.DragListListenerAdapter() {

                @Override
                public void onItemDragEnded(int fromPosition, int toPosition) {
                    Log.i(TAG,"onItemDragEnded " + fromPosition + " to " + toPosition);
                    if (fromPosition != toPosition) {
                        mLocalDataMgr.logout();
                        mLocalDataMgr.swapData(fromPosition,toPosition);
                    }
                }
            });
            mLocalListView.setSwipeListener(new ListSwipeHelper.OnSwipeListenerAdapter() {

                @Override
                public void onItemSwipeEnded(ListSwipeItem item, ListSwipeItem.SwipeDirection swipedDirection) {
                    super.onItemSwipeEnded(item, swipedDirection);
                    if (swipedDirection != ListSwipeItem.SwipeDirection.NONE) {
                        SyncData data = (SyncData) item.getTag();
                        mLocalDataMgr.delete(data);
                        mLocalAdapter.notifyDataSetChanged();
                    }
                }
            });
            mLocalAdapter = new ItemAdapter(mLocalDataMgr);
            mLocalListView.setLayoutManager(new LinearLayoutManager(mContext));
            mLocalListView.setAdapter(mLocalAdapter,true);

            mAddCloud = (TextView)mContent.findViewById(R.id.add_cloud_data);
            mAddCloud.setOnClickListener(this);
            mAddLocal = (TextView)mContent.findViewById(R.id.add_local_data);
            mAddLocal.setOnClickListener(this);

            mSyncDown = (Button) mContent.findViewById(R.id.sync_down);
            mSyncDown.setOnClickListener(this);
            mSyncUp = (Button) mContent.findViewById(R.id.sync_up);
            mSyncUp.setOnClickListener(this);

            mResultListView = (DragListView)mContent.findViewById(R.id.cloud_list);
            mResultListView.setCanDragHorizontally(false);
            mResultListView.setDragEnabled(false);
            mResultAdapter = new ItemAdapter(mResultDataMgr);
            mResultListView.setLayoutManager(new LinearLayoutManager(mContext));
            mResultListView.setAdapter(mResultAdapter,true);
        }
    }

    public View getView(){
        return mContent;
    }

    private long id = 0;
    private SyncData productItem(){

        SyncData ret = new SyncData();
        ret.id = ++ id;
        ret.name = String.valueOf(id);
        return ret;
    }

    private ArrayList<SyncData> convert2Arraylist(SyncData head){
        Log.i(TAG,"convert2Arraylist >>> ");
        ArrayList<SyncData> ret = new ArrayList<>();

        while(head != null){
            ret.add(head);
            head = head.next;
        }

        Log.i(TAG,"convert2Arraylist <<< ");
        return ret;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_cloud_data:

                mCloudDataMgr.add(productItem());
                mCloudAdapter.notifyDataSetChanged();
                break;

            case R.id.add_local_data:
                mLocalDataMgr.add(productItem());
                mLocalAdapter.notifyDataSetChanged();
                break;

            case R.id.sync_down:
                SyncData head = null;
                try {
                    head = new OrderSyncResolver(mCloudDataMgr.getHead(),mLocalDataMgr.getHead()).resolve();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ArrayList<SyncData> syncResult = convert2Arraylist(head);
                mResultDataMgr.copyFrom(syncResult);
                mResultAdapter.notifyDataSetChanged();

                break;

            case R.id.sync_up:

                mCloudDataMgr.copyFrom(mResultDataMgr.getData());
                mCloudDataMgr.afterSync();
                mCloudAdapter.notifyDataSetChanged();

                mLocalDataMgr.copyFrom(mResultDataMgr.getData());
                mLocalDataMgr.afterSync();
                mLocalAdapter.notifyDataSetChanged();

                mResultDataMgr.setData(null);
                mResultAdapter.notifyDataSetChanged();

                break;
        }
    }
}
