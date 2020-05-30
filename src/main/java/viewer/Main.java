package viewer;


import controlPanel.BillboardControl;
import helpers.Helpers;
import server.DbBillboard;
import server.ScheduleAdmin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main implements Runnable {

    // Contains the server's response (Billboard XML) as a string
    private String billboardXML;
    private byte[] pictureData;
    private static Viewer viewer; // Single instance of the viewer class to prevent multiple windows

    @Override
    public void run() {
        try {
            DbBillboard billboardObject = null;
            billboardObject = (DbBillboard) activebillboardRequest();
            if (billboardObject == null) {
                System.out.println("No billboard...");
                viewer.displaySpecialMessage("There are no billboards to display right now."); // Show no billboard screen
            } else {
                System.out.println("Attempting to display billboard...");
                String billboardXML = billboardObject.getXMLCode();
                byte[] pictureData = billboardObject.getPictureData();
                viewer.displayBillboard(billboardXML, pictureData);
            }
        } catch (IOException e) {
            viewer.displaySpecialMessage("Error: Cannot connect to server. Trying again now..."); // Error in receiving content
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
//        // Get current billboard from schedule and display
//        try {
//            billboardXML = ScheduleAdmin.getCurrentBillboardXML();
//            pictureData = ScheduleAdmin.getCurrentBillboardPictureData();
//            System.out.println("XML received from server: " + billboardXML);
//            //System.out.println("Picture data received from server: " + pictureData);
//            if (!billboardXML.equals("") || billboardXML.isEmpty()) {
//                System.out.println("No billboard...");
//                viewer.displaySpecialMessage("There are no billboards to display right now."); // Show no billboard screen
//            } else {
//                System.out.println("Attempting to display billboard...");
//                viewer.displayBillboard(billboardXML, pictureData);
//            }
//        } catch (IOException | SQLException e) {
//            viewer.displaySpecialMessage("Error: Cannot connect to server. Trying again now..."); // Error in receiving content
//        } catch (Exception e) {
//            e.printStackTrace(); // Something else occurred
//        }
    }

    public static Object activebillboardRequest() throws IOException, ClassNotFoundException {
        String message = String.format("Viewer");
        return Helpers.initClient(message);
    }

    public static void main(String[] args) {
        Main viewerMain = new Main();
        viewer = new Viewer();
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(viewerMain, 0, 15, TimeUnit.SECONDS);
    }

}
