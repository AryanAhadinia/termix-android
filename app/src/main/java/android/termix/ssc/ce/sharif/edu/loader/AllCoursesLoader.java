package android.termix.ssc.ce.sharif.edu.loader;

import android.content.Context;
import android.content.SharedPreferences;
import android.termix.ssc.ce.sharif.edu.App;
import android.termix.ssc.ce.sharif.edu.model.Course;
import android.termix.ssc.ce.sharif.edu.network.NetworkException;
import android.termix.ssc.ce.sharif.edu.network.tasks.GetAllCoursesTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author AryanAhadinia
 * @since 1
 */
public class AllCoursesLoader implements Runnable {
    private static final String SHARED_PREFERENCE_LABEL = "allCourses";
    private static final String SHARED_PREFERENCE_NAME = "allCourses";

    private static AllCoursesLoader instance;

    private final HashMap<Integer, ArrayList<Course>> fromNetwork = new HashMap<>();
    private int networkCacheSubscriber = 0;
    private final HashMap<Integer, ArrayList<Course>> fromLocal = new HashMap<>();
    private int localCacheSubscribers = 0;

    private final Context context;

    public AllCoursesLoader(Context context) {
        this.context = context;
    }

    public static void init(Context context) {
        instance = new AllCoursesLoader(context);
    }

    public static AllCoursesLoader getInstance() {
        return instance;
    }

    @Override
    public void run() {
        App.getExecutorService().execute(this::fromNetwork);
        App.getExecutorService().execute(this::fromLocal);
    }

    public void fromNetwork() {
        new GetAllCoursesTask() {
            @Override
            public void onResult(String o) {
                try {
                    JSONObject resultJSON = new JSONObject(o);
                    HashMap<Integer, ArrayList<Course>> courses = new HashMap<>();
                    Iterator<String> keys = resultJSON.keys();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        courses.put(Integer.parseInt(key), Course.parseCourseArray(resultJSON
                                .getJSONArray(key)));
                    }
                    synchronized (fromNetwork) {
                        fromNetwork.clear();
                        fromNetwork.putAll(courses);
                        if (networkCacheSubscriber != 0) {
                            networkCacheSubscriber = 0;
                            fromNetwork.notifyAll();
                        }
                    }
                    synchronized (context) {
                        SharedPreferences preferences = context.getSharedPreferences(
                                SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(SHARED_PREFERENCE_LABEL, o);
                        editor.apply();
                    }
                } catch (JSONException e) {
                    onError(e);
                }
            }

            @Override
            public void onException(NetworkException e) {
                synchronized (fromNetwork) {
                    if (networkCacheSubscriber != 0) {
                        networkCacheSubscriber = 0;
                        fromNetwork.notifyAll();
                    }
                }
            }

            @Override
            public void onError(Exception e) {
                if (networkCacheSubscriber != 0) {
                    networkCacheSubscriber = 0;
                    fromNetwork.notifyAll();
                }
            }
        }.run();
    }

    public void fromLocal() {
        String result;
        synchronized (context) {
            SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME,
                    Context.MODE_PRIVATE);
            result = preferences.getString(SHARED_PREFERENCE_LABEL, "");
        }
        synchronized (fromLocal) {
            if (result.equals("")) {
                if (localCacheSubscribers != 0) {
                    localCacheSubscribers = 0;
                    fromLocal.notifyAll();
                }
            } else {
                try {
                    JSONObject resultJSON = new JSONObject(result);
                    HashMap<Integer, ArrayList<Course>> courses = new HashMap<>();
                    Iterator<String> keys = resultJSON.keys();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        courses.put(Integer.parseInt(key), Course.parseCourseArray(resultJSON
                                .getJSONArray(key)));
                    }
                    synchronized (fromLocal) {
                        fromLocal.clear();
                        fromLocal.putAll(courses);
                        if (localCacheSubscribers != 0) {
                            localCacheSubscribers = 0;
                            fromLocal.notifyAll();
                        }
                    }
                } catch (JSONException e) {
                    if (localCacheSubscribers != 0) {
                        localCacheSubscribers = 0;
                        fromLocal.notifyAll();
                    }
                }
            }
        }
    }

    public HashMap<Integer, ArrayList<Course>> getFromNetwork() {
        if (!fromNetwork.isEmpty()) {
            return fromNetwork;
        }
        try {
            synchronized (fromNetwork) {
                networkCacheSubscriber++;
                fromNetwork.wait();
                return fromNetwork;
            }
        } catch (InterruptedException e) {
            return null;
        }
    }

    public HashMap<Integer, ArrayList<Course>> getFromLocal() {
        if (!fromLocal.isEmpty()) {
            return fromLocal;
        }
        try {
            synchronized (fromLocal) {
                localCacheSubscribers++;
                fromLocal.wait();
                return fromLocal;
            }
        } catch (InterruptedException e) {
            return null;
        }
    }
}
