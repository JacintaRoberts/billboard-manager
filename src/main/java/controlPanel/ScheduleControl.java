package controlPanel;

import helpers.Helpers;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ScheduleControl {


    /**
     * Send Queries: Schedule Creation. This is a generic method which sends a create billboard Schedule request
     * from control panel to server.
     * <p>
     * This method always returns immediately.
     * @param  sessionToken A sessionToken generated when logged in
     * @param  startTime A String in format of Java Time to store into database
     * @param  Duration An Integer representing an integer which provides Duration which to store into database
     * @param  CreationDateTime A String in format of DateTime which provides CreationDateTime to store into database
     * @param  Repeat An Integer representing an integer how often the schedule is repeated (in minutes)
     * @param  Sunday An Integer that's either 1 or 0 to see if the schedule is to be run during Sunday
     * @param  Monday An Integer that's either 1 or 0  to see if the schedule is to be run during Monday
     * @param  Tuesday An Integer that's either 1 or 0  to see if the schedule is to be run during Tuesday
     * @param  Wednesday An Integer that's either 1 or 0  to see if the schedule is to be run during Wednesday
     * @param  Thursday An Integer that's either 1 or 0  to see if the schedule is to be run during Thursday
     * @param  Friday An Integer that's either 1 or 0  to see if the schedule is to be run during Friday
     * @param  Saturday An Integer that's either 1 or 0  to see if the schedule is to be run during Saturday
     * @return
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
     * @param  sessionToken A sessionToken generated when logged in
     * @param  startTime A String in format of Java Time to store into database
     * @param  Duration An Integer representing an integer which provides Duration which to store into database
     * @param  CreationDateTime A String in format of DateTime which provides CreationDateTime to store into database
     * @param  Repeat An Integer representing an integer how often the schedule is repeated (in minutes)
     * @param  Sunday An Integer that's either 1 or 0 to see if the schedule is to be run during Sunday
     * @param  Monday An Integer that's either 1 or 0  to see if the schedule is to be run during Monday
     * @param  Tuesday An Integer that's either 1 or 0  to see if the schedule is to be run during Tuesday
     * @param  Wednesday An Integer that's either 1 or 0  to see if the schedule is to be run during Wednesday
     * @param  Thursday An Integer that's either 1 or 0  to see if the schedule is to be run during Thursday
     * @param  Friday An Integer that's either 1 or 0  to see if the schedule is to be run during Friday
     * @param  Saturday An Integer that's either 1 or 0  to see if the schedule is to be run during Saturday
     * @return
     */
    public static  String updateScheduleBillboardRequest(String sessionToken, String billboardName, String startTime, int Duration,
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
            String message = String.format("Schedule,EditSchedule,%s,%s,%s,%d,%s,%d,%d,%d,%d,%d,%d,%d,%d",
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
            return serverResponse = "Fail: Invalid Duration Repeat Combination";
        }
    }

    /**
     * Send Queries: Schedule. This is a generic method which sends a request to Delete a billboard schedule
     * from control panel to server.
     * <p>
     * This method always returns immediately.
     * @param  sessionToken A sessionToken generated when logged in
     * @param  billboardName A String which provides Billboard Name to store into database
     * @return
     */
    public static  String deleteScheduleRequest(String sessionToken,
                                         String billboardName) throws IOException, ClassNotFoundException {
        String message = String.format("Schedule,DeleteSchedule,%s,%s",
                sessionToken,
                billboardName);
        return (String) Helpers.initClient(message); // Send constructed method request and parameters to the server
    }

    /**
     * Send Queries: Schedule. This is a generic method which sends a request to Delete all billboard schedule
     * from control panel to server.
     * <p>
     * This method always returns immediately.
     * @param  sessionToken A sessionToken generated when logged in
     * @return
     */
    public static  String deleteAllScheduleRequest(String sessionToken) throws IOException, ClassNotFoundException {
        String message = String.format("Schedule,DeleteAllSchedule,%s",
                sessionToken);
        return (String) Helpers.initClient(message); // Send constructed method request and parameters to the server
    }


    /**
     * Send Queries: Schedule. This is a generic method which sends a request to List all billboard schedule
     * from control panel to server for a specific  day.
     * <p>
     * This method always returns immediately.
     * @param  sessionToken A sessionToken generated when logged in
     * @param  Day A String which provides Billboard Name to store into database
     * @return
     */
    public static  Object listDayScheduleRequest(String sessionToken,
                                        String Day) throws IOException, ClassNotFoundException {
        String message = String.format("Schedule,ListAllDaySchedule,%s,%s",
                sessionToken,
                Day);
        return Helpers.initClient(message); // Send constructed method request and parameters to the server
    }


    /**
     * Send Queries: Schedule. This is a generic method which sends a request to get a specific billboard schedule
     * from control panel to server/
     * <p>
     * This method always returns immediately.
     * @param  sessionToken A sessionToken generated when logged in
     * @param  BillboardName A String which provides Billboard Name to filter schedules from database
     * @return
     */
    public static  Object listABillboardSchedule(String sessionToken,
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
     * @param  sessionToken A sessionToken generated when logged in
     * @param  Day A String which provides Billboard Name to store into database
     * @return
     */
    public static Object listActiveSchedule(String sessionToken,
                                            String Day, String currentTime) throws IOException, ClassNotFoundException {

        // Parses the date
//        LocalDate dt = LocalDate.parse("2018-11-27");
        // Prints the day
//        System.out.println(dt.getDayOfWeek());
//        LocalTime now = LocalTime.now();
        
        String message = String.format("Schedule,ListActiveSchedule,%s,%s,%s",
                sessionToken,
                Day,
                currentTime);
        return Helpers.initClient(message); // Send constructed method request and parameters to the server
    }


}
