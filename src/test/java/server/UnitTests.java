package server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;
import static server.Server.*;
import static server.Server.ServerAcknowledge.*;

/**
 * ================================================================================================
 * UNIT TESTS - MOCK TABLES TO REMOVE SQL/SERVER DEPENDENCY
 * ================================================================================================
 */

class UnitTests {
    /* Test 0: Declaring MockTables and MockSessionTokens Object
     * Description: MockUserTable object should be running in background on application start.
     * Expected Output: MockUserTable object and dummy testing data is declared
     */
    MockUserTable mockUserTable;
    MockBillboardTable mockBillboardTable;
    MockScheduleTable mockScheduleTable;
    MockSessionTokens mockSessionTokens;
    // Declaration and initialisation of testing variables
    private String mockToken;
    private String basicToken;
    private String callingUser = "callingUser";
    private String testUser = "testUser";
    private String basicUser = "basicUser";
    private String dummyHashedPassword = "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8";// hash("password");
    private Boolean createBillboard = true;
    private Boolean editBillboard = true;
    private Boolean scheduleBillboard = true;
    private Boolean editUser = true;
    // Defining permissions to be tested
    private ArrayList<Boolean> fullPermissions = new ArrayList<>(Arrays.asList(true, true, true, true));
    private ArrayList<Boolean> basicPermissions = new ArrayList<>(Arrays.asList(false, false, false, false));
    // Billboard Dummy Data
    private String billboardName="testBillboard";
    private String newBillboardName="newBillboard";
    private String billboardXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><billboard></billboard>";
    private byte[] pictureData = "f9lMAwAghdLKCgWHgMqOtwXHA2+YIDNzrhIG1JLk/stdh4I4IgYFTgoFv7yn+P7zBWVK5SO9cAAAAAElFTkSuQmCC".getBytes();
    // Schedule Dummy Data
    String startTime = "05:00";
    String duration = "30";
    String creationDateTime = "2020-05-18 12:55";
    String repeat = "120";
    String sunday = "1";
    String monday = "1";
    String tuesday = "0";
    String wednesday = "0";
    String thursday = "0";
    String friday = "0";
    String saturday = "0";


    /* Test 1: Constructing a MockUserTable and MockSessionTokens object
     * Description: MockUserTable and MockSessionTokens created and some initial testing data populated.
     * Expected Output: MockUserTable object able to be instantiated and populated with fake data.
     */
    @BeforeEach
    @Test
    public void setUpMockTables() throws NoSuchAlgorithmException {
        mockUserTable = new MockUserTable();
        mockBillboardTable = new MockBillboardTable();
        mockScheduleTable = new MockScheduleTable();
        mockSessionTokens = new MockSessionTokens();
        // Generate Dummy Data as required
        mockToken = mockSessionTokens.generateTokenTest(callingUser);
        basicToken = mockSessionTokens.generateTokenTest(basicUser);
        mockUserTable.createUserTest(mockToken, callingUser, dummyHashedPassword, createBillboard, editBillboard, scheduleBillboard, editUser);
        mockBillboardTable.createBillboardTest(mockToken, billboardName, callingUser, billboardXML, pictureData);
        mockScheduleTable.updateScheduleTest(mockToken, billboardName, startTime, duration, creationDateTime, repeat,
                                                sunday, monday, tuesday, wednesday, thursday, friday, saturday);
    }

    /**
     * ================================================================================================
     * USER UNIT TESTS
     * ================================================================================================
     */

    /* Test 2: Check User Exists (Pass)
     * Description: Check that a user exists in the MockUserTable, checks for case sensitivity, trailing whitespace,
     * empty etc.
     * Expected Output: Boolean true is returned if the user is found in the MockUserTable, and false otherwise.
     */
    @Test
    public void mockUserExists() {
        assertAll("Check for Existing User",
                // Ensure that these users don't exist in the Fake DB.
                () -> assertFalse(mockUserTable.userExistsTest("non-existent")),
                // Check for case sensitivity
                () -> assertFalse(mockUserTable.userExistsTest("callinguser")),
                // Check for trailing whitespace stripping
                () -> assertFalse(mockUserTable.userExistsTest("callingUser ")),
                // Check for empty
                () -> assertFalse(mockUserTable.userExistsTest("")),
                // Check for valid
                () -> assertTrue(mockUserTable.userExistsTest(callingUser))
        );
    }


    /* Test 3: Create User (Pass)
     * Description: Create the corresponding username in the MockUserTable with the hashed password and permissions
     * and return server acknowledgement.
     * Expected Output: User is created in the MockUserTable and returns Success server acknowledge.
     */
    @Test
    public void mockCreateUser() throws NoSuchAlgorithmException {
        ServerAcknowledge mockResponse = mockUserTable.createUserTest(mockToken, testUser, dummyHashedPassword, createBillboard, editBillboard, editBillboard, editUser);
        assertEquals(Success, mockResponse);
        // Check that the user is actually added to the MockUserTable
        assertTrue(mockUserTable.userExistsTest(testUser));
    }



    /* Test 4: Get Username From Token (Pass)
     * Description: Retrieve the username associated with the SessionToken in the MockSessionTokens.
     * Expected Output: Returns string username of the session token
     */
    @Test
    public void mockGetUsernameFromTokenTest() {
        String username = mockSessionTokens.getUsernameFromTokenTest(mockToken);
        assertEquals(callingUser, username);
    }


    /* Test 5: Get Permissions From Token (Pass)
     * Description: Retrieve the full user's own permissions associated with the SessionToken in the MockSessionTokens.
     * Expected Output: Returns an ArrayList of Booleans to indicate whether the username associated with the session
     * token has the corresponding permissions.
     */
    @Test
    public void mockGetOwnPermissionsFromTokenTest() {
        // Get own permissions
        ArrayList<Boolean> userPermisisons = (ArrayList<Boolean>) mockSessionTokens.mockGetPermissionsFromTokenTest(mockToken, callingUser);
        assertEquals(fullPermissions, userPermisisons);
    }


    /* Test 6: Get Other Permissions From Token (Pass)
     * Description: Retrieve the basic user's permissions associated with the SessionToken in the MockSessionTokens.
     * Expected Output: Returns an ArrayList of Booleans to indicate whether the username associated with the session
     * token has the corresponding permissions.
     */
    @Test
    public void mockGetOtherPermissionsFromTokenTest() throws NoSuchAlgorithmException {
        // Test Setup - Ensure the basicUser to be deleted exists in the MockUser Table
        mockUserTable.createUserTest(mockToken, basicUser, dummyHashedPassword, false, false, false, false);
        ArrayList<Boolean> userPermisisons = (ArrayList<Boolean>) mockSessionTokens.mockGetPermissionsFromTokenTest(mockToken, basicUser);
        assertEquals(basicPermissions, userPermisisons);
    }


    /* Test 7: Get Other Permissions From Token (Exception Handling)
     * Description: Retrieve the other user's permissions associated with the SessionToken in the MockSessionTokens,
     * calling user is basic and does not have the EditUsers permission required.
     * Expected Output: Returns a Server Acknowledgement for InsufficientPermission.
     */
    @Test
    public void mockGetOtherPermissionsFromTokenTestInsufficientPermission() throws NoSuchAlgorithmException {
        // Test Setup - Ensure the basicUser to be deleted exists in the MockUser Table
        mockUserTable.createUserTest(mockToken, basicUser, dummyHashedPassword, false, false, false, false);
        ServerAcknowledge userPermisisons = (ServerAcknowledge) mockSessionTokens.mockGetPermissionsFromTokenTest(basicToken, callingUser);
        assertEquals(InsufficientPermission, userPermisisons);
    }


    /* Test 8: Delete User (Pass)
     * Description: Delete the corresponding username in the Mock User Table and return server acknowledgement
     * Expected Output: User is deleted from MockUserTable and returns Success server acknowledgement.
     */
    @Test
    public void mockDeleteOtherUserTest() throws NoSuchAlgorithmException {
        // Test Setup - Ensure the testUser to be deleted exists in the MockUser Table
        mockUserTable.createUserTest(mockToken, testUser, dummyHashedPassword, createBillboard, editBillboard, editBillboard, editUser);
        ServerAcknowledge mockResponse = mockUserTable.deleteUserTest(mockToken, testUser);
        assertEquals(Success, mockResponse);
        // Check that the user is removed from the Mock DB
        assertFalse(mockUserTable.userExistsTest(testUser));
    }


    /* Test 9: Delete User (Exception Handling)
     * Description: Delete own username in the Mock User Table and return server acknowledgement
     * Expected Output: User is not deleted from MockUserTable and returns CannotDeleteSelf server acknowledgement.
     */
    @Test
    public void mockDeleteOwnUserTest() {
        ServerAcknowledge mockResponse = mockUserTable.deleteUserTest(mockToken, callingUser);
        assertEquals(CannotDeleteSelf, mockResponse);
        // Check that the user is not removed from the Mock DB anyway
        assertTrue(mockUserTable.userExistsTest(callingUser));
    }



    /* Test 10: Delete User (Exception Handling)
     * Description: Delete non-existent username in the Mock User Table and return server acknowledgement
     * Expected Output: User is not deleted from MockUserTable and returns NoSuchUser server acknowledgement.
     */
    @Test
    public void mockDeleteNonExistentUserTest() {
        ServerAcknowledge mockResponse = mockUserTable.deleteUserTest(mockToken, "non-existent");
        assertEquals(NoSuchUser, mockResponse);
    }

    /**
     * ================================================================================================
     * BILLBOARD UNIT TESTS
     * ================================================================================================
     */
//TODO: COULD ADD MORE LOGIC HERE FOR THE OTHER SERVER ACKNOWLEDGE RETURN TYPES
    /* Test 11: Create Billboard (Pass)
     * Description: Create the corresponding billboard in the MockBillboardTable with the billboard name, creator, xml
     * and picture data - return server acknowledgement.
     * Expected Output: Billboard is created in the MockBillboardTable and returns Success server acknowledge.
     */
    @Test
    public void mockCreateBillboardTest() {
        ServerAcknowledge mockResponse = mockBillboardTable.createBillboardTest(mockToken, newBillboardName, callingUser, billboardXML, pictureData);
        assertEquals(Success, mockResponse);
        // Check that the billboard is actually added to the MockBillboardTable
        assertTrue(mockBillboardTable.billboardExistsTest(newBillboardName));
    }


    /* Test 12: Delete Billboard (Pass)
     * Description: Delete the corresponding billboard in the MockBillboardTable with the billboard name, returns
     * server acknowledgement.
     * Expected Output: Billboard is delete from the MockBillboardTable and returns Success server acknowledge.
     */
    @Test
    public void mockDeleteBillboardTest() {
        ServerAcknowledge mockResponse = mockBillboardTable.deleteBillboardTest(mockToken, billboardName);
        assertEquals(Success, mockResponse);
        // Check that the billboard is deleted from the MockBillboardTable
        assertFalse(mockBillboardTable.billboardExistsTest(billboardName));
    }

    /* Test 13: Get Billboard (Pass)
     * Description: Retrieve the corresponding billboard in the MockBillboardTable with the billboard name, returns
     * DbBillboard object.
     * Expected Output: DbBillboard corresponding to the billboard name provided is returned.
     */
    @Test
    public void mockGetBillboardInformationTest() {
        DbBillboard mockResponse = mockBillboardTable.getBillboardInformationTest(billboardName);
        assertEquals(Success, mockResponse.getServerResponse());
        // Check that the correct billboard is returned
        assertEquals(billboardName, mockResponse.getBillboardName());
    }

    /**
     * ================================================================================================
     * SCHEDULE UNIT TESTS
     * ================================================================================================
     */
//TODO: COULD ADD MORE LOGIC HERE FOR THE OTHER SERVER ACKNOWLEDGE RETURN TYPES
    /* Test 14: Create Schedule (Pass)
     * Description: Create the corresponding schedule in the MockScheduleTable with the billboard name, start time,
     * duration, creation date time, repeat, sunday, monday, tuesday, wednesday, thursday, friday, saturday
     * - returns server acknowledgement.
     * Expected Output: Schedule is created in the MockScheduleTable and returns Success server acknowledge.
     */
    @Test
    public void mockCreateScheduleTest() {
        ServerAcknowledge mockResponse = mockScheduleTable.updateScheduleTest(mockToken, newBillboardName, startTime,
                                                            duration, creationDateTime, repeat, sunday, monday, tuesday,
                                                            wednesday, thursday, friday, saturday);
        assertEquals(Success, mockResponse);
        // Check that the schedule is actually added to the MockScheduleTable
        assertTrue(mockScheduleTable.BillboardScheduleExistsTest(billboardName));
    }

    /* Test 14: Edit Schedule (Pass)
     * Description: Edit the corresponding schedule in the MockScheduleTable with the billboard name, start time,
     * duration, creation date time, repeat, sunday, monday, tuesday, wednesday, thursday, friday, saturday
     * - returns server acknowledgement.
     * Expected Output: Schedule for billboardName is edited in the MockScheduleTable to display on Saturday and
     * returns Success server acknowledge.
     */
    @Test
    public void mockEditScheduleTest() {
        ServerAcknowledge mockResponse = mockScheduleTable.updateScheduleTest(mockToken, billboardName, startTime,
                duration, creationDateTime, repeat, sunday, monday, tuesday,
                wednesday, thursday, friday, "1");
        assertEquals(Success, mockResponse);
        // Check that the schedule still exists in the MockScheduleTable // todo: change to get info
        assertTrue(mockScheduleTable.BillboardScheduleExistsTest(billboardName));
    }


    /* Test 15: Delete Schedule (Pass)
     * Description: Delete the corresponding schedule in the MockScheduleTable with the billboard name,
     * returns server acknowledgement.
     * Expected Output: Schedule is deleted from the MockScheduleTable and returns Success server acknowledge.
     */
    @Test
    public void mockDeleteScheduleTest() {
        ServerAcknowledge mockResponse = mockScheduleTable.deleteScheduleTest(mockToken, billboardName);
        assertEquals(Success, mockResponse);
        // Check that the schedule no longer exists in the MockScheduleTable
        assertFalse(mockScheduleTable.BillboardScheduleExistsTest(billboardName));
    }



    // updateSchedule (create, edit after)
    // getScheduleInformation (THIS IS ONE)
    // getCurrentBillboard (NAME ONLY) *FAKE

}