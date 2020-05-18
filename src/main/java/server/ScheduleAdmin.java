package server;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

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
            "SET StartTime = ?" +
            "SET Duration = ?" +
            "SET CreationDateTime = ?" +
            "SET Repeat = ?" +
            "SET Sunday = ?" +
            "SET Monday = ?" +
            "SET Tuesday = ?" +
            "SET Thursday = ?" +
            "SET Friday = ?" +
            "SET Saturday = ?" +
            "WHERE BillboardName = ?";
    public static final String STORE_SCHEDULE_SQL = "INSERT INTO Schedules VALUES (?,?,?,?,?,?,?,?,?,?,?,?) ";
    public static final String LIST_SCHEDULE_SQL = "SELECT * FROM Schedules";


    // Custom Parameters for connection
    private static Connection connection;
    private static PreparedStatement countFilterSchedule;
    private static PreparedStatement deleteSchedule;
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





}
