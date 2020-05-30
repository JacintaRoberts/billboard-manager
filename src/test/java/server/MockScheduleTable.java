package server;

import java.io.IOException;
import java.lang.reflect.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import static server.MockBillboardTable.getBillboardInformationTest;
import static server.MockSessionTokens.getUsernameFromTokenTest;
import static server.MockUserTable.hasPermissionTest;
import static server.Server.Permission.ScheduleBillboard;
import static server.Server.ServerAcknowledge;
import static server.Server.ServerAcknowledge.*;

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
     * @param billboard A String which provides Billboard Name to create the schedule for in the MockBillboardTable
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
     * @return Returns a ServerAcknowledge to indicate whether or not the Schedule was created/edited successfully,
     * or failed due to some other reason.
     */
    protected static ServerAcknowledge updateScheduleTest(String sessionToken, String billboard, String startTime,
                                                          String duration, String creationDateTime, String repeat,
                                                          String sunday, String monday, String tuesday, String wednesday,
                                                          String thursday, String friday, String saturday) {
        // User requires the ScheduleBillboard permission to perform this method
        String callingUsername = getUsernameFromTokenTest(sessionToken);
        if (hasPermissionTest(callingUsername, ScheduleBillboard)){
            // Ensure billboard exists
            ServerAcknowledge billboardExist = getBillboardInformationTest(billboard).getServerResponse();
            if (billboardExist.equals(BillboardNotExists)){
                System.out.println("Billboard does not exist");
                return BillboardNotExists; // 1. Billboard no longer exists
            } else {
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
     * Create/edit billboard schedule in the MockScheduleTable
     * @param billboard A String which provides Billboard Name to create the schedule for in the MockBillboardTable
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
    private static void addScheduleTest(String billboard, String startTime, String duration, String creationDateTime,
                                        String repeat, String sunday, String monday, String tuesday, String wednesday,
                                        String thursday, String friday, String saturday) {
        // Add values to array list
        ArrayList<String> values = new ArrayList();
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
     * deleteSchedules removes the schedules associated with the billboard name from the MockScheduleTable.
     * Returns a serverAcknowledge to signal whether the schedule deletion was successful, or if an exception occurred.
     * @param sessionToken A String which provides request username to search in MockUserTable for permissions
     * @param billboard A String which provides Billboard Name of the schedule to be deleted in the MockBillboardTable
     * @return Returns a ServerAcknowledge whether or not the Schedule was deleted successfully,
     * or failed due to some other reason.
     */
    public static ServerAcknowledge deleteScheduleTest(String sessionToken, String billboard) {
        // User requires the ScheduleBillboard permission to perform this method
        String callingUsername = getUsernameFromTokenTest(sessionToken);
        if (hasPermissionTest(callingUsername, ScheduleBillboard)){
            // Ensure that the billboard exists in the MockBillboardTable
            ServerAcknowledge billboardExist = getBillboardInformationTest(billboard).getServerResponse();
            if (billboardExist.equals(BillboardNotExists)){
                System.out.println("Billboard does not exist");
                return BillboardNotExists; // 1. Billboard no longer exists
            } else {
                // Ensure that the schedule to be deleted exists
                if (!BillboardScheduleExistsTest(billboard)) {
                    System.out.println("Schedule does not exist");
                    return ScheduleNotExists; // 2. Schedule to be deleted no longer exists
                } else {
                    // Delete the billboard's corresponding schedule from the MockScheduleTable
                    internal.remove(billboard);
                    System.out.println("Schedule was successfully deleted");
                    return Success; // 3. Successfully deleted schedule
                }
            }
        } else {
            System.out.println("Insufficient User Permission");
            return InsufficientPermission; // 4. User has insufficient permissions to remove the schedule.
        }
    }


    /**
     * Method returns true if the billboard is already scheduled in the MockScheduleTable, false otherwise.
     * @param billboard A String which provides the Billboard Name to search in the MockScheduleTable.
     * @return Boolean true or false to indicate whether the billboard has been scheduled (true), or not (false).
     */
    protected static boolean BillboardScheduleExistsTest(String billboard) {
        return internal.containsKey(billboard);
    }



    /**
     * This function will list Schedules for billboards for a specific day. The day parameter is parsed into as a string
     * and filters results to display raw schedules for the day.
     * <p>
     * This method always returns immediately. It will either return a success message or fail message if there is nothing
     * to return for billboard schedules
     * @param billboard A String of the billboard name to fetch the schedule information of from the MockScheduleTable
     * @return Returns a scheduleInfo object which contains information on all fields. Each field is an array and can be read via getters.
     */
    protected static ScheduleInfo getScheduleInformation(String sessionToken, String billboard) {
        // Initialise variable array
        String retrievedBillboard;
        String retrievedStartTime;
        String retrievedDuration;
        String retrievedCreationDateTime;
        String retrievedRepeat;
        String retrievedSunday;
        String retrievedMonday;
        String retrievedTuesday;
        String retrievedWednesday;
        String retrievedThursday;
        String retrievedFriday;
        String retrievedSaturday;
        ServerAcknowledge serverResponse = null;
        // If the Schedule does not exist for the requested billboard
        if (!BillboardScheduleExistsTest(billboard)) {
            serverResponse = ScheduleNotExists;
            retrievedBillboard = null;
            retrievedStartTime = null;
            retrievedDuration = null;
            retrievedCreationDateTime = null;
            retrievedRepeat = null;
            retrievedSunday = null;
            retrievedMonday = null;
            retrievedTuesday = null;
            retrievedWednesday = null;
            retrievedThursday = null;
            retrievedFriday = null;
            retrievedSaturday = null;
        } else {
            ArrayList<String> retrievedSchedule = (ArrayList) internal.get(billboard).get(0);
            // Store return from sql into respective data fields.
            retrievedBillboard = retrievedSchedule.get(0);
            retrievedStartTime = retrievedSchedule.get(1);
            retrievedDuration = retrievedSchedule.get(2);
            retrievedCreationDateTime = retrievedSchedule.get(3);
            retrievedRepeat = retrievedSchedule.get(4);
            retrievedSunday = retrievedSchedule.get(5);
            retrievedMonday = retrievedSchedule.get(6);
            retrievedTuesday = retrievedSchedule.get(7);
            retrievedWednesday = retrievedSchedule.get(8);
            retrievedThursday = retrievedSchedule.get(9);
            retrievedFriday = retrievedSchedule.get(10);
            retrievedSaturday = retrievedSchedule.get(11);
            serverResponse = Success;
        }
        // Create scheduleInfo return object
        ScheduleInfo scheduleInfo = new ScheduleInfo(serverResponse,
                retrievedBillboard,
                retrievedStartTime,
                retrievedDuration,
                retrievedCreationDateTime,
                retrievedRepeat,
                retrievedSunday,
                retrievedMonday,
                retrievedTuesday,
                retrievedWednesday,
                retrievedThursday,
                retrievedFriday,
                retrievedSaturday
        );
        // Return scheduleInfo Object
        return scheduleInfo;
    }


}
