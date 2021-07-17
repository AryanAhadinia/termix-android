package android.termix.ssc.ce.sharif.edu;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.termix.ssc.ce.sharif.edu.network.CookieManager;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        System.out.println(CookieManager.getInstance().getCookies());
    }
}
