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

    /**
     * 初始化DataProvider
     * 一般来讲调用同一个WebService的话WSDL和namespace的值是不变的，所以这里设置成静态，免去多次传值的麻烦
     * 如果一个应用中调用了多个WebService，每次调用不同的WebService时请重新初始化这两个变量
     *
     * @param WSDL
     * @param namespace
     */
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

    /**
     * 执行请求
     *
     * @param methodName 调用的方法名
     * @param valueMap 该方法需要哪些参数（无参方法直接传null）
     * @param c 希望返回什么对象（返回标志位的话直接传null）
     * @param listener 返回的结果在哪里接收
     */
    public void execute(String methodName, @Nullable Map<String, String> valueMap, @Nullable Class c, OnResultListener listener) {
        if (WSDL == null || namespace == null) {
            Logger.e(this, "WSDL or namespace cannot be null! Please call initial() to initialize DataProvider first.");
            return;
        }
        new UniversalTask(WSDL, namespace, methodName, listener, valueMap, c).execute();
    }
}
