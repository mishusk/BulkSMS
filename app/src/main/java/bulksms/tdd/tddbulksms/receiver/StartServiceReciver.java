package bulksms.tdd.tddbulksms.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import bulksms.tdd.tddbulksms.service.SmsBroadCastService;

/**
 * Created by y34h1a on 3/15/17.
 */

public class StartServiceReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intentStartService = new Intent(context, SmsBroadCastService.class);
        context.startService(intentStartService);
    }
}
