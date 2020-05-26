package server;
import java.io.File;
import java.io.Serializable;

public class CpBillboard implements Serializable {

    // Set Fields for DbBillboard
    private String billboardName;
    private String creator;
    private String XMLCode;
    private File image;

    // Set Constructor for DbBillboard
    public CpBillboard(String billboardName, String creator, String XMLCode, File image) {
        this.billboardName = billboardName;
        this.creator = creator;
        this.XMLCode = XMLCode;
        this.image = image; // server success or fail message
    }

    // Set Getters for DbBillboard
    public String getBillboardName() {
        return billboardName;
    }

    public String getCreator() {
        return creator;
    }

    public String getXMLCode() {
        return XMLCode;
    }

    public File getImage() {
        return image;
    }

    // Set Setters for DbBillboard
    public void setBillboardName(String billboardName) {
        this.billboardName = billboardName;
    }

    public void setName(String creator) {
        this.creator = creator;
    }

    public void setXMLCode(String XMLCode) {
        this.XMLCode = XMLCode;
    }

    public void setImage(File image) {
        this.image = image;
    }
}
