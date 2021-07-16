package android.termix.ssc.ce.sharif.edu;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.termix.ssc.ce.sharif.edu.network.NetworkException;
import android.termix.ssc.ce.sharif.edu.network.tasks.GetAllCoursesTask;
import android.termix.ssc.ce.sharif.edu.network.tasks.SignUpTask;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.PopupWindow;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private AutoCompleteTextView searchBar;
    private TextInputLayout textInputLayout;
    private ConstraintLayout constraintLayout;

    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new
            ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            if (result.getData() != null) {
                ArrayList<String> finalResults = result.getData()
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                searchBar.setText(Objects.requireNonNull(finalResults).get(0));
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        setUpVoiceSearch();
        setUpSettings();
    }

    private void setUpVoiceSearch() {
        textInputLayout.setEndIconOnClickListener(v -> {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "fa");
            resultLauncher.launch(intent);
        });
    }

    private void setUpSettings() {
        textInputLayout.setStartIconOnClickListener(v -> {
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.settings_popup, null);
            PopupWindow popup = new PopupWindow(popupView, 300, 600, true);
            popup.showAtLocation(constraintLayout, Gravity.CENTER, 0, 0);
        });
    }
}