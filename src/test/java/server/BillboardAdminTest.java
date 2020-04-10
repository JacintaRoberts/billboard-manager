package server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

class BillboardAdminTest {
    /* Test 0: Declaring BillboardAdmin object
     * Description: BillboardAdmin object should be running in background on application start.
     * Expected Output: BillboardAdmin object is declared
     */
    BillboardAdmin billboardAdmin;
    DatabaseMock<String, ArrayList<String>> billboardTableMock;

    /* Test 1: Constructing a BillboardAdmin object
     * Description: BillboardAdmin Object should be able to be created to handle requests from control panel
     * Expected Output: BillboardAdmin object is instantiated from BillboardAdmin class
     */
    @BeforeEach
    @Test
    public void setUpBillboardAdmin() {
        billboardAdmin = new BillboardAdmin();
    }

    /* Test 2: Constructing a DatabaseMock object for Billboard Table
     * Description: DatabaseMock should be used to verify unit tests and setup of the DB
     * Expected Output: DatabaseMock object is instantiated to mimic Billboard Table
     */
    @BeforeEach
    @Test
    public void setUpUserTableMock() {
        billboardTableMock = new DatabaseMock<>();
        // Add a dummy value (insert more as necessary or create separate function to populate)
        billboardTableMock.addValue("myBillboard", new ArrayList<String>( Arrays.asList("Creator",
                                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?><billboard background=\"#0000FF\">" +
                                            "<message colour=\"#FFFF00\">Welcome!</message></billboard>")));
    }


    /* Test 3: Create Billboard in Billboard Table (Success)
     * Description: Receive create billboard request from Control Panel, will have billboard name, user, and xml.
     *              Assume sessionToken is valid.
     * Expected Output: Billboard is added to the table and returns "Pass: Billboard Created"
     */
//    @Test
//    public void createABillboardRequest(){
//        String serverResponse = billboardAdmin.createBillboardRequest("Billboard1", "userFromSessionToken", xmlCode);
//        assertEquals(serverResponse, "Pass: Billboard Created");
//    }


    /* Test 4: Create Billboard - Duplicate Billboard Name (Exception Handling)
     * Description: Receive create billboard request from Control Panel, will have billboard name, user, and xml.
     *              Expect Fail due to Invalid Name (same name). Assume sessionToken is valid.
     * Expected Output: Billboard is not added to the table and returns "Fail: Billboard Name Already Exists"
     * // TODO: Investigate if there is a way to check for a more specific JDBC SQL error
     */
//    @Test
//    public void createSameNameBillboardRequest(){
//        String serverResponse = billboardAdmin.createBillboardRequest("Billboard1", "userFromSessionToken", xmlCode);
//        assertEquals(serverResponse, "Fail: Billboard Name Already Exists");
//        // Assert that the DB throws an SQL Exception for duplicate key
//        assertThrows(SQLException);
//    }



    /* Test 5: Create Billboard - Illegal Characters in Billboard Name (Exception Handling)
     * Description: Receive create billboard request from Control Panel, will have billboard name, user, and xml.
     *          Expect Fail due to Invalid Name (Illegal Characters). Assume sessionToken is valid.
     * Expected Output: Billboard is not added to the table and returns "Fail: Billboard Name Already Exists"
     * // TODO: WE SHOULD PROB TELL THE USER ON THE GUI NOT TO ENTER WEIRD CHARS BUT JUST CHECK ANYWAY IN CASE THEY
     *     ESCAPE THEM - ALSO WORK OUT WHAT CHARACTERS ARE BAD FOR MARIA DB/JAVA AND REPLACE THE DUMMY ONES I HAVE
     */
//    @Test
//    public void createIllegalNameBillboardRequest(){
//        String serverResponse = billboardAdmin.createBillboardRequest("!@#$%^&*()~", "userFromSessionToken", xmlCode);
//        assertEquals(serverResponse, "Fail: Billboard Contains Illegal Character");
//        // Have to create this exception ourselves - if (name.contains("!@#$%^&*()~)) throw Exception ->
//        assertThrows(IllegalBillboardNameException);
//    }


    /* Test 6: Edit Billboard in Billboard Table (Success)
     * Description: Receive edit billboard request from Control Panel, need to have suffice user permission to edit
     *              board and user needs to provide a new xmlCode. Assume sessionToken is valid.
     * Expected Output: Billboard is edited in the table and returns "Pass: Billboard Edited"
     */
//    @Test
//    public void editABillboardRequest(){
//        String serverResponse = billboardAdmin.editBillboardRequest("Billboard1", "userFromSessionToken", xmlCode);
//        assertEquals(serverResponse, "Pass: Billboard Edited");
//    }


    /* Test 7: Edit Billboard - Insufficient User Permission (Exception Handling)
     * Description: Receive edit billboard request from Control Panel, need to have suffice user permissions to edit
     *              board and user needs to provide a new xmlCode. Assume sessionToken is valid.
     *              Fail due to insufficient permission.
     * Expected Output: Billboard is not edited in the table and returns "Fail: Insufficient User Permission"
     */
//    @Test
//    public void editABillboardRequestNoPermission(){
//        String serverResponse = billboardAdmin.editBillboardRequest("Billboard1", "userFromSessionToken", xmlCode);
//        assertEquals(serverResponse, "Fail: Insufficient User Permission");
//    }


    /* Test 8: Edit Billboard - Billboard Name Does Not Exist (Exception Handling)
     * Description: Receive edit billboard request from Control Panel, need to have suffice user permissions to edit
     *              board and user needs to provide a new xmlCode. Assume sessionToken is valid.
     *              Fail due to non-existent board (does not exists or was deleted before clicking button).
     * Expected Output: Billboard is not edited in the table and returns "Fail: Billboard Does not Exist"
     * // TODO: Investigate if there is a way to check for a more specific JDBC SQL error
     */
//    @Test
//    public void editABillboardRequestNoBillboard(){
//        String serverResponse = billboardAdmin.editBillboard("Billboard1", "userFromSessionToken", xmlCode);
//        assertEquals(serverResponse, "Fail: Billboard Does not Exist");
//        // Billboard Name does not exist in DB
//        assertThrows(SQLException);
//    }


    /* Test 9: Delete Billboard in Billboard Table (Success)
     * Description: Receive delete billboard request from CP. Assume sessionToken is valid, and target exists.
     * Expected Output: Billboard is deleted in the table and returns "Pass: Billboard Deleted"
     */
//    @Test
//    public void deleteABillboard(){
//        String serverResponse = billboardAdmin.deleteBillboard("sampleSessionToken", "Billboard1");
//        // sampleSessionToken required to be passed in to check permissions inside function
//        assertEquals(serverResponse, "Pass: Billboard Deleted");
//    }


    /* Test 10: Delete Billboard - Billboard Name Does Not Exist (Exception Handling)
     * Description: Receive delete billboard request from CP. Assume sessionToken is valid, and target exists.
     *              Fail due to non-existent billboard name requested.
     * Expected Output: Billboard is not deleted in the table and returns "Fail: Billboard Does not Exist"
     * // TODO: Investigate if there is a way to check for a more specific JDBC SQL error
     */
//    @Test
//    public void deleteABillboardNoBillboard(){
//        String serverResponse = billboardAdmin.deleteBillboard("sampleSessionToken","Billboard1");
//        assertEquals(serverResponse, "Fail: Billboard Does Not Exist");
//        // Billboard Name does not exist in DB
//        assertThrows(SQLException);
//    }


    /* Test 11: Delete Billboard - Insufficient Permissions (Exception Handling)
     * Description: Receive delete billboard request from CP. Assume sessionToken is valid, and target exists.
     *              Fail due to insufficient permissions.
     * Expected Output: Billboard is not deleted in the table and returns "Fail: Insufficient User Permission"
     */
//    @Test
//    public void deleteABillboardNoPermission(){
//        String serverResponse = billboardAdmin.deleteBillboard("sampleSessionToken","Billboard1");
//        assertEquals(serverResponse, "Fail: Insufficient User Permission");
//    }


    /* Test 12: List Billboards from Billboard Table (Success)
     * Description: Receive list billboard request from CP. Assume sessionToken is valid. Returns an array
     *              list of billboards.
     * Expected Output: Returns billboard names array and "Pass: Billboard List Returned"
     * //TODO: Set up mock db to have a Billboard1, Billboard2 and Billboard3 as below with dummy xml code
     */
//    @Test
//    public void listAllBillboard(){
//        List<String> testBillboardList = new ArrayList<String>();
//        testBillboardList.add("Billboard1");
//        testBillboardList.add("Billboard2");
//        testBillboardList.add("Billboard3");
//        BillboardList billboardList = billboardAdmin.listBillboards("sessionToken");
//        assertEquals(billboardList.getServerResponse(),"Pass: Billboard List Returned");
//        assertArrayEquals(testBillboardList, billboardList.getBillboardList());
//    }


    /* Test 13: List Billboards - Billboard Name Does Not Exist (Exception Handling)
     * Description: Receive list billboard request from CP. Assume sessionToken is valid.
     *              Fails because there is no billboard to return (empty).
     * Expected Output: Returns billboard names array and "Fail: No Billboard Exists"
     * //TODO: Ensure that the MockDB is empty for this test
     * // TODO: Investigate if there is a way to check for a more specific JDBC SQL error
     */
//    @Test
//    public void listAllBillboardNoBillboard(){
//        BillboardList billboardList = billboardAdmin.listBillboardRequest("sessionToken");
//        assertTrue(billboardList.getBillboardList() == 0);
//        assertEquals(billboardList.getServerResponse(),"Fail: No Billboard Exists");
//        // Billboard Name does not exist in DB
//        assertThrows(SQLException);
//    }


    /* Test 14: Billboard Information from Billboard Table (Success)
     * Description: Receive billboard information request from CP. Information provided will include
     *              information such as billboardName, Creator, xmlCode.
     * Expected Output: Returns Billboard Information object and "Pass: Billboard Info Returned"
     */
//    @Test
//    public void getABillboardInformationPass(){
//        billboardAdmin.createBillboardRequest("sampleToken", "Billboard1", xmlCode);
//        BillboardInformation billboardInformation = billboardAdmin.getBillboardInformation("sessionToken","Billboard1");
//        assertAll("Should return details of Given Billboard",
//                () -> assertEquals("Pass: Billboard Info Returned", billboardInformation.getServerResponse()),
//                () -> assertEquals("Billboard1", billboardInformation.getBillboardName()),
//                () -> assertEquals("CAB302", billboardInformation.getBillboardCreator()),
//                () -> assertEquals(xmlCode, billboardInformation.getBillboardXML())
//        );
//    }


    /* Test 15: Billboard Information - Billboard Name Does Not Exist (Exception Handling)
     * Description: Receive billboard information request from CP. Information provided will include
     *              information such as billboardName, Creator, xmlCode.
     *              The method will fail because the billboard does not exist.
     * Expected Output: Returns string failure message of "Fail: Billboard Does Not Exist"
     * // TODO: Investigate if there is a way to check for a more specific JDBC SQL error
     */
//    @Test
//    public void getABillboardInformationNoBillboard(){
//        BillboardInformation billboardInformation = billboardAdmin.getBillboardInformation("sessionToken","Billboard500");
//        assertTrue(billboardInformation.length == 0);
//        assertEquals(billboardInformation.getServerResponse(), "Fail: Billboard Does Not Exist")
//        // Billboard Name does not exist in DB
//        assertThrows(SQLException);
//    }


    /* Test 16: Billboard Information - Insufficient Permissions (Exception Handling)
     * Description: Receive billboard information request from CP. Information provided will include
     *              information such as billboardName, Creator, xmlCode.
     *              The method will fail because no permission error.
     * Expected Output: Return string failure message of "Fail: Insufficient User Permission"
     */
//    @Test
//    public void getABillboardInformationNoPermission(){
//        BillboardInformation serverResponse = billboardAdmin.getBillboardInformation("sessionToken","Billboard500");
//        assertEquals(billboardInformation.getServerResponse(), "Fail: Insufficient User Permission");
//    }





}