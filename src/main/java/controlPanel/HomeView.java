package controlPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import controlPanel.Main.VIEW_TYPE;

/**
 * Home View to allow users to navigate to other areas of the application.
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
        gbc = new GridBagConstraints();
        optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridBagLayout());
        title = new JLabel("HOME");
        title.setForeground(Color.WHITE);
        title.setFont(title.getFont().deriveFont(60f));

        usersButton = new JButton("Users");
        scheduleButton = new JButton("Schedule");
        billboardButton = new JButton("Billboard");
        gbc.insets = new Insets(5,20,5,20);
        optionsPanel.add(usersButton, setGBC(gbc, 1,1,1,1));
        optionsPanel.add(scheduleButton, setGBC(gbc, 2,1,1,1));
        optionsPanel.add(billboardButton, setGBC(gbc, 3,1,1,1));
        gbc.insets = new Insets(250,1,1,1);
        optionsPanel.add(title, setGBC(gbc, 2,2,1,1));
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
