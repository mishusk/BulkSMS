package bulksms.tdd.tddbulksms.utils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by mishu on 2/25/2017.
 */

public class SMSBroadCastRcvr extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        String message = null;

        switch (getResultCode()) {
            case Activity.RESULT_OK:
                message = "Message sent!";
                break;
            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                message = "Error. Message not sent.";
                break;
            case SmsManager.RESULT_ERROR_NO_SERVICE:
                message = "Error: No service.";
                break;
            case SmsManager.RESULT_ERROR_NULL_PDU:
                message = "Error: Null PDU.";
                break;
            case SmsManager.RESULT_ERROR_RADIO_OFF:
                message = "Error: Radio off.";
                break;
        }

        Log.e("sms", "onReceive: " + message);
        Toast.makeText(context, "show " + message, Toast.LENGTH_SHORT).show();
    }
}
