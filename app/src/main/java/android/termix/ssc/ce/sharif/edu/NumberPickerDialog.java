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
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.DialogFragment;

import org.jetbrains.annotations.NotNull;

public class NumberPickerDialog extends DialogFragment {
    private final int id, group;

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

        String label;
        if (id == 0) {
            label = AlarmCenter.PREFERENCE_LABEL;
        } else {
            label = AlarmCenter.PREFERENCE_LABEL + "_" + id + "_" + group;
        }
        SwitchCompat alarmSituation = root.findViewById(R.id.notification_switch);

        int value = sharedPreference.getInt(label, 5);
        if (value == -1) {
            minutePicker.setValue(5);
            alarmSituation.setChecked(false);
        } else {
            minutePicker.setValue(value);
            alarmSituation.setChecked(true);
        }

        Button button = root.findViewById(R.id.save_button);
        button.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreference.edit();
            if (alarmSituation.isChecked()) {
                editor.putInt(label, minutePicker.getValue());
            } else {
                editor.putInt(label, -1);
            }
            editor.apply();
            dismiss();
        });

        builder.setView(root);
        return builder.create();
    }
}
