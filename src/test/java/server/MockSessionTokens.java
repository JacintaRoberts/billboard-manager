package server;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static helpers.Helpers.bytesToString;

public class MockSessionTokens {
    private static HashMap<String, ArrayList<Object>> internalTokens = new HashMap<String, ArrayList<Object>>();

    /**
     * Mock method for unit testing generation of session tokens
     * @param username Username of the successful login
     * @return String session token
     */
    public static String generateTokenTest(String username) {
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
        System.out.println("All session tokens: " + internalTokens.keySet());
        System.out.println("Newly created token: " + internalTokens.get(sessionToken));
        return sessionToken;
    }


    /**
     * Mock method for unit testing validation of session tokens
     * @param sessionToken String session token to track successful user login
     * @return Boolean value to test if the session token is valid (true if valid, false otherwise)
     */
    public static boolean validateTokenTest(String sessionToken) {
        String username = (String) internalTokens.get(sessionToken).get(0);
        System.out.println("Username fetched is : " + username);
        return internalTokens.containsKey(sessionToken); // Check if there is a valid session token for the existing user
    }
}
