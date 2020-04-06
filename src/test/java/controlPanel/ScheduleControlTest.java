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


    /* Test 2: Request to server to Update Billboard Schedule (Fail)
     * Description: Method to put in a schedule for billboards. Will require a session token (valid), the billbaord
     *              name, the starting date of the method and also the duration (this is currently just in hrs format).
     *              This tests when the billboard does not exists and will raise an exception.
     * Expected Output: An exception
     */
//    @Test
//    public void scheduleBillboardRequestTest(){
//      String serverResponse = scheduleControl.scheduleBillboardRequest("sampleToken", "Billboard1",
//                                                                       "03/04/2020", "01:00");
//      assertEquals(serverResponse, "Billboard does not Exist");
//      assertThrows(NoBillboardException);
//    }


    /* Test 3: Request to server to Update Billboard Schedule (Fail)
     * Description: Method to put in a schedule for billboards. Will require a session token (valid), the billbaord
     *              name, the starting date of the method and also the duration (this is currently just in hrs format).
     *              This tests when the billboard  exists but input time is incorrect and will raise an exception.
     * Expected Output: An exception
     */
//    @Test
//    public void scheduleBillboardRequestTest(){
//      billboardControl.createBillboardRequest("sampleToken", "Billboard1", xmlCode);
//      String serverResponse = scheduleControl.scheduleBillboardRequest("sampleToken", "Billboard1",
//                                                                       "03/02-2020", "0100");
//      assertEquals(serverResponse, "Incorrect Date Time Format");
//      assertThrows(DateTimeFormatException);
//    }


    /* Test 4: Request to server to Remove Billboard Schedule (Fail)
     * Description: Method to remove Billboard. Will require a valid sessiontoken, and name of billboard to remove.
     *              This test is to check if billboard does not exisits, and if the method can raise exception.
     * Expected Output: An exception
     */
//    @Test
//    public void scheduleBillboardRequestTest(){
//        String serverResponse = scheduleControl.scheduleBillboardRequest("sampleToken", "billboard",
//                "03/04/2020", "01:00");
//      assertEquals(serverResponse, "Billboard does not Exist");
//      assertThrows(NoBillboardException);
//    }


    /* Test 5: Request to server to Remove Billboard Schedule (Fail)
     * Description: Method to remove Billboard. Will require a valid sessiontoken, and name of billboard to remove.
     *              This test is to check if billboard exisits, but user does not have suffice user permission.
     * Expected Output: An exception
     */
//    @Test
//    public void removeFromScheduleRequestTest(){
//        billboardControl.createBillboardRequest("sampleToken", "Billboard1", xmlCode);
//        scheduleControl.scheduleBillboardRequest("sampleToken", "billboard",
//                "03/04/2020", "01:00");
//      String serverResponse = scheduleControl.removeFromScheduleRequest("sessionToken", "Billboard1");
//      assertEquals(serverResponse, "No User Permission");
//      assertThrows(NoUserPermissionException);
//    }


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
//      assertEquals(serverResponse, "Billboard Schedule Removed");
//    }


    /* Test 8: Request to server to view billboard Schedules (Nothing)
     * Description: Method to request to server to view billboard schedules. Assume sessionToken is valid.
     * Expected Output: Server will return nothing / raise exception
     */
//    @Test
//    public void viewScheduleRequestTest(){
//      BillboardSchedules billboardSchedules = viewScheduleRequest("sessionToken");
//      assertTrue(billboardSchedules.length == 0);
//      assertThrows(NoBillboardScheduleException);
//    }

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
//                () -> assertArrayEquals(["Billboard1","Billboard2"], billboardSchedules.getBillboardName()),
//                () -> assertArrayEquals(["03/04/2020","04/04/2020"], billboardSchedules.getBillboardStartDate()),
//                () -> assertArrayEquals(["01:00","02:00"], billboardSchedules.getBillboardDuration()),
//    }


    /* Test 10: Request to server to View specific billboard information (Nothing)
     * Description: Method to request to server for specific billboard information. No Schedule found
     * Expected Output: Server will raise exception
     */
//    @Test
//    public void viewABillboardScheduleRequest(String sessionToken, String billboard){
//      billboardControl.createBillboardRequest("sampleToken", "Billboard1", xmlCode);
//      BillboardScheduleInformation billboardScheduleInformation = viewBillboardScheduleRequest("sessionToken",
//                "Billboard1");
//      assertTrue(billboardScheduleInformation.length == 0);
//      assertThrows(NoBillboardScheduleException);
//    }


    /* Test 11: Request to server to View specific billboard information (Fail)
     * Description: Method to request to server for specific billboard information. Billboard Not Found
     * Expected Output: Server will raise exception
     */
//    @Test
//    public void viewABillboardScheduleRequest(String sessionToken, String billboard){
//      BillboardScheduleInformation billboardScheduleInformation = viewBillboardScheduleRequest("sessionToken",
//                "Billboard1");
//      assertTrue(billboardScheduleInformation.length == 0);
//      assertThrows(NoBillboardException);
//    }


    /* Test 12: Request to server to View specific billboard information (Success)
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
//                () -> assertEquals("03/04/2020", billboardScheduleInformation.getBillboardStartDate()),
//                () -> assertEquals("01:00", billboardScheduleInformation.getBillboardDuration())
//                () -> assertEquals("03/04/2020", billboardScheduleInformation.getBillboardEndDate())
//        );
//    }

}