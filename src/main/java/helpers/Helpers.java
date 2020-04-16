package helpers;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Helpers {

    public static Properties readProps(String fileName) throws IOException {
        FileReader reader = new FileReader(fileName);
        Properties p = new Properties();
        p.load(reader);
        return p;
    }

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
