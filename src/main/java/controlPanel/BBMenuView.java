package controlPanel;

import controlPanel.Main.VIEW_TYPE;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;

/**
 * Menu for navigating to the Billboard list or Billboard create/update view.
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
     * Constructor to set up JFrame with provided name and create GUI components
     * Set ENUM value allowing use in Controller Class
     */
    public BBMenuView() {
        super("Billboard View");
        this.view_type = VIEW_TYPE.BB_MENU;
    }

    /**
     * Create View Components which include panels, buttons, text etc. These components make up the view/JFrame seen
     * by the user.
     */
    @Override
    void createComponents()
    {
        // instantiate gbc
        gbc = new GridBagConstraints();

        // create options panel
        optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridBagLayout());

        // create title and define color
        title = new JLabel("BILLBOARD MENU");
        title.setForeground(Color.WHITE);
        title.setFont(title.getFont().deriveFont(60f));

        // create list bb and create bb button
        billboardsButton = new JButton("List Billboards");
        createBillboardButton = new JButton("Create Billboard");

        // add items to options panel with gbc layout
        gbc.insets = new Insets(1,10,1,10);
        optionsPanel.add(billboardsButton, setGBC(gbc, 1,1,1,1));
        optionsPanel.add(createBillboardButton, setGBC(gbc, 3,1,1,1));
        gbc.insets = new Insets(250,1,1,1);
        optionsPanel.add(title, setGBC(gbc, 1,2,3,1));

        // add options panel to frame
        getContentPane().add(optionsPanel, BorderLayout.CENTER);
    }

    /**
     * Clean Up all data that should not persist in the GUI. The view will be cleaned up after leaving the view.
     */
    @Override
    void cleanUp() {

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

    /**
     * Get Enum associated to this View. This is defined in the Constructor and is used in the Controller Class.
     * @return view type enum assigned to view
     */
    @Override
    VIEW_TYPE getEnum()
    {
        return view_type;
    }
}
