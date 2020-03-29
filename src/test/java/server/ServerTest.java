package server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

class ServerTest {
    /* Test 0: Declaring Server object
     * Description: Server object should be running in background on application start.
     * Expected Output: Server object is declared
     */
    Server server;

    /* Test 1: Constructing a Server object
     * Description: Server Object should be running in background on application start.
     * Expected Output: Server object is instantiated from Server class
     */
    @BeforeEach
    @Test
    public void setUpServer() {
        server = new Server();
    }

    /* Test 2: Shared Helper function - Read from network properties files (success)
     * Description: Takes the file name to be read and returns the port number (Integer) and ip (string) in a HashMap
     * Expected Output: Successfully return the port number and ip from the network.props file as a HashMap<String, Object>
     * Suggested Implementation provided - wanted to figure out how the links worked for file paths for db.props
     */
    @Test
    public void readNetworkProps() {
        int testPort = 4444;
        String testIp = "127.0.0.1";
        assertEquals(testPort, server.readProps("src\\test\\resources\\network.props").get("port"));
        assertEquals(testIp, server.readProps("src\\test\\resources\\network.props").get("ip"));
    }

    /* Test 3: Shared Helper function - Validate port from string (success)
     * Description: Method takes the string to be validated and returns the port number parsed as an int or an exception
     * Expected Output: Successfully validate and return the port number from the network.props file as an int
     */
//    @Test
//    public void validateNetworkPort() {
//        assertEquals(4444, server.validatePort(4444));
//    }

    /* Test 4: Shared Helper function - Bad port numbers (error handling)
     * Description: Implement appropriate error handling for bad port number read from file (only 0 to 65535 is ok)
     * Expected Output: Throws BadPortNumberException (need to create this specific Exception!)
     */
//    @Test
//    public void badPortNumbers() {
//        // Check min-1
//        assertThrows(BadPortNumberException.class, () -> {
//            server.validatePort(-1)
//        });
//        // Check max+1
//        assertThrows(BadPortNumberException.class, () -> {
//            server.validatePort(65536);
//        });
//    }

    /* Test 5: Shared Helper function - Read port/IP from network properties files (error handling)
     * Description: Implement appropriate error handling for bad file name. This could also pick up:
     * reading a local file that was no longer available or trying to read file but don't have permission.
     * Expected Output: IOException from non-existent file name
     */
//    @Test
//    public void badNetworkPropsFile() {
//       // Check both port and ip retrieval failed
//        assertThrows(java.io.FileNotFoundException.class, () -> {
//            server.readProps("unknown.props");
//        });
//    }

    /* Test 6: Shared Helper function - Convert IP address from string to bytes array (from network properties file)
     * Description: Read and convert IP address from network.props
     * Expected Output: Ip address string is successfully converted to byte array
     */
//    @Test
//    public void convertNetworkIpToBytes() {
//        byte[] addr = new byte[] { 127, 0, 0, 1 };
//        assertEquals(addr, server.convertIpToBytes("127.0.0.1"));
//    }

    /* Test 7: Shared Helper function - Check that IP bytes is able to be converted to an InetAddress
     * Description: Checks that the bytes IP address is able to be converted appropriately.
     * Expected Output: IP address is successfully converted to an InetAddress
     */
//    @Test
//    public void convertIpBytesToINetAddress() {
//        byte[] addr = server.convertIpToBytes("127.0.0.1")
//        assertTrue(InetAddress.getByAddress(addr) instanceof InetAddress);
//    }

    /* Test 8: Listen for Connection with Default IP (success)
     * Description: Server listens for connections on specified port number and defaulted local host
     * listenForConnections(portNumber, *optional* ip); -> default ip is localhost if not specified.
     * Expected Output: Server successfully listens for connections on localhost at port 4444
     */
//    @Test
//    public void listenForConnection() {
//        server.listenForConnections(4444);
//    }
//

    /* Test 9: Listen for Connection with a Specific IP (success)
     * Description: Check that the server is able to listen for connections on a specific IP address
     * listenForConnections(port, *optional* addr); -> default addr is localhost if not specified
     * Suggested implementation: InetAddress.getByAddress(byte[]) returns InetAddress which is passed into ->
     * ServerSocket sock = new ServerSocket(port, backlog, addr); (backlog = 50 as a default) to facilitate this.
     * Relevant tutorial: https://docs.oracle.com/javase/tutorial/networking/sockets/clientServer.html
     * Expected Output: Server successfully listens for connection on specified IP address at port 4444
     */
//    @Test
//    public void listenForConnection() {
//        // Specific IP to be used
//        byte[] addr = server.getIp(""src\\test\\resources\\network.props"");
//        server.listenForConnections(4444, addr);
//    }

    /* Test 10: Send acknowledgement to client
     * Description: Send appropriate acknowledgements made to client based on commandType received
     * Expected Output: Assert true as sendAcknowledgment should return a string containing message to send to client
     */
//    @Test
//    public void sendAcknowledgement() {
//        assertTrue(server.sendAcknowledgement(String[] commandType) instanceof String[]);
//        });
//    }


}