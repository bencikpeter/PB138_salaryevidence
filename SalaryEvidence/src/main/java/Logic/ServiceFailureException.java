package Logic;

/**
 *
 * @author Peter 
 */
public class ServiceFailureException extends Exception {
    
    public ServiceFailureException() {
        super();
    }
    
    public ServiceFailureException(String message) {
        super(message);
    }

    public ServiceFailureException(Throwable cause) {
        super(cause);
    }

    public ServiceFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
