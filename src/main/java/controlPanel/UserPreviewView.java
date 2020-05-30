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

    public UserPreviewView()
    {
        super("Preview User");
        view_type = VIEW_TYPE.USER_VIEW;
        setEditable(false);
        usernameText.setEditable(false);
        title.setText("VIEW USER");
    }

    @Override
    VIEW_TYPE getEnum() {
        return view_type;
    }

    @Override
    void cleanUp()
    {
        setUsername("");
        setPermissions(new ArrayList<>(Arrays.asList(false,false,false,false)));
    }
}
