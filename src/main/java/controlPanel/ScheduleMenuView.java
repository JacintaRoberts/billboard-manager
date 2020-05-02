package controlPanel;

import controlPanel.Main.VIEW_TYPE;
import observer.Subject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.time.Month;
import java.util.ArrayList;

public class ScheduleMenuView extends AbstractGenericView
{
    //*** VARIABLES**
    // --- ENUM ---
    private VIEW_TYPE view_type;

    /**
     * Constructor to create schedule view, use parent constructor.
     */
    public ScheduleMenuView()
    {
        super("Schedule Menu View");
        view_type = VIEW_TYPE.SCHEDULE_MENU;
    }

    @Override
    VIEW_TYPE getEnum() {
        return view_type;
    }

    @Override
    void cleanUp() {

    }

    @Override
    public void update(Subject s) {

    }

    @Override
    void createComponents() {

    }
}
