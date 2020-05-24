package controlPanel;

import controlPanel.Main.VIEW_TYPE;
import observer.Subject;
import viewer.Viewer;

import javax.swing.*;
import org.w3c.dom.Document;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import static viewer.Viewer.*;

public class BBPreviewView extends AbstractView
{
    // *** VARIABLES**
    // --- Panels ---
    private JPanel mainPanel;
    // --- ENUM ---
    private VIEW_TYPE view_type;
    // --- Labels ---
    private JLabel messageLabel;
    private JLabel informationLabel;
    private JLabel pictureLabel;

    /**
     * Constructor for creating the Views of the application. The constructor sets the frame's name and set's up the
     * View by defining Width and Height, default close operation and the Layout.
     */
    public BBPreviewView() {
        super("Billboard Preview View");
        this.view_type = VIEW_TYPE.BB_PREVIEW;
        createComponents();
    }

    private void createComponents()
    {
        Viewer viewer = new Viewer();
        // FIXME: This will be a string which is an xml file
        viewer.displayBillboard("xml");
    }

    protected VIEW_TYPE getEnum()
    {
        return view_type;
    }

    @Override
    void cleanUp() {

    }

    protected void addBBXML(ArrayList<Object> xml)
    {
        Document document = null;

        // Extract the billboard data using input file
        HashMap<String, String> billboardDataServer = extractDataFromXML(document);

//        formatBillboard(billboardDataServer);
//        listenEscapeKey();
//        listenMouseClick();
//        showViewer();
    }

    @Override
    public void update(Subject s) {

    }
}
