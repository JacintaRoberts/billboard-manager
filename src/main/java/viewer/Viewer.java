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
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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
     * Displays a billboard which has only a message
     * @param message - a string which stores the message to display
     * TODO: The message should be displayed almost as large as possible, within the constraints that the text cannot
     *       be broken across multiple lines and it must all fit on the screen.
     */
    public void messageOnlyBillboard(String message) {
        // Set the text and font of the message label
        messageLabel.setText(message);
        messageLabel.setFont(new Font(messageLabel.getFont().getName(), Font.PLAIN, 50));

        // Add the message label to the central panel in the JFrame
        mainPanel.add(messageLabel);
    }


    /**
     * Adds the picture to the central panel - for pictureOnlyBillboard at this stage
     * @param image - the image that needs to be added to the panel
     * TODO: The image should be scaled up to half the width and height of the screen and displayed in the centre.
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
        double ratio = 1;

        // Decide how to scale the image (or if we need to at all)
        if (originalWidth > maxImageWidth || originalHeight > maxImageHeight) {
            if (originalWidth < maxImageWidth && originalHeight > maxImageHeight) {
                newHeight = maxImageHeight;
                ratio = newHeight/originalHeight;
                newWidth = ratio*originalWidth;
            }

        }

        // Testing
        System.out.println("Maximum Image Dimensions: " + maxImageWidth + " x " + maxImageHeight);
        System.out.println("Original Dimensions: " + originalWidth + " x " + originalHeight);
        System.out.println("Ratio: " + ratio);
        System.out.println("New Dimensions: " + newWidth + " x " + newHeight);

        // Scale the image
        Image scaledImage = image.getScaledInstance( (int) newWidth, (int) newHeight, Image.SCALE_DEFAULT);

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
     * Displays a billboard which has only a information
     * @param information - a string which stores the information to display
     * TODO: The text should be displayed in the centre, with word wrapping and font size chosen so that the text fills
     *       up no more than 75% of the screen's width and 50% of the screen's height.
     */
    public void informationOnlyBillboard(String information) {
        // Set the text and font of the information label
        informationLabel.setText("<html><div style='text-align: center;'>" + information + "</div></html>");
        informationLabel.setFont(new Font(informationLabel.getFont().getName(), Font.PLAIN, 30));

        // Add information label to the central panel in the JFrame
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
//        constraints.gridheight = 1;
//        constraints.gridwidth = 1;
//        constraints.gridx = 0;
//        constraints.gridy = 0;
        constraints.ipadx = 800;

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

        // If there is a background colour, format the JFrame so that this is visible
        if (backgroundColour != null) {
            mainPanel.setBackground(Color.decode(backgroundColour));
        }

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

        // Check if there's a specific message text colour
        if (messageColour != null) {
            messageLabel.setForeground(Color.decode(messageColour));
            messageLabel.setBackground(Color.decode(messageColour));
        }

        // Check if there's a specific information text colour
        if (informationColour != null) {
            informationLabel.setForeground(Color.decode(informationColour));
            informationLabel.setBackground(Color.decode(informationColour));
        }

    }

    /**
     * If there are no billboards to display, display a message for the user.
     * TODO: Formatting exactly the same as messageOnlyBillboard
     */
    public void noBillboardToDisplay() {
        // Create a label to display a message and format it
        messageLabel.setText("There are no billboards to display right now.");
        messageLabel.setFont(new Font(messageLabel.getFont().getName(), Font.PLAIN, 50));

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

        File fileToDisplay = extractXMLFile(13);
        HashMap<String, String> billboardData = extractDataFromXML(fileToDisplay);
        formatBillboard(billboardData);
//        noBillboardToDisplay();

        listenEscapeKey();
        listenMouseClick();
        showViewer();
    }

    /**
     * Displays an error if the viewer cannot connect to the server
     * TODO: Formatting exactly the same as messageOnlyBillboard
     */
    public void displayError() {
        setupBillboard();

        // Add an error message label to the central panel of the JFrame
        messageLabel.setText("Error: Cannot connect to server. Trying again now...");
        messageLabel.setFont(new Font(messageLabel.getFont().getName(), Font.PLAIN, 50));
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
        // Read port number and ip address from network.props
        final String networkPropsFilePath = "src\\main\\resources\\network.props";
        final int port = Helpers.getPort(networkPropsFilePath);
        final String ip = Helpers.getIp(networkPropsFilePath);

        try {
            // Connect to server
            Socket socket = new Socket(ip, port);

            // Write to the server
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();

            ObjectOutputStream oos = new ObjectOutputStream(outputStream);
            ObjectInputStream ois = new ObjectInputStream(inputStream);

            int[] ar = {1, 2, 3};
            oos.writeObject("Hello server");
            oos.writeObject(ar);

            // oos.writeUTF("Hello server");
            // oos.writeUTF("1");
            // oos.flush();
            // System.out.println("Message from server: " + ois.readUTF());
            // oos.writeUTF("2");
            // oos.writeUTF("3");
            oos.flush();

            ois.close();
            oos.close();

            // System.out.println("Connected to server");
            // outputStream.write(42);
            // System.out.println("Sent byte");

            // System.out.print("Received byte: " + inputStream.read());

            // BufferedOutputStream bos = new BufferedOutputStream(outputStream);
            // bos.write(42); //0-255
            // bos.flush();
            // bos.close();

            socket.close();
        } catch (IOException e) { // Could not connect to server
            // TODO: Billboard show something alternate if cannot connect to server
            System.err.println("Exception caught: " + e);
        }

        SwingUtilities.invokeLater(new Viewer("Billboard Viewer"));
    }

}
