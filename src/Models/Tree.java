package Models;

import soot.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by olisa_000 on 15.06.17.
 */
public class Tree {
    private Node root;

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    public List<Node> iterateBfs() {
        Stack<Node> toVisit = new Stack<>();
        toVisit.push(root);
        List result = new ArrayList();
        while (!toVisit.empty()) {
            Node<Value> current = toVisit.pop();
            result.add(current);
            for (Node<Value> child : current.getChildren()) {
                toVisit.push(child);
            }
        }
        //TODO: yield return possible?!
        return result;
    }

    public String toString() {
        String result = "TREE:\n";
        String whiteSpace = " ";
        result += "root: " + root.getNumberValue();
        Stack<Node> toVisit = new Stack<>();
        toVisit.push(root);
        result += "\n";
        while (!toVisit.empty()) {
            Node<Value> current = toVisit.pop();
            result += "Node " + current.getNumberValue();
            result += (current.getAnnotation() != null) ? "\n  [Symbol: " + current.getAnnotation() + "]" : "";
            result += "\nChildren ";
            for (Node<Value> child : current.getChildren()) {
                toVisit.push(child);
                result += whiteSpace + child.getNumberValue();
            }
            result += "\n";
        }
        return result;
    }
}
