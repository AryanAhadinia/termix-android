package android.termix.ssc.ce.sharif.edu.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.termix.ssc.ce.sharif.edu.loader.MySelectionsLoader;
import android.termix.ssc.ce.sharif.edu.model.CourseSession;
import android.termix.ssc.ce.sharif.edu.preferenceManager.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {
    static String TAG = " homo alarm ";
    private static long lastAlarm = 0;
    int alarmMargin = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive: ");
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        MySelectionsLoader.getInstance().run();
        ArrayList<ArrayList<CourseSession>> mySelections = getMySelections();
        Log.i(TAG, "onReceive: " + mySelections);
        for (CourseSession courseSession : mySelections.get(dayOfWeek)) {
            alarmMargin = PreferenceManager.getInstance(context).
                    readAlarmOffset(courseSession.getCourse().getCourseId(), courseSession.getCourse().getGroupId());
            Log.i(TAG, "onReceive: margin " + alarmMargin);
            if (isAlarmTime(calendar, courseSession, alarmMargin)) {
                ring(context, courseSession);
            }
        }
    }

    private void ring(Context context, CourseSession courseSession) {
        Log.i(TAG, "RANG: ");
        lastAlarm = System.currentTimeMillis();
        Intent alarmIntent = setUpAlarmIntent(context, courseSession);
        context.startActivity(alarmIntent);
    }

    private ArrayList<ArrayList<CourseSession>> getMySelections() {
        ArrayList<ArrayList<CourseSession>> mySelections;
        mySelections = CourseSession.getWeekdayCourseSessionsMap(MySelectionsLoader.getInstance().getFromLocal());
        if (!isMySelectionsEmpty(mySelections)) return mySelections;
        mySelections = CourseSession.getWeekdayCourseSessionsMap(MySelectionsLoader.getInstance().getFromNetwork());
        if (!isMySelectionsEmpty(mySelections)) return mySelections;
        MySelectionsLoader.getInstance().run();
        mySelections = CourseSession.getWeekdayCourseSessionsMap(MySelectionsLoader.getInstance().getFromNetwork());
        return mySelections;
    }

    private boolean isMySelectionsEmpty(ArrayList<ArrayList<CourseSession>> selections) {
        for (ArrayList<CourseSession> selection : selections) {
            if (!selection.isEmpty()) return false;
        }
        return true;
    }

    private boolean isAlarmTime(Calendar calendar, CourseSession courseSession, int alarmMargin) {
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int minutesDifference = (courseSession.getSession().getStartHour() - hourOfDay) * 60
                + courseSession.getSession().getStartMin() - minute;
        Log.i(TAG, "isAlarmTime: minutes difference:" + minutesDifference);
        Log.i(TAG, "isAlarmTime: last alarm: " + lastAlarm);
        return minutesDifference < alarmMargin && minutesDifference > 0 && System.currentTimeMillis() - lastAlarm > alarmMargin * 60 * 1000;
    }

    private Intent setUpAlarmIntent(Context context, CourseSession courseSession) {
        Intent alarmIntent = new Intent(context, AlarmActivity.class);
        alarmIntent.putExtra("course_name", courseSession.getCourse().getTitle());
        alarmIntent.putExtra("course_start_time",
                String.format("%02d:%02d", courseSession.getSession().getStartHour(),
                        courseSession.getSession().getStartMin()));
        alarmIntent.putExtra("course_instructor", courseSession.getCourse().getInstructor());
        alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return alarmIntent;
    }
}
