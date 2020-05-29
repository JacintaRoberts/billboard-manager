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
 * UNIT TESTS - MOCK USER TABLE TO REMOVE SQL/SERVER DEPENDENCY
 * ================================================================================================
 */

class UnitTests {
    /* Test 0: Declaring MockUserTable Object
     * Description: MockUserTable object should be running in background on application start.
     * Expected Output: MockUserTable object and dummy testing data is declared
     */
    MockUserTable mockUserTable;
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


    /* Test 1: Constructing a MockUserTable and MockSessionTokens object
     * Description: MockUserTable and MockSessionTokens created and some initial testing data populated.
     * Expected Output: MockUserTable object able to be instantiated and populated with fake data.
     */
    @BeforeEach
    @Test
    public void setUpMockUserTable() throws NoSuchAlgorithmException {
        mockUserTable = new MockUserTable();
        mockSessionTokens = new MockSessionTokens();
        // Populate Mock User Table and Generate Values as required - For Unit Testing
        mockToken = mockSessionTokens.generateTokenTest(callingUser);
        basicToken = mockSessionTokens.generateTokenTest(basicUser);
        mockUserTable.createUserTest(mockToken, callingUser, dummyHashedPassword, createBillboard, editBillboard, scheduleBillboard, editUser);
    }


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

}