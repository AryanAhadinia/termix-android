package android.termix.ssc.ce.sharif.edu;

import android.app.Application;
import android.termix.ssc.ce.sharif.edu.network.CookieManager;

/**
 * @author AryanAhadinia
 * @since 1
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CookieManager.init(getApplicationContext());
    }
}
