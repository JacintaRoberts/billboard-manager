package controlPanel;

import controlPanel.Main.VIEW_TYPE;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;

/**
 * Menu View to allow users to navigate to the schedule week calendar view or create/update schedule view.
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
     * Constructor to set up JFrame with provided name and create GUI components
     * Set ENUM value allowing use in Controller Class
     */
    public ScheduleMenuView()
    {
        super("Schedule Menu View");
        // set enum to view
        view_type = VIEW_TYPE.SCHEDULE_MENU;
    }

    /**
     * Get Enum associated to this View. This is defined in the Constructor and is used in the Controller Class.
     * @return view type enum assigned to view
     */
    @Override
    VIEW_TYPE getEnum() {
        return view_type;
    }

    /**
     * Clean Up all data that should not persist in the GUI. The view will be cleaned up after leaving the view.
     */
    @Override
    void cleanUp() {
    }

    /**
     * Create View Components which include panels, buttons, text etc. These components make up the view/JFrame seen
     * by the user.
     */
    @Override
    void createComponents()
    {
        // create gbc
        gbc = new GridBagConstraints();
        // create options panel
        optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridBagLayout());
        // create view schedule and create/update schedule
        viewScheduleButton = new JButton("View Schedule");
        createScheduleButton = new JButton("Create/Update Schedule");
        // add title
        title = new JLabel("SCHEDULE MENU");
        title.setForeground(Color.WHITE);
        title.setFont(title.getFont().deriveFont(60f));
        // add items to options panel
        gbc.insets = new Insets(1,10,1,10);
        optionsPanel.add(viewScheduleButton, setGBC(gbc,1,1,1,1));
        optionsPanel.add(createScheduleButton,  setGBC(gbc,3,1,1,1));
        gbc.insets = new Insets(250,1,1,1);
        optionsPanel.add(title, setGBC(gbc, 1,2,3,1));
        // add options panel to frame
        getContentPane().add(optionsPanel, BorderLayout.CENTER);
    }

    /**
     * Add calendar schedule view listener to move to calendar view
     * @param listener listener
     */
    protected void addScheduleViewListener(MouseListener listener)
    {
        viewScheduleButton.addMouseListener(listener);
    }

    /**
     * Add schedule create listener to move to create BB view
     * @param listener listener
     */
    protected void addScheduleCreateListener(MouseListener listener)
    {
        createScheduleButton.addMouseListener(listener);
    }
}
