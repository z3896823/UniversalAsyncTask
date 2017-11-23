package sysu.evteam.zyb.universalasynctask;

import java.util.List;

/**
 * @author : zyb
 *         email : hbdxzyb@hotmail.com
 *         time   : 2017/11/16 下午10:08
 *         desc   :
 *         version: 1.0
 */

public interface OnResultListener {

    /**
     * 统一的回调接口，每个调用者实现这个接口来接收异步任务的返回结果，然后自己转型
     *
     * @param o result
     */
    void onResult(List<Object> resultList);
}
