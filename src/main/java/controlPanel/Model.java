package controlPanel;

import observer.Subject;

public class Model extends Subject
{
    // *** VARIABLES**
    private String username;
    private String sessionToken;

    /**
     * Model Constructor
     */
    public Model()
    {
    }

    /**
     * Get current Username string
     * @return username string
     */
    public String getUsername()
    {
        return username;
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

    public void storeSessionToken(String sessionToken)
    {
        System.out.println("MODEL LEVEL: Store session token in model. Session Token: " + sessionToken);
        this.sessionToken = sessionToken;
    }

    public String getSessionToken(String sessionToken)
    {
        return sessionToken;
    }
}
