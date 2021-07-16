package android.termix.ssc.ce.sharif.edu.network;

import java.util.Locale;

import okhttp3.HttpUrl;
import okhttp3.Request;

/**
 * An abstract class for networking.
 *
 * @author AryanAhadinia
 * @since 1
 */
public abstract class NetworkTask implements Runnable {
    private static String token;
    private static long expiry;

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        NetworkTask.token = token;
    }

    public static boolean hasCredential() {
        if (token == null) {
            return false;
        }
        return System.currentTimeMillis() < expiry;
    }

    protected abstract HttpUrl getURL();

    protected Request getRequest() {
        return new Request.Builder().url(getURL()).build();
    }

    protected Request getRequestWithCredential() {
        return new Request.Builder()
                .url(getURL())
                .addHeader("Cookie", String.format(Locale.US, "token=%s", getToken()))
                .build();
    }


    public abstract void onResult(Object o);

    public abstract void onError(Exception e);
}
