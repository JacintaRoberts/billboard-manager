package server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Properties;

public class DbConnection {

    // TODO: Alan test for jdbc connection
     public static void main(String[] args) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/mydb", "root", "");
        Statement statement = connection.createStatement();
        ResultSet resultset = statement.executeQuery("SELECT * from test");

        connection.close();
    }


    public DbConnection(HashMap<String, String> props) {
        Properties props = new Properties();
        FileInputStream in = null;
        try{
            // set input path and load
            in new FileInputStream("./db.props");
            props.load(in);
            in.close();

            // specify data source, username and password
            String url = props.getProperty("jdbc.url");
            String username = props.getProperty("jdbc.username");
            String password = props.getProperty("jdbc.password");
            String schema = props.getProperty("jdbc.schema");

            // get a connection
            Connection instance = DriverManager.getConnection(url + "/" + schema, username, password);
        } catch (SQLException sqle){
            System.err.println(sqle);
        } catch (FileNotFoundException fnfe){
            System.err.println(fnfe);
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public HashMap<String, String> readProps(String s) {
        HashMap<String,String> dummy = new HashMap<>();
        return dummy;
    }
}
