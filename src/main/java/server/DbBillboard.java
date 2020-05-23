package server;

import java.io.IOException;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DbBillboard implements Serializable {

    // Set Fields for DbBillboard
    private String BillboardName;
    private String Creator;
    private String XMLCode;
    private String returnString;

    // Set Constructor for DbBillboard
    public DbBillboard(String BillboardName, String Creator, String XMLCode, String returnString) {
        this.BillboardName = BillboardName;
        this.Creator = Creator;
        this.XMLCode = XMLCode;
        this.returnString = returnString;
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

    public String getReturnString() {
        return returnString;
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


    public void setReturnString(String returnString) {
        this.returnString = returnString;
    }




}
