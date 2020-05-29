package controlPanel;

import observer.Subject;
import controlPanel.Main.VIEW_TYPE;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;

/**
 * View to view the bb options - create/update bb or list bb
 */
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
    // --- GBC ---
    private GridBagConstraints gbc;
    // --- Labels ---
    private JLabel title;

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
        gbc = new GridBagConstraints();

        optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridBagLayout());

        title = new JLabel("BILLBOARD MENU");
        title.setForeground(Color.WHITE);
        title.setFont(title.getFont().deriveFont(60f));

        billboardsButton = new JButton("List Billboards");
        createBillboardButton = new JButton("Create Billboard");
        gbc.insets = new Insets(1,10,1,10);
        optionsPanel.add(billboardsButton, setGBC(gbc, 1,1,1,1));
        optionsPanel.add(createBillboardButton, setGBC(gbc, 3,1,1,1));
        gbc.insets = new Insets(250,1,1,1);
        optionsPanel.add(title, setGBC(gbc, 1,2,3,1));
        getContentPane().add(optionsPanel, BorderLayout.CENTER);
    }

    @Override
    void cleanUp() {

    }

    /**
     * Update is used when model is updated and view needs to change accordingly!
     * @param s The subject that has been updated.
     */
    @Override
    public void update(Subject s)
    {

    }

    /**
     * Add BB create button listener
     * @param listener listener
     */
    public void addBBCreateButtonListener(MouseListener listener)
    {
        createBillboardButton.addMouseListener(listener);
    }

    /**
     * Add BB list listener
     * @param listener listener
     */
    public void addBBListListener(MouseListener listener)
    {
        billboardsButton.addMouseListener(listener);
    }

    @Override
    VIEW_TYPE getEnum()
    {
        return view_type;
    }
}
