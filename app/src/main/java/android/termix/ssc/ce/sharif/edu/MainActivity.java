package android.termix.ssc.ce.sharif.edu;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.termix.ssc.ce.sharif.edu.alarm.AlarmBroadcastReceiver;
import android.termix.ssc.ce.sharif.edu.loader.MySelectionsLoader;
import android.termix.ssc.ce.sharif.edu.model.Course;
import android.termix.ssc.ce.sharif.edu.model.CourseSession;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {
    public final static String[] PERMISSIONS = {Manifest.permission.INTERNET, Manifest.permission.RECORD_AUDIO};

    private TextInputLayout textInputLayout;
    private AutoCompleteTextView searchBar;
    private ArrayList<DayAdapter> adapters;
    private SearchResultAdapter coursesAdapter;

    private ProgressBar progressBar;
    private LinearLayout mainLinearLayout;

    private NestedScrollView nestedScrollView;

    private RecyclerView searchResultRecyclerView;

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
        // get weekdays recycler view
        coursesAdapter = new SearchResultAdapter(this);

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
            DayAdapter adapter = new DayAdapter(this);
            recyclerView.setAdapter(adapter);
            recyclerView.setNestedScrollingEnabled(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new DaySwipeHelper() {
                @Override
                public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
                    System.out.println("salam");
                }
            });
            itemTouchHelper.attachToRecyclerView(recyclerView);
            adapters.add(adapter);
        }
        // create handler
        Handler handler = new Handler();
        // get required views
        mainLinearLayout = findViewById(R.id.main_linear_layout);
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
                        if (mainLinearLayout.getVisibility() != View.VISIBLE) {
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
                    if (mainLinearLayout.getVisibility() != View.VISIBLE) {
                        handler.post(this::onLoad);
                    }
                }
            }
        });

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                coursesAdapter.getFilter().filter(s);
            }
        });

        searchBar.setOnFocusChangeListener((v, hasFocus) -> {
            isSearching = hasFocus;
            if (searchResultRecyclerView == null) {
                searchResultRecyclerView = findViewById(R.id.searchResultRecyclerView);
                AlphaInAnimationAdapter alphaAnimatedAdapter = new AlphaInAnimationAdapter(coursesAdapter);
                alphaAnimatedAdapter.setFirstOnly(false);
                ScaleInAnimationAdapter scaleAnimatedAdapter = new ScaleInAnimationAdapter(alphaAnimatedAdapter);
                scaleAnimatedAdapter.setFirstOnly(false);
                searchResultRecyclerView.setAdapter(scaleAnimatedAdapter);
                searchResultRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
            }
            if (hasFocus) {
                nestedScrollView.setVisibility(View.GONE);
                searchResultRecyclerView.setVisibility(View.VISIBLE);
            } else {
                nestedScrollView.setVisibility(View.VISIBLE);
                searchResultRecyclerView.setVisibility(View.GONE);
                searchBar.setText("");
            }
        });

        textInputLayout.setStartIconOnClickListener(e -> mPermissionResult.launch(PERMISSIONS));

        setUpSettings(); // TODO


//        AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(this, AlarmBroadcastReceiver.class);
//        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.set(Calendar.HOUR_OF_DAY, 23);
//        calendar.set(Calendar.MINUTE, 16);
//
//        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * 1, alarmIntent);
    }

    @Override
    public void onBackPressed() {
        if (isSearching) {
            isSearching = false;
            nestedScrollView.setVisibility(View.VISIBLE);
            searchResultRecyclerView.setVisibility(View.GONE);
            searchBar.clearFocus();
            searchBar.setText("");
        } else {
            super.onBackPressed();
        }
    }

    public void addCourse(Course course) {
        isSearching = false;
        nestedScrollView.setVisibility(View.VISIBLE);
        searchResultRecyclerView.setVisibility(View.GONE);
        searchBar.clearFocus();
        searchBar.setText("");
        ArrayList<CourseSession> courseSessions = CourseSession.getCourseSessions(course);
        for (CourseSession courseSession : courseSessions) {
            adapters.get(courseSession.getSession().getDay())
                    .insertCourseSessionAndNotify(courseSession);
        }
    }

    private final ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new
            ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            if (result.getData() != null) {
                ArrayList<String> finalResults = result.getData()
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                searchBar.setText(Objects.requireNonNull(finalResults).get(0));
                searchBar.requestFocus();
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
        mainLinearLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    public SearchResultAdapter getCoursesAdapter() {
        return coursesAdapter;
    }

    enum LoadSource {
        NOT_LOADED,
        LOCAL,
        NETWORK,
    }
}
