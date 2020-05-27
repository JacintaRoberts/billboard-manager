package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DbBillboard implements Serializable {

    // Set Fields for DbBillboard
    private String BillboardName;
    private String Creator;
    private byte[] imageFilePointer;
    private String XMLCode;

    // Set Constructor for DbBillboard
    public DbBillboard(String BillboardName, String Creator, byte[] imageFilePointer, String XMLCode) {
        this.BillboardName = BillboardName;
        this.Creator = Creator;
        this.imageFilePointer = imageFilePointer; // server success or fail message
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
    public byte[] getImageFilePointer() {
        return imageFilePointer;
    }





}
