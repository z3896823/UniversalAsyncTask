package sysu.evteam.zyb.universalasynctask;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataProvider.initial("http://39.108.72.65:81/schedulepro.asmx", "http://tempuri.org/");

        Button btn_1 = findViewById(R.id.id_btn_1);// 无参，返回对象列表
        Button btn_2 = findViewById(R.id.id_btn_2);// 无参，返回标志位
        Button btn_3 = findViewById(R.id.id_btn_3);// 有参，返回对象列表
        Button btn_4 = findViewById(R.id.id_btn_4);// 有参，返回标志位
        final TextView tv = findViewById(R.id.id_tv_result);

        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test1(tv);
            }
        });

        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test2(tv);
            }
        });

        btn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test3(tv);
            }
        });

        btn_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test4(tv);
            }
        });
    }

    public void test1(final TextView textView) {
        Logger.d(this, "UniversalTaskTest-test1()");

        final StringBuilder sb = new StringBuilder();
        DataProvider.getInstance().execute("getUserList", null, User.class, new OnResultListener() {
            @Override
            public void onResult(List<Object> resultList) {
                // 做个安全判断
                if (resultList.get(0) instanceof User) {
                    User user;
                    for (Object o : resultList) {
                        user = (User) o;
                        sb.append(user.toString()+"\n");
                    }
                } else {
                    sb.append("无法识别收到的数据");
                }
                textView.setText(sb.toString());
                Logger.d(this,sb.toString());
            }
        });
    }

    public void test2(final TextView textView) {
        Logger.d(this, "UniversalTaskTest-test2()");

        final StringBuilder sb = new StringBuilder();
        DataProvider.getInstance().execute("HelloWorld", null, null, new OnResultListener() {
            @Override
            public void onResult(List<Object> resultList) {
                // 做个安全判断
                if (resultList.get(0) instanceof String){
                    sb.append(resultList.get(0).toString());
                } else {
                    sb.append("无法识别收到的数据");
                }
                textView.setText(sb.toString());
                Logger.d(this,sb.toString());
            }
        });
    }


    public void test3(final TextView textView) {
        Logger.d(this, "UniversalTaskTest-test2()");

        final StringBuilder sb = new StringBuilder();
        Map<String,String> valueMap = new HashMap<>(1);
        valueMap.put("name","邹渊博");
        DataProvider.getInstance().execute("getEventByName", valueMap, Event.class, new OnResultListener() {
            @Override
            public void onResult(List<Object> resultList) {
                // 做个安全判断
                if (resultList.get(0) instanceof Event) {
                    Event e;
                    for (Object o : resultList) {
                        e = (Event) o;
                        sb.append(e.toString()+"\n");
                    }
                } else {
                    sb.append("无法识别收到的数据");
                }
                textView.setText(sb.toString());
                Logger.d(this,sb.toString());
            }
        });
    }


    public void test4(final TextView textView) {
        Logger.d(this, "UniversalTaskTest-test2()");

        final StringBuilder sb = new StringBuilder();
        Map<String,String> valueMap = new HashMap<>(7);
        valueMap.put("name","邹渊博");
        valueMap.put("status","LEVEL_SOFTWARELEADER");
        valueMap.put("type","事假");
        valueMap.put("begin","2017-11-23");
        valueMap.put("end","2017-11-25");
        valueMap.put("days","2");
        valueMap.put("remark","封装库测试");
        DataProvider.getInstance().execute("addEvent", valueMap, null, new OnResultListener() {
            @Override
            public void onResult(List<Object> resultList) {
                // 做个安全判断
                if (resultList.get(0) instanceof String){
                    sb.append(resultList.get(0).toString());
                } else {
                    sb.append("无法识别收到的数据");
                }
                textView.setText(sb.toString());
                Logger.d(this,sb.toString());
            }
        });
    }
}
