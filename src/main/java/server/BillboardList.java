package server;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * The BillboardList is a class which is used as an serializable object to send billboard list in the database.
 */


public class BillboardList implements Serializable {
    private String serverResponse;
    private ArrayList<String> Billboard;

    // Set Constructor for DbBillboard
    public BillboardList(String serverResponse, ArrayList<String> Billboard) {
        this.serverResponse = serverResponse;
        this.Billboard = Billboard;
    }

    // Set Getters for DbBillboard
    public ArrayList<String> getBillboardNames() {
        return Billboard;
    }

    public String getServerResponse() {
        return serverResponse;
    }

}
