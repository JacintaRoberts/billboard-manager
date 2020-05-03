package server;

import helpers.Helpers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import static helpers.Helpers.networkPropsFilePath;

public class Server {
    // Session tokens are stored in memory on server as per the specification
    private static HashMap<String, ArrayList<Object>> validSessionTokens = new HashMap<String, ArrayList<Object>>();

    /**
     * addToken adds the given sessionToken to the Hashmap of valid session tokens
     * @param sessionToken to be added
     */
    public static void addToken(String sessionToken, String username) {
        // Generate current date time
        LocalDateTime creationTime = LocalDateTime.now(); // Create a date-time object
        ArrayList<Object> values = new ArrayList<>();
        values.add(username);
        values.add(creationTime);
        validSessionTokens.put(sessionToken, values);
    }

    /**
     * @param sessionToken to be added
     * @return boolean true if the session token exists, false otherwise
     */
    public static boolean validateToken(String sessionToken) {
        return validSessionTokens.containsKey(sessionToken);
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
    private static void initServer() throws IOException {
        // Read port number from network.props
        final int port = Helpers.getPort(networkPropsFilePath);
        // Bind port number and begin listening, loop to keep receiving connections from clients
        ServerSocket serverSocket = listenForConnections(port);
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
            //MyClass o = (MyClass) ois.readObject(); // Example casting to MyClass for reading object
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
    private static Object callServerMethod(String clientRequest) {
        String[] clientArgs = clientRequest.split(",");
        String method = clientArgs[0]; // First argument is the method
        String sessionToken = clientArgs[1]; // Second argument is the session token
        switch (method) {
            case "Viewer":
                return "BillboardXMLObject"; // TODO: Actually implement this method to return the object
            case "Logout":
                return logout(sessionToken); // Returns string acknowledgement
            case "Login":
                String username = clientArgs[1]; // Overwrite as this only method that doesn't send session token
                String hashedPassword = clientArgs[2]; //TODO: Hash this somewhere here as well
                return login(username, hashedPassword); // Returns session token
            default: {
                return "No server method requested";
            }
        }
    }

    /**
     * logout server-side expires the provided session token to log out the user
     * @param sessionToken The session token of the login to be terminated
     * @return String acknowledgement from server which determines whether the expiration was successful
     */
    public static String logout(String sessionToken) {
        if (validSessionTokens.containsKey(sessionToken)) {
            validSessionTokens.remove(sessionToken);
            return "Pass: Logout Successful";  // Session token existed and was successfully expired
        } else {
            return "Fail: Already Logged Out"; // Session token was already expired/did not exist
        }
    }

    /**
     * login server-side checks the username and password provided in the database, if they exist
     * and are correctly matched, then a session token is created and returned.
     * @param username The username entered by the user on the GUI
     * @param hashedPassword The hashed password entered by the user on the GUI
     * @return A valid session token for the user or server acknowledgment for user/password mismatch
     */
    public static String login(String username, String hashedPassword) {
        // TODO: Implement logic for salting, hashing etc. accessing database to check
        if (hashedPassword=="goodPass") {
            return "sessionToken";
        } else if (UserAdmin.userExists(username)) {
            return "Fail: Incorrect Password";
        } else {
            return "Fail: No Such User";
        }
    }

    public static void main(String[] args) {
        //TODO: May want to handle this IOException better (if fatal error close and restart maybe?)
        try {
            DbConnection db = new DbConnection();
            initServer();
        } catch (IOException e) {
            System.err.println("Exception caught: " + e);
        }
    }


}