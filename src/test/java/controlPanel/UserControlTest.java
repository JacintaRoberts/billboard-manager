package controlPanel;


class UserControlTest {
    /* Test 0: Declaring UserControl object
     * Description: UserControl object should be running in background on application start.
     * Expected Output: UserAdmin object is declared
     */
    UserControl userControl;

    /* Test 1: Constructing a UserControl object
     * Description: UserControl Object should be able to be created on logged in user request from control panel
     * Expected Output: UserControl object is instantiated from UserControl class
     */
//    @BeforeEach
//    @Test
//    public void setUpUserControl() {
//      userControl = new UserControl();
//    }

    /* Test 2: Log out Request (Success)
     * Description: User's request to log out is sent to the server and an acknowledgement is received
     * Expected Output: Successful log out of the user, acknowledgement received and the session token is expired.
     */
//    @Test(expected = Test.None.class /* no exception expected */)
//    public void logOut() {
//      String serverResponse = userControl.logout(sessionToken)
//      assertEquals(serverResponse, "Logout Successful");
//    }

    //TODO: WRITE TESTS FOR EXCEPTION HANDLING OF LIST CURRENT USERS
    //      test 3: list users success (already done below)
    //      test 4: list users exception handling assertThrows(CallingUsernameDeletedException.class, () -> { server resp "Error: Calling Username Deleted"
    //      test 5: list users exception handling assertThrows(InsufficientPermissionsException.class, () -> { server resp "Error: Insufficient User Permissions"

    /* Test 3: Request to server to list Current Users
     * Description: Method to request to server to send a list of active users in the database. Requires a valid
     *              sessionToken.
     * Expected Output: A list of users
     */
//    @Test
//    public void listUsersTest(){
//        userControl.createUserRequest("sessionToken", "NewUser1", "Pass1");
//        userControl.createUserRequest("sessionToken", "NewUser2", "Pass2");
//        List<String> testUserList = new ArrayList<String>();
//        testUserList.add("NewUser1");
//        testUserList.add("NewUser2");
//        UserList userList = userControl.listUsers("sessionToken");
//        assertArrayEquals(testUserList, userList);
//    }

    //TODO: NEED TO WRITE TEST/FIX SET PASSWORD HERE PLEASE (CORRESPOND WITH USERADMINTEST) keep notes for server resp.
    //      test6: set own success           server resp "Success: Own Password Updated"
    //      test7: set own exception handling assertThrows(CallingUsernameDeletedException.class, () -> { server resp "Error: Calling Username Deleted"
    //      test8: set other success         server resp "Success: Other User Password Updated"
    //      test9: set other exception handling assertThrows(CallingUsernameDeletedException.class, () -> { server resp "Error: Calling Username Deleted"
    //      test10: set other exception handling assertThrows(InsufficientPermissionsException.class, () -> {          server resp "Error: Insufficient User Permissions"
    //      test11: set other exception handling assertThrows(UsernameNotFoundException.class, () -> {           server resp "Error: Username Does Not Exist"


    /* Test 6: Request to server to change password (Success)
     * Description: Method to request to server to change a specific users password. Assumes a valid sessionToken is
     *              running, and that user has permission. This test tests for themself.
     * Expected Output: Success response from the server
     */
    //TODO: MAKE NAMES OF ALL TESTS UNIQUE + DESCRIPTIVE, "setUserPasswordRequestTest" -> "setOwnUserPasswordRequest" etc.
//    @Test
//    public void setUserPasswordRequestTest() {
//        userControl.createUserRequest("sessionToken", "NewUser1", "Pass1");
//        userControl.createUserRequest("sessionToken", "NewUser2", "Pass2");
//        String serverResponse = userControl.setUserPasswordRequest("sessionToken", "CAB302", "NewPassword");
//        assertEquals(serverResponse, "Password Change Successfully");
//    }


    /* Test 8: Request to server to change password (Success)
     * Description: Method to request to server to change a specific users password. Assumes a valid sessionToken is
     *              running, and that user has permission. This test tests for otherUsers.
     * Expected Output: Success response from the server
     */
//    @Test
//    public void setUserPasswordRequestTest() {
//        userControl.createUserRequest("sessionToken", "NewUser1", "Pass1");
//        userControl.createUserRequest("sessionToken", "NewUser2", "Pass2");
//        String serverResponse = userControl.setUserPasswordRequest("sessionToken", "NewUser2", "NewPassword");
//        assertEquals(serverResponse, "Password Change Successfully");
//    }


    /* Test 10: Request to server to change password (Exception Handling)
     * Description: Method to request to server to change a specific users password. Assumes a valid sessionToken is
     *              running, and that user has permission. This test tests for insufficent permission
     * Expected Output: Throws InsufficientPermissionsException
     */
//    @Test
//    public void setUserPasswordRequestTest() {
//        userControl.createUserRequest("sessionToken", "NewUser1", "Pass1");
//        userControl.createUserRequest("sessionToken", "NewUser2", "Pass2");
//        String serverResponse = userControl.setUserPasswordRequest("sessionToken", "NewUser2", "NewPassword");
//        assertEquals(serverResponse,"No Such User");
//        assertThrows(NoUserPermissionException);
//   }


    /* Test 11: Request to server to change password (Exception Handling)
     * Description: Method to request to server to change a specific users password. Assumes a valid sessionToken is
     *              running, and that user has permission. This test tests for nonexistent Users.
     * Expected Output: Throws UsernameNotFoundException
     */
//    @Test
//    public void setUserPasswordRequestTest() {
//        String serverResponse = userControl.setUserPasswordRequest("sessionToken", "NewUser3", "NewPassword");
//        assertEqual(serverResponse, "User does not Exist");
//        assertThrows(NoUserValueException);
//    }

    //TODO: NEED TO WRITE TEST/FIX SET PERMISSIONS HERE PLEASE (CORRESPOND WITH USERADMINTEST) keep notes for server resp.
    //      test12: set own success           server resp "Success: Own Permissions Updated"
    //      test13: set own exception handling assertThrows(RemoveOwnEditUsersException.class, () -> { server resp "Error: Cannot Remove Own Edit Users Permission"
    //      test14: set own exception handling assertThrows(CallingUsernameDeletedException.class, () -> { server resp "Error: Calling Username Deleted"
    //      test15: set other exception handling assertThrows(InsufficientPermissionsException.class, () -> {          server resp "Error: Insufficient User Permissions"
    //      test16: set other success           server resp "Success: Other User Permissions Updated"
    //      test17: set other exception handling assertThrows(CallingUsernameDeletedException.class, () -> { server resp "Error: Calling Username Deleted"
    //      test18: set other exception handling assertThrows(InsufficientPermissionsException.class, () -> {          server resp "Error: Insufficient User Permissions"
    //      test19: set other exception handling assertThrows(UsernameNotFoundException.class, () -> {           server resp "Error: Username Does Not Exist"


    /* Test 16: Request to server to set user permissions of a user (Success)
     * Description: Method to set a user permission for another person
     * Expected Output: Success Message
     */
//    @Test
//    public void setUserPermissionTest() {
//      userControl.createUserRequest("sessionToken", "NewUser1", "Pass1");
//      String serverResponse = userControl.setUserPermission("sessionToken", "NewUser1", boolean[] {1,1,1,1});
//      assertEqual(serverResponse, "Permission Changed");
//    }


    /* Test 18: Request to server to set user permissions of a user (Exception Handling)
     * Description: Method to set a user permission for another person. No permission to do so.
     * Expected Output: Exception error
     */
//    @Test
//    public void setUserPermissionTest() {
//      userControl.createUserRequest("sessionToken", "NewUser1", "Pass1");
//      userControl.setUserPermission("sessionToken", "NewUser1", boolean[] {1,1,1,1})
//      String serverResponse = userControl.setUserPermission("sessionToken", "NewUser1", boolean[] {1,1,1,1});
//      assertEquals(serverResponse,"No User Permssion");
//      assertThrows(NoUserPermissionException);
//    }


    /* Test 19: Request to server to set user permissions of a user (Exception Handling)
     * Description: Method to set a user permission for another person. Such Person does not exists. The database does
     *              not have newUser1
     * Expected Output: Exception error
     */
//    @Test
//    public void setUserPermissionTest() {
//      String serverResponse = userControl.setUserPermission("sessionToken", "NewUser1", boolean[] {1,1,1,1});
//      assertEquals(serverResponse,"No Such User");
//      assertThrows(NoUserPermissionException);
//    }


    //TODO: FOR GET USER PERMISSIONS IMPLEMENT TEST FOR EXCEPTION HANDLING
    // test 20: handling assertThrows(CallingUsernameDeletedException.class, () -> { server resp "Error: Calling Username Deleted"
    // test 21: handling assertThrows(InsufficientPermissionsException.class, () -> { server resp "Error: Insufficient User Permissions"
    // test 22: handling assertThrows(UsernameNotFoundException.class, () -> {           server resp "Error: Username Does Not Exist"

    /* Test 20: Request to server to get User Permission from a user (Success)
     * Description: Method to request userpermissions.
     * Expected Output: Return of UserPermission in an array
     */
//    @Test
//    public void getUserPermissionTest(){
//      userControl.createUserRequest("sessionToken", "NewUser1", "Pass1");
//      int[] basicUserPermissionsExpected = {0,0,0,0};
//      int[] basicUserPermissions = userControl.getUserPermission("sessionToken", "User1");;
//      assertTrue(basicUserPermissionsExpected, basicUserPermissions);
//    }

    /* Test 21: Request to server to get User Permission from a user (Exception Handling)
     * Description: Get other User's Permissions from db - throw exception due to insufficient calling permissions
     * Require "EditUsers" permission which is the 4th element in UserPermissions object
     * e.g. [1,1,1,0] can't call the method.
     * Expected Output: The permissions of requested user cannot be retrieved, throw InsufficientPermissionsException
     * TODO: Implement Fake db through HashMap to verify
     */
//    @Test
//    public void getOtherUserPermissionsException() {
//      userControl.createUserRequest("sessionToken", "NewUser1", "Pass1");
//      assertThrows(InsufficientPermissionsException.class, () -> {
//          userControl.getPermissions("root", sessionToken);
//       });
//    }


    //TODO: NEED TO WRITE DELETE USERS TESTS HERE (CORRESPOND WITH USERADMINTEST) keep notes for server resp. like create user below
    //      test23: success                                                     server resp "Success: User Deleted"
    //      test24: assertThrows(CallingUsernameDeletedException.class, () -> { server resp "Error: Calling Username Deleted"
    //      test25: assertThrows(InsufficientPermissionsException.class, () -> { server resp "Error: Insufficient User Permissions"
    //      test26: assertThrows(UsernameNotFoundException.class, () -> {           server resp "Error: Username Does Not Exist"
    //      test27: asserthrows(CannotDeleteOwnUserException.class, () -> {     server resp "Error: Cannot Delete Yourself"


    /* Test 28: Request to server to Create New Users (Success)
     * Description: New method to create new users to the system. This will take a unique username, user permissions,
     *              a password string and a valid sessionToken to create a new user.
     * Expected Output: Server will return "Error: Calling Username Deleted" and an CallingUsernameDeletedException
     *                  will be thrown
     */
//    public void createUserRequest(){
//        String serverResponse = userControl.createUserRequest("NewUser1", {0,0,0,0}, "Pass1", sessionToken);
//        assertEquals("Success: User Created", serverResponse);
//    }

    /* Test 29: Request to server to Create New Users (Exception Handling)
     * Description: New method to create new users to the system. This will take a unique username, user permissions,
     *              a password string and a valid sessionToken to create a new user. This test checks for sufficient
     *              permissions on the calling user.
     * Expected Output: Server will return "Error: Calling Username Deleted" and an CallingUsernameDeletedException
     *                  will be thrown
     * //TODO: NEED TO IMPLEMENT SOME WAY TO CHANGE THE "CALLING USERNAME" IN THIS METHOD RATHER THAN JUST
     *    "USERNAME TO BE CREATED" SO THAT THIS CAN BE ADEQUATELY TESTED
     */
//    public void createUserRequestCallingUsernameDeleted() {
//      assertThrows(CallingUsernameDeletedException.class, () -> {
//        String serverResponse = userControl.createUserRequest("NewUser1", {0,0,0,0}, "Pass1", "sessionToken"));
//      }
//      // Check for correct message received
//      assertEquals("Error: Calling Username Deleted", serverResponse);
//    }

    /* Test 30: Request to server to Create New Users (Exception Handling)
     * Description: New method to create new users to the system. This will take a unique username, user permissions,
     *              a password string and a valid sessionToken to create a new user. This test checks for sufficient
     * permissions on the calling user.
     * Expected Output: An InsufficientPermissionsException will be thrown
     */
//    public void createUserRequestInsufficientPermissions() {
//      // Check for correct exception thrown
//      assertThrows(InsufficientPermissionsException.class, () -> {
//          String serverResponse = userControl.createUserRequest("NewUser1", {0,0,0,0}, "Pass1", "sessionToken"));
//      }
//      // Check for correct message received
//      assertEquals("Error: Insufficient User Permissions", serverResponse);
//    }


    /* Test 31: Request to server to Create New Users (Exception Handling)
     * Description: New method to create new users to the system. This will take a unique username, user permissions,
     *              a password string and a valid sessionToken to create a new user. This will test if the username
     *              provided is not unique.
     * Expected Output: Server will return "Error: Username Already Taken" and an InsufficientPermissionsException
     *                  will be thrown
     */
//    public void createUserRequestDuplicateUsername() {
//      // Check for correct exception thrown
//      assertThrows(InsufficientPermissionsException.class, () -> {
//          String serverResponse == userControl.createUserRequest("NewUser1", {0,0,0,0}, "Pass1", "sessionToken");
//          // If username did not already exist, need to call this method again to throw exception
//          if (serverResponse == "Success: User Created") {
//              serverResponse = userControl.createUserRequest("NewUser1", {0,0,0,0}, "Pass1", "sessionToken");
//          }
//      });
//      // Check for correct message received
//      assertEquals("Error: Username Already Taken", serverResponse);
//    }

}