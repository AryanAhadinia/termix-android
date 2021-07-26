package android.termix.ssc.ce.sharif.edu.myCourseManager;

import java.util.ArrayList;

public class MyCourseManager {
    public static int SELECT = 0;
    public static int UNSELECT = 1;

    private static MyCourseManager instance;

    private ArrayList<MyCourseManager.Task> postponedTasks;

    public MyCourseManager() {
        this.postponedTasks = new ArrayList<>();
    }

    public static MyCourseManager getInstance() {
        if (instance == null) {
            instance = new MyCourseManager();
        }
        return instance;
    }

    public static void rebase() {

    }

    public static void select() {

    }

    public static void unselect() {

    }

    public static class Task {
        private final int courseId;
        private final int groupId;
        private final int action;

        public Task(int courseId, int groupId, int action) {
            this.courseId = courseId;
            this.groupId = groupId;
            this.action = action;
        }

        public int getCourseId() {
            return courseId;
        }

        public int getGroupId() {
            return groupId;
        }

        public int getAction() {
            return action;
        }
    }
}
