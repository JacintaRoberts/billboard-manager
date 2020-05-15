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
     * Send Queries: Billboard. This is a generic method which sends a request from control panel to server.
     * <p>
     * This method always returns immediately.
     * @param  billboardName A String which provides Billboard Name to store into database
     * @param  xmlCode A String which provides xmlCode to store into database
     * @return
     */
    public String createBillboardRequest(String sessionToken,
                                              String billboardName,
                                              String xmlCode) throws IOException, ClassNotFoundException {
    String message = String.format("CreateBillboard,%s,%s,%s",
            sessionToken,
            billboardName,
            xmlCode);
    return (String) Helpers.initClient(message); // Send constructed method request and parameters to the server

}

}
