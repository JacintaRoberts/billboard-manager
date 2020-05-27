package controlPanel;

import helpers.Helpers;
import server.Server;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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
     *
     * @param scheduleInfo@return
     */
    public static Server.ServerAcknowledge updateScheduleBillboardRequest(String sessionToken, ArrayList<Object> scheduleInfo) throws IOException, ClassNotFoundException {
        // Define Varaibles
        String billboardName = String.valueOf(scheduleInfo.get(0));
        ArrayList<Boolean>  daysOfWeek = (ArrayList<Boolean>) scheduleInfo.get(1);
        Integer Monday = daysOfWeek.get(0) ? 1 : 0;
        Integer Tuesday = daysOfWeek.get(1) ? 1 : 0;
        Integer Wednesday = daysOfWeek.get(2) ? 1 : 0;
        Integer Thursday = daysOfWeek.get(3) ? 1 : 0;
        Integer Friday = daysOfWeek.get(4) ? 1 : 0;
        Integer Saturday = daysOfWeek.get(5) ? 1 : 0;
        Integer Sunday = daysOfWeek.get(6) ? 1 : 0;
        String startHour = String.valueOf(scheduleInfo.get(2));
        String startMin = String.valueOf(scheduleInfo.get(3));
        Integer Duration = (Integer) scheduleInfo.get(4);
        Integer Repeat = (Integer) scheduleInfo.get(6);

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
