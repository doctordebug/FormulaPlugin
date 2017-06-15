package Models;

import soot.Value;
import soot.jimple.AssignStmt;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

/**
 * Created by olisa_000 on 11.06.17.
 */
public class FormulaTree extends Tree{

    private SymbolNode root;

    public FormulaTree(Node<Value> root){
        initTree(root);
    }

    private void initTree(Node<Value> oldRoot) {
        Stack<MathNode> toVisitMath = new Stack<>();
        Stack<Node> toVisit = new Stack<>();
        Set<Node> visited = new HashSet<>();
        toVisit.push(oldRoot);
        root = new SymbolNode();
        root.setSymbolRepresentation((String) root.getAnnotation());
        toVisitMath.push(root);
        while(!toVisit.empty()){
            Node<Value> current = toVisit.pop();
            Node<MathNode> currentMath = toVisitMath.pop();
            if(visited.contains(current))
                continue;
            visited.add(current);

            for (Node<Value> child : current.getChildren()){
                toVisit.push(child);
                //TODO
                //MathNode newChild = new M;
                //toVisitMath.push(newChild);
            }

        }
    }
}
