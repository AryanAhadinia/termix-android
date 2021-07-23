package android.termix.ssc.ce.sharif.edu;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.termix.ssc.ce.sharif.edu.loader.MySelectionsLoader;
import android.termix.ssc.ce.sharif.edu.model.Course;
import android.termix.ssc.ce.sharif.edu.model.CourseSession;
import android.termix.ssc.ce.sharif.edu.scheduleUI.DayAdapter;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {
    public final static String[] PERMISSIONS = {Manifest.permission.INTERNET, Manifest.permission.RECORD_AUDIO};

    private TextInputLayout textInputLayout;
    private AutoCompleteTextView searchBar;
    private ArrayList<DayAdapter> adapters;

    private ProgressBar progressBar;
    private NestedScrollView nestedScrollView;

    private LoadSource selectionsLoadSource;
    private final Object loadingLock = new Object();

    private boolean isSearching;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set view
        setContentView(R.layout.activity_main);
        // set status bar color
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        // get recycler view
        ArrayList<RecyclerView> recyclerViews = new ArrayList<>();
        recyclerViews.add(findViewById(R.id.recycler_saturday));
        recyclerViews.add(findViewById(R.id.recycler_sunday));
        recyclerViews.add(findViewById(R.id.recycler_monday));
        recyclerViews.add(findViewById(R.id.recycler_tuesday));
        recyclerViews.add(findViewById(R.id.recycler_wednesday));
        recyclerViews.add(findViewById(R.id.recycler_thursday));
        // initial recycler views adapter
        adapters = new ArrayList<>();
        for (RecyclerView recyclerView : recyclerViews) {
            DayAdapter adapter = new DayAdapter();
            recyclerView.setAdapter(adapter);
            recyclerView.setNestedScrollingEnabled(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapters.add(adapter);
        }
        // create handler
        Handler handler = new Handler();
        // get required views
        progressBar = findViewById(R.id.progressBar);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        textInputLayout = findViewById(R.id.text_input_layout);
        searchBar = findViewById(R.id.search_bar);
        // load selections
        selectionsLoadSource = LoadSource.NOT_LOADED;
        isSearching = false;
        // load my selections from
        App.getExecutorService().execute(() -> {
            ArrayList<Course> mySelections = MySelectionsLoader.getInstance().getFromLocal();
            Log.i("Selections", "Local fetched");
            if (mySelections != null) {
                ArrayList<ArrayList<CourseSession>> mySelectionMap = CourseSession
                        .getWeekdayCourseSessionsMap(mySelections);
                synchronized (loadingLock) {
                    if (selectionsLoadSource != LoadSource.NETWORK) {
                        selectionsLoadSource = LoadSource.LOCAL;
                        for (int i = 0; i < adapters.size(); i++) {
                            int finalI = i;
                            handler.post(() -> adapters.get(finalI).insertCourseSessionsAndNotify(mySelectionMap.get(finalI)));
                        }
                        if (nestedScrollView.getVisibility() != View.VISIBLE) {
                            handler.post(this::onLoad);
                        }
                    }
                }
            }
        });
        // load my selections from network
        App.getExecutorService().execute(() -> {
            ArrayList<Course> mySelections = MySelectionsLoader.getInstance().getFromNetwork();
            Log.i("Selections", "Network fetched");
            if (mySelections != null) {
                ArrayList<ArrayList<CourseSession>> mySelectionMap = CourseSession.getWeekdayCourseSessionsMap(mySelections);
                synchronized (loadingLock) {
                    selectionsLoadSource = LoadSource.NETWORK;
                    for (int i = 0; i < adapters.size(); i++) {
                        int finalI = i;
                        handler.post(() -> adapters.get(finalI).insertCourseSessionsAndNotify(mySelectionMap.get(finalI)));
                    }
                    if (nestedScrollView.getVisibility() != View.VISIBLE) {
                        handler.post(this::onLoad);
                    }
                }
            }
        });

        searchBar.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                isSearching = true;
            }
        });

        textInputLayout.setStartIconOnClickListener(e -> mPermissionResult.launch(PERMISSIONS));

        setUpSettings(); // TODO
    }

    @Override
    public void onBackPressed() {
        if (isSearching) {
            isSearching = false;
        } else {
            super.onBackPressed();
        }

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

    private void onLoad() {
        nestedScrollView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    enum LoadSource {
        NOT_LOADED,
        LOCAL,
        NETWORK,
    }
}
