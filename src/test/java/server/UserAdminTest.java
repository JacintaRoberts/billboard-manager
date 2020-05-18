package server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import static controlPanel.UserControl.hash;
import static org.junit.jupiter.api.Assertions.*;
import static server.Server.*;
import static server.Server.ServerAcknowledge.*;
import static server.UserAdmin.*;

class UserAdminTest {
    /* Test 0: Declaring UserAdmin object
     * Description: UserAdmin object should be running in background on application start.
     * Expected Output: UserAdmin object is declared
     */
    UserAdmin userAdmin;
    MockUserTable mockUserTable;
    // Declaration and initialisation of testing variables
    private String sessionToken;
    private String callingUser = "testUser";
    private String dummySalt = "68b91e68f846f39f742b4e8e5155bd6ac5a4238b7fc4360becc02b064c006433";
    private String dummyPasswordFromCp = "password";
    private String dummyHashedPassword = "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8";// hash(dummyPasswordFromCp);
    private String dummyHashedSaltedPassword = "6df8615d2bf6d4a2f43287b0061682ffad743230739fba51c97777d6a51545ce"; // hash(dummyHashedPassword + dummySalt);
    private Boolean createBillboard = true;
    private Boolean editBillboard = true;
    private Boolean scheduleBillboard = true;
    private Boolean editUser = true;
    private ArrayList<Object> dummyValues;
    // Defining additional testing users
    private String basicUser = "basicUser";
    private String testUser = "test";
    private String createBillboardUser = "createBillboardUser";
    private String editBillboardUser = "editBillboardUser";
    private String editScheduleUser = "editScheduleUser";
    private String editUserUser = "editUserUser";
    // Defining permissions to be tested
    private ArrayList<Boolean> fullPermissions = new ArrayList<>(Arrays.asList(true,true,true,true));
    private ArrayList<Boolean> basicPermissions = new ArrayList<>(Arrays.asList(false, false, false, false));
    private ArrayList<Boolean> createBillboardPermission = new ArrayList<>(Arrays.asList(true, false, false, false));
    private ArrayList<Boolean> editBillboardPermission = new ArrayList<>(Arrays.asList(false, true, false, false));
    private ArrayList<Boolean> editSchedulePermission = new ArrayList<>(Arrays.asList(false, false, true, false));
    private ArrayList<Boolean> editUserPermission = new ArrayList<>(Arrays.asList(false, false, false, true));


    /* Test 1: Constructing a UserAdmin and Mock User Table object
     * Description: UserAdmin and MockUserTable Objects should be able to be created
     * Expected Output: UserAdmin and MockUserTable objects able to be instantiated from their respective classes.
     */
    @BeforeEach @Test
    public void setUpUserAdmin() throws IOException, SQLException, NoSuchAlgorithmException {
        userAdmin = new UserAdmin();
        mockUserTable = new MockUserTable();

        // Populate Mock User Table and Generate Values as required - For Unit Testing
        sessionToken = MockSessionTokens.generateMockToken(callingUser);
        dummyValues = new ArrayList<>();
        dummyValues.add(dummyHashedSaltedPassword);
        dummyValues.add(dummySalt);
        dummyValues.add(createBillboard);
        dummyValues.add(editBillboard);
        dummyValues.add(scheduleBillboard);
        dummyValues.add(editUser);
        MockUserTable.populateDummyData(callingUser, dummyValues);

        // Populate Database Table - For Integrated Testing
        // Start with a fresh test user each test
        if (!DbUser.retrieveUser(callingUser).isEmpty()) {
            DbUser.deleteUser(callingUser);
        }
        DbUser.addUser(callingUser, dummyHashedSaltedPassword, dummySalt, createBillboard, editBillboard, scheduleBillboard, editUser);
        sessionToken = (String) login(callingUser, dummyHashedPassword); // generate a test token to be used by other functions
    }

    // -- UNIT TESTS WITH MOCK USER TABLE -- //
    /* Test 2: Check User Exists (Helper for other methods in this class) (Pass)
     * Description: Check that a user exists in the database - helper method
     * Expected Output: A boolean where true is returned if the user is found in the Mock Table and false otherwise
     */
    @Test
    public void mockUserExists() {
        assertAll("Check for Existing User",
                // Ensure that these users don't exist in the Fake DB.
                ()-> assertFalse(MockUserTable.userExists("non-existent-user")),
                // Check for case sensitivity
                ()-> assertFalse(MockUserTable.userExists("testuser")),
                // Check for trailing whitespace stripping
                ()-> assertFalse(MockUserTable.userExists("testuser ")),
                // Check for empty
                ()-> assertFalse(MockUserTable.userExists("")),
                // Check for valid
                ()-> assertTrue(MockUserTable.userExists("testUser"))
        );
    }


    /* Test 3: Create Users (Pass)
     * Description: Create the corresponding username in the Mock User Table with the hashed password and permissions
     * and return acknowledgement to Control Panel.
     * Expected Output: User is created in the DB and returns string "Pass: User Created"
     */
    @Test
    public void mockCreateUser() {
        // Test setup - Ensure the user to be created does not already exist
        String dummyHashedPassword = "794b258f6780a0606f35aeac1d1b747bc81658f276a12b1fa58009a8a2bcf23c";
        String sessionToken = MockSessionTokens.generateMockToken(callingUser);
        ServerAcknowledge dbResponse = mockUserTable.createUser(sessionToken, callingUser, dummyHashedPassword,
                true, true, true, true);
        assertEquals(Success, dbResponse);
        // Check that the user is actually added to the DB
        assertTrue(MockUserTable.userExists(callingUser));
    }
    // -- END UNIT TESTS -- //
//TODO: THERE IS A LOT OF THESE TO DO...WAITING TO SEE WHAT TIM WANTS US TO DO WITH IT.



    // -- START INTEGRATED TESTS -- //
    // -- DEPENDENCY: REQUIRE SERVER TO BE RUNNING IN THE BACKGROUND -- //

    /* Test 2: Check User Exists (Helper for other methods in this class)
     * Description: Check that a user exists in the database - helper method
     * Expected Output: A boolean where true is returned if the user is found in the DB and false otherwise
     */
    @Test
    public void userExists() {
      assertAll("Check for Existing User",
        // Ensure that these users don't exist in the Fake DB.
        ()-> assertFalse(UserAdmin.userExists("non-existent-user")),
        // Check for case sensitivity
        ()-> assertFalse(UserAdmin.userExists("testuser")),
        // Check for trailing whitespace stripping
        ()-> assertFalse(UserAdmin.userExists("testuser ")),
        // Check for empty
        ()-> assertFalse(UserAdmin.userExists("")),
        // Check for valid
        ()-> assertTrue(UserAdmin.userExists("testUser"))
      );
    }


    /* Test 3: View Other User's Permissions
     * Description: Check that only users with "Edit Permissions" can see any user's permissions
     * Order - Create Billboards, Edit All Billboards, Schedule Billboards, Edit Users
     * Expected Output: Given the requested username, the method should return associated permissions.
     */
    @Test
    public void getOtherUserPermissions() throws IOException, SQLException {
        // Test Setup - Create the users if they do not exist
        if (DbUser.retrieveUser(basicUser).isEmpty()) {
            DbUser.addUser(basicUser, dummyHashedSaltedPassword, dummySalt, false, false, false, false);
        }
        if (DbUser.retrieveUser(createBillboardUser).isEmpty()) {
            DbUser.addUser(createBillboardUser, dummyHashedSaltedPassword, dummySalt, true, false, false, false);
        }
        if (DbUser.retrieveUser(editBillboardUser).isEmpty()) {
            DbUser.addUser(editBillboardUser, dummyHashedSaltedPassword, dummySalt, false, true, false, false);
        }
        if (DbUser.retrieveUser(editScheduleUser).isEmpty()) {
            DbUser.addUser(editScheduleUser, dummyHashedSaltedPassword, dummySalt, false, false, true, false);
        }
        if (DbUser.retrieveUser(editUserUser).isEmpty()) {
            DbUser.addUser(editUserUser, dummyHashedSaltedPassword, dummySalt, false, false, false, true);
        }
        assertAll("Check for a few Possible User Permission Combinations",
            ()-> assertEquals(basicPermissions, getUserPermissions(sessionToken, basicUser)),
            ()-> assertEquals(createBillboardPermission, getUserPermissions(sessionToken, createBillboardUser)),
            ()-> assertEquals(editBillboardPermission, getUserPermissions(sessionToken, editBillboardUser)),
            ()-> assertEquals(editSchedulePermission, getUserPermissions(sessionToken, editScheduleUser)),
            ()-> assertEquals(editUserPermission, getUserPermissions(sessionToken, editUserUser))
        );
    }


    /* Test 4: Get Own User Permissions
     * Description: Check that any user can see their own user permissions (int[4])
     * Order - Create Billboards, Edit All Billboards, Schedule Billboards, Edit Users
     * Expected Output: Given the user's own username, the method should return associated permissions
     */
    @Test
    public void getOwnUserPermissions() throws IOException, SQLException, NoSuchAlgorithmException {
        // Test Setup - Create the users if they do not exist
        if (DbUser.retrieveUser(basicUser).isEmpty()) {
            DbUser.addUser(basicUser, dummyHashedSaltedPassword, dummySalt, false, false, false, false);
        }
        if (DbUser.retrieveUser(createBillboardUser).isEmpty()) {
            DbUser.addUser(createBillboardUser, dummyHashedSaltedPassword, dummySalt, true, false, false, false);
        }
        if (DbUser.retrieveUser(editBillboardUser).isEmpty()) {
            DbUser.addUser(editBillboardUser, dummyHashedSaltedPassword, dummySalt, false, true, false, false);
        }
        if (DbUser.retrieveUser(editScheduleUser).isEmpty()) {
            DbUser.addUser(editScheduleUser, dummyHashedSaltedPassword, dummySalt, false, false, true, false);
        }
        if (DbUser.retrieveUser(editUserUser).isEmpty()) {
            DbUser.addUser(editUserUser, dummyHashedSaltedPassword, dummySalt, false, false, false, true);
        }
        // Test Setup - Need to create tokens for each of the users
        String basicToken = (String) login(basicUser, dummyHashedPassword);
        String createBillboardToken = (String) login(createBillboardUser, dummyHashedPassword);
        String editBillboardToken = (String) login(editBillboardUser, dummyHashedPassword);
        String ediScheduleToken = (String) login(editScheduleUser, dummyHashedPassword);
        String editUserToken = (String) login(editUserUser, dummyHashedPassword);

        assertAll("Check for a few Possible Own User Permission Combinations",
                ()-> assertEquals(basicPermissions, getUserPermissions(basicToken, basicUser)),
                ()-> assertEquals(createBillboardPermission, getUserPermissions(createBillboardToken, createBillboardUser)),
                ()-> assertEquals(editBillboardPermission, getUserPermissions(editBillboardToken, editBillboardUser)),
                ()-> assertEquals(editSchedulePermission, getUserPermissions(ediScheduleToken, editScheduleUser)),
                ()-> assertEquals(editUserPermission, getUserPermissions(editUserToken, editUserUser))
        );
    }


    /* Test 5: Get Other User's Permissions (Exception Handling)
     * Description: Get other User's Permissions from db - throw exception due to non-existent calling username in DB
     * Expected Output: User's Permissions unable to be retrieved from DB and returns "Fail: Invalid Session Token"
     */
    @Test
    public void getOtherUserPermissionsCallingUserDeleted() throws IOException, SQLException, NoSuchAlgorithmException {
        // Temporarily change calling username to something unknown (via the session token)
        // Test setup - Create another admin user to delete the calling user
        if (!DbUser.retrieveUser(testUser).isEmpty()) {
            DbUser.deleteUser(testUser); // Clean user
        }
        System.out.println("The test user does not exists, so it will be created.");
        DbUser.addUser(testUser, dummyHashedSaltedPassword, dummySalt, createBillboard, editBillboard, scheduleBillboard, editUser);

        // Use other admin to delete the actual calling user
        String otherToken = (String) login(testUser, dummyHashedPassword);
        UserAdmin.deleteUser(otherToken, callingUser); // Should expire all tokens associated with the user deleted

        Object dbResponseViewOtherUser = userAdmin.getUserPermissions(sessionToken, testUser);
        Object dbResponseViewDeletedSelf = userAdmin.getUserPermissions(sessionToken, callingUser);
        // Check return value
        assertEquals(InvalidToken, dbResponseViewOtherUser);
        assertEquals(InvalidToken, dbResponseViewDeletedSelf);
    }


    /* Test 6: Get Other User's Permissions (Exception Handling)
     * Description: Get other User's Permissions from db - throw exception due to insufficient calling permissions
     * Require "EditUsers" permission which is the 4th element in UserPermissions object
     * e.g. [1,1,1,0] can't call the method.
     * Expected Output: User's Permissions unable to be retrieved and returns InsufficientUserPermission
     */
    @Test
    public void getOtherUserPermissionsInsufficientPermissions() throws IOException, SQLException, NoSuchAlgorithmException {
        // Temporarily change to basic user as the calling user (via the session token)
        String basicToken = (String) login(basicUser, dummyHashedPassword);
        Object dbResponse =  getUserPermissions(basicToken, testUser);
        // Check return value
        assertEquals(InsufficientPermission, dbResponse);
    }


    /* Test 7: Get Other User's Permissions (Exception Handling)
     * Description: Get other User's Permissions from db - throw exception due to non-existent username in DB
     * Expected Output: User's Permissions unable to be retrieved from DB and returns "Fail: Username Does Not Exist"
     */
    @Test
    public void getOtherUserPermissionsNoUsernameInDb() throws IOException, SQLException {
        // Test setup - Ensure the user to be deleted does not exist in DB
        if (UserAdmin.userExists(testUser)) {
            System.out.println("The test user exists, so it will be deleted for this test.");
            UserAdmin.deleteUser(sessionToken, testUser);
            assertFalse(UserAdmin.userExists(testUser));
        }
        // Check return value - session should be invalid now
        Object dbResponse = UserAdmin.getUserPermissions(sessionToken, testUser);
        assertEquals(NoSuchUser, dbResponse);
    }


    /* Test 8: List Users (Pass)
     * Description: List all of the users in the database if calling username has "Edit Users" permissions
     * Note: The calling username is retrieved as a private field from this UserAdmin Class
     * Expected Output: All of the users in the database are able to be listed.
     */
    @Test
    public void listUsers() throws IOException, SQLException {
      ArrayList<String> dbResponse = (ArrayList<String>) userAdmin.listUsers(sessionToken);
      assertTrue(dbResponse.contains(callingUser));
    }


    /* Test 9: List Users (Exception Handling)
     * Description: List all of the users in the database - throw exception due to non-existent calling username
     * (e.g. if someone else deleted you whilst logged in).
     * Expected Output: List of Users unable to be retrieved from DB and returns "Fail: Invalid Session Token"
     */
    @Test
    public void listUsersCallingUserDeleted() throws IOException, SQLException, NoSuchAlgorithmException {
        // Test setup - Create another admin user to delete the calling user
        if (!DbUser.retrieveUser(testUser).isEmpty()) {
            DbUser.deleteUser(testUser); // Clean user
        }
        System.out.println("The test user does not exists, so it will be created.");
        DbUser.addUser(testUser, dummyHashedSaltedPassword, dummySalt, createBillboard, editBillboard, scheduleBillboard, editUser);

        // Use other admin to delete the actual calling user
        String otherToken = (String) login(testUser, dummyHashedPassword);
        UserAdmin.deleteUser(otherToken, callingUser); // Should expire all tokens associated with the user deleted
        ServerAcknowledge dbResponse = (ServerAcknowledge) userAdmin.listUsers(sessionToken);
        // Check return value
        assertEquals(dbResponse, InvalidToken);
    }


    /* Test 10: List Users (Exception Handling)
     * Description: List all of the users in the database - throw exception due to insufficient permissions
     * Require "EditUsers" permission which is the 4th element in UserPermissions object
     * e.g. [1,1,1,0] can't call the method.
     * Expected Output: List of Users unable to be retrieved from DB and returns "Fail: Insufficient User Permission"
     */
    @Test
    public void listUsersInsufficientPermissions() throws IOException, SQLException, NoSuchAlgorithmException {
        // Ensure basic user exists with desired password
        if (!UserAdmin.userExists(basicUser)) {
            System.out.println("The basic user does not exists, so it will be created.");
            DbUser.addUser(basicUser, dummyHashedSaltedPassword, dummySalt, false, false, false, false);
        }
        // Check return value - calling username should have insufficient permissions now
        String basicToken = (String) login(basicUser, dummyHashedPassword);
        ServerAcknowledge dbResponse = (ServerAcknowledge) userAdmin.listUsers(basicToken);
        // Check return value
        assertEquals(InsufficientPermission, dbResponse);
    }


    /* Test 11: Set Own User Permissions (Pass)
     * Description: Check that the calling user has "EditUsers" permission, then find corresponding username in db
     * (if it exists) and then modify to the specified permissions and return string acknowledgement to Control Panel.
     * Expected Output: User permissions updated in the DB and returns string "Pass: Own Permissions Updated"
     */
    @Test
    public void setOwnUserPermissions() throws IOException, SQLException, NoSuchAlgorithmException {
        // Test setup - Ensure that the user starts with all permissions
        if (!DbUser.retrieveUser(testUser).isEmpty()) {
            DbUser.deleteUser(testUser); // Clean user
        }
        System.out.println("The test user does not exists, so it will be created.");
        DbUser.addUser(testUser, dummyHashedSaltedPassword, dummySalt, createBillboard, editBillboard, scheduleBillboard, editUser);
        String testToken = (String) login(testUser, dummyHashedPassword); // calling user
        // Attempt to remove own CreateBillboardsPermission (first element in permissions boolean array list)
        ServerAcknowledge dbResponse = userAdmin.setUserPermissions(testToken, testUser, false, false, false, true);
        // Check return value
        assertEquals(Success, dbResponse);
        // Check that the user permissions are actually updated in the DB
        assertEquals(editUserPermission, userAdmin.getUserPermissions(testToken, testUser));
    }


    /* Test 12: Set Own User Permissions (Exception Handling)
     * Description: Check that the calling user has "EditUsers" permission, then find corresponding username in db
     * (if it exists) and then modify to the specified permissions and return string acknowledgement to Control Panel.
     * Expected Output: User permissions not updated in DB and returns "Fail: Cannot Remove Own Edit Users Permission"
     */
    @Test
    public void removeOwnUserEditUsersPermission() throws IOException, SQLException, NoSuchAlgorithmException {
        // Test setup - Ensure that the user starts with all permissions
        if (!DbUser.retrieveUser(testUser).isEmpty()) {
            DbUser.deleteUser(testUser); // Clean user
        }
        System.out.println("The test user does not exists, so it will be created.");
        DbUser.addUser(testUser, dummyHashedSaltedPassword, dummySalt, createBillboard, editBillboard, scheduleBillboard, editUser);
        String testToken = (String) login(testUser, dummyHashedPassword); // calling user
        // Attempt to remove own EditUsersPermission (last element)
        ServerAcknowledge dbResponse = userAdmin.setUserPermissions(testToken, testUser, true,true,true,false);
        // Check return value
        assertEquals(CannotRemoveOwnAdminPermission, dbResponse);
        // Check that the user permissions are not updated in the DB
        assertEquals(fullPermissions, userAdmin.getUserPermissions(testToken,testUser));
    }


    /* Test 13: Set Own User Permissions (Exception Handling)
     * Description: Check that the calling user has "EditUsers" permission, then find corresponding username in db
     * (if it exists) and then modify to the specified permissions and return string acknowledgement to Control Panel.
     * This test will check for appropriate handling of: if someone else deleted you before you click the submit button
     * Expected Output: User permissions not updated in DB and returns "Fail: Invalid Session Token"
     */
//    @Test
//    public void setOwnUserPermissionscallingUserDeleted() {
//      // Test setup - Ensure that the calling user does not exist
//      if (userAdmin.userExists("sessionToken", "non-existent")) {
//          userAdmin.deleteUser("sessionToken", "non-existent");
//      }
//      String dbResponse = userAdmin.setUserPermissions("unknownSessionToken", "non-existent", {0,1,1,1});
//      // Check return value
//      assertEquals("Fail: Invalid Session Token", dbResponse);
//      // Check that the user permissions are still unobtainable
//      assertThrows(UsernameNotFoundException.class, () -> {
//          userAdmin.getUserPermissions("sessionToken", "non-existent"));
//      }
//    }


    /* Test 14: Set Own User Permissions (Exception Handling)
     * Description: Attempt to set user permissions however calling user does not have "EditUser" Permission
     * Expected Output: User permissions not updated in DB and returns "Fail: Insufficient User Permission"
     */
//    @Test
//    public void setOwnUserPermissionsInsufficientPermissions() {
//      // Test setup - Ensure that the user starts with different permissions
//      if (userAdmin.getUserPermissions("sessionToken", "Joe") !== {0,0,0,0} ) {
//          userAdmin.setUserPermissions("sessionToken", "Joe", {0,0,0,0});
//      }
//      String dbResponse = userAdmin.setUserPermissions("basicToken", "Joe", {0,0,0,1});
//      // Check return value
//      assertEquals("Fail: Insufficient User Permission", dbResponse);
//      // Check that the user permissions are not updated in the DB
//      assertEquals({0,0,0,0}, userAdmin.getUserPermissions("Joe"));
//    }



    /* Test 15: Set Other User Permissions (Pass)
     * Description: Check that the calling user has "EditUsers" permission, then find corresponding username in db
     * (if it exists) and then modify to the specified permissions and return string acknowledgement to Control Panel.
     * Expected Output: User permissions updated in the DB and returns string "Pass: Other User Permissions Updated"
     */
//    @Test(expected = Test.None.class /* no exception expected */)
//    public void setOtherUserPermissions() {
//      // Test Setup - Ensure that the user exists and has different permissions to start
//      if ( !userAdmin.userExists("sessionToken", "Jenny") ) {
//          userAdmin.createUser("sessionToken", "Jenny", "pass", {0,1,1,1});
//      }
//      elseif ( userAdmin.getUserPermissions("Jenny") !== {0,1,1,1} ) {
//          userAdmin.setUserPermissions("sessionToken", "Jenny", {0,1,1,1});
//      }
//      // Attempt to give CreateBillboards Permission and Remove Edit Users
//      String dbResponse = userAdmin.setUserPermissions("sessionToken", "Jenny", {1,1,1,0});
//      // Check return value
//      assertEquals("Pass: Other User Permissions Updated", dbResponse);
//      // Check that the user permissions are actually updated in the DB
//      assertEquals({1,1,1,0}, userAdmin.getUserPermissions("Jenny"));
//    }


    /* Test 16: Set Other User Permissions (Exception Handling)
     * Description: Check that the calling user has "EditUsers" permission, then find corresponding username in db
     * (if it exists) and then modify to the specified permissions and return string acknowledgement to Control Panel.
     * This test will check for appropriate handling of: if someone else deleted you before you click the submit button
     * Expected Output: User permissions not updated in DB and returns "Fail: Invalid Session Token"
     */
//    @Test
//    public void setOtherUserPermissionscallingUserDeleted() {
//      // Test setup - Ensure that the user starts with different permissions and that the calling user does not exist
//      if (userAdmin.getUserPermissions("sessionToken", "root") !== {1,1,1,1} ) {
//          userAdmin.setUserPermissions("sessionToken", "root", {1,1,1,1});
//      }
//      if (userAdmin.userExists("sessionToken", "non-existent")) {
//          userAdmin.deleteUser("sessionToken", "non-existent");
//      }
//      String dbResponse = userAdmin.setUserPermissions("unknownSessionToken", "root", {0,1,1,1});
//      // Check return value
//      assertEquals("Fail: Invalid Session Token", dbResponse);
//      // Check that the user permissions are not updated in the DB
//      assertEquals({1,1,1,1}, userAdmin.getUserPermissions("root"));
//    }


    /* Test 17: Set Other User Permissions (Exception Handling)
     * Description: Attempt to set user permissions however calling user does not "EditUser" Permission
     * Expected Output: User permissions not updated in DB and returns "Fail: Insufficient User Permission"
     */
//    @Test
//    public void setOtherUserPermissionsInsufficientPermissions() {
//      // Test setup - Ensure that the user starts with different permissions
//      if (userAdmin.getUserPermissions("sessionToken", "Jenny") !== {1,1,1,0} ) {
//          userAdmin.setUserPermissions("sessionToken", "Jenny", {1,1,1,0});
//      }
//      String dbResponse = userAdmin.setUserPermissions("basicToken", "Jenny", {0,0,0,0});
//      // Check return value
//      assertEquals("Fail: Insufficient User Permission", dbResponse);
//      // Check that the user permissions are not updated in the DB
//      assertEquals({1,1,1,0}, userAdmin.getUserPermissions("Jenny"));
//    }


    /* Test 18: Set Other User Permissions (Exception Handling)
     * Description: Attempt to set user's permissions but throw exception due to non-existent requested username in db
     * (e.g. if someone else deleted the other user whilst logged in).
     * Expected Output: User permissions not updated in DB and returns "Fail: Username Does Not Exist"
     */
//    @Test
//    public void setOtherUserPermissionsNoUsernameInDb() {
//      // Test setup - Ensure that the username does not exist
//      if (userAdmin.userExists("sessionToken", "non-existent")) {
//          userAdmin.deleteUser("sessionToken", "non-existent");
//      String dbResponse = userAdmin.setUserPermissions("sessionToken", "non-existent", {0,1,1,1});
//      // Check return value
//      assertEquals("Fail: Username Does Not Exist", dbResponse);
//      // Check that the user permissions are still unobtainable
//      assertThrows(UsernameNotFoundException.class, () -> {
//          userAdmin.getUserPermissions("non-existent"));
//      }
//    }


    /* Test 19: Get Password (Pass)
     * Description: Find corresponding username in db (if it exists) and then return the password
     * Expected Output: Password is retrieved from the DB and is returned as a string
     */
//    @Test
//    public void getPassword() {
//      //TODO: Ensure user in fake db with "test" as password
//      string dbResponse = userAdmin.getPassword("sessionToken", "user");
//      // Check return value
//      assertEquals("test", dbResponse);
//    }


    /* Test 20: Get Password (Exception Handling)
     * Description: Find corresponding username in db (if it exists) and then return the password
     * Expected Output: UsernameNotFoundException is thrown as the username does not exist in the DB
     */
//    @Test
//    public void getPassword() {
//      // Test setup - Ensure the username does not exist in DB
//      if (userAdmin.userExists("non-existent")) {
//          userAdmin.deleteUser("sessionToken", "non-existent");
//      }
//      // Check that exception is thrown
//      assertThrows(UsernameNotFoundException.class, () -> {
//          string dbResponse = userAdmin.getPassword("sessionToken", "non-existent");
//      });
//    }

    //TODO: Should maybe test for get password where they have insufficient permission

    /* Test 21: Set Own Password (Pass)
     * Description: Find corresponding username in db (if it exists) and then modify to the hashed password and
     *              return acknowledgement string to Control Panel.
     * Expected Output: Hashed password updated in the DB and returns string "Pass: Own Password Updated"
     */
//    @Test
//    public void setOwnPassword() {
//     // Test setup - Ensure the user's original password is different
//     if (userAdmin.getPassword("sessionToken", "newUser") == "changedPass") {
//          userAdmin.setPassword("sessionToken", "newUser", "pass");
//      }
//      string dbResponse = userAdmin.setPassword("basicToken", "Joe", "changedPass");
//      // Check return value
//      assertEquals("Pass: Own Password Updated", dbResponse);
//      // Check that the user pass is actually updated in the DB
//      assertEquals("changedPass",userAdmin.getPassword("sessionToken","Joe"));
//    }


    /* Test 22: Set Own Password (Exception Handling)
     * Description: Set own user password in the database - throw exception due to non-existent calling username
     * (e.g. if someone else deleted you whilst logged in).
     * Expected Output: Hashed password not updated in the DB and returns string "Fail: Invalid Session Token"
     */
//    @Test
//    public void setOwnPasswordcallingUserDeleted() {
//      // Test setup - Ensure the user exists with the expected password in the DB
//      if (userAdmin.userExists("non-existent")) {
//          userAdmin.deleteUser("sessionToken", "non-existent");
//      }
//      string dbResponse = userAdmin.setPassword("unknownSessionToken", "non-existent", "changedPass");
//      // Check return value
//      assertEquals("Fail: Invalid Session Token", dbResponse);
//      // Check for Exception that the password cannot be obtained for user that does not exist in DB
//      assertThrows(UsernameNotFoundException.class, () -> {
//          string dbResponse = userAdmin.getPassword("sessionToken", "non-existent");
//      });
//    }


    /* Test 23: Set Other User Password (Pass)
     * Description: Check that the calling user has "EditUsers" permission, then find corresponding username in db
     * (if it exists) and then modify to the hashed password and return acknowledgement (String) to Control Panel.
     * Expected Output: Hashed password updated in the DB and returns string "Pass: Other User Password Updated"
     */
//    @Test(expected = Test.None.class /* no exception expected */)
//    public void setOtherPassword() {
//     // Test setup - Ensure the user's original password is different
//     if (userAdmin.getPassword("sessionToken", "newUser") == "changedPass") {
//          userAdmin.setPassword("sessionToken", "newUser", "pass");
//      }
//      String dbResponse = userAdmin.setPassword("sessionToken", "newUser", "changedPass");
//      // Check return value
//      assertEquals("Pass: Other User Password Updated", dbResponse);
//      // Check that the user pass is actually updated in the DB
//      assertEquals("changedPass", userAdmin.getPassword("sessionToken", "newUser"));
//    }


    /* Test 24: Set Other User Password (Exception Handling)
     * Description: Check that the calling user still exists in the DB before setting user password.
     * Expected Output: Hashed password not updated in the DB and returns string "Fail: Invalid Session Token"
     */
//    @Test
//    public void setOtherPasswordcallingUserDeleted() {
//      // Test setup - Ensure the user exists with the expected password in the DB
//      if (!userAdmin.userExists("Jenny")) {
//          userAdmin.createUser("sessionToken", "Jenny", {0,0,0,0}, "pass");
//      }
//      if (userAdmin.getPassword("sessionToken", "Jenny") !== "pass") {
//          userAdmin.setPassword("sessionToken", "Jenny", "pass");
//      }
//      String dbResponse = userAdmin.setPassword("unknownSessionToken", "Jenny", "changedPass");
//      // Check return value
//      assertEquals("Fail: Invalid Session Token", dbResponse);
//      // Check that the user pass is not actually still updated in the DB
//      assertEquals("pass",userAdmin.getPassword("sessionToken", "Jenny"));
//    }


    /* Test 25: Set Other User Password (Exception Handling)
     * Description: Check that if the calling user does not have "EditUsers" permission that they are unable to
     * modify password of other users.
     * Expected Output: Hashed password not updated in the DB and returns string "Fail: Insufficient User Permission"
     */
//    @Test
//    public void setOtherPasswordInsufficientPermissions() {
//      // Test setup - Ensure the user exists with the expected password in the DB
//      if (!userAdmin.userExists("Jenny")) {
//          userAdmin.createUser("sessionToken", "Jenny", {0,0,0,0}, "pass");
//      }
//      if (userAdmin.getPassword("sessionToken", "Jenny") !== "pass") {
//          userAdmin.setPassword("sessionToken", "Jenny", "pass");
//      }
//      String dbResponse = userAdmin.setPassword("basicToken", "Jenny", "changedPass");
//      // Check return value
//      assertEquals("Fail: Insufficient User Permission", dbResponse);
//      // Check that the user pass is not actually still updated in the DB
//      assertEquals("pass",userAdmin.getPassword("sessionToken", "Jenny"));
//    }


    /* Test 26: Set Other User Password (Exception Handling)
     * Description: Check that if the username associated with the hashed password does not exist in database then
     * the password should not be updated and an exception should be thrown.
     * Expected Output: Hashed password not updated in the DB and returns string "Fail: Username Does Not Exist"
     */
//    @Test
//    public void setOtherPasswordNoUsernameInDb() {
//      // Test setup - Ensure the user to have password updated does not exist in DB
//      if (userAdmin.userExists("unknownUser")) {
//          userAdmin.deleteUser("sessionToken", "unknownUser");
//      }
//      String dbResponse = userAdmin.setPassword("sessionToken", "unknownUser", "changedPass");
//      // Check return value
//      assertEquals("Fail: Username Does Not Exist", dbResponse);
//      // Check for Exception that the password cannot be obtained for user that does not exist in DB
//      assertThrows(UsernameNotFoundException.class, () -> {
//          string dbResponse = userAdmin.getPassword("sessionToken", "unknownUser");
//      });//    }


    /* Test 27: Delete User (Pass)
     * Description:
     * Check that the calling user has "EditUsers" permission, then find corresponding username in db
     * (if it exists) and then remove and return acknowledgement to Control Panel.
     * Expected Output: Username is deleted in DB and returns string "Pass: User Deleted"
     */
    @Test
    public void deleteUser() throws IOException, SQLException, NoSuchAlgorithmException {
        // Test setup - Ensure the user to be deleted exists in DB
        if (!UserAdmin.userExists(testUser)) {
            System.out.println("The test user does not exists, so it will be created.");
            UserAdmin.createUser(sessionToken, testUser, dummyHashedSaltedPassword, createBillboard, editBillboard, scheduleBillboard, editUser);
        }
        // Check return value
        ServerAcknowledge dbResponse = UserAdmin.deleteUser(sessionToken, testUser);
        assertEquals(Success, dbResponse);
        // Check that the user is actually removed from DB
        assertFalse(UserAdmin.userExists(testUser));
    }

    /* Test 28: Delete User (Exception Handling)
     * Description: Check that the calling user exists and has not been deleted since attempt to call (check on submit)
     * Expected Output: Username is not deleted in DB and returns string "Fail: Invalid Session Token"
     */
    @Test
    public void deleteUserCallingUserDeleted() throws IOException, SQLException, NoSuchAlgorithmException {
        // Test setup - Create another admin user to delete the calling user
        if (!DbUser.retrieveUser(testUser).isEmpty()) {
            DbUser.deleteUser(testUser); // Clean user
        }
        System.out.println("The test user does not exists, so it will be created.");
        DbUser.addUser(testUser, dummyHashedSaltedPassword, dummySalt, createBillboard, editBillboard, scheduleBillboard, editUser);

        // Ensure user to be deleted by calling user exists
        if (!DbUser.retrieveUser(basicUser).isEmpty()) {
            DbUser.deleteUser(basicUser); // Clean user
        }
        System.out.println("The basic user does not exists, so it will be created.");
        DbUser.addUser(basicUser, dummyHashedSaltedPassword, dummySalt, false, false, false, false);

        // Use other admin to delete the actual calling user
        String otherToken = (String) login(testUser, dummyHashedPassword);
        UserAdmin.deleteUser(otherToken, callingUser); // Should expire all tokens associated with the user deleted
        // Check return value - session should be invalid now for the calling user (sessionToken always generated with "callingUser")
        ServerAcknowledge dbResponse = UserAdmin.deleteUser(sessionToken, basicUser);
        assertEquals(InvalidToken, dbResponse);
        // Check that the user is not actually removed from DB
        assertTrue(UserAdmin.userExists(testUser));
    }


    /* Test 29: Delete User (Exception Handling)
     * Description: Check that if the calling user does not have "EditUsers" permission that they are unable to
     * delete other users.
     * Expected Output: Username is not deleted in DB and returns string "Fail: Insufficient User Permission"
     */
    @Test
    public void deleteUserInsufficientPermissions() throws IOException, SQLException, NoSuchAlgorithmException {
        // Test setup - Ensure the user to be deleted exists in DB
        if (!UserAdmin.userExists(testUser)) {
            System.out.println("The test user does not exists, so it will be created.");
            UserAdmin.createUser(sessionToken, testUser, dummyHashedSaltedPassword, createBillboard, editBillboard, scheduleBillboard, editUser);
        }
        // Ensure basic user exists with desired password
        if (!UserAdmin.userExists(basicUser)) {
            System.out.println("The basic user does not exists, so it will be created.");
            DbUser.addUser(basicUser, dummyHashedSaltedPassword, dummySalt, false, false, false, false);
        }
        // Check return value - calling username should have insufficient permissions now
        String basicToken = (String) login(basicUser, dummyHashedPassword);
        ServerAcknowledge dbResponse = UserAdmin.deleteUser(basicToken, testUser);
        assertEquals(InsufficientPermission, dbResponse);
        // Check that the user is not actually removed from DB
        assertTrue(UserAdmin.userExists(testUser));
    }

    /* Test 30: Delete User (Exception Handling)
     * Description: Check that if the username specified does not exist in database then, they should not be deleted
     * and instead an exception should be thrown.
     * Expected Output: Username is not deleted in DB and returns string "Fail: Username Does Not Exist"
     */
    @Test
    public void deleteUserNoUsernameInDb() throws IOException, SQLException, NoSuchAlgorithmException {
        // Test setup - Ensure the user to be deleted does not exist in DB
        if (UserAdmin.userExists(testUser)) {
            System.out.println("The test user exists, so it will be deleted for this test.");
            UserAdmin.deleteUser(sessionToken, testUser);
            assertFalse(UserAdmin.userExists(testUser));
        }
        // Check return value - session should be invalid now
        ServerAcknowledge dbResponse = UserAdmin.deleteUser(sessionToken, testUser);
        assertEquals(NoSuchUser, dbResponse);
        // Check that the user still does not exist in DB
        assertFalse(UserAdmin.userExists(testUser));
    }


    /* Test 31: Delete User (Exception Handling)
     * Description: Check that if the username specified does not exist in database then, they should not be deleted
     * and instead an exception should be thrown.
     * Expected Output: Username is not deleted in DB and returns string "Fail: Cannot Delete Yourself"
     */
    @Test
    public void deleteUserCannotDeleteYourself() throws IOException, SQLException {
        // Check return value - Attempt to delete self from database
        ServerAcknowledge dbResponse = UserAdmin.deleteUser(sessionToken, callingUser);
        assertEquals(CannotDeleteSelf, dbResponse);
        // Check that the user is not actually removed from DB
        assertTrue(UserAdmin.userExists(callingUser));
    }


    /* Test 32: Create User (Pass)
     * Description: Check that the calling user has "EditUsers" permission, then create the corresponding username in
     * the DB with the hashed password and permissions and return acknowledgement to Control Panel.
     * Expected Output: User is created in the DB and returns string "Pass: User Created"
     */
    @Test
    public void createUser() throws IOException, SQLException, NoSuchAlgorithmException {
        // Test setup - Ensure the user to be created does not already exist
        if (UserAdmin.userExists(testUser)) {
            System.out.println("The user exists, so it will be deleted.");
            UserAdmin.deleteUser(sessionToken, testUser);
        }
        // Check return value
        ServerAcknowledge dbResponse = UserAdmin.createUser(sessionToken, testUser, hash("pass"), true, true, true, true);
        assertEquals(Success, dbResponse);
        // Check that the user is actually added to the DB
        assertTrue(UserAdmin.userExists(testUser));
    }

    /* Test 33: Create User (Exception Handling)
     * Description: Check that the calling user exists and has not been deleted since attempt to call (check on submit)
     * Expected Output: Username is not created in DB and returns string "Fail: Invalid Session Token"
     */
    @Test
    public void createUserCallingUserDeleted() throws IOException, SQLException, NoSuchAlgorithmException {
        // Test setup - Create another admin user to delete the calling user
        if (!DbUser.retrieveUser(testUser).isEmpty()) {
            DbUser.deleteUser(testUser); // Clean user
        }
        System.out.println("The test user does not exists, so it will be created.");
        DbUser.addUser(testUser, dummyHashedSaltedPassword, dummySalt, createBillboard, editBillboard, scheduleBillboard, editUser);
        // Ensure the user to be added does not already exist
        if (UserAdmin.userExists(basicUser)) {
            UserAdmin.deleteUser(sessionToken, basicUser);
        }
        // Use other admin to delete the actual "callingUser"
        String otherToken = (String) login(testUser, dummyHashedPassword);
        UserAdmin.deleteUser(otherToken, callingUser); // Should expire tokens associated with callingUser
        // Check return value
        ServerAcknowledge dbResponse = UserAdmin.createUser(sessionToken, basicUser, dummyHashedPassword, false, false, false, false);
        assertEquals(InvalidToken, dbResponse);
        // Check that the user to be created is not added to the DB anyway
        assertFalse(UserAdmin.userExists(basicUser));
    }

    /* Test 34: Create User (Exception Handling)
     * Description: Check that if the calling user does not have "EditUsers" permission that they are unable to
     * create other users.
     * Expected Output: Username is not created in DB and returns string "Fail: Insufficient User Permission"
     */
    @Test
    public void createUserInsufficientPermissions() throws NoSuchAlgorithmException, IOException, SQLException {
        // Test setup - Ensure the user to be created does not already exist
        // Ensure the user to be added does not already exist
        if (!DbUser.retrieveUser(testUser).isEmpty()) {
            DbUser.deleteUser(testUser);
        }
        // Add a basic user for permission testing
        if (DbUser.retrieveUser(basicUser).isEmpty()) {
            DbUser.addUser(basicUser, dummyHashedSaltedPassword, dummySalt, false, false, false, false);
        }
        String basicToken = (String) login(basicUser, dummyHashedPassword);
        // Check return value
        ServerAcknowledge dbResponse = UserAdmin.createUser(basicToken, testUser, dummyHashedPassword, true, true, true, true);
        // Check return value
        assertEquals(InsufficientPermission, dbResponse);
        // Check that the user to be created is not added to the DB anyway
        assertFalse(UserAdmin.userExists(testUser));
    }


    /* Test 35: Create User (Exception Handling)
     * Description: Check that if the desired username does not already exist in the DB (must be unique).
     * Expected Output: Username already exists in DB and returns string "Fail: Username Already Taken"
     */
    @Test
    public void createUserDuplicateUsername() throws IOException, SQLException, NoSuchAlgorithmException {
      // Test Setup - Add the user to the DB if not already in existence
      String duplicateUsername = "duplicateUser";
      if (DbUser.retrieveUser(duplicateUsername).isEmpty()) {
          DbUser.addUser(duplicateUsername, dummyHashedSaltedPassword, dummySalt, false, false, false, false);
      }
      // Attempt to add duplicate username
      ServerAcknowledge dbResponse = UserAdmin.createUser(sessionToken, duplicateUsername, dummyHashedPassword, true, true, true, true);
      // Check return value
      assertEquals(PrimaryKeyClash, dbResponse);
    }
}