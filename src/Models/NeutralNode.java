package Models;

/**
 * Created by olisa_000 on 15.06.17.
 */
public class NeutralNode extends MathNode{

    @Override
    public StringBuffer buildNodeRepresentation(StringBuffer sb) {
        /*only 1 child*/
        if(getLeftChild() != null)
            sb = getLeftChild().buildNodeRepresentation(sb);
        if(getRightChild() != null)
            sb = getRightChild().buildNodeRepresentation(sb);

        return sb;
    }

    @Override
    public StringBuffer buildLatexNodeRepresentation(StringBuffer sb) {
        if(getLeftChild() != null)
            sb = getLeftChild().buildNodeRepresentation(sb);
        if(getRightChild() != null)
            sb = getRightChild().buildNodeRepresentation(sb);

        return sb;
    }
}
