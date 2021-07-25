package android.termix.ssc.ce.sharif.edu.model;

/**
 * @author AryanAhadinia
 * @since 1
 */
public class Session implements Comparable<Session> {
    public static final Session NULL_SESSION = new Session(-1, 0, 0, 0, 0);
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

    public int getDay() {
        return day;
    }

    public int getStartHour() {
        return startHour;
    }

    public int getStartMin() {
        return startMin;
    }

    public int getEndHour() {
        return endHour;
    }

    public int getEndMin() {
        return endMin;
    }

    public float getLength() {
        return (((endHour * 60) + endMin) - ((startHour * 60) + startMin)) /(float) 60;
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
