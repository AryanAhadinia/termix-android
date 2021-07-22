package android.termix.ssc.ce.sharif.edu.loader;

import android.termix.ssc.ce.sharif.edu.App;
import android.termix.ssc.ce.sharif.edu.database.DatabaseManager;
import android.termix.ssc.ce.sharif.edu.model.Course;
import android.termix.ssc.ce.sharif.edu.network.NetworkException;
import android.termix.ssc.ce.sharif.edu.network.tasks.GetMySelectionsTask;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author AryanAhadinia
 * @since 1
 */
public class MySelectionsLoader implements Runnable {
    private static MySelectionsLoader instance;

    private ArrayList<Course> fromNetwork;
    private int networkCacheSubscriber = 0;
    private final Object fromNetworkLock = new Object();
    private boolean isNetworkLoading;
    private ArrayList<Course> fromLocal;
    private int localCacheSubscribers = 0;
    private final Object fromLocalLock = new Object();
    private boolean isLocalLoading;
    private final Object databaseLock = new Object();

    public static MySelectionsLoader getInstance() {
        if (instance == null) {
            instance = new MySelectionsLoader();
        }
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
        new GetMySelectionsTask() {
            @Override
            public void onResult(ArrayList<Course.CourseIdentifier> o) {
                HashMap<Integer, ArrayList<Course>> allCourses = AllCoursesLoader.getInstance().getFromNetwork();
                if (allCourses == null) {
                    onError(null);
                } else {
                    ArrayList<Course> result = new ArrayList<>();
                    for (Course.CourseIdentifier courseIdentifier : o) {
                        for (Course departmentCourse : allCourses.getOrDefault(courseIdentifier.getCourseId() / 1000, new ArrayList<>())) {
                            if (departmentCourse.getCourseId() == courseIdentifier.getCourseId() &&
                                    departmentCourse.getGroupId() == courseIdentifier.getGroupId()) {
                                result.add(departmentCourse);
                            }
                        }
                    }
                    synchronized (fromNetworkLock) {
                        fromNetwork = result;
                        isNetworkLoading = false;
                        if (networkCacheSubscriber != 0) {
                            networkCacheSubscriber = 0;
                            fromNetworkLock.notifyAll();
                        }
                    }
                    synchronized (databaseLock) {
                        DatabaseManager.getInstance().deleteData();
                        DatabaseManager.getInstance().insertCourses(result);
                    }
                }
            }

            @Override
            public void onException(NetworkException e) {
                synchronized (fromNetworkLock) {
                    fromNetwork = null;
                    isNetworkLoading = false;
                    if (networkCacheSubscriber != 0) {
                        networkCacheSubscriber = 0;
                        fromNetworkLock.notifyAll();
                    }
                }
            }

            @Override
            public void onError(Exception e) {
                synchronized (fromNetworkLock) {
                    fromNetwork = null;
                    isNetworkLoading = false;
                    if (networkCacheSubscriber != 0) {
                        networkCacheSubscriber = 0;
                        fromNetworkLock.notifyAll();
                    }
                }
            }
        }.run();
    }

    private void fromLocal() {
        try {
            ArrayList<Course> selectedCourses;
            synchronized (databaseLock) {
                selectedCourses = DatabaseManager.getInstance().loadCourses();
            }
            synchronized (fromLocalLock) {
                fromLocal = selectedCourses;
                isLocalLoading = false;
                if (localCacheSubscribers != 0) {
                    fromLocalLock.notifyAll();
                }
            }
        } catch (JSONException e) {
            synchronized (fromLocalLock) {
                fromLocal = null;
                isLocalLoading = false;
                if (localCacheSubscribers != 0) {
                    fromLocalLock.notifyAll();
                }
            }
        }
    }

    public ArrayList<Course> getFromNetwork() {
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

    public ArrayList<Course> getFromLocal() {
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
