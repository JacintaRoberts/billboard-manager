package controlPanel;


import helpers.Helpers;

import java.io.IOException;

public class UserControl {

    /**
     * Send a user's request to log out to the server
     * Expected Output: Successful log out of the user, acknowledgement received and the session token is expired.
     * @param sessionToken String session token that is created when the user initially logs in
     * @return String server acknowledgement as to whether logout was successful or an error occurred
     * @throws IOException Thrown if unknown server host when communicating through sockets
     * @throws ClassNotFoundException If the object received from the server is instantiated from a class that is not found
     */
    public String logout(String sessionToken) throws IOException, ClassNotFoundException {
        String message = "Logout,"+sessionToken;
        return (String) Helpers.initClient(message);
    }
}
