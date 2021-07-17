package android.termix.ssc.ce.sharif.edu.network;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

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

    @NotNull
    @Override
    public String toString() {
        return String.format(Locale.US, "%d: %s", getStatusCode(), getMessage());
    }
}
