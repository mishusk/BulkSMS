package bulksms.tdd.tddbulksms.utils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import bulksms.tdd.tddbulksms.constant.Constant;
import bulksms.tdd.tddbulksms.model.InfoModel;

/**
 * Created by mishu on 2/25/2017.
 */

public class SMSBroadCastRcvr extends BroadcastReceiver {
    ArrayList<InfoModel> infoModels = new ArrayList<>();
    SmsManager smsManager;
    private int timeInterval = 4000;
    private Handler mHandler;
    int position = 0;
    BroadcastReceiver sendBroadcastReceiver;
    BroadcastReceiver deliveryBroadcastReciever;



    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Service Started", Toast.LENGTH_SHORT).show();

        smsManager = SmsManager.getDefault();
        mHandler=new Handler();
        String message = null;

        Log.e("sms", "onReceive: " + message);
        Toast.makeText(context, "show " + message, Toast.LENGTH_SHORT).show();
        startRepeatingTask();
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try{


                Log.i("arif","counting....");
                /*if (position < infoModels.size()){
                    sendSms(infoModels.get(position).getMessage(), infoModels.get(position).getPhoneNumber());
                    position++;
                }
                else {
                    stopRepeatingTask();
                }*/

            }finally {
                mHandler.postDelayed(mStatusChecker,timeInterval);
            }
        }
    };

    void startRepeatingTask(){
        mStatusChecker.run();
    }

    void stopRepeatingTask(){
        mHandler.removeCallbacks(mStatusChecker);
    }

    public void sendSms(String smsBody, String phoneNumber) {
        smsManager.sendTextMessage(phoneNumber, null, smsBody, null, null);
    }
}
