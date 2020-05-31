package controlPanel;

import controlPanel.Main.VIEW_TYPE;
import org.w3c.dom.Document;
import server.BillboardList;
import server.DbBillboard;
import server.ScheduleInfo;
import server.Server;
import server.Server.ServerAcknowledge;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static controlPanel.Main.VIEW_TYPE.*;
import static controlPanel.UserControl.loginRequest;
import static controlPanel.UserControl.logoutRequest;
import static server.Server.ServerAcknowledge.*;

/**
 * Controller Class is designed to manage user inputs appropriately, sending requests to the Control Classes and
 * updating the model and GUI views when required. The constructor stores a reference to the model and views, and
 * adds listeners to each GUI view.
 */
public class Controller
{
    // store model, view, server response and the billboard viewer
    private Model model;
    private HashMap<VIEW_TYPE, AbstractView> views;
    private Object serverResponse;
    private BBFullPreview BBViewer;

    /*
     #################################### CONSTRUCTOR ####################################
     */

    /**
     * Controller Constructor stores instances of Views, adds listeners to Views, sets up Log In View allowing users
     * to log in to the application on startup and instantiates the billboard viewer.
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

        addScheduleWeekListener();
        addScheduleListener();
        addScheduleMenuListener();

        addUserMenuListener();
        addUserProfileListener();
        addUserListListener();
        addUserEditListener();
        addUserPreviewListener();
        addUserCreateListener();

        addBBMenuListener();
        addBBCreateListener();
        addBBListListener();

        // instantiate the viewer object which allows users to view the BB in full view from the control panel
        BBViewer = new BBFullPreview();

        // show the Log In view
        showView(LOGIN);
    }

    /*
     #################################### LISTENERS ####################################
     */

    //--------------------------------- GENERIC LISTENER --------------------------
    //----------------------- INCL: HOME, BACK, PROFILE BUTTONS -------------------
    /**
     * addGenericListener is designed to add the Home, Log Out and Profile button to the defined view.
     * @param view_type GUI view type
     */
    private void addGenericListeners(VIEW_TYPE view_type)
    {
        AbstractGenericView genericView = (AbstractGenericView) views.get(view_type);
        genericView.addHomeButtonListener(new HomeButtonListener());
        genericView.addViewUserButtonListener(new ProfileButtonListener());
        genericView.addLogOutListener(new LogOutButtonListener());
        views.put(view_type, genericView);
    }

    //--------------------------------- LOGIN / HOME LISTENER --------------------------

    /**
     * LOG IN LISTENERS: designed to add listeners to the LOGIN VIEW.
     * Listeners include: Login Submit Button.
     */
    private void addLogInListener()
    {
        // get GUI view and add listeners
        LogInView logInView = (LogInView) views.get(LOGIN);
        logInView.addSubmitButtonListener(new LogInSubmitButtonListener());
        views.put(LOGIN, logInView);
    }

    /**
     * HOME LISTENERS: designed to add listeners to the HOME VIEW.
     * Listeners include: generic listeners, User Menu, BB Menu, Schedule Menu, Profile and Log out Button.
     */
    private void addHomeListener()
    {
        // get GUI view and add listeners
        HomeView homeView = (HomeView) views.get(HOME);
        // add listeners
        homeView.addUserMenuListener(new UserMenuButtonListener());
        homeView.addBillboardsButtonListener(new BBMenuButtonListener());
        homeView.addScheduleButtonListener(new ScheduleButtonListener());
        homeView.addViewUserButtonListener(new ProfileButtonListener());
        homeView.addLogOutListener(new LogOutButtonListener());
        views.put(HOME, homeView);
    }

    //------------------------------------ USER LISTENERS ------------------------------

    /**
     * USER MENU LISTENERS: designed to add listeners to the USER MENU VIEW.
     * Listeners include: generic listeners, List Users, Create User Button.
     */
    private void addUserMenuListener()
    {
        addGenericListeners(USERS_MENU);
        // add listeners
        UsersMenuView usersMenuView = (UsersMenuView) views.get(USERS_MENU);
        usersMenuView.addListUserButtonListener(new ListUsersListener());
        usersMenuView.addCreateUserButtonListener(new CreateUserButtonListener());
        views.put(USERS_MENU, usersMenuView);
    }

    /**
     * USER VIEW (PROFILE) LISTENERS: designed to add listeners to the USER VIEW.
     * Listeners include: generic listeners, edit profile and user menu button
     */
    private void addUserProfileListener()
    {
        UserProfileView userProfileView = (UserProfileView) views.get(USER_PROFILE);
        // add listeners
        userProfileView.addHomeButtonListener(new HomeButtonListener());
        userProfileView.addEditButtonListener(new EditProfileButtonListener());
        userProfileView.addUserMenuButton(new UserMenuButtonListener());
        views.put(USER_PROFILE, userProfileView);
    }

    /**
     * USER CREATE LISTENERS: designed to add listeners to the USER LIST view.
     * Listeners include: Home, Back, Profile and User Menu Button.
     * Please Note: additional listeners are added dynamically upon navigating to this VIEW
     * (i.e. User Edit, Delete, View)
     */
    private void addUserListListener()
    {
        addGenericListeners(USER_LIST);
        UserListView userListView = (UserListView) views.get(USER_LIST);
        userListView.addUserMenuButton(new UserMenuButtonListener());
        views.put(USER_LIST, userListView);
    }

    /**
     * USER EDIT LISTENERS: designed to add listeners to the USER EDIT VIEW.
     * Listeners include: Home, Back and Profile Button, including User Permission Update and Password Update Buttons,
     * and User Menu button.
     */
    private void addUserEditListener()
    {
        addGenericListeners(USER_EDIT);
        UserEditView userEditView = (UserEditView) views.get(USER_EDIT);
        userEditView.addSubmitButtonListener(new UserPermissionUpdateListener());
        userEditView.addPasswordButtonListener(new UserPasswordUpdateButtonListener());
        userEditView.addUserMenuButton(new UserMenuButtonListener());
        views.put(USER_EDIT, userEditView);
    }

    /**
     * USER PREVIEW LISTENERS: designed to add listeners to the USER VIEW VIEW.
     * Listeners include: Home, Back, Profile and User Menu button.
     */
    private void addUserPreviewListener()
    {
        addGenericListeners(USER_VIEW);
        UserPreviewView userPreviewView = (UserPreviewView) views.get(USER_VIEW);
        userPreviewView.addUserMenuButton(new UserMenuButtonListener());
        views.put(USER_VIEW, userPreviewView);
    }

    /**
     * USER CREATE LISTENERS: designed to add listeners to the USER CREATE VIEW.
     * Listeners include: Home, Back and Profile Button, including user create button and add password button
     */
    private void addUserCreateListener()
    {
        addGenericListeners(USER_CREATE);
        UserCreateView userCreateView = (UserCreateView) views.get(USER_CREATE);
        userCreateView.addSubmitButtonListener(new UserCreateButtonListener());
        userCreateView.addPasswordButtonListener(new UserPasswordCreateButtonListener());
        userCreateView.addUserMenuButton(new UserMenuButtonListener());
        views.put(USER_CREATE, userCreateView);
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
        bbMenuView.addBBCreateButtonListener(new BBCreateButtonListener());
        bbMenuView.addBBListListener(new ListBBListener());
        views.put(BB_MENU, bbMenuView);
    }

    /**
     * BB CREATE LISTENERS: designed to add listeners to the BB CREATE VIEW.
     * Listeners include: Home, Back and Profile, including a range of listeners designed to handle the BB Design
     */
    private void addBBCreateListener()
    {
        addGenericListeners(BB_CREATE);
        BBCreateView bbCreateView = (BBCreateView) views.get(BB_CREATE);
        bbCreateView.addBBBackgroundColourListener(new ColourListener());
        bbCreateView.addBBTitleListener(new TitleListener());
        bbCreateView.addBBTextListener(new BBTextListener());
        bbCreateView.addBBPhotoListener(new BBPhotoListener());
        bbCreateView.addBBXMLImportListener(new BBXMLImportListener());
        bbCreateView.addXMLExportListener(new BBXMLExportListener());
        bbCreateView.addBBNameListener(new NameListener());
        bbCreateView.addBBCreationListener(new BBCreateListener());
        bbCreateView.addBBPreviewListener(new BBPreviewListener());
        bbCreateView.addBBMenuListener(new BBMenuButtonListener());
        views.put(BB_CREATE, bbCreateView);
    }

    /**
     * BB LIST LISTENERS: designed to add listeners to the BB LIST VIEW.
     * Listeners include: Home, Back and Profile. Please Note: additional listeners are added dynamically upon
     * navigating to this VIEW (i.e. BB Edit, Delete, View).
     */
    private void addBBListListener()
    {
        BBListView bbListView = (BBListView) views.get(BB_LIST);
        addGenericListeners(BB_LIST);
        bbListView.addBBMenuListener(new BBMenuButtonListener());
        views.put(BB_LIST, bbListView);
    }

    //---------------------------------- SCHEDULE LISTENER ------------------------------

    /**
     * SCHEDULE MENU LISTENERS: designed to add listeners to the SCHEDULE MENU VIEW.
     * Listeners include: Home, Back and Profile. Including Schedule Calendar View and Schedule Create/Update buttons.
     */
    private void addScheduleMenuListener()
    {
        addGenericListeners(SCHEDULE_MENU);
        ScheduleMenuView scheduleMenuView = (ScheduleMenuView) views.get(SCHEDULE_MENU);
        scheduleMenuView.addScheduleViewListener(new ScheduleViewButtonListener());
        scheduleMenuView.addScheduleCreateListener(new ScheduleCreateButtonListener());
        views.put(SCHEDULE_MENU, scheduleMenuView);
    }

    /**
     * SCHEDULE WEEK LISTENERS: designed to add listeners to the SCHEDULE WEEK VIEW.
     * Listeners include: Home, Back, Profile and Schedule Menu buttons
     */
    private void addScheduleWeekListener()
    {
        addGenericListeners(SCHEDULE_WEEK);
        ScheduleWeekView scheduleWeekView = (ScheduleWeekView) views.get(SCHEDULE_WEEK);
        scheduleWeekView.addScheduleMenuListener(new ScheduleButtonListener());
        views.put(SCHEDULE_WEEK, scheduleWeekView);
    }

    /**
     * SCHEDULE UPDATE LISTENERS: designed to add listeners to the SCHEDULE UPDATE VIEW.
     * Listeners include: Home, Back and Profile, including a range of buttons for the schedule creation.
     */
    private void addScheduleListener()
    {
        addGenericListeners(SCHEDULE_UPDATE);
        ScheduleUpdateView scheduleUpdateView = (ScheduleUpdateView) views.get(SCHEDULE_UPDATE);
        scheduleUpdateView.addScheduleTimeListener(new ScheduleDurationListener());
        scheduleUpdateView.addRadioButtonListener(new ScheduleRadioButtonListener());
        scheduleUpdateView.addPopulateScheduleListener(new SchedulePopulateListener());
        scheduleUpdateView.addScheduleSubmitButtonListener(new ScheduleSubmitButtonListener());
        scheduleUpdateView.addMinuteRepeatListener(new ScheduleMinuteRepeatListener());
        scheduleUpdateView.addScheduleClearButtonListener(new ScheduleClearButtonListener());
        scheduleUpdateView.addScheduleMenuListener(new ScheduleButtonListener());
        views.put(SCHEDULE_UPDATE, scheduleUpdateView);
    }

   /*
     #################################### VIEW MANAGEMENT ####################################
    */

    /**
     * Change user's view by hiding the old view and showing the new.
     * @param newView the user's new view type
     */
    private void updateView(VIEW_TYPE newView)
    {
        // hide old view
        hideView(model.getCurrentView());
        // set up new frame
        showView(newView);
    }

    /**
     * hideView is designed to hide old view by:
     * - clean up gui
     * - setting old view as invisible
     * - storing old view in hash map
     */
    private void hideView(VIEW_TYPE oldViewType)
    {
        // get old view
        AbstractView oldView = views.get(oldViewType);
        // clean up gui (remove information)
        oldView.cleanUp();
        // set view as hidden
        oldView.setVisible(false);
        // update hashmap with hidden view
        views.put(oldView.getEnum(), oldView);
    }

    /**
     * ShowView is designed to set up new view by:
     * - setting new view as current view in model
     * - setting new view as visible
     * - update components if required
     * - storing updated view in hash map
     */
    private void showView(VIEW_TYPE newViewType)
    {
        // get new view
        AbstractView view = views.get(newViewType);
        // set current view in model
        model.setCurrentView(view.getEnum());
        // set view as visible
        view.setVisible(true);
        // update components if required
        updateComponents(newViewType);
        // update hashmap with updated view
        views.put(newViewType, view);
    }

    /**
     * Designed to update components before navigating to the view.
     * @param newViewType view type of new view
     */
    private void updateComponents(VIEW_TYPE newViewType){
        switch(newViewType)
        {
            // HOME: set welcome text
            case HOME:
                HomeView homeView = (HomeView) views.get(VIEW_TYPE.HOME);
                homeView.setWelcomeText(model.getUsername());
                views.put(HOME, homeView);
                break;

            // SCHEDULE_WEEK: set welcome text, populate schedule from DB
            case SCHEDULE_WEEK:
                scheduleWeek();
                break;

            // SCHEDULE_UPDATE: set welcome text, populate schedule from DB
            case SCHEDULE_UPDATE:
                scheduleUpdate();
                break;

            // SCHEDULE_MENU: set welcome text
            case SCHEDULE_MENU:
                ScheduleMenuView scheduleMenuView = (ScheduleMenuView) views.get(SCHEDULE_MENU);
                scheduleMenuView.setWelcomeText(model.getUsername());
                views.put(SCHEDULE_MENU, scheduleMenuView);
                break;

            // USER_LIST: set welcome text, populate user info
            case USER_LIST:
                UserListView userListView = (UserListView) views.get(USER_LIST);
                userListView.setWelcomeText(model.getUsername());
                views.put(USER_LIST, userListView);
                break;

            // USER_EDIT: set welcome text
            case USER_EDIT:
                UserEditView userEditView = (UserEditView) views.get(USER_EDIT);
                userEditView.setWelcomeText(model.getUsername());
                views.put(USER_EDIT, userEditView);
                break;

            // USER_CREATE: set welcome text
            case USER_CREATE:
                UserCreateView userCreateView = (UserCreateView) views.get(USER_CREATE);
                userCreateView.setWelcomeText(model.getUsername());
                views.put(USER_CREATE, userCreateView);
                break;

            // USER_EDIT: set welcome text, populate user info
            case USER_PROFILE:
                UserProfileView userProfileView = (UserProfileView) views.get(USER_PROFILE);
                userProfileView.setWelcomeText(model.getUsername());
                views.put(USER_PROFILE, userProfileView);
                break;

            // USER_VIEW: set welcome text, populate user info
            case USER_VIEW:
                UserPreviewView userPreviewView = (UserPreviewView) views.get(USER_VIEW);
                userPreviewView.setWelcomeText(model.getUsername());
                views.put(USER_VIEW, userPreviewView);
                break;

            // USERS_MENU: set welcome text
            case USERS_MENU:
                UsersMenuView usersMenuView = (UsersMenuView) views.get(USERS_MENU);
                usersMenuView.setWelcomeText(model.getUsername());
                views.put(USERS_MENU, usersMenuView);
                break;

            // BB_LIST: set welcome text, populate BB list
            case BB_LIST:
                BBList();
                break;

            // BB_CREATE: set welcome text
            case BB_CREATE:
                BBCreateView bbCreateView = (BBCreateView) views.get(BB_CREATE);
                bbCreateView.setWelcomeText(model.getUsername());
                views.put(BB_CREATE, bbCreateView);
                break;

            // BB_MENU: set welcome text
            case BB_MENU:
                BBMenuView bbMenuView = (BBMenuView) views.get(BB_MENU);
                bbMenuView.setWelcomeText(model.getUsername());
                views.put(BB_MENU, bbMenuView);
                break;
        }
    }

   /*
     #################################### DEFINE LISTENERS ####################################
    */

    //---------------------------------- GENERIC LISTENERS ------------------------------
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
            updateView(VIEW_TYPE.HOME);
        }
    }

    /**
     * Listener to handle Profile Button mouse clicks.
     * If user clicks the Profile button, user is navigated to Profile view. Profile view is updated with information.
     */
    private class ProfileButtonListener extends MouseAdapter
    {
        /**
         * Define logic to navigate to Profile View when mouse clicked.
         * Profile view is updated with information.
         * @param e mouse click event
         */
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: Profile button clicked");

            // get PROFILE VIEW
            UserProfileView userProfileView = (UserProfileView) views.get(USER_PROFILE);

            // get username
            String username = model.getUsername();

            // set username, password and permissions in Profile View
            userProfileView.setUsername(username);

            // Get user permissions from server
            getUserPermissionsFromServer(userProfileView, USER_PROFILE, username);

            views.put(USER_PROFILE, userProfileView);
        }
    }

    /**
     * Get user permissions from server, store in view and display appropriate error message pop-up windows/actions
     */
    private void getUserPermissionsFromServer(AbstractUserView userView, VIEW_TYPE viewType, String username) {
        try {
            serverResponse = UserControl.getPermissionsRequest(model.getSessionToken(), username);
            ArrayList<Boolean> userPermissions = (ArrayList<Boolean>) serverResponse;
            userView.setPermissions(userPermissions);
            updateView(viewType);
            views.put(viewType, userView);
        } catch (IOException | ClassNotFoundException ex)
        {
            userView.showFatalError();
            System.exit(0);
            // If the return is not an array list of booleans, an exception occurred
        } catch ( ClassCastException ex )
        {
            if (serverResponse.equals(InvalidToken))
            {
                userView.showInvalidTokenException();
                updateView(LOGIN);
            } else if ( serverResponse.equals(InsufficientPermission) )
            {
                userView.showInsufficientPermissionsException();
            } else if (serverResponse.equals(NoSuchUser))
            {
                userView.showNoSuchUserException();
                updateView(USERS_MENU);
            }
            views.put(viewType, userView);
        }
    }


    /**
     * Listener to handle Log Out Button mouse clicks. User is navigated to Log In Screen.
     */
    private class LogOutButtonListener extends MouseAdapter
    {
        /**
         * Define logic to navigate to Log Out view when mouse clicked.
         * @param e mouse click event
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            System.out.println("CONTROLLER LEVEL: Log Out button clicked");
            LogInView logInView = (LogInView) views.get(VIEW_TYPE.LOGIN);
            // Retrieve server response
            try {
                String sessionToken = model.getSessionToken(); // Retrieve session token
                serverResponse = logoutRequest(sessionToken); // CP Backend method call
                System.out.println("Received from server: " + serverResponse);
            } catch (IOException | ClassNotFoundException ex) {
                logInView.showFatalError();
                System.exit(0);
            }
            // If successful, let the user know, navigate to login screen
            if (serverResponse.equals(Success)) {
                System.out.println("CONTROLLER LEVEL - Session Token Successfully Expired");
                logInView.showLogoutSuccess();
            // Error handling as follows
            } else {
                System.out.println("CONTROLLER LEVEL - Session Token Was Already Expired!");
                logInView.showInvalidTokenException();
            }
            // Navigate to log in screen for both cases
            updateView(LOGIN);
        }
    }

    //---------------------------------- LOG IN LISTENER ------------------------------

    /**
     * Listener to handle user's log in attempt. The username and password is retrieved from the GUI input and a
     * request is sent to server to check validity of user. If response is true, username is stored in model and user
     * is navigated to home screen. If invalid credentials, error is displayed.
     */
    private class LogInSubmitButtonListener extends MouseAdapter
    {
        /**
         * Define logic to handle User Log In attempt when mouse clicked.
         * Show pop up error message for various error, or nav to Home Screen if successful.
         * @param e mouse click event
         */
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: Submit button clicked");
            // get LOGIN GUI
            LogInView logInView = (LogInView) views.get(VIEW_TYPE.LOGIN);

            // get username and password text from GUI
            String username = logInView.getUsername();
            String password = logInView.getPassword();

            // Attempt to handle the login request
            try {
                serverResponse = loginRequest(username, password); // CP Backend call

                // NOTE: loginRequest will return 1 of 3 serverAcknowledgments
                // if unsuccessful, show error and do not allow log in
                if (serverResponse.equals(BadPassword)) {
                    System.out.println("CONTROLLER LEVEL - Incorrect Password");
                    logInView.showBadPasswordException();
                    views.put(VIEW_TYPE.LOGIN, logInView);
                } else if (serverResponse.equals(NoSuchUser)) {
                    System.out.println("CONTROLLER LEVEL - No Such User");
                    logInView.showNoSuchUserException();
                    views.put(VIEW_TYPE.LOGIN, logInView);
                } else { // login request success
                    System.out.println("CONTROLLER LEVEL - Correct Credentials");
                    // store username and session token in model
                    model.storeUsername(username);
                    String sessionToken = (String) serverResponse; // Store as a session token
                    model.storeSessionToken(sessionToken);
                    // nav user to home screen
                    updateView(VIEW_TYPE.HOME);
                }
            } catch (IOException | ClassNotFoundException | NoSuchAlgorithmException ex) {
                logInView.showFatalError();
                System.exit(0);
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
        /**
         * Define logic to navigate to USER MENU when mouse clicked User Menu button.
         * @param e mouse click event
         */
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
        /**
         * Define logic to navigate to Edit User View when mouse clicked on User Edit button.
         * If user permissions valid, proceed to User Edit View and populate user information from database.
         * @param e mouse click event
         */
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: Edit User button clicked");
            // get source button
            JButton button = (JButton) e.getSource();
            // update information in EDIT USER view
            UserEditView userEditView = (UserEditView) views.get(USER_EDIT);
            // get selected user
            String usernameSelected = button.getName();
            // set username, password and permissions in User Edit View
            userEditView.setUsername(usernameSelected);

            // Get user permissions from server
            getUserPermissionsFromServer(userEditView, USER_EDIT, usernameSelected);

            views.put(USER_EDIT, userEditView);
        }
    }

    /**
     * Listener to handle Create User Button mouse clicks.
     * This will navigate the user to the User Create view.
     */
    private class CreateUserButtonListener extends MouseAdapter
    {
        /**
         * Define logic to navigate to Create User View when mouse clicked on User Create button.
         * @param e mouse click event
         */
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: Create User button clicked");
            // navigate to create user screen
            updateView(USER_CREATE);
        }
    }

    /**
     * Listener to handle Permission Update Button mouse clicks.
     */
    private class UserPermissionUpdateListener extends MouseAdapter
    {
        /**
         * Define logic to navigate to Submit New User Permissions when mouse clicked on Submit new Permissions button
         * if Edit User view. Error pop ups are displayed for any errors occurred.
         * @param e mouse click event
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            System.out.println("CONTROLLER LEVEL: Submit new user permission button clicked");

            // update information in EDIT USER view
            UserEditView userEditView = (UserEditView) views.get(USER_EDIT);

            // ask user to confirm updating user permissions
            int response = userEditView.showUserConfirmation();

            // add permissions to DB if user confirms permissions
            if (response == 0) {
                // Try to store selected permissions in database
                try {
                    // get current user info input by user
                    ArrayList<Object> userArray = userEditView.getUserInfo();

                    // Parsing elements from user array for the UserControl method to update user permission/password
                    String username = (String) userArray.get(0);
                    Boolean createBillboards = (Boolean) userArray.get(1);
                    Boolean editBillboards = (Boolean) userArray.get(2);
                    Boolean editSchedules = (Boolean) userArray.get(3);
                    Boolean editUsers = (Boolean) userArray.get(4);

                    ServerAcknowledge serverResponse = UserControl.setPermissionsRequest(model.getSessionToken(), username,
                            createBillboards, editBillboards, editSchedules, editUsers);

                    if (serverResponse.equals(Success)) {
                        userEditView.showEditPermissionsSuccess();
                    } else if (serverResponse.equals(InsufficientPermission)) {
                        userEditView.showInsufficientPermissionsException();
                    } else if (serverResponse.equals(InvalidToken)) {
                        userEditView.showInvalidTokenException();
                    } else if (serverResponse.equals(CannotRemoveOwnAdminPermission)) {
                        userEditView.showCannotRemoveOwnAdminPermissionException();
                    } else if (serverResponse.equals(NoSuchUser)) {
                        userEditView.showNoSuchUserException();
                    }
                } catch (IOException | ClassNotFoundException ex) {
                    userEditView.showFatalError();
                    System.exit(0);
                }
            }
            // do nothing if user does not confirm user permission update
        }
    }

    /**
     * Listener to handle Create New User Button mouse clicks.
     */
    private class UserCreateButtonListener extends MouseAdapter
    {
        /**
         * Define logic to navigate to Submit New User View when mouse clicked on Submit button in UserCreateView.
         * @param e mouse click event
         */
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: Submit new User button clicked");

            // update information in CREATE USER view
            UserCreateView userCreateView = (UserCreateView) views.get(USER_CREATE);

            // check if user info is valid
            boolean validUserInput = userCreateView.checkValidUser();

            // if valid user input, ask user to confirm user creation
            if (validUserInput)
            {
                int response = userCreateView.showCreateUserConfirmation();
                // add user to DB if user confirms user creation
                if (response == 0)
                {
                    ArrayList<Object> userArray = userCreateView.getUserInfo();

                    // Parsing elements from user array for the UserControl method to create the request to server
                    String username = (String) userArray.get(0);
                    String password = (String) userArray.get(1);
                    Boolean createBillboards = (Boolean) userArray.get(2);
                    Boolean editBillboards = (Boolean) userArray.get(3);
                    Boolean editSchedules = (Boolean) userArray.get(4);
                    Boolean editUsers = (Boolean) userArray.get(5);

                    // Retrieve server response
                    try {
                        serverResponse = UserControl.createUserRequest(model.getSessionToken(), username, password, createBillboards, editBillboards, editSchedules, editUsers);
                    }
                    catch (IOException | NoSuchAlgorithmException | ClassNotFoundException ex)
                    {
                        userCreateView.showFatalError();
                        System.exit(0);
                    }

                    // Filter response and display appropriate action
                    if (serverResponse.equals(Success))
                    {
                        userCreateView.showCreateSuccess();
                        // navigate to user menu screen
                        updateView(USERS_MENU);
                    }
                    else if (serverResponse.equals(InvalidToken))
                    {
                        userCreateView.showInvalidTokenException();
                        // navigate to log in screen
                        updateView(LOGIN);
                    }
                    else if (serverResponse.equals(InsufficientPermission))
                    {
                        userCreateView.showInsufficientPermissionsException();
                    }
                    else if (serverResponse.equals(PrimaryKeyClash))
                    {
                        userCreateView.showUsernamePrimaryKeyClashException();
                    }
                    views.put(USER_CREATE, userCreateView);
                }
            }
            else
            {
                // show invalid error message
                userCreateView.showErrorMessage();
            }
        }
    }

    /**
     * Listener to handle user password update button events.
     */
    private class UserPasswordUpdateButtonListener extends MouseAdapter
    {
        /**
         * Define logic to navigate to Update User Password when mouse clicked on Set Password in Edit User view.
         * Update password in db if user has correct permissions, else show error.
         * @param e mouse click event
         */
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: User Password Set button clicked");

            UserEditView userEditView = (UserEditView) views.get(USER_EDIT);

            try
            {
                // get password from user
                String password = userEditView.showNewPasswordInput();

                // ask user for confirmation of editing password
                int response = userEditView.showUserConfirmation();

                // Confirm response of updated password
                if  (response == 0)
                {
                    try {
                        ServerAcknowledge serverResponse = UserControl.setPasswordRequest(model.getSessionToken(), model.getUsername(), password);
                        if ( serverResponse.equals(Success) ) {
                            userEditView.showEditPasswordSuccess();
                        }  else if ( serverResponse.equals(InvalidToken) ) {
                            userEditView.showInvalidTokenException();
                            updateView(LOGIN);
                        } else if ( serverResponse.equals(InsufficientPermission) ) {
                            userEditView.showInsufficientPermissionsException();
                        } else if ( serverResponse.equals(NoSuchUser) ) {
                            userEditView.showNoSuchUserException();
                        }
                    } catch (IOException | ClassNotFoundException | NoSuchAlgorithmException ex) {
                        userEditView.showFatalError();
                        System.exit(0);
                    }
                }
            }
            catch (Exception ex)
            {
                userEditView.showMessageToUser("Unable to Set Password. Reason: " + ex.getMessage());
            }
            views.put(USER_EDIT, userEditView);
        }
    }

    /**
     * Listener to handle User Password Create Button allowing user to input password
     */
    private class UserPasswordCreateButtonListener extends MouseAdapter
    {
        /**
         * Define logic to allow user to input user password when mouse clicked on Set Password in User Create View.
         * @param e mouse click event
         */
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: User Password Set button clicked");
            UserCreateView userCreateView = (UserCreateView) views.get(USER_CREATE);
            userCreateView.showNewPasswordInput();
            views.put(USER_CREATE, userCreateView);
        }
    }

    /**
     * Listener to handle Delete User Button mouse clicks.
     */
    private class DeleteUserButtonListener extends MouseAdapter
    {
        /**
         * Define logic to handle User Delete event when mouse clicked on User Delete button in User List view.
         * Delete user from DB if user has the correct permissions or is not trying to delete own user.
         * Error message pops up when required.
         * @param e mouse click event
         */
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: Delete User button clicked");
            JButton button = (JButton) e.getSource();
            String username = button.getName();
            UserListView userListView = (UserListView) views.get(USER_LIST);
            // ask user to confirm deletion of user
            int response = userListView.showDeleteConfirmation();

            // if confirmed response, delete from DB
            if (response == 0)
            {
                // Retrieve response from DB
                try {
                    String sessionToken = model.getSessionToken(); // Retrieve session token
                    serverResponse = UserControl.deleteUserRequest(sessionToken, username); // CP Backend method call
                } catch (IOException | ClassNotFoundException ex) {
                    userListView.showFatalError();
                    System.exit(0);
                }

                // If successful, let the user know
                if (serverResponse.equals(Success)) {
                    System.out.println("CONTROLLER LEVEL - User was Successfully Deleted");
                    userListView.showDeleteSuccess();
                    // navigate back to the same page to refresh view
                    updateView(USERS_MENU);
                // Error handling on GUI via pop-up windows
                } else if (serverResponse.equals(InsufficientPermission)) {
                    System.out.println("CONTROLLER LEVEL - Insufficient Permission");
                    userListView.showInsufficientPermissionsException();
                } else if (serverResponse.equals(InvalidToken)) {
                    System.out.println("CONTROLLER LEVEL - Invalid Token");
                    userListView.showInvalidTokenException();
                    updateView(LOGIN);
                } else if (serverResponse.equals(NoSuchUser)) {
                    System.out.println("CONTROLLER LEVEL - No Such User Found in DB to be Deleted");
                    userListView.showNoSuchUserException();
                } else if (serverResponse.equals(CannotDeleteSelf)) {
                    System.out.println("CONTROLLER LEVEL - Cannot Delete Your Own User");
                    userListView.showCannotDeleteSelfException();
                }
            }
        }
    }

    /**
     * Listener to handle View User Button mouse clicks, allowing users to navigate to USER VIEW view to
     * view the details of the user selected.
     */
    private class ViewUserButtonListener extends MouseAdapter
    {
        /**
         * Define logic to navigate to View User when mouse clicked on User View button in User List view.
         * Errors displayed when relevant (i.e. user permissions, token invalid)
         * @param e mouse click event
         */
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: View User button clicked");
            JButton button = (JButton) e.getSource();
            // update information in EDIT PREVIEW view
            UserPreviewView userPreviewView = (UserPreviewView) views.get(USER_VIEW);
            // get selected user
            String usernameSelected = button.getName();
            // set username, password and permissions in User Edit View
            userPreviewView.setUsername(usernameSelected);
            // Get user permissions from server
            getUserPermissionsFromServer(userPreviewView, USER_VIEW, usernameSelected);
        }
    }

    /**
     * Listener to handle List Users mouse clicks.
     */
    private class ListUsersListener extends MouseAdapter
    {
        /**
         * Define logic to navigate to List User View when mouse clicked on User List button in User Menu.
         * @param e mouse click event
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            System.out.println("CONTROLLER LEVEL: List Users button clicked");
            listUserHandling();
        }
    }

    /**
     * List User Handling is defined to add User Data to the List User view.
     * Welcome text is set and error messages are displayed when relevant.
     */
    private void listUserHandling()
    {
        // get LIST USER view
        UserListView userListView = (UserListView) views.get(USER_LIST);
        userListView.setWelcomeText(model.getUsername());
        ArrayList<String> usernames = null;
        ServerAcknowledge errorMessage = Success;
        try {
            serverResponse = UserControl.listUsersRequest(model.getSessionToken());
            // Attempt to cast to a string ArrayList for successful response
            usernames = (ArrayList<String>) serverResponse;
        } catch (IOException | ClassNotFoundException ex)
        {
            userListView.showFatalError();
            System.exit(0);
        } catch (ClassCastException ex) {
            // Otherwise, some other error message was returned from the server
            errorMessage = (ServerAcknowledge) serverResponse;
        }

        // Error handling on GUI as follows
        if (errorMessage.equals(InsufficientPermission)) {
            System.out.println("CONTROLLER LEVEL - Insufficient Permissions");
            userListView.showInsufficientPermissionsException();
        } else if (serverResponse.equals(InvalidToken)) {
            System.out.println("CONTROLLER LEVEL - Invalid Token");
            userListView.showInvalidTokenException();
            updateView(LOGIN);
        } else { // Successful, let the user know and populate with list of users
            userListView.addContent(usernames, new EditUserButtonListener(), new DeleteUserButtonListener(), new ViewUserButtonListener());
            updateView(USER_LIST);
        }
        views.put(USER_LIST, userListView);
    }

    /**
     * Listener to handle Edit Profile Button mouse clicks.
     */
    private class EditProfileButtonListener extends MouseAdapter
    {
        /**
         * Define logic to navigate to Edit Profile View when mouse clicked on Edit Profile button in Profile View.
         * @param e mouse click event
         */
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: Edit Profile button clicked");
            // get USER EDIT VIEW
            UserEditView userEditView = (UserEditView) views.get(USER_EDIT);
            String username = model.getUsername();

            // set username, password and permissions in Profile View
            userEditView.setUsername(username);

            // Get user permissions from server
            getUserPermissionsFromServer(userEditView, USER_EDIT, username);
        }
    }

    //---------------------------------- BB LISTENERS ------------------------------

    /**
     * Listener to handle BBMenu Button mouse clicks.
     * If user clicks the BBMenu button, user is navigated to BB Menu view.
     */
    private class BBMenuButtonListener extends MouseAdapter
    {
        /**
         * Define logic to navigate to BB Menu View when mouse clicked on BB Menu button in Home View.
         * @param e mouse click event
         */
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
        /**
         * Define logic to navigate to BB Create View when mouse clicked on BB Create button.
         * @param e mouse click event
         */
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: BB Create button clicked");
            // set BB Name to enabled so user can set a name
            BBCreateView bbCreateView = (BBCreateView) views.get(BB_CREATE);
            bbCreateView.setBBNameEnabled(true);
            views.put(BB_CREATE, bbCreateView);

            // navigate to home screen
            updateView(BB_CREATE);
        }
    }

    /**
     * Listener to handle Edit BB Button mouse clicks.
     */
    private class EditBBButtonListener extends MouseAdapter
    {
        /**
         * Define logic to navigate to Edit BB View when mouse clicked on BB Edit button in the BB List View.
         * BB will be populated with data from the db.
         * Errors are handled by showing pop up view.
         * @param e mouse click event
         */
        @Override
        public void mousePressed(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: Edit BB button clicked");

            // get the BB name associated to the edit button
            JButton button = (JButton) e.getSource();
            String BBName = button.getName();

            BBCreateView bbCreateView = (BBCreateView) views.get(BB_CREATE);

            bbCreateView.showBBEditingMessage(BBName);

            try {
                DbBillboard billboardObject = (DbBillboard) BillboardControl.getBillboardRequest(model.getSessionToken(), BBName);
                String xmlFile = billboardObject.getXMLCode();
                byte[] pictureData = billboardObject.getPictureData();
                Document document = bbCreateView.getXMLDocument(xmlFile);
                bbCreateView.setXMLBB(document, pictureData);

                if (billboardObject.getServerResponse()== Success)
                {
                    // set BB Name based on selected button, ensure user cannot update BB name
                    bbCreateView.setBBName(button.getName());
                    bbCreateView.setBBNameEnabled(false);
                    updateView(BB_CREATE);
                } else if (billboardObject.getServerResponse() == BillboardNotExists)
                {
                    bbCreateView.showBBInvalidErrorMessageNonExistBillboard();
                }
                else if (billboardObject.getServerResponse()== InvalidToken)
                {
                    bbCreateView.showBBInvalidErrorMessageTokenError();
                    updateView(LOGIN);
                }
                else
                {
                    bbCreateView.showMessageToUser("Error occurred. Reason: " + billboardObject.getServerResponse());
                }
            }
            catch (Exception ex)
            {
                bbCreateView.showBBInvalidErrorMessage();
            }
            views.put(BB_CREATE, bbCreateView);
        }
    }

    /**
     * Listener to handle Delete BB Button mouse clicks.
     */
    private class DeleteBBButtonListener extends MouseAdapter
    {
        /**
         * Define logic to Delete User when mouse clicked on Delete button in the User List View.
         * Errors are handled by showing pop up view if user is trying to delete own account or does not have
         * permissions.
         * @param e mouse click event
         */
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: Delete BB button clicked");

            // get the BB name associated to the edit button
            JButton button = (JButton) e.getSource();
            String BBName = button.getName();

            BBListView bbListView = (BBListView) views.get(BB_LIST);

            // ask user to confirm deletion of BB
            int response = bbListView.showDeleteConfirmation();

            // if confirmed response, delete from DB
            if (response == 0)
            {
                try {
                    ServerAcknowledge result = BillboardControl.deleteBillboardRequest(model.getSessionToken(),BBName,model.getUsername());
                    bbListView.showBBDeletedMessage(result);
                    // navigate to bb list screen to refresh screen
                    updateView(BB_MENU);
                } catch (IOException | ClassNotFoundException ex)
                {
                    bbListView.showMessageToUser("A Fatal Error has occurred. Please Restart Application");
                    System.exit(0);
                }
            }
            // nothing happens if user did not confirm deletion
            views.put(BB_LIST, bbListView);
        }
    }

    /**
     * Listener to handle View BB Button mouse clicks.
     */
    private class ViewBBButtonListener extends MouseAdapter
    {
        /**
         * Define logic to View BB in full view when mouse clicked on View BB button in the BB List View
         * @param e mouse click event
         */
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: View BB button clicked");

            // get the BB name associated to the edit button
            JButton button = (JButton) e.getSource();
            String BBName = button.getName();

            try {
                DbBillboard billboardObject = (DbBillboard) BillboardControl.getBillboardRequest(model.getSessionToken(), BBName);
                String xmlFile = billboardObject.getXMLCode();
                byte[] pictureData = billboardObject.getPictureData();
                BBViewer.displayBillboard(xmlFile, pictureData);
            }
            catch (IOException | ClassNotFoundException | IllegalComponentStateException ex)
            {
                BBListView bbListView = (BBListView) views.get(BB_LIST);
                bbListView.showBBInvalid();
                views.put(BB_LIST, bbListView);
            }
        }
    }

    /**
     * Listener to handle List BB mouse clicks.
     */
    private class ListBBListener extends MouseAdapter
    {
        /**
         * Define logic to navigate to List billboards when mouse clicked on BB List button in the Home View
         * @param e mouse click event
         */
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: List BB button clicked");
            // navigate to bb list screen
            updateView(BB_LIST);
        }
    }

    /**
     * Listener to handle background colour BB mouse clicks.
     */
    private class ColourListener extends MouseAdapter
    {
        /**
         * Define logic to allow user to select background colour and set colour in the panel & xml
         * @param e mouse click event
         */
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: Background color button clicked");

            // get list BB create
            BBCreateView bbCreateView = (BBCreateView) views.get(BB_CREATE);
            // get colour selected by user
            String newColor = bbCreateView.showColorChooser();
            if (newColor != null)
            {
                // set background colour
                bbCreateView.setBackgroundColour(newColor);
            }
            views.put(BB_CREATE, bbCreateView);
        }
    }

    /**
     * Listener to handle name BB mouse clicks.
     */
    private class NameListener implements ActionListener
    {
        /**
         * Define logic to allow user to set billboard name
         * Error pop up message if billboard name is not correct.
         * @param e mouse click event
         */
        @Override
        public void actionPerformed(ActionEvent e)
        {
            System.out.println("CONTROLLER LEVEL: BB Name button clicked");

            // get list BB create
            BBCreateView bbCreateView = (BBCreateView) views.get(BB_CREATE);
            // get BB name provided by user
            String BBName = bbCreateView.showBBNameChooser();
            String validCharacters = "([A-Za-z0-9-_ ]+)";

            // if valid BB name, then check that
            if (BBName != null)
            {
                boolean validName = BBName.matches(validCharacters);
                if (validName)
                {
                    bbCreateView.setBBName(BBName);
                }
                else
                {
                    bbCreateView.showMessageToUser("Invalid Billboard Name");
                }
            }
            views.put(BB_CREATE, bbCreateView);
        }
    }

    /**
     * Listener to handle BB create button mouse clicks.
     */
    private class BBCreateListener extends MouseAdapter
    {
        /**
         * Define logic to create BB. Ask user to confirm creation of BB. If confirmed, ask user to schedule BB.
         * Error pop up message if billboard content is not valid.
         * @param e mouse click event
         */
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: BB Create button clicked");

            // get list BB create
            BBCreateView bbCreateView = (BBCreateView) views.get(BB_CREATE);
            // get selected name
            String bbName = bbCreateView.getSelectedBBName();
            // check that a BB name has been set, if not set raise error
            if (!bbName.equals("") && bbCreateView.checkBBValid())
            {
                // raise confirmation to create BB
                int confirmCreation = bbCreateView.showConfirmationCreateBB();

                // if user confirms BB creation, show scheduling option
                if (confirmCreation == 0)
                {
                    try {
                        // get xml doc and image data
                        ArrayList<Object> xmlData = bbCreateView.getBBXMLDocument(true);

                        // convert doc to string
                        String BBXMLString = bbCreateView.getBBXMLString((Document) xmlData.get(0));

                        // get creator username
                        String creator = model.getUsername();

                        // create bb
                        ServerAcknowledge createBillboardAction = BillboardControl.createBillboardRequest(model.getSessionToken(), bbName, creator, BBXMLString, (byte[]) xmlData.get(1));

                        // if successfully created then update response from server
                        if (createBillboardAction.equals(Success))
                        {
                            // show scheduling option - asking user if they want to schedule BB now
                            int optionSelected = bbCreateView.showSchedulingOption();

                            // User Selected YES to schedule BB
                            if (optionSelected == 0) {
                                // navigate to schedule create view
                                updateView(SCHEDULE_UPDATE);
                            }
                            // User Selected NO to skip scheduling the BB
                            else {
                                // you have just created a bb message
                                bbCreateView.showBBCreatedSuccessMessage();
                                // navigate to schedule menu view
                                updateView(BB_MENU);
                            }
                        } else if (createBillboardAction.equals(BillboardNameExists)) {
                            String message = "Billboard Name already exists";
                            bbCreateView.showMessageToUser(message);
                        } else
                        {
                            String message = "Billboard Creation Unsuccessful. Reason: " + createBillboardAction ;
                            bbCreateView.showMessageToUser(message);
                        }
                    }catch (ParserConfigurationException | TransformerException | ClassNotFoundException | IOException ex)
                    {
                        String message = "Exception Occurred. Please try again. " + ex.getMessage();
                        bbCreateView.showMessageToUser(message);
                    }
                }
            }
            // if no bb name or at no element is provided, alert user to add one
            else
            {
                bbCreateView.showBBInvalidErrorMessage();
            }
        }
    }

    /**
     * BB Preview Listener to handle mouse clicks made to the Preview button in the Create BB View
     */
    private class BBPreviewListener extends MouseAdapter
    {
        /**
         * Define logic to preview Billboards in full view
         * Error pop up message if billboard xml is invalid
         * @param e mouse click event
         */
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: Preview BB button clicked");

            // get BB create view
            BBCreateView bbCreateView = (BBCreateView) views.get(BB_CREATE);

            // if bb name and bb design are valid, get info and display on viewer
            if (bbCreateView.checkBBValid())
            {
                try {
                    ArrayList<Object> xmlData = bbCreateView.getBBXMLDocument(true);
                    String xmlString = bbCreateView.getBBXMLString((Document)xmlData.get(0));
                    BBViewer.displayBillboard(xmlString, (byte[]) xmlData.get(1));
                }
                catch (IllegalComponentStateException | TransformerException | ParserConfigurationException ex)
                {
                    bbCreateView.showInvalidXMLMessage();
                }
            }
            else
            {
                String message = "Ensure to design a valid Billboard with at least 1 feature.";
                bbCreateView.showMessageToUser(message);
            }
            views.put(BB_CREATE, bbCreateView);
        }
    }

    /**
     * Listener to handle title setting in BB create view.
     */
    private class TitleListener extends MouseAdapter
    {
        /**
         * Define logic to allow user to set title of BB and colour of title
         * Error pop up message if billboard title is not correct.
         * @param e mouse click event
         */
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: Title button clicked");

            // get BB create view
            BBCreateView bbCreateView = (BBCreateView) views.get(BB_CREATE);
            // allow user to enter bb title
            String BBTitle = bbCreateView.showBBTitleChooser();
            // if not null, set bb title in text field
            if (BBTitle != null)
            {
                bbCreateView.setBBTitle(BBTitle);
                // ask user to select text colour
                String titleColour = bbCreateView.browseTitleColour();
                // if not null, set text colour
                if (titleColour != null)
                {
                    bbCreateView.setBBTitleColour(titleColour);
                }
            }
            views.put(BB_CREATE, bbCreateView);
        }
    }

    /**
     * Listener to set text in BB
     */
    private class BBTextListener extends MouseAdapter
    {
        /**
         * Define logic to allow user to set billboard text and text colour
         * @param e mouse click event
         */
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: Text button clicked");

            // get BB create view
            BBCreateView bbCreateView = (BBCreateView) views.get(BB_CREATE);

            // allow user to enter text
            String BBText = bbCreateView.showBBTextChooser();

            // if text is provided, set text field
            if (BBText != null)
            {
                bbCreateView.setBBText(BBText);
                // browse for colour of text
                String textColour = bbCreateView.browseTextColour();

                // if colour is selected set colour
                if (textColour != null)
                {
                    bbCreateView.setBBTextColour(textColour);
                }
            }
            views.put(BB_CREATE, bbCreateView);
        }
    }

    /**
     * Listener to add photos to BB either URL or upload photo.
     */
    private class BBPhotoListener extends MouseAdapter
    {
        /**
         * Define logic to allow user to add photo URL or upload an image
         * Error pop up message if errors occurred.
         * @param e mouse click event
         */
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: Photo button clicked");
            // get BB create view
            BBCreateView bbCreateView = (BBCreateView) views.get(BB_CREATE);

            // get users selection of photo type either url or data
            int response = bbCreateView.photoTypeSelection();

            try
            {
                // URL selected
                if (response == 0)
                {
                    // allow user to enter url
                    String photoURL = bbCreateView.showURLInputMessage();

                    // if not null, set photo
                    if (photoURL != null)
                    {
                        ArrayList<Object> imageData = null;
                        imageData = bbCreateView.getImageData(photoURL);
                        bbCreateView.setPhoto((ImageIcon)imageData.get(0), BBCreateView.PhotoType.URL, imageData.get(1));
                    }
                    // else show error
                    else
                    {
                        bbCreateView.showURLErrorMessage();
                    }
                }
                // if personal photo selected, allow user to select photo from file
                else if (response == 1)
                {
                    // get photo selected - this contains Image Icon and byte array
                    ArrayList<Object> photoData = bbCreateView.browsePhotos();
                    bbCreateView.setPhoto((ImageIcon)photoData.get(0), BBCreateView.PhotoType.DATA, (String)photoData.get(1));
                }
                // if clear button is selected, remove current image
                else if (response == 2)
                {
                    bbCreateView.setPhoto(null, null, null);
                }
            }
            catch (Exception ex)
            {
                bbCreateView.showMessageToUser("Unable to Import Photo.");
            }

            views.put(BB_CREATE, bbCreateView);
        }
    }

    /**
     * Listener to handle xml import
     */
    private class BBXMLImportListener extends MouseAdapter
    {
        /**
         * Define logic to allow user to import xml
         * Error pop up message if xml is invalid
         * @param e mouse click event
         */
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: XML import button clicked");

            // get BB create view
            BBCreateView bbCreateView = (BBCreateView) views.get(BB_CREATE);
            try
            {
                String path = bbCreateView.browseXMLImport();
                Document doc = bbCreateView.createXMLDoc(path);
                byte[] noPictureData = new byte[]{};
                bbCreateView.setXMLBB(doc, noPictureData);
            }
            catch (Exception ex)
            {
                bbCreateView.showMessageToUser("Unable to import Billboard XML. Reason: " + ex.getMessage());
            }
            views.put(BB_CREATE, bbCreateView);
        }
    }

    /**
     * Listener to handle xml export BB mouse clicks.
     */
    private class BBXMLExportListener extends MouseAdapter
    {
        /**
         * Define logic to allow user to export XML
         * Select filename and folder path and if successful store the xml file to user's selected folder
         * Error pop up message if billboard xml is not correct.
         * @param e mouse click event
         */
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: XML export button clicked");

            // get BB Create view
            BBCreateView bbCreateView = (BBCreateView) views.get(BB_CREATE);

            try
            {
                // if BB is valid, show folder selector
                if (bbCreateView.checkBBValid())
                {
                    // select folder
                    int value = bbCreateView.showFolderChooserSelector();

                    // if selection is approved, proceed
                    if(value == JFileChooser.APPROVE_OPTION)
                    {
                        // get filename from user
                        String filename = bbCreateView.enterXMLFileName();

                        // if filename is valid, proceed to export xml
                        if (!filename.equals(""))
                        {
                            // get selected folder path
                            String path = bbCreateView.browseExportFolder();

                            ArrayList<Object> xmlData = bbCreateView.getBBXMLDocument(false);

                            String absolutePhotoPath = path + "\\" + filename + ".xml";

                            // success value indicates if xml was successfully converted to file
                            bbCreateView.xmlExport(absolutePhotoPath, (Document) xmlData.get(0));

                            // show success message
                            bbCreateView.showSuccessfulExport();
                        }
                        else
                        {
                            String message = "No filename selected - XML not exported.";
                            JOptionPane.showMessageDialog(null, message);
                        }
                    }
                }
                // if BB is invalid, show error message
                else
                {
                    String message = "Invalid Billboard. Please ensure to select at least a title, text or picture.";
                    JOptionPane.showMessageDialog(null, message);
                }
            }
            catch(ParserConfigurationException | TransformerException ex)
            {
                String message = "Invalid Action - Exception occurred.";
                JOptionPane.showMessageDialog(null, message);
            }
            views.put(BB_CREATE, bbCreateView);
        }
    }

    //---------------------------------- SCHEDULE LISTENERS ------------------------------

    /**
     * Listener to handle Schedule Button mouse clicks.
     * If user clicks the Schedule button, user is navigated to Schedule Menu view.
     */
    private class ScheduleButtonListener extends MouseAdapter
    {
        /**
         * Define logic to allow user to navigate to schedule menu
         * @param e mouse click event
         */
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: Schedule button clicked");
            // navigate to home screen
            updateView(SCHEDULE_MENU);
        }
    }

    /**
     * Listener to handle View Schedule Button mouse clicks.
     */
    private class ScheduleViewButtonListener extends MouseAdapter
    {
        /**
         * Define logic to allow user to view schedule week view
         * @param e mouse click event
         */
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: View Schedule button clicked");
            // navigate to edit schedule week view
            updateView(SCHEDULE_WEEK);
        }
    }

    /**
     * Listener to handle Create/Update Schedule Button mouse clicks. This is to allow user to navigate to the
     * Schedule Create/Update view.
     */
    private class ScheduleCreateButtonListener extends MouseAdapter
    {
        /**
         * Define logic to allow user to navigate to the Schedule Update/Create View
         * @param e mouse click event
         */
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: Create Schedule button clicked");
            // navigate to edit schedule update view
            updateView(SCHEDULE_UPDATE);
        }
    }

    /**
     * Listener to handle show duration label in Schedule Create
     */
    private class ScheduleDurationListener implements ItemListener
    {
        /**
         * Define logic to set Duration label and variable upon selecting a start or end time.
         * @param e mouse click event
         */
        @Override
        public void itemStateChanged(ItemEvent e)
        {
            // get event id
            int eventId = e.getStateChange();
            // only process if item was selected
            if (eventId == ItemEvent.SELECTED)
            {
                System.out.println("CONTROLLER LEVEL: Time changed");
                // calculate the duration in the schedule update view
                ScheduleUpdateView scheduleUpdateView = (ScheduleUpdateView) views.get(SCHEDULE_UPDATE);
                scheduleUpdateView.calcDuration();
                views.put(SCHEDULE_UPDATE, scheduleUpdateView);
            }
        }
    }

    /**
     * Listener to handle Schedule Radio Button mouse clicks - these define the recurrence pattern of the schedule
     */
    private class ScheduleRadioButtonListener implements ActionListener
    {
        /**
         * Define logic to handle clicks made to the radio buttons for recurrence
         * Error pop up message if error occurred.
         * @param e mouse click event
         */
        @Override
        public void actionPerformed(ActionEvent e)
        {
            System.out.println("CONTROLLER LEVEL: Recurrence button clicked");
            // get button source
            JRadioButton button = (JRadioButton) e.getSource();
            ScheduleUpdateView scheduleUpdateView = (ScheduleUpdateView) views.get(SCHEDULE_UPDATE);
            // get button name
            String buttonName = button.getName();

            try {
                // switch case to handle radio button selected
                switch (buttonName) {
                    case "hourly":
                        // show message
                        scheduleUpdateView.showHourlyMessage();
                        scheduleUpdateView.enableMinuteSelector(false);
                        break;
                    case "no repeats":
                        // show message
                        scheduleUpdateView.showNoRepeatMessage();
                        scheduleUpdateView.enableMinuteSelector(false);
                        break;
                    case "minute":
                        // show message
                        scheduleUpdateView.showMinuteMessage();
                        scheduleUpdateView.enableMinuteSelector(true);
                        int minuteRepeat = scheduleUpdateView.getMinuteRepeat();
                        // set minute label if greater than 0
                        if (minuteRepeat > 0) {
                            scheduleUpdateView.setMinuteLabel(minuteRepeat);
                        }
                        break;
                }
            }
            catch (Exception ex)
                {
                    scheduleUpdateView.showMessageToUser("Error Encountered. Reason: " + ex.getMessage());
                }
            views.put(SCHEDULE_UPDATE, scheduleUpdateView);
        }
    }

    /**
     * Listener to handle Minute Repeat item clicks.
     */
    private class ScheduleMinuteRepeatListener implements ItemListener
    {
        /**
         * Define logic to handle clicks made to minutes selected
         * @param e item click event
         */
        @Override
        public void itemStateChanged(ItemEvent e)
        {
            // get event id
            int eventId = e.getStateChange();
            // if event is a selected item event, proceed
            if (eventId == ItemEvent.SELECTED)
            {
                System.out.println("CONTROLLER LEVEL: Minute Repeat button clicked");
                ScheduleUpdateView scheduleUpdateView = (ScheduleUpdateView) views.get(SCHEDULE_UPDATE);
                // get menu item selected which is associated to a minute value
                JComboBox menuItem = (JComboBox) e.getSource();
                int minuteSelected = (int) menuItem.getSelectedItem();
                // set minute value
                scheduleUpdateView.setMinuteLabel(minuteSelected);
                views.put(SCHEDULE_UPDATE, scheduleUpdateView);
            }
        }
    }

    /**
     * Listener to handle Schedule Clear mouse clicks.
     */
    private class ScheduleClearButtonListener extends MouseAdapter {
        /**
         * Define logic to allow user to clear the schedule logic
         * Error pop up message if error/exception occurred
         * @param e mouse click event
         */
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: Schedule Clear button clicked");
            ScheduleUpdateView scheduleUpdateView = (ScheduleUpdateView) views.get(SCHEDULE_UPDATE);
            // ask user for confirmation to clear the schedule
            int clear = scheduleUpdateView.showScheduleClearConfirmation();
            // if user confirms deletion of schedule
            if (clear == 0)
            {
                String BBName = scheduleUpdateView.getSelectedBBName();
                Server.ServerAcknowledge result = null;
                try {
                    result = ScheduleControl.deleteScheduleRequest(model.getSessionToken(), BBName);
                } catch (IOException | ClassNotFoundException ex) {
                    scheduleUpdateView.showMessageToUser("A Fatal Error has occurred. Please Restart Application");
                    System.exit(0);
                }
                if(result.equals(Success)){
                    scheduleUpdateView.showMessageToUser("Schedule Removed and Cleared!");
                    // navigate back to schedule menu
                    updateView(SCHEDULE_MENU);
                } else if (result.equals(BillboardNotExists)) {
                    scheduleUpdateView.showMessageToUser("Billboard does not exist! Please try again or check data!");
                } else if (result.equals(ScheduleNotExists)) {
                    scheduleUpdateView.showMessageToUser("Schedule does not exist! Please try again or check data!");
                } else if (result.equals(InvalidToken)) {
                    scheduleUpdateView.showMessageToUser("Invalid Token! Please re-login to reauthenticate!");
                    updateView(LOGIN);
                } else if (result.equals(InsufficientPermission)) {
                scheduleUpdateView.showMessageToUser("Invalid Permissions!");
                }
            }
            views.put(SCHEDULE_UPDATE, scheduleUpdateView);
        }
    }

    /**
     * Listener to handle BB Schedules to populate information
     */
    private class SchedulePopulateListener implements ItemListener
    {
        /**
         * Define logic to populate schedule data upon selecting a new billboard name
         * Error pop up message if schedule is invalid
         * @param e mouse click event
         */
        @Override
        public void itemStateChanged(ItemEvent e)
        {
            // get event id
            int eventId = e.getStateChange();
            // process event if BB Name has been SELECTED
            if (eventId == ItemEvent.SELECTED)
            {
                System.out.println("CONTROLLER LEVEL: Schedule Populate button clicked");

                // get bb name that has been selected
                JComboBox menuItem = (JComboBox) e.getSource();
                String bbName = (String)menuItem.getSelectedItem();

                ScheduleUpdateView scheduleUpdateView = (ScheduleUpdateView) views.get(SCHEDULE_UPDATE);
                ScheduleInfo schedule = null;
                try {
                    // get schedule info for bb
                    schedule = (ScheduleInfo) ScheduleControl.listABillboardSchedule(model.getSessionToken(), bbName);
                    // if schedule exists, proceed
                    if (schedule.getScheduleBillboardName() != null)
                    {
                        Boolean sunday = schedule.getSunday().equals("1");
                        Boolean monday = schedule.getMonday().equals("1");
                        Boolean tuesday = schedule.getTuesday().equals("1");
                        Boolean wednesday = schedule.getWednesday().equals("1");
                        Boolean thursday = schedule.getThursday().equals("1");
                        Boolean friday = schedule.getFriday().equals("1");
                        Boolean saturday = schedule.getSaturday().equals("1");
                        String startTime = schedule.getStartTime();
                        Integer duration = Integer.parseInt(schedule.getDuration().trim());
                        Integer minRepeat = Integer.parseInt(schedule.getRepeat().trim());

                        ArrayList<Boolean> daysOfWeek= new ArrayList<Boolean>(Arrays.asList(monday,tuesday,wednesday,thursday,friday,saturday,sunday));
                        String recurrenceButton;

                        if(minRepeat.equals(60))
                        {
                            recurrenceButton = "hourly";
                        } else if (minRepeat.equals(0))
                        {
                            recurrenceButton = "no repeats";
                        } else {
                            recurrenceButton = "minute";
                        }
                        String AMPMTag = "";
                        Integer startHour = Integer.parseInt(startTime.substring(0, 2));

                        // Check AM PM
                        if (startHour >= 12){
                            AMPMTag = "PM";
                        } else {
                            AMPMTag = "AM";
                        }

                        // Change hour to 12hour format
                        if (startHour > 12){
                            startHour -= 12;
                        }

                        Integer startMin = Integer.parseInt(startTime.substring(3, 5));

                        scheduleUpdateView.setScheduleValues(daysOfWeek, startHour, startMin, duration, recurrenceButton, minRepeat, AMPMTag);
                    }
                    else
                    {
                        scheduleUpdateView.showNoExistingScheduleMessage();
                        scheduleUpdateView.removeScheduleSelection();
                    }
                } catch (IOException | ClassNotFoundException ex)
                {
                    scheduleUpdateView.showScheduleErrorMessage();
                }
                views.put(SCHEDULE_UPDATE, scheduleUpdateView);
            }
        }
    }

    /**
     * Listener to handle Submit Schedule Button mouse clicks.
     */
    private class ScheduleSubmitButtonListener extends MouseAdapter
    {
        /**
         * Define logic to create schedule and save to DB
         * Error pop up message if error occurred. Error shown when schedule created by user is invalid.
         * @param e mouse click event
         */
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: Create Schedule button clicked");

            ScheduleUpdateView scheduleUpdateView = (ScheduleUpdateView) views.get(SCHEDULE_UPDATE);

            // id validation is not valid, raise error
            if (!scheduleUpdateView.checkValidDuration())
            {
                scheduleUpdateView.raiseDurationError();
            }
            // if day is not valid, raise error
            else if (!scheduleUpdateView.checkValidDaySelected())
            {
                scheduleUpdateView.raiseDayError();
            }
            // if BB is not valid, raise error
            else if (!scheduleUpdateView.checkValidBB())
            {
                scheduleUpdateView.raiseBBError();
            }
            // if everything is valid, proceed
            else
            {
                // ask user to confirm submission of schedule
                int response = scheduleUpdateView.showConfirmationCreateSchedule();

                // confirmation response
                if (response == 0)
                {
                    Server.ServerAcknowledge returnResult = null;
                    ArrayList<Object> scheduleInfo = scheduleUpdateView.getScheduleInfo();
                    try {
                        // update schedule
                        returnResult = ScheduleControl.updateScheduleBillboardRequest(model.getSessionToken(),scheduleInfo);
                        if(returnResult.equals(Success)){
                            scheduleUpdateView.showConfirmationDialog();
                            updateView(SCHEDULE_MENU);
                        } else if (returnResult.equals(BillboardNotExists)){
                            scheduleUpdateView.showMessageToUser("Billboard does not exist! Please try again or check data!");
                        } else if (returnResult.equals(InsufficientPermission)){
                            scheduleUpdateView.showMessageToUser("Schedule not created. Insufficient User Permission!");
                        } else if (returnResult.equals(InvalidToken)){
                            scheduleUpdateView.showMessageToUser("Invalid Token! Please re-login to reauthenticate!");
                            updateView(LOGIN);
                        }
                        views.put(SCHEDULE_UPDATE, scheduleUpdateView);

                    } catch (IOException | ClassNotFoundException ioException) {
                        scheduleUpdateView.showMessageToUser("A Fatal Error has occurred. Please Restart Application");
                        System.exit(0);
                    }
                }
            }
        }
    }

    /**
     * Schedule Update is designed to update the schedule Update View with the all billboard names from the db.
     * If an error occurs, exit application.
     */
    private void scheduleUpdate()
    {
        ScheduleUpdateView scheduleUpdateView = (ScheduleUpdateView) views.get(SCHEDULE_UPDATE);
        scheduleUpdateView.setWelcomeText(model.getUsername());
        try {
            BillboardList billboardList = (BillboardList) BillboardControl.listBillboardRequest(model.getSessionToken());
            if (!billboardList.getBillboardNames().isEmpty())
            {
                ArrayList<String> stringArray = billboardList.getBillboardNames();
                scheduleUpdateView.setBBNamesFromDB(stringArray);
                scheduleUpdateView.showInstructionMessage();
                views.put(SCHEDULE_UPDATE, scheduleUpdateView);
            }
        } catch (IOException | ClassNotFoundException e)
        {
            scheduleUpdateView.showMessageToUser("A Fatal Error has occurred. Please Restart Application");
            System.exit(0);
        }
    }

    /**
     * Schedule Week View is designed to update the Schedule Week view with all the schedule information for the weekly
     * calendar view. This schedule information contains the creator, start and end time, and billboard name.
     */
    private void scheduleWeek()
    {
        ScheduleWeekView scheduleWeekView = (ScheduleWeekView) views.get(SCHEDULE_WEEK);
        scheduleWeekView.setWelcomeText(model.getUsername());

        // initialise
        ArrayList<ArrayList<String>> scheduleMonday = new ArrayList<>();
        ArrayList<ArrayList<String>> scheduleTuesday = new ArrayList<>();
        ArrayList<ArrayList<String>> scheduleWednesday = new ArrayList<>();
        ArrayList<ArrayList<String>> scheduleThursday = new ArrayList<>();
        ArrayList<ArrayList<String>> scheduleFriday = new ArrayList<>();
        ArrayList<ArrayList<String>> scheduleSaturday = new ArrayList<>();
        ArrayList<ArrayList<String>> scheduleSunday = new ArrayList<>();

        try {
            scheduleMonday = (ArrayList<ArrayList<String>>) ScheduleControl.listDayScheduleRequest(model.getSessionToken(), "Monday");
            scheduleTuesday = (ArrayList<ArrayList<String>>) ScheduleControl.listDayScheduleRequest(model.getSessionToken(), "Tuesday");
            scheduleWednesday = (ArrayList<ArrayList<String>>) ScheduleControl.listDayScheduleRequest(model.getSessionToken(), "Wednesday");
            scheduleThursday = (ArrayList<ArrayList<String>>) ScheduleControl.listDayScheduleRequest(model.getSessionToken(), "Thursday");
            scheduleFriday = (ArrayList<ArrayList<String>>) ScheduleControl.listDayScheduleRequest(model.getSessionToken(), "Friday");
            scheduleSaturday = (ArrayList<ArrayList<String>>) ScheduleControl.listDayScheduleRequest(model.getSessionToken(), "Saturday");
            scheduleSunday = (ArrayList<ArrayList<String>>) ScheduleControl.listDayScheduleRequest(model.getSessionToken(), "Sunday");

            // Billboard Schedule: day, time, bb name
            ArrayList<ArrayList<ArrayList<String>>> schedule = new ArrayList<>();
            if(scheduleMonday.get(0).get(1) != null){
                schedule.add(scheduleMonday);
            } else{
                schedule.add(null);
            }
            if(scheduleTuesday.get(0).get(1) != null){
                schedule.add(scheduleTuesday);
            } else{
                schedule.add(null);
            }
            if(scheduleWednesday.get(0).get(1) != null){
                schedule.add(scheduleWednesday);
            } else{
                schedule.add(null);
            }
            if(scheduleThursday.get(0).get(1) != null){
                schedule.add(scheduleThursday);
            } else{
                schedule.add(null);
            }
            if(scheduleFriday.get(0).get(1) != null){
                schedule.add(scheduleFriday);
            } else{
                schedule.add(null);
            }
            if(scheduleSaturday.get(0).get(1) != null){
                schedule.add(scheduleSaturday);
            } else{
                schedule.add(null);
            }
            if(scheduleSunday.get(0).get(1) != null){
                schedule.add(scheduleSunday);
            } else{
                schedule.add(null);
            }

            scheduleWeekView.populateSchedule(schedule);
        } catch (IOException | ClassNotFoundException e)
        {
            scheduleWeekView.showMessageToUser("A Fatal Error has occurred. Please Restart Application");
            System.exit(0);
        }

        views.put(SCHEDULE_WEEK, scheduleWeekView);
    }

    /**
     * Show BB list view and populate the BB names from the database.
     */
    private void BBList()
    {
        // get list BB view
        BBListView bbListView = (BBListView) views.get(BB_LIST);
        bbListView.setWelcomeText(model.getUsername());
        try {
            BillboardList billboard_List = (BillboardList) BillboardControl.listBillboardRequest(model.getSessionToken());
            ArrayList<String> BBListArray = billboard_List.getBillboardNames();
            // Check if not null to add billboard list else return error message
            if (!billboard_List.getServerResponse().equals("Fail: No Billboard Exists")){
                bbListView.addContent(BBListArray, new EditBBButtonListener(), new DeleteBBButtonListener(), new ViewBBButtonListener());
            } else {
                bbListView.showMessageToUser("Billboard List is empty! Please Create a new Billboard.");
            }
        } catch (IOException | ClassNotFoundException ex) {
            bbListView.showMessageToUser("A Fatal Error has occurred. Please Restart Application");
            System.exit(0);
        }
        views.put(BB_LIST, bbListView);
    }
}