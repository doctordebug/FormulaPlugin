import Models.ForLoop;
import Utils.SootEnviroment;
import soot.*;
import soot.jimple.AssignStmt;
import soot.jimple.DefinitionStmt;
import soot.jimple.IfStmt;
import soot.jimple.Stmt;
import soot.jimple.internal.*;
import soot.jimple.toolkits.annotation.logic.Loop;
import soot.toolkits.graph.LoopNestTree;

import java.util.Arrays;
import java.util.stream.Collectors;

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
            LoopNestTree loopNestTree = new LoopNestTree(sb);
            for (Loop loop : loopNestTree) {
                if(loop.loopsForever() || !loop.hasSingleExit()) continue;
                ForLoop fl = new ForLoop();
                //Condition: first if stmt in loop is condition
                JIfStmt condition = (JIfStmt) loop.getLoopStatements().stream().filter(x -> x instanceof IfStmt).findFirst().map(x -> (IfStmt) x).get();
                fl.setCondition(condition);
                //find all assign statements
                fl.setAssignStmts(loop.getLoopStatements().stream().filter( x -> x instanceof AssignStmt).map( x -> (AssignStmt) x).collect(Collectors.toList()));
                //find iterator
                if(!findIterator(fl, condition)) continue;
                //find all possible accumulation-vars
                fl.setAccuVariables(fl.getAssignStmts().stream().map(DefinitionStmt::getLeftOp).distinct().collect(Collectors.toList()));
                //find all stmts touching iterators
                fl.setTouchStmts(fl.getAssignStmts().stream().filter(x -> fl.getIterators().keySet().contains(x.getLeftOp())).collect(Collectors.toList()));
                System.out.println(fl);
            }
        }
    }

    private boolean findIterator(ForLoop fl, JIfStmt condition) {
        Value c = condition.getCondition();
        Value iterator = null;
        Value maxValue = null;
        if(c instanceof JLtExpr){
            iterator = ((JLtExpr) c).getOp1();
            maxValue = ((JLtExpr) c).getOp2();
        }
        else if(c instanceof JGtExpr){
            iterator = ((JGtExpr) c).getOp1();
            maxValue = ((JGtExpr) c).getOp2();
        }
        if(iterator != null && maxValue != null)
            fl.addIterator(iterator, maxValue);
        return fl.getIterators().size() > 0;
    }
}