package entrants.pacman.jiangyunwei;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.internal.Maze;
import pacman.game.internal.Node;
import java.util.*;
public class UninformedAlgorithms 
{
	static Stack<Graph> stack = new Stack<Graph>();
	static Queue<Graph> queue = new LinkedList<Graph>();
	////////////////////////////////////////////////////
	public static Stack<Graph> DFS(Graph graph,Stack<Graph> stack)
	{
		graph.visited = true;
		stack.push(graph);
		for(int i=0;i<graph.neighbors.size();i++)
		{
			Graph adj = graph.neighbors.get(i);
			if(!adj.visited)
			{
				DFS(adj,stack);
			}
		}
		return stack;
	}
	public static MOVE DFS(Game game)
	{
		int current = game.getPacmanCurrentNodeIndex();
		//get all the visible pills and powerpills
		List<Integer> all = Utils.allPills(game);
		//divide into left, right, up, down
		List<List<Integer>> directions = Utils.divide(all,current, game);
		
		//build graph for these directions
		Graph graph = Graph.buildGraph(directions, current);
		//DFS to get the traversal order
		Stack<Graph> stack = new Stack<Graph>();
		stack = DFS(graph,stack);
		//transform the traversal order into arrays
		MOVE move = game.getNextMoveTowardsTarget(current,stack.pop().node, DM.PATH);
		return move;
	}
	
	//////////////////////////////////////////////
	public static Queue<Graph> BFS(Graph graph,Game game)
	{
		Queue<Graph> queue = new LinkedList<Graph>();
		Queue<Graph> path = new LinkedList<Graph>();//return as the BFS path
		graph.visited = true;
		queue.add(graph);
		while(!queue.isEmpty())
		{
			Graph g = queue.poll();
			path.add(g);
			Set<Integer> set = new HashSet<Integer>();
			for(int i=0;i<g.neighbors.size();i++)
			{
				set.add(i);
			}
			for(int i=0;i<g.neighbors.size();i++)
			{
				Graph neighbor = g.neighbors.get(i);
				if(!neighbor.visited)
				{
					queue.add(neighbor);
					neighbor.visited = true;
				}
			}
		}
		return path;
	}
	public static MOVE BFS(Game game)
	{
			int current = game.getPacmanCurrentNodeIndex();
			//get all the visible pills and powerpills
			List<Integer> all = Utils.allPills(game);
			//divide into left, right, up, down
			List<List<Integer>> directions = Utils.divide(all,current, game);
			List<Integer> left = directions.get(0);
			List<Integer> right = directions.get(1);
			List<Integer> up = directions.get(2);
			List<Integer> down = directions.get(3);
			//since visible pills in each direction is in the same line,
			//it is feasible to visit the farthest pill
			if(left.size()>=2)
			{
				int leftExtreme = left.get(0);
				left.clear();
				left.add(leftExtreme);
			}
			if(right.size()>=2)
			{
				int rightExtreme = right.get(right.size()-1);
				right.clear();
				right.add(rightExtreme);
			}
			if(up.size()>=2)
			{
				int upExtreme = up.get(0);
				up.clear();
				up.add(upExtreme);
			}
			if(down.size()>=2)
			{
				int downExtreme = down.get(down.size()-1);
				down.clear();
				down.add(downExtreme);
			}
			directions.clear();
			directions.add(left);
			directions.add(right);
			directions.add(up);
			directions.add(down);
			
			//build graph for these directions
			Graph graph = Graph.buildGraph(directions, current);
			//DFS to get the traversal order
			queue = BFS(graph,game);
			//visit the fist pill
			while(queue.size()>1)
				queue.poll();
			//transform the traversal order into arrays
			MOVE move = game.getNextMoveTowardsTarget(current,queue.poll().node, DM.PATH);
			return move;	
	}
}
