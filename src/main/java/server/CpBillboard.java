package server;
import java.io.File;
import java.io.Serializable;

/**
 * The CpBillboard is a serializable class which allows Control Panel objects to be sent into the server for storage
 */


public class CpBillboard implements Serializable {

    // Set Fields for CpBillboard
    private String message;
    private String billboardName;
    private String creator;
    private String XMLCode;
    private byte[] pictureData;

    // Set Constructor for DbBillboard
    public CpBillboard(String sessionToken, String billboardName, String creator, String XMLCode, byte[] pictureData) {
        // Construct message to be sent to server
        String message = String.format("Billboard,CreateBillboard,%s,%s,%s,%s", sessionToken, billboardName, creator, XMLCode);
        this.message = message;
        this.billboardName = billboardName;
        this.creator = creator;
        this.XMLCode = XMLCode;
        this.pictureData = pictureData;
    }

    // Set Getters for CpBillboard
    public String getMessage() {
        return message;
    }

    public String getBillboardName() { return billboardName; }

    public String getCreator() {
        return creator;
    }

    public String getXMLCode() {
        return XMLCode;
    }

    public byte[] getPictureData() { return pictureData; }

    // Set Setters for CpBillboard
    public void setMessage(String message) {
        this.message = message;
    }

    public void setBillboardName(String billboardName) {
        this.billboardName = billboardName;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setXMLCode(String XMLCode) {
        this.XMLCode = XMLCode;
    }

    public void setPictureData(byte[] pictureData) { this.pictureData = pictureData; }
}
