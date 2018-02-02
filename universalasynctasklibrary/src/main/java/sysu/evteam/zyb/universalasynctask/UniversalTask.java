package sysu.evteam.zyb.universalasynctask;

import android.os.AsyncTask;

import org.greenrobot.eventbus.EventBus;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Element;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import sysu.evteam.zyb.universalasynctask.data.ListData;


/**
 * <pre>
 *     @author: zyb
 *     email  : hbdxzyb@hotmail.com
 *     time   : 2017/11/20 上午10:46
 *     desc   : 缺陷之一：每个对象都是反射生成的，只不过每次反射的参数不同，有没有什么办法可以反射一次生成一个空对象，
 *     然后对这个空对象进行多次复制利用呢？
 *     version: 1.0
 * </pre>
 */

public class UniversalTask<T> extends AsyncTask<Void, Void, List<T>> {

    private String WSDL, namespace, methodName;
    private Element[] soapHeader;

    private Class c;
    private boolean flag = true;

    private ResultListener<T> listener;
    private Map<String, String> valueMap;

    private String dataTag;

    /**
     * 使用回调来获取结果
     */
    UniversalTask(String WSDL, String namespace, String methodName, ResultListener<T> listener,
                         Map<String, String> valueMap, Class c, Element[] soapHeader) {
        super();
        this.WSDL = WSDL;
        this.namespace = namespace;
        this.methodName = methodName;
        this.listener = listener;
        this.valueMap = valueMap;
        this.c = c;
        this.soapHeader = soapHeader;
    }

    /**
     * 使用 EventBus 来获取结果
     */
    UniversalTask(String WSDL, String namespace,String methodName,Map<String,String> valueMap, Class c,Element[] soapHeader,String dataTag){
        this.WSDL = WSDL;
        this.namespace = namespace;
        this.methodName = methodName;
        this.valueMap = valueMap;
        this.c = c;
        this.soapHeader = soapHeader;
        this.dataTag = dataTag;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Logger.d(this, "异步任务启动, onPreExecute() executed");
    }


    /**
     * WebService 返回的多数据分两种
     * 一种是一个或多个对象，一种是单个的标志位，两种不同的数据会生成不同的 SoapObject，这一点调用方最好事先知道
     * 虽然这里的判断不需要调用方参与（使用 isList()方法实现）
     * 对于第一种，生成一个 List 对象返回，比如 List<User>
     * 对于第二种，生成一个List<String>，里面只有一个值
     *
     * @param voids webService的参数直接通过构造函数传到对象中来，所以 doInBackground 的参数为 Void
     * @return List<T> 包含结果对象的 List
     */
    @Override
    protected List<T> doInBackground(Void... voids) {
        List<T> resultList = new ArrayList<>();

        SoapObject requestObj = new SoapObject(namespace, methodName);
        if (valueMap != null) {
            // 填充参数
            for (Map.Entry e : valueMap.entrySet()) {
                requestObj.addProperty((String) e.getKey(), e.getValue());
            }
        }

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.dotNet = true;
        envelope.bodyOut = requestObj;
        if (soapHeader != null) {
            envelope.headerOut = soapHeader;
        }

        if (isCancelled()) {
            return null;
        }

        HttpTransportSE transportSE = new HttpTransportSE(WSDL);
        try {
            transportSE.call(namespace + methodName, envelope);
            SoapObject resultObj = (SoapObject) envelope.bodyIn;
            if (isList(resultObj)) {
                double begin = System.currentTimeMillis();

                // 将根据泛型参数获得的结果类型和根据SoapObject解析的结果类型做比较，进行安全检查
                if (c.getName().equals(String.class.getName())) {
                    android.util.Log.e("UniversalTask", "结果解析与结果预期不同，请检查返回的结果是否为String类型的标志位");
                    return null;
                }
                SoapObject tempObj = (SoapObject) envelope.getResponse();//包含了所有的对象
                SoapObject temp;//包含单个对象的SoapObject
                List<String> attributes = new ArrayList<>();// 一个容器，用来保存提取的某对象的属性
                int objCount = tempObj.getPropertyCount();//包含的对象个数
                int propertyCount = ((SoapObject) tempObj.getProperty(0)).getPropertyCount();//对象的属性个数

                if (isCancelled()) {
                    return null;
                }

                for (int i = 0; i < objCount; i++) {

                    if (isCancelled()) {
                        return null;
                    }

                    temp = (SoapObject) tempObj.getProperty(i);
                    attributes.clear();
                    // 提取从网络获得的对象属性到一个List中
                    for (int j = 0; j < propertyCount; j++) {
                        attributes.add(temp.getProperty(j).toString());
                    }
                    resultList.add(generateObject(attributes,false));
                }

                Logger.d(this, objCount + "个对象的反射解析耗时（ms）：" + (System.currentTimeMillis() - begin));
            } else {
                String result = resultObj.getProperty(0).toString();
                List<String> a = new ArrayList<>();
                a.add(result);
                resultList.add(generateObject(a,true));
            }
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    @Override
    protected void onCancelled(List<T> objects) {
        Logger.d(this, "task has been cancelled");
        listener = null;
    }

    /**
     * 由于生成的对象总是List<Object>，所以如果使用 EventBus 发布的话，每个 subscriber 都会收到消息
     * 所以这里使用 ListData 对数据做一层封装
     * @param objects
     */
    @Override
    protected void onPostExecute(List<T> objects) {
        if (listener == null) {
            ListData<T> listData = new ListData<>();
            listData.setDataList(objects);
            listData.setTag(dataTag);
            EventBus.getDefault().post(listData);
        } else {
            listener.onResult(objects);
            listener = null;
        }
    }

    /**
     * 判断返回的结果是一个单一的标志位还是一系列对象的属性值
     *
     * @param resultObj 从envelope中取出来的最原始的数据，具体打断点查看Envelope的结构
     * @return true if resultObj contains a List of Object
     */
    private boolean isList(SoapObject resultObj) {
        Logger.d(this, "返回值为对象列表？" + (resultObj.getProperty(0) instanceof SoapObject));
        return resultObj.getProperty(0) instanceof SoapObject;
    }

    /**
     * **核心代码**
     * <p>
     * 根据传入的List<String>使用反射生成一个对象返回
     *
     * @param attributes attributes list
     * @param isString true if T = String
     * @return an object generated with elements
     */
    private T generateObject(List<String> attributes,boolean isString) {
        if (isString){
            return (T) attributes.get(0);
        }

        Method[] methods = c.getMethods();
        List<Method> annotationMethodList = new ArrayList<>();
        // 方法的筛选
        for (Method method : methods) {
            if (method.getName().contains("set") && method.getAnnotation(BeanMethodAnnotation.class).order() < 99) {
                annotationMethodList.add(method);
            }
        }
        // 方法的排序
        Collections.sort(annotationMethodList, new Comparator<Method>() {
            @Override
            public int compare(Method o1, Method o2) {
                if (o1.getAnnotation(BeanMethodAnnotation.class).order() > o2.getAnnotation(BeanMethodAnnotation.class).order()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        // 安全检查
        if (attributes.size() != annotationMethodList.size()) {
            try {
                throw new Exception("请检查服务器返回的数据个数与本地需要填充的JavaBean的属性个数是否匹配");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 输出排序好的方法（仅作调试用）
        if (flag) {
            for (Method method : annotationMethodList) {
                Logger.d(this, method.getName());
            }
            flag = false;
        }
        // 生成Bean
        Object o = null;
        try {
            o = c.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (o != null) {
            for (int i = 0; i < attributes.size(); i++) {
                try {
                    annotationMethodList.get(i).invoke(o, attributes.get(i));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

        return (T) o;
    }
}
