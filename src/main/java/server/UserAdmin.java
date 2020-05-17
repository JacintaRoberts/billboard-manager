package server;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Random;

import static controlPanel.UserControl.hash;
import static helpers.Helpers.bytesToString;
import static server.Server.*;
import static server.Server.Permission.EditUser;
import static server.Server.ServerAcknowledge.*;
import static server.Server.checkPermission;
import static server.Server.validateToken;

// SERVER SIDE USER ADMIN CONTROLS
public class UserAdmin {
    /**
     * Checks if desired user exists in the database
     * @param username Checks if the desired username exists in the database
     * @return boolean true if they exist, boolean false if the user does not exist
     */
    public static boolean userExists(String username) throws IOException, SQLException {
        ArrayList<String> user = DbUser.retrieveUser(username);
        System.out.println("This was received: " + user.toString());
        if (!user.isEmpty()) { // If username exists in db (case sensitivity and whitespace)
            System.out.println("User is not empty.");
            if (user.get(0).equals(username)) { // Username matches
                System.out.println("Username matches!");
                return true;
            }
        }
            System.out.println("The user: " + username + " does not exist");
            return false;
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

    /**
     * Creates User in the database
     * @param sessionToken
     * @param username
     * @param hashedPassword
     * @param createBillboard
     * @param editBillboard
     * @param scheduleBillboard
     * @param editUser
     * @return
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws SQLException
     */
    public static ServerAcknowledge createUser(String sessionToken, String username, String hashedPassword, boolean createBillboard,
                                                      boolean editBillboard, boolean scheduleBillboard, boolean editUser) throws NoSuchAlgorithmException, IOException, SQLException {
        if (validateToken(sessionToken)) {
            System.out.println("Session is valid");
            if (checkPermission(sessionToken, EditUser)) {
                try {
                    // Prepare parameters for storage in the database
                    Random rng = new Random();
                    byte[] saltBytes = new byte[32];
                    rng.nextBytes(saltBytes);
                    String saltString = bytesToString(saltBytes); // Generate salt
                    System.out.println("The salt string is: " + saltString);
                    String saltedHashedPassword = hash(hashedPassword + saltString); // Generate new hashed password
                    System.out.println("The salted, hashed password is: " + saltedHashedPassword);
                    DbUser.addUser(username, saltedHashedPassword, saltString, createBillboard,
                            editBillboard, scheduleBillboard, editUser);
                    return Success; // 1. User Created - Valid token, sufficient permission and valid user to create
                } catch (SQLIntegrityConstraintViolationException e) {
                    return PrimaryKeyClash; // 2. A user already exists with that username - primary key issue
                }
            } else {
                System.out.println("Permissions were not sufficient, no user was deleted");
                return InsufficientPermission; // 3. Valid token but insufficient permission
            }
        } else {
            System.out.println("Session was not valid");
            return InvalidToken; // 4. Invalid token
        }
    }


    /**
     * Method to delete user from database
     * @param sessionToken Session token from the calling user
     * @param username Username to be deleted
     * @return String server acknowledgement
     * @throws SQLException
     * @throws IOException
     */
    public static ServerAcknowledge deleteUser(String sessionToken, String username) throws SQLException, IOException {
        if ( validateToken(sessionToken) ) {
            System.out.println("Session is valid");
            if ( checkPermission(sessionToken, EditUser) ) { // Ensures the user has the edit user permission
                DbUser.deleteUser(username);
                System.out.println("Username was deleted: " + username);
                return Success; // 1. User Deleted - Valid token and sufficient permission
            } else {
                System.out.println("Permissions were not sufficient, no user was deleted");
                return InsufficientPermission; // 2. Valid token but insufficient permission
            }
        } else {
            System.out.println("Session was not valid, no user was deleted");
            return InvalidToken; // 3. Invalid token
        }
    }
}




