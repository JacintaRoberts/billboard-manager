package controlPanel;

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
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;

import controlPanel.Main.VIEW_TYPE;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import static javax.swing.JOptionPane.*;

/**
 * BB Create View designed to contain software elements allowing users to create a BB.
 */
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
    private JButton importXMLButton;
    private JButton backgroundColourButton;
    private JButton titleButton;
    private JButton textButton;
    private JButton photoButton;
    private JButton billboardNameButton;
    private JButton BBMenuButton;
    // --- Labels ---
    private JLabel photoLabel;
    private JLabel BBNameLabel;
    private JLabel disclaimerText;
    // --- ENUM ---
    private VIEW_TYPE view_type;
    private Object photoPath;
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

    /**
     * Constructor to create the BB create view
     */
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
        addBBMenuButton();
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

        infoColour = toHexString(Color.BLACK);
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

        createButton = new JButton("Submit");
        disclaimerText = new JLabel("Display is not to scale. Select 'Preview Billboard' to view.");
        disclaimerText.setForeground(Color.WHITE);
        JPanel navPanel = getNavPanel();
        GridBagConstraints gbc_nav = getNavGBCPanel();
        navPanel.add(createButton, setGBC(gbc_nav,3,1,1,1));
        navPanel.add(disclaimerText, setGBC(gbc_nav,4,1,1,1));

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

    private void addBBMenuButton()
    {
        BBMenuButton = new JButton("Billboard Menu");
        JPanel navPanel = getNavPanel();
        GridBagConstraints gbc = getNavGBCPanel();
        navPanel.add(BBMenuButton, setGBC(gbc, 2,1,1,1));
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
    protected void setBBTextColour(String colour)
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
    protected void setBackgroundColour(String colour)
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
    }

    /**
     * Set Photo in the Drawing Panel
     * @param icon BB image in icon format
     */
    protected void setPhoto(ImageIcon icon, PhotoType imgType, Object imgPath)
    {
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

    /**
     * get selected BB name
     * @return bb name
     */
    protected String getSelectedBBName() {return BBNameLabel.getText();}

    /**
     * check if BB is valid
     * @return valid BB
     */
    protected boolean checkBBValid()
    {
        boolean infoNull = (BBTextField.getText().equals(""));
        boolean titleNull = (titleLabel.getText().equals(""));
        boolean pictureNull = (photoPath == null);

        if (infoNull && titleNull && pictureNull)
        {
            return false;
        }
        return true;
    }

    /**
     * Get BB XML document
     * @param separatePictureData
     * @return array list containing xml and picture data
     * @throws ParserConfigurationException
     */
    protected ArrayList<Object> getBBXMLDocument(boolean separatePictureData) throws ParserConfigurationException {

        byte[] photoData = new byte[0];

        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();

        boolean infoExists = !BBTextField.getText().equals("");
        boolean titleExists = !titleLabel.getText().equals("");
        boolean pictureExists = photoPath != null;

        // ------- ROOT -------
        // root element - billboard
        Element root = document.createElement("billboard");
        document.appendChild(root);

        // set a colour attribute to root element
        Attr attr_background = document.createAttribute("background");
        attr_background.setValue(backgroundColour);
        root.setAttributeNode(attr_background);

        if (titleExists)
        {
            // ------- MESSAGE / TITLE -------
            Element message = document.createElement("message");
            message.setTextContent(titleLabel.getText());
            root.appendChild(message);

            // set a colour attribute to message element
            Attr attr_message = document.createAttribute("colour");
            attr_message.setValue(titleColour);
            message.setAttributeNode(attr_message);
        }

        // ------- INFORMATION -------
        if (infoExists)
        {
            Element information = document.createElement("information");
            information.setTextContent(BBTextField.getText());
            root.appendChild(information);

            // set a colour attribute to information element
            Attr attr_information = document.createAttribute("colour");
            attr_information.setValue(infoColour);
            information.setAttributeNode(attr_information);
        }

        // ------- PICTURE -------
        if (pictureExists)
        {
            // set a colour attribute to information element
            if (photoType == PhotoType.URL) {
                Element picture = document.createElement("picture");
                root.appendChild(picture);
                Attr attr_picture;
                attr_picture = document.createAttribute("url");
                attr_picture.setValue((String)photoPath);
                picture.setAttributeNode(attr_picture);
            }
            // update byte array to contain image data
            else if (photoType == PhotoType.DATA && separatePictureData)
            {
                byte[] imageByteArray = Base64.getDecoder().decode((String) photoPath);
                photoData = imageByteArray;
            }
            // set a picture attribute with data
            else if (photoType == PhotoType.DATA && !separatePictureData)
            {
                Element picture = document.createElement("picture");
                root.appendChild(picture);
                Attr attr_picture;
                attr_picture = document.createAttribute("data");
                attr_picture.setValue((String) photoPath);
                picture.setAttributeNode(attr_picture);
            }
        }

        // return array list of xml document and photo data
        ArrayList<Object> xmlData = new ArrayList<>();
        xmlData.add(document);
        xmlData.add(photoData);

        return xmlData;
    }

    /**
     * Get BB xml doc as string
     * @return xml string
     */
    protected String getBBXMLString(Document xmlDocument) throws TransformerException
    {
        Transformer t;
        TransformerFactory tf = TransformerFactory.newInstance();
        t = tf.newTransformer();
        StringWriter sw = new StringWriter();
        t.transform(new DOMSource(xmlDocument), new StreamResult(sw));
        String xmlString = sw.toString();
        return xmlString;
    }

    // ###################### BROWSE FOR BB SETTINGS ######################

    /**
     * Show Dialog to allow user to select Title Colour
     * @return colour selected by user (hexadecimal format)
     */
    protected String browseTitleColour()
    {
        Color colorSelect = JColorChooser.showDialog(null, "Choose a Colour for Title", drawingPadPanel.getForeground());
        if (colorSelect !=null)
        {
            return toHexString(colorSelect);
        }
        return null;
    }

    /**
     * Show Dialog to allow user to set BB information text
     * @return text provided by user (String format)
     */
    protected String browseTextColour()
    {
        Color colorSelect = JColorChooser.showDialog(null, "Choose a Colour for Text", drawingPadPanel.getForeground());
        if (colorSelect !=null)
        {
            return toHexString(colorSelect);
        }
        return null;
    }

    /**
     * Show Dialog to show invalid title colour selected
     */
    private void showInvalidTitleColourMessage()
    {
        JOptionPane.showMessageDialog(null, "Invalid Title Colour Provided in XML. Title has been set to default colour of black.");
    }

    /**
     * Show Dialog to show invalid info colour selected
     */
    private void showInvalidInformationColourMessage()
    {
        JOptionPane.showMessageDialog(null, "Invalid Information Text Colour Provided in XML. Information has been set to default colour of black.");
    }

    /**
     * Show Dialog to show invalid background colour selected
     */
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
        JOptionPane.showMessageDialog(null, "Invalid Billboard XML. Exception Occurred.");
    }

    /**
     * Show Dialog to allow user to set Schedule
     * @return
     */
    protected int showSchedulingOption()
    {
        String message = "Would you like to update Schedule for the Billboard now?";
        String[] options = {"Schedule Now", "Schedule Later"};
        return JOptionPane.showOptionDialog(null, message, "Schedule", DEFAULT_OPTION, INFORMATION_MESSAGE, null, options, null);
    }

    /**
     * Ask User for confirmation of BB creation
     * @return
     */
    protected int showConfirmationCreateBB()
    {
        String message = "Are you sure you want to update Billboard "+ BBNameLabel.getText() + "?";
        return JOptionPane.showConfirmDialog(null, message);
    }

    /**
     * Show Successful BB Created Message to user
     * @return
     */
    protected void showBBCreatedSuccessMessage()
    {
        String message = "You have successfully published Billboard " + BBNameLabel.getText() + ". You are able to update schedule at a later time.";
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
    protected void showBBInvalidErrorMessage()
    {
        String message = "Invalid Billboard. Please ensure to select a Billboard Name and at least a title, text or picture.";
        JOptionPane.showMessageDialog(null, message);
    }

    /**
     * Show Error Message as no BB name was provided and it has been deleted indicated by server
     */
    protected void showBBInvalidErrorMessageNonExistBillboard()
    {
        String message = "Invalid Billboard. Billboard does not exist. Please Refresh to check if it has been deleted.";
        JOptionPane.showMessageDialog(null, message);
    }

    /**
     * Show Error Message due to sessionToken
     */
    protected void showBBInvalidErrorMessageTokenError()
    {
        String message = "Cannot Retreive Billboard. SessionToken Expired. Please Re-login";
        JOptionPane.showMessageDialog(null, message);
    }


    /**
     * Browse Photos to add to BB
     * @return BB Image (icon format)
     */
    protected ArrayList<Object> browsePhotos() throws Exception {
        int value = photoChooser.showSaveDialog(null);
        if(value == JFileChooser.APPROVE_OPTION)
        {
            ArrayList<Object> imageDetails = new ArrayList<>();
            String photoPath = photoChooser.getSelectedFile().getAbsolutePath();
            Image img = ImageIO.read(new File(photoPath)).getScaledInstance(380,380,Image.SCALE_DEFAULT);
            imageDetails.add(new ImageIcon(img));
            byte[] fileContent = Files.readAllBytes(new File(photoPath).toPath());
            String encodedString = Base64.getEncoder().encodeToString((fileContent));
            imageDetails.add(encodedString);
            return imageDetails;
        }
        else
        {
            throw new Exception("Nothing selected.");
        }

    }

    /**
     * Allow user to enter URL for image
     * @return array list containing image and url provided
     */
    protected String showURLInputMessage() {
        return JOptionPane.showInputDialog(null, "Provide URL to Image:");
    }

    protected ArrayList<Object> getImageData(String urlPath) throws IOException {
        ArrayList<Object> imageDetails = new ArrayList<>();
        URL url = new URL(urlPath);
        Image image = ImageIO.read(url).getScaledInstance(380,380,Image.SCALE_DEFAULT);
        ImageIcon icon = new ImageIcon(image);
        imageDetails.add(icon);
        imageDetails.add(urlPath);
        return imageDetails;
    }

    /**
     * Show message to user for successful export
     */
    protected void showSuccessfulExport()
    {
        String messageDialog = "Successful Export of Billboard. Saved in " + xmlExportPath;
        JOptionPane.showMessageDialog(null, messageDialog);
    }

    /**
     * Show message to user for invalid url
     */
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
    protected String browseXMLImport() throws Exception {
        int value = xmlChooser.showSaveDialog(null);
        if (value == JFileChooser.APPROVE_OPTION)
        {
            return xmlImportPath = xmlChooser.getSelectedFile().getAbsolutePath();
        }
        else
        {
            throw new Exception("No XML Selected.");
        }
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
    protected String browseExportFolder()
    {
        xmlExportPath = xmlFolderChooser.getSelectedFile().getAbsolutePath();
        return xmlExportPath;
    }

    /**
     * Show folder chooser
     * @return value selected
     */
    protected int showFolderChooserSelector()
    {
        return xmlFolderChooser.showSaveDialog(null);
    }

    /**
     * Show photo type selection
     * @return selection
     */
    protected int photoTypeSelection()
    {
        String message = "Please select the Type of Image to add to the Billboard.";
        String[] options = {"URL", "Browse Personal Photos", "Clear", "Cancel"};
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
     * Set XML bb based on input doc and byte[]
     * @param doc xml document
     * @param pictureData  byte [] of picture
     */
    protected void setXMLBB(Document doc, byte[] pictureData) throws Exception
    {
        // clean current panel
        setBackgroundColour(toHexString(Color.WHITE));
        setBBTitle("");
        setBBText("");
        setPhoto(null, null, null);

        // normalise the XML structure
        doc.getDocumentElement().normalize();

        // get root element (billboard)
        Element root = doc.getDocumentElement();

        // ensure tag element is billboard
        if (root.getTagName().equals("billboard"))
        {
            // get message, info and picture from document
            NodeList titleList = doc.getElementsByTagName("message");
            NodeList infoList = doc.getElementsByTagName("information");
            NodeList photoList = doc.getElementsByTagName("picture");

            // if nothing provided in xml - throw exception
            if (titleList.getLength() == 0 && infoList.getLength() == 0 && photoList.getLength() == 0 && pictureData.length == 0)
            {
                throw new Exception("Invalid XML provided - provide at least a Message, Info or Picture");
            }

            // set Picture if provided
            if (photoList.getLength() != 0) {
                setXMLPicture(photoList);
            }
            // set Picture Data if provided
            else if (pictureData.length != 0)
            {
                manageDataPicture(pictureData);
            }

            // get background colour
            String backgroundColour = root.getAttribute("background");
            setBackgroundColour(backgroundColour);

            // set title and colour if provided
            if (titleList.getLength() != 0)
            {
                Element titleNode = (Element) titleList.item(0);
                String title = titleNode.getTextContent();
                setBBTitle(title);
                String titleColour = titleNode.getAttribute("colour");
                setBBTitleColour(titleColour);
            }

            // set info and colour if provided
            if (infoList.getLength() != 0) {
                Element infoNode = (Element) infoList.item(0);
                String information = infoNode.getTextContent();
                String informationColour = infoNode.getAttribute("colour");
                setBBText(information);
                setBBTextColour(informationColour);
            }
        }
        // invalid tag name for root
        else
        {
            throw new Exception("Invalid Root Name");
        }
    }

    /**
     * Import BB XML file from personal files and set title, text and photo on Drawing Panel
     * @param xmlImportPath String path to xml file
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    protected Document createXMLDoc(String xmlImportPath) throws ParserConfigurationException, IOException, SAXException {
        //creating a constructor of file class and parsing an XML file
        File file = new File(xmlImportPath);
        //an instance of factory that gives a document builder
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        //an instance of builder to parse the specified xml file
        Document doc;
        DocumentBuilder db;
        db = dbf.newDocumentBuilder();
        doc = db.parse(file);
        return doc;
    }

    /**
     * Manage data picture to set picture data to BB
     * @param pictureData pic data in byte []
     * @return true if set correctly, otherwise false
     */
    private void manageDataPicture(byte[] pictureData) throws IOException
    {
        Image image = ImageIO.read(new ByteArrayInputStream(pictureData)).getScaledInstance(380,380,Image.SCALE_DEFAULT);
        String encodedString = Base64.getEncoder().encodeToString(pictureData);
        setPhoto(new ImageIcon(image), PhotoType.DATA, encodedString);
    }

    /**
     * Manage xml picture to set picture url to BB
     * @param photoList pic data in node list
     * @return true if set correctly, otherwise false
     */
    private void setXMLPicture(NodeList photoList) throws Exception {
        Element photoNode = (Element) photoList.item(0);

        String photoPathURL;
        PhotoType photoPathType;
        Image photoImage;

        if (photoNode.hasAttribute("url"))
        {
            photoPathType = PhotoType.URL;
            photoPathURL = photoNode.getAttribute("url");

            // check that url is not null and contains https
            if (photoPathURL != null && photoPathURL.contains("https"))
            {
                URL url = new URL(photoPathURL);
                photoImage = ImageIO.read(url).getScaledInstance(380,380,Image.SCALE_DEFAULT);
                setPhoto(new ImageIcon(photoImage),photoPathType, photoPathURL);
            }
        }
        else if(photoNode.hasAttribute("data"))
        {
            photoPathURL = photoNode.getAttribute("data");
            photoPathType = PhotoType.DATA;

            if (photoPathURL != null)
            {
                byte[] imgBytes = Base64.getDecoder().decode(photoPathURL);
                photoImage =ImageIO.read(new ByteArrayInputStream(imgBytes)).getScaledInstance(380,380,Image.SCALE_DEFAULT);
                setPhoto(new ImageIcon(photoImage),photoPathType, Base64.getEncoder().encodeToString(imgBytes));
            }
        }
        else
        {
            throw new Exception("Invalid Picture Provided - cannot set photo");
        }
    }

    /**
     * Create XML and export to user's personal file explorer based on specified file path
     * @param path
     * @param xmlDocument
     * @throws ParserConfigurationException
     * @throws TransformerException
     */
    protected void xmlExport(String path, Document xmlDocument) throws TransformerException {

        // create transformer which is designed to transform document to XML
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transf = null;
        transf = transformerFactory.newTransformer();

        // set encoding type, indentation,
        transf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transf.setOutputProperty(OutputKeys.INDENT, "yes");

        // source contains the dom tree
        DOMSource source = new DOMSource(xmlDocument);

        File myFile = new File(path);

        // write to console and file
        StreamResult console = new StreamResult(System.out);
        StreamResult file = new StreamResult(myFile);

        transf.transform(source, console);
        transf.transform(source, file);
    }

    /**
     * Get XML Document from String
     * @param xmlString xml document as string
     */
    protected Document getXMLDocument(String xmlString) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document document;
        builder = factory.newDocumentBuilder();
        document = builder.parse( new InputSource( new StringReader( xmlString)));

        return document;
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

    /**
     * Add listener to Preview BB Button
     * @param listener mouse listener
     */
    protected void addBBPreviewListener(MouseListener listener)
    {
        previewButton.addMouseListener(listener);
    }

    /**
     * Add listener to navigate to BB Menu View
     * @param listener mouse listener
     */
    protected void addBBMenuListener(MouseListener listener)
    {
        BBMenuButton.addMouseListener(listener);
    }

    /**
     * Type of photo provided by user either url or data
     */
    protected enum PhotoType
    {
        URL,
        DATA
    }
}
