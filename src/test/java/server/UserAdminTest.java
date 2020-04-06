package server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserAdminTest {
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

    /* Test 2: Check User Exists (Helper)
     * Description: Check that a user exists in the database - helper method
     * Expected Output: A boolean where true is returned if the user is found in the DB and false otherwise
     * // TODO: Implement Fake db through HashMap in the UserAdmin source code
     * // TODO: Ensure this method is implemented and errors thrown when doing any of these method calls
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

    /* Test 3: Get Other User's Permissions
     * Description: Check that only users with "Edit Permissions" can see any user's permissions
     * Order - Create Billboards, Edit All Billboards, Schedule Billboards, Edit Users
     * Expected Output: Given the requested username, the method should return associated permissions.
     */
//    @Test
//    public void getOtherUserPermissions() {
//      // Here "root" user wants to check permission's of the following "test" users
//      // "sessionToken" should be sent from ControlPanel
//      // TODO: Add these usernames with the corresponding permissions in the Fake DB in UserAdmin source code
//      assertAll("Check for All Possible User Permission Combinations",
//        ()-> assertEquals({0,0,0,0}, userAdmin.getUserPermissions("test0", "sessionToken")),
//        ()-> assertEquals({1,0,0,0}, userAdmin.getUserPermissions("test1", "sessionToken")),
//        ()-> assertEquals({0,1,0,0}, userAdmin.getUserPermissions("test2", "sessionToken")),
//        ()-> assertEquals({0,0,1,0}, userAdmin.getUserPermissions("test3", "sessionToken")),
//        ()-> assertEquals({0,0,0,1}, userAdmin.getUserPermissions("test4", "sessionToken")),
//        ()-> assertEquals({1,1,0,0}, userAdmin.getUserPermissions("test5", "sessionToken")),
//        ()-> assertEquals({1,0,1,0}, userAdmin.getUserPermissions("test6", "sessionToken")),
//        ()-> assertEquals({1,0,0,1}, userAdmin.getUserPermissions("test7", "sessionToken")),
//        ()-> assertEquals({0,1,1,0}, userAdmin.getUserPermissions("test8", "sessionToken")),
//        ()-> assertEquals({0,1,0,1}, userAdmin.getUserPermissions("test9", "sessionToken")),
//        ()-> assertEquals({0,0,1,1}, userAdmin.getUserPermissions("test10", "sessionToken")),
//        ()-> assertEquals({1,1,1,0}, userAdmin.getUserPermissions("test11", "sessionToken")),
//        ()-> assertEquals({1,1,0,1}, userAdmin.getUserPermissions("test12", "sessionToken")),
//        ()-> assertEquals({1,0,1,1}, userAdmin.getUserPermissions("test13", "sessionToken")),
//        ()-> assertEquals({0,1,1,1}, userAdmin.getUserPermissions("test14", "sessionToken")),
//        ()-> assertEquals({1,1,1,1}, userAdmin.getUserPermissions("root", "sessionToken"))
//      )
//    }

    /* Test 4: Get Own Users Permissions
     * Description: Check that any user can see their own user permissions (int[4])
     * Order - Create Billboards, Edit All Billboards, Schedule Billboards, Edit Users
     * Expected Output: Given the user's own username, the method should return associated permissions
     */
//    @Test
//    public void getOwnUserPermissions() {
//      // "sessionToken" should be sent from ControlPanel
//      // Only testing a few select uses cases unlike above test
//      assertAll("Check for Retrieving Most Own User Permission Combinations",
//        ()-> {
//          testUserAdmin = new UserAdmin("test0");
//          assertEquals({0,0,0,0}, testUserAdmin.getUserPermissions("test0", "sessionToken"))
//        },
//        ()-> {
//          testUserAdmin = new UserAdmin("test1");
//          assertEquals({1,0,0,0}, testUserAdmin.getUserPermissions("test1", "sessionToken"))
//        },
//        ()-> {
//          testUserAdmin = new UserAdmin("test2");
//          assertEquals({0,1,0,0}, testUserAdmin.getUserPermissions("test2", "sessionToken"))
//        },
//        ()-> {
//          testUserAdmin = new UserAdmin("test3");
//          assertEquals({0,0,1,0}, testUserAdmin.getUserPermissions("test3", "sessionToken"))
//        },
//        ()-> {
//          testUserAdmin = new UserAdmin("test4");
//          assertEquals({0,0,0,1}, testUserAdmin.getUserPermissions("test4", "sessionToken"))
//        },
//        ()-> {
//          testUserAdmin = new UserAdmin("root");
//          assertEquals({1,1,1,1}, testUserAdmin.getUserPermissions("root", "sessionToken"))
//        }
//      )
//    }

    /* Test 5: Get Other User's Permissions (Exception Handling)
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
//          basicUserAdmin.getPermissions("root", "sessionToken");
//       });
//    }

    /* Test 6: List Users (Success)
     * Description: List all of the users in the database if calling username has "Edit Users" permissions
     * Note: The calling username is retrieved as a private field from this UserAdmin Class
     * Expected Output: All of the users in the database are able to be listed.
     */
//    @Test(expected = Test.None.class /* no exception expected */
//    public void listUsers() {
//      userAdmin.listUsers("sessionToken");
//    }

    /* Test 7: List Users (Exception Handling)
     * Description: List all of the users in the database - throw exception due to non-existent calling username
     * (e.g. if someone else deleted you whilst logged in).
     * Expected Output: None of the users in the database are able to be listed, throw UsernameNotFoundException
     */
//    @Test
//    public void listUsers() {
//      unknownUserAdmin = new UserAdmin("non-existent"); // temporarily change calling username to something unknown
//      assertThrows(UsernameNotFoundException.class, () -> {
//          unknownUserAdmin.listUsers("sessionToken");
//      });
//    }

    /* Test 8: List Users (Exception Handling)
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
//          basicUserAdmin.listUsers("sessionToken");
//      });
//    }

    /* Test 9: Set User Permissions (Success)
     * Description: Check that the calling user has "EditUsers" permission, then find corresponding username in db
     * (if it exists) and then modify to the specified permissions and return acknowledgement (boolean) to Control Panel.
     * Expected Output: Username updated in the DB with the specified permissions and return success (boolean).
     */
//    @Test(expected = Test.None.class /* no exception expected */)
//    public void setUserPermissions() {
//      int[] requestedPermissions = {1,1,0,0};
//      bool success = userAdmin.setUserPermissions("Jenny", requestedPermissions, "sessionToken");
//      assertEquals(requestedPermissions, userAdmin.getUserPermissions("Jenny"));
//      assertTrue(success);
//    }

    /* Test 10: Set User Permissions (Exception Handling)
     * Description: Attempt to set user's permissions but throw exception due to non-existent calling username
     * (e.g. if someone else deleted you whilst logged in).
     * Expected Output: The requested permissions are not set in the DB, throw UsernameNotFoundException
     */
//    @Test
//    public void setUserPermissionsDeletedCallingUsername() {
//      unknownUserAdmin = new UserAdmin("non-existent"); // temporarily change calling username to something unknown
//      int[] requestedPermissions = {1,1,1,0};
//      assertThrows(UsernameNotFoundException.class, () -> {
//          unknownUserAdmin.setUserPermissions("Jenny", requestedPermissions, "sessionToken");
//      });
//      assertFalse(success);
//    }

    /* Test 11: Set User Permissions (Exception Handling)
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
//          bool success = basicUserAdmin.setUserPermissions("Jenny", requestedPermissions, "sessionToken");
//      });
//      assertFalse(success);
//    }

    /* Test 12: Set User Permissions (Exception Handling)
     * Description: Attempt to set user's permissions but throw exception due to non-existent requested username in db
     * (e.g. if someone else deleted the other user whilst logged in).
     * Expected Output: The requested permissions are not set in the DB, throw UsernameNotFoundException
     */
//    @Test
//    public void setUserPermissionsNoUsernameInDb() {
//      assertThrows(UsernameNotFoundException.class, () -> {
//          int[] requestedPermissions = {1,1,1,0};
//          unknownUserAdmin.setUserPermissions("unknownUser", requestedPermissions, "sessionToken");
//      });
//      assertFalse(success);
//    }

// TODO: FIX FROM HERE UPWARDS!

    /* Test 13: Get Password (Success)
     * Description: Find corresponding username in db (if it exists) and then return the password
     * Expected Output: Password is retrieved from the DB and returns as string
     */
//    @Test
//    public void getPassword() {
//      //TODO: Ensure testUser in fake db with "test" as password
//      string dbResponse = userAdmin.getPassword("testUser", "sessionToken");
//      // Check return value
//      assertEquals("test", dbResponse);
//    }

    /* Test 13: Get Password (Exception Handling)
     * Description: Find corresponding username in db (if it exists) and then return the password
     * Expected Output: UsernameNotFoundException is thrown as the username does not exist in the db
     */
//    @Test
//    public void getPassword() {
//      // Test setup - Ensure the username does not exist in DB
//      if (userAdmin.userExists("non-existent")) {
//          userAdmin.deleteUser("non-existent", "sessionToken");
//      }
//      // Check that exception is thrown
//      assertThrows(UsernameNotFoundException.class, () -> {
//          string dbResponse = userAdmin.getPassword("non-existent", "sessionToken");
//      });
//    }


    /* Test 13: Set Own Password (Success)
     * Description: Find corresponding username in db (if it exists) and then modify to the hashed password and
     *              return acknowledgement string to Control Panel.
     * Expected Output: Hashed password updated in the DB and returns string "Success: Own Password Updated"
     */
//    @Test
//    public void setOwnPassword() {
//     // Test setup - Ensure the user's original password is different
//     if (userAdmin.getPassword("newUser", "sessionToken") == "changedPass") {
//          userAdmin.setPassword("newUser", "pass", "sessionToken");
//      }
//      basicUserAdmin = new UserAdmin("Joe"); // temporarily change calling username to a basic user
//      string dbResponse = basicUserAdmin.setPassword("Joe", "changedPass", "sessionToken");
//      // Check return value
//      assertEquals("Success: Own Password Updated", dbResponse);
//      // Check that the user pass is actually updated in the DB
//      assertEquals("changedPass",userAdmin.getPassword("Joe"));
//    }

    /* Test 14: Set Own Password (Exception Handling)
     * Description: Set own user password in the database - throw exception due to non-existent calling username
     * (e.g. if someone else deleted you whilst logged in).
     * Expected Output: Hashed password not updated in the DB and returns string "Error: Calling Username Deleted"
     */
//    @Test
//    public void setOwnPasswordDeletedCallingUsername() {
//      // Test setup - Ensure the user exists with the expected password in the DB
//      if (userAdmin.userExists("non-existent")) {
//          userAdmin.deleteUser("non-existent", "sessionToken");
//      }
//      unknownUserAdmin = new UserAdmin("non-existent"); // temporarily change calling username to something unknown
//      string dbResponse = unknownUserAdmin.setPassword("non-existent", "changedPass", "sessionToken");
//      // Check return value
//      assertEquals("Error: Calling Username Deleted", dbResponse);
//      // Check for Exception that the password cannot be obtained for user that does not exist in DB
//      assertThrows(UsernameNotFoundException.class, () -> {
//          string dbResponse = userAdmin.getPassword("non-existent", "sessionToken");
//      });
//    }


    /* Test 15: Set Other User Password (Success)
     * Description: Check that the calling user has "EditUsers" permission, then find corresponding username in db
     * (if it exists) and then modify to the hashed password and return acknowledgement (boolean) to Control Panel.
     * Expected Output: Hashed password updated in the DB and returns string "Success: Other User Password Updated"
     */
//    @Test(expected = Test.None.class /* no exception expected */)
//    public void setOtherPassword() {
//     // Test setup - Ensure the user's original password is different
//     if (userAdmin.getPassword("newUser", "sessionToken") == "changedPass") {
//          userAdmin.setPassword("newUser", "pass", "sessionToken");
//      }
//      String dbResponse = userAdmin.setPassword("newUser", "changedPass", "sessionToken");
//      // Check return value
//      assertEquals("Success: Other User Password Updated", dbResponse);
//      // Check that the user pass is actually updated in the DB
//      assertEquals("changedPass", userAdmin.getPassword("newUser"));
//    }

    /* Test 16: Set Other User Password (Exception Handling)
     * Description: Check that the calling user still exists in the DB before setting user password.
     * Expected Output: Hashed password not updated in the DB and returns string "Error: Calling Username Deleted"
     */
//    @Test
//    public void setOtherPasswordDeletedCallingUsername() {
//      // Test setup - Ensure the user exists with the expected password in the DB
//      if (!userAdmin.userExists("Jenny")) {
//          userAdmin.createUser("Jenny", {0,0,0,0}, "pass", "sessionToken");
//      }
//      if (userAdmin.getPassword("Jenny", "sessionToken") !== "pass") {
//          userAdmin.setPassword("Jenny", "pass", "sessionToken");
//      }
//      unknownUserAdmin = new UserAdmin("non-existent"); // temporarily change calling username to something unknown
//      String dbResponse = unknownUserAdmin.setPassword("Jenny", "changedPass", "sessionToken");
//      // Check return value
//      assertEquals("Error: Calling Username Deleted", dbResponse);
//      // Check that the user pass is not actually still updated in the DB
//      assertEquals("pass",userAdmin.getPassword("Jenny"));
//    }

    /* Test 17: Set Other User Password (Exception Handling)
     * Description: Check that if the calling user does not have "EditUsers" permission that they are unable to
     * modify password of other users.
     * Expected Output: Hashed password not updated in the DB and returns string "Error: Insufficient User Permissions"
     */
//    @Test
//    public void setOtherPasswordInsufficientPermissions() {
//      // Test setup - Ensure the user exists with the expected password in the DB
//      if (!userAdmin.userExists("Jenny")) {
//          userAdmin.createUser(Jenny", {0,0,0,0}, "pass", "sessionToken");
//      }
//      if (userAdmin.getPassword("Jenny", "sessionToken") !== "pass") {
//          userAdmin.setPassword("Jenny", "pass", "sessionToken");
//      }
//      basicUserAdmin = new UserAdmin("Joe"); // temporarily change calling username
//      String dbResponse = basicUserAdmin.setPassword("Jenny", "changedPass", "sessionToken");
//      // Check return value
//      assertEquals("Error: Insufficient User Permissions", dbResponse);
//      // Check that the user pass is not actually still updated in the DB
//      assertEquals("pass",userAdmin.getPassword("Jenny"));
//    }

    /* Test 18: Set Other User Password (Exception Handling)
     * Description: Check that if the username associated with the hashed password does not exist in database then
     * the password should not be updated and an exception should be thrown.
     * Expected Output: Hashed password not updated in the DB and returns string "Error: Username Does Not Exist"
     */
//    @Test
//    public void setOtherPasswordNoUsernameInDb() {
//      // Test setup - Ensure the user to have password updated does not exist in DB
//      if (userAdmin.userExists("unknownUser")) {
//          userAdmin.deleteUser("unknownUser", "sessionToken");
//      }
//      String dbResponse = userAdmin.setPassword("unknownUser", "changedPass", "sessionToken");
//      // Check return value
//      assertEquals("Error: Username Does Not Exist", dbResponse);
//      // Check for Exception that the password cannot be obtained for user that does not exist in DB
//      assertThrows(UsernameNotFoundException.class, () -> {
//          string dbResponse = userAdmin.getPassword("unknownUser", "sessionToken");
//      });//    }

//TODO: CHECK "DELETE OWN USER" HANDLES
    /* Test 19: Delete User (Success)
     * Description:
     * Check that the calling user has "EditUsers" permission, then find corresponding username in db
     * (if it exists) and then remove and return acknowledgement to Control Panel.
     * Expected Output: Username is deleted in DB and returns string "Success: User Deleted"
     */
//    @Test
//    public void deleteUser() {
//      // Test setup - Ensure the user to be deleted exists in DB
//      if (!userAdmin.userExists("Jenny")) {
//          userAdmin.createUser("Jenny", {0,0,0,0}, "pass", "sessionToken");
//      }
//      // Check return value
//      string dbResponse = userAdmin.deleteUser("Jenny", "sessionToken");
//      assertEquals("Success: User Deleted", dbResponse);
//      // Check that the user is actually removed from DB
//      assertFalse(userAdmin.userExists("Jenny"));
//    }

    /* Test 20: Delete User (Exception Handling)
     * Description: Check that the calling user exists and has not been deleted since attempt to call (check on submit)
     * Expected Output: Username is not deleted in DB and returns string "Error: Calling Username Deleted"
     */
//    @Test
//    public void deleteUserDeletedCallingUsername()() {
//      // Test setup - Ensure the user to be deleted exists in DB
//      if (!userAdmin.userExists("Jenny")) {
//          userAdmin.createUser("Jenny", {0,0,0,0}, "pass", "sessionToken");
//      }
//      unknownUserAdmin = new UserAdmin("non-existent"); // temporarily change calling username
//      // Check return value
//      string dbResponse = unknownUserAdmin.deleteUser("Jenny", "sessionToken");
//      assertEquals("Error: Calling Username Deleted", dbResponse);
//      // Check that the user to be deleted isn't removed anyway
//      assertTrue(userAdmin.userExists("Jenny"));
//    }

    /* Test 21: Delete User (Exception Handling)
     * Description: Check that if the calling user does not have "EditUsers" permission that they are unable to
     * delete other users.
     * Expected Output: Username is not deleted in DB and returns string "Error: Insufficient User Permissions"
     */
//    @Test
//    public void deleteUserInsufficientPermissions() {
//      // Test setup - Ensure the user to be deleted exists in DB
//      if (!userAdmin.userExists("Jenny")) {
//          userAdmin.createUser("Jenny", {0,0,0,0}, "pass", "sessionToken");
//      }
//      basicUserAdmin = new UserAdmin("Joe"); // temporarily change calling username
//      // Check return value
//      string dbResponse = basicUserAdmin.deleteUser("Jenny", "sessionToken");
//      assertEquals("Error: Insufficient User Permissions", dbResponse);
//      // Check that the user to be deleted isn't removed anyway
//      assertTrue(userAdmin.userExists("Jenny"));
//    }

    /* Test 22: Delete User (Exception Handling)
     * Description: Check that if the username specified does not exist in database then, they should not be deleted
     * and instead an exception should be thrown.
     * Expected Output: Username is not deleted in DB and returns string "Error: Username Does Not Exist"
     */
//    @Test
//    public void deleteUserNoUsernameInDb() {
//      // Test setup - Ensure the user to be deleted does not exist in DB
//      if (userAdmin.userExists("unknownUser")) {
//          userAdmin.deleteUser("unknownUser", "sessionToken");
//      }
//      // Check return value
//      string dbResponse = userAdmin.deleteUser("unknownUser", "sessionToken");
//      assertEquals("Error: Username Does Not Exist", dbResponse);
//      // Check that the user to be deleted still doesn't exist
//      assertFalse(userAdmin.userExists("unknownUser"));
//    }


    /* Test 23: Create User (Success)
     * Description: Check that the calling user has "EditUsers" permission, then create the corresponding username in
     * the DB with the hashed password and permissions and return acknowledgement to Control Panel.
     * Expected Output: User is created in the DB and returns string "Success: User Created"
     */
//    @Test(expected = Test.None.class /* no exception expected */)
//    public void createUser() {
//      // Test setup - Ensure the user to be created does not already exist
//      if (userAdmin.userExists("Jacinta")) {
//          userAdmin.deleteUser("Jacinta", "sessionToken");
//      }
//      // Check return value
//      String dbResponse = userAdmin.createUser("Jacinta", {0,0,0,0}, "pass", "sessionToken");
//      assertEquals("Success: User Created", dbResponse);
//      // Check that the user is actually added to the DB
//      assertTrue(userAdmin.userExists("Jacinta"));
//    }

    /* Test 24: Create User (Exception Handling)
     * Description: Check that the calling user exists and has not been deleted since attempt to call (check on submit)
     * Expected Output: Username is not created in DB and returns string "Error: Calling Username Deleted"
     */
//    @Test
//    public void createUserDeletedCallingUsername() {
//      //TODO: ASK ABOUT BEST WAY WE CAN PASS IN THE "CALLING USERNAME"
//      // Test setup - Ensure the user to be created does not already exist
//      if (userAdmin.userExists("Ra")) {
//          userAdmin.deleteUser("Ra", "sessionToken");
//      }
//      unknownUserAdmin = new UserAdmin("non-existent"); // temporarily change calling username to something unknown
//      String dbResponse = unknownUserAdmin.createUser("Ra", {0,0,0,0}, "pass", "sessionToken");
//      // Check return value
//      assertEquals("Error: Calling Username Deleted", dbResponse);
//      // Check that the user to be created is not added to the DB anyway
//      assertFalse(unknownUserAdmin.userExists("Ra"));
//    }

    /* Test 25: Create User (Exception Handling)
     * Description: Check that if the calling user does not have "EditUsers" permission that they are unable to
     * create other users.
     * Expected Output: Username is not created in DB and returns string "Error: Insufficient User Permissions"
     */
//    @Test
//    public void createUserInsufficientPermissions() {
//      basicUserAdmin = new UserAdmin("Joe"); // temporarily change calling username
//      String dbResponse = basicUserAdmin.createUser("DuplicateUser", {0,0,0,0}, "pass", "sessionToken");
//      // Check return value
//      assertEquals("Error: Insufficient User Permissions", dbResponse);
//      // Check that the user is not added to the DB anyway
//      assertFalse(basicUserAdmin.userExists("Ra"));
//    }

    /* Test 26: Create User (Exception Handling)
     * Description: Check that if the desired username does not already exist in the DB (must be unique).
     * Expected Output: Username already exists in DB and returns string "Error: Username Already Taken"
     */
//    @Test
//    public void createUserDuplicateUsername() {
//      // Test Setup - Add the user to the DB if not already in existence
//      if (!userAdmin.userExists("DuplicateUser")) {
//          userAdmin.createUser("DuplicateUser", {0,0,0,0}, "pass", "sessionToken");
//      }
//      // Attempt to add duplicate username
//      String dbResponse = userAdmin.createUser("DuplicateUser", {0,0,0,0}, "pass", "sessionToken");
//      // Check return value
//      assertEquals("Error: Username Already Taken", dbResponse);
//    }

}