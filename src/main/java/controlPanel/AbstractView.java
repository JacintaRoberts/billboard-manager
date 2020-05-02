package controlPanel;

import observer.Observer;

import javax.swing.*;
import java.awt.*;

public abstract class AbstractView extends JFrame implements Observer
{
    // *** DECLARE VARIABLES**
    private static final int WIDTH = 800;
    private static final int HEIGHT = 800;

    public AbstractView(String frame_name)
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
        setLocationRelativeTo(null);
    }

    abstract Main.VIEW_TYPE getEnum();

    abstract void cleanUp();
}
