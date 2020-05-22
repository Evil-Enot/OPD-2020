package utils;

/**
 * Throw if link url was not parsed
 */
public class WrongFormedLinkException extends RuntimeException {
    public WrongFormedLinkException(String url, Throwable cause) {
        super("Not a site " + url, cause);
    }
}
