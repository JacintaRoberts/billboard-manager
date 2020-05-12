package server;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class DbUser {


    // Set Fields for DbUser
    private String Username;
    private String Password;
    private String Salt;
    private String CreateBillboard;
    private String EditBillboard;
    private String ScheduleBillboard;
    private String EditUser;

    public static final String SELECT_USER_SQL = "SELECT * FROM users WHERE Username = ?";
    public static final String ADD_USER_SQL = "INSERT INTO users VALUES (?, ?, ?, ?, ?, ?, ?)";
    public static final String DELETE_USER_SQL = "DELETE FROM users WHERE Username = ?";
    private static Connection connection;
    private static PreparedStatement selectUser;
    private static PreparedStatement addUser;
    private static PreparedStatement deleteUser;

    public DbUser(String Username, String Password, String Salt, String CreateBillboard,
                  String EditBillboard, String ScheduleBillboard, String EditUser) {
        this.Username = Username;
        this.Password = Password;
        this.Salt = Salt;
        this.CreateBillboard = CreateBillboard;
        this.EditBillboard = EditBillboard;
        this.ScheduleBillboard = ScheduleBillboard;
        this.EditUser = EditUser;
    }


    /**
     * Stores Database Queries: Users. This is a generic method which stores any query sent to the database.
     * <p>
     * This method always returns immediately.
     * @param  st A Statement object which is the connection.createStatement()
     * @param  query A String which has the query fed into executeQuery
     * @throws IOException
     * @throws SQLException
     */
    public static ArrayList<DbUser> storeUserContents(Statement st, String query) throws IOException, SQLException {
        // Set List to store contents in
        ArrayList<DbUser> queryList = new ArrayList<>();

        // get all current entries
        ResultSet rs = st.executeQuery(query);

        // Loop through cursor
        while (rs.next()) {
            DbUser dbUser = new DbUser(rs.getString("Username"),
                    rs.getString("Password"),
                    rs.getString("Salt"),
                    rs.getString("CreateBillboard"),
                    rs.getString("EditBillboard"),
                    rs.getString("ScheduleBillboard"),
                    rs.getString("EditUser"));
            queryList.add(dbUser);
        }

        return queryList;
    }


    /**
     * Method to fetch a user from the database - feel free to change this just wanted it to work!
     * @return
     * @throws IOException
     * @throws SQLException
     */
    public static ArrayList<String> retrieveUser(String username) throws IOException, SQLException {
        connection = DbConnection.getInstance();
        selectUser = connection.prepareStatement(SELECT_USER_SQL);
        selectUser.setString(1, username);
        ResultSet rs = selectUser.executeQuery();
        // Use metadata to get the number of columns
        int columnCount = rs.getMetaData().getColumnCount();
        ArrayList<String> retrievedUser = new ArrayList<>();
        // Fetch each row
        while (rs.next()) {
            for (int i = 0; i < columnCount; i++) {
                String value = rs.getString(i + 1);
                retrievedUser.add(value);
                System.out.printf("%-20s",value);
            }
        }
        return retrievedUser;
    }


    /**
     * Method to put a user from the database - feel free to change this just wanted it to work!
     * @return
     * @throws IOException
     * @throws SQLException
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
     * Method to delete user from database
     * @param username String username that corresponds to the record to be deleted
     */
    public static void deleteUser(String username) throws SQLException, IOException {
        connection = DbConnection.getInstance();
        deleteUser = connection.prepareStatement(DELETE_USER_SQL);
        deleteUser.setString(1, username);
        ResultSet rs = deleteUser.executeQuery();
    }


    /**
     * Tidy up connections
     */
    private static void close() {
        try {
            selectUser.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }


}
