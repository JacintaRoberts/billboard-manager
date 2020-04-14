//package controlPanel;
//
//import observer.Subject;
//
//import javax.swing.*;
//import java.awt.*;
//
//public class ListBBView extends AbstractGenericView
//{
//    // *** VARIABLES**
//    // --- Panels ---
//    private JPanel listPanel;
//    // --- Buttons ---
//    private JButton billboardsButton;
//    private JButton createBillboardButton;
//    // -- Colour Panel --
//    private JColorChooser colorChooser;
//
//    /**
//     * Constructor for creating the Views of the application. The constructor sets the frame's name and set's up the
//     * View by defining Width and Height, default close operation and the Layout.
//     */
//    public ListBBView() {
//        super("List Billboards View");
//    }
//
//    @Override
//    void createComponents()
//    {
//        listPanel = new JPanel();
//        listPanel.setLayout(new FlowLayout());
//        billboardsButton = new JButton("List Billboards");
//        createBillboardButton = new JButton("Create Billboard");
//        listPanel.add(billboardsButton);
//        listPanel.add(createBillboardButton);
//        colorChooser = new JColorChooser();
//        listPanel.add(colorChooser);
//        getContentPane().add(listPanel, BorderLayout.CENTER);
//
//        // add profile and home panel
//        addProfilePanel();
//        addHomePanel();
//    }
//
//    @Override
//    public void update(Subject s)
//    {
//
//    }
//}
