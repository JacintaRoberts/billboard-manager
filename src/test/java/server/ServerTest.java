package server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetAddress;

import static org.junit.jupiter.api.Assertions.*;
// HELLO THIS IS WORKING!
class ServerTest {
    /* Test 0: Declaring Server object
     */
    Server server;

    /* Test 1: Constructing a Server object
     */
    @BeforeEach
    @Test
    public void setUpServer() {
        server = new Server();
    }

    /* Test 2: Get port from network properties files (success)
     * This method should take the file name to be read and return the port number
     * Relevant tutorial: https://docs.oracle.com/javase/tutorial/networking/sockets/clientServer.html
     */
//    @Test
//    public void getNetworkPort() {
//        assertEquals(4444, server.getPort("network.props"));
//    }

    /* Test 3: Get port from network properties files (error handling)
     * implement appropriate error handling for bad file name.
     * Could also trigger on: reading a local file that was no longer available.
     * Or trying to read file but don't have permission.
     */
//    @Test
//    public void badNetworkPropsFile() {
//        assertThrows(java.io.IOException.class, () -> {
//            server.getPort("unknown.props");
//        });
//    }

    /* Test 4: Listen for Connection with Default IP (success)
     * listenForConnections(portNumber, *optional* ip); -> default ip is localhost if not specified
     */
//    @Test
//    public void listenForConnection() {
//        server.listenForConnections(4444);
//    }
//
    /* Test 5: Get IP address from network properties files (success) to host the server on
     * InetAddress.getByAddress(byte[]) returns InetAddress which is passed into -> InetSocketAddress(addr, int port)
     */
//    @Test
//    public void getServerIp() {
//        byte[] addr = new byte[] { 127, 0, 0, 1 };
//        assertEquals(addr, server.getIp("network.props"));
//    }

    /* Test 6: Listen for Connection with a Specific IP (success)
     * listenForConnections(portNumber, *optional* ip); -> default ip is localhost if not specified
     * Use: ServerSocket sock = new ServerSocket(1234, 50, addr); (50 = backlog) to facilitate this.
     */
//    @Test
//    public void listenForConnection() {
//        byte[] addr = server.getIp("network.props");
//        server.listenForConnections(4444, addr);
//    }

    /* Test 7: Listen for Connection (error handling)
     * implement appropriate error handling for bad port number (0 to 65535 is ok)
     */
//    @Test
//    public void badPortNumberBelowMin() {
//        assertThrows(IllegalArgumentException.class, () -> {
//            server.listenForConnections(-1);
//        });
//    }

    /* Test 8: Listen for Connection (error handling)
     * implement appropriate error handling for bad port number (0 to 65535 is ok)
     */
//    @Test
//    public void badPortNumberAboveMax() {
//        assertThrows(IllegalArgumentException.class, () -> {
//            server.listenForConnections(65536);
//        });
//    }

    /* Test 9: Listen for Connection (error handling)
     * implement appropriate error handling for empty args (e.g. port number retrieved was empty)
     */
//    @Test
//    public void missingArgs() {
//        assertThrows(IllegalArgumentException.class, () -> {
//            server.listenForConnections();
//        });
//    }


}