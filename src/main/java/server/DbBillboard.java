package server;

public class DbBillboard {

    // Set Fields for DbBillboard
    private String BillboardName;
    private String Creator;
    private String XMLCode;

    public DbBillboard(String billboardName, String creator, String xmlCode) {
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
