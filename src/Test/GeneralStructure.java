package Test;

import Structures.AbstractTree.StructureType;

public abstract class GeneralStructure<K extends Comparable<? super K>, V> {

    public abstract void add(K key, V value);

    public abstract V search(K key);

    public abstract void remove(K key);

    public abstract StructureType getType();
}

