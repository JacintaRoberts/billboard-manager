package server;

import java.io.IOException;
import java.sql.*;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class ScheduleAdmin {

    // Custom SQL Strings for Specific Queries
    public static final String COUNT_SCHEDULE_SQL = "SELECT COUNT(*) FROM Schedules";
    public static final String COUNT_FILTER_SCHEDULE_SQL = "SELECT COUNT(*) FROM Schedules WHERE BillboardName = ?";
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
    private static Connection connection;
    private static PreparedStatement countFilterSchedule;
    private static PreparedStatement listFilterSchedule;
    private static PreparedStatement countListFilterSchedule;
    private static PreparedStatement deleteSchedule;
    private static PreparedStatement editSchedule;
    private static PreparedStatement createSchedule;




    /**
     * Stores Database Queries: Schedules. This is a generic method which Make sures Schedules is made with default data.
     * <p>
     * This method always returns immediately.
     * @return
     */
    public static String createScheduleTable() throws IOException, SQLException {
        String resultMessage;

        connection = DbConnection.getInstance();
        Statement countSchedule = connection.createStatement();
        ResultSet rs = null;
        try {
            rs = countSchedule.executeQuery(COUNT_SCHEDULE_SQL);
            rs.next();
            String count = rs.getString(1);
            if (count.equals("0")){
//                createGenericBillboard();
                resultMessage = "Data Filled";
            } else {
                resultMessage = "Data Exists";
            }
        } catch (SQLSyntaxErrorException throwables) {
            rs = countSchedule.executeQuery(CREATE_SCHEDULE_TABLE);
//            createGenericBillboard();
            resultMessage = "Table Created";
        }
        return resultMessage;
    }


    /**
     * Drop Table: Schedules. This is a generic method which deletes the table.
     * <p>
     * This method always returns immediately.
     * @return
     */
    public String dropScheduleTable() throws IOException, SQLException{
        String resultMessage;
        connection = DbConnection.getInstance();
        Statement dropSchedule = connection.createStatement();
        ResultSet rs = dropSchedule.executeQuery(DROP_SCHEDULE_TABLE);
        resultMessage = "Schedule Table Dropped";
        return resultMessage;
    }


    /**
     * Stores Database Queries: Schedule. This is a generic method which stores schedule query sent to the database.
     * <p>
     * This method always returns immediately.
     * @param  billboard A String which provides Billboard Name to store into database
     * @param  StartTime A String which provides xmlCode to store into database
     * @param  Duration A String which provides Billboard Name to store into database
     * @param  CreationDateTime A String which provides xmlCode to store into database
     * @param  Repeat A Integer representing how often the schedule is repeated (in minutes)
     * @param  Sunday A Boolean to see if the schedule is to be run during Sunday
     * @param  Monday A Boolean to see if the schedule is to be run during Monday
     * @param  Tuesday A Boolean to see if the schedule is to be run during Tuesday
     * @param  Wednesday A Boolean to see if the schedule is to be run during Wednesday
     * @param  Thursday A Boolean to see if the schedule is to be run during Thursday
     * @param  Friday A Boolean to see if the schedule is to be run during Friday
     * @param  Saturday A Boolean to see if the schedule is to be run during Saturday

     * @return
     */
    public static String createSchedule(String billboard,
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
        String resultMessage;
        String validCharacters = "([A-Za-z0-9-_]+)";
        if (billboard.matches(validCharacters)) {
            connection = DbConnection.getInstance();
            countFilterSchedule = connection.prepareStatement(COUNT_FILTER_SCHEDULE_SQL);
            countFilterSchedule.setString(1,billboard);
            ResultSet rs = countFilterSchedule.executeQuery();
            rs.next();
            String count = rs.getString(1);
            if (count.equals("1")){
                resultMessage = "Fail: Schedule Already Exists";
            }else {
                connection = DbConnection.getInstance();
                BillboardAdmin.countFilterBillboard = connection.prepareStatement(BillboardAdmin.COUNT_FILTER_BILLBOARD_SQL);
                BillboardAdmin.countFilterBillboard.setString(1,billboard);
                rs = BillboardAdmin.countFilterBillboard.executeQuery();
                rs.next();
                String count2 = rs.getString(1);
                if (count2.equals("0")){
                    resultMessage = "Fail: Billboard does not Exist";
                } else{
                    connection = DbConnection.getInstance();
                    createSchedule = connection.prepareStatement(STORE_SCHEDULE_SQL);
                    createSchedule.setString(1,billboard);
                    createSchedule.setString(2,StartTime);
                    createSchedule.setString(3,Duration);
                    createSchedule.setString(4,CreationDateTime);
                    createSchedule.setString(5,Repeat);
                    createSchedule.setString(6,Sunday);
                    createSchedule.setString(7,Monday);
                    createSchedule.setString(8,Tuesday);
                    createSchedule.setString(9,Wednesday);
                    createSchedule.setString(10,Thursday);
                    createSchedule.setString(11,Friday);
                    createSchedule.setString(12,Saturday);
                    rs = createSchedule.executeQuery();
                    resultMessage = "Pass: Billboard Scheduled";
                }
            }

        } else {
            resultMessage = "Fail: Billboard Name Contains Illegal Characters";
        }
        return resultMessage;
    }



    /**
     * Stores Database Queries: Schedules. This is a generic method which deletes all Schedules stored into database.
     * <p>
     * This method always returns immediately.
     * @return
     */
    public String deleteAllSchedules() throws IOException, SQLException {
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
     * Stores Database Queries: Schedules. This is a generic method which deletes A specific Schedules stored into database.
     * <p>
     * This method always returns immediately.
     * @param  billboard A String which provides Billboard Name to store into database
     * @return
     */
    public String deleteSchedule(String billboard) throws IOException, SQLException {
        String resultMessage;
        String validCharacters = "([A-Za-z0-9-_]+)";
        if (billboard.matches(validCharacters)) {
            connection = DbConnection.getInstance();
            BillboardAdmin.countFilterBillboard = connection.prepareStatement(BillboardAdmin.COUNT_FILTER_BILLBOARD_SQL);
            BillboardAdmin.countFilterBillboard.setString(1,billboard);
            ResultSet rs = BillboardAdmin.countFilterBillboard.executeQuery();
            rs.next();
            String count2 = rs.getString(1);

            if (count2.equals("0")){
                resultMessage = "Fail: Billboard Does Not Exist";
            } else{
                connection = DbConnection.getInstance();
                countFilterSchedule = connection.prepareStatement(COUNT_FILTER_SCHEDULE_SQL);
                countFilterSchedule.setString(1,billboard);
                rs = countFilterSchedule.executeQuery();
                rs.next();
                String count = rs.getString(1);
                if (count.equals("1")){
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
        return resultMessage;
    }



    /**
     * Stores Database Queries: Schedule. This is a generic method which returns a list of billboard Schedules
     * stored into database.
     * <p>
     * This method always returns immediately.
     * @return
     */
    public static ScheduleList listScheduleInformation() throws IOException, SQLException {

        // Initialise Variable
        ArrayList<String> retrievedBillboard = new ArrayList<>();
        ArrayList<String> retrievedStartTime = new ArrayList<>();
        ArrayList<String> retrievedDuration = new ArrayList<>();
        ArrayList<String> retrievedCreationDateTime = new ArrayList<>();
        ArrayList<String> retrievedRepeat = new ArrayList<>();
        ArrayList<String> retrievedSunday = new ArrayList<>();
        ArrayList<String> retrievedMonday = new ArrayList<>();
        ArrayList<String> retrievedTueseday = new ArrayList<>();
        ArrayList<String> retrievedWednesday = new ArrayList<>();
        ArrayList<String> retrievedThursday = new ArrayList<>();
        ArrayList<String> retrievedFriday = new ArrayList<>();
        ArrayList<String> retrievedSaturday = new ArrayList<>();


        connection = DbConnection.getInstance();
        Statement countSchedule = connection.createStatement();
        ResultSet rs = countSchedule.executeQuery(COUNT_SCHEDULE_SQL);
        rs.next();
        String count = rs.getString(1);
        String serverResponse = null;
        if (count.equals("0")) {
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
            Statement listSchedule = connection.createStatement();
            rs = listSchedule.executeQuery(LIST_SCHEDULE_SQL);
            int columnCount = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                retrievedBillboard.add(rs.getString(1));
                retrievedStartTime.add(rs.getString(2));
                retrievedDuration.add(rs.getString(3));
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
            serverResponse = "Pass: Schedule Detail List Returned";

        }
        ScheduleList scheduleList = new ScheduleList(serverResponse,
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

        return scheduleList;
    }


    /**
     * Stores Database Queries: Schedule. This is a generic method which edits Schedule in the database.
     * <p>
     * This method always returns immediately.
     * @param  billboard A String which provides Billboard Name to store into database
     * @param  StartTime A String which provides xmlCode to store into database
     * @param  Duration A String which provides Billboard Name to store into database
     * @param  CreationDateTime A String which provides xmlCode to store into database
     * @param  Repeat A Integer representing how often the schedule is repeated (in minutes)
     * @param  Sunday A Boolean to see if the schedule is to be run during Sunday
     * @param  Monday A Boolean to see if the schedule is to be run during Monday
     * @param  Tuesday A Boolean to see if the schedule is to be run during Tuesday
     * @param  Wednesday A Boolean to see if the schedule is to be run during Wednesday
     * @param  Thursday A Boolean to see if the schedule is to be run during Thursday
     * @param  Friday A Boolean to see if the schedule is to be run during Friday
     * @param  Saturday A Boolean to see if the schedule is to be run during Saturday
     * @return
     */
    public static String editSchedule(String billboard,
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
        String resultMessage;
        String validCharacters = "([A-Za-z0-9-_]+)";
        if (billboard.matches(validCharacters)) {
            connection = DbConnection.getInstance();
            BillboardAdmin.countFilterBillboard = connection.prepareStatement(BillboardAdmin.COUNT_FILTER_BILLBOARD_SQL);
            BillboardAdmin.countFilterBillboard.setString(1,billboard);
            ResultSet rs = BillboardAdmin.countFilterBillboard.executeQuery();
            rs.next();
            String count2 = rs.getString(1);
            if (count2.equals(0)){
                resultMessage = "Fail: Billboard Does Not Exist";
            } else{
                connection = DbConnection.getInstance();
                countFilterSchedule = connection.prepareStatement(COUNT_FILTER_SCHEDULE_SQL);
                countFilterSchedule.setString(1,billboard);
                rs = countFilterSchedule.executeQuery();
                rs.next();
                String count = rs.getString(1);
                if (count.equals("1")){
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
                    resultMessage = "Pass: Schedule Edited";
                }else {
                    resultMessage = "Fail: Schedule Does not Exist";
                }
            }
        } else {
            resultMessage = "Fail: Scheduled Billboard Name Contains Illegal Characters";
        }

        return resultMessage;
    }

    /**
     * Stores Database Queries: Schedule. This is a generic method which returns a list of billboard Schedules for a day
     * stored into database.
     * <p>
     * This method always returns immediately.
     * @param day  String to filter by day
     * @return
     */
    public static ScheduleList listFilteredScheduleInformation(String day) throws IOException, SQLException {

        // Initialise Variable
        ArrayList<String> retrievedBillboard = new ArrayList<>();
        ArrayList<String> retrievedStartTime = new ArrayList<>();
        ArrayList<String> retrievedDuration = new ArrayList<>();
        ArrayList<String> retrievedCreationDateTime = new ArrayList<>();
        ArrayList<String> retrievedRepeat = new ArrayList<>();
        ArrayList<String> retrievedSunday = new ArrayList<>();
        ArrayList<String> retrievedMonday = new ArrayList<>();
        ArrayList<String> retrievedTueseday = new ArrayList<>();
        ArrayList<String> retrievedWednesday = new ArrayList<>();
        ArrayList<String> retrievedThursday = new ArrayList<>();
        ArrayList<String> retrievedFriday = new ArrayList<>();
        ArrayList<String> retrievedSaturday = new ArrayList<>();

        String serverResponse = null;
        Integer dayCheckPass = 0;

        connection = DbConnection.getInstance();
        Statement st = null;
        ResultSet rs = null;
        String count = "0";
        if (day.matches("Sunday")){
            st = connection.createStatement();
            rs = st.executeQuery(MONDAY_COUNT_LIST_FILTERED_SCHEDULE_SQL);
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
            rs = st.executeQuery(MONDAY_COUNT_LIST_FILTERED_SCHEDULE_SQL);
            dayCheckPass = 1;
            rs.next();
            count = rs.getString(1);
        } else if (day.matches("Wednesday")){
            st = connection.createStatement();
            rs = st.executeQuery(MONDAY_COUNT_LIST_FILTERED_SCHEDULE_SQL);
            dayCheckPass = 1;
            rs.next();
            count = rs.getString(1);
        } else if (day.matches("Thursday")){
            st = connection.createStatement();
            rs = st.executeQuery(MONDAY_COUNT_LIST_FILTERED_SCHEDULE_SQL);
            dayCheckPass = 1;
            rs.next();
            count = rs.getString(1);
        } else if (day.matches("Friday")){
            st = connection.createStatement();
            rs = st.executeQuery(MONDAY_COUNT_LIST_FILTERED_SCHEDULE_SQL);
            dayCheckPass = 1;
            rs.next();
            count = rs.getString(1);
        } else if (day.matches("Saturday")){
            st = connection.createStatement();
            rs = st.executeQuery(MONDAY_COUNT_LIST_FILTERED_SCHEDULE_SQL);
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

        if (dayCheckPass.equals(0)){

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

            while (rs.next()) {
                retrievedBillboard.add(rs.getString(1));
                retrievedStartTime.add(rs.getString(2));
                retrievedDuration.add(rs.getString(3));
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
            serverResponse = "Pass: Schedule Detail List Returned";

        }
    ScheduleList scheduleList = new ScheduleList(serverResponse,
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

    return scheduleList;
    }

    /**
     * Stores Database Queries: Schedule. This is a generic method which returns a list of billboard Schedules for a day
     * stored into database.
     * <p>
     * This method always returns immediately.
     * @param scheduleList  Variable scheduleList which is all billboards in a day
     * @return
     */
    public static ScheduleList viewAllDaySchedule(ScheduleList scheduleList) throws IOException, SQLException {
        ScheduleList allDaySchedule = null;
        // Initialise Variable
        ArrayList<String> retrievedBillboard = new ArrayList<>();
        ArrayList<String> retrievedStartTime = new ArrayList<>();
        ArrayList<String> retrievedDuration = new ArrayList<>();
        ArrayList<String> retrievedCreationDateTime = new ArrayList<>();
        ArrayList<String> retrievedRepeat = new ArrayList<>();
        ArrayList<String> retrievedSunday = new ArrayList<>();
        ArrayList<String> retrievedMonday = new ArrayList<>();
        ArrayList<String> retrievedTuesday = new ArrayList<>();
        ArrayList<String> retrievedWednesday = new ArrayList<>();
        ArrayList<String> retrievedThursday = new ArrayList<>();
        ArrayList<String> retrievedFriday = new ArrayList<>();
        ArrayList<String> retrievedSaturday = new ArrayList<>();

        int numBillboards = scheduleList.getScheduleBillboardName().size();
        LocalTime endTime = LocalTime.parse("23:59");


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


        // Repeats
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
            for (int j = 0; j <= extraSched; j++){
                retrievedBillboard.add(billboardName);
                retrievedStartTime.add(String.valueOf(startTime.plusMinutes(j*repeatMinutes)));
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
        String serverResponse = "Pass: All Day Schedule Returned";

        allDaySchedule = new ScheduleList(serverResponse,
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

        return allDaySchedule;
    }

    /**
     * Stores Database Queries: Schedule. This is a generic method which returns a list of billboard that is currently
     * scheduled.
     * <p>
     * This method always returns immediately.
     * @param allDaySchedule  Variable scheduleList which is all billboards in a day
     * @return
     */
    public static CurrentSchedule viewCurrentSchedule(ScheduleList allDaySchedule, LocalTime currentTime) throws IOException, SQLException {
        CurrentSchedule currentSchedule = null;
        // Initialise Variable
        ArrayList<String> retrievedBillboard = new ArrayList<>();
        ArrayList<String> retrievedStartTime = new ArrayList<>();
        ArrayList<String> retrievedCreationDateTime = new ArrayList<>();


        int numBillboards = allDaySchedule.getScheduleBillboardName().size();

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

        // Repeats
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
        if (counter > 0){
            serverResponse = "Pass: Current Active Schedule Returned";
        } else {
            serverResponse = "Pass: No current Active Schedule";
            retrievedBillboard.add("0");
            retrievedStartTime.add("0");
            retrievedStartTime.add("0");
        }

        System.out.println(serverResponse);
        System.out.println(retrievedBillboard);
        System.out.println(retrievedStartTime);
        System.out.println(retrievedCreationDateTime);

        currentSchedule = new CurrentSchedule(serverResponse,
                retrievedBillboard,
                retrievedStartTime,
                retrievedCreationDateTime
        );

        return currentSchedule;
    }


}
