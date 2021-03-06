package Models;

import org.scilab.forge.jlatexmath.TeXFormula;
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
    public String latextRepresentation;
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
    }

    private String buildStringRepresentation(FormulaTree formulaTree) {
        MathNode root = (MathNode) formulaTree.getRoot();
        if(!(root instanceof SymbolNode)){
            return root.getNumberValue()+"";
        }
        String initString = root.buildNodeRepresentation(new StringBuffer()).toString();
        buildLatexRepresentation(initString);
        return initString;
    }

    private void buildLatexRepresentation(String initString) {
        StringBuffer sb = new StringBuffer();
        sb.append(accuVariable)
                .append(" = ")
                    .append("\\sum_{")
                        .append(iterator.toString())
                            .append("=0}^{")
                                .append(maxIteratorValue.toString())
                                    .append("}(")
                                        .append(initString)
                                            .append(")");
        latextRepresentation = sb.toString();
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

    public String getStringRepresentation() {
        return stringRepresentation;
    }

    public String getLatextRepresentation() {

        return latextRepresentation;
    }
}
