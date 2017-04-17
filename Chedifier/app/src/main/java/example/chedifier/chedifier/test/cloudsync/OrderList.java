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



    protected boolean add(String signature, int pos){
        if(indexOf(signature) != INVALIDATE_ORDER){
            return false;
        }

        pos = adjustPos(pos);

        long cur = System.currentTimeMillis();
        orders.add(new OrderInfo(signature));
        for(int i=pos+1;i<orders.size();i++){
            orders.get(i).order_time = cur;
        }

        return true;
    }

    protected void delete(String signature){
        int idx = orders.size();
        for(int i=0;i<orders.size();i++){
            if(signature.equals(orders.get(i).signature)){
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

    public int indexOf(String signature){
        for(int i=0;i<orders.size();i++){
            if(signature.equals(orders.get(i).signature)){
                return i;
            }
        }

        return INVALIDATE_ORDER;
    }

    public OrderInfo findOrderInfo(String signature){
        for(int i=0;i<orders.size();i++){
            if(orders.get(i).signature == signature){
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
        public String signature;
        public long order_time;

        protected OrderInfo(String signature){
            this.signature = signature;
            order_time = System.currentTimeMillis();
        }

        protected OrderInfo(OrderInfo o){
            this.signature = o.signature;
            this.order_time = o.order_time;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof OrderInfo){
                return this.signature.equals(((OrderInfo) obj).signature);
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
                j.put("sig",String.valueOf(signature));
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
            s += orders.get(i).signature + "  ";
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
