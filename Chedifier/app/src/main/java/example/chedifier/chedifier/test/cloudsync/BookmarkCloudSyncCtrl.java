package example.chedifier.chedifier.test.cloudsync;

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
import example.chedifier.chedifier.test.cloudsync.framework.AbsSyncItem;
import example.chedifier.chedifier.test.cloudsync.framework.SyncResolver;
import example.chedifier.chedifier.test.cloudsync.list.SyncData;

/**
 * Created by Administrator on 2017/3/29.
 */

public class BookmarkCloudSyncCtrl implements View.OnClickListener,SyncResolver.ISyncCallBack<Bookmark>{

    private static final String TAG = "BookmarkCloudSyncCtrl";

    private Context mContext;

    private LinearLayout mContent;

    private Button mSyncDown;
    private Button mSyncUp;

    private DragListView mCloudListView;
    private BookmarkListAdapter mCloudAdapter;
    private DragListView mLocalListView;
    private BookmarkListAdapter mLocalAdapter;
    private TextView mAddCloud;
    private TextView mAddLocal;

    private DragListView mLocalOptListView;
    private BookmarkListAdapter mLocalOptListAdapter;
    private DragListView mCloudOptListView;
    private BookmarkListAdapter mCloudOptListAdapter;

    private BookMarkDataMgr mCloudDataMgr = new BookMarkDataMgr();
    private BookMarkDataMgr mLocalDataMgr = new BookMarkDataMgr();
    private BookMarkDataMgr mCloudOptDataMgr = new BookMarkDataMgr();
    private BookMarkDataMgr mLocalOptDataMgr = new BookMarkDataMgr();

    public BookmarkCloudSyncCtrl(Context context){
        mContext = context;

        Log.i(TAG,dumpJson());

        initView();
        prepareRoot();
    }

    private void initView(){
        if(mContent == null){
            mContent = (LinearLayout)View.inflate(mContext, R.layout.cloud_sync_test,null);
            mContent.setOrientation(LinearLayout.VERTICAL);

            mCloudListView = (DragListView)mContent.findViewById(R.id.cloud_list);
            mCloudListView.setCanDragHorizontally(false);
            mCloudListView.setCanNotDragAboveTopItem(true);
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
                        Bookmark data = (Bookmark) item.getTag();
                        mCloudDataMgr.delete(data);
                        mCloudAdapter.notifyDataSetChanged();
                    }
                }
            });
            mCloudAdapter = new BookmarkListAdapter(mCloudDataMgr);
            mCloudListView.setLayoutManager(new LinearLayoutManager(mContext));
            mCloudListView.setAdapter(mCloudAdapter,true);

            mLocalListView = (DragListView)mContent.findViewById(R.id.local_list);
            mLocalListView.setCanDragHorizontally(false);
            mLocalListView.setCanNotDragAboveTopItem(true);
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
                        Bookmark data = (Bookmark) item.getTag();
                        mLocalDataMgr.delete(data);
                        mLocalAdapter.notifyDataSetChanged();
                    }
                }
            });
            mLocalAdapter = new BookmarkListAdapter(mLocalDataMgr);
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

            mLocalOptListView = (DragListView)mContent.findViewById(R.id.local_opt_list);
            mLocalOptListView.setCanDragHorizontally(false);
            mLocalOptListView.setCanNotDragAboveTopItem(true);
            mLocalOptListView.setDragEnabled(false);
            mLocalOptListAdapter = new BookmarkListAdapter(mLocalOptDataMgr);
            mLocalOptListView.setLayoutManager(new LinearLayoutManager(mContext));
            mLocalOptListView.setAdapter(mLocalOptListAdapter,true);

            mCloudOptListView = (DragListView)mContent.findViewById(R.id.cloud_opt_list);
            mCloudOptListView.setCanDragHorizontally(false);
            mCloudOptListView.setCanNotDragAboveTopItem(true);
            mCloudOptListView.setDragEnabled(false);
            mCloudOptListAdapter = new BookmarkListAdapter(mCloudOptDataMgr);
            mCloudOptListView.setLayoutManager(new LinearLayoutManager(mContext));
            mCloudOptListView.setAdapter(mCloudOptListAdapter,true);
        }
    }

    public View getView(){
        return mContent;
    }

    private long id = 0;
    private Bookmark productItem(){

        Bookmark ret = new Bookmark();
        ret.luid = ++ id;
        ret.p_luid = Bookmark.ID_ROOT;
        ret.name = String.valueOf(id);
        return ret;
    }

    private void prepareRoot(){
        Bookmark root = new Bookmark(Bookmark.ITEM_TYPE.DIRECTORY);
        root.luid = Bookmark.ID_ROOT;
        root.name = String.valueOf(root.luid);

        mCloudDataMgr.add(root);
        mLocalDataMgr.add(root.syncClone());
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
                down();
                break;

            case R.id.sync_up:
                down();
                up();
                break;
        }
    }

    private void down(){
        mCloudOptDataMgr.copyFrom(mCloudDataMgr,true);
        mLocalOptDataMgr.copyFrom(mLocalDataMgr,true);
        try {
            new SyncResolver(mCloudDataMgr.getPatch(),mLocalDataMgr.getPatch())
                    .resolve(new BookmarkConflictResolver(), this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mCloudOptDataMgr.cleanupDirectoryOrderList();
        mCloudOptDataMgr.reorder();
        mLocalOptDataMgr.cleanupDirectoryOrderList();
        mLocalOptDataMgr.reorder();

        mCloudOptListAdapter.notifyDataSetChanged();
        mLocalOptListAdapter.notifyDataSetChanged();
    }

    private void up(){
        mCloudDataMgr.copyFrom(mCloudOptDataMgr,false);
        mCloudDataMgr.resetDirty();
        mCloudAdapter.notifyDataSetChanged();

        mLocalDataMgr.copyFrom(mLocalOptDataMgr,false);
        mLocalDataMgr.resetDirty();
        mLocalAdapter.notifyDataSetChanged();

        mCloudOptDataMgr.clear();
        mCloudOptListAdapter.notifyDataSetChanged();
        mLocalOptDataMgr.clear();
        mLocalOptListAdapter.notifyDataSetChanged();
    }


    @Override
    public boolean addCloud(Bookmark data) {
        return mCloudOptDataMgr.add(data);
    }

    @Override
    public boolean deleteCloud(Bookmark data) {
        Log.i(TAG,"deleteCloud >>> " + data.luid);
        mCloudOptDataMgr.getBookmarkByLuid(Bookmark.ID_ROOT).order_info.logout();

        Bookmark b = mCloudOptDataMgr.getBookmarkByLuid(data.luid);
        if(b != null){
            if(mCloudOptDataMgr.getPatch().delete(b)){

                Bookmark dir = mCloudOptDataMgr.getBookmarkByLuid(b.p_luid);
                if(dir != null && dir.item_type == Bookmark.ITEM_TYPE.DIRECTORY){
                    dir.order_info.delete(b.luid);
                }
            }
        }
        mCloudOptDataMgr.getBookmarkByLuid(Bookmark.ID_ROOT).order_info.logout();
        Log.i(TAG,"deleteCloud <<< " + data.luid);
        return true;
    }

    @Override
    public boolean updateCloud(Bookmark data) {
        Bookmark b = mCloudOptDataMgr.getBookmarkByLuid(data.luid);
        if(b != null && mCloudOptDataMgr.getPatch().update(b)){
            b.updateTo(data);
        }
        return true;
    }

    @Override
    public boolean addLocal(Bookmark data) {
        mLocalOptDataMgr.add(data);
        return true;
    }

    @Override
    public boolean deleteLocal(Bookmark data) {
        Log.i(TAG,"deleteLocal >>> " + data.luid);
        mLocalOptDataMgr.getBookmarkByLuid(Bookmark.ID_ROOT).order_info.logout();

        Bookmark l = mLocalOptDataMgr.getBookmarkByLuid(data.luid);
        if(l != null){
            if(mLocalOptDataMgr.getPatch().delete(l)){

                Bookmark dir = mLocalOptDataMgr.getBookmarkByLuid(l.p_luid);
                if(dir != null && dir.item_type == Bookmark.ITEM_TYPE.DIRECTORY){
                    dir.order_info.delete(l.luid);
                }

            }
        }

        mLocalOptDataMgr.getBookmarkByLuid(Bookmark.ID_ROOT).order_info.logout();
        Log.i(TAG,"deleteLocal <<< " + data.luid);

        return true;
    }

    @Override
    public boolean updateLocal(Bookmark data) {
        Bookmark b = mLocalOptDataMgr.getBookmarkByLuid(data.luid);
        if(b != null && mLocalOptDataMgr.getPatch().update(b)) {
            b.updateTo(data);
        }
        return true;
    }

    private String dumpJson(){

        BookMarkDataMgr bmmg = new BookMarkDataMgr();

        Bookmark root = new Bookmark(Bookmark.ITEM_TYPE.DIRECTORY);
        root.luid = 1;
        root.guid = 10001;
        root.p_luid = -1;
        root.p_guid = -1;
        root.name = "root";
        root.url = "";
        root.create_time = System.currentTimeMillis();
        root.last_modify = root.create_time;
        root.account = "TestAccount";
        bmmg.add(root);

        Bookmark i1 = new Bookmark();
        i1.luid = 2;
        i1.guid = 10002;
        i1.p_luid = root.luid;
        i1.p_guid = root.guid;
        i1.name = "baidu";
        i1.url = "www.baidu.com";
        i1.create_time = System.currentTimeMillis();
        i1.last_modify = root.create_time;
        i1.account = "TestAccount";
        bmmg.add(i1);

        i1 = new Bookmark();
        i1.luid = 3;
        i1.guid = 10003;
        i1.p_luid = root.luid;
        i1.p_guid = root.guid;
        i1.name = "google";
        i1.url = "www.google.com.hk";
        i1.create_time = System.currentTimeMillis();
        i1.last_modify = root.create_time;
        i1.account = "TestAccount";
        bmmg.add(i1);

        return bmmg.dumpJson();
    }
}
