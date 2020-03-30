package server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserAdminTest{
    /* Test 0: Declaring UserAdmin object
     * Description: UserAdmin object should be running in background on application start.
     * Expected Output: UserAdmin object is declared
     */
    UserAdmin userAdmin;

    /* Test 1: Constructing a UserAdmin object
     * Description: UserAdmin Object should be able to be created on logged in user request from control panel
     * Expected Output: UserAdmin object is instantiated from UserAdmin class
     */
    @BeforeEach
    @Test
    public void setUpUserAdmin() {
        String userName = "root";
        userAdmin = new UserAdmin(userName);
    }


    /* Test 2: Validate SessionToken
     * Description: Check that User is still logged in and can do actions
     * Expected Output: A boolean where True is given if session is active and false if session is inactive
     * TODO: outComes is a array consist of 0/1, outPut is the return of the check for sessionToken
     */
//    @Test
//    public void verifySession(String sessionToken) {
//      assertTrue(outComes.contains(outPut));
//    }

    /* Test 2: Get Own Users Permissions
     * Description: Check that any user can see their own user permissions (int[4])
     * Order - Create Billboards, Edit All Billboards, Schedule Billboards, Edit Users
     * Expected Output: Given the user's own username, the method should return associated permissions
     * TODO: Implement Fake db through HashMap
     */
//    @Test
//    public void getOwnUserPermissions() {
//      // sessionToken should be sent from ControlPanel
//      int[] rootUserPermissions = UserAdmin.getUserPermissions("root", sessionToken);
//      int[] rootUserPermissionsExpected = {1,1,1,1};
//      assertEquals(rootUserPermissionsExpected, rootUserPermissions);
//      // TODO: Could extend this to check all combinations...
//    }

    /* Test 3: Get Other User's Permissions
     * Description: Check that only users with "Edit Permissions" can see any user's permissions
     * Expected Output: Given the requested username, the method should return associated permissions.
     * TODO: Implement Fake db through HashMap
     */
//    @Test
//    public void getOtherUserPermissions() {
//      // Here "root" user wants to check permission's of the basic user "Joe"
//      int[] basicUserPermissionsExpected = {0,0,0,0};
//      int[] basicUserPermissions = userAdmin.getUserPermissions("Joe", sessionToken);
//      assertTrue(basicUserPermissionsExpected, basicUserPermissions);
//      //TODO: Could extend this to check all combinations...
//    }

    /* Test 4: Get Other User's Permissions (exception handling)
     * Description: Get other User's Permissions from db - throw exception due to insufficient calling permissions
     * Require "EditUsers" permission which is the 4th element in UserPermissions object
     * e.g. [1,1,1,0] can't call the method.
     * Expected Output: The permissions of requested user cannot be retrieved, throw InsufficientPermissionsException
     * TODO: Implement Fake db through HashMap to verify
     */
//    @Test
//    public void getOtherUserPermissionsException() {
//      basicUserAdmin = new UserAdmin("Joe"); // temporarily change calling username
//      assertThrows(InsufficientPermissionsException.class, () -> {
//          // Joe tries to get root user's permission has but he does not have req. "EditUsers" permission
//          basicUserAdmin.getPermissions("root", sessionToken);
//       });
//    }

    /* Test 5: List Users (success)
     * Description: List all of the users in the database if calling username has "Edit Users" permissions (success)
     * Note: The calling username is retrieved as a private field from this UserAdmin Class
     * Expected Output: All of the users in the database are able to be listed.
     * TODO: Implement Fake db through HashMap to verify
     */
//    @Test
//    public void listUsers() {
//      userAdmin.listUsers(sessionToken);
//    }

    /* Test 6: List Users (exception handling)
     * Description: List all of the users in the database - throw exception due to non-existent calling username
     * (e.g. if someone else deleted you whilst logged in).
     * Expected Output: None of the users in the database are able to be listed, throw UsernameNotFoundException
     * TODO: Implement Fake db through HashMap to verify
     */
//    @Test
//    public void listUsers() {
//      unknownUserAdmin = new UserAdmin("non-existent"); // temporarily change calling username to something unknown
//      assertThrows(UsernameNotFoundException.class, () -> {
//          unknownUserAdmin.listUsers(sessionToken);
//      });
//    }

    /* Test 7: List Users (exception handling)
     * Description: List all of the users in the database - throw exception due to insufficient permissions
     * Require "EditUsers" permission which is the 4th element in UserPermissions object
     * e.g. [1,1,1,0] can't call the method.
     * Expected Output: None of the users in the database are able to be listed, throw InsufficientPermissionsException
     * TODO: Implement Fake db through HashMap to verify
     */
//    @Test
//    public void listUsers() {
//      basicUserAdmin = new UserAdmin("Joe");
//      assertThrows(InsufficientPermissionsException.class, () -> {
//          // Joe tries to list users but does not have necessary permissions to call method
//          basicUserAdmin.listUsers(sessionToken);
//      });
//    }

    /* Test 8: Set User Permissions (success)
     * Description: Check that the calling user has "EditUsers" permission, then find corresponding username in db
     * (if it exists) and then modify to the specified permissions and return acknowledgement (boolean) to Control Panel.
     * Expected Output: Username updated in the DB with the specified permissions and return success (boolean).
     * TODO: Implement Fake db through HashMap
     */
//    @Test
//    public void setUserPermissions() {
//      int[] requestedPermissions = {1,1,0,0};
//      bool success = userAdmin.setUserPermissions("Jenny", requestedPermissions, sessionToken);
//      assertEquals(requestedPermissions, userAdmin.getUserPermissions("Jenny"));
//      assertTrue(success);
//    }

    /* Test 8: Set User Permissions (exception handling)
     * Description: Attempt to set user's permissions but throw exception due to non-existent calling username
     * (e.g. if someone else deleted you whilst logged in).
     * Expected Output: The requested permissions are not set in the DB, throw UsernameNotFoundException
     * TODO: Implement Fake db through HashMap to verify
     */
//    @Test
//    public void setUserPermissionsDeletedCallingUsername() {
//      unknownUserAdmin = new UserAdmin("non-existent"); // temporarily change calling username to something unknown
//      int[] requestedPermissions = {1,1,1,0};
//      assertThrows(UsernameNotFoundException.class, () -> {
//          unknownUserAdmin.setUserPermissions("Jenny", requestedPermissions, sessionToken);
//      });
//      assertTrue(success!==true);
//    }

    /* Test 9: Set User Permissions (exception handling)
     * Description: Attempt to set user permissions however calling user does not "EditUser" Permission
     * Expected Output: The username is not updated in the database with the specified permissions, throw
     * InsufficientPermissionsException
     * TODO: Implement Fake db through HashMap
     */
//    @Test
//    public void setUserPermissionsInsufficientPermissions() {
//      basicUserAdmin = new UserAdmin("Joe");
//      assertThrows(InsufficientPermissionsException.class, () -> {
//          // Joe tries to list users but does not have necessary permissions to call method
//          int[] requestedPermissions = {1,1,1,0};
//          bool success = basicUserAdmin.setUserPermissions("Jenny", requestedPermissions, sessionToken);
//      });
//      assertTrue(success!==true);
//    }

    /* Test 10: Set User Permissions (exception handling)
     * Description: Attempt to set user's permissions but throw exception due to non-existent requested username in db
     * (e.g. if someone else deleted the other user whilst logged in).
     * Expected Output: The requested permissions are not set in the DB, throw UsernameNotFoundException
     * TODO: Implement Fake db through HashMap to verify
     */
//    @Test
//    public void setUserPermissionsNoUsernameInDb() {
//      assertThrows(UsernameNotFoundException.class, () -> {
//          int[] requestedPermissions = {1,1,1,0};
//          unknownUserAdmin.setUserPermissions("unknownUser", requestedPermissions, sessionToken);
//      });
//      assertTrue(success!==true);
//    }

    /* Test 11: Set Own Password (success)
     * Description: Find corresponding username in db (if it exists) and then modify to the hashed password and
     * return acknowledgement (boolean) to Control Panel.
     * Expected Output: Username updated in the DB with the specified hashed password and return success (boolean).
     * TODO: Implement Fake db through HashMap
     */
//    @Test
//    public void setUserPassword() {
//      basicUserAdmin = new UserAdmin("Joe"); // temporarily change calling username to a basic user
//      String hashedPassword = "test"; // this is received from ControlPanel
//      bool success = basicUserAdmin.setUserPassword("Joe", hashedPassword, sessionToken);
//      assertTrue(success);
//    }

    /* Test 12: Set Own Password (exception handling)
     * Description: Set own user password in the database - throw exception due to non-existent calling username
     * (e.g. if someone else deleted you whilst logged in).
     * Expected Output: Password not changed in the database, throw UsernameNotFoundException
     * TODO: Implement Fake db through HashMap to verify
     */
//    @Test
//    public void setUserPasswordDeletedCallingUsername() {
//      unknownUserAdmin = new UserAdmin("non-existent"); // temporarily change calling username to something unknown
//      String hashedPassword = "test"; // this is received from ControlPanel
//      assertThrows(UsernameNotFoundException.class, () -> {
//          unknownUserAdmin.setUserPassword("non-existent", hashedPassword, sessionToken);
//      });
//    }

    /* Test 13: Set Other User Password (success)
     * Description: Check that the calling user has "EditUsers" permission, then find corresponding username in db
     * (if it exists) and then modify to the hashed password and return acknowledgement (boolean) to Control Panel.
     * Expected Output: Username updated in the DB with the specified hashed password and return success (boolean).
     * TODO: Implement Fake db through HashMap
     */
//    @Test
//    public void setUserPassword() {
//      String hashedPassword = "test"; // this is received from ControlPanel
//      bool success = userAdmin.setUserPassword("Joe", hashedPassword, sessionToken);
//      assertTrue(success);
//    }

    /* Test 14: Set Other User Password (exception handling)
     * Description: Check that the calling user still exists in the DB before setting user password.
     * Expected Output: Hashed password not updated in the DB, throw UsernameNotFoundException
     * TODO: Implement Fake db through HashMap
     */
//    @Test
//    public void setUserPasswordDeletedCallingUsername() {
//      unknownUserAdmin = new UserAdmin("non-existent"); // temporarily change calling username to something unknown
//      String hashedPassword = "test"; // this is received from ControlPanel
//      assertThrows(UsernameNotFoundException.class, () -> {
//          unknownUserAdmin.setUserPassword("Jenny", hashedPassword, sessionToken);
//      });
//      assertTrue(success!==true);
//    }

    /* Test 15: Set Other User Password (exception handling)
     * Description: Check that if the calling user does not have "EditUsers" permission that they are unable to
     * modify password of other users.
     * Expected Output: Hashed password not updated in the DB, throw InsufficientPermissionsException
     * TODO: Implement Fake db through HashMap
     */
//    @Test
//    public void setUserPasswordInsufficientPermissions() {
//      basicUserAdmin = new UserAdmin("Joe"); // temporarily change calling username
//      String hashedPassword = "test"; // this is received from ControlPanel
//      assertThrows(InsufficientPermissionsException.class, () -> {
//          bool success = basicUserAdmin.setUserPassword("Jenny", hashedPassword, sessionToken);
//      });
//      assertTrue(success!==true);
//    }

    /* Test 16: Set Other User Password (exception handling)
     * Description: Check that if the username associated with the hashed password does not exist in database then
     * the password should not be updated and an exception should be thrown.
     * Expected Output: Hashed password not updated in the DB, throw InsufficientPermissionsException
     * TODO: Implement Fake db through HashMap
     */
//    @Test
//    public void setUserPasswordNoUsernameInDb() {
//      String hashedPassword = "test"; // this is received from ControlPanel
//      assertThrows(UsernameNotFoundException.class, () -> {
//          unknownUserAdmin.setUserPassword("unknown", hashedPassword, sessionToken);
//      });
//      assertTrue(success!==true);
//    }

//TODO: I GOT UP TO HERE! NEED TO FIX FROM HERE ONWARDS
    /* Test 17: Delete User (success)
     * Description:
     * Check that the calling user has "EditUsers" permission, then find corresponding username in db
     * (if it exists) and then modify to the specified permissions and return acknowledgement (boolean) to Control Panel.
     * Expected Output: Username updated in the DB with the specified permissions and return success (boolean).
     * TODO: Implement Fake db through HashMap
     */
//    @Test
//    public void setUserPermissions() {
//      int[] requestedPermissions = {1,1,0,0};
//      bool success = userAdmin.setUserPermissions("Jenny", requestedPermissions, sessionToken);
//      assertAll("Check for Delete Users",
//        ()-> assertEquals(requestedPermissions, userAdmin.getUserPermissions("Jenny")),
//        ()-> assertTrue(success)
//      )
//    }

    /* Test 18: Delete User (exception handling)
     * Description: Check you exist
     * Check that the calling user has "EditUsers" permission, then find corresponding username in db
     * (if it exists) and then modify to the specified permissions and return acknowledgement (boolean) to Control Panel.
     * Expected Output: Username updated in the DB with the specified permissions and return success (boolean).
     * TODO: Implement Fake db through HashMap
     */
//    @Test
//    public void setUserPermissions() {
//      int[] requestedPermissions = {1,1,0,0};
//      bool success = userAdmin.setUserPermissions("Jenny", requestedPermissions, sessionToken);
//      assertEquals(requestedPermissions, userAdmin.getUserPermissions("Jenny"));
//      assertTrue(success);
//    }

    /* Test 19: Delete User (exception handling)
     * Description: Check you have permission
     * Check that the calling user has "EditUsers" permission, then find corresponding username in db
     * (if it exists) and then modify to the specified permissions and return acknowledgement (boolean) to Control Panel.
     * Expected Output: Username updated in the DB with the specified permissions and return success (boolean).
     * TODO: Implement Fake db through HashMap
     */
//    @Test
//    public void setUserPermissions() {
//      int[] requestedPermissions = {1,1,0,0};
//      bool success = userAdmin.setUserPermissions("Jenny", requestedPermissions, sessionToken);
//      assertEquals(requestedPermissions, userAdmin.getUserPermissions("Jenny"));
//      assertTrue(success);
//    }

    /* Test 20: Delete User (exception handling)
     * Description: Check they exist
     * Check that the calling user has "EditUsers" permission, then find corresponding username in db
     * (if it exists) and then modify to the specified permissions and return acknowledgement (boolean) to Control Panel.
     * Expected Output: Username updated in the DB with the specified permissions and return success (boolean).
     * TODO: Implement Fake db through HashMap
     */
//    @Test
//    public void setUserPermissions() {
//      int[] requestedPermissions = {1,1,0,0};
//      bool success = userAdmin.setUserPermissions("Jenny", requestedPermissions, sessionToken);
//      assertEquals(requestedPermissions, userAdmin.getUserPermissions("Jenny"));
//      assertTrue(success);
//    }

    /* Test 21: Create User (success)
     * Description:
     * Check that the calling user has "EditUsers" permission, then find corresponding username in db
     * (if it exists) and then modify to the specified permissions and return acknowledgement (boolean) to Control Panel.
     * Expected Output: Username updated in the DB with the specified permissions and return success (boolean).
     * TODO: Implement Fake db through HashMap
     */
//    @Test
//    public void setUserPermissions() {
//      int[] requestedPermissions = {1,1,0,0};
//      bool success = userAdmin.setUserPermissions("Jenny", requestedPermissions, sessionToken);
//      assertEquals(requestedPermissions, userAdmin.getUserPermissions("Jenny"));
//      assertTrue(success);
//    }

    /* Test 22: Create User (exception handling)
     * Description: Check you exist
     * Check that the calling user has "EditUsers" permission, then find corresponding username in db
     * (if it exists) and then modify to the specified permissions and return acknowledgement (boolean) to Control Panel.
     * Expected Output: Username updated in the DB with the specified permissions and return success (boolean).
     * TODO: Implement Fake db through HashMap
     */
//    @Test
//    public void setUserPermissions() {
//      int[] requestedPermissions = {1,1,0,0};
//      bool success = userAdmin.setUserPermissions("Jenny", requestedPermissions, sessionToken);
//      assertEquals(requestedPermissions, userAdmin.getUserPermissions("Jenny"));
//      assertTrue(success);
//    }

    /* Test 23: Create User (exception handling)
     * Description: Check you have permission
     * Check that the calling user has "EditUsers" permission, then find corresponding username in db
     * (if it exists) and then modify to the specified permissions and return acknowledgement (boolean) to Control Panel.
     * Expected Output: Username updated in the DB with the specified permissions and return success (boolean).
     * TODO: Implement Fake db through HashMap
     */
//    @Test
//    public void setUserPermissions() {
//      int[] requestedPermissions = {1,1,0,0};
//      bool success = userAdmin.setUserPermissions("Jenny", requestedPermissions, sessionToken);
//      assertEquals(requestedPermissions, userAdmin.getUserPermissions("Jenny"));
//      assertTrue(success);
//    }

    /* Test 24: Create User (exception handling)
     * Description: Check they exist
     * Check that the calling user has "EditUsers" permission, then find corresponding username in db
     * (if it exists) and then modify to the specified permissions and return acknowledgement (boolean) to Control Panel.
     * Expected Output: Username updated in the DB with the specified permissions and return success (boolean).
     * TODO: Implement Fake db through HashMap
     */
//    @Test
//    public void setUserPermissions() {
//      int[] requestedPermissions = {1,1,0,0};
//      bool success = userAdmin.setUserPermissions("Jenny", requestedPermissions, sessionToken);
//      assertEquals(requestedPermissions, userAdmin.getUserPermissions("Jenny"));
//      assertTrue(success);
//    }

    /* Test 25: Log out success (exception handling)
     * Description: Check they exist
     * Check that the calling user has "EditUsers" permission, then find corresponding username in db
     * (if it exists) and then modify to the specified permissions and return acknowledgement (boolean) to Control Panel.
     * Expected Output: Username updated in the DB with the specified permissions and return success (boolean).
     * TODO: Implement Fake db through HashMap
     */
//    @Test
//    public void setUserPermissions() {
//      int[] requestedPermissions = {1,1,0,0};
//      bool success = userAdmin.setUserPermissions("Jenny", requestedPermissions, sessionToken);
//      assertEquals(requestedPermissions, userAdmin.getUserPermissions("Jenny"));
//      assertTrue(success);
//    }

}