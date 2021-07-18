package android.termix.ssc.ce.sharif.edu.model;

import android.termix.ssc.ce.sharif.edu.model.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * Parse JSONArray of class times to a proper syntax tree
 *
 * @author AryanAhadinia
 * @since 1
 */
public class SessionParser {
    private static HashMap<Integer, String> weekdays;

    private final JSONArray sessionJsonArray;

    private ArrayList<Session> sessions;
    private String sessionsSting;

    public SessionParser(JSONArray sessionJsonArray) {
        this.sessionJsonArray = sessionJsonArray;
    }

    private synchronized void parse() throws JSONException {
        if (sessions != null) {
            return;
        }
        sessions = new ArrayList<>();
        ArrayList<String> sessionsArray = new ArrayList<>();
        for (int i = 0; i < sessionJsonArray.length(); i++) {
            StringBuilder stringBuilder = new StringBuilder();
            JSONObject currentIteration = sessionJsonArray.getJSONObject(i);
            int startHour = currentIteration.getInt("startHour");
            int startMin = currentIteration.getInt("startMin");
            int endHour = currentIteration.getInt("endHour");
            int endMin = currentIteration.getInt("endMin");
            JSONArray daysJsonArray = currentIteration.getJSONArray("days");
            ArrayList<String> daysArray = new ArrayList<>();
            for (int j = 0; j < daysJsonArray.length(); j++) {
                int day = daysJsonArray.getInt(j);
                sessions.add(new Session(day, startHour, startMin, endHour, endMin));
                daysArray.add(getWeekdays().get(day));
            }
            stringBuilder.append(String.join(" و ", daysArray))
                    .append(" ")
                    .append(String.format(Locale.US, "%d:%02d", startHour, startMin))
                    .append(" تا ")
                    .append(String.format(Locale.US, "%d:%02d", endHour, endMin));
            sessionsArray.add(stringBuilder.toString());
        }
        sessionsSting = String.join(" و ", sessionsArray);
    }

    public JSONArray getSessionJsonArray() {
        return sessionJsonArray;
    }

    public ArrayList<Session> getSessions() throws JSONException {
        if (sessions == null) {
            parse();
        }
        return sessions;
    }

    public String getSessionsSting() throws JSONException {
        if (sessionsSting == null) {
            parse();
        }
        return sessionsSting;
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
}
