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
    private static final String KEY_START_TIME = "key_start_time";
    private static final String KEY_IS_STARTED = "key_is_started";
    private static final String KEY_MESSAGE_INFO = "message_info";

    public SharedPref(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        //TODO fix this warning
        editor = pref.edit();
    }


    public void setPrefSelectedOrg(int position) {
        editor.putInt(KEY_SELECTED_ORG, position);
        editor.commit();
    }

    public int getPrefSelectedOrg() {
        return pref.getInt(KEY_SELECTED_ORG, 0);
    }
}