package controlPanel;

import controlPanel.Main.VIEW_TYPE;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * View designed for viewing the personal profile of the current user.
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

    /**
     * Add edit button allowing user to edit their own profile
     */
    protected void addEditButton()
    {
        editButton = new JButton("Edit Profile");
        JPanel navPanel = getNavPanel();
        GridBagConstraints gbc = getNavGBCPanel();
        navPanel.add(editButton, setGBC(gbc,3,1,1,1));
    }

    /**
     * Add edit button listener
     */
    public void addEditButtonListener(MouseListener listener)
    {
        editButton.addMouseListener(listener);
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