package android.termix.ssc.ce.sharif.edu.activities.loginSignUpActivity.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.termix.ssc.ce.sharif.edu.activities.changePasswordActivity.ForgetPasswordRequestEmailActivity;
import android.termix.ssc.ce.sharif.edu.activities.loginSignUpActivity.LoginSignupActivity;
import android.termix.ssc.ce.sharif.edu.R;
import android.termix.ssc.ce.sharif.edu.model.Account;
import android.termix.ssc.ce.sharif.edu.network.NetworkException;
import android.termix.ssc.ce.sharif.edu.network.tasks.SignInTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

public class LoginFragment extends Fragment {
    private TextInputEditText emailText;
    private TextInputEditText passwordText;
    private Button loginButton;
    private ProgressBar progressBar;
    private View forgetPasswordTV;
    private Animation animFadeIn, animFadeOut;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.login_fragment, container, false);

        emailText = root.findViewById(R.id.email);
        passwordText = root.findViewById(R.id.password);
        loginButton = root.findViewById(R.id.login);
        progressBar = root.findViewById(R.id.progress_login);
        forgetPasswordTV = root.findViewById(R.id.forget_password_textview);
        setUpButtonAnimations();

        emailText.setOnFocusChangeListener(LoginSignupActivity.getEditTextFocusChangeListener());
        passwordText.setOnFocusChangeListener(LoginSignupActivity.getEditTextFocusChangeListener());

        setUpLoginButton();
        setUpForgetPasswordTV();
        return root;
    }

    private void setUpFadeInAnimation() {
        animFadeIn = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in);
        animFadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {
                loginButton.setAlpha(1);
                progressBar.setAlpha(0);
                loginButton.setClickable(true);
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
        animFadeOut = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_out);
        animFadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {
                progressBar.setAlpha(1);
                loginButton.setClickable(false);
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                loginButton.setAlpha(0);
            }
        });
    }

    private void setUpButtonAnimations() {
        setUpFadeInAnimation();
        setUpFadeOutAnimation();
    }



    private void makeToastAndStartFadeIn(String message) {
        Toast toast = Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT);
        toast.show();
        loginButton.startAnimation(animFadeIn);
    }

    private void setUpLoginButton() {
        Handler handler = new Handler();

        loginButton.setOnClickListener(v -> {
            loginButton.startAnimation(animFadeOut);
            String email = emailText.getText().toString();
            String password = passwordText.getText().toString();

            if (!LoginSignupActivity.isEmailValid(email)) {
                makeToastAndStartFadeIn("رایانامه معتبر نیست.");
                return;
            }

            if (!LoginSignupActivity.isPasswordValid(password)) {
                makeToastAndStartFadeIn("گذرواژه باید بین ۸ تا ۳۲ نویسه باشد.");
                return;
            }

            runSignInTask(email, password, handler);
        });
    }

    private void setUpForgetPasswordTV(){
        forgetPasswordTV.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ForgetPasswordRequestEmailActivity.class);
            startActivity(intent);
        });
    }

    private void runSignInTask(String email, String password, Handler handler) {
        new SignInTask(email, password) {
            @Override
            public void onResult(Account o) {
                LoginSignupActivity loginSignupActivity = LoginSignupActivity
                        .getLoginSignupActivityWeakReference().get();
                if (loginSignupActivity != null) {
                    loginSignupActivity.goToMainActivity();
                }
            }

            @Override
            public void onException(NetworkException e) {
                Log.e("SIGN_IN_EXCEPT: ", e.getMessage());
                handler.post(() -> makeToastAndStartFadeIn(e.getMessage()));
            }

            @Override
            public void onError(Exception e) {
                Log.e("SIGN_IN_ERROR: ", e.getMessage());
                handler.post(() -> makeToastAndStartFadeIn("اینترنت در دسترس نیست."));
            }
        }.run();
    }
}