package sysu.evteam.zyb.universalasynctask;

import android.util.Log;

/**
 * <pre>
 *     @author : zyb
 *     e-mail : hbdxzyb@hotmail.com
 *     time   : 2017/11/3
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class Logger {

    private static boolean isDebug = true;

    public static void d(Object o, String info){
        if (isDebug) {
            Log.d("ybz-UniversalTask", o.getClass().getSimpleName()+"--->"+info);
        }
    }

    public static void d(String o, String info){
        if (isDebug) {
            Log.d("ybz-UniversalTask", o.getClass().getSimpleName()+"--->"+info);
        }
    }

    public static void e(Object o, String info){
        if (isDebug) {
            Log.e("ybz-UniversalTask", o.getClass().getSimpleName()+"--->"+info);
        }
    }
}
