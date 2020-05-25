package controlPanel;

import controlPanel.Main.VIEW_TYPE;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;

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
        usernameText.setEditable(false);
        addEditButton();
        title.setText("PROFILE");
    }

    protected void addEditButton()
    {
        editButton = new JButton("Edit Profile");
        JPanel navPanel = getNavPanel();
        GridBagConstraints gbc = getNavGBCPanel();
        navPanel.add(editButton, setGBC(gbc,3,1,1,1));
    }

    public void addEditButtonListener(MouseListener listener)
    {
        editButton.addMouseListener(listener);
    }

    @Override
    VIEW_TYPE getEnum() {
        return view_type;
    }


}