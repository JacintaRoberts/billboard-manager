//package controlPanel;
//
//import controlPanel.Main.VIEW_TYPE;
//
//import javax.swing.*;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.util.HashMap;
//
//
//public class Controller
//{
//    private Model model;
////    private HomeView homeView;
////    private LogInView logInView;
////    private UsersMenuView usersMenuView;
////    private BBMenuView bbMenuView;
////    private CreateBBView createBBView;
//    private HashMap<VIEW_TYPE, JFrame> views;
//
//    /**
//     * Controller Constructor stores instances of Views, adds listeners to Views and sets up Log In View allowing users
//     * to log in to the application.
//     * @param model application's model
//     * @param views hashmap of views
//     */
//    public Controller(Model model, HashMap<VIEW_TYPE, JFrame> views)
//    {
//        // store instances of views
//        this.model = model;
////        this.homeView = (HomeView) views.get(VIEW_TYPE.HOME);
////        this.logInView = (LogInView) views.get(VIEW_TYPE.LOGIN);
//        this.views = views;
//
//        // adds listeners to views
//        addLogInListener();
//        addHomeListener();
////        addUsersListener();
////        addBillboardListener();
//
//        // set up log in frame
//        model.attachObserver((LogInView) views.get(VIEW_TYPE.LOGIN));
//        logInView.setVisible(true);
//        model.setCurrentView(VIEW_TYPE.LOGIN);
//    }
//
//    private void get
//
//    private void addLogInListener()
//    {
//        // add listeners
//        logInView.addSubmitButtonListener(new SubmitButtonListener());
//    }
//
//
//    /**
//     * Define all the listeners here to simplify the constructor
//     */
//    private void addHomeListener()
//    {
//        homeView.addBillboardsButtonListener(new BillboardsButtonListener());
//        homeView.addUsersButtonListener(new UsersButtonListener());
//    }
//
////    private void addUsersListener()
////    {
////        usersMenuView.addHomeButtonListener(new HomeButtonListener());
////    }
////
////    private void addBillboardListener()
////    {
////        bbMenuView.addHomeButtonListener(new HomeButtonListener());
////        bbMenuView.addCreateButtonListener(new CreateButtonListener());
////    }
//
//    /**
//     * Change user's view by hiding the old view and showing the new.
//     * Detach observer from old view and attach observer to new.
//     * @param newView the user's new view
//     */
//    public void updateView(AbstractGenericView newView)
//    {
//        // FIXME: do I need a switch case here? to define current view as we only want one copy!
//        // detach observer & hide old view
//        model.detachObserver(model.getCurrentView());
//        currentView.setVisible(false);
//
//        // check permissions
//        // this could be a switch case - to show/hide features
////        checkPermissions(newView);
//
//        // redefine view
//        this.currentView = newView;
//
//        // set new view to be visible
//        currentView.setVisible(true);
//
//        // attach observer to new view
//        model.attachObserver(newView);
//    }
//
//    private void checkPermissions(VIEW_TYPE key)
//    {
//        switch(key)
//        {
//            case HOME:
//                System.out.println("Check Home permission");
//                break;
//            case LOGIN:
//                // if new view is not log in screen, set welcome text
//                logInView.setWelcomeText(model.getUsername());
//                System.out.println("Check LogIn permission");
//                break;
//        }
//
//    }
//
//    /**
//     * Listener to handle user's log in attempt. The username and password is retrieved from the GUI input and a
//     * request is sent to server to check validity of user. If response is true, username is stored in model and user
//     * is navigated to home screen. If invalid credentials, error is displayed.
//     */
//    private class SubmitButtonListener extends MouseAdapter
//    {
//        @Override
//        public void mouseClicked(MouseEvent e)
//        {
//            System.out.println("CONTROLLER LEVEL: Submit button clicked");
//
//            LogInView logInView = (LogInView) views.get(VIEW_TYPE.LOGIN);
//
//            // get username and password text from GUI
//            String username = logInView.getUsername();
//            String password = logInView.getPassword();
//            // TODO: String[] response = userControl.loginRequest(username, Password)
//            // String valid = response[0];
//            // String responseMessage = response[1]; This could either be error message or session token
//            boolean response = false;
//            String sessionToken = "SessionToken";
//            if ((username.equals("User") && password.equals("Password")) || (username.equals("Team60") && password.equals("Password")))
//            {
//                response = true;
//            }
//
//            // if successful
//            if (response)
//            {
//                // store username and session token in model
//                model.storeUsername(username);
//                model.storeSessionToken(sessionToken);
//
//                // **************************************
//                checkPermissions(VIEW_TYPE.LOGIN);
//                // ** CHECK PERMISSIONS **
//                // check permissions
//                // TODO: implement this is Control
//                boolean editUsersPermission;
////            boolean editUsersPermission = checkPermission(model.getUsername(), 2, model.getSessionToken());
//                if(model.getUsername().equals("Team60"))
//                {
//                    editUsersPermission = true;
//                }
//                else
//                {
//                    editUsersPermission = false;
//                }
//
//                // set visibility of users button
//                if (editUsersPermission)
//                {
//                    HomeView homeView = (HomeView) views.get(VIEW_TYPE.HOME);
//                    homeView.usersButton.setVisible(true);
//                    System.out.println("SHOW");
//                }
//                else
//                {
//                    HomeView homeView = (HomeView) views.get(VIEW_TYPE.HOME);
//                    homeView.usersButton.setVisible(false);
//                    System.out.println("HIDE");
//                }
//                // **************************************
//
//                // nav user to home screen
//                updateView(views.get(VIEW_TYPE.HOME));
//                // hide error string
//                logInView.setErrorVisibility(false);
//            }
//            else
//            {
//                // show error message
//                logInView.setErrorVisibility(true);
//            }
//        }
//    }
//
//    /**
//     * Listener to handle Home Button mouse clicks. If user clicks the Home button, user is navigated to Home Screen.
//     */
//    private class HomeButtonListener extends MouseAdapter
//    {
//        @Override
//        public void mouseClicked(MouseEvent e)
//        {
//            System.out.println("CONTROLLER LEVEL: Home button clicked");
//            // navigate to home screen
//            updateView(views.get(VIEW_TYPE.HOME));
//        }
//    }
//
//    private class CreateButtonListener extends MouseAdapter
//    {
//        @Override
//        public void mouseClicked(MouseEvent e)
//        {
//            System.out.println("CONTROLLER LEVEL: Create button clicked");
//            // navigate to billboard view
//            updateView(views.get(VIEW_TYPE.BILLBOARDS));
//        }
//    }
//
//    private class
//}
