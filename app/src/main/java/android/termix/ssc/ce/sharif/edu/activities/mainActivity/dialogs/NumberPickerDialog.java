package android.termix.ssc.ce.sharif.edu.activities.mainActivity.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.termix.ssc.ce.sharif.edu.R;
import android.termix.ssc.ce.sharif.edu.preferenceManager.PreferenceManager;
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
    private NumberPicker minutePicker;
    private View root;

    public NumberPickerDialog(int id, int group) {
        this.id = id;
        this.group = group;
    }

    private void setNumberPicker() {
        minutePicker = root.findViewById(R.id.numberpicker_setting_picker);
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(12);
        String[] minuteValues = new String[13];
        for (int i = 0; i < 13; i++) {
            minuteValues[i] = Integer.toString(i * 5);
        }
        minutePicker.setDisplayedValues(minuteValues);
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        root = inflater.inflate(R.layout.numberpicker_setting_time, null);

        setNumberPicker();

        SwitchCompat alarmSituation = root.findViewById(R.id.notification_switch);

        int value = readPreference();
        if (value == -1) {
            minutePicker.setValue(1);
            alarmSituation.setChecked(false);
        } else {
            minutePicker.setValue(value / 5);
            alarmSituation.setChecked(true);
        }

        Button button = root.findViewById(R.id.save_button);
        button.setOnClickListener(v -> {
            if (alarmSituation.isChecked()) {
                writePreference(minutePicker.getValue() * 5);
            } else {
                writePreference(-1);
            }
            dismiss();
        });

        builder.setView(root);
        return builder.create();
    }

    private int readPreference() {
        if (id == 0) {
            return PreferenceManager.getInstance(requireContext().getApplicationContext()).readAlarmOffset();
        } else {
            return PreferenceManager.getInstance(requireContext().getApplicationContext()).readAlarmOffset(id, group);
        }
    }

    private void writePreference(int offset) {
        if (id == 0) {
            PreferenceManager.getInstance(requireContext().getApplicationContext()).writeAlarmOffset(offset);
        } else {
            PreferenceManager.getInstance(requireContext().getApplicationContext()).writeAlarmOffset(offset, id, group);
        }
    }
}
