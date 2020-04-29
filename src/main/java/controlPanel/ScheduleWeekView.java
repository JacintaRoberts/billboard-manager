package controlPanel;

import observer.Subject;

import javax.swing.*;
import controlPanel.Main.VIEW_TYPE;

import java.awt.*;
import java.awt.event.MouseListener;
import java.time.Month;
import java.util.ArrayList;

public class ScheduleWeekView extends AbstractGenericView
{
    //*** VARIABLES**
    //--- Panels ---
    private JPanel calendarPanel;
    //--- Labels ---
    private JLabel monLabel;
    private JLabel tuesLabel;
    private JLabel wedLabel;
    private JLabel thurLabel;
    private JLabel friLabel;
    private JLabel satLabel;
    private JLabel sunLabel;
    // --- ENUM ---
    private VIEW_TYPE view_type;


    /**
     * Constructor to create schedule view, use parent constructor.
     */
    public ScheduleWeekView()
    {
        super("Schedule View");
        view_type = VIEW_TYPE.SCHEDULE_WEEK;
    }

    @Override
    void createComponents()
    {
        int days_in_week = 7;
        // calendar panel
        calendarPanel = new JPanel();
        calendarPanel.setLayout(new GridLayout(2,7));
        // create labels
        monLabel = new JLabel("Mon");
        tuesLabel = new JLabel("Tues");
        wedLabel = new JLabel("Wed");
        thurLabel = new JLabel("Thurs");
        friLabel = new JLabel("Fri");
        satLabel = new JLabel("Sat");
        sunLabel = new JLabel("Sun");
        // add labels to panel
        calendarPanel.add(monLabel);
        calendarPanel.add(tuesLabel);
        calendarPanel.add(wedLabel);
        calendarPanel.add(thurLabel);
        calendarPanel.add(friLabel);
        calendarPanel.add(satLabel);
        calendarPanel.add(sunLabel);
        // create 7 scrollable tables
        


        getContentPane().add(calendarPanel, BorderLayout.CENTER);
    }

    @Override
    void cleanUp() {

    }

    @Override
    Main.VIEW_TYPE getEnum() {
        return view_type;
    }

    @Override
    public void update(Subject s) {

    }
}
