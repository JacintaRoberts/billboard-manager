package server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;
import static server.Server.*;
import static server.Server.ServerAcknowledge.*;
class UnitTests {
    /* Test 0: Declaring UserAdmin object
     * Description: UserAdmin object should be running in background on application start.
     * Expected Output: UserAdmin object is declared
     */
    MockUserTable mockUserTable;
    MockSessionTokens mockSessionTokens;
    // Declaration and initialisation of testing variables
    private String mockToken;
    private String callingUser = "callingUser";
    private String testUser = "testUser";
    private String dummyHashedPassword = "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8";// hash("password");
    private Boolean createBillboard = true;
    private Boolean editBillboard = true;
    private Boolean scheduleBillboard = true;
    private Boolean editUser = true;
    // Defining permissions to be tested
    private ArrayList<Boolean> fullPermissions = new ArrayList<>(Arrays.asList(true, true, true, true));
    private ArrayList<Boolean> basicPermissions = new ArrayList<>(Arrays.asList(false, false, false, false));
    private ArrayList<Boolean> createBillboardPermission = new ArrayList<>(Arrays.asList(true, false, false, false));
    private ArrayList<Boolean> editBillboardPermission = new ArrayList<>(Arrays.asList(false, true, false, false));
    private ArrayList<Boolean> editSchedulePermission = new ArrayList<>(Arrays.asList(false, false, true, false));
    private ArrayList<Boolean> editUserPermission = new ArrayList<>(Arrays.asList(false, false, false, true));

    /* Test 1: Constructing a UserAdmin and Mock User Table object
     * Description: UserAdmin and MockUserTable Objects should be able to be created
     * Expected Output: UserAdmin and MockUserTable objects able to be instantiated from their respective classes.
     */
    @BeforeEach
    @Test
    public void setUpMockUserTable() throws NoSuchAlgorithmException {
        mockUserTable = new MockUserTable();
        mockSessionTokens = new MockSessionTokens();
        // Populate Mock User Table and Generate Values as required - For Unit Testing
        mockToken = mockSessionTokens.generateTokenTest(callingUser);
        mockUserTable.createUserTest(mockToken, callingUser, dummyHashedPassword, createBillboard, editBillboard, scheduleBillboard, editUser);
    }

    /**
     * ================================================================================================
     * UNIT TESTS - MOCK USER TABLE TO REMOVE SQL/SERVER DEPENDENCY
     * ================================================================================================
     */

    /* Test 2: Check User Exists (Helper for other methods in this class) (Pass)
     * Description: Check that a user exists in the database - helper method
     * Expected Output: A boolean where true is returned if the user is found in the Mock Table and false otherwise
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


    /* Test 3: Create Users (Pass)
     * Description: Create the corresponding username in the Mock User Table with the hashed password and permissions
     * and return acknowledgement to Control Panel.
     * Expected Output: User is created in the DB and returns string "Pass: User Created"
     */
    @Test
    public void mockCreateUser() throws NoSuchAlgorithmException {
        // Test setup - Ensure the user to be created does not already exist
        System.out.println("My mock token is :" + mockToken);
        ServerAcknowledge mockResponse = mockUserTable.createUserTest(mockToken, testUser, dummyHashedPassword, createBillboard, editBillboard, editBillboard, editUser);
        assertEquals(Success, mockResponse);
        // Check that the user is actually added to the DB
        assertTrue(mockUserTable.userExistsTest(testUser));
    }
}