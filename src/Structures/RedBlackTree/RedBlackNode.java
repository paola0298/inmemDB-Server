package Structures.RedBlackTree;

public class RedBlackNode <K extends Comparable<K>, V> {


        /** Possible color for this node */
        public static final int BLACK = 0;
        /** Possible color for this node */
        public static final int RED = 1;
        // the key of each node
        public K key;
        public V value;

        /** Parent of node */
        RedBlackNode<K, V> parent;
        /** Left child */
        RedBlackNode<K, V> left;
        /** Right child */
        RedBlackNode<K, V> right;

        public int numLeft = 0;

        public int numRight = 0;

        public int color;


        RedBlackNode(){
            color = BLACK;
            numLeft = 0;
            numRight = 0;
            parent = null;
            left = null;
            right = null;
        }


        RedBlackNode(K key, V value){
            this();
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

    public RedBlackNode<K, V> getParent() {
        return parent;
    }

    public void setParent(RedBlackNode<K, V> parent) {
        this.parent = parent;
    }

    public RedBlackNode<K, V> getLeft() {
        return left;
    }

    public void setLeft(RedBlackNode<K, V> left) {
        this.left = left;
    }

    public RedBlackNode<K, V> getRight() {
        return right;
    }

    public void setRight(RedBlackNode<K, V> right) {
        this.right = right;
    }

    public int getNumLeft() {
        return numLeft;
    }

    public void setNumLeft(int numLeft) {
        this.numLeft = numLeft;
    }

    public int getNumRight() {
        return numRight;
    }

    public void setNumRight(int numRight) {
        this.numRight = numRight;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}

