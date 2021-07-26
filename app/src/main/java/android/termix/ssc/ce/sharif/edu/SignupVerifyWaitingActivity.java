package android.termix.ssc.ce.sharif.edu;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.termix.ssc.ce.sharif.edu.network.NetworkException;
import android.termix.ssc.ce.sharif.edu.network.tasks.VerifyAccountTask;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * @author AryanAhadinia
 * @author MohammadKashaniJabbari
 * @author ShahabHM
 * @since 1
 */
public class SignupVerifyWaitingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set layout
        setContentView(R.layout.activity_signup_verify_waiting);
        // set status bar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        // set background animation
        ConstraintLayout constraintLayout = findViewById(R.id.signup_verify_constraint_layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(0);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();
        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
        App.getExecutorService().execute(new VerifyAccountTask(appLinkData.toString()) {
            @Override
            public void onResult(Object o) {
                Log.i("Verification", "Verified");
            }

            @Override
            public void onException(NetworkException e) {
                Log.i("Verification", "Exception");
            }

            @Override
            public void onError(Exception e) {
                Log.i("Verification", "Network Not Found");
            }
        });
    }
}