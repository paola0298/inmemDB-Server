package Structures.AATree;

/**
 *  Java Program to Implement AA Tree
 */


/** Class AATree **/
public class AATree<K extends Comparable<? super K>, V> {
    private AANode<K,V> root;


    /**
     * El método constructor del árbol
     */
    public AATree() {
        root = null;
    }
    /**
     * El método se encarga de verificar si el árbol está vacío
     */
    /** Function to check if tree is empty **/
    public boolean isEmpty() {
        return root == null;
    }
    /**
     * Método que vacía el árbol
     */
    /** Make the tree empty **/
    public void clear() {
        root = null;
    }

    /**
     * Este método se encarga de hacer inserciones en el árbol
     * @param key
     * @param value
     */
    /* Functions to insert data */
    public void insert(K key, V value) {
        root = insert(key, root, value);
    }

    private AANode<K,V> insert(K key, AANode<K,V> T, V value) {
        if (T == null)
            T = new AANode<>(key, value);
        else if ( key.compareTo(T.getKey()) < 0 )
            T.setLeft(insert(key, T.getLeft(), value));
        else if ( key.compareTo(T.getKey()) > 0)
            T.setRight(insert(key, T.getRight(), value));
        else
            return T;

        T = skew(T);
        T = split(T);
        return T;
    }
    /**
     *
     */
    /** Function Skew **/
    private AANode<K,V> skew(AANode<K,V> T) {
        if (T == null)
            return null;
        else if (T.getLeft() == null)
            return T;
        else if (T.getLeft().getLevel() == T.getLevel()) {
            AANode<K,V> L = T.getLeft();
            T.setLeft(L.getRight());
            L.setRight(T);
            return L;
        }
        else
            return T;
    }
    /** Este método se encarga de separar el árbol
    /** Function split **/
    private AANode<K,V> split(AANode<K,V> T) {
        if (T == null)
            return null;
        else if (T.getRight() == null || T.getRight().getRight() == null)
            return T;
        else if (T.getLevel() == T.getRight().getRight().getLevel()) {
            AANode<K,V> R = T.getRight();
            T.setRight(R.getLeft());
            R.setLeft(T);

            R.setLevel(R.getLevel() + 1);
            return R;
        }
        else
            return T;
    }
    /**
     * Este método se encarga de disminuir el nivel de una llave
     */
    /** Function decrease key **/
    private AANode<K,V> decreaseLevel(AANode<K,V> T) {
        int shouldBe = Math.min(T.getLeft().getLevel(), T.getRight().getLevel()) + 1;
        if (shouldBe < T.getLevel()) {
            T.setLevel(shouldBe);
            if (shouldBe < T.getRight().getLevel())
                T.getRight().setLevel(shouldBe);
        }
        return T;
    }
    /**
     * Este método cuenta los nodos del árbol
     */
    /** Functions to count number of nodes **/
    public int countNodes() {
        return countNodes(root);
    }

    private int countNodes(AANode<K,V> r) {
        if (r == null)
            return 0;
        else {
            int l = 1;
            l += countNodes(r.getLeft());
            l += countNodes(r.getRight());
            return l;
        }
    }
    /**
     * Este método es el encargado de hacer la llamada de la búsqueda da un elemento dado un key
     */
    /** Functions to search for an element **/
    public V search(K key) {
        return search(root, key);
    }

    /**
     * Este método es el encargado de hacer la búsqueda da un elemento dado un key
     * @param r
     * @param key
     * @return
     */
    private V search(AANode<K,V> r, K key) {
        V result = null;
        while ((r != null) && result==null) {
            K rval = r.getKey();
            if (key.compareTo(rval) < 0)
                r = r.getLeft();
            else if (key.compareTo(rval) > 0)
                r = r.getRight();
            else {
                result = r.getValue();
                break;
            }
            result = search(r, key);
        }
        return result;
    }

    /** Function for inorder traversal **/
    public void inorder() {
        inorder(root);
    }

    private void inorder(AANode r) {
        if (r != null) {
            inorder(r.getLeft());
            System.out.print(r.getKey() +" ");
            inorder(r.getRight());
        }
    }

    /** Function for preorder traversal **/
    public void preorder() {
        preorder(root);
    }

    private void preorder(AANode r) {
        if (r != null) {
            System.out.print(r.getKey() +" ");
            preorder(r.getLeft());
            preorder(r.getRight());
        }
    }
    /** Function for postorder traversal **/
    public void postorder() {
        postorder(root);
    }

    private void postorder(AANode r) {
        if (r != null) {
            postorder(r.getLeft());
            postorder(r.getRight());
            System.out.print(r.getKey() +" ");
        }
    }


    //TODO metodo para eliminar

    public static void main(String[] args)
    {

        AATree<Integer, Integer> at= new AATree<>();
        at.insert(30,300);
        at.insert(15,150);
        at.insert(70,700);
        at.insert(5,50);
        at.insert(20,200);
        at.insert(10,100);
        at.insert(50,500);
        at.insert(85,850);
        at.insert(35,350);
        at.insert(60,600);
        at.insert(40,400);
        at.insert(55,550);
        at.insert(65,650);
        at.insert(80,800);
        at.insert(90,900);

        at.inorder();

        System.out.println(at.search(40));


    }
}
