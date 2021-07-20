package android.termix.ssc.ce.sharif.edu.loader;

import android.termix.ssc.ce.sharif.edu.App;
import android.termix.ssc.ce.sharif.edu.database.DatabaseManager;
import android.termix.ssc.ce.sharif.edu.model.Course;
import android.termix.ssc.ce.sharif.edu.network.NetworkException;
import android.termix.ssc.ce.sharif.edu.network.tasks.GetAllCoursesTask;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author AryanAhadinia
 * @since 1
 */
public abstract class AllCoursesLoader implements Runnable,
        LoaderCallback<HashMap<Integer, ArrayList<Course>>> {
    private static final HashMap<Integer, ArrayList<Course>> cachedResult = new HashMap<>();
    private static int cacheSubscribers = 0;

    private final Source source;

    public AllCoursesLoader(Source source) {
        this.source = source;
    }

    @Override
    public void run() {
        if (source.equals(Source.NETWORK)) {
            App.getExecutorService().execute(this::fromNetwork);
        } else { // source == Source.LOCAL
            App.getExecutorService().execute(this::fromLocal);
        }
    }

    public void fromNetwork() {
        new GetAllCoursesTask() {
            @Override
            public void onResult(HashMap<Integer, ArrayList<Course>> o) {
                // call callback
                onPriorLoad(o);
                // cache result and notify waiters
                synchronized (cachedResult) {
                    cachedResult.clear();
                    cachedResult.putAll(o);
                    if (cacheSubscribers != 0) {
                        cacheSubscribers = 0;
                        cachedResult.notifyAll();
                    }
                }
                // insert fetched data in database
                DatabaseManager.getInstance().deleteData();
                for (ArrayList<Course> courses : o.values()) {
                    DatabaseManager.getInstance().insertCourses(courses);
                }
            }

            @Override
            public void onException(NetworkException e) {
                onFail(e);
            }

            @Override
            public void onError(Exception e) {
                onFail(e);
            }
        }.run();
    }

    public void fromLocal() {
        if (!cachedResult.isEmpty()) {
            onPriorLoad(cachedResult);
        } else {
            try {
                HashMap<Integer, ArrayList<Course>> result = DatabaseManager.getInstance().loadCourses();
                if (result.keySet().size() != 0) {
                    onPriorLoad(result);
                    synchronized (cachedResult) {
                        cachedResult.clear();
                        cachedResult.putAll(result);
                        if (cacheSubscribers != 0) {
                            cacheSubscribers = 0;
                            cachedResult.notifyAll();
                        }
                    }
                } else {
                    onFail(new NullPointerException());
                }
            } catch (JSONException e) {
                onFail(e);
            }
        }
    }

    public static HashMap<Integer, ArrayList<Course>> getCachedResult() {
        if (!cachedResult.isEmpty()) {
            return cachedResult;
        }
        try {
            synchronized (cachedResult) {
                cacheSubscribers++;
                cachedResult.wait();
                return cachedResult;
            }
        } catch (InterruptedException e) {
            return null;
        }
    }
}
