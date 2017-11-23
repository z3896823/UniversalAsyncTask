package sysu.evteam.zyb.universalasynctask;

import android.os.AsyncTask;
import android.support.annotation.Nullable;

import java.util.List;
import java.util.Map;

/**
 * @author : zyb
 *         e-mail : hbdxzyb@hotmail.com
 *         time   : 2017/11/16
 *         desc   : 采用单例，后续要加入异步任务的管理
 *         version: 1.0
 */

public class DataProvider {

    private static DataProvider instance;

    private List<AsyncTask> taskList;

    private static String WSDL;
    private static String namespace;


    private DataProvider() {
    }

    public static void initial(String WSDL, String namespace) {
        DataProvider.WSDL = WSDL;
        DataProvider.namespace = namespace;
    }

    /**
     * 返回单例
     *
     * @return a instance
     */
    public static DataProvider getInstance() {
        if (instance == null) {
            instance = new DataProvider();
        }
        return instance;
    }

    public void execute(String methodName, @Nullable Map<String, String> valueMap, Class c, OnResultListener listener) {
        if (WSDL == null || namespace == null) {
            Logger.e(this, "WSDL or namespace cannot be null! Please call initial() to initialize DataProvider first.");
            return;
        }

        new UniversalTask(WSDL, namespace, methodName, listener, valueMap, c).execute();
    }

}
