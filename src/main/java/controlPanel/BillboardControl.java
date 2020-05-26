package controlPanel;

import helpers.Helpers;
import server.CpBillboard;
import server.Server;
import static server.Server.*;

import java.io.IOException;

public class BillboardControl
{

    /**
     * Send Queries: Billboard. This is a generic method which sends a create billboard request
     * from control panel to server.
     * <p>
     * This method always returns immediately.
     * @param imageData A byte array containing the base64 encoded string for the picture data
     * @param  sessionToken A sessionToken generated when logged in
     * @param  billboardName A String which provides Billboard Name to store into database
     * @param  XMLCode A String which provides xmlCode to store into database
     * @param pictureData
     * @return ServerAcknowledge TODO: Refactor BillboardAdmin/ScheduleAdmin to use ServerAcknowledge Return type
     */
    public static ServerAcknowledge createBillboardRequest(String sessionToken, String billboardName, String creator,
                                                           String XMLCode, byte[] pictureData) throws IOException, ClassNotFoundException {
        CpBillboard cpBillboard = new CpBillboard(sessionToken, billboardName, creator, XMLCode, pictureData);
        return (ServerAcknowledge) Helpers.initClient(cpBillboard); // Send constructed method request and parameters to the server
    }

//    /**
//     * Send Queries: Billboard. This is a generic method which sends a request to edit a billboard
//     * from control panel to server.
//     * <p>
//     * This method always returns immediately.
//     * @param  sessionToken A sessionToken generated when logged in
//     * @param  billboardName A String which provides Billboard Name to store into database
//     * @param  xmlCode A String which provides xmlCode to store into database
//     * @return
//     */
//    public static String editBillboardRequest(String sessionToken,
//                                         String billboardName,
//                                         String xmlCode) throws IOException, ClassNotFoundException {
//        String message = String.format("Billboard,EditBillboard,%s,%s,%s",
//                sessionToken,
//                billboardName,
//                xmlCode);
//        return (String) Helpers.initClient(message); // Send constructed method request and parameters to the server
//
//    }


    /**
     * Send Queries: Billboard. This is a generic method which sends a request to Delete a billboard
     * from control panel to server.
     * <p>
     * This method always returns immediately.
     * @param  sessionToken A sessionToken generated when logged in
     * @param  billboardName A String which provides Billboard Name to store into database
     * @return
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
     * @param  sessionToken A sessionToken generated when logged in
     * @return
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
     * @param  sessionToken A sessionToken generated when logged in
     * @return
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
     * @param  sessionToken A sessionToken generated when logged in
     * @param  billboardName A String which provides Billboard Name to store into database
     * @return
     */
    public static Object getBillboardRequest(String sessionToken,
                                           String billboardName) throws IOException, ClassNotFoundException {
        String message = String.format("Billboard,GetBillboard,%s,%s",
                sessionToken,
                billboardName);
        return Helpers.initClient(message); // Send constructed method request and parameters to the server
    }


}
