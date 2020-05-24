package controlPanel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class ScheduleControlTest {

    /* Test 0: Declaring ScheduleControl object
     * Description: ScheduleControl object should be running in background on application start.
     * Expected Output: ScheduleControl object is declared
     */
    ScheduleControl scheduleControl;

    /* Test 1: Constructing a ScheduleControl object
     * Description: ScheduleControl Object should be able to be created on logged in user request from control panel
     * Expected Output: ScheduleControl object is instantiated from ScheduleControl class
     */
    @BeforeEach
    @Test
    public void setUpScheduleControl() {
      scheduleControl = new ScheduleControl();
    }


    /* Test 2: Request to server to Update Billboard Schedule (Success)
     * Description: Method to put in a schedule for billboards. Will require a session token (valid), the billbaord
     *              name, the starting date of the method and also the duration .
     * Expected Output: An exception
     */
    @Test
    public void scheduleBillboardRequestTest() throws IOException, ClassNotFoundException {
      BillboardControl.deleteAllBillboardRequest("sessionToken");
      //BillboardControl.createBillboardRequest("sampleToken", "Billboard1", "xmlCode");
      ScheduleControl.deleteAllScheduleRequest("sampleToken");
      String serverResponse = scheduleControl.scheduleBillboardRequest("sampleToken", "Billboard1",
              "09:30", 30, "2020-05-20 13:00", 40,0,0,
              1,0,1,1,0);
      assertEquals(serverResponse, "Pass: Billboard Scheduled");
    }


    /* Test 3: Request to server to Update Billboard Schedule (Exception Handling)
     * Description: Method to put in a schedule for billboards. Will require a session token (valid), the billboard
     *              name, the starting date of the method and also the duration .
     *              This test when the billboard does not exists
     * Expected Output: An exception
     */
//    @Test
//    public void scheduleBillboardRequestTestNoBillboard(){
//      String serverResponse = scheduleControl.scheduleBillboardRequest("sampleToken", "Billboard1",
//                                                                       "2020-04-14 09:30:00", "01:00:00", "24:00:00");
//      assertEquals(serverResponse, "Fail: Billboard does not Exist");
//      assertThrows(NoBillboardException);
//    }


    /* Test 4: Request to server to Update Billboard Schedule (Exception Handling)
     * Description: Method to put in a schedule for billboards. Will require a session token (valid), the billbaord
     *              name, the starting date of the method and also the duration (this is currently just in hrs format).
     *              This tests when the billboard  exists but input time is incorrect and will raise an exception.
     * Expected Output: An exception
     */
//    @Test
//    public void scheduleBillboardRequestTestInvalidDateTimeFormat(){
//      billboardControl.createBillboardRequest("sampleToken", "Billboard1", xmlCode);
//      String serverResponse = scheduleControl.scheduleBillboardRequest("sampleToken", "Billboard1",
//                                                                       "2020-04-14 09:30:00", "01:00:00", "24:00:00");
//      assertEquals(serverResponse, "Fail: Invalid Date Time Format");
//      assertThrows(DateTimeFormatException);
//    }


    /* Test 5: Request to server to Update Billboard Schedule (Exception Handling)
     * Description: Method to put in a schedule for billboards. Will require a session token (valid), the billbaord
     *              name, the starting date of the method and also the duration
     *              This tests when the user does not have permission.
     * Expected Output: An exception
     */
//    @Test
//    public void scheduleBillboardRequestTestNoPermission(){
//      billboardControl.createBillboardRequest("sampleToken", "Billboard1", xmlCode);
//      String serverResponse = scheduleControl.scheduleBillboardRequest("sampleToken", "Billboard1",
//                                                                       "2020-04-14 09:30:00", "01:00:00", "24:00:00");
//      assertEquals(serverResponse, "Fail: Insufficient User Permission");
//    }


    /* Test 6: Create Schedule - Repeat Exceeds Duration (Exception Handling)
     * Description: Receive create schedule request from CP, will require a session token, billboard name,
     *              start date and the duration (mins) and repeats (daily, hourly, or every ___ mins)
     *              Assume sessionToken is valid. This tests per minute
     *              This tests when the user wishes to repeat the billboard more often than the duration
     * Expected Output: A schedule is not added to the table and returns "Fail: Billboard Scheduled More Frequently
     *                  Than Duration"
     */
//    @Test
//    public void scheduleBillboardNoPermission(){
//      billboardAdmin.createBillboard("sampleToken", "Billboard1", xmlCode);
//      // Example 1 - Billboard duration is 5 mins and repeats every 1 min should throw an error
//      String dbResponse = scheduleAdmin.scheduleBillboard("basicToken", "Billboard1",
//                                                                       "2020-04-14 09:00:00", "00:05:00", "00:01:00");
//      assertEquals(serverResponse, "Fail: Billboard Scheduled More Frequently Than Duration");


    /* Test 7: Create Schedule - Repeat Exceeds Duration (Exception Handling)
     * Description: Receive create schedule request from CP, will require a session token, billboard name,
     *              start date and the duration (mins) and repeats (daily, hourly, or every ___ mins)
     *              Assume sessionToken is valid. This test per hour
     *              This tests when the user wishes to repeat the billboard more often than the duration
     * Expected Output: A schedule is not added to the table and returns "Fail: Billboard Scheduled More Frequently
     *                  Than Duration"
     */
//    @Test
//    public void scheduleBillboardNoPermission(){
//      billboardAdmin.createBillboard("sampleToken", "Billboard1", xmlCode);
//      // Example 1 - Billboard duration is 5 mins and repeats every 1 min should throw an error
//      String dbResponse = scheduleAdmin.scheduleBillboard("basicToken", "Billboard1",
//                                                                       "2020-04-14 09:00:00", "05:00:00", "01:00:00");
//      assertEquals(serverResponse, "Fail: Billboard Scheduled More Frequently Than Duration");



    /* Test 8: Create Schedule - Repeat Exceeds Duration (Exception Handling)
     * Description: Receive create schedule request from CP, will require a session token, billboard name,
     *              start date and the duration (mins) and repeats (daily, hourly, or every ___ mins)
     *              Assume sessionToken is valid. This tests per day
     *              This tests when the user wishes to repeat the billboard more often than the duration
     * Expected Output: A schedule is not added to the table and returns "Fail: Billboard Scheduled More Frequently
     *                  Than Duration"
     */
    @Test
    public void scheduleBillboardBadRepeatDuration() throws IOException, SQLException, ClassNotFoundException {
        BillboardControl.deleteAllBillboardRequest("sessionToken");
        //BillboardControl.createBillboardRequest("sampleToken", "Billboard1", "xmlCode");
        ScheduleControl.deleteAllScheduleRequest("sampleToken");
        // Example 1 - Billboard duration is 5 mins and repeats every 1 min should throw an error
        String serverResponse = ScheduleControl.scheduleBillboardRequest("sampleToken","Billboard1",
                "04:00",50,"2020-04-15 13:40",20,1,0,
                0,1,0,0,1);
        assertEquals(serverResponse, "Fail: Billboard Scheduled More Frequently Than Duration");
    }


    /* Test 9: Create Schedule - Repeat Exceeds Duration (Exception Handling)
     * Description: Receive create schedule request from CP, will require a session token, billboard name,
     *              start date and the duration (mins) and repeats (daily, hourly, or every ___ mins)
     *              Assume sessionToken is valid.
     *              This tests when the user wishes to repeat the billboard more often than the duration
     * Expected Output: A schedule is not added to the table and returns "Fail: Duplicate Start Date Time"
     */
//    @Test
//    public void scheduleBillboardNoPermission(){
//      billboardAdmin.createBillboard("sampleToken", "Billboard1", xmlCode);
//      scheduleAdmin.scheduleBillboard("basicToken", "Billboard1","2020-04-14 09:00:00", "00:05:00", "00:01:00");
//      String dbResponse = scheduleAdmin.scheduleBillboard("basicToken", "Billboard1",
//                                                                       "2020-04-14 09:00:00", "00:05:00", "00:01:00");
//      assertEquals(serverResponse, "Fail: Duplicate Start Date Time");


    /* Test 10: Request to server to Remove Billboard Schedule (Success)
     * Description: Method to remove Billboard. Will require a valid sessiontoken, and name of billboard to remove.
     * Expected Output: An success message from the server
     */
    @Test
    public void removeFromScheduleRequestTest() throws IOException, ClassNotFoundException {
      BillboardControl.deleteAllBillboardRequest("sessionToken");
      ScheduleControl.deleteAllScheduleRequest("sampleToken");
      //BillboardControl.createBillboardRequest("sampleToken", "Billboard1", "xmlCode");
      ScheduleControl.scheduleBillboardRequest("sampleToken","Billboard1",
                "04:00",20,"2020-04-15 13:40",60,1,0,
                0,1,0,0,1);
      String serverResponse = scheduleControl.deleteScheduleRequest("sessionToken", "Billboard1");
      assertEquals(serverResponse, "Pass: Billboard Schedule Deleted");
    }


    /* Test 11: Request to server to Remove Billboard Schedule (Exception Handling)
     * Description: Method to remove Billboard. Will require a valid sessiontoken, and name of billboard to remove.
     *              This test is to check if billboard exisits, but user does not have suffice user permission.
     * Expected Output: An exception
     */
//    @Test
//    public void removeFromScheduleRequestTestNoPermission(){
//        billboardControl.createBillboardRequest("sampleToken", "Billboard1", xmlCode);
//        scheduleControl.scheduleBillboardRequest("sampleToken", "billboard",
//                "2020-04-14 09:00:00", "00:01:00", "00:10:00");
//      String serverResponse = scheduleControl.removeFromScheduleRequest("sessionToken", "Billboard1");
//      assertEquals(serverResponse, "Fail: Insufficient User Permission");
//      assertThrows(NoUserPermissionException);
//    }


    /* Test 12: Request to server to Remove Billboard Schedule (Exception Handling)
     * Description: Method to remove Billboard. Will require a valid sessiontoken, and name of billboard to remove.
     *              This test is to check if billboard does not exisits, and if the method can raise exception.
     * Expected Output: An exception
     */
//    @Test
//    public void removeFromScheduleRequestTestNoBillboard(){
//        String serverResponse = scheduleControl.removeFromScheduleRequest("sampleToken", "billboard",
//                "2020-04-14 09:00:00", "00:01:00", "00:10:00");
//      assertEquals(serverResponse, "Fail: Billboard Does Not Exist");
//      assertThrows(NoBillboardException);
//    }


    /* Test 13: Request to server to Remove Billboard Schedule (Exception Handling)
     * Description: Method to remove Billboard. Will require a valid sessiontoken, and name of billboard to remove.
     *              This test is to check if billboard schedule does not exisits, and if the method can raise exception.
     * Expected Output: An exception
     */
//    @Test
//    public void removeFromScheduleRequestTestNoSchedule(){
//      billboardControl.createBillboardRequest("sampleToken", "Billboard1", xmlCode);
//      scheduleControl.scheduleBillboardRequest("sampleToken", "billboard",
//                "2020-04-14 09:00:00", "00:01:00", "00:10:00");
//      scheduleControl.removeFromScheduleRequest("sessionToken", "Billboard1", "2020-04-14 09:00:00");
//      String serverResponse = scheduleControl.removeFromScheduleRequest("sessionToken", "Billboard1", "2020-04-14 09:00:00");
//      assertEquals(serverResponse, "Fail: Billboard Schedule Does not exist");
//    }


    /* Test 14: Request to server to view raw billboard Schedules (Success)
     * Description: Method to request to server to view billboard schedules. Assume sessionToken is valid.
     * Expected Output: Server will return a string of schedules
     */
    @Test
    public void viewScheduleRequestDayTest() throws IOException, ClassNotFoundException {
        // Cleaning for test
        BillboardControl.deleteAllBillboardRequest("sessionToken");
        ScheduleControl.deleteAllScheduleRequest("sampleToken");
        //BillboardControl.createBillboardRequest("sampleToken", "ScheduledBillboard1", "xmlCode");
        ScheduleControl.scheduleBillboardRequest("sampleToken","ScheduledBillboard1",
                "05:00", 30, "2020-05-18 12:55", 120,
                0,0,1,1,0,0,0);
        //BillboardControl.createBillboardRequest("sampleToken", "ScheduledBillboard2", "xmlCode");
        ScheduleControl.scheduleBillboardRequest("sampleToken", "ScheduledBillboard2",
                "06:00", 20, "2020-05-18 13:55", 40,
                0,1,1,1,1,1,0);

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
        server.ScheduleList allDaySchedule = (ScheduleList) ScheduleControl.listDayScheduleRequest("sampleToken", day);

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


    /* Test 15: Request to server to view billboard Schedules (Exception Handling)
     * Description: Method to request to server to view billboard schedules. Assume sessionToken is valid. No
     *              billboard is present
     * Expected Output: Server will return nothing / raise exception
     */
//    @Test
//    public void viewScheduleRequestTestNoBillboard(){
//      BillboardSchedules billboardSchedules = viewScheduleRequest("sessionToken");
//      assertEquals("Fail: No Schedule Exists", billboardSchedules.getServerResponse())
//      assertTrue(billboardSchedules.getBillboardName().length == 0);
//    }


    /* Test 16: Request to server to view billboard Schedules (Exception Handling)
     * Description: Method to request to server to view billboard schedules. Assume sessionToken is valid. Insuffice
     *              user permssion
     * Expected Output: Server will return nothing / raise exception "Fail: Insufficient User Permission"
     */
//    @Test
//    public void viewScheduleRequestTestNoBillboard(){
//      noPermissionAdmin = new userControl("NewUser0");
//      userControl.setUserPermissions("sessionToken","NewUser0", {0,0,0,0});
//      BillboardSchedules billboardSchedules = noPermissionAdmin.viewScheduleRequest("sessionToken");
//      assertEquals("Fail: Insufficient User Permission", billboardSchedules.getServerResponse());
//      assertTrue(billboardSchedules.getBillboardName().length == 0);
//    }


    /* Test 17: Request to server to View specific billboard information (Success)
     * Description: Method to request to server for specific billboard information
     * Expected Output: Server will return Start Date, Duration and End Date
     */
    @Test
    public void viewABillboardScheduleRequestTest() throws IOException, ClassNotFoundException {
        // Set test cases
        String ExpectedBillboardList= "ScheduledBillboard1";
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

        // Cleaning for test
        BillboardControl.deleteAllBillboardRequest("sessionToken");
        ScheduleControl.deleteAllScheduleRequest("sampleToken");
        //BillboardControl.createBillboardRequest("sampleToken", "ScheduledBillboard1", "xmlCode");
        ScheduleControl.scheduleBillboardRequest("sampleToken","ScheduledBillboard1",
                "05:00", 30, "2020-05-18 12:55", 120,
                0,0,1,1,0,0,0);
        //BillboardControl.createBillboardRequest("sampleToken", "ScheduledBillboard2", "xmlCode");
        ScheduleControl.scheduleBillboardRequest("sampleToken", "ScheduledBillboard2",
                "06:00", 20, "2020-05-18 13:55", 40,
                0,1,1,1,1,1,0);


        server.ScheduleInfo returnInfo = (ScheduleInfo)  ScheduleControl.listABillboardSchedule("sessionToken",
                "ScheduledBillboard1");

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


    /* Test 18: Request to server to View specific billboard information (Exception Handling)
     * Description: Method to request to server for specific billboard information. No Schedule found
     * Expected Output: Server will raise exception
     */
//    @Test
//    public void viewABillboardScheduleRequest(String sessionToken, String billboard){
//      billboardControl.createBillboardRequest("sampleToken", "Billboard1", xmlCode);
//      BillboardScheduleInformation billboardScheduleInformation = viewBillboardScheduleRequest("sessionToken",
//                "Billboard1");
//      assertTrue(billboardScheduleInformation.getServerResponse() == "Fail: No Schedule Exists");
//      assertThrows(NoBillboardScheduleException);
//    }


    /* Test 19: Request to server to View specific billboard information (Exception Handling)
     * Description: Method to request to server for specific billboard information. Billboard Not Found
     * Expected Output: Server will raise exception: "Fail: Billboard Does Not Exist"
     */
//    @Test
//    public void viewABillboardScheduleRequest(String sessionToken, String billboard){
//      BillboardScheduleInformation billboardScheduleInformation = viewBillboardScheduleRequest("sessionToken",
//                "Billboard1");
//      assertTrue(billboardScheduleInformation.getServerResponse() == "Fail: Billboard Does Not Exist");
//      assertThrows(NoBillboardException);
//    }


    /* Test 20: Request to server to View specific billboard information (Exception Handling)
     * Description: Method to request to server for specific billboard information. No user permission
     * Expected Output: Server will raise exception
     */
//    @Test
//    public void viewABillboardScheduleRequest(String sessionToken, String billboard){
//      noPermissionAdmin = new userControl("NewUser0");
//      userControl.setUserPermissions("sessionToken","NewUser0", {0,0,0,0});
//      BillboardScheduleInformation billboardScheduleInformation = noPermissionAdmin.viewBillboardScheduleRequest(
//      "sessionToken","Billboard1");
//      assertTrue(billboardScheduleInformation.getServerResponse() == "Fail: No User Permission");
//    }

    /* Test 22: View Active Schedule (Pass)
     * Description: Receive view schedule request from CP, will require a session token.
     *              Assume sessionToken is valid. View all schedule for a day.
     * Expected Output:
     */
    @Test
    public void ViewCurrentScheduleTest() throws IOException, SQLException, ClassNotFoundException {
        // Cleaning
        BillboardControl.deleteAllBillboardRequest("sessionToken");
        ScheduleControl.deleteAllScheduleRequest("sampleToken");
        //BillboardControl.createBillboardRequest("sampleToken", "ScheduledBillboard1", "xmlCode");
        ScheduleControl.scheduleBillboardRequest("sampleToken","ScheduledBillboard1",
                "05:00", 30, "2020-05-18 12:55", 120,
                0,0,1,1,0,0,0);
        //BillboardControl.createBillboardRequest("sampleToken", "ScheduledBillboard2", "xmlCode");
        ScheduleControl.scheduleBillboardRequest("sampleToken", "ScheduledBillboard2",
                "06:00", 20, "2020-05-18 13:55", 40,
                0,1,1,1,1,1,0);
        //BillboardControl.createBillboardRequest("sampleToken", "ScheduledBillboard3","testXML");
        ScheduleControl.scheduleBillboardRequest("sampleToken", "ScheduledBillboard3",
                "13:00", 50, "2020-05-20 15:55", 0,
                0,0,0,0,0,0,1);


        // Set test cases
        List<String> ExpectedBillboardList= new ArrayList<String>();
        ExpectedBillboardList.add("ScheduledBillboard1");
        List<String> ExpectedStartTimeList= new ArrayList<String>();
        ExpectedStartTimeList.add("07:00");
        List<String> ExpectedCreationDateTimeList = new ArrayList<String>();
        ExpectedCreationDateTimeList.add("2020-05-18 12:55");

        String day = "Wednesday";
        LocalTime currentTime = LocalTime.parse("07:20");

        server.CurrentSchedule activeSchedule = (CurrentSchedule) ScheduleControl.listActiveSchedule("sampleToken",day, String.valueOf(currentTime));

        assertAll("Should return details of Given Billboard",
                () -> assertEquals("Pass: Current Active Schedule Returned", activeSchedule.getScheduleServerResponse()),
                () -> assertArrayEquals(ExpectedStartTimeList.toArray(), activeSchedule.getStartTime().toArray()),
                () -> assertArrayEquals(ExpectedCreationDateTimeList.toArray(), activeSchedule.getCreationDateTime().toArray()),
                () -> assertArrayEquals(ExpectedBillboardList.toArray(), activeSchedule.getScheduleBillboardName().toArray())
        );
    }



}