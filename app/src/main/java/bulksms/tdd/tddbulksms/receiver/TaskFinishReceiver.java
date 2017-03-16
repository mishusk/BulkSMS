package bulksms.tdd.tddbulksms.receiver;

import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

/**
 * Created by y34h1a on 3/1/17.
 */

public class TaskFinishReceiver extends WakefulBroadcastReceiver {
    private PowerManager mPowerManager;
    private PowerManager.WakeLock mWakeLock;
    @Override
    public void onReceive(Context context, Intent intent) {
        mPowerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        turnOnScreen();
        Intent wakeIntent = new Intent();

        wakeIntent.setClassName("bulksms.tdd.tddbulksms", "bulksms.tdd.tddbulksms.activity.TaskFinished");
        wakeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(wakeIntent);
    }


    public void turnOnScreen(){
        // turn on screen
        Log.v("ProximityActivity", "ON!");
        mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "tag");
        mWakeLock.acquire();
    }
}
