package android.termix.ssc.ce.sharif.edu;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.termix.ssc.ce.sharif.edu.alarm.AlarmCenter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import org.jetbrains.annotations.NotNull;

public class NumberPickerDialog extends DialogFragment {
    private final int id, group;
    private int tmp;

    public NumberPickerDialog(int id, int group) {
        this.id = id;
        this.group = group;
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        SharedPreferences sharedPreference = requireContext().getSharedPreferences(
                AlarmCenter.PREFERENCE_NAME, Context.MODE_PRIVATE);
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View root = inflater.inflate(R.layout.numberpicker_setting_time, null);
        NumberPicker minutePicker = root.findViewById(R.id.numberpicker_setting_picker);
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(30);
        if (id == 0) {
            minutePicker.setValue(sharedPreference.getInt(AlarmCenter.PREFERENCE_LABEL, 5));
        } else {
            minutePicker.setValue(sharedPreference.getInt(AlarmCenter.PREFERENCE_LABEL + "_" +
                    id + "_" + group, 5));
        }
        tmp = minutePicker.getValue();

        minutePicker.setOnValueChangedListener((picker, oldVal, newVal) -> tmp = newVal);

        Button button = root.findViewById(R.id.save_button);
        button.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreference.edit();
            if (id == 0) {
                editor.putInt(AlarmCenter.PREFERENCE_LABEL, tmp);
            } else {
                editor.putInt(AlarmCenter.PREFERENCE_LABEL + "_" + id + "_" + group, tmp);
            }
            editor.apply();
            dismiss();
        });

        builder.setView(root);
        return builder.create();
    }
}
