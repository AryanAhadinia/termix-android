package android.termix.ssc.ce.sharif.edu.model;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @author AryanAhadinia
 * @since 1
 */
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

    public boolean hasConflict(CourseSession other) {
        return this.getSession().hasConflict(other.getSession());
    }

    @Override
    public int compareTo(CourseSession o) {
        return this.session.compareTo(o.session);
    }

    public static ArrayList<CourseSession> getCourseSession(Course course) {
        ArrayList<CourseSession> courseSessions = new ArrayList<>();
        for (Session session : course.getSessions()) {
            courseSessions.add(new CourseSession(course, session));
        }
        return courseSessions;
    }


    public static ArrayList<ArrayList<CourseSession>> getWeekdayCourseSessionsMap(ArrayList<Course> courses) {
        ArrayList<ArrayList<CourseSession>> list = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            list.add(new ArrayList<>());
        }
        if (courses == null) return list;
        for (Course course : courses) {
            for (Session session : course.getSessions()) {
                list.get(session.getDay()).add(new CourseSession(course, session));
            }
        }
        return list;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CourseSession)) return false;
        CourseSession session1 = (CourseSession) o;
        return course.equals(session1.course) &&
                session.equals(session1.session);
    }

    @Override
    public int hashCode() {
        return Objects.hash(course, session);
    }
}
