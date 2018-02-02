package sysu.evteam.zyb.universalasynctask;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.greenrobot.eventbus.Subscribe;
import org.kxml2.kdom.Element;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * @author : zyb
 *         e-mail : hbdxzyb@hotmail.com
 *         time   : 2017/11/16
 *         desc   :
 *         使用时在 DataProvider 和 ResultListener 中均声明了泛型，但这两个泛型的作用是不一样的
 *          ResultListener 中的泛型是为了获得返回的对象类型，据此使用反射来生成 Object。但也只能生成 Object
 *          为了免去调用端强制转型的步骤，在 DataProvider 中传入泛型对象，使用这个泛型对生成的 Object 进行强制转型
 *         version: 1.0
 */

public class DataProvider<T> extends AbstractDataProvider<T>{

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
     * @param listener 接收返回结果的回调
     * @param soapHeader 如果 WebService 加密了的话，必须使用 SoapHeader 验证
     */
    public void query(String methodName, @Nullable Map<String, String> valueMap, @Nullable ResultListener<T> listener, Element[] soapHeader) {
        if (WSDL == null || namespace == null) {
            Logger.e(this, "WSDL or namespace cannot be null! Please call initial() to initialize DataProvider first.");
            return;
        }

        Class clazz = getResultType(listener);
        Logger.d(this,"监听的泛型类型为 "+clazz.getName());

        // 必须要显式声明异步任务的泛型对象，以下两行代码不可合成一行
        UniversalTask<T> task = new UniversalTask<>(WSDL, namespace, methodName, listener, valueMap, clazz, soapHeader);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);// 使用并发数为5的线程池
    }

    /**
     *
     *
     * @param methodName 同上
     * @param valueMap 同上
     * @param clazz 由于 Java 的泛型机制会导致运行期的泛型类型擦除,所以如果不使用监听（匿名类）的话无法获取到泛型参数，要手动指名
     * @param soapHeader 同上
     * @param tag 由于返回的数据全部封装在 List 中，所以要加一个 TAG 方便 EventBus 的 subdcriber 辨别消息来源
     */
    public void query(String methodName, @Nullable Map<String,String> valueMap, Class clazz, Element[] soapHeader, @NonNull String tag){
        if (WSDL == null || namespace == null) {
            Logger.e(this, "WSDL or namespace cannot be null! Please call initial() to initialize DataProvider first.");
            return;
        }

        UniversalTask<T> task = new UniversalTask<>(WSDL,namespace,methodName,valueMap,clazz,soapHeader,tag);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
