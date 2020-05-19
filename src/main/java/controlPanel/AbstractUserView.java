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
    private JLabel title;
    // --- CheckBox ---
    protected JCheckBox editUsersPermission;
    protected JCheckBox editBBPermission;
    protected JCheckBox scheduleBBPermission;
    protected JCheckBox createBBPermission;
    // --- GBC ---
    private GridBagConstraints gbc;
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
        title = new JLabel("");
        title.setForeground(Color.WHITE);
        title.setFont(title.getFont().deriveFont(90f));
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(50,10,50,10);
        userDetailsPane = new JPanel();
        userDetailsPane.setLayout(new GridBagLayout());
        usernameLabel = new JLabel("Username");
        usernameText = new JTextField("");
        usernameText.setPreferredSize(new Dimension(100,100));
        passwordLabel = new JLabel("Password");
        passwordText = new JTextField("");
        passwordText.setPreferredSize(new Dimension(100,100));
        userPermissionsLabel = new JLabel("User Permissions");

        editBBPermission = new JCheckBox("Edit All Billboards");
        scheduleBBPermission = new JCheckBox("Edit Schedules");
        editUsersPermission = new JCheckBox("Edit Users");
        createBBPermission = new JCheckBox("Create Billboards");
        userPermissionsPanel = new JPanel();
        userPermissionsPanel.setLayout(new GridLayout(4,1));
        userPermissionsPanel.add(editBBPermission);
        userPermissionsPanel.add(scheduleBBPermission);
        userPermissionsPanel.add(createBBPermission);
        userPermissionsPanel.add(editUsersPermission);

        userDetailsPane.add(title, setGBC(gbc, 2,1,1,1));
        userDetailsPane.add(usernameLabel, setGBC(gbc, 1,2,1,1));
        gbc.fill = GridBagConstraints.HORIZONTAL;
        userDetailsPane.add(usernameText, setGBC(gbc, 2,2,2,1));
        gbc.fill = GridBagConstraints.NONE;
        userDetailsPane.add(passwordLabel, setGBC(gbc, 1,3,1,1));
        gbc.fill = GridBagConstraints.HORIZONTAL;
        userDetailsPane.add(passwordText, setGBC(gbc, 2,3,2,1));
        gbc.fill = GridBagConstraints.NONE;
        userDetailsPane.add(userPermissionsLabel, setGBC(gbc, 1,4,1,1));
        userDetailsPane.add(userPermissionsPanel, setGBC(gbc, 2,4,1,1));

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

    protected void setPermissions(boolean[] permissions)
    {
        editUsersPermission.setSelected(permissions[0]);
        scheduleBBPermission.setSelected(permissions[1]);
        editBBPermission.setSelected(permissions[2]);
        createBBPermission.setSelected(permissions[3]);
    }

    protected void setBBFrameTitle(String titleName)
    {
        title.setText(titleName);
    }

    protected void setEditable(boolean editable)
    {
        passwordText.setEditable(editable);
        usernameText.setEditable(editable);
        editBBPermission.setEnabled(editable);
        scheduleBBPermission.setEnabled(editable);
        editUsersPermission.setEnabled(editable);
        createBBPermission.setEnabled(editable);
    }

    @Override
    public void update(Subject s) {

    }

    @Override
    void cleanUp()
    {
        setUsername("");
        setPassword("");
        setPermissions(new boolean[]{false,false,false,false});
    }

}
