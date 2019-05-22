package Structures.AVLTree;


public class AVLTree<K extends Comparable<? super K>, V> {

    private AVLNode<K,V> root;

    /**
     * Devuelve la altura de cierto nodo
     * @param N nodo a consultar altura
     * @return la altura comoun entero
     */
    public int height(AVLNode N)
    {
        if (N == null)
            return 0;
        return N.getHeight();
    }

    /**
     * Devuelve el máximo entre dós números
     * @param a primer numero
     * @param b segundo numero
     * @return el mayor de ambos
     */
    public int max(int a, int b)
    {
        return (a > b) ? a : b;
    }


    /**
     * Una rotación simple a la derecha
     * @param y el nodo donde comienza la rotación
     * @return Lanueva raíz despues de rotar
     */
    public AVLNode<K, V> rightRotate(AVLNode<K, V> y)
    {
        AVLNode<K, V> x = y.getLeft();
        AVLNode<K, V> T2 = x.getRight();

        // Perform rotation
        x.setRight(y);
        y.setLeft(T2);

        // Update heights
        y.setHeight(max(height(y.getLeft()), height(y.getRight())) + 1);
        x.setHeight(max(height(x.getLeft()), height(x.getRight())) + 1);

        // Return new root
        return x;
    }


    /**
     * Una rotación simple a la izquierda
     * @param x el nodo donde comienza la rotación
     * @return Lanueva raíz despues de rotar
     */
    public AVLNode<K, V> leftRotate(AVLNode<K, V> x)
    {
        AVLNode<K, V> y = x.getRight();
        AVLNode<K, V> T2 = y.getLeft();

        // Perform rotation
        y.setLeft(x);
        x.setRight(T2);

        // Update heights
        x.setHeight(max(height(x.getLeft()), height(x.getRight())) + 1);
        y.setHeight(max(height(y.getLeft()), height(y.getRight())) + 1);

        // Return new root
        return y;
    }

    /**
     * Devuelve el factor de balnceo de cierto nodo
     * @param N nodo a consultar
     * @return el balance como la diferencia de las alturas izquierda y derecha
     */
    public int getBalance(AVLNode<K, V> N)
    {
        if (N == null)
            return 0;
        return height(N.getLeft()) - height(N.getRight());
    }

    /**
     * Sobrecarga para facilitar método recursivo
     * @param key dragon a insertar
     */
    public void insert(K key, V value) {
        this.root=insert(this.root, key, value);
    }

    /**
     * Método recursivo de inserción, al inicio busca la posicion correcta para insertar
     * como en un ABB pero luego retrocede de forma recursiva para actualizar las alturas
     * y portanto verificar los factores de balanceo y de no cumplirse realiza las rotaciones necesarias
     * @param node el nodo actual
     * @param key el dragon a insertar
     * @return un nodo modificado para comunicarse entre recursiones
     */
    private AVLNode<K, V> insert(AVLNode<K, V> node, K key, V value)
    {
        /* 1. Perform the normal BST rotation */
        if (node == null){
            return (new AVLNode<>(key, value));
        }

        if (key.compareTo(node.getKey()) < 0)
            node.setLeft(insert(node.getLeft(), key, value));
        else if (key.compareTo(node.getKey()) > 0)
            node.setRight(insert(node.getRight(), key, value));
        else // Equal keys not allowed
            return node;

        /* 2. Update height of this ancestor node */
        node.setHeight(1 + max(height(node.getLeft()),
                height(node.getRight())));

        /* 3. Get the balance factor of this ancestor
        node to check whether this node became
        Wunbalanced */
        int balance = getBalance(node);

        // If this node becomes unbalanced, then
        // there are 4 cases Left Left Case
        if (balance > 1 && key.compareTo(node.getLeft().getKey()) < 0)
            return rightRotate(node);

        // Right Right Case
        if (balance < -1 && key.compareTo(node.getRight().getKey()) > 0)
            return leftRotate(node);

        // Left Right Case
        if (balance > 1 && key.compareTo(node.getLeft().getKey()) > 0)
        {
            node.setLeft(leftRotate(node.getLeft()));
            return rightRotate(node);
        }

        // Right Left Case
        if (balance < -1 && key.compareTo(node.getRight().getKey()) < 0)
        {
            node.setRight(rightRotate(node.getRight()));
            return leftRotate(node);
        }

        /* return the (unchanged) node pointer */
        return node;
    }

    private AVLNode<K, V> minValueNode(AVLNode<K, V> node)
    {
        AVLNode<K, V> current = node;

        /* loop down to find the leftmost leaf */
        while (current.getLeft() != null)
            current = current.getLeft();

        return current;
    }

    public void delete(K key){
        this.root = deleteNode(this.root, key);
    }

    private AVLNode<K, V> deleteNode(AVLNode<K, V> root, K key) {
        // STEP 1: PERFORM STANDARD BST DELETE
        if (root == null)
            return root;

        // If the key to be deleted is smaller than
        // the root's key, then it lies in left subtree
        if (key.compareTo(root.getKey()) < 0)
            root.setLeft(deleteNode(root.getLeft(), key));

            // If the key to be deleted is greater than the
            // root's key, then it lies in right subtree
        else if (key.compareTo(root.getKey()) > 0)
            root.setRight(deleteNode(root.getRight(), key));

            // if key is same as root's key, then this is the node
            // to be deleted
        else
        {

            // node with only one child or no child
            if ((root.getLeft() == null) || (root.getRight() == null))
            {
                AVLNode<K, V> temp = null;
                if (temp == root.getLeft())
                    temp = root.getRight();
                else
                    temp = root.getLeft();

                // No child case
                if (temp == null)
                {
                    temp = root;
                    root = null;
                }
                else // One child case
                    root = temp; // Copy the contents of
                // the non-empty child
            }
            else
            {

                // node with two children: Get the inorder
                // successor (smallest in the right subtree)
                AVLNode<K, V> temp = minValueNode(root.getRight());

                // Copy the inorder successor's data to this node
                root.setKey(temp.getKey());

                // Delete the inorder successor
                root.setRight(deleteNode(root.getRight(), temp.getKey()));
            }
        }

        // If the tree had only one node then return
        if (root == null)
            return root;

        // STEP 2: UPDATE HEIGHT OF THE CURRENT NODE
        root.setHeight(max(height(root.getLeft()), height(root.getRight())) + 1);

        // STEP 3: GET THE BALANCE FACTOR OF THIS NODE (to check whether
        // this node became unbalanced)
        int balance = getBalance(root);

        // If this node becomes unbalanced, then there are 4 cases
        // Left Left Case
        if (balance > 1 && getBalance(root.getLeft()) >= 0)
            return rightRotate(root);

        // Left Right Case
        if (balance > 1 && getBalance(root.getLeft()) < 0)
        {
            root.setLeft(leftRotate(root.getLeft()));
            return rightRotate(root);
        }

        // Right Right Case
        if (balance < -1 && getBalance(root.getRight()) <= 0)
            return leftRotate(root);

        // Right Left Case
        if (balance < -1 && getBalance(root.getRight()) > 0)
        {
            root.setRight(rightRotate(root.getRight()));
            return leftRotate(root);
        }

        return root;
    }

    /**
     * Sobrecarga para facilitar el uso del preorden recursivo
     */
    public void preOrder() {
        System.out.print("AVL en preorden: ");
        preOrder(this.root,this.root);
        System.out.println();
    }

    /**
     * Imprime el arbol en recorriendolo en preorden y mostrando las edades de sus nodos
     * que son el criterio de ordenamiento del AVL
     * En parentesis coloca la edad del padre del nodo para orientarse mejor
     */

    private void preOrder(AVLNode<K, V> node, AVLNode<K, V> padre) {
        if (node != null)
        {
            System.out.print(node.getKey() +"("+padre.getKey()+")"+" ");
            preOrder(node.getLeft(),node);
            preOrder(node.getRight(),node);
        }
    }

    public AVLNode<K, V> getRoot() {
        return root;
    }

    public void setRoot(AVLNode<K, V> root) {
        this.root = root;
    }

    public V search(K key) {
        return search(key, this.root);
    }

    private V search(K key, AVLNode<K,V> current) {
        if (current == null) {
            return null;
        } else if (current.getKey().compareTo(key) < 0) {
            return search(key, current.getRight());
        } else if (current.getKey().compareTo(key) > 0) {
            return search(key, current.getLeft());
        }
        System.out.println("Node " + current.getValue() + " found!");
        return current.getValue();
    }

    public static void main(String[] args)
    {
        AVLTree<Integer, Integer> tree = new AVLTree<>();

        /* Constructing tree given in the above figure */

        tree.insert(9, 900);
        tree.insert(5, 500);
        tree.insert(10, 1000);
        tree.insert(0, 1);
        tree.insert(6, 600);
        tree.insert(11, 1100);
        tree.insert(-1, -100);
        tree.insert(1, 100);
        tree.insert(2, 200);

        tree.search(2);


        /* The constructed AVL Tree would be
        9
        / \
        1 10
        / \ \
        0 5 11
        / / \
        -1 2 6
        */
        System.out.println("Preorder traversal of "+
                "constructed tree is : ");
        tree.preOrder();

        tree.delete(10);

        /* The AVL Tree after deletion of 10
        1
        / \
        0 9
        /     / \
        -1 5 11
        / \
        2 6
        */
        System.out.println("");
        System.out.println("Preorder traversal after "+
                "deletion of 10 :");
        tree.preOrder();
    }
}