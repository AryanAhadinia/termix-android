package android.termix.ssc.ce.sharif.edu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.termix.ssc.ce.sharif.edu.network.NetworkException;
import android.termix.ssc.ce.sharif.edu.network.tasks.GetAllCoursesTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new GetAllCoursesTask() {
            @Override
            public void onResult(Object o) {
                System.out.println(o);
            }

            @Override
            public void onError(Exception e) {
                System.out.println(e);
            }

            @Override
            public void onUnexpected(NetworkException e) {
                System.out.println(e);
            }
        }.run();
    }
}