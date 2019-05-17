package Structures;

public class SplayNode implements Comparable<SplayNode> {
        private SplayNode parent;
        private SplayNode leftChild;
        private SplayNode rightChild;
        private Entry entry;

        public SplayNode(Entry entry) {
                this.entry = entry;
        }

        public SplayNode getParent() {
                return parent;
        }

        public void setParent(SplayNode parent) {
                this.parent = parent;
        }

        public SplayNode getLeftChild() {
                return leftChild;
        }

        public void setLeftChild(SplayNode leftChild) {
                this.leftChild = leftChild;
        }

        public SplayNode getRightChild() {
                return rightChild;
        }

        public void setRightChild(SplayNode rightChild) {
                this.rightChild = rightChild;
        }

        public Entry getEntry() {
                return entry;
        }

        public void setEntry(Entry entry) {
                this.entry = entry;
        }

        @Override
        public int compareTo(SplayNode that) {
                return this.entry.compareTo(that.entry);

        }

        public void inOrder() {
                if (this.getLeftChild() != null) {
                        this.getLeftChild().inOrder();
                }
                this.visit();
                if (this.getRightChild() != null) {
                        this.getRightChild().inOrder();
                }
        }


        private void visit() {
                System.out.println(this.entry);
        }

        public WhichChild findWhichChild() {
                SplayNode parent = this.getParent();
                if (parent == null) {
                        return WhichChild.NOT_A_CHILD;
                }

                if (parent.getLeftChild()!=null &&  parent.getLeftChild().equals(this)) {
                        return WhichChild.LEFTCHILD;
                } else {
                        return WhichChild.RIGHTCHILD;
                }

        }

        public boolean hasGrandParent() {
                boolean hasGrandParent = false;
                SplayNode parent = this.getParent();
                if (parent != null) {
                        hasGrandParent = parent.getParent() != null ? true : false;
                }
                return hasGrandParent;
        }

        @Override
        public String toString() {
                return this.entry.toString();
        }


}

//public class SplayNode<K extends Comparable<K>, V>{
//
//private SplayNode<K,V> parent;
//private SplayNode<K,V> leftChild;
//private SplayNode<K,V> rightChild;
//private Entry entry;
//
//public SplayNode(Entry entry) {
//        this.entry = entry;
//        }
//
//public SplayNode<K,V> getParent() {
//        return parent;
//        }
//
//public void setParent(SplayNode<K,V> parent) {
//        this.parent = parent;
//        }
//
//public SplayNode<K,V> getLeftChild() {
//        return leftChild;
//        }
//
//public void setLeftChild(SplayNode<K,V> leftChild) {
//        this.leftChild = leftChild;
//        }
//
//public SplayNode<K,V> getRightChild() {
//        return rightChild;
//        }
//
//public void setRightChild(SplayNode<K,V> rightChild) {
//        this.rightChild = rightChild;
//        }
//
//public Entry getEntry() {
//        return entry;
//        }
//
//public void setEntry(Entry entry) {
//        this.entry = entry;
//        }
//
//
//
////@Override
////public int compareTo(SplayNode that) {
////        return this.entry.compareTo(that.entry);
////
////        }
//
//public void inOrder() {
//        if (this.getLeftChild() != null) {
//        this.getLeftChild().inOrder();
//        }
//        this.visit();
//        if (this.getRightChild() != null) {
//        this.getRightChild().inOrder();
//        }
//        }
//
//
//private void visit() {
//        System.out.println(this.entry);
//        }
//
//public WhichChild findWhichChild() {
//        SplayNode<K,V> parent = this.getParent();
//        if (parent == null) {
//        return WhichChild.NOT_A_CHILD;
//        }
//
//        if (parent.getLeftChild()!=null &&  parent.getLeftChild().equals(this)) {
//        return WhichChild.LEFTCHILD;
//        } else {
//        return WhichChild.RIGHTCHILD;
//        }
//
//        }
//
//public boolean hasGrandParent() {
//        boolean hasGrandParent = false;
//        SplayNode<K,V> parent = this.getParent();
//        if (parent != null) {
//        hasGrandParent = parent.getParent() != null ? true : false;
//        }
//        return hasGrandParent;
//        }
//
//@Override
//public String toString() {
//        return this.entry.toString();
//        }
//
//
////    public int compareTo(SplayNode o) {
////        return 0;
////    }


