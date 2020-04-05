package controlPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// views, buttons,
public class Controller
{
    private Model model;
    private ControlPanelView currentView;
    private LogInView logInView;
    private HomeView homeView;
    private BillboardsView billboardsView;
    private UsersView usersView;

    /**
     * Controller Constructor stores instances of Views, adds listeners to Views and sets up Log In View allowing users
     * to log in to the application.
     * @param model application's model
     * @param logInView application's log in screen
     * @param homeView application's homeView screen
     */
    public Controller(Model model, LogInView logInView, HomeView homeView, BillboardsView billboardsView, UsersView usersView)
    {
        // store instances of views
        this.model = model;
        this.logInView = logInView;
        this.homeView = homeView;
        this.billboardsView = billboardsView;
        this.usersView = usersView;

        // adds listeners to views
        logInView.addSubmitListener(new SubmitListener());
        homeView.addBillboardsListener(new BillboardsListener());
        homeView.addUsersListener(new UsersListener());
        billboardsView.addHomeListener(new HomeListener());
        usersView.addHomeListener(new HomeListener());

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

        // if new view is not log in screen, set welcome text
        // FIXME: catch exception. Is this the BEST WAY to handle this?
        if (currentView != logInView)
        {
            currentView.setWelcomeText(model.getUsername());
        }
    }

    /**
     * Listener to handle user's log in attempt. The username and password is retrieved from the GUI input and a
     * request is sent to server to check validity of user. If response is true, username is stored in model and user
     * is navigated to home screen. If invalid credentials, error is displayed.
     */
    private class SubmitListener extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: Submit button clicked");

            // get username and password text from GUI
            String username = logInView.getUsername();
            String password = logInView.getPassword();
            // TODO: userControl.
            boolean response = false;
            String sessionToken = "SessionToken";
            // USER CONTROL REQUEST - move this code into user control req.
//            request()
            // TODO: send log in request to server and get boolean response and session token
            if (username.equals("User") && password.equals("Password"))
            {
                response = true;
            }
            // if successful
            if (response)
            {
                // store username and session token in model
                model.storeUsername(username);
                model.storeSessionToken(sessionToken);
                // nav user to home screen
                updateView(homeView);
                // hide error string
                logInView.setErrorVisibility(false);
            }
            else
            {
                // show error message
                logInView.setErrorVisibility(true);
            }
        }
    }

    /**
     * Listener to handle Home Button mouse clicks. If user clicks the Home button, user is navigated to Home Screen.
     */
    private class HomeListener extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: Home button clicked");
            // navigate to home screen
            updateView(homeView);
        }
    }

    /**
     * Listener to handle Billboard button on Home Screen. If user clicks Billboards button, user is navigated to
     * Billboards Screen.
     */
    private class BillboardsListener extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: Billboards button clicked");
            // navigate to billboard view
            updateView(billboardsView);
        }
    }

    /**
     * Listener to handle Users button on Home Screen. If user clicks Users button, user is navigated to
     * Users Screen.
     */
    private class UsersListener extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: Users button clicked");
            // navigate to billboard view
            updateView(usersView);
        }
    }
}
