package Analysis;

import soot.*;
import soot.jimple.AssignStmt;
import soot.jimple.Stmt;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.scalar.ForwardFlowAnalysis;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by olisa_000 on 19.05.17.
 */
public class ReachingDefinitions extends ForwardFlowAnalysis{

    private BriefUnitGraph g;
    private Map<Unit, Integer> labels ;
    private Set<Integer> labSet;
    private Map<Value, Set<Integer>> result = new HashMap<>();

    public ReachingDefinitions(BriefUnitGraph g){
        super(g);
        labelStatements(g);
        doAnalysis();
        for(Unit key : labels.keySet()){
            System.out.println(key +" < - > "+labels.get(key));
        }
        System.out.println("####################################");
        for(Value key : result.keySet()){
            System.out.println(key +" < - > "+result.get(key));
        }
    }

    private void labelStatements(BriefUnitGraph g) {
        int currentLabel = 0;
        Set<Unit> visited = new HashSet<>();
        Stack<Unit> toVisit = new Stack<>();
        toVisit.addAll(g.getHeads());
        labels = new HashMap<Unit, Integer>(g.getHeads().size());
        while(!toVisit.empty()) {
            Unit current = toVisit.pop();
            if(visited.contains(current)) continue;
            visited.add(current);
            toVisit.addAll(g.getSuccsOf(current));
            labels.put(current, ++currentLabel);

            for(Unit s : g.getSuccsOf(current)){
                if(!visited.contains(s)) {
                    toVisit.addAll(g.getSuccsOf(s));
                }
            }
        }
        labSet = labels.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toSet());
    }

    @Override
    protected void flowThrough(Object setIn, Object unit, Object setOut) {
        HashMap<Value, Set<Integer>> sMap = (HashMap<Value, Set<Integer>>) setIn;
        HashMap<Value, Set<Integer>> tMap = (HashMap<Value, Set<Integer>>) setOut;
        sMap.putAll(result);
        Unit u = (Unit) unit;
        if( !(u instanceof AssignStmt))return;
        Stmt s = (AssignStmt) u;
        int label = labels.get(u);
        tMap.clear();
        tMap.putAll(sMap);

        for(ValueBox st : s.getDefBoxes()){
            Set<Integer> current = (sMap.get(st.getValue()) == null) ? new HashSet<>() : sMap.get(st.getValue());
            current.add(label);
            tMap.put(st.getValue(),current);
        }
        result = tMap;
        setOut = tMap;
    }

    @Override
    protected Object entryInitialFlow() {
        return new HashMap<Value, Set<Integer>>();
    }

    @Override
    protected Object newInitialFlow() {
        return new HashMap<Value, Set<Integer>>();
    }

    @Override
    protected void merge(Object setIn1, Object setIn2, Object setOut) {
        HashMap<Value, Set<Integer>> sMap1 = (HashMap<Value, Set<Integer>>) setIn1;
        HashMap<Value, Set<Integer>> sMap2 = (HashMap<Value, Set<Integer>>) setIn2;
        HashMap<Value, Set<Integer>> tMap = (HashMap<Value, Set<Integer>>) setOut;
        tMap.clear();
        tMap.putAll(sMap1);
        for(Value key : sMap2.keySet()){
            Set<Integer> current = tMap.get(key) == null ? new HashSet<>() : tMap.get(key);
            current.addAll(sMap2.get(key));
            tMap.put(key, current);
        }
        result = tMap;
        setOut = tMap;
    }

    @Override
    protected void copy(Object src, Object target) {
        HashMap<Value, Set<Integer>> sMap = (HashMap<Value, Set<Integer>>) src;
        HashMap<Value, Set<Integer>> tMap = (HashMap<Value, Set<Integer>>) target;
        tMap.clear();
        tMap.putAll(sMap);
        result = tMap;
        target = tMap;
    }
}
