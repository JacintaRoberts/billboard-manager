package controlPanel;

import controlPanel.Main.VIEW_TYPE;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import static controlPanel.Main.VIEW_TYPE.*;


public class Controller
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
    public Controller(Model model, HashMap<VIEW_TYPE, AbstractView> views)
    {
        // store instances of views
        this.model = model;
        this.views = views;

        // adds listeners to views
        addLogInListener();
        addHomeListener();

        addScheduleListener();

        addUserMenuListener();
        addUserViewListener();
        addUserListListener();
        addUserEditListener();

        addBBMenuListener();
        addBBCreateListener();
        addBBListListener();

        addEditContentListener();

        // set up Log In view
        setUpView(LOGIN);
    }

    /**
    ############## ADD LISTENERS TO VIEWS ##############
     **/
    private void addLogInListener()
    {
        LogInView logInView = (LogInView) views.get(LOGIN);
        logInView.addSubmitButtonListener(new SubmitButtonListener());
        views.put(LOGIN, logInView);
    }

    private void addHomeListener()
    {
        HomeView homeView = (HomeView) views.get(HOME);
        homeView.addUserMenuListener(new UserMenuButtonListener());
        homeView.addBillboardsButtonListener(new BBMenuButtonListener());
        homeView.addScheduleButtonListener(new ScheduleButtonListener());
        homeView.addViewUserButtonListener(new ProfileButtonListener());
        homeView.addBackButtonListener(new BackButtonListener());
        views.put(HOME, homeView);
    }

    private void addUserMenuListener()
    {
        addGenericListeners(USERS_MENU);
        UsersMenuView usersMenuView = (UsersMenuView) views.get(USERS_MENU);
        usersMenuView.addListUserButtonListener(new ListUsersListener());
        views.put(USERS_MENU, usersMenuView);
    }

    private void addEditContentListener()
    {
        addGenericListeners(USER_EDIT);
        UserEditView userEditView = (UserEditView) views.get(USER_EDIT);
        views.put(USER_EDIT, userEditView);
    }

    private void addBBMenuListener()
    {
        addGenericListeners(BB_MENU);
        BBMenuView bbMenuView = (BBMenuView) views.get(BB_MENU);
        bbMenuView.addBBCreateButtonListener(new BBCreateButtonListener());
        bbMenuView.addBBListListener(new ListBBListener());
        views.put(BB_MENU, bbMenuView);
    }

    private void addScheduleListener()
    {
        addGenericListeners(SCHEDULE);
        ScheduleView scheduleView = (ScheduleView) views.get(SCHEDULE);
        views.put(SCHEDULE, scheduleView);
    }

    private void addUserViewListener()
    {
        UserView userView = (UserView) views.get(USER_VIEW);
        userView.addHomeButtonListener(new HomeButtonListener());
        userView.addBackButtonListener(new BackButtonListener());
        views.put(USER_VIEW, userView);
    }

    private void addBBCreateListener()
    {
        addGenericListeners(BB_CREATE);
        BBCreateView bbCreateView = (BBCreateView) views.get(BB_CREATE);
        views.put(BB_CREATE, bbCreateView);
    }

    private void addBBListListener()
    {
        addGenericListeners(BB_LIST);
        BBListView bbListView = (BBListView) views.get(BB_LIST);
        views.put(BB_LIST, bbListView);
    }

    private void addUserListListener()
    {
        addGenericListeners(USER_LIST);
        UserListView userListView = (UserListView) views.get(USER_LIST);
        userListView.addEditContentListener(new EditUserButtonListener());
        views.put(USER_LIST, userListView);
    }

    private void addUserEditListener()
    {
        addGenericListeners(USER_EDIT);
        UserEditView userEditView = (UserEditView) views.get(USER_EDIT);
        views.put(USER_EDIT, userEditView);
    }

    /**
     * addGenericListener is designed to add the Home, Back and Profile button to the defined view.
     * @param view_type GUI view type
     */
    private void addGenericListeners(VIEW_TYPE view_type)
    {
        AbstractGenericView genericView = (AbstractGenericView) views.get(view_type);
        genericView.addHomeButtonListener(new HomeButtonListener());
        genericView.addBackButtonListener(new BackButtonListener());
        genericView.addViewUserButtonListener(new ProfileButtonListener());
        views.put(view_type, genericView);
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
            updateView(BB_MENU);
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
            updateView(SCHEDULE);
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
            UserView userView = (UserView) views.get(USER_VIEW);

            // set username and password text on GUI
            // FIXME: get password and user permissions from Control/Server
            userView.setUsername(model.getUsername());
            userView.setPasswordText("Password");
            userView.setUserPermissions("Edit All Users");
            views.put(USER_VIEW, userView);

            // navigate to home screen
            updateView(USER_VIEW);
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
            updateView(BB_CREATE);
        }
    }

    /**
     * Listener to handle Edit User Button mouse clicks.
     */
    private class EditUserButtonListener extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: Edit User button clicked");
            System.out.println("Source :" + e.getSource());
            System.out.println("Button :" + e.getButton());
            System.out.println("Button :" + e.getID());
            // navigate to edit user screen
            updateView(USER_EDIT);
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
            UserListView userListView = (UserListView) views.get(USER_LIST);
            String[] stringArray = {"Alan","Kanu", "Jacinta", "Patrice"};
            userListView.addContent(stringArray);
            views.put(USER_LIST, userListView);

            // navigate to users list screen
            updateView(USER_LIST);
        }
    }

    /**
     * Listener to handle List BB mouse clicks.
     */
    private class ListBBListener extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: List BB button clicked");

            // get list BB view
            BBListView bbListView = (BBListView) views.get(BB_LIST);
            String[] stringArray = {"Myer's Biggest Sale","Kathmandu Summer Sale", "Quilton's Covid Special", "Macca's New Essentials Range"};
            bbListView.addContent(stringArray);
            views.put(BB_LIST, bbListView);

            // navigate to bb list screen
            updateView(BB_LIST);
        }
    }
}
