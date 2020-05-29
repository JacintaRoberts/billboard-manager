package controlPanel;

import observer.Subject;
import controlPanel.Main.VIEW_TYPE;

import java.awt.*;

/**
 * Model class contains all data that is persistent during a session.
 */
public class Model extends Subject
{
    // *** VARIABLES**
    private String username;
    private String sessionToken;
    private VIEW_TYPE currentView;
    private VIEW_TYPE previousView;

    /**
     * Model Constructor
     */
    public Model()
    {
        // set the current to LOGIN frame & previous to HOME
        currentView = VIEW_TYPE.LOGIN;
        previousView = VIEW_TYPE.HOME;
    }

    /**
     * Get current Username string
     * @return username string
     */
    public String getUsername()
    {
        return this.username;
    }

    /**
     * Get current View
     * @return user's current view
     */
    public VIEW_TYPE getCurrentView()
    {
        return this.currentView;
    }

    /**
     * Set user's current View
     * @param newView
     */
    public void setCurrentView(VIEW_TYPE newView)
    {
        // redefine previous and current view
        this.previousView = this.currentView;
        this.currentView = newView;

    }

    /**
     * Get User's previous view
     * @return previous view
     */
    public VIEW_TYPE getPreviousView()
    {
        return this.previousView;
    }

    /**
     * Store username string
     * @param username username string to store
     */
    public void storeUsername(String username)
    {
        System.out.println("MODEL LEVEL: Store username in model. User: " + username);
        this.username = username;
    }

    /**
     * Store session token string
     * @param sessionToken tocken string to store
     */
    public void storeSessionToken(String sessionToken)
    {
        System.out.println("MODEL LEVEL: Store session token in model. Session Token: " + sessionToken);
        this.sessionToken = sessionToken;
    }

    /**
     * Get session token string
     * @return session token string
     */
    public String getSessionToken()
    {
        return sessionToken;
    }
}
