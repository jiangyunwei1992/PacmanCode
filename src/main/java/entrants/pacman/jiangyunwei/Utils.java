package entrants.pacman.jiangyunwei;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import pacman.game.Game;
import pacman.game.internal.Node;

public class Utils 
{
	public static Map<String,Node> pos_node = new HashMap<String,Node>();
	public static boolean isOfDirection(String direction,Node currentNode, Node node)
	{
		//to judge the relative position of node to currentNode
		int curX = currentNode.x;
		int curY = currentNode.y;
		int nodeX = node.x;
		int nodeY = node.y;
		if(direction.equals("left"))
		{
			return nodeX<curX;
		}else if(direction.equals("right"))
		{
			return nodeX>curX;
		}else if(direction.equals("up"))
		{
			return nodeY<curY;
		}
		return nodeY>curY;
	}
	public static List<Integer> allPills(Game game)
	{
		int[] pills = game.getActivePillsIndices();
		//get visible powers
		int[] powers = game.getActivePowerPillsIndices();
		// 
		List<Integer> all = new Stack<Integer>();
		int index = 0;
		for(int pill:pills)
		{
			all.add(pill);
		}
		for(int power:powers)
		{
			all.add(power);
		}
		return all;
	}
	public static List<List<Integer>> divide(List<Integer> all,int current,Game game)
	{
	//the visible pills are in a form of 十,
	// so divide these fills to 4 directions: left, right, up and down
		List<Integer> left = new ArrayList<Integer>();
		List<Integer> right = new ArrayList<Integer>();
		List<Integer> up = new ArrayList<Integer>();
		List<Integer> down = new ArrayList<Integer>();
		Node[] graph = game.getCurrentMaze().graph;
		Node curNode = graph[current];
		for(int i=0;i<all.size();i++)
		{
			Node all_i = graph[all.get(i)];
			if(isOfDirection("left",curNode,all_i))
			{
				left.add(all_i.nodeIndex);
			}else if(isOfDirection("right",curNode,all_i))
			{
				right.add(all_i.nodeIndex);
			}else if(isOfDirection("up",curNode,all_i))
			{
				up.add(all_i.nodeIndex);
			}else
			{
				down.add(all_i.nodeIndex);
			}
		}
		List<List<Integer>> list = new ArrayList<List<Integer>>();
		list.add(left);
		list.add(right);
		list.add(up);
		list.add(down);
		return list;
	}
}
