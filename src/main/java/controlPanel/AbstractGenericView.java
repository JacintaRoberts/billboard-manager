package controlPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;

/**
 * Abstract Generic View is designed to provide all generic functionality employed by a large portion of views.
 * Functionality includes: adding a Profile, Home and Back Button.
 */
public abstract class AbstractGenericView extends AbstractView
{
    // *** DECLARE VARIABLES**
    private JPanel profilePanel;
    private JPanel navPanel;
    // --- Buttons ---
    private JButton homeButton;
    protected JButton profileButton;
    private JButton backButton;
    // --- Labels ---
    private JLabel welcomeText;

    /**
     * Constructor for creating Views of the application. The constructor sets the frame's name and set's up the
     * View by defining Width and Height, default close operation and the Layout. The constructor also calls the
     * createComponents() method which is defined in child classes. The Profile and Nav Panel are added to allow users
     * to navigate Home, Back or to their Profile.
     * @param frame_name name of JFrame
     */
    public AbstractGenericView(String frame_name)
    {
        // assign frame name
        super(frame_name);
        // create components specific to view
        createComponents();
        // add profile and nav panel
        addProfilePanel();
        addNavPanel();
    }

    /**
     * Create components for the View, to be implemented by child classes.
     */
    abstract void createComponents();

    /**
     *  Add Profile panel
     *  1. Create Profile Panel
     *  2. Add 'View Profile' Button
     *  3. Add Placeholder for Welcome <Name> Text
     *  3. Add Panel to Frame
     */
    protected void addProfilePanel()
    {
        profilePanel = new JPanel();
        profilePanel.setLayout(new FlowLayout());
        welcomeText = new JLabel("Welcome");
        profileButton = new JButton("View Profile");
        profilePanel.add(profileButton);
        profilePanel.add(welcomeText);
        getContentPane().add(profilePanel, BorderLayout.NORTH);
    }

    /**
     *  Add Nav Panel
     *  1. Create Nav Panel
     *  2. Add 'HomeView' Button
     *  3. Add 'Back' button
     *  3. Add Panel to Frame
     */
    protected void addNavPanel()
    {
        navPanel = new JPanel();
        homeButton = new JButton("Home");
        backButton = new JButton("Back");
        navPanel.add(homeButton);
        navPanel.add(backButton);
        getContentPane().add(navPanel, BorderLayout.SOUTH);
    }

    /**
     * Add listener to Home button. This listener will navigate user back to Home frame.
     * @param listener Mouse Click listener
     */
    protected void addHomeButtonListener(MouseListener listener)
    {
        homeButton.addMouseListener(listener);
    }

    /**
     * Add listener to back button. This listener will navigate user back to previous frame.
     * @param listener Mouse Click listener
     */
    protected void addBackButtonListener(MouseListener listener)
    {
        backButton.addMouseListener(listener);
    }

    /**
     * Set username in Welcome text i.e "Welcome <Name>"
     * @param username username of current user
     */
    protected void setWelcomeText(String username)
    {
        welcomeText.setText("Welcome " + username);
    }

    /**
     * Add Profile Button Listener. This listener will navigate user to profile view.
     * @param listener Mouse Click listener
     */
    protected void addViewUserButtonListener(MouseListener listener) {profileButton.addMouseListener(listener);}
}
