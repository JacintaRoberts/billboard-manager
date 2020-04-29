package controlPanel;

import observer.Subject;

import javax.swing.*;
import controlPanel.Main.VIEW_TYPE;

import java.awt.*;
import java.awt.event.MouseListener;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;

public class ScheduleMonthlyView extends AbstractGenericView
{
    //*** VARIABLES**
    //--- Panels ---
    private JPanel calendarPanel;
    private JPanel monthPanel;
    //--- Labels ---
    private JLabel monLabel;
    private JLabel tuesLabel;
    private JLabel wedLabel;
    private JLabel thurLabel;
    private JLabel friLabel;
    private JLabel satLabel;
    private JLabel sunLabel;
    private JLabel yearLabel;
    // --- Buttons ---
    private ArrayList<JButton> buttonArray;
    // --- Combo Box ---
    private JComboBox<Month> monthComboBox;
    // --- Text Field ---
    private JTextField yearText;
    // --- ENUM ---
    private VIEW_TYPE view_type;
    // --- MISC ---
    private int days_in_month = 31;


    /**
     * Constructor to create schedule view, use parent constructor.
     */
    public ScheduleMonthlyView()
    {
        super("Schedule View");
        view_type = VIEW_TYPE.SCHEDULE_MONTH;
    }

    @Override
    void createComponents()
    {
        // calendar panel
        calendarPanel = new JPanel();
        calendarPanel.setLayout(new GridLayout(6,7));
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
        // create and fill button array
        buttonArray = new ArrayList<>();
        for (int i = 1 ; i <= 31; i++)
        {
            JButton dayButton = new JButton(String.valueOf(i));
            dayButton.setName(String.valueOf(i));
            calendarPanel.add(dayButton);
            buttonArray.add(dayButton);
        }
        getContentPane().add(calendarPanel, BorderLayout.CENTER);
        // month panel
        monthPanel = new JPanel();
        monthPanel.setLayout(new GridLayout(6, 1));
        monthComboBox = new JComboBox<>(Month.values());
        yearLabel = new JLabel("Year:");
        yearText = new JTextField();
        monthPanel.add(monthComboBox);
        monthPanel.add(yearLabel);
        monthPanel.add(yearText);
        getContentPane().add(monthPanel, BorderLayout.WEST);
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

    /**
     * Add listener to handle mouse click of submit button.
     * @param listener mouse click listener
     */
    public void addDayButtonListener(MouseListener listener)
    {
        for (JButton dayButton : buttonArray)
        {
            dayButton.addMouseListener(listener);
        }
    }
}
