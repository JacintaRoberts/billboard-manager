package controlPanel;

class ControlPanelTest {
    /* Test 0: Declaring ControlPanel object.
     * Description: ControlPanel Object is created upon application start.
     * Output: ControlPanel Object is declared
     */
    ControlPanel controlPanel;

    /* Test 1: Constructing a ControlPanel object
     * Description: ControlPanel Object is created upon application start.
     * Output: ControlPanel Object is instantiated from ControlPanel class
     */
//    @BeforeEach
//    @Test
//    public void setControlPanel() {
//        controlPanel = new ControlPanel();
//    }


    /* Test 2: Listen for Connection with Default IP (success)
     * Description: ControlPanel listens for connections on specified port number and defaulted local host
     * listenForConnections(portNumber, *optional* ip); -> default ip is localhost if not specified.
     * Expected Output: Server successfully listens for connections on localhost at port 4444
     */
//    @Test
//    public void listenForConnection() {
//        controlPanel.listenForConnections(4444);
//    }

    /* Test 3: Listen for Connection with a Specific IP (success)
     * Description: Check that the ControlPanel is able to listen for connections on a specific IP address
     * listenForConnections(port, *optional* addr); -> default addr is localhost if not specified
     * Suggested implementation: InetAddress.getByAddress(byte[]) returns InetAddress which is passed into ->
     * ServerSocket sock = new ServerSocket(port, backlog, addr); (backlog = 50 as a default) to facilitate this.
     * Expected Output: Server successfully listens for connection on specified IP address at port 4444
     */
//    @Test
//    public void listenForConnectionSpecificIP() {
//        // Specific IP to be used
//        byte[] addr = controlPanel.getIp(""src\\test\\resources\\network.props"");
//        controlPanel.listenForConnections(4444, addr);
//    }

    /* Test 4: Send acknowledgement to Server
     * Description: Send appropriate acknowledgements made to server based on commandType received
     * Expected Output: Assert true as sendAcknowledgment should return a string containing message to send to client
     */
//    @Test
//    public void sendAcknowledgement() {
//        assertTrue(controlPanel.sendAcknowledgement(String[] commandType) instanceof String[]);
//    }


    /* Test 5: ControlPanel - Login (Success)
    * Description: Login Request sent with the username and hashed password to authenticate user into session.
    * Input: Username and password
    * Output: Receives sessionToken from Server.
     */
//    @Test
//    public void loginRequestTest(){
//      String serverResponse = loginRequest(username, Password)
//      assertTrue(serverResponse.length() > 1);
//    }


    /* Test 6: ControlPanel - Login (Exception Handling)
     * Description: Login Request sent with the username and hashed password to authenticate user into session.
     *              Implement appropriate exception handling when user details are incorrect.
     * Input: Username and password
     * Output: Receives error message from server.
     */
//    @Test
//    public void loginRequestTest(){
//      String serverResponse = loginRequest("username", "Password");
//      assertEquals(serverResponse, "Fail: Incorrect Password");
//      assertThrows(IncorrectPasswordException);
//    }


    /* Test 7: ControlPanel - Login (Exception Handling)
     * Description: Login Request sent with the username and hashed password to authenticate user into session.
     *              Implement appropriate exception handling when user details are incorrect.
     * Input: Username and password
     * Output: Receives error message from server Fail: UserNotExistException. 
     */
//    @Test
//    public void loginRequestTest(){
//      String serverResponse = loginRequest("username", "Password");
//      assertEquals(serverResponse, "Fail: User Does Not Exist");
//      assertThrows(UserNotExistException);
//    }


    /* Test 8: ControlPanel - Logout (Success)
     * Description: Login Request sent to Server. Server will acknowledge request once the local memory in server
     *              Deletes the sessionToken. The User is brought back to the login Screen
     * Output: The user will receive logout confirmation and be sent back to the login screen
     */
//    @Test
//    public void logoutRequestTest() {
//      userControl.logoutRequest("sessionLogoutTestToken");
//      assertFalse(validSessionTokens.contains("sessionLogoutTestToken));
//      String serverResponse = logoutRequest("sessionToken");
//      assertAll("Assertion for successful logouts",
//          () -> assertFalse(userControl.sessionToken.length() > 1),
//          () -> assertEquals(serverResponse, "Pass: User Logged Out")
//      );
//    }

}