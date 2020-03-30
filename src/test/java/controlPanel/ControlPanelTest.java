package controlPanel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;


class ControlPanelTest {
    /* Test 0: Declaring ControlPanel object.
     * Description: ControlPanel Object is created upon application start.
     * Output: ControlPanel Object is declared
     */
    ControlPanel controlpanel;

    /* Test 1: Constructing a ControlPanel object
     * Description: ControlPanel Object is created upon application start.
     * Output: ControlPanel Object is instantiated from ControlPanel class
     */
    @BeforeEach
    @Test
//    public void setControlPanel() {
//        controlPanel = new ControlPanel();
//    }



    /* Test 8: Listen for Connection with Default IP (success)
     * Description: ControlPanel listens for connections on specified port number and defaulted local host
     * listenForConnections(portNumber, *optional* ip); -> default ip is localhost if not specified.
     * Expected Output: Server successfully listens for connections on localhost at port 4444
     */
//    @Test
//    public void listenForConnection() {
//        controlPanel.listenForConnections(4444);
//    }
//

    /* Test 9: Listen for Connection with a Specific IP (success)
     * Description: Check that the ControlPanel is able to listen for connections on a specific IP address
     * listenForConnections(port, *optional* addr); -> default addr is localhost if not specified
     * Suggested implementation: InetAddress.getByAddress(byte[]) returns InetAddress which is passed into ->
     * ServerSocket sock = new ServerSocket(port, backlog, addr); (backlog = 50 as a default) to facilitate this.
     * Expected Output: Server successfully listens for connection on specified IP address at port 4444
     */
//    @Test
//    public void listenForConnection() {
//        // Specific IP to be used
//        byte[] addr = controlPanel.getIp(""src\\test\\resources\\network.props"");
//        controlPanel.listenForConnections(4444, addr);
//    }

    /* Test 10: Send acknowledgement to Server
     * Description: Send appropriate acknowledgements made to server based on commandType received
     * Expected Output: Assert true as sendAcknowledgment should return a string containing message to send to client
     */
//    @Test
//    public void sendAcknowledgement() {
//        assertTrue(controlPanel.sendAcknowledgement(String[] commandType) instanceof String[]);
//        });
//    }


    /* Test 6: ControlPanel - Login (Success)
    * Description: Login Request sent with the username and hashed password to authenticate user into session.
    * Input: Username and password
    * Output: Receives sessionToken from Server.
    * // TODO: May want to edit the input name variable - which is the input buffer read from the client
     */
//    @Test
//    public void loginRequest(String username, String Password) {
//      assertTrue(input.length() > 1);
//    }


    /* Test 7: ControlPanel - Login (Fail)
     * Description: Login Request sent with the username and hashed password to authenticate user into session.
     *              Implement appropriate error handling when user details are incorrect.
     * Input: Username and password
     * Output: Receives error message from server.
     * // TODO: May want to edit the input name variable - which is the input buffer read from the client
     */
//    @Test
//    public void loginRequest(String username, String Password) {
//      assertFalse(input);
//    }




}