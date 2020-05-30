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
     * Constructor to create home view, use parent constructor.
     */
    public UsersMenuView()
    {
        super("Users View");
        view_type = VIEW_TYPE.USERS_MENU;
    }

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

    @Override
    void cleanUp() {

    }

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
