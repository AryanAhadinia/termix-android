package android.termix.ssc.ce.sharif.edu.activities.mainActivity.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.termix.ssc.ce.sharif.edu.R;
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
    private final Course course;
    private View root;

    public CourseDialog(Course course) {
        this.course = course;
    }

    private void setTitle() {
        TextView title = root.findViewById(R.id.course_name);
        title.setText(course.getTitle());
    }

    private void setId() {
        TextView id = root.findViewById(R.id.course_id);
        id.setText(String.valueOf(course.getCourseId()));
    }

    private void setTime() {
        TextView time = root.findViewById(R.id.course_time);
        if (course.getSessions().isEmpty()) {
            time.setText("بدون کلاس");
        } else {
            time.setText(MessageFormat.format("زمان کلاس: {0}", course.getSessionsString()));
        }
    }

    private void setGroup() {
        TextView group = root.findViewById(R.id.course_group);
        group.setText(MessageFormat.format("گروه {0}", course.getGroupId()));
    }

    private void setUnit() {
        TextView unit = root.findViewById(R.id.course_unit);
        unit.setText(MessageFormat.format("{0} واحد", course.getUnit()));
    }

    private void setInstructor() {
        TextView instructor = root.findViewById(R.id.course_instructor);
        if (course.getInstructor().isEmpty()) {
            instructor.setText("بدون استاد");
        } else {
            instructor.setText(MessageFormat.format("استاد: {0}", course.getInstructor()));
        }
    }

    private void setDepartment() {
        TextView department = root.findViewById(R.id.course_department);
        department.setText(Course.getDepartments().get(course.getDepId()));

    }

    private void setCapacity() {
        TextView capacity = root.findViewById(R.id.course_capacity);
        capacity.setText(MessageFormat.format("ظرفیت: {0}", course.getCapacity()));

    }

    private void setExam() {
        TextView exam = root.findViewById(R.id.course_exam);
        if (course.getExamTime().isEmpty()) {
            exam.setText("بدون آزمون");
        } else {
            exam.setText(MessageFormat.format("زمان آزمون: {0}", course.getExamTime()));
        }
    }

    private void setRegister() {
        TextView register = root.findViewById(R.id.register_message);
        if (course.getOnRegisterMessage().isEmpty()) {
            register.setText("بدون پیام نام‌نویسی");
        } else {
            register.setText(MessageFormat.format("پیام نام‌نویسی: {0}",
                    course.getOnRegisterMessage()));
        }
    }

    private void setInfo() {
        TextView info = root.findViewById(R.id.info_message);
        if (course.getInfoMessage().isEmpty()) {
            info.setText("بدون پیام افزونه");
        } else {
            info.setText(MessageFormat.format("پیام افزونه: {0}", course.getInfoMessage()));
        }
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        root = inflater.inflate(R.layout.course_page, null);

        setTitle();
        setId();
        setTime();
        setGroup();
        setUnit();
        setInstructor();
        setDepartment();
        setInfo();
        setRegister();
        setExam();
        setCapacity();

        builder.setView(root);
        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.background_course_frame);
    }
}
