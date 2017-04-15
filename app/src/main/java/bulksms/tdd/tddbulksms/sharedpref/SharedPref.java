package bulksms.tdd.tddbulksms.sharedpref;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;
import java.util.ArrayList;

import bulksms.tdd.tddbulksms.constant.Constant;
import bulksms.tdd.tddbulksms.model.InfoModel;

/**
 * Created by y34h1a on 2/27/17.
 */

public class SharedPref {
    // LogCat tag
    private static String TAG = SharedPref.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    SharedPreferences.Editor editor;
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "SessionContent";
    private static final String KEY_SELECTED_ORG = "key_selected_org";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_PHONES = "phones";
    private static final String KEY_OPERATOR = "key_operator";
    private static final String KEY_POSITION = "key_position";
    private static final String KEY_SENT_COUNT = "sent_count";
    private static final String KEY_DELIVER_COUNT = "delivered_count";
    private static final String KEY_START_SERVICE_TIME = "start_service_time";
    private static final String KEY_END_SERVICE_TIME = "end_service_time";
    private static final String KEY_START_ALARM_ID = "key_start_alarm_id";
    private static final String KEY_END_ALARM_ID = "key_end_alarm_id";

    private static final String KEY_MESSAGE_1 = "key_message_1";
    private static final String KEY_MESSAGE_2 = "key_message_2";
    private static final String KEY_MESSAGE_3 = "key_message_3";
    private static final String KEY_MESSAGE_4 = "key_message_4";
    private static final String KEY_MESSAGE_5 = "key_message_5";

    private static final String KEY_MESSAGE_REAMAINING = "key_message_remain";


    public SharedPref(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        //TODO fix this warning
        editor = pref.edit();
    }

    public void setMessageRemaing(int num){
        editor.putInt(KEY_MESSAGE_REAMAINING, num);
        editor.commit();
    }

    public int getMessageRemaining(){
        return pref.getInt(KEY_MESSAGE_REAMAINING, 0);
    }

    public void setMessage1(String message1){
        editor.putString(KEY_MESSAGE_1, message1);
        editor.commit();
    }

    public String getMessage1(){
        return pref.getString(KEY_MESSAGE_1,"");
    }

    public void setMessage2(String message2){
        editor.putString(KEY_MESSAGE_2, message2);
        editor.commit();
    }

    public String getMessage2(){
        return pref.getString(KEY_MESSAGE_2,"");
    }

    public void setMessage3(String message3){
        editor.putString(KEY_MESSAGE_3, message3);
        editor.commit();
    }

    public String getMessage3(){
        return pref.getString(KEY_MESSAGE_3,"");
    }

    public void setMessage4(String message4){
        editor.putString(KEY_MESSAGE_4, message4);
        editor.commit();
    }

    public String getMessage4(){
        return pref.getString(KEY_MESSAGE_4,"");
    }

    public void setMessage5(String message5){
        editor.putString(KEY_MESSAGE_5, message5);
        editor.commit();
    }

    public String getMessage5(){
        return pref.getString(KEY_MESSAGE_5,"");
    }

    public void setMessage(String message){
        editor.putString(KEY_MESSAGE, message);
        editor.commit();
    }



    public String getMessage(){
        return pref.getString(KEY_MESSAGE,"");
    }


    public void setKeyStartServiceTime(long startTime){
        editor.putLong(KEY_START_SERVICE_TIME, startTime);
        editor.commit();
    }

    public long getKeyStartServiceTime(){
        return pref.getLong(KEY_START_SERVICE_TIME, 0L);
    }

    public void setKeyEndServiceTime(long endTime){
        editor.putLong(KEY_END_SERVICE_TIME, endTime);
        editor.commit();
    }

    public long getKeyEndServiceTime(){
        return pref.getLong(KEY_END_SERVICE_TIME, 0L);
    }

    public void setKeyStartAlarmId(int id){
        editor.putInt(KEY_START_ALARM_ID, id);
        editor.commit();
    }
    public void setKeyEndAlarmId(int id){
        editor.putInt(KEY_END_ALARM_ID, id);
        editor.commit();
    }

    public int getEndAlarmId(){
        return pref.getInt(KEY_START_ALARM_ID, 0);
    }

    public int getStartAlarmId(){
        return pref.getInt(KEY_END_ALARM_ID, 0);
    }

    public void setPhones(String phones){
        editor.putString(KEY_PHONES, phones);
        editor.commit();
    }

    public String getPhones(){
        return pref.getString(KEY_PHONES, "");
    }

    public void setOperator(String operator){
        editor.putString(KEY_OPERATOR, operator);
        editor.commit();
    }

    public String getOperator(){
        return pref.getString(KEY_OPERATOR,"");
    }

    public void setCurrentPosition(int position){
        editor.putInt(KEY_POSITION,position);
        editor.commit();
    }

    public int getCurrentPosition(){
        return pref.getInt(KEY_POSITION,0);
    }


    public void setSentCount(int count){
        editor.putInt(KEY_SENT_COUNT,count);
        editor.commit();
    }

    public int getSentCount(){
        return pref.getInt(KEY_SENT_COUNT,0);
    }

    public void setDeliveredCount(int count){
        editor.putInt(KEY_DELIVER_COUNT,count);
        editor.commit();
    }

    public int getDeliveredCount(){
        return pref.getInt(KEY_DELIVER_COUNT,0);
    }
}