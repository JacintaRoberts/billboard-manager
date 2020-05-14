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
    protected JTextField usernameText;
    protected JTextField passwordText;
    private JPanel userPermissionsPanel;
    private JLabel userPermissionsLabel;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    // --- CheckBox ---
    protected JCheckBox editUsersPermission;
    protected JCheckBox editBBPermission;
    protected JCheckBox editSchedulePermission;

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
    }

    @Override
    void createComponents()
    {
        userDetailsPane = new JPanel();
        userDetailsPane.setLayout(new GridLayout(3,2));
        usernameLabel = new JLabel("Username");
        usernameText = new JTextField("");
        passwordLabel = new JLabel("Password");
        passwordText = new JTextField("");
        userPermissionsLabel = new JLabel("User Permissions");

        editBBPermission = new JCheckBox("Edit Billboards");
        editSchedulePermission = new JCheckBox("Edit Schedules");
        editUsersPermission = new JCheckBox("Edit All Users");
        userPermissionsPanel = new JPanel();
        userPermissionsPanel.setLayout(new GridLayout(3,1));
        userPermissionsPanel.add(editBBPermission);
        userPermissionsPanel.add(editSchedulePermission);
        userPermissionsPanel.add(editUsersPermission);

        userDetailsPane.add(usernameLabel);
        userDetailsPane.add(usernameText);
        userDetailsPane.add(passwordLabel);
        userDetailsPane.add(passwordText);
        userDetailsPane.add(userPermissionsLabel);
        userDetailsPane.add(userPermissionsPanel);

        getContentPane().add(userDetailsPane, BorderLayout.CENTER);
    }


//    protected JPanel getUserDetailsPanel()
//    {
//        return userDetailsPane;
//    }

    protected void setUsername(String username)
    {
        usernameText.setText(username);
    }

    protected void setPassword(String password)
    {
        passwordText.setText(password);
    }

    protected void setPermissions(boolean[] permissions)
    {
        editUsersPermission.setSelected(permissions[0]);
        editSchedulePermission.setSelected(permissions[1]);
        editBBPermission.setSelected(permissions[2]);
    }

    abstract void setEditable();

    @Override
    public void update(Subject s) {

    }
}
