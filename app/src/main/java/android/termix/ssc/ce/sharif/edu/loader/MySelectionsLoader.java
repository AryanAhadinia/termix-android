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

    private final ArrayList<Course> fromNetwork = new ArrayList<>();
    private int networkCacheSubscriber = 0;
    private final ArrayList<Course> fromLocal = new ArrayList<>();
    private int localCacheSubscribers = 0;

    public static MySelectionsLoader getInstance() {
        if (instance == null) {
            instance = new MySelectionsLoader();
        }
        return instance;
    }

    @Override
    public void run() {
        App.getExecutorService().execute(this::fromNetwork);
        App.getExecutorService().execute(this::fromLocal);
    }

    public void fromNetwork() {
        new GetMySelectionsTask() {
            @Override
            public void onResult(ArrayList<Course.CourseIdentifier> o) {
                HashMap<Integer, ArrayList<Course>> allCourses = AllCoursesLoader.getInstance().getFromNetwork();
                if (allCourses.size() == 0) {
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
                    synchronized (fromNetwork) {
                        fromNetwork.clear();
                        fromNetwork.addAll(result);
                        if (networkCacheSubscriber != 0) {
                            networkCacheSubscriber = 0;
                            fromNetwork.notifyAll();
                        }
                    }
                }
            }

            @Override
            public void onException(NetworkException e) {

            }

            @Override
            public void onError(Exception e) {
                synchronized (fromNetwork) {
                    if (networkCacheSubscriber != 0) {
                        networkCacheSubscriber = 0;
                        fromNetwork.notifyAll();
                    }
                }
            }
        }.run();
    }

    public void fromLocal() {
        try {
            ArrayList<Course> selectedCourses = DatabaseManager.getInstance().loadCourses();
            synchronized (fromLocal) {
                fromLocal.clear();
                fromLocal.addAll(selectedCourses);
                if (localCacheSubscribers != 0) {
                    fromLocal.notifyAll();
                }
            }
        } catch (JSONException e) {
            if (localCacheSubscribers != 0) {
                fromLocal.notifyAll();
            }
        }
    }

    public ArrayList<Course> getFromNetwork() {
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

    public ArrayList<Course> getFromLocal() {
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
