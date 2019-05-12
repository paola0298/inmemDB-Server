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
                    queryData();
                    break;

                case "createIndex":
                    createIndex();
                    break;

                case "deleteIndex":
                    deleteIndex();
                    break;

                default:
                    sendResponse("Palabra clave no encontrada", con);
            }

            try {
                con.close();
                System.out.println("Conexión finalizada");
            } catch (IOException e) {
                System.out.println("Error closing the connection " + e.getMessage());
            }
        }

    }

    private JSONObject createScheme(String newScheme) {
        JSONObject response = new JSONObject();
        JSONObject schemeObject = new JSONObject(newScheme);
        String schemeName = schemeObject.getString("name");
        String serializedScheme = "";
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

    private void modifyScheme() { //modificar tipo de dato y tamaño
    }


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

    private void queryData() { }

    private void createIndex() { }

    private void deleteIndex() { }


    public static void main(String[] args) {
        int port = 6307;
        Server server = new Server(port);
        server.connectionListener();

    }
}

