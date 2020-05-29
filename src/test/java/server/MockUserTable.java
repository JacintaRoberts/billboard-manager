package server;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

import static controlPanel.UserControl.hash;
import static server.MockSessionTokens.validateTokenTest;
import static server.Server.*;
import static server.Server.ServerAcknowledge.*;


/**================================================================================================
 * UNIT TESTS USE THIS MOCK USER TABLE CLASS TO REMOVE SQL/SERVER DEPENDENCY
 ================================================================================================*/
public class MockUserTable extends MockDatabase {
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
    public static ServerAcknowledge createUserTest(String sessionToken, String username, String hashedPassword, boolean createBillboard,
                                                   boolean editBillboard, boolean scheduleBillboard, boolean editUser) throws NoSuchAlgorithmException {
        // Check session
        if ( validateTokenTest(sessionToken) ) {
            System.out.println("Session is valid");
            // Add values to array list
            ArrayList<Object> values = new ArrayList();
            String dummySalt = "68b91e68f846f39f742b4e8e5155bd6ac5a4238b7fc4360becc02b064c006433";
            String dummyHashedSaltedPassword = hash(dummySalt + hashedPassword);// 10330629f1ddb57a41a9c41d19f0d30c53af983bcd7f1d582bdd203c7875b585";
            values.add(dummyHashedSaltedPassword);
            values.add(dummySalt);
            values.add(createBillboard);
            values.add(editBillboard);
            values.add(scheduleBillboard);
            values.add(editUser);
            ServerAcknowledge dbResponse = addUserTest(username, values); // Update the table
            return dbResponse; // Return server acknowledgement - either PrimaryKeyClash or Success
        } else {
            System.out.println("Session was not valid");
            return InvalidToken; // Return for bad token server acknowledgement
        }
    }


    /**
     * Mock Method to See if a user exists in db
     * @return Boolean value to indicate that the user exists (true) or false otherwise.
     */
    public static boolean userExistsTest(String username) {
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
    private static ServerAcknowledge addUserTest(String username, ArrayList<Object> values) {
        ServerAcknowledge dbResponse = PrimaryKeyClash;
        if (!internal.containsKey(username)) { // If username did not contain the username already, there would not be a clash
            System.out.println("Mock User Table did not contain " + username + " ...adding the user!");
            internal.put(username, new ArrayList<>());
            dbResponse = Success;
        }
        internal.get(username).add(values); // Add values to the mock user table
        return dbResponse;
    }


}
