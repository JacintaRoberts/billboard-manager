package controlPanel;

import observer.Observer;

import javax.swing.*;
import java.awt.*;
import java.awt.Font;

public abstract class AbstractView extends JFrame implements Observer
{
    // *** DECLARE VARIABLES**

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

        // scroll pane
//        UIManager.put("ScrollPane.font", font);
//        UIManager.put("ScrollPane.background", navyColour);
//        UIManager.put("ScrollPane.foreground",Color.WHITE);

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

    abstract Main.VIEW_TYPE getEnum();

    abstract void cleanUp();

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

    //---------------------------------- POP-UP WINDOWS ------------------------------

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

}
