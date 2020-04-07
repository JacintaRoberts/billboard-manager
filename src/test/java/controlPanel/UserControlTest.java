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
//      userControl = new UserControl("CAB302");
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


    /* Test 3: Request to server to list Current Users (Success)
     * Description: Method to request to server to send a list of active users in the database. Requires a valid
     *              sessionToken.
     * Expected Output: A list of users
     */
//    @Test
//    public void listUsersTest(){
//        userControl.createUserRequest("sessionToken", "NewUser1", "Pass1", {1,1,1,1});
//        userControl.createUserRequest("sessionToken", "NewUser2", "Pass2", {1,1,1,1});
//        List<String> testUserList = new ArrayList<String>();
//        testUserList.add("NewUser1");
//        testUserList.add("NewUser2");
//        UserList userList = userControl.listUsers("sessionToken");
//        assertArrayEquals(testUserList, userList);
//    }


    /* Test 4: Request to server to list Current Users (Fail)
     * Description: Method to request to server to send a list of active users in the database. Requires a valid
     *              sessionToken. Throw exception due to non-existent calling username
     *              (e.g. if someone else deleted you whilst logged in).
     * Expected Output: List of Users unable to be retrieved from DB and returns "Fail: Calling Username Deleted"
     */
//    @Test
//    public void listUsersTestUsernameDeleted(){
//        userControl.createUserRequest("sessionToken", "NewUser1", "Pass1", {1,1,1,1});
//        userControl.createUserRequest("sessionToken", "NewUser2", "Pass2", {1,1,1,1});
//        String serverResponse = userControl.listUsers("sessionToken");
//        assertArrayEquals(serverResponse, "Fail: Calling Username Deleted");
//        assertThrows(DeletedUserException);
//    }


    /* Test 5: Request to server to list Current Users (Fail)
     * Description: Method to request to server to send a list of active users in the database. Requires a valid
     *              sessionToken. Throw exception due to insufficient permission. Assume current user logged in is called "CAB302"
     * Expected Output: List of Users unable to be retrieved from DB and returnsreturns string "Fail: Insufficient User Permissions"
     */
//    @Test
//    public void listUsersTestUsernameDeleted(){
//        noPermissionAdmin = new userControl("NewUser0");
//        userControl.setUserPermissions("sessionToken","NewUser0", {0,0,0,0});
//        userControl.createUserRequest("sessionToken", "NewUser1", "Pass1", {1,1,1,1});
//        userControl.createUserRequest("sessionToken", "NewUser2", "Pass2", {1,1,1,1});
//        userControl2 = new UserControl();
//        String serverResponse = noPermissionAdmin.listUsers("sessionToken");
//        assertArrayEquals(serverResponse, "Fail: Insufficient User Permissions");
//        assertThrows(NoUserPermissionException.class, 
//                     () -> {noPermissionAdmin.getUserPermission("Non-existent");}
//                    );
//    }


    /* Test 6: Request to server to change password (Success)
     * Description: Method to request to server to change a specific users password. Assumes a valid sessionToken is
     *              running, and that user has permission. This test tests for themself. 
     * Expected Output: Success response from the server saying "Success: Own Password Updated"
     */
//    @Test
//    public void setOwnUserPasswordRequest() {
//        userControl.createUserRequest("sessionToken", "NewUser1", "Pass1");
//        userControl.createUserRequest("sessionToken", "NewUser2", "Pass2");
//        String serverResponse = userControl.setUserPasswordRequest("sessionToken", "CAB302", "NewPassword");
//        assertEquals(serverResponse, "Success: Own Password Updated");
//    }


    /* Test 7: Request to server to change password (Fail)
     * Description: Method to request to server to change a specific users password. Assumes a valid sessionToken is
     *              running, and that user has permission. This test tests for themself. This test will test when
     *              the method is called but user is deleted halfway during a session. This will raise an exception
     *              saying that "Fail: Calling Username Deleted".  Assume current user logged in is called "CAB302"
     * Expected Output: Exception raised with message of "Fail: Calling Username Deleted"
     */
//    @Test
//    public void setOwnUserPasswordRequest() {
//        userControl.createUserRequest("sessionToken", "NewUser1", "Pass1");
//        userControl.createUserRequest("sessionToken", "NewUser2", "Pass2");
//        userControl2 = new UserControl();
//        userControl2.deleteUser("sessionToken", "CAB302");
//        String serverResponse = userControl.setUserPasswordRequest("sessionToken", "CAB302", "NewPassword");
//        assertEquals(serverResponse, Fail: Calling Username Deleted");
//        assertThrows(CallingUsernameDeletedException.class, 
//                     () -> { userControl.getUserPermission("non-existent");}
//                    );
//    }


    /* Test 8: Request to server to change password (Success)
     * Description: Method to request to server to change a specific users password. Assumes a valid sessionToken is
     *              running, and that user has permission. This test tests for otherUsers.
     * Expected Output: Success response from the server saying "Success: Password Change Successfully"
     */
//    @Test
//    public void setOtherUserPasswordRequestTest() {
//        userControl.createUserRequest("sessionToken", "NewUser1", "Pass1");
//        userControl.createUserRequest("sessionToken", "NewUser2", "Pass2");
//        String serverResponse = userControl.setUserPasswordRequest("sessionToken", "NewUser2", "NewPassword");
//        assertEquals(serverResponse, "Password Change Successfully");
//    }


    /* Test 9: Request to server to change password (Exception Handling)
     * Description: Method to request to server to change a specific users password. Assumes a valid sessionToken is
     *              running, and that user has permission. This test test for when the useradmin gets deleted.
     * Expected Output: Throws InsufficientPermissionsException
     */
//    @Test
//    public void setOtherUserPasswordRequestNoAdminTest() {
//        userControl.createUserRequest("sessionToken", "NewUser1", "Pass1");
//        userControl.createUserRequest("sessionToken", "NewUser2", "Pass2");
//        userControl2 = new UserControl();
//        userControl2.deleteUser("sessionToken", "CAB302");
//        String serverResponse = userControl.setUserPasswordRequest("sessionToken", "NewUser2", "NewPassword");
//        assertEquals(serverResponse, Fail: Calling Username Deleted");
//        List<String> testUserList = new ArrayList<String>();
//        testUserList.add("NewUser1");
//        testUserList.add("NewUser2");
//        assertArrayEquals(testUserList, userList);
//        assertThrows(CallingUsernameDeletedException.class, 
//                     () -> { userControl.getUserPermission("non-existent");}
//                    );
//   }


    /* Test 10: Request to server to change password (Exception Handling)
     * Description: Method to request to server to change a specific users password. Assumes a valid sessionToken is
     *              running, and that user has permission. This test tests for insufficent permission
     * Expected Output: Throws InsufficientPermissionsException
     */
//    @Test
//    public void setOtherUserPasswordRequestNoPermissionTest() {
//        noPermissionAdmin = new userControl("NewUser0");
//        userControl.setUserPermissions("sessionToken","NewUser0", {0,0,0,0});
//        userControl.createUserRequest("sessionToken", "NewUser1", "Pass1");
//        userControl.createUserRequest("sessionToken", "NewUser2", "Pass2");
//        List<String> testUserList = new ArrayList<String>();
//        testUserList.add("NewUser1");
//        testUserList.add("NewUser2");
//        assertArrayEquals(testUserList, userList);
//        String serverResponse = noPermissionAdmin.setUserPasswordRequest("sessionToken", "NewUser2", "NewPassword");
//        assertArrayEquals(serverResponse, "Fail: Insufficient User Permissions");
//        assertThrows(NoUserPermissionException);
//   }


    /* Test 11: Request to server to change password (Exception Handling)
     * Description: Method to request to server to change a specific users password. Assumes a valid sessionToken is
     *              running, and that user has permission. This test tests for nonexistent Users.
     * Expected Output: Throws UsernameNotFoundException
     */
//    @Test
//    public void setOtherUserPasswordRequestTestNoUser() {
//        String serverResponse = userControl.setUserPasswordRequest("sessionToken", "NewUser3", "NewPassword");
//        assertEqual(serverResponse, "User does not Exist");
//        assertThrows(NoUserValueException);
//    }



    /* Test 12: Request to server to set user permissions of Own User (Success)
     * Description: Method to set a user permission for your own user account. Assume valid session and valid user permissions to do so
     * Expected Output: Success Message of "Success: Own Permissions Updated"
     */
//    @Test
//    public void setOwnUserPermissionTest() {
//      fullPermissionAdmin = new userControl("NewUser0");
//      fullPermissionAdmin.setUserPermissions("sessionToken", "NewUser0", {1,1,1,1});
//      String serverResponse = fullPermissionAdmin.setUserPermission("sessionToken", "CAB302", boolean[] {0,0,0,1});
//      boolean[] userPermissionArray = fullPermissionAdmin.getUserPermission("sessionToken", "NewUser0");
//      assertEqual(serverResponse, "Success: Own Permissions Updated");
//      assertArrayEqual(userPermissionArray, boolean[] {0,0,0,1});
//    }


    /* Test 13: Request to server to set user permissions of a user (Exception Handling)
     * Description: Method to set a user permission for another person. No permission to do so.
     * Expected Output: Exception Fail with insuffice permission Fail "Fail: Cannot Remove Own Edit Users Permission"
     * //TODO: This might be a way to do assert checks ?
     */
//    @Test
//    public void setOwnUserPermissionTestEditUser() {
//      fullPermissionAdmin = new userControl("NewUser0");
//      fullPermissionAdmin.setUserPermissions("sessionToken", "NewUser0", {1,1,1,1});
//      Exception exception = assertThrows(RemoveOwnEditUsersException.class, 
//                                         () -> {fullPermissionAdmin.setUserPermission("sessionToken", "CAB302", boolean[] {0,0,0,0});}
//                                        );
//      assertEquals(exception.getMessage(),"Fail: Cannot Remove Own Edit Users Permission");
//    }


    /* Test 14: Request to server to set user permissions of a user (Exception Handling)
     * Description: Method to set a user permission for another person. User being delted mid session
     * Expected Output: Exception Fail with user deleted halfway "Fail: Calling Username Deleted"
     */
//    @Test
//    public void setOwnUserPermissionTestDeleteMidway() {
//      fullPermissionAdmin = new userControl("NewUser0");
//      fullPermissionAdmin.setUserPermissions("sessionToken", "NewUser0", {1,1,1,1});
//      String serverResponse = userControl.deleteUserRequest("sessionToken", "CAB302");
//      Exception exception = assertThrows(CallingUsernameDeletedException.class,
//                                         () -> {userControl.setUserPermission("sessionToken", "CAB302", boolean[] {0,0,0,1});}
//                                        );
//      assertEquals(exception.getMessage(),"Fail: Calling Username Deleted");
//    }

    //      test15: set other exception handling assertThrows(InsufficientPermissionsException.class, () -> {          server resp "Fail: Insufficient User Permissions"
    //      test16: set other success           server resp "Success: Other User Permissions Updated"
    //      test17: set other exception handling assertThrows(CallingUsernameDeletedException.class, () -> { server resp "Fail: Calling Username Deleted"
    //      test18: set other exception handling assertThrows(InsufficientPermissionsException.class, () -> {          server resp "Fail: Insufficient User Permissions"
    //      test19: set other exception handling assertThrows(UsernameNotFoundException.class, () -> {           server resp "Fail: Username Does Not Exist"


    /* Test 15: Request to server to set user permissions of a user (Exception Handling)
     * Description: Method to set a user permission for another person. User does not have enough permission
     * Expected Output: Exception Fail with user deleted halfway "Fail: Insufficient User Permission"
     */
//    @Test
//    public void setOtherUserPermissionTestNoPermission() {
//      noPermissionAdmin = new userControl("NewUser0");
//      userControl.setUserPermissions("sessionToken","NewUser0", {0,0,0,0});
//      userControl.createUserRequest("sessionToken", "NewUser1", "Pass1");
//      userControl.setUserPermissions("sessionToken","NewUser1", {1,1,0,0});
//      String serverResponse = noPermissionAdmin.setUserPermission("sessionToken", "NewUser1", boolean[] {1,1,1,1});
//      assertEquals(serverResponse, "Fail: Insufficient User Permissions");
//    }


    /* Test 14: Request to server to set user permissions of a user (Exception Handling)
     * Description: Method to set a user permission for another person. User being delted mid session
     * Expected Output: Exception Fail with user deleted halfway "Fail: Calling Username Deleted"
     */
//    @Test
//    public void setOwnUserPermissionTestDeleteMidway() {
//      fullPermissionAdmin = new userControl("NewUser0");
//      fullPermissionAdmin.setUserPermissions("sessionToken", "NewUser0", {1,1,1,1});
//      String serverResponse = userControl.deleteUserRequest("sessionToken", "CAB302");
//      Exception exception = assertThrows(CallingUsernameDeletedException.class,
//                                         () -> {userControl.setUserPermission("sessionToken", "CAB302", boolean[] {0,0,0,1});}
//                                        );
//      assertEquals(exception.getMessage(),"Fail: Calling Username Deleted");
//    }


    /* Test 14: Request to server to set user permissions of a user (Exception Handling)
     * Description: Method to set a user permission for another person. User being delted mid session
     * Expected Output: Exception Fail with user deleted halfway "Fail: Calling Username Deleted"
     */
//    @Test
//    public void setOwnUserPermissionTestDeleteMidway() {
//      fullPermissionAdmin = new userControl("NewUser0");
//      fullPermissionAdmin.setUserPermissions("sessionToken", "NewUser0", {1,1,1,1});
//      String serverResponse = userControl.deleteUserRequest("sessionToken", "CAB302");
//      Exception exception = assertThrows(CallingUsernameDeletedException.class,
//                                         () -> {userControl.setUserPermission("sessionToken", "CAB302", boolean[] {0,0,0,1});}
//                                        );
//      assertEquals(exception.getMessage(),"Fail: Calling Username Deleted");
//    }


    /* Test 14: Request to server to set user permissions of a user (Exception Handling)
     * Description: Method to set a user permission for another person. User being delted mid session
     * Expected Output: Exception Fail with user deleted halfway "Fail: Calling Username Deleted"
     */
//    @Test
//    public void setOwnUserPermissionTestDeleteMidway() {
//      fullPermissionAdmin = new userControl("NewUser0");
//      fullPermissionAdmin.setUserPermissions("sessionToken", "NewUser0", {1,1,1,1});
//      String serverResponse = userControl.deleteUserRequest("sessionToken", "CAB302");
//      Exception exception = assertThrows(CallingUsernameDeletedException.class,
//                                         () -> {userControl.setUserPermission("sessionToken", "CAB302", boolean[] {0,0,0,1});}
//                                        );
//      assertEquals(exception.getMessage(),"Fail: Calling Username Deleted");
//    }


    /* Test 14: Request to server to set user permissions of a user (Exception Handling)
     * Description: Method to set a user permission for another person. User being delted mid session
     * Expected Output: Exception Fail with user deleted halfway "Fail: Calling Username Deleted"
     */
//    @Test
//    public void setOwnUserPermissionTestDeleteMidway() {
//      fullPermissionAdmin = new userControl("NewUser0");
//      fullPermissionAdmin.setUserPermissions("sessionToken", "NewUser0", {1,1,1,1});
//      String serverResponse = userControl.deleteUserRequest("sessionToken", "CAB302");
//      Exception exception = assertThrows(CallingUsernameDeletedException.class,
//                                         () -> {userControl.setUserPermission("sessionToken", "CAB302", boolean[] {0,0,0,1});}
//                                        );
//      assertEquals(exception.getMessage(),"Fail: Calling Username Deleted");
//    }


    /* Test 14: Request to server to set user permissions of a user (Exception Handling)
     * Description: Method to set a user permission for another person. User being delted mid session
     * Expected Output: Exception Fail with user deleted halfway "Fail: Calling Username Deleted"
     */
//    @Test
//    public void setOwnUserPermissionTestDeleteMidway() {
//      fullPermissionAdmin = new userControl("NewUser0");
//      fullPermissionAdmin.setUserPermissions("sessionToken", "NewUser0", {1,1,1,1});
//      String serverResponse = userControl.deleteUserRequest("sessionToken", "CAB302");
//      Exception exception = assertThrows(CallingUsernameDeletedException.class,
//                                         () -> {userControl.setUserPermission("sessionToken", "CAB302", boolean[] {0,0,0,1});}
//                                        );
//      assertEquals(exception.getMessage(),"Fail: Calling Username Deleted");
//    }












    /* Test 19: Request to server to set user permissions of a user (Exception Handling)
     * Description: Method to set a user permission for another person. Such Person does not exists. The database does
     *              not have newUser1
     * Expected Output: Exception Fail
     */
//    @Test
//    public void setUserPermissionTest() {
//      String serverResponse = userControl.setUserPermission("sessionToken", "NewUser1", boolean[] {1,1,1,1});
//      assertEquals(serverResponse,"No Such User");
//      assertThrows(NoUserPermissionException);
//    }


    //TODO: FOR GET USER PERMISSIONS IMPLEMENT TEST FOR EXCEPTION HANDLING
    // test 20: handling assertThrows(CallingUsernameDeletedException.class, () -> { server resp "Fail: Calling Username Deleted"
    // test 21: handling assertThrows(InsufficientPermissionsException.class, () -> { server resp "Fail: Insufficient User Permissions"
    // test 22: handling assertThrows(UsernameNotFoundException.class, () -> {           server resp "Fail: Username Does Not Exist"

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
    //      test24: assertThrows(CallingUsernameDeletedException.class, () -> { server resp "Fail: Calling Username Deleted"
    //      test25: assertThrows(InsufficientPermissionsException.class, () -> { server resp "Fail: Insufficient User Permissions"
    //      test26: assertThrows(UsernameNotFoundException.class, () -> {           server resp "Fail: Username Does Not Exist"
    //      test27: asserthrows(CannotDeleteOwnUserException.class, () -> {     server resp "Fail: Cannot Delete Yourself"



    /* Test 23: Request to server to Delete Current Users (Success)
     * Description: Method to request to server to delete a specified user in the database. Requires a valid
     *              sessionToken.
     * Expected Output: A list of users with success message from server saying "Success: User Deleted"
     */
//    @Test
//    public void deleteUserRequestTest(){
//        userControl.createUserRequest("sessionToken", "NewUser1", "Pass1", {1,1,1,1});
//        userControl.createUserRequest("sessionToken", "NewUser2", "Pass2", {1,1,1,1});
//        String serverResponse = userControl.deleteUserRequest("sessionToken", "NewUser2");
//        List<String> testUserList = new ArrayList<String>();
//        testUserList.add("NewUser1");
//        assertEquals(serverResponse, "Success: User Deleted");
//        UserList userList = userControl.listUsers("sessionToken");
//        assertArrayEquals(testUserList, userList);
//    }


    /* Test 24: Request to server to Delete Current Users (Fail)
     * Description: Method to request to server to delete a specified user in the database. Requires a valid
     *              sessionToken. Throw exception due to non-existent calling username
     *              (e.g. if someone else deleted you whilst logged in).
     * Expected Output: Cannot delete user from DB and returns "Fail: User does not exist"
     */
//    @Test
//    public void deleteUserNoUserTest(){
//        userControl.createUserRequest("sessionToken", "NewUser1", "Pass1");
//        userControl.createUserRequest("sessionToken", "NewUser2", "Pass2");
//        String serverResponse = userControl.deleteUserRequest("sessionToken", "NewUser3");
//        assertArrayEquals(serverResponse, "Fail: User does not exist");
//        List<String> testUserList = new ArrayList<String>();
//        testUserList.add("NewUser1");
//        testUserList.add("NewUser2");
//        assertArrayEquals(testUserList, userList);
//        assertThrows(DeletedUserException);
//    }



    /* Test 25: Request to server to Delete Current Users (Fail)
     * Description: Method to request to server to delete a specified user in the database. Requires a valid
     *              sessionToken. Throw exception due to insuffice permission.
     *              (e.g. if someone else deleted you whilst logged in).
     * Expected Output: Cannot delete user from DB and returns "Fail: Insufficient User Permissions"
     */
//    @Test
//    public void deleteUserNoPermissionTest(){
//        noPermissionAdmin = new userControl("NewUser0");
//        userControl.createUserRequest("sessionToken", "NewUser1", "Pass1");
//        userControl.createUserRequest("sessionToken", "NewUser2", "Pass2");
//        String serverResponse = noPermissionAdmin.deleteUserRequest("sessionToken", "NewUser2");
//        assertArrayEquals(serverResponse, "Fail: Insufficient User Permissions");
//        List<String> testUserList = new ArrayList<String>();
//        testUserList.add("NewUser1");
//        testUserList.add("NewUser2");
//        assertArrayEquals(testUserList, userList);
//        assertThrows(NoUserPermissionException);
//    }


    /* Test 28: Request to server to Create New Users (Success)
     * Description: New method to create new users to the system. This will take a unique username, user permissions,
     *              a password string and a valid sessionToken to create a new user.
     * Expected Output: Server will return "Fail: Calling Username Deleted" and an CallingUsernameDeletedException
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
     * Expected Output: Server will return "Fail: Calling Username Deleted" and an CallingUsernameDeletedException
     *                  will be thrown
     * //TODO: NEED TO IMPLEMENT SOME WAY TO CHANGE THE "CALLING USERNAME" IN THIS METHOD RATHER THAN JUST
     *    "USERNAME TO BE CREATED" SO THAT THIS CAN BE ADEQUATELY TESTED
     */
//    public void createUserRequestCallingUsernameDeleted() {
//      assertThrows(CallingUsernameDeletedException.class, () -> {
//        String serverResponse = userControl.createUserRequest("NewUser1", {0,0,0,0}, "Pass1", "sessionToken"));
//      }
//      // Check for correct message received
//      assertEquals("Fail: Calling Username Deleted", serverResponse);
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
//      assertEquals("Fail: Insufficient User Permissions", serverResponse);
//    }


    /* Test 31: Request to server to Create New Users (Exception Handling)
     * Description: New method to create new users to the system. This will take a unique username, user permissions,
     *              a password string and a valid sessionToken to create a new user. This will test if the username
     *              provided is not unique.
     * Expected Output: Server will return "Fail: Username Already Taken" and an InsufficientPermissionsException
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
//      assertEquals("Fail: Username Already Taken", serverResponse);
//    }

}