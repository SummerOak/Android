//package example.chedifier.chedifier.test.cloudsync.list;
//
//import android.util.Pair;
//
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//
//import example.chedifier.chedifier.test.cloudsync.Bookmark;
//import example.chedifier.chedifier.test.cloudsync.OrderList;
//
///**
// * Created by Administrator on 2017/4/1.
// */
//
//public class OrderListResolver {
//
//    private final String TAG = "OrderListResolver";
//
//    private OrderList mCOrder;
//    private OrderList mLOrder;
//
//    private Map<Long,Pair<Integer,Integer>> mICL = new HashMap<>();
//
//    private Map<Long,OrderList.OrderInfo> mlOrderInfos = new HashMap<>();
//    private Map<Long,OrderList.OrderInfo> mcOrderInfos = new HashMap<>();
//
//    private Map<Long,Integer> mPriority = new HashMap<>();
//    private OrderList.OrderInfo[] mResult;
//
//    public OrderListResolver(OrderList cOrder,OrderList lOrder){
//        this.mCOrder = cOrder;
//        this.mLOrder = lOrder;
//
//        buildMap();
//    }
//
//    private void buildMap(){
//        for(OrderList.OrderInfo oi:mCOrder.orders){
//            mcOrderInfos.put(oi.luid,oi);
//        }
//        for(OrderList.OrderInfo oi:mLOrder.orders){
//            mlOrderInfos.put(oi.luid,oi);
//        }
//    }
//
//    public OrderList resolve(){
//
//        mResult = null;
//        mPriority.clear();
//
//        buildTable(mCOrder.orders,mLOrder.orders,true);
//        buildTable(mLOrder.orders,mCOrder.orders,false);
//
//        Iterator<Map.Entry<Long,Pair<Integer,Integer>>> itr = mICL.entrySet().iterator();
//        while(itr.hasNext()){
//            Map.Entry<Long,Pair<Integer,Integer>> entry = itr.next();
//            mPriority.put(entry.getKey(),entry.getValue().first + entry.getValue().second);
//        }
//
//        reorder();
//
//        return buildOrderList();
//    }
//
//    private void buildTable(List<OrderList.OrderInfo> c1,
//                            List<OrderList.OrderInfo> c2, boolean c2l){
//
//        int i,j;
//        for(i = 0;i<c1.size();i++){
//            OrderList.OrderInfo oii = c1.get(i);
//            for(j = 0;j<c2.size();j++){
//                OrderList.OrderInfo oij = c2.get(j);
//                if(oii.luid == oij.luid){
//                    break;
//                }
//            }
//
//            if(c2l){
//                mICL.put(oii.luid,new Pair<Integer, Integer>(i,j<c2.size()?j:i));
//            }else{
//                mICL.put(oii.luid,new Pair<Integer, Integer>(j<c2.size()?j:i,i));
//            }
//        }
//    }
//
//    private long lastModifyTime(long luid){
//        long t1 = localLastModifyTime(luid);
//        long t2 = cloudLastModifyTime(luid);
//        return t1>t2?t1:t2;
//    }
//
//    private long localLastModifyTime(long luid){
//        OrderList.OrderInfo oil = mlOrderInfos.get(luid);
//        return oil == null?0:oil.order_time;
//    }
//
//    private long cloudLastModifyTime(long luid){
//        OrderList.OrderInfo oic = mcOrderInfos.get(luid);
//        return oic == null?0:oic.order_time;
//    }
//
//    private void reorder(){
//        int size = mPriority.size();
//        mResult = new OrderList.OrderInfo[size];
//        for(int i=0;i<size;i++){
//            long bid = Bookmark.ID_NONE;
//            int min = Integer.MAX_VALUE;
//            Iterator<Map.Entry<Long,Integer>> itr = mPriority.entrySet().iterator();
//            while(itr.hasNext()){
//                Map.Entry<Long,Integer> entry = itr.next();
//                long bidt = entry.getKey();
//                int pri = entry.getValue();
//                if(pri < min){
//                    min = pri; bid = bidt;
//                }else if(pri == min){
//                    long ltc = cloudLastModifyTime(bid);
//                    long ltc1 = cloudLastModifyTime(bidt);
//                    long ltl = localLastModifyTime(bid);
//                    long ltl1 = localLastModifyTime(bidt);
//
//                    if((ltc>=ltl || ltc1 >= ltl1)){
//                        if(mCOrder.indexOf(bid) > mCOrder.indexOf(bidt)){
//                            bid = bidt;
//                        }
//                    }else if(mLOrder.indexOf(bid) > mLOrder.indexOf(bidt)){
//                        bid = bidt;
//                    }
//                }
//            }
//
//            mPriority.remove(bid);
//
//            OrderList.OrderInfo oi = new OrderList.OrderInfo(bid);
//            oi.order_time = lastModifyTime(bid);
//            mResult[i] = oi;
//        }
//    }
//
//    private OrderList buildOrderList(){
//        OrderList result = new OrderList();
//        for(int i=0;i<mResult.length;i++){
//            result.orders.add(mResult[i]);
//        }
//
//        return result;
//    }
//}
