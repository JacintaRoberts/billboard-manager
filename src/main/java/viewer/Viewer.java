package viewer;

import helpers.Helpers;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;


public class Viewer {

    // Create and set up the JFrame to show the viewer
    private static JFrame viewer = new JFrame ("Billboard Viewer");


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
     * @return billboardTags - an ArrayList which stores the background colour, message, message colour, picture,
     *      information, and information colour of the billboard, if there is no content for one or more of these tags,
     *      it stores an empty string
     * TODO: Change from an ArrayList to a HashMap?? - this will need to change below as well
     */
    public static ArrayList<String> extractDataFromXML(File xmlFile) {
        // Initiate an ArrayList to return
        ArrayList<String> billboardData = new ArrayList<>();

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
            String backgroundColour = billboardElement.getAttribute("background");
            billboardData.add(0, backgroundColour);

            // If the message tag exists, add the message and colour, else add an empty string
            if (billboardElement.getElementsByTagName("message").getLength() == 1) {
                Node messageNode = billboardElement.getElementsByTagName("message").item(0);
                Element messageElement = (Element) messageNode;
                String message = messageElement.getTextContent();
                String messageColour = messageElement.getAttribute("colour");
                billboardData.add(1, message);
                billboardData.add(2, messageColour);
            } else {
                billboardData.add(1, "");
                billboardData.add(2, "");
            }

            // If the picture tag exists, add the url or data attribute, else add an empty string
            if (billboardElement.getElementsByTagName("picture").getLength() == 1) {
                Node pictureNode = billboardElement.getElementsByTagName("picture").item(0);
                Element pictureElement = (Element) pictureNode;
                String picture = "";
                if (pictureElement.getAttribute("url") != "") {
                    picture = pictureElement.getAttribute("url");
                } else {
                    picture = pictureElement.getAttribute("data");
                }
                billboardData.add(3, picture);
            } else {
                billboardData.add(3, "");
            }

            // If the information tag exists, add the information and colour, else add an empty string
            if (billboardElement.getElementsByTagName("information").getLength() == 1) {
                Node informationNode = billboardElement.getElementsByTagName("information").item(0);
                Element informationElement = (Element) informationNode;
                String information = informationElement.getTextContent();
                String informationColour = informationElement.getAttribute("colour");
                billboardData.add(4, information);
                billboardData.add(5, informationColour);
            } else {
                billboardData.add(4, "");
                billboardData.add(5, "");
            }

            // Print the tags to the console - for testing
//            System.out.println("Background Colour: " + billboardData.get(0));
//            System.out.println("Message: " + billboardData.get(1));
//            System.out.println("Message Colour: " + billboardData.get(2));
//            System.out.println("Picture: " + billboardData.get(3));
//            System.out.println("Information: " + billboardData.get(4));
//            System.out.println("Information Colour: " + billboardData.get(5));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return billboardData;
    }


    /**
     * If there are no billboards to display, display a message for the user.
     */
    public static void noBillboardToDisplay() {
        // Create a label to display a message and format it
        JLabel messageLabel = new JLabel("There are no billboards to display right now.", SwingConstants.CENTER);
        messageLabel.setFont(new Font(messageLabel.getFont().getName(), Font.PLAIN, 50));
        viewer.add(messageLabel);
    }


    /**
     * Displays a billboard which has only a message
     * TODO: Set the colour attribute to the JLabel
     */
    public static void MessageOnlyBillboard(ArrayList<String> billboardData) {
        // Extract the message from the billboard data
        String message = billboardData.get(1);
        String messageColour = billboardData.get(2);

        // Create a label to display a message and format it
        JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
        messageLabel.setFont(new Font(messageLabel.getFont().getName(), Font.PLAIN, 50));
        if (messageColour != "") {
            messageLabel.setForeground(Color.decode(messageColour));
            messageLabel.setBackground(Color.decode(messageColour));
        }
        viewer.add(messageLabel);
    }


    /**
     * Displays a billboard which has only a picture
     * TODO
     */
    public static void PictureOnlyBillboard(ArrayList<String> billboardData) {

    }


    /**
     * Displays a billboard which has only a information
     * TODO
     */
    public static void InformationOnlyBillboard(ArrayList<String> billboardData) {

    }


    /**
     * Displays a billboard which has a message and picture
     * TODO
     */
    public static void MessagePictureBillboard(ArrayList<String> billboardData) {

    }


    /**
     * Displays a billboard which has a message and information
     * TODO
     */
    public static void MessageInformationBillboard(ArrayList<String> billboardData) {

    }

    /**
     * Displays a billboard which has a picture and information
     * TODO
     */
    public static void PictureInformationBillboard(ArrayList<String> billboardData) {

    }


    /**
     * Displays a billboard which has all the features: a message, picture and information
     * TODO
     */
    public static void AllFeaturesBillboard(ArrayList<String> billboardData) {

    }


    /**
     * Formats the display window using the mock database depending on whether there is a message,
     * picture, information or a combination.
     * TODO: Insert a switch statement which chooses between different cases - calls on different functions
     *      i.e. there is a function for each case displaying the billboard properly.
     * TODO: This function will deal with the background colour as this might be relevant to all billboards.
     */
    public static void formatBillboard(ArrayList<String> billboardData) {


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

        viewer.addKeyListener(escListener);
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

        viewer.addMouseListener(clickListener);
    }


    /**
     * Show the GUI for displaying the billboard.
     */
    public static void showViewer() {

        // Displaying the window to be completely full screen
        viewer.setExtendedState(Frame.MAXIMIZED_BOTH);
        viewer.setUndecorated(true);

        viewer.setVisible(true);
    }


    /**
     * Displays the billboards
     * TODO: Update this to actually display a billboard
     * TODO: Get this talk to the server to see the schedule
     */
    public static void displayBillboard() {
        File fileToDisplay = extractXMLFile(11);
        ArrayList<String> billboardTags = extractDataFromXML(fileToDisplay);
        MessageOnlyBillboard(billboardTags);
        // formatBillboard(billboardTags);
        // noBillboardToDisplay();
        listenEscapeKey();
        listenMouseClick();
        showViewer();
    }


    public static void main(String[] args ) {
        // Read port number and ip address from network.props
        final String networkPropsFilePath = "src\\main\\resources\\network.props";
        final int port = Helpers.getPort(networkPropsFilePath);
        final String ip = Helpers.getIp(networkPropsFilePath);

        // Connect to server
        try {
            Socket socket = new Socket(ip, port);
            // Write to the server
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(42); //0-255
            socket.close();
        } catch (IOException e) {
            //TODO: Billboard show something alternate if cannot connect to server
            System.err.println("Exception caught: " + e);
        }

        //displayBillboard();

    }

}
