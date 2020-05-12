package server;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static helpers.Helpers.bytesToString;

public class MockSessionTokens {
    private static HashMap<String, ArrayList<Object>> internalTokens = new HashMap<String, ArrayList<Object>>();

    public static String generateMockToken(String username) {
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
        return sessionToken;
    }


    public static boolean validateMockToken(String sessionToken) {
        String username = (String) internalTokens.get(sessionToken).get(0);
        if (MockUserTable.userExists(username)) {
            System.out.println("Username exists");
            return internalTokens.containsKey(sessionToken); // Check if there is a valid session token for the existing user
        }
        System.out.println("Username does not exist");
        return false; // Return false as the user does not exist anymore
    }
}
