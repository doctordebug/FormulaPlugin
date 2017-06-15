package Models;

import soot.Local;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.Constant;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

/**
 * Created by olisa_000 on 19.05.17.
 */
public class Formula {
    public AccuMode type;
    public Value accuVariable;
    public Value iterator;
    public Value maxIteratorValue;
    public Value iteratorInitValue;
    public String stringRepresentation;

    public void build(List<AssignStmt> loopAssignStatements) {
        ValueTree tree = new ValueTree(accuVariable, loopAssignStatements);
        System.err.println(tree);
        Node<Value> root = tree.getRoot();
        if (root.getChildren().size() != 1) return;
        Node<Value> firstChild = root.getChildren().stream().findFirst().get();
        findType(firstChild);

        Set<Node<Value>> firstChildChildren = firstChild.getChildren();
        if (firstChildChildren.size() != 2) return;
        Node<Value> accuChild = firstChildChildren.stream().filter(x-> x.getAnnotation() == null).findFirst().get();
        Node<Value> formulaChild = firstChildChildren.stream().filter(x-> x != accuChild).findFirst().get();
        buildFormulaTree(formulaChild);
    }

    //TODO use sb // debug
    private void buildFormulaTree(Node<Value> root) {
        new FormulaTree(root);
    }

    private void findType(Node<Value> firstChild) {
        String typeString = ((String) firstChild.getAnnotation()).trim();
        switch (typeString) {
            case "*":
                type = AccuMode.PROD;
                break;
            case "+":
                type = AccuMode.SUM;
                break;
            default:
                type = AccuMode.UNKNOWN;
                break;
        }
    }
}
