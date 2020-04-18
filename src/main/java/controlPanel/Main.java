package controlPanel;

import java.util.HashMap;

import static controlPanel.Main.VIEW_TYPE.*;

public class Main {

    private static HashMap<VIEW_TYPE, AbstractView> app_views = new HashMap<>();

    protected enum VIEW_TYPE {
        LOGIN, HOME, BB_MENU, BB_LIST, USERS_MENU, BB_CREATE, BB_PREVIEW, USER_VIEW, SCHEDULE, USER_LIST, USER_EDIT;
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
        app_views.put(LOGIN, new LogInView());
        app_views.put(HOME, new HomeView());
        app_views.put(BB_MENU, new BBMenuView());
        app_views.put(USERS_MENU, new UsersMenuView());
        app_views.put(SCHEDULE, new ScheduleView());
        app_views.put(USER_VIEW, new UserView());
        app_views.put(BB_CREATE, new BBCreateView());
        app_views.put(USER_LIST, new UserListView());
        app_views.put(BB_LIST, new BBListView());
        app_views.put(USER_EDIT, new UserEditView());
//        app_views.put(BB_PREVIEW, new BBPreviewView());

        // set up the controller
        Controller controller = new Controller(model, app_views);
    }
}
