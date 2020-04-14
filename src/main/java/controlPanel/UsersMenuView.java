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
        optionsPanel = new JPanel();
        optionsPanel.setLayout(new FlowLayout());
        viewUsersButton = new JButton("View Users");
        createUsersButton = new JButton("Create Users");
        optionsPanel.add(viewUsersButton);
        optionsPanel.add(createUsersButton);
        getContentPane().add(optionsPanel, BorderLayout.CENTER);
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
