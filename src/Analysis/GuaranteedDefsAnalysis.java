package Analysis;

import soot.toolkits.scalar.ForwardFlowAnalysis;
import soot.Local;
import soot.Unit;
import soot.ValueBox;
import soot.toolkits.graph.DominatorsFinder;
import soot.toolkits.graph.MHGDominatorsFinder;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.FlowSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
/**
 * Created by olisa_000 on 26.05.17.
 */
public class GuaranteedDefsAnalysis extends ForwardFlowAnalysis
{
    FlowSet emptySet = new ArraySparseSet();
    Map<Unit, FlowSet> unitToGenerateSet;

    public GuaranteedDefsAnalysis(UnitGraph graph)
    {
        super(graph);
        DominatorsFinder df = new MHGDominatorsFinder(graph);
        unitToGenerateSet = new HashMap<>(graph.size() * 2 + 1, 0.7f);

        // pre-compute generate sets
        for(Iterator unitIt = graph.iterator(); unitIt.hasNext();){
            Unit s = (Unit) unitIt.next();
            FlowSet genSet = emptySet.clone();

            for(Iterator domsIt = df.getDominators(s).iterator(); domsIt.hasNext();){
                Unit dom = (Unit) domsIt.next();
                for(Iterator boxIt = dom.getDefBoxes().iterator(); boxIt.hasNext();){
                    ValueBox box = (ValueBox) boxIt.next();
                    if(box.getValue() instanceof Local)
                        genSet.add(box.getValue(), genSet);
                }
            }

            unitToGenerateSet.put(s, genSet);
        }

        doAnalysis();
    }

    protected Object newInitialFlow()
    {
        return emptySet.clone();
    }

    protected Object entryInitialFlow()
    {
        return emptySet.clone();
    }

    protected void flowThrough(Object inValue, Object unit, Object outValue)
    {
        FlowSet
                in = (FlowSet) inValue,
                out = (FlowSet) outValue;

        in.union(unitToGenerateSet.get(unit), out);
    }

    protected void merge(Object in1, Object in2, Object out)
    {
        FlowSet
                inSet1 = (FlowSet) in1,
                inSet2 = (FlowSet) in2,
                outSet = (FlowSet) out;

        inSet1.intersection(inSet2, outSet);
    }

    protected void copy(Object source, Object dest)
    {
        FlowSet
                sourceSet = (FlowSet) source,
                destSet = (FlowSet) dest;

        sourceSet.copy(destSet);
    }

    public Map<Unit, FlowSet> getUnitToGenerateSet() {
        return unitToGenerateSet;
    }
}