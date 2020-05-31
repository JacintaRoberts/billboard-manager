package viewer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;


/**
 * Viewer sets up and creates the GUI for displaying the billboards.
 */
public class Viewer extends JFrame {

    // Set up the panels, labels, image icons etc. to display the different parts of the billboard
    private JPanel mainPanel;
    private JLabel messageLabel;
    private ImageIcon pictureIcon;
    private JLabel pictureLabel;
    private JLabel informationLabel;

    // Dimensions of screen the viewer will display on
    private double SCREEN_HEIGHT;
    private double SCREEN_WIDTH;


    /**
     * Constructor method for Viewer class.
     * @throws HeadlessException Thrown when code that is dependent on a keyboard, display, or mouse is called in an
     * environment that does not support a keyboard, display, or mouse.
     */
    public Viewer() throws HeadlessException {
        // Set up the panels, labels, image icons etc. to display the different parts of the billboard
        mainPanel = new JPanel();
        messageLabel = new JLabel();
        pictureIcon = new ImageIcon();
        pictureLabel = new JLabel();
        informationLabel = new JLabel();

        // Set a consistent font
        Font font = new Font("Garamond",  Font.BOLD, 12);
        messageLabel.setFont(font);
        pictureLabel.setFont(font);
        informationLabel.setFont(font);

        // Remove the borders of the JFrame
        setUndecorated(true);

        // Dimensions of screen the viewer will display on
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        SCREEN_HEIGHT = screenSize.height;
        SCREEN_WIDTH = screenSize.width;
    }


    /**
     * Parses in the xml file from a mock "database", that we want to display from the given test xml files. These xml
     * files are stored in resources. The mock database simply stores the file names.
     * @param fileNum An int which is the file name (number), of the example xml files from the mock "database".
     * @return Returns a Document object which is the xml file, so that the xml tags can be extracted.
     * @throws ParserConfigurationException Throws an exception if there is an error from either the XML parser or the application.
     * @throws IOException Throws an exception if an I/O exception of some sort has occurred.
     * @throws SAXException Throws an exception if there is a serious configuration error.
     *
     */
    public static Document extractXMLFile(int fileNum) throws ParserConfigurationException, IOException, SAXException {
        ArrayList<File> xmlFiles = MockBillboardDatabase.setupDatabase();
        File xmlFile = xmlFiles.get(fileNum-1);

        // Use DocumentBuilderFactory to parse xml file in as a Document
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document xmlDoc = dBuilder.parse(xmlFile);

        return xmlDoc;
    }


    /**
     * Extracts data from an xml file for creating the billboard. It gets the content from the message, picture and
     * information tags as well as the custom colours for specified the billboard.
     * <p>
     * Note if the byte array is non-empty then picture will be 'present' and picture type will be 'byte'.
     * @param xmlDoc An xml file as a Document, from which to extract information from.
     * @param pictureData A byte[] array which stores the picture data attribute, instead of in the xml (only occurs
     * when xml file is incoming from server).
     * @return Returns a HashMap(String, String) which stores the background colour, message, message colour, picture,
     * picture type (url, data or byte), information, and information colour of the billboard, if there is no content
     * for one or more of these tags, the string is null.
     */
    public static HashMap<String, String> extractDataFromXML(Document xmlDoc, byte[] pictureData) {
        // Initiate an ArrayList to return
        HashMap<String, String> billboardData = new HashMap<>();

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
            if (!pictureElement.getAttribute("url").equals("")) {
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

        // Check if the pictureData byte array is non-empty, add present in Picture and byte in Picture Type
        if (!Arrays.equals(pictureData, new byte[0])) {
            billboardData.put("Picture", "present");
            billboardData.put("Picture Type", "byte");
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

        return billboardData;
    }

    /**
     * This function reads in a byte array which represents an image as a BufferedImage and returns this.
     * @param pictureData A byte[] array which stores the picture data attribute, instead of in the xml (only occurs,
     *      when xml file is incoming from server).
     * @return Returns a BufferedImage which is the picture stored in the byte array.
     */
    public static BufferedImage readByteArrayToPicture(byte[] pictureData) {
        BufferedImage pictureImage = null;

        // Read in the byte array as a buffered image for displaying.
        if (!Arrays.equals(pictureData, new byte[0])) {
            try {
                pictureImage = ImageIO.read(new ByteArrayInputStream(pictureData));
            } catch (IOException e) {
                // e.printStackTrace();
            }
        }

        return pictureImage;
    }


    /**
     * Reads in a picture from a file based on whether it is url or data and returns it in a form so that it can be
     * displayed properly on the billboard.
     * @param picture A String which is the picture to read in, this could be in the form of a url or data attribute
     * @param pictureType A String which is either url or data, so that we can decode the image
     * @return Returns the picture as a BufferedImage so it can be displayed on the billboard. Returns null if the
     * picture cannot be read in.
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
                // e.printStackTrace();
            }
        }
        else {
            // Try to decode the base-64 encoded representation of the image
            try {
                // Decode data attribute from url and convert to an image
                byte[] pictureByteArray = Base64.getDecoder().decode(picture.getBytes(StandardCharsets.UTF_8));
                pictureImage = ImageIO.read(new ByteArrayInputStream(pictureByteArray));

            } catch (Exception e) {
                // e.printStackTrace();
            }
        }

        return pictureImage;
    }


    /**
     * Scales the given image to be no larger than the provided maximum width and no larger than the maximum height.
     * @param image A BufferedImage which is the image that needs to be scaled.
     * @param maxImageWidth A double which represents the largest width the image can be displayed as on the screen.
     * @param maxImageHeight A double which represents the largest height the image can be displayed as on the screen.
     * @return Returns an Image which is the scaled version of the input image.
     */
    public static Image getScaledImage(BufferedImage image, double maxImageWidth, double maxImageHeight) {
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
     * @param message A String which is the message display on the screen.
     * @param maxStringHeight A double which is the maximum height the message can be displayed as
     * @return Returns an int which is the largest font size that the message can be displayed at
     */
    public int getMessageFontSize(String message, double maxStringHeight) {
        // Get the current font size and initialise the variable to return
        int currentFontSize = 12;
//        int currentFontSize = messageLabel.getFont().getSize();
        int fontSize = currentFontSize;

        // Horizontal spacing on borders of message
        double screenWidthBorder = 75;

        // Calculate what the width of the string would be based on the current font size
        FontMetrics fontMetrics = messageLabel.getFontMetrics(new Font(messageLabel.getFont().getName(), Font.BOLD,
                currentFontSize));
        double stringWidth = fontMetrics.stringWidth(message);
        double stringHeight = fontMetrics.getLeading() + fontMetrics.getMaxAscent() + fontMetrics.getMaxDescent();

        // While the current width of the string is less than the screen width minus some threshold for the border
        while (stringWidth < (SCREEN_WIDTH - screenWidthBorder*2) && stringHeight < (maxStringHeight - screenWidthBorder*2)) {
            // Increase the font size
            fontSize = fontSize + 1;

            // Recalculate the string width and height
            fontMetrics = messageLabel.getFontMetrics(new Font(messageLabel.getFont().getName(), Font.BOLD, fontSize));
            stringWidth = fontMetrics.stringWidth(message);
            stringHeight = fontMetrics.getLeading() + fontMetrics.getMaxAscent() + fontMetrics.getMaxDescent();

            // Break if the string width is too large
            if (stringWidth >= (SCREEN_WIDTH - screenWidthBorder*2)) {
                break;
            }

        }

        return fontSize;
    }


    /**
     * Calculates and sets the largest font size for the information string so that the text fills up no more than
     * 50% of the screen's height. The information font size is always smaller than the message font size.
     * @param maxStringHeight A double which is the maximum height the string can be.
     * @param message A boolean which is true if there is a message displayed on the screen and false if there isn't.
     */
    public void setInformationFontSize(double maxStringHeight, boolean message) {
        // Get the current font size and initialise the variable to return
        int fontSize = 12;
        informationLabel.setFont(new Font(informationLabel.getFont().getName(), Font.BOLD, fontSize));
//        int fontSize = informationLabel.getFont().getSize();

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
        while (currentHeight < maxStringHeight - threshold && fontSize < maxFontSize - 5) {
            fontSize = fontSize + 1;

            // Update font size
            informationLabel.setFont(new Font(informationLabel.getFont().getName(), Font.BOLD, fontSize));

            // Recalculate height of label
            currentHeight = informationLabel.getPreferredSize().height;
            currentWidth = informationLabel.getPreferredSize().width;

            // Update the size of the label
            informationLabel.setSize((int) currentWidth, (int) currentHeight);
        }
    }


    /**
     * Returns a string which formats the information string so that the text is wrapped, centered, and does not exceed
     * the maximum string width. This can then be used when setting the information label text.
     * @param information A String of the information to format for displaying.
     * @param maxStringWidth An int which is the maximum string width for information string.
     * @return Returns the formatted HTML version of information string.
     */
    public static String getInformationHTMLString(String information, int maxStringWidth) {
        String informationHTML = "<html><div style='width: " + maxStringWidth + "; text-align: center;'>"
                + information + "</div></html>";
        return informationHTML;
    }


    /**
     * Sets up the message so that it can be added to the billboard and returns the height of the message label for
     * formatting and spacing purposes.
     * @param message A String which stores the message to display.
     * @param maxStringHeight A double which is the maximum height the message can be dispayed at.
     * @return Returns a double which represents the height of the message.
     */
    public double setUpMessage(String message, double maxStringHeight) {
        // Set the text of the message label
        messageLabel.setText(message);

        // Choose the font size so the message fits in one line
        int fontSize = getMessageFontSize(message, maxStringHeight);
        Font messageFont = new Font(messageLabel.getFont().getName(), Font.BOLD, fontSize);
        messageLabel.setFont(messageFont);

        // Calculate the message height
        double messageHeight = messageLabel.getFontMetrics(messageFont).getHeight();

        // Return the message height
        return messageHeight;
    }


    /**
     * Sets up the picture so that it can be added to the billboard, given a maximum image width and height. Returns the
     * height of the picture for formatting and spacing purposes.
     * @param picture A String which stores the picture to display.
     * @param pictureType A String which specifies the type of picture, either as a "url", "data" or "byte".
     * @param pictureData A byte[] array which stores the picture data attribute if present.
     * @param maxImageWidth A double which represents the maximum width of the image.
     * @param maxImageHeight A double which represents the maximum height of the image.
     * @return Returns a double which represents the height of the picture.
     */
     public double setUpPicture(String picture, String pictureType, byte[] pictureData, double maxImageWidth, double maxImageHeight) {
         BufferedImage pictureImage = null;

         // Read in the picture and check it's format
         if (picture == "present" && pictureType == "byte") {
             pictureImage = readByteArrayToPicture(pictureData);
         } else {
             pictureImage = readPictureFromFile(picture, pictureType);
         }
         double pictureHeight = 0;

         // Has the picture been read in or not
         if (pictureImage == null) {
             // Display an error if the picture couldn't be read in
             pictureLabel.setText("Error: Couldn't read in image file.");
             pictureLabel.setFont(new Font(pictureLabel.getFont().getName(), Font.BOLD, 40));
             pictureLabel.setForeground(Color.BLACK);
             pictureLabel.setBackground(Color.BLACK);

         }
         else {
             // Scale the image we are displaying based on the maximum image dimensions
             Image scaledImage = getScaledImage(pictureImage, maxImageWidth, maxImageHeight);

             // Set the scaled image as the JLabel
             pictureIcon.setImage(scaledImage);
             pictureLabel.setIcon(pictureIcon);

             // Calculate the height of the picture
             pictureHeight = scaledImage.getHeight(null);
         }

         // Return the picture height
         return pictureHeight;
     }

    /**
     * Sets up the information so that it can be added to the billboard, given a maximum string height and width.
     * Returns the height of the label for formatting and spacing purposes.
     * @param information A String which stores the information to display.
     * @param maxStringWidth A double which represents the maximum width of the information label.
     * @param maxStringHeight A double which represents the maximum height of the information label.
     * @param message A boolean which is true if there is a message also being displayed and false if there isn't.
     * @return Returns a double which represents the height of the information.
     */
     public double setUpInformation(String information, double maxStringWidth, double maxStringHeight, boolean message) {
         // Use html styling
         String informationHTML = getInformationHTMLString(information, (int) maxStringWidth);

         // Set the text of the information label
         informationLabel.setText(informationHTML);

         // Choose and set the font size based on the maximum height of the information label
         setInformationFontSize(maxStringHeight, message);

         // Calculate the height of the information label
         double informationHeight = informationLabel.getSize().height;

         // Return the height of the information label
         return informationHeight;
     }


    /**
     * Sets the grid bag constraints for an element which will be added to the billboard.
     * @param gridx An int which is the x position of the element.
     * @param gridy An int which is the y position of the element.
     * @param gridHeight An int which is the grid height of the element.
     * @param topPadding An int which is the padding to add on the top of the element.
     * @param bottomPadding An int which is the padding to add on the bottom of the element.
     * @return Returns a GridBagConstraints which has all of the parameters as constraints.
     */
     public static GridBagConstraints setGridBagConstraints(int gridx, int gridy, int gridHeight, int topPadding, int bottomPadding) {
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
     * Displays a billboard which has only a message. The is displayed in the centre of the screen with the maximum
     * possible font size.
     * @param message A String which stores the message to display.
     */
    public void messageOnlyBillboard(String message) {
        // Set up the message and add the message label to the central panel in the JFrame
        setUpMessage(message, SCREEN_HEIGHT);
        mainPanel.add(messageLabel);
    }


    /**
     * Displays a billboard which has only a picture. The picture takes up no more than 50% of the screen height and no
     * more than 50% of the screen width. The picture is displayed in the centre of the screen.
     * @param picture A String which stores the picture to display, in the form of a url or data attribute.
     * @param pictureType A String which specifies the type of picture, either as a "url", "data" or "byte".
     * @param pictureData A byte[] array which stores the picture data attribute if present.
     */
    public void pictureOnlyBillboard(String picture, String pictureType, byte[] pictureData) {
        // Define the maximum image dimensions based on the screen dimensions
        double maxImageWidth = SCREEN_WIDTH/2;
        double maxImageHeight = SCREEN_HEIGHT/2;

        // Set up the picture and add it to the main panel
        setUpPicture(picture, pictureType, pictureData, maxImageWidth, maxImageHeight);
        mainPanel.add(pictureLabel);
    }


    /**
     * Displays a billboard which only has information. The information takes up no more than 75% of the screen's width
     * and 50% of the screen's height. The information is displayed in the centre of the screen.
     * @param information A String which stores the information to display.
     */
    public void informationOnlyBillboard(String information) {
        // Calculate the maximum string width and height for the information
        double maxStringWidth = SCREEN_WIDTH*0.75;
        double maxStringHeight = SCREEN_HEIGHT*0.5;

        // Set up the information label and add it to the main panel
        setUpInformation(information, maxStringWidth, maxStringHeight, false);
        mainPanel.add(informationLabel);
    }


    /**
     * Displays a billboard which has a message and a picture. The picture is displayed in the center of the bottom 2/3
     * of the screen, and the message is displayed in the center of the remaining top part of the screen.
     * @param message A String which stores the message to display.
     * @param picture A String which stores the picture to display, in the form of a url or data attribute.
     * @param pictureType A String which specifies the type of picture, either as a "url", "data" or "byte".
     * @param pictureData A byte[] array which stores the picture data attribute if present.
     */
    public void messagePictureBillboard(String message, String picture, String pictureType, byte[] pictureData) {
        // Define the maximum image dimensions based on the screen dimensions
        double maxImageWidth = SCREEN_WIDTH/2;
        double maxImageHeight = SCREEN_HEIGHT/2;

        // Set up the picture elements and get the height
        double messageHeight = setUpMessage(message, SCREEN_HEIGHT/3);
        double pictureHeight = setUpPicture(picture, pictureType, pictureData, maxImageWidth, maxImageHeight);

        // Initialise variables
        int topPicturePadding = 0;
        int bottomPicturePadding = 0;
        int messagePadding = 0;

        // Has the picture been read in properly or not
        if (pictureHeight == 0) {
            // Calculate the height of the "picture"
            pictureHeight = pictureLabel.getFontMetrics(pictureLabel.getFont()).getHeight();

            // Calculate vertical padding for image. It should fit in the center of bottom 2/3 of screen.
            topPicturePadding = (int) ((maxImageHeight - pictureHeight) / 2);
            bottomPicturePadding = (int) (((2*(SCREEN_HEIGHT /3)) - pictureHeight) / 2);


            // Calculate vertical padding for message. It should fit in the centre of the remaining space at the top.
            messagePadding = (int) (((SCREEN_HEIGHT/3) - messageHeight ) / 2);
        }
        else {
            // Calculate vertical padding for image. It should fit in the center of bottom 2/3 of screen.
            bottomPicturePadding = (int) (((2*(SCREEN_HEIGHT/3)) - pictureHeight) / 2);

            // Calculate vertical padding for message. It should fit in the centre of the remaining space at the top.
            messagePadding = (int) (((SCREEN_HEIGHT/3) + bottomPicturePadding - messageHeight ) / 2);
        }

        // Create grid bag constraints for the message and picture
        GridBagConstraints messageConstraints = setGridBagConstraints(0, 0, 1, messagePadding,
                messagePadding);
        GridBagConstraints pictureConstraints = setGridBagConstraints(0, 1, 2, topPicturePadding,
                bottomPicturePadding);

        // Add the message and image to the billboard
        mainPanel.add(messageLabel, messageConstraints);
        mainPanel.add(pictureLabel, pictureConstraints);
    }


    /**
     * Displays a billboard which has a message and information. The message is displayed in the center of the top half
     * of the screen and the information is displayed in the center of the bottom half of the screen. The message font
     * size is always larger than the information font size.
     * @param message A String which stores the message to display.
     * @param information A String which stores the information to display
     */
    public void messageInformationBillboard(String message, String information) {
        // Set up the message label and get it's height
        double maxMessageHeight = SCREEN_HEIGHT/2;
        double messageHeight = setUpMessage(message, maxMessageHeight);

        // Calculate the maximum string width and height for the information
        double maxStringWidth = SCREEN_WIDTH*0.75;
        double maxStringHeight = SCREEN_HEIGHT*0.25;

        // Set up the message label and get it's height
        double informationHeight = setUpInformation(information, maxStringWidth, maxStringHeight, true);

        // Calculate vertical padding for the message. It should be in the centre of the top half of the screen.
        int messagePadding = (int) (((SCREEN_HEIGHT/2)  - messageHeight) / 2);

        // Calculate vertical padding for the information. It should be in the centre of the top half of the screen.
        int informationPadding = (int) (((SCREEN_HEIGHT/2)  - informationHeight) / 2);

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
     * Displays a billboard which has a picture and information. The picture is displayed in the center of the top 2/3
     * of the screen, and the information is displayed in the center of the remaining bottom part of the screen.
     * @param picture A String which stores the picture to display, in the form of a url or data attribute.
     * @param pictureType A String which specifies the type of picture, either as a "url", "data" or "byte".
     * @param pictureData A byte[] array which stores the picture data attribute if present.
     * @param information A String which stores the information to display.
     */
    public void pictureInformationBillboard(String picture, String pictureType, byte[] pictureData, String information) {
        // Define the maximum image dimensions based on the screen dimensions
        double maxImageWidth = SCREEN_WIDTH /2;
        double maxImageHeight = SCREEN_HEIGHT /2;

        // Set up the picture and get it's height
        double pictureHeight = setUpPicture(picture, pictureType, pictureData, maxImageWidth, maxImageHeight);

        // Initialise variables
        double maxStringWidth;
        double maxStringHeight;
        int informationPadding;

        // Calculate vertical padding for picture. It should be in the centre of the top 2/3 of the screen.
        int topPicturePadding = (int) ( ((2*(SCREEN_HEIGHT /3)) - pictureHeight) / 2 );
        int bottomPicturePadding = 0;

        // If the picture couldn't be read in properly - still display the contents nicely
        if (pictureHeight == 0) {
            // Calculate the height of the "picture"
            pictureHeight = pictureLabel.getFontMetrics(pictureLabel.getFont()).getHeight();

            // Calculate the padding below the label
            bottomPicturePadding = (int) ((maxImageHeight - pictureHeight) / 2);

            // Calculate the maximum string width and height for the information
            maxStringWidth = SCREEN_WIDTH *0.75;
            maxStringHeight = (SCREEN_HEIGHT - pictureHeight - topPicturePadding - bottomPicturePadding) / 2;

            // Set up the information and get it's height
            double informationHeight = setUpInformation(information, maxStringWidth, maxStringHeight, false);

            // Calculate vertical padding for information. It should be in the centre of remaining space at the bottom.
            informationPadding = (int) ( ((SCREEN_HEIGHT /3) - informationHeight)/2 );
        }
        else {
            // Calculate the maximum string width and height for the information
            maxStringWidth = SCREEN_WIDTH *0.75;
            maxStringHeight = (SCREEN_HEIGHT - pictureHeight - topPicturePadding) / 2;

            // Set up the information and get it's height
            double informationHeight = setUpInformation(information, maxStringWidth, maxStringHeight, false);

            // Calculate vertical padding for information. It should be in the centre of remaining space at the bottom.
            informationPadding = (int) ( ((SCREEN_HEIGHT /3) + topPicturePadding - informationHeight)/2 );
        }

        // Create grid bag constraints for the picture and information
        GridBagConstraints pictureConstraints = setGridBagConstraints(0, 0, 2, topPicturePadding,
                bottomPicturePadding);
        GridBagConstraints informationConstraints = setGridBagConstraints(0, 2, 1,
                informationPadding, informationPadding);

        // Add the message and image to the billboard
        mainPanel.add(informationLabel, informationConstraints);
        mainPanel.add(pictureLabel, pictureConstraints);
    }


    /**
     * Displays a billboard which has all the features: a message, picture and information. The picture is displayed in
     * the centre of the screen, the message is displayed in the remaining top part of the screen, and the information
     * is displayed in the remaining bottom part of the screen.
     * @param message A String which stores the message to display.
     * @param picture A String which stores the picture to display, in the form of a url or data attribute.
     * @param pictureType A String which specifies the type of picture, either as a "url", "data" or "byte".
     * @param pictureData A byte[] array which stores the picture data attribute if present.
     * @param information A String which stores the information to display.
     */
    public void allFeaturesBillboard (String message, String picture, String pictureType, byte[] pictureData, String information) {
        // Define the maximum image dimensions based on the screen dimensions
        double maxImageWidth = SCREEN_WIDTH/3;
        double maxImageHeight = SCREEN_HEIGHT/3;

        // Set up the picture and get the height
        double messageHeight = setUpMessage(message, SCREEN_HEIGHT/3);
        double pictureHeight = setUpPicture(picture, pictureType, pictureData, maxImageWidth, maxImageHeight);

        // Initialise variables
        int messagePadding = 0;
        int picturePadding = 0;
        int informationPadding = 0;

        // If the picture couldn't be read in properly - still display the rest of the contents properly
        if (pictureHeight == 0) {
            // Calculate the height of the "picture"
            pictureHeight = pictureLabel.getFontMetrics(pictureLabel.getFont()).getHeight();

            // Calculate picture padding - make sure the label is centered
            picturePadding = (int) ((maxImageHeight - pictureHeight) / 2);

            // Calculate vertical padding of message. It should be centred in the top part of the screen.
            messagePadding = (int) (((SCREEN_HEIGHT/2) - (maxImageHeight/2) - messageHeight) / 2);

            // Calculate the maximum string width and height for the information
            double maxInformationWidth = SCREEN_WIDTH*0.75;
            double maxInformationHeight = ((SCREEN_HEIGHT/2) - (maxImageHeight/2)) / 2;

            // Set up the information and get it's height
            double informationHeight = setUpInformation(information, maxInformationWidth, maxInformationHeight,
                    true);

            // Calculate vertical padding of information. It should be centred in the bottom part of the screen.
            informationPadding = (int) (((SCREEN_HEIGHT/2) - (maxImageHeight/2) - informationHeight) / 2);
        }
        else {
            // Calculate vertical padding of message. It should be centred in the top part of the screen.
            messagePadding = (int) (((SCREEN_HEIGHT/2) - (pictureHeight/2) - messageHeight) / 2);

            // Calculate the maximum string width and height for the information
            double maxInformationWidth = SCREEN_WIDTH*0.75;
            double maxInformationHeight = ((SCREEN_HEIGHT/2) - (pictureHeight/2)) / 2;

            // Set up the information and get it's height
            double informationHeight = setUpInformation(information, maxInformationWidth, maxInformationHeight,
                    true);

            // Calculate vertical padding of information. It should be centred in the bottom part of the screen.
            informationPadding = (int) (((SCREEN_HEIGHT/2) - (pictureHeight/2) - informationHeight) / 2);
        }

        // Create grid bag constraints for the elements
        GridBagConstraints messageConstraints = setGridBagConstraints(0, 0, 1, messagePadding,
                messagePadding);
        GridBagConstraints pictureConstraints = setGridBagConstraints(0, 1, 1, picturePadding,
                picturePadding);
        GridBagConstraints informationConstraints = setGridBagConstraints(0, 2, 1,
                informationPadding, informationPadding);

        // Add the message and image to the billboard
        mainPanel.add(messageLabel, messageConstraints);
        mainPanel.add(pictureLabel, pictureConstraints);
        mainPanel.add(informationLabel, informationConstraints);
    }


    /**
     * Formats the display window using depending on whether there is a message, picture, information or any combination
     * of the three.
     * @param billboardData A HashMap(String, String) which stores the background colour, message, message colour,
     * picture, picture type (data or url), information, and information colour of the billboard, if there is no
     * content for one or more of these tags, the string is null.
     * @param pictureData A byte[] array which stores the picture data attribute if present.
     */
    public void formatBillboard(HashMap<String, String> billboardData, byte[] pictureData) {
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
            allFeaturesBillboard(message, picture, pictureType, pictureData, information);
        }
        else if (message != null && picture != null) {
            messagePictureBillboard(message, picture, pictureType, pictureData);
        }
        else if (message != null && information != null) {
            messageInformationBillboard(message, information);
        }
        else if (picture != null && information != null) {
            pictureInformationBillboard(picture, pictureType, pictureData, information);
        }
        else if (message != null) {
            messageOnlyBillboard(message);
        }
        else if (picture != null) {
            pictureOnlyBillboard(picture, pictureType, pictureData);
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

        // Repaint the billboard to show the correct layout
        mainPanel.revalidate();
        mainPanel.repaint();
    }


    /**
     * Sets up the basic layout of the billboard.
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
     * Exits the window if the user presses the escape key.
     */
    public void listenEscapeKey() {
        KeyListener escListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent event) {
            if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
                System.out.println("Exited Viewer by pressing the escape key.");
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
                System.out.println("Exited Viewer by clicking the mouse.");
                System.exit(0);
            }
        };


        addMouseListener(clickListener);
    }


    /**
     * Shows the GUI to display the Viewer.
     */
    public void showViewer() {
        // Displaying the window to be completely full screen and visible
        setExtendedState(Frame.MAXIMIZED_BOTH);
        setVisible(true);
    }


    /**
     * Displays just a special message on the billboard e.g. an error message.
     * @param message A String which represents the message to display on the screen.
     */
    public void displaySpecialMessage(String message) {
        // Remove all previous elements and set up billboard again
        mainPanel.removeAll();
        setupBillboard();

        // Set up the message to display and add it to the main panel
        setUpMessage(message, SCREEN_HEIGHT);
        mainPanel.add(messageLabel);
        messageLabel.setForeground(Color.BLACK);
        messageLabel.setBackground(Color.BLACK);

        // Repaint the billboard to show the correct layout
        mainPanel.revalidate();
        mainPanel.repaint();

        listenEscapeKey();
        listenMouseClick();
        showViewer();
    }


    /**
     * Displays the billboard on the viewer.
     * @param billboardXML A String which stores the xml file to be displayed.
     * @param pictureData A byte[] array which stores the picture data attribute.
     */
    public void displayBillboard(String billboardXML, byte[] pictureData) {
        // Remove all previous elements and set up billboard again
        mainPanel.removeAll();
        setupBillboard();

        // Extract the billboard data using the server's response
        try {
            // Parse in the xml file and get the formatting of the billboard
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document xmlDocServer = dBuilder.parse(new InputSource(new StringReader(billboardXML)));
            HashMap<String, String> billboardDataServer = extractDataFromXML(xmlDocServer, pictureData);

            // Display the billboard
            formatBillboard(billboardDataServer, pictureData);

            // Testing from the provided xml files
//            Document xmlDoc = extractXMLFile(17);
//            HashMap<String, String> billboardData = extractDataFromXML(xmlDoc, new byte[0]);
//            formatBillboard(billboardData, new byte[0]);

        } catch (ParserConfigurationException | IOException | SAXException e) {
            // Display an error is the xml File couldn't be parsed in
            System.out.println("Couldn't read in xml file...");
            displaySpecialMessage("Error: Couldn't read in xml file. Reconnecting to server...");
        }

        listenEscapeKey();
        listenMouseClick();
        showViewer();
    }



}