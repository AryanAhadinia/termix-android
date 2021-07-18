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
public abstract class AllCoursesLoader extends Loader<HashMap<Integer, ArrayList<Course>>> {
    private static HashMap<Integer, ArrayList<Course>> cachedResult;

    public AllCoursesLoader(Source[] sources) {
        super(sources);
    }

    @Override
    public void run() {
        if (sources.contains(Source.NETWORK)) {
            App.getExecutorService().execute(this::fromNetwork);
        }
        if (sources.contains(Source.LOCAL)) {
            App.getExecutorService().execute(this::fromLocal);
        }
    }

    public void fromNetwork() {
        new GetAllCoursesTask() {
            @Override
            public void onResult(HashMap<Integer, ArrayList<Course>> o) {
                postResult(Source.NETWORK, o, Source.NETWORK, Source.LOCAL);
                cachedResult = o;
                for (Integer depId : o.keySet()) {
                    DatabaseManager.getInstance().insertCourses(o.get(depId));
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
        if (cachedResult != null) {
            postResult(Source.LOCAL, cachedResult, Source.LOCAL);
        } else {
            try {
                HashMap<Integer, ArrayList<Course>> result = DatabaseManager.getInstance().loadCourses();
                postResult(Source.LOCAL, result, Source.LOCAL);
                cachedResult = result;
            } catch (JSONException e) {
                onFail(e);
            }
        }
    }
}
