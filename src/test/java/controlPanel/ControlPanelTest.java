package controlPanel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ControlPanelTest {
    /* Test 0: Declaring ControlPanel object
     */
    ControlPanel controlpanel;

    /* Test 1: Constructing a ControlPanel object
     */
    @BeforeEach
    @Test
    public void setControlpanel() {
        controlpanel = new ControlPanel();
    }

    /* Test 2: ControlPanel - Connect to Server (Success)
     * Description: Action is called when methods are conducted that requires server information. Address Defaults to
     *              localhost.
     * Input: Address and Port
     * Output: Receive Acceptance string from Server
     */
//    @Test
//    public void connectToServer(address, port) {
//    }


    /* Test 3: ControlPanel - Connect to Server  (Error)
     * Description: Action is called when methods are conducted that requires server information. Address Defaults to
     *              localhost.
     *              Implement appropriate error handling when address does not exists.
     * Input: Address and Port.
     * Output: Throws an error on address.
     */
//    @Test
//    public void connectToServer(address, port) {
//    }


    /* Test 4: ControlPanel - Connect to Server  (Error)
     * Description: Action is called when methods are conducted that requires server information. Address Defaults to
     *              localhost. Implement appropriate error handling when port number is illegal (Outside range of port 0
     *              and 65536).
     * Input: Address and Port.
     * Output: Throws an error on bad port.
     */
//    @Test
//    public void connectToServer(address, port) {
//    }


    /* Test 5: ControlPanel - Connect to Server  (Error)
     * Description: Action is called when methods are conducted that requires server information. Address Defaults to
     *              localhost. Implement appropriate error handling when port number is illegal (Outside range of port 0
     *              and 65536).
     * Input: Address and Port.
     * Output: Throws an error on address.
     */
//    @Test
//    public void connectToServer(address, port) {
//    }


    /* Test 6: ControlPanel - Login (Success)
    * Description: Login Request sent with the username and hashed password to authenticate user into session.
    * Input: Username and password
    * Output: Receives sessionToken from Server.
     */
//    @Test
//    public void loginRequest(username, Password) {
//    }


    /* Test 7: ControlPanel - Login (Fail)
     * Description: Login Request sent with the username and hashed password to authenticate user into session.
     *              Implement appropriate error handling when user details are incorrect.
     * Input: Username and password
     * Output: Receives error message from server.
     */
//    @Test
//    public void loginRequest(username, Password) {
//    }


}