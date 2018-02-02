package sysu.evteam.zyb.universalasynctask;

/**
 * <pre>
 *     @author: zyb
 *     email  : hbdxzyb@hotmail.com
 *     time   : 2017/11/26 下午10:16
 *     desc   : 似乎是由于 Java 的特性，当获取一个带泛型的类的单行类型时，只能获得其父类的泛型类型
 *     所以在这里让 ResultListener 继承了一个接口，纯粹是为了获取泛型的类型
 *     version: 1.0
 * </pre>
 */

public interface UATCallback<T> {
}
