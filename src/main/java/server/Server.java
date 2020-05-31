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
import java.sql.SQLNonTransientConnectionException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static helpers.Helpers.networkPropsFilePath;
import static java.lang.Boolean.parseBoolean;
import static server.ScheduleAdmin.activeBillboardDisplay;
import static server.Server.ServerAcknowledge.*;
/**
 * The server class consists of the main logic of running the server. It is used to communicate with the Control Panel,
 * and sends and receives request from it. The viewer also communicates with the server to query for active billboards.
 */

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
    private static String[] concatString = new String[0];

    /**
     * The four different permissions that are available for the user to have
     */
    public enum Permission {
        /**
         * Users with the “Create Billboards” permission can create new billboards. They will also be able to
         * edit or delete any billboards they created, as long as those billboards are not presently scheduled.
         */
        CreateBillboard,
        /**
         * Users with the “Edit All Billboards” permission will be able to edit or delete
         * any billboard on the system, including billboards that are currently
         * scheduled.
         */
        EditBillboard,
        /**
         * Users with the “Schedule Billboards” permission will be able to schedule
         * billboards to be displayed on the Viewers.
         */
        ScheduleBillboard,
        /**
         * Users with the “Edit Users” permission (administrators) will be able to
         * access a list of all users and can both edit any user listed in the system as
         * well as create new users.
         */
        EditUser
    }


    /**
     * Server acknowledgments that are available and are sent to the Controller from the Server to indicate
     * success or exception occurred.
     */
    public enum ServerAcknowledge {
        /**
         * Successful completion of create/edit/delete event.
         */
        Success,
        /**
         * Login - The username was correct but the user entered the wrong password.
         */
        BadPassword,
        /**
         * The requested username does not exist in the database.
         */
        NoSuchUser,
        /**
         * Invalid session token sent - have the user re-login to confirm that they have not been deleted or have
         * had their session expire after 24 hours.
         */
        InvalidToken,
        /**
         * Database exception - there was a duplicate primary key attempted to be created (e.g. create user with same
         * username or billboard with same billboard name).
         */
        PrimaryKeyClash,
        /**
         * The username associated with the provided session token does not have the required permission to call
         * the requested method.
         */
        InsufficientPermission,
        /**
         * If the user tries to delete their own user from the database.
         */
        CannotDeleteSelf,
        /**
         * If the user tries to update their permissions to remove their own "Edit Users" permission.
         */
        CannotRemoveOwnAdminPermission,
        /**
         * When the duration and repeat does not sync up. The duration must be smaller than the repeat.
         */
        BadTimeRepeatDuration,
        /**
         * The schedule requested to be deleted, or retrieved does not exist.
         */
        ScheduleNotExists,
        /**
         * The billboard requested to be deleted, scheduled or retrieved does not exist.
         */
        BillboardNotExists,
        /**
         * The user tried to enter characters that were not allowed for the SQL database.
         * Only alphanumerical characters, dashes, underscores and spaces are allowed. Anything else not allowed.
         */
        InvalidCharacters
    }


    /**
     * Generates a sessionToken and adds it to the HashMap of valid session tokens.
     * @param username A String which is a username that was successfully validated and logged in.
     * @return Returns a String which is a session token key that is a random 32-bit integer and stored in the
     * HashMap of valid session tokens.
     */
    private static String generateToken(String username) {
        LocalDateTime creationTime = LocalDateTime.now(); // Generate current date time
        ArrayList<Object> values = new ArrayList<>();
        values.add(username);
        values.add(creationTime);
        String sessionToken = String.valueOf(UUID.randomUUID()); // Generate UUID to ensure unique session token key
        validSessionTokens.put(sessionToken, values);
        return sessionToken;
    }


    /**
     * Method to determine whether the creation time of a token is acceptable.
     * @param creationTime A LocalDateTime object which is the creation time of the token.
     * @return Returns a Boolean which is true if the creation time is within 24 hours, false if it is more than
     * 24 hours since creation.
     */
    private static boolean tokenIsCurrent(LocalDateTime creationTime){
        LocalDateTime currentTime = LocalDateTime.now(); // Generate current date time
        LocalDateTime acceptableTime = currentTime.minusHours(24);
        System.out.println("Acceptable time is: " + acceptableTime);
        System.out.println("Session token creation time is: " + creationTime);
        return creationTime.isAfter(acceptableTime);
    }


    /**
     * Validates the provided session token.
     * @param sessionToken A String which is the session token to be validated.
     * @return Returns a Boolean which is true if the session token exists, false otherwise.
     * @throws IOException Throws an exception if an I/O exception of some sort has occurred.
     * @throws SQLException Throws an exception if there is a database access error or other errors.
     */
    public static boolean validateToken(String sessionToken) throws IOException, SQLException {
        try {
            String username = getUsernameFromToken(sessionToken);
            System.out.println("Username of the session token: " + username);
            if (UserAdmin.userExists(username)) {
                // Check if there is a valid session token for the existing user
                if (validSessionTokens.containsKey(sessionToken)){
                    LocalDateTime creationTime = (LocalDateTime) validSessionTokens.get(sessionToken).get(1);
                    return tokenIsCurrent(creationTime); // Determine whether the creation time is still valid
                }
            }
        } catch (NullPointerException err) { // Token does not exist at all
            return false;
        }
        return false; // Return false as the user does not exist anymore or never did
    }


    /**
     * Retrieves the username of the provided session token.
     * @param sessionToken A String which is the session token to have the username retrieved from.
     * @return Returns a String which is the username stored with the session token.
     */
    public static String getUsernameFromToken(String sessionToken) {
        return (String) validSessionTokens.get(sessionToken).get(0);
    }


    /**
     * THis function creates a ServerSocket that is bound on the specified port.
     * @param port An int which is the port number to begin listening on.
     * @return Returns a ServerSocket bound on the given port number.
     * @throws IOException Throws an exception if an I/O exception of some sort has occurred.
     */
    public static ServerSocket listenForConnections(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        return serverSocket;
    }


    /**
     * initServer initialises the Server to begin listening for connections from clients (TCP/IP) through a
     * ServerSocket, this continually loops so that multiple connections can be handled.
     * <p>
     * Port number is read from network.props and is bound to the ServerSocket.
     * <p>
     * Client will later connect to the server socket with a port number and
     * the communication is handled through input/output streams.
     * <p>
     * General usage notes: always flush streams at the correct time + interleave the operations so that they
     * are in the same order. Write, read on client = Read, write on server (flush in between).
     * <p>
     * Ensure that every object sent across implements "Serializable" to convert to bytes.
     * @throws IOException Throws an exception if an I/O exception of some sort has occurred.
     * @throws SQLException Throws an exception if there is a database access error or other errors.
     * @throws NoSuchAlgorithmException Throws an exception when the hashing algorithm requested does not exist.
     * @throws ClassNotFoundException Throws an exception when a specified class cannot be found in the classpath.
     */
    private static void initServer() throws IOException, SQLException, NoSuchAlgorithmException, ClassNotFoundException {
        // Read port number from network.props
        final int port = Helpers.getPort(networkPropsFilePath);
        // Bind port number and begin listening, loop to keep receiving connections from clients
        ServerSocket serverSocket = listenForConnections(port);
        System.out.println("Server has begun listening on port: " + port);
        BillboardAdmin.createBillboardTable();
        ScheduleAdmin.createScheduleTable();
        DbUser.createUserTable();
        for (;;) {
            // Accept client
            Socket socket = serverSocket.accept();
            System.out.println("Connected to " + socket.getInetAddress());

            // Input/Output Stream setup
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            // Read client's request
            Object clientRequest = ois.readObject();
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
     * This function calls the corresponding method from the server which fetches/updates data from
     * the database as necessary.
     * @param clientRequest A String to indicate the method that the client requests.
     * @return Returns an Object which is the server's response which contains data from database/acknowledgement.
     * @throws IOException Throws an exception if an I/O exception of some sort has occurred.
     * @throws SQLException Throws an exception if there is a database access error or other errors.
     * @throws NoSuchAlgorithmException Throws an exception when the hashing algorithm requested does not exist.
     */
    private static Object callServerMethod(Object clientRequest) throws IOException, SQLException, NoSuchAlgorithmException {
        System.out.println("Received from client: " + clientRequest);
        String clientStringRequest;
        byte[] pictureData = null;
        try { // Try to cast the received request immediately to a string
            clientStringRequest = (String) clientRequest;
        } catch ( ClassCastException e) {
            // A billboard object was instead sent, retrieve message field
            CpBillboard billboardReceived = (CpBillboard) clientRequest;
            clientStringRequest = billboardReceived.getMessage();
            pictureData = billboardReceived.getPictureData();
        }
        // Filter string message
        String[] clientArgs = clientStringRequest.split(",");
        if (clientArgs.length >= 4) { additionalArgs = Arrays.copyOfRange(clientArgs, 3, clientArgs.length); }
        if (clientArgs.length >= 3) { sessionToken = clientArgs[2]; } // Third argument is the session token
        if (clientArgs.length >= 2) { method = clientArgs[1]; } // Second argument is the method
        if (clientArgs.length >= 1) { module = clientArgs[0]; } // First argument is the module

        // Determine which method to execute
        switch (module) {
            case "Viewer":
                return activeBillboardDisplay(); // TODO: Actually implement this method to return the object
            case "User":
                return callUserAdminMethod();
            case "Billboard":
                return callBillboardAdminMethod(pictureData);
            case "Schedule":
                return callScheduleAdminMethod();
            case "Logout":
                String sessionToken = clientArgs[1]; // No module required.
                return logout(sessionToken);
            case "Login":
                String username = clientArgs[1]; // No module or session token required.
                String hashedPassword = clientArgs[2];
                return login(username, hashedPassword);
            default:
                return "No server method requested";
        }
    }


    /**
     * This function calls the corresponding method from the UserAdmin class which fetches/updates data from
     * the database as necessary.
     * @return Returns an Object which is the server's response which contains data from database/acknowledgement.
     * @throws IOException Throws an exception if an I/O exception of some sort has occurred.
     * @throws SQLException Throws an exception if there is a database access error or other errors.
     * @throws NoSuchAlgorithmException Throws an exception when the hashing algorithm requested does not exist.
     */
    private static Object callUserAdminMethod() throws IOException, SQLException, NoSuchAlgorithmException {
        // Determine which method from UserAdmin to execute
        switch (method) {
            case "createUser":
                String username = additionalArgs[0];
                String hashedPassword = additionalArgs[1];
                boolean createBillboard = parseBoolean(additionalArgs[2]);
                boolean editBillboard = parseBoolean(additionalArgs[3]);
                boolean scheduleBillboard = parseBoolean(additionalArgs[4]);
                boolean editUser = parseBoolean(additionalArgs[5]);
                return UserAdmin.createUser(sessionToken, username, hashedPassword, createBillboard, editBillboard,
                        scheduleBillboard, editUser); // Returns session token or fail message
            case "deleteUser":
                username = additionalArgs[0];
                return UserAdmin.deleteUser(sessionToken, username); // Returns server acknowledgment of deletion or fail message
            case "listUsers":
                return UserAdmin.listUsers(sessionToken); // Returns string array list of usernames or server acknowledge error
            case "getPermissions":
                username = additionalArgs[0];
                return UserAdmin.getPermissions(sessionToken, username); // Returns boolean array list of usernames or server acknowledge error
            case "setPermissions":
                username = additionalArgs[0];
                createBillboard = parseBoolean(additionalArgs[1]);
                editBillboard = parseBoolean(additionalArgs[2]);
                scheduleBillboard = parseBoolean(additionalArgs[3]);
                editUser = parseBoolean(additionalArgs[4]);
                // Returns boolean array list of usernames or server acknowledge error
                return UserAdmin.setPermissions(sessionToken, username, createBillboard, editBillboard, scheduleBillboard, editUser);
            case "setPassword":
                username = additionalArgs[0];
                hashedPassword = additionalArgs[1];
                return UserAdmin.setPassword(sessionToken, username, hashedPassword); // Returns boolean array list of usernames or server acknowledge error
            default:
                return "No UserAdmin method requested";
        }
    }


    /**
     * This function calls the corresponding method from the server which fetches/updates data from
     * the database as necessary.
     * @param pictureData A byte[] array which is the picture data attribute of a base-64 encoded image.
     * @return Returns an Object which is the server's response which contains data from database/acknowledgement.
     * @throws IOException Throws an exception if an I/O exception of some sort has occurred.
     * @throws SQLException Throws an exception if there is a database access error or other errors.
     */
    private static Object callBillboardAdminMethod(byte[] pictureData) throws IOException, SQLException {
        // Determine which method from BillboardAdmin to execute
        switch (method) {
            case "CreateBillboard":
                String billboardName = additionalArgs[0];
                String creator = additionalArgs[1];
                String XMLCode = additionalArgs[2];
                if ( XMLHasCommas() ) {
                    System.out.println("XML has commas!");
                    // Rejoin the XML code into a single variable
                    concatString = Arrays.copyOfRange(additionalArgs, 2, additionalArgs.length);
                    XMLCode = String.join(",", concatString);
                }
                System.out.println("received contents!");
                System.out.println("pictureData is: " + pictureData);
                System.out.println("xmlCode is: " + XMLCode);
                return BillboardAdmin.createBillboard(sessionToken, billboardName, creator, XMLCode, pictureData);
            case "DeleteBillboard":
                String deleteBillboardName = additionalArgs[0];
                String deleteBillboardRequestor = additionalArgs[1];
                return BillboardAdmin.deleteBillboard(sessionToken,deleteBillboardName,deleteBillboardRequestor);
            case "DeleteAllBillboard":
                return BillboardAdmin.deleteAllBillboard();
            case "GetBillboard":
                String getBillboardName = additionalArgs[0];
                return BillboardAdmin.getBillboardInformation(sessionToken,getBillboardName);
            case "ListBillboard":
                return BillboardAdmin.listBillboard(sessionToken);
            default:
                return "No BillboardAdmin method requested";
        }
    }

    /**
     * This function determines whether the xml provided has commas in the message.
     * @return Returns a Boolean which is true if there are commas and false if there aren't
     */
    private static Boolean XMLHasCommas() {
        if ( additionalArgs.length > 3 ) {
            return true;
        }
        return false;
    }


    /**
     * This function calls the corresponding method from the server which fetches/updates data from
     * the database as necessary.
     * @return Returns an Object which is the server's response which contains data from database/acknowledgement.
     * @throws IOException Throws an exception if an I/O exception of some sort has occurred.
     * @throws SQLException Throws an exception if there is a database access error or other errors.
     */
    private static Object callScheduleAdminMethod() throws IOException, SQLException {
        // Determine which method from ScheduleAdmin to execute
        switch (method) {
            case "UpdateSchedule":
                String editBillboardName = additionalArgs[0];
                String editStartTime = additionalArgs[1];
                String editDuration = additionalArgs[2];
                String editCreationDateTime = additionalArgs[3];
                String editRepeat = additionalArgs[4];
                String editSunday = additionalArgs[5];
                String editMonday = additionalArgs[6];
                String editTuesday = additionalArgs[7];
                String editWednesday = additionalArgs[8];
                String editThursday = additionalArgs[9];
                String editFriday = additionalArgs[10];
                String editSaturday = additionalArgs[11];
                return ScheduleAdmin.updateSchedule(sessionToken, editBillboardName,editStartTime,editDuration,editCreationDateTime,editRepeat,
                        editSunday,editMonday,editTuesday,editWednesday,editThursday,editFriday,editSaturday);
            case "DeleteSchedule":
                String deleteScheduleName = additionalArgs[0];
                return ScheduleAdmin.deleteSchedule(sessionToken, deleteScheduleName);
            case "DeleteAllSchedule":
                return ScheduleAdmin.deleteAllSchedules();
            case "ListAllDaySchedule":
                String dayList = additionalArgs[0];
                return ScheduleAdmin.scheduleAllDayCP(sessionToken, dayList);
            case "ListABillboardSchedule":
                String BillboardSchedule = additionalArgs[0];
                return ScheduleAdmin.getScheduleInformation(sessionToken, BillboardSchedule);
            case "ListActiveSchedule":
                String day = additionalArgs[0];
                LocalTime currentTime = LocalTime.parse(additionalArgs[1]);
                return ScheduleAdmin.viewCurrentSchedule(ScheduleAdmin.listAllFilteredScheduleInformation(sessionToken, day), currentTime);
            default:
                return "No ScheduleAdmin method requested";
        }
    }


    /**
     * This logs the user out and the server-side expires the provided session token to log out the user.
     * @param sessionToken A String which is the session token of the login to be terminated.
     * @return Returns a ServerAcknowledge which is a message which determines whether the expiration was successful.
     */
    public static ServerAcknowledge logout(String sessionToken) {
        if (validSessionTokens.containsKey(sessionToken)) {
            validSessionTokens.remove(sessionToken);
            return Success;  // 1. Session token existed and was successfully expired
        }
        return InvalidToken; // 2. Session token was already expired/did not exist
    }


    /**
     * Expires all session tokens with an associated user.
     * @param username A String which is the username which will have all of its related tokens expired.
     * @return Server acknowledgment enum to indicate whether the expiration of tokens was successful, or if an
     * exception occurred (NoSuchUser means that the user requested could not be found).
     */
    public static ServerAcknowledge expireTokens(String username) {
        // Collect Session Tokens to be expired
        ServerAcknowledge serverAcknowledge = NoSuchUser;
        System.out.println("All keys: " + validSessionTokens.keySet());
        // Get the iterator over the HashMap
        Iterator<Map.Entry<String, ArrayList<Object>>> iterator = validSessionTokens.entrySet().iterator();
        // Iterate over the HashMap of valid session tokens
        while (iterator.hasNext()) {
            // Get the entry at this iteration
            Map.Entry<String, ArrayList<Object>> entry = iterator.next();
            System.out.println("Value being checked: " + entry.getValue().get(0));
            // Check if this value is the required value
            if (username.equals(entry.getValue().get(0))) {
                // Remove this entry from HashMap
                iterator.remove();
                System.out.println("A session token associated with: " + username + " was deleted");
                serverAcknowledge = Success;
            }
        }
        return serverAcknowledge;
    }


    /**
     * This logs in the user and the server-side login checks the username and password provided in the database, if
     * they exist and are correctly matched, then a session token is created and returned.
     * @param username A String which is the username entered by the user on the GUI.
     * @param hashedPassword A String which is the hashed version of the password entered by the user on the GUI.
     * @return Returns an Object which is a valid session token for the user or server acknowledgment for user/password mismatch.
     * @throws IOException Throws an exception if an I/O exception of some sort has occurred.
     * @throws SQLException Throws an exception if there is a database access error or other errors.
     * @throws NoSuchAlgorithmException Throws an exception when the hashing algorithm requested does not exist.
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


    /**
     * Main entry point for the Server program
     * @param args Command line arguments (not required)
     */
    public static void main(String[] args) {
        try {
            db = DbConnection.getInstance();
            initServer();
        } catch (IOException e) {
            System.err.println("Server IO Exception caught!");
            System.out.println("More information on this error: \n" + e);
        } catch (SQLException e) {
            System.err.println("Database/SQL Connection Exception caught!");
            System.out.println("Please check the connection to the database and re-run server application.");
            System.out.println("More information on this error: \n" + e);
        } catch (NoSuchAlgorithmException | ClassNotFoundException e) {
            System.err.println("Server code Exception caught!");
            System.out.println("More information on this error: \n" + e);
        }
    }


}