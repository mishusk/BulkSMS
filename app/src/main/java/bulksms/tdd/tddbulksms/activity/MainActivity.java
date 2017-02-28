package bulksms.tdd.tddbulksms.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;
import bulksms.tdd.tddbulksms.R;
import bulksms.tdd.tddbulksms.constant.Constant;
import bulksms.tdd.tddbulksms.database.DbManager;
import bulksms.tdd.tddbulksms.model.InfoModel;
import bulksms.tdd.tddbulksms.utils.SMSBroadCastRcvr;
import bulksms.tdd.tddbulksms.utils.SmsBroadCastService;


public class MainActivity extends AppCompatActivity {
    EditText etTextBody;
    EditText etNumbers;
    Button btSendSms;
    DbManager dbManager ;


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

        initListener();
    }

    private void initListener() {
        btSendSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numbers[] = etNumbers.getText().toString().split(",");
                String message = etTextBody.getText().toString();

                for(int i = 0; i < numbers.length; i++){
                    InfoModel info = new InfoModel(numbers[i], message);
                    dbManager.setPhoneInfo(info);
                }

                Log.i("arif",dbManager.getAllPhones().size()+"");


                Intent intent = new Intent(getApplicationContext(), SmsBroadCastService.class);
                startService(intent);
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void bt_sendSms(View view) {

        Toast.makeText(this, "click start thread", Toast.LENGTH_SHORT).show();
    }

    public void bt_stopTheThread(View view) {

        Toast.makeText(this, "click stop thread", Toast.LENGTH_SHORT).show();

    }
}
