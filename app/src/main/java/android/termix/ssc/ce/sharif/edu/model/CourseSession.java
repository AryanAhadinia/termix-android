package android.termix.ssc.ce.sharif.edu.model;

import java.util.ArrayList;
import java.util.HashMap;

public class CourseSession implements Comparable<CourseSession> {
    private final Course course;
    private final Session session;

    public CourseSession(Course course, Session session) {
        this.course = course;
        this.session = session;
    }

    public Course getCourse() {
        return course;
    }

    public Session getSession() {
        return session;
    }

    @Override
    public int compareTo(CourseSession o) {
        return this.session.compareTo(o.session);
    }

    public static ArrayList<CourseSession> getCourseSessions(ArrayList<Course> courses) {
        ArrayList<CourseSession> courseSessions = new ArrayList<>();
        for (Course course : courses) {
            for (Session session : course.getSessions()) {
                courseSessions.add(new CourseSession(course, session));
            }
        }
        return courseSessions;
    }

    public static HashMap<Integer, ArrayList<CourseSession>> getWeekdayCourseSessionsMap(ArrayList<Course> courses) {
        HashMap<Integer, ArrayList<CourseSession>> map = new HashMap<>();
        for (int i = 0; i < 7; i++) {
            map.put(i, new ArrayList<>());
        }
        for (Course course : courses) {
            for (Session session : course.getSessions()) {
                map.get(session.getDay()).add(new CourseSession(course, session));
            }
        }
        return map;
    }
}
