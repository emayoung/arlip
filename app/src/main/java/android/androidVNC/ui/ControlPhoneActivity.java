package android.androidVNC.ui;

import android.androidVNC.Helpers.HomeWatcher;
import android.androidVNC.R;
import android.androidVNC.callbacks.OnHomePressedListener;
import android.androidVNC.service.ControlService;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import java.util.List;


public class ControlPhoneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_phone);


        if (Build.VERSION.SDK_INT >= 21)
            getWindow().setStatusBarColor(Color.TRANSPARENT);

        setHomeWatcher(this);
    }
    public void setHomeWatcher(final Context context){
        HomeWatcher mHomeWatcher = new HomeWatcher(this);
        mHomeWatcher.setOnHomePressedListener(new OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                // do something here...
                Log.e("TAG", "home pressed");
                startAlarmManager(context);
            }
            @Override
            public void onHomeLongPressed() {
                Log.e("TAG", "long home pressed");
                startAlarmManager(context);
            }

        });
        mHomeWatcher.startWatch();
    }

    @Override
    protected void onUserLeaveHint() {
        //            ControlUtils.restartActivity(this);
//            ControlUtils.startControlCheck(this);
        startAlarmManager(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        onCreate(new Bundle());
        Log.e("TAG", "start control service from back pressed");
        if (!isAppActive()){
            //            ControlUtils.restartActivity(this);
//            ControlUtils.startControlCheck(this);
            startAlarmManager(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
//        onCreate(new Bundle());
        Log.e("TAG", "start control service from back pressed");
        if (!isAppActive()){
            startAlarmManager(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("TAG", "start control service from back pressed");
        if (!isAppActive()){

            startAlarmManager(this);
        }
    }

    @Override
    public void onBackPressed() {
//        onCreate(new Bundle());
        Log.e("TAG", "start control service from back pressed");
        if (!isAppActive()){
            startAlarmManager(this);
        }

    }

    public boolean isAppActive(){
        final ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(1);
        boolean isjobrunning = false;
        if (!tasks.isEmpty()) {

            ComponentName topActivity = tasks.get(0).topActivity;
            if (topActivity.getPackageName().equals("androiddegree.udacity.ememobong.controldistraction")) {

                return true;
            }
        }

        if (!isjobrunning){
//            app is not in background
            return false;
        }
        return false;
    }

    public static void startAlarmManager(Context context){
//start alarm for every 50mins when user is on the app
        Intent intent = new Intent(context, ControlService.class);
        PendingIntent pintent = PendingIntent.getService(context, 0, intent, 0);
        AlarmManager alarm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 100, pintent);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_HOME)
        {
            Log.e("Home Button","Clicked");
            startAlarmManager(this);
        }
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            startAlarmManager(this);
        }
        return false;
    }

}
