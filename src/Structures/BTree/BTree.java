package Structures.BTree;

import Structures.AbstractTree.AbstractTree;
import Structures.AbstractTree.StructureType;

public class BTree<K extends Comparable<K>, V> extends AbstractTree<K, V> {
        // max children per B-tree node = M-1
        // (must be even and greater than 2)
        private static final int M = 4;

        private Node root;       // root of the B-tree
        private int height = 0;      // height of the B-tree
        private int n;           // number of key-value pairs in the B-tree

        private static final class Node {
            private int m;                             // number of children
            private Entry[] children = new Entry[M];   // the array of children
            private boolean isLeaf;

            // create a node with k children
            private Node(int k) {
                isLeaf = true;
                m = k;

            }

            public void setLeaf(boolean b) {
                isLeaf = b;
            }
        }

        // internal nodes: only use key and next
        // external nodes: only use key and value
        private static class Entry {
            private Comparable key;
            private final Object val;
            private Node next;     // helper field to iterate over array entries
            public Entry(Comparable key, Object val, Node next) {
                this.key  = key;
                this.val  = val;
                this.next = next;
            }

            public Comparable getKey() {
                return key;
            }

        //TODO delete method

            public void setKey(Comparable key) {
                this.key = key;
            }

            public Object getVal() {
                return val;
            }

            public Node getNext() {
                return next;
            }

            public void setNext(Node next) {
                this.next = next;
            }
        }


        /**
         * Initializes an empty B-tree.
         */
        public BTree() {
            root = new Node(0);
        }

        /**
         * Returns true if this symbol table is empty.
         * @return {@code true} if this symbol table is empty; {@code false} otherwise
         */
        public boolean isEmpty() {
            return size() == 0;
        }

        /**
         * Returns the number of key-value pairs in this symbol table.
         * @return the number of key-value pairs in this symbol table
         */
        public int size() {
            return n;
        }

        /**
         * Returns the height of this B-tree (for debugging).
         *
         * @return the height of this B-tree
         */
        public int height() {
            return height;
        }


        /**
         * Returns the value associated with the given key.
         *
         * @param  key the key
         * @return the value associated with the given key if the key is in the symbol table
         *         and {@code null} if the key is not in the symbol table
         * @throws IllegalArgumentException if {@code key} is {@code null}
         */
        public V search(K key) {
            if (key == null) throw new IllegalArgumentException("argument to get() is null");
            return search(root, key, height);
        }

        private V search(Node x, K key, int ht) {
            Entry[] children = x.children;

            // external node
            if (ht == 0) {
                for (int j = 0; j < x.m; j++) {
                    if (eq(key, children[j].key)) return  (V) children[j].getVal();
                }
            }

            // internal node
            else {
                for (int f = 0; f+1 < x.m; f++)
                    if (f + 1 == x.m || less(key, children[f + 1].key)) {
                        return search(children[f].next, key, ht - 1);
                    }
            }
            return null;
        }


        /**
         * Inserts the key-value pair into the symbol table, overwriting the old value
         * with the new value if the key is already in the symbol table.
         * If the value is {@code null}, this effectively deletes the key from the symbol table.
         *
         * @param  key the key
         * @param  val the value
         * @throws IllegalArgumentException if {@code key} is {@code null}
         */
        public void add(K key, V val) {
            if (key == null) throw new IllegalArgumentException("argument key to add() is null");
            Node u = insert(root, key, val, height);
            n++;
            if (u == null) return;

            // need to split root
            Node t = new Node(2);
            t.children[0] = new Entry(root.children[0].key, null, root);
            t.children[1] = new Entry(u.children[0].key, null, u);
            t.setLeaf(false);
            root = t;
            height++;
        }

        private Node insert(Node h, K key, V val, int ht) {
            int j;
            Entry t = new Entry(key, val, null);

            // external node
            if (ht == 0) {
                for (j = 0; j < h.m; j++) {
                    if (less(key, h.children[j].key)) break;
                }
            }

            // internal node
            else {
                for (j = 0; j < h.m; j++) {
                    if ((j+1 == h.m) || less(key, h.children[j+1].key)) {
                        Node u = insert(h.children[j++].next, key, val, ht-1);
                        if (u == null) return null;
                        t.key = u.children[0].key;
                        t.next = u;
                        break;
                    }
                }
            }

            for (int i = h.m; i > j; i--)
                h.children[i] = h.children[i-1];
            h.children[j] = t;
            h.m++;
            if (h.m < M) return null;
            else         return split(h);
        }

        // split node in half
        private Node split(Node h) {
            Node t = new Node(M/2);
            h.m = M/2;
            for (int j = 0; j < M/2; j++)
                t.children[j] = h.children[M/2+j];
            return t;
        }

        /**
         * Returns a string representation of this B-tree (for debugging).
         *
         * @return a string representation of this B-tree.
         */
        public String toString() {
            return toString(root, height, "") + "\n";
        }

        private String toString(Node h, int ht, String indent) {
            StringBuilder s = new StringBuilder();
            Entry[] children = h.children;

            if (ht == 0) {
                for (int j = 0; j < h.m; j++) {
                    s.append(indent + children[j].key + " " + children[j].val + "\n");
                }
            }
            else {
                for (int j = 0; j < h.m; j++) {
                    if (j > 0) s.append(indent + "(" + children[j].key + ")\n");
                    s.append(toString(children[j].next, ht-1, indent + "     "));
                }
            }
            return s.toString();
        }


        // comparison functions - make Comparable instead of K to avoid casts
        private boolean less(Comparable k1, Comparable k2) {
            return k1.compareTo(k2) < 0;
        }

        private boolean eq(Comparable k1, Comparable k2) {
            return k1.compareTo(k2) == 0;
        }



        public void remove(K key){
            remove(key,this.root, height);
        }

        @Override
        public void show() {
            System.out.println("toString");
            toString();
        }

        @Override
        public StructureType getType() {
            return StructureType.B;
        }

        private void remove(K key, Node root, int ht){
            Entry[] children = root.children;

            // external node
            if (ht == 1) {
                for (int j = 0; j < root.m; j++) {
                    if (eq(key, children[j].key)) {
                        removeAux(children[j],children,j,1);

                    }
                }
            }

            // internal node
            else {
                for (int j = 0; j < root.m; j++) {
                    if (j+1 == root.m || less(key, children[j+1].key)) {
                        //return search(children[j].next, key, ht-1);
                    }
                }
            }


        }
        public void removeAux(Object data, Entry[] children, int j,int acc){
            if (acc > 0){
                children[j] = children[j+1];
            }
//            else if(){
//
//            }


        }


        /**
         * Unit tests the {@code BTree} data type.
         *
         * @param args the command-line arguments
         */
        public static void main(String[] args) {
            BTree<Integer, String> prueba = new BTree();
            prueba.add(1, "Primero");
            prueba.add(2, "segundo");
            prueba.add(3, "Tercero");
            prueba.add(4, "Cuarto");
            prueba.add(5, "Quinto");
            System.out.println(prueba.search(4));
            prueba.remove(3);
            System.out.println(prueba.search(3));


        }

    }