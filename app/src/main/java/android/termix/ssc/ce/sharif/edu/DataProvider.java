package android.termix.ssc.ce.sharif.edu;

import android.content.Context;
import android.content.Intent;
import android.termix.ssc.ce.sharif.edu.loader.MySelectionsLoader;
import android.termix.ssc.ce.sharif.edu.model.Course;
import android.termix.ssc.ce.sharif.edu.model.Session;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

//com
public class DataProvider implements RemoteViewsService.RemoteViewsFactory {
    List<String> myListView = new ArrayList<>();
    Context mContext = null;

    public DataProvider(Context context, Intent intent) {
        mContext = context;
    }

    @Override
    public void onCreate() {
        initData();
    }

    @Override
    public void onDataSetChanged() {
        initData();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return myListView.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews view = new RemoteViews(mContext.getPackageName(), R.layout.widget_single_schedule_item);
        view.setTextViewText(R.id.widget_course_remaining_to_start, myListView.get(position));
        //view.setTextViewText(R.id.widget_course_name, myListView.get(position));
        return view;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private void initData() {
        myListView.clear();
        ArrayList<Course> courses = MySelectionsLoader.getInstance().getFromLocal();
        for (Course course : courses) {
            System.out.println(course.getSessionsString());
            System.out.println(course.getSessions());
            System.out.println(course.getSessionJSON());
        }
    }
}