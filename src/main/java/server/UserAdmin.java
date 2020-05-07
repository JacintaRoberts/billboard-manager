package server;


import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

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
        //TODO: ALAN HELP ME WORK OUT THIS LOGIC WITH DB LOL
        ArrayList<String> user = DbUser.retrieveUser(username);
        System.out.println("This was received"+ user.toString());
        if (!user.isEmpty()) { // Logic: If username exists in db
            System.out.println("User exists");
            return true;
        } else {
            System.out.println("User does not exist");
            return false;
        }
    }

    /**
     * Checks if desired user exists in the database
     * @param username String username that the user typed from GUI
     * @param hashedPassword User provided password from GUI
     * @return boolean true if the password matches, boolean false if password mismatch
     */
    public static boolean checkPassword(String username, String hashedPassword) throws IOException, SQLException {
        ArrayList<String> user = DbUser.retrieveUser(username);
        System.out.println("This was received: " + user.toString());
        System.out.println("Your pass..." + hashedPassword);
        if (user.get(1).equals(hashedPassword)) { // Logic: If password matches the password in the database
            System.out.println("Password matches");
            return true;
        } else {
            System.out.println("Password mismatch");
            return false;
        }
    }

}




