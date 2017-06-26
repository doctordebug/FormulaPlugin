package Models;

/**
 * Created by olisa_000 on 11.06.17.
 */
public class SymbolNode extends MathNode{

    private String SymbolRepresentation;

    public String getSymbolRepresentation() {
        return SymbolRepresentation;
    }

    public void setSymbolRepresentation(String symbolRepresentation) {
        SymbolRepresentation = symbolRepresentation;
    }
    @Override
    public StringBuffer buildNodeRepresentation(StringBuffer sb) {
        StringBuffer result = new StringBuffer();
        if(getLeftChild() != null)
            result.append(getLeftChild().buildNodeRepresentation(sb));
        result.append(SymbolRepresentation);
        if(getRightChild() != null)
            result.append(getRightChild().buildNodeRepresentation(sb));

        return result;
    }

    @Override
    public StringBuffer buildLatexNodeRepresentation(StringBuffer sb) {
        StringBuffer result = new StringBuffer();
        if(getLeftChild() != null)
            result.append(getLeftChild().buildNodeRepresentation(sb));
        result.append(SymbolRepresentation);
        if(getRightChild() != null)
            result.append(getRightChild().buildNodeRepresentation(sb));

        return result;
    }
}
