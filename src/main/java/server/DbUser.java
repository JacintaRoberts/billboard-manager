package server;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

/**
 * The DbUser class contains methods when querying the User Table.
 * Methods querying, creation, editing and deletion of user from the user table. This class file only consists of the
 * raw sql queries with no checks.
 */


public class DbUser {

    public static final String SELECT_USER_SQL = "SELECT * FROM users WHERE Username = ?";
    public static final String COUNT_USER_SQL = "SELECT COUNT(*) FROM Users";
    public static final String ADD_USER_SQL = "INSERT INTO users VALUES (?, ?, ?, ?, ?, ?, ?)";
    public static final String DELETE_USER_SQL = "DELETE FROM users WHERE Username = ?";
    public static final String LIST_USERS_SQL = "SELECT Username FROM users;";
    public static final String UPDATE_PERMISSIONS_SQL = "UPDATE users SET CreateBillboard=?, EditBillboard=?, ScheduleBillboard=?, EditUser=? WHERE Username = ?";
    public static final String UPDATE_PASSWORD_SQL = "UPDATE users SET Password=?, Salt=? WHERE Username = ?";
    public static final String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS `BillboardDatabase`.`Users` (\n" +
            "      `Username` varchar(255) NOT NULL default '',\n" +
            "      `Password` varchar(255) NOT NULL,\n" +
            "      `Salt` varchar(255) NOT NULL,\n" +
            "      `CreateBillboard` bool NOT NULL default 0,\n" +
            "      `EditBillboard` bool NOT NULL default 0,\n" +
            "      `ScheduleBillboard` bool NOT NULL default 0,\n" +
            "      `EditUser` bool NOT NULL default 0,\n" +
            "      PRIMARY KEY (`Username`))";
    public static final String DROP_USER_TABLE = "DROP TABLE IF EXISTS `BillboardDatabase`.`Users`";

    private static Connection connection;
    private static PreparedStatement selectUser;
    private static PreparedStatement addUser;
    private static PreparedStatement deleteUser;
    private static PreparedStatement listUsers;
    private static PreparedStatement updatePermissions;
    private static PreparedStatement updatePassword;


    /**
     * Ensures that the ScheduleTable is created inside the database. The method should be called when the server is being
     * set up to ensure the Schedule Table exists if it is not in the database.
     * <p>
     * This method always returns immediately. It will either return a success message "Schedule Table Created", or further
     * information such as Schedule Table exists with/without data pre-populated inside
     * @return Returns a String which is a message that ensures Schedule Table exists within the Database.
     * @throws IOException Throws an exception if an I/O exception of some sort has occurred.
     * @throws SQLException Throws an exception if there is a database access error or other errors.
     */
    public static String createUserTable() throws IOException, SQLException {
        // Initialise Variable
        String resultMessage;
        // Set Connection
        connection = DbConnection.getInstance();
        Statement countUser = connection.createStatement();
        ResultSet rs = null;
        try {
            // Check if Table exists, and if Data exists
            rs = countUser.executeQuery(COUNT_USER_SQL);
            rs.next();
            String count = rs.getString(1);
            if (count.equals("0")){
                addUser("root", "a461ab9266dbbec4623de686f806a23e69337f524527e282bb325092159f0d87",
                        "8bca1326370a157d9c33acd5a173440d9475d3955ae559872f47cfe34aa793bd",
                        true, true, true, true);
                resultMessage = "User Table Exists and has No Data. Root has been added";
            } else {
                resultMessage = "User Table Exists and has Data";
            }
        } catch (SQLSyntaxErrorException throwables) {
            // If dosent exist, create table
            rs = countUser.executeQuery(CREATE_USER_TABLE);
            addUser("root", "a461ab9266dbbec4623de686f806a23e69337f524527e282bb325092159f0d87",
                    "8bca1326370a157d9c33acd5a173440d9475d3955ae559872f47cfe34aa793bd",
                    true, true, true, true);
            resultMessage = "User Table Created";
        }
        return resultMessage;
    }


    /**
     * Delete Table: User. This is a generic method which deletes the table.
     * <p>
     * This method always returns immediately.
     * @return Returns a String which returns a message that the table has been dropped.
     * @throws IOException Throws an exception if an I/O exception of some sort has occurred.
     * @throws SQLException Throws an exception if there is a database access error or other errors.
     */
    public static String dropUserTable() throws IOException, SQLException{
        String resultMessage;

        connection = DbConnection.getInstance();
        Statement dropUserTable = connection.createStatement();
        ResultSet rs = dropUserTable.executeQuery(DROP_USER_TABLE);
        resultMessage = "User Table Dropped";
        return resultMessage;
    }


    /**
     * Method to fetch a specific user's details from the database.
     * @param username A String which specifies the username.
     * @return Returns an ArrayList of Strings of the user details (username, hashedSaltedPassword, randomSalt, permissions).
     * @throws IOException Throws an exception if an I/O exception of some sort has occurred.
     * @throws SQLException Throws an exception if there is a database access error or other errors.
     */
    public static ArrayList<String> retrieveUser(String username) throws IOException, SQLException {
        ArrayList<String> retrievedUser = new ArrayList<>();
        try {
            connection = DbConnection.getInstance();
            selectUser = connection.prepareStatement(SELECT_USER_SQL);
            selectUser.setString(1, username);
            ResultSet rs = selectUser.executeQuery();
            // Use metadata to get the number of columns
            int columnCount = rs.getMetaData().getColumnCount();
            // Fetch each row
            while (rs.next()) {
                for (int i = 0; i < columnCount; i++) {
                    String value = rs.getString(i + 1);
                    retrievedUser.add(value);
                }
            }
            return retrievedUser; // populated
        } catch (SQLIntegrityConstraintViolationException err) {
            return retrievedUser; // empty
        }
    }


    /**
     * Method to add a user and the user permissions to the database.
     * @param username A String which specifies the username of the current user.
     * @param password A String which is the user's password.
     * @param randomSalt A String which is the salt so that the password can be salted.
     * @param createBillboard A Boolean which specifies if the user has the create billboards permission.
     * @param editBillboard A Boolean which specifies if the user has the edit billboards permission.
     * @param scheduleBillboard A Boolean which specifies if the user has the schedule billboards permission.
     * @param editUser A Boolean which specifies if the user has the edit users permission.
     * @throws IOException Throws an exception if an I/O exception of some sort has occurred.
     * @throws SQLException Throws an exception if there is a database access error or other errors.
     */
    public static void addUser(String username, String password, String randomSalt, boolean createBillboard,
                               boolean editBillboard, boolean scheduleBillboard, boolean editUser) throws IOException, SQLException {
        connection = DbConnection.getInstance();
        addUser = connection.prepareStatement(ADD_USER_SQL);
        addUser.setString(1, username);
        addUser.setString(2, password);
        addUser.setString(3, randomSalt);
        addUser.setBoolean(4, createBillboard);
        addUser.setBoolean(5, editBillboard);
        addUser.setBoolean(6, scheduleBillboard);
        addUser.setBoolean(7, editUser);
        ResultSet rs = addUser.executeQuery();
    }


    /**
     * Method to delete a user from database.
     * @param username A String which is the username that corresponds to the record to be deleted.
     * @throws SQLException Throws an exception if there is a database access error or other errors.
     * @throws IOException Throws an exception if an I/O exception of some sort has occurred.
     */
    public static void deleteUser(String username) throws SQLException, IOException {
        connection = DbConnection.getInstance();
        deleteUser = connection.prepareStatement(DELETE_USER_SQL);
        deleteUser.setString(1, username);
        ResultSet rs = deleteUser.executeQuery();
    }


    /**
     * Method to fetch a list of users from the database.
     * @return An ArrayList(String) of all the usernames in the database.
     * @throws IOException Throws an exception if an I/O exception of some sort has occurred.
     * @throws SQLException Throws an exception if there is a database access error or other errors.
     */
    public static ArrayList<String> listUsers() throws IOException, SQLException {
        ArrayList<String> usernameList = new ArrayList<>();
        try {
            connection = DbConnection.getInstance();
            listUsers = connection.prepareStatement(LIST_USERS_SQL);
            ResultSet rs = listUsers.executeQuery();
            int rowCount = 0;
            // Fetch each row
            while (rs.next()) {
                rowCount++;
                String value = rs.getString("username");
                usernameList.add(value);
            }
            System.out.println(rowCount + " users were found in the database");
            return usernameList;
        } catch (SQLIntegrityConstraintViolationException err) {
            return usernameList;
        }
    }


    /**
     * Method to update a user's permissions in the databases.
     * @param username A String which specifies the username of the current user.
     * @param createBillboard A Boolean which specifies if the user has the create billboards permission.
     * @param editBillboard A Boolean which specifies if the user has the edit billboards permission.
     * @param scheduleBillboard A Boolean which specifies if the user has the schedule billboards permission.
     * @param editUser A Boolean which specifies if the user has the edit users permission.
     * @throws SQLException Throws an exception if there is a database access error or other errors.
     * @throws IOException Throws an exception if an I/O exception of some sort has occurred.
     */
    public static void updatePermissions(String username, Boolean createBillboard, Boolean editBillboard,
                                         Boolean scheduleBillboard, Boolean editUser) throws SQLException, IOException {
        connection = DbConnection.getInstance();
        updatePermissions = connection.prepareStatement(UPDATE_PERMISSIONS_SQL);
        updatePermissions.setBoolean(1, createBillboard);
        updatePermissions.setBoolean(2, editBillboard);
        updatePermissions.setBoolean(3, scheduleBillboard);
        updatePermissions.setBoolean(4, editUser);
        updatePermissions.setString(5, username);
        ResultSet rs = updatePermissions.executeQuery();
    }


    /**
     * Method to update a user's password in the database.
     * @param username A String which specifies the username of the current user.
     * @param password A String which is the user's password.
     * @param randomSalt A String which is the salt so that the password can be salted.
     * @throws IOException Throws an exception if an I/O exception of some sort has occurred.
     * @throws SQLException Throws an exception if there is a database access error or other errors.
     */
    public static void updatePassword(String username, String password, String randomSalt) throws IOException, SQLException {
        connection = DbConnection.getInstance();
        updatePassword = connection.prepareStatement(UPDATE_PASSWORD_SQL);
        updatePassword.setString(1, password);
        updatePassword.setString(2, randomSalt);
        updatePassword.setString(3, username);
        ResultSet rs = updatePassword.executeQuery();
    }



}