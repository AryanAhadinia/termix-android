package android.termix.ssc.ce.sharif.edu.network;

/**
 * NetworkException
 * Indicate an error on http status code.
 *
 * @author AryanAhadinia
 * @since 1
 */
public class NetworkException extends Exception {
    private final int statusCode;

    public NetworkException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
