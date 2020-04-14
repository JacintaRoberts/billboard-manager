package controlPanel;

import controlPanel.Main.VIEW_TYPE;

import java.awt.desktop.SystemSleepEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;


public class ControllerTest
{
    private Model model;
    private HashMap<VIEW_TYPE, AbstractView> views;

    /**
     ############## CONTROLLER CONSTRUCTOR ##############
     **/

    /**
     * Controller Constructor stores instances of Views, adds listeners to Views and sets up Log In View allowing users
     * to log in to the application.
     * @param model application's model
     * @param views hashmap of views
     */
    public ControllerTest(Model model, HashMap<VIEW_TYPE, AbstractView> views)
    {
        // store instances of views
        this.model = model;
        this.views = views;

        // adds listeners to views
        addLogInListener();
        addHomeListener();
        addUserMenuListener();
        addBBMenuListener();
        addScheduleListener();
        addUserViewListener();
        addCreateBBListener();

        // set up Log In view
        setUpView(VIEW_TYPE.LOGIN);
    }

    /**
    ############## ADD LISTENERS TO VIEWS ##############
     **/
    private void addLogInListener()
    {
        LogInView logInView = (LogInView) views.get(VIEW_TYPE.LOGIN);
        logInView.addSubmitButtonListener(new SubmitButtonListener());
        views.put(VIEW_TYPE.LOGIN, logInView);
    }

    private void addHomeListener()
    {
        HomeView homeView = (HomeView) views.get(VIEW_TYPE.HOME);
        homeView.addUserMenuListener(new UserMenuButtonListener());
        homeView.addBillboardsButtonListener(new BBMenuButtonListener());
        homeView.addScheduleButtonListener(new ScheduleButtonListener());
        homeView.addViewUserButtonListener(new ProfileButtonListener());
        homeView.addBackButtonListener(new BackButtonListener());
        views.put(VIEW_TYPE.HOME, homeView);
    }

    private void addUserMenuListener()
    {
        UsersMenuView usersMenuView = (UsersMenuView) views.get(VIEW_TYPE.USERS_MENU);
        usersMenuView.addHomeButtonListener(new HomeButtonListener());
        usersMenuView.addViewUserButtonListener(new ProfileButtonListener());
        usersMenuView.addBackButtonListener(new BackButtonListener());
        usersMenuView.addListUserButtonListener(new ListUsersListener());
        views.put(VIEW_TYPE.USERS_MENU, usersMenuView);
    }

    private void addBBMenuListener()
    {
        BBMenuView bbMenuView = (BBMenuView) views.get(VIEW_TYPE.BB_MENU);
        bbMenuView.addHomeButtonListener(new HomeButtonListener());
        bbMenuView.addViewUserButtonListener(new ProfileButtonListener());
        bbMenuView.addBBCreateButtonListener(new BBCreateButtonListener());
        bbMenuView.addBackButtonListener(new BackButtonListener());
        views.put(VIEW_TYPE.BB_MENU, bbMenuView);
    }

    private void addScheduleListener()
    {
        ScheduleView scheduleView = (ScheduleView) views.get(VIEW_TYPE.SCHEDULE);
        scheduleView.addHomeButtonListener(new HomeButtonListener());
        scheduleView.addViewUserButtonListener(new ProfileButtonListener());
        scheduleView.addBackButtonListener(new BackButtonListener());
        views.put(VIEW_TYPE.SCHEDULE, scheduleView);
    }

    private void addUserViewListener()
    {
        UserView userView = (UserView) views.get(VIEW_TYPE.USER_VIEW);
        userView.addHomeButtonListener(new HomeButtonListener());
        userView.addBackButtonListener(new BackButtonListener());
        views.put(VIEW_TYPE.USER_VIEW, userView);
    }

    private void addCreateBBListener()
    {
        BBCreateView bbCreateView = (BBCreateView) views.get(VIEW_TYPE.CREATE_BB);
        bbCreateView.addHomeButtonListener(new HomeButtonListener());
        bbCreateView.addBackButtonListener(new BackButtonListener());
        views.put(VIEW_TYPE.CREATE_BB, bbCreateView);
    }

    /**
     ############## MANAGE VIEWS' SETUP ##############
     **/

    private void setUpView(VIEW_TYPE view_type)
    {
        AbstractView view = views.get(view_type);
        model.attachObserver(view);
        model.setCurrentView(view.getEnum());
        views.put(view_type, view);
        view.setVisible(true);
    }

    /**
     * Change user's view by hiding the old view and showing the new.
     * Detach observer from old view and attach observer to new.
     * @param newView the user's new view
     */
    private void updateView(VIEW_TYPE newView)
    {
        // detach observer & hide old view
        AbstractView oldView = views.get(model.getCurrentView());
        model.detachObserver(oldView);
        oldView.setVisible(false);
        views.put(oldView.getEnum(), oldView);

        // update components based on permissions
        updateComponents(newView);

        // set up new frame
        setUpView(newView);
    }

    /**
     * Designed to check Access Permissions in order to hide/show components of the Frame.
     * @param key
     */
    private void updateComponents(VIEW_TYPE key)
    {
        switch(key)
        {
            case HOME:
                System.out.println("Check Home permission");
                System.out.println("Set Username Welcome Text");
                HomeView homeView = (HomeView) views.get(VIEW_TYPE.HOME);
                homeView.setWelcomeText(model.getUsername());
                // FIXME: this is where Requests would be made to the Server
                // FIXME: checkPermission(model.getUsername(), 2, model.getSessionToken());
                homeView.usersButton.setVisible(true);
                // TODO: at the end - store updated version of view in HashMap
                break;
            case LOGIN:
                System.out.println("Check LogIn permission");
                break;
            case USER_VIEW:
                // hide or show Edit button depending on permission
        }
    }

    /**
     ############## DEFINE LISTENERS FOR COMPONENTS ##############
     **/

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

            // get LOGIN GUI
            LogInView logInView = (LogInView) views.get(VIEW_TYPE.LOGIN);

            // get username and password text from GUI
            String username = logInView.getUsername();
            String password = logInView.getPassword();

            // TODO: String[] response = userControl.loginRequest(username, Password). response[0], response[1]; This could either be error message or session token
            boolean response = false;
            String sessionToken = "SessionToken";

            if (username.equals("Team60") && password.equals("HelloWorld"))
            {
                response = true;
            }

            // if successful
            if (response)
            {
                System.out.println("CONTROLLER LEVEL - Correct Credentials");
                // store username and session token in model
                model.storeUsername(username);
                model.storeSessionToken(sessionToken);

                // hide error string
                logInView.setErrorVisibility(false);
                views.put(VIEW_TYPE.LOGIN, logInView);

                // nav user to home screen
                updateView(VIEW_TYPE.HOME);
            }
            else
            {
                System.out.println("CONTROLLER LEVEL - Incorrect Credentials");
                // show error message
                logInView.setErrorVisibility(true);
                views.put(VIEW_TYPE.LOGIN, logInView);
            }
        }
    }

    /**
     * Listener to handle Home Button mouse clicks.
     * If user clicks the Home button, user is navigated to Home Screen.
     */
    private class HomeButtonListener extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: Home button clicked");
            // navigate to home screen
            updateView(VIEW_TYPE.HOME);
        }
    }

    /**
     * Listener to handle Back Button mouse clicks.
     * If user clicks the Back button, user is navigated to previous screen.
     */
    private class BackButtonListener extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: Back button clicked to go to " + model.getPreviousView());
            // navigate to previous screen
            updateView(model.getPreviousView());
        }
    }

    /**
     * Listener to handle UserMenu Button mouse clicks.
     * If user clicks the UserMenu button, user is navigated to User Menu view.
     */
    private class UserMenuButtonListener extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: UserMenu button clicked");
            // navigate to home screen
            updateView(VIEW_TYPE.USERS_MENU);
        }
    }

    /**
     * Listener to handle BBMenu Button mouse clicks.
     * If user clicks the BBMenu button, user is navigated to BB Menu view.
     */
    private class BBMenuButtonListener extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: BBMenu button clicked");
            // navigate to home screen
            updateView(VIEW_TYPE.BB_MENU);
        }
    }

    /**
     * Listener to handle Schedule Button mouse clicks.
     * If user clicks the Schedule button, user is navigated to Schedule view.
     */
    private class ScheduleButtonListener extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: Schedule button clicked");
            // navigate to home screen
            updateView(VIEW_TYPE.SCHEDULE);
        }
    }

    /**
     * Listener to handle Profile Button mouse clicks.
     * If user clicks the Profile button, user is navigated to Profile view.
     */
    private class ProfileButtonListener extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: Profile button clicked");

            // get LOGIN GUI
            UserView userView = (UserView) views.get(VIEW_TYPE.USER_VIEW);

            // set username and password text on GUI
            // FIXME: get password and user permissions from Control/Server
            userView.setUsername(model.getUsername());
            userView.setPasswordText("Password");
            userView.setUserPermissions("Edit All Users");
            views.put(VIEW_TYPE.USER_VIEW, userView);

            // navigate to home screen
            updateView(VIEW_TYPE.USER_VIEW);
        }
    }

    /**
     * Listener to handle BB Create Button mouse clicks.
     */
    private class BBCreateButtonListener extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: BB Create button clicked");
            // navigate to home screen
            updateView(VIEW_TYPE.CREATE_BB);
        }
    }

    /**
     * Listener to handle List Users mouse clicks.
     */
    private class ListUsersListener extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: List Users button clicked");

            // get LIST USER view
            UserListView userListView = (UserListView) views.get(VIEW_TYPE.USER_LIST);
            String[] stringArray = {"Alan","Kanu", "Jacinta", "Patrice"};
            userListView.addContent(stringArray);
            views.put(VIEW_TYPE.USER_LIST, userListView);

            // navigate to home screen
            updateView(VIEW_TYPE.USER_LIST);
        }
    }
}
