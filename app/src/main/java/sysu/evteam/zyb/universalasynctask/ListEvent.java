package sysu.evteam.zyb.universalasynctask;

import java.util.List;

/**
 * <pre>
 *     @author: zyb
 *     email  : hbdxzyb@hotmail.com
 *     time   : 2018/1/26 下午4:57
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class ListEvent<T> {

    public static final int TYPE_USER = 0;
    public static final int TYPE_EVENT = 1;
    public static final int TYPE_ATTENDANCE_OF_ALL = 2;
    public static final int TYPE_ATTENDANCE_OF_ONE = 3;
    public static final int TYPE_CURRENTATTENDANCE = 4;
    public static final int TYPE_EVENT_OF_THIS_WEEK = 5;

    private int type;

    private List<T> dataList;

    public ListEvent() {
    }

    public ListEvent(int type) {
        this.type = type;
    }

    public ListEvent(int type, List<T> dataList) {
        this.type = type;
        this.dataList = dataList;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    @Override
    public String toString() {
        if (dataList == null || dataList.size() == 0) {
            return "data list object is null";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < dataList.size(); i++) {
            sb.append(dataList.get(i).toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}
