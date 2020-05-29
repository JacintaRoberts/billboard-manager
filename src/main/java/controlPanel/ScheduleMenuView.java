package controlPanel;

import controlPanel.Main.VIEW_TYPE;
import observer.Subject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.time.Month;
import java.util.ArrayList;

/**
 * Schedule Menu View designed to allow users to navigate to scheduling functionality including creeating/updating
 * schedule and viewing the calendar view of the schedule.
 */
public class ScheduleMenuView extends AbstractGenericView
{
    // *** VARIABLES**
    // --- Panels ---
    private JPanel optionsPanel;
    // --- Buttons ---
    private JButton viewScheduleButton;
    private JButton createScheduleButton;
    // --- ENUM ---
    private VIEW_TYPE view_type;
    // --- GBC ---
    private GridBagConstraints gbc;
    // --- Label ---
    private JLabel title;

    /**
     * Constructor to create schedule view, use parent constructor.
     */
    public ScheduleMenuView()
    {
        super("Schedule Menu View");
        view_type = VIEW_TYPE.SCHEDULE_MENU;
    }

    @Override
    VIEW_TYPE getEnum() {
        return view_type;
    }

    @Override
    void cleanUp() {

    }

    @Override
    public void update(Subject s) {

    }

    @Override
    void createComponents()
    {
        gbc = new GridBagConstraints();
        optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridBagLayout());
        viewScheduleButton = new JButton("View Schedule");
        createScheduleButton = new JButton("Create/Update Schedule");
        title = new JLabel("SCHEDULE MENU");
        title.setForeground(Color.WHITE);
        title.setFont(title.getFont().deriveFont(60f));

        gbc.insets = new Insets(1,10,1,10);
        optionsPanel.add(viewScheduleButton, setGBC(gbc,1,1,1,1));
        optionsPanel.add(createScheduleButton,  setGBC(gbc,3,1,1,1));
        gbc.insets = new Insets(250,1,1,1);
        optionsPanel.add(title, setGBC(gbc, 1,2,3,1));

        getContentPane().add(optionsPanel, BorderLayout.CENTER);
    }

    /**
     * Add calendar schedule view listener
     * @param listener listener
     */
    protected void addScheduleViewListener(MouseListener listener)
    {
        viewScheduleButton.addMouseListener(listener);
    }

    /**
     * Add schedule create listener
     * @param listener listener
     */
    protected void addScheduleCreateListener(MouseListener listener)
    {
        createScheduleButton.addMouseListener(listener);
    }
}
