package controlPanel;

import observer.Subject;

import javax.swing.*;
import java.awt.*;

public abstract class AbstractUserView extends AbstractGenericView
{
    // *** DECLARE VARIABLES**
    // --- Panels ---
    private JPanel userDetailsPane;
    // --- Buttons ---
    private JButton submitButton;
    // --- Labels ---
    private JLabel usernameText;
    private JLabel passwordText;
    private JLabel userPermissions;
    private JLabel errorText;
    private JLabel usernameLabel;
    private JLabel passwordLabel;

    /**
     * Constructor for creating the Views of the application. The constructor sets the frame's name and set's up the
     * View by defining Width and Height, default close operation and the Layout. The constructor also calls the
     * createComponents() method which is defined in child classes.
     *
     * @param frame_name name of JFrame
     */
    public AbstractUserView(String frame_name)
    {
        super(frame_name);
        addUserPermissions();
    }

    @Override
    void createComponents()
    {
        userDetailsPane = new JPanel();
        userDetailsPane.setLayout(new GridLayout(3,2));
        usernameLabel = new JLabel("Username");
        usernameText = new JLabel("");
        passwordLabel = new JLabel("Password");
        passwordText = new JLabel("");
        userPermissions = new JLabel("User Permissions");
        submitButton = new JButton("Submit");
        errorText = new JLabel("Incorrect Credentials");
        userDetailsPane.add(usernameLabel);
        userDetailsPane.add(usernameText);
        userDetailsPane.add(passwordLabel);
        userDetailsPane.add(passwordText);
        userDetailsPane.add(userPermissions);
        userDetailsPane.add(errorText);
        userDetailsPane.add(submitButton);

        errorText.setVisible(false);
        getContentPane().add(userDetailsPane, BorderLayout.CENTER);
    }

    abstract void addUserPermissions();

    @Override
    public void update(Subject s) {

    }
}
