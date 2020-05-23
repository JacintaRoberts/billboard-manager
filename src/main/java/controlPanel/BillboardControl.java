package controlPanel;

import helpers.Helpers;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static helpers.Helpers.bytesToString;
import java.io.IOException;
import java.sql.SQLException;

public class BillboardControl
{

    /**
     * Send Queries: Billboard. This is a generic method which sends a create billboard request
     * from control panel to server.
     * <p>
     * This method always returns immediately.
     * @param  sessionToken A sessionToken generated when logged in
     * @param  billboardName A String which provides Billboard Name to store into database
     * @param  xmlCode A String which provides xmlCode to store into database
     * @return
     */
    public static String createBillboardRequest(String sessionToken,
                                                String billboardName,
                                                String xmlCode) throws IOException, ClassNotFoundException {
    String message = String.format("Billboard,CreateBillboard,%s,%s,%s",
            sessionToken,
            billboardName,
            xmlCode);
    System.out.println(message);
    return (String) Helpers.initClient(message); // Send constructed method request and parameters to the server
    }

    /**
     * Send Queries: Billboard. This is a generic method which sends a request to edit a billboard
     * from control panel to server.
     * <p>
     * This method always returns immediately.
     * @param  sessionToken A sessionToken generated when logged in
     * @param  billboardName A String which provides Billboard Name to store into database
     * @param  xmlCode A String which provides xmlCode to store into database
     * @return
     */
    public String editBillboardRequest(String sessionToken,
                                         String billboardName,
                                         String xmlCode) throws IOException, ClassNotFoundException {
        String message = String.format("Billboard,EditBillboard,%s,%s,%s",
                sessionToken,
                billboardName,
                xmlCode);
        return (String) Helpers.initClient(message); // Send constructed method request and parameters to the server

    }


    /**
     * Send Queries: Billboard. This is a generic method which sends a request to Delete a billboard
     * from control panel to server.
     * <p>
     * This method always returns immediately.
     * @param  sessionToken A sessionToken generated when logged in
     * @param  billboardName A String which provides Billboard Name to store into database
     * @return
     */
    public String deleteBillboardRequest(String sessionToken,
                                       String billboardName) throws IOException, ClassNotFoundException {
        String message = String.format("Billboard,DeleteBillboard,%s,%s",
                sessionToken,
                billboardName);
        return (String) Helpers.initClient(message); // Send constructed method request and parameters to the server

    }


    /**
     * Send Queries: Billboard. This is a generic method which sends a request to List billboards
     * from control panel to server.
     * <p>
     * This method always returns immediately.
     * @param  sessionToken A sessionToken generated when logged in
     * @return
     * // TODO: CHECK RETURN
     */
    public String listBillboardRequest(String sessionToken) throws IOException, ClassNotFoundException {
        String message = String.format("Billboard,ListBillboard,%s",
                sessionToken);
        return (String) Helpers.initClient(message); // Send constructed method request and parameters to the server
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
    public String getBillboardRequest(String sessionToken,
                                      String billboardName) throws IOException, ClassNotFoundException {
        String message = String.format("Billboard,GetBillboard,%s,%s",
                sessionToken,
                billboardName);
        return (String) Helpers.initClient(message); // Send constructed method request and parameters to the server
    }


}
