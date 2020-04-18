package controlPanel;

import observer.Subject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class AbstractListView extends AbstractGenericView
{
    // *** VARIABLES**
    // --- Panels ---
    private JPanel listPanel;
    // --- Label ---
    private static JLabel label;
    private static JLabel text;
    // --- Array ---
    public static ArrayList<JPanel> jPanels = new ArrayList<>();


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

    protected void addContent(String[] contentArray, MouseListener editMouseListener, MouseListener deleteMouseListener, MouseListener viewMouseListener)
    {
        for (String contentName : contentArray)
        {
            // create one panel per piece of information (i.e. User or BB)
            JPanel contentPanel = new JPanel();
            contentPanel.setLayout(new GridLayout(1,4));

            // add Edit, Delete and View Button
            JButton editButton = new JButton("Edit");
            JButton deleteButton = new JButton("Delete");
            JButton viewButton = new JButton("View");

            // add content name to buttons
            editButton.setName(contentName);
            deleteButton.setName(contentName);
            viewButton.setName(contentName);

            // TODO: Check that this is OK - I have put listener in here which is referenced in the control panel by
            // TODO: another listener. This was due to the fact that the panels are created dynamically and hence listeners
            // TODO: are required to be attached to the buttons dynamically - this is the only solution I found!!
            // add listener
            editButton.addMouseListener(editMouseListener);
            deleteButton.addMouseListener(deleteMouseListener);
            viewButton.addMouseListener(viewMouseListener);

            // create name and label
            label = new JLabel("Name:");
            text = new JLabel(contentName);

            // add to content panel
            contentPanel.add(label);
            contentPanel.add(text);
            contentPanel.add(editButton);
            contentPanel.add(deleteButton);
            contentPanel.add(viewButton);

            jPanels.add(contentPanel);

            // add content panel to list panel
            listPanel.add(contentPanel);
        }
    }

    /**
     * Remove everything in list panel upon re-entering screen. Each time the list of content may be different so this
     * information should not be persistent whilst hidden.
     */
    protected void cleanUp()
    {
        listPanel.removeAll();
        listPanel.revalidate();
        listPanel.repaint();
    }
}
