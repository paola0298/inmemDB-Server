package Structures;



public class Entry implements Comparable<Entry> {
    private int key;
    private String value;
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
                "key=" + key +
                ", value='" + value + '\'' +
                '}';
    }

    public Entry(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }


    @Override
    public int compareTo(Entry that) {
        if (this.key > that.key) {
            return 1;
        } else if (that.key > this.key) {
            return -1;
        } else {
            return 0;
        }
    }


}
