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
import java.time.LocalTime;
import java.util.*;

import static helpers.Helpers.bytesToString;
import static helpers.Helpers.networkPropsFilePath;
import static java.lang.Boolean.parseBoolean;
import static server.Server.ServerAcknowledge.*;

public class Server {
    // Session tokens are stored in memory on server as per the specification
    static HashMap<String, ArrayList<Object>> validSessionTokens = new HashMap<String, ArrayList<Object>>();
    // Private connection to database declaration
    private static Connection db;
    // Initialise parameters for determining server method to call
    private static String module = null;
    private static String method = null;
    private static String sessionToken = null;
    private static String[] additionalArgs = new String[0];
    private static final int TOKEN_SIZE = 32; // Constant for the number of bytes in a session token

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
        CannotDeleteSelf, // Delete user handling
        CannotRemoveOwnAdminPermission, // Set user permissions handling
        BadPassword, // Login
        NoSuchUser; // Login
    }

    /**
     * Generates a sessionToken and adds it to the HashMap of valid session tokens
     * @param username that was successfully validated and logged in
     * @return a session token key that is a random 32-bit integer and stored in the HashMap of valid session tokens
     */
    private static String generateToken(String username) {
        LocalDateTime creationTime = LocalDateTime.now(); // Generate current date time
        ArrayList<Object> values = new ArrayList<>();
        values.add(username);
        values.add(creationTime);
        // Generate session token key
        Random rng = new Random();
        byte[] sessionTokenBytes = new byte[TOKEN_SIZE]; // Technically there is a very small chance the same token could be generated (primary key clash)
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
        try {
            System.out.println("All keys: " + validSessionTokens.keySet());
            String username = getUsernameFromToken(sessionToken);
            System.out.println("Username of the session token: " + username);
            if (UserAdmin.userExists(username)) {
                return validSessionTokens.containsKey(sessionToken); // Check if there is a valid session token for the existing user
            }
        } catch (NullPointerException err) {
            return false;
        }
        return false; // Return false as the user does not exist anymore or never did
    }

    /**
     * Retrieves the username of the provided session token
     * @param sessionToken to have the name retrieved from
     * @return String username stored with the session token
     */
    public static String getUsernameFromToken(String sessionToken) {
        return (String) validSessionTokens.get(sessionToken).get(0);
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
            case "ListUsers":
                return UserAdmin.listUsers(sessionToken); // Returns string array list of usernames or server acknowledge error
            case "getPermissions":
                username = additionalArgs[0];
                return UserAdmin.getPermissions(sessionToken, username); // Returns boolean array list of usernames or server acknowledge error
            case "setPassword":
                username = additionalArgs[0];
                hashedPassword = additionalArgs[1];
                return UserAdmin.setPassword(sessionToken, username, hashedPassword); // Returns boolean array list of usernames or server acknowledge error
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
        switch (method) {
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
            case "DeleteAllBillboard":
                return BillboardAdmin.deleteAllBillboard();
            case "GetBillboard":
                String getBillboardName = additionalArgs[0];
                return BillboardAdmin.getBillboardInformation(getBillboardName);
            case "ListBillboard":
                return BillboardAdmin.listBillboard();
            default:
                return "No BillboardAdmin method requested";
        }
    }



    /**
     * callScheduleAdminMethod calls the corresponding method from the server which fetches/updates data from
     * the database as necessary.
     * @return Server's response (Object which contains data from database/acknowledgement)
     */
    private static Object callScheduleAdminMethod() throws IOException, SQLException {
        // Determine which method from ScheduleAdmin to execute
        switch (method) {
            case "CreateSchedule":
                String billboardName = additionalArgs[0];
                String startTime = additionalArgs[1];
                String duration = additionalArgs[2];
                String creationDateTime = additionalArgs[3];
                String repeat = additionalArgs[4];
                String sunday = additionalArgs[5];
                String monday = additionalArgs[6];
                String tuesday = additionalArgs[7];
                String wednesday = additionalArgs[8];
                String thursday = additionalArgs[9];
                String friday = additionalArgs[10];
                String saturday = additionalArgs[11];
                return ScheduleAdmin.createSchedule(billboardName,startTime,duration,creationDateTime,repeat,
                sunday,monday,tuesday,wednesday,thursday,friday,saturday);
            case "EditSchedule":
                String editbillboardName = additionalArgs[0];
                String editstartTime = additionalArgs[1];
                String editduration = additionalArgs[2];
                String editcreationDateTime = additionalArgs[3];
                String editrepeat = additionalArgs[4];
                String editsunday = additionalArgs[5];
                String editmonday = additionalArgs[6];
                String edittuesday = additionalArgs[7];
                String editwednesday = additionalArgs[8];
                String editthursday = additionalArgs[9];
                String editfriday = additionalArgs[10];
                String editsaturday = additionalArgs[11];
                return ScheduleAdmin.editSchedule(editbillboardName,editstartTime,editduration,editcreationDateTime,editrepeat,
                        editsunday,editmonday,edittuesday,editwednesday,editthursday,editfriday,editsaturday);
            case "DeleteSchedule":
                String deleteScheduleName = additionalArgs[0];
                return ScheduleAdmin.deleteSchedule(deleteScheduleName);
            case "DeleteAllSchedule":
                return ScheduleAdmin.deleteAllSchedules();
            case "ListAllDaySchedule":
                String dayList = additionalArgs[0];
                return ScheduleAdmin.listAllFilteredScheduleInformation(dayList);
            case "ListABillboardSchedule":
                String BillboardSchedule = additionalArgs[0];
                return ScheduleAdmin.getScheduleInformation(BillboardSchedule);
            case "ListActiveSchedule":
                String day = additionalArgs[0];
                LocalTime currentTime = LocalTime.parse(additionalArgs[1]);
                return ScheduleAdmin.viewCurrentSchedule(ScheduleAdmin.listAllFilteredScheduleInformation(day), currentTime);
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

    // Expires all session tokens with an associated user
    public static ServerAcknowledge expireTokens(String username){
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