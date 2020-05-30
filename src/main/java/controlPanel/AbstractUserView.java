package controlPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * Abstract User View is designed with the key features for displaying with User data including username, permissions and
 * password. Child classes, including Profile, Edit User, Create User and View User inherit this common structure.
 * Abstract Generic View is extended to gain generic functionality.
 */
public abstract class AbstractUserView extends AbstractGenericView
{
    // *** DECLARE VARIABLES**
    // --- Panels ---
    private JPanel userDetailsPane;
    private JScrollPane scrollPane;
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
    // --- button ---
    private JButton userMenuButton;

    /**
     * Constructor to set up JFrame with provided name and create GUI componentes
     * @param frame_name name of JFrame
     */
    public AbstractUserView(String frame_name)
    {
        super(frame_name);
        // add user menu button
        addUserMenuButton();
    }

    /**
     * Create View Components which include panels, buttons, text etc. These components make up the view/JFrame seen
     * by the user.
     */
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

        scrollPane = new JScrollPane(userDetailsPane);

        getContentPane().add(scrollPane, BorderLayout.CENTER);
    }

    private void addUserMenuButton()
    {
        userMenuButton = new JButton("User Menu");
        JPanel navPanel = getNavPanel();
        GridBagConstraints gbc = getNavGBCPanel();
        navPanel.add(userMenuButton, setGBC(gbc, 2,1,1,1));
    }

    /**
     * Set username text for User
     * @param username username string
     */
    protected void setUsername(String username)
    {
        usernameText.setText(username);
    }

    /**
     * Set permissions of user which requires a boolean array list as input
     * @param permissions boolean array list for permissions
     */
    protected void setPermissions(ArrayList<Boolean> permissions)
    {
        editUsersPermission.setSelected(permissions.get(3));
        scheduleBBPermission.setSelected(permissions.get(2));
        editBBPermission.setSelected(permissions.get(1));
        createBBPermission.setSelected(permissions.get(0));
    }

    /**
     * Set editable user data fields dependent on child class
     * @param editable boolean value
     */
    protected void setEditable(boolean editable)
    {
        usernameText.setEditable(editable);
        editBBPermission.setEnabled(editable);
        scheduleBBPermission.setEnabled(editable);
        editUsersPermission.setEnabled(editable);
        createBBPermission.setEnabled(editable);
    }

    /**
     * Get User Panel for child classes to use
     * @return user panel
     */
    protected JPanel getUserPanel()
    {
        return userDetailsPane;
    }

    /**
     * Get User Panel GBC for child classes to use
     * @return user panel GBC
     */
    protected GridBagConstraints getUserPanelGBC()
    {
        return gbc;
    }

    /**
     * Add listener to navigate to user Menu View
     * @param listener mouse listener
     */
    protected void addUserMenuButton(MouseListener listener)
    {
        userMenuButton.addMouseListener(listener);
    }

}
