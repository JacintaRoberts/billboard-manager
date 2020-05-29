package controlPanel;

import controlPanel.Main.VIEW_TYPE;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * View designed for viewing users in the database.
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
