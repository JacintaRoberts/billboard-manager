package server;

import javax.sql.rowset.serial.SerialBlob;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

import static server.Server.*;
import static server.Server.Permission.*;
import static server.Server.ServerAcknowledge.*;
import static server.Server.ServerAcknowledge.InvalidToken;
import static server.Server.validateToken;

public class BillboardAdmin {

    // Custom SQL Strings for Specific Queries
    public static final String STORE_BILLBOARD_SQL = "INSERT INTO Billboards VALUES (?,?,?,?) ";
    public static final String EDIT_BILLBOARD_SQL = "UPDATE Billboards SET XMLCode = ?, Image = ? WHERE BillboardName = ?";
    public static final String DELETE_BILLBOARD_SQL = "DELETE FROM Billboards WHERE BillboardName = ?";
    public static final String DELETE_ALL_BILLBOARD_SQL = "DELETE FROM Billboards";
    public static final String COUNT_FILTER_BILLBOARD_SQL = "SELECT COUNT(*) FROM Billboards WHERE BillboardName = ?";
    public static final String COUNT_BILLBOARD_SQL = "SELECT COUNT(*) FROM Billboards";
    public static final String CREATE_BILLBOARD_TABLE = "CREATE TABLE IF NOT EXISTS `BillboardDatabase`.`Billboards` (\n" +
            "    `BillboardName` varchar(255) NOT NULL default '',\n" +
            "      `Creator` varchar(255) NOT NULL default '',\n" +
            "      `Image` MEDIUMBLOB default '',\n" +
            "      `XMLCode` TEXT default '',\n" +
            "      PRIMARY KEY (`BillboardName`)\n" +
            "  )";
    public static final String DROP_BILLBOARD_TABLE = "DROP TABLE IF EXISTS `BillboardDatabase`.`Billboards`";
    public static final String LIST_BILLBOARD_SQL = "SELECT BillboardName FROM Billboards";
    public static final String SHOW_BILLBOARD_SQL = "SELECT * FROM Billboards WHERE BillboardName = ?";

    // Custom Parameters for connection
    public static Connection connection;
    public static PreparedStatement createBillboard;
    public static PreparedStatement deleteBillboard;
    public static PreparedStatement editBillboard;
    public static PreparedStatement listaBillboard;
    public static PreparedStatement countFilterBillboard;

    public BillboardAdmin() throws UnsupportedEncodingException {
    }

    /**
     * Delete Table: Billboard. This is a generic method which drops the table. This is a method used for testing only.
     * <p>
     * This method always returns immediately.
     * @return Returns a String with the message of Billboard Table Dropped
     */
    public static String dropBillboardTable() throws IOException, SQLException{
        String resultMessage;

        connection = DbConnection.getInstance();
        Statement dropBillboard = connection.createStatement();
        ResultSet rs = dropBillboard.executeQuery(DROP_BILLBOARD_TABLE);
        resultMessage = "Billboard Table Dropped";
        return resultMessage;
    }


    /**
     * Create Table: Billboard. This is a method which will create Billboard Table in the database if it dosent exist.
     * The method will also return a message string if data already exists.
     * <p>
     * This method always returns immediately.
     * @throws IOException Throws an exception if an I/O exception of some sort has occurred.
     * @throws SQLException Throws an exception if there is a database access error or other errors.
     * @return A return string which denotes if a table has been created (Table Created), or the data status within the
     * table (Data Filled, Data Exists).
     */
    public static String createBillboardTable() throws IOException, SQLException{
        String resultMessage;

        connection = DbConnection.getInstance();
        Statement countBillboard = connection.createStatement();
        ResultSet rs = null;
        try {
            rs = countBillboard.executeQuery(COUNT_BILLBOARD_SQL);
            rs.next();
            String count = rs.getString(1);
            if (count.equals("0")){
                resultMessage = "Data Filled";
            } else {
                resultMessage = "Data Exists";
            }
        } catch (SQLSyntaxErrorException throwables) {
            Statement createTable = connection.createStatement();
            rs = createTable.executeQuery(CREATE_BILLBOARD_TABLE);
            resultMessage = "Table Created";
        }
        return resultMessage;
    }



    /**
     * Stores Database Queries: Billboard. This is a generic method which stores any query sent to the database. This method
     * will update and also create billboards if it dosent exist in the billboard already.
     * <p>
     * This method always returns immediately.
     * @param  sessionToken A String which is used to check if the user are allowed to be interacting with the app. Generated
     *                      when the user first logins
     * @param  billboard A String which provides Billboard Name to store into database
     * @param  creator A string which notes the user who created the billbaord
     * @param  xmlCode A String which provides xmlCode to store into database
     * @param  pictureData A Byte array which is base64 encoded represenation of the actual picture
     * @throws IOException Throws an exception if an I/O exception of some sort has occurred.
     * @throws SQLException Throws an exception if there is a database access error or other errors.
     * @return ServerAcknowledge A enum which is used to assist in the GUI actions.
     */
    public static ServerAcknowledge createBillboard(String sessionToken, String billboard, String creator, String xmlCode, byte[] pictureData)
                                                            throws IOException, SQLException {
        String validCharacters = "([A-Za-z0-9-_ ]+)";

        if (validateToken(sessionToken)) {
            System.out.println("Session is valid");
            // Check if Billboard already exists to see if its edit and new
            String count = countFilterBillboardSql(billboard);

            if (count.equals("0")){
                // New billboard Case
                if (UserAdmin.checkSinglePermission(sessionToken, CreateBillboard)) {
                    if (billboard.matches(validCharacters)) {
                            addBillboardSQL(billboard,creator,pictureData,xmlCode);
                            return Success;
                    } else {
                        System.out.println("Billboard Name Contains Invalid Characters");
                        return InvalidCharacters; // 2. Valid token but insufficient permission
                    }
                } else {
                    System.out.println("Permissions were not sufficient, no Billboard was Created");
                    return InsufficientPermission; // 3. Valid token but insufficient permission
                }
            } else {
                // Existing billboard case
                String OGCreator = getBillboardInformation(sessionToken, billboard).getCreator();
                String checkSchedule = ScheduleAdmin.countFilterScheduleSql(billboard);
                if (creator.equals(OGCreator) && !checkSchedule.equals("1")){
                    if (UserAdmin.checkSinglePermission(sessionToken, CreateBillboard)) {
                        if (billboard.matches(validCharacters)) {
                                editBillboardSQL(billboard,creator,pictureData,xmlCode);
                                return Success;
                        } else {
                            System.out.println("Billboard Name Contains Invalid Characters");
                            return InvalidCharacters; // 2. Valid token but insufficient permission
                        }
                    } else {
                        System.out.println("Permissions were not sufficient, no Billboard was Edited");
                        return InsufficientPermission; // 3. Valid token but insufficient permission
                    }
                } else {
                    if (UserAdmin.checkSinglePermission(sessionToken, EditBillboard)) {
                        if (billboard.matches(validCharacters)) {
                            try {
                                editBillboardSQL(billboard,creator,pictureData,xmlCode);
                                return Success;
                            } catch (SQLIntegrityConstraintViolationException e) {
                                return PrimaryKeyClash;
                            }
                        } else {
                            System.out.println("Billboard Name Contains Invalid Characters");
                            return InvalidCharacters; // 2. Valid token but insufficient permission
                        }
                    } else {
                        System.out.println("Permissions were not sufficient, no Billboard was Edited");
                        return InsufficientPermission; // 3. Valid token but insufficient permission
                    }
                }
            }
        } else {
            System.out.println("Session was not valid");
            return InvalidToken; // 4. Invalid token
        }
    }



    /**
     * Stores Database Queries: Billboard. This is a generic method which deletes a billboard stored in database.
     * <p>
     * This method always returns immediately.
     * @param  sessionToken A String which is used to check if the user are allowed to be interacting with the app. Generated
     *                      when the user first logins
     * @param  billboard A String which provides Billboard Name to delete from database
     * @param  requestor A string which provides the username whom wants to delete data from the database
     * @throws IOException Throws an exception if an I/O exception of some sort has occurred.
     * @throws SQLException Throws an exception if there is a database access error or other errors.
     * @return ServerAcknowledge A enum which is used to assist in the GUI actions.
     */
    public static ServerAcknowledge deleteBillboard(String sessionToken, String billboard, String requestor) throws IOException, SQLException {
        if (validateToken(sessionToken)) {
            System.out.println("Session is valid");
            // Check if Billboard exists
            String count = countFilterBillboardSql(billboard);
            if (count.equals("0")){
                System.out.println("Billboard does not exist");
                return BillboardNotExists;
            } else {
                // Existing billboard case
                String OGCreator = getBillboardInformation(sessionToken, billboard).getCreator();
                String checkSchedule = ScheduleAdmin.countFilterScheduleSql(billboard);
                if (requestor.equals(OGCreator) && !checkSchedule.equals("1")){
                    if (UserAdmin.checkSinglePermission(sessionToken, CreateBillboard)) {
                        deleteBillboardSQL(billboard);
                        ScheduleAdmin.deleteScheduleSql(billboard);
                        return Success;
                    } else {
                        System.out.println("Permissions were not sufficient, no Billboard was Deleted");
                        return InsufficientPermission; // 3. Valid token but insufficient permission
                    }
                } else {
                    if (UserAdmin.checkSinglePermission(sessionToken, EditBillboard)) {
                        deleteBillboardSQL(billboard);
                        ScheduleAdmin.deleteScheduleSql(billboard);
                        return Success;
                    } else {
                        System.out.println("Permissions were not sufficient, no Billboard was Deleted");
                        return InsufficientPermission; // 3. Valid token but insufficient permission
                    }
                }
            }
        } else {
            System.out.println("Session was not valid");
            return InvalidToken; // 4. Invalid token
        }
    }



    /**
     * Stores Database Queries: Billboard. This is a generic method which edits billboard xmlCode in the database.
     * <p>
     * This method always returns immediately.
     * @param  sessionToken A String which is used to check if the user are allowed to be interacting with the app. Generated
     *                      when the user first logins
     * @param  billboard A String which provides Billboard Name to store into database
     * @throws IOException Throws an exception if an I/O exception of some sort has occurred.
     * @throws SQLException Throws an exception if there is a database access error or other errors.
     * @return dbBillboard A DbBillboard Object which contains information on the Name, Creator, XML, Picture data and
     * server status through ServerAcknowledgement
     */
    public static DbBillboard getBillboardInformation(String sessionToken, String billboard) throws IOException, SQLException {
        if (validateToken(sessionToken)) {
            System.out.println("Session is valid");
            String count = countFilterBillboardSql(billboard);
            if (count.equals("1")){
                DbBillboard dbBillboard = getBillboardSQL(billboard);
                return dbBillboard;
            }else {
                System.out.println("Fail: Billboard Does not Exist");
                DbBillboard dbBillboard = new DbBillboard("0","0",null,"",BillboardNotExists);
                return dbBillboard;
            }
        } else {
            System.out.println("Fail: Session was not valid");
            DbBillboard dbBillboard = new DbBillboard("0","0",null,"",InvalidToken);
            return dbBillboard;
        }
    }


    /**
     * List all billboard names that are stored in the database as an Array String
     * <p>
     * This method always returns immediately.
     * @param  sessionToken A String which is used to check if the user are allowed to be interacting with the app. Generated
     *                      when the user first logins
     * @throws IOException Throws an exception if an I/O exception of some sort has occurred.
     * @throws SQLException Throws an exception if there is a database access error or other errors.
     * @return Returns an BillboardList object which contains an ArrayString Field that contains all the Billboard that
     * is stored in the database.
     */

    public static BillboardList listBillboard(String sessionToken) throws IOException, SQLException {
        ArrayList<String> retrievedBillboard = new ArrayList<>();
        String serverResponse = null;

        if (validateToken(sessionToken)) {
            System.out.println("Session is valid");
            String count = countBillboardSql();
            if (count.equals("0")) {
                serverResponse = "Fail: No Billboard Exists";
                retrievedBillboard.add(null);
            } else {
                connection = DbConnection.getInstance();
                Statement listBillboard = connection.createStatement();
                ResultSet rs = listBillboard.executeQuery(LIST_BILLBOARD_SQL);
                while (rs.next()) {
                    String value = rs.getString(1);
                    retrievedBillboard.add(value);
                }
                serverResponse = "Pass: Billboard List Returned";
            }
        } else {
            System.out.println("Session was not valid");
            serverResponse = "Session was not valid";
        }
        BillboardList billboardList = new BillboardList(serverResponse,retrievedBillboard);
        return billboardList;
    }




    /**================================================================================================
     * SQL Methods
     ================================================================================================*/


    /**
     * Method to check if a billboard from the database
     * @param billboardName String of billboard to check if exist in database
     * @throws IOException Throws an exception if an I/O exception of some sort has occurred.
     * @throws SQLException Throws an exception if there is a database access error or other errors.
     * @return Returns a String if the billboard exists (1) or not (0)
     */
    public static String countFilterBillboardSql(String billboardName) throws IOException, SQLException {
        connection = DbConnection.getInstance();
        countFilterBillboard = connection.prepareStatement(COUNT_FILTER_BILLBOARD_SQL);
        countFilterBillboard.setString(1,billboardName);
        ResultSet rs = countFilterBillboard.executeQuery();
        String count = "";
        while(rs.next()){
            count = rs.getString(1);
        }
        rs.close();
        return count;
    }

    /**
     * Method to check if a billboard from the database
     * @throws IOException Throws an exception if an I/O exception of some sort has occurred.
     * @throws SQLException Throws an exception if there is a database access error or other errors.
     * @return Returns a String if the billboard exists (1) or not (0)
     */
    public static String countBillboardSql() throws IOException, SQLException {
        connection = DbConnection.getInstance();
        Statement countBillboard = connection.createStatement();
        ResultSet rs = countBillboard.executeQuery(COUNT_BILLBOARD_SQL);
        String count = "";
        while(rs.next()) {
            count = rs.getString(1);
        }
        rs.close();
        return count;
    }

    /**
     * Method to put a billboard from the database
     * @return The billboard will be created into the Database
     * @param  billboardName A String which provides Billboard Name to store into database
     * @param  creator A string which notes the user who created the billbaord
     * @param  XMLCode A String which provides xmlCode to store into database
     * @param  pictureData A Byte array which is base64 encoded represenation of the actual picture
     * @throws IOException Throws an exception if an I/O exception of some sort has occurred.
     * @throws SQLException Throws an exception if there is a database access error or other errors.
     */
    public static void addBillboardSQL(String billboardName, String creator,
                                       byte[] pictureData, String XMLCode) throws IOException, SQLException {
        connection = DbConnection.getInstance();
        createBillboard = connection.prepareStatement(STORE_BILLBOARD_SQL);
        createBillboard.setString(1,billboardName);
        createBillboard.setString(2,creator);
        Blob pictureBlob = null;
        if (!(pictureData == null)) {
            pictureBlob = new javax.sql.rowset.serial.SerialBlob(pictureData); // Construct blob to store picture from byte array
        }
        createBillboard.setBlob(3, pictureBlob);
        createBillboard.setString(4, XMLCode);
        ResultSet rs = createBillboard.executeQuery();
        rs.close();
    }

    /**
     * Method to put a billboard from the database
     * @param  billboardName A String which provides Billboard Name to store into database
     * @param  creator A string which notes the user who created the billbaord
     * @param  XMLCode A String which provides xmlCode to store into database
     * @param  pictureData A Byte array which is base64 encoded represenation of the actual picture
     * @return The billboard will be edited from the database
     * @throws IOException Throws an exception if an I/O exception of some sort has occurred.
     * @throws SQLException Throws an exception if there is a database access error or other errors.
     */
    public static void editBillboardSQL(String billboardName, String creator,
                                       byte[] pictureData, String XMLCode) throws IOException, SQLException {
        connection = DbConnection.getInstance();
        editBillboard = connection.prepareStatement(EDIT_BILLBOARD_SQL);
        editBillboard.setString(1, XMLCode);
        Blob pictureBlob = null;
        if (!(pictureData == null)) {
            pictureBlob = new javax.sql.rowset.serial.SerialBlob(pictureData); // Construct blob to store picture from byte array
        }
        editBillboard.setBlob(2, pictureBlob);
        editBillboard.setString(3,billboardName);
        ResultSet rs = editBillboard.executeQuery();
        rs.close();
    }


    /**
     * Method to get a billboard from the database
     * @param billboardName The billboardName via a string will be supplied to query from SQL
     * @return DbBillboard Object will be returned which consist of billboard information
     * @throws IOException Throws an exception if an I/O exception of some sort has occurred.
     * @throws SQLException Throws an exception if there is a database access error or other errors.
     */
    public static DbBillboard getBillboardSQL(String billboardName) throws IOException, SQLException {
        connection = DbConnection.getInstance();
        listaBillboard = connection.prepareStatement(SHOW_BILLBOARD_SQL);
        listaBillboard.setString(1,billboardName);
        ResultSet rs = listaBillboard.executeQuery();
        DbBillboard dbBillboard = null;
        String billboard = null;
        String Creator = null;
        byte[] Image = null;
        String XMLcode = null;
        while(rs.next()){
            billboard = rs.getString("BillboardName");
            Creator = rs.getString("Creator");
            Image = rs.getBytes("Image");
            XMLcode = rs.getString("XMLCode");
        }
        dbBillboard = new DbBillboard(billboard,Creator , Image, XMLcode,Success);
        return dbBillboard;
    }


    /**
     * Method to delete a billboard from the database
     * @param billboardName The name of the billboard to be deleted from the database
     * @return The billboard will be deleted from the database
     * @throws IOException Throws an exception if an I/O exception of some sort has occurred.
     * @throws SQLException Throws an exception if there is a database access error or other errors.
     */
    public static void deleteBillboardSQL(String billboardName) throws IOException, SQLException {
        connection = DbConnection.getInstance();
        deleteBillboard = connection.prepareStatement(DELETE_BILLBOARD_SQL);
        deleteBillboard.setString(1,billboardName);
        ResultSet rs = deleteBillboard.executeQuery();
        rs.close();
    }


    /**
     * Stores Database Queries: Billboard. This is a generic method which deletes all billboards stored into database.
     * <p>
     * @throws IOException Throws an exception if an I/O exception of some sort has occurred.
     * @throws SQLException Throws an exception if there is a database access error or other errors.
     * This method always returns immediately.
     * @return ServerAcknowledgement which allows the GUI to acknowledge
     */
    public static ServerAcknowledge deleteAllBillboard() throws IOException, SQLException {
        connection = DbConnection.getInstance();
        Statement deleteAllBillboard = connection.createStatement();
        deleteAllBillboard.executeQuery(DELETE_ALL_BILLBOARD_SQL);
        connection.close();
        return Success;
    }





}
