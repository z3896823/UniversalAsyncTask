package sysu.evteam.zyb.universalasynctask;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 *     @author: zyb
 *     email  : hbdxzyb@hotmail.com
 *     time   : 2017/11/23 下午3:09
 *     desc   :
 *     version: 1.0
 * </pre>
 */


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BeanMethodAnnotation {
    int order();
}
