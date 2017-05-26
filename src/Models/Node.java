package Models;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by olisa_000 on 26.05.17.
 */
public class Node<T> {

    private T value = null;
    private Set<T> chiildren = new HashSet<T>();
    private Node<T> parent = null;

    public void setValue(T value) {
        this.value = value;
    }

    public void setChiildren(Set<T> chiildren) {
        this.chiildren = chiildren;
    }

    public Set<T> getChiildren() {
        return chiildren;
    }

    public T getValue() {
        return value;
    }

    public void setParent(Node<T> parent) {
        this.parent = parent;
    }

    public Node<T> getParent() {
        return parent;
    }
}
