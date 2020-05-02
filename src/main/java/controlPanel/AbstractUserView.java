package controlPanel;

import observer.Subject;

import javax.swing.*;
import java.awt.*;

public abstract class AbstractUserView extends AbstractGenericView
{
    // *** DECLARE VARIABLES**
    // --- Panels ---
    private JPanel userDetailsPane;
    // --- Labels ---
    private JLabel usernameText;
    private JLabel passwordText;
    private JPanel userPermissionsPanel;
    private JLabel userPermissionsLabel;
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
        userPermissionsLabel = new JLabel("User Permissions");
        userPermissionsPanel = new JPanel();

        userDetailsPane.add(usernameLabel);
        userDetailsPane.add(usernameText);
        userDetailsPane.add(passwordLabel);
        userDetailsPane.add(passwordText);
        userDetailsPane.add(userPermissionsLabel);
        userDetailsPane.add(userPermissionsPanel);

        getContentPane().add(userDetailsPane, BorderLayout.CENTER);
    }

    protected void setUsername(String username)
    {
        usernameText.setText(username);
    }

    protected void setPassword(String password)
    {
        passwordText.setText(password);
    }

    protected void setPermissions(String[] permissions)
    {
        userPermissionsPanel.setLayout(new GridLayout(permissions.length,1));
        for (String permission: permissions)
        {
            JLabel permissionLabel = new JLabel(permission);
            userPermissionsPanel.add(permissionLabel);
        }
    }

    abstract void addUserPermissions();

    @Override
    public void update(Subject s) {

    }
}
