package android.termix.ssc.ce.sharif.edu.myCourseManager;

import android.termix.ssc.ce.sharif.edu.database.DatabaseManager;
import android.termix.ssc.ce.sharif.edu.network.tasks.SelectTask;
import android.termix.ssc.ce.sharif.edu.network.tasks.UnselectTask;

import java.util.ArrayList;
import java.util.Random;

public class MyCourseManager {
    public static int SELECT = 0;
    public static int UNSELECT = 1;

    private static final int RETRIES_COUNT = 3;

    private static MyCourseManager instance;

    private final ArrayList<MyCourseManager.Task> postponedTasks;

    public MyCourseManager() {
        this.postponedTasks = new ArrayList<>();
    }

    public static MyCourseManager getInstance() {
        if (instance == null) {
            instance = new MyCourseManager();
        }
        return instance;
    }

    public void rebase() {
        postponedTasks.clear();
    }

    public boolean select(int courseId, int groupId) {
        try {
            return execute(new Task(courseId, groupId, SELECT));
        } catch (InterruptedException e) {
            return false;
        }
    }

    public boolean unselect(int courseId, int groupId) {
        try {
            return execute(new Task(courseId, groupId, UNSELECT));
        } catch (InterruptedException e) {
            return false;
        }
    }

    public boolean execute(Task task) throws InterruptedException {
        for (int i = 0; i < RETRIES_COUNT; i++) {
            if (flushPostponedTasks() && task.execute()) {
                return true;
            } else {
                if (i == RETRIES_COUNT - 1) {
                    Thread.sleep(new Random().nextInt(2000) + 1000);
                }
            }
        }
        DatabaseManager.getInstance().insertTask(task);
        postponedTasks.add(task);
        return false;
    }

    public boolean flushPostponedTasks() {
        while (!postponedTasks.isEmpty()) {
            Task task = postponedTasks.get(0);
            if (!task.execute()) {
                return false;
            } else {
                DatabaseManager.getInstance().removeTask(task);
                postponedTasks.remove(task);
            }
        }
        return true;
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

        public boolean execute() {
            if (action == SELECT) {
                return new SelectTask(getCourseId(), getGroupId()).execute();
            } else if (action == UNSELECT) {
                return new UnselectTask(getCourseId(), getGroupId()).execute();
            }
            return false;
        }
    }
}
