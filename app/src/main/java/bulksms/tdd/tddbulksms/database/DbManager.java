package bulksms.tdd.tddbulksms.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;

import bulksms.tdd.tddbulksms.model.InfoModel;

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
            DbHelper.COL_SMS,
            DbHelper.COL_STATUS
    };

    private String[] column_phn_info = {
            DbHelper.COL_ID,
            DbHelper.COL_PHN_NU,
            DbHelper.COL_OPERATOR
    };

    public boolean setPhoneInfo(InfoModel mModel) {
        boolean isDuplicate = false;

        String[] columns = {
                DbHelper.COL_PHN_NU
        };

        Cursor cursor = mDatabase.query(DbHelper.TABLE_INFO,
                columns, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String phoneNum;
                phoneNum = cursor.getString(cursor.getColumnIndex(DbHelper.COL_PHN_NU));
                if (phoneNum.equals(mModel.getPhoneNumber())) {
                    isDuplicate = true;
                    break;
                }
            }
            while (cursor.moveToNext());
        }
        if (!isDuplicate) {
            String sql = "INSERT INTO " + (DbHelper.TABLE_INFO + " VALUES (?,?,?);");
            SQLiteStatement statement = mDatabase.compileStatement(sql);
            mDatabase.beginTransaction();
            statement.clearBindings();
            statement.bindString(2, mModel.getPhoneNumber());
            statement.bindString(3, mModel.getOperatorName());
            statement.execute();
            mDatabase.setTransactionSuccessful();
            mDatabase.endTransaction();
        }
        return isDuplicate;
    }


    public boolean setSmsInfo(InfoModel mModel) {
        boolean isDuplicate = false;

        String[] columns = {
                DbHelper.COL_PHN_NU
        };

        Cursor cursor = mDatabase.query(DbHelper.TABLE_SMS_INFO,
                columns, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String phoneNum;
                phoneNum = cursor.getString(cursor.getColumnIndex(DbHelper.COL_PHN_NU));
                if (phoneNum.equals(mModel.getPhoneNumber())) {
                    isDuplicate = true;
                    break;
                }
            }
            while (cursor.moveToNext());
        }
        if (!isDuplicate) {
            String sql = "INSERT INTO " + (DbHelper.TABLE_SMS_INFO + " VALUES (?,?,?,?,?);");
            SQLiteStatement statement = mDatabase.compileStatement(sql);
            mDatabase.beginTransaction();
            statement.clearBindings();
            statement.bindString(2, mModel.getPhoneNumber());
            statement.bindString(3, mModel.getOperatorName());
            statement.bindString(4, mModel.getMessage());
            statement.bindString(5, mModel.getStatus());
            statement.execute();
            mDatabase.setTransactionSuccessful();
            mDatabase.endTransaction();
        }
        return isDuplicate;
    }

    public ArrayList<InfoModel> getDeliveriedPhn() {
        ArrayList<InfoModel> phones = new ArrayList<>();
        Cursor cursor = mDatabase.query(DbHelper.TABLE_SMS_INFO, column_phn_info,
                null, null, null, null, DbHelper.COL_STATUS +" ASC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                InfoModel phoneInfo = new InfoModel();
                phoneInfo.setId(cursor.getInt(cursor.getColumnIndex(DbHelper.COL_ID)));
                phoneInfo.setPhoneNumber(cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.COL_PHN_NU)));
                phoneInfo.setPhoneNumber(cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.COL_OPERATOR)));
                phoneInfo.setPhoneNumber(cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.COL_SMS)));
                phoneInfo.setPhoneNumber(cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.COL_STATUS)));
                phones.add(phoneInfo);
            } while (cursor.moveToNext());
        }
        return phones;
    }




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


}