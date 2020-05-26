package controlPanel;

import helpers.Helpers;
import server.CpBillboard;

import java.io.File;
import java.io.IOException;

public class BillboardControl
{

    /**
     * Send Queries: Billboard. This is a generic method which sends a create billboard request
     * from control panel to server.
     * <p>
     * This method always returns immediately.
     * @param  sessionToken A sessionToken generated when logged in
     * @param  billboardName A String which provides Billboard Name to store into database
     * @param  XMLCode A String which provides xmlCode to store into database
     * @return ServerAcknowledge TODO: Refactor BillboardAdmin/ScheduleAdmin to use ServerAcknowledge Return type
     */
    public static String createBillboardRequest(String sessionToken, String billboardName, String creator,
                                                String imageFilePointer, String XMLCode) throws IOException, ClassNotFoundException {
        //CpBillboard billboard = new CpBillboard(billboardName, creator, XMLCode, imageFilePointer);
        //String message = String.format("Billboard,CreateBillboard,%s,%s,%s,%s,%s", sessionToken,billboardName,creator,XMLCode,imageFilePointer);
        String message = String.format("Billboard,CreateBillboard,%s,%s,%s,%s,%s", sessionToken,billboardName,creator,imageFilePointer,XMLCode);
        return (String) Helpers.initClient(message); // Send constructed method request and parameters to the server
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
    public static String deleteBillboardRequest(String sessionToken,
                                       String billboardName, String requestor) throws IOException, ClassNotFoundException {
        String message = String.format("Billboard,DeleteBillboard,%s,%s, %s",
                sessionToken,
                billboardName,
                requestor);
        return (String) Helpers.initClient(message); // Send constructed method request and parameters to the server
    }

    /**
     * Send Queries: Billboard. This is a generic method which sends a request to Delete all billboards
     * from control panel to server.
     * <p>
     * This method always returns immediately.
     * @param  sessionToken A sessionToken generated when logged in
     * @return
     */
    public static String deleteAllBillboardRequest(String sessionToken) throws IOException, ClassNotFoundException {
        String message = String.format("Billboard,DeleteAllBillboard,%s",
                sessionToken);
        return (String) Helpers.initClient(message); // Send constructed method request and parameters to the server
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
