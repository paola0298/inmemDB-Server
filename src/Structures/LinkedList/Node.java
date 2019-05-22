package Structures.LinkedList;

public class Node<T> {
    private T value;
    private Node<T> next;

    /**
     * Constructor del nodo de la lista enlazada
     * @param value
     */
    public Node(T value) {
        this.value = value;
        this.next = null;
    }

    /**
     * Constructor del nodo de la lista enlazada
     */
    public Node() {
        this.value = null;
        this.next = null;
    }

    /**
     * Constructor del nodo de la lista enlazada
     * @param element
     * @param next
     */
    public Node(T element, Node next) {
        this.value = element;
        this.next = next;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Node<T> getNext() {
        return next;
    }

    public void setNext(Node<T> next) {
        this.next = next;
    }
}
