package controlPanel;


import helpers.Helpers;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import static helpers.Helpers.bytesToString;
import static server.Server.*;

/**
 * The UserControl contains on the communication methods between the Control Panel to the server for User Methods. It includes methods
 * which will use functions from helpers that initialises communication and returns relevant object from server or server acknowledgements,
 * such as creating, editing, removing users.
 */

public class UserControl {

    /**
     * Send a user's request to log out to the server and expire the session token.
     * @param sessionToken A String which is the session token that is created when the user initially logs in.
     * @return Returns a ServerAcknowledge which is a message as to whether logout was successful or an error occurred.
     * @throws IOException Throws an exception if an I/O exception of some sort has occurred.
     * @throws ClassNotFoundException Throws an exception when a specified class cannot be found in the classpath.
     */
    public static ServerAcknowledge logoutRequest(String sessionToken) throws IOException, ClassNotFoundException {
        String message = String.format("Logout,%s", sessionToken);
        return (ServerAcknowledge) Helpers.initClient(message); // Send constructed method request and parameters to the server
    }


    /**
     * Hashes the password that the user types in on the control panel on login to be sent to the server for validation.
     * @param password A String which is the password to be hashed.
     * @return Returns a String which is the representation of the hashed password.
     * @throws NoSuchAlgorithmException Throws an exception when the hashing algorithm requested does not exist.
     */
    public static String hash(String password) throws NoSuchAlgorithmException {
        // Initialise message digest using SHA-256 algorithm
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        String hashedPassword = bytesToString(md.digest(password.getBytes()));
        return hashedPassword;
    }


    /**
     * Sends a user's request to log in to the server. Hashes the password on the client-side before sending across for
     * validation. Receives a string acknowledgement from the server and the session token is created if successful.
     * @param username A String which is the username of the user that is entered upon starting the GUI.
     * @param passwordFromControlPanel A String which is the password of the user that is entered upon starting the GUI.
     * @return Returns an Object which is the session token if match found in database, otherwise string acknowledgement for an error occurred.
     * @throws IOException Throws an exception if an I/O exception of some sort has occurred.
     * @throws ClassNotFoundException Throws an exception when a specified class cannot be found in the classpath.
     * @throws NoSuchAlgorithmException Throws an exception when the hashing algorithm requested does not exist.
     */
    public static Object loginRequest(String username, String passwordFromControlPanel) throws IOException,
            ClassNotFoundException, NoSuchAlgorithmException {
        String hashedPassword = hash(passwordFromControlPanel); // Hash password
        String message = String.format("Login,%s,%s", username, hashedPassword);
        return Helpers.initClient(message); // Send constructed method request and parameters to the server
    }


    /**
     * Sends a user's request to create a user to the server. Receives a string acknowledgement from the server if user
     * creation is successful.
     * @param sessionToken A String which is the session token for the current log in (contains calling user).
     * @param username A String which is the username of the user that is to be created.
     * @param passwordFromControlPanel A String password of the user that is entered upon starting the GUI.
     * @param createBillboard A Boolean which is true if the user has the Create Billboards Permission.
     * @param editBillboard A Boolean which is true if the user has the Edit Billboards Permission.
     * @param scheduleBillboard A Boolean which is true if the user has the Schedule Billboards Permission.
     * @param editUser A Boolean which is true if the user has the Edit Users Permission.
     * @return Returns a String server acknowledgement if user creation is successful, otherwise error message occurred.
     * @throws IOException Throws an exception if an I/O exception of some sort has occurred.
     * @throws ClassNotFoundException Throws an exception when a specified class cannot be found in the classpath.
     * @throws NoSuchAlgorithmException Throws an exception when the hashing algorithm requested does not exist.
     */
    public static ServerAcknowledge createUserRequest(String sessionToken, String username, String passwordFromControlPanel, boolean createBillboard, boolean editBillboard, boolean scheduleBillboard, boolean editUser) throws IOException,
            ClassNotFoundException, NoSuchAlgorithmException {
        String hashedPassword = hash(passwordFromControlPanel); // Hash password entered by the user
        String message = String.format("User,createUser,%s,%s,%s,%s,%s,%s,%s", sessionToken, username, hashedPassword,
                                        createBillboard, editBillboard, scheduleBillboard, editUser);
        return (ServerAcknowledge) Helpers.initClient(message); // Send constructed method request and parameters to the server
    }


    /**
     * Sends a user's request to delete a user to the server. Receives a string acknowledgement from the server
     * if user deletion is successful.
     * @param sessionToken A String which is the session token for the current log in (contains calling user).
     * @param username A String which is the username of the user that is to be deleted.
     * @return Returns a ServerAcknowledge which is a message if user deletion is successful, otherwise error message occurred.
     * @throws IOException Throws an exception if an I/O exception of some sort has occurred.
     * @throws ClassNotFoundException Throws an exception when a specified class cannot be found in the classpath.
     */
    public static ServerAcknowledge deleteUserRequest(String sessionToken, String username) throws IOException, ClassNotFoundException {
        String message = String.format("User,deleteUser,%s,%s", sessionToken, username);
        return (ServerAcknowledge) Helpers.initClient(message); // Send constructed method request and parameters to the server
    }


    /**
     * Sends a user's request to lists users to the server. Receives a string acknowledgement from the server if user
     * deletion is successful.
     * @param sessionToken A String which a session token for the current log in (contains calling user).
     * @return Returns an Object which is an ArrayList of all usernames, or ServerAcknowledge for error message occurred.
     * @throws IOException Throws an exception if an I/O exception of some sort has occurred.
     * @throws ClassNotFoundException Throws an exception when a specified class cannot be found in the classpath.
     */
    public static Object listUsersRequest(String sessionToken) throws IOException, ClassNotFoundException {
        String message = String.format("User,listUsers,%s", sessionToken);
        return Helpers.initClient(message); // Send constructed method request and parameters to the server
    }


    /**
     * Sends a user's request to get a user's permissions to the server. Receives a server acknowledgement from the
     * server if user permission retrieval failed, otherwise a boolean arrayList for whether the user has the
     * corresponding permission (order is createBillboard, editBillboard, editSchedule, editUser).
     * @param sessionToken A String which is a session token for the current log in (contains calling user).
     * @param username A String which is the username of the user's permissions to be retrieved.
     * @return Returns an Object which is an ArrayList(Boolean) for the user's permissions, or ServerAcknowledge for error message occurred.
     * @throws IOException Throws an exception if an I/O exception of some sort has occurred.
     * @throws ClassNotFoundException Throws an exception when a specified class cannot be found in the classpath.
     */
    public static Object getPermissionsRequest(String sessionToken, String username) throws IOException, ClassNotFoundException {
        String message = String.format("User,getPermissions,%s,%s", sessionToken, username);
        return Helpers.initClient(message); // Send constructed method request and parameters to the server
    }

    /**
     * Sends a user's request to set a password in the database to the server. Hashes the password before sending across
     * to the server. Receives a server acknowledgement from the server - Success or Failure acknowledgment.
     * @param sessionToken A String which is the session token for the current log in (contains calling user).
     * @param username A String which is the username of the user's password to be set.
     * @param password A String which is the new password to be set.
     * @return A ServerAcknowledge for success or error message.
     * @throws IOException Throws an exception if an I/O exception of some sort has occurred.
     * @throws ClassNotFoundException Throws an exception when a specified class cannot be found in the classpath.
     * @throws NoSuchAlgorithmException Throws an exception when the hashing algorithm requested does not exist.
     */
    public static ServerAcknowledge setPasswordRequest(String sessionToken, String username, String password) throws IOException, ClassNotFoundException, NoSuchAlgorithmException {
        String hashedPassword = hash(password);
        String message = String.format("User,setPassword,%s,%s,%s", sessionToken, username, hashedPassword);
        return (ServerAcknowledge) Helpers.initClient(message); // Send constructed method request and parameters to the server
    }


    /**
     * Sends a user's request to set a user's permissions in the database to the server. Receives a server acknowledgement
     * from the server - Success or Failure acknowledgment.
     * @param sessionToken A String which is a session token for the current log in (contains calling user).
     * @param username A String which is the username of the user's permissions to be set.
     * @param createBillboard A Boolean value to indicate whether the user has the createBillboards permission.
     * @param editBillboard A Boolean value to indicate whether the user has the editBillboards permission.
     * @param scheduleBillboard A Boolean value to indicate whether the user has the scheduleBillboards permission.
     * @param editUser A Boolean value to indicate whether the user has the editUsers permission.
     * @return A ServerAcknowledge for success or error message.
     * @throws IOException Throws an exception if an I/O exception of some sort has occurred.
     * @throws ClassNotFoundException Throws an exception when a specified class cannot be found in the classpath.
     */
    public static ServerAcknowledge setPermissionsRequest(String sessionToken, String username, Boolean createBillboard,
                                                          Boolean editBillboard, Boolean scheduleBillboard, Boolean editUser)
                                                    throws IOException, ClassNotFoundException {
        String message = String.format("User,setPermissions,%s,%s,%s,%s,%s,%s", sessionToken, username, createBillboard,
                                                                        editBillboard, scheduleBillboard, editUser);
        return (ServerAcknowledge) Helpers.initClient(message); // Send constructed method request and parameters to the server
    }


}
