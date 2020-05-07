package server;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class BillboardAdmin {

    // Custom SQL Strings for Specific Queries
    public static final String SELECT_USER_SQL = "SELECT * FROM users WHERE Username = ?";
    public static final String STORE_BILLBOARD_SQL = "INSERT INTO Billboards VALUES (?,?,?) ";

    // Custom Parameters for connection
    private static Connection connection;
    private static PreparedStatement selectBillboard;


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
     * @param  sessionToken A Statement object which is the connection.createStatement()
     * @param  billboard A String which has the query fed into executeQuery
     * @param  xmlCode A String which has the query fed into executeQuery
     * @return
     */
    public String createBillboard(String userName,
                                  String billboard,
                                  String xmlCode) throws IOException, SQLException {
        String resultMessage;

        connection = DbConnection.getInstance();
        selectBillboard = connection.prepareStatement(STORE_BILLBOARD_SQL);
        selectBillboard.setString(1,billboard);
        selectBillboard.setString(2,userName);
        selectBillboard.setString(3, xmlCode);
        ResultSet rs = selectBillboard.executeQuery();

        resultMessage = "Pass: Billboard Created";
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
