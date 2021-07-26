package android.termix.ssc.ce.sharif.edu.alarm;

import android.termix.ssc.ce.sharif.edu.model.CourseSession;
import android.util.Log;

import java.util.Calendar;

public class Alarm {
    Calendar ringTime;
    String courseTitle;
    String courseInstructor;
    String courseTime;
    CourseSession courseSession;

    String TAG = "homo: alarm class";

    public Alarm(CourseSession courseSession) {
        this.courseSession = courseSession;
        courseTitle = courseSession.getCourse().getTitle();
        courseInstructor = courseSession.getCourse().getInstructor();
        courseTime = courseSession.getSession().getStartHour() + ":" +courseSession.getSession().getStartHour();
    }

    public Alarm(){
        Log.i(TAG, "Alarm: ");
        ringTime = Calendar.getInstance();
        courseTime = "12:34";
        courseInstructor = "چکاه";
        courseTitle = "AP";
    }
}
