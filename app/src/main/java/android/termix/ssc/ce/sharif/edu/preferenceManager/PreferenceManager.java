package android.termix.ssc.ce.sharif.edu.preferenceManager;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    private static final String ALL_COURSES_NAME = "allCourses";
    private static final String ALL_COURSES_KEY = "allCourses";

    private static PreferenceManager instance;

    private final Context context;

    private PreferenceManager(Context context) {
        this.context = context;
    }

    public static void init(Context context) {
        instance = new PreferenceManager(context);
    }

    public static PreferenceManager getInstance() {
        return instance;
    }

    public static PreferenceManager getInstance(Context context) {
        if (instance == null) {
            instance = new PreferenceManager(context);
        }
        return instance;
    }

    public String readAllCourses() {
        SharedPreferences preferences = context.getSharedPreferences(ALL_COURSES_NAME,
                Context.MODE_PRIVATE);
        return preferences.getString(ALL_COURSES_KEY, "");
    }

    public boolean containsAllCourses() {
        SharedPreferences preferences = context.getSharedPreferences(ALL_COURSES_NAME,
                Context.MODE_PRIVATE);
        return preferences.contains(ALL_COURSES_KEY);
    }

    public void writeAllCourses(String toWrite) {
        SharedPreferences preferences = context.getSharedPreferences(ALL_COURSES_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ALL_COURSES_KEY, toWrite);
        editor.apply();
    }
}
