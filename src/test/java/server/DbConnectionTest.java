package server;

import org.junit.jupiter.api.Test;

import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class DbConnectionTest {
    /* Test 0: Declaring Connection object
     * Description: Connection object should be declared to call subsequent tests on.
     * Expected Output: Connection object is declared
     */
    DbConnection instance;


    /* Test 1: Helper function - Read from database properties files (Success)
     * Description: Takes the file name to be read and returns the jdbcUrl, jdbcSchema, jdbcUsername and jdbcPassword
     * in an HashMap<String, String> to facilitate the connection to MariaDB.
     * Expected Output: Successfully return the url, schema, username and password from the database.props file
     * Implementation: very similar to Server.readProps except diff properties/return data structure and PRIVATE!
     */
    @Test
    public void readDbProps() throws IOException {

        // Set predetermined test cases
        String jdbcUrl = "jdbc:mysql://localhost:3306";
        String jdbcSchema = "BillboardDatabase";
        String jdbcUsername = "root";
        String jdbcPassword = "";

        // Initiate and run properties
        Properties props = DbConnection.readProperties("src\\test\\resources\\db.props");

        // Do Assertion to check if properties are read in or not
        assertAll("This should return the properties of the DBprops file",
                () -> assertEquals(jdbcUrl, props.getProperty("jdbc.url")),
                () -> assertEquals(jdbcSchema, props.getProperty("jdbc.schema")),
                () -> assertEquals(jdbcUsername, props.getProperty("jdbc.username")),
                () -> assertEquals(jdbcPassword, props.getProperty("jdbc.password"))
        );
    }


    /* Test 2: Helper function - Read properties from database properties files (error handling)
     * Description: Implement appropriate error handling for bad file name. This could also pick up:
     * reading a local file that was no longer available or trying to read file but don't have permission.
     * Expected Output: IOException from non-existent file name
     */
//    @Test
//    public void badDbPropsFile() {
//        assertThrows(java.io.FileNotFoundException.class, () -> {
//            instance.readProperties("unknown.props");
//        });
//    }


    /* Test 3: Connection of database server(Success)
     * Description: DBConnection Object should be instantiated to facilitate connection to MariaDb
     * Expected Output: DBConnection object is instantiated from DBConnection class
     * Expected Output: If server is not running - an exception will be thrown for failed connection
     * Use: instance = DriverManager.getConnection(url + "/" + schema, username, password);
     */
    @Test
    public void testDBConnection() throws IOException, SQLException {
        Connection connection = DbConnection.getInstance();
        assertTrue(connection!=null);
    }


    /* Test 4: Check for creation of tables and database (Success)
     * Description: Testing for SQL Script to see if it creates tables as specified even after deletion
     * Expected Output: 3 tables will be present within the database
     */
    @Test
    public void checkTableInDatabase() throws IOException, SQLException {

        // Set connection
        Connection connection = DbConnection.getInstance();

        // Sanity Check for connection
        if(connection!=null){

            // Create Statement to Check table information
            final String GET_TABLE_COUNT = "SELECT COUNT(DISTINCT `table_name`) " +
                    "FROM `information_schema`.`columns` "+
                    "WHERE `table_schema` = 'BillboardDatabase'";
            Statement st = null;

            // Try statement to verify table count
            try {
                // Create Statement
                st = connection.createStatement();

                // rename the table
                ResultSet result = st.executeQuery(GET_TABLE_COUNT);

//                // Store in variable for testing
//                instance.resultSetToArrayList(result);
//                DbConnection.storeContents(st,GET_TABLE_COUNT);

                // Display in Console
                DbConnection.displayContents(st,GET_TABLE_COUNT);

                // Close connection and statement
                st.close();
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        } else {
            System.out.println("Connection is Null");
        }

    }


    /* Test 5: Check that the Users table exists with correct columns (Success)
     * Description: Users table should have the following columns:
     * `Username` varchar(255)
     * `Password` varchar(255)
     * `Salt` varchar(255)
     * `CreateBillboard` bool
     * `EditBillboard` bool
     * `ScheduleBillboard` bool
     * `EditUser` bool
     * Expected Output: The Users table exists and has the columns specified
     */
    @Test
    public void checkUsersTable() throws IOException, SQLException {

        // Set SQL Query
        final String GET_USERS_COLS = "SHOW COLUMNS FROM users FROM billboarddatabase";

        // Set predetermined test cases
        String Username = "Username";
        String Password = "Password";
        String Salt = "Salt";
        String CreateBillboard = "CreateBillboard";
        String EditBillboard = "EditBillboard";
        String ScheduleBillboard = "ScheduleBillboard";
        String EditUser = "EditUser";

        // Set connection
        Connection connection = DbConnection.getInstance();

        // Set Statement Object
        Statement st = null;

        // Generate Java list
        List<String> verifyList = new ArrayList<>();

        // Try statement to verify table count
        try {
            st = connection.createStatement();

            // rename the table
            st.executeQuery(GET_USERS_COLS);

            // Display Contents in console
            DbConnection.displayContents(st,GET_USERS_COLS);

            // Storage for assertion Checks
            // get all current entries
            ResultSet rs = st.executeQuery(GET_USERS_COLS);
            // Get Column
            int columnCount = rs.getMetaData().getColumnCount();

            // output each row
            while (rs.next()) {
                verifyList.add(rs.getString(1));
            }

            // Close connection
            st.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        // Do Assertion to check if properties are read in or not
        assertAll("This should check if table columns are set properly",
                () -> assertEquals(Username, verifyList.get(0)),
                () -> assertEquals(Password, verifyList.get(1)),
                () -> assertEquals(Salt, verifyList.get(2)),
                () -> assertEquals(CreateBillboard, verifyList.get(3)),
                () -> assertEquals(EditBillboard, verifyList.get(4)),
                () -> assertEquals(ScheduleBillboard, verifyList.get(5)),
                () -> assertEquals(EditUser, verifyList.get(6))
        );

    }


    /* Test 6: Check that the Billboard table exists with correct columns (Success)
     * Description: Billboard table should have the following columns:
     * `BillboardName` varchar(255)
     * `Creator` varchar(255)
     * `XMLCode` bool
     * Expected Output: The Billboard table exists and has the columns specified
     */
    @Test
    public void checkBillboardsTable() throws IOException, SQLException {

        // Set SQL Query
        final String GET_BILLBOARDS_COLS = "SHOW COLUMNS FROM Billboards FROM billboarddatabase";

        // Set predetermined test cases
        String BillboardName = "BillboardName";
        String Creator = "Creator";
        String XMLCode = "XMLCode";

        // Set connection
        Connection connection = DbConnection.getInstance();

        // Set Statement Object
        Statement st = null;

        // Storage Variable
        ArrayList<DbBillboard> firstRow = null;

        // Generate Java list
        List<String> verifyList = new ArrayList<>();

        // Try statement to verify table count
        try {
            st = connection.createStatement();

            // rename the table
            st.executeQuery(GET_BILLBOARDS_COLS);

            // Display results
            DbConnection.displayContents(st,GET_BILLBOARDS_COLS);


            // Storage for assertion Checks
            // get all current entries
            ResultSet rs = st.executeQuery(GET_BILLBOARDS_COLS);
            // Get Column
            int columnCount = rs.getMetaData().getColumnCount();

            // output each row
            while (rs.next()) {
                verifyList.add(rs.getString(1));
            }

            // Close connection
            st.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        // Do Assertion to check if properties are read in or not
        assertAll("This should check if table columns are set properly",
                () -> assertEquals(BillboardName, verifyList.get(0)),
                () -> assertEquals(Creator, verifyList.get(1)),
                () -> assertEquals(XMLCode,verifyList.get(2))
        );

    }


    /* Test 7: Check that the Schedule table exists with correct columns (Success)
     * Description: Billboard table should have the following columns:
     * `StartDateTime ` datetime
     * `BillboardName` varchar(255)
     * `Duration` time
     * Expected Output: The Billboard table exists and has the columns specified
     */
    @Test
    public void checkScheduleTable() throws IOException, SQLException {

        // Set SQL Query
        final String GET_SCHEDULES_COLS = "SHOW COLUMNS FROM Schedules FROM billboarddatabase";

        // Set predetermined test cases
        String StartDateTime = "StartDateTime";
        String BillboardName = "BillboardName";
        String Duration = "Duration";

        // Set connection
        Connection connection = DbConnection.getInstance();

        // Set Statement Object
        Statement st = null;

        // Generate Java list
        List<String> verifyList = new ArrayList<>();

        // Try statement to verify table count
        try {
            st = connection.createStatement();

            // rename the table
            st.executeQuery(GET_SCHEDULES_COLS);

            // Display Content
            DbConnection.displayContents(st,GET_SCHEDULES_COLS);


            // Storage for assertion Checks
            // get all current entries
            ResultSet rs = st.executeQuery(GET_SCHEDULES_COLS);
            // Get Column
            int columnCount = rs.getMetaData().getColumnCount();

            // output each row
            while (rs.next()) {
                verifyList.add(rs.getString(1));
            }


            // Close connection
            st.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        // Do Assertion to check if properties are read in or not
        assertAll("This should check if table columns are set properly",
                () -> assertEquals(StartDateTime, verifyList.get(0)),
                () -> assertEquals(BillboardName, verifyList.get(1)),
                () -> assertEquals(Duration, verifyList.get(2))
        );

    }

    /* Test 8: Check for creation of initial user (Success)
     * Description: DBConnection Object should create initial user if there are none
     * Expected Output: If Users table initially empty should create initial user -> table should not be empty after
     */
//    @Test
//    public void initUser() {
//        // If Users table empty, init user
//        final String GET_ROW_COUNT =
//          "SELECT COUNT(*) FROM 'Users'";
//        int rowCount = instance.executeQuery(GET_ROW_COUNT);
//        if (rowCount == 0) {
//          instance.initUser(); // Remember to give god mode!
//          // Recalculate row count (should be at 1 now if originally empty)
//          rowCount = instance.executeQuery(GET_ROW_COUNT);
//        }
//        assertTrue(rowCount >= 1);
//    }

    /* Test 9: Deletion of DB connection (Success)
     * Description: DBConnection Object should be able to be closed
     * Expected Output: Close db connection
     */
//    @AfterAll
//    @Test
//    public static void tearDown() {
//        instance.close()
//    }


    @Test
    public void checkBillboardsTable2() throws IOException, SQLException {

        // Set SQL Query
        final String SHOW_BILLBOARD_CONTENT = "SELECT BillboardName FROM Billboards ";

        // Set predetermined test cases
        String BillboardName = "BillboardName";
        String Creator = "Creator";
        String XMLCode = "XMLCode";

        // Set connection
        Connection connection = DbConnection.getInstance();

        // Set Statement Object
        Statement st = null;

        // Storage Variable
        ArrayList<DbBillboard> firstRow = null;

        // Generate Java list
        List<String> verifyList = new ArrayList<>();

        // Try statement to verify table count
        try {
            st = connection.createStatement();

            // rename the table
            st.executeQuery(SHOW_BILLBOARD_CONTENT);

            // Display results
            DbConnection.displayContents(st,SHOW_BILLBOARD_CONTENT);

            // Close connection
            st.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    @Test
    public void testSQLRead() throws IOException, FileNotFoundException {
        InputStream is = new FileInputStream("setDatabase.sql");
        BufferedReader buf = new BufferedReader(new InputStreamReader(is));
        String line = buf.readLine();
        StringBuilder sb = new StringBuilder();
        while(line != null){ sb.append(line).append("\n");
            line = buf.readLine();
        }
        String fileAsString = sb.toString();
        System.out.println("Contents : " + fileAsString);

    }



}