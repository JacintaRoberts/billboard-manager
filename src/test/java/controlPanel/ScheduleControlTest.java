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
    @BeforeEach
    @Test
//    public void setUpScheduleControl() {
//      scheduleControl = new ScheduleControl();
//    }


    /* Test 2: Request to server to view billboard Schedules
     * Description: Method to request to server to view billboard schedules. Assume sessionToken is valid.
     * Expected Output: Server will return a string of schedules
     * // TODO: *****ASK*****double check how schedules are stored for returns
     */
//    public void viewScheduleRequest(String sessionToken){
//
//    }



    /* Test x: Request to server to View specific billboard information
     * Description: Method to request to server for specific billboard information
     * Expected Output: Server will retrun ____ ?
     * // TODO: *****ASK***** Check the return
     */
    public void viewBillboardScheduleRequest(String sessionToken, String billboard){

    }



    /* Test x: Request to server to
     * Description:
     * Expected Output:
     */
    public void scheduleBillboardRequest(String sessionToken, String billboard, String time, String duration){

    }



    /* Test x: Request to server to
     * Description:
     * Expected Output:
     */
    public void removeFromScheduleRequest(String sessionToken, String billboard){

    }


}