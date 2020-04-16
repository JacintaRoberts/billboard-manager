package helpers;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Helpers {

    /**
     * Reads the properties from the specified file (.props)
     * @param filePath The string file path to be read from
     * @return Properties object that can have values retrieved from it
     * @throws IOException - If the file cannot be found
     */
    public static Properties readProps(String filePath) throws IOException {
        FileReader reader = new FileReader(filePath);
        Properties props = new Properties();
        props.load(reader);
        return props;
    }

    /**
     * Parses a String port number to an integer and ensures that it is within the acceptable range
     * of 0 to 65535.
     * @param port The string port number to be parsed and validated
     * @return Integer representation of the port number
     * @throws BadPortNumberException - For port numbers less than 0 or greater than 65535
     */
    public static int validatePort(String port) throws BadPortNumberException {
        int portNum = Integer.parseInt(port);
        if ( portNum < 0 ) {
            throw new BadPortNumberException("Port number in network.props must not be less than 0.");
        }
        else if (portNum > 65535) {
            throw new BadPortNumberException("Port number in network.props must not be greater than 65535.");
        }
        else {
            return portNum;
        }
    }



}
