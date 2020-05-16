package controlPanel;

import controlPanel.Main.VIEW_TYPE;
import observer.Subject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import static java.lang.Integer.min;
import static java.lang.Integer.parseInt;

public class ScheduleUpdateView extends AbstractGenericView
{
    //*** VARIABLES**
    //--- Panels ---
    private JPanel timeNamePanel;
    private JPanel recurrencePanel;
    private JPanel weekdayPanel;
    private JPanel repeatMinutesPanel;
    private JPanel startTimePanel;
    private JPanel endTimePanel;

    //--- Labels ---
    private JLabel bbNameLabel;
    private JLabel startTimeLabel;
    private JLabel endTimeLabel;
    private JLabel durationLabel;
    private JLabel repeatMinutesLabel;
    private JLabel durationTimeLabel;
    private JLabel timePanelDescription;
    private JLabel recurrencePanelDescription;
    private JLabel weekdayPanelDescription;
    private JLabel minutesLabel;

    // --- ComboBox ---
    private JComboBox<Integer> repeatMinutesComboBox;
    private JComboBox<String> bbNameComboBox;
    private JComboBox<Integer> startHourSelector;
    private JComboBox<Integer> startMinSelector;
    private JComboBox<Integer> endHourSelector;
    private JComboBox<Integer> endMinSelector;
    private JComboBox<String> startAMPMSelector;
    private JComboBox<String> endAMPMSelector;

    // --- CheckBox ---
    private JCheckBox monCheckBox;
    private JCheckBox tuesCheckBox;
    private JCheckBox wedCheckBox;
    private JCheckBox thurCheckBox;
    private JCheckBox friCheckBox;
    private JCheckBox satCheckBox;
    private JCheckBox sunCheckBox;

    // --- Buttons ---
    private JRadioButton hourlyButton;
    private JRadioButton minuteButton;
    private JRadioButton dailyButton;
    private JRadioButton noRepeatButton;
    private JButton submitButton;

    private int duration;
    private GridBagConstraints gbc_timeName;
    private GridBagConstraints gbc_days;
    private GridBagConstraints gbc_recurrence;

    private ArrayList<JCheckBox> weekdayArray;

    // --- ENUM ---
    private VIEW_TYPE view_type;

    /**
     * Constructor to create schedule create/update view, use parent constructor.
     */
    public ScheduleUpdateView()
    {
        super("Schedule Update View");
        view_type = VIEW_TYPE.SCHEDULE_UPDATE;
    }

    @Override
    void createComponents()
    {
        createTimeNamePanel();
        createWeekDayPanel();
        createRecurrenceSettingsPanel();
        createSubmitButton();
    }

    private void createTimeNamePanel()
    {
        // create time/name panel
        timeNamePanel = new JPanel();
        timeNamePanel.setLayout(new GridBagLayout());
        // create labels
        timePanelDescription = new JLabel("BILLBOARD DETAILS:");
        timePanelDescription.setForeground(Color.WHITE);
        bbNameLabel = new JLabel("Billboard Name: ");
        bbNameComboBox = new JComboBox<>(new String[]{""});
        // define hours and minute selection
        // --- Integer ---
        Integer[] hour = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        // --- Array List ---
        String[] min = new String[60];
        for (int minute = 0; minute < 60; minute++)
        {
            // add minutes to array, pad single digits with a 0
            min[minute] = String.format("%02d",minute);
        }
        // --- day selector ---
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

        // define AM and PM
        // --- String ---
        String[] am_pm = new String[]{"AM", "PM"};

        // -------------- START TIME PANEL ------------------
        // create start time panel
        startTimePanel = new JPanel();
        startTimePanel.setLayout(new FlowLayout());
        // hour & min selector
        startHourSelector = new JComboBox<>(hour);
        startMinSelector = new JComboBox(min);
        // am pm selector
        startAMPMSelector = new JComboBox<>(am_pm);
        // add items to panel
        startTimePanel.add(startHourSelector);
        startTimePanel.add(startMinSelector);
        startTimePanel.add(startAMPMSelector);

        // -------------- END TIME PANEL ------------------
        // create start time panel
        endTimePanel = new JPanel();
        endTimePanel.setLayout(new FlowLayout());
        // hour selector
        endHourSelector = new JComboBox<>(hour);
        endMinSelector = new JComboBox(min);
        // am pm selector
        endAMPMSelector = new JComboBox<>(am_pm);
        // add items to panel
        endTimePanel.add(endHourSelector);
        endTimePanel.add(endMinSelector);
        endTimePanel.add(endAMPMSelector);

        // -------------- CREATE LABELS ------------------
        startTimeLabel = new JLabel("Start Time:");
        endTimeLabel = new JLabel("End Time:");
        durationLabel = new JLabel("Duration: ");
        durationTimeLabel = new JLabel("0 minutes");

        // -------------- ADD ITEMS TO PANEL ------------------
        gbc_timeName = new GridBagConstraints();
        gbc_timeName.insets = new Insets(50,10,50,10);
        timeNamePanel.add(timePanelDescription, setGBC(gbc_timeName, 1,1,1,1));
        timeNamePanel.add(bbNameLabel, setGBC(gbc_timeName, 1,2,1,1));
        timeNamePanel.add(bbNameComboBox, setGBC(gbc_timeName, 2,2,1,1));
        timeNamePanel.add(startTimeLabel, setGBC(gbc_timeName, 1,3,1,1));
        timeNamePanel.add(startTimePanel, setGBC(gbc_timeName, 2,3,1,1));
        timeNamePanel.add(endTimeLabel, setGBC(gbc_timeName, 1,4,1,1));
        timeNamePanel.add(endTimePanel, setGBC(gbc_timeName, 2,4,1,1));
        timeNamePanel.add(durationLabel, setGBC(gbc_timeName, 1,5,1,1));
        timeNamePanel.add(durationTimeLabel, setGBC(gbc_timeName, 2,5,1,1));

        // -------------- ADD TIME PANEL TO FRAME ------------------
        getContentPane().add(timeNamePanel, BorderLayout.WEST);
    }

    private void createWeekDayPanel()
    {
        // -------------- WEEK DAY SELECTION ------------------
        weekdayPanel = new JPanel();
        weekdayPanel.setLayout(new GridLayout(8,1));

        // create labels
        weekdayPanelDescription = new JLabel("SELECT DAYS:");
        weekdayPanelDescription.setForeground(Color.WHITE);
        monCheckBox = new JCheckBox("Monday");
        tuesCheckBox = new JCheckBox("Tuesday");
        wedCheckBox = new JCheckBox("Wednesday");
        thurCheckBox = new JCheckBox("Thursday");
        friCheckBox = new JCheckBox("Friday");
        satCheckBox = new JCheckBox("Saturday");
        sunCheckBox = new JCheckBox("Sunday");
        weekdayArray = new ArrayList<>();

        // add labels to array list
        weekdayArray.add(monCheckBox);
        weekdayArray.add(tuesCheckBox);
        weekdayArray.add(wedCheckBox);
        weekdayArray.add(thurCheckBox);
        weekdayArray.add(friCheckBox);
        weekdayArray.add(satCheckBox);
        weekdayArray.add(sunCheckBox);

        // add labels to panel
        gbc_days = new GridBagConstraints();
        gbc_days.insets = new Insets(30,0,30,10);
        weekdayPanel.add(weekdayPanelDescription, setGBC(gbc_days, 1,1,1,1));
        weekdayPanel.add(monCheckBox, setGBC(gbc_days, 1,2,1,1));
        weekdayPanel.add(tuesCheckBox, setGBC(gbc_days, 1,3,1,1));
        weekdayPanel.add(wedCheckBox, setGBC(gbc_days, 1,4,1,1));
        weekdayPanel.add(thurCheckBox, setGBC(gbc_days, 1,5,1,1));
        weekdayPanel.add(friCheckBox, setGBC(gbc_days, 1,6,1,1));
        weekdayPanel.add(satCheckBox, setGBC(gbc_days, 1,7,1,1));
        weekdayPanel.add(sunCheckBox, setGBC(gbc_days, 1,8,1,1));

        // add panel to frame
        getContentPane().add(weekdayPanel, BorderLayout.CENTER);
    }

    private void createRecurrenceSettingsPanel()
    {
        // -------------- RECURRENCE PATTERNS ------------------
        recurrencePanel = new JPanel();
        recurrencePanel.setLayout(new GridLayout(7,1));
        recurrencePanelDescription = new JLabel("SELECT RECURRENCE:");
        recurrencePanelDescription.setForeground(Color.WHITE);

        // create radio buttons
        hourlyButton = new JRadioButton("Hourly");
        dailyButton = new JRadioButton("Daily");
        minuteButton = new JRadioButton("Minutes");
        noRepeatButton = new JRadioButton("No Repetition");

        // set name of radio buttons
        hourlyButton.setName("hourly");
        dailyButton.setName("daily");
        minuteButton.setName("minute");
        noRepeatButton.setName("no repeats");

        // add buttons to group such that only 1 can be selected
        // --- Group ---
        ButtonGroup group = new ButtonGroup();
        group.add(hourlyButton);
        group.add(dailyButton);
        group.add(minuteButton);
        group.add(noRepeatButton);

        // create labels
        repeatMinutesLabel = new JLabel("Repeat every X minute/s: ");
        minutesLabel = new JLabel("");
        minutesLabel.setEnabled(false);

        // create panel to contain minute selection
        repeatMinutesPanel = new JPanel();
        repeatMinutesPanel.setLayout(new GridLayout(3,1));
        repeatMinutesComboBox = new JComboBox(new Integer[]{});
        repeatMinutesComboBox.setEnabled(false);

        // add labels and combo box to panel
        repeatMinutesPanel.add(repeatMinutesLabel);
        repeatMinutesPanel.add(repeatMinutesComboBox);
        repeatMinutesPanel.add(minutesLabel);

        // add items to panel
        gbc_recurrence = new GridBagConstraints();
        gbc_recurrence.insets = new Insets(30,10,30,10);
        recurrencePanel.add(recurrencePanelDescription, setGBC(gbc_recurrence,1,1,1,1));
        recurrencePanel.add(hourlyButton, setGBC(gbc_recurrence,1,2,1,1));
        recurrencePanel.add(dailyButton, setGBC(gbc_recurrence,1,3,1,1));
        recurrencePanel.add(minuteButton, setGBC(gbc_recurrence,1,4,1,1));
        recurrencePanel.add(repeatMinutesPanel, setGBC(gbc_recurrence,5,1,1,1));
        recurrencePanel.add(noRepeatButton, setGBC(gbc_recurrence,1,6,1,1));

        // add panel to frame
        getContentPane().add(recurrencePanel, BorderLayout.EAST);
    }

    private void createSubmitButton()
    {
        submitButton = new JButton("Submit");
        JPanel navPanel = getNavPanel();
        GridBagConstraints gbc_nav = getNavGBCPanel();
        navPanel.add(submitButton, setGBC(gbc_nav, 3,1,1,1));
    }



    @Override
    void cleanUp() {

    }

    @Override
    VIEW_TYPE getEnum() {
        return view_type;
    }

    @Override
    public void update(Subject s)
    {

    }

    protected void setBBNamesFromDB(String[] BBNames)
    {
        for (String name: BBNames)
        {
            bbNameComboBox.addItem(name);
        }
    }

    protected void calcDuration()
    {
        // ################## VALUES FROM USER FORM ##################
        // start and end hour
        int startHour = (int) startHourSelector.getSelectedItem();
        int endHour = (int) endHourSelector.getSelectedItem();
        // start and end minutes
        String startMinuteString = (String) startMinSelector.getSelectedItem();
        String endMinuteString = (String) endMinSelector.getSelectedItem();

        int startMinute = parseInt(startMinuteString);
        int endMinute = parseInt(endMinuteString);

        // start and end am/pm
        String startAM_PM = (String) startAMPMSelector.getSelectedItem();
        String endAM_PM = (String) endAMPMSelector.getSelectedItem();

        // calculate the hour and minute difference
        int minDifference = endMinute - startMinute;
        int hourDifference = endHour - startHour;

        // ################## CALCULATE DURATION IN MINUTES ##################
        if (startAM_PM.equals("PM") && endAM_PM.equals("AM"))
        {
            duration = -1;
        }
        // Add +12 hours if AM -> PM
        else if (startAM_PM.equals("AM") && endAM_PM.equals("PM"))
        {
            hourDifference = hourDifference + 12;
            duration = hourDifference*60 + minDifference;
        }
        else
        {
            duration = hourDifference*60 + minDifference;
        }

        // ################## DETERMINE IF CORRECT/INCORRECT ##################
        // create error if duration is equal to or less than 0
        if (duration <= 0)
        {
            durationTimeLabel.setText("Invalid");
        }
        else
        {
            durationTimeLabel.setText(duration/60 + " hrs " + duration%60 + " mins. Total Minutes: " + duration);
        }

        // disable hourly button if duration is longer than 1 hour
        hourlyButton.setEnabled(duration < 60);

        repeatMinutesComboBox.removeAllItems();

        if (!durationTimeLabel.equals("Invalid"))
        {
            int hoursLeftInDay;
            if (endAM_PM.equals("AM"))
            {
                hoursLeftInDay = (24-endHour)*60;
            }
            else
            {
                hoursLeftInDay = (12-endHour)*60;
            }
            int endPossibleMinutes = hoursLeftInDay + (0-endMinute);

            for (int minute = (duration+1); minute <= endPossibleMinutes; minute++)
            {
                repeatMinutesComboBox.addItem(minute);
            }
        }
    }

    protected void checkAllDayButtons(boolean selectAll)
    {
        monCheckBox.setSelected(selectAll);
        tuesCheckBox.setSelected(selectAll);
        wedCheckBox.setSelected(selectAll);
        thurCheckBox.setSelected(selectAll);
        friCheckBox.setSelected(selectAll);
        satCheckBox.setSelected(selectAll);
        sunCheckBox.setSelected(selectAll);
    }

    protected void enableMinuteSelector(boolean enable)
    {
        repeatMinutesComboBox.setEnabled(enable);
        repeatMinutesLabel.setEnabled(enable);
        minutesLabel.setEnabled(enable);
    }

    protected void setMinuteLabel(int minuteRepeat)
    {
        minutesLabel.setText("Repeat every " +  minuteRepeat/60 + " hrs " + minuteRepeat%60 + " min");
        minutesLabel.setEnabled(true);
    }

    protected void showHourlyMessage()
    {
        JOptionPane.showMessageDialog(null,"HOURLY: the Billboard will repeat hourly from the Start Time until 11.59pm on the days selected.");
    }

    protected void showMinuteMessage()
    {
        JOptionPane.showMessageDialog(null,"MINUTES: the Billboard will repeat every X minutes from the Start Time until 11.59pm on the days selected.");
    }

    protected void showDailyMessage()
    {
        JOptionPane.showMessageDialog(null,"DAILY: the Billboard will repeat every day at the Start Time until the End Time.");
    }

    protected void showNoRepeatMessage()
    {
        JOptionPane.showMessageDialog(null,"NO REPETITION: the Billboard will only show at the Start Time and End Time on the days selected.");
    }


    protected int getMinuteRepeat()
    {
        if (repeatMinutesComboBox.getSelectedItem() == null)
        {
            return -1;
        }
        else
        {
            return (int)repeatMinutesComboBox.getSelectedItem();
        }
    }

    private void setDuration(int newDuration)
    {
        duration = newDuration;
    }

    protected void setValues(boolean[] daysOfWeek, int startHour, int startMin, int BBduration, String buttonSelect, int minRepeat)
    {
        // set day checkboxes based on BB
        for (int dayIndex = 0; dayIndex < daysOfWeek.length ;dayIndex++)
        {
            weekdayArray.get(dayIndex).setSelected(daysOfWeek[dayIndex]);
        }

        setDuration(BBduration);

        // set start time
        startHourSelector.getModel().setSelectedItem(startHour);
        startMinSelector.getModel().setSelectedItem(String.valueOf(startMin));

        int endHour = startHour + BBduration/60;
        int endMin = startMin +  BBduration%60;

        // set end time
        endHourSelector.getModel().setSelectedItem(endHour);
        endMinSelector.getModel().setSelectedItem(String.valueOf(endMin));

        // set radio button selection for recurrence
        switch (buttonSelect)
        {
            case "hourly":
                hourlyButton.setSelected(true);
                enableMinuteSelector(false);
                break;
            case "minute":
                repeatMinutesComboBox.getModel().setSelectedItem(minRepeat);
                minuteButton.setSelected(true);
                enableMinuteSelector(true);
                setMinuteLabel(minRepeat);
                break;
            case "daily":
                dailyButton.setSelected(true);
                enableMinuteSelector(false);
                break;
            case "no repeats":
                noRepeatButton.setSelected(true);
                enableMinuteSelector(false);
                break;
        }
    }

    protected void showConfirmationDialog()
    {
        JOptionPane.showMessageDialog(null,"You have just scheduled  " + bbNameComboBox.getSelectedItem() + " billboard.");
    }

    protected boolean checkValidDaySelected()
    {
        boolean isDaySelected = false;
        for (JCheckBox checkBox: weekdayArray)
        {
            if (checkBox.isSelected())
            {
                isDaySelected = true;
                break;
            }
        }
        return isDaySelected;
    }

    protected boolean checkValidBB()
    {
        String name = (String) bbNameComboBox.getSelectedItem();
        return !name.equals("");
    }

    protected ArrayList<Object> getScheduleInfo()
    {
        ArrayList<Object> scheduleInfo = new ArrayList();
        String name = (String) bbNameComboBox.getSelectedItem();
        int min_repeat = (int) repeatMinutesComboBox.getSelectedItem();

        scheduleInfo.add(name);
        scheduleInfo.add(min_repeat);
        return scheduleInfo;
    }

    protected boolean checkValidDuration()
    {
        return duration > 0;
    }

    protected void raiseDayError()
    {
        JOptionPane.showMessageDialog(null, "Please select at least one day");
    }

    protected void raiseBBError()
    {
        JOptionPane.showMessageDialog(null, "Please select a valid billboard");
    }

    protected void raiseDurationError()
    {
        JOptionPane.showMessageDialog(null, "Please select a valid duration");
    }

    // ############### ADD LISTENERS ###############

    protected void addDurationListener(ItemListener listener)
    {
        startAMPMSelector.addItemListener(listener);
        endAMPMSelector.addItemListener(listener);
        startMinSelector.addItemListener(listener);
        endMinSelector.addItemListener(listener);
        startHourSelector.addItemListener(listener);
        endHourSelector.addItemListener(listener);
    }

    protected void addDailyRadioButtonListener(ActionListener listener)
    {
        dailyButton.addActionListener(listener);
        hourlyButton.addActionListener(listener);
        minuteButton.addActionListener(listener);
    }

    protected void addPopulateScheduleListener(ItemListener listener)
    {
        bbNameComboBox.addItemListener(listener);
    }

    protected void addScheduleSubmitButtonListener(MouseAdapter listener)
    {
        submitButton.addMouseListener(listener);
    }

    protected void addMinuteRepeatListener(ItemListener listener)
    {
        repeatMinutesComboBox.addItemListener(listener);
    }
}
