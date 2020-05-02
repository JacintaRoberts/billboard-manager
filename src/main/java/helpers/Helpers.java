package helpers;

import java.io.*;
import java.net.Socket;
import java.util.Properties;

public class Helpers {

    /**
     * Reads the properties from the specified file (.props)
     * @param filePath The string file path to be read from
     * @return Properties object that can have values retrieved from it
     * @throws IOException - If the file cannot be found
     */
    public static Properties readProps(String filePath) throws IOException {
        FileReader reader = new FileReader(filePath);
        Properties props = new Properties();
        props.load(reader);
        return props;
    }

    /**
     * Parses a String port number to an integer and ensures that it is within the acceptable range
     * of 0 to 65535.
     * @param port The string port number to be parsed and validated
     * @return Integer representation of the port number
     * @throws BadPortNumberException - For port numbers less than 0 or greater than 65535
     */
    public static int validatePort(String port) throws BadPortNumberException {
        int portNum = Integer.parseInt(port);
        if ( portNum < 0 ) {
            throw new BadPortNumberException("Port number in network.props must not be less than 0.");
        }
        else if (portNum > 65535) {
            throw new BadPortNumberException("Port number in network.props must not be greater than 65535.");
        }
        else {
            return portNum;
        }
    }

    /**
     * Retrieves the port number from the filePath, validates and parses as int
     * @param filePath The string file path to be read from
     * @return port number (integer)
     */
    public static int getPort(String filePath) {
        String stringPort = null;
        try {
            stringPort = Helpers.readProps(filePath).getProperty("port");
        } catch (IOException e) {
            System.err.println("Exception caught: "+e);
        }

        int port = -1;
        try {
            port = Helpers.validatePort(stringPort);
        } catch (BadPortNumberException e) {
            System.err.println("Exception caught: "+e);
        }
        return port;
    }


    /**
     * Retrieves the ip address from the filePath as a string
     * @param filePath The string file path to be read from
     * @return ip address string
     */
    public static String getIp(String filePath) {
        String ip = null;
        try {
            ip = Helpers.readProps(filePath).getProperty("ip");
        } catch (IOException e) {
            System.err.println("Exception caught: "+e);
        }
        return ip;
    }



    /**
     * initClient initialises a client to send a message to the server via a Socket with a port number and IP
     * from the network.props file. Communication is handled through input/output streams.
     * //TODO: Methods can be requested if the message sent follows the pattern...
     * General usage notes: always flush streams at the correct time + interleave the operations so that they
     * are in the same order. Write, read on client = Read, write on server (flush in between).
     * Ensure that every object sent across implements "Serializable" to convert to bytes.
     * @param message String message to be send to the server (indicates method)
     * @return Server's response (either acknowledgement or data object from database connection)
     * @throws IOException Thrown if unknown server host when communicating through sockets
     * @throws ClassNotFoundException If the object received from the server is instantiated from a class that is not found
     * */
    public static Object initClient(String message) throws ClassNotFoundException, IOException {
        // Connect to server
        final String networkPropsFilePath = "src\\main\\resources\\network.props";
        final int port = Helpers.getPort(networkPropsFilePath);
        final String ip = Helpers.getIp(networkPropsFilePath);
        Socket socket = new Socket(ip, port);

        // Input/Output stream setup
        OutputStream outputStream = socket.getOutputStream();
        InputStream inputStream = socket.getInputStream();
        ObjectOutputStream oos = new ObjectOutputStream(outputStream);
        ObjectInputStream ois = new ObjectInputStream(inputStream);

        // Write message to server and receive server's return object
        oos.writeUTF(message);
        oos.flush();
        Object serverResponse = ois.readObject();

        // Cleanup
        ois.close();
        oos.close();
        socket.close();
        return serverResponse;
    }
}
