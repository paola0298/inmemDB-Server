package Structures;


public class Entry<K,V> implements Comparable<Entry> {
    private K key;
    private V value;

    @Override
    public String toString() {
        return "Entry<K,V>{" +
                "key=" + key +
                ", value='" + value + '\'' +
                '}';
    }

    public Entry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public void setKey(K key) {
        this.key = key;
    }


    public void setV(V v) {
        this.value = v;
    }

    public void setValue(V value) {
        this.value = value;
    }


    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    @Override
    public int compareTo(Entry o) {
        return 0;
    }


//    @Override
//    public int compareTo(Entry<K,V> that) {
//        if (this.key > that.key) {
//            return 1;
//        } else if (that.key > this.key) {
//            return -1;
//        } else {
//            return 0;
//        }
//    }
//

}