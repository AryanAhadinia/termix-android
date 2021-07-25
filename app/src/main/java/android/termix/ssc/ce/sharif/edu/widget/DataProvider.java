package android.termix.ssc.ce.sharif.edu.widget;

import android.content.Context;
import android.content.Intent;
import android.termix.ssc.ce.sharif.edu.R;
import android.termix.ssc.ce.sharif.edu.loader.MySelectionsLoader;
import android.termix.ssc.ce.sharif.edu.model.CourseSession;
import android.termix.ssc.ce.sharif.edu.model.Session;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

//com
public class DataProvider implements RemoteViewsService.RemoteViewsFactory {
    final static String TAG = "homo:DP_";
    List<String> nextSessionsStartAt = new ArrayList<>();
    List<String> nextSessionCourseNames = new ArrayList<>();
    //    ArrayList<ArrayList<CourseSession>> cachedSelections = new ArrayList<>();
    Context mContext;

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
        return nextSessionCourseNames.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews view = new RemoteViews(mContext.getPackageName(), R.layout.widget_single_schedule_item);
        view.setTextViewText(R.id.widget_course_remaining_to_start, nextSessionsStartAt.get(position));
        view.setTextViewText(R.id.widget_course_name, nextSessionCourseNames.get(position));
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
        Log.i(TAG, "initData: ");
        nextSessionsStartAt.clear();
        nextSessionCourseNames.clear();
        Calendar c = Calendar.getInstance();
        Date currentDate = new Date();
        c.setTime(currentDate);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        int hourOfDay = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        if (dayOfWeek == 6) { //friday
            minute = minute - 60;
            hourOfDay = hourOfDay - 23;
            dayOfWeek = 0;
        }

        ArrayList<ArrayList<CourseSession>> mySelections = initMySelections();
        mySelections.get(dayOfWeek).sort((courseSession, t1) -> courseSession.getSession().compareTo(t1.getSession()));
        for (CourseSession courseSession : mySelections.get(dayOfWeek)) {
            if (courseSession.getSession().getStartHour() > hourOfDay ||
                    (courseSession.getSession().getStartHour() == hourOfDay && courseSession.getSession().getStartMin() < minute)) {
                addSession(hourOfDay, minute, courseSession);
            }
        }
        for (int i = dayOfWeek + 1; i < 6; i++) {
            mySelections.get(i).sort((courseSession, t1) -> courseSession.getSession().compareTo(t1.getSession()));
            hourOfDay -= 24;
            for (CourseSession courseSession : mySelections.get(i)) {
                addSession(hourOfDay, minute, courseSession);
            }
        }
    }

    private ArrayList<ArrayList<CourseSession>> initMySelections() {
        ArrayList<ArrayList<CourseSession>> mySelections = new ArrayList<>();
        CourseSession.getWeekdayCourseSessionsMap(MySelectionsLoader.getInstance().getFromLocal());
        if (isMySelectionsEmpty(mySelections)) {
            Log.i(TAG, "initData: local empty");
            mySelections = CourseSession.getWeekdayCourseSessionsMap(MySelectionsLoader.getInstance().getFromNetwork());
            if (isMySelectionsEmpty(mySelections)) {
                Log.i(TAG, "initData: network empty");
                CourseSession.getWeekdayCourseSessionsMap(MySelectionsLoader.getInstance().getFromNetwork());
                //mySelections = new ArrayList<>(cachedSelections);
            }
            //else cachedSelections = new ArrayList<>(mySelections);
        }
        //else cachedSelections = new ArrayList<>(mySelections);
        return mySelections;
    }

    private boolean isMySelectionsEmpty(ArrayList<ArrayList<CourseSession>> mySelections) {
        for (ArrayList<CourseSession> mySelection : mySelections) {
            if (!mySelection.isEmpty()) return false;
        }
        return true;
    }

    private String calculateSessionStartsAt(int hour, int minute, Session session) {
        int hourDifference = session.getStartHour() - hour;
        int minuteDifference = session.getStartMin() - minute;
        if (minuteDifference < 0) {
            minuteDifference += 60;
            hourDifference -= 1;
        }
        return String.format("%01d:%02d", hourDifference, minuteDifference);
    }

    private void addSession(int hour, int minute, CourseSession courseSession) {
        nextSessionsStartAt.add(calculateSessionStartsAt(hour, minute, courseSession.getSession()));
        nextSessionCourseNames.add(courseSession.getCourse().getTitle());
        Log.i(TAG, "addSession: " + nextSessionsStartAt);
    }
}