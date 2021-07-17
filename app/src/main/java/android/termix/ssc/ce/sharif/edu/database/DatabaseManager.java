package android.termix.ssc.ce.sharif.edu.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.termix.ssc.ce.sharif.edu.model.Course;

import com.google.gson.Gson;

import java.util.ArrayList;

public class DatabaseManager {
    private static DatabaseManager instance;

    private final DatabaseHelper databaseHelper;
    private final SQLiteDatabase database;

    private DatabaseManager(Context context) {
        this.databaseHelper = new DatabaseHelper(context);
        this.database = this.databaseHelper.getWritableDatabase();
    }

    public static void init(Context context) {
        DatabaseManager.instance = new DatabaseManager(context);
    }

    public static DatabaseManager getInstance() {
        return instance;
    }

    public void deleteData() {
        databaseHelper.rebase(database);
    }

    private void insertCourse(Course course) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COURSE_DEP_ID, course.getDepId());
        values.put(DatabaseHelper.COURSE_ID, course.getCourseId());
        values.put(DatabaseHelper.COURSE_GROUP_ID, course.getGroupId());
        values.put(DatabaseHelper.COURSE_UNIT, course.getUnit());
        values.put(DatabaseHelper.COURSE_TITLE, course.getTitle());
        values.put(DatabaseHelper.COURSE_CAPACITY, course.getCapacity());
        values.put(DatabaseHelper.COURSE_INSTRUCTOR, course.getInstructor());
        values.put(DatabaseHelper.COURSE_EXAM_TIME, course.getExamTime());
        values.put(DatabaseHelper.COURSE_SESSIONS_JSON, new Gson().toJson(course.getSessions()));
        values.put(DatabaseHelper.COURSE_SESSIONS_STRING, course.getSessionsString());
        values.put(DatabaseHelper.COURSE_INFO_MESSAGE, course.getInfoMessage());
        values.put(DatabaseHelper.COURSE_ON_REGISTER_MESSAGE, course.getOnRegisterMessage());
        database.insert(DatabaseHelper.COURSE_TABLE, null, values);
    }

    public void insertCourses(ArrayList<Course> courses) {
        for (Course course : courses) {
            insertCourse(course);
        }
    }

    public ArrayList<Course> loadCourses() {
        return null;
    }
}
