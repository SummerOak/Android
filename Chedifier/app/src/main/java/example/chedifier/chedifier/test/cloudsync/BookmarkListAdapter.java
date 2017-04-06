package example.chedifier.chedifier.test.cloudsync;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.woxthebox.draglistview.DragItemAdapter;
import com.woxthebox.draglistview.swipe.ListSwipeItem;

import example.chedifier.chedifier.R;
import example.chedifier.chedifier.base.Color;

/**
 * Created by Administrator on 2017/4/1.
 */

class BookmarkListAdapter extends DragItemAdapter<Bookmark, BookmarkListAdapter.ViewHolder> {

    private BookMarkDataMgr mDataMgr;

    BookmarkListAdapter(BookMarkDataMgr dataMgr) {
        setHasStableIds(true);
        mDataMgr = dataMgr;
        setItemList(mDataMgr.getData());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View content = LayoutInflater.from(parent.getContext()).inflate(R.layout.sync_list_item, parent, false);
        return new BookmarkListAdapter.ViewHolder(content);
    }

    @Override
    public void onBindViewHolder(BookmarkListAdapter.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        String text = mItemList.get(position).name;
        holder.mText.setText(text);

        Color[] colors = Color.values();
        Bookmark data = mItemList.get(position);
        long lci = data.luid;
        int ci = ((int)lci)%colors.length;
        int color = colors[ci].value;
        holder.mContent.setBackgroundColor(color);
        holder.itemView.setTag(data);

        switch (data.getDirty()){
            case ADD:
                holder.mDirty.setBackgroundColor(Color.TEAL.value);
                break;
            case DELETE:
                holder.mDirty.setBackgroundColor(Color.YELLOR.value);
                break;
            case MODIFY:
                holder.mDirty.setBackgroundColor(Color.DARK_BLUE.value);
                break;
            case CLEAN:
                holder.mDirty.setBackgroundColor(Color.DEFAULT.value);
                break;
        }

    }

    @Override
    public long getItemId(int position) {
        return mItemList.get(position).luid;
    }

    class ViewHolder extends DragItemAdapter.ViewHolder {
        TextView mText;
        View mRoot;
        View mContent;
        View mDirty;

        ViewHolder(final View content) {
            super(content, R.id.title, false);
            mRoot = content;
            mText = (TextView)content.findViewById(R.id.title);
            ((ListSwipeItem)content).setSupportedSwipeDirection(ListSwipeItem.SwipeDirection.LEFT_AND_RIGHT);
            mContent = content.findViewById(R.id.item_layout);
            mDirty = content.findViewById(R.id.dirty);
        }

        @Override
        public void onItemClicked(View view) {
            Toast.makeText(view.getContext(), "clicked " + mText.getText(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public boolean onItemLongClicked(View view) {
            Toast.makeText(view.getContext(), "long clicked " + mText.getText(), Toast.LENGTH_SHORT).show();
            return true;
        }
    }
}
