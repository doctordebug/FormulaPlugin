package Models;

import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.Stmt;
import soot.jimple.internal.JIfStmt;

import java.util.*;

/**
 * Created by olisa_000 on 19.05.17.
 */
public class ForLoop {
    private JIfStmt condition;
    private List<Stmt> touchStmts = new ArrayList<>();
    private List<AssignStmt> assignStmts = new ArrayList<>();
    private List<Value> accuVariables = new ArrayList<>();
    //value and max posibla vlaue for iterator
    private Map<Value, Value> iterators = new HashMap<>(); // why a set? e.g. for(int i = 0, a = 7; i < 10 && a < 80; i++, a++)

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("For-loop:\n");
        sb.append(iterators != null? "Iterators: "+iterators+"\n" :"");
        sb.append(condition != null? "Condition: "+condition+"\n" :"");
        sb.append("Iterator is touched at: ").append(touchStmts).append("\n");
        sb.append("Possible assignments of accu: ").append(assignStmts).append("\n");
        sb.append("Possible accuVariables:").append(accuVariables).append("\n");
        return sb.toString();
    }

    public void setCondition(JIfStmt condition) {
        this.condition = condition;
    }

    public void setAssignStmts(List<AssignStmt> assignStmts) {
        this.assignStmts = assignStmts;
    }


    public void addIterator(Value iterator, Value maxValue) {
        this.iterators.put(iterator, maxValue);
    }

    public Map<Value, Value> getIterators() {
        return iterators;
    }


    public List<AssignStmt> getAssignStmts() {
        return assignStmts;
    }

    public <R> void setAccuVariables(List<Value> accuVariables) {
        this.accuVariables = accuVariables;
    }

    public List<Value> getAccuVariables() {
        return accuVariables;
    }

    public void setTouchStmts(List<Stmt> touchStmts) {
        this.touchStmts = touchStmts;
    }
}
