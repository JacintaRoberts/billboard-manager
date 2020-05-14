package controlPanel;

import controlPanel.Main.VIEW_TYPE;

/**
 * View designed for editing users.
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
        setEditable();
    }

    @Override
    void setEditable()
    {
        passwordText.setEditable(false);
        usernameText.setEditable(false);
        editBBPermission.setEnabled(false);
        editSchedulePermission.setEnabled(false);
        editUsersPermission.setEnabled(false);
    }

    @Override
    VIEW_TYPE getEnum() {
        return view_type;
    }

    @Override
    void cleanUp() {

    }
}
