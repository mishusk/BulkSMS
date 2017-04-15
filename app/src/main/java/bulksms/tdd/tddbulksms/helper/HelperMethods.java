package bulksms.tdd.tddbulksms.helper;

/**
 * Created by MD_Tareq on 2/28/2017.
 */

import android.app.ActivityManager;
import android.app.ApplicationErrorReport;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

/**
 * Created by y34h1a on 2/23/17.
 */

public class HelperMethods {

    public static ProgressDialog getProgressBar(Context context) {
        ProgressDialog progressBar = new ProgressDialog(context);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        return progressBar;
    }

    public static boolean isMyServiceRunning(Class<?> serviceClass,Context context) {
        ActivityManager manager = (ActivityManager)context. getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("Service already","running");
                return true;
            }
        }
        Log.i("Service not","running");
        return false;
    }

    public static void startProgressBar(ProgressDialog progressBar, String message) {

        progressBar.setCancelable(false);//you can cancel it by pressing back button
        progressBar.setMessage(message);
        progressBar.show();//displays the progress bar
    }

    public static void stopProgressBar(ProgressDialog progressDialog) {
        progressDialog.cancel();
    }

}
