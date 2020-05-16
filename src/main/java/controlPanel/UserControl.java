package controlPanel;


import helpers.Helpers;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static helpers.Helpers.bytesToString;
import static server.Server.*;

public class UserControl {

    /**
     * Send a user's request to log out to the server and expire the session token
     * @param sessionToken String session token that is created when the user initially logs in
     * @return String server acknowledgement as to whether logout was successful or an error occurred
     * @throws IOException Thrown if unknown server host when communicating through sockets
     * @throws ClassNotFoundException If the object received from the server is instantiated from a class that is not found
     */
    public static ServerAcknowledge logoutRequest(String sessionToken) throws IOException, ClassNotFoundException {
        String message = String.format("Logout,%s", sessionToken);
        return (ServerAcknowledge) Helpers.initClient(message); // Send constructed method request and parameters to the server
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
    public static Object loginRequest(String username, String passwordFromControlPanel) throws IOException,
            ClassNotFoundException, NoSuchAlgorithmException {
        String hashedPassword = hash(passwordFromControlPanel); // Hash password
        String message = String.format("Login,%s,%s", username, hashedPassword);
        return Helpers.initClient(message); // Send constructed method request and parameters to the server
    }

    /**
     * Sends a user's request to create a user to the server
     * Receives a string acknowledgement from the server if user creation is successful
     * @param sessionToken Session token for the current log in (contains calling user)
     * @param username String username of the user that is to be created
     * @param passwordFromControlPanel String password of the user that is entered upon starting the GUI.
     * @param createBillboard Boolean true if the user has the Create Billboards Permission
     * @param editBillboard Boolean true if the user has the Edit Billboards Permission
     * @param scheduleBillboard Boolean true if the user has the Schedule Billboards Permission
     * @param editUser Boolean true if the user has the Edit Users Permission
     * @return String server acknowledgement if user creation is successful, otherwise error message occurred.
     * @throws IOException Thrown if unknown server host when communicating through sockets.
     * @throws ClassNotFoundException If the object received from the server is instantiated from a class that is not found
     * @throws NoSuchAlgorithmException If the hashing algorithm does not exist
     */
    public static ServerAcknowledge createUserRequest(String sessionToken, String username, String passwordFromControlPanel, boolean createBillboard, boolean editBillboard, boolean scheduleBillboard, boolean editUser) throws IOException,
            ClassNotFoundException, NoSuchAlgorithmException {
        String hashedPassword = hash(passwordFromControlPanel); // Hash password entered by the user
        String message = String.format("User,CreateUser,%s,%s,%s,%s,%s,%s,%s", sessionToken, username, hashedPassword,
                                        createBillboard, editBillboard, scheduleBillboard, editUser);
        return (ServerAcknowledge) Helpers.initClient(message); // Send constructed method request and parameters to the server
    }


    /**
     * Sends a user's request to delete a user to the server
     * Receives a string acknowledgement from the server if user deletion is successful
     * @param sessionToken Session token for the current log in (contains calling user)
     * @param username String username of the user that is to be deleted
     * @return String server acknowledgement if user creation is successful, otherwise error message occurred.
     * @throws IOException Thrown if unknown server host when communicating through sockets.
     * @throws ClassNotFoundException If the object received from the server is instantiated from a class that is not found
     * @throws NoSuchAlgorithmException If the hashing algorithm does not exist
     */
    public static ServerAcknowledge deleteUserRequest(String sessionToken, String username) throws IOException, ClassNotFoundException {
        String message = String.format("User,DeleteUser,%s,%s", sessionToken, username);
        return (ServerAcknowledge) Helpers.initClient(message); // Send constructed method request and parameters to the server
    }



}
