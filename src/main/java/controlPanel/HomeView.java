package controlPanel;

import observer.Subject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import controlPanel.Main.VIEW_TYPE;

public class HomeView extends AbstractGenericView
{
    // *** VARIABLES**
    // --- Panels ---
    private JPanel optionsPanel;
    // --- Buttons ---
    protected JButton usersButton;
    private JButton scheduleButton;
    private JButton billboardButton;
    // --- ENUM ---
    private VIEW_TYPE view_type;

    /**
     * Constructor to create home view, use parent constructor.
     */
    public HomeView()
    {
        super("Home View");
        this.view_type = VIEW_TYPE.HOME;
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
    protected void addBillboardsButtonListener(MouseListener listener)
    {
        billboardButton.addMouseListener(listener);
    }

    /**
     * Add listener to handle navigation to users screen.
     * @param listener mouse click listener
     */
    protected void addUserMenuListener(MouseListener listener)
    {
        usersButton.addMouseListener(listener);
    }

    protected void addScheduleButtonListener(MouseListener listener) {scheduleButton.addMouseListener(listener);}

}
