package viewer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

/*
This class will act as a mock billboards database so that the Viewer class can be implemented without the
actual database.
 */

public class MockBillboardDatabase {

    // Initialise ArrayLists to story the message, picture and information tags from the XML
    private static ArrayList<String> billboardMessages = new ArrayList<>();
    private static ArrayList<String> billboardPictures = new ArrayList<>();
    private static ArrayList<String> billboardInformation = new ArrayList<>();
    private static int num_files = 16;

    /**
     * Import xml billboard file and set up the mock database
     */
    public static void setupDatabase() {

        // Try to parse the xml file, if it doesn't work throw an exception
        for (int i = 0; i < num_files; i++) {

            try {
                // Load xml file
                File xmlFile = new File("/Users/kanu1/Documents/GitHub/CAB302-Billboard/src/main/billboards/"
                        + (i+1) + ".xml");

                // Use DocumentBuilderFactory to parse xml file
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document xmlDoc = dBuilder.parse(xmlFile);

                // Create an element which is the billboard tag
                NodeList billboardNodeList = xmlDoc.getElementsByTagName("billboard");
                Node billboardNode = billboardNodeList.item(0);
                Element billboardElement = (Element) billboardNode;

                // If the message tag exists, add the message content to the ArrayList, if not add an empty string
                if (billboardElement.getElementsByTagName("message").getLength() == 1) {
                    String message = billboardElement.getElementsByTagName("message").item(0).getTextContent();
                    billboardMessages.add(i, message);
                } else {
                    billboardMessages.add(i, "");
                }

                // If the picture tag exists, add the url attribute to the ArrayList, if not add an empty string
                if (billboardElement.getElementsByTagName("picture").getLength() == 1) {
                    Node pictureNode = billboardElement.getElementsByTagName("picture").item(0);
                    Element pictureElement = (Element) pictureNode;
                    String picture = pictureElement.getAttribute("url");
                    billboardPictures.add(i, picture);
                } else {
                    billboardPictures.add(i, "");
                }

                // If the information tag exists, add the information content to the ArrayList, if not add an empty string
                if (billboardElement.getElementsByTagName("information").getLength() == 1){
                    String information = billboardElement.getElementsByTagName("information").item(0).getTextContent();
                    billboardInformation.add(i, information);
                } else {
                    billboardInformation.add(i, "");
                }

                // Print the tags to the console
//                System.out.println("Billboard " + (i+1));
//                System.out.println("Message: " + billboardMessages.get(i));
//                System.out.println("Picture: " + billboardPictures.get(i));
//                System.out.println("Information: " + billboardInformation.get(i) + "\n");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Gets an ArrayList of the message, picture and information associated with a certain billboard
     * @param billboardNo - the billboard number we want the tags of
     * @return billboardTags - an ArrayList containing the content in the tags of the billboard
     */
    public static ArrayList<String> getBillboard(int billboardNo) {
        // Set up the mock database
        setupDatabase();

        // Create an ArrayList to store the tag content in
        ArrayList<String> billboardTags = new ArrayList<>();

        // Add the content from the tags to the ArrayList
        billboardTags.add(0, billboardMessages.get(billboardNo));
        billboardTags.add(1, billboardPictures.get(billboardNo));
        billboardTags.add(2, billboardInformation.get(billboardNo));

        return billboardTags;
    }


    // Main method
//    public static void main(String[] args) {
//        MockBillboardDatabase dbTest = new MockBillboardDatabase();
//        setupDatabase();
//    }

}
