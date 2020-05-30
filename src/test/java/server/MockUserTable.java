package server;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import static controlPanel.UserControl.hash;
import static server.MockSessionTokens.*;
import static server.Server.*;
import static server.Server.Permission.EditUser;
import static server.Server.ServerAcknowledge.*;
import static server.UserAdmin.generateSaltString;


/**================================================================================================
 * UNIT TESTS USE THIS MOCK USER TABLE CLASS TO REMOVE SQL/SERVER DEPENDENCY
 ================================================================================================*/
class MockUserTable {
    private static HashMap<String, ArrayList<Object>> Internal = new HashMap<>();

    /**
     * Mocks the create user functionality to create a user given the user details. This method requires the EditUser
     * permission. It returns a server acknowledgement to indicate whether the creation was successful or if an
     * exception occurred.
     * <p>
     * @param sessionToken Session token from the calling user
     * @param username String username to be created
     * @param hashedPassword User provided hashed password from CP
     * @param createBillboard Boolean to indicate whether the user to be created has the createBillboard permission
     * @param editBillboard Boolean to indicate whether the user to be created has the editBillboard permission
     * @param scheduleBillboard Boolean to indicate whether the user to be created has the scheduleBillboard permission
     * @param editUser Boolean to indicate whether the user to be created has the editUser permission
     * @return ServerAcknowledge enum to indicate whether creation was successful or whether an exception occurred.
     * @throws NoSuchAlgorithmException exception is thrown if the hashing algorithm requested does not exist.
     */
    protected static ServerAcknowledge createUserTest(String sessionToken, String username, String hashedPassword,
                                                      boolean createBillboard, boolean editBillboard,
                                                      boolean scheduleBillboard, boolean editUser)
                                                                throws NoSuchAlgorithmException {
        String callingUsername = getUsernameFromTokenTest(sessionToken);
        System.out.println("Calling username is: " + callingUsername);
        if (hasPermissionTest(callingUsername, EditUser)) {
            System.out.println("Has permissions");
            // Add values to array list
            ServerAcknowledge mockResponse = addUserTest(username, hashedPassword, createBillboard, editBillboard, scheduleBillboard, editUser); // Update the table
            return mockResponse; // 1. PrimaryKeyClash or 2. Success
        } else {
            System.out.println("User has insufficient permissions to create user");
            return InsufficientPermission; // 3. Require EditUser Permission to perform this method
        }
    }


    /**
     * Mocks the user exists functionality to determine in a user exists in the MockUserTable it returns
     * a boolean to indicate if the user exists.
     * <p>
     * @param username String username of the user to be searched for
     * @return Boolean value to indicate that the user exists (true) or false otherwise.
     */
    protected static boolean userExistsTest(String username) {
        System.out.println("Mock table usernames: " + Internal.keySet());
        if (Internal.containsKey(username)) { // If username exists in db (case sensitivity and whitespace important)
            System.out.println("Mock table contains the username.");
            return true;
        }
        return false;
    }


    /**
     * Mocks the method to add a user (SQL) to the MockUserTable (hashtable). It returns a ServerAcknowledge
     * to indicate whether the creation event was successful. This method is for direct table access and does not
     * require a session token.
     * <p>
     * @param username String username to be created
     * @param hashedPassword User provided hashed password from CP
     * @param createBillboard Boolean to indicate whether the user to be created has the createBillboard permission
     * @param editBillboard Boolean to indicate whether the user to be created has the editBillboard permission
     * @param scheduleBillboard Boolean to indicate whether the user to be created has the scheduleBillboard permission
     * @param editUser Boolean to indicate whether the user to be created has the editUser permission
     * @return ServerAcknowledge enum to indicate whether creation was successful or if there was a PrimaryKeyClash.
     */
    protected static ServerAcknowledge addUserTest(String username, String hashedPassword, Boolean createBillboard, Boolean editBillboard, Boolean scheduleBillboard, Boolean editUser) throws NoSuchAlgorithmException {
        ServerAcknowledge dbResponse = PrimaryKeyClash; // Assume that there is a clash
        if (!Internal.containsKey(username)) { // If did not contain the username already, there would not be a clash
            System.out.println("Mock User Table did not contain " + username + " ...adding the user!");
            Internal.put(username, new ArrayList<>());
            dbResponse = Success; // Overwrite response
        }
        // Add values to the MockUserTable
        ArrayList<Object> values = new ArrayList();
        String dummySalt = "68b91e68f846f39f742b4e8e5155bd6ac5a4238b7fc4360becc02b064c006433";
        String dummyHashedSaltedPassword = hash(dummySalt + hashedPassword);
        values.add(dummyHashedSaltedPassword);
        values.add(dummySalt);
        values.add(createBillboard);
        values.add(editBillboard);
        values.add(scheduleBillboard);
        values.add(editUser);
        Internal.get(username).add(values);
        return dbResponse;
    }


    /**
     * Mocks the retrieval of a user from the MockUserTable.
     * <p>
     * @param username String username of the user to be searched for
     * @return ArrayList of of user data including the salted hashed password, salt, createBillboard, editBillboard,
     * scheduleBillboard and editUser permissions.
     */
    protected static ArrayList<Object> retrieveUserTest(String username) {
        return (ArrayList<Object>) Internal.get(username).get(0);
    }


    /**
     * Mocks the retrieval of users permissions from database and returnss an array list of booleans to indicate whether
     * the provided user has the permission.
     * <p>
     * @param username String username of the user's permissions to be retrieved from the database
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
     * Mocks the helper method to determine whether the retrieved user has the required permission
     * <p>
     * @param username String username of the user permissions to be checked
     * @param requiredPermission (either createBillboard, editBillboard, editSchedule, editUser) for the req. permission
     * @return Boolean value to indicate whether the requested user has the corresponding permission (true), or
     * false otherwise.
     */
    protected static boolean hasPermissionTest(String username, Permission requiredPermission) {
        // Retrieve the user's permissions from the MockUserTable
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
     * Mocks the delete user from MockUserTable functionality. Will delete the user associated with the provided
     * username. This method requires the EditUser permission. Returns a server acknowledgement for successful
     * deletion or if some other exception occurred.
     * <p>
     * @param sessionToken Session token from the calling user (permission checks)
     * @param username Username to be deleted from the MockUserTable
     * @return Server acknowledgement for Success or Exception Handling(InsufficientPermission, CannotDeleteSelf,
     * or NoSuchUser)
     */
    protected static ServerAcknowledge deleteUserTest(String sessionToken, String username) {
        String callingUsername = getUsernameFromTokenTest(sessionToken);
        if (!hasPermissionTest(callingUsername, EditUser)) { // Require edit users permission
            System.out.println("Insufficient permissions, no list of users was retrieved");
            return InsufficientPermission; // 1. Insufficient permission
        } else {
            // Delete user
            if (username.equals(getUsernameFromTokenTest(sessionToken))) {
                System.out.println("Username provided matches the calling user - cannot delete yourself.");
                return CannotDeleteSelf; // 2. Cannot Delete Self Exception - Sufficient permission
            } else if (userExistsTest(username)) {
                Internal.remove(username);
                System.out.println("Username was deleted: " + username);
                return Success; // 3. User Deleted - Valid user and sufficient permission
            } else {
                System.out.println("Requested user to be deleted does not exist, no user was deleted");
                return NoSuchUser; // 4. Requested user to be deleted does not exist in DB - Sufficient permission
            }
        }
    }


    /**
     * Mocks the list all the usernames from the MockUserTable functionality. This method requires the EditUser
     * permission. It returns a String array list of all of the usernames from the MockUserTable.
     * <p>
     * @param sessionToken The session token of the calling user.
     * @return A string Arraylist of the usernames of all users in the MockUserTable.
     */
    public static Object listUsersTest(String sessionToken) {
        String callingUsername = getUsernameFromTokenTest(sessionToken);
        if (!hasPermissionTest(callingUsername, EditUser)) { // Require edit users permission
            System.out.println("Insufficient permissions, no list of users was retrieved");
            return InsufficientPermission; // 1. Valid token but insufficient permission
        } else {
            System.out.println("Permission requirements were valid, list of users was retrieved");
            // Getting Set of keys from MockUserTable
            Set<String> keySet = Internal.keySet();
            // Creating an ArrayList of keys by passing the keySet
            ArrayList<String> listOfUsers = new ArrayList<>(keySet);
            return listOfUsers; // 2. Success, list of users returned
        }
    }

    /**
     * Mocks the setting of permissions of the corresponding user in the MockUserTable
     * <p>
     * @param sessionToken Session token from the calling user
     * @param username String username to be created
     * @param createBillboard Boolean to indicate whether the user to be created has the createBillboard permission
     * @param editBillboard Boolean to indicate whether the user to be created has the editBillboard permission
     * @param scheduleBillboard Boolean to indicate whether the user to be created has the scheduleBillboard permission
     * @param editUser Boolean to indicate whether the user to be created has the editUser permission
     * @return Server acknowledgement for Success or Exception Handling(InsufficientPermission,
     * CannotRemoveOwnAdminPermission, or NoSuchUser)
     */
    public static ServerAcknowledge setPermissionsTest(String sessionToken, String username, boolean createBillboard,
                                                       boolean editBillboard, boolean scheduleBillboard, boolean editUser) {
        String callingUsername = getUsernameFromTokenTest(sessionToken);
        if (!hasPermissionTest(callingUsername, EditUser)) {
            System.out.println("Calling user does not have EditUser permissions, no permissions were set");
            return InsufficientPermission; // 1. Valid token but insufficient permission
        }
        if (callingUsername.equals(username) && !editUser) {
            System.out.println("Session, permissions and username were valid, however cannot remove own edit users permission");
            return CannotRemoveOwnAdminPermission; // 2. Cannot remove own edit users permission
        } else {
            // Check user to be updated exists in the MockUserTable
            if (userExistsTest(username)) {
                // Update permissions in the MockUserTable
                ArrayList<Object> retrievedUser = retrieveUserTest(username);
                String hashedSaltedPassword = (String) retrievedUser.get(0);
                String salt = (String) retrievedUser.get(1);
                Internal.remove(username);
                Internal.put(username, new ArrayList<>());
                ArrayList<Object> values = new ArrayList();
                values.add(hashedSaltedPassword);
                values.add(salt);
                values.add(createBillboard);
                values.add(editBillboard);
                values.add(scheduleBillboard);
                values.add(editUser);
                Internal.get(username).add(values);
                System.out.println("Permission requirements were valid, permissions were set");
                return Success; // 3. Success, permissions returned
            } else {
                System.out.println("Requested user does not exist, permissions were not set");
                return NoSuchUser; // 4. Valid token and permissions, user requested does not exist
            }
        }
    }


    /**
     * Mocks the setting of password of the corresponding user in the MockUserTable
     * <p>
     * @param sessionToken Session token from the calling user
     * @param username String username of the user to be updated
     * @param hashedPassword User provided hashed password from CP
     * @return ServerAcknowledge enum to indicate whether creation was successful or whether an exceptions
     * (InsufficientPermission or NoSuchUser) occurred.
     * @throws NoSuchAlgorithmException This exception is thrown if the hashing algorithm for the password requested
     * does not exist.
     */
    public static ServerAcknowledge setPasswordTest(String sessionToken, String username, String hashedPassword) throws NoSuchAlgorithmException {
        String callingUsername = getUsernameFromTokenTest(sessionToken);
        if (!callingUsername.equals(username) & !hasPermissionTest(callingUsername, EditUser)) {
            System.out.println("Calling user does not have EditUser permission to set other user password - password was not updated");
            return InsufficientPermission; // 1. Insufficient permission
        }
        if (userExistsTest(username)) {
            // Store updated password in MockUserTable
            ArrayList<Object> retrievedUser = retrieveUserTest(username);
            Boolean createBillboard = (Boolean) retrievedUser.get(2);
            Boolean editBillboard = (Boolean) retrievedUser.get(3);
            Boolean editSchedule= (Boolean) retrievedUser.get(4);
            Boolean editUser = (Boolean) retrievedUser.get(5);
            Internal.remove(username);
            Internal.put(username, new ArrayList<>());
            ArrayList<Object> values = new ArrayList();
            String salt = generateSaltString();
            String hashedSaltedPassword = hash(hashedPassword + salt); // Generate new hashed password
            values.add(hashedSaltedPassword);
            values.add(salt);
            values.add(createBillboard);
            values.add(editBillboard);
            values.add(editSchedule);
            values.add(editUser);
            Internal.get(username).add(values);
            System.out.println("Permission requirements were valid - password was updated");
            return Success; // 2. Success
        } else {
            System.out.println("Requested user does not exist - password was not updated");
            return NoSuchUser; // 3. Valid token and permissions, user requested does not exist
        }
    }

}
