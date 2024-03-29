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
     * Constructor to set up JFrame with provided name and create GUI components
     * Set ENUM value allowing use in Controller Class
     */
    public ScheduleWeekView()
    {
        super("Billboard Schedule");
        // set schedule week enum
        view_type = VIEW_TYPE.SCHEDULE_WEEK;
        // add schedule menu button
        addScheduleMenuButton();
    }

    /**
     * Create View Components which include panels, buttons, text etc. These components make up the view/JFrame seen
     * by the user.
     */
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
        // add calendar panel to frame
        getContentPane().add(calendarPanel, BorderLayout.CENTER);
    }

    /**
     * Add Schedule Menu Button to allow user to navigate back to schedule menu view
     */
    private void addScheduleMenuButton()
    {
        // create schedule menu button
        scheduleMenuButton = new JButton("Schedule Menu");
        // get nav panel and gbc
        JPanel navPanel = getNavPanel();
        GridBagConstraints gbc = getNavGBCPanel();
        // add menu button to nav panel
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
            // remove row
            model.setRowCount(0);
        }
    }

    /**
     * Get Enum associated to this View. This is defined in the Constructor and is used in the Controller Class.
     * @return view type enum assigned to view
     */
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
        // loop through day schedule and populate the models in the table
        for (ArrayList<ArrayList<String>> daySchedule : schedule)
        {
            // get the correct model based on index
            DefaultTableModel model = dayScheduleMap.get(dayLabels.get(index));
            // if not null add schedule to model
            if (daySchedule != null){
                for(ArrayList<String> row : daySchedule)
                {
                    // add schedule info
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
