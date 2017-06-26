package Models;

/**
 * Created by olisa_000 on 11.06.17.
 */
public class NumberNode extends MathNode {

    private double numValue;

    public double getNumValue() {
        return numValue;
    }

    public void setNumValue(double numberValue) {
        this.numValue = numberValue;
    }

    @Override
    public StringBuffer buildNodeRepresentation(StringBuffer sb) {
        return new StringBuffer(numValue+"");
    }

    @Override
    public StringBuffer buildLatexNodeRepresentation(StringBuffer sb) {
        return new StringBuffer(numValue+"");
    }
}
