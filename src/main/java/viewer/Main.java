package viewer;


import server.ScheduleAdmin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main implements Runnable {

    // Contains the server's response (Billboard XML) as a string
    private String serverResponse;


    @Override
    public void run() {
        Viewer viewer = new Viewer();
        try {
            // TODO: Check this works
            serverResponse = ScheduleAdmin.getCurrentBillboardXML();
            System.out.println("Received from server: " + serverResponse);
            if (!serverResponse.isEmpty()) {
                viewer.displayBillboard(serverResponse);
            } else {
                viewer.displaySpecialMessage("There are no billboards to display right now."); // Show no billboard screen
            }
        } catch (IOException | SQLException e) {
            viewer.displaySpecialMessage("Error: Cannot connect to server. Trying again now..."); // Error in receiving content
        }
        // displayBillboard(serverResponse);
    }


    public static void main(String[] args ) {
        Main viewer = new Main();
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(viewer, 0, 15, TimeUnit.SECONDS);
    }

}
