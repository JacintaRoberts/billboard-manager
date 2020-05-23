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
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

import controlPanel.Main.VIEW_TYPE;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import static javax.swing.JOptionPane.*;
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
    private String backgroundColour;
    private String titleColour;
    private String infoColour;

    private PhotoType photoType;

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
        createDrawingPanel();
        createDrawingToolbar();
        createBBOptionsMenu();
    }

    /**
     * Create Drawing Panel which is where the BB will be displayed for the user to see.
     * The panel will display the title, text information and the photo uploaded by the user. The colours selected are
     * also shown.
     */
    private void createDrawingPanel()
    {
        // -------- DRAWING PANEL -----------
        drawingPadPanel = new JPanel();
        titleLabel = new JTextArea();
        titleLabel.setPreferredSize(new Dimension(1000,100));
        titleLabel.setLineWrap(true);
        titleLabel.setWrapStyleWord(true);
        titleLabel.setFont(titleLabel.getFont().deriveFont(40f));
        titleLabel.setForeground(Color.white);
        titleLabel.setOpaque(false);
        BBTextField = new JTextArea();
        BBTextField.setPreferredSize(new Dimension(1000,180));
        BBTextField.setLineWrap(true);
        BBTextField.setWrapStyleWord(true);
        BBTextField.setFont(BBTextField.getFont().deriveFont(40f));
        BBTextField.setForeground(Color.white);
        BBTextField.setOpaque(false);
        photoLabel = new JLabel();
        photoLabel.setPreferredSize(new Dimension(380,380));

        drawingPadPanel.add(titleLabel);
        drawingPadPanel.add(BBTextField);
        drawingPadPanel.add(photoLabel);
        getContentPane().add(drawingPadPanel, BorderLayout.CENTER);
    }

    /**
     * Create the Drawing Toolbar which contains the BB Name, background colour, title, text and photo selection.
     */
    private void createDrawingToolbar()
    {
        // -------- DRAWING TOOLBAR PANEL -----------
        drawingToolsPanel = new JPanel();
        drawingToolsPanel.setLayout(new GridLayout(6,1));
        billboardNameButton = new JButton("Billboard Name");
        BBNameLabel = new JLabel("");
        backgroundColourButton = new JButton("Background Colour");

        // set default background colour
        backgroundColour = toHexString(Color.WHITE);
        drawingPadPanel.setBackground(Color.decode(backgroundColour));

        titleColour = toHexString(Color.BLACK);
        titleLabel.setForeground(Color.decode(titleColour));

        infoColour = toHexString(Color.WHITE);
        BBTextField.setForeground(Color.decode(infoColour));

        titleButton = new JButton("Title");
        textButton = new JButton("Text");
        photoButton = new JButton("Photo");

        photoPath = null;
        photoType = null;

        drawingToolsPanel.add(BBNameLabel);
        drawingToolsPanel.add(billboardNameButton);
        drawingToolsPanel.add(backgroundColourButton);
        drawingToolsPanel.add(titleButton);
        drawingToolsPanel.add(textButton);
        drawingToolsPanel.add(photoButton);
        getContentPane().add(drawingToolsPanel, BorderLayout.WEST);
    }

    /**
     * Create the BB Options Menu which contains the Import XML, Export XML and preview button.
     */
    private void createBBOptionsMenu()
    {
        // -------- BB OPTIONS MENU -----------
        billboardMenuPanel = new JPanel();
        billboardMenuPanel.setLayout(new GridLayout(3,1));
        importXMLButton = new JButton("Import XML");
        exportButton = new JButton("Export XML");
        previewButton = new JButton("Preview Billboard");
        billboardMenuPanel.add(importXMLButton);
        billboardMenuPanel.add(exportButton);
        billboardMenuPanel.add(previewButton);
        getContentPane().add(billboardMenuPanel, BorderLayout.EAST);

        cancelButton = new JButton("Cancel");
        createButton = new JButton("Create");
        JPanel navPanel = getNavPanel();
        GridBagConstraints gbc_nav = getNavGBCPanel();
        navPanel.add(cancelButton, setGBC(gbc_nav,4,1,1,1));
        navPanel.add(createButton, setGBC(gbc_nav,3,1,1,1));

        photoChooser = new JFileChooser();
        photoChooser.setCurrentDirectory(new java.io.File("."));
        photoChooser.setDialogTitle("Select Photo to upload");
        FileNameExtensionFilter photoFilter = new FileNameExtensionFilter("image files (*.bmp, *jpg, *png)", "jpg","png", "bmp");
        photoChooser.setFileFilter(photoFilter);

        xmlChooser = new JFileChooser();
        xmlChooser.setCurrentDirectory(new java.io.File("."));
        xmlChooser.setDialogTitle("Select XML File to upload");
        FileNameExtensionFilter xmlFilter = new FileNameExtensionFilter("xml files (*.xml)", "xml");
        xmlChooser.setFileFilter(xmlFilter);

        xmlFolderChooser = new JFileChooser();
        xmlFolderChooser.setCurrentDirectory(new java.io.File("."));
        xmlFolderChooser.setDialogTitle("Select Folder to Save XML");
        xmlFolderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    }

    // ###################### CLEAN UP, ENUM & UPDATE ######################

    @Override
    void cleanUp()
    {
        setBBName("");
        setBBNameEnabled(true);
        setBackgroundColour(toHexString(Color.WHITE));
        setBBTitle("");
        setBBText("");
        setPhoto(null, null, null);
    }

    @Override
    public void update(Subject s)
    {

    }

    @Override
    VIEW_TYPE getEnum()
    {
        return view_type;
    }

    // ###################### SET BB VALUES ######################

    /**
     * Set text in BB Text field in Drawing Panel
     * @param text string to set in text field
     */
    protected void setBBText(String text)
    {
        BBTextField.setText(text);
    }

    /**
     * Set colour of text in BB Text field in Drawing Panel
     * @param colour colour to set in text field (hexadecimal)
     */
    protected void setBBTextColour(String colour) throws NumberFormatException
    {
        Color newColour = Color.BLACK;
        // set colour if provided, if not set to BLACK
        if (!colour.equals(""))
        {
            try
            {
                newColour = Color.decode(colour);
            }
            catch (NumberFormatException e)
            {
                showInvalidInformationColourMessage();
            }
        }
        BBTextField.setForeground(newColour);
        infoColour = toHexString(newColour);
    }

    /**
     * Set text colour in BB Title Field in Drawing Panel
     * @param colour colour to set in text field (hexadecimal)
     */
    protected void setBBTitleColour(String colour)
    {
        Color newColour = Color.BLACK;
        // set colour if provided, if not set to BLACK
        if (!colour.equals(""))
        {
            try
            {
                newColour = Color.decode(colour);
            }
            catch (NumberFormatException e)
            {
                showInvalidTitleColourMessage();
            }
        }
        titleLabel.setForeground(newColour);
        titleColour = toHexString(newColour);
    }

    /**
     * Set text in BB Title Field in Drawing Panel
     * @param titleString text of BB Title
     */
    protected void setBBTitle(String titleString)
    {
        titleLabel.setText(titleString);
    }

    /**
     * Set BB name
     * @param name name of BB
     */
    protected void setBBName(String name)
    {
        BBNameLabel.setText(name);
    }

    /**
     * Set background colour of Drawing Panel to reflect BB colour set by user
     * @param colour colour to set drawing panel in hexadecimal
     */
    protected void setBackgroundColour(String colour) throws NumberFormatException
    {
        Color newColour = Color.WHITE;
        // set colour if provided, if not set to BLACK
        if (!colour.equals(""))
        {
            try
            {
                newColour = Color.decode(colour);
            }
            catch(NumberFormatException e)
            {
                showInvalidBackgroundColourMessage();
            }
        }
        drawingPadPanel.setBackground(newColour);
        backgroundColour = toHexString(newColour);
        System.out.println("set background to white");
    }

    /**
     * Set Photo in the Drawing Panel
     * @param icon BB image in icon format
     */
    protected void setPhoto(ImageIcon icon, PhotoType imgType, String imgPath)
    {
        // FIXME: handle when these are NULL
        photoLabel.setIcon(icon);
        photoType = imgType;
        photoPath = imgPath;
    }

    /**
     * Set BB Name button enabled so user can set a BB Name when creating new BB.
     * Disable BB Name button when updating existing BB.
     */
    protected void setBBNameEnabled(boolean enabled)
    {
        billboardNameButton.setEnabled(enabled);
    }

    // ###################### GET BB VALUES ######################

    protected String getSelectedBBName() {return BBNameLabel.getText();}

    protected ArrayList<Object> getBBXML()
    {
        ArrayList<Object> BBInfo = new ArrayList<>();
        // add BB name
        BBInfo.add(getSelectedBBName());
        // add background colour
        BBInfo.add(backgroundColour);
        // add title
        BBInfo.add(titleLabel.getText());
        // add title colour
        BBInfo.add(titleColour);
        // add text
        BBInfo.add(BBTextField.getText());
        // add text colour
        BBInfo.add(infoColour);
        // photo
        BBInfo.add(photoLabel.getIcon());

        return BBInfo;
    }

    // ###################### BROWSE FOR BB SETTINGS ######################

    /**
     * Show Dialog to allow user to select Title Colour
     * @return colour selected by user (hexadecimal format)
     */
    protected String browseTitleColour()
    {
        Color colorSelect = JColorChooser.showDialog(null, "Choose a Colour for Title", drawingPadPanel.getForeground());
        return toHexString(colorSelect);
    }

    /**
     * Show Dialog to allow user to set BB information text
     * @return text provided by user (String format)
     */
    protected String browseTextColour()
    {
        Color colorSelect = JColorChooser.showDialog(null, "Choose a Colour for Text", drawingPadPanel.getForeground());
        return toHexString(colorSelect);
    }

    private void showInvalidTitleColourMessage()
    {
        JOptionPane.showMessageDialog(null, "Invalid Title Colour Provided in XML. Title has been set to default colour of black.");
    }

    private void showInvalidInformationColourMessage()
    {
        JOptionPane.showMessageDialog(null, "Invalid Information Text Colour Provided in XML. Information has been set to default colour of black.");
    }

    private void showInvalidBackgroundColourMessage()
    {
        JOptionPane.showMessageDialog(null, "Invalid Background Colour Provided in XML. Background has been set to default colour of white.");
    }

    /**
     * Show Dialog to allow user to set background colour of BB
     * @return background colour provided by user (hexadecimal format)
     */
    protected String showColorChooser()
    {
        Color colorSelect = JColorChooser.showDialog(null, "Choose a Color", drawingPadPanel.getForeground());
        if (colorSelect != null)
        {
            return toHexString(colorSelect);
        }
        return null;
    }

    /**
     * Show Dialog to allow user to set bb name
     * @return bb name (String)
     */
    protected String showBBNameChooser()
    {
        return JOptionPane.showInputDialog(null, "Provide Billboard Name");
    }

    /**
     * Show Dialog to allow user to set bb title
     * @return bb name (String)
     */
    protected String showBBTitleChooser()
    {
        return JOptionPane.showInputDialog(null, "Provide Billboard Title");
    }

    /**
     * Show Dialog to allow user to set bb text
     * @return bb text (String)
     */
    protected String showBBTextChooser()
    {
        return JOptionPane.showInputDialog(null, "Set Billboard Text");
    }

    /**
     * Show Message to user to inform that XML Import was invalid
     */
    protected void showInvalidXMLMessage()
    {
        JOptionPane.showMessageDialog(null, "Invalid XML Uploaded");
    }

    /**
     * Show Dialog to allow user to set Schedule
     * @return
     */
    protected int showSchedulingOption()
    {
        return JOptionPane.showConfirmDialog(null, "Would you like to create a Schedule for the Billboard now?");
    }

    /**
     * Ask User for confirmation of BB creation
     * @return
     */
    protected int showConfirmationCreateBB()
    {
        String message = "Are you sure you want to create Billboard "+ BBNameLabel.getText() + "?";
        return JOptionPane.showConfirmDialog(null, message);
    }

    /**
     * Show Successful BB Created Message to user
     * @return
     */
    protected void showBBCreatedSuccessMessage()
    {
        String message = "You have successfully created the Billboard " + BBNameLabel.getText() + ". You are able to schedule the Billboard at a later time.";
        JOptionPane.showMessageDialog(null, message);
    }

    /**
     * Show Editing BB message
     */
    protected void showBBEditingMessage(String BBName)
    {
        String message = "You are editing Billboard: " + BBName + ". Please wait patiently for the billboard to load.";
        JOptionPane.showMessageDialog(null, message);
    }

    /**
     * Show Error Message as no BB name was provided
     */
    protected void showBBNameErrorMessage()
    {
        String message = "Please select a Billboard Name before creating the Billboard.";
        JOptionPane.showMessageDialog(null, message);
    }

    /**
     * Browse Photos to add to BB
     * @return BB Image (icon format)
     */
    protected ArrayList<Object> browsePhotos()
    {
        int value = photoChooser.showSaveDialog(null);
        if(value == JFileChooser.APPROVE_OPTION)
        {
            try
            {
                ArrayList<Object> imageDetails = new ArrayList<>();
                String photoPath = photoChooser.getSelectedFile().getAbsolutePath();
                Image img = ImageIO.read(new File(photoPath)).getScaledInstance(380,380,Image.SCALE_DEFAULT);
                imageDetails.add(new ImageIcon(img));

                byte[] fileContent = Files.readAllBytes(new File(photoPath).toPath());
                String encodedString = Base64.getEncoder().encodeToString(fileContent);

                imageDetails.add(encodedString);
                return imageDetails;
            }
            catch (Exception e)
            {
                return null;
            }
        }
        return null;
    }

    protected ArrayList<Object> showURLInputMessage() {
        String pathProvided = JOptionPane.showInputDialog(null, "Provide URL to Image:");
        try
        {
            ArrayList<Object> imageDetails = new ArrayList<>();
            URL url = new URL(pathProvided);
            Image image = ImageIO.read(url).getScaledInstance(380,380,Image.SCALE_DEFAULT);
            ImageIcon icon = new ImageIcon(image);
            imageDetails.add(icon);
            imageDetails.add(pathProvided);
            return imageDetails;
        }
        catch(Exception e)
        {
            return null;
        }
    }

    protected void showURLErrorMessage()
    {
        String message = "An invalid URL was provided. Please try again with a different URL.";
        JOptionPane.showMessageDialog(null, message);
    }

    /**
     * Browse File Chooser to select BB XML file
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    protected boolean browseXMLImport() {
        int value = xmlChooser.showSaveDialog(null);
        if (value == JFileChooser.APPROVE_OPTION)
        {
            xmlImportPath = xmlChooser.getSelectedFile().getAbsolutePath();
            return xmlImport(xmlImportPath);
        }
        return false;
    }

    /**
     * Show Input Dialog to set FileName text
     * @return filename (string)
     */
    protected String enterXMLFileName()
    {
        return JOptionPane.showInputDialog(null, "Provide XML name:");
    }

    /**
     * Allow users to browse folders in order to select one to save the XML BB file
     * @throws TransformerException
     * @throws ParserConfigurationException
     */
    protected void browseExportFolder() throws TransformerException, ParserConfigurationException {
        String XMLFileName = enterXMLFileName();
        if (XMLFileName != null)
        {
            int value = xmlFolderChooser.showSaveDialog(null);
            if(value == JFileChooser.APPROVE_OPTION)
            {
                xmlExportPath = xmlFolderChooser.getSelectedFile().getAbsolutePath();
                xmlExport(xmlExportPath + "\\" + XMLFileName + ".xml");
            }
        }
    }

    protected int photoTypeSelection()
    {
        String message = "Please select the Type of Image to add to the Billboard.";
        String[] options = {"URL", "Browse Personal Photos", "Cancel"};
        return JOptionPane.showOptionDialog(null, message, "Photo Type", DEFAULT_OPTION, INFORMATION_MESSAGE, null, options, null);
    }

    /**
     * Convert Java Colour object to a Hexidecimal String
     * @param colour Colour Object
     * @return colour in hexidecimal string
     */
    private static String toHexString(Color colour) {
        StringBuilder stringBuilder = new StringBuilder("#");

        // append red hexidecimal value to string builder
        if (colour.getRed() < 16) stringBuilder.append('0');
        stringBuilder.append(Integer.toHexString(colour.getRed()));

        // append green hexidecimal value to string builder
        if (colour.getGreen() < 16) stringBuilder.append('0');
        stringBuilder.append(Integer.toHexString(colour.getGreen()));

        // append blue hexidecimal value to string builder
        if (colour.getBlue() < 16) stringBuilder.append('0');
        stringBuilder.append(Integer.toHexString(colour.getBlue()));

        return stringBuilder.toString();
    }

    /**
     * Import BB XML file from personal files and set title, text and photo on Drawing Panel
     * @param xmlImportPath String path to xml file
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    private boolean xmlImport(String xmlImportPath)
    {
        //creating a constructor of file class and parsing an XML file
        File file = new File(xmlImportPath);

        //an instance of factory that gives a document builder
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        //an instance of builder to parse the specified xml file
        Document doc;
        DocumentBuilder db;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            return false;
        }

        try {
            doc = db.parse(file);
        } catch (SAXException | IOException e) {
            return false;
        }

        // normalise the XML structure
        doc.getDocumentElement().normalize();

        // get root element (billboard)
        Element root = doc.getDocumentElement();
        if (root.getTagName().equals("billboard"))
        {
            // get title and title colour
            NodeList titleList = doc.getElementsByTagName("message");
            NodeList infoList = doc.getElementsByTagName("information");
            NodeList photoList = doc.getElementsByTagName("picture");
            if (titleList.getLength() == 0 && infoList.getLength() == 0 && photoList.getLength() == 0)
            {
                return false;
            }

            if (photoList.getLength() != 0)
            {
                boolean setPicture = manageXMLPicture(photoList);
                if (!setPicture)
                {
                    return false;
                }
            }

            // get background colour
            String backgroundColour = root.getAttribute("background");
            setBackgroundColour(backgroundColour);

            if (titleList.getLength() != 0)
            {
                Element titleNode = (Element) titleList.item(0);
                String title = titleNode.getTextContent();
                setBBTitle(title);
                String titleColour = titleNode.getAttribute("colour");
                setBBTitleColour(titleColour);
            }

            if (infoList.getLength() != 0)
            {
                Element infoNode = (Element) infoList.item(0);
                String information = infoNode.getTextContent();
                String informationColour = infoNode.getAttribute("colour");
                setBBText(information);
                setBBTextColour(informationColour);
            }
            return true;
        }
        // invalid tag name for root
        else
        {
            return false;
        }
    }

    private boolean manageXMLPicture(NodeList photoList)
    {
        Element photoNode = (Element) photoList.item(0);

        String photoPathURL;
        PhotoType photoPathType;
        Image photoImage;

        if (photoNode.hasAttribute("url"))
        {
            photoPathType = PhotoType.URL;
            photoPathURL = photoNode.getAttribute("url");

            // check that
            if (photoPathURL != null && photoPathURL.contains("https"))
            {
                try
                {
                    URL url = new URL(photoPathURL);
                    photoImage = ImageIO.read(url).getScaledInstance(380,380,Image.SCALE_DEFAULT);
                    setPhoto(new ImageIcon(photoImage),photoPathType, photoPathURL);
                    return true;
                }
                catch (IOException e)
                {
                    return false;
                }
            }
            return false;
        }
        else if(photoNode.hasAttribute("data"))
        {
            photoPathURL = photoNode.getAttribute("data");
            photoPathType = PhotoType.DATA;

            if (photoPathURL != null)
            {
                byte[] imgBytes = Base64.getDecoder().decode(photoPathURL);
                try
                {
                    photoImage =ImageIO.read(new ByteArrayInputStream(imgBytes)).getScaledInstance(380,380,Image.SCALE_DEFAULT);
                    setPhoto(new ImageIcon(photoImage),photoPathType, photoPathURL);
                    return true;
                }
                catch (IOException e)
                {
                    return false;
                }
            }
            return false;
        }
        else
        {
            return false;
        }
    }

    /**
     * Create XML and export to user's personal file explorer based on specified file path
     * @param xmlExportPath
     * @throws ParserConfigurationException
     * @throws TransformerException
     */
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

    /**
     * In XML document, set Message text and colour
     * @param doc XML document representing BB
     * @param colour colour of message
     * @param message text of message
     * @return
     */
    private Node createMessage(Document doc, String colour, String message) {

        Element user = doc.createElement("message");
        user.setAttribute("colour", colour);
        user.appendChild(doc.createTextNode(message));
        return user;
    }

    /**
     * In XML document, set image URL
     * @param doc XML document representing BB
     * @param image image as url or data
     * @return
     */
    private Node createPicture(Document doc, String image) {

        Element user = doc.createElement("picture");
        if (photoType == PhotoType.URL)
        {
            user.setAttribute("url", image);
        }
        else if (photoType == PhotoType.DATA)
        {
            user.setAttribute("data", image);
        }
        // FIXME: ELSE CONDITION

        return user;
    }

    /**
     * In XML document, set information text and colour
     * @param doc XML document representing BB
     * @param colour colour of info text
     * @param info text of info
     * @return
     */
    private Node createInfo(Document doc, String colour, String info) {

        Element user = doc.createElement("information");
        user.setAttribute("colour", colour);
        user.appendChild(doc.createTextNode(info));
        return user;
    }

    /**
     * Display BB details, upon Editing the BB selected
     * @param fileToDisplay path to BB XML
     */
    protected void addBBXML(File fileToDisplay)
    {
        // extract xml data into hash map using viewer code
        HashMap<String, String> billboardData = extractDataFromXML(fileToDisplay);

        String backgroundColour = billboardData.get("Background Colour");
        System.out.println("background colour " + backgroundColour);
        String message = billboardData.get("Message");
        String messageColour = billboardData.get("Message Colour");
        String picture = billboardData.get("Picture");
        String pictureType = billboardData.get("Picture Type");
        String information = billboardData.get("Information");
        String informationColour = billboardData.get("Information Colour");

        // Read in the picture
        BufferedImage pictureImage = readPictureFromFile(picture, pictureType);
        PhotoType photoType = null;
        if (pictureType.equals("url"))
        {
            photoType = PhotoType.URL;
        }
        else if (pictureType.equals("data"))
        {
            photoType = PhotoType.DATA;
        }
        setPhoto(new ImageIcon(pictureImage),photoType, picture);

        setBBTitle(message);
        setBBText(information);

        // Check if there's a specific background colour & set, else set it to be white
        if (backgroundColour != null)
        {
            setBackgroundColour(backgroundColour);
        }
        else {
            setBackgroundColour(toHexString(Color.WHITE));
        }

        // Check if there's a specific message text colour, else set it to be black
        if (message != null)
        {
            if (messageColour != null) {
                setBBTitleColour(messageColour);
            }
            else {
                setBBTitleColour(toHexString(Color.BLACK));
            }
        }

        // Check if there's a specific information text colour, else set it to be black
        if (information != null)
        {
            if (informationColour != null)
            {
                setBBTextColour(informationColour);
            }
            else {
                setBBTextColour(toHexString(Color.BLACK));
            }
        }
        // disable billboard name button as user should not be able to change BB name
        setBBNameEnabled(false);
    }

    // ###################### LISTENERS ######################

    /**
     * Add listener to Background Colour button
     * @param listener mouse listener
     */
    protected void addBBBackgroundColourListener(MouseListener listener)
    {
        backgroundColourButton.addMouseListener(listener);
    }

    /**
     * Add listener to title button
     * @param listener mouse listener
     */
    protected void addBBTitleListener(MouseListener listener)
    {
        titleButton.addMouseListener(listener);
    }

    /**
     * Add listener to Text button
     * @param listener mouse listener
     */
    protected void addBBTextListener(MouseListener listener)
    {
        textButton.addMouseListener(listener);
    }

    /**
     * Add listener to Photo button
     * @param listener mouse listener
     */
    protected void addBBPhotoListener(MouseListener listener)
    {
        photoButton.addMouseListener(listener);
    }

    /**
     * Add listener to XML Import Button
     * @param listener mouse listener
     */
    protected void addBBXMLImportListener(MouseListener listener)
    {
        importXMLButton.addMouseListener(listener);
    }

    /**
     * Add listener to Export XML button
     * @param listener mouse listener
     */
    protected void addXMLExportListener(MouseListener listener)
    {
        exportButton.addMouseListener(listener);
    }

    /**
     * Add listener to BB Name button
     * @param listener mouse listener
     */
    protected void addBBNameListener(ActionListener listener)
    {
        billboardNameButton.addActionListener(listener);
    }

    /**
     * Add listener to Create Button
     * @param listener mouse listener
     */
    protected void addBBCreationListener(MouseListener listener)
    {
        createButton.addMouseListener(listener);
    }

    protected enum PhotoType
    {
        URL,
        DATA
    }
}
