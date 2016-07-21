package entrants.pacman.jiangyunwei;

import java.util.ArrayList;
import java.util.List;

public class Graph
{
	int node;
	List<Graph> neighbors;
	boolean visited = false;
	public Graph(int node, List<Graph> neighbors)
	{
		this.node = node;
		this.neighbors = neighbors;
	}
	public Graph(int node)
	{
		this.node = node;
		this.neighbors = new ArrayList<Graph>();
	}
	public static Graph buildGraph(List<List<Integer>> lists,int current)
	{
		//all visible pills of each direction is on a line
		//thus, when creating the graph, all nodes should
		//be lined to each other to form a line
		Graph root = new Graph(current);
		for(int index=0;index<lists.size();index++)
		{
			List<Integer> list = lists.get(index);
			
			if(list.size()>0)
			{
				Graph[] links = new Graph[list.size()];
				for(int i=0;i<list.size();i++)
				{
					links[i] = new Graph(list.get(i));
				}
				for(int i=list.size()-1;i>0;i--)
				{
					links[i].neighbors.add(links[i-1]);
				}
				root.neighbors.add(links[links.length-1]);
			}
		}
		return root;
	}
}
