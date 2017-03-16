package bulksms.tdd.tddbulksms.activity;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
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

import bulksms.tdd.tddbulksms.R;
import bulksms.tdd.tddbulksms.constant.Constant;
import bulksms.tdd.tddbulksms.database.DbManager;
import bulksms.tdd.tddbulksms.model.InfoModel;
import bulksms.tdd.tddbulksms.receiver.EndServiceReceiver;
import bulksms.tdd.tddbulksms.receiver.StartServiceReciver;
import bulksms.tdd.tddbulksms.sharedpref.SharedPref;
import bulksms.tdd.tddbulksms.service.SmsBroadCastService;
import fr.ganfra.materialspinner.MaterialSpinner;


public class MainActivity extends AppCompatActivity {
    EditText etTextBody;
    EditText etNumbers;
    Button btSendSms;
    DbManager dbManager;
    SharedPref sharedPref;
    MaterialSpinner spOperator;
    AlarmManager alarmManager;
    int[] operatorNumber = new int[]{0,0,0,0,0,0};

    String phonesString ;
    String numbers[] ;
    String message ;
    String slectedOprator;

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
        btSendSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phonesString = etNumbers.getText().toString();
                numbers = phonesString.split(",");
                message = etTextBody.getText().toString();


                //Selected Operator
                slectedOprator = spOperator.getSelectedItem().toString();
                operatorNumber[0] = numbers.length;

                if(!message.isEmpty() && !message.isEmpty() &&
                        !slectedOprator.equals(Constant.OPRATOR_NOT_SELETED)){
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

                    showDialog();


                }else {
                    Toast.makeText(getApplicationContext(),"Invalid Input", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public String operatorFinder(String phnNumber){
        String operatorCode = phnNumber.substring(0, Math.min(phnNumber.length(),3));

        switch (operatorCode){

            case "017" : {
                return Constant.GP;
            }


            case "016" : {
                return Constant.AIRTEL;
            }

            case "015" : {
                return Constant.TTALK;
            }

            case "018" : {
                return Constant.ROBI;
            }

            case "019" : {
                return Constant.BLINK;
            }

            case "555" :{
                return Constant.EMULATOR;
            }
            default:
                return "";
        }
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
        btConfirm = (Button) dialog.findViewById(R.id.btConfirm);

        tvAll.setText("All: " + operatorNumber[0]);
        tvGp.setText("Gp: " + operatorNumber[1]);
        tvAirtel.setText("Airtel: " + operatorNumber[2]);
        tvTTalk.setText("Tale Talk: " + operatorNumber[3]);
        tvRobi.setText("Robi: " + operatorNumber[4]);
        tvBLink.setText("BLink: " + operatorNumber[5]);

        clearOperatorCounter();

        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPref.setMessage(message);
                sharedPref.setPhones(phonesString);
                sharedPref.setOperator(slectedOprator);
                sharedPref.setSentCount(0);
                sharedPref.setDeliveredCount(0);
                for(String phoneNumber: numbers){

                    InfoModel info = new InfoModel();
                    info.setMessage(message);
                    info.setOperatorName(operatorFinder(phoneNumber));
                    info.setPhoneNumber(phoneNumber);
                    info.setStatus(Constant.STATUS_NOT_DELIVERED);

                    dbManager.setPhoneInfo(info);
                    dbManager.setSmsInfo(info);
                }
                clearOperatorCounter();

                Log.i("arif","Deliverd number: " + dbManager.getDeliveredPhn().size());
                startService();

                dialog.cancel();
            }
        });

        dialog.show();
    }

    private void startService(){
        long startTime = sharedPref.getKeyStartServiceTime();
        long endTime = sharedPref.getKeyEndServiceTime();

        PendingIntent pendingStartServiceIntent;
        PendingIntent pendingEndServiceIntent;

        Intent startIntent = new Intent(getApplicationContext(), StartServiceReciver.class);
        Intent endIntent = new Intent(getApplicationContext(), EndServiceReceiver.class);

        final int _id = (int) System.currentTimeMillis();
        final int idend = (int) System.currentTimeMillis();
        //myIntent.putExtra(Constant.TYPE, type);
        pendingStartServiceIntent = PendingIntent.getBroadcast(getApplicationContext(), _id, startIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        pendingEndServiceIntent = PendingIntent.getBroadcast(getApplicationContext(), idend, endIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setInexactRepeating(AlarmManager.RTC, startTime,
                AlarmManager.INTERVAL_DAY, pendingStartServiceIntent);

        alarmManager.setInexactRepeating(AlarmManager.RTC, endTime,
                AlarmManager.INTERVAL_DAY, pendingEndServiceIntent);

        sharedPref.setKeyStartAlarmId(_id);
        sharedPref.setKeyEndAlarmId(idend);
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
}
