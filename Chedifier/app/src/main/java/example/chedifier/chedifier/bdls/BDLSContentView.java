package example.chedifier.chedifier.bdls;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import example.chedifier.base.utils.ScreenUtils;
import example.chedifier.chedifier.R;

/**
 * Created by chedifier on 2016/12/21.
 */
public class BDLSContentView implements View.OnClickListener,AdapterView.OnItemClickListener{

    private Context mContext;

    private LinearLayout mView;

    //logo
    private ImageView mLogoImageView;

    //输入框和百度一下按钮
    private LinearLayout mSearchBar;
    private EditText mInputEditText;
    private Button mEnterButton;

    //底部入口
    private GridView mGridView;
    private ContentAdapter mAdapter;

    //入口数据
    private List<BDLSPortalModel> mBottomPortals = new ArrayList<>();

    private final int VIEW_ID_LOGO = 1;
    private final int VIEW_ID_INPUT_EDIT = 2;
    private final int VIEW_ID_ENTER_BTN = 3;


    private IOnClickEvent mOnClickListener;

    public BDLSContentView(Context context){
        mContext = context;

        initView();
    }

    public View getView(){
        return mView;
    }

    public void updatePortals(List<BDLSPortalModel> portals){
        mBottomPortals.clear();
        if(portals != null){
            mBottomPortals.addAll(portals);
        }

        mAdapter.notifyDataSetChanged();
    }

    public void setOnClickEventListener(IOnClickEvent l){
        mOnClickListener = l;
    }

    public void initView(){

        mView = new LinearLayout(mContext);
        mView.setOrientation(LinearLayout.VERTICAL);

        // logo
        mLogoImageView = new ImageView(mContext);
        mLogoImageView.setImageResource(R.drawable.bdls_logo);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.topMargin = (int)ScreenUtils.dipToPixels(mContext,160);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        mLogoImageView.setId(VIEW_ID_LOGO);
        mLogoImageView.setOnClickListener(this);
        mView.addView(mLogoImageView,lp);

        //search bar
        mSearchBar = new LinearLayout(mContext);
        mSearchBar.setOrientation(LinearLayout.HORIZONTAL);
        mInputEditText = new EditText(mContext);
        mInputEditText.setFocusable(false);
        mInputEditText.setBackgroundResource(R.drawable.bdls_input_edit_bg);
        mInputEditText.setHint("输入要搜索的关键字");
        mInputEditText.setHintTextColor(0xffaaaaaa);
        mInputEditText.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
        mInputEditText.setGravity(Gravity.LEFT|Gravity.BOTTOM);
        mInputEditText.setPadding((int)ScreenUtils.dipToPixels(mContext,16f),
                mInputEditText.getPaddingTop(),
                mInputEditText.getPaddingRight(),
                mInputEditText.getPaddingBottom());

        mInputEditText.setId(VIEW_ID_INPUT_EDIT);
        mInputEditText.setOnClickListener(this);
        mSearchBar.addView(mInputEditText,(int)ScreenUtils.dipToPixels(mContext,240),(int)ScreenUtils.dipToPixels(mContext,42));

        mEnterButton = new Button(mContext);
        mEnterButton.setBackgroundResource(R.drawable.bdls_enter_btn_bg);
        mEnterButton.setText("百度一下");
        mEnterButton.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM);
        mEnterButton.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
        mEnterButton.setTextColor(0xffffffff);
        mEnterButton.setId(VIEW_ID_ENTER_BTN);
        mEnterButton.setOnClickListener(this);
        mSearchBar.addView(mEnterButton,(int)ScreenUtils.dipToPixels(mContext,90),(int)ScreenUtils.dipToPixels(mContext,42));

        lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.topMargin = (int)ScreenUtils.dipToPixels(mContext,30);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        mView.addView(mSearchBar,lp);

        //bottom entries
        mAdapter = new ContentAdapter();
        mGridView = new GridView(mContext);
        mGridView.setNumColumns(5);
        mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        int vSpacing = (int)ScreenUtils.dipToPixels(mContext,5f);
        mGridView.setVerticalSpacing(vSpacing);
        mGridView.setHorizontalSpacing(0);
        mGridView.setStretchMode(GridView.STRETCH_SPACING_UNIFORM);
        mGridView.setGravity(Gravity.CENTER);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(this);

        lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT,1f);
        lp.topMargin = (int)ScreenUtils.dipToPixels(mContext,70);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        mView.addView(mGridView,lp);
    }

    private class ContentAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mBottomPortals.size();
        }

        @Override
        public BDLSPortalModel getItem(int position) {
            return (0<=position&&position<mBottomPortals.size())?mBottomPortals.get(position):null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = createPortalItem();
            }

            setupItem(convertView,getItem(position));

            return convertView;
        }

        private View createPortalItem(){
            TextView view = new TextView(mContext);
            view.setLayoutParams(new ViewGroup.LayoutParams(
                    (int)ScreenUtils.dipToPixels(mContext,66f),
                    (int)ScreenUtils.dipToPixels(mContext,42f)));
            view.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
            view.setTextColor(0xff222222);
            view.setGravity(Gravity.CENTER);
            view.setBackgroundResource(R.drawable.bdls_portal_bg);

            return view;
        }

        private void setupItem(View view,BDLSPortalModel portal){
            if(portal != null && view instanceof TextView){
                ((TextView) view).setText(portal.mName);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case VIEW_ID_ENTER_BTN:{
                if(mOnClickListener != null)
                    mOnClickListener.onClickEvent(IOnClickEvent.EVENT_ENTER_CLICK,null);
                break;
            }
            case VIEW_ID_INPUT_EDIT:{
                if(mOnClickListener != null)
                    mOnClickListener.onClickEvent(IOnClickEvent.EVENT_INPUT_EDIT_CLICK,null);
                break;
            }

            case VIEW_ID_LOGO:{
                if(mOnClickListener != null)
                    mOnClickListener.onClickEvent(IOnClickEvent.EVENT_LOGO_CLICK,null);
                break;
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BDLSPortalModel portalModel = mAdapter.getItem(position);
        if(portalModel != null){
            if(mOnClickListener != null)
                mOnClickListener.onClickEvent(IOnClickEvent.EVENT_PORTAL_CLICK,portalModel);
        }
    }

    public interface IOnClickEvent{
        int EVENT_LOGO_CLICK = 1;
        int EVENT_INPUT_EDIT_CLICK = 2;
        int EVENT_ENTER_CLICK = 3;
        int EVENT_PORTAL_CLICK = 4;

        void onClickEvent(int event,Object data);
    }

}
