package bookmark;

import toporesolver.TopoResolver.INode;

public class Bookmark implements INode{
	
	
	public String name;
	public String url;
	
	public String local_next;
	public String cloud_next;
	
	public long order_time;
	public long cloud_order_time;
	
	
	public int dirty = 1;
	
	public int deleted = 0;
	
	public Bookmark(String name){
		this.name = name;
	}
	

	@Override
	public String nextLuid() {
		return local_next;
	}

	@Override
	public String nextLuidOfCloud() {
		return cloud_next;
	}

	@Override
	public long nextPriority() {
		return order_time;
	}
	
	@Override
	public long nextPriorityOfCloud() {
		return cloud_order_time;
	}

	@Override
	public String luid() {
		return name;//Utils.getMD5(name+url);
	}
	
	@Override
	public int deleted() {
		return deleted;
	}

	@Override
	public void updateNext(String identifier) {
		if(local_next == null || !local_next.equals(identifier)){
			local_next = identifier;
			dirty = 1;
			order_time = cloud_order_time = System.currentTimeMillis();
		}
	}

}
