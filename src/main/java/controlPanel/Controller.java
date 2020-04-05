package controlPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller
{
    private Model model;
    private ControlPanelView currentView;
    private LogInView logInView;
    private HomeView homeView;

    /**
     * Controller Constructor stores instances of Views, adds listeners to Views and sets up Log In View allowing users
     * to log in to the application.
     * @param model application's model
     * @param logInView application's log in screen
     * @param homeView application's homeView screen
     */
    public Controller(Model model, LogInView logInView, HomeView homeView)
    {
        // store instances of views
        this.model = model;
        this.logInView = logInView;
        this.homeView = homeView;

        // adds listeners to views
        logInView.addSubmitListener(new SubmitListener());
        homeView.addHomeListener(new HomeListener());

        // set up log in frame
        model.attachObserver(logInView);
        logInView.setVisible(true);
        this.currentView = logInView;
    }

    /**
     * Change user's view by hiding the old view and showing the new.
     * Detach observer from old view and attach observer to new.
     * @param newView the user's new view
     */
    private void updateView(ControlPanelView newView)
    {
        // detach observer & hide old view
        model.detachObserver(currentView);
        currentView.setVisible(false);

        // redefine view
        this.currentView = newView;

        // set new view to be visible
        currentView.setVisible(true);

        // attach observer to new view
        model.attachObserver(newView);
    }

    private class SubmitListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            // get username and password text from GUI
            String username = logInView.getUsername();
            String password = logInView.getPassword();
            boolean response = false;
            // TODO: send log in request to server and get boolean response
            if (username.equals("Patrice") && password.equals("Password"))
            {
                response = true;
            }
            // if successful
            if (response)
            {
                // store username in model
                model.submitLogin(username);
                updateView(homeView);
            }
            else
            {
                // show error message
                logInView.setErrorVisibility(true);
            }
        }
    }

    private class HomeListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            model.updateModel();
        }
    }
}
