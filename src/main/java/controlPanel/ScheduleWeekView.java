package controlPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import controlPanel.Main.VIEW_TYPE;
import java.awt.*;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Schedule Week View to view the weekly schedule of billboards. This displays the billboard name and creator,
 * including the start and end time.
 */
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
    ArrayList<String> dayLabels;
    // -- Button --
    private JButton scheduleMenuButton;

    /**
     * Constructor to create schedule view, use parent constructor.
     */
    public ScheduleWeekView()
    {
        super("Billboard Schedule");
        view_type = VIEW_TYPE.SCHEDULE_WEEK;
        addScheduleMenuButton();
    }

    @Override
    void createComponents()
    {
        // create a array list of day labels (mon - sun)
        dayLabels = new ArrayList<>();
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

        // create 7 day scrollable tables
        for(String days : dayLabels)
        {
            // create table model and add columns
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Time");
            model.addColumn("Billboard");
            model.addColumn("Creator");
            // create table with table model
            JTable dayTable = new JTable(model);
            dayTable.setRowHeight(50);
            // ensure table is not editable by the user
            dayTable.setDefaultEditor(Object.class, null);
            // set name of table so it can be used in controller
            dayTable.setName(days);
            // ensure table has a scroll bar
            JScrollPane scrollPane = new JScrollPane(dayTable);
            // set title of table
            scrollPane.setBorder(BorderFactory.createTitledBorder(days));
            // set colour of scroll pane
            scrollPane.setForeground(Color.WHITE);
            scrollPane.setBackground(Color.BLACK);
            // add scroll pane to the array so this can be populated later
            dayScheduleMap.put(days, model);
            // add table to calendar panel
            calendarPanel.add(scrollPane);
        }
        getContentPane().add(calendarPanel, BorderLayout.CENTER);
    }

    private void addScheduleMenuButton()
    {
        scheduleMenuButton = new JButton("Schedule Menu");
        JPanel navPanel = getNavPanel();
        GridBagConstraints gbc = getNavGBCPanel();
        navPanel.add(scheduleMenuButton, setGBC(gbc, 2,1,1,1));
    }

    /**
     * Clean up the schedule by removing all rows in the models for each day.
     */
    @Override
    void cleanUp()
    {
        // loop thru each day's model and set row count to 0 to remove
        for(DefaultTableModel model : dayScheduleMap.values())
        {
            model.setRowCount(0);
        }
    }

    @Override
    Main.VIEW_TYPE getEnum() {
        return view_type;
    }

    /**
     * Populate Schedule from the database
     * @param schedule array containing weekly schedule
     */
    protected void populateSchedule(ArrayList<ArrayList<ArrayList<String>>> schedule)
    {
        int index = 0;
        for (ArrayList<ArrayList<String>> daySchedule : schedule)
        {
            DefaultTableModel model = dayScheduleMap.get(dayLabels.get(index));
            if (daySchedule != null){
                for(ArrayList<String> row : daySchedule)
                {
                    // add info
                    model.addRow(row.toArray());
                }
            }
            index++;
        }
    }

    /**
     * Add listener to navigate to schedule Menu View
     * @param listener mouse listener
     */
    protected void addScheduleMenuListener(MouseListener listener)
    {
        scheduleMenuButton.addMouseListener(listener);
    }
}
