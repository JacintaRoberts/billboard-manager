package controlPanel;

import controlPanel.Main.VIEW_TYPE;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * User Create View is designed to allow users to create new User accounts. A unique username, password and permissions
 * are required to create a new account. This extends the AbstractUserView to acquire key elements.
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

    /**
     * Constructor to set up JFrame with provided name and create GUI components
     * Set ENUM value allowing use in Controller Class
     */
    public UserCreateView()
    {
        super("Create User");
        // assign enum to frame
        view_type = VIEW_TYPE.USER_CREATE;
        // set user fields to be editable
        setUserFieldsEditable(true);
        // set password to null
        passwordText = null;
        // allow username text to be edited
        usernameText.setEditable(true);
        // add submit and password button
        addSubmitButton();
        addSetPasswordButton();
        // set title of screen
        title.setText("CREATE USER");
    }

    /**
     * Add submit button to nav panel
     */
    protected void addSubmitButton()
    {
        // create submit button
        submitButton = new JButton("Create User");
        // get nav panel and gbc
        JPanel navPanel = getNavPanel();
        GridBagConstraints gbc = getNavGBCPanel();
        // add submit button to nav panel
        navPanel.add(submitButton, setGBC(gbc,3,1,1,1));
    }

    /**
     * Add set password button to user panel
     */
    private void addSetPasswordButton()
    {
        // get user panel
        JPanel userPanel = getUserPanel();
        GridBagConstraints gbc = getUserPanelGBC();
        // create password button and add to user panel
        setPasswordButton = new JButton("Create Password");
        userPanel.add(setPasswordButton, setGBC(gbc,2,5,1,1));
    }

    /**
     * Get new user info provided by user
     * @return array list of user info
     */
    protected ArrayList<Object> getUserInfo()
    {
        // add all user info to the user array
        ArrayList<Object> userInfoArray = new ArrayList<>();
        userInfoArray.add(usernameText.getText());
        userInfoArray.add(passwordText);
        userInfoArray.add(createBBPermission.isSelected());
        userInfoArray.add(editBBPermission.isSelected());
        userInfoArray.add(scheduleBBPermission.isSelected());
        userInfoArray.add(editUsersPermission.isSelected());
        // return user array info
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
     * Ask User for confirmation of user creation
     * @return int response (0 = confirm, else not confirmed)
     */
    protected int showCreateUserConfirmation()
    {
        String message = "Are you sure you want to proceed?";
        return JOptionPane.showConfirmDialog(null, message);
    }

    /**
     * Ask user for new password and set if not null
     */
    protected void showNewPasswordInput()
    {
        String message = "Please enter new password.";
        String password = JOptionPane.showInputDialog(null, message);

        // null catches when user has canceled set, "" when user has not provided a string but has clicked OK
        if (password != null && !password.equals(""))
        {
            passwordText = password;
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
        String message = "Please fill out all fields correctly. Ensure 20 Alphanumerical characters for username only.";
        JOptionPane.showMessageDialog(null, message);
    }

    /**
     * Check that user data is valid
     * @return boolean true = valid, false = invalid
     */
    protected boolean checkValidUser()
    {
        String validCharacters = "([A-Za-z0-9-_ ]+)";
        boolean validlength = false;
        // if username is less than or equal to 20 chars, return valid length true
        if(usernameText.getText().length() <= 20){
            validlength = true;
        }
        // determine if name is valid and doe not contain any invalid chars
        boolean validName = usernameText.getText().matches(validCharacters);
        // return validity of username selected
        return !usernameText.getText().equals("") && passwordText!= null && validName && validlength;
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
        // reset username, permissions and password text
        setUsername("");
        setPermissions(new ArrayList<>(Arrays.asList(false,false,false,false)));
        passwordText = null;
    }
}
