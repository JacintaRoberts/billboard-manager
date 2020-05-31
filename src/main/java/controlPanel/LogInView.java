package controlPanel;

import controlPanel.Main.VIEW_TYPE;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;

/**
 * Log In View to allow users to log in with their username and password. An error message is displayed for
 * incorrect credentials.
 */
public class LogInView extends AbstractView
{
    // *** DECLARE VARIABLES**
    // --- Panels ---
    private JPanel loginPanel;
    // --- Buttons ---
    private JButton submitButton;
    // --- Labels ---
    private JLabel usernameText;
    private JLabel passwordText;
    // --- Fields ---
    private static JTextField usernameField;
    private static JPasswordField passwordField;
    // --- ENUM ---
    private VIEW_TYPE logInType;
    // --- GBC ---
    private GridBagConstraints gbc = new GridBagConstraints();

    /**
     * Constructor to set up JFrame with provided name and create GUI components
     * Set ENUM value allowing use in Controller Class
     */
    public LogInView()
    {
        super("LogInView Panel");
        // create components
        createComponents();
        // set login enum to frame
        this.logInType = VIEW_TYPE.LOGIN;
    }

    /**
     * Create View Components which include panels, buttons, text etc. These components make up the view/JFrame seen
     * by the user.
     */
    private void createComponents()
    {
        // create login panel
        loginPanel = new JPanel();
        loginPanel.setLayout(new GridBagLayout());
        // set user name text and field
        usernameText = new JLabel("USERNAME");
        usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(300,60));
        // set password text and field
        passwordText = new JLabel("PASSWORD");
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(300,60));
        // create submit button
        submitButton = new JButton("SUBMIT");

        gbc.insets = new Insets(5,5,5,5);
        // add items to log in panel with gbc layout
        loginPanel.add(usernameText, setGBC(gbc, 1,1,1,1));
        loginPanel.add(usernameField, setGBC(gbc,5,1,1,1));
        loginPanel.add(passwordText, setGBC(gbc,1,2,1,1));
        loginPanel.add(passwordField, setGBC(gbc,5,2,1,1));
        loginPanel.add(submitButton, setGBC(gbc,3,3,5,2));
        // add login panel to frame
        getContentPane().add(loginPanel, BorderLayout.CENTER);
    }

    /**
     * Get username text field from frame
     * @return username string
     */
    public static String getUsername()
    {
        return usernameField.getText();
    }

    /**
     * Get password text field from frame
     * @return password string
     */
    public static String getPassword()
    {
        return new String(passwordField.getPassword());
    }

    /**
     * Add listener to handle mouse click of submit button.
     * @param listener mouse click listener
     */
    public void addSubmitButtonListener(MouseListener listener)
    {
        submitButton.addMouseListener(listener);
    }

    /**
     * Get Enum associated to this View. This is defined in the Constructor and is used in the Controller Class.
     * @return view type enum assigned to view
     */
    public VIEW_TYPE getEnum()
    {
        return logInType;
    }

    /**
     * Clean Up all data that should not persist in the GUI. The view will be cleaned up after leaving the view.
     */
    @Override
    void cleanUp()
    {
        // clean up by setting username and password to null
        usernameField.setText(null);
        passwordField.setText(null);
    }
}
