package Models;

import Analysis.ReachingDefinitions;
import Utils.Log;
import soot.Local;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.Stmt;
import soot.jimple.toolkits.annotation.logic.Loop;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by olisa_000 on 26.05.17.
 */
public class EquationTree {

    private final Unit root;
    private final ReachingDefinitions rd;

    public EquationTree(Unit unit, ReachingDefinitions rd) {
        this.root = unit;
        this.rd = rd;
    }

    public static EquationTree build(Loop loop, Value accu, ReachingDefinitions rd) {
        List<Stmt> statements = loop.getLoopStatements();
        Set<Integer> touchedAt = rd.getResult().get(accu);
        Map<Integer, Unit> unitsToLabel = rd.getLabelsToUnit();
        List<Unit> accuAssignedAt = touchedAt.stream().map(unitsToLabel::get).collect(Collectors.toList());
        List<Unit> assignmentsOfAccuInLoop = accuAssignedAt.stream().filter(x -> statements.contains(x)).collect(Collectors.toList());
        Log.i(assignmentsOfAccuInLoop);
        EquationTree result = new EquationTree(assignmentsOfAccuInLoop.get(0), rd);
        result.resolveAssignment();
        return null;
    }

    protected void resolveAssignment() {
        Node rootNode = new Node<Unit>();
        rootNode.setValue(root);
        Stack<ValueBox> toVisit = new Stack<>();
        toVisit.addAll(root.getUseBoxes());
        Map<Value, Set<Integer>> labelsToUnit = rd.getResult();
        Map<Integer, Unit> lzu = rd.getLabelsToUnit();
        Set<ValueBox> visited = new HashSet<>();
        while(!toVisit.empty()){
            ValueBox current = toVisit.pop();
            if (visited.contains(current)) continue;
            visited.add(current);
            System.out.println( "+++" + current + "+++");
            Set<Integer> temp = labelsToUnit.get(current.getValue());
            if(temp == null) continue;
            for(int key : temp){
                toVisit.addAll(lzu.get(key).getUseBoxes());
            }

        }

        System.out.println("exit");
    }
}
