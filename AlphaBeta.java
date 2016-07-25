package entrants.pacman.jiangyunwei;

import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.internal.Node;
import pacman.controllers.Controller;
import pacman.game.Game;
import java.util.*;
class GameState
{
    Game game;
    int depth;
    Queue<MOVE> moves;
    double minimaxScore;
    public GameState(Game game, int depth, Queue<MOVE> moves) {
        this.game = game;
        this.depth = depth;
        this.moves = moves;
    }
    //Updates minimax score to GameState
    public void updateMinimaxScore(double score) {
        this.minimaxScore = score;
    }
}	

public class AlphaBeta{
	private static final int MAX_DEPTH = 3;// the maximum depth to search
	private static final int SEARCH_STEP_DEPTH = 5;
	private static Queue<MOVE> feasibleActions = new LinkedList<MOVE>(); // Stores moves if solution is found
	private static int targetIndex;// the target node's target to search
	private static boolean lastMissing = false;
	// if get lost when making decision, do a random guess to select a target
	public static MOVE randomGuess(Game game)
	{
		int current = game.getPacmanCurrentNodeIndex();
		int[] alivePills = game.getActivePillsIndices();
		int[] alivePowers = game.getActivePowerPillsIndices();
		List<Integer> alive = new ArrayList<Integer>();
		for(int pill:alivePills) alive.add(pill);
		for(int power:alivePowers) alive.add(power);
		int randomIndex =(new Random()).nextInt(alive.size());
		//Making decision based on previous status
		// if it was lost for an odd time, it should choose one destination randomly
		// or, it should try a clockwise way
		if(!lastMissing)
		{
			lastMissing = true;
			return game.getNextMoveTowardsTarget(current,alive.get(randomIndex), DM.EUCLID);
		}else
		{
			MOVE lastMove = game.getPacmanLastMoveMade();
			MOVE nextMove = MOVE.NEUTRAL;
			if(lastMove==MOVE.UP)
				nextMove = MOVE.RIGHT;
			else if(lastMove==MOVE.RIGHT)
				nextMove = MOVE.DOWN;
			else if(lastMove==MOVE.DOWN)
				nextMove = MOVE.LEFT;
			else nextMove = MOVE.UP;
			return game.getNextMoveTowardsTarget(current, alive.get(randomIndex), nextMove, DM.EUCLID);
		}
	}
	public static int getNearestActivePill(Game game) {
		int[] activePills = game.getActivePillsIndices();
		int[] activePowers = game.getActivePowerPillsIndices();
		ArrayList<Integer> allPillsIndices = new ArrayList<Integer>();
		int res = -1;
		double distance = Integer.MAX_VALUE;
		int pacmanIndex = game.getPacmanCurrentNodeIndex();
		for(int pill:activePills)
		{
			double dis = game.getEuclideanDistance(pacmanIndex, pill);
			if(dis<=distance)
			{
				distance = dis;
				res = pill;
			}
		}
		//since power has more scores, and thus we can scale it 
		for(int power:activePowers)
		{
			double dis = game.getEuclideanDistance(pacmanIndex, power)/1.25;
			if(dis<=distance)
			{
				distance = dis;
				res = power;
			}
		}
		return res;
	}
	public static MOVE getMove(Game game, long timeDue) {
		lastMissing = false;
		if(feasibleActions.isEmpty()){ // start new search
			GameState pmNode = new GameState(game, 0, new LinkedList<MOVE>());
			int alpha = 0;
			int beta = Integer.MAX_VALUE;
			GameState solution = alphaBeta(pmNode, MAX_DEPTH, alpha, beta, true, null, pmNode);
			feasibleActions = new LinkedList<MOVE>(solution.moves);
			targetIndex 	= solution.game.getPacmanCurrentNodeIndex();			
		}else{					// Get a move from list
			Game gameCopy = game.copy();
			for(MOVE action: feasibleActions){
				int currentNodeIndex =  gameCopy.getPacmanCurrentNodeIndex();
				gameCopy.updatePacMan(action);	
			}
			int actpillidx = getNearestActivePill(game);
		}
		if (feasibleActions.isEmpty())
		{	
			return randomGuess(game); 
		}
		return feasibleActions.remove();
	}
	/*
	 * function alphabeta(node, depth, α, β, Player)         
		    if  depth = 0 or node is a terminal node
		        return the heuristic value of node
		    if  Player = MaxPlayer // maximum
		        for each child of node // minimum
		            α := max(α, alphabeta(child, depth-1, α, β, not(Player) ))   
		            if β ≤ α // maximum>=α>=β，
		                break                             (* Beta cut-off *)
		        return α
		    else // minimum
		        for each child of node // maximum
		            β := min(β, alphabeta(child, depth-1, α, β, not(Player) )) // 极小节点
		            if β ≤ α // 
		                break                             (* Alpha cut-off *)
		        return β 
		(* Initial call *)
		alphabeta(origin, depth, -infinity, +infinity, MaxPlayer)
	 */
	private static GameState alphaBeta(GameState pmNode,int depth, double alpha, double beta, boolean max, GHOST g, GameState bestSolution) 
	{
		//if depth ==0 or terminal, return heuristics
		if (depth == 0 || pmNode.game.gameOver())
		{	
			bestSolution = pmNode;
			bestSolution.updateMinimaxScore(heuristic(bestSolution));
			return bestSolution;
		}	
		//return max for pacman
		if (max){
			return maxValue(pmNode,depth,alpha, beta,bestSolution);	
		// return min for ghosts
		}else{	 
			return minValue(pmNode,depth,alpha,beta,g,bestSolution);
		}
	}
	private static GameState maxValue(GameState pmNode,int depth,double alpha, double beta,GameState bestSolution)
	{
		double v = Integer.MIN_VALUE;
		GameState solution;
		LinkedList<GameState> nextStates = pacmanSuccessor(pmNode,null);
		for(GameState pmChild : nextStates)
		{
			solution = alphaBeta(pmChild, depth - 1, alpha, beta, false, GHOST.values()[0], bestSolution);	
			v = Math.max(v, solution.minimaxScore);
			if(alpha <= v){
				alpha = v;
				bestSolution = solution;
		}			
			if (beta <= alpha)//pruning
				break;
		}	
		return bestSolution;
	}
	private static GameState minValue(GameState pmNode,int depth, double alpha, double beta, GHOST g, GameState bestSolution)
	{
		double v = Integer.MAX_VALUE;
		GameState solution;
		LinkedList<GameState> nextStates = ghostSuccessor(pmNode,g);
		
		for(GameState pmChild : nextStates){
			if(g.ordinal() == (GHOST.values().length-1))
				solution = alphaBeta(pmChild, depth - 1, alpha, beta, true , null, bestSolution);	
			else
				solution = alphaBeta(pmChild, depth, alpha, beta, false, GHOST.values()[g.ordinal() + 1], bestSolution);	
							
			v = Math.min(v, solution.minimaxScore);
			if(beta >= v)
			{
				beta = v;
				bestSolution = solution;
			}
			if (beta <= alpha){
				break;
			}
		}
		return bestSolution;
	}
	
	/**
	 * Generates heuristic value of each node state
	 * @param pmNode: Current state of game in terms of GameState
	 * @return: Heuristic value of GameState
	 */
	public static double heuristic(GameState pmNode){
		double heuristicScore = 0;
		
		double currentScore 	= pmNode.game.getScore();
		heuristicScore += currentScore * 1000;
		double shortestFoodDistance = pmNode.moves.size() + pmNode.game.getEuclideanDistance(pmNode.game.getPacmanCurrentNodeIndex(), getNearestActivePill(pmNode.game));
		
		shortestFoodDistance = 1000/(shortestFoodDistance+1);
		heuristicScore  += shortestFoodDistance;

		double distanceToGhost	 = getNearestGhostDistance(pmNode.game);
		int lives = pmNode.game.getPacmanNumberOfLivesRemaining()>=2?pmNode.game.getPacmanNumberOfLivesRemaining():2;
		
		distanceToGhost	= Math.min(distanceToGhost, 12) * 50000*(lives);
		heuristicScore += distanceToGhost;
		int currentTime = pmNode.game.getCurrentLevelTime();
		//heuristicScore +=currentTime/20;
		return heuristicScore*0.95;
	}

	private static LinkedList<GameState> pacmanSuccessor(GameState pacman,GHOST g)
	{
		LinkedList<GameState> pacmanNexts = new LinkedList<GameState>();
		int maxDepth = SEARCH_STEP_DEPTH + pacman.depth;
		
		Stack<GameState> stack = new Stack<GameState>();
		stack.push(pacman); 
		
		while(!stack.isEmpty())
		{
			GameState pmNode = stack.pop();
			int pacmanIndex = pmNode.game.getPacmanCurrentNodeIndex();
			MOVE lastMove = pmNode.game.getPacmanLastMoveMade();
			MOVE[] legalActions;
			
			if (pmNode.depth == 0)
				legalActions = pmNode.game.getPossibleMoves(pacmanIndex);
			else
				legalActions = pmNode.game.getPossibleMoves(pacmanIndex, lastMove);				
			
			for(MOVE action : legalActions){
				// Create a child
				GameState pacmanNext = null;
				Queue<MOVE> movesQueue = new LinkedList<MOVE>(pmNode.moves);
				Game gameCopy = pmNode.game.copy();
				gameCopy.updatePacMan(action);
				gameCopy.updateGame();
				
				movesQueue.add(action);
				pacmanNext = new GameState(gameCopy, pmNode.depth + 1, movesQueue);
				int nextNodeIndex = pacmanNext.game.getPacmanCurrentNodeIndex();
				
				if(pacmanNext.depth == maxDepth)
					pacmanNexts.add(pacmanNext);
				else{
					if(pacmanNext.game.isJunction(nextNodeIndex))	pacmanNexts.add(pacmanNext);
					stack.push(pacmanNext);
				}
			}
		}
		return pacmanNexts;
	}
	private static LinkedList<GameState> ghostSuccessor(GameState pmStartNode,GHOST g)
	{
		LinkedList<GameState> pacmanNexts = new LinkedList<GameState>();
		int maxDepth = SEARCH_STEP_DEPTH + pmStartNode.depth;
		
		Stack<GameState> stack = new Stack<GameState>();
		stack.push(pmStartNode); 
		
		while(!stack.isEmpty())
		{
				GameState pmNode = stack.pop();
				// Create a child
				GameState pacmanNext = null;
				int currentNodeIndex = pmNode.game.getGhostCurrentNodeIndex(g);
				MOVE lastMove        = pmNode.game.getGhostLastMoveMade(g);
				MOVE[] legalActions;
				
				if ((pmNode.depth - pmStartNode.depth) == 0)
					legalActions = pmNode.game.getPossibleMoves(currentNodeIndex);
				else
					legalActions = pmNode.game.getPossibleMoves(currentNodeIndex, lastMove);				
				
				if (legalActions.length == 0) {
					Queue<MOVE> movesQueue = new LinkedList<MOVE>(pmNode.moves);
					Game gameCopy = pmNode.game.copy();
					pacmanNext = new GameState(gameCopy, pmNode.depth + 1, movesQueue);					
					pacmanNexts.add(pacmanNext);
				}else{
					for(MOVE action: legalActions)
					{
						Queue<MOVE> movesQueue = new LinkedList<MOVE>(pmNode.moves);
						Game gameCopy = pmNode.game.copy();
						EnumMap<GHOST, MOVE> ghostsMoves = new EnumMap<GHOST, MOVE>(GHOST.class);
						ghostsMoves.put(g, action);					
						gameCopy.updateGhosts(ghostsMoves);
						gameCopy.updateGame();
						pacmanNext = new GameState(gameCopy, pmNode.depth + 1, movesQueue);
						int nextNodeIndex = pacmanNext.game.getGhostCurrentNodeIndex(g);
						
						if(pacmanNext.depth == maxDepth)
							pacmanNexts.add(pacmanNext);
						else{
							if(pacmanNext.game.isJunction(nextNodeIndex))	pacmanNexts.add(pacmanNext);
							stack.push(pacmanNext);
						}
					}
				}
			}
		return pacmanNexts;
	}
	
	/**
	 * Gets indices of nearest ghost using Manhattan distance between current pacman and pill.
	 * @param game: Copy of current game.
	 * @return The index of nearest ghost to pacman. 
	 */
	public static int getNearestGhostDistance(Game game) 
	{
		int currentNodeIndex = game.getPacmanCurrentNodeIndex();
		int distMin	=Integer.MAX_VALUE;
		
		for(GHOST g : GHOST.values()){
			int targetNodeIndex = game.getGhostCurrentNodeIndex(g);
			if (!game.isGhostEdible(g))
				distMin = (int) Math.min(distMin,game.getEuclideanDistance(currentNodeIndex, targetNodeIndex));
		}
		return distMin<5?5:distMin;
	}
}
