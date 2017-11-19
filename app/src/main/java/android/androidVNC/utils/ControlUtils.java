package android.androidVNC.utils;

import android.androidVNC.service.ControlService;
import android.androidVNC.ui.MainActivity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


/**
 * Created by Bless on 10/15/2017.
 */

public class ControlUtils {

    public static void startControlCheck(Context context){
        Log.e("TAG", "control service about to be started");
        Intent intent = new Intent(context, ControlService.class);
        context.startService(intent);

    }

    public static void restartActivity(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);

    }
}
