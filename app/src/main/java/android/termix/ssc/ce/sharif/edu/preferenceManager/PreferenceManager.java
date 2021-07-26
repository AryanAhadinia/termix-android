package android.termix.ssc.ce.sharif.edu.preferenceManager;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Locale;

public class PreferenceManager {
    private static final String ALL_COURSES_NAME = "allCourses";
    private static final String ALL_COURSES_KEY = "allCourses";

    public static final String ALARM_OFFSET_NAME = "alarm";
    public static final String ALARM_OFFSET_KEY = "alarmOffset";

    public static final String DEPARTMENT_NAME = "myDep";
    public static final String DEPARTMENT_KEY = "myDep";

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
        return getPreferences(ALARM_OFFSET_NAME).getInt(ALARM_OFFSET_KEY, 5);
    }

    public int readAlarmOffset(int courseId, int groupId) {
        String key = String.format(Locale.US, "%s_%d_%d", ALARM_OFFSET_KEY, courseId, groupId);
        SharedPreferences preferences = getPreferences(ALARM_OFFSET_NAME);
        return preferences.getInt(key, preferences.getInt(ALARM_OFFSET_KEY, 5));
    }

    public void writeAlarmOffset(int offset) {
        SharedPreferences.Editor editor = getEditor(ALARM_OFFSET_NAME);
        editor.putInt(ALARM_OFFSET_KEY, offset);
        editor.apply();
    }

    public void writeAlarmOffset(int offset, int courseId, int groupId) {
        SharedPreferences.Editor editor = getEditor(ALARM_OFFSET_NAME);
        String key = String.format(Locale.US, "%s_%d_%d", ALARM_OFFSET_KEY, courseId, groupId);
        editor.putInt(key, offset);
        editor.apply();
    }

    public void removeAlarmOffset(int courseId, int groupId) {
        SharedPreferences.Editor editor = getEditor(ALARM_OFFSET_NAME);
        String key = String.format(Locale.US, "%s_%d_%d", ALARM_OFFSET_KEY, courseId, groupId);
        editor.remove(key);
        editor.apply();
    }

    public int readDepartment() {
        return getPreferences(DEPARTMENT_NAME).getInt(DEPARTMENT_KEY, 40);
    }

    public void writeDepartment(int depId) {
        SharedPreferences.Editor editor = getEditor(DEPARTMENT_NAME);
        editor.putInt(DEPARTMENT_KEY, depId);
        editor.apply();
    }
}
