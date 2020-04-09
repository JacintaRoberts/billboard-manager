package viewer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilderFactory;

public class Viewer {

    // Create and set up the JFrame to show the viewer
    private JFrame viewer = new JFrame ("Billboard Viewer");

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

        viewer.addKeyListener(escListener);
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

        viewer.addMouseListener(clickListener);
    }

    /**
     * Formats the display window using the mock database depending on whether there is a message,
     * picture, information or a combination.
     */
    public void formatBillboard() {

    }


    /**
     * If there are no billboards to display, display a message for the user.
     */
    public void noBillboardDisplay() {
        // Create a label to display a message and format it
        JLabel message = new JLabel("There are no billboards to display right now.", SwingConstants.CENTER);
        message.setFont(new Font(message.getFont().getName(), Font.PLAIN, 50));
        viewer.add(message);
    }

    /**
     * Show the GUI for displaying the billboard.
     */
    public void showViewer() {

        // Displaying the window to be completely full screen
        viewer.setExtendedState(Frame.MAXIMIZED_BOTH);
        viewer.setUndecorated(true);

        viewer.setVisible(true);
    }


    public static void main(String[] args ) {
        Viewer BillboardDemo = new Viewer();
        BillboardDemo.noBillboardDisplay();
        BillboardDemo.listenEscapeKey();
        BillboardDemo.listenMouseClick();
        BillboardDemo.showViewer();

    }

}
