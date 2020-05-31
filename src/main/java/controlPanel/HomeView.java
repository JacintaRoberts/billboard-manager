package controlPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import controlPanel.Main.VIEW_TYPE;

/**
 * Home view to allow users to navigate to all areas of the application including Users, Billboard and Schedule menus.
 */
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
    // --- Label ---
    private JLabel title;
    // --- GBC ---
    private GridBagConstraints gbc;

    /**
     * Constructor to set up JFrame with provided name and create GUI components
     * Set ENUM value allowing use in Controller Class
     */
    public HomeView()
    {
        super("Home View");
        // define Home view enum
        this.view_type = VIEW_TYPE.HOME;
    }

    /**
     * Create View Components which include panels, buttons, text etc. These components make up the view/JFrame seen
     * by the user.
     */
    @Override
    void createComponents()
    {
        // define gbc
        gbc = new GridBagConstraints();
        // create options panel
        optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridBagLayout());
        // define title, colour and font
        title = new JLabel("HOME");
        title.setForeground(Color.WHITE);
        title.setFont(title.getFont().deriveFont(60f));
        // create user, schedule and bb buttons
        usersButton = new JButton("Users");
        scheduleButton = new JButton("Schedule");
        billboardButton = new JButton("Billboard");
        // add to panel with gbc layout
        gbc.insets = new Insets(5,20,5,20);
        optionsPanel.add(usersButton, setGBC(gbc, 1,1,1,1));
        optionsPanel.add(scheduleButton, setGBC(gbc, 2,1,1,1));
        optionsPanel.add(billboardButton, setGBC(gbc, 3,1,1,1));
        gbc.insets = new Insets(250,1,1,1);
        optionsPanel.add(title, setGBC(gbc, 2,2,1,1));
        // add options panel to frame
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

    /**
     * Add listener to handle navigation to schedule screen.
     * @param listener mouse click listener
     */
    protected void addScheduleButtonListener(MouseListener listener) {scheduleButton.addMouseListener(listener);}

}
