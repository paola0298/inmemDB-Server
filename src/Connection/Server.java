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
    private Socket clientConnection(){
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
    private String receiveDataFromClient(Socket con){
        String actualMessage = "";
        try {
            DataInputStream inputStream = new DataInputStream(con.getInputStream());
            actualMessage = inputStream.readUTF();
        } catch (IOException ex) {
            System.out.println("Error reading stream " + ex.getMessage());
        }
        return actualMessage;
    }

    /**
     * @param response Respuesta para el cliente
     * @param con Conexion con el cliente
     */
    private void sendResponse(String response, Socket con){
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
    private void connectionListener(){

        while (this.isRunning){
            System.out.println("Esperando conexión");
            Socket con = clientConnection();
            System.out.println("Conexion establecida");

            JSONObject msg = new JSONObject(receiveDataFromClient(con));
            JSONObject response;

            String scheme;

            switch (msg.get("action").toString()) {
                case "createScheme":
                    scheme = msg.getString("scheme");
                    //si hay tipo que es join, en el array de size, va a estar con quien es el join

                    response = createScheme(scheme);
                    sendResponse(response.toString(), con);
                    break;

                case "getUpdatedData":
                    response = getUpdatedData();
                    sendResponse(response.toString(), con);
                    break;

                case "getSchemeData":
                    response = getSchemeData(msg.getString("schemeName"));
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

                    scheme = msg.getString("schemeName"); //esquema al que pertenece

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

    /**
     * Este método es utilizado para obtener los datos actualizados
     * @return retorna un JSONObject con una respuesta según el estado de la creación del nuevo esquema
     */
    private JSONObject getUpdatedData() {
        JSONObject response = new JSONObject();
        String serializedSchemes;
        String serializedCollections;
        ObjectMapper mapper = new ObjectMapper();

        try {
            serializedSchemes = mapper.writeValueAsString(schemes);
            serializedCollections = mapper.writeValueAsString(collections);
            response.put("status", "success");
            response.put("schemes", serializedSchemes);
            response.put("collections", serializedCollections);

        } catch (JsonProcessingException e) {
            response.put("status", "failed");
            response.put("error", "Serialize");
        }

        return response;
    }

    /**
     * Este método es utilizado parta obtener los datos referentes a los esquemas
     * @param schemeName
     * @return retorna un JSONObject con una respuesta según el estado de la creación del nuevo esquema
     */
    private JSONObject getSchemeData(String schemeName) {
        JSONObject response = new JSONObject();
        ObjectMapper mapper = new ObjectMapper();
        String scheme = schemes.get(schemeName);
        String serializedCollection;

        try {
            Hashtable<String, String> schemeCollection = collections.get(schemeName);
            serializedCollection = mapper.writeValueAsString(schemeCollection);

            response.put("status", "success");
            response.put("scheme", scheme);
            response.put("collection", serializedCollection);

        } catch (IOException e) {
            response.put("status", "failed");
            response.put("error", "serialize");
        }

        return response;
    }

    /**
     * Este método se encarga de crear un JSONObject con la información que debe poseer un nuevo esquema
     * @param newScheme Recibe el nombre del nuevo esquema por crear
     * @return retorna un JSONObject con una respuesta según el estado de la creación del nuevo esquema
     */
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

    /**
     * Este método se encarga de la eliminación de esquemas existentes
     * @param name Recibe el nombre del esquema el cual se espera eliminar
     * @return retorna un JSONObject con una respuesta según el estado de la creación del nuevo esquema
     */
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

    /**
     * Este método se encargará de eliminar un esquema del Hash en el que se almacenan todos los esquemas
     * @param name recibe el nombre del esquema que se quiere eliminar
     * @return retorna un JSONObject con una respuesta según el estado de la creación del nuevo esquema
     */
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

    /**
     * Este método se encarga de la inserción de datos a un esquema ya creado
     * @param scheme recibe el nombre del esquema al que corresponde agregarse los datos
     * @param attr recibe un JSONArray del tipo de dato al cual se le quiere agregar datos
     * @return retorna un JSONObject con una respuesta según el estado de la creación del nuevo esquema
     */
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

    /**
     * Este método se realciona con la acción de eliminar datos pertenecientes a un esquema específico
     * @param scheme recibe el nombre del esquema al cual se le quiere eliminar datos
     * @param primaryKeys recibe un JSONArray a partir del cual es que se va a eliminar los datos
     * @return retorna un JSONObject con una respuesta según el estado de la creación del nuevo esquema
     */
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
        "searchByJoin" : true o false  Si es true, entonces el campo de busqueda va a ser de
                                        la estructura del join
    }
                     */

    /**
     * Este método se encarga de la consulta de datos sobre un esquema específico
     * @param parameters recibe un string con lo que se desee buscar
     * @return retorna un JSONObject con una respuesta según el estado de la creación del nuevo esquema
     */
    private JSONObject queryData(String parameters) {
        JSONObject response = new JSONObject();
        JSONObject parametersObject = new JSONObject(parameters);

        JSONObject result;

        String scheme = parametersObject.getString("scheme"); // Esquema seleccionado
        String columnToSearch = parametersObject.getString("searchBy"); // Atributo del esquema
        boolean index = parametersObject.getBoolean("index"); // Si se usa índice o no
        boolean searchByJoin = parametersObject.getBoolean("searchByJoin"); // Si es atributo seleccionado es de tipo join
        String dataToSearch = parametersObject.getString("dataToSearch"); // Valor a buscar


        if (index && !searchByJoin) {
            //TODO hacer busqueda por indice sin columna de join
            System.out.println("Buscando por indice");

        } else if (!index && searchByJoin) {
            System.out.println("Busqueda lineal de columna del join");

            String joinName = parametersObject.getString("joinName"); // Nombre del esquema con que se hace join
            result = searchByJoinColumn(scheme, columnToSearch, dataToSearch, joinName);
            return result;

        } else if (index) {
            //TODO hacer busqueda por indice en columna de join
            System.out.println("Busqueda por indice en columna del join");

        } else {
            System.out.println("Busqueda secuencial sin columna de join");

            result = sequentialSearch1(scheme, columnToSearch, dataToSearch);
            return result;

        }

        response.put("status", "failed");
        return response;

    }

    /**
     * En este método se realiza una búsqueda secuencial de los datos
     * @param scheme recibe el esquema sobtre el que se va a buscar
     * @param columnToSearch recibe el tipo de dato correspondiente a la columna a la cual pertenece este dato graficamente
     * @param dataToSearch recibe el dato a buscar
     * @return retorna un resultado de este proceso
     */
    private JSONObject sequentialSearch1(String scheme, String columnToSearch, String dataToSearch){
        JSONObject result = new JSONObject();
        JSONObject schemeJson = new JSONObject();
        JSONObject joinJson =  new JSONObject();
        JSONArray arrayJoinAttr = new JSONArray();
        JSONArray arrayJoinName = new JSONArray();

        JSONArray arraySchemeAttr = new JSONArray();
        JSONArray arraySchemeName = new JSONArray();

        if (schemes.containsKey(scheme)){
            if (collections.containsKey(scheme)) {
                Hashtable<String, String> collectionsOfScheme = collections.get(scheme);
                JSONObject schemeStructure = new JSONObject(schemes.get(scheme));
                JSONArray attrSize =  schemeStructure.getJSONArray("attrSize");
                int columnIndex = searchIndexOfArray(columnToSearch, schemeStructure);
                JSONArray indexOfJoins = searchIndexOfJoin(schemeStructure);

                if (columnIndex > -1){
                    for (String key : collectionsOfScheme.keySet()){
                        JSONArray attributes = new JSONArray(collectionsOfScheme.get(key));

                        if (attributes.getString(columnIndex).contains(dataToSearch)){
                            System.out.println("Register found");

                            JSONArray register = new JSONArray(collectionsOfScheme.get(key));

                            arraySchemeName.put(scheme);
                            arraySchemeAttr.put(register.toString());

                            if (indexOfJoins.length() > 0){
                                System.out.println("Hay joins... Recuperando datos...");

                                for (int i=0; i<indexOfJoins.length(); i++){
                                    int join = indexOfJoins.getInt(i);

                                    System.out.println("join " + attrSize.get(join));
                                    System.out.println(register.getString(join));

                                    String joinPk = register.getString(join);
                                    String joinName = attrSize.getString(join);
                                    String attributesActualJoin = searchAttr(joinPk, joinName);

                                    arrayJoinAttr.put(attributesActualJoin);
                                    arrayJoinName.put(joinName);
                                }
                            }
                        }
                    }

                    return getJsonObject(result, schemeJson, joinJson, arrayJoinAttr, arrayJoinName, arraySchemeAttr, arraySchemeName);
                }

            } else {
                System.out.println("El esquema no tiene colecciones de datos");
            }
        }else {
            System.out.println("El esquema no existe");
        }
        result.put("status", "failed");
        return result;
    }

    /**
     * En este método se realizará una búsqueda pero partiendo de la columna de tipo join
     * @param scheme recibe el esquema sobtre el que se va a buscar
     * @param columnToSearch recibe el tipo de dato correspondiente a la columna a la cual pertenece este dato graficamente
     * @param dataToSearch recibe el dato a buscar
     * @param joinName recibe el nombre de la columna que es de tipo join
     * @return retorna un resultado de este proceso
     */
    private JSONObject searchByJoinColumn(String scheme, String columnToSearch, String dataToSearch, String joinName) {
        JSONObject result = new JSONObject();
        JSONObject schemeJson = new JSONObject();
        JSONObject joinJson = new JSONObject();

        JSONArray arrayJoinAttr = new JSONArray();
        JSONArray arrayJoinName = new JSONArray();

        JSONArray arraySchemeAttr = new JSONArray();
        JSONArray arraySchemeName = new JSONArray();

        /*
        recorrer lista de estudiantes
        obtener pk del join con que se esta buscando
        obtener la columna porque se esta buscando
        obtener el dato que esta en esa columna
        compararlo con el dato que se esta buscando
         */



        Hashtable<String, String> collectionsOfScheme = collections.get(scheme);

        JSONObject schemeStructure = new JSONObject(schemes.get(scheme));
        JSONArray attrType = schemeStructure.getJSONArray("attrType");
        JSONArray attrSize = schemeStructure.getJSONArray("attrSize");
        JSONArray indexOfJoins = searchIndexOfJoin(schemeStructure);

        //get join index
        int joinIndex = getJoinIndex(attrType, attrSize, joinName);

        //remove join of search
        indexOfJoins = updateIndexOfJoins(indexOfJoins, joinIndex);


        Hashtable<String, String> collectionsOfJoin = collections.get(joinName);
        JSONObject joinStructure = new JSONObject(schemes.get(joinName));
        JSONArray attrName = joinStructure.getJSONArray("attrName");

        //get column index
        int columnIndex = getColumnIndex(attrName, columnToSearch);

        for (String key : collectionsOfScheme.keySet()){
            JSONArray attributes = new JSONArray(collectionsOfScheme.get(key));
            String joinPk;
            if (joinIndex > -1) {
                joinPk = attributes.getString(joinIndex);

                JSONArray attributesActualJoin = new JSONArray(collectionsOfJoin.get(joinPk));

                if (columnIndex > -1){
                    String dataToCompare = attributesActualJoin.getString(columnIndex);

                    if (dataToCompare.equals(dataToSearch)) {

                        arraySchemeAttr.put(attributes.toString());
                        arraySchemeName.put(scheme);

                        arrayJoinAttr.put(attributesActualJoin.toString());
                        arrayJoinName.put(joinName);

                        //search other joins
                        if (indexOfJoins.length() > 0){
                            System.out.println("Hay joins... Recuperando datos...");

                            for (int i=0; i<indexOfJoins.length(); i++){
                                int join = indexOfJoins.getInt(i);

                                String otherJoinPk = attributes.getString(join);
                                String otherJoinName = attrSize.getString(join);
                                String attributesActJoin = searchAttr(otherJoinPk, otherJoinName);

                                arrayJoinAttr.put(attributesActJoin);
                                arrayJoinName.put(otherJoinName);
                            }
                        }
                    }

                }


            }

        }


        return getJsonObject(result, schemeJson, joinJson, arrayJoinAttr, arrayJoinName, arraySchemeAttr, arraySchemeName);

    }

    /**
     * Este método es el encargado de construir el objeto JSONObject con la información general, los cuales son los parámtros siguientes
     * @param result
     * @param schemeJson
     * @param joinJson
     * @param arrayJoinAttr
     * @param arrayJoinName
     * @param arraySchemeAttr
     * @param arraySchemeName
     * @return retorna un objeto Json con toda la información general
     */
    private JSONObject getJsonObject(JSONObject result, JSONObject schemeJson, JSONObject joinJson, JSONArray arrayJoinAttr, JSONArray arrayJoinName, JSONArray arraySchemeAttr, JSONArray arraySchemeName) {
        schemeJson.put("attributes", arraySchemeAttr.toString());
        schemeJson.put("scheme", arraySchemeName.toString());

        joinJson.put("attributesJoin", arrayJoinAttr);
        joinJson.put("joinName", arrayJoinName);

        result.put("status", "success");
        result.put("scheme", schemeJson.toString());
        result.put("join", joinJson.toString());
        return result;
    }

    /**
     * Se obtiene el índice de la columna por la cual se esta realizando la búsqueda
     * @param attrName
     * @param columnToSearch
     * @return
     */
    private int getColumnIndex(JSONArray attrName, String columnToSearch) {
        for (int i=0; i<attrName.length(); i++){
            if (attrName.getString(i).equals(columnToSearch)){
                return i; //obtengo el indice de la columna por la cual estoy buscando
            }
        }
        return -1;
    }

    /**
     * Se actualiza el índice de los datos de tipo join
     * @param indexOfJoins
     * @param joinIndex
     * @return devuelve lo mismo pero luego de ser ordenado
     */
    private JSONArray updateIndexOfJoins(JSONArray indexOfJoins, int joinIndex) {
        if (joinIndex!=-1) {
            for (int i = 0; i < indexOfJoins.length(); i++) {
                if (indexOfJoins.getInt(i) == joinIndex){
                    indexOfJoins.remove(i);
                }
            }
        }
        return indexOfJoins;
    }

    /**
     * Se obtiene el índice de un tipo join, según es buscado
     * @param attrType
     * @param attrSize
     * @param joinName
     * @return
     */
    private int getJoinIndex(JSONArray attrType, JSONArray attrSize, String joinName) {
        for (int i=0; i<attrType.length();i++){
            if (attrType.getString(i).equals("join")){
                if (attrSize.getString(i).equals(joinName)){
                    return i; //obtener el indice de donde esta la pk del join por el cual se esta buscando
                }
            }
        }

        return -1;
    }

    /**Se va a buscar un Atributo de un esquema
     *
     * @param joinPk
     * @param scheme
     * @return
     */
    private String searchAttr(String joinPk, String scheme) {

        Hashtable<String, String> schemeOfJoin = collections.get(scheme);
        return schemeOfJoin.get(joinPk);
    }

    /**
     * Este método se ca a encargar de realizar una búsqueda a partir del índice de unidos /******
     * @param schemeObject
     * @return
     */
    private JSONArray searchIndexOfJoin(JSONObject schemeObject) {
        JSONArray index = new JSONArray();
        String attrType = schemeObject.get("attrType").toString();
        JSONArray types = new JSONArray(attrType);

        for (int i=0; i<types.length(); i++){
            if (types.get(i).equals("join")){
                index.put(i);
            }
        }

        return index;

    }

    /**
     * Este método funciona para buscar un índice de tamaño
     * @param columnToSearch
     * @param schemeObject
     * @return
     */
    private int searchIndexOfArray(String columnToSearch, JSONObject schemeObject) {
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

    private void stopServer() {
        this.isRunning = false;
    }

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

