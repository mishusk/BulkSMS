package bulksms.tdd.tddbulksms.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

import bulksms.tdd.tddbulksms.R;
import bulksms.tdd.tddbulksms.adapter.DeliverAdapter;
import bulksms.tdd.tddbulksms.database.DbManager;
import bulksms.tdd.tddbulksms.model.InfoModel;
import bulksms.tdd.tddbulksms.receiver.EndServiceReceiver;
import bulksms.tdd.tddbulksms.receiver.StartServiceReciver;
import bulksms.tdd.tddbulksms.sharedpref.SharedPref;

public class TaskFinished extends AppCompatActivity {
    TextView tvSend, tvDelivered;
    SharedPref sharedPref;
    RecyclerView rvDeliverd;
    ArrayList<InfoModel> deleverdInfo = new ArrayList<>();
    DbManager database;
    DeliverAdapter deliverAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_finished);
        initView();
        setAdapter();
    }


    private void initView() {
        tvSend = (TextView) findViewById(R.id.tvSent);
        tvDelivered = (TextView) findViewById(R.id.tvDeliverd);
        sharedPref = new SharedPref(getApplicationContext());
        database = new DbManager(getApplicationContext());
        deliverAdapter = new DeliverAdapter(database.getDeliveredPhn());
        rvDeliverd = (RecyclerView) findViewById(R.id.rvDeliveredlist);

        tvSend.setText("SENT: " + sharedPref.getSentCount() + "");
        tvDelivered.setText("DELIVERED: " + sharedPref.getDeliveredCount() + "");
        initListener();
    }

    private void initListener() {

    }

    public void setAdapter(){

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setAutoMeasureEnabled(true);
        rvDeliverd.setLayoutManager(linearLayoutManager);

        rvDeliverd.setAdapter(deliverAdapter);


        //End  broadcast receivers
        Intent startIntent = new Intent(getApplicationContext(),StartServiceReciver.class);
        Intent endIntent = new Intent(getApplicationContext(),EndServiceReceiver.class);
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntentStart = PendingIntent.getBroadcast(getApplicationContext(), sharedPref.getStartAlarmId(), startIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntentEnd = PendingIntent.getBroadcast(getApplicationContext(), sharedPref.getEndAlarmId(), endIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        am.cancel(pendingIntentStart);
        am.cancel(pendingIntentEnd);
        pendingIntentStart.cancel();
        pendingIntentEnd.cancel();

        ArrayList<InfoModel> undeliverdPhones = database.getRecentUndeliveredPhn();

        for (int i = 0; i< undeliverdPhones.size();i++){
            database.setUndeliveredPhone(undeliverdPhones.get(i));
        }

        database.cleanSmSinfoTable();
    }

}
