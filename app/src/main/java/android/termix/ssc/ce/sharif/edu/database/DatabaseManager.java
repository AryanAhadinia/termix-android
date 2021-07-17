package android.termix.ssc.ce.sharif.edu.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.termix.ssc.ce.sharif.edu.model.Course;
import android.termix.ssc.ce.sharif.edu.model.SessionParser;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

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
        values.put(DatabaseHelper.COURSE_INFO_MESSAGE, course.getInfoMessage());
        values.put(DatabaseHelper.COURSE_ON_REGISTER_MESSAGE, course.getOnRegisterMessage());
        database.insert(DatabaseHelper.COURSE_TABLE, null, values);
    }

    public void insertCourses(ArrayList<Course> courses) {
        for (Course course : courses) {
            insertCourse(course);
        }
    }

    public HashMap<Integer, ArrayList<Course>> loadCourses() throws JSONException {
        HashMap<Integer, ArrayList<Course>> courses = new HashMap<>();
        Cursor cursor = this.database.query(DatabaseHelper.COURSE_TABLE, null, null,
                new String[]{}, null, null,
                DatabaseHelper.COURSE_ID + ", " + DatabaseHelper.COURSE_GROUP_ID);
        cursor.moveToFirst();
        HashMap<String, Integer> indexes = getCourseIndexes(cursor);
        while (!cursor.isAfterLast()) {
            Course course = new Course(
                    cursor.getInt(indexes.get(DatabaseHelper.COURSE_DEP_ID)),
                    cursor.getInt(indexes.get(DatabaseHelper.COURSE_ID)),
                    cursor.getInt(indexes.get(DatabaseHelper.COURSE_GROUP_ID)),
                    cursor.getInt(indexes.get(DatabaseHelper.COURSE_UNIT)),
                    cursor.getString(indexes.get(DatabaseHelper.COURSE_TITLE)),
                    cursor.getInt(indexes.get(DatabaseHelper.COURSE_CAPACITY)),
                    cursor.getString(indexes.get(DatabaseHelper.COURSE_INSTRUCTOR)),
                    cursor.getString(indexes.get(DatabaseHelper.COURSE_EXAM_TIME)),
                    new SessionParser(new JSONArray(cursor.getString(indexes.get(DatabaseHelper.COURSE_SESSIONS_JSON)))),
                    cursor.getString(indexes.get(DatabaseHelper.COURSE_INFO_MESSAGE)),
                    cursor.getString(indexes.get(DatabaseHelper.COURSE_ON_REGISTER_MESSAGE)));
            if (!courses.containsKey(course.getDepId())) {
                courses.put(course.getDepId(), new ArrayList<>());
            }
            courses.get(course.getDepId()).add(course);
        }
        cursor.close();
        return courses;
    }

    private HashMap<String, Integer> getCourseIndexes(Cursor cursor) {
        HashMap<String, Integer> indexes = new HashMap<>();
        indexes.put(DatabaseHelper.COURSE_DEP_ID,
                cursor.getColumnIndex(DatabaseHelper.COURSE_DEP_ID));
        indexes.put(DatabaseHelper.COURSE_ID,
                cursor.getColumnIndex(DatabaseHelper.COURSE_ID));
        indexes.put(DatabaseHelper.COURSE_GROUP_ID,
                cursor.getColumnIndex(DatabaseHelper.COURSE_GROUP_ID));
        indexes.put(DatabaseHelper.COURSE_UNIT,
                cursor.getColumnIndex(DatabaseHelper.COURSE_UNIT));
        indexes.put(DatabaseHelper.COURSE_TITLE,
                cursor.getColumnIndex(DatabaseHelper.COURSE_TITLE));
        indexes.put(DatabaseHelper.COURSE_CAPACITY,
                cursor.getColumnIndex(DatabaseHelper.COURSE_CAPACITY));
        indexes.put(DatabaseHelper.COURSE_INSTRUCTOR,
                cursor.getColumnIndex(DatabaseHelper.COURSE_INSTRUCTOR));
        indexes.put(DatabaseHelper.COURSE_EXAM_TIME,
                cursor.getColumnIndex(DatabaseHelper.COURSE_EXAM_TIME));
        indexes.put(DatabaseHelper.COURSE_SESSIONS_JSON,
                cursor.getColumnIndex(DatabaseHelper.COURSE_SESSIONS_JSON));
        indexes.put(DatabaseHelper.COURSE_INFO_MESSAGE,
                cursor.getColumnIndex(DatabaseHelper.COURSE_INFO_MESSAGE));
        indexes.put(DatabaseHelper.COURSE_ON_REGISTER_MESSAGE,
                cursor.getColumnIndex(DatabaseHelper.COURSE_ON_REGISTER_MESSAGE));
        return indexes;
    }
}
