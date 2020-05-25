package server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.net.BindException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import static controlPanel.UserControl.hash;
import static org.junit.jupiter.api.Assertions.*;
import static server.Server.ServerAcknowledge.*;
import static server.Server.validateToken;

class ServerTest {
    /* Test 0: Declaring Server object
     * Description: Server object should be running in background on application start.
     * Expected Output: Server object is declared
     */
    Server server;
    String callingUser = "serverTestUser";
    String dummySalt;
    String dummyPasswordFromCp;
    String dummyHashedPassword;
    String dummyHashedSaltedPassword;

    /* Test 1: Constructing a Server object
     * Description: Server Object should be running in background on application start.
     * Expected Output: Server object is instantiated from Server class
     */
    @BeforeEach
    @Test
    public void setUpServer() throws IOException, SQLException, NoSuchAlgorithmException {
        server = new Server();
        // Add a testing user to validate tests
        dummySalt = "68b91e68f846f39f742b4e8e5155bd6ac5a4238b7fc4360becc02b064c006433";
        dummyPasswordFromCp = "password";
        dummyHashedPassword = hash(dummyPasswordFromCp);
        dummyHashedSaltedPassword = hash(dummyHashedPassword + dummySalt);
        if (!DbUser.retrieveUser(callingUser).isEmpty()) {
            DbUser.deleteUser(callingUser); // clean user
        }
        DbUser.addUser(callingUser, dummyHashedSaltedPassword, dummySalt, true, true, true, true);
    }


    /* Test 2: Listen for Connection with Default IP (Success)
     * Description: Server listens for connections on specified port number and defaulted local host
     * listenForConnections(portNumber, *optional* ip); -> default ip is localhost if not specified.
     * Expected Output: Server successfully listens for connections on localhost at port 4444
     */
    @Test
    public void listenForConnection() throws IOException {
        try {
            Server.listenForConnections(4444);
        } catch (BindException e) {
            System.out.println("Port in use.");
        }
    }

    /* Test 3: Login Response (Success)
     * Description: Control Panel will send the Server a username and hashed password. The Server will either send back
     *              an error or a valid session token. (Permissions required: none.)
     * Expected Output: Return a valid session token.
     */
    @Test
    public void loginResponse() throws IOException, SQLException, NoSuchAlgorithmException {
        // Ensure this test user exists with this password in the fake DB where this method is implemented
        String serverResponse = (String) Server.login(callingUser, dummyHashedPassword);
        assertTrue(serverResponse != null);
        assertTrue(validateToken(serverResponse)); // Validation of session token
    }

    /* Test 4: Login Response (error handling)
     * Description: Control Panel will send the Server a username and hashed password. The Server will either send back
     *              an error or a valid session token. (Permissions required: none.)
     * Expected Output: Return an invalid session token and throw IncorrectPasswordException
     */
    @Test
    public void loginIncorrectPassword() throws IOException, SQLException, NoSuchAlgorithmException {
        assertTrue(UserAdmin.userExists(callingUser));
        // Ensure this test user exists with a diff password in the fake DB where this method is implemented
        assertEquals(BadPassword, Server.login(callingUser, hash("wrongPass")));
    }

    /* Test 5: Login Response (error handling)
     * Description: Control Panel will send the Server a username and hashed password. The Server will either send back
     *              an error or a valid session token. (Permissions required: none.)
     * Expected Output: Return an invalid session token and throw UserNotExistException
     */
    @Test
    public void loginNoSuchUser() throws IOException, SQLException, NoSuchAlgorithmException {
      assertFalse(UserAdmin.userExists("wrongUser"));
      // Ensure this test user exists with a diff password in the fake DB where this method is implemented
      assertEquals(NoSuchUser, Server.login("wrongUser", dummyHashedPassword));
    }

    /* Test 6: Log out Response (Success)
     * Description: The Control Panel will send the Server a valid session token and the Server will expire that
     *              session token and send back a boolean acknowledgement for success/failure.
     * Expected Output: Successful log out of the user, session token is expired and acknowledgement returned.
     */
    @Test
    public void logOut() throws IOException, SQLException, NoSuchAlgorithmException {
        String sessionToken = (String) Server.login(callingUser, dummyHashedPassword); // Test set up
        assertTrue(validateToken(sessionToken));
        assertEquals(Success, Server.logout(sessionToken));
        // Valid session token holder should not hold the sessionToken anymore
        assertFalse(validateToken(sessionToken));
    }


    /* Test 7: Validate SessionToken (Success)
     * Description: Check that the Session token is still valid - validation needs to happen on the server-side
     *              according to the specification.
     * Expected Output: Returns true as the given session should be active
     * TODO: implement validSessionTokens is an array of strings (include "sessionToken" and exclude "failToken")
     */
    @Test
    public void verifySession() throws IOException, SQLException, NoSuchAlgorithmException {
      String sessionToken = (String) Server.login(callingUser, dummyHashedPassword);
      assertTrue(validateToken(sessionToken));
    }


    /* Test 8: Validate SessionToken (Exception Handling)
     * Description: Check that the Session token is still valid.
     * Expected Output: Returns false as the given session should be inactive, throws invalidSessionTokenException
     */
    @Test
    public void verifySessionExpiration() throws IOException, SQLException, NoSuchAlgorithmException {
      String sessionToken = (String) Server.login(callingUser, dummyHashedPassword);
      Server.logout(sessionToken); // Should expire token
      // Check Expired Token
      assertFalse(validateToken(sessionToken));
      // Also Check Empty
      assertFalse(validateToken(""));
    }


}