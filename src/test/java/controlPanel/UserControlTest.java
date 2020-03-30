package controlPanel;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserControlTest {
    /* Test 0: Declaring UserControl object
     * Description: UserControl object should be running in background on application start.
     * Expected Output: UserAdmin object is declared
     */
    UserControl userControl;

    /* Test 1: Constructing a UserControl object
     * Description: UserControl Object should be able to be created on logged in user request from control panel
     * Expected Output: UserControl object is instantiated from UserControl class
     */
    @BeforeEach
    @Test
//    public void setUpUserControl() {
//      userControl = new UserControl();
//    }


    /* Test x: Request to server to
     * Description:
     * Expected Output:
     */
    public void listUsers(String sessionToken){

    }


    /* Test x: Request to server to
     * Description:
     * Expected Output:
     */
    public void createUserRequest(String currentUser, String newUsername, String sessionToken) {

    }


    /* Test x: Request to server to
     * Description:
     * Expected Output:
     */
    public void setUserPasswordRequest(String sessionToken, String newPassword) {

    }


    /* Test x: Request to server to
     * Description:
     * Expected Output:
     */
    public void createUsers(String sessionToken, String newUsername, String newPassword){

    }

    /* Test x: Request to server to
     * Description:
     * Expected Output:
     */
    public void getUserPermission(String sessionToken, String currentUser){

    }


    /* Test x: Request to server to
     * Description:
     * Expected Output:
     */
    public void setUserPermission(String sessionToken, String currentUser, String targetUser, boolean[] permissions) {

    }



}