package Models;

/**
 * Created by olisa_000 on 11.06.17.
 */
public class ConstNode extends MathNode{
    private String constName;

    public String getConstName() {
        return constName;
    }

    public void setConstName(String constName) {
        this.constName = constName;
    }

    @Override
    public StringBuffer buildNodeRepresentation(StringBuffer sb) {
        return new StringBuffer(getConstName());
    }

    @Override
    public StringBuffer buildLatexNodeRepresentation(StringBuffer sb) {
        return new StringBuffer(getConstName());
    }
}
