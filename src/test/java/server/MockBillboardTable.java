package server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import static server.MockSessionTokens.getUsernameFromTokenTest;
import static server.MockUserTable.hasPermissionTest;
import static server.MockUserTable.retrieveUserPermissionsFromMockDbTest;
import static server.Server.Permission.*;
import static server.Server.ServerAcknowledge;
import static server.Server.ServerAcknowledge.*;


/**================================================================================================
 * UNIT TESTS USE THIS MOCK BILLBOARD TABLE CLASS TO REMOVE SQL/SERVER DEPENDENCY
 ================================================================================================*/
class MockBillboardTable {
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
    protected static ServerAcknowledge addBillboardTest(String billboard, ArrayList<Object> values) {
        ServerAcknowledge dbResponse = PrimaryKeyClash;
        if (!internal.containsKey(billboard)) { // If did not contain the billboard already, there would not be a clash
            System.out.println("MockBillboardTable did not contain " + billboard + " ...adding the billboard!");
            internal.put(billboard, new ArrayList<>());
            dbResponse = Success;
        }
        internal.get(billboard).add(values); // Add values to the MockBillboardTable
        System.out.println("values of the billboard stored: " + values);
        return dbResponse;
    }


    /**
     * Method to delete billboard from database
     * @param sessionToken Session token from the calling user
     * @param billboard Billboard to be deleted
     * @return Server acknowledgement for Success or Exception Handling
     */
    protected static ServerAcknowledge deleteBillboardTest(String sessionToken, String billboard) {
        String OGCreator = getBillboardInformationTest(billboard).getCreator();
        System.out.println("OGCreator is: " + OGCreator);
        String requestor = getUsernameFromTokenTest(sessionToken);
        System.out.println("Requestor is: " + requestor);
        boolean currentlyScheduled = MockScheduleTable.BillboardScheduleExistsTest(billboard);
        System.out.println("Is the billboard scheduled? " + currentlyScheduled);
        // If own billboard that is not currently scheduled require CreateBillboard Permission
        if (requestor.equals(OGCreator) && !currentlyScheduled) {
            if (hasPermissionTest(requestor, CreateBillboard)) {
                // Delete billboard
                if (billboardExistsTest(billboard)) {
                    internal.remove(billboard);
                    System.out.println("Billboard was deleted: " + billboard);
                    return Success; // 1. Billboard Deleted - Valid user, token and sufficient permission
                } else {
                    System.out.println("Requested billboard to be deleted does not exist, no billboard was deleted");
                    return BillboardNotExists; // 2. Requested billboard to be deleted does not exist in DB
                }
            } else {
                System.out.println("This requested action requires the user to have the CreateBillboard permission");
                return InsufficientPermission; // 3. Calling User has Insufficient Permissions
            }
        // Else require EditBillboard Permission to delete any other billboard
        } else {
            if (hasPermissionTest(requestor, EditBillboard)) {
                // Delete billboard
                if (billboardExistsTest(billboard)) {
                    internal.remove(billboard);
                    System.out.println("Billboard was deleted: " + billboard);
                    return Success; // 1. Billboard Deleted - Valid user, token and sufficient permission
                } else {
                    System.out.println("Requested billboard to be deleted does not exist, no billboard was deleted");
                    return BillboardNotExists; // 2. Requested billboard to be deleted does not exist in DB
                }
            } else {
                System.out.println("This requested action requires the user to have the EditBillboard permission");
                return InsufficientPermission; // 3. Calling User has Insufficient Permissions
            }
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
     * Mocks retrieval of billboard from database and create dbBillboard object from return
     * @param billboard The string billboard name to be fetched
     * @return ArrayList of billboard information
     */
    protected static DbBillboard getBillboardInformationTest(String billboard){
        // Retrieve values from MockBillboardTable
        if (internal.containsKey(billboard)) {
            ArrayList<Object> values = (ArrayList<Object>) internal.get(billboard).get(0);
            DbBillboard dbBillboard = new DbBillboard((String) values.get(0), (String) values.get(1), (byte[]) values.get(3), (String) values.get(2), Success);
            return dbBillboard;
        } else {
            // Billboard does not exist in the MockBillboardTable
            return new DbBillboard("0","0", new byte[0], "0", BillboardNotExists);
        }
    }


    /**
     * List all the billboard names from the MockBillboardTable
     * @return an Arraylist of the billboard names of all billboards in the MockBillboardTable
     */
    public static Object listBillboardTest() {
        // Getting Set of keys from MockBillboardTable
        Set<String> keySet = internal.keySet();
        // Creating an ArrayList of keys by passing the keySet
        ArrayList<String> listOfbBillboards = new ArrayList<>(keySet);
        return listOfbBillboards; // 2. Success, list of users returned
    }

}
