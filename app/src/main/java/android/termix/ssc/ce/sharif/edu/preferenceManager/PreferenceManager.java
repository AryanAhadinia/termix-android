package android.termix.ssc.ce.sharif.edu.preferenceManager;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Locale;

public class PreferenceManager {
    private static final String ALL_COURSES_NAME = "allCourses";
    private static final String ALL_COURSES_KEY = "allCourses";

    public static final String PREFERENCE_NAME = "alarm";
    public static final String PREFERENCE_LABEL = "alarmOffset"; // "alarmOffset_40429_1


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

    public SharedPreferences getPreferences(String name) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public SharedPreferences.Editor getEditor(String name) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE).edit();
    }

    public String readAllCourses() {
        return getPreferences(ALL_COURSES_NAME).getString(ALL_COURSES_KEY, "");
    }

    public boolean containsAllCourses() {
        return getPreferences(ALL_COURSES_NAME).contains(ALL_COURSES_KEY);
    }

    public void writeAllCourses(String toWrite) {
        SharedPreferences.Editor editor = getEditor(ALL_COURSES_NAME);
        editor.putString(ALL_COURSES_KEY, toWrite);
        editor.apply();
    }

    public int readAlarmOffset() {
        return getPreferences(PREFERENCE_NAME).getInt(PREFERENCE_LABEL, 5);
    }

    public int readAlarmOffset(int courseId, int groupId) {
        String key = String.format(Locale.US, "%s_%d_%d", PREFERENCE_LABEL, courseId, groupId);
        SharedPreferences preferences = getPreferences(PREFERENCE_NAME);
        return preferences.getInt(key, preferences.getInt(PREFERENCE_LABEL, 5));
    }

    public void writeAlarmOffset(int offset) {
        SharedPreferences.Editor editor = getEditor(PREFERENCE_NAME);
        editor.putInt(PREFERENCE_LABEL, offset);
        editor.apply();
    }

    public void writeAlarmOffset(int offset, int courseId, int groupId) {
        SharedPreferences.Editor editor = getEditor(PREFERENCE_NAME);
        String key = String.format(Locale.US, "%s_%d_%d", PREFERENCE_LABEL, courseId, groupId);
        editor.putInt(key, offset);
        editor.apply();
    }
}
