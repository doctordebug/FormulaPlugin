package Models;

import soot.Local;
import soot.Value;
import soot.jimple.ArithmeticConstant;
import soot.jimple.Constant;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by olisa_000 on 11.06.17.
 */
public class FormulaTree extends Tree{

    private SymbolNode root;

    public FormulaTree(Node<Value> root){
        super(root);
        initTree(root);
    }

    private void initTree(Node<Value> oldRoot) {
        List<Node> ordered = iterateBfs();
        List<MathNode> newTree = new ArrayList<>();

        for (Node current: ordered) {
            MathNode newNode = null;
            if(current.hasAnnotation()) {
                newNode = new SymbolNode();
                ((SymbolNode)newNode).setSymbolRepresentation(current.getAnnotation().toString());
                newTree.add(newNode);
                continue;
            }
            if(current.hasChildren()){
                newNode = new NeutralNode();
                newTree.add(newNode);
                continue;
            }
            if(current.getValue() instanceof Local){
                newNode = new ConstNode();
                ((ConstNode)newNode).setConstName(((Local)current.getValue()).getName());
                newTree.add(newNode);
                continue;
            }
            newNode = new NumberNode();
            Constant c = (Constant) newNode.getValue();
            System.out.println(current.getValue());
            newTree.add(newNode);

        }
    }
}
