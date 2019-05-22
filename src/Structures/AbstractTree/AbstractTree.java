package Structures.AbstractTree;

public abstract class AbstractTree<K extends Comparable<? super K>, V> {

    public abstract void add(K key, V value);

    public abstract V search(K key);

    public abstract void remove(K key);

    public abstract void show();

    public abstract StructureType getType();
}


