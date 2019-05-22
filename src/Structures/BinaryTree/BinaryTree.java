package Structures.BinaryTree;

public class BinaryTree<K extends Comparable<? super K>, V> {

    private int size;
    private Node<K, V> root;

    /**
     * Constructor del árbol Binary Tree
     */
    public BinaryTree() {
        this.size = 0;
        this.root = null;
    }

    /**
     * Llamada del método que se encarga de hacer inserciones en el árbol
     * @param key
     * @param value
     */
    public void insert(K key, V value) {
        this.root = insert(key, value, this.root);
    }

    /**
     * Este método es el que se encarga de realizar la inserción en el árbol
     * @param key
     * @param value
     * @param current
     * @return la raíz con el valor deseado ya agregado
     */
    private Node<K, V> insert(K key, V value, Node<K, V> current) {
        if (current == null) {
            this.size++;
            return new Node<>(key, value);
        } else if (current.getKey().compareTo(key) < 0) {
            current.setRight(insert(key, value, current.getRight()));
        } else if (current.getKey().compareTo(key) > 0) {
            current.setLeft(insert(key, value, current.getLeft()));
        }
        return current;
    }

    /**
     * Método que se encarga de llamar al método que realiza la búsqueda
     * @param key
     * @return el value del nodo buscado
     */
    public V search(K key) {
        return search(key, this.root);
    }

    /**
     * Método que se encarga de realizar la búsqueda
     * @param key
     * @param current
     * @return el nodo buscado
     */
    private V search(K key, Node<K, V> current) {
        if (current == null) {
            System.out.println("Node " + key + " not found!");
            return null;
        } else if (current.getKey().compareTo(key) < 0) {
            return search(key, current.getRight());
        } else if (current.getKey().compareTo(key) > 0) {
            return search(key, current.getLeft());
        }
        System.out.println("Node " + current.getValue() + " found!");
        return current.getValue();
    }

    /**
     * Método que se encarga de llamar al método que va a eliminar un valor específico
     * @param key
     */
    public void remove(K key) {
        this.root = remove(key, this.root);

    }

    /**
     * Método que se encarga de eliminar un valor específico
     * @param key
     * @param current
     * @return la raíz del nodo sin el valor especificado
     */
    private Node<K, V> remove(K key, Node<K, V> current) {
        if (current == null) {
            return null;
        } else if (current.getKey().compareTo(key) < 0) {
            current.setRight(remove(key, current.getRight()));
        } else if (current.getKey().compareTo(key) > 0) {
            current.setLeft(remove(key, current.getLeft()));
        } else {
            if (current.getLeft() == null) {
                return current.getRight();
            } else if (current.getRight() == null) {
                return current.getLeft();
            }
            Node<K, V> smallest = leftTraverse(current.getRight());
            K currentKey = current.getKey();
            current.setKey(smallest.getKey());
            current.setValue(smallest.getValue());
            smallest.setKey(currentKey);
            current.setRight(remove(currentKey, current.getRight()));
            this.size--;
        }
        return current;
    }


    private Node<K, V> leftTraverse(Node<K, V> current) {
        if (current.getLeft() != null) return leftTraverse(current.getLeft());
        else return current;
    }

    public int getSize() {
        return this.size;
    }

    public void inOrderTraverse() {
        inOrderTraverse(this.root);
    }

    private void inOrderTraverse(Node<K, V> current) {
        if (current != null) {
            inOrderTraverse(current.getLeft());
            System.out.println("[" + current.getValue() + "]");
            inOrderTraverse(current.getRight());
        }
    }

    public static void main(String[] args) {
        /*
                 15
               /   \
              4     23
               \    /
               7   19
         */


        BinaryTree<Integer, String> binaryTree = new BinaryTree<>();
        System.out.println("Insertando elementos");
        binaryTree.insert(15, "15");
        binaryTree.insert(4, "4");
        binaryTree.insert(7, "7");
        binaryTree.insert(23, "23");
        binaryTree.insert(19, "19");
        System.out.println("Elementos insertados");

        binaryTree.inOrderTraverse();

        /*
        4 7 15 19 23
         */

        binaryTree.remove(15);

        System.out.println("\n");
        binaryTree.inOrderTraverse();

        /*
        4 7 19 23
         */
    }

}
