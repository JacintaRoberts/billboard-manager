package controlPanel;

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
//    @BeforeEach
//    @Test
//    public void setUpScheduleControl() {
//      scheduleControl = new ScheduleControl();
//    }

// TODO: ALL TESTS NEED PROPER STRING DATE FORMATTING FOR COMPATIBILITY WITH MARIA DB (LIKE IN SCHEDULEADMIN)
// TODO: DURATION SHOULD BE IN A MINS FORMAT ("100") IS MINIMUM OF 1 MIN.
    /* Test 2: Request to server to Update Billboard Schedule (Success)
     * Description: Method to put in a schedule for billboards. Will require a session token (valid), the billbaord
     *              name, the starting date of the method and also the duration (this is currently just in hrs format).
     * Expected Output: An exception
     */
//    @Test
//    public void scheduleBillboardRequestTest(){
//      billboardControl.createBillboardRequest("sampleToken", "Billboard1", xmlCode);
//      String serverResponse = scheduleControl.scheduleBillboardRequest("sampleToken", "Billboard1",
//                                                                       "03/04/2020", "01:00");
//      assertEquals(serverResponse, "Pass: Billboard Scheduled");
//    }


    /* Test 3: Request to server to Update Billboard Schedule (Exception Handling)
     * Description: Method to put in a schedule for billboards. Will require a session token (valid), the billbaord
     *              name, the starting date of the method and also the duration (this is currently just in hrs format).
     *              This test when the billboard does not exists
     * Expected Output: An exception
     */
//    @Test
//    public void scheduleBillboardRequestTestNoBillboard(){
//      String serverResponse = scheduleControl.scheduleBillboardRequest("sampleToken", "Billboard1",
//                                                                       "03/04/2020", "01:00");
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
//                                                                       "03/02-2020", "0100");
//      assertEquals(serverResponse, "Fail: Invalid Date Time Format");
//      assertThrows(DateTimeFormatException);
//    }


    /* Test 5: Request to server to Update Billboard Schedule (Exception Handling)
     * Description: Method to put in a schedule for billboards. Will require a session token (valid), the billbaord
     *              name, the starting date of the method and also the duration (this is currently just in hrs format).
     *              This tests when the user does not have permission.
     * Expected Output: An exception
     */
//    @Test
//    public void scheduleBillboardRequestTestNoPermission(){
//      billboardControl.createBillboardRequest("sampleToken", "Billboard1", xmlCode);
//      String serverResponse = scheduleControl.scheduleBillboardRequest("sampleToken", "Billboard1",
//                                                                       "03/02-2020", "0100");
//      assertEquals(serverResponse, "Fail: Insufficient User Permission");
//    }

//TODO: "ScheduleBillboardRepeatExceedsDuration" -> Alan to Check this example and add test inside this
// for hourly (01:00:00) and daily (24:00:00) repeats please
    /* Test 6: Create Schedule - Repeat Exceeds Duration (Exception Handling)
     * Description: Receive create schedule request from CP, will require a session token, billboard name,
     *              start date and the duration (mins) and repeats (daily, hourly, or every ___ mins)
     *              Assume sessionToken is valid.
     *              This tests when the user wishes to repeat the billboard more often than the duration
     * Expected Output: A schedule is not added to the table and returns "Fail: Insufficient User Permission"
     */
//    @Test
//    public void scheduleBillboardNoPermission(){
//      billboardAdmin.createBillboard("sampleToken", "Billboard1", xmlCode);
//      // Example 1 - Billboard duration is 5 mins and repeats every 1 min should throw an error
//      String dbResponse = scheduleAdmin.scheduleBillboard("basicToken", "Billboard1",
//                                                                       "2020-04-14 09:00:00", "500", "100");
//      assertEquals(serverResponse, "Fail: Billboard Scheduled More Frequently Than Duration");

//TODO: WRITE TEST FOR "ScheduleBillboardDuplicateStartDateTime" -> "Fail: Duplicate Start Date Time"


//TODO: REMOVESCHEDULE TESTS NEED TO BE UPDATED TO HAVE StartDateTime AS AN ARGUMENT SO A SPECIFIC SCHEDULE
// CAN BE REMOVED IF A BILLBOARD HAS MULTIPLE OCCASIONS IT HAS BEEN SCHEDULED
    /* Test 6: Request to server to Remove Billboard Schedule (Success)
     * Description: Method to remove Billboard. Will require a valid sessiontoken, and name of billboard to remove.
     * Expected Output: An success message from the server
     */
//    @Test
//    public void removeFromScheduleRequestTest(){
//      billboardControl.createBillboardRequest("sampleToken", "Billboard1", xmlCode);
//      scheduleControl.scheduleBillboardRequest("sampleToken", "billboard",
//                "03/04/2020", "01:00");
//      String serverResponse = scheduleControl.removeFromScheduleRequest("sessionToken", "Billboard1");
//      assertEquals(serverResponse, "Pass: Billboard Schedule Removed");
//    }


    /* Test 7: Request to server to Remove Billboard Schedule (Exception Handling)
     * Description: Method to remove Billboard. Will require a valid sessiontoken, and name of billboard to remove.
     *              This test is to check if billboard exisits, but user does not have suffice user permission.
     * Expected Output: An exception
     */
//    @Test
//    public void removeFromScheduleRequestTestNoPermission(){
//        billboardControl.createBillboardRequest("sampleToken", "Billboard1", xmlCode);
//        scheduleControl.scheduleBillboardRequest("sampleToken", "billboard",
//                "03/04/2020", "01:00");
//      String serverResponse = scheduleControl.removeFromScheduleRequest("sessionToken", "Billboard1");
//      assertEquals(serverResponse, "Fail: Insufficient User Permission");
//      assertThrows(NoUserPermissionException);
//    }


    /* Test 8: Request to server to Remove Billboard Schedule (Exception Handling)
     * Description: Method to remove Billboard. Will require a valid sessiontoken, and name of billboard to remove.
     *              This test is to check if billboard does not exisits, and if the method can raise exception.
     * Expected Output: An exception
     */
//    @Test
//    public void removeFromScheduleRequestTestNoBillboard(){
//        String serverResponse = scheduleControl.removeFromScheduleRequest("sampleToken", "billboard",
//                "03/04/2020", "01:00");
//      assertEquals(serverResponse, "Fail: Billboard Does Not Exist");
//      assertThrows(NoBillboardException);
//    }

//TODO: WRITE TEST FOR "RemoveScheduleAlreadyDeleted"


    /* Test 9: Request to server to view billboard Schedules (Success)
     * Description: Method to request to server to view billboard schedules. Assume sessionToken is valid.
     * Expected Output: Server will return a string of schedules
     */
//    @Test
//    public void viewScheduleRequestTest(){
//      billboardControl.createBillboardRequest("sampleToken", "Billboard1", xmlCode);
//      scheduleControl.scheduleBillboardRequest("sampleToken", "Billboard1", "03/04/2020","01:00");
//      billboardControl.createBillboardRequest("sampleToken", "Billboard2", xmlCode);
//      scheduleControl.scheduleBillboardRequest("sampleToken", "Billboard2", "04/04/2020","02:00");
//      BillboardSchedules billboardSchedules = viewScheduleRequest("sessionToken");
//      assertAll("Should return details of Given Billboard",
//                () -> assertEquals("Pass: Billboard List Returned", billboardSchedules.getServerResponse()),
//                () -> assertArrayEquals(String[] {"Billboard1","Billboard2"}, billboardSchedules.getBillboardName()),
//                () -> assertArrayEquals(String[] {"03/04/2020","04/04/2020"}, billboardSchedules.getBillboardStartDate()),
//                () -> assertArrayEquals(String[] {"01:00","02:00"}, billboardSchedules.getBillboardDuration()),
//    }


    /* Test 10: Request to server to view billboard Schedules (Exception Handling)
     * Description: Method to request to server to view billboard schedules. Assume sessionToken is valid.
     * Expected Output: Server will return nothing / raise exception
     */
//    @Test
//    public void viewScheduleRequestTestNoBillboard(){
//      BillboardSchedules billboardSchedules = viewScheduleRequest("sessionToken");
//      assertEquals("Fail: No Schedule Exists", billboardSchedules.getServerResponse())
//      assertTrue(billboardSchedules.getBillboardName().length == 0);
//      assertThrows(NoBillboardScheduleException);
//    }


    /* Test 11: Request to server to View specific billboard information (Success)
     * Description: Method to request to server for specific billboard information
     * Expected Output: Server will return Start Date, Duration and End Date
     */
//    @Test
//    public void viewABillboardScheduleRequest(String sessionToken, String billboard){
//      billboardControl.createBillboardRequest("sampleToken", "Billboard1", xmlCode);
//      scheduleControl.scheduleBillboardRequest("sampleToken", "Billboard1", "03/04/2020","01:00");
//      BillboardScheduleInformation billboardScheduleInformation = viewBillboardScheduleRequest("sessionToken",
//              "Billboard1");
//      assertAll("Should return details of Given Billboard",
//                () -> assertEquals("Pass: Billboard Schedule Returned", billboardScheduleInformation.getServerResponse()),
//                () -> assertEquals("03/04/2020", billboardScheduleInformation.getBillboardStartDate()),
//                () -> assertEquals("01:00", billboardScheduleInformation.getBillboardDuration())
//                () -> assertEquals("03/04/2020", billboardScheduleInformation.getBillboardEndDate())
//        );
//    }


    /* Test 12: Request to server to View specific billboard information (Exception Handling)
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


    /* Test 13: Request to server to View specific billboard information (Exception Handling)
     * Description: Method to request to server for specific billboard information. Billboard Not Found
     * Expected Output: Server will raise exception
     */
//    @Test
//    public void viewABillboardScheduleRequest(String sessionToken, String billboard){
//      BillboardScheduleInformation billboardScheduleInformation = viewBillboardScheduleRequest("sessionToken",
//                "Billboard1");
//      assertTrue(billboardScheduleInformation.getServerResponse() == "Fail: Billboard Does Not Exists");
//      assertThrows(NoBillboardException);
//    }



}