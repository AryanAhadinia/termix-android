package android.termix.ssc.ce.sharif.edu;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.termix.ssc.ce.sharif.edu.network.NetworkException;
import android.termix.ssc.ce.sharif.edu.network.tasks.SignUpTask;
import android.widget.AutoCompleteTextView;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private AutoCompleteTextView searchBar;
    private TextInputLayout textInputLayout;

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
        new SignUpTask("aryanahadinia24@gmail.com", "123456789") {
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

        setUpVoiceSearch();
    }

    private void setUpVoiceSearch() {
        textInputLayout = findViewById(R.id.text_input_layout);
        searchBar = findViewById(R.id.search_bar);
        textInputLayout.setEndIconOnClickListener(v -> {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "fa");
            resultLauncher.launch(intent);
        });
    }
}