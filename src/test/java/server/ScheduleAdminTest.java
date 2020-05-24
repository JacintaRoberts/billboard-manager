package server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
    MockDatabase<String, ArrayList<String>> scheduleTableMock;

    /* Test 3: Constructing a DatabaseMock object for Schedule Table
     * Description: DatabaseMock should be used to verify unit tests and setup of the DB
     * Expected Output: DatabaseMock object is instantiated to mimic Schedule Table
     */
    @BeforeEach
    @Test
    public void setUpScheduleTableMock() {
        scheduleTableMock = new MockDatabase<>();
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
     *              Date time is assumed to be already formatted from the control panel.
     * Expected Output: A schedule is added to the table and returns "Pass: Billboard Scheduled"
     */
    @Test
    public void scheduleBillboardData() throws IOException, SQLException {
        scheduleAdmin.deleteAllSchedules();
        BillboardAdmin.createBillboard("TestUser","ScheduledBillboard","testXML");
      String dbResponse = scheduleAdmin.createSchedule("ScheduledBillboard",
              "05:00", "30", "2020-05-18 12:55", "120",
              "0","0","1","1","0","0","0");
      assertEquals(dbResponse, "Pass: Billboard Scheduled");
    }


    /* Test 5: Create Schedule - Billboard Does Not Exist (Exception Handling)
     * Description: Receive create schedule request from CP, will require a session token, billboard name,
     *              start date and the duration (mins) and repeats (daily, hourly, or every ___ mins)
     *              Assume sessionToken is valid.
     *              This test is for when the billboard does not exist
     * Expected Output: A schedule is not added to the table and returns "Fail: Billboard does not Exist"
     */
    @Test
    public void scheduleBillboardNoBillboard() throws IOException, SQLException {
        scheduleAdmin.deleteAllSchedules();
        String dbResponse = scheduleAdmin.createSchedule("BADBILLBOARDYOUDONTBELONGHERE",
                "05:00", "30", "2020-05-18 12:55", "120",
                "0","0","1","1","0","0","0");
      assertEquals(dbResponse, "Fail: Billboard does not Exist");
    }


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


    /* Test 7: Create Schedule - Duplicate Billboard Schedules (Exception Handling)
     * Description: Receive create schedule request from CP, will require a session token, billboard name,
     *              start date and the duration (mins) and repeats (daily, hourly, or every ___ mins)
     *              Assume sessionToken is valid.
     *              This tests when the there is a duplicate billboard (primary key error).
     * Expected Output: A schedule is not added to the table and returns "Fail: Duplicate Start Date Time"
     */
    @Test
    public void scheduleBillboardDuplicateSchedule() throws IOException, SQLException {
        scheduleAdmin.deleteAllSchedules();
        BillboardAdmin.createBillboard("TestUser","ScheduledBillboard","testXML");
        scheduleAdmin.createSchedule("ScheduledBillboard",
                "05:00", "30", "2020-05-18 12:55", "120",
                "0","0","1","1","0","0","0");
        String dbResponse = scheduleAdmin.createSchedule("ScheduledBillboard",
                "05:00", "30", "2020-05-18 12:55", "120",
                "0","0","1","1","0","0","0");
        assertEquals(dbResponse, "Fail: Schedule Already Exists");
    }


    /* Test 8: Remove Schedule from Schedule Table (Success)
     * Description: Receive delete schedule request from CP, will require a session token, billboard name
     *              and start date
     *              Assume sessionToken is valid.
     * Expected Output: The schedule is removed to the table and returns "Pass: Billboard Schedule Removed"
     */
    @Test
    public void removeFromSchedule() throws IOException, SQLException {
        scheduleAdmin.deleteAllSchedules();
        BillboardAdmin.createBillboard("TestUser","TestDeleteBillboard","testXML");
        scheduleAdmin.createSchedule("TestDeleteBillboard",
                "05:00", "30", "2020-05-18 12:55", "120",
                "0","0","1","1","0","0","0");
      String dbResponse = scheduleAdmin.deleteSchedule("TestDeleteBillboard");
      assertEquals(dbResponse, "Pass: Billboard Schedule Deleted");
    }


    /* Test 9: Remove Schedule - Insufficient Permissions
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


    /* Test 10: Remove Schedule - Billboard Does Not Exist
     * Description: Receive delete schedule request from CP, will require a session token, billboard name
     *              and start date
     *              Assume sessionToken is valid.
     *              This tests for appropriate handling when the billboard does not exist
     * Expected Output: The schedule is not removed to the table and returns "Fail: Billboard Does Not Exist"
     */
    @Test
    public void removeFromScheduleNoBillboard() throws IOException, SQLException {
        scheduleAdmin.deleteAllSchedules();
        String dbResponse = scheduleAdmin.deleteSchedule("nonexistentBillboard");
      assertEquals(dbResponse, "Fail: Billboard Does Not Exist");
    }


    /* Test 11: Remove Schedule - Schedule Does Not Exist
     * Description: Receive delete schedule request from CP, will require a session token, billboard name
     *              and start date
     *              Assume sessionToken is valid.
     *              This tests for appropriate handling when the schedule does not exist (deleted by someone else)
     * Expected Output: The schedule is not removed to the table and returns "Fail: Schedule Does Not Exist"
     */
    @Test
    public void removeFromScheduleAlreadyDeleted() throws IOException, SQLException {
      scheduleAdmin.deleteAllSchedules();
      BillboardAdmin.createBillboard("TestUser","NoSchedBillboard","testXML");
      String dbResponse = scheduleAdmin.deleteSchedule("NoSchedBillboard");
      assertEquals(dbResponse, "Fail: Billboard Schedule Does not Exist");
    }


    /* Test 12: View Schedule (All Billboards) (Success)
     * Description: Receive view schedule request from CP, will require a session token.
     *              Assume sessionToken is valid.
     * Expected Output: The schedule is retrieved as a string of schedules (billboard name, start time, duration)
     *                  and returns "Pass: Billboard List Returned"
     */
    @Test
    public void viewSchedule() throws IOException, SQLException {
      // Cleaning
      scheduleAdmin.deleteAllSchedules();
      BillboardAdmin.deleteAllBillboard();


      // Set test cases
      List<String> ExpectedBillboardList= new ArrayList<String>();
      ExpectedBillboardList.add("ScheduledBillboard1");
      ExpectedBillboardList.add("ScheduledBillboard2");
      ExpectedBillboardList.add("ScheduledBillboard3");
      List<String> ExpectedStartTimeList= new ArrayList<String>();
      ExpectedStartTimeList.add("05:00:00");
      ExpectedStartTimeList.add("06:00:00");
      ExpectedStartTimeList.add("13:00:00");
      List<String> ExpectedDurationList= new ArrayList<String>();
      ExpectedDurationList.add("30");
      ExpectedDurationList.add("20");
      ExpectedDurationList.add("50");
      List<String> ExpectedCreationDateTimeList = new ArrayList<String>();
      ExpectedCreationDateTimeList.add("2020-05-18 12:55:00.0");
      ExpectedCreationDateTimeList.add("2020-05-18 13:55:00.0");
      ExpectedCreationDateTimeList.add("2020-05-20 15:55:00.0");
      List<String> ExpectedRepeatList = new ArrayList<String>();
      ExpectedRepeatList.add("120");
      ExpectedRepeatList.add("40");
      ExpectedRepeatList.add("0");
      List<String> ExpectedSundayList = new ArrayList<String>();
      ExpectedSundayList.add("0");
      ExpectedSundayList.add("0");
      ExpectedSundayList.add("0");
      List<String> ExpectedMondayList = new ArrayList<String>();
      ExpectedMondayList.add("0");
      ExpectedMondayList.add("1");
      ExpectedMondayList.add("0");
      List<String> ExpectedTuesdayList = new ArrayList<String>();
      ExpectedTuesdayList.add("1");
      ExpectedTuesdayList.add("1");
      ExpectedTuesdayList.add("0");
      List<String> ExpectedWednesdayList = new ArrayList<String>();
      ExpectedWednesdayList.add("1");
      ExpectedWednesdayList.add("1");
      ExpectedWednesdayList.add("0");
      List<String> ExpectedThursdayList = new ArrayList<String>();
      ExpectedThursdayList.add("0");
      ExpectedThursdayList.add("1");
      ExpectedThursdayList.add("0");
      List<String> ExpectedFridayList = new ArrayList<String>();
      ExpectedFridayList.add("0");
      ExpectedFridayList.add("1");
      ExpectedFridayList.add("0");
      List<String> ExpectedSaturdayList = new ArrayList<String>();
      ExpectedSaturdayList.add("0");
      ExpectedSaturdayList.add("0");
      ExpectedSaturdayList.add("1");


      BillboardAdmin.createBillboard("TestUser","ScheduledBillboard1","testXML");
      scheduleAdmin.createSchedule("ScheduledBillboard1",
              "05:00", "30", "2020-05-18 12:55", "120",
              "0","0","1","1","0","0","0");

        BillboardAdmin.createBillboard("TestUser","ScheduledBillboard2","testXML");
        scheduleAdmin.createSchedule("ScheduledBillboard2",
                "06:00", "20", "2020-05-18 13:55", "40",
                "0","1","1","1","1","1","0");


        BillboardAdmin.createBillboard("TestUser","ScheduledBillboard3","testXML");
        scheduleAdmin.createSchedule("ScheduledBillboard3",
                "13:00", "50", "2020-05-20 15:55", "0",
                "0","0","0","0","0","0","1");

        ScheduleList scheduleList = ScheduleAdmin.listScheduleInformation();

        assertAll("Should return details of Given Billboard",
                () -> assertEquals("Pass: Schedule Detail List Returned", scheduleList.getScheduleServerResponse()),
                () -> assertArrayEquals(ExpectedBillboardList.toArray(), scheduleList.getScheduleBillboardName().toArray()),
                () -> assertArrayEquals(ExpectedStartTimeList.toArray(), scheduleList.getStartTime().toArray()),
                () -> assertArrayEquals(ExpectedDurationList.toArray(), scheduleList.getDuration().toArray()),
                () -> assertArrayEquals(ExpectedCreationDateTimeList.toArray(), scheduleList.getCreationDateTime().toArray()),
                () -> assertArrayEquals(ExpectedRepeatList.toArray(), scheduleList.getRepeat().toArray()),
                () -> assertArrayEquals(ExpectedSundayList.toArray(), scheduleList.getSunday().toArray()),
                () -> assertArrayEquals(ExpectedMondayList.toArray(), scheduleList.getMonday().toArray()),
                () -> assertArrayEquals(ExpectedTuesdayList.toArray(), scheduleList.getTuesday().toArray()),
                () -> assertArrayEquals(ExpectedWednesdayList.toArray(), scheduleList.getWednesday().toArray()),
                () -> assertArrayEquals(ExpectedThursdayList.toArray(), scheduleList.getThursday().toArray()),
                () -> assertArrayEquals(ExpectedFridayList.toArray(), scheduleList.getFriday().toArray()),
                () -> assertArrayEquals(ExpectedSaturdayList.toArray(), scheduleList.getSaturday().toArray())
      );
    }


    /* Test 13: View Schedule (All Billboards) - No Schedule Exists (Exception Handling)
     * Description: Receive view schedule request from CP, will require a session token.
     *              Assume sessionToken is valid.
     *              Tests for when no schedule exists
     * Expected Output: The schedule is not retrieved and returns "Fail: No Schedule Exists"
     */
    @Test
    public void viewScheduleNoSchedule() throws IOException, SQLException {
      // Cleaning
      scheduleAdmin.deleteAllSchedules();
      BillboardAdmin.deleteAllBillboard();
      // Remove default billboard for test set up
      ScheduleList scheduleList = ScheduleAdmin.listScheduleInformation();
      assertEquals("Fail: No Schedule Exists", scheduleList.getScheduleServerResponse());

    }


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


    /* Test 15: Edit Schedule (Pass)
     * Description: Receive Edit schedule request from CP, will require a session token, billboard name,
     *              start date and the duration (mins) and repeats (daily, hourly, or every ___ mins)
     *              Assume sessionToken is valid.
     *              This tests when there is a schedule exists and will update the schedule as required
     * Expected Output: Schedule "Pass: Schedule Edited"
     */
    @Test
    public void editBillboardTest() throws IOException, SQLException {
        scheduleAdmin.deleteAllSchedules();
        BillboardAdmin.createBillboard("TestUser","EditScheduledBillboard","testXML");
        scheduleAdmin.createSchedule("EditScheduledBillboard",
                "05:00", "30", "2020-05-18 12:55", "120",
                "0","0","1","1","0","0","0");
        String dbResponse = scheduleAdmin.editSchedule("EditScheduledBillboard",
                "05:00", "20", "2020-05-20 12:55", "40",
                "0","1","1","1","0","0","1");

        assertEquals(dbResponse, "Pass: Schedule Edited");
    }


    /* Test 16: Edit Schedule (Fail)
     * Description: Receive Edit schedule request from CP, will require a session token, billboard name,
     *              start date and the duration (mins) and repeats (daily, hourly, or every ___ mins)
     *              Assume sessionToken is valid.
     *              This tests will fail due to insufficient user permission
     * Expected Output: Schedule "Fail: Insufficient User Permission"
     */
//    @Test
//    public void editBillboardTestNoPermission() throws IOException, SQLException {
//        scheduleAdmin.deleteAllSchedules();
//        BillboardAdmin.createBillboard("TestUser","EditScheduledBillboard","testXML");
//        scheduleAdmin.createSchedule("EditScheduledBillboard",
//                "05:00", "30", "2020-05-18 12:55", "120",
//                "0","0","1","1","0","0","0");
//        String dbResponse = scheduleAdmin.editSchedule("EditScheduledBillboard",
//                "05:00", "20", "2020-05-20 12:55", "40",
//                "0","1","1","1","0","0","1");
//
//        assertEquals(dbResponse, "Fail: Insufficient User Permission");
//    }


    /* Test 17: Edit Schedule (Fail)
     * Description: Receive Edit schedule request from CP, will require a session token, billboard name,
     *              start date and the duration (mins) and repeats (daily, hourly, or every ___ mins)
     *              Assume sessionToken is valid.
     *              This test assumes Schedule does not exist
     * Expected Output: Schedule "Fail: Schedule Does not Exist"
     */
    @Test
    public void editBillboardTestNoSchedule() throws IOException, SQLException {
        scheduleAdmin.deleteAllSchedules();
        BillboardAdmin.createBillboard("TestUser","EditScheduledBillboard","testXML");
        String dbResponse = scheduleAdmin.editSchedule("EditScheduledBillboard",
                "05:00", "20", "2020-05-20 12:55", "40",
                "0","1","1","1","0","0","1");
        assertEquals(dbResponse, "Fail: Schedule Does not Exist");
    }


    /* Test 18: Edit Schedule (Fail)
     * Description: Receive Edit schedule request from CP, will require a session token, billboard name,
     *              start date and the duration (mins) and repeats (daily, hourly, or every ___ mins)
     *              Assume sessionToken is valid.
     *              This test assumes Billboard does not exist
     * Expected Output: Schedule "Fail: Billboard Does not Exist"
     */
    @Test
    public void editBillboardTestNoBillboard() throws IOException, SQLException {
        scheduleAdmin.deleteAllSchedules();
        BillboardAdmin.deleteAllBillboard();
        String dbResponse = scheduleAdmin.editSchedule("EditScheduledBillboard",
                "05:00", "20", "2020-05-20 12:55", "40",
                "0","1","1","1","0","0","1");
        assertEquals(dbResponse, "Fail: Schedule Does not Exist");
    }


    /* Test 19: View Daily Schedule  (Success)
     * Description: Receive view schedule request from CP, will require a session token.
     *              Assume sessionToken is valid.
     * Expected Output: The schedule is retrieved as a string of schedules (billboard name, start time, duration)
     *                  and returns "Pass: Schedule Detail List Returned"
     */
    @Test
    public void viewFilteredSchedule() throws IOException, SQLException {
        // Cleaning
        scheduleAdmin.deleteAllSchedules();
        BillboardAdmin.deleteAllBillboard();


        // Set test cases
        List<String> ExpectedBillboardList= new ArrayList<String>();
        ExpectedBillboardList.add("ScheduledBillboard1");
        ExpectedBillboardList.add("ScheduledBillboard2");
        List<String> ExpectedStartTimeList= new ArrayList<String>();
        ExpectedStartTimeList.add("05:00:00");
        ExpectedStartTimeList.add("06:00:00");
        List<String> ExpectedDurationList= new ArrayList<String>();
        ExpectedDurationList.add("30");
        ExpectedDurationList.add("20");
        List<String> ExpectedCreationDateTimeList = new ArrayList<String>();
        ExpectedCreationDateTimeList.add("2020-05-18 12:55:00.0");
        ExpectedCreationDateTimeList.add("2020-05-18 13:55:00.0");
        List<String> ExpectedRepeatList = new ArrayList<String>();
        ExpectedRepeatList.add("120");
        ExpectedRepeatList.add("40");
        List<String> ExpectedSundayList = new ArrayList<String>();
        ExpectedSundayList.add("0");
        ExpectedSundayList.add("0");
        List<String> ExpectedMondayList = new ArrayList<String>();
        ExpectedMondayList.add("0");
        ExpectedMondayList.add("1");
        List<String> ExpectedTuesdayList = new ArrayList<String>();
        ExpectedTuesdayList.add("1");
        ExpectedTuesdayList.add("1");
        List<String> ExpectedWednesdayList = new ArrayList<String>();
        ExpectedWednesdayList.add("1");
        ExpectedWednesdayList.add("1");
        List<String> ExpectedThursdayList = new ArrayList<String>();
        ExpectedThursdayList.add("0");
        ExpectedThursdayList.add("1");
        List<String> ExpectedFridayList = new ArrayList<String>();
        ExpectedFridayList.add("0");
        ExpectedFridayList.add("1");
        List<String> ExpectedSaturdayList = new ArrayList<String>();
        ExpectedSaturdayList.add("0");
        ExpectedSaturdayList.add("0");

        String day = "Wednesday";

        BillboardAdmin.createBillboard("TestUser","ScheduledBillboard1","testXML");
        scheduleAdmin.createSchedule("ScheduledBillboard1",
                "05:00", "30", "2020-05-18 12:55", "120",
                "0","0","1","1","0","0","0");

        BillboardAdmin.createBillboard("TestUser","ScheduledBillboard2","testXML");
        scheduleAdmin.createSchedule("ScheduledBillboard2",
                "06:00", "20", "2020-05-18 13:55", "40",
                "0","1","1","1","1","1","0");


        BillboardAdmin.createBillboard("TestUser","ScheduledBillboard3","testXML");
        scheduleAdmin.createSchedule("ScheduledBillboard3",
                "13:00", "50", "2020-05-20 15:55", "0",
                "0","0","0","0","0","0","1");

        ScheduleList scheduleList = ScheduleAdmin.listFilteredScheduleInformation(day);

        assertAll("Should return details of Given Billboard",
                () -> assertEquals("Pass: Schedule Detail List Returned", scheduleList.getScheduleServerResponse()),
                () -> assertArrayEquals(ExpectedBillboardList.toArray(), scheduleList.getScheduleBillboardName().toArray()),
                () -> assertArrayEquals(ExpectedStartTimeList.toArray(), scheduleList.getStartTime().toArray()),
                () -> assertArrayEquals(ExpectedDurationList.toArray(), scheduleList.getDuration().toArray()),
                () -> assertArrayEquals(ExpectedCreationDateTimeList.toArray(), scheduleList.getCreationDateTime().toArray()),
                () -> assertArrayEquals(ExpectedRepeatList.toArray(), scheduleList.getRepeat().toArray()),
                () -> assertArrayEquals(ExpectedSundayList.toArray(), scheduleList.getSunday().toArray()),
                () -> assertArrayEquals(ExpectedMondayList.toArray(), scheduleList.getMonday().toArray()),
                () -> assertArrayEquals(ExpectedTuesdayList.toArray(), scheduleList.getTuesday().toArray()),
                () -> assertArrayEquals(ExpectedWednesdayList.toArray(), scheduleList.getWednesday().toArray()),
                () -> assertArrayEquals(ExpectedThursdayList.toArray(), scheduleList.getThursday().toArray()),
                () -> assertArrayEquals(ExpectedFridayList.toArray(), scheduleList.getFriday().toArray()),
                () -> assertArrayEquals(ExpectedSaturdayList.toArray(), scheduleList.getSaturday().toArray())
        );
    }


    /* Test 20: View Daily Schedule  (Fail)
     * Description: Receive view schedule request from CP, will require a session token.
     *              Assume sessionToken is valid.
     * Expected Output: Return Fail message of "Fail: Not a Valid Day"
     *                  and returns "Fail: Not a Valid Day"
     */
    @Test
    public void viewFilteredScheduleTestBadDay() throws IOException, SQLException {
        // Cleaning
        scheduleAdmin.deleteAllSchedules();
        BillboardAdmin.deleteAllBillboard();

        String day = "Wednssssesday";

        BillboardAdmin.createBillboard("TestUser","ScheduledBillboard1","testXML");
        scheduleAdmin.createSchedule("ScheduledBillboard1",
                "05:00", "30", "2020-05-18 12:55", "120",
                "0","0","1","1","0","0","0");

        BillboardAdmin.createBillboard("TestUser","ScheduledBillboard2","testXML");
        scheduleAdmin.createSchedule("ScheduledBillboard2",
                "06:00", "20", "2020-05-18 13:55", "40",
                "0","1","1","1","1","1","0");


        BillboardAdmin.createBillboard("TestUser","ScheduledBillboard3","testXML");
        scheduleAdmin.createSchedule("ScheduledBillboard3",
                "13:00", "50", "2020-05-20 15:55", "0",
                "0","0","0","0","0","0","1");

        ScheduleList scheduleList = ScheduleAdmin.listFilteredScheduleInformation(day);

        assertEquals("Fail: Not a Valid Day", scheduleList.getScheduleServerResponse());
    }



    /* Test 21: View Daily Schedule  (Fail)
     * Description: Receive view schedule request from CP, will require a session token.
     *              Assume sessionToken is valid.
     * Expected Output: Return Fail message of "Fail: Not a Valid Day"
     *                  and returns "Fail: Not a Valid Day"
     */
    @Test
    public void viewFilteredScheduleTestNoSched() throws IOException, SQLException {
        // Cleaning
        scheduleAdmin.deleteAllSchedules();
        BillboardAdmin.deleteAllBillboard();

        String day = "Wednesday";

        ScheduleList scheduleList = ScheduleAdmin.listFilteredScheduleInformation(day);

        assertEquals("Fail: No Schedule Exists", scheduleList.getScheduleServerResponse());
    }

    /* Test 22: View Full Day Schedule (Pass)
     * Description: Receive view schedule request from CP, will require a session token.
     *              Assume sessionToken is valid. View all schedule for a day.
     * Expected Output:
     */
    @Test
    public void viewAllDaySchedule() throws IOException, SQLException {
        // Cleaning
        scheduleAdmin.deleteAllSchedules();
        BillboardAdmin.deleteAllBillboard();

        // Set test cases
        List<String> ExpectedBillboardList= new ArrayList<String>(Arrays.asList("ScheduledBillboard1",
                "ScheduledBillboard1", "ScheduledBillboard1", "ScheduledBillboard1", "ScheduledBillboard1",
                "ScheduledBillboard1", "ScheduledBillboard1", "ScheduledBillboard1", "ScheduledBillboard1",
                "ScheduledBillboard1", "ScheduledBillboard2", "ScheduledBillboard2", "ScheduledBillboard2",
                "ScheduledBillboard2", "ScheduledBillboard2", "ScheduledBillboard2", "ScheduledBillboard2",
                "ScheduledBillboard2", "ScheduledBillboard2", "ScheduledBillboard2", "ScheduledBillboard2",
                "ScheduledBillboard2", "ScheduledBillboard2", "ScheduledBillboard2", "ScheduledBillboard2",
                "ScheduledBillboard2", "ScheduledBillboard2", "ScheduledBillboard2", "ScheduledBillboard2",
                "ScheduledBillboard2", "ScheduledBillboard2", "ScheduledBillboard2", "ScheduledBillboard2",
                "ScheduledBillboard2", "ScheduledBillboard2", "ScheduledBillboard2", "ScheduledBillboard2"));
        List<String> ExpectedStartTimeList= new ArrayList<String>(Arrays.asList("05:00", "07:00", "09:00", "11:00",
                "13:00", "15:00", "17:00", "19:00", "21:00", "23:00", "06:00", "06:40", "07:20", "08:00", "08:40",
                "09:20", "10:00", "10:40", "11:20", "12:00", "12:40", "13:20", "14:00", "14:40", "15:20", "16:00",
                "16:40", "17:20", "18:00", "18:40", "19:20", "20:00", "20:40", "21:20", "22:00", "22:40", "23:20"));
        List<String> ExpectedDurationList= new ArrayList<String>(Arrays.asList("30", "30", "30", "30", "30", "30",
                "30", "30", "30", "30", "20", "20", "20", "20", "20", "20", "20", "20", "20", "20", "20", "20",
                "20", "20", "20", "20", "20", "20", "20", "20", "20", "20", "20", "20", "20", "20", "20"));
        List<String> ExpectedCreationDateTimeList = new ArrayList<String>(Arrays.asList("2020-05-18 12:55",
                "2020-05-18 12:55", "2020-05-18 12:55", "2020-05-18 12:55", "2020-05-18 12:55",
                "2020-05-18 12:55", "2020-05-18 12:55", "2020-05-18 12:55", "2020-05-18 12:55",
                "2020-05-18 12:55", "2020-05-18 13:55", "2020-05-18 13:55", "2020-05-18 13:55",
                "2020-05-18 13:55", "2020-05-18 13:55", "2020-05-18 13:55", "2020-05-18 13:55",
                "2020-05-18 13:55", "2020-05-18 13:55", "2020-05-18 13:55", "2020-05-18 13:55",
                "2020-05-18 13:55", "2020-05-18 13:55", "2020-05-18 13:55", "2020-05-18 13:55",
                "2020-05-18 13:55", "2020-05-18 13:55", "2020-05-18 13:55", "2020-05-18 13:55",
                "2020-05-18 13:55", "2020-05-18 13:55", "2020-05-18 13:55", "2020-05-18 13:55",
                "2020-05-18 13:55", "2020-05-18 13:55", "2020-05-18 13:55", "2020-05-18 13:55"));
        List<String> ExpectedRepeatList = new ArrayList<String>(Arrays.asList("120", "120", "120", "120", "120", "120", "120",
                "120", "120", "120", "40", "40", "40", "40", "40", "40", "40", "40", "40", "40", "40", "40", "40", "40",
                "40", "40", "40", "40", "40", "40", "40", "40", "40", "40", "40", "40", "40"));
        List<String> ExpectedSundayList = new ArrayList<String>(Arrays.asList("0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                "0", "0", "0", "0", "0"));
        List<String> ExpectedMondayList = new ArrayList<String>(Arrays.asList("0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1",
                "1", "1", "1", "1", "1"));
        List<String> ExpectedTuesdayList = new ArrayList<String>(Arrays.asList("1", "1", "1", "1", "1", "1", "1", "1", "1", "1",
                "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1",
                "1", "1", "1", "1", "1"));
        List<String> ExpectedWednesdayList = new ArrayList<String>(Arrays.asList("1", "1", "1", "1", "1", "1", "1", "1", "1", "1",
                "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1",
                "1", "1", "1", "1", "1"));
        List<String> ExpectedThursdayList = new ArrayList<String>(Arrays.asList("0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1",
                "1", "1", "1", "1", "1"));
        List<String> ExpectedFridayList = new ArrayList<String>(Arrays.asList("0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1",
                "1", "1", "1", "1", "1"));
        List<String> ExpectedSaturdayList = new ArrayList<String>(Arrays.asList("0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                "0", "0", "0", "0", "0"));

        String day = "Wednesday";

        BillboardAdmin.createBillboard("TestUser","ScheduledBillboard1","testXML");
        scheduleAdmin.createSchedule("ScheduledBillboard1",
                "05:00", "30", "2020-05-18 12:55", "120",
                "0","0","1","1","0","0","0");

        BillboardAdmin.createBillboard("TestUser","ScheduledBillboard2","testXML");
        scheduleAdmin.createSchedule("ScheduledBillboard2",
                "06:00", "20", "2020-05-18 13:55", "40",
                "0","1","1","1","1","1","0");

//        ScheduleList scheduleList = ScheduleAdmin.listFilteredScheduleInformation(day);
//        ScheduleList allDaySchedule = ScheduleAdmin.viewAllDaySchedule(scheduleList);
        ScheduleList allDaySchedule = ScheduleAdmin.listAllFilteredScheduleInformation(day);

        assertAll("Should return details of Given Billboard",
                () -> assertEquals("Pass: All Day Schedule Returned", allDaySchedule.getScheduleServerResponse()),
                () -> assertArrayEquals(ExpectedBillboardList.toArray(), allDaySchedule.getScheduleBillboardName().toArray()),
                () -> assertArrayEquals(ExpectedStartTimeList.toArray(), allDaySchedule.getStartTime().toArray()),
                () -> assertArrayEquals(ExpectedDurationList.toArray(), allDaySchedule.getDuration().toArray()),
                () -> assertArrayEquals(ExpectedCreationDateTimeList.toArray(), allDaySchedule.getCreationDateTime().toArray()),
                () -> assertArrayEquals(ExpectedRepeatList.toArray(), allDaySchedule.getRepeat().toArray()),
                () -> assertArrayEquals(ExpectedSundayList.toArray(), allDaySchedule.getSunday().toArray()),
                () -> assertArrayEquals(ExpectedMondayList.toArray(), allDaySchedule.getMonday().toArray()),
                () -> assertArrayEquals(ExpectedTuesdayList.toArray(), allDaySchedule.getTuesday().toArray()),
                () -> assertArrayEquals(ExpectedWednesdayList.toArray(), allDaySchedule.getWednesday().toArray()),
                () -> assertArrayEquals(ExpectedThursdayList.toArray(), allDaySchedule.getThursday().toArray()),
                () -> assertArrayEquals(ExpectedFridayList.toArray(), allDaySchedule.getFriday().toArray()),
                () -> assertArrayEquals(ExpectedSaturdayList.toArray(), allDaySchedule.getSaturday().toArray())
        );
    }

    /* Test 22: View Active Schedule (Pass)
     * Description: Receive view schedule request from CP, will require a session token.
     *              Assume sessionToken is valid. View all schedule for a day.
     * Expected Output:
     */
    @Test
    public void ViewCurrentScheduleTest() throws IOException, SQLException {
        // Cleaning
        scheduleAdmin.deleteAllSchedules();
        BillboardAdmin.deleteAllBillboard();


        // Set test cases
        List<String> ExpectedBillboardList= new ArrayList<String>();
        ExpectedBillboardList.add("ScheduledBillboard1");
        List<String> ExpectedStartTimeList= new ArrayList<String>();
        ExpectedStartTimeList.add("07:00");
        List<String> ExpectedCreationDateTimeList = new ArrayList<String>();
        ExpectedCreationDateTimeList.add("2020-05-18 12:55");


        String day = "Wednesday";
        LocalTime currentTime = LocalTime.parse("07:20");


        BillboardAdmin.createBillboard("TestUser","ScheduledBillboard1","testXML");
        scheduleAdmin.createSchedule("ScheduledBillboard1",
                "05:00", "30", "2020-05-18 12:55", "120",
                "0","0","1","1","0","0","0");

        BillboardAdmin.createBillboard("TestUser","ScheduledBillboard2","testXML");
        scheduleAdmin.createSchedule("ScheduledBillboard2",
                "06:00", "20", "2020-05-18 13:55", "40",
                "0","1","1","1","1","1","0");

        BillboardAdmin.createBillboard("TestUser","ScheduledBillboard3","testXML");
        scheduleAdmin.createSchedule("ScheduledBillboard3",
                "13:00", "50", "2020-05-20 15:55", "0",
                "0","0","0","0","0","0","1");

        ScheduleList scheduleList = ScheduleAdmin.listFilteredScheduleInformation(day);
        ScheduleList WednesdayAllSchedule = ScheduleAdmin.viewAllDaySchedule(scheduleList);
        CurrentSchedule activeSchedules = ScheduleAdmin.viewCurrentSchedule(WednesdayAllSchedule, currentTime);

        assertAll("Should return details of Given Billboard",
                () -> assertEquals("Pass: Current Active Schedule Returned", activeSchedules.getScheduleServerResponse()),
                () -> assertArrayEquals(ExpectedStartTimeList.toArray(), activeSchedules.getStartTime().toArray()),
                () -> assertArrayEquals(ExpectedCreationDateTimeList.toArray(), activeSchedules.getCreationDateTime().toArray()),
                () -> assertArrayEquals(ExpectedBillboardList.toArray(), activeSchedules.getScheduleBillboardName().toArray())
        );
    }

    /* Test 23: Get Schedule in Schedule Table (Success)
     * Description: Receive get schedule request from CP, will require billboard name
     *              Assume sessionToken is valid.
     * Expected Output: A schedule is returned from the table and returns "Pass: Schedule Detail Returned"
     */
    @Test
    public void scheduleBillboard() throws IOException, SQLException {
        // Cleaning
        scheduleAdmin.deleteAllSchedules();
        BillboardAdmin.deleteAllBillboard();

        // Set test cases
        String ExpectedBillboardList= "ScheduledBillboard";
        String ExpectedStartTimeList= "05:00";
        String ExpectedDurationList= "30";
        String ExpectedCreationDateTimeList = "2020-05-18 12:55";
        String ExpectedRepeatList = "120";
        String ExpectedSundayList = "0";
        String ExpectedMondayList = "0";
        String ExpectedTuesdayList = "1";
        String ExpectedWednesdayList = "1";
        String ExpectedThursdayList = "0";
        String ExpectedFridayList = "0";
        String ExpectedSaturdayList = "0";


        BillboardAdmin.createBillboard("TestUser","ScheduledBillboard","testXML");
        String dbResponse = scheduleAdmin.createSchedule("ScheduledBillboard",
                "05:00", "30", "2020-05-18 12:55", "120",
                "0","0","1","1","0","0","0");

        ScheduleInfo returnInfo = ScheduleAdmin.getScheduleInformation("ScheduledBillboard");

        assertAll("Should return details of Given Billboard",
                () -> assertEquals("Pass: Schedule Detail Returned", returnInfo.getScheduleServerResponse()),
                () -> assertEquals(ExpectedBillboardList, returnInfo.getScheduleBillboardName()),
                () -> assertEquals(ExpectedStartTimeList, returnInfo.getStartTime()),
                () -> assertEquals(ExpectedDurationList, returnInfo.getDuration()),
                () -> assertEquals(ExpectedCreationDateTimeList, returnInfo.getCreationDateTime()),
                () -> assertEquals(ExpectedRepeatList, returnInfo.getRepeat()),
                () -> assertEquals(ExpectedSundayList, returnInfo.getSunday()),
                () -> assertEquals(ExpectedMondayList, returnInfo.getMonday()),
                () -> assertEquals(ExpectedTuesdayList, returnInfo.getTuesday()),
                () -> assertEquals(ExpectedWednesdayList, returnInfo.getWednesday()),
                () -> assertEquals(ExpectedThursdayList, returnInfo.getThursday()),
                () -> assertEquals(ExpectedFridayList, returnInfo.getFriday()),
                () -> assertEquals(ExpectedSaturdayList, returnInfo.getSaturday())
        );
    }



}