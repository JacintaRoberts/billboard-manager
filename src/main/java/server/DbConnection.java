package server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DbConnection {

        /**
         * The singleton instance of the database connection.
         */
    private static Connection instance = null;


    /**
     * Constructor intializes the connection.
     */
    private DbConnection() {
        // Set new properties class and initiate File input stream and connection
        Properties props = new Properties();
        FileInputStream in = null;
        Connection connection = null;
        try{
            // set input path and load
            in = new FileInputStream("src\\test\\resources\\db.props");
            props.load(in);
            in.close();

            // specify data source, username and password
            String url = props.getProperty("jdbc.url");
            String username = props.getProperty("jdbc.username");
            String password = props.getProperty("jdbc.password");
            String schema = props.getProperty("jdbc.schema");

            // get a connection
            connection = DriverManager.getConnection(url + "/" + schema, username, password);
        } catch (SQLException sqle){
            System.err.println(sqle);
        } catch (FileNotFoundException fnfe){
            System.err.println(fnfe);
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    /**
     * Provides global access to the singleton instance of the UrlSet.
     *
     * @return a handle to the singleton instance of the UrlSet.
     */
    public static Connection getInstance() {
        if (instance == null) {
            new DbConnection();
        }
        return instance;
    }


    private static void displayContents(Statement st, String query) throws SQLException {
        // get all current entries
        ResultSet rs = st.executeQuery(query);

        // use metadata to get the number of columns
        int columnCount = rs.getMetaData().getColumnCount();

        // output the column names
        for (int i = 0; i < columnCount; i++) {
            System.out.printf("%-20s", rs.getMetaData().getColumnName(i + 1));
        }
        System.out.printf("%n");

        // output each row
        while (rs.next()) {
            for (int i = 0; i < columnCount; i++) {
                System.out.printf("%-20s", rs.getString(i + 1));
            }
            System.out.printf("%n");
        }
        System.out.printf("%n");
    }





}
