package bulksms.tdd.tddbulksms.helper;

/**
 * Created by MD_Tareq on 2/28/2017.
 */

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by y34h1a on 2/23/17.
 */

public class HelperMethods {

    public static ProgressDialog getProgressBar(Context context) {
        ProgressDialog progressBar = new ProgressDialog(context);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        return progressBar;
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
