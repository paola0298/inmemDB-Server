package Connection;

import Structures.LinkedList;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
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
                    String type = msg.getString("name");
                    JSONArray attrName = msg.getJSONArray("attr_name");
                    JSONArray attrSize = msg.getJSONArray("attr_size");
                    JSONArray attrType = msg.getJSONArray("attr_type");
                    String pk = msg.getString("primaryKey");

                    createScheme(type, attrName, attrSize, attrType, pk);
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

    private void createScheme(String type, JSONArray attrName, JSONArray attrSize, JSONArray attrType, String pk) {
    }

    private void deleteScheme() {
    }

    private void modifyScheme() {
    }

    private void queryScheme() {
    }

    private void insertData(String scheme, JSONArray attr) {
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

