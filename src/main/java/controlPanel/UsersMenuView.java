package controlPanel;

import observer.Subject;

import controlPanel.Main.VIEW_TYPE;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;

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

    private GridBagConstraints gbc;

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

        gbc.insets = new Insets(5,10,5,10);
        optionsPanel.add(viewUsersButton, setGBC(gbc, 1,1,1,1));
        optionsPanel.add(createUsersButton, setGBC(gbc, 2,1,1,1));

        getContentPane().add(optionsPanel, BorderLayout.CENTER);
    }

    @Override
    void cleanUp() {

    }

    @Override
    public void update(Subject s)
    {

    }

    @Override
    VIEW_TYPE getEnum()
    {
        return view_type;
    }

    protected void addListUserButtonListener(MouseListener listener) {viewUsersButton.addMouseListener(listener);}
}
