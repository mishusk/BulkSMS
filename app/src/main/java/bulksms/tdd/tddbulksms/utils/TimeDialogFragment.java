package bulksms.tdd.tddbulksms.utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import bulksms.tdd.tddbulksms.constant.Constant;

/**
 * Created by y34h1a on 1/27/17.
 */

@SuppressLint("ValidFragment")
public class TimeDialogFragment extends DialogFragment {
    private int timeHour;
    private int timeMinute;
    private Handler handler;


    public TimeDialogFragment(Handler handler){
        this.handler = handler;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        timeHour = bundle.getInt(Constant.HOUR);
        timeMinute = bundle.getInt(Constant.MINUTE);
        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener(){

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                timeHour = hourOfDay;
                timeMinute = minute;
                Bundle b = new Bundle();
                b.putInt(Constant.HOUR, timeHour);
                b.putInt(Constant.MINUTE, timeMinute);
                Message msg = new Message();
                msg.setData(b);
                handler.sendMessage(msg);
            }
        };
        return new TimePickerDialog(getActivity(), listener, timeHour, timeMinute, false);
    }
}
