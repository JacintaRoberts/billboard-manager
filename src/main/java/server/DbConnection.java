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
     * Main Database Connection function. This method will set up relevant property files and call relevant functions.
     * <p>
     * This method always returns immediately.
     * @throws FileNotFoundException Throws an exception when a file with the specified pathname does not exist.
     * @throws IOException Throws an exception if an I/O exception of some sort has occurred.
     * @throws SQLException Throws an exception if there is a database access error or other errors.
     */
    DbConnection() throws FileNotFoundException, IOException, SQLException {

        // Set new properties class and initiate File input stream and connection
        Properties props = readProperties("src\\test\\resources\\db.props");

        // specify data source, username and password
        String url = props.getProperty("jdbc.url");
        String username = props.getProperty("jdbc.username");
        String password = props.getProperty("jdbc.password");
        String schema = props.getProperty("jdbc.schema");

        // get a connection
        try{
            instance = DriverManager.getConnection(url + "/" + schema,username, password);
            System.out.println("Connected to database at: "+ url + "/" + schema);
        } catch (SQLSyntaxErrorException throwables) {
            instance = DriverManager.getConnection(url ,username, password);
                Statement statement = instance.createStatement();
                String sql = "CREATE DATABASE IF NOT EXISTS BillboardDatabase";
                statement.executeUpdate(sql);
                instance = DriverManager.getConnection(url + "/" + schema,username, password);
                System.out.println("Database created!");
        }
    }


    /**
     * Provides global access to the singleton instance of the database connection.
     * <p>
     * @return Returns a Connection handle to the singleton instance of the database connection.
     * @throws IOException Throws an exception if an I/O exception of some sort has occurred.
     * @throws SQLException Throws an exception if there is a database access error or other errors.
     */
    public static Connection getInstance() throws IOException, SQLException {
        if (instance == null) {
            new DbConnection();
        }
        return instance;
    }


    /**
     * Read Database Properties as input stream
     * <p>
     * This method always returns immediately.
     * @param fileLocation A String which is the location of the dbprops File.
     * @return Returns a Properties object containing information about the database.
     * @throws FileNotFoundException Throws an exception when a file with the specified pathname does not exist.
     * @throws IOException Throws an exception if an I/O exception of some sort has occurred.
     */
    public static Properties readProperties(String fileLocation) throws FileNotFoundException, IOException {
        // Set new properties class and initiate File input stream and connection
        Properties props = new Properties();
        FileInputStream in = null;
        // Load properties
        in = new FileInputStream(fileLocation);
        props.load(in);
        in.close();
        return props;
    }




    /**
     * Prints Database Query result to the system console for debugging purposes. This is mainly created for debugging
     * and printing to server console when the application is running
     * <p>
     * This method always returns immediately.
     * @param st A Statement object which is the connection.createStatement().
     * @param query A String which has the query fed into executeQuery.
     * @throws SQLException Throws an exception if there is a database access error or other errors.
     */
    public static void displayContents(Statement st, String query) throws SQLException {
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
