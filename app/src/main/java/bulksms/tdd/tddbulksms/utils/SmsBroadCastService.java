package bulksms.tdd.tddbulksms.utils;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;
import java.util.ArrayList;

import bulksms.tdd.tddbulksms.database.DbManager;
import bulksms.tdd.tddbulksms.model.InfoModel;

/**
 * Created by y34h1a on 2/26/17.
 */


public class SmsBroadCastService extends Service{
    ArrayList<InfoModel> infoModels = new ArrayList<>();
    SmsManager smsManager;
    private int timeInterval = 60000;
    private Handler mHandler;
    int position = 0;
    DbManager dbManager;

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        Toast.makeText(getApplicationContext(),"Service Created", Toast.LENGTH_SHORT).show();

        smsManager = SmsManager.getDefault();
        mHandler= new Handler();
        dbManager = new DbManager(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(intent != null){
            infoModels = dbManager.getAllPhones();
            Log.i("arif", infoModels.size()+"");
            startRepeatingTask();
        }
        return START_STICKY;
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try{

                if (position < infoModels.size()){
                    sendSms(infoModels.get(position).getMessage(), infoModels.get(position).getPhoneNumber());
                    position++;
                }
                else {
                    stopRepeatingTask();
                }

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

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }
}

