package bulksms.tdd.tddbulksms.activity;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.text.IDNA;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import bulksms.tdd.tddbulksms.R;
import bulksms.tdd.tddbulksms.constant.Constant;
import bulksms.tdd.tddbulksms.database.DbManager;
import bulksms.tdd.tddbulksms.helper.HelperMethods;
import bulksms.tdd.tddbulksms.model.InfoModel;
import bulksms.tdd.tddbulksms.receiver.EndServiceReceiver;
import bulksms.tdd.tddbulksms.receiver.StartServiceReciver;
import bulksms.tdd.tddbulksms.sharedpref.SharedPref;
import bulksms.tdd.tddbulksms.service.SmsBroadCastService;
import fr.ganfra.materialspinner.MaterialSpinner;


public class MainActivity extends AppCompatActivity {
    EditText etTextBody;
    EditText etNumbers;
    Button btSendSms,btTextClear,btShowInfo,btCancelService;
    DbManager dbManager;
    SharedPref sharedPref;
    MaterialSpinner spOperator;
    AlarmManager alarmManager;
    int[] operatorNumber = new int[]{0,0,0,0,0,0};

    String phonesString ;
    String numbers[] ;
    String message ;
    String slectedOprator;
    ArrayList<InfoModel> numberInfos  = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms);
        initView();
    }

    private void initView() {
        etTextBody = (EditText) findViewById(R.id.et_text_body);
        etNumbers = (EditText) findViewById(R.id.etPhoneNumber);

        btSendSms = (Button) findViewById(R.id.bt_send_sms);
        btTextClear = (Button) findViewById(R.id.btTextClear);
        btShowInfo = (Button) findViewById(R.id.bt_show_info);
        btCancelService = (Button) findViewById(R.id.bt_cancel_service);

        dbManager = new DbManager(getApplicationContext());
        sharedPref = new SharedPref(getApplicationContext());
        spOperator = (MaterialSpinner) findViewById(R.id.spOperator);
        addOperatorSpinner();
        alarmManager =  (AlarmManager) getSystemService(ALARM_SERVICE);

        etTextBody.setText(sharedPref.getMessage());
        etNumbers.setText(sharedPref.getPhones());

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        initListener();
    }


    private void initListener() {
        btShowInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSuccesfull = generateGenerateInfos();

                if (isSuccesfull){
                    showDialog();
                }
            }
        });


        btSendSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HelperMethods.isMyServiceRunning(SmsBroadCastService.class,getApplicationContext())) {
                    Intent intentService = new Intent(getApplicationContext(), SmsBroadCastService.class);
                    stopService(intentService);
                }


                boolean isGenerateSuccessFul = generateGenerateInfos();
                if (isGenerateSuccessFul){
                    new SaveDataInDatabase().execute();
                }
            }
        });
        btTextClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etNumbers.setText("");
                phonesString = "";
            }
        });


        btCancelService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new StopSmsService().execute();
            }
        });
    }


    public boolean generateGenerateInfos(){
        long startTime = sharedPref.getKeyStartServiceTime();
        long endTime = sharedPref.getKeyEndServiceTime();

        phonesString = etNumbers.getText().toString();

        formatNumbers();

        message = etTextBody.getText().toString();

        //Selected Operator
        slectedOprator = spOperator.getSelectedItem().toString();
        operatorNumber[0] = numbers.length;

        if(!message.isEmpty() && !message.isEmpty() &&
                !slectedOprator.equals(Constant.OPRATOR_NOT_SELETED)){


            if (startTime == 0L && endTime == 0L){
                sharedPref.setMessage(message);
                sharedPref.setPhones(phonesString);
                Intent intent = new Intent(getApplicationContext(), Settings.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this, "Please Insert Time", Toast.LENGTH_LONG).show();
                return false;
            }else {

                for(String phoneNumber: numbers){
                    String operator = operatorFinder(phoneNumber);

                    if (operator.equals(Constant.GP))
                        operatorNumber[1] = operatorNumber[1] + 1;
                    if (operator.equals(Constant.AIRTEL))
                        operatorNumber[2] = operatorNumber[2] + 1;
                    if (operator.equals(Constant.TTALK))
                        operatorNumber[3] = operatorNumber[3] + 1;
                    if (operator.equals(Constant.ROBI))
                        operatorNumber[4] = operatorNumber[4] + 1;
                    if (operator.equals(Constant.BLINK))
                        operatorNumber[5] = operatorNumber[5] + 1;
                }
            }

        }else {
            Toast.makeText(getApplicationContext(), "Invalid Input", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public void saveDataInSharedPref(){

        sharedPref.setMessage(message);
        sharedPref.setPhones(phonesString);
        sharedPref.setOperator(slectedOprator);
        sharedPref.setCurrentPosition(0);
        sharedPref.setSentCount(0);
        sharedPref.setDeliveredCount(0);
        numberInfos.clear();

        for(String phoneNumber: numbers){

            InfoModel info = new InfoModel();
            info.setMessage(message);
            info.setOperatorName(operatorFinder(phoneNumber));
            info.setPhoneNumber(phoneNumber);
            info.setStatus(Constant.STATUS_NOT_DELIVERED);

            numberInfos.add(info);
            //dbManager.setPhoneInfo(info);
        }
    }


    public String operatorFinder(String phnNumber) {
        Log.i("arif","Before: " + phnNumber);
        phnNumber = phnNumber.substring(phnNumber.indexOf("0"));
        Log.i("arif","After: " + phnNumber);

        String operatorCode = phnNumber.substring(0, Math.min(phnNumber.length(), 3));

        switch (operatorCode) {

            case "017": {
                return Constant.GP;
            }


            case "016": {
                return Constant.AIRTEL;
            }

            case "015": {
                return Constant.TTALK;
            }

            case "018": {
                return Constant.ROBI;
            }

            case "019": {
                return Constant.BLINK;
            }

            case "555": {
                return Constant.EMULATOR;
            }
            default:
                return "";
        }
    }


    public void formatNumbers(){
        phonesString = phonesString.replace(" ","");
        numbers = phonesString.split("\\W+");

    }

    private void showDialog(){

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_operator_count);
        dialog.setTitle("");

        TextView tvAll, tvGp, tvRobi, tvBLink, tvTTalk, tvAirtel;
        Button btConfirm;

        tvAll = (TextView) dialog.findViewById(R.id.tvAll);
        tvGp = (TextView) dialog.findViewById(R.id.tvGp);
        tvRobi  = (TextView) dialog.findViewById(R.id.tvRobi);
        tvBLink = (TextView) dialog.findViewById(R.id.tvBlink);
        tvTTalk = (TextView) dialog.findViewById(R.id.tvTTalk);
        tvAirtel = (TextView) dialog.findViewById(R.id.tvAirtel);

        tvAll.setText("All: " + operatorNumber[0]);
        tvGp.setText("Gp: " + operatorNumber[1]);
        tvAirtel.setText("Airtel: " + operatorNumber[2]);
        tvTTalk.setText("Tale Talk: " + operatorNumber[3]);
        tvRobi.setText("Robi: " + operatorNumber[4]);
        tvBLink.setText("BLink: " + operatorNumber[5]);

        clearOperatorCounter();

        dialog.show();
    }

    private void startService(){

        long startTime = sharedPref.getKeyStartServiceTime();
        long endTime = sharedPref.getKeyEndServiceTime();

        Intent intent = new Intent(getApplicationContext(), SmsBroadCastService.class);
        startService(intent);

        /*PendingIntent pendingStartServiceIntent;
        PendingIntent pendingEndServiceIntent;

        Intent startIntent = new Intent(getApplicationContext(), StartServiceReciver.class);
        Intent endIntent = new Intent(getApplicationContext(), EndServiceReceiver.class);

        final int _id = (int) System.currentTimeMillis();
        final int idend = (int) System.currentTimeMillis();


        sharedPref.setKeyStartAlarmId(_id);
        sharedPref.setKeyEndAlarmId(idend);

        //myIntent.putExtra(Constant.TYPE, type);
        pendingStartServiceIntent = PendingIntent.getBroadcast(getApplicationContext(), _id, startIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        pendingEndServiceIntent = PendingIntent.getBroadcast(getApplicationContext(), idend, endIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setInexactRepeating(AlarmManager.RTC, startTime,
                AlarmManager.INTERVAL_DAY, pendingStartServiceIntent);

        alarmManager.setInexactRepeating(AlarmManager.RTC, endTime,
                AlarmManager.INTERVAL_DAY, pendingEndServiceIntent);


        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(sharedPref.getKeyStartServiceTime());
        Toast.makeText(this, "Service will start at "+ formatter.format(calendar.getTime()), Toast.LENGTH_SHORT).show();*/

    }

    public void clearOperatorCounter(){
        operatorNumber[0] = 0;
        operatorNumber[1] = 0;
        operatorNumber[2] = 0;
        operatorNumber[3] = 0;
        operatorNumber[4] = 0;
        operatorNumber[5] = 0;
    }

    private void addOperatorSpinner() {
        String[] ITEMS = {Constant.ALL_PHONE, Constant.GP, Constant.ROBI, Constant.BLINK, Constant.AIRTEL, Constant.TTALK, Constant.EMULATOR};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spOperator.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        //respond to menu item selection
        switch (item.getItemId()) {
            case R.id.deliverdlist:
                startActivity(new Intent(this, TaskFinished.class));
                return true;
            case R.id.help:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private class StopSmsService extends AsyncTask<String, String, Boolean> {
        ProgressDialog progressDialog = HelperMethods.getProgressBar(getDialogContext());

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            HelperMethods.startProgressBar(progressDialog,"Please Wait.....");
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            if (HelperMethods.isMyServiceRunning(SmsBroadCastService.class,getApplicationContext())){
                Intent intentService = new Intent(getApplicationContext(), SmsBroadCastService.class);
                stopService(intentService);
                return true;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean serviceDeleted) {
            HelperMethods.stopProgressBar(progressDialog);
            if (serviceDeleted){
                Intent intent = new Intent(getApplicationContext(), TaskFinished.class);
                startActivity(intent);
            }else {
                Toast.makeText(MainActivity.this, "No service is running", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private class ShowInputNumberInfo extends AsyncTask<String, String, Boolean> {
        ProgressDialog progressDialog = HelperMethods.getProgressBar(getDialogContext());

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            HelperMethods.startProgressBar(progressDialog,"Please Wait.....");
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            return generateGenerateInfos();
        }

        @Override
        protected void onPostExecute(Boolean isTaskFinished) {
            HelperMethods.stopProgressBar(progressDialog);
            if (isTaskFinished){
                showDialog();
            }
        }
    }

    private class SaveDataInDatabase extends AsyncTask<String, String, Boolean> {
        ProgressDialog progressDialog = HelperMethods.getProgressBar(getDialogContext());

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            HelperMethods.startProgressBar(progressDialog,"Please Wait.....");
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            saveDataInSharedPref();
            dbManager.cleanSmSinfoTable();
            Log.i("arif",dbManager.getSmsInfo().size()+"");
            dbManager.setSmsInfo(numberInfos);
            dbManager.setPhoneInfo(numberInfos);
            dbManager.cleanRecentUndeliveredTable();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean isTaskFinished) {
            HelperMethods.stopProgressBar(progressDialog);
            startService();

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

    @Override
    protected void onPause() {
        sharedPref.setMessage(etTextBody.getText().toString());
        sharedPref.setPhones(etNumbers.getText().toString());
        super.onPause();
    }
}
