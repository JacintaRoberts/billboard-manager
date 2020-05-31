package controlPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;

/**
 * User List View displays a list of all users in the database displaying buttons to Edit, Delete or View the user.
 * This extends the AbstractListView and is customised to display User accounts.
 */
public class UserListView extends AbstractListView
{
    // *** VARIABLES**
    // --- ENUM ---
    private Main.VIEW_TYPE view_type;
    // --- button ---
    private JButton userMenuButton;

    /**
     * Constructor to set up JFrame with provided name and create GUI components
     * Set ENUM value allowing use in Controller Class
     */
    public UserListView()
    {
        super("User List");
        // set enum value
        view_type = Main.VIEW_TYPE.USER_LIST;
        // set list title
        setListTitle("USER LIST");
        // add user menu button
        addUserMenuButton();
    }

    /**
     * Add user menu button to nav panel
     */
    private void addUserMenuButton()
    {
        // create user menu button
        userMenuButton = new JButton("User Menu");
        // get nav panel and gbc
        JPanel navPanel = getNavPanel();
        GridBagConstraints gbc = getNavGBCPanel();
        // add button to nav panel
        navPanel.add(userMenuButton, setGBC(gbc, 2,1,1,1));
    }

    /**
     * Get Enum associated to this View. This is defined in the Constructor and is used in the Controller Class.
     * @return view type enum assigned to view
     */
    @Override
    Main.VIEW_TYPE getEnum() {
        return view_type;
    }

    /**
     * Add listener to navigate to user Menu View
     * @param listener mouse listener
     */
    protected void addUserMenuButton(MouseListener listener)
    {
        userMenuButton.addMouseListener(listener);
    }
}
