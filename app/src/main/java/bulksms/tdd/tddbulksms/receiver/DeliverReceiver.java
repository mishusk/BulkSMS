package bulksms.tdd.tddbulksms.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import bulksms.tdd.tddbulksms.constant.Constant;
import bulksms.tdd.tddbulksms.database.DbManager;
import bulksms.tdd.tddbulksms.model.InfoModel;
import bulksms.tdd.tddbulksms.sharedpref.SharedPref;

/**
 * Created by y34h1a on 3/1/17.
 */

public class DeliverReceiver extends BroadcastReceiver {
    SharedPref sharedPref;
    DbManager database;
    @Override
    public void onReceive(Context context, Intent intent) {
        sharedPref = new SharedPref(context);
        database = new DbManager(context);

        int deliveredCount = sharedPref.getDeliveredCount();
        String phoneNumber = intent.getStringExtra(Constant.POSITION);
        InfoModel infoModel = new InfoModel();
        infoModel.setPhoneNumber(phoneNumber);
        infoModel.setMessage(sharedPref.getMessage());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.ENGLISH);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        String time =  formatter.format(calendar.getTime());
        infoModel.setSendTime(time);

        Toast.makeText(context, "In Delivery", Toast.LENGTH_SHORT).show();
        switch (getResultCode()) {
            case Activity.RESULT_OK:
                Toast.makeText(context,"Sms Delivered: " + phoneNumber ,
                        Toast.LENGTH_SHORT).show();

                sharedPref.setDeliveredCount(deliveredCount + 1);
                database.setDeliveredPhone(infoModel);
                database.updateDeliveryStatus(phoneNumber);
                database.deletePhone(phoneNumber);
                break;
            case Activity.RESULT_CANCELED:
                Toast.makeText(context, "Sms Not Delivered",
                        Toast.LENGTH_SHORT).show();
                break;
        }

    }
}