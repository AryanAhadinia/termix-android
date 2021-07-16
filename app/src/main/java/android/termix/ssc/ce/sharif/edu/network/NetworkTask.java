package android.termix.ssc.ce.sharif.edu.network;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
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
public abstract class NetworkTask implements Runnable {
    private static final String SERVER_URL = "http://219a625bb33b.ngrok.io";
    private static OkHttpClient okHttpClient;

    public static String getServerUrl() {
        return SERVER_URL;
    }

    public static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient.Builder().cookieJar(new CookieJar() {
                private final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();

                @NotNull
                @Override
                public List<Cookie> loadForRequest(@NotNull HttpUrl httpUrl) {
                    List<Cookie> cookies = cookieStore.get(httpUrl);
                    return cookies != null ? cookies : new ArrayList<>();
                }

                @Override
                public void saveFromResponse(@NotNull HttpUrl httpUrl, @NotNull List<Cookie> list) {
                    cookieStore.put(httpUrl, list);
                }
            }).build();
        }
        return okHttpClient;
    }

    protected abstract HttpUrl getURL();

    protected Request getRequest() {
        return new Request.Builder().url(getURL()).build();
    }

    protected Request getRequest(RequestBody requestBody) {
        return new Request.Builder().url(getURL()).post(requestBody).build();
    }

    public abstract void onResult(Object o);

    public abstract void onException(NetworkException e);

    public abstract void onError(Exception e);
}
