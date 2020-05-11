package server;


import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;

import static controlPanel.UserControl.hash;

// SERVER SIDE USER ADMIN CONTROLS
public class UserAdmin {

    //TODO: FIX ME! I'M JUST A TEST FOR NOW...
    public static String addUser() {
        return "Pass: User Created";
    }

    /**
     * Checks if desired user exists in the database
     * @param username Checks if the desired username exists in the database
     * @return boolean true if they exist, boolean false if the user does not exist
     */
    public static boolean userExists(String username) throws IOException, SQLException {
        ArrayList<String> user = DbUser.retrieveUser(username);
        System.out.println("This was received"+ user.toString());
        if (!user.isEmpty()) { // If username exists in db
            System.out.println("User exists");
            return true;
        } else { // Username does not exist
            System.out.println("User does not exist");
            return false;
        }
    }


    /**
     * Checks if the hashed password entered from the CP matches the password stored in the database
     * @param username String username that the user typed from CP
     * @param hashedPassword User provided hashed password from CP
     * @return boolean true if the password matches, boolean false if password mismatch
     */
    public static boolean checkPassword(String username, String hashedPassword) throws IOException, SQLException, NoSuchAlgorithmException {
        ArrayList<String> user = DbUser.retrieveUser(username);
        System.out.println("This was received: " + user.toString());
        System.out.println("Hashed password from CP: " + hashedPassword);
        // TODO: Implement logic for salting, hashing etc. accessing database to check
        String saltString = user.get(2); // Retrieve salt from database
        String cpSaltedHashedPassword = hash(hashedPassword + saltString);
        String dbSaltedHashedPassword = user.get(1); // Get complete password from database
        System.out.println("Salted, hashed password from CP: " + cpSaltedHashedPassword);
        System.out.println("Salted, hashed password from DB: " + dbSaltedHashedPassword);
        if (dbSaltedHashedPassword.equals(cpSaltedHashedPassword)) { // If password matches the password in the database
            System.out.println("Password matches");
            return true;
        } else { // Password does not match
            System.out.println("Password mismatch");
            return false;
        }
    }



}




