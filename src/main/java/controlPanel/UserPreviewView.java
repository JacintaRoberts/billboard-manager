package controlPanel;

import controlPanel.Main.VIEW_TYPE;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * User Preview View displays a read only view of user information including the username and permissions selected.
 */
public class UserPreviewView extends AbstractUserView
{
    // *** DECLARE VARIABLES**
    // --- ENUM ---
    private VIEW_TYPE view_type;

    /**
     * Constructor to set up JFrame with provided name and create GUI components
     * Set ENUM value allowing use in Controller Class
     */
    public UserPreviewView()
    {
        super("Preview User");
        view_type = VIEW_TYPE.USER_VIEW;
        setEditable(false);
        usernameText.setEditable(false);
        title.setText("VIEW USER");
    }

    /**
     * Get Enum associated to this View. This is defined in the Constructor and is used in the Controller Class.
     * @return view type enum assigned to view
     */
    @Override
    VIEW_TYPE getEnum() {
        return view_type;
    }

    /**
     * Clean Up all data that should not persist in the GUI. The view will be cleaned up after leaving the view.
     */
    @Override
    void cleanUp()
    {
        setUsername("");
        setPermissions(new ArrayList<>(Arrays.asList(false,false,false,false)));
    }
}
