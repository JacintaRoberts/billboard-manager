package viewer;

import java.io.File;
import java.util.ArrayList;

/**
 * This class will act as a mock billboards database using the example xml files in resources, so that the Viewer class
 * can be implemented without the actual database.
 */
public class MockBillboardDatabase {

    // Initialise ArrayList to store the XML files in
    private static ArrayList<File> xmlFiles = new ArrayList<>();
    private static int numFiles = 17;

    /**
     * This function imports the example xml billboard files in the resources folder and returns it as an ArrayList of
     * Files. This will act as the mock database.
     * @return Returns an ArrayList of Files of the example xml files in resources folder.
     */
    public static ArrayList<File> setupDatabase() {

        // Import each xml file and add it to the ArrayList
        for (int i = 0; i < numFiles; i++) {
            File xmlFile = new File("src/main/resources/billboards/"
                    + (i+1) + ".xml");
            xmlFiles.add(i, xmlFile);
        }

        return xmlFiles;
    }

}
