package helpers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HelpersTest {
    /* Test 0: Declaring Helpers object
     * Description: Helpers object should be running in background on application start.
     * Expected Output: Helpers object is declared
     */
    Helpers helpers;


    /* Test 1: Constructing a Helpers object
     * Description: Helpers Object should be able to be created to assist in facilitating the shared functionality
     * Expected Output: Helpers object is instantiated from Helpers class
     */
    @BeforeEach
    @Test
    public void setUpHelpers() {
        helpers = new Helpers();
    }


    /* Test 2: Shared Helper function - Read from network properties files (Success)
     * Description: Takes the file name to be read and returns the port number (Integer) and ip (string) in a HashMap
     * Expected Output: Successfully return the port number and ip from the network.props file as a HashMap<String, Object>
     * Suggested Implementation provided - wanted to figure out how the links worked for file paths for db.props
     */
    @Test
    public void readNetworkProps() {
        String testPort = "4444";
        String testIp = "127.0.0.1";
        assertAll("This checks the TestPort and TestIP",
                ()-> assertEquals(testPort, Helpers.readProps("src\\test\\resources\\network.props").getProperty("port")),
                ()-> assertEquals(testIp, Helpers.readProps("src\\test\\resources\\network.props").getProperty("ip"))
        );
    }


    /* Test 3: Shared Helper function - Validate port from string (Success)
     * Description: Method takes the string to be validated and returns the port number parsed as an int or an exception
     * Expected Output: Successfully validate and return the port number from the network.props file as an int
     */
    @Test
    public void validateNetworkPort() throws BadPortNumberException {
        assertEquals(4444, Helpers.validatePort("4444"));
    }


    /* Test 4: Shared Helper function - Bad port numbers (Exception Handling)
     * Description: Implement appropriate Exception Handling for bad port number read from file (only 0 to 65535 is ok)
     * Expected Output: Throws BadPortNumberException (need to create this specific Exception!)
     */
    @Test
    public void badPortNumbers() {
        // Check min-1
        assertThrows(BadPortNumberException.class, () -> {
            helpers.validatePort("-1");
        });
        // Check max+1
        assertThrows(BadPortNumberException.class, () -> {
            helpers.validatePort("65536");
        });
    }


    /* Test 5: Shared Helper function - Read port/IP from network properties files (Exception Handling)
     * Description: Implement appropriate Exception Handling for bad file name. This could also pick up:
     * reading a local file that was no longer available or trying to read file but don't have permission.
     * Expected Output: IOException from non-existent file name
     */
    @Test
    public void badNetworkPropsFile() {
       // Check both port and ip retrieval failed
        assertThrows(java.io.FileNotFoundException.class, () -> {
            helpers.readProps("unknown.props");
        });
    }

}