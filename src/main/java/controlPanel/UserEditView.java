package controlPanel;

import controlPanel.Main.VIEW_TYPE;

import javax.swing.*;

/**
 * View designed for editing users.
 */
public class UserEditView extends AbstractUserView
{
    // *** DECLARE VARIABLES**
    // --- ENUM ---
    private VIEW_TYPE view_type;
    // --- Buttons ---
    private JButton submitButton;


    public UserEditView()
    {
        super("Edit User");
        view_type = VIEW_TYPE.USER_EDIT;
        setEditable();
        addSubmitButton();
    }

    protected void addSubmitButton()
    {
//        JPanel userDetailsPanel = getUserDetailsPanel();
//        submitButton = new JButton("Submit");
//        cancelButton = new JButton("Cancel");
//        userDetailsPanel.add(submitButton);
//        userDetailsPanel.add(cancelButton);

        submitButton = new JButton("Submit Changes");
        JPanel navPanel = getNavPanel();
        navPanel.add(submitButton);
    }

    protected void setEditable()
    {
        passwordText.setEditable(true);
        usernameText.setEditable(true);
        editBBPermission.setEnabled(true);
        editSchedulePermission.setEnabled(true);
        editUsersPermission.setEnabled(true);
    }


//    @Override
//    void addUserPermissions()
//    {
//
//    }

    @Override
    VIEW_TYPE getEnum() {
        return view_type;
    }

    @Override
    void cleanUp() {

    }
}
