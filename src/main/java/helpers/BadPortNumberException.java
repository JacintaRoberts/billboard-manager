package helpers;

/**
 * This exception indicates that there was a bad port number in the network.props file for the server configuration.
 * Occurs when port number is less than or equal to 0 or greater than 65535.
 */
public class BadPortNumberException extends Exception {
    public BadPortNumberException(String message)
    {
        super(message);
    }
}
