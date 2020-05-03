package server;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DbUser {


    // Set Fields for DbUser
    private String Username;
    private String Password;
    private String CreateBillboard;
    private String EditBillboard;
    private String ScheduleBillboard;
    private String EditUser;

    public static final String SELECT_USER_SQL = "SELECT * FROM users WHERE Username = ?";
    private static Connection connection;
    private static PreparedStatement selectUser;

    public DbUser(String Username, String Password, String CreateBillboard) {
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
