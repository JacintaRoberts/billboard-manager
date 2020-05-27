package server;

import java.io.Serializable;

public class DbBillboard implements Serializable {

    // Set Fields for DbBillboard
    private String BillboardName;
    private String Creator;
    private byte[] PictureData;
    private String XMLCode;

    // Set Constructor for DbBillboard
    public DbBillboard(String BillboardName, String Creator, byte[] PictureData, String XMLCode) {
        this.BillboardName = BillboardName;
        this.Creator = Creator;
        this.PictureData = PictureData; // server success or fail message
        this.XMLCode = XMLCode;
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

    public byte[] getPictureData() {
        return PictureData;
    }





}
