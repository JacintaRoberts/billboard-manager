package server;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DbBillboard {

    // Set Fields for DbBillboard
    private String BillboardName;
    private String Creator;
    private String XMLCode;

    // Set Constructor for DbBillboard
    public DbBillboard(String BillboardName, String Creator, String XMLCode) {
        this.BillboardName = BillboardName;
        this.Creator = Creator;
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



    // Set Setters for DbBillboard
    public void setBillboardName(String BillboardName) {
        this.BillboardName = BillboardName;
    }

    public void setName(String Creator) {
        this.Creator = Creator;
    }

    public void setXMLCode(String XMLCode) {
        this.XMLCode = XMLCode;
    }







}
