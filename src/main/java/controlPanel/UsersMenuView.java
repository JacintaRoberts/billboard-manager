package controlPanel;

import controlPanel.Main.VIEW_TYPE;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;

/**
 * Menu View to allow the users to navigate to the user list view create user view.
 */
public class UsersMenuView extends AbstractGenericView
{
    // *** VARIABLES**
    // --- Panels ---
    private JPanel optionsPanel;
    // --- Buttons ---
    private JButton viewUsersButton;
    private JButton createUsersButton;
    // --- ENUM ---
    private VIEW_TYPE view_type;
    // --- GBC ---
    private GridBagConstraints gbc;
    // --- Labels ---
    private JLabel title;

    /**
     * Constructor to set up JFrame with provided name and create GUI components
     * Set ENUM value allowing use in Controller Class
     */
    public UsersMenuView()
    {
        super("Users View");
        view_type = VIEW_TYPE.USERS_MENU;
    }

    /**
     * Create View Components which include panels, buttons, text etc. These components make up the view/JFrame seen
     * by the user.
     */
    @Override
    void createComponents()
    {
        gbc = new GridBagConstraints();
        optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridBagLayout());
        viewUsersButton = new JButton("View Users");
        createUsersButton = new JButton("Create User");
        title = new JLabel("USER MENU");
        title.setForeground(Color.WHITE);
        title.setFont(title.getFont().deriveFont(60f));

        gbc.insets = new Insets(1,10,1,10);
        optionsPanel.add(viewUsersButton, setGBC(gbc, 1,1,1,1));
        optionsPanel.add(createUsersButton, setGBC(gbc, 3,1,1,1));
        gbc.insets = new Insets(250,1,1,1);
        optionsPanel.add(title, setGBC(gbc, 1,2,3,1));
        getContentPane().add(optionsPanel, BorderLayout.CENTER);
    }

    /**
     * Clean Up all data that should not persist in the GUI. The view will be cleaned up after leaving the view.
     */
    @Override
    void cleanUp() {

    }

    /**
     * Get Enum associated to this View. This is defined in the Constructor and is used in the Controller Class.
     * @return view type enum assigned to view
     */
    @Override
    VIEW_TYPE getEnum()
    {
        return view_type;
    }

    /**
     * Add list user button listener
     * @param listener listener
     */
    protected void addListUserButtonListener(MouseListener listener) {viewUsersButton.addMouseListener(listener);}

    /**
     * Add create user button listener
     * @param listener listener
     */
    protected void addCreateUserButtonListener(MouseListener listener) {createUsersButton.addMouseListener(listener);}
}
