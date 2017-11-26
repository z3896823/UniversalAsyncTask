package sysu.evteam.zyb.universalasynctask;

import java.util.List;

/**
 * <pre>
 *     @author: zyb
 *     email  : hbdxzyb@hotmail.com
 *     time   : 2017/11/25 下午1:42
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public interface ResultListener<T> extends UATCallback<T>{

    void onResult(List<T> resultList);
}
