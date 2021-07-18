package android.termix.ssc.ce.sharif.edu;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.termix.ssc.ce.sharif.edu.network.NetworkException;
import android.termix.ssc.ce.sharif.edu.network.tasks.GetAllCoursesTask;
import android.termix.ssc.ce.sharif.edu.network.tasks.SignUpTask;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Objects;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {
    public final static String[] PERMISSIONS = {Manifest.permission.INTERNET,
            Manifest.permission.RECORD_AUDIO};
    private AutoCompleteTextView searchBar;
    private TextInputLayout textInputLayout;
    private ConstraintLayout constraintLayout;

    private final ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new
            ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            if (result.getData() != null) {
                ArrayList<String> finalResults = result.getData()
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                searchBar.setText(Objects.requireNonNull(finalResults).get(0));
            }
        }
    });

    private final ActivityResultLauncher<String[]> mPermissionResult = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                try {
                    if (result.get(Manifest.permission.RECORD_AUDIO)) {
                        setUpVoiceSearch();
                    } else {
                        Log.e(TAG, "onActivityResult: RECORD AUDIO PERMISSION REJECTED!");
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onActivityResult: PERMISSION FAILED! (NULL)");
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().setReorderingAllowed(true)
                    .add(R.id.fragment_container_view, LoadingFragment.class, null).commit();
        }
        setContentView(R.layout.activity_main);
        new GetAllCoursesTask() {
            @Override
            public void onResult(Object o) {

            }

            @Override
            public void onException(NetworkException e) {

            }

            @Override
            public void onError(Exception e) {

            }
        }.run();

        textInputLayout = findViewById(R.id.text_input_layout);
        searchBar = findViewById(R.id.search_bar);
        constraintLayout = findViewById(R.id.constraint_layout);
        textInputLayout.setEndIconOnClickListener(v -> mPermissionResult.launch(PERMISSIONS));
        setUpSettings();
        Intent intent = new Intent(this, LoginSignupActivity.class);
        startActivity(intent);
    }

    private void setUpVoiceSearch() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "fa");
        resultLauncher.launch(intent);
    }

    private void setUpSettings() {
        textInputLayout.setStartIconOnClickListener(v -> {
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.settings_popup, null);
            final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
            PopupWindow popup = new PopupWindow(popupView, (int) (200 * scale + 0.5f),
                    (int) (200 * scale + 0.5f), true);
            popup.showAtLocation(constraintLayout, Gravity.CENTER, 0, 0);
        });
    }
}