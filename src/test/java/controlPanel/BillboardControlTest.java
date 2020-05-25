package controlPanel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.BillboardList;
import server.DbBillboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BillboardControlTest {

    /* Test 0: Declaring BillboardControl object
     * Description: BillboardControl object should be running in background on application start.
     * Expected Output: BillboardControl object is declared
     */
    BillboardControl billboardControl;

    /* Test 1: Constructing a BillboardControl object
     * Description: BillboardControl Object should be able to be created on logged in user request from control panel
     * Expected Output: BillboardControl object is instantiated from BillboardControl class
     */
    @BeforeEach
    @Test
    public void setUpBillboardControl() {
      billboardControl = new BillboardControl();
    }


    /* Test 2: Request to server to create Billboard (Success)
     * Description: Method to request server to create a new sample billboard from a given xmlCode.
     *              Assume sessionToken is valid. xmlCode is a variable that stores the xmlCode either loaded in or
     *              autogenerated.
     * Expected Output: A positive reply from Server noting the success of request
     */
    @Test
    public void createABillboardRequest() throws IOException, ClassNotFoundException {
        //TODO: ENSURE THE BILLBOARD DOES NOT ALREADY EXIST IN DB FOR INTEGRATED TESTING :)
        BillboardControl.deleteAllBillboardRequest("sampleToken");
        String xmlCode = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<billboard>\n" +
                "    <information>Billboard with an information tag, and nothing else. Note that the text is word-wrapped. The quick brown fox jumped over the lazy dogs.</information>\n" +
                "</billboard>";
        String serverResponse = billboardControl.createBillboardRequest("sampleToken","newBillboard1", xmlCode);
        assertEquals( "Pass: Billboard Created", serverResponse);
        getABillboardInformationRequestPass();
    }


    /* Test 3: Request to server to create Billboard (Exception Handling)
     * Description: Method to request server to create a new sample billboard from a given xmlCode. Expect Fail due to
     *              Invalid Name (same name). Assume sessionToken is valid.
     * Expected Output: A negative reply from Server noting the failure of request
     */
    @Test
    public void createSameNameBillboardRequest() throws IOException, ClassNotFoundException {
        //String serverResponse = billboardControl.createBillboardRequest("sampleToken", "newBillboard1", "xmlCode");
        //assertEquals(serverResponse, "Fail: Billboard Already Exists");
    }


    /* Test 4: Request to server to create Billboard (Exception Handling)
     * Description: Method to request server to create a new sample billboard from a given xmlCode. Expect Fail due to
     *              Invalid Name (Illegal Characters). Assume sessionToken is valid.
     * Expected Output: A negative reply from Server noting the failure of request
     */
    @Test
    public void createIllegalNameBillboardRequest() throws IOException, ClassNotFoundException {
        //String serverResponse = billboardControl.createBillboardRequest("sampleToken", "Billboard1#$@#%", "xmlCode");
        //assertEquals(serverResponse, "Fail: Billboard Name Contains Illegal Characters");
    }


    /* Test 5: Request to server to edit a billboard (Success)
     * Description: Method to request server to edit a billboard. Need to have suffice user permission to edit said
     *              board and user needs to provide a new xmlCode. Assume sessionToken is valid.
     * Expected Output: A positive reply from Server noting the success of request
     */
    @Test
    public void editABillboardRequest() throws IOException, ClassNotFoundException {
        String serverResponse = billboardControl.editBillboardRequest("sampleToken", "newBillboard1", "xmlCode2");
        assertEquals(serverResponse, "Pass: Billboard Edited");
    }


    /* Test 6: Request to server to edit a billboard (Exception Handling)
     * Description: Method to request server to edit a billboard. Need to have suffice user permissions to edit said
     *              board and user needs to provide a new xmlCode. Assume sessionToken is valid. Fail due to insufficient
     *              permission
     * Expected Output: A negative reply from Server noting the failure of request
     */
//    @Test
//    public void editABillboardRequestNoPermission() throws IOException, ClassNotFoundException {
//        String serverResponse = billboardControl.editBillboardRequest("sampleToken", "Billboard1", "xmlCode");
//        assertEquals(serverResponse, "Fail: Insufficient User Permission");
//    }


    /* Test 7: Request to server to edit a billboard (Exception Handling)
     * Description: Method to request server to edit a billboard. Need to have suffice user permissions to edit said
     *              board and user needs to provide a new xmlCode. Assume sessionToken is valid. Fail due to insufficient
     *              board cannot be edited (does not exists).
     * Expected Output: A negative reply from Server noting the failure of request
     */
    @Test
    public void editABillboardRequestNoBillboard() throws IOException, ClassNotFoundException {
        String serverResponse = billboardControl.editBillboardRequest("sampleToken", "Billboard444", "xmlCode");
        assertEquals(serverResponse, "Fail: Billboard Does not Exist");
    }


    /* Test 8: Request to server to delete a billboard (Success)
     * Description: Method to request a server to delete a billboard. Assume sessionToken is valid, and target exists.
     * Expected Output: A positive reply from server of successful deletion.
     */
//    @Test
//    public void deleteABillboardRequest() throws IOException, ClassNotFoundException {
//        String serverResponse = billboardControl.createBillboardRequest("sampleToken", "newBillboard1234", "xmlCode");
//        String serverResponse2 = billboardControl.deleteBillboardRequest("sampleSessionToken","newBillboard1234");
//        assertEquals(serverResponse2, "Pass: Billboard Deleted");
//    }


    /* Test 9: Request to server to delete a billboard (Exception Handling)
     * Description: Method to request a server to delete a billboard. Assume sessionToken is valid. The method will
     *              Fail due to  billboard does not exists
     * Expected Output: A negative reply from server of failure to delete.
     */
//    @Test
//    public void deleteABillboardRequestNoBillboard() throws IOException, ClassNotFoundException {
//        String serverResponse = billboardControl.deleteBillboardRequest("sampleSessionToken","nonexistBillboard");
//        assertEquals(serverResponse, "Fail: Billboard Does Not Exist");
//    }


    /* Test 10: Request to server to delete a billboard (Exception Handling)
     * Description: Method to request a server to delete a billboard. Assume sessionToken is valid. The method will
     *              throw an exception due to insufficient permissions
     * Expected Output: A negative reply from server of failure to delete. Returns "Fail: Insufficient User Permission"
     */
//    @Test
//    public void deleteABillboardRequestNoPermission(){
//        String serverResponse = billboardControl.deleteBillboardRequest("sampleSessionToken","Billboard1");
//        assertEquals(serverResponse, "Fail: Insufficient User Permission");
//    }


    /* Test 11: Request to server to list billboards (Success)
     * Description: Method to request to server to list all billboards. Assume sessionToken is valid. Returns an array
     *              list of billboards
     * Expected Output: A server response with the list of billboard as an array containing billboard names.
     */
    @Test
    public void listAllBillboardRequest() throws IOException, ClassNotFoundException {
        BillboardControl.deleteAllBillboardRequest("sampleToken");
        //billboardControl.createBillboardRequest("sampleToken", "Billboard1", "xmlCode");
        //billboardControl.createBillboardRequest("sampleToken", "Billboard2", "xmlCode");
        //billboardControl.createBillboardRequest("sampleToken", "Billboard3", "xmlCode");
        List<String> testBillboardList = new ArrayList<String>();
        testBillboardList.add("Billboard1");
        testBillboardList.add("Billboard2");
        testBillboardList.add("Billboard3");
        server.BillboardList billboardInformation = (BillboardList) billboardControl.listBillboardRequest("sampleToken");

        assertAll("Should return details of Given Billboard",
                () -> assertEquals(billboardInformation.getServerResponse(),"Pass: Billboard List Returned"),
                () -> assertArrayEquals(testBillboardList.toArray(), billboardInformation.getBillboardNames().toArray())
        );
    }


    /* Test 12: Request to server to list billboards (Exception Handling)
     * Description: Method to request to server to list all billboards. Assume sessionToken is valid. Fails because
     *              there is no billboard to return
     * Expected Output: Fail message.
     */
//    @Test
//    public void listAllBillboardRequestNoBillboard(){
//        BillboardList billboardList = billboardControl.listBillboardRequest("sessionToken");
//        assertTrue(billboardList.getBillboardList() == 0);
//        assertEquals(billboardList.getServerResponse(),"Fail: No Billboard Exists");
//    }


    /* Test 13: Request to server to get billboard information (Success)
     * Description: A method to request billboard information of a given billboard - Information provided will include
     *              information such as billboardName, Creator, xmlCode.
     * Expected Output: Return of billboard information such as billboardName, Creator, xmlCode
     */
    @Test
    public void getABillboardInformationRequestPass() throws IOException, ClassNotFoundException {
        BillboardControl.deleteAllBillboardRequest("sampleToken");
        //billboardControl.createBillboardRequest("sampleToken", "Billboard1", "xmlCode");
        server.DbBillboard billboardInformation = (DbBillboard) billboardControl.getBillboardRequest("sampleToken","Billboard1");
        assertAll("Should return details of Given Billboard",
                () -> assertEquals("Pass: Billboard Info Returned", billboardInformation.getReturnString()),
                () -> assertEquals("Billboard1", billboardInformation.getBillboardName()),
                () -> assertEquals("userNameReturn", billboardInformation.getCreator()),
                () -> assertEquals("xmlCode", billboardInformation.getXMLCode())
        );
    }


    /* Test 14: Request to server to get billboard information (Exception Handling)
     * Description: A method to request billboard information of a given billboard - Information provided will include
     *              information such as billboardName, Creator, xmlCode. The method will fail because
     *              billboard does not exist
     * Expected Output: Return failure of return and message of "Fail: Billboard Does Not Exist"
     */
//    @Test
//    public void getABillboardInformationRequestNoBillboard(){
//        BillboardInformation billboardInformation = getBillboardInformationRequest("sessionToken","Billboard500");
//        assertTrue(billboardInformation.length == 0);
//        assertEquals(billboardInformation.getServerResponse(), "Fail: Billboard Does Not Exist")
//    }


    /* Test 15: Request to server to get billboard information (Exception Handling)
     * Description: A method to request billboard information of a given billboard - Information provided will include
     *              information such as billboardName, Creator, xmlCode. The method will fail because
     *              no permission error
     * Expected Output: Return failure saying "Fail: Insufficient User Permission"
     */
//    @Test
//    public void getABillboardInformationRequestNoPermission(){
//        BillboardInformation serverResponse = getBillboardInformationRequest("sessionToken","Billboard500");
//        assertEquals(billboardInformation.getServerResponse(), "Fail: Insufficient User Permission");
//    }

//    @Test

}