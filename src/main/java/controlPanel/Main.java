package controlPanel;

import helpers.Helpers;

import java.io.IOException;
import java.util.HashMap;
import static controlPanel.Main.VIEW_TYPE.*;

/**
 * Main Class creates instance of all application views and associates to an enum in a HashMap.
 * The model is instantiated and passed through along with hashmap to controller.
 */
public class Main {

    // hashmap to store Enums associated to Views
    private static HashMap<VIEW_TYPE, AbstractView> app_views = new HashMap<>();

    // create enum for all application views
    protected enum VIEW_TYPE {
        LOGIN, HOME,
        BB_MENU, BB_LIST, BB_CREATE, BB_PREVIEW,
        USERS_MENU, USER_VIEW,USER_LIST, USER_EDIT,
        SCHEDULE_MONTH, SCHEDULE_DAY, SCHEDULE_UPDATE;
    }

    /**
     * Main starts up the MVC application by instantiating the Model, Viewers and Controller.
     * Schedules threads to run application.
     * @param args
     */
    public static void main(String[] args)
    {
        // TODO: ONLY PUT THIS IN THE CP BACKEND FUNCTIONS WHEN NEED TO CONNECT TO SERVER - HERE FOR TESTING
        try {
            //Object serverResponse = initClient("Test,");
            Object serverResponse = Helpers.initClient("Logout,sessionToken");
            //Object serverResponse = initClient("Something else...");
            System.out.println("Received from server: " + serverResponse.toString());
        } catch (IOException | ClassNotFoundException e) { // Could not connect to server
            //TODO: USE GUI TO HANDLE EXCEPTION + NOTIFY USER
            System.err.println("Exception caught: " + e);
        }

        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                setUpMVC();
            }
        });
    }

    /**
     * Set up MVC. Create instance of Model, Views to then pass through to an instance of the Controller.
     */
    private static void setUpMVC()
    {
        // create an instance of the model
        Model model = new Model();

        // create an instance of the Views
        // generic
        app_views.put(LOGIN, new LogInView());
        app_views.put(HOME, new HomeView());
        // billboard
        app_views.put(BB_CREATE, new BBCreateView());
        app_views.put(BB_MENU, new BBMenuView());
        app_views.put(BB_LIST, new BBListView());
        // users
        app_views.put(USERS_MENU, new UsersMenuView());
        app_views.put(USER_VIEW, new UserView());
        app_views.put(USER_LIST, new UserListView());
        app_views.put(USER_EDIT, new UserEditView());
        // schedule
        app_views.put(SCHEDULE_MONTH, new ScheduleMonthlyView());
        app_views.put(SCHEDULE_DAY, new ScheduleDailyView());
        app_views.put(SCHEDULE_UPDATE, new ScheduleUpdateView());

        // set up the controller
        Controller controller = new Controller(model, app_views);
    }
}
