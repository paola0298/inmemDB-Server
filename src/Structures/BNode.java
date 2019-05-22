package Structures;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
public class BNode <K extends Comparable<K>, V> {

    /*
     * author : Karan Chauhan
     */

        /** The list of key values pairs in the node. */
        private List<Key> keys;

        /** The children of this node. Set only for internal Nodes */
        private List<BNode> children;

        /** The previous element in the linked list. Set only for external Nodes. */
        private BNode prev;

        /** The next element in the linked list. Set only for external Nodes. */
        private BNode next;

        /** The parent of this node. NULL for root */
        private BNode parent;

        /**
         * Instantiates a new node.
         */
        public BNode() {
            this.keys = new ArrayList<>();
            this.children = new ArrayList<>();
            this.prev = null;
            this.next = null;
        }

        /**
         * Gets the keys.
         *
         * @return the keys
         */
        public List<Key> getKeys() {
            return keys;
        }

        /**
         * Sets the keys.
         *
         * @param keys
         *            the new keys
         */
        public void setKeys(List<Key> keys) {
            Iterator<Key> iter = keys.iterator();
            while (iter.hasNext()) {
                this.keys.add(iter.next());
            }
        }

        /**
         * Gets the children.
         *
         * @return the children
         */
        public List<BNode> getChildren() {
            return children;
        }

        /**
         * Sets the children.
         *
         * @param children
         *            the new children
         */
        public void setChildren(List<BNode> children) {
            this.children = children;
        }

        /**
         * Gets the prev.
         *
         * @return the prev
         */
        public BNode getPrev() {
            return prev;
        }

        /**
         * Sets the prev.
         *
         * @param prev
         *            the new prev
         */
        public void setPrev(BNode prev) {
            this.prev = prev;
        }

        /**
         * Gets the next.
         *
         * @return the next
         */
        public BNode getNext() {
            return next;
        }

        /**
         * Sets the next.
         *
         * @param next
         *            the new next
         */
        public void setNext(BNode next) {
            this.next = next;
        }

        /**
         * Gets the parent.
         *
         * @return the parent
         */
        public BNode getParent() {
            return parent;
        }

        /**
         * Sets the parent.
         *
         * @param parent
         *            the new parent
         */
        public void setParent(BNode parent) {
            this.parent = parent;

        }

        @Override
        public String toString() {
            return "Keys =" + keys.toString();
        }

    }
