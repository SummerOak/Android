package example.chedifier.chedifier.base;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

public class ExtHandler extends Handler {
    private String mName;

    private WeakReference<IMessageHandler> mWRefMessageHandler;

    public ExtHandler(String name) {
        setName(name);
    }

    public ExtHandler(String name,IMessageHandler messageHandler){
        this(name);
        mWRefMessageHandler = new WeakReference<IMessageHandler>(messageHandler);
    }

    public ExtHandler(String name, Callback callback) {
        super(callback);
        setName(name);
    }

    public ExtHandler(String name,Callback callback,IMessageHandler messageHandler){
        this(name,callback);
        mWRefMessageHandler = new WeakReference<IMessageHandler>(messageHandler);
    }

    public ExtHandler(String name, Looper looper) {
        super(looper);
        setName(name);
    }

    public ExtHandler(String name,Looper looper,IMessageHandler messageHandler){
        this(name,looper);
        mWRefMessageHandler = new WeakReference<IMessageHandler>(messageHandler);
    }

    public ExtHandler(String name, Looper looper, Callback callback) {
        super(looper, callback);
        setName(name);
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getName() {
        return mName;
    }

    @Override
    public void handleMessage(Message msg) {
        if(mWRefMessageHandler != null && mWRefMessageHandler.get() != null){
            mWRefMessageHandler.get().handleMessage(msg);
        }
    }

    @Override
    public String toString() {
        return "HandlerEx (" + mName + ") {}";
    }


    public interface IMessageHandler{
        void handleMessage(Message msg);
    }
}

