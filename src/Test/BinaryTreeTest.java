package Test;

import Structures.AbstractTree.StructureType;

public class BinaryTreeTest<K extends Comparable<? super K>, V> extends GeneralStructure<K, V> {

    @Override
    public void add(K key, V value) {
        System.out.println("Añadir " + key);
    }

    @Override
    public V search(K key) {
        System.out.println("Buscar " + key);
        return null;
    }

    @Override
    public void remove(K key) {
        System.out.println("Eliminar " + key);
    }

    @Override
    public StructureType getType() {
        return StructureType.BINARY;
    }

    public static void main(String[] args) {
        GeneralStructure<Integer, String> structure = new BinaryTreeTest<>();

        structure.add(5, "Hola");

        System.out.println(structure.getType());

    }
}
