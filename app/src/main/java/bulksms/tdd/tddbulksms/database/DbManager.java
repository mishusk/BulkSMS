package bulksms.tdd.tddbulksms.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import bulksms.tdd.tddbulksms.model.InfoModel;

/**
 * Created by ThirdEye-lll on 2/26/2017.
 */

public class DbManager {

    SQLiteDatabase sql;
    DbHelper dbHelper;

    public DbManager(DbHelper helper) {
        this.dbHelper = helper;
    }


    public void addDataToTable(InfoModel mModel){

        sql =  dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(DbHelper.NAME,mModel.getName());
        cv.put(DbHelper.PHONE,mModel.getMessage());

        sql.insert(DbHelper.TABLE_NAME,null,cv);
        sql.close();

    }




    public String[] getTableData(){

        sql = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM "+DbHelper.TABLE_NAME;
        Cursor c = sql.rawQuery(query , null);

        String[] received_data = new String[c.getCount()];

        c.moveToFirst();

        if (c.moveToFirst()){
            int counter = 0;
            do {
                received_data[counter] = c.getString(c.getColumnIndex(DbHelper.NAME+""))+"\n"+
                        c.getString(c.getColumnIndex(DbHelper.PHONE))+"\n";
                counter = counter+1;
            }while (c.moveToNext());
        }

        return received_data;

    }



}