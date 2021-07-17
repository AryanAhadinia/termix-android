package android.termix.ssc.ce.sharif.edu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

import static android.termix.ssc.ce.sharif.edu.MainActivity.PERMISSIONS;

public class LoadingFragment extends Fragment {
    private final ActivityResultLauncher<String[]> mPermissionResult = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(), result -> requireActivity()
                    .getSupportFragmentManager().beginTransaction().remove(this).commit());

    public LoadingFragment() {
        super(R.layout.fragment_loading);
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations
                                     .Nullable Bundle savedInstanceState) {
        mPermissionResult.launch(PERMISSIONS);
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}