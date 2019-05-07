import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

public class JSONArrayTest {

    public static void main(String[] args) {

        // Insert in JSONArray

        JSONObject arrayElements = new JSONObject();
        arrayElements.put("firstName", "John");
        arrayElements.put("lastName", "Doe");

        JSONArray jsonArray = new JSONArray();
        jsonArray.put(arrayElements);

        JSONObject mainObj = new JSONObject();
        mainObj.put("employees", jsonArray);


        // read json array

        String array = mainObj.toString();
        JSONObject obj = new JSONObject(array);
        JSONArray arr = obj.getJSONArray("employees");

        JSONObject elements = arr.getJSONObject(0);
        Iterator<String> keys = elements.keys();


        while(keys.hasNext()) {
            String key = keys.next();
            System.out.println(elements.getString(key));
        }

    }
}
