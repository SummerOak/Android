package example.chedifier.chedifier.test.cloudsync;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/31.
 */

public class OrderList {

    private final String TAG = "OrderList";

    public static final int INVALIDATE_ORDER = -1;

    public List<OrderInfo> orders = new ArrayList<>();

    protected OrderList(){

    }

    private OrderList(OrderList d){
        for(OrderInfo o:d.orders){
            orders.add(o.clone());
        }
    }



    protected boolean add(long luid, int pos){
        if(indexOf(luid) != INVALIDATE_ORDER){
            return false;
        }

        pos = adjustPos(pos);

        long cur = System.currentTimeMillis();
        orders.add(new OrderInfo(luid));
        for(int i=pos+1;i<orders.size();i++){
            orders.get(i).order_time = cur;
        }

        return true;
    }

    protected void delete(long luid){
        int idx = orders.size();
        for(int i=0;i<orders.size();i++){
            if(orders.get(i).luid == luid){
                idx = i;
            }

            if(i > idx){
                orders.set(i-1,orders.get(i));
            }
        }

        if(idx != orders.size()){
            orders.remove(orders.size()-1);
        }
    }

    protected void swap(int p1,int p2){
        if(0<=p1&&p1<orders.size() && 0<=p2&&p2<orders.size()){
            long cur = System.currentTimeMillis();
            OrderInfo o1 = orders.get(p1);
            for(int i=p1;i != p2;){
                int j = p1<p2?i+1:i-1;
                OrderInfo next = orders.get(j);
                orders.set(i,next);
                next.order_time = cur;
                i = j;
            }
            orders.set(p2,o1);
            o1.order_time = cur;

            logout();
        }
    }

    public int indexOf(long luid){
        for(int i=0;i<orders.size();i++){
            if(orders.get(i).luid == luid){
                return i;
            }
        }

        return INVALIDATE_ORDER;
    }

    public OrderInfo findOrderInfo(long luid){
        for(int i=0;i<orders.size();i++){
            if(orders.get(i).luid == luid){
                return orders.get(i);
            }
        }

        return null;
    }

    private int adjustPos(int pos){
        if(pos < 0){
            return 0;
        }

        if(pos > orders.size()){
            return orders.size();
        }

        return pos;
    }

    public static class OrderInfo{
        public long guid;
        public long luid;
        public long order_time;

        protected OrderInfo(long luid){
            this.luid = luid;
            order_time = System.currentTimeMillis();
        }

        protected OrderInfo(OrderInfo o){
            this.guid = o.guid;
            this.luid = o.luid;
            this.order_time = o.order_time;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof OrderInfo){
                return ((OrderInfo) obj).luid == this.luid;
            }

            return false;
        }

        @Override
        protected OrderInfo clone() {
            return new OrderInfo(this);
        }

        public JSONObject toJson(){
            JSONObject j = new JSONObject();
            try {
                j.put("guid",String.valueOf(guid));
                j.put("luid",String.valueOf(luid));
                j.put("order_time",String.valueOf(order_time));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return j;
        }
    }

    @Override
    protected OrderList clone() {
        return new OrderList(this);
    }


    public void logout(){
        String s = "";
        for(int i=0;i<orders.size();i++){
            s += orders.get(i).luid + "  ";
        }

        Log.i(TAG,s);
    }

    public JSONArray toJson(){
        JSONArray j = new JSONArray();
        for(int i=0;i<orders.size();i++){
            j.put(orders.get(i).toJson());
        }

        String s = "我是abc";

        long l = 9223372036854775807L;

        Log.i(TAG,"size of order list: " + j.toString().length() + "  " + s.getBytes().length + " " + l);

        return j;
    }
}
