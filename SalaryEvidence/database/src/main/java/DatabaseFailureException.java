package main.java;

/**
 * Created by olda on 09.06.2016.
 */
public class DatabaseFailureException extends Exception {

    public DatabaseFailureException(String message) { super(message); }

    public DatabaseFailureException(Throwable cause) { super(cause); }

    public DatabaseFailureException(String message, Throwable cause) { super(message, cause); }
}
