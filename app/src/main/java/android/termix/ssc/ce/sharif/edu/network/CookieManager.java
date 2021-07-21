package android.termix.ssc.ce.sharif.edu.network;

import android.content.Context;
import android.content.SharedPreferences;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * @author AryanAhadinia
 * @since 1
 */
public class CookieManager implements CookieJar {
    private static CookieManager instance;

    private static final String COOKIE_JAR_PREFERENCE_NAME = "CookieJar";

    private static final String TOKEN_VALUE = "TokenValue";
    private static final String TOKEN_EXPIRES_AT = "TokenExpiresAt";
    private static final String TOKEN_DOMAIN = "TokenDomain";
    private static final String TOKEN_PATH = "TokenPath";

    private final Context applicationContext;
    private List<Cookie> cookies;

    private CookieManager(Context applicationContext) {
        this.applicationContext = applicationContext;
        this.cookies = new ArrayList<>();
        SharedPreferences sharedPreferences = applicationContext
                .getSharedPreferences(COOKIE_JAR_PREFERENCE_NAME, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(TOKEN_VALUE)) {
            Cookie tokenCookie = new Cookie.Builder()
                    .name("token")
                    .value(sharedPreferences.getString(TOKEN_VALUE, ""))
                    .expiresAt(sharedPreferences.getLong(TOKEN_EXPIRES_AT, 0))
                    .hostOnlyDomain(sharedPreferences.getString(TOKEN_DOMAIN, ""))
                    .path(sharedPreferences.getString(TOKEN_PATH, "/"))
                    .build();
            cookies.add(tokenCookie);
        }
    }

    public static void init(Context context) {
        instance = new CookieManager(context);
    }

    public static CookieManager getInstance() {
        return instance;
    }

    @NotNull
    @Override
    public List<Cookie> loadForRequest(@NotNull HttpUrl httpUrl) {
        return cookies != null ? cookies : new ArrayList<>();
    }

    @Override
    public void saveFromResponse(@NotNull HttpUrl httpUrl, @NotNull List<Cookie> list) {
        cookies = list;
        for (Cookie cookie : cookies) {
            if (cookie.name().equals("token")) {
                SharedPreferences.Editor editor = applicationContext
                        .getSharedPreferences(COOKIE_JAR_PREFERENCE_NAME, Context.MODE_PRIVATE)
                        .edit();
                editor.putString(TOKEN_VALUE, cookie.value());
                editor.putLong(TOKEN_EXPIRES_AT, cookie.expiresAt());
                editor.putString(TOKEN_DOMAIN, cookie.domain());
                editor.putString(TOKEN_PATH, cookie.path());
                editor.apply();
            }
        }
    }

    public void setCookie(String domain, String tokenValue) {
        ArrayList<Cookie> newCookies = new ArrayList<>();
        newCookies.add(new Cookie.Builder()
                .name("token")
                .value(tokenValue)
                .expiresAt(System.currentTimeMillis() + 50000000L)
                .hostOnlyDomain(domain)
                .path("/")
                .build());
        cookies = newCookies;
    }
}
