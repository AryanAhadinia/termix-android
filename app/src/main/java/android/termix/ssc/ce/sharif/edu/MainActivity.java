package android.termix.ssc.ce.sharif.edu;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.termix.ssc.ce.sharif.edu.model.Course;
import android.termix.ssc.ce.sharif.edu.model.CourseSession;
import android.termix.ssc.ce.sharif.edu.network.NetworkException;
import android.termix.ssc.ce.sharif.edu.network.tasks.TestTokenTask;
import android.termix.ssc.ce.sharif.edu.scheduleUI.AllCoursesDialogFragment;
import android.termix.ssc.ce.sharif.edu.scheduleUI.DayAdapter;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.ColorInt;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {
    public final static String[] PERMISSIONS = {Manifest.permission.INTERNET, Manifest.permission.RECORD_AUDIO};

    private ConstraintLayout constraintLayout;
    private TextInputLayout textInputLayout;
    private AutoCompleteTextView searchBar;
    private ArrayList<RecyclerView> recyclerViews;
    private ArrayList<DayAdapter> adapters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.dark_red));

        new TestTokenTask() {
            @Override
            public void onResult(Object o) {
                System.out.println("salam");
            }

            @Override
            public void onException(NetworkException e) {
                System.out.println("salam");
            }

            @Override
            public void onError(Exception e) {
                System.out.println("salam");
            }
        }.run();

        setContentView(R.layout.activity_main);

        constraintLayout = findViewById(R.id.constraint_layout);
        textInputLayout = findViewById(R.id.text_input_layout);
        searchBar = findViewById(R.id.search_bar);

        recyclerViews = new ArrayList<>();
        recyclerViews.add(findViewById(R.id.recycler_saturday));
        recyclerViews.add(findViewById(R.id.recycler_sunday));
        recyclerViews.add(findViewById(R.id.recycler_monday));
        recyclerViews.add(findViewById(R.id.recycler_tuesday));
        recyclerViews.add(findViewById(R.id.recycler_wednesday));
        recyclerViews.add(findViewById(R.id.recycler_thursday));

        adapters = new ArrayList<>();
        for (RecyclerView recyclerView : recyclerViews) {
            DayAdapter adapter = new DayAdapter();
            recyclerView.setAdapter(adapter);
            recyclerView.setNestedScrollingEnabled(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapters.add(adapter);
        }


        ArrayList<Course> test = Course.getTestList();
        ArrayList<ArrayList<CourseSession>> testMap = CourseSession.getWeekdayCourseSessionsMap(test);
        for (int i = 0; i < adapters.size(); i++) {
            adapters.get(i).insertCourseSessionsAndNotify(testMap.get(i));
        }


        textInputLayout.setStartIconOnClickListener(e -> mPermissionResult.launch(PERMISSIONS));

        searchBar.setOnLongClickListener(e -> {
            DialogFragment dialog = new AllCoursesDialogFragment();
            dialog.show(getSupportFragmentManager(), "Courses");
            return true;
        });

        setUpSettings(); // TODO
    }

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

    private final ActivityResultLauncher<String[]> mPermissionResult = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
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


    private void setUpVoiceSearch() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "fa");
        resultLauncher.launch(intent);
    }

    private void setUpSettings() {
        textInputLayout.setEndIconOnClickListener(v -> {
            DialogFragment dialog = new SettingsDialog();
            dialog.show(getSupportFragmentManager(), "SettingsDialogFragment");
        });
    }
}