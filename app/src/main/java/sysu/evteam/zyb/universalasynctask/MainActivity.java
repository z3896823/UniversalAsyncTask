package sysu.evteam.zyb.universalasynctask;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sysu.evteam.zyb.universalasynctask.data.ListData;

public class MainActivity extends AppCompatActivity {

    private static final String TAG_USER_LIST = "a";
    private static final String TAG_HELLO_WORLD = "b";

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataProvider.initial("http://120.24.17.120:84/schedulepro.asmx", "http://tempuri.org/");

        Button btn_1 = findViewById(R.id.id_btn_1);// 无参，返回对象列表
        Button btn_2 = findViewById(R.id.id_btn_2);// 无参，返回标志位
        Button btn_3 = findViewById(R.id.id_btn_3);// 有参，返回对象列表
        Button btn_4 = findViewById(R.id.id_btn_4);// 有参，返回标志位
        tv = findViewById(R.id.id_tv_result);

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
        },SoapHeaderUtil.getHeader());
    }

    public void test2(final TextView textView) {
        Logger.d(this, "UniversalTaskTest-test2()");

        new DataProvider<String>().query("HelloWorld", null, new ResultListener<String>() {
            @Override
            public void onResult(List<String> resultList) {
                textView.setText(resultList.get(0));
            }
        },SoapHeaderUtil.getHeader());
    }

    public void test3(final TextView textView) {
        Logger.d(this, "UniversalTaskTest-test3()");
        new DataProvider<User>().query("getUserList", null, User.class,SoapHeaderUtil.getHeader(),TAG_USER_LIST);
    }

    public void test4(final TextView textView) {
        Logger.d(this, "UniversalTaskTest-test4()");
        new DataProvider<String>().query("HelloWorld", null, String.class, SoapHeaderUtil.getHeader(),TAG_HELLO_WORLD);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserList(ListData<User> reuslt){
        if (reuslt.getTag().equals(TAG_USER_LIST)){
            StringBuilder sb = new StringBuilder();
            List<User> userList = reuslt.getDataList();
            for (User user : userList){
                sb.append(user.toString());
                sb.append("\n");
            }
            tv.setText(sb.toString());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHelloWorld(ListData<String> reuslt){
        if (reuslt.getTag().equals(TAG_HELLO_WORLD)){
            tv.setText(reuslt.getDataList().get(0));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
