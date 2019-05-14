package Structures;

public class AATree<K extends Comparable<K>,V> {

    private AANode<K, V> root;
    private int size;

    public AATree(){
        this.size = 0;
        this.root = null;
    }

    public void insert(K key, V value){
        this.root = this.insert(root, key, value);
    }

    private AANode<K, V> insert(AANode<K, V> current, K key, V value){
        if (current == null){
            return new AANode<>(key, value);
        }

        if (key.compareTo(current.getKey()) < 0){
            current.setLeft(insert(current.getLeft(), key, value));
        } else if (key.compareTo(current.getKey()) > 0){
            current.setRight(insert(current.getRight(), key, value));
        }

        //TODO hacer skew y split

        current = skew(current);
        current = split(current);

        return current;
    }

    private AANode<K,V> split(AANode<K,V> current) {
        return current;
    }

    private AANode<K,V> skew(AANode<K,V> current) {
        return current;

    }


}
