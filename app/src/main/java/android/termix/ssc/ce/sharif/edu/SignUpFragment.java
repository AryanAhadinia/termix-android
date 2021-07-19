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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Pattern;

public class SignUpFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.sign_up_fragment, container, false);
        TextInputEditText emailText = root.findViewById(R.id.email_new);
        TextInputEditText passwordText = root.findViewById(R.id.password_new);
        TextInputEditText repeatPasswordText = root.findViewById(R.id.password_repeat);
        Button signUpButton = root.findViewById(R.id.signup);
        CheckBox checkBox = root.findViewById(R.id.checkBox);

        emailText.setOnFocusChangeListener(LoginSignupActivity.getEditTextFocusChangeListener());
        passwordText.setOnFocusChangeListener(LoginSignupActivity.getEditTextFocusChangeListener());
        repeatPasswordText.setOnFocusChangeListener(LoginSignupActivity
                .getEditTextFocusChangeListener());

        Handler handler = new Handler();

        signUpButton.setOnClickListener(v -> {
            String email = emailText.getText().toString();
            String password = passwordText.getText().toString();
            String repeatPassword = repeatPasswordText.getText().toString();
            if (!Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-" +
                    "]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\" +
                    "[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])" +
                    "?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][" +
                    "0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[" +
                    "a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f" +
                    "]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])").matcher(email).matches()) {
                Toast toast = Toast.makeText(requireContext(), "رایانامه معتبر نیست.",
                        Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
            if (password.length() < 8 || password.length() > 32) {
                Toast toast = Toast.makeText(requireContext(), "گذرواژه باید بین ۸ تا ۳۲ " +
                        "نویسه باشد.", Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
            if (!repeatPassword.equals(password)) {
                Toast toast = Toast.makeText(requireContext(), "گذرواژه و تکرارش برابر نیستند.",
                        Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
            if (!checkBox.isChecked()) {
                Toast toast = Toast.makeText(requireContext(), "لطفا با شرایط استقاده از برنامه موافقت کنید.",
                        Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
            new SignUpTask(email, password) {
                @Override
                public void onResult(Account o) {
                    LoginSignupActivity loginSignupActivity = LoginSignupActivity.getLoginSignupActivityWeakReference().get();
                    if (loginSignupActivity != null) {
                        loginSignupActivity.goToMainActivity();
                    }
                }

                @Override
                public void onException(NetworkException e) {
                    Log.e("SIGN_IN_EXCEPT: ", e.getMessage());
                    handler.post(() -> {
                        Toast toast = Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT);
                        toast.show();
                    });
                }

                @Override
                public void onError(Exception e) {
                    Log.e("SIGN_IN_ERROR: ", e.getMessage());
                    handler.post(() -> {
                        Toast toast = Toast.makeText(requireContext(), "اینترنت در دسترس نیست.", Toast.LENGTH_SHORT);
                        toast.show();
                    });
                }
            }.run();
        });
        return root;
    }
}