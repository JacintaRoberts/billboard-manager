package controlPanel;

import controlPanel.Main.VIEW_TYPE;
import observer.Subject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Time;
import java.util.ArrayList;

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
    private JRadioButton noRepeatButton;
    private JButton submitButton;
    private JButton clearScheduleButton;

    // --- Group ---
    private ButtonGroup group;

    // --- GBC ---
    private GridBagConstraints gbc_timeName;
    private GridBagConstraints gbc_days;
    private GridBagConstraints gbc_recurrence;

    // --- MISC ----
    private ArrayList<JCheckBox> weekdayArray;
    private int duration;

    // --- ENUM ---
    private VIEW_TYPE view_type;

    /**
     * Constructor to create/update schedule view, use parent constructor.
     */
    public ScheduleUpdateView()
    {
        super("Schedule Update");
        view_type = VIEW_TYPE.SCHEDULE_UPDATE;
    }

    @Override
    void createComponents()
    {
        createPanels();
        createAdditionalButtons();
    }

    /**
     * Time Panel contains BB Name, Start and End Time and Duration Fields.
     * Weekday Panel contains days of week selection.
     * Recurrence Panel contains Hourly, Minutes or No Repetition options.
     */
    private void createPanels()
    {
        // create time/name panel
        timeNamePanel = new JPanel();
        timeNamePanel.setLayout(new GridBagLayout());
        // create labels
        timePanelDescription = new JLabel("BILLBOARD DETAILS:");
        timePanelDescription.setForeground(Color.WHITE);
        bbNameLabel = new JLabel("Billboard Name: ");
        bbNameComboBox = new JComboBox<>();
        // define hours and minute selection
        Integer[] hour = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        String[] min = new String[60];
        for (int minute = 0; minute < 60; minute++)
        {
            // add minutes to array, pad single digits with a 0
            min[minute] = String.format("%02d",minute);
        }
        // define AM and PM
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
        // create end time panel
        endTimePanel = new JPanel();
        endTimePanel.setLayout(new FlowLayout());
        // hour & min selector
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

        // -------------- ADD ITEMS TO TIME PANEL ------------------
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

        // add labels to array list - allowing code to check if selected
        weekdayArray = new ArrayList<>();
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

        // create panel and set layout
        recurrencePanel = new JPanel();
        recurrencePanel.setLayout(new GridLayout(7,1));

        // create label for top of panel
        recurrencePanelDescription = new JLabel("SELECT RECURRENCE:");
        recurrencePanelDescription.setForeground(Color.WHITE);

        // create radio buttons
        hourlyButton = new JRadioButton("Hourly");
        minuteButton = new JRadioButton("Minutes");
        noRepeatButton = new JRadioButton("No Repetition");

        // set name of radio buttons - used for event handling
        hourlyButton.setName("hourly");
        minuteButton.setName("minute");
        noRepeatButton.setName("no repeats");

        // add buttons to group such that only 1 can be selected
        group = new ButtonGroup();
        group.add(hourlyButton);
        group.add(minuteButton);
        group.add(noRepeatButton);

        // create panel to contain minute selection
        repeatMinutesPanel = new JPanel();
        repeatMinutesPanel.setLayout(new GridLayout(3,1));
        repeatMinutesComboBox = new JComboBox(new Integer[]{});

        // create labels for minutes panel
        repeatMinutesLabel = new JLabel("Repeat every X minute/s: ");
        minutesLabel = new JLabel("");

        // disable minute labels and combobox
        enableMinuteSelector(false);

        // add labels and combo box to panel
        repeatMinutesPanel.add(repeatMinutesLabel);
        repeatMinutesPanel.add(repeatMinutesComboBox);
        repeatMinutesPanel.add(minutesLabel);

        // add items to panel
        gbc_recurrence = new GridBagConstraints();
        gbc_recurrence.insets = new Insets(30,10,30,10);
        recurrencePanel.add(recurrencePanelDescription, setGBC(gbc_recurrence,1,1,1,1));
        recurrencePanel.add(hourlyButton, setGBC(gbc_recurrence,1,2,1,1));
        recurrencePanel.add(minuteButton, setGBC(gbc_recurrence,1,3,1,1));
        recurrencePanel.add(repeatMinutesPanel, setGBC(gbc_recurrence,4,1,1,1));
        recurrencePanel.add(noRepeatButton, setGBC(gbc_recurrence,1,5,1,1));

        // add panel to frame
        getContentPane().add(recurrencePanel, BorderLayout.EAST);
    }

    /**
     * Add a submit and delete schedule button to the Navigation Panel
     */
    private void createAdditionalButtons()
    {
        submitButton = new JButton("Submit");
        clearScheduleButton = new JButton("Clear Schedule");
        JPanel navPanel = getNavPanel();
        GridBagConstraints gbc_nav = getNavGBCPanel();
        navPanel.add(submitButton, setGBC(gbc_nav, 3,1,1,1));
        navPanel.add(clearScheduleButton, setGBC(gbc_nav, 4,1,1,1));
    }

    // ####################### CLEANUP, ENUM & UPDATE #######################

    @Override
    void cleanUp()
    {
        // remove all names as these will be re-populated upon entering screen
        bbNameComboBox.removeAllItems();
        removeScheduleSelection();
    }

    protected void removeScheduleSelection()
    {
        // deselect all weekday checkboxes
        for (JCheckBox day : weekdayArray)
        {
            day.setSelected(false);
        }

        // deselect all recurrence radio buttons
        hourlyButton.setSelected(true);
        minuteButton.setSelected(false);
        noRepeatButton.setSelected(false);

        // remove all minute values from combobox
        repeatMinutesComboBox.removeAllItems();

        // reset duration label
        durationTimeLabel.setText("0 minutes");

        // reset minute repeat label
        minutesLabel.setText("");

        // reset time selectors
        endHourSelector.setSelectedItem(1);
        endMinSelector.setSelectedItem("00");
        startHourSelector.setSelectedItem(1);
        startMinSelector.setSelectedItem("00");
        startAMPMSelector.setSelectedItem("AM");
        endAMPMSelector.setSelectedItem("AM");
    }

    @Override
    VIEW_TYPE getEnum() {
        return view_type;
    }

    @Override
    public void update(Subject s)
    {

    }

    // ####################### VALIDATE INPUT #######################

    /**
     * Upon Submitting Schedule, ensure that at least one day has been selected
     */
    protected boolean checkValidDaySelected()
    {
        boolean isDaySelected = false;
        // looping thru checkbox state to update boolean value to true if at least one day is selected
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

    /**
     * Upon submitting Schedule, ensure user has selected a BB name
     * @return true if BB has been selected, false if no BB has been selected
     */
    protected boolean checkValidBB()
    {
        String name = (String) bbNameComboBox.getSelectedItem();
        // return true if name is not empty
        return !name.equals("");
    }

    /**
     * Upon submitting schedule, check that the duration is bigger than 0
     * @return true if valid duration, false if invalid duration
     */
    protected boolean checkValidDuration()
    {
        return duration > 0;
    }

    // ####################### POP-UP MESSAGE #######################

    /**
     * Upon entering the Schedule Update view, the user will be instructed to select a BB name to schedule.
     */
    protected void showInstructionMessage()
    {
        JOptionPane.showMessageDialog(null,"Please Select your Billboard to Schedule.");
    }

    /**
     * Upon clicking the Hourly radio button, the user will be informed on the workings of the hour button
     */
    protected void showHourlyMessage()
    {
        JOptionPane.showMessageDialog(null,"HOURLY: the Billboard will repeat hourly from the Start Time until 11.59pm on the days selected.");
    }

    /**
     * Upon clicking the Minute radio button, the user will be informed on the workings of the minute button
     */
    protected void showMinuteMessage() {
        JOptionPane.showMessageDialog(null, "MINUTES: the Billboard will repeat every X minutes from the Start Time until 11.59pm on the days selected.");
    }

    /**
     * Upon clicking the No Repeat radio button, the user be informed on the workings of the No Repeat button
     */
    protected void showNoRepeatMessage()
    {
        JOptionPane.showMessageDialog(null,"NO REPETITION: the Billboard will only show at the Start Time and End Time on the days selected.");
    }

    /**
     * Show user confirmation of successfully scheduling a billboard
     */
    protected void showConfirmationDialog()
    {
        JOptionPane.showMessageDialog(null,"You have just scheduled  " + bbNameComboBox.getSelectedItem() + " billboard.");
    }

    /**
     * Show Ask User for confirmation of schedule creation
     * @return response of user (int)
     */
    protected int showConfirmationCreateSchedule()
    {
        String message = "Are you sure you want to publish the Schedule?";
        return JOptionPane.showConfirmDialog(null, message);
    }

    /**
     * Upon submitting schedule, raise error if at least one day is not selected
     */
    protected void raiseDayError()
    {
        JOptionPane.showMessageDialog(null, "Please select at least one day");
    }

    /**
     * Upon submitting schedule, raise error if a BB has not been selected
     */
    protected void raiseBBError()
    {
        JOptionPane.showMessageDialog(null, "Please select a valid billboard");
    }

    /**
     * Upon submitting schedule, raise error if a valid duration has not been selected
     */
    protected void raiseDurationError()
    {
        JOptionPane.showMessageDialog(null, "Please select a valid duration");
    }

    /**
     * Upon selecting a BB that does not currently have a schedule, alert user of this through a message dialog
     */
    protected void showNoExistingScheduleMessage()
    {
        JOptionPane.showMessageDialog(null, "Please note: No schedule currently exists for this Billboard.");
    }

    /**
     * Upon attempting to clear schedule, ask user to confirm schedule removal
     * @return
     */
    protected int showScheduleClearConfirmation()
    {
        int result =  JOptionPane.showConfirmDialog(null, "Are you sure you want to clear the Schedule? Please note, an existing Schedule for this Billboard will be removed from the database.");
        return result;
    }

    protected void showScheduleErrorMessage()
    {
        JOptionPane.showMessageDialog(null, "Error occurred");
    }

    // ####################### SET SCHEDULE INFO ON GUI #######################

    /**
     * Set values from the server on the GUI. These values include: the selected days, start hour, start min,
     * duration, button selected, minute repetition
     */
    protected void setScheduleValues(ArrayList<Boolean> selectedDays, int startHour, int startMin, int BBduration, String buttonSelected, int minRepeat)
    {
        // ------------- SELECTED DAYS -------------
        // set selected days on the GUI
        for (int dayIndex = 0; dayIndex < selectedDays.size() ;dayIndex++)
        {
            System.out.println(selectedDays.get(dayIndex));
            weekdayArray.get(dayIndex).setSelected(selectedDays.get(dayIndex));
        }

        // ------------- DURATION -------------
        // set duration of the BB
        setDuration(BBduration);

        // ------------- TIMES -------------
        // set start time
        startHourSelector.getModel().setSelectedItem(startHour);
        startMinSelector.getModel().setSelectedItem(String.valueOf(startMin));

        // define end time
        int endHour = startHour + BBduration/60;
        int endMin = startMin +  BBduration%60;

        // set end time
        endHourSelector.getModel().setSelectedItem(endHour);
        endMinSelector.getModel().setSelectedItem(String.valueOf(endMin));

        // ------------- RECURRENCE -------------
        // set radio button selection for recurrence
        switch (buttonSelected)
        {
            case "hourly":
                // set radio button to selected
                hourlyButton.setSelected(true);
                // disable minute selector
                enableMinuteSelector(false);
                break;
            case "minute":
                // set minute value in combobox
                repeatMinutesComboBox.getModel().setSelectedItem(minRepeat);
                // set radio button to selected
                minuteButton.setSelected(true);
                // enable all labels
                enableMinuteSelector(true);
                // set minute label to provide more info
                setMinuteLabel(minRepeat);
                break;
            case "no repeats":
                // set radio button to selected
                noRepeatButton.setSelected(true);
                // disable radio button
                enableMinuteSelector(false);
                break;
        }
    }

    /**
     * Set the duration of the BB
     */
    protected void setBBSelected(String bbName)
    {
        bbNameComboBox.setSelectedItem(bbName);
    }

    /**
     * Set the duration of the BB
     */
    private void setDuration(int newDuration)
    {
        duration = newDuration;
    }

    /**
     * Set Minute Label to reflect the number of hours and minutes the bb will be repeated
     * @param minuteRepeat number of minutes the bb will be shown
     */
    protected void setMinuteLabel(int minuteRepeat)
    {
        minutesLabel.setText("Repeat every " +  minuteRepeat/60 + " hrs " + minuteRepeat%60 + " min");
        minutesLabel.setEnabled(true);
    }

    /**
     * Add BB Names to the ComboBox Selector such that users are able to select their Billboard to schedule.
     * @param BBNames string array of all billboard names
     */
    protected void setBBNamesFromDB(ArrayList<String> BBNames)
    {
        // add each billboard name to the combobox selector
        for (String name: BBNames)
        {
            bbNameComboBox.addItem(name);
        }
    }

    /**
     * Enable or Disable Minute Selector
     * @param enable boolean value whereby false = disable and true = enable
     */
    protected void enableMinuteSelector(boolean enable)
    {
        repeatMinutesComboBox.setEnabled(enable);
        repeatMinutesLabel.setEnabled(enable);
        minutesLabel.setEnabled(enable);
    }

    /**
     * Calculate the Billboard duration based on the start and end time selected. Inform user when invalid time is selected.
     */
    protected void calcDuration()
    {
        // ################## USER INPUT ##################
        // get user input: start and end hour
        int startHour = (int) startHourSelector.getSelectedItem();
        int endHour = (int) endHourSelector.getSelectedItem();
        // get user input: start and end minutes
        int startMinute = parseInt((String) startMinSelector.getSelectedItem());
        int endMinute = parseInt((String) endMinSelector.getSelectedItem());
        // get user input: start and end am/pm
        String startAM_PM = (String) startAMPMSelector.getSelectedItem();
        String endAM_PM = (String) endAMPMSelector.getSelectedItem();

        // calculate the hour and minute difference
        int minDifference = endMinute - startMinute;
        int hourDifference = endHour - startHour;

        // ################## CALCULATE DURATION IN MINUTES ##################
        // if end time is before start time - set duration to -1 to indicate invalid
        if (startAM_PM.equals("PM") && endAM_PM.equals("AM"))
        {
            duration = -1;
        }
        // calculate duration in minutes by adding +12 hours if AM -> PM and transforming to minutes (multiply by 60)
        else if (startAM_PM.equals("AM") && endAM_PM.equals("PM"))
        {
            hourDifference = hourDifference + 12;
            duration = hourDifference*60 + minDifference;
        }
        // calculate duration in minutes by transforming hourDifference to minutes (multiply by 60)
        else
        {
            duration = hourDifference*60 + minDifference;
        }

        // ################## MANAGE VALID/INVALID DURATION ##################
        // create error if duration is equal to or less than 0, disable minute selector
        if (duration <= 0)
        {
            System.out.println("Set duration to invalid");
            durationTimeLabel.setText("Invalid");
            enableMinuteSelector(false);
            minutesLabel.setText("Invalid Time selected.");
        }
        // set duration label if valid time, enable minute selector
        else
        {
            System.out.println("Set duration to correct time");
            durationTimeLabel.setText(duration/60 + " hrs " + duration%60 + " mins. Total Minutes: " + duration);
            enableMinuteSelector(true);
        }

        // ################## MANAGE HOURLY SELECTION ##################
        // disable hourly button if duration is longer than 1 hour
        hourlyButton.setEnabled(duration < 60);

        // ################ ## MANAGE MINUTES SELECTION ##################
        repeatMinutesComboBox.removeAllItems();
        if (!durationTimeLabel.getText().equals("Invalid"))
        {
            int minutesLeftInDay;
            // calculate the number of minutes left in the day after the end time of the billboard
            if (endAM_PM.equals("AM"))
            {
                minutesLeftInDay = (24-endHour)*60 + (0-endMinute);
            }
            else
            {
                minutesLeftInDay = (12-endHour)*60 + (0-endMinute);
            }
            // add list of possible minute repetitions into the combobox
            for (int minute = (duration + 1); minute <= minutesLeftInDay; minute++)
            {
                repeatMinutesComboBox.addItem(minute);
            }
        }
    }

    // ####################### GET SCHEDULE INFO FROM SERVER #######################

    /**
     * Upon successfully submitting Schedule, get the schedule information from the user inputs and return an Object type
     * Array list.
     * @return schedule info array
     */
    protected ArrayList<Object> getScheduleInfo()
    {
        ArrayList<Object> scheduleInfo = new ArrayList();

        // --- GET USER INPUT ---
        // get BB name
        String name = getSelectedBBName();
        // get times selected
        Integer startHour = (Integer)startHourSelector.getSelectedItem();
        String startMin = (String)startMinSelector.getSelectedItem();

        // selected days
        ArrayList<Boolean> daysOfWeek = new ArrayList<>();
        daysOfWeek.add(monCheckBox.isSelected());
        daysOfWeek.add(tuesCheckBox.isSelected());
        daysOfWeek.add(wedCheckBox.isSelected());
        daysOfWeek.add(thurCheckBox.isSelected());
        daysOfWeek.add(friCheckBox.isSelected());
        daysOfWeek.add(satCheckBox.isSelected());
        daysOfWeek.add(sunCheckBox.isSelected());

        String recurrenceButton = "";
        Integer min_repeat = null;
        // get recurrence selection
        if(hourlyButton.isSelected())
        {
            recurrenceButton = "hourly";
        }
        if (minuteButton.isSelected())
        {
            recurrenceButton = "minute";
            // get minute repeat (this may be empty)
            min_repeat = (Integer) repeatMinutesComboBox.getSelectedItem();
        }
        if (noRepeatButton.isSelected())
        {
            recurrenceButton = "no repeats";
        }

        // --- ADD INPUT TO ARRAY ---
        // add info to the array
        scheduleInfo.add(name);
        scheduleInfo.add(daysOfWeek);
        scheduleInfo.add(startHour);
        scheduleInfo.add(startMin);
        scheduleInfo.add(duration);
        scheduleInfo.add(recurrenceButton);
        scheduleInfo.add(min_repeat);

        // return schedule information
        return scheduleInfo;
    }

    protected String getSelectedBBName()
    {
        return (String) bbNameComboBox.getSelectedItem();
    }

    /**
     * Get the number of minutes the bb will be repeatedly scheduled.
     * Returning -1 if invalid, or the valid minute value in int format
     */
    protected int getMinuteRepeat()
    {
        // if nothing has been selected, return an invalid minute number -1
        if (repeatMinutesComboBox.getSelectedItem() == null)
        {
            return -1;
        }
        // return selected minutes if valid
        else
        {
            return (int)repeatMinutesComboBox.getSelectedItem();
        }
    }

    // ############### ADD LISTENERS ###############

    /**
     * Add item listener to schedule time selectors
     * @param listener item listener designed to calculate the new duration each time one of these is selected.
     */
    protected void addScheduleTimeListener(ItemListener listener)
    {
        startAMPMSelector.addItemListener(listener);
        endAMPMSelector.addItemListener(listener);
        startMinSelector.addItemListener(listener);
        endMinSelector.addItemListener(listener);
        startHourSelector.addItemListener(listener);
        endHourSelector.addItemListener(listener);
    }

    /**
     * Add action listener for radio buttons
     * @param listener action listener designed for radio buttons
     */
    protected void addRadioButtonListener(ActionListener listener)
    {
        hourlyButton.addActionListener(listener);
        minuteButton.addActionListener(listener);
        noRepeatButton.addActionListener(listener);
    }

    /**
     * Add action listener for combobox bb name selector which is designed to populate the schedule
     * @param listener action listener designed for bb name combobox
     */
    protected void addPopulateScheduleListener(ItemListener listener)
    {
        bbNameComboBox.addItemListener(listener);
    }

    /**
     * Add schedule submit listener which is designed to validate user input and send data to server if successful
     * @param listener action listener designed for submit button
     */
    protected void addScheduleSubmitButtonListener(MouseAdapter listener)
    {
        submitButton.addMouseListener(listener);
    }

    /**
     * Add minute repeat combobox listener which is designed to set minute label for the user
     * @param listener action listener designed for repeat minute selector
     */
    protected void addMinuteRepeatListener(ItemListener listener)
    {
        repeatMinutesComboBox.addItemListener(listener);
    }

    /**
     * Add schedule clear button which is designed to delete the schedule from the database
     * @param listener action listener designed for the clear schedule button
     */
    protected void addScheduleClearButtonListener(MouseAdapter listener)
    {
        clearScheduleButton.addMouseListener(listener);
    }
}
