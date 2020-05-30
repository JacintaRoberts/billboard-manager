package controlPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;

/**
 * User list view which displays list of all users in the database
 */
public class UserListView extends AbstractListView
{
    // *** VARIABLES**
    // --- ENUM ---
    private Main.VIEW_TYPE view_type;
    // --- button ---
    private JButton userMenuButton;

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
