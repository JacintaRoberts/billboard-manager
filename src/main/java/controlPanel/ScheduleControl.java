package controlPanel;

import helpers.Helpers;
import server.Server;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * The ScheduleControl contains on the communication methods between the Control Panel to the server for Schedule Methods. It includes methods
 * which will use functions from helpers that initialises communication and returns relevant object from server or server acknowledgements,
 * such as creating, editing, removing Schedules.
 */

public class ScheduleControl {


    /**
     * Send Queries: Schedule Creation. This is a generic method which sends a create billboard Schedule request
     * from control panel to server.
     * <p>
     * This method always returns immediately.
     * @param sessionToken A String which represents the sessionToken generated when logged in.
     * @param billboardName A String which represents the billboard name to schedule.
     * @param startTime A String in format of Java Time to store into database.
     * @param Duration An Integer representing an integer which provides Duration which to store into database.
     * @param CreationDateTime A String in format of DateTime which provides CreationDateTime to store into database.
     * @param Repeat An Integer representing an integer how often the schedule is repeated (in minutes).
     * @param Sunday An Integer that's either 1 or 0 to see if the schedule is to be run during Sunday.
     * @param Monday An Integer that's either 1 or 0  to see if the schedule is to be run during Monday.
     * @param Tuesday An Integer that's either 1 or 0  to see if the schedule is to be run during Tuesday.
     * @param Wednesday An Integer that's either 1 or 0  to see if the schedule is to be run during Wednesday.
     * @param Thursday An Integer that's either 1 or 0  to see if the schedule is to be run during Thursday.
     * @param Friday An Integer that's either 1 or 0  to see if the schedule is to be run during Friday.
     * @param Saturday An Integer that's either 1 or 0  to see if the schedule is to be run during Saturday.
     * @return A String which is an acknowledgement message from the server.
     * @throws IOException Throws an exception if an I/O exception of some sort has occurred.
     * @throws ClassNotFoundException Throws an exception when a specified class cannot be found in the classpath.
     */
    public static String scheduleBillboardRequest(String sessionToken, String billboardName, String startTime, Integer Duration,
                                           String CreationDateTime, Integer Repeat, Integer Sunday, Integer Monday,
                                           Integer Tuesday, Integer Wednesday, Integer Thursday, Integer Friday,
                                           Integer Saturday) throws IOException, ClassNotFoundException {
        // Create Formatter
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // Initialise Variable
        LocalTime localTimeStartTime = LocalTime.parse(startTime);

        // Parse Formatted Time Variables
        String formatStartTime = localTimeStartTime.format(timeFormatter);
        String formatCreationDateTime = String.valueOf(LocalDateTime.parse(CreationDateTime, dateTimeFormatter));


        // Check Valid Duration Repeat variable
        if(Duration < Repeat){
            String message = String.format("Schedule,CreateSchedule,%s,%s,%s,%d,%s,%d,%d,%d,%d,%d,%d,%d,%d",
                    sessionToken,
                    billboardName,
                    formatStartTime,
                    Duration,
                    formatCreationDateTime,
                    Repeat,
                    Sunday,
                    Monday,
                    Tuesday,
                    Wednesday,
                    Thursday,
                    Friday,
                    Saturday);
            return (String) Helpers.initClient(message);
        } else {
            String serverResponse;
            return serverResponse = "Fail: Billboard Scheduled More Frequently Than Duration";
        }
    }


    /**
     * Send Queries: Schedule Update. This is a generic method which sends a update billboard Schedule request
     * from control panel to server.
     * <p>
     * This method always returns immediately.
     * @param sessionToken A String which represents the sessionToken generated when logged in.
     * @param scheduleInfo An ArrayList(Object) which is the current schedule information.
     * @return A Server.ServerAcknowledge which is an acknowledgement message from the server.
     * @throws IOException Throws an exception if an I/O exception of some sort has occurred.
     * @throws ClassNotFoundException Throws an exception when a specified class cannot be found in the classpath.
     */
    public static Server.ServerAcknowledge updateScheduleBillboardRequest(String sessionToken, ArrayList<Object> scheduleInfo) throws IOException, ClassNotFoundException {
        // Define Variables
        String billboardName = String.valueOf(scheduleInfo.get(0));
        ArrayList<Boolean>  daysOfWeek = (ArrayList<Boolean>) scheduleInfo.get(1);
        Integer Monday = daysOfWeek.get(0) ? 1 : 0;
        Integer Tuesday = daysOfWeek.get(1) ? 1 : 0;
        Integer Wednesday = daysOfWeek.get(2) ? 1 : 0;
        Integer Thursday = daysOfWeek.get(3) ? 1 : 0;
        Integer Friday = daysOfWeek.get(4) ? 1 : 0;
        Integer Saturday = daysOfWeek.get(5) ? 1 : 0;
        Integer Sunday = daysOfWeek.get(6) ? 1 : 0;
        Integer startHourCalcs = (Integer) scheduleInfo.get(2);
        String startMin = String.valueOf(scheduleInfo.get(3));
        Integer Duration = (Integer) scheduleInfo.get(4);
        Integer Repeat = (Integer) scheduleInfo.get(6);
        Boolean PM = scheduleInfo.get(7).equals("PM");
        if(PM && startHourCalcs != 12){
            startHourCalcs  += 12;
        } else {
            // dont do anything in AM
        }
        String startHour = String.valueOf(startHourCalcs);

        // Condition if hourly
        if (scheduleInfo.get(5).equals("hourly")){
            Repeat = 60;
        }

        String startTime = startHour + ":" + startMin;
        // Create Formatter
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // Parse Formatted Time Variables
        LocalDateTime CreationDateTime = LocalDateTime.now();
        String formatCreationDateTime = CreationDateTime.format(dateTimeFormatter);

        // Ensure its 0
        if (Repeat == null){
            Repeat = 0;
        }

        String message = String.format("Schedule,UpdateSchedule,%s,%s,%s,%d,%s,%d,%d,%d,%d,%d,%d,%d,%d",
                sessionToken,
                billboardName,
                startTime,
                Duration,
                formatCreationDateTime,
                Repeat,
                Sunday,
                Monday,
                Tuesday,
                Wednesday,
                Thursday,
                Friday,
                Saturday);
        return (Server.ServerAcknowledge) Helpers.initClient(message);
    }

    /**
     * Send Queries: Schedule. This is a generic method which sends a request to Delete a billboard schedule
     * from control panel to server.
     * <p>
     * This method always returns immediately.
     * @param sessionToken A String which represents the sessionToken generated when logged in.
     * @param billboardName A String which provides Billboard Name to store into database.
     * @return A Server.ServerAcknowledge which is an acknowledgement message from the server.
     * @throws IOException Throws an exception if an I/O exception of some sort has occurred.
     * @throws ClassNotFoundException Throws an exception when a specified class cannot be found in the classpath.
     */
    public static Server.ServerAcknowledge deleteScheduleRequest(String sessionToken,
                                                                 String billboardName) throws IOException, ClassNotFoundException {
        String message = String.format("Schedule,DeleteSchedule,%s,%s",
                sessionToken,
                billboardName);
        return (Server.ServerAcknowledge) Helpers.initClient(message); // Send constructed method request and parameters to the server
    }

    /**
     * Send Queries: Schedule. This is a generic method which sends a request to Delete all billboard schedule
     * from control panel to server.
     * <p>
     * This method always returns immediately.
     * @param sessionToken A String which represents the sessionToken generated when logged in.
     * @return A String which is an acknowledgement message from the server.
     * @throws IOException Throws an exception if an I/O exception of some sort has occurred.
     * @throws ClassNotFoundException Throws an exception when a specified class cannot be found in the classpath.
     */
    public static String deleteAllScheduleRequest(String sessionToken) throws IOException, ClassNotFoundException {
        String message = String.format("Schedule,DeleteAllSchedule,%s",
                sessionToken);
        return (String) Helpers.initClient(message); // Send constructed method request and parameters to the server
    }


    /**
     * Send Queries: Schedule. This is a generic method which sends a request to List all billboard schedule
     * from control panel to server for a specific  day.
     * <p>
     * This method always returns immediately.
     * @param sessionToken A String which represents the sessionToken generated when logged in.
     * @param Day A String which provides the day of the schedule to get.
     * @return An Object which is an acknowledgement message from the server.
     * @throws IOException Throws an exception if an I/O exception of some sort has occurred.
     * @throws ClassNotFoundException Throws an exception when a specified class cannot be found in the classpath.
     */
    public static Object listDayScheduleRequest(String sessionToken,
                                        String Day) throws IOException, ClassNotFoundException {
        String message = String.format("Schedule,ListAllDaySchedule,%s,%s",
                sessionToken,
                Day);
        return Helpers.initClient(message); // Send constructed method request and parameters to the server
    }


    /**
     * Send Queries: Schedule. This is a generic method which sends a request to get a specific billboard schedule
     * from control panel to server.
     * <p>
     * This method always returns immediately.
     * @param sessionToken A String which represents the sessionToken generated when logged in.
     * @param BillboardName A String which provides Billboard Name to store into database.
     * @return An Object which is an acknowledgement message from the server.
     * @throws IOException Throws an exception if an I/O exception of some sort has occurred.
     * @throws ClassNotFoundException Throws an exception when a specified class cannot be found in the classpath.
     */
    public static Object listABillboardSchedule(String sessionToken,
                                                 String BillboardName) throws IOException, ClassNotFoundException {
        String message = String.format("Schedule,ListABillboardSchedule,%s,%s",
                sessionToken,
                BillboardName);
        return Helpers.initClient(message); // Send constructed method request and parameters to the server
    }


    /**
     * Send Queries: Schedule. This is a generic method which sends a request to List all billboard schedule
     * from control panel to server for a specific  day.
     * <p>
     * This method always returns immediately.
     * @param sessionToken A String which represents the sessionToken generated when logged in.
     * @param Day A String which provides the day of the schedule to get.
     * @param currentTime A String which provides the current time.
     * @return An Object which is an acknowledgement message from the server.
     * @throws IOException Throws an exception if an I/O exception of some sort has occurred.
     * @throws ClassNotFoundException Throws an exception when a specified class cannot be found in the classpath.
     */
    public static Object listActiveSchedule(String sessionToken,
                                            String Day, String currentTime) throws IOException, ClassNotFoundException {

        String message = String.format("Schedule,ListActiveSchedule,%s,%s,%s",
                sessionToken,
                Day,
                currentTime);
        return Helpers.initClient(message); // Send constructed method request and parameters to the server
    }


}
