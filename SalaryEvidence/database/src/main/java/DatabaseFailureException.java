package main.java;

/**
 * DatabaseFailureException represent database exceptions
 * Created by oldrichkonecny on 13.05.16.
 */
public class DatabaseFailureException extends RuntimeException {

    public DatabaseFailureException(String message) { super(message); }

    public DatabaseFailureException(Throwable cause) { super(cause); }

    public DatabaseFailureException(String message, Throwable cause) { super(message, cause); }

}
