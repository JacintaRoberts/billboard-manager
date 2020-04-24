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


public class Viewer {

    // Set up the JFrame to show the viewer
    private static JFrame viewerFrame = new JFrame();

    // Set up the panels on the viewer
    private static JPanel northBorderPanel = new JPanel();
    private static JPanel eastBorderPanel = new JPanel();
    private static JPanel southBorderPanel = new JPanel();
    private static JPanel westBorderPanel = new JPanel();
    private static JPanel centralPanel = new JPanel();

    // Set up the labels, image icons etc. to display the different parts of the billboard
    private static JLabel messageLabel = new JLabel();
    private static ImageIcon pictureIcon = new ImageIcon();
    private static JLabel pictureLabel = new JLabel();
    private static JTextArea informationTextArea = new JTextArea();

    // Dimensions of screen the viewer will display on
    private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static int screenHeight = screenSize.height;
    private static int screenWidth = screenSize.width;


    /**
     * Extracts the xml file that we want to display.
     * TODO: This method should be the only one which needs to be changed when the actual database is connected
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
    public static void setupBillboard() {
        // Create a border layout
        viewerFrame.setLayout(new BorderLayout());

        // Set each of the border panels to a location in the border layout
        viewerFrame.add(northBorderPanel, BorderLayout.NORTH);
        viewerFrame.add(eastBorderPanel, BorderLayout.EAST);
        viewerFrame.add(southBorderPanel, BorderLayout.SOUTH);
        viewerFrame.add(westBorderPanel, BorderLayout.WEST);

        northBorderPanel.setBackground(Color.LIGHT_GRAY);
        eastBorderPanel.setBackground(Color.LIGHT_GRAY);
        southBorderPanel.setBackground(Color.LIGHT_GRAY);
        westBorderPanel.setBackground(Color.LIGHT_GRAY);

        // Add the central panel to the center of the billboard
        viewerFrame.add(centralPanel, BorderLayout.CENTER);

        // Create a grid bag layout
        centralPanel.setLayout(new GridBagLayout());
    }


    /**
     * A convenience method to add a component to given grid bag layout locations. Taken from Practical 6.
     * @param c the component to add
     * @param x the x grid position
     * @param y the y grid position
     * @param w the grid width of the component
     * @param h the grid height of the component
     */
    private static void addToPanel(JPanel jp, Component c, int x, int y, int w, int h) {
        // Add default constraints to grid bag layout
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 100;
        constraints.weighty = 100;

        // Add constraints that were in the input
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.gridwidth = w;
        constraints.gridheight = h;
        jp.add(c, constraints);
    }


    /**
     * Displays a billboard which has only a message
     * @param message - a string which stores the message to display
     * TODO: Format so that the string will fit the width of the screen on one line, and so that there are margins
     *       between the edge of the screen and the text. Make sure the minimum message text is larger than the maximum
     *       information text.
     */
    public static void messageOnlyBillboard(String message) {
        // Set the text and font of the message label
        messageLabel.setText(message);
        messageLabel.setFont(new Font(messageLabel.getFont().getName(), Font.PLAIN, 50));

        // Add the message label to the central panel in the JFrame
        addToPanel(centralPanel, messageLabel, 0, 0, 1, 1);
    }


    /**
     * Displays a billboard which has only a picture
     * @param picture - a string which is the picture to display, this could be in the form of a url or data attribute
     * @param pictureType - a string which is either url or data, so that we can decode the iamge
     * TODO: Fit image to screen but keep the right aspect ratio
     */
    public static void pictureOnlyBillboard(String picture, String pictureType) {
        // Decide if it's a url or data attribute
        if (pictureType.equals("url")) {
            // Try to extract picture from url, if this fails catch the exception
            try {
                // Extract picture from URL
                URL pictureURL = new URL(picture);
                Image pictureImage = ImageIO.read(pictureURL);

                // Add Image to the central panel in the JFrame
                pictureIcon.setImage(pictureImage);
                pictureLabel.setIcon(pictureIcon);
                addToPanel(centralPanel, pictureLabel, 0, 0, 1, 1);

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

                // Add Image to the central panel in the JFrame
                pictureIcon.setImage(pictureImage);
                pictureLabel.setIcon(pictureIcon);
                addToPanel(centralPanel, pictureLabel, 0, 0, 1, 1);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }


    /**
     * Displays a billboard which has only a information
     * @param information - a string which stores the information to display
     * TODO: Format so that the string will automatically wrap the text, and so that there are margins between the
     *       edge of the screen and the text. Make sure the minimum message text is larger than the maximum
     *       information text.
     */
    public static void informationOnlyBillboard(String information) {
        // Set the text and font of the information label
        informationTextArea.setEditable(false);
        informationTextArea.setLineWrap(true);
        informationTextArea.setText(information);
        informationTextArea.setFont(new Font(informationTextArea.getFont().getName(), Font.PLAIN, 30));

        // Add information label to the central panel in the JFrame
        addToPanel(centralPanel, informationTextArea, 0, 0, 20, 1);
    }


    /**
     * Displays a billboard which has a message and picture
     * TODO
     */
    public static void messagePictureBillboard(HashMap<String, String> billboardData) {

    }


    /**
     * Displays a billboard which has a message and information
     * TODO
     */
    public static void messageInformationBillboard(HashMap<String, String> billboardData) {

    }

    /**
     * Displays a billboard which has a picture and information
     * TODO
     */
    public static void pictureInformationBillboard(HashMap<String, String> billboardData) {

    }


    /**
     * Displays a billboard which has all the features: a message, picture and information
     * TODO
     */
    public static void allFeaturesBillboard(HashMap<String, String> billboardData) {

    }


    /**
     * Formats the display window using the mock database depending on whether there is a message,
     * picture, information or a combination.
     * TODO: Check the if statements to make sure they have the correct
     * TODO: Potentially move message colour and information colour into this function?
     */
    public static void formatBillboard(HashMap<String, String> billboardData) {
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
            centralPanel.setBackground(Color.decode(backgroundColour));
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


        // Check if there's a specific message colour
        if (informationColour != null) {
            informationTextArea.setForeground(Color.decode(informationColour));
            informationTextArea.setBackground(Color.decode(informationColour));
        }

        // Check if there's a specific message colour
        if (messageColour != null) {
            messageLabel.setForeground(Color.decode(messageColour));
            messageLabel.setBackground(Color.decode(messageColour));
        }

    }

    /**
     * If there are no billboards to display, display a message for the user.
     */
    public static void noBillboardToDisplay() {
        // Create a label to display a message and format it
        messageLabel.setText("There are no billboards to display right now.");
        messageLabel.setFont(new Font(messageLabel.getFont().getName(), Font.PLAIN, 50));

        // Add the message label to the JFrame
        addToPanel(centralPanel, messageLabel, 0, 0, 1, 1);
    }


    /**
     * Exits the window if the user presses the escape key.
     */
    public static void listenEscapeKey() {
        KeyListener escListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    System.exit(0);
                }
            }
        };

        viewerFrame.addKeyListener(escListener);
    }


    /**
     * Exits the window if the user clicks anywhere on the window.
     */
    public static void listenMouseClick() {
        MouseListener clickListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                System.exit(0);
            }
        };

        viewerFrame.addMouseListener(clickListener);
    }


    /**
     * Show the GUI for displaying the billboard.
     */
    public static void showViewer() {
        // Displaying the window to be completely full screen
        viewerFrame.setExtendedState(Frame.MAXIMIZED_BOTH);
        viewerFrame.setUndecorated(true);

        viewerFrame.setVisible(true);
    }


    /**
     * Displays the billboards
     */
    public static void displayBillboard() {
        setupBillboard();
        File fileToDisplay = extractXMLFile(8);
        HashMap<String, String> billboardData = extractDataFromXML(fileToDisplay);
        formatBillboard(billboardData);
        // noBillboardToDisplay();

        listenEscapeKey();
        listenMouseClick();
        showViewer();
    }

    /**
     * Displays an error if the viewer cannot connect to the server
     */
    public static void displayError() {
        // Add an error message label to the central panel of the JFrame
        messageLabel.setText("Error: Cannot connect to server. Trying again now...");
        messageLabel.setFont(new Font(messageLabel.getFont().getName(), Font.PLAIN, 50));
        addToPanel(centralPanel, messageLabel, 0, 0, 1, 1);

        listenEscapeKey();
        listenMouseClick();
        showViewer();
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
            // displayError();
        }

        displayBillboard();
    }

}
