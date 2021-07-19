package android.termix.ssc.ce.sharif.edu;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.os.Bundle;
import android.termix.ssc.ce.sharif.edu.network.NetworkException;
import android.termix.ssc.ce.sharif.edu.network.tasks.TestTokenTask;
import android.widget.ImageView;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        ImageView logo = findViewById(R.id.logo);
        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                logo,
                PropertyValuesHolder.ofFloat("scaleX", 1.2f),
                PropertyValuesHolder.ofFloat("scaleY", 1.2f));
        scaleDown.setDuration(400);
        scaleDown.setRepeatCount(ObjectAnimator.INFINITE);
        scaleDown.setRepeatMode(ObjectAnimator.REVERSE);
        scaleDown.start();
        Intent intent = new Intent(LoadingActivity.this, LoginSignupActivity.class);
        startActivity(intent);

        App.getExecutorService().execute(new TestTokenTask() {
            @Override
            public void onResult(Object o) {
                // Network Ok, Token OK
//                Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
//                startActivity(intent);

                //TODO

                Intent intent = new Intent(LoadingActivity.this, LoginSignupActivity.class);
                startActivity(intent);
            }

            @Override
            public void onException(NetworkException e) {
                // Network Ok, Login required
                Intent intent = new Intent(LoadingActivity.this, LoginSignupActivity.class);
                startActivity(intent);
            }

            @Override
            public void onError(Exception e) {
                // Network not responding
            }
        });
    }
}