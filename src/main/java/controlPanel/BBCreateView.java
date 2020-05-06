package controlPanel;

import observer.Subject;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import controlPanel.Main.VIEW_TYPE;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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

    private String photoPath;
    private Image image;
    private ImageIcon icon;
    private String backgroundColour;
    private String titleColour;
    private String infoColour;

    private JFileChooser photoChooser;
    private JFileChooser xmlChooser;
    private JFileChooser xmlFolderChooser;
    private String xmlExportPath;
    private String xmlImportPath;

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
        // set default background colour
        backgroundColour = toHexString(Color.CYAN);
        drawingPadPanel.setBackground(Color.decode(backgroundColour));

        titleColour = toHexString(Color.BLACK);
        titleLabel.setForeground(Color.decode(titleColour));

        infoColour = toHexString(Color.WHITE);
        BBTextField.setForeground(Color.decode(infoColour));

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
        xmlChooser.setCurrentDirectory(new java.io.File("."));
        xmlChooser.setDialogTitle("Select XML File to upload");
        FileNameExtensionFilter xmlfilter = new FileNameExtensionFilter("xml files (*.xml)", "xml");
        xmlChooser.setFileFilter(xmlfilter);

        xmlFolderChooser = new JFileChooser();
        xmlFolderChooser.setCurrentDirectory(new java.io.File("."));
        xmlFolderChooser.setDialogTitle("Select Folder to Save XML");
        xmlFolderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    }

    private static String toHexString(Color c) {
        StringBuilder sb = new StringBuilder("#");

        if (c.getRed() < 16) sb.append('0');
        sb.append(Integer.toHexString(c.getRed()));

        if (c.getGreen() < 16) sb.append('0');
        sb.append(Integer.toHexString(c.getGreen()));

        if (c.getBlue() < 16) sb.append('0');
        sb.append(Integer.toHexString(c.getBlue()));

        return sb.toString();
    }

    protected void setBBText(String text)
    {
        BBTextField.setText(text);
    }

    protected void setBackgroundColour(String colour)
    {
        backgroundColour = colour;
        drawingPadPanel.setBackground(Color.decode(backgroundColour));
    }

    protected void setBBTitle(String titleString)
    {
        titleLabel.setText(titleString);
    }

    private void setPhoto(String url)
    {
    }

    private void setInfoColour(String colour)
    {
        System.out.println("info c: " + colour);
        titleLabel.setForeground(Color.decode(colour));
    }

    private void setTitleColour(String colour)
    {
        System.out.println("title c: " + colour);
        titleLabel.setForeground(Color.decode(colour));
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

    protected String showColorChooser()
    {
        Color colorSelect = JColorChooser.showDialog(null, "Choose a Color", drawingPadPanel.getForeground());
        return toHexString(colorSelect);
    }

    protected String showBBTitleChooser()
    {
        return JOptionPane.showInputDialog(null, "Provide Billboard Title");
    }

    protected String showBBTextChooser()
    {
        return JOptionPane.showInputDialog(null, "Set Billboard Text");
    }

    protected void browsePhotos() throws IOException
    {
        int value = photoChooser.showSaveDialog(null);
        if(value == JFileChooser.APPROVE_OPTION)
        {
            photoPath = photoChooser.getSelectedFile().getAbsolutePath();
            image = ImageIO.read(new File(photoPath)).getScaledInstance(500,500,Image.SCALE_DEFAULT);
            icon = new ImageIcon(image);
            photoLabel.setIcon(icon);
        }
    }

    protected void browseXMLImport() throws IOException, SAXException, ParserConfigurationException {
        int value = xmlChooser.showSaveDialog(null);
        if (value == JFileChooser.APPROVE_OPTION)
        {
            xmlImportPath = xmlChooser.getSelectedFile().getAbsolutePath();
            xmlImport(xmlImportPath);
        }
    }

    private void xmlImport(String xmlImportPath) throws ParserConfigurationException, IOException, SAXException {
        //creating a constructor of file class and parsing an XML file
        File file = new File(xmlImportPath);

        //an instance of factory that gives a document builder
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        //an instance of builder to parse the specified xml file
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(file);

        // normalise the XML structure
        doc.getDocumentElement().normalize();

        Element root = doc.getDocumentElement();
        setBackgroundColour(root.getAttribute("background"));

        NodeList titleList = doc.getElementsByTagName("message");
        Element titleNode = (Element) titleList.item(0);
        setTitleColour(titleNode.getAttribute("colour"));

        NodeList infoList = doc.getElementsByTagName("information");
        Element infoNode = (Element) infoList.item(0);
        setInfoColour(infoNode.getAttribute("colour"));

        NodeList photoList = doc.getElementsByTagName("picture");
        Element photoNode = (Element) photoList.item(0);
        setPhoto(photoNode.getAttribute("url"));
    }

    protected String enterXMLFileName()
    {
        return JOptionPane.showInputDialog(null, "Provide XML name:");
    }


    protected void browseExportFolder() throws TransformerException, ParserConfigurationException {
        int value = xmlFolderChooser.showSaveDialog(null);
        if(value == JFileChooser.APPROVE_OPTION)
        {
            xmlExportPath = xmlFolderChooser.getSelectedFile().getAbsolutePath();
            xmlExport(xmlExportPath);
        }
    }

    private void xmlExport(String xmlExportPath) throws ParserConfigurationException, TransformerException {
        String XMLFileName = enterXMLFileName();

        // create doc builder factory to build a new document
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        // create a new document
        Document doc = builder.newDocument();

        // create a root element and add it to the doc
        Element root = doc.createElement("billboard");
        root.setAttribute("background",backgroundColour);
        doc.appendChild(root);

        // append three child elements to the root element

        root.appendChild(createMessage(doc, titleColour, titleLabel.getText()));
        root.appendChild(createPicture(doc, photoPath));
        root.appendChild(createInfo(doc, infoColour,BBTextField.getText()));

        // create transformer which is designed to transform document to XML
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transf = transformerFactory.newTransformer();

        // set encoding type, indentation,
        transf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transf.setOutputProperty(OutputKeys.INDENT, "yes");

        // source contains the dom tree
        DOMSource source = new DOMSource(doc);

        File myFile = new File(xmlExportPath+"/"+XMLFileName+".xml");

        // write to console and file
        StreamResult console = new StreamResult(System.out);
        StreamResult file = new StreamResult(myFile);

        transf.transform(source, console);
        transf.transform(source, file);

        String messageDialog = "Successful Export of Billboard. Saved as " +XMLFileName +".xml";
        JOptionPane.showMessageDialog(null, messageDialog);
    }

    private Node createMessage(Document doc, String colour, String message) {

        Element user = doc.createElement("message");
        user.setAttribute("colour", colour);
        user.appendChild(doc.createTextNode(message));
        return user;
    }

    private Node createPicture(Document doc, String url) {

        Element user = doc.createElement("picture");
        user.setAttribute("url", url);
        return user;
    }

    private Node createInfo(Document doc, String colour, String info) {

        Element user = doc.createElement("information");
        user.setAttribute("colour", colour);
        user.appendChild(doc.createTextNode(info));
        return user;
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
