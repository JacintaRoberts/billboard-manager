package controlPanel;

import observer.Subject;

public class Model extends Subject
{
    private String username;

    public Model()
    {
    }

    public void updateModel()
    {
        System.out.println("You pressed the HomeView Button - update Model!");
        notifyObservers();
    }

    public void submitLogin(String username)
    {
        System.out.println("You pressed the Submit Button - update Model!");
        // store username
        this.username = username;
    }
}
