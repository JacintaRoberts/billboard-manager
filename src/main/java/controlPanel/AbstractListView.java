package controlPanel;

import observer.Subject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.util.HashMap;

public abstract class AbstractListView extends AbstractGenericView
{
    // *** VARIABLES**
    // --- Panels ---
    private JPanel listPanel;
    // --- Label ---
    private static JLabel label;
    private static JLabel text;
    // --- HashMap ---
    public static HashMap<String, JButton> editButtonsMap = new HashMap<>();


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
            // buttons
            JButton editButton = new JButton("Edit");
            editButton.setName(content);
            editButtonsMap.put(content, editButton);
            JButton deleteButton = new JButton("Delete");
            // labels
            label = new JLabel("Name:");
            text = new JLabel(content);
            // add to content panel
            contentPanel.add(label);
            contentPanel.add(text);
            contentPanel.add(editButton);
            contentPanel.add(deleteButton);
            // add content panel to list panel
            listPanel.add(contentPanel);
        }
    }

//    protected void addEditContentListener(MouseListener listener)
//    {
//        System.out.println("editButtonsMap: " + editButtonsMap);
//        for (HashMap.Entry<String, JButton> entry : editButtonsMap.entrySet())
//        {
//            // to navigate to edit user screen
//            entry.getValue().addMouseListener(listener);
//            System.out.println("button: " + entry.getValue());
//        }
//
//    }
}
