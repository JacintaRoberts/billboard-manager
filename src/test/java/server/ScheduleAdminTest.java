package server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

class ScheduleAdminTest {
    /* Test 0: Declaring ScheduleAdmin object
     * Description: ScheduleAdmin object should be running in background on application start.
     * Expected Output: ScheduleAdmin object is declared
     */
    ScheduleAdmin scheduleAdmin;

    /* Test 1: Constructing a ScheduleAdmin object
     * Description: ScheduleAdmin Object should be able to be created to handle requests from control panel
     * Expected Output: ScheduleAdmin object is instantiated from ScheduleAdmin class
     */
    @BeforeEach
    @Test
    public void setUpScheduleAdmin() {
        scheduleAdmin = new ScheduleAdmin();
    }

    /* Test 2: Declaring Database Mock object
     * Description: Database Mock simulates Maria DB for testing purposes.
     * Expected Output: DatabaseMock object for Schedule Table is declared
     */
    DatabaseMock<String, ArrayList<String>> scheduleTableMock;

    /* Test 3: Constructing a DatabaseMock object for Schedule Table
     * Description: DatabaseMock should be used to verify unit tests and setup of the DB
     * Expected Output: DatabaseMock object is instantiated to mimic Schedule Table
     */
    @BeforeEach
    @Test
    public void setUpScheduleTableMock() {
        scheduleTableMock = new DatabaseMock<>();
        // TODO: For date-time/time storage in MariaDB
        //  For start date (date-time) storage:
        //  Use this formatting: 'YYYY-MM-DD HH:MM:SS', e.g. 2007-11-30 10:30:19
        //  CREATE TABLE t2 (d DATETIME(6));
        //  INSERT INTO t2 VALUES ("2011-03-11"), ("2012-04-19 13:08:22");
        //  SELECT CONVERT('2007-11-30 10:30:19',datetime);
        //  For duration (time) storage:
        //  Use this formatting: 'HH:MM:SS.ssssss'
        //  INSERT INTO time VALUES ('90:00:00'), ('800:00:00'), (800), (22);
        //  SELECT CONVERT('800',time); // 8 mins
        //  This converts to: 90:00:00, 800:00:00, 00:08:00, 00:00:22 etc.
        //  Note: Min increment in duration is 1 minute so min acceptable value is "100" (entered in DB as string and received as time)
        //  This link is quite helpful https://mariadb.com/kb/en/datetime/
        // Note: duration minimum increment is 1 whole minute
        // Add a dummy value (insert more as necessary or create separate function to populate)
        // This example start will have a duration of 2 minutes that repeats every hour
        scheduleTableMock.addValue("2020-04-14 09:30:00", new ArrayList<String>( Arrays.asList("Billboard1", "200", "01:00:00")));
    }

    /* Test 4: Create Schedule in Schedule Table (Success)
     * Description: Receive create schedule request from CP, will require a session token, billboard name,
     *              start date, duration (mins) and repeats (daily, hourly, or every ___ mins)
     *              Assume sessionToken is valid.
     * Expected Output: A schedule is added to the table and returns "Pass: Billboard Scheduled"
     */
//    @Test
//    public void scheduleBillboard(){
//      // Ensure Billboard 1 Exists: billboardAdmin.createBillboard("sampleToken", "Billboard1", "xmlCode");
//      String dbResponse = scheduleAdmin.scheduleBillboard("sampleToken", "Billboard1",
//                                                                       "2020-04-14 05:00:00", "01:00:00", "24:00:00");
//      assertEquals(dbResponse, "Pass: Billboard Scheduled");
//    }


    /* Test 5: Create Schedule - Billboard Does Not Exist (Exception Handling)
     * Description: Receive create schedule request from CP, will require a session token, billboard name,
     *              start date and the duration (mins) and repeats (daily, hourly, or every ___ mins)
     *              Assume sessionToken is valid.
     *              This test is for when the billboard does not exist
     * Expected Output: A schedule is not added to the table and returns "Fail: Billboard does not Exist"
     */
//    @Test
//    public void scheduleBillboardNoBillboard(){
//      String dbResponse = scheduleAdmin.scheduleBillboard("sampleToken", "BillboardNOTEXISTS",
//                                                                       "2020-04-14 05:00:00", "01:00:00", "24:00:00");
//      assertEquals(dbResponse, "Fail: Billboard does not Exist");
//      // Check DB throws an SQL Exception for Billboard Name not found in Billboard Table (cross-checks for valid)
//      assertThrows(SQLException);
//    }


    /* Test 6: Create Schedule - Insufficient Permissions (Exception Handling)
     * Description: Receive create schedule request from CP, will require a session token, billboard name,
     *              start date and the duration (mins) and repeats (daily, hourly, or every ___ mins)
     *              Assume sessionToken is valid.
     *              This tests when the user does not have permission.
     * Expected Output: A schedule is not added to the table and returns "Fail: Insufficient User Permission"
     */
//    @Test
//    public void scheduleBillboardNoPermission(){
//      billboardAdmin.createBillboard("sampleToken", "Billboard1", xmlCode);
//      String dbResponse = scheduleAdmin.scheduleBillboard("basicToken", "Billboard1",
//                                                                       "2020-04-14 05:00:00", "01:00:00", "24:00:00");
//      assertEquals(dbResponse, "Fail: Insufficient User Permission");
//    }


    /* Test 7: Create Schedule - Duplicate Start DateTime (Exception Handling)
     * Description: Receive create schedule request from CP, will require a session token, billboard name,
     *              start date and the duration (mins) and repeats (daily, hourly, or every ___ mins)
     *              Assume sessionToken is valid.
     *              This tests when the there is a duplicate start date time (primary key error).
     * Expected Output: A schedule is not added to the table and returns "Fail: Duplicate Start Date Time"
     */
//    @Test
//    public void scheduleBillboardNoPermission(){
//      billboardAdmin.createBillboard("sampleToken", "Billboard1", xmlCode);
//      // There should already be a schedule with the same start date time in the Schedules table
//      String dbResponse = scheduleAdmin.scheduleBillboard("sampleToken", "Billboard1",
//                                                                       "2020-04-14 09:30:00", "200", "01:00:00");
//      assertEquals(dbResponse, "Fail: Duplicate Start Date Time");
//      // Check DB throws an SQL Exception for Duplicate Primary Key (Start Date)
//      assertThrows(SQLException);
//    }



}