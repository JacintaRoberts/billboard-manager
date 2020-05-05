package controlPanel;

import observer.Subject;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.synth.SynthTextAreaUI;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import controlPanel.Main.VIEW_TYPE;

public class BBCreateView extends AbstractGenericView
{
    // *** DECLARE VARIABLES**
    // --- Panels ---
    private JPanel drawingPadPanel;
    private JPanel billboardMenuPanel;
    private JPanel drawingToolsPanel;
    // --- Buttons ---
    private JButton createButton;
    private JButton exportButton;
    private JButton previewButton;
    private JButton cancelButton;
    private JButton importXMLButton;
    private JButton backgroundColourButton;
    private JButton titleButton;
    private JButton textButton;
    private JButton photoButton;
    private JButton scheduleButton;
    // --- Labels ---
    private JLabel designLabel;
    private JLabel titleLabel;
    private JLabel BBTextField;
    private JLabel photoLabel;
    // --- Fields ---
    private JTextField billboardNameField;
    // --- ENUM ---
    private VIEW_TYPE view_type;

    private JFrame textWindow;
    private JButton submitButton;
    private JTextArea textArea;
    private JFileChooser photoChooser;
    private JFileChooser xmlChooser;

    public BBCreateView()
    {
        super("Create Billboard");
        view_type = VIEW_TYPE.BB_CREATE;
    }

    @Override
    void createComponents()
    {
        // Drawing Pad Panel
        drawingPadPanel = new JPanel();
        drawingPadPanel.setLayout(new FlowLayout());
        titleLabel = new JLabel("");
        titleLabel.setFont(titleLabel.getFont().deriveFont(64f));
        titleLabel.setForeground(Color.white);
        BBTextField = new JLabel("");
        BBTextField.setFont(titleLabel.getFont().deriveFont(64f));
        BBTextField.setForeground(Color.white);
        photoLabel = new JLabel("");

        drawingPadPanel.add(titleLabel);
        drawingPadPanel.add(BBTextField);
        drawingPadPanel.add(photoLabel);
        getContentPane().add(drawingPadPanel, BorderLayout.CENTER);

        // Drawing Tool Bar Panel
        drawingToolsPanel = new JPanel();
        drawingToolsPanel.setLayout(new GridLayout(6,1));
        designLabel = new JLabel("Design Billboard");
        backgroundColourButton = new JButton("Background Colour");
        titleButton = new JButton("Title");
        textButton = new JButton("Text");
        photoButton = new JButton("Photo");
        billboardNameField = new JTextField("Add Billboard Name");
        drawingToolsPanel.add(designLabel);
        drawingToolsPanel.add(billboardNameField);
        drawingToolsPanel.add(backgroundColourButton);
        drawingToolsPanel.add(titleButton);
        drawingToolsPanel.add(textButton);
        drawingToolsPanel.add(photoButton);
        getContentPane().add(drawingToolsPanel, BorderLayout.WEST);

        // Billboard Options Menu Panel
        billboardMenuPanel = new JPanel();
        billboardMenuPanel.setLayout(new GridLayout(3,1));
        importXMLButton = new JButton("Import XML");
        exportButton = new JButton("Export");
        previewButton = new JButton("Preview");
        billboardMenuPanel.add(importXMLButton);
        billboardMenuPanel.add(exportButton);
        billboardMenuPanel.add(previewButton);
        getContentPane().add(billboardMenuPanel, BorderLayout.EAST);

        cancelButton = new JButton("Cancel");
        scheduleButton = new JButton("Schedule");
        createButton = new JButton("Create");
        JPanel navPanel = getNavPanel();
        navPanel.add(cancelButton);
        navPanel.add(createButton);
        navPanel.add(scheduleButton);

        photoChooser = new JFileChooser();
        xmlChooser = new JFileChooser();
    }

    protected void setBBText(String text)
    {
        BBTextField.setText(text);
    }

    protected void setColour(Color colour)
    {
        drawingPadPanel.setBackground(colour);
    }

    protected void setBBTitle(String titleString)
    {
        titleLabel.setText(titleString);
    }


    @Override
    void cleanUp() {

    }

    @Override
    public void update(Subject s) {

    }

    @Override
    VIEW_TYPE getEnum() {
        return view_type;
    }

    protected Color showColorChooser()
    {
        return JColorChooser.showDialog(null, "Choose a Color", drawingPadPanel.getForeground());
    }

    protected String showBBTitleChooser()
    {
        return JOptionPane.showInputDialog(null, "Provide Billboard Title");
    }

    protected String showBBTextChooser()
    {
        return JOptionPane.showInputDialog(null, "Set Billboard Text");
    }

    protected void browsePhotos() throws IOException {
        Image myPicture;
        ImageIcon icon;
        int value = photoChooser.showSaveDialog(null);
        if(value == JFileChooser.APPROVE_OPTION){
            String photoPath = photoChooser.getSelectedFile().getAbsolutePath();
            myPicture = ImageIO.read(new File(photoPath));
            myPicture = myPicture.getScaledInstance(500,500,Image.SCALE_DEFAULT);
            icon = new ImageIcon(myPicture);
            photoLabel.setIcon(icon);
        }
    }

    protected void BBXLMExport()
    {

    }

    protected void browseXMLImport()
    {
        xmlChooser.showSaveDialog(null);
    }

    protected void addScheduleButtonListener(MouseListener listener)
    {
        scheduleButton.addMouseListener(listener);
    }

    protected void addBBBackgroundColourListener(MouseListener listener)
    {
        backgroundColourButton.addMouseListener(listener);
    }

    protected void addBBTitleListener(MouseListener listener)
    {
        titleButton.addMouseListener(listener);
    }

    protected void addBBTextListener(MouseListener listener)
    {
        textButton.addMouseListener(listener);
    }

    protected void addBBPhotoListener(MouseListener listener)
    {
        photoButton.addMouseListener(listener);
    }

    protected void addBBXMLImportListener(MouseListener listener)
    {
        importXMLButton.addMouseListener(listener);
    }

    protected void addXMLExportListener(MouseListener listener) {exportButton.addMouseListener(listener);}

}
