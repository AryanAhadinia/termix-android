package android.termix.ssc.ce.sharif.edu.widget;

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

public class WidgetService extends RemoteViewsService {
    static String TAG = "homo:serv";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.i(TAG, "onGetViewFactory: ");
        return new DataProvider(this.getApplicationContext(), intent);
    }
}