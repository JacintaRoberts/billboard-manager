package controlPanel;

import observer.Subject;

import javax.swing.*;
import java.awt.*;

public class UsersView extends ControlPanelView
{
    // *** VARIABLES**
    // --- Panels ---
    private JPanel optionsPanel;
    // --- Buttons ---
    private JButton viewUsersButton;
    private JButton createUsersButton;

    /**
     * Constructor to create home view, use parent constructor.
     */
    public UsersView()
    {
        super("Users View");
    }

    @Override
    void createComponents() {
        optionsPanel = new JPanel();
        optionsPanel.setLayout(new FlowLayout());
        viewUsersButton = new JButton("View Users");
        createUsersButton = new JButton("Create Users");
        optionsPanel.add(viewUsersButton);
        optionsPanel.add(createUsersButton);
        getContentPane().add(optionsPanel, BorderLayout.CENTER);

        addProfilePanel();
        addHomePanel();
    }

    @Override
    public void update(Subject s)
    {

    }
}
