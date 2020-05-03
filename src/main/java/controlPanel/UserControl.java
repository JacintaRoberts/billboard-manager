package controlPanel;


import helpers.Helpers;

import java.io.IOException;

public class UserControl {

    /**
     * Send a user's request to log out to the server and expire the session token
     * @param sessionToken String session token that is created when the user initially logs in
     * @return String server acknowledgement as to whether logout was successful or an error occurred
     * @throws IOException Thrown if unknown server host when communicating through sockets
     * @throws ClassNotFoundException If the object received from the server is instantiated from a class that is not found
     */
    public static String logout(String sessionToken) throws IOException, ClassNotFoundException {
        String message = "Logout,"+sessionToken;
        return (String) Helpers.initClient(message);
    }

    /**
     * Send a user's request to log in to the server
     * Expected Output: Successful log in of the user, acknowledgement received and the session token is expired.
     * @param username String username of the user that is entered upon starting the GUI
     * @param hashedPassword String password of the user that is enter upon starting the GUI
     * @return String session token if match found in database, otherwise string acknowledgement for an error occurred
     * @throws IOException Thrown if unknown server host when communicating through sockets
     * @throws ClassNotFoundException If the object received from the server is instantiated from a class that is not found
     */
    public static String login(String username, String hashedPassword) throws IOException, ClassNotFoundException {
        //TODO: Add in password hashing
        String message = "Login,"+username+hashedPassword;
        return (String) Helpers.initClient(message);
    }
}
