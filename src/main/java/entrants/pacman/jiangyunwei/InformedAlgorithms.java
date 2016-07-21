package entrants.pacman.jiangyunwei;

import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.internal.Node;

import java.util.*;
public class InformedAlgorithms
{
	static boolean lastMissing = false;
	public static MOVE Astar(Game game)
	{
		int current = game.getPacmanCurrentNodeIndex();
		List<Integer> all = Utils.allPills(game);
		List<List<Integer>> directions = Utils.divide(all, current, game);
		List<Integer> left = directions.get(0);
		List<Integer> right = directions.get(1);
		List<Integer> up = directions.get(2);
		List<Integer> down = directions.get(3);
		int maxSize = Math.max(Math.max(left.size(), right.size()),Math.max(up.size(), down.size()));
		int dest = -1;
		
		if(maxSize>0)
		{
			lastMissing = true;
			if(left.size()==maxSize) dest=left.get(0);
			else if(right.size()==maxSize) dest = right.get(right.size()-1);
			else if(up.size()==maxSize) dest = up.get(0);
			else dest = down.get(down.size()-1);
			//build the A* path to know which nodes to visit
			int[] path = game.getAStarPath(current, dest, game.getPacmanLastMoveMade());
			//System.out.println(Arrays.toString(path));
			return game.getNextMoveTowardsTarget(current, path[0], DM.MANHATTAN);
			//else return game.getPacmanLastMoveMade();
		}
		else
		{
			// there should be something unexpected if there are no visible pills,
			//in such case, should make decisions based on the situation
			// if the pacman is lost again, it should guess one node have a pill
			// and then go to that node so that it can get out of the dilemma
			//if it is lost for the first time, it should go to the opposite direction
			if(!lastMissing)
			{
				lastMissing = false;
				Node[] nodes = game.getCurrentMaze().graph;
				int randomIndex =(new Random()).nextInt(nodes.length);
				return game.getNextMoveTowardsTarget(current,nodes[randomIndex].nodeIndex, DM.MANHATTAN);
			}else
			{
				if(game.getNeighbour(current, MOVE.UP)<0)
					return MOVE.DOWN;
				if(game.getNeighbour(current, MOVE.DOWN)<0)
					return MOVE.UP;
				if(game.getNeighbour(current, MOVE.LEFT)<0)
					return MOVE.RIGHT;
				if(game.getNeighbour(current, MOVE.RIGHT)<0)
					return MOVE.LEFT;
				return MOVE.NEUTRAL;
			}
		}
		
	}
}
