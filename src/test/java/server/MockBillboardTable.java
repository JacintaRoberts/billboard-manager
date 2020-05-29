package server;

import java.util.ArrayList;
import java.util.HashMap;

import static server.MockSessionTokens.getUsernameFromTokenTest;
import static server.MockUserTable.hasPermissionTest;
import static server.Server.Permission.CreateBillboard;
import static server.Server.ServerAcknowledge;
import static server.Server.ServerAcknowledge.*;


/**================================================================================================
 * UNIT TESTS USE THIS MOCK BILLBOARD TABLE CLASS TO REMOVE SQL/SERVER DEPENDENCY
 ================================================================================================*/
class MockBillboardTable extends MockDatabase {
    private static HashMap<String, ArrayList<Object>> internal = new HashMap<>();

    /**
     * Mock create billboard for unit testing
     * @param sessionToken A string session token which provides username to store into the MockBillboardTable
     * @param creator A string corresponding to the username of the creator (for editing purposes)
     * @param billboard A string which provides Billboard Name to store into the MockBillboardTable
     * @param xmlCode A string which provides xmlCode to store into the MockBillboardTable
     * @param pictureData A byte array of the picture data to store into the MockBillboardTable
     * @return ServerAcknowledge to confirm creation or signal exception occurred.
     */
    protected static ServerAcknowledge createBillboardTest(String sessionToken, String billboard, String creator, String xmlCode, byte[] pictureData) {
        // New billboard Case
        String callingUsername = getUsernameFromTokenTest(sessionToken);
        if (hasPermissionTest(callingUsername, CreateBillboard)) {
            // Add values to array list
            ArrayList<Object> values = new ArrayList();
            values.add(billboard);
            values.add(creator);
            values.add(xmlCode);
            values.add(pictureData);
            ServerAcknowledge mockResponse = addBillboardTest(billboard, values); // Update the table
            return mockResponse; // 1. PrimaryKeyClash or 2. Success
        } else {
            return InsufficientPermission; // 3. Calling User has Insufficient Permissions to call this method
        }
    }

    /**
     * Mock method to add a billboard to the mock user hashtable
     * @return ServerAcknowledge for Success or PrimaryKeyClash
     */
    private static ServerAcknowledge addBillboardTest(String billboard, ArrayList<Object> values) {
        ServerAcknowledge dbResponse = PrimaryKeyClash;
        if (!internal.containsKey(billboard)) { // If did not contain the billboard already, there would not be a clash
            System.out.println("Mock User Table did not contain " + billboard + " ...adding the billboard!");
            internal.put(billboard, new ArrayList<>());
            dbResponse = Success;
        }
        internal.get(billboard).add(values); // Add values to the MockBillboardTable
        return dbResponse;
    }


    /**
     * Method to delete user from database
     * @param sessionToken Session token from the calling user
     * @param billboard Billboard to be deleted
     * @return Server acknowledgement for Success or Exception Handling
     */
    protected static ServerAcknowledge deleteBillboardTest(String sessionToken, String billboard) {
        if (hasPermissionTest(sessionToken, CreateBillboard)) {
            // Delete billboard
            if (billboardExistsTest(billboard)) {
                internal.remove(billboard);
                System.out.println("Billboard was deleted: " + billboard);
                return Success; // 1. Billboard Deleted - Valid user, token and sufficient permission
            } else {
                System.out.println("Requested user to be deleted does not exist, no user was deleted");
                return BillboardNotExists; // 2. Requested billboard to be deleted does not exist in DB
            }
        } else {
            return InsufficientPermission; // 3. Calling User has Insufficient Permissions
        }
    }

    /**
     * Determine whether the particular billboard exists in the MockBillboardTable
     * @param billboard String billboard name to be searched for in the MockBillboardTable
     * @return boolean value to indicate whether the billboard exists (true), false otherwise
     */
    protected static boolean billboardExistsTest(String billboard) {
        if (internal.containsKey(billboard)) {
            return true;
        } return false;
    }


    /**
     * Mocks retrieval of user from database
     * @param billboard The string billboard name to be fetched
     * @return ArrayList of billboard information
     */
    private static ArrayList<Object> getBillboardInformationTest(String billboard) {
        return (ArrayList<Object>) internal.get(billboard).get(0);
    }


}
