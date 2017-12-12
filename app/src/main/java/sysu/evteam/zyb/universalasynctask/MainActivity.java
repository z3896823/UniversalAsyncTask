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

        DataProvider.initial("http://39.108.72.65:82/schedulepro.asmx", "http://tempuri.org/");

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
        new DataProvider<User>().query("getUserList", null, new ResultListener<User>() {
            @Override
            public void onResult(List<User> resultList) {
                for (User user : resultList){
                    sb.append(user.toString());
                    sb.append("\n");
                }
                textView.setText(sb.toString());
            }
        },null);
    }

    public void test2(final TextView textView) {
        Logger.d(this, "UniversalTaskTest-test2()");

        new DataProvider<String>().query("HelloWorld", null, new ResultListener<String>() {
            @Override
            public void onResult(List<String> resultList) {
                textView.setText(resultList.get(0));
            }
        },null);
    }


    public void test3(final TextView textView) {
        Logger.d(this, "UniversalTaskTest-test2()");

        final StringBuilder sb = new StringBuilder();
        Map<String,String> valueMap = new HashMap<>(1);
        valueMap.put("name","邹渊博");
        new DataProvider<Event>().query("getEventByName", valueMap, new ResultListener<Event>() {
            @Override
            public void onResult(List<Event> resultList) {
                for (Event event : resultList){
                    sb.append(event.toString());
                    sb.append("\n");
                }
                textView.setText(sb.toString());
            }
        },null);
    }


    public void test4(final TextView textView) {
        Logger.d(this, "UniversalTaskTest-test2()");

        Map<String,String> valueMap = new HashMap<>(7);
        valueMap.put("name","邹渊博");
        valueMap.put("status","LEVEL_SOFTWARELEADER");
        valueMap.put("type","事假");
        valueMap.put("begin","2017-11-23");
        valueMap.put("end","2017-11-25");
        valueMap.put("days","2");
        valueMap.put("remark","封装库测试0.2");
        new DataProvider<String>().query("addEvent", valueMap, new ResultListener<String>() {
            @Override
            public void onResult(List<String> resultList) {
                textView.setText(resultList.get(0));
            }
        },null);
    }
}
