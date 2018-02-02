package sysu.evteam.zyb.universalasynctask.data;

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

public class ListData<T> {

    private String tag;

    private List<T> dataList;

    public ListData() {
    }

    public ListData(String tag) {
        this.tag = tag;
    }

    public ListData(String tag, List<T> dataList) {
        this.tag = tag;
        this.dataList = dataList;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
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
            return "listdata object is null";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < dataList.size(); i++) {
            sb.append(dataList.get(i).toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}
