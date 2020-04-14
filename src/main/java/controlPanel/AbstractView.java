package controlPanel;

import observer.Observer;

import javax.swing.*;

public abstract class AbstractView extends JFrame implements Observer
{

    public AbstractView(String frame_name)
    {
        // assign frame name
        super(frame_name);
    }

    abstract Main.VIEW_TYPE getEnum();

}
