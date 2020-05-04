package controlPanel;

import controlPanel.Main.VIEW_TYPE;
import observer.Subject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.time.Month;
import java.util.ArrayList;

public class ScheduleMenuView extends AbstractGenericView
{
    // *** VARIABLES**
    // --- Panels ---
    private JPanel optionsPanel;
    // --- Buttons ---
    private JButton viewScheduleButton;
    private JButton createScheduleButton;
    // --- ENUM ---
    private VIEW_TYPE view_type;

    /**
     * Constructor to create schedule view, use parent constructor.
     */
    public ScheduleMenuView()
    {
        super("Schedule Menu View");
        view_type = VIEW_TYPE.SCHEDULE_MENU;
    }

    @Override
    VIEW_TYPE getEnum() {
        return view_type;
    }

    @Override
    void cleanUp() {

    }

    @Override
    public void update(Subject s) {

    }

    @Override
    void createComponents()
    {
        optionsPanel = new JPanel();
        optionsPanel.setLayout(new FlowLayout());
        viewScheduleButton = new JButton("View Schedule");
        createScheduleButton = new JButton("Create Schedule");
        optionsPanel.add(viewScheduleButton);
        optionsPanel.add(createScheduleButton);
        getContentPane().add(optionsPanel, BorderLayout.CENTER);
    }

    protected void addScheduleViewListener(MouseListener listener)
    {
        viewScheduleButton.addMouseListener(listener);
    }

    protected void addScheduleCreateListener(MouseListener listener)
    {
        createScheduleButton.addMouseListener(listener);
    }
}
