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
        usernameText.setEditable(false);
        passwordText = null;
        addSubmitButton();
        addSetPasswordButton();
        title.setText("EDIT USER");
    }

    protected void addSubmitButton()
    {
        submitButton = new JButton("Submit New Permissions");
        JPanel navPanel = getNavPanel();
        GridBagConstraints gbc = getNavGBCPanel();
        navPanel.add(submitButton, setGBC(gbc,3,1,1,1));

    }

    private void addSetPasswordButton()
    {
        JPanel userPanel = getUserPanel();
        GridBagConstraints gbc = getUserPanelGBC();
        setPasswordButton = new JButton("Update Password");
        userPanel.add(setPasswordButton, setGBC(gbc,2,5,1,1));
    }

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
    protected int showUserConfirmation()
    {
        String message = "Are you sure you want to proceed?";
        return JOptionPane.showConfirmDialog(null, message);
    }

    /**
     * Ask user for new password
     * @return response of user (int)
     */
    protected String showNewPasswordInput()
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
            else
            {
                passwordText = null;
            }
        }
        else
        {
            passwordText = null;
        }
        return passwordText;
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
