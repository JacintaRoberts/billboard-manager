package server;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class BillboardAdmin {

    // Custom SQL Strings for Specific Queries
    public static final String STORE_BILLBOARD_SQL = "INSERT INTO Billboards VALUES (?,?,?) ";
    public static final String EDIT_BILLBOARD_SQL = "UPDATE Billboards " +
                                                    "SET XMLCode = ?" +
                                                    "WHERE BillboardName = ?";
    public static final String DELETE_BILLBOARD_SQL = "DELETE FROM Billboards WHERE BillboardName = ?";
    public static final String DELETE_ALL_BILLBOARD_SQL = "DELETE FROM Billboards";
    public static final String COUNT_FILTER_BILLBOARD_SQL = "SELECT COUNT(*) FROM Billboards WHERE BillboardName = ?";
    public static final String COUNT_BILLBOARD_SQL = "SELECT COUNT(*) FROM Billboards";
    public static final String CREATE_BILLBOARD_TABLE = "CREATE TABLE IF NOT EXISTS `BillboardDatabase`.`Billboards` (\n" +
            "    `BillboardName` varchar(255) NOT NULL default '',\n" +
            "      `Creator` varchar(255) NOT NULL default '',\n" +
            "      `XMLCode` TEXT,\n" +
            "      PRIMARY KEY (`BillboardName`)\n" +
            "  )";
    public static final String DROP_BILLBOARD_TABLE = "DROP TABLE IF EXISTS `BillboardDatabase`.`Billboards`";
    public static final String LIST_BILLBOARD_SQL = "SELECT BillboardName FROM Billboards";
    public static final String SHOW_BILLBOARD_SQL = "SELECT * FROM Billboards WHERE BillboardName = ?";

    // Custom Parameters for connection
    private static Connection connection;
    private static PreparedStatement createBillboard;
    private static PreparedStatement deleteBillboard;
    private static PreparedStatement dropBillboard;
    private static PreparedStatement countFilterBillboard;
    private static PreparedStatement editBillboard;
    private static PreparedStatement listaBillboard;


    // Public Class for Custom Exceptions
    public class illegalBillboardNameException extends Exception {
        public illegalBillboardNameException(String errorMessage) {
            super(errorMessage);
        }
    }

    public class BillboardNotExistException extends Exception {
        public BillboardNotExistException(String errorMessage) {
            super(errorMessage);
        }
    }

    public class emptyBillboardTable extends Exception {
        public emptyBillboardTable(String errorMessage) {
            super(errorMessage);
        }
    }


    /**
     * Stores Database Queries: Billboard. This is a generic method which stores any query sent to the database.
     * <p>
     * This method always returns immediately.
     * @param  st A Statement object which is the connection.createStatement()
     * @param  query A String which has the query fed into executeQuery
     * @throws IOException
     * @throws SQLException
     */
    public static ArrayList<DbBillboard> storeBillboardContents(Statement st, String query) throws IOException,SQLException {
        // Set List to store contents in
        ArrayList<DbBillboard> queryList = new ArrayList<>();

        // get all current entries
        ResultSet rs = st.executeQuery(query);


        // Loop through cursor
        while (rs.next()) {
            DbBillboard dbBillboard = new DbBillboard(rs.getString("BillboardName"),
                    rs.getString("Creator"),
                    rs.getString("XMLCode"),
                    "List");
            queryList.add(dbBillboard);
        }

        return queryList;
    }


    /**
     * Stores Database Queries: Billboard. This is a generic method which Make sures billboard is made with default data.
     * <p>
     * This method always returns immediately.
     * @return
     */
    public String createGenericBillboard() throws IOException, SQLException{
        String resultMessage;

        connection = DbConnection.getInstance();
        Statement countBillboard = connection.createStatement();
        ResultSet rs = countBillboard.executeQuery(COUNT_BILLBOARD_SQL);
        rs.next();
        String count = rs.getString(1);
        if (count.equals("0")){
            createBillboard = connection.prepareStatement(STORE_BILLBOARD_SQL);
            createBillboard.setString(1,"TestBillboard");
            createBillboard.setString(2,"TestUser");
            createBillboard.setString(3, "TestXMLCODE");
            createBillboard.executeQuery();

            createBillboard = connection.prepareStatement(STORE_BILLBOARD_SQL);
            createBillboard.setString(1,"TestBillboard2");
            createBillboard.setString(2,"TestUser2");
            createBillboard.setString(3, "TestXMLCODE2");
            createBillboard.executeQuery();

            createBillboard = connection.prepareStatement(STORE_BILLBOARD_SQL);
            createBillboard.setString(1,"TestBillboard3");
            createBillboard.setString(2,"TestUser3");
            createBillboard.setString(3, "TestXMLCODE3");
            createBillboard.executeQuery();

            resultMessage = "Pass: Billboard Database Created";

        } else {
            resultMessage = "Pass: Billboard Database Already Created";
        }
        return resultMessage;
    }


    /**
     * Delete Table: Billboard. This is a generic method which deletes the table.
     * <p>
     * This method always returns immediately.
     * @return
     */
    public String dropBillboardTable() throws IOException, SQLException{
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
    public String createBillboardTable() throws IOException, SQLException{
        String resultMessage;

        connection = DbConnection.getInstance();
        Statement countBillboard = connection.createStatement();
        ResultSet rs = null;
        try {
            rs = countBillboard.executeQuery(COUNT_BILLBOARD_SQL);
            rs.next();
            String count = rs.getString(1);
            if (count.equals("0")){
                createGenericBillboard();
                resultMessage = "Data Filled";
            } else {
                resultMessage = "Data Exists";
            }
        } catch (SQLSyntaxErrorException throwables) {
            rs = countBillboard.executeQuery(CREATE_BILLBOARD_TABLE);
            createGenericBillboard();
            resultMessage = "Table Created";
        }
        return resultMessage;
    }


    /**
     * Stores Database Queries: Billboard. This is a generic method which stores any query sent to the database.
     * <p>
     * This method always returns immediately.
     * @param  userName A String which provides username to store into database
     * @param  billboard A String which provides Billboard Name to store into database
     * @param  xmlCode A String which provides xmlCode to store into database
     * @return
     */
    public String createBillboard(String userName,
                                  String billboard,
                                  String xmlCode) throws IOException, SQLException, illegalBillboardNameException {
        String resultMessage;
        String validCharacters = "([A-Za-z0-9-_]+)";
        if (billboard.matches(validCharacters)) {
            connection = DbConnection.getInstance();
            createBillboard = connection.prepareStatement(STORE_BILLBOARD_SQL);
            createBillboard.setString(1,billboard);
            createBillboard.setString(2,userName);
            createBillboard.setString(3, xmlCode);
            ResultSet rs = createBillboard.executeQuery();
        } else {
            throw new illegalBillboardNameException("Fail: Billboard Name Contains Illegal Characters");
        }

        resultMessage = "Pass: Billboard Created";
        return resultMessage;
    }


    /**
     * Stores Database Queries: Billboard. This is a generic method which edits billboard xmlCode in the database.
     * <p>
     * This method always returns immediately.
     * @param  billboard A String which provides Billboard Name to store into database
     * @param  xmlCode A String which provides xmlCode to store into database
     * @return
     */
    public String editBillboard(String billboard,
                                String xmlCode) throws IOException, SQLException, illegalBillboardNameException, BillboardNotExistException {
        String resultMessage;
        String validCharacters = "([A-Za-z0-9-_]+)";
        if (billboard.matches(validCharacters)) {
            connection = DbConnection.getInstance();
            countFilterBillboard = connection.prepareStatement(COUNT_FILTER_BILLBOARD_SQL);
            countFilterBillboard.setString(1,billboard);
            ResultSet rs = countFilterBillboard.executeQuery();
            rs.next();
            String count = rs.getString(1);
            if (count.equals("1")){
                editBillboard = connection.prepareStatement(EDIT_BILLBOARD_SQL);
                editBillboard.setString(1,xmlCode);
                editBillboard.setString(2,billboard);
                rs = editBillboard.executeQuery();
            }else {
                throw new BillboardNotExistException("Fail: Billboard Does not Exist");
            }
        } else {
            throw new illegalBillboardNameException("Fail: Billboard Name Contains Illegal Characters");
        }

        resultMessage = "Pass: Billboard Edited";
        return resultMessage;
    }


    /**
     * Stores Database Queries: Billboard. This is a generic method which deletes billboards stored into database.
     * <p>
     * This method always returns immediately.
     * @param  billboard A String which provides Billboard Name to store into database
     * @return
     */
    public String deleteBillboard(String billboard) throws IOException, SQLException, illegalBillboardNameException, BillboardNotExistException {
        String resultMessage;
        String validCharacters = "([A-Za-z0-9-_]+)";
        if (billboard.matches(validCharacters)) {
            connection = DbConnection.getInstance();
            Statement countBillboard = connection.createStatement();
            ResultSet rs = countBillboard.executeQuery(COUNT_BILLBOARD_SQL);
            rs.next();
            String count = rs.getString(1);
            if (!count.equals("0")){
                connection = DbConnection.getInstance();
                deleteBillboard = connection.prepareStatement(DELETE_BILLBOARD_SQL);
                deleteBillboard.setString(1,billboard);
                rs = deleteBillboard.executeQuery();
            }else {
                throw new BillboardNotExistException("Fail: Billboard Does not Exist");
            }
        } else {
            throw new illegalBillboardNameException("Fail: Billboard Name Contains Illegal Characters");
        }

        resultMessage = "Pass: Billboard Deleted";
        return resultMessage;
    }


    /**
     * Stores Database Queries: Billboard. This is a generic method which deletes all billboards stored into database.
     * <p>
     * This method always returns immediately.
     * @return
     */
    public String deleteAllBillboard() throws IOException, SQLException, emptyBillboardTable {
        String resultMessage;
        connection = DbConnection.getInstance();
        Statement countBillboard = connection.createStatement();
        ResultSet rs = countBillboard.executeQuery(COUNT_BILLBOARD_SQL);
        rs.next();
        String count = rs.getString(1);
        if (count.equals("0")){
            throw new emptyBillboardTable("Fail: No Billboard Exists");
        }else {
            connection = DbConnection.getInstance();
            Statement deleteAllBillboard = connection.createStatement();
            rs = deleteAllBillboard.executeQuery(DELETE_ALL_BILLBOARD_SQL);
        }
        resultMessage = "Pass: All Billboards Deleted";
        return resultMessage;
    }



    /**
     * Stores Database Queries: Billboard. This is a generic method which returns a list of billboards stored into database.
     * <p>
     * This method always returns immediately.
     * @return
     */
    //     LIST_BILLBOARD_SQL,COUNT_BILLBOARD_SQL
    public BillboardList listBillboard() throws IOException, SQLException, emptyBillboardTable {
        ArrayList<String> retrievedBillboard = new ArrayList<>();

        connection = DbConnection.getInstance();
        Statement countBillboard = connection.createStatement();
        ResultSet rs = countBillboard.executeQuery(COUNT_BILLBOARD_SQL);
        rs.next();
        String count = rs.getString(1);
        String serverResponse = null;
        if (count.equals("0")) {
            serverResponse = "Fail: No Billboard Exists";
            retrievedBillboard.add("0");
            throw new emptyBillboardTable("Fail: No Billboard Exists");
        } else {
            connection = DbConnection.getInstance();
            Statement listBillboard = connection.createStatement();
            rs = listBillboard.executeQuery(LIST_BILLBOARD_SQL);
            while (rs.next()) {
                String value = rs.getString(1);
                retrievedBillboard.add(value);
            }
            serverResponse = "Pass: Billboard List Returned";
        }

        BillboardList billboardList = new BillboardList(serverResponse,retrievedBillboard);

        return billboardList;
    }



    /**
     * Stores Database Queries: Billboard. This is a generic method which edits billboard xmlCode in the database.
     * <p>
     * This method always returns immediately.
     * @param  billboard A String which provides Billboard Name to store into database
     * @param  xmlCode A String which provides xmlCode to store into database
     * @return
     */
    public DbBillboard getBillboardInformation(String billboard) throws IOException, SQLException, illegalBillboardNameException, BillboardNotExistException {
        String resultMessage;
        String validCharacters = "([A-Za-z0-9-_]+)";
        if (billboard.matches(validCharacters)) {
            connection = DbConnection.getInstance();
            countFilterBillboard = connection.prepareStatement(COUNT_FILTER_BILLBOARD_SQL);
            countFilterBillboard.setString(1,billboard);
            ResultSet rs = countFilterBillboard.executeQuery();
            rs.next();
            String count = rs.getString(1);
            if (count.equals("1")){
                listaBillboard = connection.prepareStatement(SHOW_BILLBOARD_SQL);
                listaBillboard.setString(1,billboard);
                rs = listaBillboard.executeQuery();
                rs.next();
                DbBillboard dbBillboard = new DbBillboard(rs.getString("BillboardName"),
                        rs.getString("Creator"),
                        rs.getString("XMLCode"),
                        "Pass: Billboard Info Returned");
                return dbBillboard;
            }else {
                throw new BillboardNotExistException("Fail: Billboard Does not Exist");
            }
        } else {
            throw new illegalBillboardNameException("Fail: Billboard Name Contains Illegal Characters");
        }
    }



    /**
     * Tidy up connections
     */
    public static void close() {
        try {
            createBillboard.close();
            deleteBillboard.close();
            countFilterBillboard.close();
            editBillboard.close();
            dropBillboard.close();
            listaBillboard.close();
            connection.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

}
