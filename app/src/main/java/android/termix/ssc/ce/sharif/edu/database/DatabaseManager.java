package android.termix.ssc.ce.sharif.edu.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.termix.ssc.ce.sharif.edu.model.Course;
import android.termix.ssc.ce.sharif.edu.model.SessionParser;
import android.termix.ssc.ce.sharif.edu.myCourseManager.MyCourseManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * @author AryanAhadinia
 * @since 1
 */
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

    public void insertCourse(Course course) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COURSE_DEP_ID, course.getDepId());
        values.put(DatabaseHelper.COURSE_ID, course.getCourseId());
        values.put(DatabaseHelper.COURSE_GROUP_ID, course.getGroupId());
        values.put(DatabaseHelper.COURSE_UNIT, course.getUnit());
        values.put(DatabaseHelper.COURSE_TITLE, course.getTitle());
        values.put(DatabaseHelper.COURSE_CAPACITY, course.getCapacity());
        values.put(DatabaseHelper.COURSE_INSTRUCTOR, course.getInstructor());
        values.put(DatabaseHelper.COURSE_EXAM_TIME, course.getExamTime());
        values.put(DatabaseHelper.COURSE_SESSIONS_JSON, course.getSessionParser().getSessionJsonArray().toString());
        values.put(DatabaseHelper.COURSE_INFO_MESSAGE, course.getInfoMessage());
        values.put(DatabaseHelper.COURSE_ON_REGISTER_MESSAGE, course.getOnRegisterMessage());
        database.insert(DatabaseHelper.COURSE_TABLE, null, values);
    }

    public void insertCourses(ArrayList<Course> courses) {
        for (Course course : courses) {
            insertCourse(course);
        }
    }

    public boolean deleteCourse(int courseId, int groupId) {
        String where = String.format(Locale.US, "%s=? AND %s=?", DatabaseHelper.COURSE_ID,
                DatabaseHelper.COURSE_GROUP_ID);
        String[] whereArgs = new String[]{String.valueOf(courseId), String.valueOf(groupId)};
        return database.delete(DatabaseHelper.COURSE_TABLE, where, whereArgs) > 0;
    }

    public boolean deleteCourse(Course course) {
        return deleteCourse(course.getCourseId(), course.getGroupId());
    }

    public ArrayList<Course> loadCourses() throws JSONException {
        ArrayList<Course> courses = new ArrayList<>();
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
            courses.add(course);
            cursor.moveToNext();
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

    public void insertTask(MyCourseManager.Task task) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.TASK_ACTION, task.getAction());
        values.put(DatabaseHelper.TASK_COURSE_ID, task.getCourseId());
        values.put(DatabaseHelper.TASK_GROUP_ID, task.getGroupId());
        values.put(DatabaseHelper.TASK_TIME_STAMP, System.currentTimeMillis());
        database.insert(DatabaseHelper.TASK_TABLE, null, values);
    }

    public boolean removeTask(MyCourseManager.Task task) {
        String where = String.format(Locale.US, "%s=? AND %s=? AND %s=?",
                DatabaseHelper.TASK_ACTION,
                DatabaseHelper.TASK_COURSE_ID,
                DatabaseHelper.TASK_GROUP_ID);
        String[] whereArgs = new String[]{
                String.valueOf(task.getAction()),
                String.valueOf(task.getCourseId()),
                String.valueOf(task.getGroupId())};
        return database.delete(DatabaseHelper.COURSE_TABLE, where, whereArgs) > 0;
    }

    public ArrayList<MyCourseManager.Task> loadTasks() {
        ArrayList<MyCourseManager.Task> tasks = new ArrayList<>();
        Cursor cursor = this.database.query(DatabaseHelper.TASK_TABLE, null, null,
                new String[]{}, null, null,
                DatabaseHelper.TASK_TIME_STAMP);
        cursor.moveToFirst();
        HashMap<String, Integer> indexes = getTaskIndexes(cursor);
        while (!cursor.isAfterLast()) {
            MyCourseManager.Task task = new MyCourseManager.Task(
                    cursor.getInt(indexes.get(DatabaseHelper.TASK_COURSE_ID)),
                    cursor.getInt(indexes.get(DatabaseHelper.TASK_GROUP_ID)),
                    cursor.getInt(indexes.get(DatabaseHelper.TASK_ACTION)));
            tasks.add(task);
            cursor.moveToNext();
        }
        cursor.close();
        return tasks;
    }

    private HashMap<String, Integer> getTaskIndexes(Cursor cursor) {
        HashMap<String, Integer> indexes = new HashMap<>();
        indexes.put(DatabaseHelper.TASK_ACTION,
                cursor.getColumnIndex(DatabaseHelper.TASK_ACTION));
        indexes.put(DatabaseHelper.TASK_COURSE_ID,
                cursor.getColumnIndex(DatabaseHelper.TASK_COURSE_ID));
        indexes.put(DatabaseHelper.TASK_GROUP_ID,
                cursor.getColumnIndex(DatabaseHelper.TASK_GROUP_ID));
        indexes.put(DatabaseHelper.TASK_TIME_STAMP,
                cursor.getColumnIndex(DatabaseHelper.TASK_TIME_STAMP));
        return indexes;
    }
}
