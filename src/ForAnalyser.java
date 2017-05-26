import Analysis.GuaranteedDefsAnalysis;
import Analysis.ReachingDefinitions;
import Models.EquationTree;
import Models.ForLoop;
import Models.Formula;
import Utils.Log;
import soot.*;
import soot.jimple.AssignStmt;
import soot.jimple.DefinitionStmt;
import soot.jimple.IfStmt;
import soot.jimple.Stmt;
import soot.jimple.internal.*;
import soot.jimple.toolkits.annotation.logic.Loop;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.LoopNestTree;
import soot.toolkits.scalar.FlowSet;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
                List<IfStmt> condition = loop.getLoopStatements().stream().filter(x -> x instanceof IfStmt).map(x -> (IfStmt) x).collect(Collectors.toList());
                Value c = condition.get(0).getCondition();

                Value iterator = null;
                Value maxValue = null;

                if(c instanceof JGtExpr){
                    iterator = ((JGtExpr) c).getOp1();
                    maxValue = ((JGtExpr) c).getOp2();
                }
                if(c instanceof JLtExpr){
                    iterator = ((JLtExpr) c).getOp1();
                    maxValue = ((JLtExpr) c).getOp2();
                }

                FlowSet assignedBeforeLoop = gds.getUnitToGenerateSet().get(loop.getHead());
                List<Value> accumulatedInsideLoop = loop.getLoopStatements().stream().filter(x -> x instanceof AssignStmt).map(x -> ((AssignStmt) x).getLeftOp()).collect(Collectors.toList());
                List<Value> possibleAccuVarsIncludingIteratorVars = accumulatedInsideLoop.stream().filter(x -> assignedBeforeLoop.contains(x)).collect(Collectors.toList());
                possibleAccuVarsIncludingIteratorVars.remove(iterator);
                for(Value accu : possibleAccuVarsIncludingIteratorVars){
                    Formula f = new Formula();
                    f.setMaxIteratorValue(Integer.parseInt(maxValue.toString().trim()));
                    f.setInitialValue(Integer.parseInt(rd.getFirstOccurence().get(accu).getUseBoxes().get(0).getValue().toString().trim()));
                    f.setIteratorName(iterator.toString().trim());
                    f.setEquation(EquationTree.build(loop,accu,rd));
                    Log.i(f.toString());
                }

            }
        }
    }


}
