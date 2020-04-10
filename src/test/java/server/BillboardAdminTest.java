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

//TODO: Create Billboards - change sessionToken appears first for everything
    /* Test 3: Create Billboard - Insert into Billboard Table (Success)
     * Description: Receive create billboard request from Control Panel, will have billboard name, creator and xml.
     *              Assume sessionToken is valid.
     * Expected Output: Billboard is added to the database and returns "Pass: Billboard Created"
     */
//    @Test
//    public void createABillboardRequest(){
//        String serverResponse = billboardControl.createBillboardRequest("Billboard1", xmlCode);
//        assertEquals(serverResponse, "Pass: Billboard Created");
//    }


    /* Test 3: Request to server to create Billboard (Exception Handling)
     * Description: Method to request server to create a new sample billboard from a given xmlCode. Expect Fail due to
     *              Invalid Name (same name). Assume sessionToken is valid.
     * Expected Output: A negative reply from Server noting the failure of request
     */
//    @Test
//    public void createSameNameBillboardRequest(){
//        String serverResponse = billboardControl.createBillboardRequest("sampleToken", "Billboard1", xmlCode);
//        assertEquals(serverResponse, "Billboard Name Taken");
//        assertThrows(SameBillboardNameException);
//    }


    /* Test 4: Request to server to create Billboard (Exception Handling)
     * Description: Method to request server to create a new sample billboard from a given xmlCode. Expect Fail due to
     *              Invalid Name (Illegal Characters). Assume sessionToken is valid.
     * Expected Output: A negative reply from Server noting the failure of request
     */
//    @Test
//    public void createIllegalNameBillboardRequest(){
//        String serverResponse = billboardControl.createBillboardRequest("sampleToken", "Billboard1", xmlCode);
//        assertEquals(serverResponse, "Billboard Contains Illegal Character");
//        assertThrows(IllegalBillboardNameException);
//    }


}