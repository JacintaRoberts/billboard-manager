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
            if (checkSinglePermission(sessionToken, EditUser)) {
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
     * @return String server acknowledgement - 5 are possible
     * @throws SQLException
     * @throws IOException
     */
    // TODO: THIS IS QUITE A MESSY METHOD AND THE IF STATEMENTS SHOULD BE CLEANED...
    public static ServerAcknowledge deleteUser(String sessionToken, String username) throws SQLException, IOException {
        if (validateToken(sessionToken)) {
            System.out.println("Session is valid");
            if (checkSinglePermission(sessionToken, EditUser)) { // Ensures the user has the edit user permission
                if (userExists(username)) { // Ensures that the user to be deleted
                    if (username.equals(getUsernameFromToken(sessionToken))) { // Ensures that you're not deleting yourself
                        System.out.println("Username provided matches the calling user - cannot delete yourself.");
                        return CannotDeleteSelf; // 1. Cannot Delete Self Exception - Valid token and sufficient permission
                    } else {
                        DbUser.deleteUser(username);
                        //TODO: EXPIRE ANY TOKENS WHERE THEY NAME WAS
                        ServerAcknowledge expirationMessage = Server.expireTokens(username);
                        System.out.println("Message from expiring session tokens: " + expirationMessage);
                        System.out.println("Username was deleted: " + username);
                        return Success; // 2. User Deleted - Valid user, token and sufficient permission
                    }
                } else {
                    System.out.println("Requested user to be deleted does not exist, no user was deleted");
                    return NoSuchUser; // 3. Requested user to be deleted does not exist in DB - Valid token and sufficient permission
                }
            } else {
                System.out.println("Permissions were not sufficient, no user was deleted");
                return InsufficientPermission; // 4. Valid token but insufficient permission
            }
        } else {
            System.out.println("Session was not valid, no user was deleted");
            return InvalidToken; // 5. Invalid token
        }
    }



    /**
     * Checks whether the provided username has the required permissions to invoke a particular function
     * @param sessionToken with the username to be checked
     * @param requiredPermission required permission to execute the server method
     * @return boolean true if the session token exists and the user has the required permission, false otherwise
     */
    public static boolean checkSinglePermission(String sessionToken, Permission requiredPermission) throws IOException, SQLException {
        String username = getUsernameFromToken(sessionToken); // Extract username from session token
        if (hasPermission(username, requiredPermission)) {
            System.out.println("The calling username " + username + " has the required permissions!");
            return true; // User has required permission
        }
        return false; // Return false as the user does not have the required permission
    }

    // Helper method to determine whether the retrieved user has the required permission
    private static boolean hasPermission(String username, Permission requiredPermission) throws IOException, SQLException {
        ArrayList<Boolean> userPermissions = retrieveUserPermissionsFromDb(username);
            switch (requiredPermission) {
                case CreateBillboard:
                    if (userPermissions.get(0)) return true;
                    return false;
                case EditBillboard:
                    if (userPermissions.get(1)) return true;
                    return false;
                case ScheduleBillboard:
                    if (userPermissions.get(2)) return true;
                    return false;
                case EditUser:
                    if (userPermissions.get(3)) return true;
                    return false;
                default:
                    return false; // Default to false if permission cannot be identified
            }
    }


    /**
     * Retrieve view users permissions from database and to return it an array list of booleans
     * @param username Username's permissions to be retrieved from the database
     * @return userPermissions An ArrayList of size 4 that contains a boolean value for whether the requested user has
     * the corresponding permission (order is createBillboard, editBillboard, editSchedule, editUser)
     */
    private static ArrayList<Boolean> retrieveUserPermissionsFromDb(String username) throws IOException, SQLException {
        ArrayList<Boolean> userPermissions = new ArrayList<>();
        ArrayList<String> retrievedUser = DbUser.retrieveUser(username);
        userPermissions.add(0, stringToBoolean(retrievedUser.get(3))); // Create billboard
        userPermissions.add(1, stringToBoolean(retrievedUser.get(4))); // Edit billboard
        userPermissions.add(2, stringToBoolean(retrievedUser.get(5))); // Edit schedule
        userPermissions.add(3, stringToBoolean(retrievedUser.get(6))); // Edit User
        return userPermissions;
    }


    /**
     * Get user permissions as defined in assignment specification
     * to retrieve view users permissions and to return it an array list of booleans or as a server acknowledgement
     * If viewing your own details, no permission required
     * If viewing other's details, editUser permission required
     * @param sessionToken sessionToken of the calling user to determine whether Edit User permissions are required
     * @param username Username of the user details to be retrieved from the database
     * @return userPermissions - a boolean ArrayList of size 4 that indicates whether the requested user has
     * the corresponding permission (order is createBillboard, editBillboard, editSchedule, editUser) or an enum to indicate
     * insufficient permission to view requested user.
     */
    public static Object getUserPermissions(String sessionToken, String username) throws IOException, SQLException {
        if (validateToken(sessionToken)) {
            String callingUsername = getUsernameFromToken(sessionToken);
            if (!callingUsername.equals(username)) {
                // Require edit user permission if calling user trying to view others permissions
                if (!hasPermission(callingUsername, EditUser)) {
                    System.out.println("Insufficient permissions, no permissions were retrieved");
                    return InsufficientPermission; // 1. Valid token but insufficient permission
                }
            } // Do not require permission
            if (userExists(username)) {
                System.out.println("Session and permission requirements were valid, permissions were retrieved");
                return retrieveUserPermissionsFromDb(username); // 2. Success, permissions returned
            } else {
                System.out.println("Session and permission requirements were valid, however request user does not exist");
                return NoSuchUser; // 3. Valid token and permissions, user requested does not exist
            }
        } else {
            System.out.println("Session was not valid, no permissions were retrieved");
            return InvalidToken; // 4. Invalid Token
        }
    }


    // Converts a string representation of an integer such as "0" or "1" to a boolean
    private static boolean stringToBoolean(String value) {
        if (value.equals("1")) {
            return true;
        }
        else {
            return false;
        }
    }


    /**
     * List all the users from the database
     * Requires the edit users permission
     * @param sessionToken the session token of the calling user
     * @return an array list of the usernames of all users in the database
     */
    public Object listUsers(String sessionToken) throws IOException, SQLException {
        if (validateToken(sessionToken)) {
            String callingUsername = getUsernameFromToken(sessionToken);
            if (!hasPermission(callingUsername, EditUser)) { // Require edit users permission
                System.out.println("Insufficient permissions, no list of users was retrieved");
                return InsufficientPermission; // 1. Valid token but insufficient permission
            } else {
                System.out.println("Session and permission requirements were valid, list of users was retrieved");
                return DbUser.listUsers(); // 2. Success, list of users returned
            }
        } else {
            System.out.println("Session was not valid, no list of users was retrieved");
            return InvalidToken; // 4. Invalid Token
        }
    }


    public ServerAcknowledge setUserPermissions(String sessionToken, String username, boolean createBillboards,
                                     boolean editBillboards, boolean editSchedules, boolean editUsers) throws IOException, SQLException {
        if (validateToken(sessionToken)) {
            String callingUsername = getUsernameFromToken(sessionToken);
            //if (!callingUsername.equals(username)) {
                // Require edit user permission if calling user trying to view others permissions
            if (!hasPermission(callingUsername, EditUser)) {
                System.out.println("Insufficient permissions, no permissions were retrieved");
                return InsufficientPermission; // 1. Valid token but insufficient permission
            }
            //} // Do not require permission
            if (userExists(username)) {
                //TODO: UPDATE DBUSER
                DbUser.updatePermissions(username, createBillboards, editBillboards, editSchedules, editUsers);
                System.out.println("Session and permission requirements were valid, permissions were set");
                return Success; // 2. Success, permissions returned
            } else {
                System.out.println("Session and permission requirements were valid, however request user does not exist");
                return NoSuchUser; // 3. Valid token and permissions, user requested does not exist
            }
        } else {
            System.out.println("Session was not valid, permissions were set");
            return InvalidToken; // 4. Invalid Token
        }
    }
}




