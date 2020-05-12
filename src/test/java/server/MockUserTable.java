package server;

import java.util.ArrayList;
import java.util.HashMap;

import static server.MockSessionTokens.validateMockToken;

public class MockUserTable extends MockDatabase {
    private static HashMap<String, ArrayList<Object>> internal = new HashMap<>();


    // Fills with some initial test data
    public static void populateDummyData(String username, ArrayList<Object> values) {
        internal.put(username, new ArrayList<>());
        internal.get(username).add(values);
    }


    // Mock Method to See if a user exists in db
    public static boolean userExists(String username) {
        if (internal.containsKey(username)) { // If username exists in db (case sensitivity and whitespace)
            System.out.println(username + " was found in database.");
            return true;
        }
        System.out.println(username + " was not found in database.");
        return false;
    }


    // Mock method to add a user to the user table
    private String addUser(String username, ArrayList<Object> values) {
        String dbResponse = "Fail: Username Already Taken";
        if (!containsKey(username)) {
            internal.put(username, new ArrayList<>());
            dbResponse = "Pass: User Created";
        }
        internal.get(username).add(values);
        return dbResponse;
    }


    public String createUser(String sessionToken, String username, String hashedPassword, boolean createBillboard,
                             boolean editBillboard, boolean scheduleBillboard, boolean editUser) {
        // Check session
        if ( validateMockToken(sessionToken) ) {
            // Add values to array list
            ArrayList<Object> values = new ArrayList();
            String dummySalt = "68b91e68f846f39f742b4e8e5155bd6ac5a4238b7fc4360becc02b064c006433";
            String dummyHashedSaltedPassword = "10330629f1ddb57a41a9c41d19f0d30c53af983bcd7f1d582bdd203c7875b585";
            values.add(dummyHashedSaltedPassword);
            values.add(dummySalt);
            values.add(createBillboard);
            values.add(editBillboard);
            values.add(scheduleBillboard);
            values.add(editUser);
            String dbResponse = addUser(username, values); // Update the table
            return dbResponse; // Return server acknowledgement
        }
        return "Fail: Session Token"; // Return server acknowledgement
    }
}
