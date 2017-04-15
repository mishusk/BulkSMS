package bulksms.tdd.tddbulksms.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.icu.text.IDNA;

import java.util.ArrayList;

import bulksms.tdd.tddbulksms.model.InfoModel;
import bulksms.tdd.tddbulksms.model.MessageModel;

/**
 * Created by ThirdEye-lll on 2/26/2017.
 */

public class DbManager {

    SQLiteDatabase mDatabase;
    DbHelper dbHelper;

    public DbManager(Context context) {
        dbHelper = new DbHelper(context.getApplicationContext());
        mDatabase = dbHelper.getReadableDatabase();
    }


    private String[] column_sms_info = {
            DbHelper.COL_ID,
            DbHelper.COL_PHN_NU,
            DbHelper.COL_OPERATOR,
            DbHelper.COL_STATUS,
            DbHelper.COL_SMS
    };

    private String[] column_phn_info = {
            DbHelper.COL_ID,
            DbHelper.COL_PHN_NU,
            DbHelper.COL_OPERATOR
    };

    private String[] column_undeliverd_info = {
            DbHelper.COL_ID,
            DbHelper.COL_PHN_NU,
            DbHelper.COL_OPERATOR,
            DbHelper.COL_SMS
    };

    private String[] column_deliverd_info = {
            DbHelper.COL_ID,
            DbHelper.COL_PHN_NU,
            DbHelper.COL_DELIVERD_TIME,
            DbHelper.COL_SMS
    };

    private String[] column_message_info = {
            DbHelper.COL_ID,
            DbHelper.COL_SMS
    };

    //-------------SET PHONE INFO-------------------

    public void setMessage(MessageModel messageInfo) {
        String sql = "INSERT INTO " + (DbHelper.TABLE_MESSAGE + " VALUES (?,?);");
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();
        statement.clearBindings();
        statement.bindString(2, messageInfo.getMessage());
        statement.execute();
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }

    public ArrayList<MessageModel> getMessges(){
        ArrayList<MessageModel> messageList = new ArrayList<>();
        Cursor cursor = mDatabase.query(DbHelper.TABLE_MESSAGE, column_message_info,
                null, null, null, null, DbHelper.COL_ID + " DESC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                MessageModel messageModel = new MessageModel();

                messageModel.setMessage(cursor.getString(cursor.getColumnIndex(DbHelper.COL_SMS)));
                messageList.add(messageModel);
            } while (cursor.moveToNext());
        }
        return messageList;
    }

    //TODO:: Impliment this
    public void deleteMessage(int id){

    }


    public void setPhoneInfo(ArrayList<InfoModel> mModels) {
        boolean isDuplicate = false;
        ArrayList<InfoModel> withoutDuplicate = new ArrayList<>();

        String[] columns = {
                DbHelper.COL_PHN_NU
        };


        Cursor cursor = mDatabase.query(DbHelper.TABLE_INFO,
                columns, null, null, null, null, null);

        for (int i = 0; i < mModels.size(); i++){
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String phoneNum;
                    phoneNum = cursor.getString(cursor.getColumnIndex(DbHelper.COL_PHN_NU));
                    if (phoneNum.equals(mModels.get(i).getPhoneNumber())) {
                        isDuplicate = true;
                        break;
                    }
                }
                while (cursor.moveToNext());
            }

            if (!isDuplicate){
                withoutDuplicate.add(mModels.get(i));
            }
        }


        if (withoutDuplicate.size() != 0){
            String sql = "INSERT INTO " + (DbHelper.TABLE_INFO + " VALUES (?,?,?);");
            SQLiteStatement statement = mDatabase.compileStatement(sql);
            mDatabase.beginTransaction();
            for(int i = 0; i < mModels.size(); i++) {
                statement.clearBindings();
                InfoModel mModel = withoutDuplicate.get(i);
                statement.bindString(2, mModel.getPhoneNumber());
                statement.bindString(3, mModel.getOperatorName());
                statement.execute();
            }
            mDatabase.setTransactionSuccessful();
            mDatabase.endTransaction();
        }
    }


    public void setSmsInfo(ArrayList<InfoModel> mModels) {

        ArrayList<InfoModel> withoutDuplicate = new ArrayList<>();
        boolean isDuplicate = false;
        //TODO::Comment out this
        String[] columns = {
                DbHelper.COL_PHN_NU
        };
        Cursor cursor = mDatabase.query(DbHelper.TABLE_SMS_INFO,
                columns, null, null, null, null, null);

        for (int i = 0; i < mModels.size(); i++){
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String phoneNum;
                    phoneNum = cursor.getString(cursor.getColumnIndex(DbHelper.COL_PHN_NU));
                    if (phoneNum.equals(mModels.get(i).getPhoneNumber())) {
                        isDuplicate = true;
                        break;
                    }
                }
                while (cursor.moveToNext());
            }

            if (!isDuplicate){
                withoutDuplicate.add(mModels.get(i));
            }
        }



        if (withoutDuplicate.size() != 0){
            String sql = "INSERT INTO " + (DbHelper.TABLE_SMS_INFO + " VALUES (?,?,?,?,?);");
            SQLiteStatement statement = mDatabase.compileStatement(sql);
            mDatabase.beginTransaction();

            for(int i = 0; i < mModels.size(); i++){
                statement.clearBindings();

                InfoModel mModel = withoutDuplicate.get(i);
                statement.bindString(2, mModel.getPhoneNumber());
                statement.bindString(3, mModel.getOperatorName());
                statement.bindString(4, mModel.getStatus());
                statement.bindString(5, mModel.getMessage());
                statement.execute();
            }

            mDatabase.setTransactionSuccessful();
            mDatabase.endTransaction();
        }
    }


    public void setDeliveredPhone(InfoModel infoModel){

        boolean isDuplicate = false;
        //TODO::Comment out this

        String[] columns = {
                DbHelper.COL_PHN_NU
        };
        Cursor cursor = mDatabase.query(DbHelper.TABLE_UNDELIVERD_INFO,
                columns, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String phoneNum;
                phoneNum = cursor.getString(cursor.getColumnIndex(DbHelper.COL_PHN_NU));
                if (phoneNum.equals(infoModel.getPhoneNumber())) {
                    isDuplicate = true;
                    break;
                }
            }
            while (cursor.moveToNext());
        }

        if (!isDuplicate){
            String sql = "INSERT INTO " + (DbHelper.TABLE_DELIVERD_INFO + " VALUES (?,?,?,?);");
            SQLiteStatement statement = mDatabase.compileStatement(sql);
            mDatabase.beginTransaction();
            statement.clearBindings();
            statement.bindString(2, infoModel.getPhoneNumber());
            statement.bindString(3, infoModel.getSendTime());
            statement.bindString(4, infoModel.getMessage());
            statement.execute();
            mDatabase.setTransactionSuccessful();
            mDatabase.endTransaction();
        }
    }

    public void setUndeliveredPhone(InfoModel infoModel){

        boolean isDuplicate = false;
        //TODO::Comment out this

        String[] columns = {
                DbHelper.COL_PHN_NU
        };
        Cursor cursor = mDatabase.query(DbHelper.TABLE_UNDELIVERD_INFO,
                columns, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String phoneNum;
                phoneNum = cursor.getString(cursor.getColumnIndex(DbHelper.COL_PHN_NU));
                if (phoneNum.equals(infoModel.getPhoneNumber())) {
                    isDuplicate = true;
                    break;
                }
            }
            while (cursor.moveToNext());
        }

        if (!isDuplicate){
            String sql = "INSERT INTO " + (DbHelper.TABLE_UNDELIVERD_INFO + " VALUES (?,?,?,?);");
            SQLiteStatement statement = mDatabase.compileStatement(sql);
            mDatabase.beginTransaction();
            statement.clearBindings();
            statement.bindString(2, infoModel.getPhoneNumber());
            statement.bindString(3, infoModel.getOperatorName());
            statement.bindString(4, infoModel.getMessage());
            statement.execute();
            mDatabase.setTransactionSuccessful();
            mDatabase.endTransaction();
        }
    }

    public void setRecentUndeliveredPhone(InfoModel infoModel){
        boolean isDuplicate = false;
        //TODO::Comment out this

        String[] columns = {
                DbHelper.COL_PHN_NU
        };
        Cursor cursor = mDatabase.query(DbHelper.TABLE_RECENT_UNDELIVERD_INFO,
                columns, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String phoneNum;
                phoneNum = cursor.getString(cursor.getColumnIndex(DbHelper.COL_PHN_NU));
                if (phoneNum.equals(infoModel.getPhoneNumber())) {
                    isDuplicate = true;
                    break;
                }
            }
            while (cursor.moveToNext());
        }


        if (!isDuplicate){
            String sql = "INSERT INTO " + (DbHelper.TABLE_RECENT_UNDELIVERD_INFO + " VALUES (?,?,?,?);");
            SQLiteStatement statement = mDatabase.compileStatement(sql);
            mDatabase.beginTransaction();
            statement.clearBindings();
            statement.bindString(2, infoModel.getPhoneNumber());
            statement.bindString(3, infoModel.getOperatorName());
            statement.bindString(4, infoModel.getMessage());
            statement.execute();
            mDatabase.setTransactionSuccessful();
            mDatabase.endTransaction();
        }
    }

    //-------------GET PHONE INFO-------------------
    public ArrayList<InfoModel> getAllPhones() {

        ArrayList<InfoModel> phones = new ArrayList<>();
        Cursor cursor = mDatabase.query(DbHelper.TABLE_INFO, column_phn_info,
                null, null, null, null, DbHelper.COL_ID + " ASC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                InfoModel phoneInfo = new InfoModel();

                phoneInfo.setId(cursor.getInt(cursor.getColumnIndex(DbHelper.COL_ID)));
                phoneInfo.setPhoneNumber(cursor.getString(cursor.getColumnIndex(DbHelper.COL_PHN_NU)));
                phoneInfo.setOperatorName(cursor.getString(cursor.getColumnIndex(DbHelper.COL_OPERATOR)));
                phones.add(phoneInfo);
            } while (cursor.moveToNext());
        }
        return phones;
    }

    public ArrayList<InfoModel> getSmsInfo(){
        ArrayList<InfoModel> phones = new ArrayList<>();
        Cursor cursor = mDatabase.query(DbHelper.TABLE_SMS_INFO, column_sms_info,
                null, null, null, null, DbHelper.COL_ID + " ASC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                InfoModel phoneInfo = new InfoModel();

                phoneInfo.setId(cursor.getInt(cursor.getColumnIndex(DbHelper.COL_ID)));
                phoneInfo.setPhoneNumber(cursor.getString(cursor.getColumnIndex(DbHelper.COL_PHN_NU)));
                phoneInfo.setOperatorName(cursor.getString(cursor.getColumnIndex(DbHelper.COL_OPERATOR)));
                phoneInfo.setMessage(cursor.getString(cursor.getColumnIndex(DbHelper.COL_SMS)));
                phoneInfo.setStatus(cursor.getString(cursor.getColumnIndex(DbHelper.COL_STATUS)));
                phones.add(phoneInfo);
            } while (cursor.moveToNext());
        }
        return phones;
    }

    //todo: status = 1 >> sms delivered
    public ArrayList<InfoModel> getDeliveredPhn() {
        ArrayList<InfoModel> phones = new ArrayList<>();
        Cursor cursor = mDatabase.query(DbHelper.TABLE_DELIVERD_INFO, column_deliverd_info,
                null, null, null, null, DbHelper.COL_ID + " DESC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                InfoModel phoneInfo = new InfoModel();
                phoneInfo.setId(cursor.getInt(cursor.getColumnIndex(DbHelper.COL_ID)));
                phoneInfo.setPhoneNumber(cursor.getString(cursor.getColumnIndex(DbHelper.COL_PHN_NU)));
                phoneInfo.setSendTime(cursor.getString(cursor.getColumnIndex(DbHelper.COL_DELIVERD_TIME)));
                phoneInfo.setMessage(cursor.getString(cursor.getColumnIndex(DbHelper.COL_SMS)));
                phones.add(phoneInfo);
            } while (cursor.moveToNext());
        }
        return phones;
    }



    //todo: status = 0 >> sms not delivered
    public ArrayList<InfoModel> getUndeliveredPhn() {
        ArrayList<InfoModel> phones = new ArrayList<>();
        Cursor cursor = mDatabase.query(DbHelper.TABLE_UNDELIVERD_INFO, column_undeliverd_info,
                null, null, null, null, DbHelper.COL_ID + " DESC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                InfoModel phoneInfo = new InfoModel();
                phoneInfo.setId(cursor.getInt(cursor.getColumnIndex(DbHelper.COL_ID)));
                phoneInfo.setPhoneNumber(cursor.getString(cursor.getColumnIndex(DbHelper.COL_PHN_NU)));
                phoneInfo.setOperatorName(cursor.getString(cursor.getColumnIndex(DbHelper.COL_OPERATOR)));
                phoneInfo.setMessage(cursor.getString(cursor.getColumnIndex(DbHelper.COL_SMS)));
                phones.add(phoneInfo);
            } while (cursor.moveToNext());
        }
        return phones;
    }

    public ArrayList<InfoModel> getRecentUndeliveredPhn() {
        ArrayList<InfoModel> phones = new ArrayList<>();
        Cursor cursor = mDatabase.query(DbHelper.TABLE_RECENT_UNDELIVERD_INFO, column_undeliverd_info,
                null, null, null, null, DbHelper.COL_ID + " DESC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                InfoModel phoneInfo = new InfoModel();
                phoneInfo.setId(cursor.getInt(cursor.getColumnIndex(DbHelper.COL_ID)));
                phoneInfo.setPhoneNumber(cursor.getString(cursor.getColumnIndex(DbHelper.COL_PHN_NU)));
                phoneInfo.setOperatorName(cursor.getString(cursor.getColumnIndex(DbHelper.COL_OPERATOR)));
                phoneInfo.setMessage(cursor.getString(cursor.getColumnIndex(DbHelper.COL_SMS)));
                phones.add(phoneInfo);
            } while (cursor.moveToNext());
        }
        return phones;
    }

    public ArrayList<InfoModel> getTemRecentUndeliveredPhn() {
        ArrayList<InfoModel> phones = new ArrayList<>();
        Cursor cursor = mDatabase.query(DbHelper.TABLE_SMS_INFO, column_sms_info,
                DbHelper.COL_STATUS + "=\"0\"", null, null, null, DbHelper.COL_STATUS + " ASC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                InfoModel phoneInfo = new InfoModel();
                phoneInfo.setId(cursor.getInt(cursor.getColumnIndex(DbHelper.COL_ID)));
                phoneInfo.setPhoneNumber(cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.COL_PHN_NU)));
                phoneInfo.setOperatorName(cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.COL_OPERATOR)));
                phoneInfo.setMessage(cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.COL_SMS)));
                phones.add(phoneInfo);
            } while (cursor.moveToNext());
        }
        return phones;
    }





    //------------------------GET INFO BY OPERATOR------------------------
    public ArrayList<InfoModel> getPhnByOperatorPhoneInfo(String constantOperator) {
        ArrayList<InfoModel> phones = new ArrayList<>();
        Cursor cursor = mDatabase.query(DbHelper.TABLE_INFO, column_phn_info,
                DbHelper.COL_OPERATOR + "=\"" + constantOperator + "\"", null, null, null, DbHelper.COL_ID + " ASC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                InfoModel phoneInfo = new InfoModel();
                phoneInfo.setId(cursor.getInt(cursor.getColumnIndex(DbHelper.COL_ID)));
                phoneInfo.setPhoneNumber(cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.COL_PHN_NU)));
                phoneInfo.setOperatorName(cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.COL_OPERATOR)));
                phones.add(phoneInfo);
            } while (cursor.moveToNext());
        }
        return phones;
    }
    public ArrayList<InfoModel> getPhnByOperatorSmSinfo(String constantOperator) {
        ArrayList<InfoModel> phones = new ArrayList<>();
        Cursor cursor = mDatabase.query(DbHelper.TABLE_SMS_INFO, column_sms_info,
                DbHelper.COL_OPERATOR + "=\"" + constantOperator + "\"", null, null, null, DbHelper.COL_ID + " ASC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                InfoModel phoneInfo = new InfoModel();
                phoneInfo.setId(cursor.getInt(cursor.getColumnIndex(DbHelper.COL_ID)));
                phoneInfo.setPhoneNumber(cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.COL_PHN_NU)));
                phoneInfo.setOperatorName(cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.COL_OPERATOR)));
                phoneInfo.setMessage(cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.COL_SMS)));
                phoneInfo.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.COL_STATUS)));
                phones.add(phoneInfo);
            } while (cursor.moveToNext());
        }
        return phones;
    }



    //------------------------UPDATE MESSAGE DELEIVERY STATUS------------------------
    public void updateDeliveryStatus(String phoneNumber){
        ContentValues cv = new ContentValues();
        cv.put(DbHelper.COL_STATUS, "1");
        mDatabase.update(DbHelper.TABLE_SMS_INFO, cv, DbHelper.COL_PHN_NU + "=\"" + phoneNumber + "\"", null);
    }


    //----------------------------CLEAN SMS TABLE INFO--------------------------------------
    public boolean cleanSmSinfoTable() {
        return mDatabase.delete(DbHelper.TABLE_SMS_INFO, null, null) > 0;
    }


    public boolean cleanPhoneinfoTable() {
        return mDatabase.delete(DbHelper.TABLE_INFO, null, null) > 0;
    }

    public boolean cleanRecentUndeliveredTable() {
        return mDatabase.delete(DbHelper.TABLE_RECENT_UNDELIVERD_INFO, null, null) > 0;
    }

    public boolean cleanUndeliveredTable() {
        return mDatabase.delete(DbHelper.TABLE_UNDELIVERD_INFO, null, null) > 0;
    }

    public boolean cleanDeliveredTable(){
        return mDatabase.delete(DbHelper.TABLE_DELIVERD_INFO, null, null) > 0;
    }

    public boolean deletePhone(String phoneNumber)
    {
        return mDatabase.delete(DbHelper.TABLE_UNDELIVERD_INFO, DbHelper.COL_PHN_NU + "=" + phoneNumber, null) > 0;
    }
}