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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import controlPanel.Main.VIEW_TYPE;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import viewer.Viewer;

import static viewer.Viewer.*;

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
    private JButton billboardNameButton;
    // --- Labels ---
    private JLabel photoLabel;
    private JLabel BBNameLabel;
    // --- ENUM ---
    private VIEW_TYPE view_type;

    private String photoPath;
    private Image image;
    private ImageIcon icon;
    private String backgroundColour;
    private String titleColour;
    private String infoColour;

    private JTextArea titleLabel;
    private JTextArea BBTextField;

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
        titleLabel = new JTextArea("");
        titleLabel.setFont(titleLabel.getFont().deriveFont(80f));
        titleLabel.setForeground(Color.white);
        titleLabel.setOpaque(false);
        BBTextField = new JTextArea("");
        BBTextField.setFont(BBTextField.getFont().deriveFont(50f));
        BBTextField.setForeground(Color.white);
        BBTextField.setOpaque(false);
        photoLabel = new JLabel();

        drawingPadPanel.add(titleLabel);
        drawingPadPanel.add(BBTextField);
        drawingPadPanel.add(photoLabel);
        getContentPane().add(drawingPadPanel, BorderLayout.CENTER);

        // Drawing Tool Bar Panel
        drawingToolsPanel = new JPanel();
        drawingToolsPanel.setLayout(new GridLayout(6,1));
        billboardNameButton = new JButton("Billboard Name");
        BBNameLabel = new JLabel("");
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
        photoPath = null;

        drawingToolsPanel.add(BBNameLabel);
        drawingToolsPanel.add(billboardNameButton);
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
        createButton = new JButton("Create");
        JPanel navPanel = getNavPanel();
        navPanel.add(cancelButton);
        navPanel.add(createButton);

        photoChooser = new JFileChooser();
        photoChooser.setCurrentDirectory(new java.io.File("."));
        photoChooser.setDialogTitle("Select Photo to upload");
        FileNameExtensionFilter photofilter = new FileNameExtensionFilter("image files (*.bmp, *jpg, *png)", "jpg","png", "bmp");
        photoChooser.setFileFilter(photofilter);

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

    protected void setBBTextColour(String colour)
    {
        infoColour = colour;
        BBTextField.setForeground(Color.decode(infoColour));
    }

    protected String getBBTextColour(String colour)
    {
        return infoColour;
    }

    protected void setBBTitleColour(String colour)
    {
        titleColour = colour;
        titleLabel.setForeground(Color.decode(titleColour));
    }

    protected String getBBTitleColour(String colour)
    {
        return titleColour;
    }

    protected void setBBName(String name)
    {
        BBNameLabel.setText(name);
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

    protected String browseTitleColour()
    {
        Color colorSelect = JColorChooser.showDialog(null, "Choose a Colour for Title", drawingPadPanel.getForeground());
        return toHexString(colorSelect);
    }

    protected String browseTextColour()
    {
        Color colorSelect = JColorChooser.showDialog(null, "Choose a Colour for Text", drawingPadPanel.getForeground());
        return toHexString(colorSelect);
    }

//    private void setPhoto(String url)
//    {
//         new ImageIcon(this.getClass().getResource(url));
//    }

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

    protected String showBBNameChooser() { return JOptionPane.showInputDialog(null, "Provide Billboard Name");}

    protected String showBBTitleChooser()
    {
        return JOptionPane.showInputDialog(null, "Provide Billboard Title");
    }

    protected String showBBTextChooser()
    {
        return JOptionPane.showInputDialog(null, "Set Billboard Text");
    }

    protected ImageIcon browsePhotos() throws IOException
    {
        icon = null;
        int value = photoChooser.showSaveDialog(null);
        if(value == JFileChooser.APPROVE_OPTION)
        {
            photoPath = photoChooser.getSelectedFile().getAbsolutePath();
            image = ImageIO.read(new File(photoPath)).getScaledInstance(500,500,Image.SCALE_DEFAULT);
            icon = new ImageIcon(image);
        }
        return icon;
    }

    protected void setPhoto(ImageIcon icon)
    {
        photoLabel.setIcon(icon);
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
        setBBTitle(titleNode.getTextContent());
        setTitleColour(titleNode.getAttribute("colour"));

        NodeList infoList = doc.getElementsByTagName("information");
        Element infoNode = (Element) infoList.item(0);
        setBBText(infoNode.getTextContent());
        setInfoColour(infoNode.getAttribute("colour"));

        NodeList photoList = doc.getElementsByTagName("picture");
        Element photoNode = (Element) photoList.item(0);
        photoPath = photoNode.getAttribute("url");
        System.out.println(photoPath);
        Image photoImage = ImageIO.read(new File(photoPath)).getScaledInstance(500,500,Image.SCALE_DEFAULT);
        setPhoto(new ImageIcon(photoImage));
    }

    protected String enterXMLFileName()
    {
        return JOptionPane.showInputDialog(null, "Provide XML name:");
    }


    protected void browseExportFolder() throws TransformerException, ParserConfigurationException {
        String XMLFileName = enterXMLFileName();
        if (!XMLFileName.isEmpty())
        {
            int value = xmlFolderChooser.showSaveDialog(null);
            if(value == JFileChooser.APPROVE_OPTION)
            {
                xmlExportPath = xmlFolderChooser.getSelectedFile().getAbsolutePath();
                xmlExport(xmlExportPath + "\\" + XMLFileName + ".xml");
            }
        }
    }

    private void xmlExport(String xmlExportPath) throws ParserConfigurationException, TransformerException
    {

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

        File myFile = new File(xmlExportPath);

        // write to console and file
        StreamResult console = new StreamResult(System.out);
        StreamResult file = new StreamResult(myFile);

        transf.transform(source, console);
        transf.transform(source, file);

        String messageDialog = "Successful Export of Billboard. Saved in " + xmlExportPath;
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

    protected void addBBXML(File fileToDisplay)
    {
        HashMap<String, String> billboardData = extractDataFromXML(fileToDisplay);

        String backgroundColour = billboardData.get("Background Colour");
        String message = billboardData.get("Message");
        String messageColour = billboardData.get("Message Colour");
        String picture = billboardData.get("Picture");
        String pictureType = billboardData.get("Picture Type");
        String information = billboardData.get("Information");
        String informationColour = billboardData.get("Information Colour");

        // Read in the picture
        BufferedImage pictureImage = readPictureFromFile(picture, pictureType);
        setPhoto(new ImageIcon(pictureImage));

        setBBTitle(message);
        setBBText(information);

        // Check if there's a specific background colour, else set it to be white
        if (backgroundColour != null) {
            setBBTitleColour(backgroundColour);
        }
        else {
            setBBTitleColour("#fff");
        }

        // Check if there's a specific message text colour, else set it to be black
        if (message != null) {
            if (messageColour != null) {
                setBBTextColour(messageColour);
            }
            else {
                setBBTextColour("#fff");
            }
        }

        // Check if there's a specific information text colour, else set it to be black
        if (information != null) {
            if (informationColour != null) {
                setBBTextColour(informationColour);
            }
            else {
                setBBTextColour("#fff");
            }
        }
    }


    protected void addBBBackgroundColourListener(MouseListener listener){backgroundColourButton.addMouseListener(listener);}

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

    protected void addBBNameListener(MouseListener listener) {billboardNameButton.addMouseListener(listener);}

}
