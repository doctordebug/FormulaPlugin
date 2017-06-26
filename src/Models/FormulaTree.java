package Models;

import soot.Local;
import soot.Value;
import soot.jimple.*;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by olisa_000 on 11.06.17.
 */
public class FormulaTree extends Tree{

    public FormulaTree(Node<Value> root){
        super(root);
        initTree();
    }

    private void initTree() {
        List<Node> ordered = iterateBfs();
        List<MathNode> newTree = new ArrayList<>();

        for (Node current: ordered) {
            MathNode newNode;
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
            Constant c = (Constant) current.getValue();
            if(c instanceof IntConstant){
                ((NumberNode)newNode).setNumValue(((IntConstant) c).value);
            }
            if(c instanceof FloatConstant){
                ((NumberNode)newNode).setNumValue(((FloatConstant) c).value);
            }
            if(c instanceof DoubleConstant){
                ((NumberNode)newNode).setNumValue(((DoubleConstant) c).value);
            }
            System.err.println("Class:"+ c.getClass());
            newTree.add(newNode);
        }
        assignParents(ordered, newTree);
        setRoot(newTree.get(0));
    }

    private void assignParents(List<Node> ordered, List<MathNode> newTree) {
        for(int index = 0; index < ordered.size(); ++index){
            Node current = ordered.get(index);
            if(current.hasChildren()){
                List children = new ArrayList(2);
                children.addAll(current.getChildren());
                if(children.size() > 0){
                    int newLeftChildIndex = ordered.indexOf(children.get(0));
                    newTree.get(index).setLeftChild(newTree.get(newLeftChildIndex));
                }
                if(children.size() > 1){
                    int newRightChildIndex = ordered.indexOf(children.get(1));
                    newTree.get(index).setRightChild(newTree.get(newRightChildIndex));
                }
            }
        }
    }
}
