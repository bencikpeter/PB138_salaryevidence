package main.java;

/**
 * Created by olda on 08.06.2016.
 */
public class ValidateException extends RuntimeException {

    public ValidateException(String message) { super(message); }

    public ValidateException(Throwable cause) { super(cause); }

    public ValidateException(String message, Throwable cause) { super(message, cause); }
}
