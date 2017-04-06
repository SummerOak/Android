package example.chedifier.chedifier.test.cloudsync;

import android.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Administrator on 2017/4/1.
 */

public class OrderListResolver2 {

    private final String TAG = "OrderListResolver2";

    private OrderList mCOrder;
    private OrderList mLOrder;


    private List<Pair<Integer,OrderList.OrderInfo>> mResult;

    public OrderListResolver2(OrderList cOrder,OrderList lOrder){
        this.mCOrder = cOrder;
        this.mLOrder = lOrder;
    }

    public OrderList resolve(){
        List<OrderList.OrderInfo> sum = new ArrayList<>();
        sum.addAll(mCOrder.orders);

        List<OrderList.OrderInfo> t = new ArrayList<>();
        t.addAll(mLOrder.orders);
        t.removeAll(sum);
        sum.addAll(t);

        mResult = new ArrayList<>(sum.size());

        for(OrderList.OrderInfo oi:sum){
            OrderList.OrderInfo coi = mCOrder.findOrderInfo(oi.luid);
            OrderList.OrderInfo loi = mLOrder.findOrderInfo(oi.luid);

            OrderList.OrderInfo toi = null;
            int order = -1;

            if(coi == null){
                toi = loi;
                order = mLOrder.indexOf(oi.luid);
            }else if(loi == null){
                toi = coi;
                order = mCOrder.indexOf(oi.luid);
            }else if(coi.order_time > loi.order_time){
                toi = coi;
                order = mCOrder.indexOf(oi.luid);
            }else{
                toi = loi;
                order = mLOrder.indexOf(oi.luid);
            }

            toi = new OrderList.OrderInfo(toi);
            Pair<Integer,OrderList.OrderInfo> p = new Pair<>(order,toi);
            mResult.add(p);
        }

        Collections.sort(mResult, new Comparator<Pair<Integer, OrderList.OrderInfo>>() {
            @Override
            public int compare(Pair<Integer, OrderList.OrderInfo> o1, Pair<Integer, OrderList.OrderInfo> o2) {
                if(o1.first != o2.first){
                    return o1.first-o2.first;
                }

                int cid1 = mCOrder.indexOf(o1.second.luid);
                int cid2 = mCOrder.indexOf(o2.second.luid);
                if(cid1 == OrderList.INVALIDATE_ORDER || cid2 == OrderList.INVALIDATE_ORDER){
                    return (int)(o1.second.order_time - o2.second.order_time);
                }

                return cid1 - cid2;
            }
        });

        return buildOrderList();
    }


    private OrderList buildOrderList(){
        OrderList result = new OrderList();
        for(Pair<Integer,OrderList.OrderInfo> p:mResult){
            result.orders.add(p.second);
        }
        return result;
    }
}
