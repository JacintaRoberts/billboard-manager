package server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BillboardAdminTest {
    /* Test 0: Declaring BillboardAdmin object
     * Description: BillboardAdmin object should be running in background on application start.
     * Expected Output: BillboardAdmin object is declared
     */
    BillboardAdmin billboardAdmin;

    /* Test 1: Constructing a BillboardAdmin object
     * Description: BillboardAdmin Object should be able to be created to handle requests from control panel
     * Expected Output: BillboardAdmin object is instantiated from BillboardAdmin class
     */
    @BeforeEach
    @Test
    public void setUpBillboardAdmin() {
        billboardAdmin = new BillboardAdmin();
    }

    /* Test 2: Declaring Database Mock object
     * Description: Database Mock simulates Maria DB for testing purposes.
     * Expected Output: DatabaseMock object for Billboard Table is declared
     */
    MockDatabase<String, ArrayList<String>> billboardTableMock;

    /* Test 3: Constructing a DatabaseMock object for Billboard Table
     * Description: DatabaseMock should be used to verify unit tests and setup of the DB
     * Expected Output: DatabaseMock object is instantiated to mimic Billboard Table
     */
    @BeforeEach
    @Test
    public void setUpBillboardTableMock() {
        billboardTableMock = new MockDatabase<>();
        // Add a dummy value (insert more as necessary or create separate function to populate)
        billboardTableMock.addValue("myBillboard", new ArrayList<String>( Arrays.asList("Creator",
                                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?><billboard background=\"#0000FF\">" +
                                            "<message colour=\"#FFFF00\">Welcome!</message></billboard>")));
    }


    /* Test 4: Create Billboard in Billboard Table (Success)
     * Description: Receive create billboard request from CP, will have sessionToken, billboard name, and xml.
     *              Assume sessionToken is valid.
     * Expected Output: Billboard is added to the table and returns "Pass: Billboard Created"
     */
    @Test
    public void createABillboard() throws IOException, SQLException, BillboardAdmin.illegalBillboardNameException {
        String xmlCode = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<billboard>\n" +
                "    <message>Basic message-only billboard</message>\n" +
                "</billboard>";
        String userName = "testUser1";
        String billboardName = "Billboard1";

        String dbResponse = billboardAdmin.createBillboard( userName, billboardName, xmlCode);
        assertEquals(dbResponse, "Pass: Billboard Created");
        billboardAdmin.close();
    }


    /* Test 5: Create Billboard - Duplicate Billboard Name (Exception Handling)
     * Description: Receive create billboard request from CP, will have sessionToken, billboard name, and xml.
     *              Expect Fail due to Invalid Name (same name). Assume sessionToken is valid.
     * Expected Output: Billboard is not added to the table and returns "Fail: Billboard Name Already Exists"
     * // TODO: Investigate if there is a way to check for a more specific JDBC SQL error
     */
    @Test
    public void createSameNameBillboard() throws IOException, SQLException {
        String xmlCode = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<billboard>\n" +
                "    <message>Basic message-only billboard</message>\n" +
                "</billboard>";
        String userName = "testUser1";
        String billboardName = "Billboard1";


        assertThrows(SQLException.class, () -> {
            String dbResponse = billboardAdmin.createBillboard( userName, billboardName, xmlCode);
        });

        billboardAdmin.close();
    }


    /* Test 6: Create Billboard - Illegal Characters in Billboard Name (Exception Handling)
     * Description: Receive create billboard request from CP, will have sessionToken, billboard name, and xml.
     *          Expect Fail due to Invalid Name (Illegal Characters). Assume sessionToken is valid.
     * Expected Output: Billboard is not added to the table and returns "Fail: Billboard Name Already Exists"
     */
    @Test
    public void createIllegalNameBillboard(){
        String xmlCode = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<billboard>\n" +
                "    <message>Basic message-only billboard</message>\n" +
                "</billboard>";
        String userName = "testUser1";
        String billboardName = "ksdj@$#(_%I";

        assertThrows(BillboardAdmin.illegalBillboardNameException.class, () -> {
            String dbResponse = billboardAdmin.createBillboard( userName, billboardName, xmlCode);
        });


    }


    /* Test 7: Edit Billboard in Billboard Table (Success)
     * Description: Receive edit billboard request from Control Panel, need to have suffice user permission to edit
     *              board and user needs to provide a new xmlCode. Assume sessionToken is valid.
     * Expected Output: Billboard is edited in the table and returns "Pass: Billboard Edited"
     */
    @Test
    public void editABillboard() throws SQLException, IOException, BillboardAdmin.illegalBillboardNameException, BillboardAdmin.BillboardNotExistException {
        String xmlCode = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<billboard>\n" +
                "    <message>Basic message-only billboard</message>\n" +
                "</billboard>";
        String billboardName = "TestBillboard2";

        String dbResponse = billboardAdmin.editBillboard(billboardName, xmlCode);
        assertEquals(dbResponse, "Pass: Billboard Edited");
        billboardAdmin.close();
    }


    /* Test 8: Edit Billboard - Insufficient User Permission (Exception Handling)
     * Description: Receive edit billboard request from Control Panel, need to have suffice user permissions to edit
     *              board and user needs to provide a new xmlCode. Assume sessionToken is valid.
     *              Fail due to insufficient permission.
     * Expected Output: Billboard is not edited in the table and returns "Fail: Insufficient User Permission"
     */
//    @Test
//    public void editABillboardNoPermission(){
//        String dbResponse = billboardAdmin.editBillboard("basicToken", "Billboard1", xmlCode);
//        assertEquals(dbResponse, "Fail: Insufficient User Permission");
//    }


    /* Test 9: Edit Billboard - Billboard Name Does Not Exist (Exception Handling)
     * Description: Receive edit billboard request from Control Panel, need to have suffice user permissions to edit
     *              board and user needs to provide a new xmlCode. Assume sessionToken is valid.
     *              Fail due to non-existent board (does not exists or was deleted before clicking button).
     * Expected Output: Billboard is not edited in the table and returns "Fail: Billboard Does not Exist"
     * // TODO: Weird Assertion here
     */
    @Test
    public void editABillboardNoBillboard() throws SQLException, IOException,
            BillboardAdmin.illegalBillboardNameException,
            BillboardAdmin.BillboardNotExistException {
        String xmlCode = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<billboard>\n" +
                "    <message>Basic message-only billboard</message>\n" +
                "</billboard>";
        String billboardName = "ThisDosentExistdddd";

        assertThrows(BillboardAdmin.BillboardNotExistException.class, () -> {
            String dbResponse = billboardAdmin.editBillboard(billboardName, xmlCode);
        });

        billboardAdmin.close();

    }


    /* Test 10: Delete Billboard in Billboard Table (Success)
     * Description: Receive delete billboard request from CP. Assume sessionToken is valid, and target exists.
     * Expected Output: Billboard is deleted in the table and returns "Pass: Billboard Deleted"
     */
    @Test
    public void deleteABillboard() throws SQLException, IOException, BillboardAdmin.illegalBillboardNameException, BillboardAdmin.BillboardNotExistException {
    String billboardName = "TestBillboard2";
    String dbResponse = billboardAdmin.deleteBillboard(billboardName);
    assertEquals(dbResponse, "Pass: Billboard Deleted");
        billboardAdmin.close();
    }


    /* Test 11: Delete Billboard - Billboard Name Does Not Exist (Exception Handling)
     * Description: Receive delete billboard request from CP. Assume sessionToken is valid, and target exists.
     *              Fail due to non-existent billboard name requested.
     * Expected Output: Billboard is not deleted in the table and returns "Fail: Billboard Does not Exist"
     */
    @Test
    public void deleteABillboardNoBillboard(){
        String billboardName = "ThisDosentExistdddd";
        assertThrows(BillboardAdmin.BillboardNotExistException.class, () -> {
            String dbResponse = billboardAdmin.deleteBillboard(billboardName);
        });
        billboardAdmin.close();
    }


    /* Test 12: Delete Billboard - Insufficient Permissions (Exception Handling)
     * Description: Receive delete billboard request from CP. Assume sessionToken is valid, and target exists.
     *              Fail due to insufficient permissions.
     * Expected Output: Billboard is not deleted in the table and returns "Fail: Insufficient User Permission"
     */
//    @Test
//    public void deleteABillboardNoPermission(){
//        String dbResponse = billboardAdmin.deleteBillboard("basicToken","Billboard1");
//        assertEquals(dbResponse, "Fail: Insufficient User Permission");
//    }


    /* Test 13: List Billboards from Billboard Table (Success)
     * Description: Receive list billboard request from CP. Assume sessionToken is valid. Returns an array
     *              list of billboards.
     * Expected Output: Returns billboard names array and "Pass: Billboard List Returned"
     * //TODO: Set up mock db to have a Billboard1, Billboard2 and Billboard3 as below with dummy xml code
     */
    @Test
    public void listAllBillboard() throws BillboardAdmin.emptyBillboardTable, SQLException, IOException {
        List<String> testBillboardList = new ArrayList<String>();
        // Set test cases
        testBillboardList.add("TestBillboard");
        testBillboardList.add("TestBillboard2");
        testBillboardList.add("TestBillboard3");


        BillboardList billboardList = billboardAdmin.listBillboard();
        assertEquals(billboardList.getServerResponse(),"Pass: Billboard List Returned");
        assertArrayEquals(testBillboardList.toArray(),billboardList.getBillboardNames().toArray());
    }


    /* Test 14: List Billboards - Billboard Name Does Not Exist (Exception Handling)
     * Description: Receive list billboard request from CP. Assume sessionToken is valid.
     *              Fails because there is no billboard to return (empty).
     * Expected Output: Returns billboard names array and "Fail: No Billboard Exists"
     * //TODO: See throws or nay
     */
    @Test
    public void listAllBillboardNoBillboard() throws BillboardAdmin.emptyBillboardTable, SQLException, IOException {

        // Required if table is not empty
//        billboardAdmin.deleteAllBillboard();

//        BillboardList billboardList = billboardAdmin.listBillboard();
//        assertEquals(billboardList.getServerResponse(),"Fail: No Billboard Exists");
//        assertTrue(billboardList.getBillboardNames().get(0).equals("0"));

        assertThrows(BillboardAdmin.emptyBillboardTable.class, () -> {
            BillboardList billboardList = billboardAdmin.listBillboard();
        });


    }


    /* Test 15: Billboard Information from Billboard Table (Success)
     * Description: Receive billboard information request from CP. Information provided will include
     *              information such as billboardName, Creator, xmlCode.
     * Expected Output: Returns Billboard Information object and "Pass: Billboard Info Returned"
     */
    @Test
    public void getABillboardInformationPass() throws BillboardAdmin.illegalBillboardNameException, SQLException, BillboardAdmin.BillboardNotExistException, IOException {
        // Use below code to create if needed
        //        billboardAdmin.createBillboard("User1", "Billboard1", "xmlCode");

        DbBillboard billboardInformation = billboardAdmin.getBillboardInformation("Billboard1");
        System.out.println();
        System.out.println();
        System.out.println();
        assertAll("Should return details of Given Billboard",
                () -> assertEquals("User1", billboardInformation.getCreator()),
                () -> assertEquals("Billboard1", billboardInformation.getBillboardName()),
                () -> assertEquals("xmlCode", billboardInformation.getXMLCode()),
                () -> assertEquals("Pass: Billboard Info Returned", billboardInformation.getReturnString())
        );
    }


    /* Test 16: Billboard Information - Billboard Name Does Not Exist (Exception Handling)
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


    /* Test 17: Billboard Information - Insufficient Permissions (Exception Handling)
     * Description: Receive billboard information request from CP. Information provided will include
     *              information such as billboardName, Creator, xmlCode.
     *              The method will fail because no permission error.
     * Expected Output: Return string failure message of "Fail: Insufficient User Permission"
     */
//    @Test
//    public void getABillboardInformationNoPermission(){
//        BillboardInformation dbResponse = billboardAdmin.getBillboardInformation("basicToken","Billboard500");
//        assertEquals(billboardInformation.getServerResponse(), "Fail: Insufficient User Permission");
//    }


}