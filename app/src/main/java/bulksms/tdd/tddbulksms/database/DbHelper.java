package bulksms.tdd.tddbulksms.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ThirdEye-lll on 2/26/2017.
 */

public class DbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "messageDb";
    public static final String TABLE_NAME = "msgTable";
    public static final String _ID = "_id";
    public static final String NAME = "name";
    public static final String PHONE = "phone";

    public static final int VERSION = 1;

    private final String createDB = "create table if not exists " + TABLE_NAME + " ( "
            + _ID + " integer primary key autoincrement, "
            + NAME + " text, "
            + PHONE + " text)";



    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createDB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table " + TABLE_NAME);
    }
}
