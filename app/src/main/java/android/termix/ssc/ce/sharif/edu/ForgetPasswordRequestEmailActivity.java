package android.termix.ssc.ce.sharif.edu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.termix.ssc.ce.sharif.edu.network.NetworkException;
import android.termix.ssc.ce.sharif.edu.network.tasks.RequestForgetPasswordTask;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

public class ForgetPasswordRequestEmailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_forget_pass_request_email);

        ConstraintLayout constraintLayout = findViewById(R.id.forget_password_layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(0);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();

        TextInputEditText email = findViewById(R.id.email_new);
        Button requestSend = findViewById(R.id.request_send_email);

        requestSend.setOnClickListener(v -> {
            String emailText = email.getText().toString();
            if (!LoginSignupActivity.isEmailValid(emailText)) {
                Log.i("ForgetPass", "Email is not valid");
                // TODO: toast
            }
            App.getExecutorService().execute(new RequestForgetPasswordTask(emailText) {
                @Override
                public void onResult(Object o) {
                    // TODO
                    System.out.println("email sent");
                }

                @Override
                public void onException(NetworkException e) {
                    // TODO: toast
                    System.out.println("ex sent");
                }

                @Override
                public void onError(Exception e) {
                    // TODO: toast
                    System.out.println("err sent");
                }
            });
        });
    }
}