package android.termix.ssc.ce.sharif.edu.activities.mainActivity.adapters;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.termix.ssc.ce.sharif.edu.activities.mainActivity.dialogs.NumberPickerDialog;
import android.termix.ssc.ce.sharif.edu.R;
import android.termix.ssc.ce.sharif.edu.activities.mainActivity.MainActivity;
import android.termix.ssc.ce.sharif.edu.model.Course;
import android.termix.ssc.ce.sharif.edu.model.CourseSession;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.ViewHolder> {
    private final MainActivity mainActivity;

    private final ArrayList<CourseSession> courseSessions;
    private ArrayList<Pair<CourseSession, CourseSession>> conflictedCourseSessions;

    private final Set<CourseSession> conflictedCourseSessionsCache;

    public DayAdapter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.courseSessions = new ArrayList<>();
        this.conflictedCourseSessions = new ArrayList<>();
        this.conflictedCourseSessionsCache = new HashSet<>();
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent,
                                         int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View courseView = inflater.inflate(R.layout.layout_selection, parent, false);
        return new ViewHolder(courseView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        CourseSession courseSession = courseSessions.get(position);
        holder.setCourseSession(courseSession, conflictedCourseSessionsCache.contains(courseSession),
                mainActivity);
    }

    @Override
    public int getItemCount() {
        return this.courseSessions.size();
    }

    public void insertCourseSessionAndNotify(CourseSession courseSession) {
        conflictedCourseSessionsCache.clear();
        boolean conflicted = false;
        for (int i = 0; i < courseSessions.size(); i++) {
            if (courseSession.hasConflict(courseSessions.get(i))) {
                conflicted = true;
                conflictedCourseSessions.add(new Pair<>(courseSession, courseSessions.get(i)));
                conflictedCourseSessionsCache.add(courseSessions.get(i));
                notifyItemChanged(i);
            }
        }
        if (conflicted) {
            conflictedCourseSessionsCache.add(courseSession);
        }
        int index = 0;
        while (index < courseSessions.size()) {
            if (courseSession.compareTo(courseSessions.get(index)) < 0) {
                courseSessions.add(index, courseSession);
                notifyItemInserted(index);
                return;
            } else {
                index++;
            }
        }
        courseSessions.add(courseSession);
        notifyItemInserted(index);
    }

    public void rebaseCourseSessionsAndNotify(ArrayList<CourseSession> courseSessions) {
        this.courseSessions.clear();
        notifyDataSetChanged();
        for (CourseSession courseSession : courseSessions) {
            insertCourseSessionAndNotify(courseSession);
        }
    }

    public void remove(CourseSession courseSession) {
        conflictedCourseSessionsCache.clear();
        int index = courseSessions.indexOf(courseSession);
        notifyItemChanged(index);
        courseSessions.remove(index);
        notifyItemRemoved(index);
        ArrayList<Pair<CourseSession, CourseSession>> newConflictedCourseSession = new ArrayList<>();
        HashSet<CourseSession> wasConflicting = new HashSet<>();
        HashSet<CourseSession> stillConflicting = new HashSet<>();
        for (Pair<CourseSession, CourseSession> conflictedCourseSessionPair : conflictedCourseSessions) {
            if (conflictedCourseSessionPair.first.equals(courseSession)) {
                wasConflicting.add(conflictedCourseSessionPair.second);
            } else if (conflictedCourseSessionPair.second.equals(courseSession)) {
                wasConflicting.add(conflictedCourseSessionPair.first);
            } else {
                newConflictedCourseSession.add(conflictedCourseSessionPair);
                stillConflicting.add(conflictedCourseSessionPair.first);
                stillConflicting.add(conflictedCourseSessionPair.second);
            }
        }
        this.conflictedCourseSessions = newConflictedCourseSession;
        wasConflicting.removeAll(stillConflicting);
        for (int i = 0; i < courseSessions.size(); i++) {
            if (wasConflicting.contains(courseSessions.get(i))) {
                notifyItemChanged(i);
            }
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnLongClickListener {
        private CourseSession courseSession;
        private MainActivity mainActivity;

        private final ConstraintLayout background;
        private final ConstraintLayout foreground;

        private final TextView unitTextView;
        private final TextView idTextView;
        private final TextView titleTextView;
        private final TextView instructorTextView;
        private final TextView timesTextView;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            this.background = (ConstraintLayout) itemView.findViewById(R.id.selectionBackground);
            this.foreground = (ConstraintLayout) itemView.findViewById(R.id.selectionForeground);
            this.unitTextView = (TextView) itemView.findViewById(R.id.unit);
            this.idTextView = (TextView) itemView.findViewById(R.id.identifier);
            this.titleTextView = (TextView) itemView.findViewById(R.id.title);
            this.instructorTextView = (TextView) itemView.findViewById(R.id.instructor);
            this.timesTextView = (TextView) itemView.findViewById(R.id.times);
            itemView.setOnLongClickListener(this);
        }

        public void setCourseSession(CourseSession courseSession, boolean conflicted, MainActivity mainActivity) {
            this.mainActivity = mainActivity;
            this.courseSession = courseSession;
            Course course = courseSession.getCourse();
            this.unitTextView.setText(String.valueOf(course.getUnit()));
            this.idTextView.setText(String.format(Locale.US, "%d, %d", course.getCourseId(),
                    course.getGroupId()));
            this.titleTextView.setText(course.getTitle());
            this.instructorTextView.setText(course.getInstructor());
            this.timesTextView.setText(course.getSessionsString());
            int colorId;
            try {
                GradientDrawable viewColor = (GradientDrawable) foreground.getBackground();
                ColorStateList colorStateList = viewColor.getColor();
                colorId = colorStateList.getDefaultColor();
            } catch (Exception e) {
                ColorDrawable viewColor = (ColorDrawable) foreground.getBackground();
                colorId = viewColor.getColor();
            }

            if (conflicted && colorId == mainActivity.getApplicationContext().getResources()
                    .getColor(R.color.dark_red, mainActivity.getTheme())) {
                animateToConflicted();
            } else if (conflicted && colorId == mainActivity.getApplicationContext().getResources()
                    .getColor(R.color.stripe_start, mainActivity.getTheme())) {
                foreground.setBackgroundResource(R.drawable
                        .background_course_frame_conflicted_orange);
            } else if (!conflicted && colorId == mainActivity.getApplicationContext().getResources()
                    .getColor(R.color.stripe_start, mainActivity.getTheme())) {
                animateToNormal();
            } else {
                foreground.setBackgroundResource(R.drawable.background_course_frame);
            }
        }

        public ConstraintLayout getBackground() {
            return background;
        }

        public ConstraintLayout getForeground() {
            return foreground;
        }

        public CourseSession getCourseSession() {
            return courseSession;
        }

        @Override
        public boolean onLongClick(View v) {
            DialogFragment dialog = new NumberPickerDialog(courseSession.getCourse().getCourseId(),
                    courseSession.getCourse().getGroupId());
            dialog.show(mainActivity.getSupportFragmentManager(), "NumberPickerDialogFragment");
            return true;
        }

        private ObjectAnimator buildColorFade(int startColorId, int endColorId) {
            ObjectAnimator colorFade = ObjectAnimator.ofObject(foreground,
                    "backgroundColor", new ArgbEvaluator(),
                    mainActivity.getApplicationContext().getResources().getColor(startColorId,
                            mainActivity.getTheme()),
                    mainActivity.getApplicationContext().getResources().getColor(endColorId,
                            mainActivity.getTheme()));
            colorFade.setDuration(2000);
            colorFade.setStartDelay(0);
            return colorFade;
        }

        private Animator.AnimatorListener setAnimatorListenerForFinalBackground(int backgroundId) {
            return new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    foreground.setBackgroundResource(backgroundId);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            };
        }

        private void animateToConflicted() {
            ObjectAnimator colorFade = buildColorFade(R.color.dark_red, R.color.stripe_start);

            colorFade.addListener(setAnimatorListenerForFinalBackground(R.drawable
                    .background_course_frame_conflicted_orange));

            colorFade.start();
        }

        private void animateToNormal() {
            ObjectAnimator colorFade = buildColorFade(R.color.stripe_start, R.color.dark_red);

            colorFade.addListener(setAnimatorListenerForFinalBackground(R.drawable
                    .background_course_frame));

            colorFade.start();
        }
    }
}
