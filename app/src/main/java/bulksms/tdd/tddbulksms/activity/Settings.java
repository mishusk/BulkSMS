package bulksms.tdd.tddbulksms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Calendar;

import bulksms.tdd.tddbulksms.R;
import bulksms.tdd.tddbulksms.constant.Constant;
import bulksms.tdd.tddbulksms.sharedpref.SharedPref;
import bulksms.tdd.tddbulksms.utils.TimeDialogFragment;
import bulksms.tdd.tddbulksms.utils.Util;

public class Settings extends AppCompatActivity {
    Button btEndTime, btStartTime, btUpdateSettings;
    FloatingActionButton fab;
    private static int timeHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    private static int timeMinute = Calendar.getInstance().get(Calendar.MINUTE);
    private String startTime;
    private String endTime;
    private long startTimeInMillis;
    private long endTimeInMillis;
    private SharedPref sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        initView();


    }

    private void initView() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        btEndTime = (Button) findViewById(R.id.btEndTime);
        btStartTime = (Button) findViewById(R.id.btStartTime);
        btUpdateSettings = (Button) findViewById(R.id.btUpdateSettings);
        sharedPref = new SharedPref(getApplicationContext());
        initListener();
    }

    private void initListener() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        btEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(Constant.HOUR, timeHour);
                bundle.putInt(Constant.MINUTE, timeMinute);

                TimeDialogFragment fragment = new TimeDialogFragment(new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        Bundle bundle = msg.getData();
                        timeHour = bundle.getInt(Constant.HOUR);
                        timeMinute = bundle.getInt(Constant.MINUTE);

                        //SET TIME IN SHARED PREF
                        Calendar calendar = Calendar.getInstance();

                        //TODO: change add next day condition
                        if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) > timeHour) {
                            calendar.add(Calendar.DAY_OF_YEAR, 1); // add, not set!
                        }

                        calendar.set(Calendar.HOUR_OF_DAY,timeHour);
                        calendar.set(Calendar.MINUTE,timeMinute);
                        calendar.set(Calendar.SECOND,0);
                        calendar.set(Calendar.MILLISECOND,0);
                        endTimeInMillis = calendar.getTimeInMillis();

                        endTime = Util.convert24HourTo12(timeHour + ":" + timeMinute);
                        btEndTime.setText(endTime);
                        return false;
                    }
                }));
                fragment.setArguments(bundle);
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(fragment, Constant.TIME_PICKER);
                transaction.commit();
            }
        });

        btStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(Constant.HOUR, timeHour);
                bundle.putInt(Constant.MINUTE, timeMinute);

                TimeDialogFragment fragment = new TimeDialogFragment(new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        Bundle bundle = msg.getData();
                        timeHour = bundle.getInt(Constant.HOUR);
                        timeMinute = bundle.getInt(Constant.MINUTE);

                        //SET TIME IN SHARED PREF
                        Calendar calendar = Calendar.getInstance();

                        //TODO: change add next day condition
                        if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) > timeHour) {
                            calendar.add(Calendar.DAY_OF_YEAR, 1); // add, not set!
                        }

                        calendar.set(Calendar.HOUR_OF_DAY,timeHour);
                        calendar.set(Calendar.MINUTE,timeMinute);
                        calendar.set(Calendar.SECOND,0);
                        calendar.set(Calendar.MILLISECOND,0);
                        startTimeInMillis = calendar.getTimeInMillis();

                        startTime = Util.convert24HourTo12(timeHour + ":" + timeMinute);
                        btStartTime.setText(startTime);
                        return false;
                    }
                }));
                fragment.setArguments(bundle);
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(fragment, Constant.TIME_PICKER);
                transaction.commit();
            }
        });

        btUpdateSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPref.setKeyStartServiceTime(startTimeInMillis);
                sharedPref.setKeyEndServiceTime(endTimeInMillis);
                Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Settings Updated", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
