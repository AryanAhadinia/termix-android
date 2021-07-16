package android.termix.ssc.ce.sharif.edu.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Course implements Comparable<Course> {
    private final int depId;
    private final int courseId;
    private final int groupId;
    private final int unit;
    private final String title;
    private final int capacity;
    private final String instructor;
    private final String examTime;
    private final ArrayList<Session> sessions;
    private final String infoMessage;
    private final String onRegisterMessage;

    private Course(int depId, int courseId, int groupId, int unit, String title, int capacity,
                   String instructor, String examTime, ArrayList<Session> classTime,
                   String infoMessage, String onRegisterMessage) {
        this.depId = depId;
        this.courseId = courseId;
        this.groupId = groupId;
        this.unit = unit;
        this.title = title;
        this.capacity = capacity;
        this.instructor = instructor;
        this.examTime = examTime;
        this.sessions = classTime;
        this.infoMessage = infoMessage;
        this.onRegisterMessage = onRegisterMessage;
    }

    public static Course parseCourse(JSONObject courseJSON) throws JSONException {
        return new Course(courseJSON.getInt("depId"),
                courseJSON.getInt("courseId"),
                courseJSON.getInt("groupId"),
                courseJSON.getInt("unit"),
                courseJSON.getString("title"),
                courseJSON.getInt("capacity"),
                courseJSON.getString("instructor"),
                courseJSON.getString("examTime"),
                Session.parseClassTimeArray(courseJSON.getJSONArray("classTimeArray")),
                courseJSON.getString("info"),
                courseJSON.getString("onRegister"));
    }

    public static ArrayList<Course> parseCourseArray(JSONArray courseJSONArray)
            throws JSONException {
        ArrayList<Course> courses = new ArrayList<>();
        for (int i = 0; i < courseJSONArray.length(); i++) {
            courses.add(parseCourse(courseJSONArray.getJSONObject(i)));
        }
        courses.sort(Course::compareTo);
        return courses;
    }

    public int getDepId() {
        return depId;
    }

    public int getCourseId() {
        return courseId;
    }

    public int getGroupId() {
        return groupId;
    }

    public int getUnit() {
        return unit;
    }

    public String getTitle() {
        return title;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getInstructor() {
        return instructor;
    }

    public String getExamTime() {
        return examTime;
    }

    public ArrayList<Session> getClassTime() {
        return sessions;
    }

    public String getInfoMessage() {
        return infoMessage;
    }

    public String getOnRegisterMessage() {
        return onRegisterMessage;
    }

    @Override
    public int compareTo(Course o) {
        if (this.courseId < o.courseId) {
            return -1;
        } else if (this.courseId > o.courseId) {
            return 1;
        } else if (this.groupId < o.groupId) {
            return -1;
        } else if (this.groupId > o.groupId) {
            return 1;
        }
        return 0;
    }
}