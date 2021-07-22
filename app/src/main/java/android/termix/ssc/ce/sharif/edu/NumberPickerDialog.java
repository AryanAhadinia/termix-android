package android.termix.ssc.ce.sharif.edu;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import org.jetbrains.annotations.NotNull;

public class NumberPickerDialog extends DialogFragment {
    private NumberPicker minutePicker;

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View root = inflater.inflate(R.layout.numberpicker_setting_time, null);
        minutePicker = root.findViewById(R.id.numberpicker_setting_picker);
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(60);
        minutePicker.setValue(10);

        minutePicker.setOnValueChangedListener((picker, oldVal, newVal) ->
                System.out.println("Heshamt: " + newVal));

        builder.setView(root);
        return builder.create();
    }
}
