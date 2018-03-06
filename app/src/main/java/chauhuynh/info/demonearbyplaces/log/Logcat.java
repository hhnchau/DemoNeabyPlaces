package chauhuynh.info.demonearbyplaces.log;

import android.util.Log;

/**
 * Created by appro on 02/03/2018.
 */

public class Logcat {
    private static final String TAG = "TAG-";
    private static boolean log = true;

    public static void write(String tag, String s) {
        if (log) {
            Log.d(TAG + tag, s);
        }
    }
}
