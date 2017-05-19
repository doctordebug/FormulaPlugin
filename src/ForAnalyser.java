import soot.Body;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.toolkits.annotation.logic.Loop;
import soot.toolkits.graph.LoopNestTree;

/**
 * Created by olisa_000 on 18.05.17.
 */
public class ForAnalyser {

    private SootClass toAnalyse;

    public ForAnalyser(SootClass c) {
        this.toAnalyse = c;
    }

    public void analyse() {
       for (SootMethod m : toAnalyse.getMethods()) {
            Body sb = m.retrieveActiveBody();
            System.out.println(sb);

            LoopNestTree loopNestTree = new LoopNestTree(sb);


            for (Loop loop : loopNestTree) {
                System.out.println(loop.getLoopStatements());
            }
        }
    }
}
