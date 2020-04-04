package controlPanel;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

    /* Test 2: Log out (success)
     * Description: Check that the user is able to log out and the session token is destroyed.
     * Expected Output: Successful log out of the user and the session token is no longer available.
     */
//    @Test(expected = Test.None.class /* no exception expected */)
//    public void logOut() {
//      bool success = userAdmin.logout()
//      assertTrue(success);
//    }

    /* Test 3: Request to server to Create New Users (Success)
     * Description: New method to create new users to the system. This will take a unique username, user permissions,
     *              a password string and a valid sessionToken to create a new user.
     * Expected Output: A success feedback from the server
     */
//    public void createUserRequestTest(){
//        userControl.createUserRequest("NewUser1", {0,0,0,0}, "Pass1", sessionToken);
//        assertTrue(serverResponse,"User Created!");
//    }


    /* Test 4: Request to server to Create New Users (Fail)
     * Description: New method to create new users to the system. This will take a unique username, user permissions,
     *              a password string and a valid sessionToken to create a new user. This will test if the username
     *              provided is not unique.
     * Expected Output: An error from server
     */
//    public void createUserRequestTest(){
//        userControl.createUserRequest("NewUser1", {0,0,0,0}, "Pass1", sessionToken);
//        userControl.createUserRequest("NewUser1", {0,0,0,0}, "Pass1", sessionToken);
//        assertTrue(serverResponse,"Username Already Taken");
//    }

    /* Test 5: Request to server to Create New Users (Fail)
     * Description: New method to create new users to the system. This will take a unique username, user permissions,
     *              a password string and a valid sessionToken to create a new user. This test checks for sufficient
     * permissions on the calling user.
     * Expected Output: An error from server
     */
//    public void createUserRequestTest(){
//        userControl.createUserRequest("sessionToken", "NewUser1", "Pass1");
//        assertTrue(serverResponse,"Insuffice User Permission");
//    }

    /* Test 6: Request to server to list Current Users
     * Description: Method to request to server to send a list of active users in the database. Requires a valid
     *              sessionToken.
     * Expected Output: A list of users
     */
//    public void listUsersTest(){
//        userControl.createUserRequest("sessionToken", "NewUser1", "Pass1");
//        userControl.createUserRequest("sessionToken", "NewUser2", "Pass2");
//        List<String> testUserList = new ArrayList<String>();
//        testUserList.add("NewUser1");
//        testUserList.add("NewUser2");
//        UserList userList = userControl.listUsers("sessionToken");
//        assertArrayEquals(testUserList ,userList);
//    }


    /* Test 7: Request to server to change password (Success)
     * Description: Method to request to server to change a specific users password. Assumes a valid sessionToken is
     *              running, and that user has permission. This test tests for themself.
     * Expected Output: Success response from the server
     */
//    public void setUserPasswordRequestTest() {
//        userControl.createUserRequest("sessionToken", "NewUser1", "Pass1");
//        userControl.createUserRequest("sessionToken", "NewUser2", "Pass2");
//        userControl.setUserPasswordRequest("sessionToken", "CAB302", "NewPassword");
//        assertTrue(serverResponse, "Password Change Successfully");
//    }


    /* Test 8: Request to server to change password (Success)
     * Description: Method to request to server to change a specific users password. Assumes a valid sessionToken is
     *              running, and that user has permission. This test tests for otherUsers.
     * Expected Output: Success response from the server
     */
//    public void setUserPasswordRequestTest() {
//        userControl.createUserRequest("sessionToken", "NewUser1", "Pass1");
//        userControl.createUserRequest("sessionToken", "NewUser2", "Pass2");
//        userControl.setUserPasswordRequest("sessionToken", "NewUser2", "NewPassword");
//        assertTrue(serverResponse, "Password Change Successfully");
//    }


    /* Test 9: Request to server to change password (Fail)
     * Description: Method to request to server to change a specific users password. Assumes a valid sessionToken is
     *              running, and that user has permission. This test tests for insuffice permission
     * Expected Output: Success response from the server
     */
//    public void setUserPasswordRequestTest() {
//        userControl.createUserRequest("sessionToken", "NewUser1", "Pass1");
//        userControl.createUserRequest("sessionToken", "NewUser2", "Pass2");
//        userControl.setUserPasswordRequest("sessionToken", "NewUser2", "NewPassword");
//        if (serverResponse == "No Permission"){
//          throw new UserValueException();
//        }
//   }


    /* Test 10: Request to server to change password (Fail)
     * Description: Method to request to server to change a specific users password. Assumes a valid sessionToken is
     *              running, and that user has permission. This test tests for nonexistent Users.
     * Expected Output: Success response from the server
     */
//    public void setUserPasswordRequestTest() {
//        userControl.setUserPasswordRequest("sessionToken", "NewUser3", "NewPassword");
//        if (serverResponse == "No such User"){
//          throw new NoUserValueException();
//        }
//    }


    /* Test 11: Request to server to get User Permission from a user (Success)
     * Description: Method to request userpermissions.
     * Expected Output: Return of UserPermission in an array
     */
//    public void getUserPermissionTest(){
//      userControl.createUserRequest("sessionToken", "NewUser1", "Pass1");
//      int[] basicUserPermissionsExpected = {0,0,0,0};
//      int[] basicUserPermissions = userControl.getUserPermission("sessionToken", "User1");;
//      assertTrue(basicUserPermissionsExpected, basicUserPermissions);
//    }


    /* Test 12: Request to server to get User Permission from a user (Fail)
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


    /* Test 13: Request to server to set user permissions of a user (success)
     * Description: Method to set a user permission for another person
     * Expected Output: Success Message
     */
//    public void setUserPermissionTest() {
//      userControl.createUserRequest("sessionToken", "NewUser1", "Pass1");
//      userControl.setUserPermission("sessionToken", "NewUser1", boolean[] {1,1,1,1})
//      assertTrue(serverResponse, "Permission Changed");
//    }

    /* Test 14: Request to server to set user permissions of a user (Fail)
     * Description: Method to set a user permission for another person. Such Person does not exists
     * Expected Output: Exception error
     */
//    public void setUserPermissionTest() {
//      userControl.setUserPermission("sessionToken", "NewUser1", boolean[] {1,1,1,1})
//      if (serverResponse == "No Such User"){
//          throw new NoUserValueException();
//      }
//    }


    /* Test 15: Request to server to set user permissions of a user (Fail)
     * Description: Method to set a user permission for another person. No permission to do so.
     * Expected Output: Exception error
     */
//    public void setUserPermissionTest() {
//      userControl.createUserRequest("sessionToken", "NewUser1", "Pass1");
//      userControl.setUserPermission("sessionToken", "NewUser1", boolean[] {1,1,1,1})
//      if (serverResponse == "No Permission"){
//        throw new UserValueException();
//      }
//    }


}