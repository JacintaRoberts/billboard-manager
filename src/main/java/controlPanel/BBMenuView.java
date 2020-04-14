package controlPanel;

import observer.Subject;
import controlPanel.Main.VIEW_TYPE;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;


public class BBMenuView extends AbstractGenericView
{
    // *** VARIABLES**
    // --- Panels ---
    private JPanel optionsPanel;
    // --- Buttons ---
    private JButton billboardsButton;
    private JButton createBillboardButton;
    // --- ENUM ---
    private VIEW_TYPE view_type;

    /**
     * Constructor for creating the Views of the application. The constructor sets the frame's name and set's up the
     * View by defining Width and Height, default close operation and the Layout.
     */
    public BBMenuView() {
        super("Billboard View");
        this.view_type = VIEW_TYPE.BB_MENU;
    }

    @Override
    void createComponents()
    {
        optionsPanel = new JPanel();
        optionsPanel.setLayout(new FlowLayout());
        billboardsButton = new JButton("List Billboards");
        createBillboardButton = new JButton("Create Billboard");
        optionsPanel.add(billboardsButton);
        optionsPanel.add(createBillboardButton);
        getContentPane().add(optionsPanel, BorderLayout.CENTER);
    }

    /**
     * Update is used when model is updated and view needs to change accordingly!
     * @param s The subject that has been updated.
     */
    @Override
    public void update(Subject s)
    {

    }

    public void addBBCreateButtonListener(MouseListener listener)
    {
        createBillboardButton.addMouseListener(listener);
    }

    @Override
    VIEW_TYPE getEnum()
    {
        return view_type;
    }
}
