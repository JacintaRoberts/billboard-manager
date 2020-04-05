package controlPanel;

import observer.Subject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;


public class BillboardsView extends ControlPanelView
{
    // *** VARIABLES**
    // --- Panels ---
    private JPanel optionsPanel;
    // --- Buttons ---
    private JButton billboardsButton;
    private JButton createBillboardButton;
    // -- Colour Panel --
    private JColorChooser colorChooser;

    /**
     * Constructor for creating the Views of the application. The constructor sets the frame's name and set's up the
     * View by defining Width and Height, default close operation and the Layout.
     */
    public BillboardsView() {
        super("Billboard View");
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
        colorChooser = new JColorChooser();
        optionsPanel.add(colorChooser);
        getContentPane().add(optionsPanel, BorderLayout.CENTER);

        // add profile and home panel
        addProfilePanel();
        addHomePanel();
    }

//    public void colourSelectorPopup()
//    {
//        colorChooser = new JColorChooser();
//    }

    /**
     * Update is used when model is updated and view needs to change accordingly!
     * @param s The subject that has been updated.
     */
    @Override
    public void update(Subject s)
    {

    }

    public void addCreateButtonListener(MouseListener listener)
    {
        createBillboardButton.addMouseListener(listener);
    }
}
