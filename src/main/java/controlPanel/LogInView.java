package controlPanel;


import observer.Subject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

public class LogInView extends ControlPanelView
{
    // *** DECLARE VARIABLES**
    // --- Panels ---
    private JPanel loginPanel;
    // --- Buttons ---
    private JButton submitButton;
    // --- Labels ---
    private JLabel usernameText;
    private JLabel passwordText;
    private JLabel errorText;
    // --- Fields ---
    private JTextField usernameField;
    private JTextField passwordField;

    /**
     * Constructor for LogIn View - set frame name
     */
    public LogInView()
    {
        super("LogInView Panel");
    }

    @Override
    void createComponents()
    {
        loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(3,2));
        usernameText = new JLabel("Username");
        usernameField = new JTextField();
        passwordText = new JLabel("Password");
        passwordField = new JTextField();
        submitButton = new JButton("Submit");
        errorText = new JLabel("Incorrect Credentials");
        loginPanel.add(usernameText);
        loginPanel.add(usernameField);
        loginPanel.add(passwordText);
        loginPanel.add(passwordField);
        loginPanel.add(errorText);
        loginPanel.add(submitButton);

        errorText.setVisible(false);
        getContentPane().add(loginPanel, BorderLayout.CENTER);
    }

    /**
     * Get username text field from frame
     * @return username string
     */
    public String getUsername()
    {
        return usernameField.getText();
    }

    /**
     * Get password text field from frame
     * @return password string
     */
    public String getPassword()
    {
        return passwordField.getText();
    }

    /**
     * Set error message visibility
     * @param visible true = visible, false = hidden
     */
    public void setErrorVisibility(boolean visible)
    {
        errorText.setVisible(visible);
    }

    @Override
    public void update(Subject s)
    {
        Model model = (Model) s;
        System.out.println("Update - submit pushed");
    }

    /**
     * Add listener to handle mouse click of submit button.
     * @param listener mouse click listener
     */
    public void addSubmitButtonListener(MouseListener listener)
    {
        submitButton.addMouseListener(listener);
    }
}
