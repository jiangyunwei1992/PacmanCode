
import entrants.pacman.jiangyunwei.MyPacMan;
import examples.commGhosts.POCommGhosts;
import pacman.Executor;
//import pacman.entries.pacman.MyPacMan;
import examples.poPacMan.POPacMan;


/**
 * Created by pwillic on 06/05/2016.
 */
public class Main {

    public static void main(String[] args) {

        Executor executor = new Executor(true, true);

        //executor.runGameTimed(new POPacMan(), new POCommGhosts(50), true);
        executor.runGameTimed(new MyPacMan(), new POCommGhosts(50), true);
    }
}
