package android.termix.ssc.ce.sharif.edu.network;

import java.util.logging.Level;
import java.util.logging.Logger;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * An abstract class for networking.
 *
 * @author AryanAhadinia
 * @since 1
 */
public abstract class NetworkTask<T> implements Runnable {
    private static final String SERVER_URL = "https://ce419d0e99e3.ngrok.io";
    private static OkHttpClient okHttpClient;

    public static String getServerUrl() {
        return SERVER_URL;
    }

    public static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient.Builder().cookieJar(CookieManager.getInstance()).build();
            Logger.getLogger(OkHttpClient.class.getName()).setLevel(Level.FINE);
        }
        return okHttpClient;
    }

    protected abstract HttpUrl getURL();

    protected Request getRequest() {
        return new Request.Builder().url(getURL()).build();
    }

    protected Request getPostRequest(RequestBody requestBody) {
        return new Request.Builder().url(getURL()).post(requestBody).build();
    }

    protected Request getPutRequest(RequestBody requestBody) {
        return new Request.Builder().url(getURL()).put(requestBody).build();
    }

    protected Request getDeleteRequest(RequestBody requestBody) {
        return new Request.Builder().url(getURL()).delete(requestBody).build();
    }

    public abstract void onResult(T o);

    public abstract void onException(NetworkException e);

    public abstract void onError(Exception e);
}
