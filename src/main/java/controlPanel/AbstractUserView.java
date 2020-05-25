package controlPanel;

import observer.Subject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class AbstractUserView extends AbstractGenericView
{
    // *** DECLARE VARIABLES**
    // --- Panels ---
    private JPanel userDetailsPane;
    // --- Labels ---
    protected JTextField usernameText;
    private JPanel userPermissionsPanel;
    private JLabel userPermissionsLabel;
    private JLabel usernameLabel;
    protected JLabel title;
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
        userDetailsPane.add(userPermissionsLabel, setGBC(gbc, 1,4,1,1));
        userDetailsPane.add(userPermissionsPanel, setGBC(gbc, 2,4,1,1));

        getContentPane().add(userDetailsPane, BorderLayout.CENTER);
    }

    protected void setUsername(String username)
    {
        usernameText.setText(username);
    }

    protected void setPermissions(ArrayList<Boolean> permissions)
    {
        editUsersPermission.setSelected(permissions.get(3));
        scheduleBBPermission.setSelected(permissions.get(2));
        editBBPermission.setSelected(permissions.get(1));
        createBBPermission.setSelected(permissions.get(0));
    }

    protected void setEditable(boolean editable)
    {
        usernameText.setEditable(editable);
        editBBPermission.setEnabled(editable);
        scheduleBBPermission.setEnabled(editable);
        editUsersPermission.setEnabled(editable);
        createBBPermission.setEnabled(editable);
    }

    protected JPanel getUserPanel()
    {
        return userDetailsPane;
    }

    protected GridBagConstraints getUserPanelGBC()
    {
        return gbc;
    }

    @Override
    public void update(Subject s) {

    }

    @Override
    void cleanUp()
    {
        setUsername("");
        setPermissions(new ArrayList<>(Arrays.asList(false,false,false,false)));
    }
}
