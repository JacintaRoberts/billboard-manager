package controlPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * Abstract List View is designed to create the main structure for displaying the User and Billboard data in a scrollable
 * list. Child classes extend this structure and can customise  for the specific data employed.
 */
public abstract class AbstractListView extends AbstractGenericView
{
    // *** VARIABLES**
    // --- Panels ---
    private JScrollPane scrollPane;
    private JPanel mainPanel;
    // --- Label ---
    private static JLabel text;
    private static JLabel titleScreen;
    // --- Array ---
    public static ArrayList<JPanel> jPanels = new ArrayList<>();
    // --- GBC ---
    private GridBagConstraints gbc;
    private GridBagConstraints gbc_main;

    /**
     * Constructor to set up JFrame with provided name and create GUI componentes
     * @param frame_name name of JFrame
     */
    public AbstractListView(String frame_name) {
        super(frame_name);
    }

    /**
     * Create View Components which include panels, buttons, text etc. These components make up the view/JFrame seen
     * by the user.
     */
    @Override
    void createComponents()
    {
        // create main and scroll pane
        mainPanel = new JPanel();
        scrollPane = new JScrollPane(mainPanel);
        // define main panel layout
        mainPanel.setLayout(new GridBagLayout());
        // instantiate gbc for main panel
        gbc_main = new GridBagConstraints();
        // create title screen label
        titleScreen = new JLabel("LIST BILLBOARDS");
        titleScreen.setForeground(Color.WHITE);
        titleScreen.setFont(titleScreen.getFont().deriveFont(60f));
        // add title screen title to main panel
        mainPanel.add(titleScreen, setGBC(gbc_main,1,1,1,1));
        // add scroll pane to frame
        getContentPane().add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Set List Title which will be displayed on panel.
     * Used for BB List and User List
     * @param titleName The string title to be set for the panel
     */
    protected void setListTitle(String titleName)
    {
        titleScreen.setText(titleName);
    }

    /**
     * Add content dynamically to the list panel. It's design is to associate the added buttons with the correct listeners.
     * @param contentArray array of content (either BB names or usernames)
     * @param editMouseListener edit listener
     * @param deleteMouseListener delete listener
     * @param viewMouseListener view content listener
     */
    protected void addContent(ArrayList<String> contentArray, MouseListener editMouseListener, MouseListener deleteMouseListener, MouseListener viewMouseListener)
    {
        int index = 1;
        // looping through all content names (i.e. bbname or username)
        for (String contentName : contentArray)
        {
            // increase index
            index ++;

            // instantiate new gbc
            gbc = new GridBagConstraints();

            // create one panel per piece of information (i.e. User or BB)
            JPanel contentPanel = new JPanel();
            contentPanel.setLayout(new GridBagLayout());

            // add Edit, Delete and View Button
            JButton editButton = new JButton("Edit");
            JButton deleteButton = new JButton("Delete");
            JButton viewButton = new JButton("View");

            // add content name to buttons
            editButton.setName(contentName);
            deleteButton.setName(contentName);
            viewButton.setName(contentName);

            // add listeners for edit delete and view buttons
            editButton.addMouseListener(editMouseListener);
            deleteButton.addMouseListener(deleteMouseListener);
            viewButton.addMouseListener(viewMouseListener);

            // create name and label
            text = new JLabel(contentName);
            text.setPreferredSize(new Dimension(500, 60));

            // add to content panel defining location in gbc
            gbc.insets = new Insets(5,50,5,50);
            contentPanel.add(text, setGBC(gbc, 2,1,10,1));
            gbc.insets = new Insets(5,10,5,10);
            contentPanel.add(editButton, setGBC(gbc, 12,1,2,1));
            contentPanel.add(deleteButton, setGBC(gbc, 14,1,2,1));
            contentPanel.add(viewButton, setGBC(gbc, 16,1,2,1));

            // add content pane to array list
            jPanels.add(contentPanel);

            // add content panel to list panel
            mainPanel.add(contentPanel, setGBC(gbc_main,1,index,1,1));
        }
    }

    /**
     * Show Dialog to ask user to confirm deleting object
     * @return integer to confirm whether they press the Confirm button (0= confirm, otherwise not confirmed)
     */
    protected int showDeleteContentConfirmation()
    {
        return JOptionPane.showConfirmDialog(null, "Are you sure you want to Delete?");
    }

    /**
     * Remove everything upon leaving screen. Each time the list of content may be different so this
     * information should not be persistent whilst hidden.
     */
    @Override
    void cleanUp()
    {
        // for all jpanels in the view, remove and clear
        for (JPanel jPanel : jPanels)
        {
            mainPanel.remove(jPanel);
        }
        // clear jpanels array
        jPanels.clear();
    }

}
