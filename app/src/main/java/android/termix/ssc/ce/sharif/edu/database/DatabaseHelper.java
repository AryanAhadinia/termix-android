package android.termix.ssc.ce.sharif.edu.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/**
 * @author AryanAhadinia
 * @since 1
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final int version = 1;

    public static final String DATABASE_NAME = "edu.sharif.ce.ssc.termix";

    public static final String COURSE_TABLE = "course";
    public static final String COURSE_DEP_ID = "depId";
    public static final String COURSE_ID = "courseId";
    public static final String COURSE_GROUP_ID = "groupId";
    public static final String COURSE_UNIT = "unit";
    public static final String COURSE_TITLE = "title";
    public static final String COURSE_CAPACITY = "capacity";
    public static final String COURSE_INSTRUCTOR = "instructor";
    public static final String COURSE_EXAM_TIME = "examTime";
    public static final String COURSE_SESSIONS_JSON = "classTimeJSON";
    public static final String COURSE_INFO_MESSAGE = "infoMessage";
    public static final String COURSE_ON_REGISTER_MESSAGE = "onRegisterMessage";

    public static final String TASK_TABLE = "task";
    public static final String TASK_COURSE_ID = "courseId";
    public static final String TASK_GROUP_ID = "groupId";
    public static final String TASK_ACTION = "actionType";
    public static final String TASK_TIME_STAMP = "time";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createCourseTable(db);
        createTaskTable(db);
    }

    public void createCourseTable(SQLiteDatabase db) {
        db.execSQL("create table if not exists " + COURSE_TABLE + " (" +
                COURSE_DEP_ID + " INTEGER," +
                COURSE_ID + " INTEGER," +
                COURSE_GROUP_ID + " INTEGER," +
                COURSE_UNIT + " INTEGER," +
                COURSE_TITLE + " TEXT," +
                COURSE_CAPACITY + " INTEGER," +
                COURSE_INSTRUCTOR + " TEXT," +
                COURSE_EXAM_TIME + " TEXT," +
                COURSE_SESSIONS_JSON + " TEXT," +
                COURSE_INFO_MESSAGE + " TEXT," +
                COURSE_ON_REGISTER_MESSAGE + " TEXT" +
                ");");
    }

    public void createTaskTable(SQLiteDatabase db) {
        db.execSQL("create table if not exists " + TASK_TABLE + " (" +
                TASK_ACTION + " INTEGER," +
                TASK_COURSE_ID + " INTEGER," +
                TASK_GROUP_ID + " INTEGER," +
                TASK_TIME_STAMP + " LONG" +
                ");");
    }

    public void rebase(SQLiteDatabase db) {
        dropCourseTable(db);
        dropTaskTable(db);
        onCreate(db);
    }

    public void dropCourseTable(SQLiteDatabase db) {
        db.execSQL("drop table if exists " + COURSE_TABLE);
    }

    public void dropTaskTable(SQLiteDatabase db) {
        db.execSQL("drop table if exists " + TASK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        rebase(db);
    }
}
