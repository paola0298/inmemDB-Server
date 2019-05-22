import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Hashtable;

public class HashTableTest {


    public static void main(String[] args) {

        Hashtable<String, Hashtable<String, JSONObject>> collections = new Hashtable<>();


        Hashtable<String, JSONObject> registros = new Hashtable<>();

        JSONObject registro1 = new JSONObject();
        JSONArray array = new JSONArray();
        array.put("nombre");
        array.put("cedula");
        array.put("Carro");
        registro1.put("attr_name", array);

        JSONArray arraySize = new JSONArray();
        arraySize.put("123");
        arraySize.put("123");
        arraySize.put("Join");
        registro1.put("attr_size", arraySize);

        registros.put("cedula1", registro1);





        JSONObject registro2 = new JSONObject();
        registro2.put("nombre", "nombre2...");
        registro2.put("cedula", "cedula2...");
        registros.put("cedula2...", registro2);

        JSONArray arraySize2 = new JSONArray();
        arraySize2.put("123");
        arraySize2.put("123");
        registro2.put("attr_size", arraySize2);


        JSONObject registro3 = new JSONObject();
        registro3.put("nombre", "nombre3...");
        registro3.put("cedula", "cedula3...");

        JSONArray arraySize3 = new JSONArray();
        arraySize3.put("123");
        arraySize3.put("123");
        registro3.put("attr_size", arraySize3);



        registros.put("cedula3...", registro3);





//        collections.put("esquema1", registros);



        // esquema 1


        Hashtable<String, JSONObject> collec = collections.get("esquema1");
        System.out.println(collec);


        if (collec != null){

            for (JSONObject item : collec.values()){

                System.out.println(item);
                JSONArray sizeAttr = item.getJSONArray("attr_size");
                sizeAttr.forEach(value -> {
                    if (value.equals("Join")){
                        System.out.println(value);
                    }
                });
            }
            System.out.println("No hay joins en el esquema");
        }

    }

}
