## UniversalAsyncTask

### 简介

本lib搭建了服务端WebService与客户端之间的桥梁。使用AsyncTask作为异步请求的方式，使用反射根据传入的泛型生成对象，最终返回一个List<Object>，调用方可对返回的结果对象直接使用。

对象实例化遵循一定的规约，使用BeanMethodAnnotation注解对JavaBean的方法进行标记，以保证反射时获取到的方法列表呈现一定的顺序。

>编译时，JVM有权决定方法和属性的顺序，因此反射并不能保证获得的方法是什么样的顺序，经过测试，同样的类在不同的Java版本下反射方法的返回顺序不同。
>
>Java doc中的原文是:The elements in the returned array are not sorted and are not in any particular order.

由于WebService的方法参数分为有参与无参，本库提供一个Map参数来标志WebService调用时的形参列表。其中key为参数名，value为参数值。

返回值分为单一的标志位（如『success』等）和一系列对象的属性。如果是后者，生成对象后封装在List中返回，如果是前者，也封装在List中返回。调用方在调用WebService方法前应该对返回值有一定的预期，如果类型不对，本库的安全检查会打印异常信息，异常信息为『结果解析与结果预期不同...』。

为方便调试，我在自己的服务器上专门开了一个82端口。具体见项目源码。



#### 关于异步任务的管理

暂时使用异步任务自带的管理方式：使用并发数为5的线程池。

需要注意的是，AsyncTask设计之初就表明它仅适合一些几秒钟就完成的异步请求场景，对于需要长时间在后台运行的任务，**不要**使用AsyncTask，这是因为AsyncTask对于后台线程的管理机制决定的。

> [异步任务线程管理机制](http://blog.csdn.net/hitlion2008/article/details/7983449)



### 导入方法

1. build.gradle(project)

```groovy
allprojects {
    repositories {
        google()
        jcenter()
        maven { url 'https://jitpack.io' }
        maven { url 'https://oss.sonatype.org/content/repositories/ksoap2-android-releases' }
    }
}
```

2. build.gradle(module)

```groovy
    compile 'com.github.z3896823:UniversalAsyncTask:v0.1'
```

### 使用范例

1. JavaBean：

```java
public class User {

    private String name;
    private String password;
    private String tel;
    private String email;
    private String level;
    private String group;
    private String admin;

    public String getName() {
        return name;
    }

    @BeanMethodAnnotation(order = 1)
    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    @BeanMethodAnnotation(order = 2)
    public void setPassword(String password) {
        this.password = password;
    }

    public String getTel() {
        return tel;
    }

    @BeanMethodAnnotation(order = 3)
    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    @BeanMethodAnnotation(order = 4)
    public void setEmail(String email) {
        this.email = email;
    }

    public String getLevel() {
        return level;
    }

    @BeanMethodAnnotation(order = 5)
    public void setLevel(String level) {
        this.level = level;
    }
}
```

2. MainActivity.java

```java
    

DataProvider.initial("WSDL","namespace");

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
        valueMap.put("name","xxx");
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
        valueMap.put("name","xxx");
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
```

