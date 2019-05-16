package Structures;

public class AANode<K extends Comparable<K>,V> {
    private AANode<K, V> left;
    private AANode<K, V> right;
    private K key;
    private V value;
    private int level;

    public AANode(K key, V value){
        this.key = key;
        this.value = value;
        this.left = this.right = null;
    }


    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public AANode<K, V> getRight() {
        return right;
    }

    public void setRight(AANode<K, V> right) {
        this.right = right;
    }

    public AANode<K, V> getLeft() {
        return left;
    }

    public void setLeft(AANode<K, V> left) {
        this.left = left;
    }
}
