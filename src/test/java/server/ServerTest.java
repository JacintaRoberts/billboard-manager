package server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

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


    /* Test 2: Listen for Connection with Default IP (Success)
     * Description: Server listens for connections on specified port number and defaulted local host
     * listenForConnections(portNumber, *optional* ip); -> default ip is localhost if not specified.
     * Expected Output: Server successfully listens for connections on localhost at port 4444
     */
    @Test
    public void listenForConnection() throws IOException {
        server.listenForConnections(4444);
    }

    /* Test 3: Login Response (Success)
     * Description: Control Panel will send the Server a username and hashed password. The Server will either send back
     *              an error or a valid session token. (Permissions required: none.)
     * Expected Output: Return a valid session token.
     */
    @Test
    public void loginResponse() throws IOException, SQLException, NoSuchAlgorithmException {
        // Ensure this test user exists with this password in the fake DB where this method is implemented
        String serverResponse = server.login("testUser", "goodPass");
        assertEquals(serverResponse,"sessionToken");
    }

    /* Test 4: Login Response (error handling)
     * Description: Control Panel will send the Server a username and hashed password. The Server will either send back
     *              an error or a valid session token. (Permissions required: none.)
     * Expected Output: Return an invalid session token and throw IncorrectPasswordException
     */
    @Test
    public void loginIncorrectPassword() throws IOException, SQLException, NoSuchAlgorithmException {
        boolean userExists = UserAdmin.userExists("testUser");
        assertTrue(userExists);
        // Ensure this test user exists with a diff password in the fake DB where this method is implemented
        assertEquals(server.login("testUser", "wrongPass"), "Fail: Incorrect Password");
    }

    /* Test 5: Login Response (error handling)
     * Description: Control Panel will send the Server a username and hashed password. The Server will either send back
     *              an error or a valid session token. (Permissions required: none.)
     * Expected Output: Return an invalid session token and throw UserNotExistException
     */
    @Test
    public void loginNoSuchUser() throws IOException, SQLException, NoSuchAlgorithmException {
      boolean userExists = UserAdmin.userExists("wrongUser");
      assertFalse(userExists);
      // Ensure this test user exists with a diff password in the fake DB where this method is implemented
      assertEquals(server.login("wrongUser", "anything"), "Fail: No Such User");
    }

    /* Test 6: Log out Response (Success)
     * Description: The Control Panel will send the Server a valid session token and the Server will expire that
     *              session token and send back a boolean acknowledgement for success/failure.
     * Expected Output: Successful log out of the user, session token is expired and acknowledgement returned.
     */
    @Test
    public void logOut() {
        server.addToken("testToken", "testUser"); // Test set up
        assertEquals(server.logout("testToken"), "Pass: Logout Successful");
        // Valid session token holder should not hold the sessionToken anymore
        assertFalse(server.validateToken("testToken"));
    }


    /* Test 7: Validate SessionToken (Success)
     * Description: Check that the Session token is still valid - validation needs to happen on the server-side
     *              according to the specification.
     * Expected Output: Returns true as the given session should be active
     * TODO: implement validSessionTokens is an array of strings (include "sessionToken" and exclude "failToken")
     */
    @Test
    public void verifySession() {
      Server.addToken("sessionToken", "testUser");
      assertTrue(Server.validateToken("sessionToken"));
    }


    /* Test 8: Validate SessionToken (Exception Handling)
     * Description: Check that the Session token is still valid.
     * Expected Output: Returns false as the given session should be inactive, throws invalidSessionTokenException
     */
    @Test
    public void verifySessionExpiration() {
      Server.addToken("failToken", "testUser");
      Server.logout("failToken");
      // Check Expired Token
      assertFalse(Server.validateToken("failToken"));
      // Also Check Empty
      assertFalse(Server.validateToken(""));
    }


}