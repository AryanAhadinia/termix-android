package android.termix.ssc.ce.sharif.edu;

import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import org.jetbrains.annotations.NotNull;

public class SettingsDialog extends DialogFragment {
    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(R.layout.settings_dialog);
        builder.setMessage(R.string.settings).setPositiveButton(R.string.save, (dialog, id) -> {
            //TODO: save
        }).setNegativeButton(R.string.cancel, (dialog, id) -> {
            //TODO: do nothing/ ignore
        });

        return builder.create();
    }
}
