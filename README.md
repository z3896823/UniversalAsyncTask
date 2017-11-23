## UniversalAsyncTask

本lib搭建了服务端WebService与客户端之间的桥梁。使用AsyncTask作为异步请求的方式，使用反射根据class对象生成对象，最终返回一个List<Object>，调用方只需要对返回的列表元素进行强制转换即可使用。

对象实例化遵循一定的规约，使用BeanMethodAnnotation注解对JavaBean的方法进行标记，以保证反射时获取到的方法列表呈现一定的顺序。

>编译时，JVM有权决定方法和属性的顺序，因此反射并不能保证获得的方法是什么样的顺序，经过测试，同样的类在不同的Java版本下反射方法的返回顺序不同。
>
>Java doc中的原文是:The elements in the returned array are not sorted and are not in any particular order.

由于WebService的方法参数分为有参与无参，本库提供一个Map参数来标志WebService调用时的形参列表。其中key为参数名，value为参数值。

返回值分为单一的标志位（如『success』等）和一系列对象的属性。如果是后者，生成对象后封装在List中返回，如果是前者，也封装在List中返回。调用方自行判断返回值是什么然后采取相应的处理措施即可。