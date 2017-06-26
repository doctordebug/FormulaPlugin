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
    private FormulaTree formulaTree;

    public void build(List<AssignStmt> loopAssignStatements) {
        ValueTree tree = ValueTree.LoadTree(accuVariable, loopAssignStatements);
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


    private void buildFormulaTree(Node<Value> root) {
        formulaTree = new FormulaTree(root);
        stringRepresentation = buildStringRepresentation(formulaTree);
        System.err.println(stringRepresentation);
    }

    private String buildStringRepresentation(FormulaTree formulaTree) {
        MathNode root = (MathNode) formulaTree.getRoot();
        if(!(root instanceof SymbolNode)){
            return root.getNumberValue()+"";
        }
        String initString = root.buildNodeRepresentation(new StringBuffer()).toString();

        return initString;
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
