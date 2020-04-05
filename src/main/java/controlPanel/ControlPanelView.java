package controlPanel;

import observer.Observer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;

public abstract class ControlPanelView extends JFrame implements Observer
{
    // *** DECLARE VARIABLES**
    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;
    private JPanel profilePanel;
    private JPanel homePanel;
    // --- Buttons ---
    private JButton homeButton;
    private JButton profileButton;
    // --- Labels ---
    private JLabel welcomeText;

    /**
     * Constructor for creating the Views of the application. The constructor sets the frame's name and set's up the
     * View by defining Width and Height, default close operation and the Layout. The constructor also calls the
     * createComponents() method which is defined in child classes.
     * @param frame_name name of JFrame
     */
    public ControlPanelView(String frame_name)
    {
        // assign frame name
        super(frame_name);
        setupFrame();
        createComponents();
    }

    /**
     * Set up Frame by setting size, close operation and layout.
     */
    private void setupFrame()
    {
        // Purpose: size, close operation and layout
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
    }

    /**
     * Create components for the View
     */
    abstract void createComponents();

    /**
     *  Add Profile panel which can be called by child classes.
     *  1. Create Profile Panel
     *  2. Add 'View Profile' Button
     *  3. Add Placeholder for Welcome <Name> Text
     *  3. Add Panel to Frame
     */
    protected void addProfilePanel()
    {
        profilePanel = new JPanel();
        profilePanel.setLayout(new FlowLayout());
        welcomeText = new JLabel("");
        profileButton = new JButton("View Profile");
        profilePanel.add(profileButton);
        profilePanel.add(welcomeText);
        getContentPane().add(profilePanel, BorderLayout.NORTH);
    }

    /**
     *  Add Home Panel which can be called by child classes.
     *  1. Create HomeView Panel
     *  2. Add 'HomeView' Button
     *  3. Add Panel to Frame
     */
    protected void addHomePanel()
    {
        homePanel = new JPanel();
        homeButton = new JButton("Home");
        homePanel.add(homeButton);
        getContentPane().add(homePanel, BorderLayout.SOUTH);
    }

    /**
     * Add listener to Home button. This listener will navigate user back to Home frame.
     * @param listener Mouse Click listener
     */
    protected void addHomeListener(MouseListener listener)
    {
        homeButton.addMouseListener(listener);
    }

    /**
     * Set username in Welcome text i.e "Welcome <Name>"
     * @param username username of current user
     */
    protected void setWelcomeText(String username)
    {
        welcomeText.setText("Welcome " + username);
    }
}
