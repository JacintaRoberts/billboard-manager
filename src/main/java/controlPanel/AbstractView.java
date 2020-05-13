package controlPanel;

import observer.Observer;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.Font;

public abstract class AbstractView extends JFrame implements Observer
{
    // *** DECLARE VARIABLES**
    private static final int WIDTH = 800;
    private static final int HEIGHT = 800;

    public AbstractView(String frame_name)
    {
        // assign frame name
        super(frame_name);
        Font font = new Font("Garamond",  Font.BOLD, 30);
        Color pinkColour = new Color(255,87,87);
        Color navyColour = new Color(31,29, 41);
        // label colour
        UIManager.put("Label.font", font);
        UIManager.put("Label.foreground", pinkColour);
        // check box colour
        UIManager.put("CheckBox.font", font);
        UIManager.put("CheckBox.foreground", Color.WHITE);
        // option pane
        UIManager.put("OptionPane.font", font);
        UIManager.put("OptionPane.messageFont", font);
        UIManager.put("OptionPane.foreground", Color.WHITE);
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
        UIManager.put("OptionPane.background", navyColour);
        // text field pane
        UIManager.put("TextField.font", font);
        UIManager.put("TextField.background", Color.GRAY);
        UIManager.put("TextField.foreground", Color.WHITE);
        // password field pane
        UIManager.put("PasswordField.font", font);
        UIManager.put("PasswordField.background", Color.GRAY);
        UIManager.put("PasswordField.foreground", Color.WHITE);

        // buttons
        UIManager.put("Button.font", font);
        UIManager.put("Button.background", Color.WHITE);
        UIManager.put("Button.foreground",Color.BLACK);

        // radio buttons
        UIManager.put("RadioButton.font", font);
        UIManager.put("RadioButton.background", navyColour);
        UIManager.put("RadioButton.foreground",Color.WHITE);

        // combobox
        UIManager.put("ComboBox.font", font);
        UIManager.put("ComboBox.foreground",Color.BLACK);

        // checkbox
        UIManager.put("CheckBox.font", font);
        UIManager.put("CheckBox.background", navyColour);
        UIManager.put("CheckBox.foreground",Color.WHITE);

        UIManager.put("Panel.background", navyColour);

        setupFrame();
    }

    protected static GridBagConstraints setGBC(GridBagConstraints gbc, int x, int y, int w, int h)
    {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridheight = h;
        gbc.gridwidth = w;
        return gbc;
    }

    /**
     * Set up Frame by setting size, close operation and layout.
     */
    private void setupFrame()
    {
        // Purpose: size, close operation and layout
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setState(Frame.NORMAL);
        setLocationRelativeTo(null);
    }

    abstract Main.VIEW_TYPE getEnum();

    abstract void cleanUp();
}
