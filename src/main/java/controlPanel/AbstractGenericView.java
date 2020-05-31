package controlPanel;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseListener;

/**
 * Abstract Generic View is designed to provide all generic functionality extended by all views excluding LogInView and
 * BBFullPreview. Functionality includes a Profile, Log Out and Home Button embedded in a Navigation and Profile panel.
 * Abstract View is extended to gain generic style and set up of the GUI frame.
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
    private JButton logOutButton;
    // --- Labels ---
    private JLabel welcomeText;
    // --- GBC ---
    private GridBagConstraints gbc_profile;
    private GridBagConstraints gbc_nav;

    /**
     * Constructor for creating Views of the application. The constructor calls the createComponents() method. The
     * Profile and Nav Panel are added to allow user to navigate Home, Log out or view their Profile.
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
     *  3. Add 'Log Out' Button
     *  4. Add Placeholder for Welcome "Name" Text
     *  5. Add Panel to Frame
     */
    protected void addProfilePanel()
    {
        // create profile pane, set layout and background
        profilePanel = new JPanel();
        profilePanel.setLayout(new GridBagLayout());
        profilePanel.setBackground(Color.GRAY);
        // create welcome text on panel
        welcomeText = new JLabel("Welcome");
        welcomeText.setForeground(Color.WHITE);
        // create profile and log out button
        profileButton = new JButton("Profile");
        logOutButton = new JButton("Log Out");
        // define gbc insets for location of components
        gbc_profile.insets = new Insets(5,5,5,5);
        // add button and text to profile panel
        profilePanel.add(profileButton, setGBC(gbc_profile, 1,1,1,1));
        profilePanel.add(welcomeText, setGBC(gbc_profile, 6,1,1,1));
        gbc_profile.insets = new Insets(5,500,5,5);
        // add log out button to profile panel
        profilePanel.add(logOutButton, setGBC(gbc_profile, 10,1,2,1));
        // add profile panel to frame
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
        // create nav panel and set background colour
        navPanel = new JPanel(new GridBagLayout());
        navPanel.setBackground( new Color(255,87,87));
        // create home button
        homeButton = new JButton("Home");
        gbc_nav.insets = new Insets(5,20,5,20);
        // add home button to nav panel
        navPanel.add(homeButton, setGBC(gbc_nav,1,1,1,1));
        // add nav panel to frame
        getContentPane().add(navPanel, BorderLayout.SOUTH);
    }

    // --------- LISTENERS ---------

    /**
     * Add listener to Home button. This listener will navigate user back to Home frame.
     * @param listener Mouse Click listener
     */
    protected void addHomeButtonListener(MouseListener listener)
    {
        homeButton.addMouseListener(listener);
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
     * Set username in Welcome text i.e "Welcome 'Name'"
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
     * @param message The message to be displayed to the user
     */
    protected void showMessageToUser(String message)
    {
        JOptionPane.showMessageDialog(null, message);
    }
}
