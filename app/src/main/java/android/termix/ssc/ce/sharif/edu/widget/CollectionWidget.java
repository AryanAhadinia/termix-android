package android.termix.ssc.ce.sharif.edu.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.termix.ssc.ce.sharif.edu.R;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.NonNull;

//com
public class CollectionWidget extends AppWidgetProvider {
    public static final String ACTION_AUTO_UPDATE = "android.appwidget.action.APPWIDGET_UPDATE";
    final static String TAG = "homo:CW_";
    AppWidgetAlarm appWidgetAlarm;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Log.i(TAG, "updateAppWidget: ");
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_list);
        setRemoteAdapter(context, views);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static void setRemoteAdapter(Context context, @NonNull final RemoteViews views) {
        Log.i(TAG, "setRemoteAdapter: ");
        views.setRemoteAdapter(R.id.widget_list,
                new Intent(context, WidgetService.class));
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.i(TAG, "onUpdate: ");
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(
                    context.getPackageName(),
                    R.layout.widget_list);
            Intent intent = new Intent(context, WidgetService.class);
            views.setRemoteAdapter(R.id.widget_list, intent);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        Log.i(TAG, "onEnabled: ");
        appWidgetAlarm = new AppWidgetAlarm(context.getApplicationContext());
        appWidgetAlarm.startAlarm();
    }

    @Override
    public void onDisabled(Context context) {
        Toast.makeText(context, "onDisabled called", Toast.LENGTH_LONG).show();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisAppWidgetComponentName = new ComponentName(context.getPackageName(), getClass().getName());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidgetComponentName);
        if (appWidgetIds.length == 0) {
            // stop alarm
            appWidgetAlarm.stopAlarm();
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.i(TAG, "ACTION_AUTO_UPDATE = " + intent.getAction());
        if (intent.getAction().equals(ACTION_AUTO_UPDATE)) {
            Log.i(TAG, "onReceive: ACTION_AUTO_UPDATE");
            Intent i = new Intent(context, CollectionWidget.class);
            i.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            ComponentName thisAppWidget = new ComponentName(context, CollectionWidget.class);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetManager.getAppWidgetIds(thisAppWidget),
                    R.id.widget_list);
        }
        super.onReceive(context, intent);
    }
}