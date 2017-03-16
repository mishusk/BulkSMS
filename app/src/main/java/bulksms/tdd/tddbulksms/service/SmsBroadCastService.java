package bulksms.tdd.tddbulksms.service;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telephony.SmsManager;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


import bulksms.tdd.tddbulksms.constant.Constant;
import bulksms.tdd.tddbulksms.database.DbManager;
import bulksms.tdd.tddbulksms.model.InfoModel;
import bulksms.tdd.tddbulksms.receiver.DeliverReceiver;
import bulksms.tdd.tddbulksms.receiver.SentReceiver;
import bulksms.tdd.tddbulksms.receiver.TaskFinishReceiver;
import bulksms.tdd.tddbulksms.sharedpref.SharedPref;

/**
 * Created by y34h1a on 2/26/17.
 */


public class SmsBroadCastService extends Service{
    ArrayList<InfoModel> infoModels = new ArrayList<>();
    SmsManager smsManager;
    private int timeInterval = 8000;
    private Handler mHandler;
    DbManager dbManager;
    SharedPref sharedPref;
    BroadcastReceiver sendBroadcastReceiver;
    BroadcastReceiver deliveryBroadcastReciever;
    BroadcastReceiver taskFinishReceiver;
    Timer timer;

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }


    @Override
    public void onCreate()
    {
        smsManager = SmsManager.getDefault();
        mHandler= new Handler();
        dbManager = new DbManager(getApplicationContext());
        sharedPref = new SharedPref(getApplicationContext());
        sendBroadcastReceiver = new SentReceiver();
        deliveryBroadcastReciever = new DeliverReceiver();
        taskFinishReceiver = new TaskFinishReceiver();
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        timer = new Timer();
        Toast.makeText(getApplicationContext(), "Service Started", Toast.LENGTH_SHORT).show();
        String operator = sharedPref.getOperator();

        if (operator.equals(Constant.ALL_PHONE)){
            infoModels = dbManager.getSmsInfo();
        }else {
            infoModels = dbManager.getPhnByOperatorSmSinfo(operator);
        }

        if (infoModels.size() > 0)
            startService();

        return START_STICKY;
    }

    public void sendSms(String smsBody, String phoneNumber) {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        Intent intentSend = new Intent(SENT);
        intentSend.putExtra(Constant.POSITION, phoneNumber);

        Intent intentDelivered = new Intent(DELIVERED);
        intentDelivered.putExtra(Constant.POSITION, phoneNumber);

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, intentSend, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, intentDelivered, PendingIntent.FLAG_UPDATE_CURRENT);

        registerReceiver(sendBroadcastReceiver, new IntentFilter(SENT));
        registerReceiver(deliveryBroadcastReciever, new IntentFilter(DELIVERED));
        try{
            smsManager.sendTextMessage(phoneNumber, null, smsBody, sentPI, deliveredPI);
        }catch (IllegalArgumentException e){

        }

    }

    private void startService()
    {
        timer.scheduleAtFixedRate(new mainTask(), 5000, timeInterval);
    }

    private class mainTask extends TimerTask
    {
        public void run()
        {
            toastHandler.sendEmptyMessage(0);
        }
    }


    private final Handler toastHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {

            int position = sharedPref.getCurrentPosition();
            String message = sharedPref.getMessage();
            if(position < infoModels.size()){
                String phoneNumber = infoModels.get(position).getPhoneNumber();
                sendSms(message, phoneNumber);
                sharedPref.setCurrentPosition(position + 1);
            }
            else {

                sharedPref.setCurrentPosition(0);
                infoModels.clear();
                stopRepeatingTask();

            }
        }
    };

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        Toast.makeText(getApplicationContext(), "Service Deleted", Toast.LENGTH_SHORT).show();
        super.onDestroy();
        try {

            unregisterReceiver(sendBroadcastReceiver);
            unregisterReceiver(deliveryBroadcastReciever);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    void stopRepeatingTask(){
        timer.cancel();
        Intent intent = new Intent(getApplicationContext(),TaskFinishReceiver.class);
        sendBroadcast(intent);
    }
}