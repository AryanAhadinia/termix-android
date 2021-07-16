package android.termix.ssc.ce.sharif.edu.model;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

public class Session implements Comparable<Session> {
    private static HashMap<Integer, String> weekdays;

    private final int day;
    private final int startHour;
    private final int startMin;
    private final int endHour;
    private final int endMin;

    public Session(int day, int startHour, int startMin, int endHour, int endMin) {
        this.day = day;
        this.startHour = startHour;
        this.startMin = startMin;
        this.endHour = endHour;
        this.endMin = endMin;
    }

    public static ArrayList<Session> parseClassTimeArray(JSONArray classTimeArray) {
        return null;
    }

    public static HashMap<Integer, String> getWeekdays() {
        if (weekdays == null) {
            weekdays = new HashMap<>();
            weekdays.put(0, "شنبه");
            weekdays.put(1, "یکشنبه");
            weekdays.put(2, "دوشنبه");
            weekdays.put(3, "سه شنبه");
            weekdays.put(4, "چهارشنبه");
            weekdays.put(5, "پنج شنبه");
            weekdays.put(6, "جمعه");
        }
        return weekdays;
    }

    @Override
    public int compareTo(Session o) {
        if (this.day < o.day) {
            return -1;
        } else if (this.day > o.day) {
            return 1;
        } else if (this.startHour < o.startHour) {
            return -1;
        } else if (this.startHour > o.startHour) {
            return 1;
        } else if (this.startMin < o.startMin) {
            return -1;
        } else if (this.startMin > o.startMin) {
            return 1;
        } else if (this.endHour < o.endHour) {
            return -1;
        } else if (this.endHour > o.endHour) {
            return 1;
        } else return Integer.compare(this.endMin, o.endMin);
    }
}
