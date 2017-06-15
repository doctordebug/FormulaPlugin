package Models;

/**
 * Created by olisa_000 on 11.06.17.
 */
public class MathNode extends Node {

    private MathNode leftChild;

    private MathNode rightChild;

    public MathNode getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(MathNode leftChild) {
        this.leftChild = leftChild;
    }

    public MathNode getRightChild() {
        return rightChild;
    }

    public void setRightChild(MathNode rightChild) {
        this.rightChild = rightChild;
    }
}
