package android.termix.ssc.ce.sharif.edu;

import android.app.Dialog;
import android.os.Bundle;
import android.termix.ssc.ce.sharif.edu.model.Course;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;

public class CourseDialog extends DialogFragment {
    private Course course;

    public CourseDialog(Course course) {
        this.course = course;
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View root = inflater.inflate(R.layout.course_page, null);

        TextView title = root.findViewById(R.id.course_name);
        TextView id = root.findViewById(R.id.course_id);
        TextView time = root.findViewById(R.id.course_time);
        TextView group = root.findViewById(R.id.course_group);
        TextView unit = root.findViewById(R.id.course_unit);
        TextView instructor = root.findViewById(R.id.course_instructor);
        TextView department = root.findViewById(R.id.course_department);
        TextView capacity = root.findViewById(R.id.course_capacity);
        TextView exam = root.findViewById(R.id.course_exam);
        TextView register = root.findViewById(R.id.register_message);
        TextView info = root.findViewById(R.id.info_message);

        title.setText(course.getTitle());
        capacity.setText(MessageFormat.format("ظرفیت: {0}", course.getCapacity()));
        unit.setText(MessageFormat.format("{0} واحد", course.getUnit()));
        group.setText(MessageFormat.format("گروه {0}", course.getGroupId()));
        id.setText(String.valueOf(course.getCourseId()));
        department.setText(Course.getDepartments().get(course.getDepId()));

        if (course.getInstructor().isEmpty()) {
            instructor.setVisibility(View.GONE);
        } else {
            instructor.setText(course.getInstructor());
        }

        if (course.getSessions().isEmpty()) {
            time.setVisibility(View.GONE);
        } else {
            time.setText(course.getSessionsString());
        }

        if (course.getExamTime().isEmpty()) {
            exam.setVisibility(View.GONE);
        } else {
            exam.setText(course.getExamTime());
        }

        if (course.getOnRegisterMessage().isEmpty()) {
            register.setVisibility(View.GONE);
        } else {
            register.setText(course.getOnRegisterMessage());
        }

        if (course.getInfoMessage().isEmpty()) {
            info.setVisibility(View.GONE);
        } else {
            info.setText(course.getInfoMessage());
        }

        builder.setView(root);
        return builder.create();
    }
}
