package server;

import java.io.Serializable;

public class DbBillboard implements Serializable {

    // Set Fields for DbBillboard
    private String BillboardName;
    private String Creator;
    private byte[] PictureData;
    private String XMLCode;
    private String serverResponse;

    // Set Constructor for DbBillboard
    public DbBillboard(String BillboardName, String Creator, byte[] PictureData, String XMLCode, String serverResponse) {
        this.BillboardName = BillboardName;
        this.Creator = Creator;
        this.PictureData = PictureData; // server success or fail message
        this.XMLCode = XMLCode;
        this.serverResponse = serverResponse;
    }


    // Set Getters for DbBillboard
    public String getBillboardName() {
        return BillboardName;
    }
    public String getCreator() {
        return Creator;
    }
    public String getXMLCode() {
        return XMLCode;
    }
    public String getServerResponse() {
        return serverResponse;
    }

    public byte[] getPictureData() {
        return PictureData;
    }





}
