package android.termix.ssc.ce.sharif.edu;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.termix.ssc.ce.sharif.edu.database.DatabaseManager;
import android.termix.ssc.ce.sharif.edu.alarm.AlarmCenter;
import android.termix.ssc.ce.sharif.edu.loader.MySelectionsLoader;
import android.termix.ssc.ce.sharif.edu.model.Course;
import android.termix.ssc.ce.sharif.edu.model.CourseSession;
import android.termix.ssc.ce.sharif.edu.model.Session;
import android.termix.ssc.ce.sharif.edu.myCourseManager.MyCourseManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

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
import java.util.Objects;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.ScaleInAnimator;

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

    private Handler handler;

    private boolean isSearching;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new AlarmCenter(getBaseContext()).startAlarms(2);
        // set view
        setContentView(R.layout.activity_main);
        // set status bar color
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        // get weekdays recycler view
        ArrayList<RecyclerView> recyclerViews = new ArrayList<>();
        recyclerViews.add(findViewById(R.id.recycler_saturday));
        recyclerViews.add(findViewById(R.id.recycler_sunday));
        recyclerViews.add(findViewById(R.id.recycler_monday));
        recyclerViews.add(findViewById(R.id.recycler_tuesday));
        recyclerViews.add(findViewById(R.id.recycler_wednesday));
        recyclerViews.add(findViewById(R.id.recycler_thursday));
        recyclerViews.add(findViewById(R.id.recycler_no_class_time));
        // initial recycler views adapter
        adapters = new ArrayList<>();
        for (RecyclerView recyclerView : recyclerViews) {
            DayAdapter adapter = new DayAdapter(this);
            recyclerView.setAdapter(adapter);
            recyclerView.setNestedScrollingEnabled(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setItemAnimator(new ScaleInAnimator());
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new DaySwipeHelper() {
                @Override
                public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
                    removeWithCourseSession(((DayAdapter.ViewHolder) viewHolder).getCourseSession());
                }
            });
            itemTouchHelper.attachToRecyclerView(recyclerView);
            adapters.add(adapter);
        }
        // create handler
        handler = new Handler();
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
            if (mySelections != null) {
                ArrayList<ArrayList<CourseSession>> mySelectionMap = CourseSession
                        .getWeekdayCourseSessionsMap(mySelections);
                ArrayList<CourseSession> noClassCourseSessions = new ArrayList<>();
                for (Course course : mySelections) {
                    if (course.getSessionParser().getSessionJsonArray().length() == 0) {
                        noClassCourseSessions.add(new CourseSession(course, Session.NULL_SESSION));
                    }
                }
                synchronized (loadingLock) {
                    if (selectionsLoadSource != LoadSource.NETWORK) {
                        selectionsLoadSource = LoadSource.LOCAL;
                        for (int i = 0; i < adapters.size() - 1; i++) {
                            int finalI = i;
                            handler.post(() -> adapters.get(finalI).rebaseCourseSessionsAndNotify(mySelectionMap.get(finalI)));
                        }
                        handler.post(() -> adapters.get(adapters.size() - 1).rebaseCourseSessionsAndNotify(noClassCourseSessions));
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
            if (mySelections != null) {
                ArrayList<ArrayList<CourseSession>> mySelectionMap = CourseSession.getWeekdayCourseSessionsMap(mySelections);
                ArrayList<CourseSession> noClassCourseSessions = new ArrayList<>();
                for (Course course : mySelections) {
                    if (course.getSessionParser().getSessionJsonArray().length() == 0) {
                        noClassCourseSessions.add(new CourseSession(course, Session.NULL_SESSION));
                    }
                }
                synchronized (loadingLock) {
                    selectionsLoadSource = LoadSource.NETWORK;
                    for (int i = 0; i < adapters.size() - 1; i++) {
                        int finalI = i;
                        handler.post(() -> adapters.get(finalI).rebaseCourseSessionsAndNotify(mySelectionMap.get(finalI)));
                    }
                    handler.post(() -> adapters.get(adapters.size() - 1).rebaseCourseSessionsAndNotify(noClassCourseSessions));
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
                if (coursesAdapter == null) {
                    coursesAdapter = new SearchResultAdapter(MainActivity.this);
                }
                coursesAdapter.getFilter().filter(s);
            }
        });

        searchBar.setOnFocusChangeListener((v, hasFocus) -> {
            isSearching = hasFocus;
            if (searchResultRecyclerView == null) {
                searchResultRecyclerView = findViewById(R.id.searchResultRecyclerView);
                coursesAdapter = new SearchResultAdapter(this);
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
        hideKeyboard(this);
        isSearching = false;
        nestedScrollView.setVisibility(View.VISIBLE);
        searchResultRecyclerView.setVisibility(View.GONE);
        searchBar.clearFocus();
        searchBar.setText("");
        ArrayList<CourseSession> courseSessions = CourseSession.getCourseSession(course);
        if (courseSessions.isEmpty()) {
            adapters.get(adapters.size() - 1).insertCourseSessionAndNotify(new CourseSession(course,
                    Session.NULL_SESSION));
        } else {
            for (CourseSession courseSession : courseSessions) {
                adapters.get(courseSession.getSession().getDay()).insertCourseSessionAndNotify(courseSession);
            }
        }
        App.getExecutorService().execute(() -> {
            boolean status = MyCourseManager.getInstance().select(course.getCourseId(), course.getGroupId());
            if (!status) {
                handler.post(() -> Toast.makeText(MainActivity.this, "شبکه در دسترس نیست.", Toast.LENGTH_SHORT).show());

            }
            DatabaseManager.getInstance().insertCourse(course);
        });
    }

    public void removeWithCourseSession(CourseSession courseSession) {
        if (courseSession.getSession() == Session.NULL_SESSION) {
            adapters.get(adapters.size() - 1).remove(courseSession);
        } else {
            removeCourse(courseSession.getCourse());
        }
    }

    private void removeCourse(Course course) {
        ArrayList<CourseSession> courseSessions = CourseSession.getCourseSession(course);
        for (CourseSession courseSession : courseSessions) {
            adapters.get(courseSession.getSession().getDay()).remove(courseSession);
        }
        App.getExecutorService().execute(() -> {
            boolean status = MyCourseManager.getInstance().unselect(course.getCourseId(), course.getGroupId());
            if (!status) {
                handler.post(() -> Toast.makeText(MainActivity.this, "شبکه در دسترس نیست.", Toast.LENGTH_SHORT).show());
            }
            DatabaseManager.getInstance().deleteCourse(course);
        });
    }

    private final ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new
            ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            if (result.getData() != null) {
                ArrayList<String> finalResults = result.getData()
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                searchBar.setText(Objects.requireNonNull(finalResults).get(0));
                searchBar.requestFocus();
                hideKeyboard(this);
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

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    enum LoadSource {
        NOT_LOADED,
        LOCAL,
        NETWORK,
    }
}
