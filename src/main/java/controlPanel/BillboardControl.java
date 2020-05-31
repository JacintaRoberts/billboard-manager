package controlPanel;

import helpers.Helpers;
import server.CpBillboard;
import server.Server;
import static server.Server.*;

import java.io.IOException;

/**
 * The BillboardControl contains on the communication methods between the Control Panel to the server for Billboard Methods. It includes methods
 * which will use functions from helpers that initialises communication and returns relevant object from server or server acknowledgements,
 * such as creating, editing, removing Billboards.
 */

public class BillboardControl
{

    /**
     * Send Queries: Billboard. This is a generic method which sends a create billboard request
     * from control panel to server.
     * <p>
     * This method always returns immediately.
     * @param  sessionToken A String which is used to check if the user are allowed to be interacting with the app. Generated
     *                      when the user first logins
     * @param  billboardName A String which provides Billboard Name to store into database
     * @param  creator A string which notes the user who created the billbaord
     * @param  XMLCode A String which provides xmlCode to store into database
     * @param  pictureData A Byte array which is base64 encoded represenation of the actual picture
     * @throws IOException Throws an exception if an I/O exception of some sort has occurred.
     * @throws ClassNotFoundException Throws an exception when a specified class cannot be found in the classpath.
     * @return ServerAcknowledge A enum which indicates pass/fail status which is used to assist in the GUI actions.
     */
    public static ServerAcknowledge createBillboardRequest(String sessionToken, String billboardName, String creator,
                                                           String XMLCode, byte[] pictureData) throws IOException, ClassNotFoundException {
        CpBillboard cpBillboard = new CpBillboard(sessionToken, billboardName, creator, XMLCode, pictureData);
        return (ServerAcknowledge) Helpers.initClient(cpBillboard); // Send constructed method request and parameters to the server
    }


    /**
     * Send Queries: Billboard. This is a generic method which sends a request to Delete a billboard
     * from control panel to server.
     * <p>
     * This method always returns immediately.
     * @param  sessionToken A String which is used to check if the user are allowed to be interacting with the app. Generated
     *                      when the user first logins
     * @param  billboardName A String which provides Billboard Name to delete from database
     * @param  requestor A string which provides the username whom wants to delete data from the database
     * @throws IOException Throws an exception if an I/O exception of some sort has occurred.
     * @throws ClassNotFoundException Throws an exception when a specified class cannot be found in the classpath.
     * @return ServerAcknowledge A enum which indicates pass/fail status which is used to assist in the GUI actions.
     */
    public static ServerAcknowledge deleteBillboardRequest(String sessionToken,
                                       String billboardName, String requestor) throws IOException, ClassNotFoundException {
        String message = String.format("Billboard,DeleteBillboard,%s,%s, %s",
                sessionToken,
                billboardName,
                requestor);
        return (ServerAcknowledge) Helpers.initClient(message); // Send constructed method request and parameters to the server
    }

    /**
     * Send Queries: Billboard. This is a generic method which sends a request to Delete all billboards
     * from control panel to server.
     * <p>
     * This method always returns immediately.
     * @param  sessionToken A String which is used to check if the user are allowed to be interacting with the app. Generated
     *                      when the user first logins
     * @throws IOException Throws an exception if an I/O exception of some sort has occurred.
     * @throws ClassNotFoundException Throws an exception when a specified class cannot be found in the classpath.
     * @return ServerAcknowledge A enum which indicates pass/fail status which is used to assist in the GUI actions.
     */
    public static ServerAcknowledge deleteAllBillboardRequest(String sessionToken) throws IOException, ClassNotFoundException {
        String message = String.format("Billboard,DeleteAllBillboard,%s",
                sessionToken);
        return (ServerAcknowledge) Helpers.initClient(message); // Send constructed method request and parameters to the server
    }



    /**
     * Send Queries: Billboard. This is a generic method which sends a request to List billboards
     * from control panel to server.
     * <p>
     * This method always returns immediately.
     * @param  sessionToken A String which is used to check if the user are allowed to be interacting with the app. Generated
     *                      when the user first logins
     * @throws IOException Throws an exception if an I/O exception of some sort has occurred.
     * @throws ClassNotFoundException Throws an exception when a specified class cannot be found in the classpath.
     * @return Returns an Object file via serialised from server which can be casted into BillboardList for a list of billboards
     */
    public static Object listBillboardRequest(String sessionToken) throws IOException, ClassNotFoundException {
        String message = String.format("Billboard,ListBillboard,%s",
                sessionToken);
        return Helpers.initClient(message); // Send constructed method request and parameters to the server
    }


    /**
     * Send Queries: Billboard. This is a generic method which sends a request to get a billboard information
     * from control panel to server.
     * <p>
     * This method always returns immediately.
     * @param  sessionToken A String which is used to check if the user are allowed to be interacting with the app. Generated
     *                      when the user first logins
     * @param  billboardName A String which provides Billboard Name to store into database
     * @throws IOException Throws an exception if an I/O exception of some sort has occurred.
     * @throws ClassNotFoundException Throws an exception when a specified class cannot be found in the classpath.
     * @return Returns an object file from server via serialisation which can be cast into DbBillboard which contains all information
     *         about the quiried billboard.
     */
    public static Object getBillboardRequest(String sessionToken,
                                           String billboardName) throws IOException, ClassNotFoundException {
        String message = String.format("Billboard,GetBillboard,%s,%s",
                sessionToken,
                billboardName);
        return Helpers.initClient(message); // Send constructed method request and parameters to the server
    }


}
