package android.termix.ssc.ce.sharif.edu.alarm;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.termix.ssc.ce.sharif.edu.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AlarmActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_alarm);
    }
}
