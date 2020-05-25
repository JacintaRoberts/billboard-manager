package controlPanel;

import viewer.Viewer;

import java.awt.event.*;

public class BBFullPreview extends Viewer
{

    /**
     * HIdes the window if the user presses the escape key.
     */
    @Override
    public void listenEscapeKey() {
        KeyListener escListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    setVisible(false);
                }
            }
        };

        addKeyListener(escListener);
    }


    /**
     * Hides the viewer if the user clicks anywhere on the window.
     */
    @Override
    public void listenMouseClick() {
        MouseListener clickListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                setVisible(false);
            }
        };

        addMouseListener(clickListener);
    }

}
