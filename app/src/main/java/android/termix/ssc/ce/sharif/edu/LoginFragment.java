package android.termix.ssc.ce.sharif.edu;

import android.os.Bundle;
import android.os.Handler;
import android.termix.ssc.ce.sharif.edu.model.Account;
import android.termix.ssc.ce.sharif.edu.network.NetworkException;
import android.termix.ssc.ce.sharif.edu.network.tasks.SignInTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

public class LoginFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.login_fragment, container, false);

        TextInputEditText emailText = root.findViewById(R.id.email);
        TextInputEditText passwordText = root.findViewById(R.id.password);
        Button loginButton = root.findViewById(R.id.login);

        emailText.setOnFocusChangeListener(LoginSignupActivity.getEditTextFocusChangeListener());
        passwordText.setOnFocusChangeListener(LoginSignupActivity.getEditTextFocusChangeListener());

        Handler handler = new Handler();

        loginButton.setOnClickListener(v -> {
            String email = emailText.getText().toString();
            String password = passwordText.getText().toString();
            new SignInTask(email, password) {
                @Override
                public void onResult(Account o) {
                    LoginSignupActivity loginSignupActivity = LoginSignupActivity.getLoginSignupActivityWeakReference().get();
                    if (loginSignupActivity != null) {
                        loginSignupActivity.goToMainActivity();
                    }
                }

                @Override
                public void onException(NetworkException e) {
                    handler.post(() -> {
                        Toast toast = Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT);
                        toast.show();
                    });
                }

                @Override
                public void onError(Exception e) {
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