package android.termix.ssc.ce.sharif.edu.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.os.Parcelable;
import android.util.Log;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    public static String CHANNEL_ID = "ALARM_SUPER_CHANNEL";
    final String TAG = "homo alarm : ABR";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive: " + intent.getAction());
        if (CHANNEL_ID.equals(intent.getAction())){
            Log.i(TAG, "onReceive: received");
        }
    }
}
