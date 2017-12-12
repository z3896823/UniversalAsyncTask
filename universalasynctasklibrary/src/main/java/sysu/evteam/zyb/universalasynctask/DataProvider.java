package sysu.evteam.zyb.universalasynctask;

import android.os.AsyncTask;
import android.support.annotation.Nullable;

import org.kxml2.kdom.Element;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * @author : zyb
 *         e-mail : hbdxzyb@hotmail.com
 *         time   : 2017/11/16
 *         desc   : 采用单例，后续要加入异步任务的管理
 *         version: 1.0
 */

public class DataProvider<T> {

    private static DataProvider instance;

    private static String WSDL;
    private static String namespace;

    public DataProvider() {
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
     * 执行请求
     *
     * @param methodName 调用的方法名
     * @param valueMap 该方法需要哪些参数（无参方法直接传null）
     * @param listener 返回的结果在哪里接收
     */
    public void query(String methodName, @Nullable Map<String, String> valueMap, ResultListener<T> listener, Element[] soapHeader) {
        if (WSDL == null || namespace == null) {
            Logger.e(this, "WSDL or namespace cannot be null! Please call initial() to initialize DataProvider first.");
            return;
        }

        Class clazz = getResultType(listener);
        Logger.d(this,"监听的泛型类型为"+clazz.getName());

        // 必须要显式声明异步任务的泛型对象，以下两行代码不可合成一行
        UniversalTask<T> task = new UniversalTask(WSDL, namespace, methodName, listener, valueMap, clazz, soapHeader);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);// 使用并发数为5的线程池
    }

    /**
     * 从listener中抽取泛型类型
     *
     * @param listener 回调接口
     * @return 获得的泛型类型
     */
    private Class getResultType(ResultListener listener){
        // 获得监听器实现的接口
        Type type = listener.getClass().getGenericInterfaces()[0];
        // 对监听器接口转型成参数化类型，并获得其中的泛型类型
        Type resultType = ((ParameterizedType)type).getActualTypeArguments()[0];
        // 将泛型类型转型成Class后返回
        return (Class) resultType;
    }
}
