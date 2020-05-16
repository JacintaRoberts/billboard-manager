package controlPanel;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.Server;
import server.Server.ServerAcknowledge;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static server.Server.ServerAcknowledge.*;
import static server.Server.generateToken;

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
    @BeforeEach
    @Test
    public void setUpUserControl() {
      userControl = new UserControl();
    }

    /* Test 2: Log out Request (Success)
     * Description: User's request to log out is sent to the server and an acknowledgement is received
     * Expected Output: Successful log out of the user, acknowledgement received and the session token is expired.
     */
    @Test
    public void logOut() throws IOException, ClassNotFoundException {
      ServerAcknowledge serverResponse = userControl.logoutRequest("sessionToken");
      assertEquals(Success, serverResponse);
    }


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
     * Expected Output: List of Users unable to be retrieved from DB and returns "Fail: Invalid Session Token"
     */
//    @Test
//    public void listUsersTestUsernameDeleted(){
//        userControl.createUserRequest("sessionToken", "NewUser1", "Pass1", {1,1,1,1});
//        userControl.createUserRequest("sessionToken", "NewUser2", "Pass2", {1,1,1,1});
//        String serverResponse = userControl.listUsers("sessionToken");
//        assertArrayEquals(serverResponse, "Fail: Invalid Session Token");
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
//        assertEquals(serverResponse, "Pass: Own Password Updated");
//    }


    /* Test 7: Request to server to change password (Fail)
     * Description: Method to request to server to change a specific users password. Assumes a valid sessionToken is
     *              running, and that user has permission. This test tests for themself. This test will test when
     *              the method is called but user is deleted halfway during a session. This will raise an exception
     *              saying that "Fail: Invalid Session Token".  Assume current user logged in is called "CAB302"
     * Expected Output: Exception raised with message of "Fail: Invalid Session Token"
     */
//    @Test
//    public void setOwnUserPasswordRequest() {
//        userControl.createUserRequest("sessionToken", "NewUser1", "Pass1");
//        userControl.createUserRequest("sessionToken", "NewUser2", "Pass2");
//        userControl2 = new UserControl();
//        userControl2.deleteUser("sessionToken", "CAB302");
//        String serverResponse = userControl.setUserPasswordRequest("sessionToken", "CAB302", "NewPassword");
//        assertEquals(serverResponse, "Fail: Invalid Session Token");
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
//        assertEquals(serverResponse, "Pass: Password Change Successfully");
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
//        assertEquals(serverResponse, Fail: Invalid Session Token");
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
//        assertEqual(serverResponse, "Fail: User Does Not Exist");
//        assertThrows(NoUserValueException, () -> server resp "Fail: Username Does Not Exist");
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
     * Expected Output: Exception Fail with user deleted halfway "Fail: Invalid Session Token"
     */
//    @Test
//    public void setOwnUserPermissionTestDeleteMidway() {
//      fullPermissionAdmin = new userControl("NewUser0");
//      fullPermissionAdmin.setUserPermissions("sessionToken", "NewUser0", {1,1,1,1});
//      String serverResponse = userControl.deleteUserRequest("sessionToken", "CAB302");
//      Exception exception = assertThrows(CallingUsernameDeletedException.class,
//                                         () -> {userControl.setUserPermission("sessionToken", "CAB302", boolean[] {0,0,0,1});}
//                                        );
//      assertEquals(exception.getMessage(),"Fail: Invalid Session Token");
//    }


    /* Test 15: Request to server to set user permissions of a user (Exception Handling)
     * Description: Method to set a user permission for another person. User does not have enough permission
     * Expected Output: Exception Fail with user not having suffice permission "Fail: Insufficient User Permission"
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


    /* Test 16: Request to server to set user permissions of a user (Success)
     * Description: Method to set a user permission for another person. Assume SessionToken is valid
     * Expected Output:  Pass with Response: "Pass: Other User Permissions Updated"
     */
//    @Test
//    public void setOtherUserPermissionTestPass() {
//      fullPermissionAdmin = new userControl("NewUser0");
//      userControl.setUserPermissions("sessionToken", "NewUser0", {1,1,1,1});
//      userControl.createUserRequest("sessionToken", "NewUser1", "Pass1");
//      userControl.setUserPermissions("sessionToken","NewUser1", {0,0,0,0});
//      String serverResponse = fullPermissionAdmin.setUserPermission("sessionToken", "NewUser1", boolean[] {1,1,1,1});
//      assertEquals(serverResponse, "Pass: Other User Permission Updated");
//    }


    /* Test 17: Request to server to set user permissions of a user (Exception Handling)
     * Description: Method to set a user permission for another person. User being delted mid session
     * Expected Output: Exception Fail with user deleted halfway "Fail: Invalid Session Token"
     */
//    @Test
//    public void setOwnUserPermissionTestDeleteMidway() {
//      fullPermissionAdmin = new userControl("NewUser0");
//      fullPermissionAdmin.setUserPermissions("sessionToken", "NewUser0", {1,1,1,1});
//      testAdmin = new userControl("NewUser1");
//      testAdmin.setUserPermissions("sessionToken", "NewUser1", {1,1,1,1});
//      userControl.createUserRequest("sessionToken", "NewUser2", "Pass2");
//      String serverResponse = fullPermissionAdmin.deleteUserRequest("sessionToken", "NewUser1");
//      Exception exception = assertThrows(CallingUsernameDeletedException.class,
//                                         () -> {testAdmin.setUserPermission("sessionToken", "CAB302", boolean[] {0,0,0,1});}
//                                        );
//      assertEquals(exception.getMessage(),"Fail: Invalid Session Token");
//    }


    /* Test 18: Request to server to set other user permissions of a user (Exception Handling)
     * Description: Method to set a user permission for another person. Such Person does not exists. The database does
     *              not have newUser1
     * Expected Output: Exception Fail with response: "Fail: User Does Not Exist"
     */
//    @Test
//    public void setOtherUserPermissionTestUserNotExist() {
//      String serverResponse = userControl.setUserPermission("sessionToken", "NewUser1", boolean[] {1,1,1,1});
//      assertEquals(serverResponse,"Fail: User Does Not Exist");
//      assertThrows(NoUserPermissionException);
//    }


    /* Test 19: Request to server to get User Permission from a user (Success)
     * Description: Method to request userpermissions.
     * Expected Output: Return of UserPermission in an array
     */
//    @Test
//    public void getUserPermissionTest(){
//      userControl.createUserRequest("sessionToken", "NewUser1", "Pass1");
//      int[] basicUserPermissionsExpected = {0,0,0,0};
//      int[] basicUserPermissions = userControl.getUserPermission("sessionToken", "NewUser1");;
//      assertEquals(basicUserPermissionsExpected, basicUserPermissions.getUserPermissionArray());
//      assertEquals("Pass: User Permission Returned", basicUserPermissions.getServerResponse());
//    }


    /* Test 20: Request to server to get User Permission from a user (Exception Handling)
     * Description: Get other User's Permissions from db - throw exception due to User being deleted halfway
     * Expected Output: The permissions of requested user cannot be retrieved, throw UsernameDeleted
     * TODO: Implement Fake db through HashMap to verify
     */
//    @Test
//    public void getOtherUserPermissionsExceptionUsernameDeleted() {
//      fullPermissionAdmin = new userControl("NewUser0");
//      fullPermissionAdmin.setUserPermissions("sessionToken", "NewUser0", {1,1,1,1});
//      testAdmin = new userControl("NewUser1");
//      testAdmin.setUserPermissions("sessionToken", "NewUser1", {1,1,1,1});
//      userControl.createUserRequest("sessionToken", "NewUser2", "Pass2");
//      String serverResponse = fullPermissionAdmin.deleteUserRequest("sessionToken", "NewUser1");
//      Exception exception = assertThrows(CallingUsernameDeletedException.class,
//                                         () -> {testAdmin.getUserPermission("sessionToken", "CAB302");}
//                                        );
//      assertEquals(exception.getMessage(),"Fail: Invalid Session Token");
//    }


    /* Test 21: Request to server to get User Permission from a user (Exception Handling)
     * Description: Get other User's Permissions from db - throw exception due to insufficient calling permissions
     * Require "EditUsers" permission which is the 4th element in UserPermissions object
     * e.g. [1,1,1,0] can't call the method.
     * Expected Output: The permissions of requested user cannot be retrieved, throw InsufficientPermissionsException
     * TODO: Implement Fake db through HashMap to verify
     */
//    @Test
//    public void getOtherUserPermissionsExceptionUserNotExist() {
//      noPermissionAdmin = new userControl("NewUser0");
//      userControl.setUserPermissions("sessionToken","NewUser0", {0,0,0,0});
//      userControl.createUserRequest("sessionToken", "NewUser1", "Pass1");
//      userControl.setUserPermissions("sessionToken","NewUser1", {1,1,0,0});
//      UserPermission userPermission = noPermissionAdmin.getUserPermission("sessionToken", "NewUser1");
//      assertEquals(userPermission.getserverResponse(), "Fail: Insufficient User Permissions");
//      assertThorws(InsufficientPermissionsException.class);
//    }


    /* Test 22: Request to server to set user permissions of a user (Exception Handling)
     * Description: Method to set a user permission for another person. User called does not exist
     * Expected Output: Exception Fail with username not existing "Fail: Username Does Not Exist"
     */
//    @Test
//    public void setOtherUserPermissionTestNoPermission() {
//      UserPermission userPermission = userControl.setUserPermission("sessionToken", "NewUser1");
//      assertEquals(userPermission.getserverResponse(), "Fail: Username Does Not Exist");
//      assertThorws(UsernameNotFoundException.class);
//    }


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

 
    /* Test 24: Request to server to Delete a user (Exception Handling)
     * Description: Delete a user from from db - throw exception due to User being deleted halfway
     * Expected Output: User cannot be deleted as account deleted halfway, throw UsernameDeleted
     * TODO: Implement Fake db through HashMap to verify
     */
//    @Test
//    public void getOtherUserPermissionsExceptionUsernameDeleted() {
//      fullPermissionAdmin = new userControl("NewUser0");
//      fullPermissionAdmin.setUserPermissions("sessionToken", "NewUser0", {1,1,1,1});
//      testAdmin = new userControl("NewUser1");
//      testAdmin.setUserPermissions("sessionToken", "NewUser1", {1,1,1,1});
//      userControl.createUserRequest("sessionToken", "NewUser2", "Pass2");
//      String serverResponse = fullPermissionAdmin.deleteUserRequest("sessionToken", "NewUser1");
//      Exception exception = assertThrows(CallingUsernameDeletedException.class,
//                                         () -> {testAdmin.deleteUserRequest("sessionToken", "CAB302");}
//                                        );
//      assertEquals(exception.getMessage(),"Fail: Invalid Session Token");
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


    /* Test 26: Request to server to Delete Current Users (Fail)
     * Description: Method to request to server to delete a specified user in the database. Requires a valid
     *              sessionToken. Throw exception due to User does not exists
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


    /* Test 27: Request to server to Delete Current Users (Fail)
     * Description: Method to request to server to delete a specified user in the database. Requires a valid
     *              sessionToken. Throw exception due to User does not exists
     * Expected Output: Cannot delete user from DB and returns "Fail: User does not exist"
     */
//    @Test
//    public void deleteUserNoUserTest(){
//        String serverResponse = userControl.deleteUserRequest("sessionToken", "CAB302");
//        assertArrayEquals(serverResponse, "Fail: Cannot Delete Yourself");
//        asserthrows(CannotDeleteOwnUserException.class);
//    }


    /* Test 28: Request to server to Create New Users (Success)
     * Description: New method to create new users to the system. This will take a unique username, user permissions,
     *              a password string and a valid sessionToken to create a new user.
     * Expected Output: Server will return "Success: User Created"
     */
    //TODO: FOR SOME REASON THIS IS NOT PASSING? NO IDEA WHY IT CAN'T SEEM TO CREATE THE SESSION TOKEN WHEN WE CALL
    // DIRECTLY FROM USER CONTROL. IT DOESN'T MAKE SENSE, THE ISSUE IS VALIDATING A SESSION TOKEN FROM CP, WORKS FINE
    // IN THE ACTUAL PROGRAM JUST THIS UNIT TEST IDK...I ONLY ADDED THE GETTERS AND SETTERS FOR VALID SESSION TOKEN
    // TO TRY AND FIX THE ISSUE BUT I DON'T THINK ITS DONE ANYTHING?
    @Test
    public void createUserRequest() throws NoSuchAlgorithmException, IOException, ClassNotFoundException, SQLException {
        String callingUsername = "testUser";
        String testToken = generateToken(callingUsername);
        assertTrue(Server.validateToken(testToken));
        ServerAcknowledge serverResponse = userControl.createUserRequest(testToken, "NewUser1",
                "myPass", true, true, true, true);
        assertEquals(serverResponse, Success);
    }


    /* Test 29: Request to server to Create New Users (Exception Handling)
     * Description: New method to create new users to the system. This will take a unique username, user permissions,
     *              a password string and a valid sessionToken to create a new user. This test checks for sufficient
     *              permissions on the calling user.
     * Expected Output: Server will return "Fail: Invalid Session Token"
     *                  will be thrown
     * //TODO: NEED TO IMPLEMENT SOME WAY TO CHANGE THE "CALLING USERNAME" IN THIS METHOD RATHER THAN JUST
     *    "USERNAME TO BE CREATED" SO THAT THIS CAN BE ADEQUATELY TESTED
     */
//    public void createUserRequestCallingUsernameDeleted() {
//      assertThrows(CallingUsernameDeletedException.class, () -> {
//        String serverResponse = userControl.createUserRequest("NewUser1", {0,0,0,0}, "Pass1", "sessionToken"));
//      }
//      // Check for correct message received
//      //TODO: CHECK INSTANCES OF Fail: Invalid Session Token AND CHANGE TO INVALID SESSION TOKEN WHICH MAKES MORE SENSE!
//      assertEquals("Fail: Invalid Session Token", serverResponse);
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