package server;

import javax.sql.rowset.serial.SerialBlob;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

import static server.Server.Permission.*;
import static server.Server.ServerAcknowledge.*;
import static server.Server.ServerAcknowledge.InvalidToken;
import static server.Server.validateToken;

public class BillboardAdmin {

    // Custom SQL Strings for Specific Queries
    public static final String STORE_BILLBOARD_SQL = "INSERT INTO Billboards VALUES (?,?,?,?) ";
    public static final String EDIT_BILLBOARD_SQL = "UPDATE Billboards " +
                                                    "SET XMLCode = ?" +
                                                    "SET Image = ?" +
                                                    "WHERE BillboardName = ?";
    public static final String DELETE_BILLBOARD_SQL = "DELETE FROM Billboards WHERE BillboardName = ?";
    public static final String DELETE_ALL_BILLBOARD_SQL = "DELETE FROM Billboards";
    public static final String COUNT_FILTER_BILLBOARD_SQL = "SELECT COUNT(*) FROM Billboards WHERE BillboardName = ?";
    public static final String COUNT_BILLBOARD_SQL = "SELECT COUNT(*) FROM Billboards";
    public static final String CREATE_BILLBOARD_TABLE = "CREATE TABLE IF NOT EXISTS `BillboardDatabase`.`Billboards` (\n" +
            "    `BillboardName` varchar(255) NOT NULL default '',\n" +
            "      `Creator` varchar(255) NOT NULL default '',\n" +
            "      `Image` MEDIUMBLOB,\n" +
            "      `XMLCode` TEXT,\n" +
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
     * Delete Table: Billboard. This is a generic method which deletes the table.
     * <p>
     * This method always returns immediately.
     * @return
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
     * Stores Database Queries: Billboard. This is a generic method which Make sures billboard is made with default data.
     * <p>
     * This method always returns immediately.
     * @return
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
            rs = countBillboard.executeQuery(CREATE_BILLBOARD_TABLE);
            resultMessage = "Table Created";
        }
        return resultMessage;
    }




    //TODO: JACINTA HELPING
    //CREATE SERIALIZABLE OWNXML CLASS (XML, BLOB PIC)
    //GETBILLBOARDREQUEST
    //DBBILLBOARD (ALSO HAS PICTURE FIELD)
    //SEND SIMILAR DBBILLBOARD
    //ADJUST INPUT PARAMETER TYPE TO OBJECT FOR SERVER
    //SPLIT ON PICTURE DATA (END ON />)
    //byte[] byteData = pictureData.getBytes("UTF-8");
    // Send byteData to server, then to store in db:
    //Blob blobData = dbConnection.createBlob();
    //blobData.setBytes(1, byteData);
    /**
     * Stores Database Queries: Billboard. This is a generic method which stores any query sent to the database.
     * <p>
     * This method always returns immediately.
     * @param  sessionToken A String which provides username to store into database
     * @param  billboard A String which provides Billboard Name to store into database
     * @param  xmlCode A String which provides xmlCode to store into database
     * @return
     */
    public static Server.ServerAcknowledge createBillboard(String sessionToken, String billboard, String creator, String xmlCode, byte[] pictureData)
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
                        try {
                            addBillboardSQL(billboard,creator,pictureData,xmlCode);
                            return Success;
                        } catch (SQLIntegrityConstraintViolationException e) {
                            return PrimaryKeyClash;
                        }
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
                if (creator.equals(OGCreator)){
                    //todo: to edit in if scheduled (need edit)
                    // Creator only needs Create permission if not scheduled
                    if (UserAdmin.checkSinglePermission(sessionToken, CreateBillboard)) {
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
                } else {
                    if (UserAdmin.checkSinglePermission(sessionToken, EditBillboard)) {
                        if (billboard.matches(validCharacters)) {
                            try {
                                addBillboardSQL(billboard,creator,pictureData,xmlCode);
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


//    /**
//     * Stores Database Queries: Billboard. This is a generic method which edits billboard xmlCode in the database.
//     * <p>
//     * This method always returns immediately.
//     * @param  billboard A String which provides Billboard Name to store into database
//     * @param  xmlCode A String which provides xmlCode to store into database
//     * @return
//     */
//    public static String editBillboard(String billboard,
//                                       InputStream xmlCode) throws IOException, SQLException {
//        String resultMessage;
//        String validCharacters = "([A-Za-z0-9-_ ]+)";
//        if (billboard.matches(validCharacters)) {
//            connection = DbConnection.getInstance();
//            countFilterBillboard = connection.prepareStatement(COUNT_FILTER_BILLBOARD_SQL);
//            countFilterBillboard.setString(1,billboard);
//            ResultSet rs = countFilterBillboard.executeQuery();
//            rs.next();
//            String count = rs.getString(1);
//            if (count.equals("1")){
//                editBillboard = connection.prepareStatement(EDIT_BILLBOARD_SQL);
//                editBillboard.setBlob(1,xmlCode);
//                editBillboard.setString(2,billboard);
//                rs = editBillboard.executeQuery();
//                resultMessage = "Pass: Billboard Edited";
//            }else {
//                resultMessage = "Fail: Billboard Does not Exist";
//            }
//        } else {
//            resultMessage = "Fail: Billboard Name Contains Illegal Characters";
//        }
//        return resultMessage;
//    }


    /**
     * Stores Database Queries: Billboard. This is a generic method which deletes billboards stored into database.
     * <p>
     * This method always returns immediately.
     * @param  billboard A String which provides Billboard Name to store into database
     * @return
     */
    public static Server.ServerAcknowledge deleteBillboard(String sessionToken, String billboard, String requestor) throws IOException, SQLException {
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
                if (requestor.equals(OGCreator)){
                    //todo: to edit in if scheduled (need edit)
                    // Creator only needs Create permission if not scheduled
                    if (UserAdmin.checkSinglePermission(sessionToken, CreateBillboard)) {
                        deleteBillboardSQL(billboard);
                        return Success;
                    } else {
                        System.out.println("Permissions were not sufficient, no Billboard was Edited");
                        return InsufficientPermission; // 3. Valid token but insufficient permission
                    }
                } else {
                    if (UserAdmin.checkSinglePermission(sessionToken, EditBillboard)) {
                            deleteBillboardSQL(billboard);
                            return Success;
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
     * Stores Database Queries: Billboard. This is a generic method which edits billboard xmlCode in the database.
     * <p>
     * This method always returns immediately.
     * @param  billboard A String which provides Billboard Name to store into database
     * @return
     */
    public static DbBillboard getBillboardInformation(String sessionToken, String billboard) throws IOException, SQLException {
        if (validateToken(sessionToken)) {
            System.out.println("Session is valid");
            String count = countFilterBillboardSql(billboard);
            if (count.equals("1")){
                listaBillboard = connection.prepareStatement(SHOW_BILLBOARD_SQL);
                listaBillboard.setString(1,billboard);
                ResultSet rs = listaBillboard.executeQuery();
                rs.next();
                DbBillboard dbBillboard = new DbBillboard(rs.getString("BillboardName"),
                        rs.getString("Creator"),
                        rs.getBytes("Image"),
                        rs.getString("XMLCode")
                );
                return dbBillboard;
            }else {
                DbBillboard dbBillboard = new DbBillboard("0","0",null,"Fail: Billboard Does not Exist");
                return dbBillboard;
            }
        } else {
            System.out.println("Session was not valid");
            DbBillboard dbBillboard = new DbBillboard("0","0",null,"Fail: Session was not valid");
            return dbBillboard;
        }
    }


    /**
     * Stores Database Queries: Billboard. This is a generic method which returns a list of billboards stored into database.
     * <p>
     * This method always returns immediately.
     * @return
     */

    public static BillboardList listBillboard(String sessionToken) throws IOException, SQLException {
        ArrayList<String> retrievedBillboard = new ArrayList<>();
        String serverResponse = null;

        if (validateToken(sessionToken)) {
            System.out.println("Session is valid");
            String count = countBillboardSql();
            if (count.equals("0")) {
                serverResponse = "Fail: No Billboard Exists";
                retrievedBillboard.add("0");
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
     * To be refactored like User
     ================================================================================================*/


    /**
     * Method to put a billboard from the database
     * @return
     * @throws IOException
     * @throws SQLException
     */
    public static String countFilterBillboardSql(String billboardName) throws IOException, SQLException {
        connection = DbConnection.getInstance();
        countFilterBillboard = connection.prepareStatement(COUNT_FILTER_BILLBOARD_SQL);
        countFilterBillboard.setString(1,billboardName);
        ResultSet rs = countFilterBillboard.executeQuery();
        rs.next();
        String count = rs.getString(1);
        return count;
    }

    public static String countBillboardSql() throws IOException, SQLException {
        connection = DbConnection.getInstance();
        Statement countBillboard = connection.createStatement();
        ResultSet rs = countBillboard.executeQuery(COUNT_BILLBOARD_SQL);
        rs.next();
        String count = rs.getString(1);
        return count;
    }

    /**
     * Method to put a billboard from the database
     * @return
     * @throws IOException
     * @throws SQLException
     */
    public static void addBillboardSQL(String billboardName, String creator,
                                       byte[] pictureData, String XMLCode) throws IOException, SQLException {
        connection = DbConnection.getInstance();
        createBillboard = connection.prepareStatement(STORE_BILLBOARD_SQL);
        createBillboard.setString(1,billboardName);
        createBillboard.setString(2,creator);
        createBillboard.setString(3, XMLCode);
        Blob pictureBlob = new javax.sql.rowset.serial.SerialBlob(pictureData); // Construct blob to store picture from byte array
        createBillboard.setBlob(4, pictureBlob);
        ResultSet rs = createBillboard.executeQuery();
    }

    /**
     * Method to put a billboard from the database
     * @return
     * @throws IOException
     * @throws SQLException
     */
    public static void editBillboardSQL(String billboardName, String creator,
                                       byte[] pictureData, String XMLCode) throws IOException, SQLException {
        connection = DbConnection.getInstance();
        editBillboard = connection.prepareStatement(EDIT_BILLBOARD_SQL);
        editBillboard.setString(1, XMLCode);
        Blob pictureBlob = new javax.sql.rowset.serial.SerialBlob(pictureData); // Construct blob to store picture from byte array
        editBillboard.setBlob(2, pictureBlob);
        editBillboard.setString(3,billboardName);
        ResultSet rs = editBillboard.executeQuery();
    }



    /**
     * Method to delete a billboard from the database
     * @return
     * @throws IOException
     * @throws SQLException
     */
    public static void deleteBillboardSQL(String billboardName) throws IOException, SQLException {
        connection = DbConnection.getInstance();
        deleteBillboard = connection.prepareStatement(DELETE_BILLBOARD_SQL);
        deleteBillboard.setString(1,billboardName);
        ResultSet rs = deleteBillboard.executeQuery();
    }


    /**
     * Stores Database Queries: Billboard. This is a generic method which deletes all billboards stored into database.
     * <p>
     * This method always returns immediately.
     * @return
     */
    public static Server.ServerAcknowledge deleteAllBillboard() throws IOException, SQLException {
        connection = DbConnection.getInstance();
        Statement deleteAllBillboard = connection.createStatement();
        deleteAllBillboard.executeQuery(DELETE_ALL_BILLBOARD_SQL);
        return Success;
    }





}
