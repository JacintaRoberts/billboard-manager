package controlPanel;

import controlPanel.Main.VIEW_TYPE;
import server.BillboardList;
import server.DbBillboard;
import server.ScheduleInfo;
import server.Server;
import server.Server.ServerAcknowledge;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;

import static controlPanel.Main.VIEW_TYPE.*;
import static controlPanel.UserControl.loginRequest;
import static controlPanel.UserControl.logoutRequest;
import static server.Server.ServerAcknowledge.*;

/**
 * Controller Class is designed to manage user inputs appropriately, sending requests to the server and updating the model/gui when required.
 * The constructor stores a reference to the model and views, and adds listeners to the views.
 */
public class Controller
{
    // store model and view
    private Model model;
    private HashMap<VIEW_TYPE, AbstractView> views;
    private Object serverResponse;
    private BBFullPreview BBViewer;

    /*
     #################################### CONSTRUCTOR ####################################
     */

    /**
     * Controller Constructor stores instances of Views, adds listeners to Views and sets up Log In View allowing users
     * to log in to the application on startup.
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
     * addGenericListener is designed to add the Home, Back, Log Out and Profile button to the defined view.
     * @param view_type GUI view type
     */
    private void addGenericListeners(VIEW_TYPE view_type)
    {
        AbstractGenericView genericView = (AbstractGenericView) views.get(view_type);
        genericView.addHomeButtonListener(new HomeButtonListener());
        genericView.addBackButtonListener(new BackButtonListener());
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
        logInView.addSubmitButtonListener(new SubmitButtonListener());
        views.put(LOGIN, logInView);
    }

    /**
     * HOME LISTENERS: designed to add listeners to the HOME VIEW.
     * Listeners include: User Menu, BB Menu, Schedule Menu, Profile and Back Button.
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
        homeView.addBackButtonListener(new BackButtonListener());
        homeView.addLogOutListener(new LogOutButtonListener());
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
        usersMenuView.addCreateUserButtonListener(new CreateUserButtonListener());
        views.put(USERS_MENU, usersMenuView);
    }

    /**
     * USER VIEW (PROFILE) LISTENERS: designed to add listeners to the USER VIEW.
     * Listeners include: Home, Back and Edit button.
     */
    private void addUserProfileListener()
    {
        UserProfileView userProfileView = (UserProfileView) views.get(USER_PROFILE);
        // add listeners
        userProfileView.addHomeButtonListener(new HomeButtonListener());
        userProfileView.addBackButtonListener(new BackButtonListener());
        userProfileView.addEditButtonListener(new EditProfileButtonListener());
        views.put(USER_PROFILE, userProfileView);
    }

    /**
     * USER CREATE LISTENERS: designed to add listeners to
     * Listeners include: Home, Back and Profile Button. Please Note: additional listeners are added dynamically upon
     * navigating to this VIEW (i.e. User Edit, Delete, View).
     */
    private void addUserListListener()
    {
        addGenericListeners(USER_LIST);
    }

    /**
     * USER EDIT LISTENERS: designed to add listeners to the USER EDIT VIEW.
     * Listeners include: Home, Back and Profile Button, including User Permission Update and Password Update Buttons.
     */
    private void addUserEditListener()
    {
        addGenericListeners(USER_EDIT);
        UserEditView userEditView = (UserEditView) views.get(USER_EDIT);
        userEditView.addSubmitButtonListener(new UserPermissionUpdateListener());
        userEditView.addPasswordButtonListener(new UserPasswordUpdateButtonListener());
        views.put(USER_EDIT, userEditView);
    }

    /**
     * USER PREVIEW LISTENERS: designed to add listeners to the USER VIEW VIEW.
     * Listeners include: Home, Back and Profile Button.
     */
    private void addUserPreviewListener()
    {
        addGenericListeners(USER_VIEW);
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
    }

    //---------------------------------- SCHEDULE LISTENER ------------------------------

    /**
     * SCHEDULE MENU LISTENERS: designed to add listeners to the SCHEDULE MENU VIEW.
     * Listeners include: Home, Back and Profile. Including Schedule View and Schedule Create buttons.
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
     * Listeners include: Home, Back and Profile
     */
    private void addScheduleWeekListener()
    {
        addGenericListeners(SCHEDULE_WEEK);
    }

    /**
     * SCHEDULE UPDATE LISTENERS: designed to add listeners to the SCHEDULE UPDATE VIEW.
     * Listeners include: Home, Back and Profile, including a range of buttons for the schedule design.
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
        views.put(SCHEDULE_UPDATE, scheduleUpdateView);
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
        // hide old view
        hideView(model.getCurrentView());
        // set up new frame
        showView(newView);
    }

    /**
     * hideView is designed to hide old view by:
     * - detaching observer to stop listening to model updates
     * - clean up gui
     * - setting old view as invisible
     * - storing old view in hash map
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
     * - update components if required
     * - storing updated view in hash map
     */
    private void showView(VIEW_TYPE newViewType)
    {
        // get new view
        AbstractView view = views.get(newViewType);
        // attach observer (this listens for model updates)
        model.attachObserver(view);
        // set current view in model
        model.setCurrentView(view.getEnum());
        // set view as visible
        view.setVisible(true);
        // update components based on permissions
        updateComponents(newViewType);
        // update hashmap with updated view
        views.put(newViewType, view);
    }

    /**
     * Designed to update components before navigating to the view.
     * @param newViewType
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
                ScheduleWeekView scheduleWeekView = (ScheduleWeekView) views.get(SCHEDULE_WEEK);
                scheduleWeekView.setWelcomeText(model.getUsername());

                // Initilaise
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
                    }
                    if(scheduleTuesday.get(0).get(1) != null){
                        schedule.add(scheduleTuesday);
                    }
                    if(scheduleWednesday.get(0).get(1) != null){
                        schedule.add(scheduleWednesday);
                    }
                    if(scheduleThursday.get(0).get(1) != null){
                        schedule.add(scheduleThursday);
                    }
                    if(scheduleFriday.get(0).get(1) != null){
                        schedule.add(scheduleFriday);
                    }
                    if(scheduleSaturday.get(0).get(1) != null){
                        schedule.add(scheduleSaturday);
                    }
                    if(scheduleSunday.get(0).get(1) != null){
                        schedule.add(scheduleSunday);
                    }

                    scheduleWeekView.populateSchedule(schedule);
                } catch (IOException | ClassNotFoundException e)
                {
                    scheduleWeekView.showMessageToUser("A Fatal Error has occurred. Please Restart Application");
                }

                views.put(SCHEDULE_WEEK, scheduleWeekView);
                break;

            // SCHEDULE_UPDATE: set welcome text, populate schedule from DB
            case SCHEDULE_UPDATE:
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
                }
                break;

            // SCHEDULE_MENU: set welcome text
            case SCHEDULE_MENU:
                ScheduleMenuView scheduleMenuView = (ScheduleMenuView) views.get(SCHEDULE_MENU);
                scheduleMenuView.setWelcomeText(model.getUsername());
                views.put(SCHEDULE_MENU, scheduleMenuView);
                break;

            // USER_LIST: set welcome text, populate user info
            case USER_LIST:
                listUserHandling();
                break;

            // USER_EDIT: set welcome text
            case USER_EDIT:
                UserEditView userEditView = (UserEditView) views.get(USER_EDIT);
                userEditView.setWelcomeText(model.getUsername());
                // FIXME: move logic here?
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
                // FIXME: move logic here?
                views.put(USER_PROFILE, userProfileView);
                break;

            // USER_VIEW: set welcome text, populate user info
            case USER_VIEW:
                UserPreviewView userPreviewView = (UserPreviewView) views.get(USER_VIEW);
                userPreviewView.setWelcomeText(model.getUsername());
                // FIXME: move logic here?
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
                }
                views.put(BB_LIST, bbListView);
                break;

            // BB_CREATE: set welcome text
            case BB_CREATE:
                BBCreateView bbCreateView = (BBCreateView) views.get(BB_CREATE);
                bbCreateView.setWelcomeText(model.getUsername());
                // FIXME: move logic in here?
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
            UserProfileView userProfileView = (UserProfileView) views.get(USER_PROFILE);

            String username = model.getUsername();

            // set username, password and permissions in Profile View
            userProfileView.setUsername(username);

            // Get user permissions from server
            getUserPermissionsFromServer(userProfileView, USER_PROFILE, username);

            views.put(USER_PROFILE, userProfileView);

            // navigate to home screen
            updateView(USER_PROFILE);
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
            views.put(viewType, userView);
        } catch (IOException | ClassNotFoundException ex)
        {
            userView.showFatalError();
            views.put(viewType, userView);
            // TODO: JACINTA - FIX ME terminate Control Panel and restart
            ex.printStackTrace();
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
                updateView(LOGIN);
            }
            views.put(viewType, userView);
        }
    }


    /**
     * Listener to handle Log Out Button mouse clicks. User is navigated to Log In Screen.
     */
    private class LogOutButtonListener extends MouseAdapter
    {
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
                // FIXME: JACINTA - FIX ME - terminate Control Panel and restart
                // FIXME: need to ensure, if exception raised - code below  (checking serverResponse value )cannot be reached
                ex.printStackTrace();
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
                // TODO: JACINTA - FIX ME - terminate Control Panel and restart
                ex.printStackTrace();
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

            // navigate to user edit screen
            updateView(USER_EDIT);
        }
    }

    /**
     * Listener to handle Create User Button mouse clicks.
     * This will navigate the user to the User Create view.
     */
    private class CreateUserButtonListener extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: Create User button clicked");
            // navigate to create user screen
            updateView(USER_CREATE);
        }
    }

    /**
     * Listener to handle Submit New User Button mouse clicks.
     */
    private class UserPermissionUpdateListener extends MouseAdapter
    {
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
                    // TODO: JACINTA _ FIX ME - terminate Control Panel and restart
                    ex.printStackTrace();
                }
            }
            // do nothing if user does not confirm user permission update
        }
    }

    /**
     * Listener to handle Create User Button mouse clicks.
     */
    private class UserCreateButtonListener extends MouseAdapter
    {
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
                        ex.printStackTrace();
                        userCreateView.showFatalError();
                        // TODO: JACINTA  - terminate Control Panel and restart
                        // TODO - ensure that code cannot reach the below code, if an exception is raised
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
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: User Password Set button clicked");

            UserEditView userEditView = (UserEditView) views.get(USER_EDIT);

            // get password from user
            // FIXME - check if valid??? PATRICE
            String password = userEditView.showNewPasswordInput();

            // ask user for confirmation of editing password
            int response = userEditView.showUserConfirmation();

            // Confirm response of updated password
            if  (response == 0) {
                if (password == null)
                { // Ensure the user enters a valid password (not empty)
                    userEditView.showEnterValidPasswordException();
                }
                else
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
                        // TODO: JACINTA - FIX ME terminate Control Panel and restart
                        ex.printStackTrace();
                    }
                }
                views.put(USER_EDIT, userEditView);
            }
        }
    }

    /**
     * Listener to handle User Password Create Button.
     */
    private class UserPasswordCreateButtonListener extends MouseAdapter
    {
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
                    // TODO: JACINTA - terminate Control Panel and restart - ensure that code below cannot be reached
                    ex.printStackTrace();
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
     * Listener to handle View User Button mouse clicks.
     */
    private class ViewUserButtonListener extends MouseAdapter
    {
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

            updateView(USER_VIEW);
        }
    }

    /**
     * Listener to handle List Users mouse clicks.
     */
    private class ListUsersListener extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e) {
            System.out.println("CONTROLLER LEVEL: List Users button clicked");
            // navigate to users list screen
            updateView(USER_LIST);
        }
    }

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
            // TODO: JACINTA - TO FIX - terminate Control Panel and restart - ensure below code cannot be reached
            ex.printStackTrace();
        } catch (ClassCastException ex) {
            // Otherwise, some other error message was returned from the server
            errorMessage = (ServerAcknowledge) serverResponse;
        }

        // Error handling on GUI as follows
        if (errorMessage.equals(InsufficientPermission)) {
            System.out.println("CONTROLLER LEVEL - Insufficient Permissions");
            userListView.showInsufficientPermissionsException();
            updateView(USERS_MENU); // FIXME - TEST PATRICE
        } else if (serverResponse.equals(InvalidToken)) {
            System.out.println("CONTROLLER LEVEL - Invalid Token");
            userListView.showInvalidTokenException();
            updateView(LOGIN); // FIXME - TEST PATRICE
        } else { // Successful, let the user know and populate with list of users
            userListView.addContent(usernames, new EditUserButtonListener(), new DeleteUserButtonListener(), new ViewUserButtonListener());
        }
        views.put(USER_LIST, userListView);
    }

    /**
     * Listener to handle Edit Profile Button mouse clicks.
     */
    private class EditProfileButtonListener extends MouseAdapter
    {
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

            // navigate to edit users
            updateView(USER_EDIT);
        }
    }

    //---------------------------------- BB LISTENERS ------------------------------

    /**
     * Listener to handle BBMenu Button mouse clicks.
     *      * If user clicks the BBMenu button, user is navigated to BB Menu view.
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
                DbBillboard billboardObject = null;
                billboardObject = (DbBillboard) BillboardControl.getBillboardRequest(model.getSessionToken(), BBName);
                String xmlFile = billboardObject.getXMLCode();
                byte[] pictureData = billboardObject.getPictureData();
                boolean valid = bbCreateView.addBBXML(xmlFile, pictureData);

                if (valid)
                {
                    // set BB Name based on selected button, ensure user cannot update BB name
                    bbCreateView.setBBName(button.getName());
                    bbCreateView.setBBNameEnabled(false);
                    updateView(BB_CREATE);

                } else if (billboardObject.getServerResponse().equals("Fail: Billboard Does not Exist")){
                    bbCreateView.showBBInvalidErrorMessageNonExistBillboard(); // FIXME - should this be on listBBView
                } else if (billboardObject.getServerResponse().equals("Fail: Session was not valid")) {
                    bbCreateView.showBBInvalidErrorMessageTokenError(); // FIXME - should this be on listBBView
                }
            }
            catch (IOException | ClassNotFoundException ex)
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
                    // FIXME: ALAN - NEED TO HANDLE EXCEPTION
                    ex.printStackTrace();
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
        @Override
        public void actionPerformed(ActionEvent e)
        {
            System.out.println("CONTROLLER LEVEL: BB Name button clicked");

            // get list BB create
            BBCreateView bbCreateView = (BBCreateView) views.get(BB_CREATE);
            // get BB name provided by user
            String BBName = bbCreateView.showBBNameChooser();
            // FIXME : ALAN REGEX to ensure BB NAME IS VALID - change true below to a regex check
            boolean validName = true;
            // if valid BB name, then check that
            if (BBName != null)
            {
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
    private class BBCreateListener extends MouseAdapter {

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
                        // get XML string and picture data
                        ArrayList<Object> BBXMLString = bbCreateView.getBBXMLString();
                        // if not null, then create BB
                        if (BBXMLString != null)
                        {
                            // get creator username
                            String creator = model.getUsername();

                            // create bb
                            ServerAcknowledge createBillboardAction = BillboardControl.createBillboardRequest(model.getSessionToken(), bbName, creator, (String)BBXMLString.get(0), (byte[])BBXMLString.get(1));

                            // if successfully created then update response from server
                            if (createBillboardAction.equals(Success))
                            {
                                // show scheduling option - asking user if they want to schedule BB now
                                int optionSelected = bbCreateView.showSchedulingOption();

                                // User Selected YES to schedule BB
                                if (optionSelected == 0)
                                {
                                    // navigate to schedule create view
                                    updateView(SCHEDULE_UPDATE);
                                }
                                // User Selected NO to skip scheduling the BB
                                else if (optionSelected == 1)
                                {
                                    // you have just created a bb message
                                    bbCreateView.showBBCreatedSuccessMessage();
                                    // navigate to schedule menu view
                                    updateView(SCHEDULE_MENU);
                                }
                            }
                            else if (createBillboardAction.equals(BillboardNameExists))
                            {
                                String message = "Billboard Name already exists";
                                bbCreateView.showMessageToUser(message);
                            }
                            else
                            {
                                String message = "Billboard Creation Unsuccessful. Try again.";
                                bbCreateView.showMessageToUser(message);
                            }
                        }
                    } catch ( IOException | ClassNotFoundException ex)
                    {
                        String message = "Error encountered whilst creating BB. Exception " + ex.toString();
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
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: Preview BB button clicked");

            // get BB create view
            BBCreateView bbCreateView = (BBCreateView) views.get(BB_CREATE);

            // get selected BB
            String bbName = bbCreateView.getSelectedBBName();

            // if bb name and bb design are valid, get info and display on viewer
            if ((bbName != null || !bbName.equals("")) && bbCreateView.checkBBValid())
            {
                try {
                    ArrayList<Object> xmlData = bbCreateView.getBBXMLString();
                    if (xmlData != null)
                    {
                        BBViewer.displayBillboard((String)xmlData.get(0), (byte[]) xmlData.get(1));
                    }
                    else
                    {
                        bbCreateView.showInvalidXMLMessage();
                    }
                }
                catch (IllegalComponentStateException ex)
                {
                    bbCreateView.showInvalidXMLMessage();
                }
            }
            else
            {
                bbCreateView.showInvalidXMLMessage();
            }
            views.put(BB_CREATE, bbCreateView);
        }
    }

    /**
     * Listener to handle title BB mouse clicks.
     */
    private class TitleListener extends MouseAdapter
    {
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
     * Listener to handle text BB mouse clicks.
     */
    private class BBTextListener extends MouseAdapter
    {
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
     * Listener to handle photo BB mouse clicks.
     */
    private class BBPhotoListener extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: Photo button clicked");
            // get BB create view
            BBCreateView bbCreateView = (BBCreateView) views.get(BB_CREATE);

            // get users selection of photo type either url or data
            int response = bbCreateView.photoTypeSelection();

            // URL selected
            if (response == 0)
            {
                // allow user to enter url
                ArrayList<Object> photoData = bbCreateView.showURLInputMessage();

                // if not null, set photo
                if (photoData != null)
                {
                    bbCreateView.setPhoto((ImageIcon)photoData.get(0), BBCreateView.PhotoType.URL, photoData.get(1));
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

                // photo data is not null, set photo
                if (photoData != null)
                {
                    String encodedString = Base64.getEncoder().encodeToString((byte[])photoData.get(1));
                    bbCreateView.setPhoto((ImageIcon)photoData.get(0), BBCreateView.PhotoType.DATA, encodedString);
                }
            }
            views.put(BB_CREATE, bbCreateView);
        }
    }

    /**
     * Listener to handle xml import BB mouse clicks.
     */
    private class BBXMLImportListener extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: XML import button clicked");

            // get BB create view
            BBCreateView bbCreateView = (BBCreateView) views.get(BB_CREATE);

            // browse import returns boolean value to indicate whether import was successful
            if (!bbCreateView.browseXMLImport())
            {
                // show error if unsuccessful import
                bbCreateView.showInvalidXMLMessage();
            }
            views.put(BB_CREATE, bbCreateView);
        }
    }

    /**
     * Listener to handle xml export BB mouse clicks.
     */
    private class BBXMLExportListener extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: XML export button clicked");

            // get BB Create view
            BBCreateView bbCreateView = (BBCreateView) views.get(BB_CREATE);

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
                    if (filename != null || !filename.equals(""))
                    {
                        // get selected folder path
                        String path = bbCreateView.browseExportFolder();

                        // success value indicates if xml was successfully converted to file
                        Boolean success = bbCreateView.xmlExport(path + "\\" + filename + ".xml");

                        // show message to user depending on export status
                        if (success)
                        {
                            bbCreateView.showSuccessfulExport();
                        }
                        else
                        {
                            bbCreateView.showBBInvalidErrorMessage();
                        }
                    }
                }
            }
            // if BB is invalid, show error message
            else
            {
                bbCreateView.showBBInvalidErrorMessage();
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
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: View Schedule button clicked");
            // navigate to edit schedule week view
            updateView(SCHEDULE_WEEK);
        }
    }

    /**
     * Listener to handle Create Schedule Button mouse clicks.
     */
    private class ScheduleCreateButtonListener extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: Create Schedule button clicked");
            // navigate to edit schedule update view
            updateView(SCHEDULE_UPDATE);
        }
    }

    /**
     * Listener to handle show duration in Schedule Create
     */
    private class ScheduleDurationListener implements ItemListener
    {
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
     * Listener to handle Schedule Radio Button mouse clicks.
     */
    private class ScheduleRadioButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            System.out.println("CONTROLLER LEVEL: Daily Recurrence button clicked");
            // get button source
            JRadioButton button = (JRadioButton) e.getSource();
            ScheduleUpdateView scheduleUpdateView = (ScheduleUpdateView) views.get(SCHEDULE_UPDATE);
            // get button name
            String buttonName = button.getName();

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
                    // set minute label
                    if (minuteRepeat > 0)
                    {
                        scheduleUpdateView.setMinuteLabel(minuteRepeat);
                    }
                    break;
            }
            views.put(SCHEDULE_UPDATE, scheduleUpdateView);
        }
    }

    /**
     * Listener to handle Minute Repeat mouse clicks.
     */
    private class ScheduleMinuteRepeatListener implements ItemListener {

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
                }
            }
            views.put(SCHEDULE_UPDATE, scheduleUpdateView);
        }
    }

    /**
     * Listener to handle BB Schedules to populate information
     */
    private class SchedulePopulateListener implements ItemListener {

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
                        // TODO: PATRICE / ALAN. This is just showing theres this method here to parse. Not sure why it is not populating the screen
                        Boolean sunday = Boolean.parseBoolean(schedule.getSunday());
                        Boolean monday = Boolean.parseBoolean(schedule.getMonday());
                        Boolean tuesday = Boolean.parseBoolean(schedule.getTuesday());
                        Boolean wednesday = Boolean.parseBoolean(schedule.getWednesday());
                        Boolean thursday = Boolean.parseBoolean(schedule.getThursday());
                        Boolean friday = Boolean.parseBoolean(schedule.getFriday());
                        Boolean saturday = Boolean.parseBoolean(schedule.getSaturday());
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

                        Integer startHour = Integer.parseInt(startTime.substring(0, Math.min(startTime.length(), 1)).trim());
                        Integer startMin = Integer.parseInt(startTime.substring(3, Math.min(startTime.length(), 4)).trim());

                        scheduleUpdateView.setScheduleValues(daysOfWeek, startHour, startMin, duration, recurrenceButton, minRepeat);
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
                        // FIXME: ALAN - HANDLE WHAT HAPPENS WHEN FATAL ERROR OCCURS
                    }
                }
            }
        }
    }
}