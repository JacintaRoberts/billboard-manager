package controlPanel;

import controlPanel.Main.VIEW_TYPE;
import observer.Subject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.time.Month;
import java.util.ArrayList;

public class ScheduleUpdateView extends AbstractGenericView
{
    //*** VARIABLES**
    //--- Panels ---
    private JPanel timePanel;
    private JPanel recurrencePanel;
    //--- Labels ---
    private JLabel bbNameLabel;
    private JLabel bbNameText;
    private JLabel startTimeLabel;
    private JLabel endTimeLabel;
    private JLabel durationLabel;
    private JLabel startDateLabel;
    private JLabel endDateLabel;
    private JLabel repeatLabel;
    private JCheckBox dailyLabel;
    private JCheckBox weeklyLabel;
    private JCheckBox monthlyLabel;
    private JCheckBox yearlyLabel;
    private JComboBox<Integer> repeatCheckBox;
    private JCheckBox monLabel;
    private JCheckBox tuesLabel;
    private JCheckBox wedLabel;
    private JCheckBox thurLabel;
    private JCheckBox friLabel;
    private JCheckBox satLabel;
    private JCheckBox sunLabel;
    // --- Buttons ---
    private JButton submitButton;
    private JButton cancelButton;
    // --- Text Field ---
    private JTextField startTimeText;
    private JTextField endTimeText;
    private JTextField durationText;
    // --- Date Field ---

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
        timePanel.setLayout(new GridLayout(6,2));
        // create labels
        bbNameLabel = new JLabel("Billboard Name: ");
        bbNameText = new JLabel("");
        startTimeLabel = new JLabel("Start Time:");
        endTimeLabel = new JLabel("End Time:");
        durationLabel = new JLabel("Duration: ");
        startDateLabel = new JLabel("Start Date: ");
        endDateLabel = new JLabel("End Date: ");
        timePanel.add(bbNameLabel);
        timePanel.add(bbNameText);
        timePanel.add(startTimeLabel);
        timePanel.add(endTimeLabel);
        timePanel.add(durationLabel);
        timePanel.add(startDateLabel);
        timePanel.add(endDateLabel);
        getContentPane().add(timePanel, BorderLayout.WEST);

        // recurrence panel
        recurrencePanel = new JPanel();
        recurrencePanel.setLayout(new GridLayout(7,2));
        // create labels
        monLabel = new JCheckBox("Mon");
        tuesLabel = new JCheckBox("Tues");
        wedLabel = new JCheckBox("Wed");
        thurLabel = new JCheckBox("Thurs");
        friLabel = new JCheckBox("Fri");
        satLabel = new JCheckBox("Sat");
        sunLabel = new JCheckBox("Sun");
        // add labels to panel
        dailyLabel = new JCheckBox("Daily");
        weeklyLabel = new JCheckBox("Weekly");
        monthlyLabel = new JCheckBox("Monthly");
        yearlyLabel = new JCheckBox("Yearly");
        repeatLabel = new JLabel("Repeat every week/s: ");
        Integer[] weeklySelection = new Integer[53];
        for (int i = 1; i < 53; i++)
        {
            weeklySelection[i] = i;
        }
        repeatCheckBox = new JComboBox(weeklySelection);
        recurrencePanel.add(monLabel);
        recurrencePanel.add(tuesLabel);
        recurrencePanel.add(wedLabel);
        recurrencePanel.add(thurLabel);
        recurrencePanel.add(friLabel);
        recurrencePanel.add(satLabel);
        recurrencePanel.add(sunLabel);
        recurrencePanel.add(dailyLabel);
        recurrencePanel.add(weeklyLabel);
        recurrencePanel.add(monthlyLabel);
        recurrencePanel.add(yearlyLabel);
        recurrencePanel.add(repeatLabel);
        recurrencePanel.add(repeatCheckBox);
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
}
