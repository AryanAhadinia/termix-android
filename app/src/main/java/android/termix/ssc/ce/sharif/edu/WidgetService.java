package android.termix.ssc.ce.sharif.edu;

import android.content.Intent;
import android.widget.RemoteViewsService;
//com
public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new DataProvider(this, intent);
    }
}