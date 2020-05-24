package controlPanel;

import controlPanel.Main.VIEW_TYPE;
import org.xml.sax.SAXException;
import server.Server.ServerAcknowledge;
import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
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
                ScheduleWeekView scheduleWeekView = (ScheduleWeekView) views.get(SCHEDULE_WEEK);

                // FIXME: SERVER CALL: getBillboardSchedule(Monday), getBillboardSchedule(Tuesday) which will return ArrayList<ArrayList<String>>

                // Billboard Schedule: day, time, bb name
                ArrayList<ArrayList<String>> billboardScheduleMonday = new ArrayList<>();
                billboardScheduleMonday.add(new ArrayList<>(Arrays.asList("1-2pm", "Myer's Sale")));
                billboardScheduleMonday.add(new ArrayList<>(Arrays.asList("1-2pm", "Myer's Sale")));
                billboardScheduleMonday.add(new ArrayList<>(Arrays.asList("1-2pm", "Myer's Sale")));
                billboardScheduleMonday.add(new ArrayList<>(Arrays.asList("1-2pm", "Myer's Sale")));
                billboardScheduleMonday.add(new ArrayList<>(Arrays.asList("1-2pm", "Myer's Sale")));

                ArrayList<ArrayList<String>> billboardScheduleTuesday = new ArrayList<>();
                billboardScheduleTuesday.add(new ArrayList<>(Arrays.asList("1-2pm", "Myer's Sale")));
                billboardScheduleTuesday.add(new ArrayList<>(Arrays.asList("1-2pm", "Myer's Sale")));
                billboardScheduleTuesday.add(new ArrayList<>(Arrays.asList("1-2pm", "Myer's Sale")));
                billboardScheduleTuesday.add(new ArrayList<>(Arrays.asList("1-2pm", "Myer's Sale")));
                billboardScheduleTuesday.add(new ArrayList<>(Arrays.asList("1-2pm", "Myer's Sale")));

                ArrayList<ArrayList<ArrayList<String>>> schedule = new ArrayList<>();
                schedule.add(billboardScheduleMonday);
                schedule.add(billboardScheduleTuesday);

                scheduleWeekView.populateSchedule(schedule);
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
            try {
                serverResponse = UserControl.getPermissionsRequest(sessionToken, username);
                ArrayList<Boolean> userPermissions = (ArrayList<Boolean>) serverResponse;
                // FIXME: setPermissions is wrong mapping
                userProfileView.setPermissions(userPermissions);
            } catch (IOException | ClassNotFoundException ex) {
                // TODO: error pop-up window for fatal error
                // terminate Control Panel and restart
                ex.printStackTrace();
                // Error handling
            } catch ( ClassCastException ex ) {
                if (serverResponse.equals(InvalidToken)) {
                    // TODO: error pop-up window for expired session
                    // navigate to logout/login screen
                } else if (serverResponse.equals(NoSuchUser)) {
                    // TODO: error pop-up window for deleted user
                    // display error and navigate to logout/login screen
                }
            }
            //TODO: GIVE THE USER THE OPTION OF "CHANGING" PASSWORD RATHER THAN SHOWING PLAIN TEXT VERSION AS
            // HASHING IS 1 WAY AND IT'S IMPOSSIBLE TO DO THAT CURRENTLY.
            // FIX: userProfileView.setPassword();
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
            // Retrieve server response
            try {
                String sessionToken = model.getSessionToken(); // Retrieve session token
                serverResponse = logoutRequest(sessionToken); // CP Backend method call
                System.out.println("Received from server: " + serverResponse);
            } catch (IOException | ClassNotFoundException ex) {
                // TODO: error pop-up window for fatal error
                // terminate Control Panel and restart
                ex.printStackTrace();
            }

            // If successful, let the user know, navigate to login screen
            if (serverResponse.equals(Success)) {
                System.out.println("CONTROLLER LEVEL - Session Token Successfully Expired");
                //DisplayLogoutSuccess(); // TODO: Implement some visual acknowledgement to user
            // Error handling as follows:
            } else { // Session token was already expired
                System.out.println("CONTROLLER LEVEL - Session Token Was Already Expired!");
                //DisplaySessionTokenExpired(); //TODO: Implement some visual acknowledgement to user
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
            model.storeSessionToken("");

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
                // TODO: error pop-up window for fatal error
                // terminate Control Panel and restart
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
            try {
                serverResponse = UserControl.getPermissionsRequest(sessionToken, usernameSelected);
                ArrayList<Boolean> userPermissions = (ArrayList<Boolean>) serverResponse;
                // FIXME: setPermissions is wrong mapping
                userEditView.setPermissions(userPermissions);
            } catch (IOException | ClassNotFoundException ex) {
                // TODO: error pop-up window for fatal error
                // terminate Control Panel and restart
                ex.printStackTrace();
                // Error handling
            } catch ( ClassCastException ex ) {
                if (serverResponse.equals(InvalidToken)) {
                    // TODO: error pop-up window for expired session
                    // navigate to logout/login screen
                } else if (serverResponse.equals(NoSuchUser)) {
                    // TODO: error pop-up window for deleted user
                    // display error and navigate to logout/login screen
                }
            }
            //TODO: GIVE THE USER THE OPTION OF "CHANGING" PASSWORD RATHER THAN SHOWING PLAIN TEXT VERSION AS
            // HASHING IS 1 WAY AND IT'S IMPOSSIBLE TO DO THAT CURRENTLY.
            // FIX: userEditView.setPassword("Password");

            // set Title of Screen
            userEditView.setBBFrameTitle("EDIT USER");
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

            // update information in CREATE USER view
            UserEditView userEditView = (UserEditView) views.get(USER_EDIT);
            userEditView.setBBFrameTitle("CREATE USER");
            views.put(USER_EDIT, userEditView);

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
            UserEditView userEditView = (UserEditView) views.get(USER_EDIT);
            boolean validUserInput = userEditView.checkValidUser();
            // if valid user input, ask user to confirm user creation
            if (validUserInput)
            {
                int response = userEditView.showCreateUserConfirmation();
                // add user to DB if user confirms user creation
                if (response == 0)
                {
                    ArrayList<Object> userArray = userEditView.getUserInfo();

                    // Parsing elements from user array for the UserControl method to create the request to server
                    String username = (String) userArray.get(0);
                    String password = (String) userArray.get(1);
                    Boolean createBillboards = (Boolean) userArray.get(5);
                    Boolean editBillboards = (Boolean) userArray.get(2);
                    Boolean editSchedules = (Boolean) userArray.get(3);
                    Boolean editUsers = (Boolean) userArray.get(4);

                    // Retrieve server response
                    try {
                        serverResponse = UserControl.createUserRequest(sessionToken, username, password, createBillboards, editBillboards, editSchedules, editUsers);
                    } catch (IOException | NoSuchAlgorithmException | ClassNotFoundException ex) {
                        ex.printStackTrace();
                        // TODO: error pop-up window for fatal error
                        // terminate Control Panel and restart
                    }

                    // Filter response and display appropriate action
                    if (serverResponse.equals(Success)) {
                        // TODO: success pop up window
                    }
                    else if (serverResponse.equals(InvalidToken)) {
                        // TODO: error pop up window for expired session
                        // navigate to logout/login screen
                    } else if (serverResponse.equals(InsufficientPermission)) {
                        // TODO: error pop up window for insufficient permissions
                        // let the user know they don't have necessary permissions
                }
                views.put(USER_EDIT, userEditView);

                // navigate to edit menu screen
                updateView(USERS_MENU);
                }
            }
            else
            {
                userEditView.showErrorMessage();
            }
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
            if (response == 0) // TODO: NOT SURE WHAT RESPONSE == 0 CORRESPONDS TO BUT MAY NEED TO BE INTEGRATED BELOW
            {
                System.out.println("Delete user from DB");
                // Retrieve response from DB
                try {
                    String sessionToken = model.getSessionToken(); // Retrieve session token
                    serverResponse = UserControl.deleteUserRequest(sessionToken, username); // CP Backend method call
                    //System.out.println("RESPONSE FROM SERVER: " + serverResponse);
                } catch (IOException | ClassNotFoundException ex) {
                    // TODO: error pop-up window for fatal error
                    // terminate Control Panel and restart
                    ex.printStackTrace();
                }

                // If successful, let the user know
                if (serverResponse.equals(Success)) {
                    System.out.println("CONTROLLER LEVEL - User was Successfully Deleted");
                    //DisplayUserDeletedSuccess(); // TODO: Implement some visual acknowledgement to user
                // Error handling on GUI as follows
                } else if (serverResponse.equals(InsufficientPermission)) {
                    System.out.println("CONTROLLER LEVEL - Insufficient Permission");
                    //DisplayInsufficientPermission(); //TODO: Implement some visual acknowledgement to user
                } else if (serverResponse.equals(InvalidToken)) {
                    System.out.println("CONTROLLER LEVEL - Invalid Token");
                    //DisplayInvalidSessionToken(); //TODO: Implement some visual acknowledgement to user
                } else if (serverResponse.equals(NoSuchUser)) {
                    System.out.println("CONTROLLER LEVEL - No Such User Found in DB to be Deleted");
                    //DisplayUserAlreadyDeleted(); //TODO: Implement some visual acknowledgement to user
                } else if (serverResponse.equals(CannotDeleteSelf)) {
                    System.out.println("CONTROLLER LEVEL - Cannot Delete Your Own User");
                    //DisplayCannotDeleteSelf(); //TODO: Implement some visual acknowledgement to user
                }

                // navigate back to the same page to refresh view
                updateView(USER_LIST);
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
            try {
                serverResponse = UserControl.getPermissionsRequest(sessionToken, usernameSelected);
                ArrayList<Boolean> userPermissions = (ArrayList<Boolean>) serverResponse;
                // FIXME: setPermissions is wrong mapping
                userPreviewView.setPermissions(userPermissions);
            } catch (IOException | ClassNotFoundException ex) {
                // TODO: error pop-up window for fatal error
                // terminate Control Panel and restart
                ex.printStackTrace();
                // Error handling
            } catch ( ClassCastException ex ) {
                if (serverResponse.equals(InvalidToken)) {
                    // TODO: error pop-up window for expired session
                    // navigate to logout/login screen
                } else if (serverResponse.equals(NoSuchUser)) {
                    // TODO: error pop-up window for deleted user
                    // display error and navigate to logout/login screen
                }
            }
            //TODO: GIVE THE USER THE OPTION OF "CHANGING" PASSWORD RATHER THAN SHOWING PLAIN TEXT VERSION AS
            // HASHING IS 1 WAY AND IT'S IMPOSSIBLE TO DO THAT CURRENTLY.
            // FIX: userPreviewView.setPassword("Password");
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
            ArrayList<String> usernames = null;
            ServerAcknowledge errorMessage = Success;
            try {
                serverResponse = UserControl.listUsersRequest(sessionToken);
                // Attempt to cast to a string ArrayList for successful response
                usernames = (ArrayList<String>) serverResponse;
            } catch (IOException | ClassNotFoundException ex) {
                // TODO: error pop-up window for fatal error
                // terminate Control Panel and restart
                ex.printStackTrace();
            } catch (ClassCastException ex) {
                // Otherwise, some other error message was returned from the server
                errorMessage = (ServerAcknowledge) serverResponse;
            }

            // Error handling on GUI as follows
            if (errorMessage.equals(InsufficientPermission)) {
                System.out.println("CONTROLLER LEVEL - Insufficient Permissions");
                //DisplayInsufficientPermission(); //TODO: Implement some visual acknowledgement to user
            } else if (serverResponse.equals(InvalidToken)) {
                System.out.println("CONTROLLER LEVEL - Invalid Token");
                //DisplayInvalidSessionToken(); //TODO: Implement some visual acknowledgement to user
            } else { // Successful, let the user know and populate with list of users
                userListView.addContent(usernames, new EditUserButtonListener(), new DeleteUserButtonListener(), new ViewUserButtonListener());
                views.put(USER_LIST, userListView);
            }

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
            // get USER EDIT VIEW
            UserEditView userEditView = (UserEditView) views.get(USER_EDIT);
            String username = model.getUsername();

            // set username, password and permissions in Profile View
            userEditView.setUsername(username);

            // Get user permissions from server
            try {
                serverResponse = UserControl.getPermissionsRequest(sessionToken, username);
                ArrayList<Boolean> userPermissions = (ArrayList<Boolean>) serverResponse;
                // FIXME: setPermissions is wrong mapping
                userEditView.setPermissions(userPermissions);
            } catch (IOException | ClassNotFoundException ex) {
                // TODO: error pop-up window for fatal error
                // terminate Control Panel and restart
                ex.printStackTrace();
                // Error handling
            } catch ( ClassCastException ex ) {
                if (serverResponse.equals(InvalidToken)) {
                    // TODO: error pop-up window for expired session
                    // navigate to logout/login screen
                } else if (serverResponse.equals(NoSuchUser)) {
                    // TODO: error pop-up window for deleted user
                    // display error and navigate to logout/login screen
                }
            }
            //TODO: GIVE THE USER THE OPTION OF "CHANGING" PASSWORD RATHER THAN SHOWING PLAIN TEXT VERSION AS
            // HASHING IS 1 WAY AND IT'S IMPOSSIBLE TO DO THAT CURRENTLY.
            // FIX: userEditView.setPassword("Password");

            views.put(USER_EDIT, userEditView);

            // navigate to edit users
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
            // get the BB name associated to the edit button
            JButton button = (JButton) e.getSource();
            String BBName = button.getName();

            BBCreateView bbCreateView = (BBCreateView) views.get(BB_CREATE);

            bbCreateView.showBBEditingMessage(BBName);

            // FIXME: GET XML FILE FROM SERVER (VIEWER CLASS WILL NEED THE SAME METHOD)
            // FIXME: SERVER CALL: getBBXML(bbName) returning a File

            // TODO: remove once server call is working
            File fileToDisplay = extractXMLFile(6);

            bbCreateView.addBBXML(fileToDisplay);

            // set BB Name based on selected button
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

            // get the BB name associated to the edit button
            JButton button = (JButton) e.getSource();
            String BBName = button.getName();

            BBListView bbListView = (BBListView) views.get(BB_LIST);

            // ask user to confirm deletion of BB
            int response = bbListView.showDeleteConfirmation();

            // if confirmed response, delete from DB
            if (response == 0)
            {
                // FIXME: SERVER CALL: deleteBB(BBName)

                // navigate to bb list screen to refresh screen
                updateView(BB_LIST);
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
            // FIXME: SERVER CALL - getListOfBillboardNames() return an ArrayList<String> of all Billboard Names
            ArrayList<String> stringArray = new ArrayList<>();
            stringArray.add("Myer's Biggest Sale");
            stringArray.add("Kathmandu Summer Sale");
            stringArray.add("Quilton's Covid Special");
            stringArray.add("Macca's New Essentials Range");
            stringArray.add("David Jones's Biggest Sale");
            stringArray.add("BigW Summer Sale");
            stringArray.add("Kmarts's Covid Special");
            stringArray.add("Mecca's New Essentials Range");
            stringArray.add("Peter Alexanders's Biggest Sale");
            stringArray.add("Target Summer Sale");
            stringArray.add("BigW Summer Sale");
            stringArray.add("Kmarts's Covid Special");
            stringArray.add("Mecca's New Essentials Range");
            stringArray.add("Peter Alexanders's Biggest Sale");
            stringArray.add("Target Summer Sale");
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
            if (!bbName.equals(""))
            {
                // raise confirmation to create BB
                int confirmCreation = bbCreateView.showConfirmationCreateBB();
                // if user confirms BB creation, show scheduling option
                if (confirmCreation == 0)
                {
                    // show scheduling option - asking user if they want to schedule BB now
                    int optionSelected = bbCreateView.showSchedulingOption();

                    // FIXME: SERVER CALL: addBBXML(BBXMLFile) ADD BB TO DB!!!
                    ArrayList<Object> BBXMLFile = bbCreateView.getBBXML();

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
            // if no bb name is provided, alert user to add one
            else
            {
                bbCreateView.showBBNameErrorMessage();
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

                if (bbName.equals("Myer"))
                {
                    ArrayList<Boolean> daysOfWeek = new ArrayList<>();
                    daysOfWeek.add(true);
                    daysOfWeek.add(true);
                    daysOfWeek.add(true);
                    daysOfWeek.add(true);
                    daysOfWeek.add(true);
                    daysOfWeek.add(true);
                    daysOfWeek.add(true);
                    int startHour = 5;
                    int startMin = 6;
                    int duration = 30;
                    int minRepeat = 220;
                    String recurrenceButton = "minute";
                    scheduleUpdateView.setScheduleValues(daysOfWeek, startHour, startMin, duration, recurrenceButton, minRepeat);
                }
                else if (bbName.equals("Anaconda"))
                {
                    ArrayList<Boolean> daysOfWeek = new ArrayList<>();
                    daysOfWeek.add(true);
                    daysOfWeek.add(true);
                    daysOfWeek.add(true);
                    daysOfWeek.add(true);
                    daysOfWeek.add(true);
                    daysOfWeek.add(true);
                    daysOfWeek.add(true);
                    int startHour = 1;
                    int startMin = 0;
                    int duration = 30;
                    int minRepeat = -1;
                    String recurrenceButton = "hourly";
                    scheduleUpdateView.setScheduleValues(daysOfWeek, startHour, startMin, duration, recurrenceButton, minRepeat);
                }
                else
                {
                    // alert user that no schedule exists in db
                    scheduleUpdateView.showNoExistingScheduleMessage();
                    // clear the schedule
                    scheduleUpdateView.removeScheduleSelection();
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
                int response = scheduleUpdateView.showConfirmationCreateSchedule();
                // confirmation response
                if (response == 0)
                {
                    ArrayList<Object> scheduleInfo = scheduleUpdateView.getScheduleInfo();
                    // FIXME: SCHEDULE CONTROL: storeBBSchedule(scheduleInfo)
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