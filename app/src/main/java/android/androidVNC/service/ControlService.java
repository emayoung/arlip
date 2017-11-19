package android.androidVNC.service;

import android.androidVNC.ui.ControlPhoneActivity;
import android.app.ActivityManager;
import android.app.IntentService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;
/**
 * Created by Bless on 10/13/2017.
 */

public class ControlService extends IntentService {

    // this service is responsible for starting the starting the screen that puts the user shows that control mode has been activated

    public ControlService(){
        super("ControlService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent){

        Log.e("TAG", "eneterd on handle intent part");
        Intent startAppIntent = new Intent(getApplicationContext(), ControlPhoneActivity.class);
        startAppIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startAppIntent);
        stopSelf();


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


}
