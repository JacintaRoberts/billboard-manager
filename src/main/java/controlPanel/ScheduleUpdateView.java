package controlPanel;

import controlPanel.Main.VIEW_TYPE;
import observer.Subject;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseListener;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.concurrent.Flow;

public class ScheduleUpdateView extends AbstractGenericView
{
    //*** VARIABLES**
    //--- Panels ---
    private JPanel timePanel;
    private JPanel recurrencePanel;
    private JPanel weekRecurrencePanel;
    //--- Labels ---
    private JLabel bbNameLabel;
    private JLabel startTimeLabel;
    private JLabel endTimeLabel;
    private JLabel durationLabel;
    private JLabel startDayLabel;
    private JLabel endDayLabel;
    private JLabel repeatLabel;
    private JCheckBox dailyLabel;
    private JCheckBox hourlyLabel;
    private JComboBox<Integer> repeatCheckBox;
    private JComboBox<String> bbNameSelector;
    private JCheckBox monLabel;
    private JCheckBox tuesLabel;
    private JCheckBox wedLabel;
    private JCheckBox thurLabel;
    private JCheckBox friLabel;
    private JCheckBox satLabel;
    private JCheckBox sunLabel;
    private JPanel startTimePanel;
    private JPanel endTimePanel;
    private JLabel durationTimeLabel;

    private ButtonGroup group;
    private JRadioButton hourlyButton;
    private JRadioButton minuteButton;
    private JRadioButton dailyButton;

    private JPanel repeatMinutes;
    private JLabel timeLabel;
    private JLabel recurrenceLabel;
    private JLabel weekLabel;

    private Integer[] hour;
    private String[] minutes;

    private JComboBox<Integer> startHourSelector;
    private JComboBox<String> startMinSelector;
    private String[] am_pm;
    private JComboBox<String> startAMPMSelector;

    private JComboBox<Integer> endHourSelector;
    private JComboBox<String> endMinSelector;
    // am pm selector
    private JComboBox<String> endAMPMSelector;

    // --- Buttons ---
    private JButton submitButton;
    private JButton cancelButton;
    // --- Text Field ---
    private JLabel bbNameText;
    private JTextField startTimeText;
    private JTextField endTimeText;
    private JTextField durationText;

    // --- ENUM ---
    private VIEW_TYPE view_type;


    /**
     * Constructor to create schedule view, use parent constructor.
     */
    public ScheduleUpdateView()
    {
        super("Schedule Update View");
        view_type = VIEW_TYPE.SCHEDULE_UPDATE;
    }

    @Override
    void createComponents()
    {
        // time panel
        timePanel = new JPanel();
        timePanel.setLayout(new GridLayout(5,2));
        // create labels
        timeLabel = new JLabel("Select Billboard Name and Time:");
        bbNameLabel = new JLabel("Billboard Name: ");
        bbNameSelector = new JComboBox(new String[]{});
        startDayLabel = new JLabel("Start Day:");

        // define hours and minute selection
        hour = new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12};
        minutes = new String[60];
        DecimalFormat df = new DecimalFormat("00");
        for (int minute = 0; minute<60; minute++)
        {
            String minuteValue = df.format(minute);
            minutes[minute] = minuteValue;
        }
        am_pm = new String[] {"AM", "PM"};

        // -------------- START TIME PANEL ------------------
        // create start time panel
        startTimePanel = new JPanel();
        startTimePanel.setLayout(new FlowLayout());
        // hour selector
        startHourSelector = new JComboBox(hour);
        startMinSelector = new JComboBox(minutes);
        // am pm selector
        startAMPMSelector = new JComboBox(am_pm);
        // add items to panel
        startTimePanel.add(startHourSelector);
        startTimePanel.add(startMinSelector);
        startTimePanel.add(startAMPMSelector);

        // -------------- END TIME PANEL ------------------
        // create start time panel
        endTimePanel = new JPanel();
        endTimePanel.setLayout(new FlowLayout());
        // hour selector
        endHourSelector = new JComboBox(hour);
        endMinSelector = new JComboBox(minutes);
        // am pm selector
        endAMPMSelector = new JComboBox(am_pm);
        // add items to panel
        endTimePanel.add(endHourSelector);
        endTimePanel.add(endMinSelector);
        endTimePanel.add(endAMPMSelector);

        startTimeLabel = new JLabel("Start Time:");
        endTimeLabel = new JLabel("End Time:");
        durationLabel = new JLabel("Duration: ");
        durationTimeLabel = new JLabel("0 minutes");
        endDayLabel = new JLabel("End Day: ");

        // -------------- ADD TO TIME PANEL ------------------
        timePanel.add(timeLabel);
        timePanel.add(new JLabel(""));
        timePanel.add(bbNameLabel);
        timePanel.add(bbNameSelector);
        timePanel.add(startTimeLabel);
        timePanel.add(startTimePanel);
        timePanel.add(endTimeLabel);
        timePanel.add(endTimePanel);
        timePanel.add(durationLabel);
        timePanel.add(durationTimeLabel);
        getContentPane().add(timePanel, BorderLayout.WEST);

        // -------------- WEEK DAY SELECTION ------------------
        weekRecurrencePanel = new JPanel();
        weekRecurrencePanel.setLayout(new GridLayout(8,1));
        // create labels
        weekLabel = new JLabel("Select Days to Schedule Billboard");
        monLabel = new JCheckBox("Mon");
        tuesLabel = new JCheckBox("Tues");
        wedLabel = new JCheckBox("Wed");
        thurLabel = new JCheckBox("Thurs");
        friLabel = new JCheckBox("Fri");
        satLabel = new JCheckBox("Sat");
        sunLabel = new JCheckBox("Sun");
        weekRecurrencePanel.add(weekLabel);
        weekRecurrencePanel.add(monLabel);
        weekRecurrencePanel.add(tuesLabel);
        weekRecurrencePanel.add(wedLabel);
        weekRecurrencePanel.add(thurLabel);
        weekRecurrencePanel.add(friLabel);
        weekRecurrencePanel.add(satLabel);
        weekRecurrencePanel.add(sunLabel);
        getContentPane().add(weekRecurrencePanel, BorderLayout.CENTER);

        // -------------- RECURRENCE PATTERNS ------------------
        recurrencePanel = new JPanel();
        recurrencePanel.setLayout(new GridLayout(5,1));
        // add labels to panel
        hourlyButton = new JRadioButton("Hourly");
        dailyButton = new JRadioButton("Daily");
        minuteButton = new JRadioButton("Minutes");
        repeatLabel = new JLabel("Repeat every X minute/s: ");
        repeatMinutes = new JPanel();
        Integer[] minuteSelection = new Integer[59];
        for (int i = 1; i < 59; i++)
        {
            minuteSelection[i] = i;
        }
        repeatCheckBox = new JComboBox(minuteSelection);
        repeatCheckBox.setEnabled(false);
        repeatMinutes.add(repeatLabel);
        repeatMinutes.add(repeatCheckBox);

        // add buttons to group such that only 1 can be selected
        group = new ButtonGroup();
        group.add(hourlyButton);
        group.add(dailyButton);
        group.add(minuteButton);

        // add items to panel
        recurrenceLabel = new JLabel("Select Recurrence of Billboard");
        recurrencePanel.add(recurrenceLabel);
        recurrencePanel.add(hourlyButton);
        recurrencePanel.add(dailyButton);
        recurrencePanel.add(minuteButton);
        recurrencePanel.add(repeatMinutes);
        getContentPane().add(recurrencePanel, BorderLayout.EAST);
    }
    
    @Override
    void cleanUp() {

    }

    @Override
    VIEW_TYPE getEnum() {
        return view_type;
    }

    @Override
    public void update(Subject s) {

    }

    protected void getBBNames(String[] BBNames)
    {
        for (String name: BBNames)
        {
            bbNameSelector.addItem(name);
        }
    }

    protected void calcDuration(MouseListener listener)
    {

    }


//    /**
//     * Add listener to handle mouse click of submit button.
//     * @param listener mouse click listener
//     */
//    public void addDayButtonListener(MouseListener listener)
//    {
//        for (JButton dayButton : buttonArray)
//        {
//            dayButton.addMouseListener(listener);
//        }
//    }

//
//    // start day
//    String[] daySelection = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
//    startDayComboBox = new JComboBox(daySelection);
//
//    // end day
//    endDayComboBox = new JComboBox(daySelection);
}
