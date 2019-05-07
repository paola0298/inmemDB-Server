import org.json.JSONObject;

import java.util.Hashtable;

public class HashTableTest {


    public static void main(String[] args) {
        JSONObject json = new JSONObject();

        Hashtable<String, JSONObject> schemes = new Hashtable<>();

        schemes.put("Prueba", json);

        JSONObject hola = schemes.get("hola");

        System.out.println(hola);




    }

}
