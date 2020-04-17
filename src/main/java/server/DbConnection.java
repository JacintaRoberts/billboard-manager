package server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbConnection {

//    // TODO: Alan test for jdbc connection
//     public static void main(String[] args) throws SQLException {
//        Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/mydb", "root", "");
//        Statement statement = connection.createStatement();
//        ResultSet resultset = statement.executeQuery("SELECT * from test");
//
//        connection.close();
//    }


    public DbConnection(String propString) {
        Properties props = new Properties();
        FileInputStream in = null;
        Connection connection = null;
        try{
            // set input path and load
            in = new FileInputStream(propString);
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

//    public HashMap<String, String> readProps(String s) {
//        HashMap<String,String> dummy = new HashMap<>();
//        return dummy;
//    }
}
