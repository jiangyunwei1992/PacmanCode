package entrants.pacman.jiangyunwei;

import pacman.controllers.PacmanController;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.internal.Node;

import java.util.*;
/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getMove() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., entrants.pacman.username).
 */
public class MyPacMan extends PacmanController {
    private MOVE myMove = MOVE.NEUTRAL;

    public MOVE getMove(Game game, long timeDue) 
    {      
    		//myMove = UninformedAlgorithms.BFS(game);
    		//myMove = UninformedAlgorithms.DFS(game);	
    		myMove = InformedAlgorithms.Astar(game);
    		return myMove;
    }
}