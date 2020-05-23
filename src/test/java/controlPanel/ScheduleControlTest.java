package controlPanel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
      BillboardControl.createBillboardRequest("sampleToken", "Billboard1", "xmlCode");
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
//    @Test
//    public void scheduleBillboardNoPermission(){
//      billboardAdmin.createBillboard("sampleToken", "Billboard1", xmlCode);
//      // Example 1 - Billboard duration is 5 mins and repeats every 1 min should throw an error
//      String dbResponse = scheduleAdmin.scheduleBillboard("basicToken", "Billboard1",
//                                                                       "2020-04-14 09:00:00", "5:00:00:00", "24:00:00");
//      assertEquals(serverResponse, "Fail: Billboard Scheduled More Frequently Than Duration");



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
//    @Test
//    public void removeFromScheduleRequestTest(){
//      billboardControl.createBillboardRequest("sampleToken", "Billboard1", xmlCode);
//      scheduleControl.scheduleBillboardRequest("sampleToken", "billboard",
//                "2020-04-14 09:00:00", "00:01:00", "00:10:00");
//      String serverResponse = scheduleControl.removeFromScheduleRequest("sessionToken", "Billboard1", "2020-04-14 09:00:00");
//      assertEquals(serverResponse, "Pass: Billboard Schedule Removed");
//    }


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


    /* Test 14: Request to server to view billboard Schedules (Success)
     * Description: Method to request to server to view billboard schedules. Assume sessionToken is valid.
     * Expected Output: Server will return a string of schedules
     */
//    @Test
//    public void viewScheduleRequestTest(){
//      billboardControl.createBillboardRequest("sampleToken", "Billboard1", xmlCode);
//      scheduleControl.scheduleBillboardRequest("sampleToken", "Billboard1", "2020-04-14 09:00:00", "00:01:00", "00:10:00");
//      billboardControl.createBillboardRequest("sampleToken", "Billboard2", xmlCode);
//      scheduleControl.scheduleBillboardRequest("sampleToken", "Billboard2", "2020-04-14 09:00:00", "00:01:00", "00:10:00");
//      BillboardSchedules billboardSchedules = viewScheduleRequest("sessionToken");
//      assertAll("Should return details of Given Billboard",
//                () -> assertEquals("Pass: Billboard List Returned", billboardSchedules.getServerResponse()),
//                () -> assertArrayEquals(String[] {"Billboard1","Billboard2"}, billboardSchedules.getBillboardName()),
//                () -> assertArrayEquals(String[] {"03/04/2020","04/04/2020"}, billboardSchedules.getBillboardStartDate()),
//                () -> assertArrayEquals(String[] {"01:00","02:00"}, billboardSchedules.getBillboardDuration()),
//      );
//    }


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
//    @Test
//    public void viewABillboardScheduleRequest(String sessionToken, String billboard){
//      billboardControl.createBillboardRequest("sampleToken", "Billboard1", xmlCode);
//      scheduleControl.scheduleBillboardRequest("sampleToken", "Billboard1", "2020-04-14 09:00:00", "00:01:00", "00:10:00");
//      BillboardScheduleInformation billboardScheduleInformation = viewBillboardScheduleRequest("sessionToken",
//              "Billboard1");
//      assertAll("Should return details of Given Billboard",
//                () -> assertEquals("Pass: Billboard Schedule Returned", billboardScheduleInformation.getServerResponse()),
//                () -> assertEquals("2020-04-14", billboardScheduleInformation.getBillboardStartDate()),
//                () -> assertEquals("00:01:00", billboardScheduleInformation.getBillboardDuration())
//                () -> assertEquals("00:10:00", billboardScheduleInformation.getBillboardRepeats())
//                () -> assertEquals("2020-04-14", billboardScheduleInformation.getBillboardEndDate())
//      );
//    }


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

}