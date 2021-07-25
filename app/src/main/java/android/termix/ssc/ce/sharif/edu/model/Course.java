package android.termix.ssc.ce.sharif.edu.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * @author AryanAhadinia
 * @since 1
 */
public class Course implements Comparable<Course> {
    private static HashMap<Integer, String> departments;

    private final int depId;
    private final int courseId;
    private final int groupId;
    private final int unit;
    private final String title;
    private final int capacity;
    private final String instructor;
    private final String examTime;
    private final SessionParser sessionParser;
    private final String infoMessage;
    private final String onRegisterMessage;

    public Course(int depId, int courseId, int groupId, int unit, String title, int capacity,
                  String instructor, String examTime, SessionParser sessionParser,
                  String infoMessage, String onRegisterMessage) {
        this.depId = depId;
        this.courseId = courseId;
        this.groupId = groupId;
        this.unit = unit;
        this.title = title;
        this.capacity = capacity;
        this.instructor = instructor;
        this.examTime = examTime;
        this.sessionParser = sessionParser;
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
                new SessionParser(courseJSON.getJSONArray("classTimeArray")),
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

    public static HashMap<Integer, String> getDepartments() {
        if (departments == null) {
            departments = new HashMap<>();
            departments.put(20, "مهندسی عمران");
            departments.put(21, "مهندسی صنایع");
            departments.put(22, "علوم ریاضی");
            departments.put(23, "شیمی");
            departments.put(24, "فیزیک");
            departments.put(25, "مهندسی برق");
            departments.put(26, "مهندسی شیمی و نفت");
            departments.put(27, "مهندسی و علم مواد");
            departments.put(28, "مهندسی مکانیک");
            departments.put(30, "مرکز تربیت بدنی");
            departments.put(31, "مرکز زیان ها و زبان شناسی");
            departments.put(33, "مرکز کارگاه ها");
            departments.put(35, "مرکز گرافیک");
            departments.put(37, "مرکز معارف اسلامی");
            departments.put(40, "مهندسی کامپیوتر");
            departments.put(42, "گروه فلسفه علم");
            departments.put(44, "مدیریت و اقتصاد");
            departments.put(45, "مهندسی هوافضا");
            departments.put(46, "مهندسی انرژی");
        }
        return departments;
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

    public SessionParser getSessionParser() {
        return sessionParser;
    }

    public ArrayList<Session> getSessions() {
        try {
            return sessionParser.getSessions();
        } catch (JSONException e) {
            return new ArrayList<>();
        }
    }

    public String getSessionsString() {
        try {
            return sessionParser.getSessionsSting();
        } catch (JSONException e) {
            return "";
        }
    }

    public String getSessionJSON() {
        return sessionParser.getSessionJsonArray().toString();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return courseId == course.courseId &&
                groupId == course.groupId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId, groupId);
    }

    public static class CourseIdentifier {
        private final int courseId;
        private final int groupId;

        public CourseIdentifier(int courseId, int groupId) {
            this.courseId = courseId;
            this.groupId = groupId;
        }

        public int getCourseId() {
            return courseId;
        }

        public int getGroupId() {
            return groupId;
        }
    }
}