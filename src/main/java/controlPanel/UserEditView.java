package controlPanel;

import controlPanel.Main.VIEW_TYPE;

import javax.swing.*;
import java.awt.event.MouseListener;
import java.util.ArrayList;

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
        setEditable(true);
        addSubmitButton();
    }

    protected void addSubmitButton()
    {
        submitButton = new JButton("Submit");
        JPanel navPanel = getNavPanel();
        navPanel.add(submitButton);
    }

    protected ArrayList<Object> getUserInfo()
    {
        ArrayList<Object> userInfoArray = new ArrayList<>();
        userInfoArray.add(usernameText.getText());
        userInfoArray.add(passwordText.getText());
        userInfoArray.add(editBBPermission.isSelected());
        userInfoArray.add(scheduleBBPermission.isSelected());
        userInfoArray.add(editUsersPermission.isSelected());
        userInfoArray.add(createBBPermission.isSelected());
        return userInfoArray;
    }

    public void addSubmitButtonListener(MouseListener listener)
    {
        submitButton.addMouseListener(listener);
    }

    @Override
    VIEW_TYPE getEnum() {
        return view_type;
    }

    @Override
    void cleanUp() {

    }
}
