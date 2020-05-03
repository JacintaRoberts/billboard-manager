package server;


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
    public static boolean userExists(String username) {
        //TODO: ALAN HELP ME WORK OUT THIS LOGIC WITH DB LOL
        if (username == "testUser") { // Logic: If username exists in db
            return true;
        } else {
            return false;
        }
    }
}




