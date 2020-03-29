package server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public class Server {
    /**
     * Retrieves the port and IP address from the specified .props file
     * @param filePath the location/name of the network.props file
     * @return ArrayList<Object> - contains Integer port number, String IP address from network.props file
     */
    public static HashMap<String, Object> readProps(String filePath) {
        Properties props = new Properties();
        FileInputStream in = null;
        try {
            in = new FileInputStream(String.valueOf(filePath));
            props.load(in);
            in.close();
            Integer port = Integer.parseInt(props.getProperty("port"));
            String ip = props.getProperty("ip");
            HashMap<String, Object> combined = new HashMap<>();
            combined.put("port", port);
            combined.put("ip", ip);
            return combined;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
