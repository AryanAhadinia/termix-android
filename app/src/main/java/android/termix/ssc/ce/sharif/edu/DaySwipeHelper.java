package android.termix.ssc.ce.sharif.edu;

import android.graphics.Canvas;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public abstract class DaySwipeHelper extends ItemTouchHelper.SimpleCallback {

    public DaySwipeHelper() {
        super(0, ItemTouchHelper.LEFT);
    }

    @Override
    public boolean onMove(@NonNull @NotNull RecyclerView recyclerView,
                          @NonNull @NotNull RecyclerView.ViewHolder viewHolder,
                          @NonNull @NotNull RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public abstract void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder,
                                  int direction);

    @Override
    public void onSelectedChanged(@Nullable @org.jetbrains.annotations.Nullable RecyclerView.ViewHolder viewHolder,
                                  int actionState) {
        if (viewHolder != null) {
            final View foregroundView = ((DayAdapter.ViewHolder) viewHolder).getForeground();
            getDefaultUIUtil().onSelected(foregroundView);
        }
        System.out.println(actionState);
    }

    @Override
    public void onChildDrawOver(@NonNull @NotNull Canvas c,
                                @NonNull @NotNull RecyclerView recyclerView,
                                RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                int actionState, boolean isCurrentlyActive) {
        final View foregroundView = ((DayAdapter.ViewHolder) viewHolder).getForeground();
        getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY, actionState,
                isCurrentlyActive);
    }

    @Override
    public void clearView(@NonNull @NotNull RecyclerView recyclerView,
                          @NonNull @NotNull RecyclerView.ViewHolder viewHolder) {
        final View foregroundView = ((DayAdapter.ViewHolder) viewHolder).getForeground();
        foregroundView.setBackgroundResource(R.drawable.background_course_frame);
        getDefaultUIUtil().clearView(foregroundView);
    }

    @Override
    public void onChildDraw(@NonNull @NotNull Canvas c, @NonNull @NotNull RecyclerView recyclerView,
                            @NonNull @NotNull RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {
        final View foregroundView = ((DayAdapter.ViewHolder) viewHolder).getForeground();
        getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive);
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }
}