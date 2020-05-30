package server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static server.MockSessionTokens.getUsernameFromTokenTest;
import static server.MockSessionTokens.validateTokenTest;
import static server.MockUserTable.hasPermissionTest;
import static server.Server.Permission.*;
import static server.Server.ServerAcknowledge;
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
    private String dummySalt = "68b91e68f846f39f742b4e8e5155bd6ac5a4238b7fc4360becc02b064c006433";
    private String dummyHashedPassword = "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8";// hash("password");
    private String newHashedPassword = "05da7dd57905dca05ed787d6f1be93bc0e4d279bee43553c2e08874f38fda93b"; // hash("newpass");
    private String dummyHashedSaltedPassword = "370203851f06f7725c40bfe1386d42724b9b82a037097ae2bb9d1b19cb1d0217"; // hash(dummyHashedPassword + dummySalt);
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
        mockUserTable.addUserTest(callingUser, dummyHashedPassword, createBillboard, editBillboard, scheduleBillboard, editUser);
        mockUserTable.addUserTest(basicUser, dummyHashedPassword, false, false, false, false);
        mockBillboardTable.createBillboardTest(mockToken, billboardName, callingUser, billboardXML, pictureData);
        mockScheduleTable.updateScheduleTest(mockToken, billboardName, startTime, duration, creationDateTime, repeat,
                                                sunday, monday, tuesday, wednesday, thursday, friday, saturday);
    }

    /**
     * ================================================================================================
     * 1. SESSION TOKEN UNIT TESTS
     * ================================================================================================
     */

    /* Test 2: Generate Session Token (Pass)
     * Description: Check that a session token is able to be generated.
     * Expected Output: A session token is created in the MockSessionTokens.
     */
    @Test
    public void generateTokenTest() {
        String testToken = mockSessionTokens.generateTokenTest(testUser);
        // Check that the session token is added to the MockSessionTokens Hash map
        assertTrue(validateTokenTest(testToken));
    }


    /* Test 3: Get Username From Token (Pass)
     * Description: Retrieve the username associated with the SessionToken in the MockSessionTokens.
     * Expected Output: Returns string username of the session token
     */
    @Test
    public void mockGetUsernameFromTokenTest() {
        String username = mockSessionTokens.getUsernameFromTokenTest(mockToken);
        assertEquals(callingUser, username);
    }


    /* Test 4: Check Permission Through Session Token (Pass)
     * Description: Check that a user's permissions are able to be checked via the session token.
     * Expected Output: User permissions are able to be retrieved via the session tokens.
     */
    @Test
    public void checkPermissionViaSessionToken() throws NoSuchAlgorithmException {
        String usernameFromBasicToken = getUsernameFromTokenTest(basicToken);
        String usernameFromFullToken = getUsernameFromTokenTest(mockToken);
        assertAll("Check for Basic and Full User Permissions",
                // Check basic user does not have any of the permissions
                () -> assertFalse(hasPermissionTest(usernameFromBasicToken, EditUser)),
                () -> assertFalse(hasPermissionTest(usernameFromBasicToken, CreateBillboard)),
                () -> assertFalse(hasPermissionTest(usernameFromBasicToken, ScheduleBillboard)),
                () -> assertFalse(hasPermissionTest(usernameFromBasicToken, EditBillboard)),
                // Check full user does have all of the permissions
                () -> assertTrue(hasPermissionTest(usernameFromFullToken, EditUser)),
                () -> assertTrue(hasPermissionTest(usernameFromFullToken, CreateBillboard)),
                () -> assertTrue(hasPermissionTest(usernameFromFullToken, ScheduleBillboard)),
                () -> assertTrue(hasPermissionTest(usernameFromFullToken, EditBillboard))
        );
    }

    /**
     * ================================================================================================
     * 2. USER-BASED UNIT TESTS
     * ================================================================================================
     */

    /* Test 5: Check User Exists (Pass)
     * Description: Check that a user exists in the MockUserTable, checks for case sensitivity, trailing whitespace,
     * empty etc.
     * Expected Output: Boolean true is returned if the user is found in the MockUserTable, and false otherwise.
     */
    @Test
    public void mockUserExists() {
        assertAll("Check for Existing User",
                // Check for non-existent user
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


    /* Test 6: Create User (Pass)
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
        // Cleanup
        mockUserTable.deleteUserTest(mockToken, testUser);
    }


    /* Test 7: Create User (Exception Handling)
     * Description: Attempt to create the corresponding username in the MockUserTable with the hashed password
     * and permissions, returns server acknowledgement. This tests for the PrimaryKeyClash Exception.
     * Expected Output: User is not created in the MockUserTable and returns PrimaryKeyClash server acknowledge.
     */
    @Test
    public void mockCreateUserPrimaryKeyClash() throws NoSuchAlgorithmException {
        ServerAcknowledge mockResponse = mockUserTable.createUserTest(mockToken, callingUser, dummyHashedPassword, createBillboard, editBillboard, editBillboard, editUser);
        assertEquals(PrimaryKeyClash, mockResponse);
    }


    /* Test 8: Create User (Exception Handling)
     * Description: Attempt to create the corresponding username in the MockUserTable with the hashed password
     * and permissions, returns server acknowledgement. This testS for the InsufficientPermission exception
     * Expected Output: User is not created in the MockUserTable and returns InsufficientPermission server acknowledge.
     */
    @Test
    public void mockCreateUserInsufficientPermission() throws NoSuchAlgorithmException {
        ServerAcknowledge mockResponse = mockUserTable.createUserTest(basicToken, testUser, dummyHashedPassword, createBillboard, editBillboard, editBillboard, editUser);
        assertEquals(InsufficientPermission, mockResponse);
    }


    /* Test 9: Retrieve User (Pass)
     * Description: Retrieve the corresponding user details from the MockUserTable returns user.
     * Expected Output: User is retrieved from the MockUserTable
     */
    @Test
    public void mockRetrieveUser() {
        ArrayList<Object> retrievedUser = mockUserTable.retrieveUserTest(callingUser);
        ArrayList<Object> expectedValues = new ArrayList();
        expectedValues.add(dummyHashedSaltedPassword);
        expectedValues.add(dummySalt);
        expectedValues.add(createBillboard);
        expectedValues.add(editBillboard);
        expectedValues.add(scheduleBillboard);
        expectedValues.add(editUser);
        assertEquals(expectedValues, retrievedUser);
    }


    /* Test 10: Get Permissions (Pass)
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


    /* Test 11: Get Other Permissions (Pass)
     * Description: Retrieve the basic user's permissions associated with the SessionToken in the MockSessionTokens.
     * Expected Output: Returns an ArrayList of Booleans to indicate whether the username associated with the session
     * token has the corresponding permissions.
     */
    @Test
    public void mockGetOtherPermissionsFromTokenTest() throws NoSuchAlgorithmException {
        ArrayList<Boolean> userPermisisons = (ArrayList<Boolean>) mockSessionTokens.mockGetPermissionsFromTokenTest(mockToken, basicUser);
        assertEquals(basicPermissions, userPermisisons);
    }


    /* Test 12: Get Other Permissions (Exception Handling)
     * Description: Retrieve the other user's permissions associated with the SessionToken in the MockSessionTokens,
     * calling user is basic and does not have the EditUsers permission required.
     * Expected Output: Returns a Server Acknowledgement for InsufficientPermission.
     */
    @Test
    public void mockGetOtherPermissionsFromTokenTestInsufficientPermission() throws NoSuchAlgorithmException {
        ServerAcknowledge userPermisisons = (ServerAcknowledge) mockSessionTokens.mockGetPermissionsFromTokenTest(basicToken, callingUser);
        assertEquals(InsufficientPermission, userPermisisons);
    }


    /* Test 13: Delete User (Pass)
     * Description: Delete the corresponding username in the MockUserTable and return server acknowledgement
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


    /* Test 14: Delete User (Exception Handling)
     * Description: Delete own username in the MockUserTable and return server acknowledgement
     * Expected Output: User is not deleted from MockUserTable and returns CannotDeleteSelf server acknowledgement.
     */
    @Test
    public void mockDeleteOwnUserTest() {
        ServerAcknowledge mockResponse = mockUserTable.deleteUserTest(mockToken, callingUser);
        assertEquals(CannotDeleteSelf, mockResponse);
        // Check that the user is not removed from the Mock DB anyway
        assertTrue(mockUserTable.userExistsTest(callingUser));
    }


    /* Test 15: Delete User (Exception Handling)
     * Description: Delete username in the MockUserTable by basic user and return server acknowledgement
     * Expected Output: User is not deleted from MockUserTable and returns InsufficientPermission server acknowledgement.
     */
    @Test
    public void mockDeleteUserInsufficientPermissionsTest() {
        ServerAcknowledge mockResponse = mockUserTable.deleteUserTest(basicToken, callingUser);
        assertEquals(InsufficientPermission, mockResponse);
        // Check that the user is not removed from the Mock DB anyway
        assertTrue(mockUserTable.userExistsTest(callingUser));
    }


    /* Test 16: Delete User (Exception Handling)
     * Description: Delete non-existent username in the MockUserTable and return server acknowledgement
     * Expected Output: User is not deleted from MockUserTable and returns NoSuchUser server acknowledgement.
     */
    @Test
    public void mockDeleteNonExistentUserTest() {
        ServerAcknowledge mockResponse = mockUserTable.deleteUserTest(mockToken, "non-existent");
        assertEquals(NoSuchUser, mockResponse);
    }


    /* Test 17: List Users (Success)
     * Description: List all usernames in the MockUserTable and return server acknowledgement
     * Expected Output: Users are listed from MockUserTable and returns Success server acknowledgement.
     */
    @Test
    public void mockListUsersTest() {
        ArrayList<String> actualUsernames = (ArrayList<String>) mockUserTable.listUsersTest(mockToken);
        ArrayList<String> expectedUsernames = new ArrayList<>();
        expectedUsernames.add(basicUser);
        expectedUsernames.add(callingUser);
        assertEquals(expectedUsernames, actualUsernames);
    }


    /* Test 18: List Users (Exception Handling)
     * Description: List all usernames in the MockUserTable and return server acknowledgement
     * Expected Output: Users are not listed and returns InsufficientPermission Exception server acknowledgement.
     */
    @Test
    public void mockListUsersInsufficientPermissionTest() {
        ServerAcknowledge mockResponse = (ServerAcknowledge) mockUserTable.listUsersTest(basicToken);
        assertEquals(InsufficientPermission, mockResponse);
    }


    /* Test 19: Set Permissions (Pass)
     * Description: Set permissions of the basic user in the MockUserTable to full privileges and return server
     * acknowledgement.
     * Expected Output: User permissions are updated in the MockUserTable and returns Success server acknowledgement.
     */
    @Test
    public void mockSetPermissionsTest() {
        ServerAcknowledge mockResponse = mockUserTable.setPermissionsTest(mockToken, basicUser, true, true, true, true);
        assertEquals(Success, mockResponse);
        // Check permissions are updated in the MockUserTable
        assertEquals(fullPermissions, mockUserTable.retrieveUserPermissionsFromMockDbTest(basicUser));
        // Cleanup
        mockUserTable.setPermissionsTest(mockToken, basicUser, false, false, false, false);
    }


    /* Test 20: Set Permissions (Exception Handling)
     * Description: Attempt to set permissions of basic user to full permissions and return server acknowledgement.
     * Expected Output: User permissions are not updated in the MockUserTable and returns InsufficientPermission
     * Exception server acknowledgement.
     */
    @Test
    public void mockSetPermissionsInsufficientPermissionsTest() {
        ServerAcknowledge mockResponse = mockUserTable.setPermissionsTest(basicToken, basicUser, true, true, true, true);
        assertEquals(InsufficientPermission, mockResponse);
        // Check permissions are not updated in the MockUserTable
        assertEquals(basicPermissions, mockUserTable.retrieveUserPermissionsFromMockDbTest(basicUser));
    }


    /* Test 21: Set Permissions (Exception Handling)
     * Description: Attempt to set permissions of full user to remove EditUsers and return server acknowledgement.
     * Expected Output: User permissions are not updated in the MockUserTable and returns CannotRemoveOwnAdminPermission
     * Exception server acknowledgement.
     */
    @Test
    public void mockSetPermissionsCannotRemoveOwnAdminTest() {
        ServerAcknowledge mockResponse = mockUserTable.setPermissionsTest(mockToken, callingUser, createBillboard,
                                                                    editBillboard, scheduleBillboard, false);
        assertEquals(CannotRemoveOwnAdminPermission, mockResponse);
        // Check permissions are not updated in the MockUserTable
        assertEquals(fullPermissions, mockUserTable.retrieveUserPermissionsFromMockDbTest(callingUser));
    }


    /* Test 22: Set Permissions (Exception Handling)
     * Description: Attempt to set permissions of username that does not exist and return server acknowledgement.
     * Expected Output: User permissions are not updated in the MockUserTable and returns NoSuchUser Exception
     * server acknowledgement.
     */
    @Test
    public void mockSetPermissionsNoSuchUserTest() {
        ServerAcknowledge mockResponse = mockUserTable.setPermissionsTest(mockToken, "non-existent",
                                                    createBillboard, editBillboard, scheduleBillboard, editUser);
        assertEquals(NoSuchUser, mockResponse);
    }


    /* Test 23: Set Password (Pass)
     * Description: Set password of the basic user in the MockUserTable to alternate password and return server
     * acknowledgement.
     * Expected Output: User password is updated in the MockUserTable and returns Success server acknowledgement.
     */
    @Test
    public void mockSetPasswordTest() throws NoSuchAlgorithmException {
        ServerAcknowledge mockResponse = mockUserTable.setPasswordTest(mockToken, basicUser, newHashedPassword);
        assertEquals(Success, mockResponse);
    }


    /* Test 24: Set Password (Exception Handling)
     * Description: Attempt to set password of the basic user in the MockUserTable to alternate password and return
     * server acknowledgement.
     * Expected Output: User password is not updated in the MockUserTable and returns InsufficientPermission
     * Exception server acknowledgement.
     */
    @Test
    public void mockSetPasswordInsufficientPermissionsTest() throws NoSuchAlgorithmException {
        ServerAcknowledge mockResponse = mockUserTable.setPasswordTest(basicToken, testUser, newHashedPassword);
        assertEquals(InsufficientPermission, mockResponse);
    }


    /* Test 25: Set Password (Exception Handling)
     * Description: Attempt to set password of a non-existent user and return server acknowledgement.
     * Expected Output: User password is not updated in the MockUserTable as the user does not exists and returns
     * NoSuchUser Exception server acknowledgement.
     */
    @Test
    public void mockSetPasswordNoSuchUserTest() throws NoSuchAlgorithmException {
        ServerAcknowledge mockResponse = mockUserTable.setPasswordTest(mockToken, "non-existent", newHashedPassword);
        assertEquals(NoSuchUser, mockResponse);
    }


    /**
     * ================================================================================================
     * 3. BILLBOARD-BASED UNIT TESTS
     * ================================================================================================
     */

    /* Test 26: Create Billboard (Pass)
     * Description: Create the corresponding billboard in the MockBillboardTable with the billboard name, creator, xml
     * and picture data - return server acknowledgement.
     * Expected Output: Billboard is created in the MockBillboardTable and returns Success server acknowledge.
     */
    @Test
    public void mockCreateBillboardTest() {
        // Test setup - ensure that the billboard to be created does not exist in the MockBillboardTable
        mockBillboardTable.deleteBillboardTest(mockToken, newBillboardName);
        ServerAcknowledge mockResponse = mockBillboardTable.createBillboardTest(mockToken, newBillboardName, callingUser, billboardXML, pictureData);
        assertEquals(Success, mockResponse);
        // Check that the billboard is actually added to the MockBillboardTable
        assertTrue(mockBillboardTable.billboardExistsTest(newBillboardName));
    }


    /* Test 27: Create Billboard (Exception Handling)
     * Description: Create the corresponding (duplicate) billboard in the MockBillboardTable with the billboard name,
     * creator, xml and picture data - return server acknowledgement.
     * Expected Output: Billboard is not created again in the MockBillboardTable and returns PrimaryKeyClash Exception
     * server acknowledge.
     */
    @Test
    public void mockCreateBillboardPrimaryKeyClashTest() {
        ServerAcknowledge mockResponse = mockBillboardTable.createBillboardTest(mockToken, billboardName, callingUser, billboardXML, pictureData);
        assertEquals(PrimaryKeyClash, mockResponse);
    }


    /* Test 28: Create Billboard (Exception Handling)
     * Description: Basic user attempts to create the corresponding billboard in the MockBillboardTable with the
     * billboard name, creator, xml and picture data - return server acknowledgement.
     * Expected Output: Billboard is not created again in the MockBillboardTable and returns InsufficientPermission
     * Exception server acknowledge.
     */
    @Test
    public void mockCreateBillboardInsufficientPermissionsTest() {
        ServerAcknowledge mockResponse = mockBillboardTable.createBillboardTest(basicToken, newBillboardName, callingUser, billboardXML, pictureData);
        assertEquals(InsufficientPermission, mockResponse);
        // Check that the billboard is not actually added to the MockBillboardTable
        assertFalse(mockBillboardTable.billboardExistsTest(newBillboardName));
    }


    /* Test 29: Delete Billboard (Pass)
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


    /* Test 29: Delete Billboard (Exception Handling)
     * Description: Attempt to delete the corresponding (non-existent) billboard in the MockBillboardTable with the
     * billboard name, returns server acknowledgement.
     * Expected Output: Billboard is not deleted from the MockBillboardTable as it never existed and returns
     * BillboardNotExists server acknowledge.
     */
    @Test
    public void mockDeleteBillboardNotExistsTest() {
        ServerAcknowledge mockResponse = mockBillboardTable.deleteBillboardTest(mockToken, "non-existent");
        assertEquals(BillboardNotExists, mockResponse);
    }


    /* Test 30: Delete Billboard (Exception Handling)
     * Description: Basic User attempts to delete the corresponding billboard in the MockBillboardTable with the
     * billboard name, returns server acknowledgement.
     * Expected Output: Billboard is not deleted from the MockBillboardTable and returns InsufficientPermission
     * server acknowledge.
     */
    @Test
    public void mockDeleteBillboardInsufficientPermissionTest() {
        ServerAcknowledge mockResponse = mockBillboardTable.deleteBillboardTest(basicToken, billboardName);
        assertEquals(InsufficientPermission, mockResponse);
    }


    /* Test 31: Get Billboard (Pass)
     * Description: Retrieve the corresponding billboard in the MockBillboardTable with the billboard name, returns
     * DbBillboard object.
     * Expected Output: DbBillboard corresponding to the billboard name provided is returned. Server response is Success.
     */
    @Test
    public void mockGetBillboardInformationTest() {
        DbBillboard mockResponse = mockBillboardTable.getBillboardInformationTest(billboardName);
        assertEquals(Success, mockResponse.getServerResponse());
        // Check that the correct billboard is returned
        assertEquals(billboardName, mockResponse.getBillboardName());
    }


    /* Test 32: Get Billboard (Exception Handling)
     * Description: Retrieve the corresponding (non-existent) billboard in the MockBillboardTable with the billboard
     * name, returns DbBillboard object.
     * Expected Output: DbBillboard corresponding to the billboard name provided is returned. Server response is
     * BillboardNotExists
     */
    @Test
    public void mockGetBillboardInformationNotExistsTest() {
        DbBillboard mockResponse = mockBillboardTable.getBillboardInformationTest("non-existent");
        assertEquals(BillboardNotExists, mockResponse.getServerResponse());
    }


    /* Test 33: List Billboard (Pass)
     * Description: Retrieve the list of billboard names in the MockBillboardTable, returns a ArrayList of Strings.
     * Expected Output: An ArrayList of Strings containing the billboard names.
     */
    @Test
    public void mockListBillboardTest() {
        // Test setup - Ensure the new billboard does not exist in the mockBillboardTable
        mockBillboardTable.deleteBillboardTest(mockToken, newBillboardName);
        ArrayList<String> actualBillboards = (ArrayList<String>) mockBillboardTable.listBillboardTest();
        ArrayList<String> expectedBillboards = new ArrayList<>();
        expectedBillboards.add(billboardName);
        assertEquals(expectedBillboards, actualBillboards);
    }


    /**
     * ================================================================================================
     * 4. SCHEDULE-BASED UNIT TESTS
     * ================================================================================================
     */

    /* Test 34: Create Schedule (Pass)
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


    /* Test 35: Create Schedule (Exception Handling)
     * Description: Attempt to create the corresponding schedule in the MockScheduleTable with the (non-existent)
     * billboard name, start time, duration, creation date time, repeat, sunday, monday, tuesday, wednesday, thursday,
     * friday, saturday - returns server acknowledgement.
     * Expected Output: Schedule is not created in the MockScheduleTable as the requested billboard does not exist
     * and returns BillboardNotExists server acknowledge.
     */
    @Test
    public void mockCreateScheduleBillboardNotExistsTest() {
        ServerAcknowledge mockResponse = mockScheduleTable.updateScheduleTest(mockToken, "non-existent",
                                                                startTime, duration, creationDateTime, repeat, sunday,
                                                                monday, tuesday, wednesday, thursday, friday, saturday);
        assertEquals(BillboardNotExists, mockResponse);
        // Check that the schedule is not added to the MockScheduleTable
        assertFalse(mockScheduleTable.BillboardScheduleExistsTest("non-existent"));
    }


    /* Test 36: Create Schedule (Exception Handling)
     * Description: Basic User attempts to create the corresponding schedule in the MockScheduleTable with the
     * billboard name, start time, duration, creation date time, repeat, sunday, monday, tuesday, wednesday, thursday,
     * friday, saturday - returns server acknowledgement.
     * Expected Output: Schedule is not created in the MockScheduleTable as the basic user did not have the required
     * CreateBillboards permission and returns InsufficientPermissions server acknowledge.
     */
    @Test
    public void mockCreateScheduleBillboardInsufficientPermissionTest() {
        ServerAcknowledge mockResponse = mockScheduleTable.updateScheduleTest(mockToken, newBillboardName,
                startTime, duration, creationDateTime, repeat, sunday,
                monday, tuesday, wednesday, thursday, friday, saturday);
        assertEquals(BillboardNotExists, mockResponse);
        // Check that the schedule is not added to the MockScheduleTable
        assertFalse(mockScheduleTable.BillboardScheduleExistsTest(newBillboardName));
    }



    /* Test 37: Edit Schedule (Pass)
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


    /* Test 38: Delete Schedule (Pass)
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


    /* Test 39: Delete Schedule (Exception Handling)
     * Description: Delete the corresponding schedule in the MockScheduleTable with the (non-existent) billboard name,
     * returns server acknowledgement.
     * Expected Output: Schedule is not deleted from the MockScheduleTable as the billboard does not exist and
     * returns BillboardNotExists server acknowledge.
     */
    @Test
    public void mockDeleteScheduleBillboardNotExistsTest() {
        ServerAcknowledge mockResponse = mockScheduleTable.deleteScheduleTest(mockToken, "non-existent");
        assertEquals(BillboardNotExists, mockResponse);
    }

    /* Test 40: Delete Schedule (Exception Handling)
     * Description: Delete the corresponding (non-existent) schedule in the MockScheduleTable with the billboard name,
     * returns server acknowledgement.
     * Expected Output: Schedule is not deleted from the MockScheduleTable as the schedule does not exist and
     * returns ScheduledNotExists server acknowledge.
     */
    @Test
    public void mockDeleteScheduleNotExistsTest() {
        // Test setup - ensure that the new billboard exists
        mockBillboardTable.createBillboardTest(mockToken, newBillboardName, callingUser, billboardXML, pictureData);
        ServerAcknowledge mockResponse = mockScheduleTable.deleteScheduleTest(mockToken, newBillboardName);
        assertEquals(ScheduleNotExists, mockResponse);
    }


    /* Test 41: Delete Schedule (Exception Handling)
     * Description: Basic User attempts to delete the corresponding schedule in the MockScheduleTable with the
     * billboard name, returns server acknowledgement.
     * Expected Output: Schedule is not deleted from the MockScheduleTable as the user does not have the required
     * permissions and returns InvalidPermission Exception server acknowledge.
     */
    @Test
    public void mockDeleteScheduleInvalidPermissionsTest() {
        ServerAcknowledge mockResponse = mockScheduleTable.deleteScheduleTest(basicToken, billboardName);
        assertEquals(InsufficientPermission, mockResponse);
    }


    /* Test 42: Get Schedule Information (Pass)
     * Description: Retrieve the corresponding schedule information for the billboard from the MockScheduleTable with
     * the billboard name, returns server acknowledgement.
     * Expected Output: ScheduleInfo object is returned from the MockScheduleTable which contains Success server acknowledge.
     */
    @Test
    public void mockGetScheduleInformationTest() {
        ScheduleInfo scheduleInfo = mockScheduleTable.getScheduleInformationTest(mockToken, billboardName);
        assertEquals(Success, scheduleInfo.getScheduleServerResponse());
    }


    /* Test 43: Get Schedule Information (Exception Handling)
     * Description: Retrieve the corresponding (non-existent) schedule information for the billboard from the
     * MockScheduleTable with the billboard name, returns server acknowledgement.
     * Expected Output: ScheduleInfo object is returned from the MockScheduleTable which contains ScheduleNotExists
     * Exception server acknowledge.
     */
    @Test
    public void mockGetScheduleInformationNotExistsTest() {
        // Test setup - ensure that the new billboard exists
        mockBillboardTable.createBillboardTest(mockToken, newBillboardName, callingUser, billboardXML, pictureData);
        ScheduleInfo scheduleInfo = mockScheduleTable.getScheduleInformationTest(mockToken, newBillboardName);
        assertEquals(ScheduleNotExists, scheduleInfo.getScheduleServerResponse());
    }


    /* Test 44: Get Schedule Information (Exception Handling)
     * Description: Basic user attempts to retrieve the corresponding schedule information for the billboard from the
     * MockScheduleTable with the billboard name, returns server acknowledgement.
     * Expected Output: ScheduleInfo object is returned from the MockScheduleTable which contains InsufficientPermission
     * Exception server acknowledge.
     */
    @Test
    public void mockGetScheduleInformationInsufficientPermissionTest() {
        ScheduleInfo scheduleInfo = mockScheduleTable.getScheduleInformationTest(basicToken, billboardName);
        assertEquals(InsufficientPermission, scheduleInfo.getScheduleServerResponse());
    }

}