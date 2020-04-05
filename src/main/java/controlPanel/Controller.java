package controlPanel;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Controller
{
    private Model model;
    private ControlPanelView currentView;
    private LogInView logInView;
    public HomeView homeView;
    private BillboardsView billboardsView;
    private UsersView usersView;
    private CreateBillboardView createBillboardView;

    /**
     * Controller Constructor stores instances of Views, adds listeners to Views and sets up Log In View allowing users
     * to log in to the application.
     * @param model application's model
     * @param logInView application's log in screen
     * @param homeView application's homeView screen
     */
    public Controller(Model model, LogInView logInView, HomeView homeView, BillboardsView billboardsView, UsersView usersView, CreateBillboardView createBillboardView)
    {
        // store instances of views
        this.model = model;
        this.logInView = logInView;
        this.homeView = homeView;
        this.billboardsView = billboardsView;
        this.usersView = usersView;
        this.createBillboardView = createBillboardView;

        // adds listeners to views
        addHomeListener();
        addUsersListener();
        addBillboardListener();
        addLogInListener();

        // set up log in frame
        model.attachObserver(logInView);
        logInView.setVisible(true);
        this.currentView = logInView;
    }

    private void addHomeListener()
    {
        homeView.addBillboardsButtonListener(new BillboardsButtonListener());
        homeView.addUsersButtonListener(new UsersButtonListener());
    }

    private void addUsersListener()
    {
        usersView.addHomeButtonListener(new HomeButtonListener());
    }

    private void addBillboardListener()
    {
        billboardsView.addHomeButtonListener(new HomeButtonListener());
        billboardsView.addCreateButtonListener(new CreateButtonListener());
    }

    private void addLogInListener()
    {
        logInView.addSubmitButtonListener(new SubmitButtonListener());
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
    private class SubmitButtonListener extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: Submit button clicked");

            // get username and password text from GUI
            String username = logInView.getUsername();
            String password = logInView.getPassword();
            // TODO: String[] response = userControl.loginRequest(username, Password)
            // String valid = response[0];
            // String responseMessage = response[1]; This could either be error message or session token
            boolean response = false;
            String sessionToken = "SessionToken";
            if ((username.equals("User") && password.equals("Password")) || (username.equals("Team60") && password.equals("Password")))
            {
                response = true;
            }

            // if successful
            if (response)
            {
                // store username and session token in model
                model.storeUsername(username);
                model.storeSessionToken(sessionToken);

                // **************************************
                // ** CHECK PERMISSIONS **
                // check permissions
                // TODO: implement this is Control
                boolean editUsersPermission;
//            boolean editUsersPermission = checkPermission(model.getUsername(), 2, model.getSessionToken());
                if(model.getUsername().equals("Team60"))
                {
                    editUsersPermission = true;
                }
                else
                {
                    editUsersPermission = false;
                }

                // set visibility of users button
                if (editUsersPermission)
                {
                    homeView.usersButton.setVisible(true);
                    System.out.println("SHOW");
                }
                else
                {
                    homeView.usersButton.setVisible(false);
                    System.out.println("HIDE");
                }
                // **************************************

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
    private class HomeButtonListener extends MouseAdapter
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
    private class BillboardsButtonListener extends MouseAdapter
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
    private class UsersButtonListener extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: Users button clicked");
            // navigate to billboard view
            updateView(usersView);
        }
    }

    private class CreateButtonListener extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: Create button clicked");
            // navigate to billboard view
            updateView(createBillboardView);
        }
    }
}
