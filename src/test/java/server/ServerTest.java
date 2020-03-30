package server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
//    public void setUpServer() {
//        server = new Server();
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