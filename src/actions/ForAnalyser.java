package actions;

import Analysis.GuaranteedDefsAnalysis;
import Analysis.ReachingDefinitions;
import Models.Formula;
import soot.*;
import soot.jimple.toolkits.annotation.logic.Loop;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.LoopNestTree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by olisa_000 on 18.05.17.
 */
public class ForAnalyser {

    private SootClass toAnalyse;

    public ForAnalyser(SootClass c) {
        this.toAnalyse = c;
    }

    //TODO: Refactor
    public List<AnalysisResult> analyse() {
        ArrayList<AnalysisResult> result = new ArrayList<>();
        for (SootMethod m : toAnalyse.getMethods()) {
            Body sb = m.retrieveActiveBody();
            ReachingDefinitions rd = new ReachingDefinitions(new BriefUnitGraph(sb));
            GuaranteedDefsAnalysis gds = new GuaranteedDefsAnalysis(new BriefUnitGraph(sb));
            LoopNestTree loopNestTree = new LoopNestTree(sb);
            for (Loop loop : loopNestTree) {
                AnalysisResult ar = new AnalysisResult();
                ar.loop = Models.Loop.buildLoop(loop,rd,gds);
                ar.formula = ar.loop.findEquations();
                result.add(ar);
            }
        }
        return result;
    }

    public class AnalysisResult{
        public Models.Loop loop;
        public List<Formula> formula;
    }

}
