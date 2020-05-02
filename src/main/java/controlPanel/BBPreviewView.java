package controlPanel;

import controlPanel.Main.VIEW_TYPE;
import observer.Subject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;


public class BBPreviewView extends AbstractView
{
    // *** VARIABLES**
    // --- Panels ---
    private JPanel previewPanel;
    // --- ENUM ---
    private VIEW_TYPE view_type;

    /**
     * Constructor for creating the Views of the application. The constructor sets the frame's name and set's up the
     * View by defining Width and Height, default close operation and the Layout.
     */
    public BBPreviewView() {
        super("Billboard Preview View");
        this.view_type = VIEW_TYPE.BB_PREVIEW;
        createComponents();
    }

    private void createComponents()
    {
        // exit on close
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // preview panel add to frame
        previewPanel = new JPanel();
        getContentPane().add(previewPanel, BorderLayout.CENTER);
    }

    protected VIEW_TYPE getEnum()
    {
        return view_type;
    }

    @Override
    void cleanUp() {

    }

    @Override
    public void update(Subject s) {

    }
}
