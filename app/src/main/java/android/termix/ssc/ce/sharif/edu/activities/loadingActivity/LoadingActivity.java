package android.termix.ssc.ce.sharif.edu.activities.loadingActivity;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.termix.ssc.ce.sharif.edu.App;
import android.termix.ssc.ce.sharif.edu.R;
import android.termix.ssc.ce.sharif.edu.activities.loginSignUpActivity.LoginSignupActivity;
import android.termix.ssc.ce.sharif.edu.activities.mainActivity.MainActivity;
import android.termix.ssc.ce.sharif.edu.alarm.AlarmReceiver;
import android.termix.ssc.ce.sharif.edu.loader.MySelectionsLoader;
import android.termix.ssc.ce.sharif.edu.network.NetworkException;
import android.termix.ssc.ce.sharif.edu.network.tasks.TestTokenTask;
import android.termix.ssc.ce.sharif.edu.preferenceManager.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Calendar;

public class LoadingActivity extends AppCompatActivity {

    private ConstraintLayout mainLayout;
    private TextView errorText;
    private ObjectAnimator scaleDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set status bar color
        makeRootFullScreen();
        // set content view
        setContentView(R.layout.activity_loading);
        // animating logo as progress bar
        setScaleDown();
        // get layouts
        mainLayout = findViewById(R.id.main);
        errorText = findViewById(R.id.errorText);
        // set click listener
        mainLayout.setOnClickListener(v -> goToLoadingActivity());
        // Handler
        Handler handler = new Handler();
        // Async tasks
        decideNextState(handler);
        setAlarm();
    }

    private void makeRootFullScreen() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    private void setScaleDown() {
        ImageView logo = findViewById(R.id.logo);
        scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                logo,
                PropertyValuesHolder.ofFloat("scaleX", 1.2f),
                PropertyValuesHolder.ofFloat("scaleY", 1.2f));
        scaleDown.setDuration(400);
        scaleDown.setRepeatCount(ObjectAnimator.INFINITE);
        scaleDown.setRepeatMode(ObjectAnimator.REVERSE);
        scaleDown.start();
    }

    private void goToLoadingActivity() {
        Intent intent = new Intent(LoadingActivity.this,
                LoadingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void setAlarm() {
        // Start alarm manager to handle alarms
        AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                1000 * 60 * 2, alarmIntent);
    }

    private void goToMainActivity() {
        Intent intent = new Intent(LoadingActivity.this,
                MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void goToLoginSignupActivity() {
        Intent intent = new Intent(LoadingActivity.this,
                LoginSignupActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void decideNextState(Handler handler) {
        App.getExecutorService().execute(() -> {
            // Check network and token
            new TestTokenTask() {
                // Call when both network and token are OK
                @Override
                public void onResult(Object o) {
                    Log.i("Token Check", "Token accepted. Going to main activity");
                    App.getExecutorService().execute(MySelectionsLoader.getInstance());
                    handler.post(() -> goToMainActivity());
                }

                // Call when network is OK but token is not accepted
                @Override
                public void onException(NetworkException e) {
                    Log.i("Token Check", "Token not proved. Going to LoginSignupActivity");
                    handler.post(() -> goToLoginSignupActivity());
                }

                // Call when server is unreachable
                @Override
                public void onError(Exception e) {
                    Log.i("Token Check", "Network not found. Fallback to local");
                    if (PreferenceManager.getInstance(getApplicationContext()).containsAllCourses()) {
                        MySelectionsLoader.getInstance().run();
                        goToMainActivity();
                    } else {
                        handler.post(() -> {
                            scaleDown.cancel();
                            mainLayout.setClickable(true);
                            errorText.setVisibility(View.VISIBLE);
                        });
                    }
                }
            }.run();
        });
    }
}