package android.termix.ssc.ce.sharif.edu;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import org.jetbrains.annotations.NotNull;

public class ChooseDepartmentDialog extends DialogFragment {

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
        SharedPreferences sharedPreference = requireContext().getSharedPreferences(
                SearchResultAdapter.PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putInt(SearchResultAdapter.PREFERENCE_NAME, depId);
        editor.apply();
        dismiss();

        MainActivity mainActivity = (MainActivity) requireContext();
        mainActivity.getCoursesAdapter().updateList();
    }

    private void setupList(View root) {
        TextView civil = root.findViewById(R.id.civil);
        civil.setOnClickListener(v -> selectDepartment(20));
        TextView industry = root.findViewById(R.id.industry);
        industry.setOnClickListener(v -> selectDepartment(21));
        TextView math = root.findViewById(R.id.math);
        math.setOnClickListener(v -> selectDepartment(22));
        TextView chemistry = root.findViewById(R.id.chemistry);
        chemistry.setOnClickListener(v -> selectDepartment(23));
        TextView physics = root.findViewById(R.id.physics);
        physics.setOnClickListener(v -> selectDepartment(24));
        TextView electric = root.findViewById(R.id.electricity);
        electric.setOnClickListener(v -> selectDepartment(25));
        TextView oil = root.findViewById(R.id.oil);
        oil.setOnClickListener(v -> selectDepartment(26));
        TextView material = root.findViewById(R.id.material);
        material.setOnClickListener(v -> selectDepartment(27));
        TextView mechanics = root.findViewById(R.id.mechanics);
        mechanics.setOnClickListener(v -> selectDepartment(28));
        TextView computer = root.findViewById(R.id.computer);
        computer.setOnClickListener(v -> selectDepartment(40));
        TextView economics = root.findViewById(R.id.economics);
        economics.setOnClickListener(v -> selectDepartment(44));
        TextView aerospace = root.findViewById(R.id.aerospace);
        aerospace.setOnClickListener(v -> selectDepartment(45));
        TextView energy = root.findViewById(R.id.energy);
        energy.setOnClickListener(v -> selectDepartment(46));
    }
}
