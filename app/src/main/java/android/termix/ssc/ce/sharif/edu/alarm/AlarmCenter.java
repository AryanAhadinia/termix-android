package android.termix.ssc.ce.sharif.edu.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class AlarmCenter {
    private static final int INTERVAL_MILLIS = 1000 * 60 * 2; // one minute

    private final Context context;

    public AlarmCenter(Context mContext) {
        this.context = mContext;
    }

    public void startAlarms() {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                INTERVAL_MILLIS, alarmIntent);
    }
}
