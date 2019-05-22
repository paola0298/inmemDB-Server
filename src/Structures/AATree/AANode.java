package Structures.AATree;

/** Class AANode **/
public class AANode <K extends Comparable<? super K>, V>
{
    private AANode<K,V> left, right;
    private int level;
    private K key;
    private V value;
    /**
     * El constructor del nodo
     * @param key
     * @param value
     */
    /** Constructor **/
    public AANode(K key, V value) {
        this(key, value, null, null);
    }
    /**
     * @param key
     * @param value
     * @param left Este es un nodo, y se refiere al nodo izquierdo del nodo por crear
     * @param right Este es un nodo, y se refiere al nodo derecho del nodo por crear
     */
    /** Constructor **/
    public AANode(K key, V value, AANode left, AANode right)
    {
        this.key = key;
        this.value = value;
        this.left = left;
        this.right = right;
        this.level = 1;
    }

    public AANode<K, V> getLeft() {
        return left;
    }

    public void setLeft(AANode<K, V> left) {
        this.left = left;
    }

    public AANode<K, V> getRight() {
        return right;
    }

    public void setRight(AANode<K, V> right) {
        this.right = right;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
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


}
