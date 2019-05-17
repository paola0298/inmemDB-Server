package Structures;

public class AVLNode<K extends Comparable<K>, V>{
    private K key;
    private int height;
    private AVLNode<K,V> left;
    private AVLNode<K, V> right;
    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
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

    public AVLNode(K key) {
        this.key = key;
        height = 1;
    }
}
