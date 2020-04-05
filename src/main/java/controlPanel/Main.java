package controlPanel;

public class Main {

    /**
     * Main starts up the MVC application by instantiating the Model, Viewers and Controller.
     * @param args
     */
    public static void main(String[] args)
    {
        // create an instance of the model
        Model model = new Model();
        // create an instance of the Viewers
        LogInView login = new LogInView();
        HomeView homeView = new HomeView();
        // set up the controller
        Controller controller = new Controller(model, login, homeView);
    }
}
