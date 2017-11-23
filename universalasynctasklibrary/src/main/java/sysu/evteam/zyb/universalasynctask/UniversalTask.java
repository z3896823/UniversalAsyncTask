package sysu.evteam.zyb.universalasynctask;

import android.os.AsyncTask;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 *     @author: zyb
 *     email  : hbdxzyb@hotmail.com
 *     time   : 2017/11/20 上午10:46
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class UniversalTask extends AsyncTask<Void, Void, List<Object>> {

    private String WSDL, namespace, methodName;

    private Class c;
    private boolean flag = true;

    private OnResultListener listener;
    private Map<String, String> valueMap;

    public UniversalTask(String WSDL, String namespace, String methodName, OnResultListener listener,
                         Map<String, String> valueMap, Class c) {
        super();
        this.WSDL = WSDL;
        this.namespace = namespace;
        this.methodName = methodName;
        this.listener = listener;
        this.valueMap = valueMap;
        this.c = c;
    }

    // 如果是个标志，返回的List只包含一个String对象
    // 如果是对象，不管是单数还是复数，全部封装在List中返回
    @Override
    protected List<Object> doInBackground(Void... voids) {
        Logger.d(this, "异步任务启动");

        List<Object> resultList = new ArrayList<>();

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

        HttpTransportSE transportSE = new HttpTransportSE(WSDL);
        try {
            transportSE.call(namespace + methodName, envelope);
            SoapObject resultObj = (SoapObject) envelope.bodyIn;
            if (isList(resultObj)) {
                SoapObject tempObj = (SoapObject) envelope.getResponse();//包含了所有的对象
                SoapObject temp;//包含单个对象的SoapObject
                List<String> attributes = new ArrayList<>();// 一个容器，用来保存提取的某对象的属性
                int objCount = tempObj.getPropertyCount();//包含的对象个数
                int propertyCount = ((SoapObject) tempObj.getProperty(0)).getPropertyCount();//对象的属性个数
                for (int i = 0; i < objCount; i++) {
                    temp = (SoapObject) tempObj.getProperty(i);
                    attributes.clear();
                    // 提取对象的属性到一个List中
                    for (int j = 0; j < propertyCount; j++) {
                        attributes.add(temp.getProperty(j).toString());
                    }
                    resultList.add(generateObject(attributes));
                }
            } else {
                String result = resultObj.getProperty(0).toString();
                resultList.add(result);
            }
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }
        return resultList;
    }


    @Override
    protected void onPostExecute(List<Object> objects) {
        listener.onResult(objects);
    }

    /**
     * 判断返回的结果是一个单一的标志位还是一系列对象的属性值
     *
     * @param resultObj 从envelope中取出来的最原始的数据，具体打断点查看Envelope的结构
     * @return true if resultObj contains a List of Object
     */
    private boolean isList(SoapObject resultObj) {
        Logger.d(this, "true or false :" + (resultObj.getProperty(0) instanceof SoapObject));
        return resultObj.getProperty(0) instanceof SoapObject;
    }

    /**
     * 根据传入的List<String>使用反射生成一个对象返回
     *
     * @param attributes attributes list
     * @return an object generated with elements
     */
    private Object generateObject(List<String> attributes) {

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
            for (Method method : annotationMethodList){
                Logger.d(this,method.getName());
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

        return o;
    }
}
