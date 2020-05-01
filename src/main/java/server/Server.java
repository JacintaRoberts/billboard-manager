package server;

import helpers.Helpers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final static String networkPropsFilePath = "src\\main\\resources\\network.props";
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
    private static void initServer() throws IOException, ClassNotFoundException {
        // Read port number from network.props
        //final String networkPropsFilePath = "src\\main\\resources\\network.props";
        final int port = Helpers.getPort(networkPropsFilePath);
        // Bind port number and begin listening, loop to keep receiving connections from clients
        ServerSocket serverSocket = new ServerSocket(port);
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
        switch (clientRequest) {
            case "Test": {
                return  "Test successful!";
            }
            // TODO: THIS IS GOING TO BE A LONG SWITCH STATEMENT BUT THIS IS THE IDEA, WOULD LIKE TO DO:
            //  clientRequest.startsWith("UserAdmin,AddUser") because we will likely append the
            //  arguments: username and pass etc. to the end...IS THERE A BETTER WAY?
            case "UserAdmin,AddUser":
                return UserAdmin.addUser();
            case "Viewer":
                return "BillboardXMLObject"; // TODO: Actually implement this method to return the object
            default: {
                return "Connection successful!";
            }
        }
    }

    public static void main(String[] args) {
        //TODO: May want to handle this IOException better (if fatal error close and restart maybe?)
        try {
            initServer();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Exception caught: " + e);
        }
    }
}