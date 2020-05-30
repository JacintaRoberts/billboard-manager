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
        view_type = Main.VIEW_TYPE.USER_LIST;
        setListTitle("USER LIST");
        addUserMenuButton();
    }

    private void addUserMenuButton()
    {
        userMenuButton = new JButton("User Menu");
        JPanel navPanel = getNavPanel();
        GridBagConstraints gbc = getNavGBCPanel();
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
