package com.example.chedifier.previewofandroidn.multiwindows;

import android.content.Intent;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.chedifier.previewofandroidn.BaseActivity;
import com.example.chedifier.previewofandroidn.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chedifier on 2016/3/14.
 */
public class MultiWindowActivityMain extends BaseActivity implements View.OnClickListener{


    private ListView mListView;
    private LinearLayout mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.multiwindow_main);

        mListView = (ListView)findViewById(R.id.multi_window_listview);

//        mContent = (LinearLayout)findViewById(R.id.content);

        List<Task> tasks = new ArrayList<>(50);
        tasks.add(new Task("在隔壁window启动一个activity",Task.ACTION_LAUNCH_ACTIVITY_ADJACENT));
        tasks.add(new Task("在当前window启动一个activity",Task.ACTION_LAUNCH_ACTIVITY_CURRENT));
        tasks.add(new Task("启动一个非多窗口Activity",Task.ACTION_LAUNCH_ACTIVITY_SINGLE_W));
        tasks.add(new Task("测试Drag&Drop",Task.ACTION_TEST_DRAG_DROP));
        tasks.add(new Task("启动DropActivity",Task.ACTION_LAUNCH_DROP_ACT));
        for(int i=0;i<10;i++){
            tasks.add(new Task());
        }

//        showTasks(tasks);

        mListView.setAdapter(new MyAdapter(tasks));
    }


    private void showTasks(List<Task> tasks){
        for(int i=0;i<tasks.size();i++){

            TextView item = new TextView(MultiWindowActivityMain.this);
            item.setText(tasks.get(i).name);
            item.setTag(tasks.get(i).action);
            item.setOnClickListener(this);
            item.setPadding(20,20,20,20);

            mContent.addView(item);
        }
    }

    @Override
    public void onClick(View view) {
        int act = (Integer)view.getTag();
        switch (act){
            case Task.ACTION_LAUNCH_ACTIVITY_ADJACENT:
                launchActivityAdjacent();

                break;

            case Task.ACTION_LAUNCH_ACTIVITY_CURRENT:
                launchActivityCurrent();

                break;

            case Task.ACTION_LAUNCH_ACTIVITY_SINGLE_W:
                launchSingleWindow();

                break;

            case Task.ACTION_TEST_DRAG_DROP:

                startActivity(new Intent(this,DragAndDropTestActivity.class));

                break;

            case Task.ACTION_LAUNCH_DROP_ACT:

                launchDropActivity();
                break;

            default:

                break;
        }
    }

    private void launchActivityAdjacent(){
        Intent intent = new Intent(this, MultiWindowSampleActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MultiWindowActivityMain.this.startActivity(intent);
    }

    private void launchActivityCurrent(){
        startActivity(new Intent(this,MultiWindowSampleActivity.class));
    }

    private void launchSingleWindow(){
        Intent intent = new Intent(this, SingleWindowActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MultiWindowActivityMain.this.startActivity(intent);
    }

    private void launchDropActivity(){
        Intent intent = new Intent(this, DragAndDropTestActivityInAnotherProc.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MultiWindowActivityMain.this.startActivity(intent);
    }


    private class MyAdapter extends BaseAdapter implements View.OnClickListener{

        private List<Task> mTasks;

        public MyAdapter(List<Task> tasks){
            mTasks = tasks;
        }

        @Override
        public int getCount() {
            return mTasks==null?0:mTasks.size();
        }

        @Override
        public Task getItem(int i) {
            return (mTasks!=null&&mTasks.size()>i)?mTasks.get(i):null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if(view == null){
                view = View.inflate(MultiWindowActivityMain.this,R.layout.list_item,null);
                view.setTag(R.id.content,view.findViewById(R.id.content));
            }

            ((TextView)(view.getTag(R.id.content))).setText(getItem(i).name);
            view.setTag(getItem(i).action);
            view.setOnClickListener(this);

            return view;
        }

        @Override
        public void onClick(View view) {
            MultiWindowActivityMain.this.onClick(view);
        }
    }

    private static class Task{

        public static final int ACTION_LAUNCH_ACTIVITY_ADJACENT = 1;
        public static final int ACTION_LAUNCH_ACTIVITY_CURRENT = 2;
        public static final int ACTION_LAUNCH_ACTIVITY_SINGLE_W = 3;
        public static final int ACTION_TEST_DRAG_DROP = 4;
        public static final int ACTION_LAUNCH_DROP_ACT = 5;

        public Task(String name,int action){
            this.name = name;
            this.action = action;
        }

        public Task(){
            name = "stub";
            action = 0;
        }

        public String name;
        public int action;
    }

}
