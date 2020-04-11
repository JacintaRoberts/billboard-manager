package controlPanel;

import observer.Subject;

import javax.swing.*;
import java.awt.*;

public abstract class AbstractListView extends AbstractGenericView
{
    // *** VARIABLES**
    // --- Panels ---
    private JPanel listPanel;
    // --- Buttons ---
    private static JButton editButton;
    private static JButton deleteButton;
    // --- Label ---
    private static JLabel label;
    private static JLabel text;


    public AbstractListView(String frame_name)
    {
        super(frame_name);
    }

    @Override
    void createComponents()
    {
        listPanel = new JPanel();
        listPanel.setLayout(new GridLayout(10,1));
        getContentPane().add(listPanel, BorderLayout.CENTER);
    }

    protected void addContent(String[] contentArray)
    {
        for (String content : contentArray)
        {
            JPanel contentPanel = new JPanel();
            contentPanel.setLayout(new GridLayout(1,4));
            editButton = new JButton("Edit");
            deleteButton = new JButton("Delete");
            label = new JLabel("Name:");
            text = new JLabel(content);
            contentPanel.add(label);
            contentPanel.add(text);
            contentPanel.add(editButton);
            contentPanel.add(deleteButton);
            listPanel.add(contentPanel);
        }
    }
}
