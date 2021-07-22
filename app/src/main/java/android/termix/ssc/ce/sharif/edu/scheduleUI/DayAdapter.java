package android.termix.ssc.ce.sharif.edu.scheduleUI;

import android.content.Context;
import android.termix.ssc.ce.sharif.edu.R;
import android.termix.ssc.ce.sharif.edu.model.Course;
import android.termix.ssc.ce.sharif.edu.model.CourseSession;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Locale;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.CourseTileViewHolder> {
    private final ArrayList<CourseSession> sessions;

    public DayAdapter() {
        this.sessions = new ArrayList<>();
    }

    @NonNull
    @NotNull
    @Override
    public CourseTileViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View courseView = inflater.inflate(R.layout.layout_course_frame, parent, false);
        return new CourseTileViewHolder(courseView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CourseTileViewHolder holder, int position) {
        CourseSession session = sessions.get(position);
        holder.setCourseSession(session);
    }

    @Override
    public int getItemCount() {
        return this.sessions.size();
    }

    public void insertCourseSessionAndNotify(CourseSession courseSession) {
        notifyItemInserted(insertCourseSession(courseSession));
    }

    public int insertCourseSession(CourseSession courseSession) {
        sessions.add(courseSession);
        sessions.sort(CourseSession::compareTo);
        return sessions.indexOf(courseSession);
    }

    public void insertCourseSessionsAndNotify(ArrayList<CourseSession> courseSessions) {
        insertCourseSessions(courseSessions);
        notifyDataSetChanged();
    }

    public void rebaseCourseSessionsAndNotify(ArrayList<CourseSession> courseSessions) {
        sessions.clear();
        insertCourseSessionsAndNotify(courseSessions);
    }

    public void insertCourseSessions(ArrayList<CourseSession> courseSessions) {
        sessions.addAll(courseSessions);
        sessions.sort(CourseSession::compareTo);
    }

    public static class CourseTileViewHolder extends RecyclerView.ViewHolder
            implements View.OnLongClickListener {
        private CourseSession courseSession;

        private final TextView unitTextView;
        private final TextView idTextView;
        private final TextView titleTextView;
        private final TextView instructorTextView;
        private final TextView timesTextView;

        public CourseTileViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            this.unitTextView = (TextView) itemView.findViewById(R.id.unit);
            this.idTextView = (TextView) itemView.findViewById(R.id.identifier);
            this.titleTextView = (TextView) itemView.findViewById(R.id.title);
            this.instructorTextView = (TextView) itemView.findViewById(R.id.instructor);
            this.timesTextView = (TextView) itemView.findViewById(R.id.times);
            itemView.setOnLongClickListener(this);
        }

        public void setCourseSession(CourseSession courseSession) {
            this.courseSession = courseSession;
            Course course = courseSession.getCourse();
            this.unitTextView.setText(String.valueOf(course.getUnit()));
            this.idTextView.setText(String.format(Locale.US, "%d, %d", course.getCourseId(),
                    course.getGroupId()));
            this.titleTextView.setText(course.getTitle());
            this.instructorTextView.setText(course.getInstructor());
            this.timesTextView.setText(course.getSessionsString());
        }

        @Override
        public boolean onLongClick(View v) {
            System.out.println("SALAMMMMMMMMMMMMMMMMMMM");
            return true;
        }
    }
}
