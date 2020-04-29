package controlPanel;

import observer.Subject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import controlPanel.Main.VIEW_TYPE;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ScheduleWeekView extends AbstractGenericView
{
    //*** VARIABLES**
    //--- Panels ---
    private JPanel calendarPanel;
    // --- ENUM ---
    private VIEW_TYPE view_type;
    // --- TABLES ---
    public HashMap<String, DefaultTableModel> dayScheduleMap;
    // --- MISC ---
    int days_in_week;


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
        // create a array list of day labels (mon - sun)
        ArrayList<String> dayLabels = new ArrayList<>();
        dayLabels.add("Monday");
        dayLabels.add("Tuesday");
        dayLabels.add("Wednesday");
        dayLabels.add("Thursday");
        dayLabels.add("Friday");
        dayLabels.add("Saturday");
        dayLabels.add("Sunday");

        days_in_week = 7;

        // calendar panel
        calendarPanel = new JPanel();
        calendarPanel.setLayout(new GridLayout(1,days_in_week));

        // initialise the day schedule map
        dayScheduleMap = new HashMap<>();

        // create 7 scrollable tables
        for(String days : dayLabels)
        {
            // create table model and add columns
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Time");
            model.addColumn("Billboard");
            // create table with table model
            JTable dayTable = new JTable(model);
            // ensure table is not editable by the user
            dayTable.setDefaultEditor(Object.class, null);
            // set name of table so it can be used in controller
            dayTable.setName(days);
            // ensure table has a scroll bar
            JScrollPane scrollPane = new JScrollPane(dayTable);
            // set title of table
            scrollPane.setBorder(BorderFactory.createTitledBorder(days));
            // add scroll pane to the array so this can be populated later
            dayScheduleMap.put(days, model);
            // add table to calendar panel
            calendarPanel.add(scrollPane);
        }
        getContentPane().add(calendarPanel, BorderLayout.CENTER);
    }

    @Override
    void cleanUp()
    {
        System.out.println("Cleaning up");
        for(DefaultTableModel model : dayScheduleMap.values())
        {
            model.setRowCount(0);
        }
    }

    @Override
    Main.VIEW_TYPE getEnum() {
        return view_type;
    }

    @Override
    public void update(Subject s) {

    }

    protected void populatedSchedule(String[][] bbScheduleArray)
    {
        for(DefaultTableModel model : dayScheduleMap.values())
        {
            for(String[] schedule: bbScheduleArray)
            {
                model.addRow(schedule);
            }
        }
    }
}
