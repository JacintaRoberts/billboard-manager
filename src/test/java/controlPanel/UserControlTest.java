package controlPanel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.DbUser;
import server.Server.ServerAcknowledge;
import server.UserAdmin;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static server.Server.ServerAcknowledge.*;

class UserControlTest {
    /* Test 0: Declaring UserControl object
     * Description: UserControl object should be running in background on application start.
     * Expected Output: UserAdmin object is declared
     */
    UserControl userControl;
    private String sessionToken;
    private String callingUser = "testUser";
    private String dummySalt = "68b91e68f846f39f742b4e8e5155bd6ac5a4238b7fc4360becc02b064c006433";
    private String dummyPasswordFromCp = "password";
    private String dummyNewPasswordFromCp = "newpass";
    private String dummyHashedSaltedPassword = "6df8615d2bf6d4a2f43287b0061682ffad743230739fba51c97777d6a51545ce"; // hash(dummyHashedPassword + dummySalt);
    private Boolean createBillboard = true;
    private Boolean editBillboard = true;
    private Boolean scheduleBillboard = true;
    private Boolean editUser = true;
    private String badToken = "badToken"; // This token is not a UUID - should Exception Handling any tests which expect an invalid token
    // Defining additional testing users
    private String basicUser = "basicUser";
    private String adminUser = "adminUser";
    private String testUser = "test";
    // Defining permissions to be tested
    private ArrayList<Boolean> fullPermissions = new ArrayList<>(Arrays.asList(true,true,true,true));
    private ArrayList<Boolean> basicPermissions = new ArrayList<>(Arrays.asList(false, false, false, false));
    private ArrayList<Boolean> editUserPermission = new ArrayList<>(Arrays.asList(false, false, false, true));


    /* Test 1: Constructing a UserControl object
     * Description: UserControl Object should be able to be created on logged in user request from control panel
     * Expected Output: UserControl object is instantiated from UserControl class
     */
    @BeforeEach
    @Test
    public void setUpUserControl() throws NoSuchAlgorithmException, IOException, ClassNotFoundException, SQLException {
        userControl = new UserControl();
        // Start with a fresh test user each test
        if (!DbUser.retrieveUser(callingUser).isEmpty()) {
            DbUser.deleteUser(callingUser);
        }
        DbUser.addUser(callingUser, dummyHashedSaltedPassword, dummySalt, createBillboard, editBillboard, scheduleBillboard, editUser);
        // generate a test token to be used by other functions
        sessionToken = (String) userControl.loginRequest(callingUser, dummyPasswordFromCp);
    }


    /* Test 2: Log out Request (Pass)
     * Description: User's request to log out is sent to the server and an acknowledgement is received
     * Expected Output: Successful log out of the user, Success ServerAcknowledge and the session token is expired.
     */
    @Test
    public void logOutRequest() throws IOException, ClassNotFoundException {
        ServerAcknowledge serverResponse = userControl.logoutRequest(sessionToken);
        assertEquals(Success, serverResponse);
    }


    /* Test 3: Request to server to list Current Users (Pass)
     * Description: Method to request to server to send a list of active users in the database. Requires a valid
     *              sessionToken.
     * Expected Output: A list of users
     */
    @Test
    public void listUsersRequest() throws IOException, ClassNotFoundException {
        ArrayList<String> serverResponse = (ArrayList<String>) userControl.listUsersRequest(sessionToken);
        assertTrue(serverResponse.contains(callingUser));
    }


    /* Test 4: Request to server to list Current Users (Exception Handling)
     * Description: Method to request to server to send a list of active users in the database. Requires a valid
     *              sessionToken. Throw exception due to non-existent calling username
     *              (e.g. if someone else deleted you whilst logged in).
     * Expected Output: List of Users unable to be retrieved from DB and returns InvalidToken ServerAcknowledge
     */
    @Test
    public void listUsersRequestCallingUserDeleted() throws IOException, ClassNotFoundException {
        ServerAcknowledge serverResponse = (ServerAcknowledge) userControl.listUsersRequest(badToken);
        assertEquals(InvalidToken, serverResponse);
    }


    /* Test 5: Request to server to list Current Users (Exception Handling)
     * Description: Method to request to server to send a list of active users in the database. Requires a valid
     *              sessionToken. Throw exception due to insufficient permission. Assume current user logged in is called "CAB302"
     * Expected Output: List of Users unable to be retrieved from DB and returns InsufficientPermission ServerAcknowledge
     */
    @Test
    public void listUsersRequestInsufficientPermissions() throws IOException, SQLException, NoSuchAlgorithmException, ClassNotFoundException {
        if (DbUser.retrieveUser(basicUser).isEmpty()) {
            DbUser.addUser(basicUser, dummyHashedSaltedPassword, dummySalt, false, false, false, false);
        }
        String basicToken = (String) UserControl.loginRequest(basicUser, dummyPasswordFromCp);
        ServerAcknowledge serverResponse = (ServerAcknowledge) userControl.listUsersRequest(basicToken);
        assertEquals(InsufficientPermission, serverResponse);
    }


    /* Test 6: Request to server to change password (Pass)
     * Description: Method to request to server to change a specific users password. Assumes a valid sessionToken is
     *              running, and that user has permission. This test tests for own password setting.
     * Expected Output: Password is updated and receives Success ServerAcknowledge
     */
    @Test
    public void setOwnPasswordRequest() throws NoSuchAlgorithmException, IOException, ClassNotFoundException {
        ServerAcknowledge serverResponse = userControl.setPasswordRequest(sessionToken, callingUser, dummyNewPasswordFromCp);
        assertEquals(Success, serverResponse);
    }


    /* Test 7: Request to server to change password (Exception Handling)
     * Description: Method to request to server to change a specific users password. Assumes a valid sessionToken is
     *              running, and that user has permission. This test tests for themself. This test will test when
     *              the method is called but user is deleted halfway during a session.
     * Expected Output: The password is not updated in the database and returns an InvalidToken ServerAcknowledge
     */
    @Test
    public void setOwnPasswordRequestCallingUserDeleted() throws NoSuchAlgorithmException, IOException, ClassNotFoundException {
        ServerAcknowledge serverResponse = userControl.setPasswordRequest(badToken, callingUser, dummyNewPasswordFromCp);
        assertEquals(InvalidToken, serverResponse);
    }


    /* Test 8: Request to server to change password (Pass)
     * Description: Method to request to server to change a specific users password. Assumes a valid sessionToken is
     *              running, and that user has permission. This test tests for otherUsers.
     * Expected Output: Password of other user updated in the database and returns Success ServerAcknowledge
     */
    @Test
    public void setOtherPasswordRequest() throws NoSuchAlgorithmException, IOException, ClassNotFoundException, SQLException {
        // Test Setup - Ensure the test user exists in the database
        if (DbUser.retrieveUser(testUser).isEmpty()) {
            DbUser.addUser(testUser, dummyHashedSaltedPassword, dummySalt, true, true, true, true);
        }
        ServerAcknowledge serverResponse = userControl.setPasswordRequest(sessionToken, testUser, dummyNewPasswordFromCp);
        assertEquals(Success, serverResponse);
    }


    /* Test 9: Request to server to change password (Exception Handling)
     * Description: Method to request to server to change a specific users password. Assumes a valid sessionToken is
     *              running, and that user has permission. This tests the calling user gets deleted (bad token).
     * Expected Output: The password of the other user is not updated in the database and returns InvalidToken ServerAcknowledge
     */
    @Test
    public void setOtherPasswordRequestCallingUserDeleted() throws IOException, SQLException, NoSuchAlgorithmException, ClassNotFoundException {
        // Test Setup - Ensure the test user exists in the database
        if (DbUser.retrieveUser(testUser).isEmpty()) {
            DbUser.addUser(testUser, dummyHashedSaltedPassword, dummySalt, true, true, true, true);
        }
        ServerAcknowledge serverResponse = userControl.setPasswordRequest(badToken, testUser, dummyNewPasswordFromCp);
        assertEquals(InvalidToken, serverResponse);
    }


    /* Test 10: Request to server to change password (Exception Handling)
     * Description: Method to request to server to change a specific users password. Assumes a valid sessionToken is
     *              running, and that user has permission. This tests for insufficient permissions.
     * Expected Output: Password of other user not updated in database and returns InsufficientPermission ServerAcknowledge
     */
    @Test
    public void setOtherPasswordRequestInsufficientPermissions() throws IOException, SQLException, NoSuchAlgorithmException, ClassNotFoundException {
        // Test Setup - Ensure the basic user exists in the database
        if (DbUser.retrieveUser(basicUser).isEmpty()) {
            DbUser.addUser(basicUser, dummyHashedSaltedPassword, dummySalt, false, false, false, false);
        }
        String basicToken = (String) UserControl.loginRequest(basicUser, dummyPasswordFromCp);
        ServerAcknowledge serverResponse = userControl.setPasswordRequest(basicToken, testUser, dummyNewPasswordFromCp);
        assertEquals(InsufficientPermission, serverResponse);
    }


    /* Test 11: Request to server to change password (Exception Handling)
     * Description: Method to request to server to change a specific users password. Assumes a valid sessionToken is
     *              running, and that user has permission. This tests for non-existent user.
     * Expected Output: Password of other user not updated in database and returns NoSuchUser ServerAcknowledge
     */
    @Test
    public void setOtherPasswordRequestNoUsernameInDb() throws IOException, SQLException, NoSuchAlgorithmException, ClassNotFoundException {
        // Test Setup - Ensure the test user does not exist in the database
        if (!DbUser.retrieveUser(testUser).isEmpty()) {
            DbUser.deleteUser(testUser);
        }
        ServerAcknowledge serverResponse = userControl.setPasswordRequest(sessionToken, testUser, dummyNewPasswordFromCp);
        assertEquals(NoSuchUser, serverResponse);
    }


    /* Test 12: Request to server to set user permissions of Own User (Pass)
     * Description: Method to set a user permission for your own user account. Assume valid session and valid user permissions to do so
     * Expected Output: Own permissions are updated in database and returns Success ServerAcknowledge
     */
    @Test
    public void setOwnUserPermissionRequest() throws IOException, ClassNotFoundException {
        // Attempt to remove all permissions except for EditUsers
        ServerAcknowledge serverResponse = userControl.setPermissionsRequest(sessionToken, callingUser, false, false, false, true);
        assertEquals(Success, serverResponse);
        // Check that the user permissions are actually updated in the DB
        assertEquals(editUserPermission, userControl.getPermissionsRequest(sessionToken, callingUser));
    }


    /* Test 13: Request to server to set user permissions of a user (Exception Handling)
     * Description: Method to set remove own EditUser permission.
     * Expected Output: Own permissions are not updated in database and returns CannotRemoveOwnAdminPermission ServerAcknowledge
     */
    @Test
    public void removeOwnUserEditUsersPermissionRequest() throws IOException, ClassNotFoundException {
        // Attempt to remove EditUsers permission
        ServerAcknowledge serverResponse = userControl.setPermissionsRequest(sessionToken, callingUser, true, true, true, false);
        assertEquals(CannotRemoveOwnAdminPermission, serverResponse);
        // Check that the user permissions are not updated in the DB anyway
        assertEquals(fullPermissions, userControl.getPermissionsRequest(sessionToken, callingUser));
    }


    /* Test 14: Request to server to set user permissions of a user (Exception Handling)
     * Description: Method to set a user permission for another person. User being deleted mid session or expired token.
     * Expected Output: Own permissions are not updated in database and InvalidToken ServerAcknowledge
     */
    @Test
    public void setOwnPermissionRequestCallingUserDeleted() throws IOException, ClassNotFoundException {
        // Attempt to remove EditUsers permission
        ServerAcknowledge serverResponse = userControl.setPermissionsRequest(badToken, callingUser, false,
                                                                true, true, true);
        assertEquals(InvalidToken, serverResponse);
        // Check that the user permissions are not updated in the DB anyway
        assertEquals(fullPermissions, userControl.getPermissionsRequest(sessionToken, callingUser));
    }


    /* Test 15: Request to server to set user permissions of a user (Exception Handling)
     * Description: Method to set a user permission for another person. User does not have enough permission
     * Expected Output:  Own permissions are not updated in database and returns InsufficientPermission ServerAcknowledge
     */
    @Test
    public void setOwnPermissionRequestInsufficientPermissions() throws IOException, SQLException, NoSuchAlgorithmException, ClassNotFoundException {
        // Test setup - Ensure basic user exists and generate token
        if (DbUser.retrieveUser(basicUser).isEmpty()) {
            DbUser.addUser(basicUser, dummyHashedSaltedPassword, dummySalt, false, false, false, false);
        }
        String basicToken = (String) UserControl.loginRequest(basicUser, dummyPasswordFromCp);
        ServerAcknowledge serverResponse = userControl.setPermissionsRequest(basicToken, basicUser, true,
                true, true, true);
        assertEquals(InsufficientPermission, serverResponse);
        // Check that the user permissions are not updated in the DB anyway
        assertEquals(basicPermissions, userControl.getPermissionsRequest(sessionToken, basicUser));
    }


    /* Test 16: Request to server to set user permissions of a user (Pass)
     * Description: Method to set a user permission for another person. Assume SessionToken is valid
     * Expected Output: Pass with Response: Success ServerAcknowledge
     */
    @Test
    public void setOtherUserPermissionRequest() throws IOException, ClassNotFoundException, SQLException {
        // Test Setup - Ensure testUser exists in the database with full permissions
        if (!DbUser.retrieveUser(testUser).isEmpty()) {
            DbUser.deleteUser(testUser);
        }
        DbUser.addUser(testUser, dummyHashedSaltedPassword, dummySalt, true, true, true, true);
        // Attempt to remove all permissions
        ServerAcknowledge serverResponse = userControl.setPermissionsRequest(sessionToken, testUser, false, false, false, false);
        assertEquals(Success, serverResponse);
        // Check that the user permissions are actually updated in the DB
        assertEquals(basicPermissions, userControl.getPermissionsRequest(sessionToken, testUser));
    }


    /* Test 17: Request to server to get own user permissions (Pass)
     * Description: Method to request own user permissions from database.
     * Expected Output: Return of UserPermission in a Boolean array list.
     */
    @Test
    public void getOwnPermissionRequest() throws IOException, ClassNotFoundException, SQLException {
        // Test Setup - Create users if they do not exist
        if (!DbUser.retrieveUser(basicUser).isEmpty()) {
            DbUser.deleteUser(basicUser); // Clean
        }
        DbUser.addUser(basicUser, dummyHashedSaltedPassword, dummySalt, false, false, false, false);

        if (!DbUser.retrieveUser(adminUser).isEmpty()) {
            DbUser.deleteUser(adminUser);
        }
        DbUser.addUser(adminUser, dummyHashedSaltedPassword, dummySalt, true, true, true, true);

        assertAll("Check for a few Possible User Permission Combinations",
                ()-> assertEquals(basicPermissions, UserControl.getPermissionsRequest(sessionToken, basicUser)),
                ()-> assertEquals(fullPermissions, UserControl.getPermissionsRequest(sessionToken, adminUser))
        );
    }


    /* Test 18: Request to server to get other user's permission (Exception Handling)
     * Description: Requests other user permissions from database, however the calling user is deleted (or session token expired).
     * Expected Output: User permissions unable to be retrieved due to bad token and returns InvalidToken Server Acknowledge
     */
    @Test
    public void getOtherPermissionsCallingUsernameDeleted() throws IOException, ClassNotFoundException, SQLException {
        if (!DbUser.retrieveUser(testUser).isEmpty()) {
            DbUser.deleteUser(testUser);
        }
        DbUser.addUser(testUser, dummyHashedSaltedPassword, dummySalt, true, true, true, true);
        ServerAcknowledge serverResponse = (ServerAcknowledge) userControl.getPermissionsRequest(badToken, callingUser);
        assertEquals(InvalidToken, serverResponse);
    }


    /* Test 19: Request to server to get user permissions of a user (Exception Handling)
     * Description: Method to set a user permission for another person. Calling user does not have sufficient permissions.
     * Require "EditUsers" permission which is the 4th element in UserPermissions object.
     * Expected Output: User permissions unable to be retrieved and returns InsufficientPermissions Server Acknowledge
     */
    @Test
    public void getOtherUserPermissionRequestInsufficientPermissions() throws IOException, ClassNotFoundException, SQLException, NoSuchAlgorithmException {
        // Test setup - Ensure basic user exists and generate token
        if (DbUser.retrieveUser(basicUser).isEmpty()) {
            DbUser.addUser(basicUser, dummyHashedSaltedPassword, dummySalt, false, false, false, false);
        }
        String basicToken = (String) UserControl.loginRequest(basicUser, dummyPasswordFromCp);
        ServerAcknowledge serverResponse = (ServerAcknowledge) userControl.getPermissionsRequest(basicToken, callingUser);
        assertEquals(InsufficientPermission, serverResponse);
    }


    /* Test 20: Request to server to get User Permission from a user (Exception Handling)
     * Description: Get other User's Permissions from db, however the user requested does not exist.
     * Expected Output: The permissions of requested user cannot be retrieved and returns NoSuchUser Server Acknowledge.
     */
    @Test
    public void getOtherUserPermissionsExceptionUserNotExist() throws IOException, SQLException, ClassNotFoundException {
        // Test setup - Ensure the user to be deleted does not exist in DB
        if ( !DbUser.retrieveUser(testUser).isEmpty() ) {
            System.out.println("The test user exists, so it will be deleted for this test.");
            DbUser.deleteUser(testUser);
            assertFalse(UserAdmin.userExists(testUser));
        }
        ServerAcknowledge serverResponse = (ServerAcknowledge) userControl.getPermissionsRequest(sessionToken, testUser);
        assertEquals(NoSuchUser, serverResponse);
    }


    /* Test 21: Request to server to Delete Users (Pass)
     * Description: Method to request to server to delete a specified user in the database. Requires a valid
     *              sessionToken.
     * Expected Output: The user is deleted and returns Success Server Acknowledge.
     */
    @Test
    public void deleteUserRequest() throws IOException, SQLException, ClassNotFoundException {
        // Test setup - Ensure the user to be deleted exists in DB
        if (!UserAdmin.userExists(testUser)) {
            System.out.println("The test user does not exist, so it will be created.");
            DbUser.addUser(testUser, dummyHashedSaltedPassword, dummySalt, true, true, true, true);
            assertTrue(UserAdmin.userExists(testUser));
        }
        // Check return value
        ServerAcknowledge serverResponse = UserControl.deleteUserRequest(sessionToken, testUser);
        assertEquals(Success, serverResponse);
    }

 
    /* Test 22: Request to server to Delete a user (Exception Handling)
     * Description: Delete a user from from db - a bad session token is received due to the calling user being deleted.
     * Expected Output: The user is not deleted and returns InvalidToken Server Acknowledge.
     */
    @Test
    public void deleteUserRequestCallingUsernameDeleted() throws IOException, SQLException, ClassNotFoundException {
        // Test setup - Ensure the user to be deleted exists in DB
        if (!UserAdmin.userExists(testUser)) {
            System.out.println("The test user does not exist, so it will be created.");
            DbUser.addUser(testUser, dummyHashedSaltedPassword, dummySalt, true, true, true, true);
            assertTrue(UserAdmin.userExists(testUser));
        }
        // Check return value
        ServerAcknowledge serverResponse = UserControl.deleteUserRequest(badToken, testUser);
        assertEquals(InvalidToken, serverResponse);
    }


    /* Test 23: Request to server to Delete Current Users (Exception Handling)
     * Description: Method to request to server to delete a specified user in the database. Requires a valid
     *              sessionToken. User attempts to call method with insufficient permissions.
     * Expected Output: The user is not deleted and returns InsufficientPermission Server Acknowledge.
     */
    @Test
    public void deleteUserRequestInsufficientPermissions() throws IOException, SQLException, NoSuchAlgorithmException, ClassNotFoundException {
        // Test setup - Ensure basic user exists and generate token
        if (DbUser.retrieveUser(basicUser).isEmpty()) {
            DbUser.addUser(basicUser, dummyHashedSaltedPassword, dummySalt, false, false, false, false);
        }
        String basicToken = (String) UserControl.loginRequest(basicUser, dummyPasswordFromCp);
        ServerAcknowledge serverResponse = userControl.deleteUserRequest(basicToken, callingUser);
        assertEquals(InsufficientPermission, serverResponse);
    }


    /* Test 24: Request to server to Delete Current Users (Exception Handling)
     * Description: Method to request to server to delete a specified user in the database. Requires a valid
     *              sessionToken. An exception occurs due to user not existing.
     * Expected Output: The user is not deleted and returns NoSuchUser Server Acknowledge.
     */
    @Test
    public void deleteUserNoSuchUserTest() throws IOException, SQLException, ClassNotFoundException {
        // Test setup - Ensure the user to be deleted does not exist in DB
        if ( !DbUser.retrieveUser(testUser).isEmpty() ) {
            System.out.println("The test user exists, so it will be deleted for this test.");
            DbUser.deleteUser(testUser);
            assertFalse(UserAdmin.userExists(testUser));
        }
        ServerAcknowledge serverResponse = userControl.deleteUserRequest(sessionToken, testUser);
        assertEquals(NoSuchUser, serverResponse);
    }


    /* Test 25: Request to server to Delete Current Users (Exception Handling)
     * Description: Method to request to server to delete a specified user in the database. Requires a valid
     *              sessionToken. An exception occurs as it is required that a user cannot delete themselves.
     * Expected Output: The user is not deleted and returns CannotDeleteSelf Server Acknowledge.
     */
    @Test
    public void deleteUserNoUserTest() throws IOException, ClassNotFoundException, SQLException {
        // Test setup - Ensure the user to be deleted does not exist in DB
        if ( !DbUser.retrieveUser(testUser).isEmpty() ) {
            System.out.println("The test user exists, so it will be deleted for this test.");
            DbUser.deleteUser(testUser);
            assertFalse(UserAdmin.userExists(testUser));
        }
        ServerAcknowledge serverResponse = userControl.deleteUserRequest(sessionToken, callingUser);
        assertEquals(CannotDeleteSelf, serverResponse);
    }


    /* Test 26: Request to server to Create New Users (Pass)
     * Description: New method to create new users to the system. This will take a unique username, user permissions,
     *              a password string and a valid sessionToken to create a new user.
     * Expected Output: The user is created and returns Success Server Acknowledge.
     */
    @Test
    public void createUserRequest() throws NoSuchAlgorithmException, IOException, ClassNotFoundException, SQLException {
        if (!DbUser.retrieveUser(testUser).isEmpty()) {
            DbUser.deleteUser(testUser); // Clean user
        }
        ServerAcknowledge serverResponse = userControl.createUserRequest(sessionToken, testUser, dummyPasswordFromCp,
                true, true, true, true);
        assertEquals(serverResponse, Success);
    }


    /* Test 27: Request to server to Create New Users (Exception Handling)
     * Description: New method to create new users to the system. This will take a unique username, user permissions,
     *              a password string and a valid sessionToken to create a new user. This test checks for valid token.
     * Expected Output: The user is not created and returns InvalidToken Server Acknowledge.
     */
    @Test
    public void createUserRequestCallingUsernameDeleted() throws NoSuchAlgorithmException, IOException, ClassNotFoundException, SQLException {
        if (!DbUser.retrieveUser(testUser).isEmpty()) {
            DbUser.deleteUser(testUser); // Clean user
        }
        ServerAcknowledge serverResponse = userControl.createUserRequest(badToken, testUser, dummyPasswordFromCp,
                true, true, true, true);
      // Check for correct message received
      assertEquals(InvalidToken, serverResponse);
    }


    /* Test 28: Request to server to Create New Users (Exception Handling)
     * Description: New method to create new users to the system. This will take a unique username, user permissions,
     *              a password string and a valid sessionToken to create a new user. This test checks for sufficient
     * permissions on the calling user.
     * Expected Output: The user is not created and returns InsufficientPermission Server Acknowledge.
     */
    @Test
    public void createUserRequestInsufficientPermissions() throws NoSuchAlgorithmException, IOException, ClassNotFoundException, SQLException {
        // Setup - Create basic user without edit user permission and generate corresponding session token
        if (!DbUser.retrieveUser(testUser).isEmpty()) {
            DbUser.deleteUser(testUser); // Clean user
        }
        if (DbUser.retrieveUser(basicUser).isEmpty()) {
            DbUser.addUser(basicUser, dummyHashedSaltedPassword, dummySalt, false, false, false, false);
        }
        String basicToken = (String) UserControl.loginRequest(basicUser, dummyPasswordFromCp);
        ServerAcknowledge serverResponse = userControl.createUserRequest(basicToken, testUser, dummyPasswordFromCp,
                true, true, true, true);
      // Check for correct message received
      assertEquals(InsufficientPermission, serverResponse);
    }


    /* Test 29: Request to server to Create New Users (Exception Handling)
     * Description: New method to create new users to the system. This will take a unique username, user permissions,
     *              a password string and a valid sessionToken to create a new user. This will test if the username
     *              provided is not unique.
     * Expected Output: The user is not created and returns PrimaryKeyClash Server Acknowledge.
     */
    @Test
    public void createUserRequestDuplicateUsername() throws IOException, SQLException, NoSuchAlgorithmException, ClassNotFoundException {
        // Setup - Ensure test user already exist in database
        if (DbUser.retrieveUser(testUser).isEmpty()) {
            DbUser.addUser(testUser, dummyHashedSaltedPassword, dummySalt, true, true, true ,true);
        }
        ServerAcknowledge serverResponse = userControl.createUserRequest(sessionToken, testUser, dummyPasswordFromCp,
                true, true, true, true);
      // Check for correct message received
      assertEquals(PrimaryKeyClash, serverResponse);
    }

}