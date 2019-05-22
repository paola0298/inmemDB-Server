package Structures.AVLTree;

public class AVLNode<K extends Comparable<? super K>, V> {
    private K key;

    private V value;
    private int height;
    private AVLNode<K, V> left, right;

    /**
     * Constructor del nodo
     * @param key
     * @param value
     */
    public AVLNode(K key, V value)
    {
        this.key = key;
        this.value = value;
        this.height = 1;
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

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public AVLNode<K, V> getLeft() {
        return left;
    }

    public void setLeft(AVLNode<K, V> left) {
        this.left = left;
    }

    public AVLNode<K, V> getRight() {
        return right;
    }

    public void setRight(AVLNode<K, V> right) {
        this.right = right;
    }
}
