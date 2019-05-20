package Structures.SplayTree;


public class Entry<k extends Comparable<? super k>, V> implements Comparable<Entry> {
    private k key;
    private V value;
    //para guardar cosas en el arbol la llave siempre va a ser un numero, si se van a guardar palabras o registros tipo String convierta los registros a integers antes de guardar los registros en el arbol
    public static int strToInt( String str ){
        int i = 0;
        int num = 0;
        boolean isNeg = false;

        //Check for negative sign; if it's there, set the isNeg flag
        if (str.charAt(0) == '-') {
            isNeg = true;
            i = 1;
        }

        //Process each character of the string;
        while( i < str.length()) {
            num *= 10;
            num += str.charAt(i++) - '0'; //Minus the ASCII code of '0' to get the value of the charAt(i++).
        }

        if (isNeg)
            num = -num;
        return num;
    }

    @Override
    public String toString() {
        return "Entry{" +
                "key=" + key.toString() +
                ", value='" + value.toString() + '\'' +
                '}';
    }

    public Entry(k key, V value) {
        this.key = key;
        this.value = value;
    }

    public k getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }


    @Override
    public int compareTo(Entry that) {
        return this.key.compareTo((k) that.key);
    }

    public static void main(String[] args) {
//        SplayTree<Lecciones> splay = new SplayTree<>();
//
//        splay.insert(new Entry<Lecciones, Persona>(new Lecciones(23, "Espanol"), new Persona("hazel", 19)));
//        splay.insert(new Entry<Lecciones, Persona>(new Lecciones(45, "Ingles"), new Persona("Alejandro", 20)));
//        splay.insert(new Entry<Lecciones, Persona>(new Lecciones(67, "Ciencias"), new Persona("Johanna", 45)));
//        splay.insert(new Entry<Lecciones, Persona>(new Lecciones(9, "Fisica"), new Persona("Leo", 48)));
//
//        //System.out.println(((Persona) splay.find(new Lecciones(23, "Ciencias")).getValue()).getNombre());
//
//        splay.printSorted();

    }


}
