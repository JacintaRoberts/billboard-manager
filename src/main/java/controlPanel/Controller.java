package controlPanel;

import controlPanel.Main.VIEW_TYPE;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import static controlPanel.Main.VIEW_TYPE.*;

/**
 * Controller Class is designed to manage user inputs appropriately, sending requests to the server and updating the model/gui when required.
 * The constructor stores the model and views, and adds listeners to the views.
 */
public class Controller
{
    // store model and view
    private Model model;
    private HashMap<VIEW_TYPE, AbstractView> views;

    /*
     #################################### CONSTRUCTOR ####################################
     */

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

        addScheduleMonthlyListener();
        addScheduleDailyListener();

        addUserMenuListener();
        addUserViewListener();
        addUserListListener();
        addUserEditListener();

        addBBMenuListener();
        addBBCreateListener();
        addBBListListener();

        // set up Log In view
        showView(LOGIN);
    }

    /*
     #################################### LISTENERS ####################################
     */

    //--------------------------------- GENERIC LISTENER --------------------------
    //----------------------- INCL: HOME, BACK, PROFILE BUTTONS -------------------
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

    //--------------------------------- LOGIN / HOME LISTENER --------------------------

    /**
     * LOG IN LISTENERS: designed to add listeners to the LOGIN VIEW.
     * Listeners include: Login Submit Button.
     */
    private void addLogInListener()
    {
        LogInView logInView = (LogInView) views.get(LOGIN);
        // add listener
        logInView.addSubmitButtonListener(new SubmitButtonListener());
        views.put(LOGIN, logInView);
    }

    /**
     * HOME LISTENERS: designed to add listeners to the HOME VIEW.
     * Listeners include: User Menu, BB Menu, Schedule Menu, Profile and Back Button.
     */
    private void addHomeListener()
    {
        HomeView homeView = (HomeView) views.get(HOME);
        // add listeners
        homeView.addUserMenuListener(new UserMenuButtonListener());
        homeView.addBillboardsButtonListener(new BBMenuButtonListener());
        homeView.addScheduleButtonListener(new ScheduleButtonListener());
        homeView.addViewUserButtonListener(new ProfileButtonListener());
        homeView.addBackButtonListener(new BackButtonListener());
        views.put(HOME, homeView);
    }

    //------------------------------------ USER LISTENER ------------------------------

    /**
     * USER MENU LISTENERS: designed to add listeners to the USER MENU VIEW.
     * Listeners include: List Users, Create User Button.
     */
    private void addUserMenuListener()
    {
        addGenericListeners(USERS_MENU);
        // add listeners
        UsersMenuView usersMenuView = (UsersMenuView) views.get(USERS_MENU);
        usersMenuView.addListUserButtonListener(new ListUsersListener());
        // FIXME: addCreateUserButtonListener
        views.put(USERS_MENU, usersMenuView);
    }

    /**
     * USER VIEW (PROFILE) LISTENERS: designed to add listeners to the USER VIEW.
     * Listeners include: Home, Back and Edit button.
     */
    private void addUserViewListener()
    {
        UserView userView = (UserView) views.get(USER_VIEW);
        // add listeners
        userView.addHomeButtonListener(new HomeButtonListener());
        userView.addBackButtonListener(new BackButtonListener());
        // FIXME: addEditButton??
        views.put(USER_VIEW, userView);
    }

    /**
     * USER LIST (PROFILE) LISTENERS: designed to add listeners to the USER LIST VIEW.
     * Listeners include: Home, Back and Profile Button. Please Note: additional listeners are added dynamically upon
     * navigating to this VIEW (i.e. User Edit, Delete, View).
     */
    private void addUserListListener()
    {
        addGenericListeners(USER_LIST);
//        UserListView userListView = (UserListView) views.get(USER_LIST);
//        views.put(USER_LIST, userListView);
    }

    /**
     * USER EDIT LISTENERS: designed to add listeners to the USER EDIT VIEW.
     * Listeners include: Home, Back and Profile Button.
     */
    private void addUserEditListener()
    {
        addGenericListeners(USER_EDIT);
        UserEditView userEditView = (UserEditView) views.get(USER_EDIT);
        // FIXME: addSubmitButton
        // FIXME: addCancelButton
        views.put(USER_EDIT, userEditView);
    }

    //------------------------------------ BB LISTENER --------------------------------

    /**
     * BB MENU LISTENERS: designed to add listeners to the BB MENU VIEW.
     * Listeners include: Home, Back and Profile, also List BB and create BB.
     */
    private void addBBMenuListener()
    {
        addGenericListeners(BB_MENU);
        BBMenuView bbMenuView = (BBMenuView) views.get(BB_MENU);
        // add listeners
        bbMenuView.addBBCreateButtonListener(new BBCreateButtonListener());
        bbMenuView.addBBListListener(new ListBBListener());
        views.put(BB_MENU, bbMenuView);
    }

    /**
     * BB CREATE LISTENERS: designed to add listeners to the BB CREATE VIEW.
     * Listeners include: Home, Back and Profile, also FIXME: need to add a range of listeners!
     */
    private void addBBCreateListener()
    {
        addGenericListeners(BB_CREATE);
        BBCreateView bbCreateView = (BBCreateView) views.get(BB_CREATE);
        // FIXME: lots to come!
        views.put(BB_CREATE, bbCreateView);
    }

    /**
     * BB LIST LISTENERS: designed to add listeners to the BB LIST VIEW.
     * Listeners include: Home, Back and Profile. Please Note: additional listeners are added dynamically upon
     * navigating to this VIEW (i.e. BB Edit, Delete, View).
     */
    private void addBBListListener()
    {
        addGenericListeners(BB_LIST);
//        BBListView bbListView = (BBListView) views.get(BB_LIST);
//        views.put(BB_LIST, bbListView);
    }

    //---------------------------------- SCHEDULE LISTENER ------------------------------

    // FIXME: change to WEEKLY listener
    private void addScheduleMonthlyListener()
    {
        addGenericListeners(SCHEDULE_MONTH);
        ScheduleMonthlyView scheduleMonthlyView = (ScheduleMonthlyView) views.get(SCHEDULE_MONTH);
        scheduleMonthlyView.addDayButtonListener(new ScheduleDailyListener());
        views.put(SCHEDULE_MONTH, scheduleMonthlyView);
    }

    /**
     * DAILY SCHEDULE LISTENERS: designed to add listeners to the DAILY SCHEDULE VIEW.
     * Listeners include: Home, Back and Profile.
     */
    private void addScheduleDailyListener()
    {
        addGenericListeners(SCHEDULE_DAY);
//        ScheduleDailyView scheduleDailyView = (ScheduleDailyView) views.get(SCHEDULE_DAY);
//        views.put(SCHEDULE_DAY, scheduleDailyView);
    }


   /*
     #################################### VIEW MANAGEMENT ####################################
    */

    /**
     * Change user's view by hiding the old view and showing the new.
     * Detach observer from old view and attach observer to new.
     * @param newView the user's new view
     */
    private void updateView(VIEW_TYPE newView)
    {
        hideView(model.getCurrentView());
        // set up new frame
        showView(newView);
    }

    /**
     * hideView is designed to hide old view by:
     * - detaching observer to stop listening to model updates
     * - clean up gui
     * - setting new view as visible
     * - storing updated view in hash map
     */
    private void hideView(VIEW_TYPE oldViewType)
    {
        // get old view
        AbstractView oldView = views.get(oldViewType);
        // detach old view to stop listening to model
        model.detachObserver(oldView);
        // clean up gui (remove information)
        oldView.cleanUp();
        // set view as hidden
        oldView.setVisible(false);
        // update hashmap with hidden view
        views.put(oldView.getEnum(), oldView);
    }

    /**
     * ShowView is designed to set up new view by:
     * - attaching observer to new to listen to model updates
     * - setting new view as current view in model
     * - setting new view as visible
     * - storing updated view in hash map
     */
    private void showView(VIEW_TYPE newViewType)
    {
        // get new view
        AbstractView view = views.get(newViewType);
        // attach observer (this listens for model updates)
        model.attachObserver(view);
        // set current view in model
        // FIXME: make this generic for setting current and previous view
        model.setCurrentView(view.getEnum());
        // set view as visible
        view.setVisible(true);
        // update components based on permissions
        updateComponents(newViewType);
        // update hashmap with updated view
        views.put(newViewType, view);
    }

    /**
     * Designed to check Access Permissions in order to hide/show components of the Frame.
     * @param newViewType
     */
    private void updateComponents(VIEW_TYPE newViewType)
    {
        switch(newViewType)
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
        }
    }

   /*
     #################################### DEFINE LISTENERS ####################################
    */

    //---------------------------------- GENERIC LISTENER ------------------------------
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
     * Listener to handle Profile Button mouse clicks.
     * If user clicks the Profile button, user is navigated to Profile view. Profile view is updated with information.
     */
    private class ProfileButtonListener extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: Profile button clicked");

            // get PROFILE VIEW
            UserView userView = (UserView) views.get(USER_VIEW);

            // set profile information
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

    //---------------------------------- LOG IN LISTENER ------------------------------

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

            if (username.equals("P") && password.equals("P"))
            {
                response = true;
            }

            // if successful, store info in model, hide error and navigate to home screen
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
            // if unsuccessful, show error and do not allow log in
            else
            {
                System.out.println("CONTROLLER LEVEL - Incorrect Credentials");
                // show error message
                logInView.setErrorVisibility(true);
                views.put(VIEW_TYPE.LOGIN, logInView);
            }
        }
    }

    //---------------------------------- USER LISTENERS ------------------------------

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
            // navigate to user menu
            updateView(VIEW_TYPE.USERS_MENU);
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

            JButton button = (JButton) e.getSource();
            System.out.println("UserName :" + button.getName());
            // TODO: get user information from server i.e. UserInfo userInfo = getUser(sessionTocken, username, userRequest);

            // update information in EDIT USER view
            UserEditView userEditView = (UserEditView) views.get(USER_EDIT);
            userEditView.setUsername(button.getName());
            userEditView.setPassword("password123");
            userEditView.setPermissions(new String[]{"Edit All Users", "Edit BB", "Edit Schedule"});
            views.put(USER_EDIT, userEditView);

            // navigate to edit user screen
            updateView(USER_EDIT);
        }
    }

    /**
     * Listener to handle Delete User Button mouse clicks.
     */
    private class DeleteUserButtonListener extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: Delete User button clicked");
            JButton button = (JButton) e.getSource();
            System.out.println("UserName :" + button.getName());
            // TODO: get user information from server i.e. UserInfo userInfo = getUser(sessionToken, username, userRequest);
        }
    }

    /**
     * Listener to handle View User Button mouse clicks.
     */
    private class ViewUserButtonListener extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: View User button clicked");
            JButton button = (JButton) e.getSource();
            System.out.println("UserName :" + button.getName());
            // TODO: get user information from server i.e. UserInfo userInfo = getUser(sessionTocken, username, userRequest);
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
            userListView.addContent(stringArray, new EditUserButtonListener(), new DeleteUserButtonListener(), new ViewUserButtonListener());
            views.put(USER_LIST, userListView);

            // navigate to users list screen
            updateView(USER_LIST);
        }
    }

    //---------------------------------- BB LISTENERS ------------------------------

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
     * Listener to handle Edit BB Button mouse clicks.
     */
    private class EditBBButtonListener extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: Edit BB button clicked");
            JButton button = (JButton) e.getSource();
            System.out.println("BB Name: " + button.getName());
            // navigate to edit BB screen
            // FIXME: CHANGE TO BB_EDIT!!!
            updateView(BB_CREATE);
        }
    }

    /**
     * Listener to handle Delete BB Button mouse clicks.
     */
    private class DeleteBBButtonListener extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: Delete BB button clicked");
        }
    }

    /**
     * Listener to handle View BB Button mouse clicks.
     */
    private class ViewBBButtonListener extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: View BB button clicked");
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
            bbListView.addContent(stringArray, new EditBBButtonListener(), new DeleteBBButtonListener(), new ViewBBButtonListener());
            views.put(BB_LIST, bbListView);

            // navigate to bb list screen
            updateView(BB_LIST);
        }
    }

    //---------------------------------- SCHEDULE LISTENERS ------------------------------

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
            updateView(SCHEDULE_MONTH);
        }
    }

    /**
     * Listener to handle List daily schedule mouse clicks.
     */
    private class ScheduleDailyListener extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: List Day Schedule button clicked");

            JButton button = (JButton) e.getSource();
            System.out.println("BB Name: " + button.getName());

            // getName() is equivalent to the day's date
            // FIXME: send req to server to get all bb for that date, returning an array of strings (BB names) or objects

            // get daily schedule view
            ScheduleDailyView scheduleDailyView = (ScheduleDailyView) views.get(SCHEDULE_DAY);
            String[] stringArray = {"8 - 9am: Myer's Biggest Sale","9.30 - 10am Kathmandu Summer Sale", "11 - 12pm Quilton's Covid Special", "1 - 3pm Macca's New Essentials Range"};
            // FIXME: required to create edit, delete and view listeners!
            scheduleDailyView.addContent(stringArray, new EditScheduleButtonListener(), new DeleteBBButtonListener(), new ViewBBButtonListener());
            views.put(SCHEDULE_DAY, scheduleDailyView);

            // navigate to bb list screen
            updateView(SCHEDULE_DAY);
        }
    }

    /**
     * Listener to handle Edit Schedule Button mouse clicks.
     */
    private class EditScheduleButtonListener extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: Edit Schedule button clicked");
            JButton button = (JButton) e.getSource();
            System.out.println("BB Name: " + button.getName());
            // navigate to edit BB screen
            // FIXME: CHANGE TO BB_EDIT!!!
            updateView(SCHEDULE_UPDATE);
        }
    }

//    /**
//     * Listener to handle Schedule Day mouse clicks.
//     */
//    private class ScheduleDayButtonListener extends MouseAdapter
//    {
//        @Override
//        public void mouseClicked(MouseEvent e)
//        {
//            System.out.println("CONTROLLER LEVEL: Schedule Day button clicked");
//
//            // get list BB view
//            BBListView bbListView = (BBListView) views.get(BB_LIST);
//            String[] stringArray = {"Myer's Biggest Sale","Kathmandu Summer Sale", "Quilton's Covid Special", "Macca's New Essentials Range"};
//            bbListView.addContent(stringArray, new EditBBButtonListener(), new DeleteBBButtonListener(), new ViewBBButtonListener());
//            views.put(BB_LIST, bbListView);
//
//            // navigate to bb list screen
//            updateView(SCHEDULE_DAY);
//        }
//    }

}
