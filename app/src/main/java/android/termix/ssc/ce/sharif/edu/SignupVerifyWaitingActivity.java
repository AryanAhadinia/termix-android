package android.termix.ssc.ce.sharif.edu;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.termix.ssc.ce.sharif.edu.loader.MySelectionsLoader;
import android.termix.ssc.ce.sharif.edu.network.NetworkException;
import android.termix.ssc.ce.sharif.edu.network.tasks.VerifyAccountTask;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.airbnb.lottie.LottieAnimationView;

/**
 * @author AryanAhadinia
 * @author MohammadKashaniJabbari
 * @author ShahabHM
 * @since 1
 */
public class SignupVerifyWaitingActivity extends AppCompatActivity {
    private Animation animFadeOut, tickFadeIn, crossFadeIn;
    private LottieAnimationView cross, tick, clock;
    private TextView message;

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
        clock = findViewById(R.id.verify_account_wait_anim);
        tick = findViewById(R.id.verified_account_tick_anim);
        cross = findViewById(R.id.unapproved_account_cross_anim);
        message = findViewById(R.id.message);

        App.getExecutorService().execute(new VerifyAccountTask(appLinkData.toString()) {
            @Override
            public void onResult(Object o) {
                setUpFadeOutAnimation(true);
                setUpTickFadeInAnimation();
                message.setText("اکنون به صفحه اصلی هدایت می‌شوید");
                clock.startAnimation(animFadeOut);
            }

            @Override
            public void onException(NetworkException e) {
                setUpFadeOutAnimation(false);
                setUpCrossFadeInAnimation();
                message.setText(e.getMessage());
                clock.startAnimation(animFadeOut);
            }

            @Override
            public void onError(Exception e) {
                setUpFadeOutAnimation(false);
                setUpCrossFadeInAnimation();
                message.setText("اینترنت در دسترس نیست");
                clock.startAnimation(animFadeOut);
            }
        });
    }

    private void setUpCrossFadeInAnimation() {
        crossFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        crossFadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                MySelectionsLoader.getInstance().run();
                Intent intent = new Intent(SignupVerifyWaitingActivity.this,
                        LoginSignupActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    private void setUpTickFadeInAnimation() {
        tickFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        tickFadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                MySelectionsLoader.getInstance().run();
                Intent intent = new Intent(SignupVerifyWaitingActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    private void setUpFadeOutAnimation(boolean verified) {
        animFadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
        animFadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                clock.setVisibility(View.GONE);
                if (verified) {
                    tick.setVisibility(View.VISIBLE);
                    tick.startAnimation(tickFadeIn);
                    tick.playAnimation();
                } else {
                    cross.setVisibility(View.VISIBLE);
                    cross.startAnimation(crossFadeIn);
                    cross.playAnimation();
                }
            }
        });
    }
}