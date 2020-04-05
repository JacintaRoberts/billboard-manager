package controlPanel;

import observer.Subject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class HomeView extends ControlPanelView
{
    // *** VARIABLES**
    // Purpose: define variables for home frame
    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;
    // --- Panels ---
    private JPanel optionsPanel;
    private JPanel profilePanel;
    private JPanel homePanel;
    // --- Buttons ---
    private JButton usersButton;
    private JButton scheduleButton;
    private JButton billboardButton;
    private JButton homeButton;
    private JButton profileButton;
    // --- Labels ---
    private JLabel welcomeText;

    // *** CONSTRUCTOR ***
    public HomeView()
    {
        super("Control Panel");
        createComponents();
    }

    private void createComponents()
    {
        // *** HomeView FRAME ***
        // Purpose: size, close operation and layout
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // *** OPTIONS PANEL ***
        // Purpose:
        // 1. Create Options Panel
        // 2. Set Layout
        // 2. Add Buttons (User, Schedule, Billboard) to Panel
        // 3. Add Panel to Frame
        optionsPanel = new JPanel();
        optionsPanel.setLayout(new FlowLayout());
        usersButton = new JButton("Users");
        scheduleButton = new JButton("Schedule");
        billboardButton = new JButton("Billboard");
        optionsPanel.add(usersButton);
        optionsPanel.add(scheduleButton);
        optionsPanel.add(billboardButton);
        getContentPane().add(optionsPanel, BorderLayout.CENTER);

        // *** PROFILE PANEL ***
        // Purpose:
        // 1. Create Profile Panel
        // 2. Add 'View Profile' Button
        // 3. Add 'Welcome <name>' Text
        // 3. Add Panel to Frame
        profilePanel = new JPanel();
        optionsPanel.setLayout(new FlowLayout());
        welcomeText = new JLabel("Welcome");
        profileButton = new JButton("View Profile");
        profilePanel.add(profileButton);
        profilePanel.add(welcomeText);
        getContentPane().add(profilePanel, BorderLayout.NORTH);

        // *** HomeView PANEL ***
        // Purpose:
        // 1. Create HomeView Panel
        // 2. Add 'HomeView' Button
        // 3. Add Panel to Frame
        homePanel = new JPanel();
        homeButton = new JButton("HomeView");
        homePanel.add(homeButton);
        getContentPane().add(homePanel, BorderLayout.SOUTH);
    }

    @Override
    public void update(Subject s)
    {
        Model model = (Model) s;
        System.out.println("Update - home pushed");
    }

    public void addHomeListener(ActionListener listener)
    {
        homeButton.addActionListener(listener);
    }
}
