package toporesolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import base.Utils;

public class TopoResolver {
	
	
	
	
	public static INode resolve(List<INode> nodes){
		INode head = null;
		if(!Utils.listEmpty(nodes)){
			final Map<String,INode> id2Node = new HashMap<String,INode>();
			final Map<String,ArrayList<Edge>> incomes = new HashMap<>();
			for(INode n:nodes){
				id2Node.put(n.identifier(), n);
				
				inc(incomes,n.identifier(),n.nextId(),n.priority());
				inc(incomes,n.identifier(),n.nextId2(),n.priority2());
				
				ArrayList<Edge> in = incomes.get(n.identifier());
				if(in == null){
					incomes.put(n.identifier(), new ArrayList<Edge>());
				}
			}
			
			Set<Edge> choose = new HashSet<>();
			
			INode cur = null;
			while(!Utils.listEmpty(nodes)){
				List<String> nexts = nexts(incomes);
				
				String next = chooseNode(choose,nexts);
				if(next == null){
					Collections.sort(nexts, new Comparator<String>() {

						@Override
						public int compare(String o1, String o2) {
							
							INode n1 = id2Node.get(o1);
							INode n2 = id2Node.get(o2);
							
							if(n1 == null){
								return n2==null?0:-1/*新增排在前面*/;
							}
							
							if(n2 == null){
								return 1/*新增排在前面*/;
							}
							
							if(Utils.listEmpty(incomes.get(o1))){
								return (int)(Math.max(n1.priority(), n1.priority2()) - Math.max(n2.priority(), n2.priority2()));
							}else{
								long priority1 = maxPriorityOfIncomes(id2Node, incomes.get(o1));
								long priority2 = maxPriorityOfIncomes(id2Node, incomes.get(o2));
								return (int)(priority1-priority2);//既然都在别人的后面，那谁先在后面的在前面
							}
						}
					});
					
					
					next = nexts.get(0);
				}

				INode node = id2Node.get(next);
				if(node != null){
					nodes.remove(node);
					
					choose.add(new Edge(node.identifier(),node.nextId(),node.priority()));
					choose.add(new Edge(node.identifier(),node.nextId2(),node.priority2()));
				
					dec(incomes,node.identifier(),node.nextId());
					dec(incomes,node.identifier(),node.nextId2());
					
					if(node.deleted() == 0){
						if(cur != null){
							cur.setNext(next);
						}else{
							head = node;
						}
						cur = node;
					}
				}
				
				incomes.remove(next);
			}
			
			if(cur != null) cur.setNext(null);
		}
		
		return head;
	}
	
	private static String chooseNode(Set<Edge> choose,List<String> nodes){
		Edge target = null;
		String next = null;
		if(choose != null && nodes != null){
			for(String node:nodes){
				for(Edge e:choose){
					if(node.equals(e.to)){
						if(target == null || target.priority < e.priority){
							target = e;
							next = node;
						}
					}
				}
			}
		}
		
		if(target != null){
			choose.remove(target);
		}
		
		return next;
	}
	
	private static long maxPriorityOfIncomes(Map<String,INode> id2Node,List<Edge> incomes){
		long max = Long.MIN_VALUE;
		if(!Utils.listEmpty(incomes)){
			for(Edge edge:incomes){
				if(edge.priority > max){
					max = edge.priority;
				}
			}
		}
		
		return max;
	}
	
	private static List<String> nexts(Map<String,ArrayList<Edge>> incomes){
		List<String> nexts = new ArrayList<String>();
		Iterator<Entry<String,ArrayList<Edge>>> itr = incomes.entrySet().iterator();
		int min = Integer.MAX_VALUE;
		while(itr.hasNext()){
			Entry<String,ArrayList<Edge>> entry = itr.next();
			if(entry.getValue().size() > min){
				continue;
			}
			
			
			if(entry.getValue().size() < min){
				min = entry.getValue().size();
				nexts.clear();
			}
			
			nexts.add(entry.getKey());
		}
		
		return nexts;
	}
	
	private static void inc(Map<String,ArrayList<Edge>> m,String from,String to,long priority){
		if(m != null && !Utils.stringEmpty(to)){
			ArrayList<Edge> v = m.get(to);
			if(v == null){
				v = new ArrayList<Edge>();
				m.put(to,v);
			}
			
			
			v.add(new Edge(from,to,priority));
		}
	}
	
	private static void dec(Map<String,ArrayList<Edge>> m,String from,String to){
		if(m != null && !Utils.stringEmpty(to)){
			ArrayList<Edge> v = m.get(to);
			
			if(v != null){
				ArrayList<Edge> dels = new ArrayList<>();
				for(Edge edge:v){
					if(edge.from.equals(from)){
						dels.add(edge);
					}
				}
				
				v.removeAll(dels);
			}
		}
	}
	
	
	private static class Edge{
		String from;
		String to;
		
		public Edge(String from,String to,long priority){
			this.from = from;
			this.to = to;
			this.priority = priority;
		}
		
		long priority;
	}

	
	public interface INode{
		
		public String nextId();
		public String nextId2();
		public long priority();
		public long priority2();
		public String identifier();
		public int deleted();
		
		public void setNext(String identifier);
		
	}
}
