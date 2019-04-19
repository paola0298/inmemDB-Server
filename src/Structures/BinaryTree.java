package Structures;

public class BinaryTree<T extends Comparable<? super T>> {

    private int size;
    private TreeNode<T> root;

    public BinaryTree() {
        this.size = 0;
        this.root = null;
    }

    public void add(T element) {
        this.root = add(element, this.root);
    }

    private TreeNode<T> add(T element, TreeNode<T> current) {
        if (current == null) {
            return new TreeNode<>(element);
        }
        if (element.compareTo(current.getValue()) < 0) {
            current.setLeft(add(element, current.getLeft()));
        } else if (element.compareTo(current.getValue()) > 0){
            current.setRight(add(element, current.getRight()));
        }
        return current;
    }

    public boolean contains(T element){
        return this.contains(element, this.root);
    }

    private boolean contains(T element, TreeNode<T> current){
        if (current == null){
            return false;
        }
        if (element.compareTo(current.getValue()) < 0){
            return contains(element, current.getLeft());
        } else if (element.compareTo(current.getValue()) > 0){
            return contains(element, current.getRight());
        } else {
            return true;
        }
    }

    public boolean remove2(T element) {
        return remove2(element, null, root);
    }

    private boolean remove2(T element, TreeNode<T> parent, TreeNode<T> current) {
        if (current == null) {
            return false;
        }

        if (element.compareTo(current.getValue()) < 0) {
            return remove2(element, current, current.getLeft());
        }

        if (element.compareTo(current.getValue()) > 0) {
            return remove2(element, current, current.getRight());
        } else {
            if (current.getRight() == null && current.getLeft() == null) {
                return removeTreeNodeCase1(current, parent);
            } else if (current.getLeft() != null && current.getRight() != null) {
                    // dos hijos
                return removeTreeNodeCase2(current, parent);

            } else if (current.getRight() != null && current.getLeft() == null) {
                // solo hijo derecho
                return removeTreeNodeCase3(current, parent, false);
            }

            else if (current.getLeft() != null && current.getRight() == null) {
                // solo hijo izquierdo
                return removeTreeNodeCase3(current, parent, true);
            }

            return false;
            }

        }

    private boolean removeTreeNodeCase3(TreeNode<T> current, TreeNode<T> parent, boolean flag){
        TreeNode<T> newChild = null;

        if (flag){
            newChild = current.getLeft();
        } else{
            newChild = current.getRight();
        }
        if (parent.getValue().compareTo(current.getValue())<0){
            parent.setRight(newChild);
        }else {
            parent.setLeft(newChild);
        }
        current.setRight(null);
        current.setLeft(null);
        return true;


    }

    private boolean removeTreeNodeCase2(TreeNode<T> current, TreeNode<T> parent) {
        TreeNode<T> smaller = leftTraverse(current.getRight());
        if (smaller != null){
            T value = smaller.getValue();
            remove2(smaller.getValue());

            current.setValue(value);

            return true;
        }
        return false;
    }

    private TreeNode<T> leftTraverse(TreeNode<T> current) {
        if (current.getLeft() != null){
            return leftTraverse(current.getLeft());
        }
        return current;
    }

    private boolean removeTreeNodeCase1(TreeNode<T> current, TreeNode<T> parent) {
        if (parent.getLeft() == current) {
            parent.setLeft(null);
            return true;
        } else if (parent.getRight() == current) {
            parent.setRight(null);
            return true;
        }
        return false;
    }


    public void inorderTraverse(){
        inorderTraverse(this.root);
    }
    private void inorderTraverse(TreeNode<T> root){
        if (root != null) {
            inorderTraverse(root.getLeft());
            visitTreeNode(root);
            inorderTraverse(root.getRight());
        }
    }

    public void preorderTraverse(){
        preorderTraverse(this.root);
    }
    private void preorderTraverse(TreeNode<T> root){
        if (root != null){
            visitTreeNode(root);
            preorderTraverse(root.getLeft());
            preorderTraverse(root.getRight());
        }
    }

    public void postOrderTraverse(){
        postOrderTraverse(this.root);
    }
    private void postOrderTraverse(TreeNode<T> root){
        if (root != null){
            postOrderTraverse(root.getLeft());
            postOrderTraverse(root.getRight());
            visitTreeNode(root);
        }
    }

    public void visitTreeNode(TreeNode<T> TreeNode){
        System.out.println(TreeNode.getValue());
    }

    public boolean isEmpty(){
        return this.root == null;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        return builder.toString();
    }

    public static void main(String[] args) {


    }
}
