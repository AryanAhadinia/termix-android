package android.termix.ssc.ce.sharif.edu;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.termix.ssc.ce.sharif.edu.preferenceManager.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import org.jetbrains.annotations.NotNull;

public class ChooseDepartmentDialog extends DialogFragment {
    private static final int[] textViewIds = {
            R.id.civil,
            R.id.industry,
            R.id.math,
            R.id.chemistry,
            R.id.physics,
            R.id.electricity,
            R.id.oil,
            R.id.material,
            R.id.mechanics,
            R.id.computer,
            R.id.economics,
            R.id.aerospace,
            R.id.energy
    };

    private static final int[] departmentIds = {
            20,
            21,
            22,
            23,
            24,
            25,
            26,
            27,
            28,
            40,
            44,
            45,
            46
    };

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View root = inflater.inflate(R.layout.dialog_departments, null);
        setupList(root);
        builder.setView(root);
        return builder.create();
    }

    private void selectDepartment(int depId) {
        PreferenceManager.getInstance().writeDepartment(depId);
        dismiss();

        MainActivity mainActivity = (MainActivity) requireContext();
        if (mainActivity.getCoursesAdapter() != null) {
            mainActivity.getCoursesAdapter().updateList(depId);
        }
    }

    private void setupList(View root) {
        for (int i = 0; i < 13; i++) {
            int finalI = i;
            root.findViewById(textViewIds[i]).setOnClickListener(v ->
                    selectDepartment(departmentIds[finalI]));
        }
    }
}
