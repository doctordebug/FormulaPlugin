package Models;

import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.BinopExpr;

import java.util.HashSet;
import java.util.List;
import java.util.Stack;


/**
 * Created by olisa_000 on 09.06.17.
 */
//TODO: build parentclass: tree
public class ValueTree extends Tree{

    //private Node root;

    private ValueTree(Node root) {
        super(root);
    }


    public static ValueTree LoadTree(Value x, List<AssignStmt> loopAssignStatements) {
        return new ValueTree(initTree(x, loopAssignStatements));
    }

    //TODO Refactor
    private static Node initTree(Value rootValue, List<AssignStmt> loopAssignStatements) {
        HashSet<Value> visited = new HashSet();
        Stack<Node<Value>> toVisit = new Stack<>();
        Node<Value> newRoot = new Node<>();
        AssignStmt lastAssignmentStmtOfRoot = loopAssignStatements.stream().filter(x -> x.getLeftOp() == rootValue).reduce((a, b) -> b).get();
        newRoot.setValue(lastAssignmentStmtOfRoot.getLeftOp());
        int maxIndex = loopAssignStatements.indexOf(lastAssignmentStmtOfRoot);
        toVisit.push(newRoot);
        while(!toVisit.empty()){
            Node<Value> current = toVisit.pop();
            if(visited.contains(current.getNumberValue()))
                continue;
            visited.add(current.getNumberValue());

            if(loopAssignStatements.subList(0, maxIndex + 1).stream().noneMatch(x -> x.getLeftOp() == current.getNumberValue()))
                continue;
            // last
            AssignStmt newChild = loopAssignStatements.subList(0, maxIndex + 1).stream().filter(x -> x.getLeftOp() == current.getNumberValue()).reduce((first, second) -> second).get();
            /*insert*/
            if(newChild.getRightOp() instanceof BinopExpr){
                BinopExpr assignment = (BinopExpr) newChild.getRightOp();
                Node<Value> n1 = new Node<>();
                n1.setParent(current);
                n1.setValue(assignment.getOp1());
                current.addChild(n1);
                Node<Value> n2 = new Node<>();
                n2.setParent(current);
                n2.setValue(assignment.getOp2());
                current.addChild(n2);
                toVisit.push(n1);
                toVisit.push(n2);
                current.setAnnotation(assignment.getSymbol());
            }else{
                Node<Value> n1 = new Node<>();
                n1.setParent(current);
                n1.setValue(newChild.getRightOp());
                current.addChild(n1);
                toVisit.push(n1);
            }
        }
        return newRoot;

    }

    public boolean refersItself(Value v){
        return pathFromTo(v,v);
    }

    public boolean pathFromTo(Value from, Value to) {
        Stack<Node> toVisit = new Stack<>();
        HashSet<Node> visited = new HashSet<>();
        toVisit.push(getRoot());
        Node<Value> start = null;
        while(!toVisit.empty()){
            Node<Value> current = toVisit.pop();
            if(visited.contains(current))
                continue;
            visited.add(current);
            if(current.getNumberValue() == from){
                start = current;
                break;}
            toVisit.addAll(current.getChildren());
        }
        toVisit.clear();
        visited.clear();
        if(start == null) return false;
        toVisit.push(start);
        while(!toVisit.empty()){
            Node<Value> current = toVisit.pop();
            if(visited.contains(current))
                continue;
            visited.add(current);
            if(current.getNumberValue() == to && current != start){
                return  true;
            }
            toVisit.addAll(current.getChildren());
        }
        return false;
    }

    //TODO: use sb
    @Override
    public String toString() {
        String result = " VALUE-TREE:\n";
        String whiteSpace = " ";
        result+= "root: " + getRoot().getNumberValue();
        Stack<Node> toVisit = new Stack<>();
        toVisit.push(getRoot());
        result +="\n";
        while(!toVisit.empty()){
            Node<Value> current = toVisit.pop();
            result += "Node "+current.getNumberValue();
            result += (current.getAnnotation() != null)? "\n  [Symbol: "+current.getAnnotation() +"]":"";
            result += "\nChildren ";
            for (Node<Value> child : current.getChildren()){
                toVisit.push(child);
                result += whiteSpace + child.getNumberValue();
            }
            result +="\n";
        }
        return result;
    }

}

