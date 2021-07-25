package android.termix.ssc.ce.sharif.edu.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

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

    public static ArrayList<Course> getTestList() {
        try {
            return parseCourseArray(new JSONArray("[\n" +
                    "    {\n" +
                    "        \"depId\": 40,\n" +
                    "        \"courseId\": 40126,\n" +
                    "        \"groupId\": 1,\n" +
                    "        \"unit\": 3,\n" +
                    "        \"title\": \"ساختار و زبان کامپیوتر\",\n" +
                    "        \"capacity\": 50,\n" +
                    "        \"instructor\": \"لاله ارشدی\",\n" +
                    "        \"examTime\": \"1400/04/03 15:30\",\n" +
                    "        \"classTimeArray\": [\n" +
                    "            {\n" +
                    "                \"days\": [\n" +
                    "                    1,\n" +
                    "                    3\n" +
                    "                ],\n" +
                    "                \"startHour\": 16,\n" +
                    "                \"startMin\": 30,\n" +
                    "                \"endHour\": 18,\n" +
                    "                \"endMin\": 0\n" +
                    "            }\n" +
                    "        ],\n" +
                    "        \"info\": \"حداکثر ظرفیت برای مقطع کارشناسی دانشکده مهندسی کامپیوتر 100 می باشد.حداکثر ظرفیت برای مقطع کارشناسی 20 می باشد.\",\n" +
                    "        \"onRegister\": \"\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"depId\": 40,\n" +
                    "        \"courseId\": 40429,\n" +
                    "        \"groupId\": 1,\n" +
                    "        \"unit\": 3,\n" +
                    "        \"title\": \"برنامه\u200Cسازی موبایل\",\n" +
                    "        \"capacity\": 60,\n" +
                    "        \"instructor\": \"امید جعفری نژاد\",\n" +
                    "        \"examTime\": \"1400/04/12 09:00\",\n" +
                    "        \"classTimeArray\": [\n" +
                    "            {\n" +
                    "                \"days\": [\n" +
                    "                    0,\n" +
                    "                    2\n" +
                    "                ],\n" +
                    "                \"startHour\": 16,\n" +
                    "                \"startMin\": 30,\n" +
                    "                \"endHour\": 18,\n" +
                    "                \"endMin\": 0\n" +
                    "            }\n" +
                    "        ],\n" +
                    "        \"info\": \"حداکثر ظرفیت برای مقطع کارشناسی 20 می باشد.حداکثر ظرفیت برای مقطع کارشناسی دانشکده مهندسی کامپیوتر 100 می باشد.\",\n" +
                    "        \"onRegister\": \"\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"depId\": 24,\n" +
                    "        \"courseId\": 24012,\n" +
                    "        \"groupId\": 8,\n" +
                    "        \"unit\": 3,\n" +
                    "        \"title\": \"فیزیک ۲\",\n" +
                    "        \"capacity\": 40,\n" +
                    "        \"instructor\": \"علی اکبر ابوالحسنی\",\n" +
                    "        \"examTime\": \"1400/04/10 09:00\",\n" +
                    "        \"classTimeArray\": [\n" +
                    "            {\n" +
                    "                \"days\": [\n" +
                    "                    0,\n" +
                    "                    2\n" +
                    "                ],\n" +
                    "                \"startHour\": 9,\n" +
                    "                \"startMin\": 0,\n" +
                    "                \"endHour\": 10,\n" +
                    "                \"endMin\": 30\n" +
                    "            }\n" +
                    "        ],\n" +
                    "        \"info\": \"https://vc.sharif.edu/ch/ali.abolhasani ساعات رفع اشکال: چهار شنبه 13:0 تا 15:0\",\n" +
                    "        \"onRegister\": \"\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"depId\": 31,\n" +
                    "        \"courseId\": 31123,\n" +
                    "        \"groupId\": 16,\n" +
                    "        \"unit\": 3,\n" +
                    "        \"title\": \"زبان خارجی\",\n" +
                    "        \"capacity\": 30,\n" +
                    "        \"instructor\": \"فرهاد تابنده\",\n" +
                    "        \"examTime\": \"1300/04/08 13:30\",\n" +
                    "        \"classTimeArray\": [\n" +
                    "            {\n" +
                    "                \"days\": [\n" +
                    "                    0,\n" +
                    "                    2\n" +
                    "                ],\n" +
                    "                \"startHour\": 10,\n" +
                    "                \"startMin\": 30,\n" +
                    "                \"endHour\": 12,\n" +
                    "                \"endMin\": 0\n" +
                    "            }\n" +
                    "        ],\n" +
                    "        \"info\": \"\",\n" +
                    "        \"onRegister\": \"\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"depId\": 33,\n" +
                    "        \"courseId\": 33018,\n" +
                    "        \"groupId\": 1,\n" +
                    "        \"unit\": 1,\n" +
                    "        \"title\": \"کارگاه عمومی\",\n" +
                    "        \"capacity\": 100,\n" +
                    "        \"instructor\": \"رضا یوسفی، نصیری\",\n" +
                    "        \"examTime\": \"1400/03/25 15:30\",\n" +
                    "        \"classTimeArray\": [\n" +
                    "            {\n" +
                    "                \"days\": [\n" +
                    "                    3\n" +
                    "                ],\n" +
                    "                \"startHour\": 7,\n" +
                    "                \"startMin\": 30,\n" +
                    "                \"endHour\": 10,\n" +
                    "                \"endMin\": 30\n" +
                    "            }\n" +
                    "        ],\n" +
                    "        \"info\": \"\",\n" +
                    "        \"onRegister\": \"\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"depId\": 40,\n" +
                    "        \"courseId\": 40181,\n" +
                    "        \"groupId\": 1,\n" +
                    "        \"unit\": 3,\n" +
                    "        \"title\": \"آمار و احتمال مهندسی\",\n" +
                    "        \"capacity\": 45,\n" +
                    "        \"instructor\": \"امیر نجفی\",\n" +
                    "        \"examTime\": \"1400/04/09 15:30\",\n" +
                    "        \"classTimeArray\": [\n" +
                    "            {\n" +
                    "                \"days\": [\n" +
                    "                    1,\n" +
                    "                    3\n" +
                    "                ],\n" +
                    "                \"startHour\": 13,\n" +
                    "                \"startMin\": 30,\n" +
                    "                \"endHour\": 15,\n" +
                    "                \"endMin\": 0\n" +
                    "            }\n" +
                    "        ],\n" +
                    "        \"info\": \"حداکثر ظرفیت برای مقطع کارشناسی دانشکده مهندسی کامپیوتر 100 می باشد.حداکثر ظرفیت برای مقطع کارشناسی 20 می باشد.\",\n" +
                    "        \"onRegister\": \"\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"depId\": 40,\n" +
                    "        \"courseId\": 40364,\n" +
                    "        \"groupId\": 1,\n" +
                    "        \"unit\": 3,\n" +
                    "        \"title\": \"طراحی زبان\u200Cهای برنامه\u200Cسازی\",\n" +
                    "        \"capacity\": 70,\n" +
                    "        \"instructor\": \"محمد ایزدی\",\n" +
                    "        \"examTime\": \"1400/04/08 09:00\",\n" +
                    "        \"classTimeArray\": [\n" +
                    "            {\n" +
                    "                \"days\": [\n" +
                    "                    1,\n" +
                    "                    3\n" +
                    "                ],\n" +
                    "                \"startHour\": 10,\n" +
                    "                \"startMin\": 30,\n" +
                    "                \"endHour\": 12,\n" +
                    "                \"endMin\": 0\n" +
                    "            }\n" +
                    "        ],\n" +
                    "        \"info\": \"حداکثر ظرفیت برای مقطع کارشناسی دانشکده مهندسی کامپیوتر 100 می باشد.حداکثر ظرفیت برای مقطع کارشناسی 20 می باشد.\",\n" +
                    "        \"onRegister\": \"\"\n" +
                    "    }\n" +
                    "]"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}