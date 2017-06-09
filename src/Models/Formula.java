package Models;

import soot.Value;

/**
 * Created by olisa_000 on 19.05.17.
 */
public class Formula {
    private int initialValue = 0;
    private int MaxIteratorValue = 0;
    private int IteratorStartsAt = 0;
    private EquationTree equation;
    private String iteratorName;

    //TODO: use SB
    @Override
    public String toString() {
        return
                initialValue+"\n" + MaxIteratorValue+"\n" + IteratorStartsAt+"\n"+equation.toReadableEquation()+"\n";
    }

    public EquationTree getEquation() {
        return equation;
    }

    public int getInitialValue() {
        return initialValue;
    }

    public int getIteratorStartsAt() {
        return IteratorStartsAt;
    }

    public int getMaxIteratorValue() {
        return MaxIteratorValue;
    }

    public void setEquation(EquationTree equation) {
        this.equation = equation;
    }

    public void setInitialValue(int initialValue) {
        this.initialValue = initialValue;
    }

    public void setIteratorStartsAt(int iteratorStartsAt) {
        IteratorStartsAt = iteratorStartsAt;
    }

    public void setMaxIteratorValue(int maxIteratorValue) {
        MaxIteratorValue = maxIteratorValue;
    }

    public void setIteratorName(String iteratorName) {
        this.iteratorName = iteratorName;
    }
}
