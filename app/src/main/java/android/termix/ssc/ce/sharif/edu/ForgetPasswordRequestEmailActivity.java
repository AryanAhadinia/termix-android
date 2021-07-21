package android.termix.ssc.ce.sharif.edu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.termix.ssc.ce.sharif.edu.network.NetworkException;
import android.termix.ssc.ce.sharif.edu.network.tasks.RequestForgetPasswordTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

public class ForgetPasswordRequestEmailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass_request_email);

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