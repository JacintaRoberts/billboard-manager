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
        userAdmin = new UserAdmin();
    }

    //TODO: SETUP HASHMAP OF SESSIONTOKENS SO THAT USER PERMISSIONS CAN BE ADEQUATELY TESTED - WILL NEED
    // TO SHARE THIS WITH MOST OF THE TEST CLASSES...

    /* Test 2: Check User Exists (Helper for other methods in this class)
     * Description: Check that a user exists in the database - helper method
     * Expected Output: A boolean where true is returned if the user is found in the DB and false otherwise
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
//      // TODO: Add these usernames with the corresponding permissions in the Fake DB in UserAdmin
//      assertAll("Check for All Possible User Permission Combinations",
//        ()-> assertEquals({0,0,0,0}, userAdmin.getUserPermissions("sessionToken", "test0")),
//        ()-> assertEquals({1,0,0,0}, userAdmin.getUserPermissions("sessionToken", "test1")),
//        ()-> assertEquals({0,1,0,0}, userAdmin.getUserPermissions("sessionToken", "test2")),
//        ()-> assertEquals({0,0,1,0}, userAdmin.getUserPermissions("sessionToken", "test3")),
//        ()-> assertEquals({0,0,0,1}, userAdmin.getUserPermissions("sessionToken", "test4")),
//        ()-> assertEquals({1,1,0,0}, userAdmin.getUserPermissions("sessionToken", "test5")),
//        ()-> assertEquals({1,0,1,0}, userAdmin.getUserPermissions("sessionToken", "test6")),
//        ()-> assertEquals({1,0,0,1}, userAdmin.getUserPermissions("sessionToken", "test7")),
//        ()-> assertEquals({0,1,1,0}, userAdmin.getUserPermissions("sessionToken", "test8")),
//        ()-> assertEquals({0,1,0,1}, userAdmin.getUserPermissions("sessionToken", "test9")),
//        ()-> assertEquals({0,0,1,1}, userAdmin.getUserPermissions("sessionToken", "test10)),
//        ()-> assertEquals({1,1,1,0}, userAdmin.getUserPermissions("sessionToken", "test11)),
//        ()-> assertEquals({1,1,0,1}, userAdmin.getUserPermissions("sessionToken", "test12)),
//        ()-> assertEquals({1,0,1,1}, userAdmin.getUserPermissions("sessionToken", "test13)),
//        ()-> assertEquals({0,1,1,1}, userAdmin.getUserPermissions("sessionToken", "test14)),
//        ()-> assertEquals({1,1,1,1}, userAdmin.getUserPermissions("sessionToken", "root"))
//      )
//    }


    /* Test 4: Get Own User Permissions
     * Description: Check that any user can see their own user permissions (int[4])
     * Order - Create Billboards, Edit All Billboards, Schedule Billboards, Edit Users
     * Expected Output: Given the user's own username, the method should return associated permissions
     */
//    @Test
//    public void getOwnUserPermissions() {
//      assertAll("Check for Retrieving All Possible Own User Permission Combinations",
//        ()-> assertEquals({0,0,0,0}, userAdmin.getUserPermissions("sessionToken", "test0")),
//        ()-> assertEquals({1,0,0,0}, userAdmin.getUserPermissions("sessionToken", "test1")),
//        ()-> assertEquals({0,1,0,0}, userAdmin.getUserPermissions("sessionToken", "test2")),
//        ()-> assertEquals({0,0,1,0}, userAdmin.getUserPermissions("sessionToken", "test3")),
//        ()-> assertEquals({0,0,0,1}, userAdmin.getUserPermissions("sessionToken", "test4")),
//        ()-> assertEquals({1,1,0,0}, userAdmin.getUserPermissions("sessionToken", "test5")),
//        ()-> assertEquals({1,0,1,0}, userAdmin.getUserPermissions("sessionToken", "test6")),
//        ()-> assertEquals({1,0,0,1}, userAdmin.getUserPermissions("sessionToken", "test7")),
//        ()-> assertEquals({0,1,1,0}, userAdmin.getUserPermissions("sessionToken", "test8")),
//        ()-> assertEquals({0,1,0,1}, userAdmin.getUserPermissions("sessionToken", "test9")),
//        ()-> assertEquals({0,0,1,1}, userAdmin.getUserPermissions("sessionToken", "test10)),
//        ()-> assertEquals({1,1,1,0}, userAdmin.getUserPermissions("sessionToken", "test11)),
//        ()-> assertEquals({1,1,0,1}, userAdmin.getUserPermissions("sessionToken", "test12)),
//        ()-> assertEquals({1,0,1,1}, userAdmin.getUserPermissions("sessionToken", "test13)),
//        ()-> assertEquals({0,1,1,1}, userAdmin.getUserPermissions("sessionToken", "test14)),
//        ()-> assertEquals({1,1,1,1}, userAdmin.getUserPermissions("sessionToken", "root"))
//      )
//    }


    /* Test 5: Get Other User's Permissions (Exception Handling)
     * Description: Get other User's Permissions from db - throw exception due to non-existent calling username in DB
     * Expected Output: User's Permissions unable to be retrieved from DB and returns "Error: Calling Username Deleted"
     */
//    @Test
//    public void getOtherUserPermissionsCallingUsernameDeleted() {
//      // Temporarily change calling username to something unknown (via the session token)
//      Object[] dbResponse = userAdmin.getUserPermissions("unknownSessionToken", "non-existent");
//      // Check return value
//      assertEquals("Error: Calling Username Deleted", dbResponse[0]);
//      assertEquals(1, dbResponse.length);
//    }


    /* Test 6: Get Other User's Permissions (Exception Handling)
     * Description: Get other User's Permissions from db - throw exception due to insufficient calling permissions
     * Require "EditUsers" permission which is the 4th element in UserPermissions object
     * e.g. [1,1,1,0] can't call the method.
     * Expected Output: User's Permissions unable to be retrieved and returns "Error: Insufficient User Permission"
     */
//    @Test
//    public void getOtherUserPermissionsInsufficientPermissions() {
//      Object[] dbResponse =  userAdmin.getUserPermissions("basicToken", "root");
//      // Check return value
//      assertEquals("Error: Insufficient User Permission", dbResponse[0]);
//      assertEquals(1, dbResponse.length);
//    }


    /* Test 7: Get Other User's Permissions (Exception Handling)
     * Description: Get other User's Permissions from db - throw exception due to non-existent username in DB
     * Expected Output: User's Permissions unable to be retrieved from DB and returns "Error: Username Does Not Exist"
     */
//    @Test
//    public void getOtherUserPermissionsNoUsernameInDb() {
//      Object[] dbResponse = userAdmin.getUserPermissions("sessionToken", "non-existent");
//      // Check return value
//      assertEquals("Error: Username Does Not Exist", dbResponse[0]);
//      assertEquals(1, dbResponse.length);
//    }


    /* Test 8: List Users (Success)
     * Description: List all of the users in the database if calling username has "Edit Users" permissions
     * Note: The calling username is retrieved as a private field from this UserAdmin Class
     * Expected Output: All of the users in the database are able to be listed.
     */
//    @Test(expected = Test.None.class /* no exception expected */
//    public void listUsers() {
//      ArrayList<String> dbResponse = userAdmin.listUsers("sessionToken");
//      assertTrue(dbResponse instanceof ArrayList<String>);
//    }


    /* Test 9: List Users (Exception Handling)
     * Description: List all of the users in the database - throw exception due to non-existent calling username
     * (e.g. if someone else deleted you whilst logged in).
     * Expected Output: List of Users unable to be retrieved from DB and returns "Error: Calling Username Deleted"
     */
//    @Test
//    public void listUsersCallingUsernameDeleted() {
//      ArrayList<String> dbResponse = userAdmin.listUsers("unknownSessionToken");
// // TODO: SOMEHOW MAKE THE RETURN TYPE CHANGE DYNAMICALLY AND HAVE CONTROL PANEL UNDERSTAND...
//      MAYBE READ FIRST ELEMENT AND IF IT STARTS WITH "ERROR" THEN WE KNOWN TO THROW EXCEPTION?
//      WE ALSO KNOW THERE SHOULD ALWAYS BE >= 1 USERS IN DB SO SEEMS SAFE?
//      // Check return message and that no other results get appended

//      // Check return value
//      assertEquals("Error: Calling Username Deleted", dbResponse.get(0));
//      assertEquals(1, dbResponse.size());
//    }


    /* Test 10: List Users (Exception Handling)
     * Description: List all of the users in the database - throw exception due to insufficient permissions
     * Require "EditUsers" permission which is the 4th element in UserPermissions object
     * e.g. [1,1,1,0] can't call the method.
     * Expected Output: List of Users unable to be retrieved from DB and returns "Error: Insufficient User Permission"
     */
//    @Test
//    public void listUsersInsufficientPermissions() {
//      userAdmin.listUsers("basicToken");
//      // Check return value
//      assertEquals("Error: Insufficient User Permission", dbResponse.get(0));
//      assertEquals(1, dbResponse.size());
//    }


    /* Test 11: Set Own User Permissions (Success)
     * Description: Check that the calling user has "EditUsers" permission, then find corresponding username in db
     * (if it exists) and then modify to the specified permissions and return string acknowledgement to Control Panel.
     * Expected Output: User permissions updated in the DB and returns string "Success: Own Permissions Updated"
     */
//    @Test(expected = Test.None.class /* no exception expected */)
//    public void setOwnUserPermissions() {
//      // Attempt to remove own CreateBillboardsPermission
//      String dbResponse = userAdmin.setUserPermissions("root", {0,1,1,1}, "sessionToken");
//      // Check return value
//      assertEquals("Success: Own Permissions Updated", dbResponse);
//      // Check that the user permissions are actually updated in the DB
//      assertEquals({0,1,1,1}, userAdmin.getUserPermissions("root"));
//    }


    /* Test 12: Set Own User Permissions (Exception Handling)
     * Description: Check that the calling user has "EditUsers" permission, then find corresponding username in db
     * (if it exists) and then modify to the specified permissions and return string acknowledgement to Control Panel.
     * Expected Output: User permissions not updated in DB and returns "Error: Cannot Remove Own Edit Users Permission"
     */
//    @Test(expected = Test.None.class /* no exception expected */)
//    public void setOwnUserEditUsersPermission() {
//      // Test setup - Ensure that the user starts with all permissions
//      if (userAdmin.getUserPermissions("sessionToken", "root") !== {1,1,1,1} ) {
//          userAdmin.setUserPermissions("sessionToken", "root", {1,1,1,1});
//      }
//      // Attempt to remove own EditUsersPermission (last element in array)
//      String dbResponse = userAdmin.setUserPermissions("sessionToken", "root", {0,1,1,0});
//      // Check return value
//      assertEquals("Error: Cannot Remove Own Edit Users Permission", dbResponse);
//      // Check that the user permissions are not updated in the DB
//      assertEquals({1,1,1,1}, userAdmin.getUserPermissions("root"));
//    }


    /* Test 13: Set Own User Permissions (Exception Handling)
     * Description: Check that the calling user has "EditUsers" permission, then find corresponding username in db
     * (if it exists) and then modify to the specified permissions and return string acknowledgement to Control Panel.
     * This test will check for appropriate handling of: if someone else deleted you before you click the submit button
     * Expected Output: User permissions not updated in DB and returns "Error: Calling Username Deleted"
     */
//    @Test
//    public void setOwnUserPermissionsCallingUsernameDeleted() {
//      // Test setup - Ensure that the calling user does not exist
//      if (userAdmin.userExists("sessionToken", "non-existent")) {
//          userAdmin.deleteUser("sessionToken", "non-existent");
//      }
//      String dbResponse = userAdmin.setUserPermissions("unknownSessionToken", "non-existent", {0,1,1,1});
//      // Check return value
//      assertEquals("Error: Calling Username Deleted", dbResponse);
//      // Check that the user permissions are still unobtainable
//      assertThrows(UsernameNotFoundException.class, () -> {
//          userAdmin.getUserPermissions("sessionToken", "non-existent"));
//      }
//    }


    /* Test 14: Set Own User Permissions (Exception Handling)
     * Description: Attempt to set user permissions however calling user does not "EditUser" Permission
     * Expected Output: User permissions not updated in DB and returns "Error: Insufficient User Permission"
     */
//    @Test
//    public void setOwnUserPermissionsInsufficientPermissions() {
//      // Test setup - Ensure that the user starts with different permissions
//      if (userAdmin.getUserPermissions("sessionToken", "Joe") !== {0,0,0,0} ) {
//          userAdmin.setUserPermissions("sessionToken", "Joe", {0,0,0,0});
//      }
//      String dbResponse = userAdmin.setUserPermissions("basicToken", "Joe", {0,0,0,1});
//      // Check return value
//      assertEquals("Error: Insufficient User Permission", dbResponse);
//      // Check that the user permissions are not updated in the DB
//      assertEquals({0,0,0,0}, userAdmin.getUserPermissions("Joe"));
//    }



    /* Test 15: Set Other User Permissions (Success)
     * Description: Check that the calling user has "EditUsers" permission, then find corresponding username in db
     * (if it exists) and then modify to the specified permissions and return string acknowledgement to Control Panel.
     * Expected Output: User permissions updated in the DB and returns string "Success: Other User Permissions Updated"
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
//      assertEquals("Success: Other User Permissions Updated", dbResponse);
//      // Check that the user permissions are actually updated in the DB
//      assertEquals({1,1,1,0}, userAdmin.getUserPermissions("Jenny"));
//    }


    /* Test 16: Set Other User Permissions (Exception Handling)
     * Description: Check that the calling user has "EditUsers" permission, then find corresponding username in db
     * (if it exists) and then modify to the specified permissions and return string acknowledgement to Control Panel.
     * This test will check for appropriate handling of: if someone else deleted you before you click the submit button
     * Expected Output: User permissions not updated in DB and returns "Error: Calling Username Deleted"
     */
//    @Test
//    public void setOtherUserPermissionsCallingUsernameDeleted() {
//      // Test setup - Ensure that the user starts with different permissions and that the calling user does not exist
//      if (userAdmin.getUserPermissions("sessionToken", "root") !== {1,1,1,1} ) {
//          userAdmin.setUserPermissions("sessionToken", "root", {1,1,1,1});
//      }
//      if (userAdmin.userExists("sessionToken", "non-existent")) {
//          userAdmin.deleteUser("sessionToken", "non-existent");
//      }
//      String dbResponse = userAdmin.setUserPermissions("unknownSessionToken", "root", {0,1,1,1});
//      // Check return value
//      assertEquals("Error: Calling Username Deleted", dbResponse);
//      // Check that the user permissions are not updated in the DB
//      assertEquals({1,1,1,1}, userAdmin.getUserPermissions("root"));
//    }


    /* Test 17: Set Other User Permissions (Exception Handling)
     * Description: Attempt to set user permissions however calling user does not "EditUser" Permission
     * Expected Output: User permissions not updated in DB and returns "Error: Insufficient User Permission"
     */
//    @Test
//    public void setOtherUserPermissionsInsufficientPermissions() {
//      // Test setup - Ensure that the user starts with different permissions
//      if (userAdmin.getUserPermissions("sessionToken", "Jenny") !== {1,1,1,0} ) {
//          userAdmin.setUserPermissions("sessionToken", "Jenny", {1,1,1,0});
//      }
//      String dbResponse = userAdmin.setUserPermissions("basicToken", "Jenny", {0,0,0,0});
//      // Check return value
//      assertEquals("Error: Insufficient User Permission", dbResponse);
//      // Check that the user permissions are not updated in the DB
//      assertEquals({1,1,1,0}, userAdmin.getUserPermissions("Jenny"));
//    }


    /* Test 18: Set Other User Permissions (Exception Handling)
     * Description: Attempt to set user's permissions but throw exception due to non-existent requested username in db
     * (e.g. if someone else deleted the other user whilst logged in).
     * Expected Output: User permissions not updated in DB and returns "Error: Username Does Not Exist"
     */
//    @Test
//    public void setOtherUserPermissionsNoUsernameInDb() {
//      // Test setup - Ensure that the username does not exist
//      if (userAdmin.userExists("sessionToken", "non-existent")) {
//          userAdmin.deleteUser("sessionToken", "non-existent");
//      String dbResponse = userAdmin.setUserPermissions("sessionToken", "non-existent", {0,1,1,1});
//      // Check return value
//      assertEquals("Error: Username Does Not Exist", dbResponse);
//      // Check that the user permissions are still unobtainable
//      assertThrows(UsernameNotFoundException.class, () -> {
//          userAdmin.getUserPermissions("non-existent"));
//      }
//    }


    /* Test 19: Get Password (Success)
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

    /* Test 21: Set Own Password (Success)
     * Description: Find corresponding username in db (if it exists) and then modify to the hashed password and
     *              return acknowledgement string to Control Panel.
     * Expected Output: Hashed password updated in the DB and returns string "Success: Own Password Updated"
     */
//    @Test
//    public void setOwnPassword() {
//     // Test setup - Ensure the user's original password is different
//     if (userAdmin.getPassword("sessionToken", "newUser") == "changedPass") {
//          userAdmin.setPassword("sessionToken", "newUser", "pass");
//      }
//      string dbResponse = userAdmin.setPassword("basicToken", "Joe", "changedPass");
//      // Check return value
//      assertEquals("Success: Own Password Updated", dbResponse);
//      // Check that the user pass is actually updated in the DB
//      assertEquals("changedPass",userAdmin.getPassword("sessionToken","Joe"));
//    }


    /* Test 22: Set Own Password (Exception Handling)
     * Description: Set own user password in the database - throw exception due to non-existent calling username
     * (e.g. if someone else deleted you whilst logged in).
     * Expected Output: Hashed password not updated in the DB and returns string "Error: Calling Username Deleted"
     */
//    @Test
//    public void setOwnPasswordCallingUsernameDeleted() {
//      // Test setup - Ensure the user exists with the expected password in the DB
//      if (userAdmin.userExists("non-existent")) {
//          userAdmin.deleteUser("sessionToken", "non-existent");
//      }
//      string dbResponse = userAdmin.setPassword("unknownSessionToken", "non-existent", "changedPass");
//      // Check return value
//      assertEquals("Error: Calling Username Deleted", dbResponse);
//      // Check for Exception that the password cannot be obtained for user that does not exist in DB
//      assertThrows(UsernameNotFoundException.class, () -> {
//          string dbResponse = userAdmin.getPassword("sessionToken", "non-existent");
//      });
//    }


    /* Test 23: Set Other User Password (Success)
     * Description: Check that the calling user has "EditUsers" permission, then find corresponding username in db
     * (if it exists) and then modify to the hashed password and return acknowledgement (String) to Control Panel.
     * Expected Output: Hashed password updated in the DB and returns string "Success: Other User Password Updated"
     */
//    @Test(expected = Test.None.class /* no exception expected */)
//    public void setOtherPassword() {
//     // Test setup - Ensure the user's original password is different
//     if (userAdmin.getPassword("sessionToken", "newUser") == "changedPass") {
//          userAdmin.setPassword("sessionToken", "newUser", "pass");
//      }
//      String dbResponse = userAdmin.setPassword("sessionToken", "newUser", "changedPass");
//      // Check return value
//      assertEquals("Success: Other User Password Updated", dbResponse);
//      // Check that the user pass is actually updated in the DB
//      assertEquals("changedPass", userAdmin.getPassword("sessionToken", "newUser"));
//    }


    /* Test 24: Set Other User Password (Exception Handling)
     * Description: Check that the calling user still exists in the DB before setting user password.
     * Expected Output: Hashed password not updated in the DB and returns string "Error: Calling Username Deleted"
     */
//    @Test
//    public void setOtherPasswordCallingUsernameDeleted() {
//      // Test setup - Ensure the user exists with the expected password in the DB
//      if (!userAdmin.userExists("Jenny")) {
//          userAdmin.createUser("sessionToken", "Jenny", {0,0,0,0}, "pass");
//      }
//      if (userAdmin.getPassword("sessionToken", "Jenny") !== "pass") {
//          userAdmin.setPassword("sessionToken", "Jenny", "pass");
//      }
//      String dbResponse = userAdmin.setPassword("unknownSessionToken", "Jenny", "changedPass");
//      // Check return value
//      assertEquals("Error: Calling Username Deleted", dbResponse);
//      // Check that the user pass is not actually still updated in the DB
//      assertEquals("pass",userAdmin.getPassword("sessionToken", "Jenny"));
//    }


    /* Test 25: Set Other User Password (Exception Handling)
     * Description: Check that if the calling user does not have "EditUsers" permission that they are unable to
     * modify password of other users.
     * Expected Output: Hashed password not updated in the DB and returns string "Error: Insufficient User Permission"
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
//      assertEquals("Error: Insufficient User Permission", dbResponse);
//      // Check that the user pass is not actually still updated in the DB
//      assertEquals("pass",userAdmin.getPassword("sessionToken", "Jenny"));
//    }


    /* Test 26: Set Other User Password (Exception Handling)
     * Description: Check that if the username associated with the hashed password does not exist in database then
     * the password should not be updated and an exception should be thrown.
     * Expected Output: Hashed password not updated in the DB and returns string "Error: Username Does Not Exist"
     */
//    @Test
//    public void setOtherPasswordNoUsernameInDb() {
//      // Test setup - Ensure the user to have password updated does not exist in DB
//      if (userAdmin.userExists("unknownUser")) {
//          userAdmin.deleteUser("sessionToken", "unknownUser");
//      }
//      String dbResponse = userAdmin.setPassword("sessionToken", "unknownUser", "changedPass");
//      // Check return value
//      assertEquals("Error: Username Does Not Exist", dbResponse);
//      // Check for Exception that the password cannot be obtained for user that does not exist in DB
//      assertThrows(UsernameNotFoundException.class, () -> {
//          string dbResponse = userAdmin.getPassword("sessionToken", "unknownUser");
//      });//    }


    /* Test 27: Delete User (Success)
     * Description:
     * Check that the calling user has "EditUsers" permission, then find corresponding username in db
     * (if it exists) and then remove and return acknowledgement to Control Panel.
     * Expected Output: Username is deleted in DB and returns string "Success: User Deleted"
     */
//    @Test
//    public void deleteUser() {
//      // Test setup - Ensure the user to be deleted exists in DB
//      if (!userAdmin.userExists("Jenny")) {
//          userAdmin.createUser("sessionToken", "Jenny", {0,0,0,0}, "pass");
//      }
//      // Check return value
//      string dbResponse = userAdmin.deleteUser("sessionToken", "Jenny");
//      assertEquals("Success: User Deleted", dbResponse);
//      // Check that the user is actually removed from DB
//      assertFalse(userAdmin.userExists("Jenny"));
//    }

    /* Test 28: Delete User (Exception Handling)
     * Description: Check that the calling user exists and has not been deleted since attempt to call (check on submit)
     * Expected Output: Username is not deleted in DB and returns string "Error: Calling Username Deleted"
     */
//    @Test
//    public void deleteUserCallingUsernameDeleted()() {
//      // Test setup - Ensure the user to be deleted exists in DB
//      if (!userAdmin.userExists("Jenny")) {
//          userAdmin.createUser("sessionToken", "Jenny", {0,0,0,0}, "pass");
//      }
//      // Check return value
//      string dbResponse = userAdmin.deleteUser("unknownSessionToken", "Jenny");
//      assertEquals("Error: Calling Username Deleted", dbResponse);
//      // Check that the user to be deleted isn't removed anyway
//      assertTrue(userAdmin.userExists("Jenny"));
//    }


    /* Test 29: Delete User (Exception Handling)
     * Description: Check that if the calling user does not have "EditUsers" permission that they are unable to
     * delete other users.
     * Expected Output: Username is not deleted in DB and returns string "Error: Insufficient User Permission"
     */
//    @Test
//    public void deleteUserInsufficientPermissions() {
//      // Test setup - Ensure the user to be deleted exists in DB
//      if (!userAdmin.userExists("Jenny")) {
//          userAdmin.createUser("sessionToken", "Jenny", {0,0,0,0}, "pass");
//      }
//      // Check return value
//      string dbResponse = userAdmin.deleteUser("basicToken", "Jenny");
//      assertEquals("Error: Insufficient User Permission", dbResponse);
//      // Check that the user to be deleted isn't removed anyway
//      assertTrue(userAdmin.userExists("Jenny"));
//    }

    /* Test 30: Delete User (Exception Handling)
     * Description: Check that if the username specified does not exist in database then, they should not be deleted
     * and instead an exception should be thrown.
     * Expected Output: Username is not deleted in DB and returns string "Error: Username Does Not Exist"
     */
//    @Test
//    public void deleteUserNoUsernameInDb() {
//      // Test setup - Ensure the user to be deleted does not exist in DB
//      if (userAdmin.userExists("unknownUser")) {
//          userAdmin.deleteUser("sessionToken", "unknownUser");
//      }
//      // Check return value
//      string dbResponse = userAdmin.deleteUser("sessionToken", "unknownUser");
//      assertEquals("Error: Username Does Not Exist", dbResponse);
//      // Check that the user to be deleted still doesn't exist
//      assertFalse(userAdmin.userExists("unknownUser"));
//    }


    /* Test 31: Delete User (Exception Handling)
     * Description: Check that if the username specified does not exist in database then, they should not be deleted
     * and instead an exception should be thrown.
     * Expected Output: Username is not deleted in DB and returns string "Error: Cannot Delete Yourself"
     */
//    @Test
//    public void deleteUserNoUsernameInDb() {
//      // Test setup - Ensure the user to be deleted exists in DB
//      if (!userAdmin.userExists("root")) {
//          userAdmin.createUser("sessionToken", "root");
//      }
//      // Check return value
//      string dbResponse = userAdmin.deleteUser("sessionToken", "root");
//      assertEquals("Error: Cannot Delete Yourself", dbResponse);
//      // Check that the user to be deleted still exists
//      assertTrue(userAdmin.userExists("root"));
//    }


    /* Test 32: Create User (Success)
     * Description: Check that the calling user has "EditUsers" permission, then create the corresponding username in
     * the DB with the hashed password and permissions and return acknowledgement to Control Panel.
     * Expected Output: User is created in the DB and returns string "Success: User Created"
     */
//    @Test(expected = Test.None.class /* no exception expected */)
//    public void createUser() {
//      // Test setup - Ensure the user to be created does not already exist
//      if (userAdmin.userExists("Jacinta")) {
//          userAdmin.deleteUser("sessionToken", "Jacinta");
//      }
//      // Check return value
//      String dbResponse = userAdmin.createUser("sessionToken", "Jacinta", {0,0,0,0}, "pass");
//      assertEquals("Success: User Created", dbResponse);
//      // Check that the user is actually added to the DB
//      assertTrue(userAdmin.userExists("Jacinta"));
//    }

    /* Test 33: Create User (Exception Handling)
     * Description: Check that the calling user exists and has not been deleted since attempt to call (check on submit)
     * Expected Output: Username is not created in DB and returns string "Error: Calling Username Deleted"
     */
//    @Test
//    public void createUserCallingUsernameDeleted() {
//      // Test setup - Ensure the user to be created does not already exist
//      if (userAdmin.userExists("non-existent")) {
//          userAdmin.deleteUser("sessionToken", "non-existent");
//      }
//      String dbResponse = unknownUserAdmin.createUser("unknownSessionToken", "Ra", {0,0,0,0}, "pass");
//      // Check return value
//      assertEquals("Error: Calling Username Deleted", dbResponse);
//      // Check that the user to be created is not added to the DB anyway
//      assertFalse(unknownUserAdmin.userExists("Ra"));
//    }

    /* Test 34: Create User (Exception Handling)
     * Description: Check that if the calling user does not have "EditUsers" permission that they are unable to
     * create other users.
     * Expected Output: Username is not created in DB and returns string "Error: Insufficient User Permission"
     */
//    @Test
//    public void createUserInsufficientPermissions() {
//      String dbResponse = userAdmin.createUser("basicToken", "DuplicateUser", {0,0,0,0}, "pass");
//      // Check return value
//      assertEquals("Error: Insufficient User Permission", dbResponse);
//      // Check that the user is not added to the DB anyway
//      assertFalse(userAdmin.userExists("Ra"));
//    }


    /* Test 35: Create User (Exception Handling)
     * Description: Check that if the desired username does not already exist in the DB (must be unique).
     * Expected Output: Username already exists in DB and returns string "Error: Username Already Taken"
     */
//    @Test
//    public void createUserDuplicateUsername() {
//      // Test Setup - Add the user to the DB if not already in existence
//      if (!userAdmin.userExists("DuplicateUser")) {
//          userAdmin.createUser("sessionToken", "DuplicateUser", {0,0,0,0}, "pass");
//      }
//      // Attempt to add duplicate username
//      String dbResponse = userAdmin.createUser("sessionToken", "DuplicateUser", {0,0,0,0}, "pass");
//      // Check return value
//      assertEquals("Error: Username Already Taken", dbResponse);
//    }

}