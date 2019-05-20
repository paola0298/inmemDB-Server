package Structures.BinaryTree;

public class BinaryTree<K extends Comparable<? super K>, V> {

    private int size;
    private Node<K, V> root;

    public BinaryTree() {
        this.size = 0;
        this.root = null;
    }

    public void insert(K key, V value) {
        this.root = insert(key, value, this.root);
    }

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

    public V search(K key) {
        return search(key, this.root);
    }

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

    public void remove(K key) {
        this.root = remove(key, this.root);

    }

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
