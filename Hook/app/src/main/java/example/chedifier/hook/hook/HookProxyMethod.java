package example.chedifier.hook.hook;

/**
 * Created by chedifier on 2016/5/14.
 */
public class HookProxyMethod {

//    public void toast(Context ctx,String content){
////        Toast.makeText(ctx, content, Toast.LENGTH_SHORT).show();
//    }

    public void setText(CharSequence content){
//        Toast.makeText(HookApplication.getAppContext(), content, Toast.LENGTH_SHORT).show();
    }

    private Object hookProxy(Object... parms){

        return null;
    }

    public static void main(String[] args)
    {
        System.out.println("Hello Word");
    }

}
