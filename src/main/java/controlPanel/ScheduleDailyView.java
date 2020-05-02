package controlPanel;

import controlPanel.Main.VIEW_TYPE;
import observer.Subject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.time.Month;
import java.util.ArrayList;

public class ScheduleDailyView extends AbstractListView
{
    //*** VARIABLES**
    // --- ENUM ---
    private VIEW_TYPE view_type;

    /**
     * Constructor to create schedule view, use parent constructor.
     */
    public ScheduleDailyView()
    {
        super("Schedule Daily View");
        view_type = VIEW_TYPE.SCHEDULE_DAY;
    }

    @Override
    VIEW_TYPE getEnum() {
        return view_type;
    }

    @Override
    public void update(Subject s) {

    }

}
