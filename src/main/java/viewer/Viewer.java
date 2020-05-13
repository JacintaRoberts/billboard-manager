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
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


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
    private static double screenWidthBorder = 75;               // Horizontal spacing on borders


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
    public static File extractXMLFile(int fileNum) {
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
    public static HashMap<String, String> extractDataFromXML(File xmlFile) {
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
    public static BufferedImage readPictureFromFile(String picture, String pictureType) {
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

        // Scale the image
        Image scaledImage = image.getScaledInstance(finalWidth, finalHeight, Image.SCALE_DEFAULT);

        // Return scaled image
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

        // While the current width of the string is less than the screen width minus some threshold for the border
        while (stringWidth < (screenWidth - screenWidthBorder*2)) {
            // Increase the font size
            fontSize = fontSize + 1;

            // Recalculate the string width
            fontMetrics = messageLabel.getFontMetrics(new Font(messageLabel.getFont().getName(), Font.BOLD, fontSize));
            stringWidth = fontMetrics.stringWidth(message);
        }

        return fontSize;
    }


    /**
     * Calculates and sets the largest font size for the information string so that the text fills up no more than
     * 50% of the screen's height. The information font size is always smaller than the message font size.
     * @param information - the string to display on the screen
     * @param maxStringHeight - the maximum height the string can be
     * @param message - a boolean which is true if there is a message displayed on the screen and false if there isn't
     */
    public void setInformationFontSize(String information, double maxStringHeight, boolean message) {
        // Get the current font size and initialise the variable to return
        int fontSize = informationLabel.getFont().getSize();

        // Define the maximum font size to be a very large number
        int maxFontSize = 500;

        // If there is a message displayed, make the maximum font size the font size of the message.
        if (message) {
            maxFontSize = messageLabel.getFont().getSize();
        }

        // Get the preferred size and set that to be the size of the label
        double currentHeight = informationLabel.getPreferredSize().height;
        double currentWidth = informationLabel.getPreferredSize().width;
        informationLabel.setSize((int) currentWidth, (int) currentHeight);

        // A threshold to ensure the height doesn't exceed the maximum height
        double threshold = maxStringHeight*0.03;

        // Check if the current height of the label is larger than the maximum string height and if the font size is
        // larger than the maximum font size.
        while (currentHeight < maxStringHeight - threshold && fontSize < maxFontSize - 1) {
            fontSize = fontSize + 1;

            // Update font size
            informationLabel.setFont(new Font(informationLabel.getFont().getName(), Font.BOLD, fontSize));

            // Recalculate height of label
            currentHeight = informationLabel.getPreferredSize().height;
            currentWidth = informationLabel.getPreferredSize().width;

            // Update the size of the label
            informationLabel.setSize((int) currentWidth, (int) currentHeight);

            // Testing
//            System.out.println("Size of label: " + currentWidth + " x " + currentHeight);
//            System.out.println("Font Size: " + fontSize);
        }
    }


    /**
     * Returns a string which formats the information string so that the text is wrapped, centered, and does not exceed
     * the maximum string width
     * @param information - the information string to format
     * @param maxStringWidth - the maximum string width for information string
     * @return informationHTML - formatted HTML version of information string
     */
    public String getInformationHTMLString(String information, int maxStringWidth) {
        String informationHTML = "<html><div style='width: " + maxStringWidth + "; text-align: center;'>"
                + information + "</div></html>";

        return informationHTML;
    }


    /**
     * Sets up the message so that it can be added to the billboard.
     * @param message - a string which stores the message to display
     * @return messageHeight - a double which is the height of the message (for formatting and spacing later)
     */
    public double setUpMessage(String message) {
        // Set the text of the message label
        messageLabel.setText(message);

        // Choose the font size so the message fits in one line
        int fontSize = getMessageFontSize(message);
        Font messageFont = new Font(messageLabel.getFont().getName(), Font.BOLD, fontSize);
        messageLabel.setFont(messageFont);

        // Calculate the message height
        double messageHeight = messageLabel.getFontMetrics(messageFont).getHeight();

        // Return the message height
        return messageHeight;
    }


    /**
     * Sets up the picture so that it can be added to the billboard.
     * @param picture - a string which stores the picture to display
     * @param pictureType - a string which specifies the type of picture - either "url" or "data
     * @param maxImageWidth - the maximum width of the image
     * @param maxImageHeight - the maximum height of the image
     * @return pictureHeight - a double which is the height of the picture (for formatting and spacing later)
     */
     public double setUpPicture(String picture, String pictureType, double maxImageWidth, double maxImageHeight) {
         // Read in the picture
         BufferedImage pictureImage = readPictureFromFile(picture, pictureType);

         // Scale the image we are displaying based on the maximum image dimensions
         Image scaledImage = getScaledImage(pictureImage, maxImageWidth, maxImageHeight);

         // Set the scaled image as the JLabel
         pictureIcon.setImage(scaledImage);
         pictureLabel.setIcon(pictureIcon);

         // Calculate the height of the picture
         int pictureHeight = scaledImage.getHeight(null);

         // Return the picture height
         return pictureHeight;
     }

    /**
     * Sets up the information so that it can be added to the billboard.
     * @param information - a string which stores the information to display
     * @param maxStringWidth - the maximum width of the information label
     * @param maxStringHeight - the maximum height of the information label
     * @param message - a boolean which is true if there is a message also being displayed and false if there isn't
     * @return informationHeight - a double which is the height of the information (for formatting and spacing later)
     */
     public double setUpInformation(String information, double maxStringWidth, double maxStringHeight,
                                    boolean message) {
         // Use html styling
         String informationHTML = getInformationHTMLString(information, (int) maxStringWidth);

         // Set the text of the information label
         informationLabel.setText(informationHTML);

         // Choose and set the font size based on the maximum height of the information label
         setInformationFontSize(information, maxStringHeight, message);

         // Calculate the height of the information label
         double informationHeight = informationLabel.getSize().height;

         // Return the height of the information label
         return informationHeight;
     }


    /**
     * Sets up a grid bag constraints for an element which will be added to the billboard
     * @param gridx - the x position of the element
     * @param gridy - the y position of the element
     * @param gridHeight - the grid height of the element
     * @param topPadding - the padding to add on the top of the element
     * @param bottomPadding - the padding to add on the bottom of the element
     * @return constraints - a GridBagConstraints which has all of the above constraints
     */
     public GridBagConstraints setGridBagConstraints(int gridx, int gridy, int gridHeight, int topPadding,
                                                     int bottomPadding) {
         // Initialise a grid bag constraints
         GridBagConstraints constraints = new GridBagConstraints();

         // Set up the grid bag constraints based on the inputs
         constraints.gridx = gridx;
         constraints.gridy = gridy;
         constraints.gridheight = gridHeight;
         constraints.insets = new Insets(topPadding, 0, bottomPadding, 0);

         // Return the grid bag constraints
         return constraints;
     }


    /**
     * Displays a billboard which has only a message
     * @param message - a string which stores the message to display
     */
    public void messageOnlyBillboard(String message) {
        // Set up the message and add the message label to the central panel in the JFrame
        setUpMessage(message);
        mainPanel.add(messageLabel);
    }


    /**
     * Displays a billboard which has only a picture which is no more than 50% of the screen height and no more than
     * 50% of the screen width
     * @param picture - a string which stores the picture to display, in the form of a url or data attribute
     * @param pictureType - a string which specifies the type of picture, either url or data
     */
    public void pictureOnlyBillboard(String picture, String pictureType) {
        // Define the maximum image dimensions based on the screen dimensions
        double maxImageWidth = screenWidth/2;
        double maxImageHeight = screenHeight/2;

        // Set up the picture and add it to the main panel
        setUpPicture(picture, pictureType, maxImageWidth, maxImageHeight);
        mainPanel.add(pictureLabel);
    }


    /**
     * Displays a billboard which has only a information, which is no more than 75% of the screen's width and 50% of
     * the screen's height.
     * @param information - a string which stores the information to display
     */
    public void informationOnlyBillboard(String information) {
        // Calculate the maximum string width and height for the information
        double maxStringWidth = screenWidth*0.75;
        double maxStringHeight = screenHeight*0.5;

        // Set up the information label and add it to the main panel
        setUpInformation(information, maxStringWidth, maxStringHeight, false);
        mainPanel.add(informationLabel);
    }


    /**
     * Displays a billboard which has a message and picture. The picture is displayed in the center of the bottom 2/3
     * of the screen, and the message is displayed in the center of the remaining top of the screen.
     * @param message - a string which stores the message to display
     * @param picture - a string which stores the picture to display, in the form of a url or data attribute
     * @param pictureType - a string which specifies the type of picture, either url or data
     */
    public void messagePictureBillboard(String message, String picture, String pictureType) {
        // Define the maximum image dimensions based on the screen dimensions
        double maxImageWidth = screenWidth/2;
        double maxImageHeight = screenHeight/2;

        // Set up the elements and get their height
        double messageHeight = setUpMessage(message);
        double pictureHeight = setUpPicture(picture, pictureType, maxImageWidth, maxImageHeight);

        // Calculate vertical padding for image. It should fit in the center of bottom 2/3 of screen.
        int picturePadding = (int) (((2*(screenHeight/3)) - pictureHeight ) / 2);

        // Calculate vertical padding for message. It should fit in the centre of the remaining space at the top.
        int messagePadding = (int) (((screenHeight/3) + picturePadding - messageHeight ) / 2);

        // Create grid bag constraints for the message and picture
        GridBagConstraints messageConstraints = setGridBagConstraints(0, 0, 1, messagePadding,
                messagePadding);
        GridBagConstraints pictureConstraints = setGridBagConstraints(0, 1, 2, 0,
                picturePadding);

        // Add the message and image to the billboard
        mainPanel.add(messageLabel, messageConstraints);
        mainPanel.add(pictureLabel, pictureConstraints);
    }


    /**
     * Displays a billboard which has a message and information. The message is displayed in the center of the top half
     * of the screen and the information is displayed in the center of the bottom half of the screen.
     * @param message - a string which stores the message to display
     * @param information - a string which stores the information to display
     */
    public void messageInformationBillboard(String message, String information) {
        // Set up the message label and get it's height
        double messageHeight = setUpMessage(message);

        // Calculate the maximum string width and height for the information
        double maxStringWidth = screenWidth*0.75;
        double maxStringHeight = screenHeight*0.25;

        // Set up the message label and get it's height
        double informationHeight = setUpInformation(information, maxStringWidth, maxStringHeight, true);

        // Calculate vertical padding for the message. It should be in the centre of the top half of the screen.
        int messagePadding = (int) (((screenHeight/2)  - messageHeight) / 2);

        // Calculate vertical padding for the information. It should be in the centre of the top half of the screen.
        int informationPadding = (int) (((screenHeight/2)  - informationHeight) / 2);

        // Create grid bag constraints for message and information
        GridBagConstraints messageConstraints = setGridBagConstraints(0, 0, 1, messagePadding,
                messagePadding);
        GridBagConstraints informationConstraints = setGridBagConstraints(0, 1, 1,
                informationPadding, informationPadding);

        // Add message and information to the main panel
        mainPanel.add(messageLabel, messageConstraints);
        mainPanel.add(informationLabel, informationConstraints);
    }

    /**
     * Displays a billboard which has a picture and information
     * @param picture - a string which stores the picture to display, in the form of a url or data attribute
     * @param pictureType - a string which specifies the type of picture, either url or data
     * @param information - a string which stores the information to display
     * TODO: CHECK - What is the maximum height of the information?
     */
    public void pictureInformationBillboard(String picture, String pictureType, String information) {
        // Define the maximum image dimensions based on the screen dimensions
        double maxImageWidth = screenWidth/2;
        double maxImageHeight = screenHeight/2;

        // Set up the picture and get it's height
        double pictureHeight = setUpPicture(picture, pictureType, maxImageWidth, maxImageHeight);

        // Calculate vertical padding for picture. It should be in the centre of the top 2/3 of the screen.
        int picturePadding = (int) ( ((2*(screenHeight/3)) - pictureHeight)/2 );

        // Calculate the maximum string width and height for the information
        double maxStringWidth = screenWidth*0.75;
        double maxStringHeight = (screenHeight - pictureHeight - picturePadding) / 2;

        // Set up the picture and get it's height
        double informationHeight = setUpInformation(information, maxStringWidth, maxStringHeight, false);

        // Calculate vertical padding for information. It should be in the centre of the remaining space at the bottom.
        int informationPadding = (int) ( ((screenHeight/3) + picturePadding - informationHeight)/2 );

        // Create grid bag constraints for the picture and information
        GridBagConstraints pictureConstraints = setGridBagConstraints(0, 0, 2, picturePadding,
                0);
        GridBagConstraints informationConstraints = setGridBagConstraints(0, 2, 1,
                informationPadding, informationPadding);

        // Add the message and image to the billboard
        mainPanel.add(informationLabel, informationConstraints);
        mainPanel.add(pictureLabel, pictureConstraints);
    }


    /**
     * Displays a billboard which has all the features: a message, picture and information
     * @param message - a string which stores the message to display
     * @param picture - a string which stores the picture to display, in the form of a url or data attribute
     * @param pictureType - a string which specifies the type of picture, either url or data
     * @param information - a string which stores the information to display
     * TODO: CHECK - What is the maximum height of the information?
     */
    public void allFeaturesBillboard (String message, String picture, String pictureType, String information) {
        // Define the maximum image dimensions based on the screen dimensions
        double maxImageWidth = screenWidth/3;
        double maxImageHeight = screenHeight/3;

        // Set up the picture and the message and get their heights
        double messageHeight = setUpMessage(message);
        double pictureHeight = setUpPicture(picture, pictureType, maxImageWidth, maxImageHeight);

        // Calculate vertical padding of message. It should be centred in the top part of the screen.
        int messagePadding = (int) (((screenHeight/2) - (pictureHeight/2) - messageHeight) / 2);

        // Calculate the maximum string width and height for the information
        double maxInformationWidth = screenWidth*0.75;
        double maxInformationHeight = ((screenHeight/2) - (pictureHeight/2)) / 2;

        // Set up the information and get it's height
        double informationHeight = setUpInformation(information, maxInformationWidth, maxInformationHeight,
                true);

        // Calculate vertical padding of information. It should be centred in the bottom part of the screen.
        int informationPadding = (int) (((screenHeight/2) - (pictureHeight/2) - informationHeight) / 2);

        // Create grid bag constraints for the elements
        GridBagConstraints messageConstraints = setGridBagConstraints(0, 0, 1, messagePadding,
                messagePadding);
        GridBagConstraints pictureConstraints = setGridBagConstraints(0, 1, 1, 0,
                0);
        GridBagConstraints informationConstraints = setGridBagConstraints(0, 2, 1,
                informationPadding, informationPadding);

        // Add the message and image to the billboard
        mainPanel.add(messageLabel, messageConstraints);
        mainPanel.add(pictureLabel, pictureConstraints);
        mainPanel.add(informationLabel, informationConstraints);
    }


    /**
     * Formats the display window using the mock database depending on whether there is a message,
     * picture, information or a combination.
     * @param billboardData - a HashMap which stores the background colour, message, message colour, picture,
     *        picture type (data or url), information, and information colour of the billboard, if there is no content
     *        for one or more of these tags, the string is null
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
            allFeaturesBillboard(message, picture, pictureType, information);
        }
        else if (message != null && picture != null) {
            messagePictureBillboard(message, picture, pictureType);
        }
        else if (message != null && information != null) {
            messageInformationBillboard(message, information);
        }
        else if (picture != null && information != null) {
            pictureInformationBillboard(picture, pictureType, information);
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

        // Check if there's a specific background colour, else set it to be white
        if (backgroundColour != null) {
            mainPanel.setBackground(Color.decode(backgroundColour));
        }
        else {
            mainPanel.setBackground(Color.WHITE);
        }

        // Check if there's a specific message text colour, else set it to be black
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

        // Check if there's a specific information text colour, else set it to be black
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
        // Set up the message to display and add it to the main panel
        String noBillboardMessage = "There are no billboards to display right now.";
        setUpMessage(noBillboardMessage);
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
     * @param serverResponse
     */
    public void displayBillboard(Object serverResponse) {
        // TODO: Do something with serverResponse
        setupBillboard();

        File fileToDisplay = extractXMLFile(6);
        HashMap<String, String> billboardData = extractDataFromXML(fileToDisplay);

        formatBillboard(billboardData);
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

        // Set up and add an error message label to the main panel
        String errorMessage = "Error: Cannot connect to server. Trying again now...";
        setUpMessage(errorMessage);
        mainPanel.add(messageLabel);

        listenEscapeKey();
        listenMouseClick();
        showViewer();
    }



    // Determines whether a billboard xml was received from the server
    private Boolean noBillboard() {
        if ( serverResponse.toString().isEmpty() ) { return true; }
        else { return false; }
    }

    // Contains the server's response (Billboard XML)
    private Object serverResponse;

    @Override
    public void run() {
        try {
            //TODO: May want to cast the return type to XML - probably Alan and Kanu figure this out.
            serverResponse = Helpers.initClient("Viewer");
            System.out.println("Received from server: " + serverResponse.toString());
            displayBillboard(serverResponse);
            if (noBillboard()) {
                noBillboardToDisplay(); // Show no billboard screen
            }
        } catch (IOException | ClassNotFoundException e) {
            displayError(); // Error in receiving content
        }
    }

    public static void main(String[] args ) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(new Viewer("Billboard Viewer"), 0, 15, TimeUnit.SECONDS);
        //SwingUtilities.invokeLater(new Viewer("Billboard Viewer")); // This is now redundant I think...
    }
}