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
     * Reads in a picture from a file based on whether it is url or data
     * @param picture - a string which is the picture to read in, this could be in the form of a url or data attribute
     * @param pictureType - a string which is either url or data, so that we can decode the image
     * @return pictureImage - a BufferedImage which is the picture so it can be displayed on the billboard
     */
    public BufferedImage readPictureFromFile(String picture, String pictureType) {
        BufferedImage pictureImage = null;

        // Decide if it's a url or data attribute
        if (pictureType.equals("url")) {
            // Try to extract picture from url, if this fails catch the exception
            try {
                // Extract picture from URL and convert to an image
                URL pictureURL = new URL(picture);
                pictureImage = ImageIO.read(pictureURL);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            // Try to decode the base-64 encoded representation of the image
            try {
                // Decode data attribute from url and convert to an image
                byte[] pictureByteArray = Base64.getDecoder().decode(picture.getBytes(StandardCharsets.UTF_8));
                pictureImage = ImageIO.read(new ByteArrayInputStream(pictureByteArray));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return pictureImage;
    }


    /**
     * Scales the given image to be no larger than the maximum width and no larger than the maximum height
     * @param image - the image that needs to be scaled
     * @param maxImageWidth - the largest width the image can be displayed as on the screen
     * @param maxImageHeight - the largest height the image can be displayed as on the screen
     * @return scaledImage - an Image which is the scaled version of the input image
     */
    public Image getScaledImage(BufferedImage image, double maxImageWidth, double maxImageHeight) {
        // Find the current dimensions of the picture
        double originalWidth = image.getWidth();
        double originalHeight = image.getHeight();

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

        return scaledImage;
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
     * Calculates the largest font size for the information string. The text should be displayed in the centre, with
     * word wrapping and font size chosen so that the text fills up no more than 75% of the screen's width and 50% of
     * the screen's height.
     * @param information - the string to display on the screen
     * @param maxStringWidth - the maximum width the string can be
     * @param maxStringHeight - the maximum height the string can be
     * @return fontSize - the largest font size that the information text can be displayed at
     * TODO: FIND A NEW WAY TO DO THIS!!!
     */
    public int getInformationFontSize(String information, double maxStringWidth, double maxStringHeight) {
        // Get the current font size and initialise the variable to return
        int currentFontSize = informationLabel.getFont().getSize();
        int fontSize = currentFontSize;



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
     * Displays a billboard which has only a picture which is no more than 50% of the screen height and no more than
     * 50% of the screen width
     * @param picture - a string which stores the picture to display, in the form of a url or data attribute
     * @param pictureType - a string which specifies the type of picture, either url or data
     */
    public void pictureOnlyBillboard(String picture, String pictureType) {
        // Read in the picture
        BufferedImage pictureImage = readPictureFromFile(picture, pictureType);

        // Define the maximum image dimensions based on the screen dimensions
        double maxImageWidth = screenWidth/2;
        double maxImageHeight = screenHeight/2;

        // Scale the image we are displaying based on the maximum image dimensions
        Image scaledImage = getScaledImage(pictureImage, maxImageWidth, maxImageHeight);

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
     * Displays a billboard which has only a information
     * @param information - a string which stores the information to display
     * TODO: The text should be displayed in the centre, with word wrapping and font size chosen so that the text fills
     *       up no more than 75% of the screen's width and 50% of the screen's height.
     */
    public void informationOnlyBillboard(String information) {
        // Set the text and font of the information label
        double maxStringWidth = screenWidth*0.75;
        double maxStringHeight = screenHeight*0.5;

        String informationHTML = "<html><div style='width: " + maxStringWidth + "; text-align: center;'>"
                + information + "</div></html>";
        informationLabel.setText(informationHTML);

        informationLabel.setFont(new Font(informationLabel.getFont().getName(), Font.BOLD, 40));

        int fontSize = getInformationFontSize(information, maxStringWidth, maxStringHeight);
//        informationLabel.setFont(new Font(informationLabel.getFont().getName(), Font.BOLD, fontSize));

        // Add information label to the central panel in the JFrame
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;

        // Add information to main panel
        mainPanel.add(informationLabel, constraints);
    }


    /**
     * Displays a billboard which has a message and picture. The picture is displayed in the center of the bottom 2/3
     * of the screen, and the message is displayed in the center of the remaining top of the screen.
     * @param message - a string which stores the message to display
     * @param picture - a string which stores the picture to display, in the form of a url or data attribute
     * @param pictureType - a string which specifies the type of picture, either url or data
     */
    public void messagePictureBillboard(String message, String picture, String pictureType) {
        // Set the text of the message label
        messageLabel.setText(message);

        // Choose the font size so the message fits in one line
        int fontSize = getMessageFontSize(message);
        Font messageFont = new Font(messageLabel.getFont().getName(), Font.BOLD, fontSize);
        messageLabel.setFont(messageFont);

        // Get the message height
        double messageHeight = messageLabel.getFontMetrics(messageFont).getHeight();

        // Read in the picture
        BufferedImage pictureImage = readPictureFromFile(picture, pictureType);

        // Define the maximum image dimensions based on the screen dimensions
        double maxImageWidth = screenWidth/2;
        double maxImageHeight = screenHeight/2;

        // Scale the image we are displaying based on the maximum image dimensions
        Image scaledImage = getScaledImage(pictureImage, maxImageWidth, maxImageHeight);

        // Set the scaled image as the JLabel
        pictureIcon.setImage(scaledImage);
        pictureLabel.setIcon(pictureIcon);

        // Calculate vertical padding for image - the image should fit in the center of bottom 2/3 of screen
        int picturePadding = (int) ( (2*(screenHeight/3)) - scaledImage.getHeight(null) )/2;

        // Calculate vertical padding for message - the message should fit in the centre of the remaining space at the
        // top of the screen
        int messageTopPadding = (int) ( (screenHeight/3) + picturePadding - messageHeight )/2;
        int messageBottomPadding = messageTopPadding - picturePadding;

        // Create a grid bag constraints for the message
        GridBagConstraints messageConstraints = new GridBagConstraints();
        messageConstraints.anchor = GridBagConstraints.CENTER;
        messageConstraints.gridx = 0;
        messageConstraints.gridy = 0;
        messageConstraints.insets = new Insets(messageTopPadding, 0, messageBottomPadding, 0);

        // Create a grid bag constraints for the picture
        GridBagConstraints pictureConstraints = new GridBagConstraints();
        pictureConstraints.anchor = GridBagConstraints.CENTER;
        pictureConstraints.gridx = 0;
        pictureConstraints.gridy = 1;
        pictureConstraints.gridheight = 2;
        pictureConstraints.insets = new Insets(picturePadding, 0, picturePadding, 0);

        // Testing
//        System.out.println("Screen Dimensions: " + screenWidth + " x " + screenHeight);
//        System.out.println("1/3 of Screen Height = " + screenHeight/3);
//        System.out.println("2/3 of Screen Height = " + 2*screenHeight/3);
//        System.out.println("Message Padding = " + messageTopPadding);
//        System.out.println("Picture Padding = " + picturePadding);

        // Add the message and image to the billboard
        mainPanel.add(messageLabel, messageConstraints);
        mainPanel.add(pictureLabel, pictureConstraints);
    }


    /**
     * Displays a billboard which has a message and information. The message is displayed in the center of the top half
     * of the screen and the information is displayed in the center of the bottom half of the screen.
     * @param message - a string which stores the message to display
     * @param information - a string which stores the information to display
     * TODO: Format properly once information label can be displayed properly on the screen.
     */
    public void messageInformationBillboard(String message, String information) {
        // Set the text of the message label
        messageLabel.setText(message);

        // Choose the font size so the message fits in one line
        int fontSize = getMessageFontSize(message);
        Font messageFont = new Font(messageLabel.getFont().getName(), Font.BOLD, fontSize);
        messageLabel.setFont(messageFont);

        // Get the message height
        double messageHeight = messageLabel.getFontMetrics(messageFont).getHeight();

        // Set the text of the information label
        informationLabel.setText(information);
        informationLabel.setFont(new Font(informationLabel.getFont().getName(), Font.BOLD, 30));

        // Calculate vertical padding for message - the message should fit in the centre of the remaining space at the
        // top of the screen
        int messagePadding = (int) ( (screenHeight/2)  - messageHeight )/2;
        System.out.println(messagePadding);

        // Create a grid bag constraints for message
        GridBagConstraints messageConstraints = new GridBagConstraints();
        messageConstraints.gridx = 0;
        messageConstraints.gridy = 0;
        messageConstraints.insets = new Insets(messagePadding, 0, messagePadding, 0);

        // Create a grid bag constraints for information
        GridBagConstraints informationConstraints = new GridBagConstraints();
        informationConstraints.gridx = 0;
        informationConstraints.gridy = 1;
        messageConstraints.insets = new Insets(10, 0, 200, 0);

        // Add message and information
        mainPanel.add(messageLabel, messageConstraints);
        mainPanel.add(informationLabel, informationConstraints);
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
            messagePictureBillboard(message, picture, pictureType);
        }
        else if (message != null && information != null) {
            messageInformationBillboard(message, information);
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

        File fileToDisplay = extractXMLFile(7);
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
    }
}