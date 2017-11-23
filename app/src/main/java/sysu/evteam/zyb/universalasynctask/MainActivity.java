package sysu.evteam.zyb.universalasynctask;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_list = findViewById(R.id.id_btn_list);
        Button btn_single = findViewById(R.id.id_btn_single);
        final TextView tv = findViewById(R.id.id_tv_result);

        btn_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test1(tv);
            }
        });

        btn_single.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test2(tv);
            }
        });
    }

    public void test1(final TextView textView) {
        Logger.d(this, "UniversalTaskTest-test()");

        final StringBuilder sb = new StringBuilder();
        DataProvider.initial("http://39.108.72.65:81/schedulepro.asmx", "http://tempuri.org/");
        DataProvider.getInstance().execute("getUserList", null, User.class, new OnResultListener() {
            @Override
            public void onResult(List<Object> resultList) {
                Logger.d(this, "监听中收到消息");
                // 判断收到的消息是什么类型
                if (resultList.get(0) instanceof User) {
                    User user;
                    for (Object o : resultList) {
                        user = (User) o;
                        sb.append(user.toString()+"\n");
                    }
                } else if (resultList.get(0) instanceof String){
                    sb.append(resultList.get(0).toString());
                } else {
                    sb.append("无法识别收到的数据");
                }
                textView.setText(sb.toString());
                Logger.d(this,sb.toString());
            }
        });
    }

    public void test2(final TextView textView) {
        Logger.d(this, "UniversalTaskTest-test()");

        final StringBuilder sb = new StringBuilder();
        DataProvider.initial("http://39.108.72.65:81/schedulepro.asmx", "http://tempuri.org/");
        DataProvider.getInstance().execute("HelloWorld", null, User.class, new OnResultListener() {
            @Override
            public void onResult(List<Object> resultList) {
                Logger.d(this, "监听中收到消息");
                // 判断收到的消息是什么类型
                if (resultList.get(0) instanceof User) {
                    User user;
                    for (Object o : resultList) {
                        user = (User) o;
                        sb.append(user.toString()+"\n");
                    }
                } else if (resultList.get(0) instanceof String){
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
