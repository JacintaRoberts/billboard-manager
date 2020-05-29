package controlPanel;

import controlPanel.Main.VIEW_TYPE;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * View designed for creating users.
 */
public class UserCreateView extends AbstractUserView
{
    // *** DECLARE VARIABLES**
    // --- ENUM ---
    private VIEW_TYPE view_type;
    // --- Buttons ---
    private JButton submitButton;
    private JButton setPasswordButton;
    // --- String ---
    private String passwordText;

    public UserCreateView()
    {
        super("Create User");
        view_type = VIEW_TYPE.USER_CREATE;
        setEditable(true);
        passwordText = null;
        usernameText.setEditable(true);
        addSubmitButton();
        addSetPasswordButton();
        title.setText("CREATE USER");
    }

    /**
     * Add submit button to nav panel
     */
    protected void addSubmitButton()
    {
        submitButton = new JButton("Create User");
        JPanel navPanel = getNavPanel();
        GridBagConstraints gbc = getNavGBCPanel();
        navPanel.add(submitButton, setGBC(gbc,3,1,1,1));

    }

    /**
     * Add set password button to user panel
     */
    private void addSetPasswordButton()
    {
        JPanel userPanel = getUserPanel();
        GridBagConstraints gbc = getUserPanelGBC();
        setPasswordButton = new JButton("Create Password");
        userPanel.add(setPasswordButton, setGBC(gbc,2,5,1,1));
    }

    /**
     * Get new user info provided by user
     * @return array list of user info
     */
    protected ArrayList<Object> getUserInfo()
    {
        ArrayList<Object> userInfoArray = new ArrayList<>();
        userInfoArray.add(usernameText.getText());
        userInfoArray.add(passwordText);
        userInfoArray.add(createBBPermission.isSelected());
        userInfoArray.add(editBBPermission.isSelected());
        userInfoArray.add(scheduleBBPermission.isSelected());
        userInfoArray.add(editUsersPermission.isSelected());
        return userInfoArray;
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
    protected int showCreateUserConfirmation()
    {
        String message = "Are you sure you want to proceed?";
        return JOptionPane.showConfirmDialog(null, message);
    }

    /**
     * Ask user for new password
     * @return response of user (int)
     */
    protected void showNewPasswordInput()
    {
        String message = "Please enter new password.";
        String password = JOptionPane.showInputDialog(null, message);

        // null catches when user has canceled set, "" when user has not provided a string but has clicked OK
        if (password != null)
        {
            if (!password.equals(""))
            {
                passwordText = password;
            }
        }
        else
        {
            passwordText = null;
        }
    }

    /**
     * Show error message to user alerting that not all fields have been provided
     */
    protected void showErrorMessage()
    {
        String message = "Please fill out all fields.";
        JOptionPane.showMessageDialog(null, message);
    }

    /**
     * Check that user data is valid
     * @return boolean true = valid, false = invalid
     */
    protected boolean checkValidUser()
    {
        return !usernameText.getText().equals("") && passwordText!= null;
    }

    @Override
    VIEW_TYPE getEnum() {
        return view_type;
    }

    @Override
    void cleanUp()
    {
        setUsername("");
        setPermissions(new ArrayList<>(Arrays.asList(false,false,false,false)));
        passwordText = null;
    }
}
