package server;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import static controlPanel.UserControl.hash;
import static server.BillboardAdmin.countFilterBillboardSql;
import static server.BillboardAdmin.getBillboardInformation;
import static server.MockBillboardTable.getBillboardInformationTest;
import static server.MockSessionTokens.getUsernameFromTokenTest;
import static server.MockUserTable.hasPermissionTest;
import static server.Server.*;
import static server.Server.Permission.ScheduleBillboard;
import static server.Server.ServerAcknowledge.*;
import static server.Server.ServerAcknowledge.InvalidToken;
import static server.Server.validateToken;

/**================================================================================================
 * UNIT TESTS USE THIS MOCK SCHEDULE TABLE CLASS TO REMOVE SQL/SERVER DEPENDENCY
 ================================================================================================*/
class MockScheduleTable extends MockDatabase {
    private static HashMap<String, ArrayList<Object>> internal = new HashMap<>();

    /**
     * updateSchedule edits/creates schedules in the MockScheduleTable.
     * Returns a serverAcknowledge to signal whether the schedule edit/creation was successful,
     * or if an exception occurred.
     * @param sessionToken A String which provides request username to search in MockUserTable for permissions
     * @param billboard A String which provides Billboard Name to search in the MockBillboardTable for editing
     * @param startTime A String in format of Java Time to store into MockScheduleTable
     * @param duration A String representing an integer which provides Duration which to store into MockScheduleTable
     * @param creationDateTime A String in format of DateTime which provides CreationDateTime to store into MockScheduleTable
     * @param repeat A String representing an integer how often the schedule is repeated (in minutes)
     * @param sunday A String that's either 1 or 0 to see if the schedule is to be run during Sunday
     * @param monday A String that's either 1 or 0  to see if the schedule is to be run during Monday
     * @param tuesday A String that's either 1 or 0  to see if the schedule is to be run during Tuesday
     * @param wednesday A String that's either 1 or 0  to see if the schedule is to be run during Wednesday
     * @param thursday A String that's either 1 or 0  to see if the schedule is to be run during Thursday
     * @param friday A String that's either 1 or 0  to see if the schedule is to be run during Friday
     * @param saturday A String that's either 1 or 0  to see if the schedule is to be run during Saturday
     * @return Returns a ServerAcknowledge whether or not the Schedule was created/edited successfully,
     * or failed due to some other reason.
     */
    protected static ServerAcknowledge updateScheduleTest(String sessionToken, String billboard, String startTime,
                                                          String duration, String creationDateTime, String repeat,
                                                          String sunday, String monday, String tuesday, String wednesday,
                                                          String thursday, String friday, String saturday) {
        // User requires the ScheduleBillboard permission to perform this method
        String callingUsername = getUsernameFromTokenTest(sessionToken);
        if (hasPermissionTest(callingUsername, ScheduleBillboard)){
            // Start connection to see if billboard exists
            ServerAcknowledge billboardExist = getBillboardInformationTest(billboard).getServerResponse();
            if (billboardExist.equals(BillboardNotExists)){
                System.out.println("Billboard does not exist");
                return BillboardNotExists; // 1. Billboard no longer exists
            } else{
                // Create/edit schedule
                addScheduleTest(billboard, startTime, duration, creationDateTime, repeat, sunday, monday, tuesday,
                            wednesday, thursday, friday, saturday);
                return Success;
            }
        } else {
            System.out.println("Permissions were not sufficient, no Schedule was Updated");
            return InsufficientPermission; // 3. Valid token but insufficient permission
        }
    }

    /**
     * Method to create/edit billboard schedule in the MockScheduleTable
     * @param billboard A String which provides Billboard Name to search in the MockBillboardTable for editing
     * @param startTime A String in format of Java Time to store into MockScheduleTable
     * @param duration A String representing an integer which provides Duration which to store into MockScheduleTable
     * @param creationDateTime A String in format of DateTime which provides CreationDateTime to store into MockScheduleTable
     * @param repeat A String representing an integer how often the schedule is repeated (in minutes)
     * @param sunday A String that's either 1 or 0 to see if the schedule is to be run during Sunday
     * @param monday A String that's either 1 or 0  to see if the schedule is to be run during Monday
     * @param tuesday A String that's either 1 or 0  to see if the schedule is to be run during Tuesday
     * @param wednesday A String that's either 1 or 0  to see if the schedule is to be run during Wednesday
     * @param thursday A String that's either 1 or 0  to see if the schedule is to be run during Thursday
     * @param friday A String that's either 1 or 0  to see if the schedule is to be run during Friday
     * @param saturday A String that's either 1 or 0  to see if the schedule is to be run during Saturday
     */
    private static void addScheduleTest(String billboard, String startTime, String duration, String creationDateTime, String repeat, String sunday, String monday, String tuesday, String wednesday, String thursday, String friday, String saturday) {
        // Add values to array list
        ArrayList<Object> values = new ArrayList();
        values.add(startTime);
        values.add(duration);
        values.add(creationDateTime);
        values.add(repeat);
        values.add(sunday);
        values.add(monday);
        values.add(tuesday);
        values.add(wednesday);
        values.add(thursday);
        values.add(friday);
        values.add(saturday);
        // Does not contain the billboard already, create new schedule
        if (!BillboardScheduleExistsTest(billboard)) {
            System.out.println("MockScheduleTable did not contain a schedule for " + billboard + " ...adding the schedule!");
            internal.put(billboard, new ArrayList<>()); // Add a key for new schedule
        } else { // Edit existing schedule
            System.out.println("MockScheduleTable contains a schedule for " + billboard + " ...editing the schedule!");
        }
        internal.get(billboard).add(values); // Add values to the MockScheduleTable

    }

    /**
     * Method returns true if the billboard is already scheduled in the MockScheduleTable, false otherwise.
     * @param billboard A String which provides the Billboard Name to search in the MockScheduleTable.
     * @return Boolean true or false to indicate whether the billboard has been scheduled (true), or not (false).
     */
    private static boolean BillboardScheduleExistsTest(String billboard) {
        return internal.containsKey(billboard);
    }


}
