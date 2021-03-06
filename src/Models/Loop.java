package Models;

import Analysis.GuaranteedDefsAnalysis;
import Analysis.ReachingDefinitions;
import org.jetbrains.annotations.NotNull;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.Stmt;
import soot.jimple.internal.JIfStmt;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by olisa_000 on 19.05.17.
 */
public class Loop {

    private boolean initilized;
    private JIfStmt condition;

    private Value indexVariable;

    private Collection<Value> accumulationVariables;

    private Collection<Value> tempVariables;

    private Collection<Value> coIndexVariables;

    private Collection<Value> useless;
    //has order!
    private List<AssignStmt> loopAssignStatements;

    private Value loopEnd;

    private Collection<Stmt> loopStatements;

    private Loop() {
        accumulationVariables = new HashSet<>();
        tempVariables = new HashSet<>();
        coIndexVariables = new HashSet<>();
        useless = new HashSet<>();
        loopStatements = new ArrayList<>();
        initilized = false;
    }

    public List<Formula> findEquations() {
        List<Formula> result = new ArrayList<>();
        if(!initilized) return result;
        for (Value accu: accumulationVariables) {
            Formula f = new Formula();
            f.accuVariable = accu;
            f.iterator = indexVariable;
            f.maxIteratorValue = loopEnd;
            f.build(loopAssignStatements);
            result.add(f);
        }
        return result;
    }

    public static Loop buildLoop(soot.jimple.toolkits.annotation.logic.Loop loop, ReachingDefinitions rd, GuaranteedDefsAnalysis gds) {
        Loop result = new Loop();
        List<Stmt> statements = loop.getLoopStatements();
        result.loopStatements = statements;
        JIfStmt cond = findCondition(statements);
        result.setCondition(cond);
        List<Value> definedBeforeLoop = (List<Value>) gds.getUnitToGenerateSet().get(loop.getHead()).toList().stream().map(x -> (Value) x).collect(Collectors.toList());
        result.setIndexVariable(findIndexVariable(cond));
        result.setLoopEnd(findLoopEnd(cond));
        result.setLoopAssignStatements(findLoopAssignStatements(statements));
        //setAccu
        Collection<Value> accuVars = findAccuVariables(definedBeforeLoop, loop.getLoopStatements());
        accuVars.remove(result.getIndexVariable());
        result.setAccumulationVariables(accuVars);
        //setCoIndex
        Collection<Value> coIndex = findCoIndex(definedBeforeLoop, loop.getLoopStatements(), result.getIndexVariable());
        result.setCoIndexVariables(coIndex);
        //setTempVars
        result.setTempVariables(findTempVariables(definedBeforeLoop, loop.getLoopStatements()));
        result.setInitilized(true);
        return result;
    }

    private static List<Value> findTempVariables(List<Value> definedBeforeLoop, List<Stmt> loopStatements) {

        List<AssignStmt> assignedInLoop = findLoopAssignStatements(loopStatements);
        //defines itself new in loop
        List<Value> valuesAssignedInLoop = assignedInLoop.stream().map(x -> x.getLeftOp()).filter(x -> !definedBeforeLoop.contains(x)).collect(Collectors.toList());
        return valuesAssignedInLoop;
    }

    private static Collection<Value> findCoIndex(List<Value> definedBeforeLoop, List<Stmt> loopStatements, Value indexVariable) {
        List<AssignStmt> assignedInLoop = findLoopAssignStatements(loopStatements);
        //defines itself new in loop
        List<Value> valuesAssignedInLoop = assignedInLoop.stream().map(x -> x.getLeftOp()).collect(Collectors.toList());
        //lives before loop AND uses inside loop
        List<Value> possibleCoIndex =  valuesAssignedInLoop.stream().filter(x -> definedBeforeLoop.contains(x)).collect(Collectors.toList());
        //HasPath to index
        List<ValueTree> varTrees = possibleCoIndex.stream().map(x -> ValueTree.LoadTree(x, assignedInLoop)).collect(Collectors.toList());
        HashSet<Value> hasPathToIndex = new HashSet<>();
        for(ValueTree t : varTrees){
            for(Value v : possibleCoIndex)
                if (t.pathFromTo(v,indexVariable))
                    hasPathToIndex.add(v);
        }
        return hasPathToIndex;
    }

    //TODO: BUGFIX!
    private static Collection<Value> findAccuVariables(List<Value> definedBeforeLoop, List<Stmt> loopStatements) {
        List<AssignStmt> assignedInLoop = findLoopAssignStatements(loopStatements);
        //defines itself new in loop
        List<Value> valuesAssignedInLoop = assignedInLoop.stream().map(x -> x.getLeftOp()).collect(Collectors.toList());
        //lives before loop AND uses inside loop
        List<Value> possibleAccuVars =  valuesAssignedInLoop.stream().filter(x -> definedBeforeLoop.contains(x)).collect(Collectors.toList());
        //uses itself to get defines new
        List<ValueTree> varTrees = possibleAccuVars.stream().map(x -> ValueTree.LoadTree(x, assignedInLoop)).collect(Collectors.toList());
        HashSet<Value> refersItself = new HashSet<>();
        for(ValueTree t : varTrees){
            for(Value v : possibleAccuVars)
                if (t.refersItself(v))
                    refersItself.add(v);
        }
        //defines itself new in loop AND lives before loop AND uses inside loop AND uses itself to get defined new
        return refersItself;
    }




    private static List<AssignStmt> findLoopAssignStatements(List<Stmt> statements) {
        return statements.
                stream().
                filter(x -> x instanceof AssignStmt).
                map(x -> (AssignStmt) x).
                collect(Collectors.toList());
    }

    private static Value findIndexVariable(JIfStmt cond) {
        return cond.
                getCondition().
                getUseBoxes().
                get(0).
                getValue();
    }

    private static Value findLoopEnd(JIfStmt cond) {
        return cond.
                getCondition().
                getUseBoxes().
                get(1).
                getValue();
    }

    @NotNull
    private static JIfStmt findCondition(List<Stmt> statements) {
        return (JIfStmt) statements
                .stream()
                .filter(s -> s instanceof JIfStmt)
                .findFirst()
                .get();
    }

    public void setCondition(JIfStmt condition) {
        this.condition = condition;
    }

    public void setIndexVariable(Value indexVariable) {
        this.indexVariable = indexVariable;
    }

    public Collection<Value> getAccumulationVariables() {
        return accumulationVariables;
    }

    public Collection<Value> getCoIndexVariables() {
        return coIndexVariables;
    }

    public Collection<Value> getTempVariables() {
        return tempVariables;
    }

    @Override
    public String toString() {
        String sb = "condition:\n" +
                condition +
                "\nindexVariable:\n" +
                indexVariable +
                "\naccumulationVariables:\n" +
                accumulationVariables +
                "\ntempVariables:\n" +
                tempVariables +
                "\ncoIndexVariables:\n" +
                coIndexVariables +
                "\nuseless:\n" +
                useless +
                "\nloopEnd:\n" +
                loopEnd +
                "\nloopStatements:\n" +
                loopStatements;
        return sb;
    }

    public void setLoopEnd(Value loopEnd) {
        this.loopEnd = loopEnd;
    }

    public void setAccumulationVariables(Collection<Value> accumulationVariables) {
        this.accumulationVariables = accumulationVariables;
    }

    public Value getIndexVariable() {
        return indexVariable;
    }

    public void setLoopAssignStatements(List<AssignStmt> loopAssignStatements) {
        this.loopAssignStatements = loopAssignStatements;
    }

    public List<AssignStmt> getLoopAssignStatements() {
        return loopAssignStatements;
    }

    public void setCoIndexVariables(Collection<Value> coIndex) {
        this.coIndexVariables = coIndex;
    }


    public void setTempVariables(Collection<Value> tempVariables) {
        this.tempVariables = tempVariables;
    }


    public void setInitilized(boolean initilized) {
        this.initilized = initilized;
    }

    public JIfStmt getCondition() {
        return condition;
    }

    public Value getLoopEnd() {
        return loopEnd;
    }
}
