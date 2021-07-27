package android.termix.ssc.ce.sharif.edu.activities.changePasswordActivity;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.termix.ssc.ce.sharif.edu.App;
import android.termix.ssc.ce.sharif.edu.R;
import android.termix.ssc.ce.sharif.edu.activities.loginSignUpActivity.LoginSignupActivity;
import android.termix.ssc.ce.sharif.edu.network.NetworkException;
import android.termix.ssc.ce.sharif.edu.network.tasks.RequestForgetPasswordTask;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.textfield.TextInputEditText;

public class ForgetPasswordRequestEmailActivity extends AppCompatActivity {
    private Animation animFadeIn, animFadeOut, layoutFadeOut;
    private ProgressBar progressBar;
    private Button requestSend;
    private LinearLayout linearLayout;
    private LottieAnimationView sentEmail;
    private TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeRootFullScreen();
        setContentView(R.layout.activity_forget_pass_request_email);
        progressBar = findViewById(R.id.progress_forget);
        setUpButtonAnimations();
        setupBackgroundAnimation();

        sentEmail = findViewById(R.id.email_sent_anim);
        linearLayout = findViewById(R.id.formLayout);
        message = findViewById(R.id.forget_message);

        Handler handler = new Handler();

        layoutFadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out_fast);
        setSentEmailAnimation(handler);

        TextInputEditText email = findViewById(R.id.email_new);
        requestSend = findViewById(R.id.request_send_email);
        setupLayoutFadeOut();
        requestSend.setOnClickListener(v -> buttonFunction(handler, email));
    }

    public static void hideKeyboard(AppCompatActivity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void setUpFadeInAnimation() {
        animFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        animFadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {
                requestSend.setAlpha(1);
                progressBar.setAlpha(0);
                requestSend.setClickable(true);
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
                requestSend.setClickable(false);
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                requestSend.setAlpha(0);
            }
        });
    }

    private void setUpButtonAnimations() {
        setUpFadeInAnimation();
        setUpFadeOutAnimation();
    }

    private void makeRootFullScreen() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    private void setupBackgroundAnimation() {
        ConstraintLayout constraintLayout = findViewById(R.id.forget_password_layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(0);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();
    }

    private void setSentEmailAnimation(Handler handler) {
        sentEmail.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                handler.post(() -> {
                    Intent intent = new Intent(ForgetPasswordRequestEmailActivity.this, LoginSignupActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                });
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    private void checkEmail(String email, Handler handler) {
        if (!LoginSignupActivity.isEmailValid(email)) {
            handler.post(() -> {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "رایانامه معتبر نیست.", Toast.LENGTH_SHORT);
                toast.show();
            });
        }
    }

    private void makeToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }

    private void setupLayoutFadeOut() {
        layoutFadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                linearLayout.setVisibility(View.INVISIBLE);
                sentEmail.setVisibility(View.VISIBLE);
                sentEmail.playAnimation();
                message.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void buttonFunction(Handler handler, TextView email) {
        requestSend.startAnimation(animFadeOut);
        String emailText = email.getText().toString();
        checkEmail(emailText, handler);
        App.getExecutorService().execute(new RequestForgetPasswordTask(emailText) {
            @Override
            public void onResult(Object o) {
                handler.post(() -> linearLayout.startAnimation(layoutFadeOut));
            }

            @Override
            public void onException(NetworkException e) {
                handler.post(() -> makeToast(e.getMessage()));
                requestSend.startAnimation(animFadeIn);
            }

            @Override
            public void onError(Exception e) {
                handler.post(() -> makeToast("اینترنت در دسترس نیست."));
                requestSend.startAnimation(animFadeIn);
            }
        });
    }
}