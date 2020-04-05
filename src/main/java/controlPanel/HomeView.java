package controlPanel;

import observer.Subject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

public class HomeView extends ControlPanelView
{
    // *** VARIABLES**
    // --- Panels ---
    private JPanel optionsPanel;
    // --- Buttons ---
    private JButton usersButton;
    private JButton scheduleButton;
    private JButton billboardButton;

    /**
     * Constructor to create home view, use parent constructor.
     */
    public HomeView()
    {
        super("Home View");
    }

    @Override
    void createComponents()
    {
        optionsPanel = new JPanel();
        optionsPanel.setLayout(new FlowLayout());
        usersButton = new JButton("Users");
        scheduleButton = new JButton("Schedule");
        billboardButton = new JButton("Billboard");
        optionsPanel.add(usersButton);
        optionsPanel.add(scheduleButton);
        optionsPanel.add(billboardButton);
        getContentPane().add(optionsPanel, BorderLayout.CENTER);

        addProfilePanel();
    }

    /**
     * Update is used when model is updated and view needs to change accordingly!
     * @param s The subject that has been updated.
     */
    @Override
    public void update(Subject s)
    {
    }

    /**
     * Add listener to handle navigation to billboards screen.
     * @param listener mouse click listener
     */
    protected void addBillboardsListener(MouseListener listener)
    {
        billboardButton.addMouseListener(listener);
    }
}
