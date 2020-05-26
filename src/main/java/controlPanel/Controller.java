package controlPanel;

import controlPanel.Main.VIEW_TYPE;
import server.BillboardList;
import server.DbBillboard;
import server.ScheduleInfo;
import server.Server.ServerAcknowledge;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
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
 * Controller Class is designed to manage user inputs appropriately, sending requests to the server and updating the model/gui when required.
 * The constructor stores the model and views, and adds listeners to the views.
 */
public class Controller
{
    // store model and view
    private Model model;
    private HashMap<VIEW_TYPE, AbstractView> views;
    private Object serverResponse;
    private String sessionToken;
    private BBFullPreview BBViewer;

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

        BBViewer = new BBFullPreview();
        // FIXME: do I need to hide?

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
     * Listeners include: Home, Back and Profile Button.
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
        UserPreviewView userPreviewView = (UserPreviewView) views.get(USER_VIEW);
        views.put(USER_VIEW, userPreviewView);
    }

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
     * Listeners include: Home, Back and Profile
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

    private void addScheduleMenuListener()
    {
        addGenericListeners(SCHEDULE_MENU);
        ScheduleMenuView scheduleMenuView = (ScheduleMenuView) views.get(SCHEDULE_MENU);
        scheduleMenuView.addScheduleViewListener(new ScheduleViewButtonListener());
        scheduleMenuView.addScheduleCreateListener(new ScheduleCreateButtonListener());
        views.put(SCHEDULE_MENU, scheduleMenuView);
    }

    private void addScheduleWeekListener()
    {
        addGenericListeners(SCHEDULE_WEEK);
        ScheduleWeekView scheduleWeekView = (ScheduleWeekView) views.get(SCHEDULE_WEEK);
        views.put(SCHEDULE_WEEK, scheduleWeekView);
    }

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
        System.out.println(model.getCurrentView());
        System.out.println(newView);


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
    private void updateComponents(VIEW_TYPE newViewType){
        switch(newViewType)
        {
            case HOME:
                HomeView homeView = (HomeView) views.get(VIEW_TYPE.HOME);
                homeView.setWelcomeText(model.getUsername());
                homeView.usersButton.setVisible(true);
                views.put(HOME, homeView);
                break;

            case LOGIN:
                System.out.println("Check LogIn permission");
                break;

            case SCHEDULE_WEEK:
                ScheduleWeekView scheduleWeekView = (ScheduleWeekView) views.get(SCHEDULE_WEEK);
                scheduleWeekView.setWelcomeText(model.getUsername());

                //ScheduleList scheduleMonday = (ScheduleList) ScheduleControl.listDayScheduleRequest(model.getSessionToken(), "Monday");

                // FIXME: ALAN TO ADD AND REMOVE UNNECESSARY CODE
                // Billboard Schedule: day, time, bb name
                ArrayList<ArrayList<String>> billboardScheduleMonday = new ArrayList<>();
                billboardScheduleMonday.add(new ArrayList<>(Arrays.asList("1-2pm", "Myer's Sale", "Creator")));
                billboardScheduleMonday.add(new ArrayList<>(Arrays.asList("1-2pm", "Myer's Sale", "Creator")));
                billboardScheduleMonday.add(new ArrayList<>(Arrays.asList("1-2pm", "Myer's Sale", "Creator")));
                billboardScheduleMonday.add(new ArrayList<>(Arrays.asList("1-2pm", "Myer's Sale", "Creator")));
                billboardScheduleMonday.add(new ArrayList<>(Arrays.asList("1-2pm", "Myer's Sale", "Creator")));

                ArrayList<ArrayList<String>> billboardScheduleTuesday = new ArrayList<>();
                billboardScheduleTuesday.add(new ArrayList<>(Arrays.asList("1-2pm", "Myer's Sale", "Creator")));
                billboardScheduleTuesday.add(new ArrayList<>(Arrays.asList("1-2pm", "Myer's Sale", "Creator")));
                billboardScheduleTuesday.add(new ArrayList<>(Arrays.asList("1-2pm", "Myer's Sale", "Creator")));
                billboardScheduleTuesday.add(new ArrayList<>(Arrays.asList("1-2pm", "Myer's Sale", "Creator")));
                billboardScheduleTuesday.add(new ArrayList<>(Arrays.asList("1-2pm", "Myer's Sale", "Creator")));

                ArrayList<ArrayList<ArrayList<String>>> schedule = new ArrayList<>();
                schedule.add(billboardScheduleMonday);
                schedule.add(billboardScheduleTuesday);

                scheduleWeekView.populateSchedule(schedule);
                views.put(SCHEDULE_WEEK, scheduleWeekView);
                break;

            case SCHEDULE_UPDATE:
                ScheduleUpdateView scheduleUpdateView = (ScheduleUpdateView) views.get(SCHEDULE_UPDATE);
                scheduleUpdateView.setWelcomeText(model.getUsername());

                BillboardList billboardList = null;
                try {
                    billboardList = (BillboardList) BillboardControl.listBillboardRequest(model.getSessionToken());
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                // FIXME: handle when BB name is null
                ArrayList<String> stringArray = billboardList.getBillboardNames();

                scheduleUpdateView.setBBNamesFromDB(stringArray);
                scheduleUpdateView.showInstructionMessage();
                views.put(SCHEDULE_UPDATE, scheduleUpdateView);
                break;

            case SCHEDULE_MENU:
                ScheduleMenuView scheduleMenuView = (ScheduleMenuView) views.get(SCHEDULE_MENU);
                scheduleMenuView.setWelcomeText(model.getUsername());
                views.put(SCHEDULE_MENU, scheduleMenuView);
                break;


            case USER_LIST:
                listUserHandling();
                break;

            case USER_EDIT:
                UserEditView userEditView = (UserEditView) views.get(USER_EDIT);
                userEditView.setWelcomeText(model.getUsername());
                views.put(USER_EDIT, userEditView);
                break;

            case USER_PROFILE:
                UserProfileView userProfileView = (UserProfileView) views.get(USER_PROFILE);
                userProfileView.setWelcomeText(model.getUsername());
                views.put(USER_PROFILE, userProfileView);
                break;

            case USER_VIEW:
                UserPreviewView userPreviewView = (UserPreviewView) views.get(USER_VIEW);
                userPreviewView.setWelcomeText(model.getUsername());
                views.put(USER_VIEW, userPreviewView);
                break;

            case USERS_MENU:
                UsersMenuView usersMenuView = (UsersMenuView) views.get(USERS_MENU);
                usersMenuView.setWelcomeText(model.getUsername());
                views.put(USERS_MENU, usersMenuView);
                break;

            case BB_LIST:
                // get list BB view
                BBListView bbListView = (BBListView) views.get(BB_LIST);
                bbListView.setWelcomeText(model.getUsername());
                ArrayList<String> BBListArray = null;
                try {
                    BillboardList billboard_List = (BillboardList) BillboardControl.listBillboardRequest(model.getSessionToken());
                    BBListArray = billboard_List.getBillboardNames();
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                    // FIXME: pop up error window!
                }
                // FIXME: if null is returned handle correctly!!!
                bbListView.addContent(BBListArray, new EditBBButtonListener(), new DeleteBBButtonListener(), new ViewBBButtonListener());
                views.put(BB_LIST, bbListView);
                break;

            case BB_CREATE:
                BBCreateView bbCreateView = (BBCreateView) views.get(BB_CREATE);
                bbCreateView.setWelcomeText(model.getUsername());
                views.put(BB_CREATE, bbCreateView);
                break;

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
            updateView(VIEW_TYPE.HOME); // navigate to home screen
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
                // terminate Control Panel and restart
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
            model.storeUsername(username);
            // Attempt to handle the login request
            try {
                serverResponse = loginRequest(username, password); // CP Backend call
                //TODO: FOR SOME REASON THIS DOESN'T ALWAYS PRINT ON THE FIRST BUTTON PRESS.

                // NOTE: loginRequest will return 1 of 3 serverAcknowledgments
                // if unsuccessful, show error and do not allow log in
                if (serverResponse.equals(BadPassword)) {
                    System.out.println("CONTROLLER LEVEL - Incorrect Password");
                    System.out.println("Please try another password");
                    logInView.showBadPasswordException();
                    views.put(VIEW_TYPE.LOGIN, logInView);
                } else if (serverResponse.equals(NoSuchUser)) {
                    System.out.println("CONTROLLER LEVEL - No Such User");
                    System.out.println("Please try another username");
                    logInView.showNoSuchUserException();
                    views.put(VIEW_TYPE.LOGIN, logInView);
                } else { // login request success
                    System.out.println("CONTROLLER LEVEL - Correct Credentials");
                    // store username and session token in model
                    model.storeUsername(username);
                    sessionToken = (String) serverResponse; // Store as a session token
                    model.storeSessionToken(sessionToken);
                    views.put(VIEW_TYPE.LOGIN, logInView);
                    // nav user to home screen
                    updateView(VIEW_TYPE.HOME);
                }
            } catch (IOException | ClassNotFoundException | NoSuchAlgorithmException ex) {
                logInView.showFatalError();
                // TODO: terminate Control Panel and restart
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

            JButton button = (JButton) e.getSource();
            // update information in EDIT USER view
            UserEditView userEditView = (UserEditView) views.get(USER_EDIT);
            // get selected user
            String usernameSelected = button.getName();
            // set username, password and permissions in User Edit View
            userEditView.setUsername(usernameSelected);

            // Get user permissions from server
            // FIXME: JACINTA - what is this doing? Should it be returning something?
            getUserPermissionsFromServer(userEditView, USER_EDIT, usernameSelected);

            views.put(USER_EDIT, userEditView);

            // navigate to user edit screen
            updateView(USER_EDIT);
        }
    }

    /**
     * Listener to handle Create User Button mouse clicks.
     */
    private class CreateUserButtonListener extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: Create User button clicked");
            // navigate to edit user screen
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

            ArrayList<Object> userArray = userEditView.getUserInfo();

            // Parsing elements from user array for the UserControl method to update user permission/password
            String username = (String) userArray.get(0);
            Boolean createBillboards = (Boolean) userArray.get(1);
            Boolean editBillboards = (Boolean) userArray.get(2);
            Boolean editSchedules = (Boolean) userArray.get(3);
            Boolean editUsers = (Boolean) userArray.get(4);

            int response = userEditView.showUserConfirmation();
            // add permissions to DB if user confirms permissions
            if (response == 0) {
                // Store selected permissions in database
                try {
                    ServerAcknowledge serverResponse = UserControl.setPermissionsRequest(sessionToken, username,
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
                    // TODO: terminate Control Panel and restart
                    ex.printStackTrace();
                }
            } // TODO: Should we do anything else if the response is not 0?
        }

    }

    /**
     * Listener to handle Edit User Button mouse clicks.
     */
    private class UserCreateButtonListener extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: Submit new User button clicked");

            // update information in EDIT USER view
            UserCreateView userCreateView = (UserCreateView) views.get(USER_CREATE);
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
                        serverResponse = UserControl.createUserRequest(sessionToken, username, password, createBillboards, editBillboards, editSchedules, editUsers);
                    } catch (IOException | NoSuchAlgorithmException | ClassNotFoundException ex) {
                        ex.printStackTrace();
                        userCreateView.showFatalError();
                        // TODO: terminate Control Panel and restart
                    }

                    // Filter response and display appropriate action
                    if (serverResponse.equals(Success)) {
                        userCreateView.showCreateSuccess();
                    }
                    else if (serverResponse.equals(InvalidToken)) {
                        userCreateView.showInvalidTokenException();
                        // TODO: navigate to logout/login screen
                    } else if (serverResponse.equals(InsufficientPermission)) {
                        userCreateView.showInsufficientPermissionsException();
                    } else if (serverResponse.equals(PrimaryKeyClash)) {
                        userCreateView.showUsernamePrimaryKeyClashException();
                        // TODO: Give user a chance to type in a new username (clear existing)
                    }
                    views.put(USER_CREATE, userCreateView);
                    System.out.println(model.getCurrentView());

                    // navigate to edit menu screen
                    updateView(USERS_MENU);
                }
            }
            else
            {
                userCreateView.showErrorMessage();
            }
        }
    }

    private class UserPasswordUpdateButtonListener extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: User Password Set button clicked");
            UserEditView userEditView = (UserEditView) views.get(USER_EDIT);
            String password = userEditView.showNewPasswordInput();
            int response = userEditView.showUserConfirmation();

            // Confirm response of updated password
            if  (response == 0) {
                if (password == null) { // Ensure the user enters a valid password (not empty)
                    userEditView.showEnterValidPasswordException();
                } else {
                    try {
                        ServerAcknowledge serverResponse = UserControl.setPasswordRequest(model.getSessionToken(), model.getUsername(), password);
                        if ( serverResponse.equals(Success) ) {
                            userEditView.showEditPasswordSuccess();
                        }  else if ( serverResponse.equals(InvalidToken) ) {
                            userEditView.showInvalidTokenException();
                        } else if ( serverResponse.equals(InsufficientPermission) ) {
                            userEditView.showInsufficientPermissionsException();
                        } else if ( serverResponse.equals(NoSuchUser) ) {
                            userEditView.showNoSuchUserException();
                        }
                    } catch (IOException | ClassNotFoundException | NoSuchAlgorithmException ex) {
                        userEditView.showFatalError();
                        // TODO: terminate Control Panel and restart
                        ex.printStackTrace();
                    }
                }
                views.put(USER_EDIT, userEditView);
            }
        }
    }


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
            UserListView userListView = (UserListView) views.get(USER_LIST); //TODO: Patrice can you check if this should be a deleteUserView.
            // ask user to confirm deletion of user
            int response = userListView.showDeleteConfirmation();

            // if confirmed response, delete from DB
            if (response == 0) // TODO: NOT SURE WHAT RESPONSE == 0 CORRESPONDS TO BUT MAY NEED TO BE INTEGRATED BELOW
            {
                System.out.println("Delete user from DB");
                // Retrieve response from DB
                try {
                    String sessionToken = model.getSessionToken(); // Retrieve session token
                    serverResponse = UserControl.deleteUserRequest(sessionToken, username); // CP Backend method call
                } catch (IOException | ClassNotFoundException ex) {
                    userListView.showFatalError();
                    // TODO: terminate Control Panel and restart
                    ex.printStackTrace();
                }

                // If successful, let the user know
                if (serverResponse.equals(Success)) {
                    System.out.println("CONTROLLER LEVEL - User was Successfully Deleted");
                    userListView.showDeleteSuccess();
                // Error handling on GUI via pop-up windows
                } else if (serverResponse.equals(InsufficientPermission)) {
                    System.out.println("CONTROLLER LEVEL - Insufficient Permission");
                    userListView.showInsufficientPermissionsException();
                } else if (serverResponse.equals(InvalidToken)) {
                    System.out.println("CONTROLLER LEVEL - Invalid Token");
                    userListView.showInvalidTokenException();
                } else if (serverResponse.equals(NoSuchUser)) {
                    System.out.println("CONTROLLER LEVEL - No Such User Found in DB to be Deleted");
                    userListView.showNoSuchUserException();
                } else if (serverResponse.equals(CannotDeleteSelf)) {
                    System.out.println("CONTROLLER LEVEL - Cannot Delete Your Own User");
                    userListView.showCannotDeleteSelfException();
                }

                // navigate back to the same page to refresh view
                updateView(USERS_MENU);
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
            serverResponse = UserControl.listUsersRequest(sessionToken);
            System.out.println(serverResponse);
            // Attempt to cast to a string ArrayList for successful response
            usernames = (ArrayList<String>) serverResponse;
        } catch (IOException | ClassNotFoundException ex) {
            userListView.showFatalError();
            // TODO: terminate Control Panel and restart
            ex.printStackTrace();
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
        } else { // Successful, let the user know and populate with list of users
            userListView.addContent(usernames, new EditUserButtonListener(), new DeleteUserButtonListener(), new ViewUserButtonListener());
            views.put(USER_LIST, userListView);
        }
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

    /**
     * Get user permissions from server, store in view and display appropriate error message pop-up windows/actions
     */
    private void getUserPermissionsFromServer(AbstractUserView userView, VIEW_TYPE viewType, String username) {
        try {
            serverResponse = UserControl.getPermissionsRequest(sessionToken, username);
            ArrayList<Boolean> userPermissions = (ArrayList<Boolean>) serverResponse;
            // FIXME: setPermissions is wrong mapping
            userView.setPermissions(userPermissions);
            views.put(viewType, userView);
        } catch (IOException | ClassNotFoundException ex) {
            userView.showFatalError();
            views.put(viewType, userView);
            // TODO: terminate Control Panel and restart
            ex.printStackTrace();
            // If the return is not an array list of booleans, an exception occurred
        } catch ( ClassCastException ex ) {
            if (serverResponse.equals(InvalidToken)) {
                userView.showInvalidTokenException();
                // TODO: navigate to logout/login screen
            } else if ( serverResponse.equals(InsufficientPermission) ) {
                userView.showInsufficientPermissionsException();
            } else if (serverResponse.equals(NoSuchUser)) {
                userView.showNoSuchUserException();
                // TODO: navigate to logout/login screen
            }
            views.put(viewType, userView);
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
            // set BB Name to enabled
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
                boolean valid = bbCreateView.addBBXML(xmlFile);
                if (valid)
                {
                    // set BB Name based on selected button
                    bbCreateView.setBBName(button.getName());
                    updateView(BB_CREATE);
                }
                else
                {
                    bbCreateView.showBBInvalidErrorMessage();
                }
            }
            catch (IOException | ClassNotFoundException ex)
            {
                ex.printStackTrace();
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
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
                // navigate to bb list screen to refresh screen
                updateView(BB_MENU);
            }
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
                System.out.println("XML " + xmlFile);
                BBViewer.displayBillboard(xmlFile);
            }
            catch (IOException | ClassNotFoundException | IllegalComponentStateException ex)
            {
                //ex.printStackTrace();
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
            String newColor = bbCreateView.showColorChooser();
            if (newColor != null)
            {
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
            String BBName = bbCreateView.showBBNameChooser();
            if (BBName != null)
            {
                bbCreateView.setBBName(BBName);
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
            String bbName = bbCreateView.getSelectedBBName();
            // check that a BB name has been set, if not set raise error
            if (!bbName.equals("") && bbCreateView.checkBBValid())
            {
                // raise confirmation to create BB
                int confirmCreation = bbCreateView.showConfirmationCreateBB();

                // if user confirms BB creation, show scheduling option
                if (confirmCreation == 0)
                {
                    String createBBReq = null;

                    try {
                        String BBXMLString = bbCreateView.getBBXMLString();
                        System.out.println("Original BBXMLString is : " + BBXMLString);
                        String BBXMLStringImageDataRemoved = RemoveImageData(BBXMLString);
                        // Create image file from image data
                        String imageFilePath = "src\\main\\resources\\tempImage.txt";
                        BufferedWriter writer = new BufferedWriter(new FileWriter(imageFilePath,false), 8); // overwrite, partition into 8kB
                        writer.write(GetPictureData(BBXMLString));
                        writer.close();
                        String creator = model.getUsername();
                        ServerAcknowledge createBillboardAction = BillboardControl.createBillboardRequest(model.getSessionToken(), bbName, creator, imageFilePath, BBXMLStringImageDataRemoved);
                        if (createBillboardAction.equals(Success)){
                            createBBReq = "Pass: Billboard Created";
                        }
                        System.out.println(createBBReq);
                    } catch (ParserConfigurationException | TransformerException | IOException | ClassNotFoundException ex)
                    {
                        ex.printStackTrace();
                        createBBReq = "Error encountered whilst creating BB. Exception " + ex.toString();
                        System.out.println("Error encountered whilst creating BB. Exception " + ex.toString());
                    }

                    if (createBBReq != null)
                    {
                        if (createBBReq.equals("Fail: Billboard Already Exists"))
                        {
                            // show pop up error - bb name already exists
                            System.out.println("BB Name already exists!");
                        }
                        else
                        {
                            // show scheduling option - asking user if they want to schedule BB now
                            int optionSelected = bbCreateView.showSchedulingOption();

                            // User Selected YES to schedule BB
                            if (optionSelected == 0)
                            {
                                // set BB name selected in schedule view to the newly added BB
                                ScheduleUpdateView scheduleUpdateView = (ScheduleUpdateView) views.get(SCHEDULE_UPDATE);
                                scheduleUpdateView.setBBSelected(bbName);
                                views.put(SCHEDULE_UPDATE, scheduleUpdateView);
                                // navigate to schedule create view
                                updateView(SCHEDULE_UPDATE);
                            }
                            // User Selected NO to skip scheduling the BB
                            else if (optionSelected == 1)
                            {
                                // you have just created a bb message
                                bbCreateView.showBBCreatedSuccessMessage();
                            }
                        }
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

    /*
    private String EscapeQuotations(String bbXMLString) {
        String escapedString = bbXMLString.replace("\"", "\\\"");
        return escapedString;
    }*/

    /**
     * Method to extract the picture data from the original billboard xml for file storage
     * @param bbXMLString Original billboard xml code generated from user inputs
     * @return String representation of the base-64 encoded image data
     */
    private String GetPictureData(String bbXMLString) {
        String[] splitString = bbXMLString.split("<picture data=\\\"");
        String[] imageStrings = splitString[1].split("\"/>");
        System.out.println("Image data returned is " + imageStrings[0]);
        return imageStrings[0];
    }


    /**
     * Method to remove the picture data tag from xml for sending to server.
     * @param bbXMLString Original billboard xml code generated from user inputs
     * @return bbXMLString with removed picture data tag.
     */
    private String RemoveImageData(String bbXMLString) {
        String[] splitString = bbXMLString.split("<picture data=");
        String[] imageStrings = splitString[1].split("/>");
        System.out.println("XML with image data removed is " + splitString[0] + imageStrings[1]);
        return splitString[0]+imageStrings[1];
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
            // get list BB create
            BBCreateView bbCreateView = (BBCreateView) views.get(BB_CREATE);

            String bbName = bbCreateView.getSelectedBBName();

            if ((bbName != null || !bbName.equals("")) && bbCreateView.checkBBValid())
            {
                try {
                    String xmlFile = bbCreateView.getBBXMLString();
                    System.out.println("full BB preview from BB Create" + xmlFile);
                    BBViewer.displayBillboard(xmlFile);
                }
                catch (IllegalComponentStateException | TransformerException | ParserConfigurationException ex)
                {
                    ex.printStackTrace();
                    bbCreateView.showInvalidXMLMessage();
                    views.put(BB_CREATE, bbCreateView);
                }
            }
            else
            {
                bbCreateView.showInvalidXMLMessage();
            }
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

            // get list BB create
            BBCreateView bbCreateView = (BBCreateView) views.get(BB_CREATE);
            String BBTitle = bbCreateView.showBBTitleChooser();
            if (BBTitle != null)
            {
                bbCreateView.setBBTitle(BBTitle);
                String titleColour = bbCreateView.browseTitleColour();
                bbCreateView.setBBTitleColour(titleColour);
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

            // get list BB create
            BBCreateView bbCreateView = (BBCreateView) views.get(BB_CREATE);
            String BBText = bbCreateView.showBBTextChooser();
            if (BBText != null)
            {
                bbCreateView.setBBText(BBText);
                String textColour = bbCreateView.browseTextColour();
                bbCreateView.setBBTextColour(textColour);
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
            // get list BB create
            BBCreateView bbCreateView = (BBCreateView) views.get(BB_CREATE);
            int response = bbCreateView.photoTypeSelection();
            // URL selected
            if (response == 0)
            {
                ArrayList<Object> photoData = bbCreateView.showURLInputMessage();
                if (photoData != null)
                {
                    bbCreateView.setPhoto((ImageIcon)photoData.get(0), BBCreateView.PhotoType.URL, (String)photoData.get(1));
                }
                else
                {
                    bbCreateView.showURLErrorMessage();
                }
            }
            else if (response == 1)
            {
                ArrayList<Object> photoData = bbCreateView.browsePhotos();

                if (photoData != null)
                {
                    System.out.println("encoded img " + (String)photoData.get(1));
                    bbCreateView.setPhoto((ImageIcon)photoData.get(0), BBCreateView.PhotoType.DATA, (String)photoData.get(1));
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

            // get list BB create
            BBCreateView bbCreateView = (BBCreateView) views.get(BB_CREATE);

            if (!bbCreateView.browseXMLImport())
            {
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

            // get list BB create
            BBCreateView bbCreateView = (BBCreateView) views.get(BB_CREATE);
            if (bbCreateView.checkBBValid())
            {
                int value = bbCreateView.showFolderChooserSelector();

                if(value == JFileChooser.APPROVE_OPTION)
                {
                    String filename = bbCreateView.enterXMLFileName();
                    if (filename != null || !filename.equals(""))
                    {
                        try {
                            bbCreateView.browseExportFolder(filename);
                            bbCreateView.showSuccessfulExport();
                        } catch (ParserConfigurationException | TransformerException ex) {
                            ex.printStackTrace();
                            bbCreateView.showBBInvalidErrorMessage();
                        }
                    }
                }
            }
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
            // navigate to edit schedule week view
            updateView(SCHEDULE_UPDATE);
        }
    }

    /**
     * Listener to handle show duration in Schedule Create
     */
    private class ScheduleDurationListener implements ItemListener
    {
        @Override
        public void itemStateChanged(ItemEvent e) {

            System.out.println("Time Item changed");
            System.out.println(e.getStateChange());
            int eventId = e.getStateChange();
            if (eventId == ItemEvent.SELECTED)
            {
                System.out.println("CONTROLLER LEVEL: Time changed");

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
            JRadioButton button = (JRadioButton) e.getSource();
            ScheduleUpdateView scheduleUpdateView = (ScheduleUpdateView) views.get(SCHEDULE_UPDATE);
            String buttonName = button.getName();

            switch (buttonName) {
                case "hourly":
                    scheduleUpdateView.showHourlyMessage();
                    scheduleUpdateView.enableMinuteSelector(false);
                    break;
                case "no repeats":
                    scheduleUpdateView.showNoRepeatMessage();
                    scheduleUpdateView.enableMinuteSelector(false);
                    break;
                case "minute":
                    scheduleUpdateView.showMinuteMessage();
                    scheduleUpdateView.enableMinuteSelector(true);
                    int minuteRepeat = scheduleUpdateView.getMinuteRepeat();
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
            int eventId = e.getStateChange();
            if (eventId == ItemEvent.SELECTED)
            {
                System.out.println("CONTROLLER LEVEL: Minute Repeat button clicked");
                ScheduleUpdateView scheduleUpdateView = (ScheduleUpdateView) views.get(SCHEDULE_UPDATE);
                JComboBox menuItem = (JComboBox) e.getSource();
                int minuteSelected = (int) menuItem.getSelectedItem();
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
            int clear = scheduleUpdateView.showScheduleClearConfirmation();
            // if user confirms deletion of schedule
            if (clear == 0)
            {
                String BBName = scheduleUpdateView.getSelectedBBName();

                //FIXME: SERVER CALL: removeBBSchedule(BBName)
                // Noting - this BB may not exist in the Schedule Table!
                try {
                    String result = ScheduleControl.deleteScheduleRequest(model.getSessionToken(), BBName);
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                    // FIXME: pop up window for error message
                }

                // navigate back to schedule menu
                updateView(SCHEDULE_MENU);
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
            int eventId = e.getStateChange();
            // process event if BB Name has been SELECTED
            if (eventId == ItemEvent.SELECTED)
            {
                System.out.println("CONTROLLER LEVEL: Schedule Populate button clicked");

                // get bb name that has been selected
                // TODO: check if this is the best way of getting the menu item selected
                JComboBox menuItem = (JComboBox) e.getSource();
                String bbName = (String)menuItem.getSelectedItem();

                // set schedule values based on db info
                ScheduleUpdateView scheduleUpdateView = (ScheduleUpdateView) views.get(SCHEDULE_UPDATE);

                // FIXME: SCHEDULE CONTROL - getBBSchedule(bbName)
                // FIXME: IF AN OBJECT IS RETURNED, use SET SCHEDULE VALUES(SCHEDULE OBJECT)
                // FIXME: IF NO OBJECT IS RETURNED, use SHOW NO EXISTING SCHEDULE MESSAGE() & REMOVE SCHEDULE SELECTION()

                ScheduleInfo schedule = null;
                try {
                    schedule = (ScheduleInfo) ScheduleControl.listABillboardSchedule(model.getSessionToken(), bbName);
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                    // FIXME: show pop up window to alert user of error
                }

                // FIXME: ALAN - FORMAT CORRECTLY

//                String monday = schedule.getMonday();
//                boolean monday = Boolean.parseBoolean(schedule.getMonday()); // hh:mm
//                schedule.getStartTime(); // hh:mm
//                schedule.getDuration();
//                schedule.getRepeat(); // get repeat minutes

                //scheduleUpdateView.setScheduleValues(daysOfWeek, startHour, startMin, duration, recurrenceButton, minRepeat);

//                if (bbName.equals("Myer"))
//                {
//                    ArrayList<Boolean> daysOfWeek = new ArrayList<>();
//                    daysOfWeek.add(true);
//                    daysOfWeek.add(true);
//                    daysOfWeek.add(true);
//                    daysOfWeek.add(true);
//                    daysOfWeek.add(true);
//                    daysOfWeek.add(true);
//                    daysOfWeek.add(true);
//                    int startHour = 5;
//                    int startMin = 6;
//                    int duration = 30;
//                    int minRepeat = 220;
//                    String recurrenceButton = "minute";
//                    scheduleUpdateView.setScheduleValues(daysOfWeek, startHour, startMin, duration, recurrenceButton, minRepeat);
//                }
//                else if (bbName.equals("Anaconda"))
//                {
//                    ArrayList<Boolean> daysOfWeek = new ArrayList<>();
//                    daysOfWeek.add(true);
//                    daysOfWeek.add(true);
//                    daysOfWeek.add(true);
//                    daysOfWeek.add(true);
//                    daysOfWeek.add(true);
//                    daysOfWeek.add(true);
//                    daysOfWeek.add(true);
//                    int startHour = 1;
//                    int startMin = 0;
//                    int duration = 30;
//                    int minRepeat = -1;
//                    String recurrenceButton = "hourly";
//                    scheduleUpdateView.setScheduleValues(daysOfWeek, startHour, startMin, duration, recurrenceButton, minRepeat);
//                }
//                else
//                {
//                    // alert user that no schedule exists in db
//                    scheduleUpdateView.showNoExistingScheduleMessage();
//                    // clear the schedule
//                    scheduleUpdateView.removeScheduleSelection();
//                }
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
            if (!scheduleUpdateView.checkValidDuration())
            {
                scheduleUpdateView.raiseDurationError();
            }
            else if (!scheduleUpdateView.checkValidDaySelected())
            {
                scheduleUpdateView.raiseDayError();
            }
            else if (!scheduleUpdateView.checkValidBB())
            {
                scheduleUpdateView.raiseBBError();
            }
            else
            {
                int response = scheduleUpdateView.showConfirmationCreateSchedule();
                // confirmation response
                if (response == 0)
                {
                    ArrayList<Object> scheduleInfo = scheduleUpdateView.getScheduleInfo();
                    // FIXME: SCHEDULE CONTROL: ALAN - take in an array list of objects

                    //ScheduleControl.scheduleBillboardRequest(scheduleInfo);

                    scheduleUpdateView.showConfirmationDialog();
                    views.put(SCHEDULE_UPDATE, scheduleUpdateView);
                    // navigate to schedule menu
                    updateView(SCHEDULE_MENU);
                }
            }
        }
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

//    /**
//     * Listener to handle List daily schedule mouse clicks.
//     */
//    private class ScheduleDailyListener extends MouseAdapter
//    {
//        @Override
//        public void mouseClicked(MouseEvent e)
//        {
//            System.out.println("CONTROLLER LEVEL: List Day Schedule button clicked");
//
//            JButton button = (JButton) e.getSource();
//            System.out.println("BB Name: " + button.getName());
//
//            // getName() is equivalent to the day's date
//            // FIXME: send req to server to get all bb for that date, returning an array of strings (BB names) or objects
//
//            // get daily schedule view
//            ScheduleDailyView scheduleDailyView = (ScheduleDailyView) views.get(SCHEDULE_DAY);
//            String[] stringArray = {"8 - 9am: Myer's Biggest Sale","9.30 - 10am Kathmandu Summer Sale", "11 - 12pm Quilton's Covid Special", "1 - 3pm Macca's New Essentials Range"};
//            // FIXME: required to create edit, delete and view listeners!
//            scheduleDailyView.addContent(stringArray, new EditScheduleButtonListener(), new DeleteBBButtonListener(), new ViewBBButtonListener());
//            views.put(SCHEDULE_DAY, scheduleDailyView);
//
//            // navigate to bb list screen
//            updateView(SCHEDULE_DAY);
//        }
//    }