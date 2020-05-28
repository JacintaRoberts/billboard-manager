package controlPanel;

import observer.Subject;

import controlPanel.Main.VIEW_TYPE;
import server.Server;

import javax.swing.*;
import java.awt.event.MouseListener;

import static server.Server.ServerAcknowledge.*;
import static server.Server.ServerAcknowledge.NoSuchUser;

public class BBListView extends AbstractListView
{
    // *** VARIABLES**
    // -- enum --
    private VIEW_TYPE view_type;

    /**
     * Constructor for creating the Views of the application. The constructor sets the frame's name and set's up the
     * View by defining Width and Height, default close operation and the Layout.
     */
    public BBListView()
    {
        super("Billboards List");
        view_type = VIEW_TYPE.BB_LIST;
        setListTitle("BILLBOARD LIST");
    }


    @Override
    public void update(Subject s)
    {

    }

    @Override
    Main.VIEW_TYPE getEnum()
    {
        return view_type;
    }

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
}
