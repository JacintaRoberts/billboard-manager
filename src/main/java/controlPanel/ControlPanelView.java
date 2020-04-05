package controlPanel;

import observer.Observer;

import javax.swing.*;
import java.awt.*;

public abstract class ControlPanelView extends JFrame implements Observer
{
    // *** DECLARE VARIABLES**
    // Purpose: define variables for size of frame
    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;

    /**
     * Constructor for creating the Views of the application. The constructor sets the frame's name and set's up the
     * View by defining Width and Height, default close operation and the Layout.
     * @param frame_name name of JFrame
     */
    public ControlPanelView(String frame_name)
    {
        // assign frame name
        super(frame_name);
        setupFrame();
    }

    /**
     * Set up Frame by setting size, close operation and layout.
     */
    private void setupFrame()
    {
        // Purpose: size, close operation and layout
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
    }
}
