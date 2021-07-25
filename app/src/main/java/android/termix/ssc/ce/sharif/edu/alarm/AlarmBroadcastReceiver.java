package android.termix.ssc.ce.sharif.edu.alarm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.termix.ssc.ce.sharif.edu.R;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.Date;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    public static String CHANNEL_ID = "ALARM_SUPER_CHANNEL";

    @Override
    public void onReceive(Context context, Intent intent) {
    }
}
