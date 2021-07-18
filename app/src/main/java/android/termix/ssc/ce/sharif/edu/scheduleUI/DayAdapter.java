package android.termix.ssc.ce.sharif.edu.scheduleUI;

import android.content.Context;
import android.termix.ssc.ce.sharif.edu.R;
import android.termix.ssc.ce.sharif.edu.model.Course;
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
    private final ArrayList<Course> courses;

    public DayAdapter() {
        this.courses = new ArrayList<>();
    }

    public DayAdapter(ArrayList<Course> courses) {
        this.courses = courses;
    }

    @NonNull
    @NotNull
    @Override
    public CourseTileViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View courseView = inflater.inflate(R.layout.layout_course_tile, parent, false);
        return new CourseTileViewHolder(courseView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CourseTileViewHolder holder, int position) {
        Course course = courses.get(position);


    }

    @Override
    public int getItemCount() {
        return this.courses.size();
    }

    public static class CourseTileViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        private Course course;
        private TextView unitTextView;
        private TextView idTextView;
        private TextView titleTextView;
        private TextView instructorTextView;
        private TextView timesTextView;

        public CourseTileViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            this.unitTextView = (TextView) itemView.findViewById(R.id.unit);
            this.idTextView = (TextView) itemView.findViewById(R.id.identifier);
            this.titleTextView = (TextView) itemView.findViewById(R.id.title);
            this.instructorTextView = (TextView) itemView.findViewById(R.id.instructor);
            this.timesTextView = (TextView) itemView.findViewById(R.id.times);
            itemView.setOnLongClickListener(this);
        }

        public void setCourse(Course course) {
            this.course = course;
            this.unitTextView.setText(String.valueOf(course.getUnit()));
            this.idTextView.setText(String.format(Locale.US,"%d, %d", course.getCourseId(), course.getGroupId()));
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
