package server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

//public class ViewerSenderTest {

    /* Test 0: Declaring viewerSender object
     * Description: ViewerSender object should send currently scheduled billboard every 15s
     * Expected Output: ViewerSender object is declared
     */
//    ViewerSenderTest viewerSender;


/* Test 1: Constructing a ViewerSender object
     * Description: ViewerSender Object should be able to be created to send billboard to viewer
     * Expected Output: ViewerSender object is instantiated from ViewerSender class
     */
//    @BeforeEach
//    @Test
//    public void setUpViewerSender() {
//      viewerSender = new ViewerSenderTest();
//}


    /* Test 2: Declaring Database Mock objects
     * Description: Database Mock simulates Maria DB for testing purposes.
     * Expected Output: DatabaseMock object for Schedule and Billboard Table is declared
     */
//    MockScheduleTable scheduleTableMock;
//    MockBillboardTable billboardTableMock;


    /* Test 3: Constructing a DatabaseMock object for Schedule and Billboard Table
     * Description: DatabaseMock should be used to verify unit tests and setup of the DB
     * Expected Output: DatabaseMock object is instantiated to mimic Schedule and Billboard Table
     */
//    @BeforeEach
//    @Test
//    public void setUpScheduleAndBillboardTableMocks() {
//        scheduleTableMock = new MockScheduleTable();
//        billboardTableMock = new MockBillboardTable();
//        // Default value for testing
//        billboardTableMock.addBillboardTest("Billboard1", new ArrayList<>( Arrays.asList("Creator", "xml1")));
//        billboardTableMock.addBillboardTest("Billboard2", new ArrayList<>( Arrays.asList("Creator", "xml2")));
//        scheduleTableMock.addScheduleTest("Billboard1","01:00:00", "02:00:00",
//                                    "2020-04-14 18:00:00", "0", "0", "0",
//                                              "0", "1", "0", "0", "0");
//    }


    /* Test 4: Get Current Billboard From Schedule (Success)
     * Description: Receive view get current billboard request from Viewer every 15s.
     * Expected Output: Current billboard's xml is retrieved as string
     */
//    @Test
//    public void getCurrentBillboard(){
//      String dbResponse = viewerSender.getCurrentBillboard("2020-04-14 18:59:00");
//      assertEquals(dbResponse, "xml1");
//    }


    /* Test 5: Get Current Billboard From Schedule - Multiple Scheduled (Success)
     * Description: Receive view get current billboard request from Viewer every 15s.
     * Expected Output: Current billboard's xml is retrieved as string (if overlap, latest added is shown)
     */
//    @Test
//    public void getCurrentBillboardMultipleScheduled(){
//      // Billboard1 starts at 6pm and goes for an hour, Billboard2 starts at 6:30pm and goes for an hour ->
//      // shows Billboard1 6pm-6:30pm and then Billboard 1 6:30-7:30pm).
//      scheduleTableMock.addValue("2020-04-14 18:30:00", new ArrayList<String>( Arrays.asList("Billboard2", "01:00:00", "24:00:00")));
//      assertAll("Should return correct XML",
//                () -> assertEquals("xml1", viewerSender.getCurrentBillboard("2020-04-14 18:01:00")),
//                () -> assertEquals("xml2", viewerSender.getCurrentBillboard("2020-04-14 18:31:00")),
//                () -> assertEquals("xml2", viewerSender.getCurrentBillboard("2020-04-14 19:29:00"))
//      );
//   }


    /* Test 6: Get Current Billboard From Schedule - None Currently Scheduled (Exception Handling)
     * Description: Receive view get current billboard request from Viewer every 15s.
     * Expected Output: Empty xml string is returned as there is no current billboard to be displayed,
     *                  instead returns "Fail: No Current Billboard" to alert viewer to display error board.
     */
//    @Test
//    public void getCurrentBillboardNoCurrent(){
//      string dbResponse = viewerSender.getCurrentBillboard("2020-04-15 22:50:00");
//      assertEquals("Fail: No Current Billboard", dbResponse);
//   }


//}
