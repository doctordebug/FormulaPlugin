package Analysis;

import Utils.Log;
import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.ValueBox;
import soot.jimple.Constant;
import soot.jimple.DefinitionStmt;
import soot.jimple.JimpleBody;
import soot.jimple.Stmt;
import soot.toolkits.graph.CompleteUnitGraph;
import soot.toolkits.scalar.LocalDefs;
import soot.toolkits.scalar.SimpleLocalDefs;
import soot.util.Chain;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by olisa_000 on 19.05.17.
 */
public class ConstantPropagation extends BodyTransformer {

    private static ConstantPropagation instance;

    private ConstantPropagation() {
    }

    public static ConstantPropagation getInstance() {
        return instance == null ? instance = new ConstantPropagation() : instance;
    }

    //https://courses.cs.washington.edu/courses/cse501/01wi/project/sable-thesis.pdf
    @Override
    protected void internalTransform(Body b, String phaseName, Map options) {
        Log.i("[" + b.getMethod().getName() + "] Propagating constants...");
        JimpleBody body = (JimpleBody) b;
        Chain units = body.getUnits();
        CompleteUnitGraph stmtGraph = new CompleteUnitGraph(body);

        LocalDefs localDefs = new SimpleLocalDefs(stmtGraph);
        Iterator stmtIt = units.iterator();

        while (stmtIt.hasNext()) {
            Stmt stmt = (Stmt) stmtIt.next();
            Iterator useBoxIt = stmt.getUseBoxes().iterator();

            while (useBoxIt.hasNext()) {
                ValueBox useBox = (ValueBox) useBoxIt.next();

                if (useBox.getValue() instanceof Local) {
                    Local l = (Local) useBox.getValue();
                    List defsOfUse = localDefs.getDefsOfAt(l, stmt);

                    if (defsOfUse.size() == 1) {
                        DefinitionStmt def = (DefinitionStmt)
                                defsOfUse.get(0);
                        if (def.getRightOp() instanceof Constant) {
                            if (useBox.canContainValue(def.getRightOp()))
                                useBox.setValue(def.getRightOp());
                        }
                    }
                }
            }
        }
    }
}
