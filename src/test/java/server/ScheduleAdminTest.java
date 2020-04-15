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
//      // Check DB throws an SQL Exception for Billboard Name not found in Schedule table
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


    /* Test 8: Remove Schedule from Schedule Table (Success)
     * Description: Receive delete schedule request from CP, will require a session token, billboard name
     *              and start date
     *              Assume sessionToken is valid.
     * Expected Output: The schedule is removed to the table and returns "Pass: Billboard Schedule Removed"
     */
//    @Test
//    public void removeFromSchedule(){
//      String dbResponse = scheduleAdmin.removeFromSchedule("sessionToken", "Billboard1", "2020-04-14 09:30:00");
//      assertEquals(dbResponse, "Pass: Billboard Schedule Removed");
//    }


    /* Test 9: Remove Schedule - Insufficient Permissions (Exception Handling)
     * Description: Receive delete schedule request from CP, will require a session token, billboard name
     *              and start date
     *              Assume sessionToken is valid.
     *              This tests when the user has insufficient permissions
     * Expected Output: The schedule is not removed to the table and returns "Fail: Insufficient User Permission"
     */
//    @Test
//    public void removeFromScheduleNoPermission(){
//      String dbResponse = scheduleAdmin.removeFromSchedule("basicToken", "Billboard1", "2020-04-14 09:30:00");
//      assertEquals(dbResponse, "Fail: Insufficient User Permission");
//    }


    /* Test 10: Remove Schedule - Billboard Does Not Exist (Exception Handling)
     * Description: Receive delete schedule request from CP, will require a session token, billboard name
     *              and start date
     *              Assume sessionToken is valid.
     *              This tests for appropriate handling when the billboard does not exist
     * Expected Output: The schedule is not removed to the table and returns "Fail: Billboard Does Not Exist"
     */
//    @Test
//    public void removeFromScheduleNoBillboard(){
//      String dbResponse = scheduleAdmin.removeFromSchedule("sampleToken", "non-existent", "2020-04-14 09:30:00");
//      assertEquals(dbResponse, "Fail: Billboard Does Not Exist");
//    }


    /* Test 11: Remove Schedule - Schedule Does Not Exist (Exception Handling)
     * Description: Receive delete schedule request from CP, will require a session token, billboard name
     *              and start date
     *              Assume sessionToken is valid.
     *              This tests for appropriate handling when the schedule does not exist (deleted by someone else)
     * Expected Output: The schedule is not removed to the table and returns "Fail: Schedule Does Not Exist"
     */
//    @Test
//    public void removeFromScheduleAlreadyDeleted(){
//      // Ensure that the billboard exists but the scheduled time does not.
//      String dbResponse = scheduleAdmin.removeFromSchedule("sampleToken", "Billboard1", "1999-04-14 03:00:00");
//      assertEquals(dbResponse, "Fail: Schedule Does Not Exist");
//    }


    /* Test 12: View Schedule (All Billboards) (Success)
     * Description: Receive view schedule request from CP, will require a session token.
     *              Assume sessionToken is valid.
     * Expected Output: The schedule is retrieved as a string of schedules (billboard name, start time, duration)
     *                  and returns "Pass: Billboard List Returned"
     */
//    @Test
//    public void viewSchedule(){
//      billboardAdmin.createBillboard("sampleToken", "Billboard1", xmlCode);
//      scheduleAdmin.scheduleBillboard("sampleToken", "Billboard1", "2020-04-03 01:00:00", "01:00:00", "0");
//      billboardAdmin.createBillboard("sampleToken", "Billboard2", xmlCode);
//      scheduleAdmin.scheduleBillboard("sampleToken", "Billboard2", "2020-04-04 01:00:00", "01:00:00", "0");
//      BillboardSchedules billboardSchedules = scheduleAdmin.viewSchedule("sessionToken");
//      assertAll("Should return details of Given Billboard",
//                () -> assertEquals("Pass: Billboard List Returned", billboardSchedules.getServerResponse()),
//                () -> assertArrayEquals(String[] {"Billboard1","Billboard2"}, billboardSchedules.getBillboardName()),
//                () -> assertArrayEquals(String[] {"2020-04-03 01:00:00","2020-04-04 01:00:00"}, billboardSchedules.getBillboardStartDate()),
//                () -> assertArrayEquals(String[] {"01:00:00","01:00:00"}, billboardSchedules.getBillboardDuration()),
//      );
//    }


    /* Test 13: View Schedule (All Billboards) - No Schedule Exists (Exception Handling)
     * Description: Receive view schedule request from CP, will require a session token.
     *              Assume sessionToken is valid.
     *              Tests for when no schedule exists
     * Expected Output: The schedule is not retrieved and returns "Fail: No Schedule Exists"
     */
//    @Test
//    public void viewScheduleNoSchedule(){
//      // Remove default billboard for test set up
//      scheduleAdmin.removeFromSchedule("sessionToken", "Billboard1", "2020-04-14 09:30:00");
//      BillboardSchedules billboardSchedules = scheduleAdmin.viewSchedule("sessionToken");
//      assertEquals("Fail: No Schedule Exists", billboardSchedules.getServerResponse());
//      assertTrue(billboardSchedules.getBillboardName().length == 0);
//    }


    /* Test 14: View Schedule (All Billboards) - Insufficient Permissions (Exception Handling)
     * Description: Receive view schedule request from CP, will require a session token.
     *              Assume sessionToken is valid.
     *              Tests for insufficient permissions (req. Schedule Billboards Permission)
     * Expected Output: The schedule is not retrieved and returns "Fail: Insufficient User Permission"
     */
//    @Test
//    public void viewScheduleNoPermission(){
//      BillboardSchedules billboardSchedules = scheduleAdmin.viewSchedule("basicToken");
//      assertTrue(billboardScheduleInformation.getServerResponse() == "Fail: Billboard Does Not Exist");
//    }


//TODO: Discuss whether "END TIME" is needed to be returned
//TODO: Implement the tests for calculating which billboard needs to be displayed at a particular time
// (e.g. need to check every entry in the Schedules table for "repeats" field).

    /* Test 15: View Billboard Schedule (1 Specific Billboard) (Success)
     * Description: Receive view billboard schedule request from CP, will require a session token and returns specific
     *              billboard information.
     *              Assume sessionToken is valid.
     * Expected Output: The specific billboard schedule will be retrieved (Start Date, Duration, End Time, Repeats)
     *                  and returns "Pass: Billboard Schedule Returned"
     */
//    @Test
//    public void viewABillboardSchedule(String sessionToken, String billboard){
//      // Get default billboard information (schedule created with each test)
//      BillboardScheduleInformation billboardScheduleInformation = scheduleAdmin.viewBillboardSchedule("sessionToken",
//              "Billboard1");
//      assertAll("Should return details of Given Billboard",
//                () -> assertEquals("Pass: Billboard Schedule Returned", billboardScheduleInformation.getServerResponse()),
//                () -> assertEquals("2020-04-14 09:30:00", billboardScheduleInformation.getBillboardStartDate()),
//                () -> assertEquals("200", billboardScheduleInformation.getBillboardDuration()),
//                () -> assertEquals("2020-04-14 09:32:00", billboardScheduleInformation.getBillboardEndDate()),
//                () -> assertEquals("01:00:00", billboardScheduleInformation.getBillboardRepeat())
//        );
//    }


    /* Test 16: View Billboard Schedule (1 Specific Billboard) - No Schedule (Exception Handling)
     * Description: Receive view billboard schedule request from CP, will require a session token and returns specific
     *              billboard information.
     *              Assume sessionToken is valid.
     *              This tests for appropriate handling when no schedule is found
     * Expected Output: The specific billboard schedule will not be retrieved and returns "Fail: No Schedule Exists"
     */
//    @Test
//    public void viewABillboardSchedule(String sessionToken, String billboard){
//      // Remove default billboard for test set up
//      scheduleAdmin.removeFromSchedule("sessionToken", "Billboard1", "2020-04-14 09:30:00");
//      BillboardScheduleInformation billboardScheduleInformation = scheduleAdmin.viewBillboardSchedule("sessionToken",
//                "Billboard1");
//      assertTrue(billboardScheduleInformation.getServerResponse() == "Fail: No Schedule Exists");
//      assertTrue(billboardScheduleInformation.getBillboardName().length == 0);
//    }


    /* Test 17:  View Billboard Schedule (1 Specific Billboard) - Billboard Does Not Exist (Exception Handling)
     * Description: Receive view billboard schedule request from CP, will require a session token and returns specific
     *              billboard information.
     *              Assume sessionToken is valid.
     *              This tests for appropriate handling when no billboard requested is not found
     * Expected Output: The specific billboard schedule will not be retrieved and returns "Fail: Billboard Does Not Exist"
     */
//    @Test
//    public void viewABillboardSchedule(String sessionToken, String billboard){
//      BillboardScheduleInformation billboardScheduleInformation = scheduleAdmin.viewBillboardSchedule("sessionToken",
//                "Billboard1");
//      assertTrue(billboardScheduleInformation.getServerResponse() == "Fail: Billboard Does Not Exist");
//      assertThrows(NoBillboardException);
//    }


    /* Test 18: View Billboard Schedule (1 Specific Billboard) - Insufficient Permissions (Exception Handling)
     * Description: Receive view schedule request from CP, will require a session token.
     *              Assume sessionToken is valid.
     *              Tests for insufficient permissions (req. Schedule Billboards Permission)
     * Expected Output: The billboard's schedule is not retrieved and returns "Fail: Insufficient User Permission"
     */
//    @Test
//    public void viewABillboardScheduleNoPermission(){
//      BillboardSchedules billboardSchedules = scheduleAdmin.viewSchedule("basicToken");
//      assertTrue(billboardScheduleInformation.getServerResponse() == "Fail: Insufficient User Permission");
//    }

}