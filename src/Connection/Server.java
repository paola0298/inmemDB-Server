package Connection;

import Structures.LinkedList;
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
import java.util.Iterator;

/**
 * @author Paola
 * @version 1.0
 *
 * La clase Server realiza la conexión con el cliente, además envía y recibe mensajes
 */
public class Server {
    private ServerSocket serverSocket;
    private boolean isRunning = true;

    private Hashtable<String, JSONObject> schemes;
    private Hashtable<String, Hashtable<String, JSONObject>> collections;


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

            switch (msg.get("action").toString()) {
                case "createScheme":
                    String name = msg.getString("name");
                    JSONArray attrName = msg.getJSONArray("attr_name");
                    JSONArray attrSize = msg.getJSONArray("attr_size");
                    JSONArray attrType = msg.getJSONArray("attr_type");
                    //TODO agregar join con otros esquemas
                    String pk = msg.getString("primaryKey");

                    response = createScheme(name, attrName, attrSize, attrType, pk);
                    sendResponse(response.toString(), con);
                    break;

                case "deleteScheme":
                    deleteScheme();

                    break;

                case "modifyScheme":
                    modifyScheme();
                    break;

                case "queryScheme":
                    queryScheme();
                    break;

                case "insertData":
                    String scheme = msg.getString("type");
                    JSONArray attr = msg.getJSONArray("attr");
                    insertData(scheme, attr);
                    break;

                case "deleteData":
                    deleteData();

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

    private JSONObject createScheme(String name, JSONArray attrName, JSONArray attrSize, JSONArray attrType, String pk) {
        JSONObject response = new JSONObject();
        JSONObject newScheme = new JSONObject();
        String serializedScheme = "";
        ObjectMapper objectMapper = new ObjectMapper();

        if (schemes.get(name) == null){
            try {
                newScheme.put("attrName", attrName);
                newScheme.put("attrType", attrType);
                newScheme.put("attrSize", attrSize);
                //TODO agregar join con otros esquemas
                newScheme.put("primaryKey", pk);
                schemes.put(name, newScheme);

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

    private void deleteScheme() {
        //TODO si el esquema tiene join no se puede eliminar, de lo contrario se borra el esquema y sus colecciones
    }

    private void modifyScheme() { //modificar tipo de dato y tamaño
    }

    private void queryScheme() {
    }

    private void insertData(String scheme, JSONArray attr) {
        JSONObject newData = new JSONObject();
        JSONObject response = new JSONObject();
        Hashtable<String, JSONObject> collection = new Hashtable<>();
        String actualPK = "";

        JSONObject actualScheme = schemes.get(scheme);
        String pkAttr = actualScheme.getString("primaryKey");

        JSONObject elements = attr.getJSONObject(0);
        Iterator<String> keys = elements.keys();

        while(keys.hasNext()) {
            String key = keys.next();
            String element = elements.getString(key);
            if (element.equals(pkAttr)){
                actualPK = element;
                break;
            }
        }







    }

    private void deleteData() {
    }

    private void queryData() {
    }

    private void createIndex() {
    }

    private void deleteIndex() {
    }


    public static void main(String[] args) {
        int port = 6307;
        Server server = new Server(port);
        server.connectionListener();

    }
}

