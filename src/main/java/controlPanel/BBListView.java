package controlPanel;

import controlPanel.Main.VIEW_TYPE;
import server.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;

import static server.Server.ServerAcknowledge.*;

/**
 *  List of Billboards allowing user with valid permissions to Edit, Delete or View billboards. This extends the
 *  AbstractListView and is customised to display Billboard data.
 */
public class BBListView extends AbstractListView
{
    // *** VARIABLES**
    // -- enum --
    private VIEW_TYPE view_type;
    // -- button --
    private JButton BBMenuButton;

    /**
     * Constructor to set up JFrame with provided name and create GUI components
     * Set ENUM value allowing use in Controller Class
     */
    public BBListView()
    {
        super("Billboards List");
        view_type = VIEW_TYPE.BB_LIST;
        setListTitle("BILLBOARD LIST");
        addBBMenuButton();
    }

    private void addBBMenuButton()
    {
        BBMenuButton = new JButton("Billboard Menu");
        JPanel navPanel = getNavPanel();
        GridBagConstraints gbc = getNavGBCPanel();
        navPanel.add(BBMenuButton, setGBC(gbc, 2,1,1,1));
    }

    /**
     * Get Enum associated to this View. This is defined in the Constructor and is used in the Controller Class.
     * @return view type enum assigned to view
     */
    @Override
    Main.VIEW_TYPE getEnum()
    {
        return view_type;
    }

    /**
     * Show BB Deleted message based on server response
     * @param serverResponse response from server after trying to delete
     */
    protected void showBBDeletedMessage(Server.ServerAcknowledge serverResponse)
    {
        String message = "";
        if ( serverResponse.equals(Success) ) {
            message = "Billboard Successfully deleted";
        }  else if ( serverResponse.equals(InvalidToken) ) {
            message = "Invalid Session Token";
        } else if ( serverResponse.equals(InsufficientPermission) ) {
            message = "Not enough Permission";
        } else if ( serverResponse.equals(BillboardNotExists) ) {
            message = "Billboard Does not Exists";
        }
        JOptionPane.showMessageDialog(null, message);
    }

    /**
     * Add listener to navigate to BB Menu View
     * @param listener mouse listener
     */
    protected void addBBMenuListener(MouseListener listener)
    {
        BBMenuButton.addMouseListener(listener);
    }
}
