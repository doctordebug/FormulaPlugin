package Models;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by olisa_000 on 26.05.17.
 */
public class Node<T> {

    private T value = null;
    private Set<Node<T>> children = new HashSet();
    private Node<T> parent = null;
    private Object annotation = null;

    public void setValue(T value) {
        this.value = value;
    }

    public void setChildren(Set<Node<T>> children) {
        this.children = children;
    }

    public Set<Node<T>> getChildren() {
        return children;
    }

    public T getNumberValue() {
        return value;
    }

    public void setParent(Node<T> parent) {
        this.parent = parent;
    }

    public Node<T> getParent() {
        return parent;
    }

    public void addChild(Node childNode) {
        this.children.add(childNode);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getNumberValue()).append(":\n").append("-");
        sb.append("L__>");
        for (Node c : children)
            sb.append(c.toString()).append(',');
        return sb.toString();
    }

    public boolean hasChildren() {
        return !children.isEmpty();
    }

    public Object getAnnotation() {
        return annotation;
    }

    public boolean hasAnnotation() {
        return annotation != null;
    }

    public void setAnnotation(Object annotation) {
        this.annotation = annotation;
    }

    public boolean isLeaf() {
        return children.size() == 0;
    }

    public T getValue() {
        return value;
    }


}
