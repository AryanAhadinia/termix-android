package android.termix.ssc.ce.sharif.edu;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.termix.ssc.ce.sharif.edu.database.DatabaseManager;
import android.termix.ssc.ce.sharif.edu.network.NetworkException;
import android.termix.ssc.ce.sharif.edu.network.tasks.SignOutTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

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

        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View root = inflater.inflate(R.layout.settings_dialog, null);
        TextView notificationTime = root.findViewById(R.id.notification_time);
        TextView changePassword = root.findViewById(R.id.change_password);
        TextView logout = root.findViewById(R.id.logout);

        Handler handler = new Handler();

        notificationTime.setOnClickListener(v -> {
            DialogFragment dialog = new NumberPickerDialog();
            dialog.show(getActivity().getSupportFragmentManager(),
                    "NumberPickerDialogFragment");
        });

        changePassword.setOnClickListener(v -> {
            new Thread(() -> DatabaseManager.getInstance().deleteData()).start();
            Intent intent = new Intent(getActivity(), ServeForgetPasswordActivity.class);
            startActivity(intent);
        });

        logout.setOnClickListener(v -> new SignOutTask() {
            @Override
            public void onResult(Object o) {
                new Thread(() -> DatabaseManager.getInstance().deleteData()).start();
                Intent intent = new Intent(getActivity(), LoginSignupActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

            @Override
            public void onException(NetworkException e) {
                handler.post(() -> {
                    Toast toast = Toast.makeText(requireContext(), e.getMessage(),
                            Toast.LENGTH_SHORT);
                    toast.show();
                });
            }

            @Override
            public void onError(Exception e) {
                handler.post(() -> {
                    Toast toast = Toast.makeText(requireContext(), "اینترنت در دسترس نیست",
                            Toast.LENGTH_SHORT);
                    toast.show();
                });
            }
        }.run());

        builder.setView(root);

        return builder.create();
    }
}
