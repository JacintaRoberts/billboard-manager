package controlPanel;

import controlPanel.Main.VIEW_TYPE;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * View designed for editing users.
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

    protected void addSubmitButton()
    {
        submitButton = new JButton("Create User");
        JPanel navPanel = getNavPanel();
        GridBagConstraints gbc = getNavGBCPanel();
        navPanel.add(submitButton, setGBC(gbc,3,1,1,1));

    }

    private void addSetPasswordButton()
    {
        JPanel userPanel = getUserPanel();
        GridBagConstraints gbc = getUserPanelGBC();
        setPasswordButton = new JButton("Create Password");
        userPanel.add(setPasswordButton, setGBC(gbc,2,5,1,1));
    }

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

    public void addSubmitButtonListener(MouseListener listener)
    {
        submitButton.addMouseListener(listener);
    }

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

    protected void showErrorMessage()
    {
        String message = "Please fill out all fields.";
        JOptionPane.showMessageDialog(null, message);
    }

    /**
     * Check that user data is valid
     * @return boolean true = valid, false = invalid
     */
    // TODO: check that user's do not need to have any permissions selected
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
        System.out.println("REMOVE USER NAME ");
        setUsername("");
        setPermissions(new ArrayList<>(Arrays.asList(false,false,false,false)));
        passwordText = null;
    }

}
