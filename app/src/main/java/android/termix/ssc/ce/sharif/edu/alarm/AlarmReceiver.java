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
    private static long lastAlarm = System.currentTimeMillis();
    int alarmMargin = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("KASHI: ", "BAH BAH");

        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);


        ArrayList<ArrayList<CourseSession>> mySelections =
                CourseSession.getWeekdayCourseSessionsMap(MySelectionsLoader.getInstance().getFromNetwork());
        for (CourseSession courseSession : mySelections.get(dayOfWeek)) {
            alarmMargin = PreferenceManager.getInstance(context).
                    readAlarmOffset(courseSession.getCourse().getCourseId(), courseSession.getCourse().getGroupId());
            if (isAlarmTime(calendar, courseSession, alarmMargin)) {
                lastAlarm = System.currentTimeMillis();
                Intent alarmIntent = setUpAlarmIntent(context, courseSession);
                context.startActivity(alarmIntent);
            }
        }
    }

    private boolean isAlarmTime(Calendar calendar, CourseSession courseSession, int alarmMargin) {
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int minutesDifference = (courseSession.getSession().getStartHour() - hourOfDay) * 60
                + courseSession.getSession().getStartMin() - minute;
        return minutesDifference < alarmMargin && minutesDifference > 0 && System.currentTimeMillis() - lastAlarm < alarmMargin * 60 * 1000;
    }

    private Intent setUpAlarmIntent(Context context, CourseSession courseSession) {
        Intent alarmIntent = new Intent(context, AlarmActivity.class);
        alarmIntent.putExtra("course_name", courseSession.getCourse().getTitle());
        alarmIntent.putExtra("course_start_time",
                Integer.toString(courseSession.getSession().getStartHour()) + courseSession.getSession().getStartMin());
        alarmIntent.putExtra("course_instructor", courseSession.getCourse().getInstructor());
        alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return alarmIntent;
    }
}
