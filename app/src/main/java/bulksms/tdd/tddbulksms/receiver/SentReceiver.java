package bulksms.tdd.tddbulksms.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.widget.Toast;

import bulksms.tdd.tddbulksms.constant.Constant;
import bulksms.tdd.tddbulksms.sharedpref.SharedPref;

/**
 * Created by y34h1a on 3/1/17.
 */

public class SentReceiver extends BroadcastReceiver {
    SharedPref sharedPref;
    @Override
    public void onReceive(Context context, Intent intent) {
        sharedPref = new SharedPref(context);

        int sentCount  = sharedPref.getSentCount();

        switch (getResultCode()) {
            case Activity.RESULT_OK:
                Toast.makeText(context, "Sms Sent", Toast.LENGTH_SHORT).show();
                sharedPref.setSentCount(sentCount + 1);
                break;
            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                Toast.makeText(context, "Generic failure",
                        Toast.LENGTH_SHORT).show();
                break;
            case SmsManager.RESULT_ERROR_NO_SERVICE:
                Toast.makeText(context, "No service",
                        Toast.LENGTH_SHORT).show();
                break;
            case SmsManager.RESULT_ERROR_NULL_PDU:
                Toast.makeText(context, "Null PDU", Toast.LENGTH_SHORT)
                        .show();
                break;
            case SmsManager.RESULT_ERROR_RADIO_OFF:
                Toast.makeText(context, "Radio off",
                        Toast.LENGTH_SHORT).show();
                break;
        }

    }
}