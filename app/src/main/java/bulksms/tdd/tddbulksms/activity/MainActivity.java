package bulksms.tdd.tddbulksms.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import bulksms.tdd.tddbulksms.R;
import bulksms.tdd.tddbulksms.model.InfoModel;
import bulksms.tdd.tddbulksms.utils.SMSThread;


public class MainActivity extends AppCompatActivity {
    ArrayList<InfoModel> infoModelArrayList;
    InfoModel infoModel;
    SMSThread smsThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms);
        infoModel = new InfoModel();
        infoModelArrayList = new ArrayList<>();
        infoModel = new InfoModel("a", "sms 1", "5556");
        infoModelArrayList.add(infoModel);
        infoModel = new InfoModel("a", "sms 2", "5556");
        infoModelArrayList.add(infoModel);
        infoModel = new InfoModel("a", "sms 3", "5556");
        infoModelArrayList.add(infoModel);
        infoModel = new InfoModel("a", "sms 4", "5556");
        infoModelArrayList.add(infoModel);
        infoModel = new InfoModel("a", "sms 5", "5556");
        infoModelArrayList.add(infoModel);

        smsThread = new SMSThread();
        smsThread.setArraySMSThread(infoModelArrayList);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void bt_sendSms(View view) {
        //todo: run thread here
        smsThread.run();
        Toast.makeText(this, "click start thread", Toast.LENGTH_SHORT).show();
    }

    public void bt_stopTheThread(View view) {
        smsThread.cancel();
        Toast.makeText(this, "click stop thread", Toast.LENGTH_SHORT).show();

    }
}
