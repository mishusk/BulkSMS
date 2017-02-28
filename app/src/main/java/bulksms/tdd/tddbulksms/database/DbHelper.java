package bulksms.tdd.tddbulksms.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by MORSHED on 2/26/2017.
 */

public class DbHelper extends SQLiteOpenHelper {

    public static final String DB_BULK_SMS = "dbBulkSmS";


    public static final String TABLE_INFO = "tbl_phn_info";
    public static final String TABLE_SMS_INFO = "tbl_sms_info";


    public static final String COL_ID = "id";
    public static final String COL_PHN_NU = "phone_number";
    public static final String COL_OPERATOR = "operator";
    public static final String COL_SMS = "sms_body";
    public static final String COL_STATUS = "status";

    public static final int VERSION = 1;

    private final String CREATE_INFO_DB = "create table if not exists " + TABLE_INFO + " ( "
            + COL_ID + " integer primary key autoincrement, "
            + COL_PHN_NU + " text, "
            + COL_OPERATOR + " text "
            + " ); ";

    private final String CREATE_STATUS_DB = "create table if not exists " + TABLE_SMS_INFO + " ( "
            + COL_ID + " integer primary key autoincrement, "
            + COL_PHN_NU + " text, "
            + COL_OPERATOR + " text, "
            + COL_STATUS + " text, "
            + COL_SMS + " text"
            + " ); ";

    public DbHelper(Context context) {
        super(context, DB_BULK_SMS, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_INFO_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table " + TABLE_INFO);
    }
}
