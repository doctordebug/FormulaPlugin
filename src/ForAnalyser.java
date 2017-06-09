import Analysis.GuaranteedDefsAnalysis;
import Analysis.ReachingDefinitions;
import soot.*;
import soot.jimple.toolkits.annotation.logic.Loop;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.LoopNestTree;

/**
 * Created by olisa_000 on 18.05.17.
 */
public class ForAnalyser {

    private SootClass toAnalyse;

    public ForAnalyser(SootClass c) {
        this.toAnalyse = c;
    }

    //TODO: Refactor
    public void analyse() {
        for (SootMethod m : toAnalyse.getMethods()) {
            Body sb = m.retrieveActiveBody();
            ReachingDefinitions rd = new ReachingDefinitions(new BriefUnitGraph(sb));
            GuaranteedDefsAnalysis gds = new GuaranteedDefsAnalysis(new BriefUnitGraph(sb));
            LoopNestTree loopNestTree = new LoopNestTree(sb);

            for (Loop loop : loopNestTree) {
                Models.Loop l = Models.Loop.buildLoop(loop,rd,gds);
                System.out.println(l);
            }
        }
    }


}
