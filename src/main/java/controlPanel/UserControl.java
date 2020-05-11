package controlPanel;


import helpers.Helpers;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static helpers.Helpers.bytesToString;

public class UserControl {

    /**
     * Send a user's request to log out to the server and expire the session token
     * @param sessionToken String session token that is created when the user initially logs in
     * @return String server acknowledgement as to whether logout was successful or an error occurred
     * @throws IOException Thrown if unknown server host when communicating through sockets
     * @throws ClassNotFoundException If the object received from the server is instantiated from a class that is not found
     */
    public static String cpLogout(String sessionToken) throws IOException, ClassNotFoundException {
        String message = "Logout," + sessionToken;
        return (String) Helpers.initClient(message); // Send constructed method request and parameters to the server
    }

    /**
     * Hashes the password that the user types in on the control panel on login to be sent to the server for validation.
     * @param password Input String password to be hashed
     * @return String representation of the hashed password
     * @throws NoSuchAlgorithmException - Thrown when the hashing algorithm requested does not exist.
     */
    public static String hash(String password) throws NoSuchAlgorithmException {
        // Initialise message digest using SHA-256 algorithm
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        String hashedPassword = bytesToString(md.digest(password.getBytes()));
        return hashedPassword;
    }

    /**
     * Sends a user's request to log in to the server
     * Hashes the password on the client-side before sending across for validation
     * Receives a string acknowledgement from the server and the session token is created if successful
     * @param username String username of the user that is entered upon starting the GUI.
     * @param passwordFromControlPanel String password of the user that is entered upon starting the GUI.
     * @return String session token if match found in database, otherwise string acknowledgement for an error occurred.
     * @throws IOException Thrown if unknown server host when communicating through sockets.
     * @throws ClassNotFoundException If the object received from the server is instantiated from a class that is not found
     * @throws NoSuchAlgorithmException If the hashing algorithm does not exist
     */
    public static String cpLogin(String username, String passwordFromControlPanel) throws IOException,
                                                                    ClassNotFoundException, NoSuchAlgorithmException {
        String hashedPassword = hash(passwordFromControlPanel); // Hash password
        String message = "Login," + username + "," + hashedPassword;
        return (String) Helpers.initClient(message); // Send constructed method request and parameters to the server
    }
}
