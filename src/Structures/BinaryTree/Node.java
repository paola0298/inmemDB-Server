package Structures.BinaryTree;

public class Node<K extends Comparable<? super K>, V> {

    private K key;
    private V value;

    private Node<K, V> left;
    private Node<K, V> right;

    /**
     * Constructor del nodo
     * @param key
     * @param value
     */
    public Node(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public Node<K, V> getLeft() {
        return left;
    }

    public void setLeft(Node<K, V> left) {
        this.left = left;
    }

    public Node<K, V> getRight() {
        return right;
    }

    public void setRight(Node<K, V> right) {
        this.right = right;
    }

    /**
     * @return valor boolean sobre si el nodo es una hoja del árbol o no
     */
    public boolean isLeaf() {
        return (left == null) && (right == null);
    }

}
