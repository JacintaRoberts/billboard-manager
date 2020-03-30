package helpers;

class HelpersTest {


    /* Test 1: Shared Helper function - Read from network properties files (success)
     * Description: Takes the file name to be read and returns the port number (Integer) and ip (string) in a HashMap
     * Expected Output: Successfully return the port number and ip from the network.props file as a HashMap<String, Object>
     * Suggested Implementation provided - wanted to figure out how the links worked for file paths for db.props
     */
//    @Test
//    public void readNetworkProps() {
//        int testPort = 4444;
//        String testIp = "127.0.0.1";
//        assertAll("This checks the TestPort and TestIP",
//                ()-> assertEquals(testPort, readProps("src\\test\\resources\\network.props").get("port")),
//                ()-> assertEquals(testIp, readProps("src\\test\\resources\\network.props").get("ip"))
//                );
//    }

    /* Test 2: Shared Helper function - Validate port from string (success)
     * Description: Method takes the string to be validated and returns the port number parsed as an int or an exception
     * Expected Output: Successfully validate and return the port number from the network.props file as an int
     */
//    @Test
//    public void validateNetworkPort() {
//        assertEquals(4444, validatePort(4444));
//    }

    /* Test 3: Shared Helper function - Bad port numbers (error handling)
     * Description: Implement appropriate error handling for bad port number read from file (only 0 to 65535 is ok)
     * Expected Output: Throws BadPortNumberException (need to create this specific Exception!)
     */
//    @Test
//    public void badPortNumbers() {
//        // Check min-1
//        assertThrows(BadPortNumberException.class, () -> {
//            validatePort(-1)
//        });
//        // Check max+1
//        assertThrows(BadPortNumberException.class, () -> {
//            validatePort(65536);
//        });
//    }

    /* Test 4: Shared Helper function - Read port/IP from network properties files (error handling)
     * Description: Implement appropriate error handling for bad file name. This could also pick up:
     * reading a local file that was no longer available or trying to read file but don't have permission.
     * Expected Output: IOException from non-existent file name
     */
//    @Test
//    public void badNetworkPropsFile() {
//       // Check both port and ip retrieval failed
//        assertThrows(java.io.FileNotFoundException.class, () -> {
//            readProps("unknown.props");
//        });
//    }

    /* Test 5: Shared Helper function - Convert IP address from string to bytes array (from network properties file)
     * Description: Read and convert IP address from network.props
     * Expected Output: Ip address string is successfully converted to byte array
     */
//    @Test
//    public void convertNetworkIpToBytes() {
//        byte[] addr = new byte[] { 127, 0, 0, 1 };
//        assertEquals(addr, convertIpToBytes("127.0.0.1"));
//    }

    /* Test 6: Shared Helper function - Check that IP bytes is able to be converted to an InetAddress
     * Description: Checks that the bytes IP address is able to be converted appropriately.
     * Expected Output: IP address is successfully converted to an InetAddress
     */
//    @Test
//    public void convertIpBytesToINetAddress() {
//        byte[] addr = convertIpToBytes("127.0.0.1")
//        assertTrue(InetAddress.getByAddress(addr) instanceof InetAddress);
//    }


}