package controlPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.Font;

/**
 * Abstract View defines the generic style of the application, abstract methods in which child classes are required to
 * extend and frame set up common to all views. Every view is a child class of AbstractView.
 */
public abstract class AbstractView extends JFrame
{

    /**
     * Abstract view constructor with the general look of the application views and the frame set up
     * @param frame_name frame name string
     */
    public AbstractView(String frame_name)
    {
        // assign frame name
        super(frame_name);
        Font font = new Font("Garamond",  Font.BOLD, 30);
        Color pinkColour = new Color(255,87,87);
        Color navyColour = new Color(31,29, 41);

        // label colour
        UIManager.put("Label.font", font);
        UIManager.put("Label.foreground", pinkColour);

        // check box colour
        UIManager.put("CheckBox.font", font);
        UIManager.put("CheckBox.foreground", Color.WHITE);

        // option pane
        UIManager.put("OptionPane.font", font);
        UIManager.put("OptionPane.messageFont", font);
        UIManager.put("OptionPane.foreground", Color.WHITE);
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
        UIManager.put("OptionPane.background", navyColour);

        // text field pane
        UIManager.put("TextField.font", font);
        UIManager.put("TextField.background", Color.DARK_GRAY);
        UIManager.put("TextField.foreground", Color.WHITE);

        // password field pane
        UIManager.put("PasswordField.font", font);
        UIManager.put("PasswordField.background", Color.DARK_GRAY);
        UIManager.put("PasswordField.foreground", Color.WHITE);

        // buttons
        UIManager.put("Button.font", font);
        UIManager.put("Button.background", Color.WHITE);
        UIManager.put("Button.foreground",Color.BLACK);

        // radio buttons
        UIManager.put("RadioButton.font", font);
        UIManager.put("RadioButton.background", navyColour);
        UIManager.put("RadioButton.foreground",Color.WHITE);

        // radio menu item buttons
        UIManager.put("RadioButtonMenuItem.font", font);
        UIManager.put("RadioButtonMenuItem.background", navyColour);
        UIManager.put("RadioButtonMenuItem.foreground",Color.WHITE);

        // combobox
        UIManager.put("ComboBox.font", font);
        UIManager.put("ComboBox.foreground",Color.BLACK);

        // checkbox
        UIManager.put("CheckBox.font", font);
        UIManager.put("CheckBox.background", navyColour);
        UIManager.put("CheckBox.foreground",Color.WHITE);

        // table
        Font tableFont = new Font("Garamond",  Font.BOLD, 20);
        UIManager.put("Table.font", tableFont);
        UIManager.put("Table.background", Color.WHITE);
        UIManager.put("Table.foreground",Color.BLACK);

        // table header
        Font tableHeaderFont = new Font("Garamond",  Font.BOLD, 25);
        UIManager.put("TableHeader.font", tableHeaderFont);
        UIManager.put("TableHeader.background", Color.WHITE);
        UIManager.put("TableHeader.foreground",pinkColour);

        UIManager.put("TitledBorder.font",font);
        UIManager.put("TitledBorder.titleColor",Color.WHITE);

        // panel colour
        UIManager.put("Panel.background", navyColour);

        setupFrame();
    }

    /**
     * Set the grid bag constraints based on the gbc and values provided
     * @param gbc current gbc
     * @param x x value
     * @param y y value
     * @param w width value
     * @param h height value
     * @return new gbc
     */
    protected static GridBagConstraints setGBC(GridBagConstraints gbc, int x, int y, int w, int h)
    {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridheight = h;
        gbc.gridwidth = w;
        return gbc;
    }

    /**
     * Set up Frame by setting size, close operation and layout.
     */
    private void setupFrame()
    {
        // Purpose: size, close operation and layout
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setState(Frame.NORMAL);
        setLocationRelativeTo(null);
    }

    /**
     * Abstract Get Enum method designed to return the enum assigned to the view
     */
    abstract Main.VIEW_TYPE getEnum();

    /**
     * Remove everything upon leaving screen. This is to ensure data does not persist.
     */
    abstract void cleanUp();

    //---------------------------------- POP-UP WINDOWS ------------------------------

    /**
     * Pop-up window to handle fatal error occurred (if something goes drastically wrong on the server)
     */
    protected void showFatalError() {
        String message = "A fatal error has occurred - restart control panel.";
        JOptionPane.showMessageDialog(null, message);
    }

    /**
     * Pop-up window to handle NoSuchUser ServerAcknowledgement
     */
    protected void showNoSuchUserException() {
        String message = "Username does not exist.";
        JOptionPane.showMessageDialog(null, message);
    }

    /**
     * Pop-up window to handle InvalidToken ServerAcknowledgement
     */
    protected void showInvalidTokenException() {
        String message = "Expired token. Please login to your account again.";
        JOptionPane.showMessageDialog(null, message);
    }

    /**
     * Pop-up window to handle InsufficientPermission ServerAcknowledgement
     */
    public void showInsufficientPermissionsException() {
        String message = "You do not have the necessary permissions to perform this action.";
        JOptionPane.showMessageDialog(null, message);
    }

    /**
     * Pop-up window to let the user know the creation event was successful
     */
    protected void showCreateSuccess() {
        String message = "Creation was successful.";
        JOptionPane.showMessageDialog(null, message);
    }

    /**
     * Pop-up window to let the user know that create user failed because that username already exists
     */
    protected void showUsernamePrimaryKeyClashException() {
        String message = "A user with that username already exists, input a different username!";
        JOptionPane.showMessageDialog(null, message);
    }

    /**
     * Pop-up window to let the user know the deletion event was successful
     */
    protected void showDeleteSuccess() {
        String message = "Deletion was successful.";
        JOptionPane.showMessageDialog(null, message);
    }

    /**
     * Pop-up window to let the user know that deletion failed because they cannot delete their own user
     */
    protected void showCannotDeleteSelfException() {
        String message = "You cannot delete your own user!";
        JOptionPane.showMessageDialog(null, message);
    }

    /**
     * Pop-up window to let the user know the logout was successful
     */
    protected void showLogoutSuccess() {
        String message = "Logout was successful.";
        JOptionPane.showMessageDialog(null, message);
    }

    /**
     * Pop-up window to let the user know the logout was successful
     */
    protected void showBadPasswordException() {
        String message = "Password was incorrect.";
        JOptionPane.showMessageDialog(null, message);
    }

    /**
     * Pop-up window to let the user know the user's permissions were successfully updated
     */
    protected void showEditPermissionsSuccess() {
        String message = "Permissions were successfully updated";
        JOptionPane.showMessageDialog(null, message);
    }

    /**
     * Pop-up window to let the user know the user's password was successfully updated
     */
    protected void showEditPasswordSuccess() {
        String message = "Password was successfully updated";
        JOptionPane.showMessageDialog(null, message);
    }

    /**
     * Pop-up window to let the user know that they cannot remove their own edit user permissions
     */
    protected void showCannotRemoveOwnAdminPermissionException() {
        String message = "You cannot remove your own edit user permissions (admin)!";
        JOptionPane.showMessageDialog(null, message);
    }
}
