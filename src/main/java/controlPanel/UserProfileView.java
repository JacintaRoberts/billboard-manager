package controlPanel;

import controlPanel.Main.VIEW_TYPE;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * User Profile View allows the current user to view personal information. This is a read-only view of the profile
 * information and extends the AbstractUserView.
 */
public class UserProfileView extends AbstractUserView
{
    // *** DECLARE VARIABLES**
    // --- ENUM ---
    private VIEW_TYPE view_type;
    // --- Buttons ---
    private JButton editButton;

    /**
     * Constructor to set up JFrame with provided name and create GUI components
     * Set ENUM value allowing use in Controller Class
     */
    public UserProfileView()
    {
        super("Profile View");
        // set enum of frame
        view_type = VIEW_TYPE.USER_PROFILE;
        // set user fields to uneditable
        setUserFieldsEditable(false);
        // ensure username text is not editable
        usernameText.setEditable(false);
        // add edit button
        addEditButton();
        // set frame title to profile
        title.setText("PROFILE");
    }

    /**
     * Add edit button allowing user to edit their own profile
     */
    protected void addEditButton()
    {
        // create edit button
        editButton = new JButton("Edit Profile");
        // get nav panel and gbc
        JPanel navPanel = getNavPanel();
        GridBagConstraints gbc = getNavGBCPanel();
        // add edit button to nav panel
        navPanel.add(editButton, setGBC(gbc,3,1,1,1));
    }

    /**
     * Add edit button listener
     * @param listener listener
     */
    public void addEditButtonListener(MouseListener listener)
    {
        editButton.addMouseListener(listener);
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
        // reset username and permissions
        setUsername("");
        setPermissions(new ArrayList<>(Arrays.asList(false,false,false,false)));
    }
}