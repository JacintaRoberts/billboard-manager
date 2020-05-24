package controlPanel;

import controlPanel.Main.VIEW_TYPE;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * View designed for editing users.
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



    public UserEditView()
    {
        super("Edit User");
        view_type = VIEW_TYPE.USER_EDIT;
        setEditable(true);
        passwordText = "";
        addSubmitButton();
    }

    protected void addSubmitButton()
    {
        submitButton = new JButton("Submit");
        setPasswordButton = new JButton("Set Password");
        JPanel navPanel = getNavPanel();
        GridBagConstraints gbc = getNavGBCPanel();
        navPanel.add(submitButton, setGBC(gbc,3,1,1,1));
        navPanel.add(setPasswordButton, setGBC(gbc,3,1,1,1));
    }

    protected ArrayList<Object> getUserInfo()
    {
        ArrayList<Object> userInfoArray = new ArrayList<>();
        userInfoArray.add(usernameText.getText());
        userInfoArray.add(passwordText);
        userInfoArray.add(editBBPermission.isSelected());
        userInfoArray.add(scheduleBBPermission.isSelected());
        userInfoArray.add(editUsersPermission.isSelected());
        userInfoArray.add(createBBPermission.isSelected());
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
        System.out.println(password);
        if (!password.equals(""))
        {
            System.out.println("Set password");
            passwordText = password;
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
        return !usernameText.getText().equals("") && !passwordText.equals("");
    }

    @Override
    VIEW_TYPE getEnum() {
        return view_type;
    }
    
}
