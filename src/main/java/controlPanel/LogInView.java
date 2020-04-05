package controlPanel;


import observer.Subject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

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

    // *** CONSTRUCTOR ***
    public LogInView()
    {
        // set JFrame name
        super("LogInView Panel");
        createComponents();
    }

    private void createComponents()
    {
        // *** LOGIN PANEL ***
        // Purpose:
        // 1. Create Panel
        // 2. Add Labels (username and password)
        // 3. Add 2 Text Fields to enter info
        // 4. Add submit button
        // 5. Add Panel to Frame
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

    public String getUsername()
    {
        return usernameField.getText();
    }

    public String getPassword()
    {
        return passwordField.getText();
    }

    public boolean getVisibilityError()
    {
        return passwordField.isVisible();
    }

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

    public void addSubmitListener(ActionListener listener)
    {
        submitButton.addActionListener(listener);
    }
}
