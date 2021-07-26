package android.termix.ssc.ce.sharif.edu.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.termix.ssc.ce.sharif.edu.LoadingActivity;
import android.termix.ssc.ce.sharif.edu.MainActivity;
import android.util.Log;

import androidx.core.app.AlarmManagerCompat;

import java.util.Calendar;

public class AlarmCenter {
    String TAG = "homo alarm : alarmCenter";
    public static final String PREFERENCE_NAME = "alarm";
    public static final String PREFERENCE_LABEL = "alarmOffset"; // "alarmOffset_40429_1
    Context mContext;

    public AlarmCenter(Context mContext) {
        Log.i(TAG, "AlarmCenter: constructor");
        this.mContext = mContext;
    }

    public void startAlarms(int triggerValue){
        Log.i(TAG, "startAlarms: ");
        Alarm alarm = new Alarm();
        Log.i(TAG, "startAlarms: ");
        Calendar calendar = Calendar.getInstance();
        int alarmDistance = 1000 * 1 * 15;
//        int INTERVAL_MILLIS = 1000 * triggerValue;
        long alarmTime = System.currentTimeMillis() + alarmDistance;
        calendar.add(Calendar.MILLISECOND, alarmDistance);
        /*
        Intent alarmIntent = new Intent(mContext, CollectionWidget.class);
        alarmIntent.setAction(CollectionWidget.ACTION_AUTO_UPDATE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, ALARM_ID, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        // RTC does not wake the device up
        alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), INTERVAL_MILLIS, pendingIntent);
*/
        Intent alarmIntent = new Intent(mContext, AlarmActivity.class);
        Intent mainIntent = new Intent(mContext, LoadingActivity.class);
        alarmIntent.setAction(AlarmBroadcastReceiver.CHANNEL_ID);
        Log.i(TAG, "startAlarms: sent intent");
        //PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(mContext, 0, mainIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        AlarmManager.AlarmClockInfo info = new AlarmManager.AlarmClockInfo(alarmTime, pendingIntent2);
        alarmManager.setAlarmClock(info, getAlarmIntent(alarm));
    }

    PendingIntent getAlarmIntent(Alarm alarm)  {
        Intent intent = new Intent(mContext, AlarmActivity.class);
        return PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void disableAllAlarms(){

    }

    public void enableAllAlarms(){

    }

    public void createMockAlarm(){

    }

    public void setNewAlarms(int minuets){

    }
}
