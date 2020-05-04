package controlPanel;

import controlPanel.Main.VIEW_TYPE;
import observer.Subject;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.synth.SynthTextAreaUI;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
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
    private JLabel repeatLabel;
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
    private Integer[] minutes;

    private JComboBox<Integer> startHourSelector;
    private JComboBox<String> startMinSelector;
    private String[] am_pm;
    private JComboBox<String> startAMPMSelector;

    private JComboBox<Integer> endHourSelector;
    private JComboBox<String> endMinSelector;
    // am pm selector
    private JComboBox<String> endAMPMSelector;

    private int duration;

    private ArrayList<JCheckBox> checkboxGroup;

    // --- Buttons ---
    private JButton submitButton;
    private JButton confirmButton;

    // --- ENUM ---
    private VIEW_TYPE view_type;

//    protected enum BUTTON_ENUM
//    {
//        DAILY, HOURLY, MINUTE;
//    }

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
        timePanel.setLayout(new GridLayout(6,2));
        // create labels
        timeLabel = new JLabel("Select Billboard Name and Time:");
        bbNameLabel = new JLabel("Billboard Name: ");
        bbNameSelector = new JComboBox(new String[]{});

        // define hours and minute selection
        hour = new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12};
        minutes = new Integer[60];
        DecimalFormat df = new DecimalFormat("00");
        for (int minute = 0; minute<60; minute++)
        {
            String minuteValue = df.format(minute);
            minutes[minute] = Integer.valueOf(minuteValue);
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
        checkboxGroup = new ArrayList<>();
        monLabel = new JCheckBox("Mon");
        tuesLabel = new JCheckBox("Tues");
        wedLabel = new JCheckBox("Wed");
        thurLabel = new JCheckBox("Thurs");
        friLabel = new JCheckBox("Fri");
        satLabel = new JCheckBox("Sat");
        sunLabel = new JCheckBox("Sun");
        checkboxGroup.add(monLabel);
        checkboxGroup.add(tuesLabel);
        checkboxGroup.add(wedLabel);
        checkboxGroup.add(thurLabel);
        checkboxGroup.add(friLabel);
        checkboxGroup.add(satLabel);
        checkboxGroup.add(sunLabel);
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
        recurrencePanel.setLayout(new GridLayout(6,1));
        // add labels to panel
        hourlyButton = new JRadioButton("Hourly");
        dailyButton = new JRadioButton("Daily");
        minuteButton = new JRadioButton("Minutes");
        hourlyButton.setName("hourly");
        dailyButton.setName("daily");
        minuteButton.setName("minute");
        repeatLabel = new JLabel("Repeat every X minute/s: ");
        repeatMinutes = new JPanel();
        repeatMinutes.setLayout(new FlowLayout());
        Integer[] minuteSelection = new Integer[60];
        for (int i = 1; i < 60; i++)
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

        submitButton = new JButton("Submit");
        JPanel navPanel = getNavPanel();
        navPanel.add(submitButton);
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

    protected void calcDuration()
    {
        // get values from user
        // hour
        int startHour = (int)startHourSelector.getSelectedItem();
        int endHour = (int)endHourSelector.getSelectedItem();
        // minutes
        int startMinute = (int)startMinSelector.getSelectedItem();
        int endMinute = (int)endMinSelector.getSelectedItem();
        // am pm
        String startAM_PM = (String) startAMPMSelector.getSelectedItem();
        String endAM_PM = (String) endAMPMSelector.getSelectedItem();
        // calculate the difference
        int minDifference = endMinute - startMinute;
        int hourDifference = endHour - startHour;

        // Invalid if PM -> AM
        if (startAM_PM.equals("PM") && endAM_PM.equals("AM"))
        {
            durationTimeLabel.setText("Invalid");
        }
        // Add +12 if AM -> PM, create error if duration is equal or less than 0, set duration text if valid
        else
        {
            if (startAM_PM.equals("AM") && endAM_PM.equals("PM"))
            {
                hourDifference = hourDifference + 12;
            }
            duration = hourDifference*60 + minDifference;
            // create error if duration is equal to or less than 0
            if (duration <= 0)
            {
                durationTimeLabel.setText("Invalid");
            }
            // calculate hours, minutes and total minutes to display to user
            else
            {
                String hours = String.valueOf(duration/60);
                String minutes = String.valueOf(duration%60);
                String totalMinutes = String.valueOf(duration);
                durationTimeLabel.setText(hours + " hrs " + minutes + " mins. Total Minutes: " + totalMinutes);
            }

            // disable hourly button if duration is longer than 1 hour
            if (duration > 60)
            {
                hourlyButton.setEnabled(false);
            }
            else
            {
                hourlyButton.setEnabled(true);
            }

            repeatCheckBox.removeAllItems();
            for (int i = (duration+1); i <= 1440; i++)
            {
                repeatCheckBox.addItem(i);
            }
        }
    }

    protected void checkAllDayButtons(boolean selectAll)
    {
        monLabel.setSelected(selectAll);
        tuesLabel.setSelected(selectAll);
        wedLabel.setSelected(selectAll);
        thurLabel.setSelected(selectAll);
        friLabel.setSelected(selectAll);
        satLabel.setSelected(selectAll);
        sunLabel.setSelected(selectAll);
    }

    protected void enableMinuteSelector(boolean enable)
    {
        repeatCheckBox.setEnabled(enable);
        repeatCheckBox.setSelectedIndex(0);
    }

    protected void setValues(boolean[] daysOfWeek, int startHour, int  endHour, int startMin, int endMin, String buttonSelect, int minRepeat)
    {
        for (int i = 0; i < daysOfWeek.length ;i++)
        {
            System.out.println("Check box " + checkboxGroup.get(i));
            System.out.println("boolean " + daysOfWeek[i]);
            checkboxGroup.get(i).setSelected(daysOfWeek[i]);
        }
        startHourSelector.getModel().setSelectedItem(startHour);
        endHourSelector.getModel().setSelectedItem(endHour);
        startMinSelector.getModel().setSelectedItem(startMin);
        endMinSelector.getModel().setSelectedItem(endMin);
        repeatCheckBox.getModel().setSelectedItem(minRepeat);

        switch (buttonSelect)
        {
            case "hourly":
                hourlyButton.setSelected(true);
                enableMinuteSelector(false);
                break;
            case "minute":
                minuteButton.setSelected(true);
                enableMinuteSelector(true);
                break;
            case "daily":
                dailyButton.setSelected(true);
                enableMinuteSelector(false);
                break;
        }
    }

    protected void showConfirmationDialog()
    {
        JOptionPane.showMessageDialog(null,"You have just scheduled Billboard " + bbNameSelector.getSelectedItem());
    }

    private boolean checkRequiredFields()
    {
        boolean isDaySelected = false;
        for (JCheckBox checkBox: checkboxGroup)
        {
            if (checkBox.isSelected())
            {
                isDaySelected = true;
                break;
            }
        }
        return isDaySelected;
    }

    protected ArrayList<Object> getScheduleInfo()
    {
        ArrayList<Object> scheduleInfo = new ArrayList();
        boolean requiredFieldsProvided = checkRequiredFields();
        if (requiredFieldsProvided)
        {
            String name = (String) bbNameSelector.getSelectedItem();
            int min_repeat = (int) repeatCheckBox.getSelectedItem();

            scheduleInfo.add(name);
            scheduleInfo.add(min_repeat);

        }
        return scheduleInfo;
    }

    protected boolean checkValidDuration()
    {
        return !durationTimeLabel.getText().equals("Invalid");
    }

    protected void raiseScheduleError()
    {
        JOptionPane.showMessageDialog(null, "Please select at least one day");
    }

    protected void raiseDurationError()
    {
        JOptionPane.showMessageDialog(null, "Please select a valid duration");
    }

    protected void addDurationListener(ActionListener listener)
    {
        startAMPMSelector.addActionListener(listener);
        endAMPMSelector.addActionListener(listener);
        startMinSelector.addActionListener(listener);
        endMinSelector.addActionListener(listener);
        startHourSelector.addActionListener(listener);
        endHourSelector.addActionListener(listener);
    }

    protected void addDailyRadioButtonListener(ActionListener listener)
    {
        dailyButton.addActionListener(listener);
        hourlyButton.addActionListener(listener);
        minuteButton.addActionListener(listener);
    }

    protected void addPopulateScheduleListener(ActionListener listener)
    {
        bbNameSelector.addActionListener(listener);
    }

    protected void addScheduleSubmitButtonListener(MouseAdapter listener)
    {
        submitButton.addMouseListener(listener);
    }
}
