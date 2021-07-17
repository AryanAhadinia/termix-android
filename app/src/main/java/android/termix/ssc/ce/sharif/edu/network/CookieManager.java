package android.termix.ssc.ce.sharif.edu.network;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class CookieManager implements CookieJar {
    private static CookieManager instance;

    private List<Cookie> cookies;

    @NotNull
    @Override
    public List<Cookie> loadForRequest(@NotNull HttpUrl httpUrl) {
        return cookies != null ? cookies : new ArrayList<>();
    }

    @Override
    public void saveFromResponse(@NotNull HttpUrl httpUrl, @NotNull List<Cookie> list) {
        cookies = list;
    }

    public List<Cookie> getCookies() {
        return cookies;
    }

    public static CookieManager getInstance() {
        if (instance == null) {
            instance = new CookieManager();
        }
        return instance;
    }
}
