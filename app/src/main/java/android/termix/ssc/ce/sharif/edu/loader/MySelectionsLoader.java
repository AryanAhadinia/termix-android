package android.termix.ssc.ce.sharif.edu.loader;

import android.termix.ssc.ce.sharif.edu.model.Course;

import java.util.ArrayList;
import java.util.HashMap;

public class MySelectionsLoader {
    private static final HashMap<Integer, ArrayList<Course>> fromNetwork = new HashMap<>();
    private static int networkCacheSubscriber = 0;
    private static final HashMap<Integer, ArrayList<Course>> fromLocal = new HashMap<>();
    private static int localCacheSubscribers = 0;
}
