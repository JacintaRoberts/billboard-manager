package server;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static helpers.Helpers.bytesToString;
import static server.MockUserTable.hasPermissionTest;
import static server.MockUserTable.retrieveUserPermissionsFromMockDbTest;
import static server.Server.Permission.EditUser;
import static server.Server.ServerAcknowledge.*;

class MockSessionTokens {
    private static HashMap<String, ArrayList<Object>> internalTokens = new HashMap<String, ArrayList<Object>>();

    /**
     * Mock method for unit testing generation of session tokens
     * @param username Username of the successful login
     * @return String session token
     */
    protected static String generateTokenTest(String username) {
        LocalDateTime creationTime = LocalDateTime.now(); // Generate current date time
        ArrayList<Object> values = new ArrayList<>();
        values.add(username);
        values.add(creationTime);
        // Generate session token key
        Random rng = new Random();
        byte[] sessionTokenBytes = new byte[32]; // Technically there is a very small chance the same token could be generated (primary key clash)
        rng.nextBytes(sessionTokenBytes);
        String sessionToken = bytesToString(sessionTokenBytes);
        internalTokens.put(sessionToken, values);
        internalTokens.get(sessionToken).add(values); // Add values to the mock session tokens
        System.out.println("Newly created token: " + internalTokens.get(sessionToken));
        return sessionToken;
    }


    /**
     * Mock method for unit testing validation of session tokens
     * @param sessionToken String session token to track successful user login
     * @return Boolean value to test if the session token is valid (true if valid, false otherwise)
     */
    protected static boolean validateTokenTest(String sessionToken) {
        String username = (String) internalTokens.get(sessionToken).get(0);
        System.out.println("Username fetched is : " + username);
        return internalTokens.containsKey(sessionToken); // Check if there is a valid session token for the existing user
    }


    /**
     * Retrieves the username of the provided session token
     * @param sessionToken to have the name retrieved from
     * @return String username stored with the session token
     */
    protected static String getUsernameFromTokenTest(String sessionToken) {
        return (String) internalTokens.get(sessionToken).get(0);
    }


    /**
     * Get user permissions as defined in assignment specification
     * to retrieve view users permissions and to return it an array list of booleans or as a server acknowledgement
     * If viewing your own details, no permission required
     * If viewing other's details, editUser permission required
     * @param sessionToken sessionToken of the calling user to determine whether Edit User permissions are required
     * @param username Username of the user details to be retrieved from the database
     * @return userPermissions - a boolean ArrayList of size 4 that indicates whether the requested user has
     * the corresponding permission (order is createBillboard, editBillboard, editSchedule, editUser) or an enum to indicate
     * insufficient permission to view requested user.
     */
    protected static Object mockGetPermissionsFromTokenTest(String sessionToken, String username) {
        String callingUsername = getUsernameFromTokenTest(sessionToken);
        // Require edit user permission if calling user trying to view others permissions
        if (!callingUsername.equals(username)) {
                if (!hasPermissionTest(callingUsername, EditUser)) {
                    System.out.println("Insufficient permissions, no permissions were retrieved");
                    return InsufficientPermission; // 1. Valid token but insufficient permission
                }
        } // Do not require permission to view own permissions
        System.out.println("Session and permission requirements were valid, permissions were retrieved for: " + username);
        return retrieveUserPermissionsFromMockDbTest(username); // 2. Success, permissions returned
    }

}
