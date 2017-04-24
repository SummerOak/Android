package example.chedifier.chedifier.test.cloudsync;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import example.chedifier.chedifier.test.cloudsync.framework.AbsSyncItem;

/**
 * Created by Administrator on 2017/3/31.
 */

public class Bookmark extends AbsSyncItem {

    enum ITEM_TYPE{
        NORMAL,
        DIRECTORY,
    }



    public static final String ID_NONE = "";
    public static final String ID_ROOT = "root";

    public String luid;
    public String guid = ID_NONE;
    public String p_guid = ID_NONE;
    public String p_luid;
    public String name;
    public String url;
    public String signature;
    public ITEM_TYPE item_type = ITEM_TYPE.NORMAL;
    public String next;
    public long next_order_time;

    public long create_time = System.currentTimeMillis();
    public long last_modify = System.currentTimeMillis();
    public String account;

    public Bookmark(){
        this(ITEM_TYPE.NORMAL);
    }

    public Bookmark(ITEM_TYPE type){
        this.item_type = type;
    }

    private Bookmark(Bookmark b){
        this.luid = b.luid;
        this.guid = b.guid;
        this.name = b.name;
        this.p_luid = b.p_luid;
        this.p_guid = b.p_guid;
        this.item_type = b.item_type;
        this.create_time = b.create_time;
        this.last_modify = b.last_modify;
        this.next = b.next;
        this.next_order_time = b.next_order_time;
    }

    public String initLuid(){
        if(luid == null){
            String hashString = (name==null?"":name) + (url==null?"":url) + create_time;

            luid = sha1(hashString.getBytes());
        }

        return luid;
    }

    public static String sha1(byte[] convertme) {
        long t = System.currentTimeMillis();
        String sha1 = null;
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
            sha1 = byteArrayToHexString(md.digest(convertme));
        }catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        Log.i("ccc","sha1 cost: " + (System.currentTimeMillis() - t));
        return sha1;
    }

    public static String byteArrayToHexString(byte[] b) {
        String result = "";
        for (int i=0; i < b.length; i++) {
            result += Integer.toString(( b[i] & 0xff ) + 0x100, 16).substring(1);
        }
        return result;
    }

    public void updateTo(Bookmark b){
        this.next = b.next;
        this.next_order_time = b.next_order_time;
        this.p_luid = b.p_luid;
        this.p_guid = b.p_guid;
        this.last_modify = System.currentTimeMillis();
    }

    @Override
    public boolean similarTo(AbsSyncItem item){
        if(item instanceof Bookmark){

        }

        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Bookmark){
            if(((Bookmark) obj).guid != ID_NONE && this.guid != ID_NONE){
                return ((Bookmark) obj).guid == this.guid;
            }

            if(((Bookmark) obj).guid == ID_NONE && this.guid == ID_NONE){
                return ((Bookmark) obj).luid == this.luid;
            }

            return this.name != null && name.equals(((Bookmark) obj).name);
        }

        return false;
    }

    @Override
    public int hashCode() {
        if(guid != ID_NONE){
            return Long.valueOf(guid).hashCode();
        }

        return Long.valueOf(luid).hashCode();
    }

    @Override
    public Bookmark syncClone() {
        return new Bookmark(this);
    }


    public String toJson(){
        JSONObject j = new JSONObject();
        try {
            j.put("luid",String.valueOf(luid));
            j.put("guid",String.valueOf(guid));
            j.put("name",String.valueOf(name));
            j.put("url",String.valueOf(url));
            j.put("is_directory",item_type == ITEM_TYPE.DIRECTORY?"1":"0");
            j.put("parent_guid",p_guid);
            j.put("parent_luid",p_luid);
            j.put("next",String.valueOf(next));
            j.put("next_order_time",String.valueOf(next_order_time));
            j.put("create_time",String.valueOf(create_time));
            j.put("last_modify",String.valueOf(last_modify));
            j.put("account",account);
            j.put("device_type","phone");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return j.toString();
    }
}
