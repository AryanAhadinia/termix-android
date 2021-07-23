package android.termix.ssc.ce.sharif.edu;

import android.content.Context;
import android.graphics.Color;
import android.termix.ssc.ce.sharif.edu.loader.AllCoursesLoader;
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

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {
    private ArrayList<Course> showingCourses;

    public ArrayList<Course> getShowingCourses() {
        if (showingCourses == null) {
            if (AllCoursesLoader.getInstance().getFromNetwork() != null) {
                showingCourses = AllCoursesLoader.getInstance().getFromNetwork().get(40);
            } else {
                showingCourses = AllCoursesLoader.getInstance().getFromLocal().get(40);
            }
        }
        return showingCourses;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View courseView = inflater.inflate(R.layout.layout_search_result, parent, false);
        return new SearchResultAdapter.ViewHolder(courseView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Course course = getShowingCourses().get(position);
        holder.setCourse(course);
    }

    @Override
    public int getItemCount() {
        return getShowingCourses().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final View itemView;

        private final TextView titleTextView;
        private final TextView instructorTextView;
        private final TextView classTimeTextView;
        private final TextView examTimeTextView;
        private final TextView capacityTextView;
        private final TextView unitTextView;
        private final TextView identifierTextView;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.titleTextView = itemView.findViewById(R.id.titleTextView);
            this.instructorTextView = itemView.findViewById(R.id.instructorTextView);
            this.classTimeTextView = itemView.findViewById(R.id.classSessionsTextView);
            this.examTimeTextView = itemView.findViewById(R.id.examTimeTextView);
            this.capacityTextView = itemView.findViewById(R.id.capacityTextView);
            this.unitTextView = itemView.findViewById(R.id.unitTextView);
            this.identifierTextView = itemView.findViewById(R.id.courseIdentifierTextView);
        }

        public void setCourse(Course course) {
            this.titleTextView.setText(course.getTitle());
            this.instructorTextView.setText(course.getInstructor());
            this.classTimeTextView.setText(course.getSessionsString());
            this.examTimeTextView.setText(course.getExamTime());
            this.capacityTextView.setText(String.valueOf(course.getCapacity()));
            this.unitTextView.setText(String.valueOf(course.getUnit()));
            this.identifierTextView.setText(String.format(Locale.US, "%d, %d",
                    course.getCourseId(), course.getGroupId()));
        }
    }
}
