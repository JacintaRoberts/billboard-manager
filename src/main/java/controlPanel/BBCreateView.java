package controlPanel;

import observer.Subject;

import javax.swing.*;
import java.awt.*;
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
    // --- Fields ---
    private JTextField billboardNameField;
    // --- ENUM ---
    private VIEW_TYPE view_type;

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
        getContentPane().add(drawingPadPanel, BorderLayout.CENTER);

        // Drawing Tool Bar Panel
        drawingToolsPanel = new JPanel();
        drawingToolsPanel.setLayout(new GridLayout(5,1));
        importXMLButton = new JButton("Import XML");
        backgroundColourButton = new JButton("Background Colour");
        titleButton = new JButton("Title");
        textButton = new JButton("Text");
        photoButton = new JButton("Photo");
        billboardNameField = new JTextField("Billboard Name");
        drawingToolsPanel.add(billboardNameField);
        drawingToolsPanel.add(backgroundColourButton);
        drawingToolsPanel.add(titleButton);
        drawingToolsPanel.add(textButton);
        drawingToolsPanel.add(photoButton);
        getContentPane().add(drawingToolsPanel, BorderLayout.WEST);

        // Billboard Options Menu Panel
        billboardMenuPanel = new JPanel();
        billboardMenuPanel.setLayout(new GridLayout(5,1));
        createButton = new JButton("Create");
        importXMLButton = new JButton("Import XML");
        exportButton = new JButton("Export");
        previewButton = new JButton("Preview");
        cancelButton = new JButton("Cancel");
        billboardMenuPanel.add(createButton);
        billboardMenuPanel.add(importXMLButton);
        billboardMenuPanel.add(exportButton);
        billboardMenuPanel.add(previewButton);
        billboardMenuPanel.add(cancelButton);
        getContentPane().add(billboardMenuPanel, BorderLayout.EAST);

//        colorChooser = new JColorChooser();
//        optionsPanel.add(colorChooser);
//        getContentPane().add(optionsPanel, BorderLayout.CENTER);
    }

    @Override
    public void update(Subject s) {

    }

    @Override
    VIEW_TYPE getEnum() {
        return view_type;
    }
}
