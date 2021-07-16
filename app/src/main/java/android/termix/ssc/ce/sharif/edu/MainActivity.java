package android.termix.ssc.ce.sharif.edu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.termix.ssc.ce.sharif.edu.network.NetworkException;
import android.termix.ssc.ce.sharif.edu.network.tasks.GetAllCoursesTask;
import android.termix.ssc.ce.sharif.edu.network.tasks.SignUpTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new GetAllCoursesTask() {
            @Override
            public void onResult(Object o) {

            }

            @Override
            public void onException(NetworkException e) {

            }

            @Override
            public void onError(Exception e) {

            }
        }.run();
    }
}