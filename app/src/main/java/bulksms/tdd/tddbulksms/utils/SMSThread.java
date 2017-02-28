package bulksms.tdd.tddbulksms.utils;

import android.telephony.SmsManager;
import android.util.Log;

import java.util.ArrayList;

import bulksms.tdd.tddbulksms.model.InfoModel;

/**
 * Created by mishu on 2/26/2017.
 */

public class SMSThread extends Thread {
    private ArrayList<InfoModel> userArrayList;
    private SmsManager manager;
    private boolean flag = true;

    public void SMSThread() {
        manager = SmsManager.getDefault();
    }

    public void setArraySMSThread(ArrayList<InfoModel> userArrayList) {
        manager = SmsManager.getDefault();
        this.userArrayList = userArrayList;
    }

    @Override
    public void run() {

        try {
//            while (!Thread.currentThread().isInterrupted()) {
//            while (flag) {
                // ...
                for (int i = 0; i < userArrayList.size(); i++) {
                    // Pause for 4 seconds
                    try {
                        Thread.sleep(2000);
                        sendSms(userArrayList.get(i).getMessage(), userArrayList.get(i).getPhoneNumber());
                        Log.e("in_thread", "in run position " + i);
                    } catch (InterruptedException e) {
                        // We've been interrupted: no more messages.
                        return;
                    }
                    if (i==userArrayList.size())
                        flag = false;
                }
//            }
        } catch (Exception e){
            e.printStackTrace();
        }
      /* Allow thread to exit */

    }

    public void sendSms(String smsBody, String phoneNumber) {
        manager.sendTextMessage(phoneNumber, null, smsBody, null, null);
    }

    @Override
    public void interrupt() {
        super.interrupt();
    }

    public void cancel() {
        interrupt();
    }

    @Override
    public boolean isInterrupted() {
        return super.isInterrupted();
    }
}
