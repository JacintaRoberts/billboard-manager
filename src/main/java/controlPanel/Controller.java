package controlPanel;

import controlPanel.Main.VIEW_TYPE;
import org.xml.sax.SAXException;
import server.Server;
import server.Server.ServerAcknowledge;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

import static controlPanel.Main.VIEW_TYPE.*;
import static controlPanel.UserControl.loginRequest;
import static server.Server.ServerAcknowledge.*;
import static viewer.Viewer.extractXMLFile;
import static controlPanel.UserControl.logoutRequest;

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
        userEditView.addSubmitButtonListener(new UserCreateButtonListener());
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
        bbCreateView.addBBBackgroundColourListener(new ColourListener());
        bbCreateView.addBBTitleListener(new TitleListener());
        bbCreateView.addBBTextListener(new BBTextListener());
        bbCreateView.addBBPhotoListener(new BBPhotoListener());
        bbCreateView.addBBXMLImportListener(new BBXMLImportListener());
        bbCreateView.addXMLExportListener(new BBXMLExportListener());
        bbCreateView.addBBNameListener(new NameListener());
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
            case SCHEDULE_WEEK:
                System.out.println("Check Schedule permission");
                System.out.println("Populate Table");
                ScheduleWeekView scheduleWeekView = (ScheduleWeekView) views.get(SCHEDULE_WEEK);
                // get information from the database (send username, session token) returns scheduleObject
                // Billboard Schedule: day, time, bb name
                String[][] billboardSchedule = new String[][] {{"1-2pm", "Myer's Sale"},{"6-7am", "Spotlight Winter Sale"}};
                scheduleWeekView.populateSchedule(billboardSchedule);
                break;
            case SCHEDULE_UPDATE:
                ScheduleUpdateView scheduleUpdateView = (ScheduleUpdateView) views.get(SCHEDULE_UPDATE);
                // FIXME: call to server: get BB names
                String[] names = {"Myer", "Anaconda", "David Jones"};
                scheduleUpdateView.setBBNamesFromDB(names);
                scheduleUpdateView.showInstructionMessage();
                views.put(SCHEDULE_UPDATE, scheduleUpdateView);
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
            UserProfileView userView = (UserProfileView) views.get(USER_PROFILE);

            // set profile information
            // set username and password text on GUI
            // FIXME: get password and user permissions from Control/Server
            userView.setUsername(model.getUsername());
            userView.setPassword("Password");
            userView.setPermissions(new boolean[]{false,true,true, true});
            views.put(USER_VIEW, userView);

            // navigate to home screen
            updateView(USER_VIEW);
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
            try {
                String sessionToken = model.getSessionToken(); // Retrieve session token
                serverResponse = logoutRequest(sessionToken); // CP Backend method call
                System.out.println("RESPONSE FROM SERVER: " + serverResponse);
                // TODO: RETURN CUSTOM MESSAGE/ACTION TO USER VIA GUI
                // NOTE: THIS logoutRequest METHOD WILL EITHER RETURN:
                // 1. ServerAcknowledgement.Success; // Occurs when session token existed and was successfully expired
                // 2. ServerAcknowledgment.InvalidToken; // Occurs when session token was already expired
                // If successful, let the user know, navigate to login screen
                if (sessionTokenExpirationSuccess()) {
                    System.out.println("CONTROLLER LEVEL - Session Token Successfully Expired");
                    //DisplayLogoutSuccess(); // TODO: Implement some visual acknowledgement to user
                } else { // Session token was already expired
                    System.out.println("CONTROLLER LEVEL - Session Token Was Already Expired!");
                    //DisplaySessionTokenExpired(); //TODO: Implement some visual acknowledgement to user
                }
                // Navigate to log in screen for both cases
                updateView(LOGIN);
                //updateView(VIEW_TYPE.LOGIN); // n
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }

        // Determines whether the session token was successfully expired
        private boolean sessionTokenExpirationSuccess() {
            if ( serverResponse.equals(Success) ) {
                return true;
            }
            return false;
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
            try {
                serverResponse = loginRequest(username, password); // CP Backend call
                //TODO: FOR SOME REASON THIS DOESN'T ALWAYS PRINT ON THE FIRST BUTTON PRESS.

                // NOTE: loginRequest will return 1 of 3 serverAcknowledgments
                // if unsuccessful, show error and do not allow log in
                if (serverResponse.equals(BadPassword)) {
                    System.out.println("CONTROLLER LEVEL - Incorrect Password");
                    System.out.println("Please try another password");
                    // show error message
                    logInView.setErrorVisibility(true);
                    views.put(VIEW_TYPE.LOGIN, logInView); //TODO: IMPLEMENT SOME LOGIC TO HAVE THE USER TRY TO RE-ENTER VALID PASSWORD
                } else if (serverResponse.equals(NoSuchUser)) {
                    System.out.println("CONTROLLER LEVEL - No Such User");
                    System.out.println("Please try another username");
                    // show error message
                    logInView.setErrorVisibility(true);
                    views.put(VIEW_TYPE.LOGIN, logInView); //TODO: IMPLEMENT SOME LOGIC TO HAVE THE USER TRY TO RE-ENTER VALID USERNAME
                } else { // login request success
                    System.out.println("CONTROLLER LEVEL - Correct Credentials");
                    // store username and session token in model
                    model.storeUsername(username);
                    sessionToken = (String) serverResponse; // Store as a session token
                    model.storeSessionToken(sessionToken);
                    // hide error string
                    logInView.setErrorVisibility(false);
                    views.put(VIEW_TYPE.LOGIN, logInView);
                    // nav user to home screen
                    updateView(VIEW_TYPE.HOME);
                }
            } catch (IOException | ClassNotFoundException | NoSuchAlgorithmException ex) {
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
            System.out.println("UserName :" + button.getName());
            // TODO: get user information from server i.e. UserInfo userInfo = getUser(sessionTocken, username, userRequest);

            // update information in EDIT USER view
            UserEditView userEditView = (UserEditView) views.get(USER_EDIT);
            userEditView.setUsername(button.getName());
            userEditView.setPassword("password123");
            userEditView.setPermissions(new boolean[]{false,true,true,false});
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

            //TODO: FOR SOME REASON THIS DOESN'T ALWAYS PRINT ON THE FIRST BUTTON PRESS.

            // navigate to edit user screen
            updateView(USER_EDIT);
        }
    }

    /**
     * Listener to handle Submit New User Button mouse clicks.
     */
    private class UserCreateButtonListener extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: Submit new User button clicked");

            // update information in EDIT USER view
            //TODO: MOVE THE LOCATION OF THE CREATE USER BUTTON AND RETURN THE
            // CREATE USER PERMISSION IN THE .getUserInfo() method
            UserEditView userEditView = (UserEditView) views.get(USER_EDIT);
            ArrayList<Object> userArray = userEditView.getUserInfo();
            System.out.println(userArray);
            // Parsing elements from user array for the UserControl method to create the request to server
            String username = (String) userArray.get(0);
            String password = (String) userArray.get(1);
            Boolean createBillboards = true; // TODO: HARDCODED SHOULD PROBABLY FIX THIS AND ADJUST INDICES BELOW
            Boolean editBillboards = (Boolean) userArray.get(2);
            Boolean editSchedules = (Boolean) userArray.get(3);
            Boolean editUsers = (Boolean) userArray.get(4);

            // TODO: HANDLE EXCEPTIONS IN GUI
            try {
                UserControl.createUserRequest(sessionToken, username, password, createBillboards, editBillboards, editSchedules, editUsers);
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            } catch (NoSuchAlgorithmException ex) {
                ex.printStackTrace();
            }

            views.put(USER_EDIT, userEditView);

            // navigate to edit menu screen
            updateView(USERS_MENU);
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
            System.out.println("UserName :" + username);
            // TODO: get user information from server i.e. UserInfo userInfo = getUser(sessionToken, username, userRequest);
            try {
                String sessionToken = model.getSessionToken(); // Retrieve session token
                serverResponse = UserControl.deleteUserRequest(sessionToken, username); // CP Backend method call
                System.out.println("RESPONSE FROM SERVER: " + serverResponse);
                // TODO: RETURN CUSTOM MESSAGE/ACTION TO USER VIA GUI
                // NOTE: deleteUserRequest will return 1 of 5 ServerAcknowledgements
                // If successful, let the user know, navigate to login screen
                if (serverResponse.equals(Success)) {
                    System.out.println("CONTROLLER LEVEL - User was Successfully Deleted");
                    //DisplayUserDeletedSuccess(); // TODO: Implement some visual acknowledgement to user
                } else if (serverResponse.equals(InsufficientPermission)) { // Session token was already expired
                    System.out.println("CONTROLLER LEVEL - Session Token Was Already Expired!");
                    //DisplayInsufficientPermission(); //TODO: Implement some visual acknowledgement to user
                } else if (serverResponse.equals(InvalidToken)) {
                    //DisplayInvalidSessionToken(); //TODO: Implement some visual acknowledgement to user
                } else if (serverResponse.equals(NoSuchUser)) {
                    //DisplayUserAlreadyDeleted(); //TODO: Implement some visual acknowledgement to user
                } else if (serverResponse.equals(CannotDeleteSelf)) {
                    //DisplayCannotDeleteSelf(); //TODO: Implement some visual acknowledgement to user
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
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
            System.out.println("UserName :" + button.getName());

            // update information in VIEW USER view
            UserPreviewView userPreviewView = (UserPreviewView) views.get(USER_VIEW);
            userPreviewView.setUsername(button.getName());
            userPreviewView.setPassword("password123");
            userPreviewView.setPermissions(new boolean[]{false,true,true,true});
            views.put(USER_VIEW, userPreviewView);

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

            // get LIST USER view
            UserListView userListView = (UserListView) views.get(USER_LIST);
            Object serverResponse = null;
            ArrayList<String> usernames = null;
            ServerAcknowledge errorMessage = null;
            try {
                serverResponse = UserControl.listUsersRequest(sessionToken);
                // Attempt to cast to ArrayList
                usernames = (ArrayList<String>) serverResponse;
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
            } catch (ClassCastException ex) {
                errorMessage = (ServerAcknowledge) serverResponse;
            }

            // TODO: DO STUFF WITH ERROR MESSAGE POSSIBILITIES
            //TODO: FOR SOME REASON THIS IS SOMEHOW DUPLICATING/CHANGING THE VIEW USERS AFTER A FEW CLICKS AWAY AND BACK
            //TODO: CHANGE ADD CONTENT USAGE TO HANDLE ARRAY LIST
            userListView.addContent(usernames, new EditUserButtonListener(), new DeleteUserButtonListener(), new ViewUserButtonListener());
            views.put(USER_LIST, userListView);

            // navigate to users list screen
            updateView(USER_LIST);
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

            // navigate to edit user
            updateView(USER_EDIT);
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
            JButton button = (JButton) e.getSource();
            System.out.println("BB Name: " + button.getName());

            BBCreateView bbCreateView = (BBCreateView) views.get(BB_CREATE);
            // FIXME: GET XML FILE FROM SERVER (VIEWER CLASS WILL NEED THE SAME METHOD)
            File fileToDisplay = extractXMLFile(6);
            bbCreateView.addBBXML(fileToDisplay);
            bbCreateView.setBBName(button.getName());
            views.put(BB_CREATE, bbCreateView);

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
            ArrayList<String> stringArray = new ArrayList<>();
            stringArray.add("Myer's Biggest Sale");
            stringArray.add("Kathmandu Summer Sale");
            stringArray.add("Quilton's Covid Special");
            stringArray.add("Macca's New Essentials Range");
            bbListView.addContent(stringArray, new EditBBButtonListener(), new DeleteBBButtonListener(), new ViewBBButtonListener());
            views.put(BB_LIST, bbListView);

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
            bbCreateView.setBackgroundColour(newColor);
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
            try {
                ImageIcon icon = bbCreateView.browsePhotos();
                bbCreateView.setPhoto(icon);
            }
            catch (IOException ex) { ex.printStackTrace(); }
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
            try {
                bbCreateView.browseXMLImport();
            } catch (IOException | ParserConfigurationException | SAXException ex) {
                ex.printStackTrace();
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
            try {
                bbCreateView.browseExportFolder();
            } catch (ParserConfigurationException | TransformerException ex) {
                ex.printStackTrace();
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
     * Listener to handle Edit Schedule Button mouse clicks.
     */
    private class ScheduleEditButtonListener extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("CONTROLLER LEVEL: Edit Schedule button clicked");
            JButton button = (JButton) e.getSource();
            System.out.println("BB Name: " + button.getName());
            // navigate to edit BB screen
            updateView(SCHEDULE_UPDATE);
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
    private class ScheduleDurationListener implements ItemListener {
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
//            if (clear)
//            {
//                // TODO: remove schedule from DB
//            }
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
            if (eventId == ItemEvent.SELECTED)
            {
                System.out.println("CONTROLLER LEVEL: Schedule Populate button clicked");
                JComboBox menuItem = (JComboBox) e.getSource();
                String bbName = (String)menuItem.getSelectedItem();
                System.out.println("selected " + bbName);
                ScheduleUpdateView scheduleUpdateView = (ScheduleUpdateView) views.get(SCHEDULE_UPDATE);
                // FIXME: get BB Schedule for this menu item
                if (bbName.equals("Myer"))
                {
                    boolean[] daysOfWeek = new boolean[]{true,true,false,false,false,false,false};
                    int startHour = 5;
                    int startMin = 06;
                    int duration = 30;
                    int minRepeat = 220;
                    String recurrenceButton = "minute";
                    scheduleUpdateView.setScheduleValues(daysOfWeek, startHour, startMin, duration, recurrenceButton, minRepeat);
                }
                else if (bbName.equals("Anaconda"))
                {
                    boolean[] daysOfWeek = new boolean[]{true,true,true,true,true,true,true};
                    int startHour = 1;
                    int startMin = 0;
                    int duration = 30;
                    int minRepeat = -1;
                    String recurrenceButton = "hourly";
                    scheduleUpdateView.setScheduleValues(daysOfWeek, startHour, startMin, duration, recurrenceButton, minRepeat);
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
                ArrayList<Object> scheduleInfo = scheduleUpdateView.getScheduleInfo();
                System.out.println("SEND INFO TO DB "+scheduleInfo);
                scheduleUpdateView.showConfirmationDialog();
                views.put(SCHEDULE_UPDATE, scheduleUpdateView);
                // navigate to schedule menu
                updateView(SCHEDULE_MENU);
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