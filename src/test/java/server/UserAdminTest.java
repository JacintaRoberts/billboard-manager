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

    /* Test 3: Check User Exists (Helper)
     * Description: Check that a user exists in the database - helper method
     * Expected Output: A boolean where true is returned if the user is found in the DB and false otherwise
     * // TODO: Implement Fake db through HashMap in the UserAdmin source code
     */
//    @Test
//    public void userExists() {
//      assertAll("Check for Existing User",
//        // Ensure that these users don't exist in the Fake DB.
//        ()-> assertFalse(userAdmin.userExists("non-existent-user")),
//        // Check for case sensitivity
//        ()-> assertFalse(userAdmin.userExists("Root")),
//        // Check for empty
//        ()-> assertFalse(userAdmin.userExists("")),
//        ()-> assertTrue(userAdmin.userExists("root"))
//      )
//    }

    /* Test 4: Get Other User's Permissions
     * Description: Check that only users with "Edit Permissions" can see any user's permissions
     * Order - Create Billboards, Edit All Billboards, Schedule Billboards, Edit Users
     * Expected Output: Given the requested username, the method should return associated permissions.
     */
//    @Test
//    public void getOtherUserPermissions() {
//      // Here "root" user wants to check permission's of the following "test" users
//      // sessionToken should be sent from ControlPanel
//      // TODO: Add these usernames with the corresponding permissions in the Fake DB in UserAdmin source code
//      assertAll("Check for All Possible User Permission Combinations",
//        ()-> assertEquals({0,0,0,0}, userAdmin.getUserPermissions("test0", sessionToken)),
//        ()-> assertEquals({1,0,0,0}, userAdmin.getUserPermissions("test1", sessionToken)),
//        ()-> assertEquals({0,1,0,0}, userAdmin.getUserPermissions("test2", sessionToken)),
//        ()-> assertEquals({0,0,1,0}, userAdmin.getUserPermissions("test3", sessionToken)),
//        ()-> assertEquals({0,0,0,1}, userAdmin.getUserPermissions("test4", sessionToken)),
//        ()-> assertEquals({1,1,0,0}, userAdmin.getUserPermissions("test5", sessionToken)),
//        ()-> assertEquals({1,0,1,0}, userAdmin.getUserPermissions("test6", sessionToken)),
//        ()-> assertEquals({1,0,0,1}, userAdmin.getUserPermissions("test7", sessionToken)),
//        ()-> assertEquals({0,1,1,0}, userAdmin.getUserPermissions("test8", sessionToken)),
//        ()-> assertEquals({0,1,0,1}, userAdmin.getUserPermissions("test9", sessionToken)),
//        ()-> assertEquals({0,0,1,1}, userAdmin.getUserPermissions("test10", sessionToken)),
//        ()-> assertEquals({1,1,1,0}, userAdmin.getUserPermissions("test11", sessionToken)),
//        ()-> assertEquals({1,1,0,1}, userAdmin.getUserPermissions("test12", sessionToken)),
//        ()-> assertEquals({1,0,1,1}, userAdmin.getUserPermissions("test13", sessionToken)),
//        ()-> assertEquals({0,1,1,1}, userAdmin.getUserPermissions("test14", sessionToken)),
//        ()-> assertEquals({1,1,1,1}, userAdmin.getUserPermissions("root", sessionToken))
//      )
//    }

    /* Test 5: Get Own Users Permissions
     * Description: Check that any user can see their own user permissions (int[4])
     * Order - Create Billboards, Edit All Billboards, Schedule Billboards, Edit Users
     * Expected Output: Given the user's own username, the method should return associated permissions
     */
//    @Test
//    public void getOwnUserPermissions() {
//      // sessionToken should be sent from ControlPanel
//      // Only testing a few select uses cases unlike above test
//      assertAll("Check for Retrieving Most Own User Permission Combinations",
//        ()-> {
//          testUserAdmin = new UserAdmin("test0");
//          assertEquals({0,0,0,0}, testUserAdmin.getUserPermissions("test0", sessionToken))
//        },
//        ()-> {
//          testUserAdmin = new UserAdmin("test1");
//          assertEquals({1,0,0,0}, testUserAdmin.getUserPermissions("test1", sessionToken))
//        },
//        ()-> {
//          testUserAdmin = new UserAdmin("test2");
//          assertEquals({0,1,0,0}, testUserAdmin.getUserPermissions("test2", sessionToken))
//        },
//        ()-> {
//          testUserAdmin = new UserAdmin("test3");
//          assertEquals({0,0,1,0}, testUserAdmin.getUserPermissions("test3", sessionToken))
//        },
//        ()-> {
//          testUserAdmin = new UserAdmin("test4");
//          assertEquals({0,0,0,1}, testUserAdmin.getUserPermissions("test4", sessionToken))
//        },
//        ()-> {
//          testUserAdmin = new UserAdmin("root");
//          assertEquals({1,1,1,1}, testUserAdmin.getUserPermissions("root", sessionToken))
//        }
//      )
//    }

    /* Test 6: Get Other User's Permissions (exception handling)
     * Description: Get other User's Permissions from db - throw exception due to insufficient calling permissions
     * Require "EditUsers" permission which is the 4th element in UserPermissions object
     * e.g. [1,1,1,0] can't call the method.
     * Expected Output: The permissions of requested user cannot be retrieved, throw InsufficientPermissionsException
     */
//    @Test
//    public void getOtherUserPermissionsException() {
//      basicUserAdmin = new UserAdmin("Joe"); // temporarily change calling username
//      assertThrows(InsufficientPermissionsException.class, () -> {
//          // Joe tries to get root user's permission has but he does not have req. "EditUsers" permission
//          basicUserAdmin.getPermissions("root", sessionToken);
//       });
//    }

    /* Test 7: List Users (success)
     * Description: List all of the users in the database if calling username has "Edit Users" permissions (success)
     * Note: The calling username is retrieved as a private field from this UserAdmin Class
     * Expected Output: All of the users in the database are able to be listed.
     */
//    @Test(expected = Test.None.class /* no exception expected */
//    public void listUsers() {
//      userAdmin.listUsers(sessionToken);
//    }

    /* Test 8: List Users (exception handling)
     * Description: List all of the users in the database - throw exception due to non-existent calling username
     * (e.g. if someone else deleted you whilst logged in).
     * Expected Output: None of the users in the database are able to be listed, throw UsernameNotFoundException
     */
//    @Test
//    public void listUsers() {
//      unknownUserAdmin = new UserAdmin("non-existent"); // temporarily change calling username to something unknown
//      assertThrows(UsernameNotFoundException.class, () -> {
//          unknownUserAdmin.listUsers(sessionToken);
//      });
//    }

    /* Test 9: List Users (exception handling)
     * Description: List all of the users in the database - throw exception due to insufficient permissions
     * Require "EditUsers" permission which is the 4th element in UserPermissions object
     * e.g. [1,1,1,0] can't call the method.
     * Expected Output: None of the users in the database are able to be listed, throw InsufficientPermissionsException
     */
//    @Test
//    public void listUsers() {
//      basicUserAdmin = new UserAdmin("Joe");
//      assertThrows(InsufficientPermissionsException.class, () -> {
//          // Joe tries to list users but does not have necessary permissions to call method
//          basicUserAdmin.listUsers(sessionToken);
//      });
//    }

    /* Test 10: Set User Permissions (success)
     * Description: Check that the calling user has "EditUsers" permission, then find corresponding username in db
     * (if it exists) and then modify to the specified permissions and return acknowledgement (boolean) to Control Panel.
     * Expected Output: Username updated in the DB with the specified permissions and return success (boolean).
     */
//    @Test(expected = Test.None.class /* no exception expected */)
//    public void setUserPermissions() {
//      int[] requestedPermissions = {1,1,0,0};
//      bool success = userAdmin.setUserPermissions("Jenny", requestedPermissions, sessionToken);
//      assertEquals(requestedPermissions, userAdmin.getUserPermissions("Jenny"));
//      assertTrue(success);
//    }

    /* Test 11: Set User Permissions (exception handling)
     * Description: Attempt to set user's permissions but throw exception due to non-existent calling username
     * (e.g. if someone else deleted you whilst logged in).
     * Expected Output: The requested permissions are not set in the DB, throw UsernameNotFoundException
     */
//    @Test
//    public void setUserPermissionsDeletedCallingUsername() {
//      unknownUserAdmin = new UserAdmin("non-existent"); // temporarily change calling username to something unknown
//      int[] requestedPermissions = {1,1,1,0};
//      assertThrows(UsernameNotFoundException.class, () -> {
//          unknownUserAdmin.setUserPermissions("Jenny", requestedPermissions, sessionToken);
//      });
//      assertFalse(success);
//    }

    /* Test 12: Set User Permissions (exception handling)
     * Description: Attempt to set user permissions however calling user does not "EditUser" Permission
     * Expected Output: The username is not updated in the database with the specified permissions, throw
     * InsufficientPermissionsException
     */
//    @Test
//    public void setUserPermissionsInsufficientPermissions() {
//      basicUserAdmin = new UserAdmin("Joe");
//      assertThrows(InsufficientPermissionsException.class, () -> {
//          // Joe tries to list users but does not have necessary permissions to call method
//          int[] requestedPermissions = {1,1,1,0};
//          bool success = basicUserAdmin.setUserPermissions("Jenny", requestedPermissions, sessionToken);
//      });
//      assertFalse(success);
//    }

    /* Test 13: Set User Permissions (exception handling)
     * Description: Attempt to set user's permissions but throw exception due to non-existent requested username in db
     * (e.g. if someone else deleted the other user whilst logged in).
     * Expected Output: The requested permissions are not set in the DB, throw UsernameNotFoundException
     */
//    @Test
//    public void setUserPermissionsNoUsernameInDb() {
//      assertThrows(UsernameNotFoundException.class, () -> {
//          int[] requestedPermissions = {1,1,1,0};
//          unknownUserAdmin.setUserPermissions("unknownUser", requestedPermissions, sessionToken);
//      });
//      assertFalse(success);
//    }

    /* Test 14: Set Own Password (success)
     * Description: Find corresponding username in db (if it exists) and then modify to the hashed password and
     * return acknowledgement (boolean) to Control Panel.
     * Expected Output: Username updated in the DB with the specified hashed password and return success (boolean).
     */
//    @Test
//    public void setUserPassword() {
//      basicUserAdmin = new UserAdmin("Joe"); // temporarily change calling username to a basic user
//      String hashedPassword = "test"; // this is received from ControlPanel
//      bool success = basicUserAdmin.setUserPassword("Joe", hashedPassword, sessionToken);
//      assertTrue(success);
//    }

    /* Test 15: Set Own Password (exception handling)
     * Description: Set own user password in the database - throw exception due to non-existent calling username
     * (e.g. if someone else deleted you whilst logged in).
     * Expected Output: Password not changed in the database, throw UsernameNotFoundException
     */
//    @Test
//    public void setUserPasswordDeletedCallingUsername() {
//      unknownUserAdmin = new UserAdmin("non-existent"); // temporarily change calling username to something unknown
//      String hashedPassword = "test"; // this is received from ControlPanel
//      assertThrows(UsernameNotFoundException.class, () -> {
//          unknownUserAdmin.setUserPassword("non-existent", hashedPassword, sessionToken);
//      });
//    }

    /* Test 16: Set Other User Password (success)
     * Description: Check that the calling user has "EditUsers" permission, then find corresponding username in db
     * (if it exists) and then modify to the hashed password and return acknowledgement (boolean) to Control Panel.
     * Expected Output: Username updated in the DB with the specified hashed password and return success (boolean).
     */
//    @Test(expected = Test.None.class /* no exception expected */)
//    public void setUserPassword() {
//      String hashedPassword = "test"; // this is received from ControlPanel
//      bool success = userAdmin.setUserPassword("Joe", hashedPassword, sessionToken);
//      assertTrue(success);
//    }

    /* Test 17: Set Other User Password (exception handling)
     * Description: Check that the calling user still exists in the DB before setting user password.
     * Expected Output: Hashed password not updated in the DB, throw UsernameNotFoundException
     */
//    @Test
//    public void setUserPasswordDeletedCallingUsername() {
//      unknownUserAdmin = new UserAdmin("non-existent"); // temporarily change calling username to something unknown
//      String hashedPassword = "test"; // this is received from ControlPanel
//      assertThrows(UsernameNotFoundException.class, () -> {
//          unknownUserAdmin.setUserPassword("Jenny", hashedPassword, sessionToken);
//      });
//      assertFalse(success);
//    }

    /* Test 18: Set Other User Password (exception handling)
     * Description: Check that if the calling user does not have "EditUsers" permission that they are unable to
     * modify password of other users.
     * Expected Output: Hashed password not updated in the DB, throw InsufficientPermissionsException
     */
//    @Test
//    public void setUserPasswordInsufficientPermissions() {
//      basicUserAdmin = new UserAdmin("Joe"); // temporarily change calling username
//      String hashedPassword = "test"; // this is received from ControlPanel
//      assertThrows(InsufficientPermissionsException.class, () -> {
//          bool success = basicUserAdmin.setUserPassword("Jenny", hashedPassword, sessionToken);
//      });
//      assertFalse(success);
//    }

    /* Test 19: Set Other User Password (exception handling)
     * Description: Check that if the username associated with the hashed password does not exist in database then
     * the password should not be updated and an exception should be thrown.
     * Expected Output: Hashed password not updated in the DB, throw UsernameNotFoundException
     */
//    @Test
//    public void setUserPasswordNoUsernameInDb() {
//      String hashedPassword = "test"; // this is received from ControlPanel
//      assertThrows(UsernameNotFoundException.class, () -> {
//          unknownUserAdmin.setUserPassword("unknown", hashedPassword, sessionToken);
//      });
//      assertFalse(success);
//    }

    /* Test 20: Delete User (success)
     * Description:
     * Check that the calling user has "EditUsers" permission, then find corresponding username in db
     * (if it exists) and then remove and return acknowledgement (boolean) to Control Panel.
     * Expected Output: Username is deleted from the DB and return success (boolean) - checking for username in the DB
     * should throw a UsernameNotFoundException.
     */
//    @Test
//    public void deleteUser() {
//      bool success = userAdmin.deleteUser("Jenny", sessionToken);
//      assertAll("Check for Delete Users",
//        ()-> assertThrows(UsernameNotFoundException.class, userAdmin.getUserPermissions("Jenny")),
//        ()-> assertTrue(success)
//      )
//    }

    /* Test 21: Delete User (exception handling)
     * Description: Check that the calling user exists and has not been deleted since attempt to call (check on submit)
     * Expected Output: Throws UsernameNotFoundException, username not deleted and method returns false (boolean).
     */
//    @Test
//    public void deleteUserDeletedCallingUsername()() {
//      unknownUserAdmin = new UserAdmin("non-existent"); // temporarily change calling username to something unknown
//      assertThrows(UsernameNotFoundException.class, () -> {
//          unknownUserAdmin.deleteUser("Jenny", sessionToken);
//      });
//      assertFalse(success);
//    }

    /* Test 22: Delete User (exception handling)
     * Description: Check that if the calling user does not have "EditUsers" permission that they are unable to
     * delete other users.
     * Expected Output: Username is not deleted from DB, return false and throw InsufficientPermissionsException
     */
//    @Test
//    public void deleteUserInsufficientPermissions() {
//      basicUserAdmin = new UserAdmin("Joe"); // temporarily change calling username
//      assertThrows(InsufficientPermissionsException.class, () -> {
//          bool success = basicUserAdmin.deleteUser("Jenny", sessionToken);
//      });
//      assertFalse(success);
//    }

    /* Test 23: Delete User (exception handling)
     * Description: Check that if the username specified does not exist in database then, they should not be deleted
     * and instead an exception should be thrown.
     * Expected Output: Username not deleted DB, throw UsernameNotFoundException
     */
//    @Test
//    public void deleteUserNoUsernameInDb()() {
//      assertThrows(UsernameNotFoundException.class, () -> {
//          unknownUserAdmin.setUserPassword("unknown", sessionToken);
//      });
//      assertFalse(success);
//    }

    /* Test 24: Create User (success)
     * Description: Check that the calling user has "EditUsers" permission, then create the corresponding username in
     * the DB with the hashed password and permissions and return acknowledgement (boolean) to Control Panel.
     * Expected Output: User is created in the DB and return success (boolean)
     */
//    @Test(expected = Test.None.class /* no exception expected */)
//    public void createUser() {
//      int[] userPermissions = {1,1,1,1};
//      String encryptedPassword = "pass";
//      bool success = userAdmin.createUser("Jacinta", userPermissions, encryptedPassword, sessionToken);
//      assertTrue(success);
//    }

    /* Test 25: Create User (exception handling)
     * Description: Check that the calling user exists and has not been deleted since attempt to call (check on submit)
     * Expected Output: Throws UsernameNotFoundException, user not created and method returns false (boolean).
     */
//    @Test
//    public void createUserDeletedCallingUsername()() {
//      unknownUserAdmin = new UserAdmin("non-existent"); // temporarily change calling username to something unknown
//      assertThrows(UsernameNotFoundException.class, () -> {
//          int[] userPermissions = {0,0,0,0};
//          String encryptedPassword = "pass";
//          bool success = unknownUserAdmin.createUser("Ra", userPermissions, encryptedPassword, sessionToken);
//      });
//      // Check return value
//      assertFalse(success);
//      // Check that the user is not added to the DB
//      assertFalse(unknownUserAdmin.userExists("Ra"));
//    }

    /* Test 26: Create User (exception handling)
     * Description: Check that if the calling user does not have "EditUsers" permission that they are unable to
     * create other users.
     * Expected Output: Username is not created in DB, return false and throw InsufficientPermissionsException
     */
//    @Test
//    public void createUserInsufficientPermissions() {
//      basicUserAdmin = new UserAdmin("Joe"); // temporarily change calling username
//      assertThrows(InsufficientPermissionsException.class, () -> {
//          int[] userPermissions = {0,0,0,0};
//          String encryptedPassword = "pass";
//          bool success = basicUserAdmin.createUser("Ra", userPermissions, encryptedPassword, sessionToken);
//      });
//      // Check return value
//      assertFalse(success);
//      // Check that the user is not added to the DB
//      assertFalse(basicUserAdmin.userExists("Ra"));
//    }

    /* Test 27: Create User (exception handling)
     * Description: Check that if the desired username does not already exist in the DB (must be unique).
     * Expected Output: Username already exists in DB and a usernameAlreadyExistsException will be thrown.
     */
//    @Test
//    public void createUserDuplicateUsername() {
//      // Check that the user is not added to the DB
//      assertFalse(basicUserAdmin.userExists("DuplicateUser"));
//      assertThrows(usernameAlreadyExistsException.class, () -> {
//          int[] userPermissions = {0,0,0,0};
//          String encryptedPassword = "pass";
//          bool success1 = basicUserAdmin.createUser("DuplicateUser", userPermissions, encryptedPassword, sessionToken);
//          bool success2 = basicUserAdmin.createUser("DuplicateUser", userPermissions, encryptedPassword, sessionToken);
//      });
//      // Check return value
//      assertTrue(success1);
//      assertFalse(success2);
//    }

}