package viewer;

import helpers.Helpers;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;


public class Viewer extends JFrame implements Runnable {

    // Set up the panels, labels, image icons etc. to display the different parts of the billboard
    JPanel mainPanel = new JPanel();
    JLabel messageLabel = new JLabel();
    ImageIcon pictureIcon = new ImageIcon();
    JLabel pictureLabel = new JLabel();
    JLabel informationLabel = new JLabel();

    // Dimensions of screen the viewer will display on
    private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static double screenHeight = screenSize.height;
    private static double screenWidth = screenSize.width;
    private static double screenWidthBorder = 75;
    private static double screenHeightBorder = 50;


    /**
     * Constructor method
     * @param title - the title of the viewer
     */
    public Viewer(String title) throws HeadlessException{
        super(title);
    }


    /**
     * Extracts the xml file that we want to display.
     * TODO: Remove or change this function to extract the XML file from the database
     */
    public File extractXMLFile(int fileNum) {
        ArrayList<File> xmlFiles = MockBillboardDatabase.setupDatabase();
        return xmlFiles.get(fileNum-1);
    }


    /**
     * Extracts data from an xml file for creating the billboard - i.e. it gets the content from the
     * message, picture and information tags as well as the custom colours
     * @param xmlFile - an xml file from which to extract information from
     * @return billboardTags - a HashMap which stores the background colour, message, message colour, picture,
     *      picture type (data or url), information, and information colour of the billboard, if there is no content
     *      for one or more of these tags, the string is null
     */
    public HashMap<String, String> extractDataFromXML(File xmlFile) {
        // Initiate an ArrayList to return
        HashMap<String, String> billboardData = new HashMap<>();

        // Try parsing the xml file - if it fails catch the exception
        try {
            // Use DocumentBuilderFactory to parse xml file
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document xmlDoc = dBuilder.parse(xmlFile);

            // Create an element which is the billboard tag
            NodeList billboardNodeList = xmlDoc.getElementsByTagName("billboard");
            Node billboardNode = billboardNodeList.item(0);
            Element billboardElement = (Element) billboardNode;

            // Add the background colour
            if (!billboardElement.getAttribute("background").isEmpty()) {
                String backgroundColour = billboardElement.getAttribute("background");
                billboardData.put("Background Colour", backgroundColour);
            } else {
                billboardData.put("Background Colour", null);
            }

            // If the message tag exists, add the message and colour, else add an empty string
            if (billboardElement.getElementsByTagName("message").getLength() == 1) {
                Node messageNode = billboardElement.getElementsByTagName("message").item(0);
                Element messageElement = (Element) messageNode;
                String message = messageElement.getTextContent();

                // Check if it has a message colour
                if (!messageElement.getAttribute("colour").isEmpty()) {
                    String messageColour = messageElement.getAttribute("colour");
                    billboardData.put("Message Colour", messageColour);
                } else {
                    billboardData.put("Message Colour", null);
                }

                billboardData.put("Message", message);
            } else {
                billboardData.put("Message", null);
                billboardData.put("Message Colour", null);
            }

            // If the picture tag exists, add the url or data attribute, else add an empty string
            if (billboardElement.getElementsByTagName("picture").getLength() == 1) {
                Node pictureNode = billboardElement.getElementsByTagName("picture").item(0);
                Element pictureElement = (Element) pictureNode;
                String picture = "";

                // Check if the picture attribute is a url or data
                if (pictureElement.getAttribute("url") != "") {
                    picture = pictureElement.getAttribute("url");
                    billboardData.put("Picture", picture);
                    billboardData.put("Picture Type", "url");
                } else {
                    picture = pictureElement.getAttribute("data");
                    billboardData.put("Picture", picture);
                    billboardData.put("Picture Type", "data");
                }

            } else {
                billboardData.put("Picture", null);
                billboardData.put("Picture Type", null);
            }

            // If the information tag exists, add the information and colour, else add an empty string
            if (billboardElement.getElementsByTagName("information").getLength() == 1) {
                Node informationNode = billboardElement.getElementsByTagName("information").item(0);
                Element informationElement = (Element) informationNode;
                String information = informationElement.getTextContent();

                // Check if it has an information colour
                if (!informationElement.getAttribute("colour").isEmpty()) {
                    String informationColour = informationElement.getAttribute("colour");
                    billboardData.put("Information Colour", informationColour);
                } else {
                    billboardData.put("Information Colour", null);
                }

                billboardData.put("Information", information);
            } else {
                billboardData.put("Information", null);
                billboardData.put("Information Colour", null);
            }

            // Print the tags to the console - for testing
//            System.out.println("Background Colour: " + billboardData.get("Background Colour"));
//            System.out.println("Message: " + billboardData.get("Message"));
//            System.out.println("Message Colour: " + billboardData.get("Message Colour"));
//            System.out.println("Picture: " + billboardData.get("Picture"));
//            System.out.println("Information: " + billboardData.get("Information"));
//            System.out.println("Information Colour: " + billboardData.get("Information Colour"));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return billboardData;
    }


    /**
     * Setups the basics of the billboard regardless of what is being displayed
     */
    public void setupBillboard() {
        // Create a border layout and aff the main panel to the billboard
        setLayout(new BorderLayout());

        // Add the central panel to the center of the billboard
        add(mainPanel, BorderLayout.CENTER);

        // Create a grid bag layout
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
    }


    /**
     * Calculates the largest font size for the message string, which must fit on one line on the screen.
     * @param message - the string to display on the screen
     * @return fontSize - the largest font size that the message can be displayed at
     */
    public int getMessageFontSize(String message) {
        // Get the current font size and initialise the variable to return
        int currentFontSize = messageLabel.getFont().getSize();
        int fontSize = currentFontSize;

        // Calculate what the width of the string would be based on the current font size
        FontMetrics fontMetrics = messageLabel.getFontMetrics(new Font(messageLabel.getFont().getName(), Font.BOLD,
                currentFontSize));
        double stringWidth = fontMetrics.stringWidth(message);

        // Testing
//        System.out.println("screenWidth = " + screenWidth);
//        System.out.println("stringWidth = " + stringWidth);
//        System.out.println("fontSize = " + fontSize);

        // While the current width of the string is less than the screen width minus some threshold for the border
        while (stringWidth < (screenWidth - screenWidthBorder*2)) {
            // Increase the font size
            fontSize = fontSize + 1;

            // Recalculate the string width
            fontMetrics = messageLabel.getFontMetrics(new Font(messageLabel.getFont().getName(), Font.BOLD, fontSize));
            stringWidth = fontMetrics.stringWidth(message);
        }

        // Testing
//        System.out.println("stringWidth = " + stringWidth);
//        System.out.println("fontSize = " + fontSize);

        return fontSize;
    }


    /**
     * Displays a billboard which has only a message
     * @param message - a string which stores the message to display
     */
    public void messageOnlyBillboard(String message) {
        // Set the text and font of the message label
        messageLabel.setText(message);

        // Choose the font size so the message fits in one line
        int fontSize = getMessageFontSize(message);
        messageLabel.setFont(new Font(messageLabel.getFont().getName(), Font.BOLD, fontSize));

        // Add the message label to the central panel in the JFrame
        mainPanel.add(messageLabel);
    }


    /**
     * Adds the picture to the central panel - for pictureOnlyBillboard at this stage
     * @param image - the image that needs to be added to the panel
     * TODO: Edit this method to account for other sizes of pictures also.
     */
    public void addPictureToPanel(BufferedImage image) {
        // Find the current dimensions of the picture
        double originalWidth = image.getWidth();
        double originalHeight = image.getHeight();

        // Define the maximum image dimensions based on the screen dimensions
        double maxImageWidth = screenWidth/2;
        double maxImageHeight = screenHeight/2;

        // Initialise variables to store the new dimensions - set the initial value as the current dimensions
        double newWidth = originalWidth;
        double newHeight = originalHeight;

        // How much the image width or height has changed - we will use this to alter the other dimension
        double ratio = 1;

        // Decide how to scale the image (or if we need to at all)
        // If the width is too big but the height is smaller
        if (originalWidth > maxImageWidth && originalHeight < maxImageHeight) {
            newWidth = maxImageWidth;
            ratio = newWidth/originalWidth;
            newHeight = ratio*originalHeight;
        }

        // If the height is too big but the width is smaller
        if (originalWidth < maxImageWidth && originalHeight > maxImageHeight) {
            newHeight = maxImageHeight;
            ratio = newHeight/originalHeight;
            newWidth = ratio*originalWidth;
        }


        // If both the width and the height are too small or too big
        if ((originalWidth < maxImageWidth && originalHeight < maxImageHeight) ||
                (originalWidth > maxImageWidth && originalHeight > maxImageHeight)) {
            if (originalWidth > originalHeight) {
                newWidth = maxImageWidth;
                ratio = newWidth/originalWidth;
                newHeight = ratio*originalHeight;
            }
            else if (originalWidth < originalHeight) {
                newHeight = maxImageHeight;
                ratio = newHeight/originalHeight;
                newWidth = ratio*originalWidth;
            }
            // The width and height are the same i.e. the image is square. Make the width and height equal to the
            // maximum screen height
            else {
                newWidth = maxImageHeight;
                newHeight = maxImageHeight;
            }
        }

        // Round the dimensions properly and cast to int (since Math.round gives a long)
        int finalWidth = (int) Math.round(newWidth);
        int finalHeight = (int) Math.round(newHeight);

        // Testing
//        System.out.println("Maximum Image Dimensions: " + maxImageWidth + " x " + maxImageHeight);
//        System.out.println("Original Dimensions: " + originalWidth + " x " + originalHeight);
//        System.out.println("Ratio: " + ratio);
//        System.out.println("New Dimensions: " + newWidth + " x " + newHeight);
//        System.out.println("Final Rounded Dimensions: " + finalWidth + " x " + finalHeight);

        // Scale the image
        Image scaledImage = image.getScaledInstance(finalWidth, finalHeight, Image.SCALE_DEFAULT);

        // Set the scaled image as the JLabel
        pictureIcon.setImage(scaledImage);
        pictureLabel.setIcon(pictureIcon);

        // Set constraints for formatting the image
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;

        // Add Image to the central panel in the JFrame
        mainPanel.add(pictureLabel, constraints);
    }


    /**
     * Displays a billboard which has only a picture
     * @param picture - a string which is the picture to display, this could be in the form of a url or data attribute
     * @param pictureType - a string which is either url or data, so that we can decode the image
     * TODO: Move some stuff out of this method and create a new method which reads in an images and returns the image.
     */
    public void pictureOnlyBillboard(String picture, String pictureType) {
        // Decide if it's a url or data attribute
        if (pictureType.equals("url")) {
            // Try to extract picture from url, if this fails catch the exception
            try {
                // Extract picture from URL
                URL pictureURL = new URL(picture);
                BufferedImage pictureImage = ImageIO.read(pictureURL);

                // Add the image to the central panel
                addPictureToPanel(pictureImage);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            // Try to decode the base-64 encoded representation of the image
            try {
                // Decode data attribute from url and convert to an image
                byte[] pictureByteArray = Base64.getDecoder().decode(picture.getBytes(StandardCharsets.UTF_8));
                BufferedImage pictureImage = ImageIO.read(new ByteArrayInputStream(pictureByteArray));

                // Add the image to the central panel
                addPictureToPanel(pictureImage);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * Calculates the largest font size for the information string. The text should be displayed in the centre, with
     * word wrapping and font size chosen so that the text fills up no more than 75% of the screen's width and 50% of
     * the screen's height.
     * @param information - the string to display on the screen
     * @return fontSize - the largest font size that the information text can be displayed at
     * TODO: Somehow calculate the number of lines.
     */
    public int getInformationFontSize(String information) {
        // Get the current font size and initialise the variable to return
        int currentFontSize = informationLabel.getFont().getSize();
        int fontSize = currentFontSize;

        // Calculate what the height of the string would be based on the current font size
        FontMetrics fontMetrics = informationLabel.getFontMetrics(new Font(informationLabel.getFont().getName(),
                Font.BOLD, currentFontSize));
        double lineHeight = fontMetrics.getHeight();
        double numLines = 2;
        double stringHeight = lineHeight * numLines;
        double maxStringHeight = screenHeight*0.5;

        // Testing
        System.out.println("maxStringHeight = " + maxStringHeight);
        System.out.println("lineHeight = " + lineHeight);
        System.out.println("numLines = " + numLines);
        System.out.println("stringHeight = " + stringHeight);
        System.out.println("fontSize = " + fontSize + "\n");

        // While the current height of the string is less than the maximum string height (50% of the screen height)
        while (stringHeight < maxStringHeight) {
            // Increase the font size
            fontSize = fontSize + 1;

            // Recalculate the string height
            fontMetrics = informationLabel.getFontMetrics(new Font(informationLabel.getFont().getName(), Font.BOLD, fontSize));
            lineHeight = fontMetrics.getHeight();
            numLines = 2;
            stringHeight = lineHeight * numLines;
        }

        // Testing
        System.out.println("lineHeight = " + lineHeight);
        System.out.println("numLines = " + numLines);
        System.out.println("stringHeight = " + stringHeight);
        System.out.println("lineHeight = " + lineHeight);
        System.out.println("fontSize = " + fontSize);

        return fontSize;
    }


    /**
     * Displays a billboard which has only a information
     * @param information - a string which stores the information to display
     * TODO: The text should be displayed in the centre, with word wrapping and font size chosen so that the text fills
     *       up no more than 75% of the screen's width and 50% of the screen's height.
     */
    public void informationOnlyBillboard(String information) {
        // Set the text and font of the information label
        double maxStringWidth = screenWidth*0.75;
        String informationHTML = "<html><div style='width: " + maxStringWidth + "; text-align: center;'>" + information
                + "</div></html>";
        informationLabel.setText(informationHTML);
        informationLabel.setFont(new Font(informationLabel.getFont().getName(), Font.BOLD, 40));

        int fontSize = getInformationFontSize(information);
//        informationLabel.setFont(new Font(informationLabel.getFont().getName(), Font.BOLD, fontSize));

        // Add information label to the central panel in the JFrame
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;

        // Add information to main panel
        mainPanel.add(informationLabel, constraints);
    }


    /**
     * Displays a billboard which has a message and picture
     * TODO
     */
    public void messagePictureBillboard(HashMap<String, String> billboardData) {

    }


    /**
     * Displays a billboard which has a message and information
     * TODO
     */
    public void messageInformationBillboard(HashMap<String, String> billboardData) {

    }

    /**
     * Displays a billboard which has a picture and information
     * TODO
     */
    public void pictureInformationBillboard(HashMap<String, String> billboardData) {

    }


    /**
     * Displays a billboard which has all the features: a message, picture and information
     * TODO
     */
    public void allFeaturesBillboard(HashMap<String, String> billboardData) {

    }


    /**
     * Formats the display window using the mock database depending on whether there is a message,
     * picture, information or a combination.
     * TODO: Check the if statements to make sure they have the correct format
     */
    public void formatBillboard(HashMap<String, String> billboardData) {
        // Retrieve all the data from the HashMap
        String backgroundColour = billboardData.get("Background Colour");
        String message = billboardData.get("Message");
        String messageColour = billboardData.get("Message Colour");
        String picture = billboardData.get("Picture");
        String pictureType = billboardData.get("Picture Type");
        String information = billboardData.get("Information");
        String informationColour = billboardData.get("Information Colour");

        // Check all cases and decide which method to call
        if (message != null && picture != null && information != null) {
            allFeaturesBillboard(billboardData);
        }
        else if (message != null && picture != null) {
            messagePictureBillboard(billboardData);
        }
        else if (message != null && information != null) {
            messageInformationBillboard(billboardData);
        }
        else if (picture != null && information != null) {
            pictureInformationBillboard(billboardData);
        }
        else if (message != null) {
            messageOnlyBillboard(message);
        }
        else if (picture != null) {
            pictureOnlyBillboard(picture, pictureType);
        }
        else if (information != null) {
            informationOnlyBillboard(information);
        }

        // Check if there's a specific background colour
        if (backgroundColour != null) {
            mainPanel.setBackground(Color.decode(backgroundColour));
        }
        else {
            mainPanel.setBackground(Color.WHITE);
        }

        // Check if there's a specific message text colour
        if (message != null) {
            if (messageColour != null) {
                messageLabel.setForeground(Color.decode(messageColour));
                messageLabel.setBackground(Color.decode(messageColour));
            }
            else {
                messageLabel.setForeground(Color.BLACK);
                messageLabel.setBackground(Color.BLACK);
            }
        }

        // Check if there's a specific information text colour
        if (information != null) {
            if (informationColour != null) {
                informationLabel.setForeground(Color.decode(informationColour));
                informationLabel.setBackground(Color.decode(informationColour));
            }
            else {
                informationLabel.setForeground(Color.BLACK);
                informationLabel.setBackground(Color.BLACK);
            }
        }

    }

    /**
     * If there are no billboards to display, display a message for the user.
     */
    public void noBillboardToDisplay() {
        // Create a label to display a message and format it
        String noBillboardMessage = "There are no billboards to display right now.";
        messageLabel.setText(noBillboardMessage);

        // Choose the correct font size
        int fontSize = getMessageFontSize(noBillboardMessage);
        messageLabel.setFont(new Font(messageLabel.getFont().getName(), Font.BOLD, fontSize));

        // Add the message label to the JFrame
        mainPanel.add(messageLabel);
    }


    /**
     * Exits the window if the user presses the escape key.
     */
    public void listenEscapeKey() {
        KeyListener escListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    System.exit(0);
                }
            }
        };

        addKeyListener(escListener);
    }


    /**
     * Exits the window if the user clicks anywhere on the window.
     */
    public void listenMouseClick() {
        MouseListener clickListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                System.exit(0);
            }
        };

        addMouseListener(clickListener);
    }


    /**
     * Show the GUI for displaying the billboard.
     */
    public void showViewer() {
        // Displaying the window to be completely full screen
        setExtendedState(Frame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setVisible(true);
    }


    /**
     * Displays the billboards
     */
    public void displayBillboard() {
        setupBillboard();

        File fileToDisplay = extractXMLFile(2);
        HashMap<String, String> billboardData = extractDataFromXML(fileToDisplay);

        formatBillboard(billboardData);

//        String picture = billboardData.get("Picture");
//        String pictureType = billboardData.get("Picture Type");
//        pictureOnlyBillboard(picture, pictureType);

//        noBillboardToDisplay();

        listenEscapeKey();
        listenMouseClick();
        showViewer();
    }

    /**
     * Displays an error if the viewer cannot connect to the server
     */
    public void displayError() {
        setupBillboard();

        // Add an error message label to the central panel of the JFrame
        String errorMessage = "Error: Cannot connect to server. Trying again now...";
        messageLabel.setText(errorMessage);
        int fontSize = getMessageFontSize(errorMessage);
        messageLabel.setFont(new Font(messageLabel.getFont().getName(), Font.BOLD, fontSize));
        mainPanel.add(messageLabel);

        listenEscapeKey();
        listenMouseClick();
        showViewer();
    }


    @Override
    public void run() {
        displayBillboard();
//        displayError();
    }

    public static void main(String[] args ) {
        //TODO: NEED TO FACILITATE THE CONNECTION EVERY 15 SECONDS
        try {
            Object serverResponse = Helpers.initClient("Viewer");
            System.out.println("Received from server: " + serverResponse.toString());
        } catch (IOException | ClassNotFoundException e) { // Could not connect to server
            //TODO: USE GUI TO HANDLE EXCEPTION + NOTIFY USER
            System.err.println("Exception caught: " + e);
        }
        SwingUtilities.invokeLater(new Viewer("Billboard Viewer"));
        //displayBillboard();
    }
}