package controlPanel;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseListener;

/**
 * Abstract Generic View is designed to provide all generic functionality employed by a large portion of views.
 * Functionality includes: adding a Profile, Home and Back Button, a navigation and profile panel.
 */
public abstract class AbstractGenericView extends AbstractView
{
    // *** DECLARE VARIABLES**
    // --- Panel ---
    private JPanel profilePanel;
    private JPanel navPanel;
    // --- Buttons ---
    private JButton homeButton;
    protected JButton profileButton;
    private JButton backButton;
    private JButton logOutButton;
    // --- Labels ---
    private JLabel welcomeText;
    // --- GBC ---
    private GridBagConstraints gbc_profile;
    private GridBagConstraints gbc_nav;

    /**
     * Constructor for creating Views of the application. The constructor sets the frame's name and set's up the
     * View by defining Width and Height, default close operation and the Layout. The constructor also calls the
     * createComponents() method. The Profile and Nav Panel are added to allow users
     * to navigate Home, Back or to their Profile.
     * @param frame_name name of JFrame
     */
    public AbstractGenericView(String frame_name)
    {
        // assign frame name
        super(frame_name);
        // create gbc for each panel
        gbc_profile = new GridBagConstraints();
        gbc_nav = new GridBagConstraints();
        // add profile and nav panel
        addProfilePanel();
        addNavPanel();
        // create components specific to view
        createComponents();
    }

    /**
     * Create components for the View, to be implemented by child classes.
     * This is method is designed to add components to the panels.
     */
    abstract void createComponents();

    /**
     *  Add Profile panel
     *  1. Create Profile Panel
     *  2. Add 'Profile' Button
     *  3. Add Placeholder for Welcome <Name> Text
     *  3. Add Panel to Frame
     */
    protected void addProfilePanel()
    {
        profilePanel = new JPanel();
        profilePanel.setLayout(new GridBagLayout());
        profilePanel.setBackground(Color.GRAY);
        welcomeText = new JLabel("Welcome");
        welcomeText.setForeground(Color.WHITE);
        profileButton = new JButton("Profile");
        logOutButton = new JButton("Log Out");
        gbc_profile.insets = new Insets(5,5,5,5);
        profilePanel.add(profileButton, setGBC(gbc_profile, 1,1,1,1));
        profilePanel.add(welcomeText, setGBC(gbc_profile, 6,1,1,1));
        gbc_profile.insets = new Insets(5,500,5,5);
        profilePanel.add(logOutButton, setGBC(gbc_profile, 10,1,2,1));
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
        navPanel = new JPanel(new GridBagLayout());
        navPanel.setBackground( new Color(255,87,87));
        homeButton = new JButton("Home");
        backButton = new JButton("Back");
        gbc_nav.insets = new Insets(5,20,5,20);
        navPanel.add(homeButton, setGBC(gbc_nav,1,1,1,1));
        navPanel.add(backButton, setGBC(gbc_nav,2,1,1,1));
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
     * Add listener to log out button. This listener will navigate user back to log in frame.
     * @param listener Mouse Click listener
     */
    protected void addLogOutListener(MouseListener listener)
    {
        logOutButton.addMouseListener(listener);
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

    /**
     * Get nav panel such that child classes can edit/update
     * @return nav panel
     */
    protected JPanel getNavPanel()
    {
        return navPanel;
    }

    /**
     * Get nav panel GBC such that child classes can edit/update
     * @return nav panel gbc
     */
    protected GridBagConstraints getNavGBCPanel()
    {
        return gbc_nav;
    }

    /**
     * Show message to user where input string is the message displayed
     */
    protected void showMessageToUser(String message)
    {
        JOptionPane.showMessageDialog(null, message);
    }
}
