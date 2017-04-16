package toporesolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import base.Utils;

public class TopoResolver {
	
	
	public static INode resolve(List<INode> nodes){
		INode head = null;
		if(!Utils.listEmpty(nodes)){
			final Map<String,INode> id2Node = new HashMap<String,INode>();
			Map<String,Integer> incomes = new HashMap<>();
			for(INode n:nodes){
				id2Node.put(n.identifier(), n);
				
				inc(incomes,n.nextId());
				inc(incomes,n.nextId2());
				
				Integer in = incomes.get(n.identifier());
				if(in == null){
					incomes.put(n.identifier(), 0);
				}
			}
			
			
			INode cur = null;
			while(!Utils.listEmpty(nodes)){
				List<String> nexts = nexts(incomes);
				
				Collections.sort(nexts, new Comparator<String>() {

					@Override
					public int compare(String o1, String o2) {
						
						INode n1 = id2Node.get(o1);
						INode n2 = id2Node.get(o2);
						if(n1 == null){
							return n2==null?0:-1;
						}
						
						if(n2 == null){
							return 1;
						}
						
						return (int)(n1.priority() - n2.priority());
					}
				});
				
				
				String next = nexts.get(0);
				INode node = id2Node.get(next);
				if(node != null){
					nodes.remove(node);
				
					dec(incomes,node.nextId());
					dec(incomes,node.nextId2());
					
					if(cur != null){
						cur.setNext(next);
					}else{
						head = node;
					}
					cur = node;
				}
				
				incomes.remove(next);
			}
			
			if(cur != null) cur.setNext(null);
		}
		
		return head;
	}
	
	
	
	private static List<String> nexts(Map<String,Integer> incomes){
		List<String> nexts = new ArrayList<String>();
		Iterator<Entry<String,Integer>> itr = incomes.entrySet().iterator();
		int min = Integer.MAX_VALUE;
		while(itr.hasNext()){
			Entry<String,Integer> entry = itr.next();
			if(entry.getValue() > min){
				continue;
			}
			
			
			if(entry.getValue() < min){
				min = entry.getValue();
				nexts.clear();
			}
			
			nexts.add(entry.getKey());
		}
		
		return nexts;
	}
	
	private static void inc(Map<String,Integer> m,String key){
		if(m != null && !Utils.stringEmpty(key)){
			Integer v = m.get(key);
			int val = v==null?1:++v;
			m.put(key, val);
		}
	}
	
	private static void dec(Map<String,Integer> m,String key){
		if(m != null && !Utils.stringEmpty(key)){
			Integer v = m.get(key);
			if(v != null){
				m.put(key, --v);
			}
		}
	}
	
	
	
	

	public interface INode{
		
		public String nextId();
		public String nextId2();
		public long priority();
		public String identifier();
		
		public void setNext(String identifier);
		
	}
}
