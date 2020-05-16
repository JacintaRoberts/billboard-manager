package controlPanel;

import controlPanel.Main.VIEW_TYPE;

import javax.swing.*;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * View designed for editing users.
 */
public class UserProfileView extends AbstractUserView
{
    // *** DECLARE VARIABLES**
    // --- ENUM ---
    private VIEW_TYPE view_type;
    // --- Buttons ---
    private JButton editButton;


    public UserProfileView()
    {
        super("Profile View");
        view_type = VIEW_TYPE.USER_PROFILE;
        setEditable(false);
        addEditButton();
    }

    protected void addEditButton()
    {
        editButton = new JButton("Edit Profile");
        JPanel navPanel = getNavPanel();
        navPanel.add(editButton);
    }

//    protected ArrayList<Object> getUserInfo()
//    {
//        ArrayList<Object> userInfoArray = new ArrayList<>();
//        userInfoArray.add(usernameText.getText());
//        userInfoArray.add(passwordText.getText());
//        userInfoArray.add(editBBPermission.isSelected());
//        userInfoArray.add(scheduleBBPermission.isSelected());
//        userInfoArray.add(editUsersPermission.isSelected());
//        return userInfoArray;
//    }


    public void addEditButtonListener(MouseListener listener)
    {
        editButton.addMouseListener(listener);
    }

    @Override
    VIEW_TYPE getEnum() {
        return view_type;
    }

    @Override
    void cleanUp() {

    }
}