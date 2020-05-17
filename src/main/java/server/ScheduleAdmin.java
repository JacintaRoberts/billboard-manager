package server;

import java.io.IOException;
import java.sql.*;

public class ScheduleAdmin {

    // Custom SQL Strings for Specific Queries
    public static final String COUNT_SCHEDULE_SQL = "SELECT COUNT(*) FROM Schedules";
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
            "      PRIMARY KEY (`BillboardName`)";

    // Custom Parameters for connection
    private static Connection connection;



    /**
     * Stores Database Queries: Schedules. This is a generic method which Make sures Schedules is made with default data.
     * <p>
     * This method always returns immediately.
     * @return
     */
    public String createScheduleTable() throws IOException, SQLException {
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
     * Delete Table: Schedules. This is a generic method which deletes the table.
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



}
