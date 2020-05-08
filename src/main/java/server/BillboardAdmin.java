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
    public static final String COUNT_FILTER_BILLBOARD_SQL = "SELECT COUNT(*) FROM Billboards WHERE BillboardName = ?";

    // Custom Parameters for connection
    private static Connection connection;
    private static PreparedStatement selectBillboard;


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
                    rs.getString("XMLCode"));
            queryList.add(dbBillboard);
        }

        return queryList;
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
            selectBillboard = connection.prepareStatement(STORE_BILLBOARD_SQL);
            selectBillboard.setString(1,billboard);
            selectBillboard.setString(2,userName);
            selectBillboard.setString(3, xmlCode);
            ResultSet rs = selectBillboard.executeQuery();
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
            selectBillboard = connection.prepareStatement(COUNT_FILTER_BILLBOARD_SQL);
            selectBillboard.setString(1,billboard);
            ResultSet rs = selectBillboard.executeQuery();
            rs.next();
            String count = rs.getString(1);
            if (count.equals("1")){
                selectBillboard = connection.prepareStatement(EDIT_BILLBOARD_SQL);
                selectBillboard.setString(1,xmlCode);
                selectBillboard.setString(2,billboard);
                rs = selectBillboard.executeQuery();
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
            selectBillboard = connection.prepareStatement(COUNT_FILTER_BILLBOARD_SQL);
            selectBillboard.setString(1,billboard);
            ResultSet rs = selectBillboard.executeQuery();
            rs.next();
            String count = rs.getString(1);
            if (count.equals("1")){
                connection = DbConnection.getInstance();
                selectBillboard = connection.prepareStatement(DELETE_BILLBOARD_SQL);
                selectBillboard.setString(1,billboard);
                rs = selectBillboard.executeQuery();
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
     * Tidy up connections
     */
    public static void close() {
        try {
            selectBillboard.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

}
