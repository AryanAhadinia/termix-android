package android.termix.ssc.ce.sharif.edu.activities;
//Reminder Activity
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.termix.ssc.ce.sharif.edu.R;
import android.termix.ssc.ce.sharif.edu.alarm.Alarm;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class AlarmActivity extends AppCompatActivity {
    static String TAG = "homo alarm : AlarmActivity:";
    MediaPlayer mediaPlayer;
    Alarm alarm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate: ____________________");
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: ");
        setContentView(R.layout.activity_alarm);

        setWindowFlags();
        setUpLabels();
        setUpButtons();
        setUpBackGround();
        setUpMediaPlayer();
        playMedia();
        int id = getIntent().getIntExtra("alarm_id", -1); // todo
    }

    private void setUpBackGround() {
        ConstraintLayout constraintLayout = findViewById(R.id.root_layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(0);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();
    }

    private void setUpLabels() {
        TextView title = findViewById(R.id.course_name);
        title.setText("نام درس");
        TextView instructor = findViewById(R.id.course_instructor);
        instructor.setText("استاد");
        TextView time = findViewById(R.id.course_time);
        time.setText("زمان شروع");
    }

    private void setWindowFlags() {
        //todo clean the deprecated ones
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD,
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED,
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    }

    private void setUpButtons() {
        Button endAlarm = findViewById(R.id.end_alarm);
        endAlarm.setOnClickListener((onClickListener)->{
            finishActivity();
        });
    }

    private void setUpMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(
                new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .build()
        );
    }

    private void playMedia(){
        Uri myUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM); //default alarm sound
        try{
            mediaPlayer.setDataSource(getApplicationContext(), myUri);
            mediaPlayer.prepare();
            mediaPlayer.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    void destroyEffects(){
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finishActivity();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        finishActivity();
    }

    void finishActivity(){
        scheduleNextAlarm(alarm);
        destroyEffects();
        finish();
        overridePendingTransition(0, 0);
    }

    private void scheduleNextAlarm(Alarm alarm) {
        //todo: set next alarm
    }
}
