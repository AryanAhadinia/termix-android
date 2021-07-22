package android.termix.ssc.ce.sharif.edu;

import android.os.Bundle;
import android.os.Handler;
import android.termix.ssc.ce.sharif.edu.model.Account;
import android.termix.ssc.ce.sharif.edu.network.NetworkException;
import android.termix.ssc.ce.sharif.edu.network.tasks.SignUpTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.textfield.TextInputEditText;

public class SignUpFragment extends Fragment {
    TextInputEditText emailText;
    TextInputEditText passwordText;
    Button signUpButton;
    CheckBox checkBox;
    ProgressBar progressBar;
    Animation animFadeOut, animFadeIn, layoutFadeOut;
    LinearLayout linearLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.sign_up_fragment, container, false);

        emailText = root.findViewById(R.id.email_new);
        passwordText = root.findViewById(R.id.password_new);
        signUpButton = root.findViewById(R.id.signup);
        checkBox = root.findViewById(R.id.checkBox);
        progressBar = root.findViewById(R.id.progress_signup);

        emailText.setOnFocusChangeListener(LoginSignupActivity.getEditTextFocusChangeListener());
        passwordText.setOnFocusChangeListener(LoginSignupActivity.getEditTextFocusChangeListener());

        layoutFadeOut = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_out_fast);
        linearLayout = root.findViewById(R.id.linearLayout2);
        LottieAnimationView sentEmail = root.findViewById(R.id.email_sent_anim);
        TextView message = root.findViewById(R.id.signup_message);
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

        setUpButtonAnimations();
        setUpSignUpButton();
        return root;
    }

    private void setUpButtonAnimations() {
        setUpFadeInAnimation();
        setUpFadeOutAnimation();
    }

    private void setUpFadeInAnimation() {
        animFadeIn = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in);
        animFadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {
                signUpButton.setAlpha(1);
                progressBar.setAlpha(0);
                signUpButton.setClickable(true);
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
                signUpButton.setClickable(false);
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                signUpButton.setAlpha(0);
            }
        });
    }

    private void makeToastAndStartFadeIn(String message) {
        Toast toast = Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT);
        toast.show();
        signUpButton.startAnimation(animFadeIn);
    }

    private void runSignUpTask(String email, String password, Handler handler) {
        new SignUpTask(email, password) {
            @Override
            public void onResult(Account o) {
                linearLayout.startAnimation(layoutFadeOut);
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

    private void setUpSignUpButton() {
        Handler handler = new Handler();

        signUpButton.setOnClickListener(v -> {
            signUpButton.startAnimation(animFadeOut);
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
            if (!checkBox.isChecked()) {
                makeToastAndStartFadeIn("لطفا با شرایط استقاده از برنامه موافقت کنید.");
                return;
            }
            runSignUpTask(email, password, handler);
        });
    }
}