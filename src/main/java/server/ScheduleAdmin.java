package server;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Locale;

import static server.Server.ServerAcknowledge.*;
import static server.Server.validateToken;

public class ScheduleAdmin {

    // Custom SQL Strings for Specific Queries
    public static final String COUNT_SCHEDULE_SQL = "SELECT COUNT(*) FROM Schedules";
    public static final String COUNT_FILTER_SCHEDULE_SQL = "SELECT COUNT(*) FROM Schedules WHERE BillboardName = ?";
    public static final String GET_FILTER_SCHEDULE_SQL = "SELECT * FROM Schedules WHERE BillboardName = ?";
    public static final String DELETE_ALL_SCHEDULE_SQL = "DELETE FROM Schedules";
    public static final String DELETE_SCHEDULE_SQL = "DELETE FROM Schedules WHERE BillboardName = ?";
    public static final String DROP_SCHEDULE_TABLE = "DROP TABLE IF EXISTS `BillboardDatabase`.`Schedules`";
    public static final String CREATE_SCHEDULE_TABLE = "CREATE TABLE IF NOT EXISTS `BillboardDatabase`.`Schedules` (\n" +
            "      `BillboardName` varchar(255) NOT NULL default '',\n" +
            "      `StartTime` TIME NOT NULL default CURRENT_TIMESTAMP,\n" +
            "      `Duration` INT NOT NULL default 1,\n" +
            "      `CreationDateTime` DATETIME NOT NULL default CURRENT_TIMESTAMP,\n" +
            "      `Repeat` INT default NULL,\n" +
            "      `Sunday` bool NOT NULL default 0,\n" +
            "      `Monday` bool NOT NULL default 0,\n" +
            "      `Tuesday` bool NOT NULL default 0,\n" +
            "      `Wednesday` bool NOT NULL default 0,\n" +
            "      `Thursday` bool NOT NULL default 0,\n" +
            "      `Friday` bool NOT NULL default 0,\n" +
            "      `Saturday` bool NOT NULL default 0,\n" +
            "      PRIMARY KEY (`BillboardName`))";
    public static final String EDIT_SCHEDULE_SQL = "UPDATE Schedules " +
            "SET `StartTime` = ?," +
            "`Duration` = ?," +
            "`CreationDateTime` = ?," +
            "`Repeat` = ?," +
            "`Sunday` = ?," +
            "`Monday` = ?," +
            "`Tuesday` = ?," +
            "`Wednesday` = ?," +
            "`Thursday` = ?," +
            "`Friday` = ?," +
            "`Saturday` = ?" +
            "WHERE `BillboardName` = ?";
    public static final String STORE_SCHEDULE_SQL = "INSERT INTO Schedules VALUES (?,?,?,?,?,?,?,?,?,?,?,?) ";
    public static final String LIST_SCHEDULE_SQL = "SELECT * FROM Schedules";
    public static final String MONDAY_LIST_FILTERED_SCHEDULE_SQL = "SELECT * FROM Schedules WHERE Monday = 1";
    public static final String MONDAY_COUNT_LIST_FILTERED_SCHEDULE_SQL = "SELECT COUNT(*) FROM Schedules WHERE Monday = 1";
    public static final String TUESDAY_LIST_FILTERED_SCHEDULE_SQL = "SELECT * FROM Schedules WHERE Tuesday = 1";
    public static final String TUESDAY_COUNT_LIST_FILTERED_SCHEDULE_SQL = "SELECT COUNT(*) FROM Schedules WHERE Tuesday = 1";
    public static final String WEDNESDAY_LIST_FILTERED_SCHEDULE_SQL = "SELECT * FROM Schedules WHERE Wednesday = 1";
    public static final String WEDNESDAY_COUNT_LIST_FILTERED_SCHEDULE_SQL = "SELECT COUNT(*) FROM Schedules WHERE Wednesday = 1";
    public static final String THURSDAY_LIST_FILTERED_SCHEDULE_SQL = "SELECT * FROM Schedules WHERE Thursday = 1";
    public static final String THURSDAY_COUNT_LIST_FILTERED_SCHEDULE_SQL = "SELECT COUNT(*) FROM Schedules WHERE Thursday = 1";
    public static final String FRIDAY_LIST_FILTERED_SCHEDULE_SQL = "SELECT * FROM Schedules WHERE Friday = 1";
    public static final String FRIDAY_COUNT_LIST_FILTERED_SCHEDULE_SQL = "SELECT COUNT(*) FROM Schedules WHERE Friday = 1";
    public static final String SATURDAY_LIST_FILTERED_SCHEDULE_SQL = "SELECT * FROM Schedules WHERE Saturday = 1";
    public static final String SATURDAY_COUNT_LIST_FILTERED_SCHEDULE_SQL = "SELECT COUNT(*) FROM Schedules WHERE Saturday = 1";
    public static final String SUNDAY_LIST_FILTERED_SCHEDULE_SQL = "SELECT * FROM Schedules WHERE Sunday = 1";
    public static final String SUNDAY_COUNT_LIST_FILTERED_SCHEDULE_SQL = "SELECT COUNT(*) FROM Schedules WHERE Sunday = 1";

    // Custom Parameters for connection
    public static Connection connection;
    public static PreparedStatement countFilterSchedule;
    public static PreparedStatement getFilterSchedule;
    public static PreparedStatement deleteSchedule;
    public static PreparedStatement editSchedule;
    public static PreparedStatement createSchedule;

    /**
     * Ensures that the ScheduleTable is created inside the database. The method should be called when the server is being
     * set up to ensure the Schedule Table exists if it is not in the database.
     * <p>
     * This method always returns immediately. It will either return a success message "Schedule Table Created", or further
     * information such as Schedule Table exists with/without data pre-populated inside
     * @return Ensures Schedule Table exists within the Database
     */
    public static String createScheduleTable() throws IOException, SQLException {
        // Initialise Variable
        String resultMessage;
        // Set Connection
        connection = DbConnection.getInstance();
        Statement countSchedule = connection.createStatement();
        ResultSet rs = null;
        try {
            // Check if Table exists, and if Data exists
            rs = countSchedule.executeQuery(COUNT_SCHEDULE_SQL);
            rs.next();
            String count = rs.getString(1);
            if (count.equals("0")){
                resultMessage = "Schedule Table Exists and has No Data";
            } else {
                resultMessage = "Schedule Table Exists and has Data";
            }
        } catch (SQLSyntaxErrorException throwables) {
            // If dosent exist, create table
            rs = countSchedule.executeQuery(CREATE_SCHEDULE_TABLE);
            resultMessage = "Schedule Table Created";
        }
        return resultMessage;
    }


    /**
     * This function will drop the Schedule Table if required. No checks are done. This function is not linked to any functionality
     * however is made during testing processes.
     * <p>
     * This method always returns immediately. It will either return a success message "Schedule Table Dropped", or throw an
     * SQLException error as the method was unable to be completed
     * @return Drops Schedule Table if exists.
     */
    public static String dropScheduleTable() throws IOException, SQLException{
        // Initialise String
        String resultMessage;
        // Get Connection
        connection = DbConnection.getInstance();
        // Run Statement
        Statement dropSchedule = connection.createStatement();
        ResultSet rs = dropSchedule.executeQuery(DROP_SCHEDULE_TABLE);
        // Return
        resultMessage = "Schedule Table Dropped";
        return resultMessage;
    }


//    /**
//     * CreateSchedule will create Schedules from existing billboards. Each parameter for the function are fed in through the
//     * Control Panel and can be assumed to be valid.
//     * <p>
//     * This method always returns immediately, and will return a relevant string noting if there is any errors or if the schedule
//     * gets created successfully.
//     * @param  billboard A String which provides Billboard Name to store into database
//     * @param  StartTime A String in format of Java Time to store into database
//     * @param  Duration A String representing an integer which provides Duration which to store into database
//     * @param  CreationDateTime A String in format of DateTime which provides CreationDateTime to store into database
//     * @param  Repeat A String representing an integer how often the schedule is repeated (in minutes)
//     * @param  Sunday A String that's either 1 or 0 to see if the schedule is to be run during Sunday
//     * @param  Monday A String that's either 1 or 0  to see if the schedule is to be run during Monday
//     * @param  Tuesday A String that's either 1 or 0  to see if the schedule is to be run during Tuesday
//     * @param  Wednesday A String that's either 1 or 0  to see if the schedule is to be run during Wednesday
//     * @param  Thursday A String that's either 1 or 0  to see if the schedule is to be run during Thursday
//     * @param  Friday A String that's either 1 or 0  to see if the schedule is to be run during Friday
//     * @param  Saturday A String that's either 1 or 0  to see if the schedule is to be run during Saturday
//     * @return Returns a message string whether or not the Schedule was created successfully or failed due to reasons.
//     */
//    public static String createSchedule(String billboard,
//                                        String StartTime,
//                                        String Duration,
//                                        String CreationDateTime,
//                                        String Repeat,
//                                        String Sunday,
//                                        String Monday,
//                                        String Tuesday,
//                                        String Wednesday,
//                                        String Thursday,
//                                        String Friday,
//                                        String Saturday) throws IOException, SQLException {
//        // Set Parameters and Varaibles
//        String resultMessage;
//        String validCharacters = "([A-Za-z0-9-_ ]+)";
//        // First Check Valid Characters for Billboard String
//        if (billboard.matches(validCharacters)) {
//            // Get Connection to see if there is a billboard that exists
//            connection = DbConnection.getInstance();
//            countFilterSchedule = connection.prepareStatement(COUNT_FILTER_SCHEDULE_SQL);
//            countFilterSchedule.setString(1,billboard);
//            ResultSet rs = countFilterSchedule.executeQuery();
//            rs.next();
//            String count = rs.getString(1);
//            if (count.equals("1")){
//                resultMessage = "Fail: Schedule Already Exists";
//            }else {
//                // Get connection to see if Billboard Exists to create a Schedule
//                connection = DbConnection.getInstance();
//                BillboardAdmin.countFilterBillboard = connection.prepareStatement(BillboardAdmin.COUNT_FILTER_BILLBOARD_SQL);
//                BillboardAdmin.countFilterBillboard.setString(1,billboard);
//                rs = BillboardAdmin.countFilterBillboard.executeQuery();
//                rs.next();
//                String count2 = rs.getString(1);
//                if (count2.equals("0")){
//                    resultMessage = "Fail: Billboard does not Exist";
//                } else{
//                    // Create Schedule to store parameters
//                    connection = DbConnection.getInstance();
//                    createSchedule = connection.prepareStatement(STORE_SCHEDULE_SQL);
//                    createSchedule.setString(1,billboard);
//                    createSchedule.setString(2,StartTime);
//                    createSchedule.setString(3,Duration);
//                    createSchedule.setString(4,CreationDateTime);
//                    createSchedule.setString(5,Repeat);
//                    createSchedule.setString(6,Sunday);
//                    createSchedule.setString(7,Monday);
//                    createSchedule.setString(8,Tuesday);
//                    createSchedule.setString(9,Wednesday);
//                    createSchedule.setString(10,Thursday);
//                    createSchedule.setString(11,Friday);
//                    createSchedule.setString(12,Saturday);
//                    rs = createSchedule.executeQuery();
//                    resultMessage = "Pass: Billboard Scheduled";
//                }
//            }
//
//        } else {
//            resultMessage = "Fail: Billboard Name Contains Illegal Characters";
//        }
//        return resultMessage;
//    }
//


    /**
     * This function will delete the Schedule Table Data if required. No checks are done.
     * <p>
     * This method always returns immediately. It will either return a success message "Schedule Table Dropped", or
     * a fail message if it dosent run
     * @return Delete Schedule Table Data if exists success string.
     */
    public static String deleteAllSchedules() throws IOException, SQLException {
        String resultMessage;
        connection = DbConnection.getInstance();
        Statement countSchedule = connection.createStatement();
        ResultSet rs = countSchedule.executeQuery(COUNT_SCHEDULE_SQL);
        rs.next();
        String count = rs.getString(1);
        if (count.equals("0")){
            resultMessage = "Fail: No Schedules Exists";
        }else {
            connection = DbConnection.getInstance();
            Statement deleteAllSchedules = connection.createStatement();
            rs = deleteAllSchedules.executeQuery(DELETE_ALL_SCHEDULE_SQL);
        }
        resultMessage = "Pass: All Schedules Deleted";
        return resultMessage;
    }


    /**
     * This function will delete a specific data in the Schedule Table. THe billboard that is to be deleted will be specified
     * in the billboard parameter
     * <p>
     * This method always returns immediately. It will either return a success message or fail message if no deletion can
     * be done
     * @return Returns a resultMessage string noting if the billboard passes (Pass: Billboard Schedule Deleted) or fails due to reasons.
     */
    public static String deleteSchedule(String billboard) throws IOException, SQLException {
        // Set Variables
        String resultMessage;
        String validCharacters = "([A-Za-z0-9-_ ]+)";
        if (billboard.matches(validCharacters)) {
            // Set Connection to see if billboard exists or not
            connection = DbConnection.getInstance();
            BillboardAdmin.countFilterBillboard = connection.prepareStatement(BillboardAdmin.COUNT_FILTER_BILLBOARD_SQL);
            BillboardAdmin.countFilterBillboard.setString(1,billboard);
            ResultSet rs = BillboardAdmin.countFilterBillboard.executeQuery();
            rs.next();
            String count2 = rs.getString(1);
            if (count2.equals("0")){
                resultMessage = "Fail: Billboard Does Not Exist";
            } else{
                // Set Connection to see if Schedule exists or not
                connection = DbConnection.getInstance();
                countFilterSchedule = connection.prepareStatement(COUNT_FILTER_SCHEDULE_SQL);
                countFilterSchedule.setString(1,billboard);
                rs = countFilterSchedule.executeQuery();
                rs.next();
                String count = rs.getString(1);
                if (count.equals("1")){
                    // Delete Billboard Schedule
                    connection = DbConnection.getInstance();
                    deleteSchedule = connection.prepareStatement(DELETE_SCHEDULE_SQL);
                    deleteSchedule.setString(1,billboard);
                    rs = deleteSchedule.executeQuery();
                    resultMessage = "Pass: Billboard Schedule Deleted";
                }else {
                    resultMessage = "Fail: Billboard Schedule Does not Exist";
                }
            }
        } else {
            resultMessage = "Fail: Billboard Name Contains Illegal Characters";
        }
        // Return resultMessage String
        return resultMessage;
    }



    /**
     * This function will list Schedules for billboards. This will only show the raw schedules with no additional computation
     * for repeats.
     * <p>
     * This method always returns immediately. It will either return a success message or fail message if there is nothing
     * to return for billboard schedules
     * @return Returns a ScheduleList object which contains information on all fields. Each field is an array and can be read via getters.
     */
    public static ScheduleList listScheduleInformation() throws IOException, SQLException {
        // Initialise Variable array
        ArrayList<String> retrievedBillboard = new ArrayList<>();
        ArrayList<String> retrievedStartTime = new ArrayList<>();
        ArrayList<String> retrievedDuration = new ArrayList<>();
        ArrayList<String> retrievedEndTime = new ArrayList<>();
        ArrayList<String> retrievedCreationDateTime = new ArrayList<>();
        ArrayList<String> retrievedRepeat = new ArrayList<>();
        ArrayList<String> retrievedSunday = new ArrayList<>();
        ArrayList<String> retrievedMonday = new ArrayList<>();
        ArrayList<String> retrievedTueseday = new ArrayList<>();
        ArrayList<String> retrievedWednesday = new ArrayList<>();
        ArrayList<String> retrievedThursday = new ArrayList<>();
        ArrayList<String> retrievedFriday = new ArrayList<>();
        ArrayList<String> retrievedSaturday = new ArrayList<>();
        // Set up connection to see if there are any schedules
        connection = DbConnection.getInstance();
        Statement countSchedule = connection.createStatement();
        ResultSet rs = countSchedule.executeQuery(COUNT_SCHEDULE_SQL);
        rs.next();
        String count = rs.getString(1);
        String serverResponse = null;
        if (count.equals("0")) {
            // No Schedules Exists, populate everything with 0 and Fail message.
            serverResponse = "Fail: No Schedule Exists";
            retrievedBillboard.add("0");
            retrievedStartTime.add("0");
            retrievedDuration.add("0");
            retrievedEndTime.add("0");
            retrievedCreationDateTime.add("0");
            retrievedRepeat.add("0");
            retrievedSunday.add("0");
            retrievedMonday.add("0");
            retrievedTueseday.add("0");
            retrievedWednesday.add("0");
            retrievedThursday.add("0");
            retrievedFriday.add("0");
            retrievedSaturday.add("0");
        } else {
            // Schedule Exists, get everything in Database and loop
            connection = DbConnection.getInstance();
            Statement listSchedule = connection.createStatement();
            rs = listSchedule.executeQuery(LIST_SCHEDULE_SQL);
            int columnCount = rs.getMetaData().getColumnCount();
            // Loop through cursor  to get all data to store
            while (rs.next()) {
                retrievedBillboard.add(rs.getString(1));
                retrievedStartTime.add(rs.getString(2));
                retrievedDuration.add(rs.getString(3));
                retrievedEndTime.add("0");
                retrievedCreationDateTime.add(rs.getString(4));
                retrievedRepeat.add(rs.getString(5));
                retrievedSunday.add(rs.getString(6));
                retrievedMonday.add(rs.getString(7));
                retrievedTueseday.add(rs.getString(8));
                retrievedWednesday.add(rs.getString(9));
                retrievedThursday.add(rs.getString(10));
                retrievedFriday.add(rs.getString(11));
                retrievedSaturday.add(rs.getString(12));
            }
            // Set Message Response as Success
            serverResponse = "Pass: Schedule Detail List Returned";
        }
        // Create ScheduleList object for return
        ScheduleList scheduleList = new ScheduleList(serverResponse,
                retrievedBillboard,
                retrievedStartTime,
                retrievedDuration,
                retrievedEndTime,
                retrievedCreationDateTime,
                retrievedRepeat,
                retrievedSunday,
                retrievedMonday,
                retrievedTueseday,
                retrievedWednesday,
                retrievedThursday,
                retrievedFriday,
                retrievedSaturday
                );
        // Return Object
        return scheduleList;
    }


    /**
     * editSchedule will edit existing Schedules inside the Data Table. Each parameter for the function are fed in through the
     * Control Panel and can be assumed to be valid.
     * <p>
     * This method always returns immediately, and will return a relevant string noting if there is any errors or if the schedule
     * gets edited successfully.
     * @param  billboard A String which provides Billboard Name to search in the data table for editing
     * @param  StartTime A String in format of Java Time to store into database
     * @param  Duration A String representing an integer which provides Duration which to store into database
     * @param  CreationDateTime A String in format of DateTime which provides CreationDateTime to store into database
     * @param  Repeat A String representing an integer how often the schedule is repeated (in minutes)
     * @param  Sunday A String that's either 1 or 0 to see if the schedule is to be run during Sunday
     * @param  Monday A String that's either 1 or 0  to see if the schedule is to be run during Monday
     * @param  Tuesday A String that's either 1 or 0  to see if the schedule is to be run during Tuesday
     * @param  Wednesday A String that's either 1 or 0  to see if the schedule is to be run during Wednesday
     * @param  Thursday A String that's either 1 or 0  to see if the schedule is to be run during Thursday
     * @param  Friday A String that's either 1 or 0  to see if the schedule is to be run during Friday
     * @param  Saturday A String that's either 1 or 0  to see if the schedule is to be run during Saturday
     * @return Returns a message string whether or not the Schedule was created successfully or failed due to reasons.
     */
    public static Server.ServerAcknowledge updateSchedule(String sessionToken,
                                                          String billboard,
                                                          String StartTime,
                                                          String Duration,
                                                          String CreationDateTime,
                                                          String Repeat,
                                                          String Sunday,
                                                          String Monday,
                                                          String Tuesday,
                                                          String Wednesday,
                                                          String Thursday,
                                                          String Friday,
                                                          String Saturday) throws IOException, SQLException {
        if (validateToken(sessionToken)) {
            System.out.println("Session is valid");
            if (UserAdmin.checkSinglePermission(sessionToken, Server.Permission.ScheduleBillboard)){
                // Start conenction to see if billboard exists
                String billboardExist = BillboardAdmin.countFilterBillboardSql(billboard);
                if (billboardExist.equals("0")){
                    // Return Fail Message
                    System.out.println("Billboard does not exist");
                    return BillboardNotExists;
                } else{
                    // Update Schedule and return pass
                    updateScheduleSQL(billboard,StartTime,Duration,CreationDateTime,Repeat,Sunday,Monday,Tuesday,
                            Wednesday,Thursday,Friday,Saturday);
                    return Success;
                }
            } else {
                System.out.println("Permissions were not sufficient, no Schedule was Updated");
                return InsufficientPermission; // 3. Valid token but insufficient permission
            }
        } else {
            System.out.println("Session was not valid");
            return InvalidToken; // 4. Invalid token
        }
    }


    /**
     * Method to update Schedule from the database
     * @return
     * @throws IOException
     * @throws SQLException
     */
    public static void createScheduleSQL(String billboard,
                                         String StartTime,
                                         String Duration,
                                         String CreationDateTime,
                                         String Repeat,
                                         String Sunday,
                                         String Monday,
                                         String Tuesday,
                                         String Wednesday,
                                         String Thursday,
                                         String Friday,
                                         String Saturday) throws IOException, SQLException {
        connection = DbConnection.getInstance();
        editSchedule = connection.prepareStatement(EDIT_SCHEDULE_SQL);
        editSchedule.setString(1,StartTime);
        editSchedule.setString(2,Duration);
        editSchedule.setString(3,CreationDateTime);
        editSchedule.setString(4,Repeat);
        editSchedule.setString(5,Sunday);
        editSchedule.setString(6,Monday);
        editSchedule.setString(7,Tuesday);
        editSchedule.setString(8,Wednesday);
        editSchedule.setString(9,Thursday);
        editSchedule.setString(10,Friday);
        editSchedule.setString(11,Saturday);
        editSchedule.setString(12,billboard);
        editSchedule.executeQuery();
        System.out.println("query RUn");
    }

    /**
     * Method to update Schedule from the database
     * @return
     * @throws IOException
     * @throws SQLException
     */
    public static void updateScheduleSQL(String billboard,
                                         String StartTime,
                                         String Duration,
                                         String CreationDateTime,
                                         String Repeat,
                                         String Sunday,
                                         String Monday,
                                         String Tuesday,
                                         String Wednesday,
                                         String Thursday,
                                         String Friday,
                                         String Saturday) throws IOException, SQLException {
        connection = DbConnection.getInstance();
        editSchedule = connection.prepareStatement(EDIT_SCHEDULE_SQL);
        editSchedule.setString(1,StartTime);
        editSchedule.setString(2,Duration);
        editSchedule.setString(3,CreationDateTime);
        editSchedule.setString(4,Repeat);
        editSchedule.setString(5,Sunday);
        editSchedule.setString(6,Monday);
        editSchedule.setString(7,Tuesday);
        editSchedule.setString(8,Wednesday);
        editSchedule.setString(9,Thursday);
        editSchedule.setString(10,Friday);
        editSchedule.setString(11,Saturday);
        editSchedule.setString(12,billboard);
        editSchedule.executeQuery();
        System.out.println("query RUn");
    }


    /**
     * Method to update Schedule from the database
     * @return
     * @throws IOException
     * @throws SQLException
     */
    public static String countFilterScheduleSql(String billboard) throws IOException, SQLException {
        connection = DbConnection.getInstance();
        countFilterSchedule = connection.prepareStatement(COUNT_FILTER_SCHEDULE_SQL);
        countFilterSchedule.setString(1,billboard);
        ResultSet rs = countFilterSchedule.executeQuery();
        rs.next();
        String count = rs.getString(1);
        return count;
    }



    /**
     * This function will list Schedules for billboards for a specific day. The day parameter is parsed into as a string
     * and filters results to display raw schedules for the day.
     * <p>
     * This method always returns immediately. It will either return a success message or fail message if there is nothing
     * to return for billboard schedules
     * @param BillboardName A String to feed in to filter SQL quiries to return only schedules for a specifc Billboard.
     * @return Returns a ScheduleList object which contains information on all fields. Each field is an array and can be read via getters.
     */
    public static ScheduleInfo getScheduleInformation(String BillboardName) throws IOException, SQLException {
        // Initialise Variable array
        String retrievedBillboard;
        String retrievedStartTime;
        String retrievedDuration;
        String retrievedCreationDateTime;
        String retrievedRepeat;
        String retrievedSunday;
        String retrievedMonday;
        String retrievedTueseday;
        String retrievedWednesday;
        String retrievedThursday;
        String retrievedFriday;
        String retrievedSaturday;
        // Set storage parameters for single
        String serverResponse = null;
        //Check if Schedule Exists and updates as required
        connection = DbConnection.getInstance();
        countFilterSchedule = connection.prepareStatement(COUNT_FILTER_SCHEDULE_SQL);
        countFilterSchedule.setString(1,BillboardName);
        ResultSet rs = countFilterSchedule.executeQuery();
        rs.next();
        String count = rs.getString(1);
        if (count.equals("0")) {
            serverResponse = "Fail: No Schedule Exists";
            retrievedBillboard = "0";
            retrievedStartTime = "0";
            retrievedDuration = "0";
            retrievedCreationDateTime = "0";
            retrievedRepeat = "0";
            retrievedSunday = "0";
            retrievedMonday = "0";
            retrievedTueseday = "0";
            retrievedWednesday = "0";
            retrievedThursday = "0";
            retrievedFriday = "0";
            retrievedSaturday = "0";
        } else {
            getFilterSchedule = connection.prepareStatement(GET_FILTER_SCHEDULE_SQL);
            getFilterSchedule.setString(1,BillboardName);
            rs = getFilterSchedule.executeQuery();
            rs.next();
            // Store return from sql into respective data fields.
            retrievedBillboard = rs.getString(1);
            retrievedStartTime = rs.getString(2).substring(0, rs.getString(2).length() - 3);
            retrievedDuration = rs.getString(3);
            retrievedCreationDateTime = rs.getString(4).substring(0, rs.getString(4).length() - 5);
            retrievedRepeat = rs.getString(5);
            retrievedSunday = rs.getString(6);
            retrievedMonday = rs.getString(7);
            retrievedTueseday = rs.getString(8);
            retrievedWednesday = rs.getString(9);
            retrievedThursday = rs.getString(10);
            retrievedFriday = rs.getString(11);
            retrievedSaturday = rs.getString(12);
            // Success Return Message
            serverResponse = "Pass: Schedule Detail Returned";
        }

        // Create Schedulelist return object
        ScheduleInfo scheduleInfo = new ScheduleInfo(serverResponse,
                retrievedBillboard,
                retrievedStartTime,
                retrievedDuration,
                retrievedCreationDateTime,
                retrievedRepeat,
                retrievedSunday,
                retrievedMonday,
                retrievedTueseday,
                retrievedWednesday,
                retrievedThursday,
                retrievedFriday,
                retrievedSaturday
        );
        // Return scheduleList Object
        return scheduleInfo;
    }



    /**
     * This function will list all Schedules for billboards for a specific day. The day parameter is parsed into as a string
     * and filters results to display raw schedules for the day and then the resultant object is then imputed for time. This
     * function builds on listFilteredScheduleInformation and viewAllDaySchedule methods, and is called by control panel to server
     * <p>
     * This method always returns immediately. It will either return a success message or fail message if there is nothing
     * to return for billboard schedules
     * @param day A String to feed in to filter SQL quiries to return only schedules for a specifc day.
     * @return Returns a ScheduleList object which contains information on all fields. Each field is an array and can be read via getters.
     */
    public static ScheduleList listAllFilteredScheduleInformation(String day) throws IOException, SQLException {
        // Get rawDaySched
        ScheduleList rawDaySched = ScheduleAdmin.listFilteredScheduleInformation(day);
        // Impute AlldaySched
        ScheduleList allDaysched = ScheduleAdmin.viewAllDaySchedule(rawDaySched);
        return  allDaysched;
    }

    /**
     * This function will list Schedules for billboards for a specific day. The day parameter is parsed into as a string
     * and filters results to display raw schedules for the day.
     * <p>
     * This method always returns immediately. It will either return a success message or fail message if there is nothing
     * to return for billboard schedules
     * @param day A String to feed in to filter SQL quiries to return only schedules for a specifc day.
     * @return Returns a ScheduleList object which contains information on all fields. Each field is an array and can be read via getters.
     */
    public static ScheduleList listFilteredScheduleInformation(String day) throws IOException, SQLException {
        // Initialise Variable array
        ArrayList<String> retrievedBillboard = new ArrayList<>();
        ArrayList<String> retrievedStartTime = new ArrayList<>();
        ArrayList<String> retrievedDuration = new ArrayList<>();
        ArrayList<String> retrievedEndTime = new ArrayList<>();
        ArrayList<String> retrievedCreationDateTime = new ArrayList<>();
        ArrayList<String> retrievedRepeat = new ArrayList<>();
        ArrayList<String> retrievedSunday = new ArrayList<>();
        ArrayList<String> retrievedMonday = new ArrayList<>();
        ArrayList<String> retrievedTueseday = new ArrayList<>();
        ArrayList<String> retrievedWednesday = new ArrayList<>();
        ArrayList<String> retrievedThursday = new ArrayList<>();
        ArrayList<String> retrievedFriday = new ArrayList<>();
        ArrayList<String> retrievedSaturday = new ArrayList<>();
        // Set storage parameters for single
        String serverResponse = null;
        Integer dayCheckPass = 0;
        // Get instance to check if there is data in the day.
        connection = DbConnection.getInstance();
        Statement st = null;
        ResultSet rs = null;
        String count = "0";
        if (day.matches("Sunday")){
            st = connection.createStatement();
            rs = st.executeQuery(SUNDAY_COUNT_LIST_FILTERED_SCHEDULE_SQL);
            dayCheckPass = 1;
            rs.next();
            count = rs.getString(1);
        } else if (day.matches("Monday")){
            st = connection.createStatement();
            rs = st.executeQuery(MONDAY_COUNT_LIST_FILTERED_SCHEDULE_SQL);
            dayCheckPass = 1;
            rs.next();
            count = rs.getString(1);
        } else if (day.matches("Tuesday")){
            st = connection.createStatement();
            rs = st.executeQuery(TUESDAY_COUNT_LIST_FILTERED_SCHEDULE_SQL);
            dayCheckPass = 1;
            rs.next();
            count = rs.getString(1);
        } else if (day.matches("Wednesday")){
            st = connection.createStatement();
            rs = st.executeQuery(WEDNESDAY_COUNT_LIST_FILTERED_SCHEDULE_SQL);
            dayCheckPass = 1;
            rs.next();
            count = rs.getString(1);
        } else if (day.matches("Thursday")){
            st = connection.createStatement();
            rs = st.executeQuery(THURSDAY_COUNT_LIST_FILTERED_SCHEDULE_SQL);
            dayCheckPass = 1;
            rs.next();
            count = rs.getString(1);
        } else if (day.matches("Friday")){
            st = connection.createStatement();
            rs = st.executeQuery(FRIDAY_COUNT_LIST_FILTERED_SCHEDULE_SQL);
            dayCheckPass = 1;
            rs.next();
            count = rs.getString(1);
        } else if (day.matches("Saturday")){
            st = connection.createStatement();
            rs = st.executeQuery(SATURDAY_COUNT_LIST_FILTERED_SCHEDULE_SQL);
            dayCheckPass = 1;
            rs.next();
            count = rs.getString(1);
        } else {
            serverResponse = "Fail: Not a Valid Day";
            retrievedBillboard.add("0");
            retrievedStartTime.add("0");
            retrievedDuration.add("0");
            retrievedCreationDateTime.add("0");
            retrievedRepeat.add("0");
            retrievedSunday.add("0");
            retrievedMonday.add("0");
            retrievedTueseday.add("0");
            retrievedWednesday.add("0");
            retrievedThursday.add("0");
            retrievedFriday.add("0");
            retrievedSaturday.add("0");
            dayCheckPass = 0;
            count = "0";
        }
        // Run test to return.
        if (dayCheckPass.equals(0)){
            // Do nothing as the previous test fails
        } else if (count.equals("0") & dayCheckPass.equals(1)) {
            serverResponse = "Fail: No Schedule Exists";
            retrievedBillboard.add("0");
            retrievedStartTime.add("0");
            retrievedDuration.add("0");
            retrievedCreationDateTime.add("0");
            retrievedRepeat.add("0");
            retrievedSunday.add("0");
            retrievedMonday.add("0");
            retrievedTueseday.add("0");
            retrievedWednesday.add("0");
            retrievedThursday.add("0");
            retrievedFriday.add("0");
            retrievedSaturday.add("0");
        } else {
            connection = DbConnection.getInstance();
            if (day.matches("Sunday")){
                st = connection.createStatement();
                rs = st.executeQuery(SUNDAY_LIST_FILTERED_SCHEDULE_SQL);
            } else if (day.matches("Monday")){
                st = connection.createStatement();
                rs = st.executeQuery(MONDAY_LIST_FILTERED_SCHEDULE_SQL);
            } else if (day.matches("Tuesday")){
                st = connection.createStatement();
                rs = st.executeQuery(TUESDAY_LIST_FILTERED_SCHEDULE_SQL);
            } else if (day.matches("Wednesday")){
                st = connection.createStatement();
                rs = st.executeQuery(WEDNESDAY_LIST_FILTERED_SCHEDULE_SQL);
            } else if (day.matches("Thursday")){
                st = connection.createStatement();
                rs = st.executeQuery(THURSDAY_LIST_FILTERED_SCHEDULE_SQL);
            } else if (day.matches("Friday")){
                st = connection.createStatement();
                rs = st.executeQuery(FRIDAY_LIST_FILTERED_SCHEDULE_SQL);
            } else if (day.matches("Saturday")){
                st = connection.createStatement();
                rs = st.executeQuery(SATURDAY_LIST_FILTERED_SCHEDULE_SQL);
            }
            // Store return from sql into respective data fields.
            while (rs.next()) {
                retrievedBillboard.add(rs.getString(1));
                retrievedStartTime.add(rs.getString(2));
                retrievedDuration.add(rs.getString(3));
                retrievedEndTime.add("0");
                retrievedCreationDateTime.add(rs.getString(4).substring(0, rs.getString(4).length() - 5));
                retrievedRepeat.add(rs.getString(5));
                retrievedSunday.add(rs.getString(6));
                retrievedMonday.add(rs.getString(7));
                retrievedTueseday.add(rs.getString(8));
                retrievedWednesday.add(rs.getString(9));
                retrievedThursday.add(rs.getString(10));
                retrievedFriday.add(rs.getString(11));
                retrievedSaturday.add(rs.getString(12));
            }
            // Success Return Message
            serverResponse = "Pass: Schedule Detail List Returned";
        }

        // Create Schedulelist return object
        ScheduleList scheduleList = new ScheduleList(serverResponse,
                retrievedBillboard,
                retrievedStartTime,
                retrievedDuration,
                retrievedEndTime,
                retrievedCreationDateTime,
                retrievedRepeat,
                retrievedSunday,
                retrievedMonday,
                retrievedTueseday,
                retrievedWednesday,
                retrievedThursday,
                retrievedFriday,
                retrievedSaturday
        );
        // Return scheduleList Object
        return scheduleList;
    }


    /**
     * This function will calculate all possible Schedules for billboards for a specific day with repeat.
     * The scheduleList Parameter will feed in raw schedules into the function to compute additional schedules for the day.
     * <p>
     * This method always returns immediately. It will either return a ScheduleList object listing all possilbe schedules
     * from the given list.
     * @param scheduleList A scheduleList object to be imputed for all additional schedules
     * @return Returns a ScheduleList object with imputed results for schedules.
     * Contains information on all fields. Each field is an array and can be read via getters.
     */
    public static ScheduleList viewAllDaySchedule(ScheduleList scheduleList) throws IOException, SQLException {
        // Initialise allDaySchedule object
        ScheduleList allDaySchedule = null;
        // Initialise ArrayList to build return Variable
        ArrayList<String> retrievedBillboard = new ArrayList<>();
        ArrayList<String> retrievedStartTime = new ArrayList<>();
        ArrayList<String> retrievedDuration = new ArrayList<>();
        ArrayList<String> retrievedEndTime = new ArrayList<>();
        ArrayList<String> retrievedCreationDateTime = new ArrayList<>();
        ArrayList<String> retrievedRepeat = new ArrayList<>();
        ArrayList<String> retrievedSunday = new ArrayList<>();
        ArrayList<String> retrievedMonday = new ArrayList<>();
        ArrayList<String> retrievedTuesday = new ArrayList<>();
        ArrayList<String> retrievedWednesday = new ArrayList<>();
        ArrayList<String> retrievedThursday = new ArrayList<>();
        ArrayList<String> retrievedFriday = new ArrayList<>();
        ArrayList<String> retrievedSaturday = new ArrayList<>();
        // Set Constant Variables used for computation
        int numBillboards = scheduleList.getScheduleBillboardName().size();
        LocalTime endTime = LocalTime.parse("23:59");
        // Set Local Variables for temporary storage and computation
        LocalTime startTime ;
        int repeatMinutes ;
        int minTillEnd ;
        int extraSched;
        int duration ;
        String billboardName;
        String creationDateTime;
        String Sunday;
        String Monday;
        String Tuesday;
        String Wednesday;
        String Thursday;
        String Friday;
        String Saturday;
        // Run through all of the billboard schedule present
        for (int i = 0; i < numBillboards; i++) {
            billboardName = scheduleList.getScheduleBillboardName().get(i);
            startTime = LocalTime.parse(scheduleList.getStartTime().get(i)) ;
            repeatMinutes = Integer.parseInt(scheduleList.getRepeat().get(i));
            duration = Integer.parseInt(scheduleList.getDuration().get(i));
            creationDateTime = String.valueOf(scheduleList.getCreationDateTime().get(i));
            minTillEnd = Integer.parseInt(String.valueOf(startTime.until(endTime, ChronoUnit.MINUTES)));
            extraSched = minTillEnd / repeatMinutes;
            Sunday = scheduleList.getSunday().get(i);
            Monday = scheduleList.getMonday().get(i);
            Tuesday = scheduleList.getTuesday().get(i);
            Wednesday = scheduleList.getWednesday().get(i);
            Thursday = scheduleList.getThursday().get(i);
            Friday = scheduleList.getFriday().get(i);
            Saturday = scheduleList.getSaturday().get(i);
            // Genearte all possible imputation of schedules and store into temporary arraylist
            for (int j = 0; j <= extraSched; j++){
                retrievedBillboard.add(billboardName);
                retrievedStartTime.add(String.valueOf(startTime.plusMinutes(j*repeatMinutes)));
                retrievedEndTime.add(String.valueOf(startTime.plusMinutes(j*repeatMinutes).plusMinutes(duration)));
                retrievedDuration.add(String.valueOf(duration));
                retrievedCreationDateTime.add(creationDateTime);
                retrievedRepeat.add(String.valueOf(repeatMinutes));
                retrievedSunday.add(Sunday);
                retrievedMonday.add(Monday);
                retrievedTuesday.add(Tuesday);
                retrievedWednesday.add(Wednesday);
                retrievedThursday.add(Thursday);
                retrievedFriday.add(Friday);
                retrievedSaturday.add(Saturday);
            }
        }
        // Generate Response message
        String serverResponse = "Pass: All Day Schedule Returned";
        // Create ruturn allDayScheduleObject
        allDaySchedule = new ScheduleList(serverResponse,
                retrievedBillboard,
                retrievedStartTime,
                retrievedDuration,
                retrievedEndTime,
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
        // Return Schedule
        return allDaySchedule;
    }

    /**
     * This function will calculate if there is any current schedule active. The function takes in an ScheduleList object generated
     * from the allDaySchedule Function and also a LocalTime object that notes the currentTime.
     * <p>
     * This method always returns immediately. It will either return a ScheduleList object listing with all possible active schedules,
     * or it will return a empty object with a Fail String.
     * @param allDaySchedule A scheduleList object from allDaySchedule for calculating active schedules
     * @param currentTime A LocalTime object which notes the current time of request
     * @return Returns a CurrentSchedule object noting any active schedules. Each field is an array and can be read via getters.
     */
    public static CurrentSchedule viewCurrentSchedule(ScheduleList allDaySchedule, LocalTime currentTime) throws IOException, SQLException {
        // Initialise currentSchedule
        CurrentSchedule currentSchedule = null;
        // Initialise Variable
        ArrayList<String> retrievedBillboard = new ArrayList<>();
        ArrayList<String> retrievedStartTime = new ArrayList<>();
        ArrayList<String> retrievedCreationDateTime = new ArrayList<>();
        // Create looping Variable
        int numBillboards = allDaySchedule.getScheduleBillboardName().size();
        // Initialise local variables for calculations
        LocalTime startTime ;
        LocalTime endTime ;
        int repeatMinutes ;
        int minTillEnd ;
        int extraSched;
        int duration ;
        int counter = 0;
        String billboardName;
        String creationDateTime;
        String serverResponse;
        // Loop through all billboards and see if it is within range
        for (int i = 0; i < numBillboards; i++) {
            billboardName = allDaySchedule.getScheduleBillboardName().get(i);
            startTime = LocalTime.parse(allDaySchedule.getStartTime().get(i)) ;
            duration = Integer.parseInt(allDaySchedule.getDuration().get(i));
            endTime =  startTime.plusMinutes(duration);
            creationDateTime = String.valueOf(allDaySchedule.getCreationDateTime().get(i));
            if(currentTime.isAfter(startTime) && currentTime.isBefore(endTime)){
                retrievedBillboard.add(billboardName);
                retrievedStartTime.add(String.valueOf(startTime));
                retrievedCreationDateTime.add(creationDateTime);
                counter++;
            }
        }
        // Return message setting
        if (counter > 0){
            serverResponse = "Pass: Current Active Schedule Returned";
        } else {
            serverResponse = "Fail: No current Active Schedule";
            retrievedBillboard.add("0");
            retrievedStartTime.add("0");
            retrievedCreationDateTime.add("0");
        }
        // Create currentSchedule Object
        currentSchedule = new CurrentSchedule(serverResponse,
                retrievedBillboard,
                retrievedStartTime,
                retrievedCreationDateTime
        );
        // Return currentSchedule
        return currentSchedule;
    }


    /**
     * This function returns the index of the latest date from an array of LocalDateTime values.
     * @param dateTimes An ArrayList of LocalDateTime values.
     * @return index Returns an int which is the index of the latest date in the dateTimes ArrayList.
     */
    public static int latestDateTimeInArray(ArrayList<LocalDateTime> dateTimes) {

        // Set the first value of the array list to be the latest value
        LocalDateTime latestDateTime = dateTimes.get(0);
        int index = 0;

        // Loop over each element in the array list and check if the dateTime is after the latest dateTime
        for (int i = 1; i < dateTimes.size(); i++) {
            if (dateTimes.get(i).isAfter(latestDateTime)) {
                latestDateTime = dateTimes.get(i);
                index = i;
            }
        }

        return index;
    }


    /**
     * This function returns the xml for the billboard that should be currently scheduled. If there are multiple
     * billboards scheduled, it will choose the one which was created first based on it's creation date time. If there
     * are no billboards scheduled, it will return an empty string.
     * @return billboardXML Returns the xml of the current billboard to be displayed to the viewer.
     * @throws IOException
     * @throws SQLException
     */
    public static String getCurrentBillboardXML() throws IOException, SQLException {
        // Initialise variable to store xml file
        String billboardXML = "";

        // Get the current date and time and get the current day of the week
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalTime currentTime = localDateTime.toLocalTime();
        String currentDayOfWeek = localDateTime.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());

        // Get today's schedule
        ScheduleList todayScheduleList = listFilteredScheduleInformation(currentDayOfWeek);

        // Get the current schedule (based on the time) and the billboard name
        CurrentSchedule currentSchedule = viewCurrentSchedule(todayScheduleList, currentTime);
        ArrayList<String> currentScheduleBillboardNames = currentSchedule.getScheduleBillboardName();
        String currentBillboardName;

        if (!currentScheduleBillboardNames.isEmpty()) {
            if (currentScheduleBillboardNames.size() == 1) {
                // There is only one billboard
                currentBillboardName = String.valueOf(currentScheduleBillboardNames);
            }
            else{
                // There is more than one billboard scheduled for right now, so get their creation date time
                ArrayList<String> creationDateTimeStrings = currentSchedule.getCreationDateTime();
                ArrayList<LocalDateTime> creationLocalDateTimes = new ArrayList<>();

                // Parse strings into LocalDateTime with the correct formatting
                for (int i = 0; i < creationDateTimeStrings.size(); i++) {
                    LocalDateTime dateTime = LocalDateTime.parse(creationDateTimeStrings.get(i),
                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                    creationLocalDateTimes.set(i, dateTime);
                }

                // Find latest date from creation date time array and get the corresponding billboard name
                int latestDateIndex = latestDateTimeInArray(creationLocalDateTimes);
                currentBillboardName = currentScheduleBillboardNames.get(latestDateIndex);
            }
            // Get the chosen billboard's schedule and extract the xml code
            //TODO: EDIT SO SCHEDULE HAS SESSION TOKEN
            DbBillboard dbBillboardSchedule = BillboardAdmin.getBillboardInformation("",currentBillboardName);
            billboardXML = dbBillboardSchedule.getXMLCode();
        }



        return billboardXML;
    }


}
