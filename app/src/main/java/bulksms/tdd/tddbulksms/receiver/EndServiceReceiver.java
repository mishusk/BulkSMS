package bulksms.tdd.tddbulksms.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import bulksms.tdd.tddbulksms.service.SmsBroadCastService;

/**
 * Created by y34h1a on 3/15/17.
 */

public class EndServiceReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intentEndService = new Intent(context, SmsBroadCastService.class);
        context.stopService(intentEndService);
    }
}