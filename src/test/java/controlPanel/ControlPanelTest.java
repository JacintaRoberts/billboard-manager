package controlPanel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ControlPanelTest {
    /* Test 0: Declaring ControlPanel object.
     * Description: ControlPanel Object is created upon application start.
     * Output: ControlPanel Object is declared
     */
    ControlPanel controlpanel;

    /* Test 1: Constructing a ControlPanel object
     * Description: ControlPanel Object is created upon application start.
     * Output: ControlPanel Object is instantiated from ControlPanel class
     */
    @BeforeEach
    @Test
    public void setControlpanel() {
        controlpanel = new ControlPanel();
    }


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