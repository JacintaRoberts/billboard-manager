package server;

import helpers.Helpers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import static helpers.Helpers.bytesToString;
import static helpers.Helpers.networkPropsFilePath;
import static java.lang.Boolean.parseBoolean;
import static server.Server.ServerAcknowledge.*;

public class Server {
    // Session tokens are stored in memory on server as per the specification
    private static HashMap<String, ArrayList<Object>> validSessionTokens = new HashMap<String, ArrayList<Object>>();
    // Private connection to database declaration
    private static Connection db;
    // Initialise parameters for determining server method to call
    private static String module = null;
    private static String method = null;
    private static String sessionToken = null;
    private static String[] additionalArgs = new String[0];

    // Different permissions that are available
    public enum Permission {
        CreateBillboard,
        EditBillboard,
        ScheduleBillboard,
        EditUser
    }

    // Different server acknowledgments that are available
    public enum ServerAcknowledge {
        Success,
        InsufficientPermission,
        InvalidToken,
        PrimaryKeyClash, // DB issue
        BadPassword, // Login
        NoSuchUser // Login
    }

    /**
     * Generates a sessionToken and adds it to the HashMap of valid session tokens
     * @param username that was successfully validated and logged in
     * @return a session token key that is a random 32-bit integer and stored in the HashMap of valid session tokens
     */
    public static String generateToken(String username) {
        LocalDateTime creationTime = LocalDateTime.now(); // Generate current date time
        ArrayList<Object> values = new ArrayList<>();
        values.add(username);
        values.add(creationTime);
        // Generate session token key
        Random rng = new Random();
        byte[] sessionTokenBytes = new byte[32]; // Technically there is a very small chance the same token could be generated (primary key clash)
        rng.nextBytes(sessionTokenBytes);
        String sessionToken = bytesToString(sessionTokenBytes);
        System.out.println("Before keys: " + validSessionTokens.keySet());
        validSessionTokens.put(sessionToken, values);
        System.out.println("After keys: " + validSessionTokens.keySet());
        return sessionToken;
    }

    /**
     * Validates the provided session token
     * @param sessionToken to be validated
     * @return boolean true if the session token exists, false otherwise
     */
    public static boolean validateToken(String sessionToken) throws IOException, SQLException {
        System.out.println("All keys: " + validSessionTokens.keySet());
        String username = (String) validSessionTokens.get(sessionToken).get(0);
        System.out.println("Username of the session token: " + username);
        if (UserAdmin.userExists(username)) {
            return validSessionTokens.containsKey(sessionToken); // Check if there is a valid session token for the existing user
        }
        return false; // Return false as the user does not exist anymore
    }

    /**
     * Checks whether the provided username has the required permissions to invoke a particular function
     * @param sessionToken with the username to be checked
     * @param requiredPermission required permission to execute the server method
     * @return boolean true if the session token exists and the user has the required permission, false otherwise
     */
    public static boolean checkPermission(String sessionToken, Permission requiredPermission) throws IOException, SQLException {
        String username = (String) validSessionTokens.get(sessionToken).get(0); // Extract username from session token
        System.out.println("Username of the session token: " + username);
        if (hasPermission(DbUser.retrieveUser(username), requiredPermission)) {
            return true; // User has required permission
        }
        return false; // Return false as the user does not have the required permission
    }

    // Helper method to determine whether the retrieved user has the required permission
    private static boolean hasPermission(ArrayList<String> retrievedUser, Permission requiredPermission) {
        System.out.println("Checking if the user has the permissions...");
        switch (requiredPermission) {
            case CreateBillboard:
                if ( retrievedUser.get(3).equals("1") ) return true;
                return false;
            case EditBillboard:
                if ( retrievedUser.get(4).equals("1") ) return true;
                return false;
            case ScheduleBillboard:
                if ( retrievedUser.get(5).equals("1") ) return true;
                return false;
            case EditUser:
                if ( retrievedUser.get(6).equals("1") ) return true;
                return false;
            default:
                return false; // Default to false if permission cannot be identified
        }
    }

    /**
     * listenforConnections creates a ServerSocket that is bound on the specified port
     * @param port The port number to begin listening on
     * @return ServerSocket bound on the given port number
     */
    public static ServerSocket listenForConnections(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        return serverSocket;
    }

    /**
     * initServer initialises the Server to begin listening for connections from clients (TCP/IP) through a
     * ServerSocket, this continually loops so that multiple connections can be handled.
     * Port number is read from network.props and is bound to the ServerSocket.
     * Client will later connect to the server socket with a port number and
     * the communication is handled through input/output streams.
     * General usage notes: always flush streams at the correct time + interleave the operations so that they
     * are in the same order. Write, read on client = Read, write on server (flush in between).
     * Ensure that every object sent across implements "Serializable" to convert to bytes.
     */
    private static void initServer() throws IOException, SQLException, NoSuchAlgorithmException {
        // Read port number from network.props
        final int port = Helpers.getPort(networkPropsFilePath);
        // Bind port number and begin listening, loop to keep receiving connections from clients
        ServerSocket serverSocket = listenForConnections(port);
        System.out.println("Server has begun listening on port: " + port);
        for (;;) {
            // Accept client
            Socket socket = serverSocket.accept();
            System.out.println("Connected to " + socket.getInetAddress());

            // Input/Output Stream setup
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            // Read client's request
            String clientRequest = ois.readUTF();
            System.out.println("Received from client: " + clientRequest);

            // Work out what method to call and send back to client
            Object serverResponse = callServerMethod(clientRequest);
            System.out.println("Server sending: " + serverResponse);
            oos.writeObject(serverResponse);
            oos.flush();

            // Cleanup
            oos.close();
            ois.close();
            socket.close();
        }
    }

    /**
     * callServerMethod calls the corresponding method from the server which fetches/updates data from
     * the database as necessary.
     * @param clientRequest String to indicate the method that the client requests
     * @return Server's response (Object which contains data from database/acknowledgement)
     */
    private static Object callServerMethod(String clientRequest) throws IOException, SQLException, NoSuchAlgorithmException {
        String [] clientArgs = clientRequest.split(",");
        if (clientArgs.length >= 4) { additionalArgs = Arrays.copyOfRange(clientArgs, 3, clientArgs.length); }
        if (clientArgs.length >= 3) { sessionToken = clientArgs[2]; } // Third argument is the session token
        if (clientArgs.length >= 2) { method = clientArgs[1]; } // Second argument is the method
        if (clientArgs.length >= 1) { module = clientArgs[0]; } // First argument is the module
        // Determine which method to execute
        switch (module) {
            case "Viewer":
                return "BillboardXMLObject"; // TODO: Actually implement this method to return the object
            case "User":
                return callUserAdminMethod();
            case "Billboard":
                return callBillboardAdminMethod();
            case "Schedule":
                return callScheduleAdminMethod();
            case "Logout":
                return logout(sessionToken); // Returns string acknowledgement
            case "Login":
                String username = clientArgs[1]; // Overwrites method position
                String hashedPassword = clientArgs[2]; // Overwrites session token position (does not require)
                return login(username, hashedPassword); // Returns session token or fail message
            default:
                return "No server method requested";
        }
    }


    /**
     * callUserAdminMethod calls the corresponding method from the UserAdmin class which fetches/updates data from
     * the database as necessary.
     * @return Server's response (Object which contains data from database/acknowledgement)
     */
    private static Object callUserAdminMethod() throws IOException, SQLException, NoSuchAlgorithmException {
        // Determine which method from UserAdmin to execute
        switch (method) {
            case "CreateUser":
                String username = additionalArgs[0];
                String hashedPassword = additionalArgs[1];
                boolean createBillboard = parseBoolean(additionalArgs[2]);
                boolean editBillboard = parseBoolean(additionalArgs[3]);
                boolean scheduleBillboard = parseBoolean(additionalArgs[4]);
                boolean editUser = parseBoolean(additionalArgs[5]);
                return UserAdmin.createUser(sessionToken, username, hashedPassword, createBillboard, editBillboard,
                        scheduleBillboard, editUser); // Returns session token or fail message
            case "DeleteUser":
                username = additionalArgs[0];
                return UserAdmin.deleteUser(sessionToken, username); // Returns server acknowledgment of deletion or fail message
            default:
                return "No UserAdmin method requested";
        }
    }


    /**
     * callBillboardAdminMethod calls the corresponding method from the server which fetches/updates data from
     * the database as necessary.
     * @return Server's response (Object which contains data from database/acknowledgement)
     */
    private static Object callBillboardAdminMethod() throws IOException, SQLException {
        // Determine which method from BillboardAdmin to execute
        switch (module) {
            case "CreateBillboard":
                String billboardName = additionalArgs[0];
                String xmlCode = additionalArgs[1];
                return BillboardAdmin.createBillboard("userNameReturn",billboardName,xmlCode);
            case "EditBillboard":
                String originalBillboardName = additionalArgs[0];
                String newXmlCode = additionalArgs[1];
                return BillboardAdmin.editBillboard(originalBillboardName,newXmlCode);
            case "DeleteBillboard":
                String deleteBillboardName = additionalArgs[0];
                return BillboardAdmin.deleteBillboard(deleteBillboardName);
            default:
                return "No BillboardAdmin method requested";
        }
    }



    /**
     * callScheduleAdminMethod calls the corresponding method from the server which fetches/updates data from
     * the database as necessary.
     * @return Server's response (Object which contains data from database/acknowledgement)
     */
    private static Object callScheduleAdminMethod() {
        // Determine which method from ScheduleAdmin to execute
        switch (module) {
            case "CreateSchedule":
                //TODO: POPULATE WITH METHOD CALLS
                //String billboardName = additionalArgs[0];
                //String xmlCode = additionalArgs[1];
                return true; // BillboardAdmin.createBillboard("userNameReturn",billboardName,xmlCode);
            default:
                return "No ScheduleAdmin method requested";
        }
    }


    /**
     * logout server-side expires the provided session token to log out the user
     * @param sessionToken The session token of the login to be terminated
     * @return String acknowledgement from server which determines whether the expiration was successful
     */
    public static ServerAcknowledge logout(String sessionToken) {
        if (validSessionTokens.containsKey(sessionToken)) {
            validSessionTokens.remove(sessionToken);
            return Success;  // Session token existed and was successfully expired
        }
        return InvalidToken; // Session token was already expired/did not exist
    }


    /**
     * login server-side checks the username and password provided in the database, if they exist
     * and are correctly matched, then a session token is created and returned.
     * @param username The username entered by the user on the GUI
     * @param hashedPassword The hashed version of the password entered by the user on the GUI
     * @return A valid session token for the user or server acknowledgment for user/password mismatch
     */
    public static Object login(String username, String hashedPassword) throws IOException, SQLException, NoSuchAlgorithmException {
        if (UserAdmin.userExists(username)) {
            // Compares hashedPassword from CP with the password stored in the database (validation with salt)
            if (UserAdmin.checkPassword(username, hashedPassword)) {
                return generateToken(username); // 1. Good password, generate session token
            }
            return BadPassword; // 2. User exists, but bad password
        }
        return NoSuchUser; // 3. No such user
    }


    public static void main(String[] args) {
        try {
            db = DbConnection.getInstance();
            initServer();
        } catch (IOException e) {
            //TODO: May want to handle this IOException better (if fatal error close and restart maybe?)
            System.err.println("Server IO Exception caught: " + e);
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Database Connection Exception caught: " + e);
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

}