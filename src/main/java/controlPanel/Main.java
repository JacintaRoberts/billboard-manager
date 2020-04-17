package controlPanel;

import java.util.HashMap;

public class Main {

    private static HashMap<VIEW_TYPE, AbstractView> app_views = new HashMap<>();

    protected enum VIEW_TYPE {
        LOGIN, HOME, BB_MENU, USERS_MENU, CREATE_BB, USER_VIEW, SCHEDULE, USER_LIST;
    }

    /**
     * Main starts up the MVC application by instantiating the Model, Viewers and Controller.
     * @param args
     */
    public static void main(String[] args)
    {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createController();
            }
        });
    }

    private static void createController()
    {
        // create an instance of the model
        Model model = new Model();

        // create an instance of the Views
        app_views.put(VIEW_TYPE.LOGIN, new LogInView());
        app_views.put(VIEW_TYPE.HOME, new HomeView());
        app_views.put(VIEW_TYPE.BB_MENU, new BBMenuView());
        app_views.put(VIEW_TYPE.USERS_MENU, new UsersMenuView());
        app_views.put(VIEW_TYPE.SCHEDULE, new ScheduleView());
        app_views.put(VIEW_TYPE.USER_VIEW, new UserView());
        app_views.put(VIEW_TYPE.CREATE_BB, new BBCreateView());
        app_views.put(VIEW_TYPE.USER_LIST, new UserListView());

        // set up the controller
        Controller controller = new Controller(model, app_views);
    }
}
