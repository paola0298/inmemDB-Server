package Structures;
import java.util.Scanner;

//public class SplayNode<K extends Comparable<K>, V> {
//    private SplayNode<K,V> left;
//    private SplayNode<K, V> right;
//    private SplayNode<K, V> parent;
//    private V element;
//
//    public SplayNode<K, V> getLeft() {
//        return left;
//    }
//
//    public void setLeft(SplayNode<K, V> left) {
//        this.left = left;
//    }
//
//    public SplayNode<K, V> getRight() {
//        return right;
//    }
//
//    public void setRight(SplayNode<K, V> right) {
//        this.right = right;
//    }
//
//    public SplayNode<K, V> getParent() {
//        return parent;
//    }
//
//    public void setParent(SplayNode<K, V> parent) {
//        this.parent = parent;
//    }
//
//
//    public V getElement() {
//        return element;
//    }
//
//    public void setElement(V element) {
//        this.element = element;
//    }
//
//    public SplayNode(SplayNode<K, V> left, SplayNode<K, V> right, SplayNode<K, V> parent, V element) {
//        this.left = left;
//        this.right = right;
//        this.parent = parent;
//        this.element = element;
//    }
public class SplayNode<K extends Comparable<K>, V>{

private SplayNode<K,V> parent;
private SplayNode<K,V> leftChild;
private SplayNode<K,V> rightChild;
private Entry entry;

public SplayNode(Entry entry) {
        this.entry = entry;
        }

public SplayNode<K,V> getParent() {
        return parent;
        }

public void setParent(SplayNode<K,V> parent) {
        this.parent = parent;
        }

public SplayNode<K,V> getLeftChild() {
        return leftChild;
        }

public void setLeftChild(SplayNode<K,V> leftChild) {
        this.leftChild = leftChild;
        }

public SplayNode<K,V> getRightChild() {
        return rightChild;
        }

public void setRightChild(SplayNode<K,V> rightChild) {
        this.rightChild = rightChild;
        }

public Entry getEntry() {
        return entry;
        }

public void setEntry(Entry entry) {
        this.entry = entry;
        }



//@Override
//public int compareTo(SplayNode that) {
//        return this.entry.compareTo(that.entry);
//
//        }

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
        SplayNode<K,V> parent = this.getParent();
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
        SplayNode<K,V> parent = this.getParent();
        if (parent != null) {
        hasGrandParent = parent.getParent() != null ? true : false;
        }
        return hasGrandParent;
        }

@Override
public String toString() {
        return this.entry.toString();
        }


//    public int compareTo(SplayNode o) {
//        return 0;
//    }
}


