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

    private HashMap<Integer, ArrayList<Course>> fromNetwork;
    private int networkCacheSubscriber = 0;
    private final Object fromNetworkLock = new Object();
    private boolean isNetworkLoading;
    private HashMap<Integer, ArrayList<Course>> fromLocal;
    private int localCacheSubscribers = 0;
    private final Object fromLocalLock = new Object();
    private boolean isLocalLoading;
    private final Object LocalCacheLock = new Object();
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
        this.isNetworkLoading = true;
        this.isLocalLoading = true;
        App.getExecutorService().execute(this::fromNetwork);
        App.getExecutorService().execute(this::fromLocal);
    }

    private void fromNetwork() {
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
                    synchronized (fromNetworkLock) {
                        fromNetwork = courses;
                        isNetworkLoading = false;
                        if (networkCacheSubscriber != 0) {
                            networkCacheSubscriber = 0;
                            fromNetworkLock.notifyAll();
                        }
                    }
                    synchronized (LocalCacheLock) {
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
                fromNetwork = null;
                isNetworkLoading = false;
                synchronized (fromNetworkLock) {
                    if (networkCacheSubscriber != 0) {
                        networkCacheSubscriber = 0;
                        fromNetworkLock.notifyAll();
                    }
                }
            }

            @Override
            public void onError(Exception e) {
                fromNetwork = null;
                isNetworkLoading = false;
                if (networkCacheSubscriber != 0) {
                    networkCacheSubscriber = 0;
                    fromNetworkLock.notifyAll();
                }
            }
        }.run();
    }

    private void fromLocal() {
        String result;
        synchronized (LocalCacheLock) {
            SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME,
                    Context.MODE_PRIVATE);
            result = preferences.getString(SHARED_PREFERENCE_LABEL, "");
        }
        synchronized (fromLocalLock) {
            if (result.equals("")) {
                fromLocal = null;
                isLocalLoading = false;
                if (localCacheSubscribers != 0) {
                    localCacheSubscribers = 0;
                    fromLocalLock.notifyAll();
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
                    synchronized (fromLocalLock) {
                        fromLocal = courses;
                        isLocalLoading = false;
                        if (localCacheSubscribers != 0) {
                            localCacheSubscribers = 0;
                            fromLocalLock.notifyAll();
                        }
                    }
                } catch (JSONException e) {
                    fromLocal = null;
                    isLocalLoading = false;
                    if (localCacheSubscribers != 0) {
                        localCacheSubscribers = 0;
                        fromLocalLock.notifyAll();
                    }
                }
            }
        }
    }

    public HashMap<Integer, ArrayList<Course>> getFromNetwork() {
        synchronized (fromNetworkLock) {
            if (!isNetworkLoading) {
                return fromNetwork;
            }
            try {
                networkCacheSubscriber++;
                fromNetworkLock.wait();
                return fromNetwork;
            } catch (InterruptedException e) {
                return null;
            }
        }
    }

    public HashMap<Integer, ArrayList<Course>> getFromLocal() {
        synchronized (fromLocalLock) {
            if (!isLocalLoading) {
                return fromLocal;
            }
            try {
                localCacheSubscribers++;
                fromLocalLock.wait();
                return fromLocal;
            } catch (InterruptedException e) {
                return null;
            }
        }
    }
}
