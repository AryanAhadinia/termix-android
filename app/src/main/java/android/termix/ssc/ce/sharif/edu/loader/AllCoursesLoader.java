package android.termix.ssc.ce.sharif.edu.loader;

import android.termix.ssc.ce.sharif.edu.model.Course;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author AryanAhadinia
 * @since 1
 */
public abstract class AllCoursesLoader extends Loader<HashMap<Integer, ArrayList<Course>>> {

    @Override
    public void run() {

    }

    public HashMap<Integer, ArrayList<Course>> fromNetwork() {
        return null;
    }

    public HashMap<Integer, ArrayList<Course>> fromLocal() {
        return null;
    }
}
