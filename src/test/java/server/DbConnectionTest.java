package server;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class DbConnectionTest {
    /* Test 0: Declaring Connection object
     * Description: Connection object should be declared to call subsequent tests on.
     * Expected Output: Connection object is declared
     */
    DbConnection instance;

    /* Test 1: Constructing a DbConnection Object to initialise the connection (success)
     * Description: DBConnection Object should be instantiated to facilitate connection to MariaDb
     * Expected Output: DBConnection object is instantiated from DBConnection class
     * NOTE: Good example on Blackboard as to how to do this :)
     * Use: instance = DriverManager.getConnection(url + "/" + schema, username, password);
     */
    @BeforeAll
    @Test
    public void setupDb() {
        HashMap<String, String> props = instance.readProps("src\\test\\resources\\db.props");
        instance = new DbConnection(props);
    }

    /* Test 2: Helper function - Read from database properties files (success)
     * Description: Takes the file name to be read and returns the jdbcUrl, jdbcSchema, jdbcUsername and jdbcPassword
     * in an HashMap<String, String> to facilitate the connection to MariaDB.
     * Expected Output: Successfully return the url, schema, username and password from the database.props file
     * Implementation: very similar to Server.readProps except diff properties/return data structure and PRIVATE!
     */
//    @Test
//    public void readDbProps() {
//        String jdbcUrl = "jdbc:mysql://localhost:3306";
//        String jdbcSchema = "cab302";
//        String jdbcUsername = "root";
//        String jdbcPassword = "";
//        // Happy to change the HashMap to something like String[] props = new String[4];, but thought it's a bit
//        // clearer to use key rather than numbers...could also use an enum (e.g. url vs "url") as the key.
//        HashMap<String, String> props = instance.readProps("src\\test\\resources\\db.props");
//        // Check Assertions
//        assertEquals(jdbcUrl, props.get("url"));
//        assertEquals(jdbcSchema, props.get("schema"));
//        assertEquals(jdbcUsername, props.get("username"));
//        assertEquals(jdbcPassword, props.get("password"));
//    }

    /* Test 3: Helper function - Read properties from database properties files (error handling)
     * Description: Implement appropriate error handling for bad file name. This could also pick up:
     * reading a local file that was no longer available or trying to read file but don't have permission.
     * Expected Output: IOException from non-existent file name
     */
//    @Test
//    public void badDbPropsFile() {
//        assertThrows(java.io.FileNotFoundException.class, () -> {
//            instance.readProps("unknown.props");
//        });
//    }

    /* Test 4: Check for creation of tables (success)
     * Description: DBConnection Object should create new tables in DB if they do not already exist
     * Expected Output: If db initially empty, should create 3 tables
     */
//    @Test
//    public void createTables() {
//        final String GET_TABLE_COUNT =
//        "SELECT COUNT(DISTINCT `table_name`) FROM `information_schema`.`columns`"
//          + "WHERE `table_schema` = 'cab302'";
//        // TODO: May want to edit this depending on where 'cab302' is actually used from the props
//        int tableCount = instance.executeQuery(GET_TABLE_COUNT);
//        if (tableCount == 0) {
//           instance.createTables();
//        }
//        // Check that the table count now equals 3
//        assertEquals(3,instance.executeQuery(GET_TABLE_COUNT));
//    }

    /* Test 5: Check that the Users table exists with correct columns (success)
     * Description: Users table should have 4 columns: Username (PK), HashedPassword, Salt, Permissions
     * Expected Output: The Users table exists and has the columns specified
     */
//    @Test
//    public void checkUsers() {
//        final String GET_USERS_COLS =
//        "SELECT * FROM `information_schema`.`columns` WHERE 'table_name' = 'Users'";
//        // TODO: May require tweaking (should we be persisting the salt?) and what is the database name if it's req.?
//        String[] usersColumns = instance.executeQuery(GET_USERS_COLS);
//        // Check for column name matches
//        assertTrue(usersColumns.contains("Username"));
//        assertTrue(usersColumns.contains("HashedPassword"));
//        assertTrue(usersColumns.contains("Salt"));
//        assertTrue(usersColumns.contains("Permissions"));
//    }

    /* Test 6: Check that the Billboards table exists with correct columns (success)
     * Description: Billboards table should have 3 columns: BillboardName (PK), Creator, BillboardXML
     * Expected Output: The Billboards table exists and has the columns specified
     */
//    @Test
//    public void checkUsers() {
//        final String GET_BILLBOARDS_COLS =
//        "SELECT * FROM `information_schema`.`columns` WHERE 'table_name' = 'Billboards'";
//        // TODO: May require tweaking (should we be persisting the salt?) and what is the database name if it's req.?
//        String[] billboardsColumns = instance.executeQuery(GET_BILLBOARDS_COLS);
//        // Check for column name matches
//        assertTrue(billboardsColumns.contains("BillboardName"));
//        assertTrue(billboardsColumns.contains("Creator"));
//        assertTrue(billboardsColumns.contains("BillboardXML"));
//    }

    /* Test 7: Check that the Schedules table exists with correct columns (success)
     * Description: Schedules table should have 4 columns: DateTime (PK), BillboardName, DurationOfDisplay, Repeats
     * Expected Output: The Schedules table exists and has the columns specified
     */
//    @Test
//    public void checkSchedules() {
//        final String GET_SCHEDULES_COLS =
//        "SELECT * FROM `information_schema`.`columns` WHERE 'table_name' = 'Schedules'";
//        // TODO: May require tweaking (should we be persisting the salt?) and what is the database name if it's req.?
//        String[] schedulesColumns = instance.executeQuery(GET_SCHEDULES_COLS);
//        // Check for column name matches
//        assertTrue(usersColumns.contains("DateTime"));
//        assertTrue(usersColumns.contains("BillboardName"));
//        assertTrue(usersColumns.contains("DurationOfDisplay"));
//        assertTrue(usersColumns.contains("Repeats"));
//    }

    /* Test 8: Check for creation of initial user (success)
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

    /* Test 9: Deletion of DB connection (success)
     * Description: DBConnection Object should be able to be closed
     * Expected Output: Close db connection
     */
//    @AfterAll
//    @Test
//    public static void tearDown() {
//        instance.close()
//    }


}