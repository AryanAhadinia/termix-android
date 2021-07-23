package android.termix.ssc.ce.sharif.edu;

import android.content.Context;
import android.termix.ssc.ce.sharif.edu.loader.AllCoursesLoader;
import android.termix.ssc.ce.sharif.edu.model.Course;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder>
        implements Filterable {
    private final ArrayList<Course> showingCourses, allCourses;

    public SearchResultAdapter() {
        if (AllCoursesLoader.getInstance().getFromNetwork() != null) {
            allCourses = AllCoursesLoader.getInstance().getFromNetwork().get(40);
        } else {
            allCourses = AllCoursesLoader.getInstance().getFromLocal().get(40);
        }
        showingCourses = new ArrayList<>(allCourses);
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
        Course course = showingCourses.get(position);
        holder.setCourse(course);
    }

    @Override
    public int getItemCount() {
        return showingCourses.size();
    }

    @Override
    public Filter getFilter() {
        return courseFilter;
    }

    private final Filter courseFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Course> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(allCourses);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                try {
                    Integer.parseInt(filterPattern);
                    for (Course course : allCourses) {
                        if (String.valueOf(course.getCourseId()).contains(filterPattern)) {
                            filteredList.add(course);
                        }
                    }
                } catch (NumberFormatException nfe) {
                    for (Course course : allCourses) {
                        if (course.getTitle().toLowerCase().contains(filterPattern) ||
                                course.getInstructor().toLowerCase().contains(filterPattern)) {
                            filteredList.add(course);
                        }
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            Log.e("RIDAM: ", Integer.toString(filteredList.size()));
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            showingCourses.clear();
            showingCourses.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

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
            if (course.getInstructor().isEmpty()) {
                this.instructorTextView.setVisibility(View.GONE);
            } else {
                this.instructorTextView.setVisibility(View.VISIBLE);
                this.instructorTextView.setText(course.getInstructor());
            }
            if (course.getSessions().isEmpty()) {
                this.classTimeTextView.setVisibility(View.GONE);
            } else {
                this.classTimeTextView.setVisibility(View.VISIBLE);
                this.classTimeTextView.setText(course.getSessionsString());
            }
            if (course.getExamTime().isEmpty()) {
                this.examTimeTextView.setVisibility(View.GONE);
            } else {
                this.examTimeTextView.setVisibility(View.VISIBLE);
                this.examTimeTextView.setText(course.getExamTime());
            }
            this.capacityTextView.setText(String.valueOf(course.getCapacity()));
            this.unitTextView.setText(String.valueOf(course.getUnit()));
            this.identifierTextView.setText(String.format(Locale.US, "%d, %d",
                    course.getCourseId(), course.getGroupId()));
        }
    }
}
