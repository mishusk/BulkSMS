package bulksms.tdd.tddbulksms.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import bulksms.tdd.tddbulksms.R;
import bulksms.tdd.tddbulksms.adapter.DeliverAdapter;
import bulksms.tdd.tddbulksms.database.DbManager;
import bulksms.tdd.tddbulksms.helper.HelperMethods;
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
    Button btDeliveredNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_finished);
        initView();
        setAdapter();
        new CleanAllServices().execute();
    }

    private void initView() {
        tvSend = (TextView) findViewById(R.id.tvSent);
        tvDelivered = (TextView) findViewById(R.id.tvDeliverd);
        sharedPref = new SharedPref(getApplicationContext());
        database = new DbManager(getApplicationContext());
        deliverAdapter = new DeliverAdapter(deleverdInfo);
        rvDeliverd = (RecyclerView) findViewById(R.id.rvDeliveredlist);
        btDeliveredNumber = (Button) findViewById(R.id.btCopyDeliveredNumber);

        tvSend.setText("SENT: " + sharedPref.getSentCount() + "");
        tvDelivered.setText("DELIVERED: " + sharedPref.getDeliveredCount() + "");
        initListener();
    }

    private void initListener() {
        btDeliveredNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<InfoModel> undeliveredNumbers = database.getDeliveredPhn();
                String numbers = "";
                for (int i = 0; i < undeliveredNumbers.size(); i++){
                    numbers  = numbers + undeliveredNumbers.get(i).getPhoneNumber() + ",";
                }

                copyText(numbers);
            }
        });
    }


    private void copyText(String text){
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "Numbers Copied", Toast.LENGTH_SHORT).show();
    }

    public void setAdapter(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setAutoMeasureEnabled(true);
        rvDeliverd.setLayoutManager(linearLayoutManager);
        rvDeliverd.setAdapter(deliverAdapter);
    }

    private class CleanAllServices extends AsyncTask<String, String, Boolean> {
        ProgressDialog progressDialog = HelperMethods.getProgressBar(getDialogContext());

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            HelperMethods.startProgressBar(progressDialog,"Please Wait.....");
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            deleverdInfo = database.getDeliveredPhn();
            deliverAdapter.reAddMatchedRide(deleverdInfo);
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

            ArrayList<InfoModel> undeliverdPhones = database.getTemRecentUndeliveredPhn();

            for (int i = 0; i< undeliverdPhones.size();i++){
                database.setUndeliveredPhone(undeliverdPhones.get(i));
                database.setRecentUndeliveredPhone(undeliverdPhones.get(i));
            }
            database.cleanSmSinfoTable();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean isTaskFinished) {
            HelperMethods.stopProgressBar(progressDialog);
        }
    }


    private Context getDialogContext(){
        Context context;
        if(getParent() != null)
            context = getParent();
        else
            context = this;

        return context;
    }

}
