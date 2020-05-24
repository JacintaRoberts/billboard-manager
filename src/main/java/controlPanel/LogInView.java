package controlPanel;


import controlPanel.Main.VIEW_TYPE;
import observer.Subject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;

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
    private JLabel errorText;
    // --- Fields ---
    private static JTextField usernameField;
    private static JPasswordField passwordField;
    // --- ENUM ---
    private VIEW_TYPE logInType;

    private GridBagConstraints gbc = new GridBagConstraints();

    /**
     * Constructor for LogIn View - set frame name
     */
    public LogInView()
    {
        super("LogInView Panel");
        createComponents();
        this.logInType = VIEW_TYPE.LOGIN;
    }

    void createComponents()
    {
        loginPanel = new JPanel();
        loginPanel.setLayout(new GridBagLayout());
        usernameText = new JLabel("USERNAME");
        usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(300,60));
        passwordText = new JLabel("PASSWORD");
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(300,60));
        submitButton = new JButton("SUBMIT");
        errorText = new JLabel("");

        gbc.insets = new Insets(5,5,5,5);

        loginPanel.add(usernameText, setGBC(gbc, 1,1,1,1));
        loginPanel.add(usernameField, setGBC(gbc,5,1,1,1));
        loginPanel.add(passwordText, setGBC(gbc,1,2,1,1));
        loginPanel.add(passwordField, setGBC(gbc,5,2,1,1));
        loginPanel.add(errorText);
        loginPanel.add(submitButton, setGBC(gbc,3,3,5,2));

        errorText.setVisible(false);
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
     * Set error message visibility
     * @param visible true = visible, false = hidden
     */
    public void setErrorVisibility(boolean visible)
    {
        errorText.setVisible(visible);
    }

    @Override
    public void update(Subject s)
    {
        Model model = (Model) s;
        System.out.println("Update - submit pushed");
    }

    /**
     * Add listener to handle mouse click of submit button.
     * @param listener mouse click listener
     */
    public void addSubmitButtonListener(MouseListener listener)
    {
        submitButton.addMouseListener(listener);
    }

    public VIEW_TYPE getEnum()
    {
        return logInType;
    }

    @Override
    void cleanUp()
    {
        usernameField.setText(null);
        passwordField.setText(null);
    }
}
