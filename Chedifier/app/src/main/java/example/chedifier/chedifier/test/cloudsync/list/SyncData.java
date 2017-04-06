package example.chedifier.chedifier.test.cloudsync.list;

/**
 * Created by Administrator on 2017/3/29.
 */

public class SyncData {

    public static final long ID_HEAD = -1;
    public static final long ID_TAIL = -2;
    public static final long ID_NONE = -3;

    public long id = ID_NONE;
    public String name;

    public SyncData next = null;

    public long create_time = System.currentTimeMillis();
    public long last_modify = System.currentTimeMillis();


    public boolean dirtyOrder = true;

    protected SyncData(){
        create_time = System.currentTimeMillis();
    }

    protected SyncData(long id){
        this.id = id;
    }

    private SyncData(SyncData data){
        this.id = data.id;
        this.name = data.name;
        this.next = data.next;
        this.create_time = data.create_time;
        this.last_modify = data.last_modify;
        this.dirtyOrder = data.dirtyOrder;
    }

    protected void updateNext(SyncData next){
        this.next = next;

        updateDirtyOrder(true);
    }

    protected void updateDirtyOrder(boolean dirty){
        this.dirtyOrder = dirty;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof SyncData){
            return ((SyncData) obj).id == this.id;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Long.valueOf(id).hashCode();
    }

    @Override
    protected SyncData clone() {
        return new SyncData(this);
    }
}
