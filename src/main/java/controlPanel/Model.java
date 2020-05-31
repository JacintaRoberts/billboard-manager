package controlPanel;

import controlPanel.Main.VIEW_TYPE;

/**
 * Model class contains all data that persists during a session. This includes the current username,
 * session token and the current view seen by the user.
 */
public class Model
{
    // *** VARIABLES**
    private String username;
    private String sessionToken;
    private VIEW_TYPE currentView;

    /**
     * Model Constructor
     */
    public Model()
    {
        // set the current to LOGIN frame
        currentView = VIEW_TYPE.LOGIN;
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
     * @param newView enum view type of the current view
     */
    public void setCurrentView(VIEW_TYPE newView)
    {
        this.currentView = newView;
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
     * @param sessionToken token string to store
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
