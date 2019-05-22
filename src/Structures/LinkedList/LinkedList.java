package Structures.LinkedList;


public class LinkedList<T> {

    //atributos LinkedList
    private Node<T> head;
    private Node<T> current;
    private Node<T> tail;
    private int position;
    private int size;

    /**
     * Constructor de la lista enlazada
     */
    public LinkedList() {
        this.head = new Node<>();
        this.current = this.head;
        this.tail = this.head;
        this.size = 0;
        this.position = -1;
    }

    /**
     * Agrega un nuevo elemento a la lista
     * @param element El elemento a agregar
     */
    public void insert(T element) {
        //insertar en cualquier posición
        Node<T> newNode = new Node<>(element, this.current.getNext());
        this.current.setNext(newNode);
        //necesario si se está insertando al final de la lista
        if (this.current == this.tail) {
            this.tail = tail.getNext();
        }
        this.size++;

    }

    /**
     * Se encarga de insertar un elemento nuevo al incio de la lista
     * @param element
     */
    public void insertFirst(T element){
        Node<T> newNode = new Node<>(element);
        newNode.setNext(this.head.getNext());
        this.head.setNext(newNode);
        this.size++;
    }

    /**
     * Este método se encarga de añadir elementos al final de la lista
     * @param element
     */
    public void append(T element) {
        //siempre se pega al final de la lista
        Node newNode = new Node(element);
        this.tail.setNext(newNode);
        this.tail = newNode;
        this.size++;
    }

    /**
     * Este método se encarga de eliminar datos de la lista
     */
    public void remove() {
        //verificar que la lista no está vacía
        if ((this.head == this.current) && (this.head == this.tail)){
            System.out.println("Lista vacía, no se puede remover ningún elemento");
            return;
        } //también if (this.size == 0) ...

        //en temp se va a almacenar el nodo ANTERIOR al que se quiere borrar
        Node temp = head;
        while (temp.getNext() != this.current) {
            temp = temp.getNext();
        }
        //borrar la referencia al nodo actual
        temp.setNext(this.current.getNext());
        //necesario si se esta borrando el último nodo
        if (this.current == this.tail) {
            this.current = this.tail = temp;
            this.position--;
        }
        else
            //necesario si se está borrando un nodo diferente al último
            this.current = this.current.getNext();

        //disminuir el tamaño
        this.size--;
    }

    /**
     * Este método se encarga de vaciar la lista
     */
    public void clear() {
        this.head = this.tail = this.current = new Node();
        this.position = -1;
        this.size = 0;
    }

    public T getValue(){
        return this.current.getValue();
    }

    public int getSize() {
        return this.size;
    }

    /**
     * Método que se encarga de avanzar al siguietne nodo en la lista
     * @return valor boolean sobre si se pudo avanzar
     */
    public boolean next() {
        if (this.current == this.tail) {
            System.out.println("Actualmente en último nodo, no se puede avanzar");
            return false;
        }
        this.current = this.current.getNext();
        this.position++;
        return true;
    }

    /**
     * Método que se encarga de retroceder los nodos en la lista
     * @return el valor boolean sobre si se pudo retroceder
     */
    public boolean previous() {
        if (this.current == this.head) {
            System.out.println("Actualmente en primer nodo, no se puede retroceder");
            return false;
        }
        Node temp = head;
        this.position = -1;
        while (temp.getNext() != this.current) {
            temp = temp.getNext();
            this.position++;
        }
        this.current = temp;
        return true;
    }

    public int getPosition() {
        return this.position;
    }

    public void goToStart(){
        this.current = this.head;
        this.position = -1;
    }

    public void goToEnd(){
        this.current = this.tail;
        this.position = this.size - 1;
    }

    /**
     * Método que se encarga de ir a la posición ingresada de la lista
     * @param pos
     */
    public void goToPos(int pos) {
        if (pos < 0 || pos >= this.size) {
            System.out.println("Posición inválida");
            return;
        }
        if (pos > this.position) {
            while (pos > this.position) {
                this.next();
            }
        } else if (pos < this.position) {
            while (pos < this.position) {
                this.previous();
            }
        }
    }

    /**
     * Este método devuelve la posición determinada de un elemento dado
     * @param element
     * @return valor entero de la posición
     */
    public int getPositionOfElement(T element) {
        Node tempNode = this.head;
        int position = -1;
        while (tempNode != null) {
            if (tempNode.getValue() != null && tempNode.getValue().equals(element)){
                return position;
            }
            tempNode = tempNode.getNext();
            position++;
        }
        return -1;
    }

    /**
     * @param position
     * @return  Este método retorna el elemento de la posición dada
     */
    public T getElementByPos(int position){
        goToPos(position);
        return this.current.getValue();
    }

    /**
     * Devuelve la representación en String de la lista
     * @return un string representado la lista
     */
    public String toString() {
        Node currentNode = this.head.getNext();

        StringBuffer result = new StringBuffer();

        for (int i = 0; currentNode != null; i++)
        {
            if (i > 0)
            {
                result.append(",");
            }
            Object element = currentNode.getValue();

            result.append(element == null ? "" : element);
            currentNode = currentNode.getNext();
        }
        return result.toString();
    }

    public static void main(String[] args) {
        LinkedList<Integer> list =  new LinkedList<>();
        list.append(1);
        list.append(2);
        list.append(3);
        list.append(4);
        list.append(5);
        list.insertFirst(100);
        list.insertFirst(200);

        System.out.println(list);


    }
//
//    public static void main(String[] args) {
//        System.out.println("**** LikedList");
//        System.out.println("**** Agregando elementos....");
//        LinkedList lista = new LinkedList();
//        lista.append(9);
//        lista.append(11);
//        lista.append(13);
//
//        System.out.println("**** Tamaño: " + lista.getSize());
//        System.out.println("**** Representación gráfica: " + lista.toString());
//
//        System.out.println("**** Recorrer la lista");
//        for(lista.goToStart(); lista.getPosition() < lista.getSize(); lista.next()) {
//            System.out.println("Pos actual:" + lista.getPosition());
//            System.out.println("Valor actual:" + lista.getValue());
//            System.out.println("@");
//            if (lista.getPosition() == lista.getSize() -1) {
//                break;
//            }
//        }
//        System.out.println("**** Insertar el 12, de forma ordenada y luego al final el 14");
//        System.out.println("**** Pos actual " + lista.getPosition());
//        for(lista.goToStart(); lista.getPosition() < lista.getSize(); lista.next()) {
//            if ((lista.getValue()) != null) {
//                if (lista.getValue().equals(11)) {
//                    lista.insert(12);
//                    lista.append(14);
//                    break;
//                }
//            }
//        }
//        System.out.println("**** Tamaño: " + lista.getSize());
//        System.out.println("**** Representación gráfica: " + lista.toString());
//        /*System.out.println("Reverseeeeeeee");
//        lista.reverse();
//        System.out.println("**** Representaci�n gr�fica: " + lista.toString());
//        System.out.println("**** Head: " + lista.head.getValue());
//        System.out.println("**** Current: " + lista.current.getValue());
//        System.out.println("**** Tail: " + lista.tail.getValue());
//        System.out.println("**** Pos: " + lista.getPosition());
//        System.out.println("**** Size: " + lista.getSize());*/
//        System.out.println("**** Pos actual luego de insertar: " + lista.getPosition());
//        System.out.println("**** Buscar el elemento 9, la pos es: " + lista.getPositionOfElement(9));
//        System.out.println("**** Buscar el elemento 14, la pos es: " + lista.getPositionOfElement(14));
//        System.out.println("**** Buscar el elemento 15, la pos es: " + lista.getPositionOfElement(15));
//        System.out.println("**** Obtener elemento en la posición actual (1), el elemento es: " + lista.getValue());
//        lista.goToPos(4);
//        System.out.println("**** Ir a la posición (4), el elemento es: " + lista.getValue());
//        System.out.println("**** Borrando elemento actual ... " + lista.getValue());
//        lista.remove();
//        System.out.println("*** El  elemento en el tail es: " + lista.tail.getValue());
//        System.out.println("*** La posición actual es " + lista.getPosition());
//        System.out.println("**** Tamaño: " + lista.getSize());
//        System.out.println("**** Representación gráfica: " + lista.toString());
//        lista.previous();
//        System.out.println("**** Retroceder una posici�n, el elemento actual es: " + lista.getValue());
//        System.out.println("*** La posici�n actual es " + lista.getPosition());
//        System.out.println("**** Borrando elemento actual ... " + lista.getValue());
//        lista.remove();
//        System.out.println("**** Tama�o: " + lista.getSize());
//        System.out.println("**** Representaci�n gr�fica: " + lista.toString());
//        System.out.println("*** La posici�n actual es " + lista.getPosition());
//        System.out.println("**** El elemento actual es ... " + lista.getValue());
//        System.out.println("**** Borrando elemento actual ... " + lista.getValue());
//        lista.remove();
//        System.out.println("**** Tama�o: " + lista.getSize());
//        System.out.println("**** Representaci�n gr�fica: " + lista.toString());
//        System.out.println("**** Head: " + lista.head.getValue());
//        System.out.println("**** Current: " + lista.current.getValue());
//        System.out.println("**** Tail: " + lista.tail.getValue());
//        System.out.println("**** Pos: " + lista.getPosition());
//        System.out.println("**** Size: " + lista.getSize());
//        System.out.println("**** Limpiando lista... ");
//        lista.clear();
//        System.out.println("**** Tama�o: " + lista.getSize());
//        System.out.println("**** Representaci�n gr�fica: " + lista.toString());
//    }
}
