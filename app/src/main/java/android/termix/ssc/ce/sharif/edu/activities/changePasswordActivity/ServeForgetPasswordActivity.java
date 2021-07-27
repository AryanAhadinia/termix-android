package android.termix.ssc.ce.sharif.edu.activities.changePasswordActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.termix.ssc.ce.sharif.edu.App;
import android.termix.ssc.ce.sharif.edu.R;
import android.termix.ssc.ce.sharif.edu.activities.loginSignUpActivity.LoginSignupActivity;
import android.termix.ssc.ce.sharif.edu.activities.mainActivity.MainActivity;
import android.termix.ssc.ce.sharif.edu.loader.MySelectionsLoader;
import android.termix.ssc.ce.sharif.edu.network.CookieManager;
import android.termix.ssc.ce.sharif.edu.network.NetworkException;
import android.termix.ssc.ce.sharif.edu.network.tasks.ChangePasswordTask;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.textfield.TextInputEditText;

public class ServeForgetPasswordActivity extends AppCompatActivity {
    private Animation animFadeIn, animFadeOut;
    private ProgressBar progressBar;
    private Button changePasswordButton;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        setContentView(R.layout.activity_change_password);

        progressBar = findViewById(R.id.progress_password);
        changePasswordButton = findViewById(R.id.change_password_button);

        ConstraintLayout constraintLayout = findViewById(R.id.change_password_layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(0);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();
        setUpButtonAnimations();

        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
        if (appLinkAction != null) {
            CookieManager.getInstance().setCookie(appLinkData.getHost(), appLinkData.getLastPathSegment());
        }

        Handler handler = new Handler();
        TextInputEditText password = findViewById(R.id.new_password);

        changePasswordButton.setOnClickListener(v -> {
//            hideKeyboard(this);
            changePasswordButton.startAnimation(animFadeOut);
            String passwordText = password.getText().toString();
            if (!LoginSignupActivity.isPasswordValid(passwordText)) {
                handler.post(() -> {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "رایانامه معتبر نیست.", Toast.LENGTH_SHORT);
                    toast.show();
                });
            }
            App.getExecutorService().execute(new ChangePasswordTask(passwordText) {
                @Override
                public void onResult(Object o) {
                    handler.post(() -> {
                        Toast toast;
                        if (isTaskRoot()) {
                            toast = Toast.makeText(getBaseContext(),
                                    "گذرواژه دگرگون شد، اکنون به صفحه اصلی هدایت می‌شوید",
                                    Toast.LENGTH_LONG);
                            MySelectionsLoader.getInstance().run();
                            Intent intent = new Intent(ServeForgetPasswordActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                    Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else {
                            toast = Toast.makeText(getBaseContext(),
                                    "گذرواژه دگرگون شد.",
                                    Toast.LENGTH_LONG);
                        }
                        toast.show();
                    });
                    changePasswordButton.startAnimation(animFadeIn);
                }

                @Override
                public void onException(NetworkException e) {
                    handler.post(() -> {
                        Toast toast = Toast.makeText(getApplicationContext(), e.getMessage(),
                                Toast.LENGTH_SHORT);
                        toast.show();
                    });
                    changePasswordButton.startAnimation(animFadeIn);
                }

                @Override
                public void onError(Exception e) {
                    handler.post(() -> {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "اینترنت در دسترس نیست.", Toast.LENGTH_SHORT);
                        toast.show();
                    });
                    changePasswordButton.startAnimation(animFadeIn);
                }
            });
        });
    }

    private void setUpFadeInAnimation() {
        animFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        animFadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {
                changePasswordButton.setAlpha(1);
                progressBar.setAlpha(0);
                changePasswordButton.setClickable(true);
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
            }
        });
    }

    private void setUpFadeOutAnimation() {
        animFadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        animFadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {
                progressBar.setAlpha(1);
                changePasswordButton.setClickable(false);
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                changePasswordButton.setAlpha(0);
            }
        });
    }

    private void setUpButtonAnimations() {
        setUpFadeInAnimation();
        setUpFadeOutAnimation();
    }
}
