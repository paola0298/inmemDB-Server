package Connection;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * @author Paola
 * @version 1.0
 *
 * La clase Server realiza la conexión con el cliente, además envía y recibe mensajes
 */
public class Server {
    private ServerSocket serverSocket;
    private boolean isRunning = true;

    private Hashtable<String, String> schemes = new Hashtable<>();
    //nombre del esquema, json con estructura del esquema
    private Hashtable<String, Hashtable<String, String>> collections = new Hashtable<>();
    // nombre del esquema, HashTable <id, jsonArray con atributos>

    /**
     * @param port Puerto en el cual el servidor esta escuchando
     */
    public Server(int port){
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Error creating the socket " + e.getMessage());
        }
    }

    /**
     * @return La conexion con el cliente
     */
    public Socket clientConnection(){
        Socket con = null;
        try {
            con = serverSocket.accept();
        } catch (IOException e) {
            System.out.println("Error connecting to the client " + e.getMessage());
        }
        return con;
    }

    /**
     * @return El mensaje recibido del cliente
     */
    public String receiveDataFromClient(Socket con){
        String actualMessage = "";
        try {
            DataInputStream inputStream = new DataInputStream(con.getInputStream());
            actualMessage = inputStream.readUTF();
        } catch (EOFException ex) {
            System.out.println("Error reading stream " + ex.getMessage());
        } catch (IOException e) {
            System.out.println("Error reading stream " + e.getMessage());
        }
        return actualMessage;
    }

    /**
     * @param response Respuesta para el cliente
     * @param con Conexion con el cliente
     */
    public void sendResponse(String response, Socket con){
        try {
            DataOutputStream outputStream = new DataOutputStream(con.getOutputStream());
            outputStream.writeUTF(response);
        } catch (IOException e) {
            System.out.println("Error writing stream " + e.getMessage());
        }
    }

    /**
     * Escucha las conexiones del cliente
     */
    public void connectionListener(){

        while (this.isRunning){
            System.out.println("Esperando conexión");
            Socket con = clientConnection();
            System.out.println("Conexion establecida");

            JSONObject msg = new JSONObject(receiveDataFromClient(con));
            JSONObject response;

            String scheme = "";

            switch (msg.get("action").toString()) {
                case "createScheme":
                    scheme = msg.getString("scheme");
                    //si hay tipo que es join, en el array de size, va a estar con quien es el join

                    response = createScheme(scheme);
                    sendResponse(response.toString(), con);
                    break;

                case "querySchemes":
                    response = querySchemes();
                    sendResponse(response.toString(), con);
                    break;

                case "deleteScheme":
                    scheme = msg.getString("scheme");
                    response = deleteScheme(scheme);
                    sendResponse(response.toString(), con);

                    break;

                case "modifyScheme":
                    modifyScheme();
                    break;

                case "insertData":
                    scheme = msg.getString("type"); //esquema al que pertenece
                    JSONArray attr = msg.getJSONArray("attr"); //lista de atributos que se ingresaron
                    response = insertData(scheme, attr);
                    sendResponse(response.toString(), con);
                    break;

                case "deleteData":
                    scheme = msg.getString("scheme");
                    JSONArray primaryKeys = msg.getJSONArray("records");

                    response = deleteData(scheme, primaryKeys);
                    sendResponse(response.toString(), con);

                    break;

                case "queryData":

                    String parameters = msg.getString("parameters");
                    response = queryData(parameters);
                    sendResponse(response.toString(), con);
                    break;

                case "createIndex":
                    createIndex();
                    break;

                case "deleteIndex":
                    deleteIndex();
                    break;

                default:
                    response = new JSONObject();
                    response.put("status", "action_not_found");
                    sendResponse(response.toString(), con);
            }

            try {
                con.close();
                System.out.println("Conexión finalizada");
            } catch (IOException e) {
                System.out.println("Error closing the connection " + e.getMessage());
            }
        }

    }

    private JSONObject querySchemes() {
        JSONObject response = new JSONObject();
        String serializedSchemes;
        ObjectMapper mapper = new ObjectMapper();

        try {
            serializedSchemes = mapper.writeValueAsString(schemes);
            response.put("status", "success");
            response.put("schemes", serializedSchemes);

        } catch (JsonProcessingException e) {
            response.put("status", "failed");
            response.put("error", "Serialize");
        }

        return response;
    }

    private JSONObject createScheme(String newScheme) {
        JSONObject response = new JSONObject();
        JSONObject schemeObject = new JSONObject(newScheme);
        String schemeName = schemeObject.getString("name");
        String serializedScheme;
        ObjectMapper objectMapper = new ObjectMapper();

        if (schemes.get(schemeName) == null){
            try {
                schemes.put(schemeName, schemeObject.toString());
                System.out.println(schemes.toString());

                serializedScheme = objectMapper.writeValueAsString(schemes);
                response.put("status", "success");
                response.put("schemes", serializedScheme);

            } catch (JsonProcessingException e) {
                response.put("status", "failed");
                response.put("error", "Serialize");
            }
        } else {
            response.put("status", "failed");
            response.put("error", "Already exists");
        }
        return response;
    }

    private JSONObject deleteScheme(String name){
        JSONObject response = new JSONObject();
        if (schemes.containsKey(name)){
            //el esquema existe
            Enumeration<String> namesOfScheme = schemes.keys();
            while(namesOfScheme.hasMoreElements()) { // por cada esquema creado
                String schemeName = namesOfScheme.nextElement();
                JSONObject schemeStructure = new JSONObject(schemes.get(schemeName));
                JSONArray sizeArr = schemeStructure.getJSONArray("attrSize"); //obtengo el array donde dice con quien es el join
                JSONArray typeArr = schemeStructure.getJSONArray("attrType"); //obtengo el array donde dice si hay join
                for (int i = 0; i < typeArr.length(); i++) {
                    if (typeArr.get(i).equals("join")) {
                        // ese esquema tiene un join
                        //ver en estructura del esquema con quien es el join
                        if (sizeArr.get(i).equals(name)) {
                            System.out.println("No se puede eliminar el esquema");
                            response.put("status", "failed");
                            response.put("error", "join");
                            return response;
                        }
                    }
                }
            }
            response = deleteSchemeFromHash(name);
            return response;
        } else {
            response.put("status", "failed");
            response.put("error", "No exists");
            return response;
        }
    }

    private JSONObject deleteSchemeFromHash(String name){
        JSONObject response = new JSONObject();
        schemes.remove(name);
        collections.remove(name);
        // enviar nuevos datos serializados a cliente
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String serializedScheme = objectMapper.writeValueAsString(schemes);
            String serializedCollections = objectMapper.writeValueAsString(collections);
            response.put("status", "success");
            response.put("schemes", serializedScheme);
            response.put("collections", serializedCollections);
        } catch (JsonProcessingException e) {
            response.put("status", "failed");
            response.put("error", "Serialize");
        }
        return response;
    }

    private void modifyScheme() { }//modificar tipo de dato y tamaño

    private JSONObject insertData(String scheme, JSONArray attr) {
        System.out.println("Insertando datos...");
        JSONObject response = new JSONObject();
        String schemeString = schemes.get(scheme);
        if (schemeString != null){
            JSONObject actualScheme = new JSONObject(schemeString);
            // el esquema existe
            //obtener quien es la llave primaria de ese esquema
            String actualPKAttr = actualScheme.getString("primaryKey");
            JSONArray attrName = actualScheme.getJSONArray("attrName");
            int posOfPk = -1;

            for(int i=0; i<attrName.length(); i++){
                if (attrName.get(i).equals(actualPKAttr)){
                    posOfPk = i;
                }
            }
            // obtener el atributo de la llave primaria
            String actualPk = attr.getString(posOfPk);
            Hashtable<String, String> collectionOfScheme =  collections.get(scheme);
            if (collectionOfScheme != null){
                // en collectionOfScheme tengo que colocar el nuevo registro
                collectionOfScheme.put(actualPk, attr.toString());
                collections.remove(scheme);
                collections.put(scheme, collectionOfScheme);

            } else{
                // tengo que crear una nueva collecion para el esquema actual
                Hashtable<String, String> newCollection = new Hashtable<>();
                newCollection.put(actualPk, attr.toString());
                collections.put(scheme, newCollection);
            }
            //serializar collections
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String serializedCollections = objectMapper.writeValueAsString(collections);
                response.put("status", "success");
                response.put("collections", serializedCollections);
            } catch (JsonProcessingException e) {
                response.put("status", "failed");
                response.put("error", "Serialize");
            }
        } else{
            //el esquema no existe
            response.put("status", "failed");
            response.put("error", "No exists");
        }
        return response;

    }

    private JSONObject deleteData(String scheme, JSONArray primaryKeys) {
        System.out.println("Eliminando registro de " + scheme);
        JSONObject response = new JSONObject();

        Hashtable<String, String> collectionOfScheme =  collections.get(scheme);

        if (collectionOfScheme != null){
            //el esquema tiene colecciones de datos
            System.out.println("El esquema tiene colecciones");

            for (int i=0; i<primaryKeys.length(); i++) {

                String primaryKey = primaryKeys.getString(i);

                System.out.println("collectionOfScheme.get(primaryKey) " + collectionOfScheme.get(primaryKey));
                if (collectionOfScheme.get(primaryKey) != null) {
                    // la coleccion de datos existe

                    System.out.println("Collections before remove " + collections);

                    collectionOfScheme.remove(primaryKey);
                    collections.remove(scheme);
                    collections.put(scheme, collectionOfScheme);

                    System.out.println("Collections after remove " + collections);

                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        String serializedCollections = objectMapper.writeValueAsString(collections);
                        response.put("status", "success");
                        response.put("collections", serializedCollections);
                        System.out.println("Sending response");
                        return response;
                    } catch (JsonProcessingException e) {
                        response.put("status", "failed");
                        response.put("error", "serialize");
                        return response;
                    }
                }
            }

            response.put("status", "failed");
            response.put("error", "register not found");
            return response;
        } else{
            //el esquema no tiene colleciones de datos
            response.put("status", "failed");
            response.put("error", "no data");
            return response;
        }
    }

    /*
                    "action" : "queryData
    "parameters" : {
        "scheme" : "nombre del esquema"
        "searchBy" : "campo de busqueda"
        "index" : true o false
        "tree" : arbol de busqueda  Esto se obtendria si index=true
        "join" : "estructura con la que tiene join" o null
        "searchByJoin" : true o false  Si es true, entonces el campo de busqueda va a ser de
                                        la estructura del join
    }
                     */


    private JSONObject queryData(String parameters) {
        JSONObject response = new JSONObject();
        JSONObject parametersObject = new JSONObject(parameters);

        JSONArray result = new JSONArray();

        String scheme = parametersObject.getString("scheme");
        String columnToSearch = parametersObject.getString("searchBy");
        Boolean index = parametersObject.getBoolean("index");
        Boolean searchByJoin = parametersObject.getBoolean("searchByJoin");
        String dataToSearch = parametersObject.getString("dataToSearch");

        if (index && !searchByJoin) {
            System.out.println("Buscando por indice");
        } else if (!index && searchByJoin) {
            System.out.println("Busqueda lineal de columna del join");
        } else if (index && searchByJoin) {
            System.out.println("Busqueda por indice en columna del join");
        } else if (!index && !searchByJoin){
            System.out.println("Busqueda secuencial sin columna de join");
            if (schemes.containsKey(scheme)){

                int indexOfArray = searchIndexOfArray(scheme, columnToSearch);

                //tengo que buscar en que indice esta el dato a buscar
                if (indexOfArray != -1) {
                    if (collections.containsKey(scheme)) {
                        System.out.println("El esquema tiene colecciones de datos");
                        Hashtable<String, String> collectionsOfScheme = collections.get(scheme);
                        System.out.println(collectionsOfScheme);
                        for (String key: collectionsOfScheme.keySet()) {
                            JSONArray attr = new JSONArray(collectionsOfScheme.get(key));

                            System.out.println("Searching "  + attr.get(indexOfArray));
                            if (attr.get(indexOfArray).equals(dataToSearch)){
                                System.out.println("Elemento encontrado");
                                result.put(collectionsOfScheme.get(key));
                            }
                        }
                    }
                    response.put("result", result);
                    response.put("status", "success");
                    return response;
                }
            } else {
                System.out.println("El esquema no existe");
            }

        }

        response.put("status", "failed");
        return response;
    }

    private int searchIndexOfArray(String scheme, String columnToSearch) {
        JSONObject schemeObject = new JSONObject(schemes.get(scheme));
        String attrName = schemeObject.get("attrName").toString();
        JSONArray schemeArray = new JSONArray(attrName);

        for (int i=0; i<schemeArray.length(); i++){
            if (schemeArray.get(i).equals(columnToSearch)){
                return i;
            }
        }

        return -1;
    }

    private void createIndex() { }

    private void deleteIndex() { }


    public static void main(String[] args) {
        int port = 6307;
        Server server = new Server(port);
        server.connectionListener();

    }


    /*

    campo de busqueda
    con o sin indice
    tipo de arbol
    si hay join
        - extraer datos
        - hay posibilidad de buscar por columnas del join




mpvc0201
     */

}

