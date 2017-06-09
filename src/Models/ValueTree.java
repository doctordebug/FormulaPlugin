package Models;

import org.apache.xerces.xs.ShortList;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.BinopExpr;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;

/**
 * Created by olisa_000 on 09.06.17.
 */
//TODO: build parentclass: tree
public class ValueTree {

    private Node<Value> root;

    public ValueTree(Value acc, List<AssignStmt> loopAssignStatements){
        this.root = new Node<>();
        root.setValue(acc);
        initTree(loopAssignStatements);
    }

    //TODO Refactor
    private void initTree(List<AssignStmt> loopAssignStatements) {
        HashSet<Value> visited = new HashSet();
        Stack<Node<Value>> toVisit = new Stack<>();
        Node<Value> newRoot = new Node<>();
        AssignStmt lastAssignmentStmtOfRoot = loopAssignStatements.stream().filter(x -> x.getLeftOp() == this.root.getValue()).reduce((a, b) -> b).get();
        newRoot.setValue(lastAssignmentStmtOfRoot.getLeftOp());
        int maxIndex = loopAssignStatements.indexOf(lastAssignmentStmtOfRoot);
        toVisit.push(newRoot);
        while(!toVisit.empty()){
            Node<Value> current = toVisit.pop();
            if(visited.contains(current.getValue()))
                continue;
            visited.add(current.getValue());

            if(loopAssignStatements.subList(0, maxIndex + 1).stream().noneMatch(x -> x.getLeftOp() == current.getValue()))
                continue;
            // last
            AssignStmt newChild = loopAssignStatements.subList(0, maxIndex + 1).stream().filter(x -> x.getLeftOp() == current.getValue()).reduce((first, second) -> second).get();
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
        this.root = newRoot;
    }

    public boolean refersItself(Value v){
        return pathPathFromTo(v,v);
    }

    public boolean pathPathFromTo(Value from, Value to) {
        Stack<Node> toVisit = new Stack<>();
        HashSet<Node> visited = new HashSet<>();
        toVisit.push(root);
        Node<Value> start = null;
        while(!toVisit.empty()){
            Node<Value> current = toVisit.pop();
            if(visited.contains(current))
                continue;
            visited.add(current);
            if(current.getValue() == from){
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
            if(current.getValue() == to && current != start){
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
        result+= "root: " + root.getValue();
        Stack<Node> toVisit = new Stack<>();
        toVisit.push(root);
        result +="\n";
        while(!toVisit.empty()){
            Node<Value> current = toVisit.pop();
            result += "Node "+current.getValue();
            result += (current.getAnnotation() != null)? "\n  [Symbol: "+current.getAnnotation() +"]":"";
            result += "\nChildren ";
            for (Node<Value> child : current.getChildren()){
                toVisit.push(child);
                result += whiteSpace + child.getValue();
            }
            result +="\n";
        }
        return result;
    }


}
