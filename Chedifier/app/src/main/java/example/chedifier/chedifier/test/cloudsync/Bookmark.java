package example.chedifier.chedifier.test.cloudsync;

import org.json.JSONException;
import org.json.JSONObject;

import example.chedifier.base.utils.Md5Utils;
import example.chedifier.chedifier.test.cloudsync.framework.AbsSyncItem;

/**
 * Created by Administrator on 2017/3/31.
 */

public class Bookmark extends AbsSyncItem {

    enum ITEM_TYPE{
        NORMAL,
        DIRECTORY,
    }



    public static final long ID_NONE = -3;
    public static final long ID_ROOT = 0;

    public long luid = 0L;
    public long guid = ID_NONE;
    public long p_guid = ID_NONE;
    public long p_luid = ID_NONE;
    public String name;
    public String url;
    public String signature;
    public ITEM_TYPE item_type = ITEM_TYPE.NORMAL;
    public String next_signature;
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
        this.next_signature = b.next_signature;
        this.next_order_time = b.next_order_time;
    }


    public String getSignature(){
        if(signature == null){
            signature = Md5Utils.getMD5(name + url);
        }

        return signature;
    }

    public long initLuid(){
        if(luid == 0){
            String hashString = (name==null?"":name) + (url==null?"":url) + create_time;
            luid = hashString.hashCode();
        }

        return luid;
    }

    public void updateTo(Bookmark b){
        this.next_signature = b.next_signature;
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
            j.put("parent_guid",String.valueOf(p_guid==-1?"none":p_guid));
            j.put("parent_luid",String.valueOf(p_luid==-1?"none":p_luid));
            j.put("next_signature",String.valueOf(next_signature));
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
