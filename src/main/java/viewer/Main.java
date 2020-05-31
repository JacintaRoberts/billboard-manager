package viewer;


import helpers.Helpers;
import server.DbBillboard;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Main starts up the application by instantiating the Viewer and connecting to the Server. The main and AWT event
 * thread are joined to run the application safely.
 */
public class Main implements Runnable {
    /**
     * Viewer declaration (singleton) to prevent multiple windows from popping up after starting application
     */
    private static Viewer viewer;

    /**
     * Attempt to display billboard for the Viewer application or show appropriate error screen.
     */
    @Override
    public void run() {
        try {
            DbBillboard billboardObject = null;
            billboardObject = (DbBillboard) activeBillboardRequest();
            if (billboardObject == null) {
                System.out.println("No billboard to display...");
                viewer.displaySpecialMessage("There are no billboards to display right now."); // Show no billboard screen
            } else {
                System.out.println("Attempting to display billboard...");
                String billboardXML = billboardObject.getXMLCode();
                byte[] pictureData = billboardObject.getPictureData();
                viewer.displayBillboard(billboardXML, pictureData);
            }
        } catch (IOException e) {
            System.out.println("Can't connect to server...");
            viewer.displaySpecialMessage("Error: Cannot connect to server. Trying again now..."); // Error in receiving content
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * This function communicates with the server to get the DbBillboard object of the current billboard to be
     * displayed.
     * @return Returns an Object which is a DbBillboard object of the current billboard to be displayed.
     * @throws IOException Throws an exception if an I/O exception of some sort has occurred.
     * @throws ClassNotFoundException Throws an exception when a specified class cannot be found in the classpath.
     */
    public static Object activeBillboardRequest() throws IOException, ClassNotFoundException {
        String message = String.format("Viewer");
        return Helpers.initClient(message);
    }


    /**
     * Main entry point for the Viewer program
     * Schedules threads for a period of 15 seconds to continue probing server for latest billboard.
     * @param args Command line arguments (not required)
     */
    public static void main(String[] args) {
        Main viewerMain = new Main();
        viewer = new Viewer();
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(viewerMain, 0, 15, TimeUnit.SECONDS);
    }

}
