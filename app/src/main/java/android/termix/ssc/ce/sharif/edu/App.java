package android.termix.ssc.ce.sharif.edu;

import android.app.Application;
import android.termix.ssc.ce.sharif.edu.database.DatabaseManager;
import android.termix.ssc.ce.sharif.edu.loader.AllCoursesLoader;
import android.termix.ssc.ce.sharif.edu.network.CookieManager;
import android.termix.ssc.ce.sharif.edu.preferenceManager.PreferenceManager;

import java.sql.Time;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author AryanAhadinia
 * @since 1
 */
public class App extends Application {
    private static ThreadPoolExecutor executorService;

    @Override
    public void onCreate() {
        super.onCreate();
        // initializing
        CookieManager.init(getApplicationContext());
        DatabaseManager.init(getApplicationContext());
        AllCoursesLoader.init(getApplicationContext());
        PreferenceManager.init(getApplicationContext());
        // loading all courses immediately
        AllCoursesLoader.getInstance().run();
    }

    public static ThreadPoolExecutor getExecutorService() {
        if (executorService == null) {
            executorService = new ThreadPoolExecutor(4, 8, 10,
                    TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        }
        return executorService;
    }
}
