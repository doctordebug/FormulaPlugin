package Models;

import Analysis.ReachingDefinitions;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.IntConstant;
import soot.jimple.Stmt;
import soot.jimple.internal.JimpleLocal;
import soot.jimple.toolkits.annotation.logic.Loop;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by olisa_000 on 26.05.17.
 */
public class EquationTree extends Tree{

    private final Node root;
    private final ReachingDefinitions rd;


    public EquationTree(Node root, ReachingDefinitions rd) {
        super(root);
        this.root = root;
        this.rd = rd;
    }

    public static EquationTree build(Loop loop, Value accu, ReachingDefinitions rd) {
        List<Stmt> statements = loop.getLoopStatements();
        Set<Integer> touchedAt = rd.getResult().get(accu);
        Map<Integer, Unit> unitsToLabel = rd.getLabelsToUnit();
        List<Unit> accuAssignedAt = touchedAt.stream().map(unitsToLabel::get).collect(Collectors.toList());
        List<Unit> assignmentsOfAccuInLoop = accuAssignedAt.stream().filter(statements::contains).collect(Collectors.toList());
        Node root = new Node<>();
        root.setValue(assignmentsOfAccuInLoop.get(0));
        for (ValueBox u: assignmentsOfAccuInLoop.get(0).getUseBoxes()) {
            Node child = new Node();
            child.setValue(u.getValue());
            child.setParent(root);
            root.addChild(child);
        }
        EquationTree result = new EquationTree(root, rd);
        result.resolveAssignment();
        return result;
    }

    protected void resolveAssignment() {
        Stack<Node> toVisit = new Stack<>();
        Set<Object> visited = new HashSet<>();
        Map<Value, Set<Integer>> unitsToLabel = rd.getResult();
        Map<Integer, Unit> labelsToUnit = rd.getLabelsToUnit();
        Node currentNode;
        toVisit.addAll(root.getChildren());
        while(!toVisit.empty()){
            currentNode = toVisit.pop();
            Value val = (Value) currentNode.getNumberValue();
            if(unitsToLabel.get(val) == null || visited.contains(currentNode.getNumberValue())) continue;
            visited.add(currentNode.getNumberValue());
            for (int label: unitsToLabel.get(val)) {
                Unit newChild = labelsToUnit.get(label);
                for(ValueBox u : newChild.getUseBoxes()){
                    Node n = new Node();
                    n.setValue(u.getValue());
                    n.setParent(currentNode);
                    currentNode.addChild(n);
                    toVisit.push(n);
                }
            }
        }
    }

    //Todo: write white label bfs in tree use stringbufffer
    public String toReadableEquation(){
        String Equi = "";
        Stack<Node> toVisit = new Stack<>();
        Set<Node> visited = new HashSet<>();
        Node currentNode;
        toVisit.push(root);
        while (!toVisit.empty()){
            currentNode = toVisit.pop();
            if(visited.contains(currentNode)) continue;
            visited.add(currentNode);
            if(currentNode.hasChildren()){
                  toVisit.addAll(currentNode.getChildren());
            }else{
                /*leafs*/
                if(currentNode.getNumberValue() instanceof JimpleLocal)
                    Equi += ((JimpleLocal)currentNode.getNumberValue()).getName()+" OP ";
                if(currentNode.getNumberValue() instanceof IntConstant)
                    Equi += ((IntConstant)currentNode.getNumberValue()).value+".0 OP ";
           }
        }
        return "#>"+Equi;
    }

    @Override
    public String toString() {
        return root.toString();
    }


}
