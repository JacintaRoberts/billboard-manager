package controlPanel;

import observer.Subject;

import javax.swing.*;
import controlPanel.Main.VIEW_TYPE;

import java.awt.*;

public class ScheduleView extends AbstractGenericView
{
    // *** VARIABLES**
    // --- Panels ---
//    private JPanel optionsPanel;
    // --- Buttons ---
//    private JButton viewUsersButton;
//    private JButton createUsersButton;
    // --- ENUM ---
    private VIEW_TYPE view_type;

    /**
     * Constructor to create schedule view, use parent constructor.
     */
    public ScheduleView()
    {
        super("Schedule View");
        view_type = VIEW_TYPE.SCHEDULE;
    }

    @Override
    void createComponents()
    {

    }

    @Override
    Main.VIEW_TYPE getEnum() {
        return view_type;
    }

    @Override
    public void update(Subject s) {

    }
}
