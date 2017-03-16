package bulksms.tdd.tddbulksms.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by y34h1a on 1/27/17.
 */

public class Util {
    public static String convert24HourTo12(String time){
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("H:mm", Locale.getDefault());
            final Date dateObj = sdf.parse(time);
            System.out.println(dateObj);
            return new SimpleDateFormat("K:mm a",Locale.getDefault()).format(dateObj);
        } catch (final ParseException e) {
            e.printStackTrace();
        }
        return "SomeThing Wrong";
    }
}
