package controlPanel;

import observer.Subject;
import controlPanel.Main.VIEW_TYPE;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;

/**
 * View designed for viewing Users.
 */
public class UserView extends AbstractGenericView
{
    // *** DECLARE VARIABLES**
    // --- Panels ---
    private JPanel userDetailsPane;
    // --- Labels ---
    private JLabel usernameText;
    private JLabel passwordText;
    private JLabel userPermissionsLabel;
    private JLabel userPermissionsText;
    private JLabel errorText;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    // --- ENUM ---
    private VIEW_TYPE view_type;


    public UserView()
    {
        super("User View");
        view_type = VIEW_TYPE.USER_VIEW;
    }

    @Override
    void createComponents()
    {
        userDetailsPane = new JPanel();
        userDetailsPane.setLayout(new GridLayout(4,2));
        usernameLabel = new JLabel("Username");
        usernameText = new JLabel("");
        passwordLabel = new JLabel("Password");
        passwordText = new JLabel("");
        userPermissionsLabel = new JLabel("User Permissions");
        userPermissionsText = new JLabel("");
        errorText = new JLabel("Incorrect Credentials");
        userDetailsPane.add(usernameLabel);
        userDetailsPane.add(usernameText);
        userDetailsPane.add(passwordLabel);
        userDetailsPane.add(passwordText);
        userDetailsPane.add(userPermissionsLabel);
        userDetailsPane.add(userPermissionsText);
        userDetailsPane.add(errorText);

        errorText.setVisible(false);
        getContentPane().add(userDetailsPane, BorderLayout.CENTER);
    }

    @Override
    void cleanUp() {

    }

    protected void setUsername(String username)
    {
        usernameText.setText(username);
    }

    protected void setPasswordText(String password)
    {
        passwordText.setText(password);
    }

    // FIXME: may arg could be array of ENUM, bool array
    protected void setUserPermissions(String permissions)
    {
        userPermissionsText.setText(permissions);
    }

    @Override
    public void update(Subject s) {

    }

    @Override
    VIEW_TYPE getEnum() {
        return view_type;
    }
}
