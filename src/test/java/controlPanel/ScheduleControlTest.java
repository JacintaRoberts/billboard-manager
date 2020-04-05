package controlPanel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
     * // Todo: **********ASK******** Check try catch block
     * Expected Output: An exception
     */
//    public void scheduleBillboardRequestTest(){
//        try{
//            scheduleControl.scheduleBillboardRequest("sampleToken", "Billboard1", "03/04/2020", "01:00");
//        } catch (MissingBillboard e){
//            e.printStackTrace();
//        }
//    }


    /* Test 3: Request to server to Update Billboard Schedule (Fail)
     * Description: Method to put in a schedule for billboards. Will require a session token (valid), the billbaord
     *              name, the starting date of the method and also the duration (this is currently just in hrs format).
     *              This tests when the billboard  exists but input time is incorrect and will raise an exception.
     * Expected Output: An exception
     */
//    public void scheduleBillboardRequestTest(){
//        billboardControl.createBillboardRequest("sampleToken", "Billboard1", xmlCode);
//        try {
//            scheduleControl.scheduleBillboardRequest("sampleToken", "Billboard1", "AprilFools", "01/00");
//        } catch (IncorrectInput e) {
//            e.printStackTrace();
//        }
//    }


    /* Test 4: Request to server to Update Billboard Schedule (Success)
     * Description: Method to put in a schedule for billboards. Will require a session token (valid), the billbaord
     *              name, the starting date of the method and also the duration (this is currently just in hrs format).
     *              This tests when the billboard does not exists and will raise an exception.
     * Expected Output: An exception
     */
//    public void scheduleBillboardRequestTest(){
//        billboardControl.createBillboardRequest("sampleToken", "Billboard1", xmlCode);
//        scheduleControl.scheduleBillboardRequest("sampleToken", "billboard",
//                "03/04/2020", "01:00");
//        if (serverResponse == "No Billboard"){
//          throw new EmptyValueException();
//        }
//    }


    /* Test 5: Request to server to Remove Billboard Schedule (Fail)
     * Description: Method to remove Billboard. Will require a valid sessiontoken, and name of billboard to remove.
     *              This test is to check if billboard does not exisits, and if the method can raise exception.
     * Expected Output: An exception
     */
//    public void scheduleBillboardRequestTest(){
//        billboardControl.createBillboardRequest("sampleToken", "Billboard1", xmlCode);
//        scheduleControl.scheduleBillboardRequest("sampleToken", "billboard",
//                "03/04/2020", "01:00");
//        if (serverResponse == "No Permission"){
//          throw new UserValueException();
//        }
//    }


    /* Test 6: Request to server to Remove Billboard Schedule (Fail)
     * Description: Method to remove Billboard. Will require a valid sessiontoken, and name of billboard to remove.
     *              This test is to check if billboard exisits, but user does not have suffice user permission.
     * Expected Output: An exception
     */
//    public void removeFromScheduleRequestTest(){
//        billboardControl.createBillboardRequest("sampleToken", "Billboard1", xmlCode);
//        scheduleControl.scheduleBillboardRequest("sampleToken", "billboard",
//                "03/04/2020", "01:00");
//        scheduleControl.removeFromScheduleRequest("sessionToken", "Billboard1");
//        assertTrue(serverResponse == "Success");
//    }


    /* Test 7: Request to server to Remove Billboard Schedule (Success)
     * Description: Method to remove Billboard. Will require a valid sessiontoken, and name of billboard to remove.
     * Expected Output: An success message from the server
     */
//    public void removeFromScheduleRequestTest(){
//        billboardControl.createBillboardRequest("sampleToken", "Billboard1", xmlCode);
//        scheduleControl.scheduleBillboardRequest("sampleToken", "billboard",
//                "03/04/2020", "01:00");
//        scheduleControl.removeFromScheduleRequest("sessionToken", "Billboard1");
//        assertTrue(serverResponse == "Success");
//    }


    /* Test 8: Request to server to view billboard Schedules (Nothing)
     * Description: Method to request to server to view billboard schedules. Assume sessionToken is valid.
     * Expected Output: Server will return nothing / raise exception
     * // TODO: *****ASK*****double check how schedules are stored for returns (arrays etc)
     */
//    public void viewScheduleRequestTest(){
//      BillboardSchedules billboardSchedules = viewScheduleRequest("sessionToken");
//        if (billboardSchedules == 0){
//          throw new EmptyValueException();
//        }
//    }

    /* Test 9: Request to server to view billboard Schedules (Success)
     * Description: Method to request to server to view billboard schedules. Assume sessionToken is valid.
     * Expected Output: Server will return a string of schedules
     * // TODO: *****ASK*****double check how schedules are stored for returns (arrays etc)
     */
//    public void viewScheduleRequestTest(){
//      billboardControl.createBillboardRequest("sampleToken", "Billboard1", xmlCode);
//      scheduleControl.schedulBillboardRequest("sampleToken", "Billboard1", "03/04/2020","01:00");
//      billboardControl.createBillboardRequest("sampleToken", "Billboard2", xmlCode);
//      scheduleControl.schedulBillboardRequest("sampleToken", "Billboard2", "04/04/2020","02:00");
//      BillboardSchedules billboardSchedules = viewScheduleRequest("sessionToken");
//      assertAll("Should return details of Given Billboard",
//                () -> assertArrayEquals(["Billboard1","Billboard2"], billboardSchedules.getBillboardName()),
//                () -> assertArrayEquals(["03/04/2020","04/04/2020"], billboardSchedules.getBillboardStartDate()),
//                () -> assertArrayEquals(["01:00","02:00"], billboardSchedules.getBillboardDuration()),
//    }


    /* Test 10: Request to server to View specific billboard information (Nothing)
     * Description: Method to request to server for specific billboard information
     * Expected Output: Server will raise exception
     * // TODO: *****ASK***** Check the return - and also is it similiar to the billboardcontrol
     */
//    public void viewABillboardScheduleRequest(String sessionToken, String billboard){
//        BillboardScheduleInformation billboardScheduleInformation = viewBillboardScheduleRequest("sessionToken",
//                "Billboard1");
//        if (billboardScheduleInformation == 0){
//          throw new EmptyValueException();
//        }
//    }


    /* Test 11: Request to server to View specific billboard information (Success)
     * Description: Method to request to server for specific billboard information
     * Expected Output: Server will return Start Date, Duration and End Date
     * // TODO: *****ASK***** Check the return - and also is it similiar to the billboardcontrol
     */
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