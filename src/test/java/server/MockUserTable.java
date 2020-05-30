package server;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

import static controlPanel.UserControl.deleteUserRequest;
import static controlPanel.UserControl.hash;
import static server.MockSessionTokens.getUsernameFromTokenTest;
import static server.MockSessionTokens.validateTokenTest;
import static server.Server.*;
import static server.Server.Permission.EditUser;
import static server.Server.ServerAcknowledge.*;


/**================================================================================================
 * UNIT TESTS USE THIS MOCK USER TABLE CLASS TO REMOVE SQL/SERVER DEPENDENCY
 ================================================================================================*/
class MockUserTable extends MockDatabase {
    private static HashMap<String, ArrayList<Object>> internal = new HashMap<>();

    /**
     * Mock create user for unit testing
     * @param sessionToken  Session token from the calling user
     * @param username String username to be created
     * @param hashedPassword User provided hashed password from CP
     * @param createBillboard Boolean to indicate whether the user to be created has the createBillboard permission
     * @param editBillboard Boolean to indicate whether the user to be created has the editBillboard permission
     * @param scheduleBillboard Boolean to indicate whether the user to be created has the scheduleBillboard permission
     * @param editUser Boolean to indicate whether the user to be created has the editUser permission
     * @return ServerAcknowledge enum to indicate whether creation was successful or whether an exception occurred.
     * @throws NoSuchAlgorithmException if the hashing algorithm requested does not exist
     */
    protected static ServerAcknowledge createUserTest(String sessionToken, String username, String hashedPassword, boolean createBillboard,
                                                   boolean editBillboard, boolean scheduleBillboard, boolean editUser) throws NoSuchAlgorithmException {
        String callingUsername = getUsernameFromTokenTest(sessionToken);
        System.out.println("Calling username is: " + callingUsername);
        if (hasPermissionTest(callingUsername, EditUser)) {
            System.out.println("Has permissions");
            // Add values to array list
            ServerAcknowledge mockResponse = addUserTest(username, hashedPassword, createBillboard, editBillboard, scheduleBillboard, editUser); // Update the table
            return mockResponse; // 1. PrimaryKeyClash or 2. Success
        } else {
            return InsufficientPermission; // 3. Require EditUser Permission to perform this method
        }
    }


    /**
     * Mock Method to See if a user exists in db
     * @return Boolean value to indicate that the user exists (true) or false otherwise.
     */
    protected static boolean userExistsTest(String username) {
        System.out.println("Mock table usernames: " + internal.keySet());
        if (internal.containsKey(username)) { // If username exists in db (case sensitivity and whitespace)
            System.out.println("Mock table contains the username.");
            return true;
        }
        return false;
    }


    /**
     * Mock method to add a user to the mock user hashtable
     * @return ServerAcknowledge for Success or PrimaryKeyClash
     */
    protected static ServerAcknowledge addUserTest(String username, String hashedPassword, Boolean createBillboard, Boolean editBillboard, Boolean scheduleBillboard, Boolean editUser) throws NoSuchAlgorithmException {
        ServerAcknowledge dbResponse = PrimaryKeyClash;
        if (!internal.containsKey(username)) { // If did not contain the username already, there would not be a clash
            System.out.println("Mock User Table did not contain " + username + " ...adding the user!");
            internal.put(username, new ArrayList<>());
            dbResponse = Success;
        }
        // Add values to the MockUserTable
        ArrayList<Object> values = new ArrayList();
        String dummySalt = "68b91e68f846f39f742b4e8e5155bd6ac5a4238b7fc4360becc02b064c006433";
        String dummyHashedSaltedPassword = hash(dummySalt + hashedPassword);// 10330629f1ddb57a41a9c41d19f0d30c53af983bcd7f1d582bdd203c7875b585";
        values.add(dummyHashedSaltedPassword);
        values.add(dummySalt);
        values.add(createBillboard);
        values.add(editBillboard);
        values.add(scheduleBillboard);
        values.add(editUser);
        internal.get(username).add(values);
        return dbResponse;
    }


    /**
     * Mocks retrieval of user from database
     * @param username to be fetched
     * @return ArrayList of User Information
     */
    private static ArrayList<Object> retrieveUserTest(String username) {
        return (ArrayList<Object>) internal.get(username).get(0);
    }


    /**
     * Retrieve view users permissions from database and to return it an array list of booleans
     * @param username Username's permissions to be retrieved from the database
     * @return userPermissions An ArrayList of size 4 that contains a boolean value for whether the requested user has
     * the corresponding permission (order is createBillboard, editBillboard, editSchedule, editUser)
     */
    protected static ArrayList<Boolean> retrieveUserPermissionsFromMockDbTest(String username) {
        ArrayList<Boolean> userPermissions = new ArrayList<>();
        ArrayList<Object> retrievedUser = null;
        try {
            retrievedUser = retrieveUserTest(username);
        } catch (NullPointerException e) {
            return userPermissions; // No user to be retrieved
        }
        System.out.println("Retrieved user details: " + retrievedUser);
        userPermissions.add(0, (Boolean) retrievedUser.get(2)); // Create billboard
        userPermissions.add(1, (Boolean) retrievedUser.get(3)); // Edit billboard
        userPermissions.add(2, (Boolean) retrievedUser.get(4)); // Edit schedule
        userPermissions.add(3, (Boolean) retrievedUser.get(5)); // Edit User
        System.out.println("Extracted user permissions: " + userPermissions);
        return userPermissions;
    }


    /**
     * Helper method to determine whether the retrieved user has the required permission
     */
    protected static boolean hasPermissionTest(String username, Permission requiredPermission) {
        ArrayList<Boolean> userPermissions = retrieveUserPermissionsFromMockDbTest(username);
        switch (requiredPermission) {
            case CreateBillboard:
                if (userPermissions.get(0)) return true;
                return false;
            case EditBillboard:
                if (userPermissions.get(1)) return true;
                return false;
            case ScheduleBillboard:
                if (userPermissions.get(2)) return true;
                return false;
            case EditUser:
                if (userPermissions.get(3)) return true;
                return false;
            default:
                return false; // Default to false if permission cannot be identified
        }
    }


    /**
     * Method to delete user from database
     * @param sessionToken Session token from the calling user
     * @param username Username to be deleted
     * @return Server acknowledgement for Success or Exception Handling
     */
    protected static ServerAcknowledge deleteUserTest(String sessionToken, String username) {
        // Delete user
        if (username.equals(getUsernameFromTokenTest(sessionToken))) {
            System.out.println("Username provided matches the calling user - cannot delete yourself.");
            return CannotDeleteSelf; // 1. Cannot Delete Self Exception - Valid token and sufficient permission
        } else if (userExistsTest(username)) {
            internal.remove(username);
            System.out.println("Username was deleted: " + username);
            return Success; // 2. User Deleted - Valid user, token and sufficient permission
        } else {
            System.out.println("Requested user to be deleted does not exist, no user was deleted");
            return NoSuchUser; // 3. Requested user to be deleted does not exist in DB - Valid token and sufficient permission
        }
    }
}
