package controlPanel;

import controlPanel.Main.VIEW_TYPE;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * User Edit View is designed to allow users to edit user accounts (assuming valid permissions). The username cannot be
 * edited, only the permissions and password can be updated. This extends the AbstractUserView to acquire key elements.
 */
public class UserEditView extends AbstractUserView
{
    // *** DECLARE VARIABLES**
    // --- ENUM ---
    private VIEW_TYPE view_type;
    // --- Buttons ---
    private JButton submitButton;
    private JButton setPasswordButton;
    // --- String ---
    private String passwordText;

    /**
     * Constructor to set up JFrame with provided name and create GUI components
     * Set ENUM value allowing use in Controller Class
     */
    public UserEditView()
    {
        super("Edit User");
        view_type = VIEW_TYPE.USER_EDIT;
        setEditable(true);
        usernameText.setEditable(false);
        passwordText = null;
        addSubmitButton();
        addSetPasswordButton();
        title.setText("EDIT USER");
    }

    /**
     * Add submit new permissions button to user panel
     */
    protected void addSubmitButton()
    {
        JPanel userPanel = getUserPanel();
        GridBagConstraints gbc = getUserPanelGBC();
        submitButton = new JButton("Submit New Permissions");
        userPanel.add(submitButton, setGBC(gbc,3,4,1,1));
    }

    /**
     * Add set password button to user panel
     */
    private void addSetPasswordButton()
    {
        JPanel userPanel = getUserPanel();
        GridBagConstraints gbc = getUserPanelGBC();
        setPasswordButton = new JButton("Update Password");
        userPanel.add(setPasswordButton, setGBC(gbc,3,5,1,1));
    }

    /**
     * Get user info provided by user
     * @return array list of user info
     */
    protected ArrayList<Object> getUserInfo()
    {
        ArrayList<Object> userInfoArray = new ArrayList<>();
        userInfoArray.add(usernameText.getText());
        userInfoArray.add(createBBPermission.isSelected());
        userInfoArray.add(editBBPermission.isSelected());
        userInfoArray.add(scheduleBBPermission.isSelected());
        userInfoArray.add(editUsersPermission.isSelected());
        return userInfoArray;
    }

    /**
     * Ask user for new password
     * @return response of user (int)
     * @throws Exception exception raised when invalid password or no password provided
     */
    protected String showNewPasswordInput() throws Exception
    {
        String message = "Please enter new password.";
        String password = JOptionPane.showInputDialog(null, message);

        // null catches when user has canceled set, "" when user has not provided a string but has clicked OK
        if (password != null)
        {
            if (!password.equals(""))
            {
                passwordText = password;
                return passwordText;
            }
            else
            {
                throw new Exception("Invalid Password Provided");
            }
        }
        else
        {
            throw new Exception("No Password Provided. Password not set.");
        }

    }

    /**
     * Add submit button listener
     * @param listener listener
     */
    public void addSubmitButtonListener(MouseListener listener)
    {
        submitButton.addMouseListener(listener);
    }

    /**
     * Add password button listener
     * @param listener listener
     */
    public void addPasswordButtonListener(MouseListener listener)
    {
        setPasswordButton.addMouseListener(listener);
    }

    /**
     * Show Ask User for confirmation of user creation
     * @return response of user (int)
     */
    protected int showUserConfirmation()
    {
        String message = "Are you sure you want to proceed?";
        return JOptionPane.showConfirmDialog(null, message);
    }

    /**
     * Get Enum associated to this View. This is defined in the Constructor and is used in the Controller Class.
     * @return view type enum assigned to view
     */
    @Override
    VIEW_TYPE getEnum() {
        return view_type;
    }

    /**
     * Clean Up all data that should not persist in the GUI. The view will be cleaned up after leaving the view.
     */
    @Override
    void cleanUp()
    {
        setUsername("");
        setPermissions(new ArrayList<>(Arrays.asList(false,false,false,false)));
        passwordText = null;
    }

}
