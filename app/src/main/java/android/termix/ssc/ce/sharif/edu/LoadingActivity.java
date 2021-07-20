package android.termix.ssc.ce.sharif.edu;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Bundle;
import android.termix.ssc.ce.sharif.edu.network.NetworkException;
import android.termix.ssc.ce.sharif.edu.network.tasks.TestTokenTask;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set status bar color
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        // set content view
        setContentView(R.layout.activity_loading);
        // animating logo as progress bar
        ImageView logo = findViewById(R.id.logo);
        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                logo,
                PropertyValuesHolder.ofFloat("scaleX", 1.2f),
                PropertyValuesHolder.ofFloat("scaleY", 1.2f));
        scaleDown.setDuration(400);
        scaleDown.setRepeatCount(ObjectAnimator.INFINITE);
        scaleDown.setRepeatMode(ObjectAnimator.REVERSE);
        scaleDown.start();
        // Check network and token
        App.getExecutorService().execute(new TestTokenTask() {

            // Call when both network and token are OK
            @Override
            public void onResult(Object o) {
                // TODO: go to main activity immediately, show process bar while loading
            }

            // Call when network is OK but token is not accepted
            @Override
            public void onException(NetworkException e) {
                // TODO: load all courses, go to login activity, In login activity, wait while course are loading
            }

            // Call when server is unreachable
            @Override
            public void onError(Exception e) {
                // TODO: fallback to local database. show error if data is not reachable
            }
        });
    }
}