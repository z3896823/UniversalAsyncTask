package sysu.evteam.zyb.universalasynctask;

/**
 * <pre>
 *     @author: zyb
 *     email  : hbdxzyb@hotmail.com
 *     time   : 2017/11/23 下午6:07
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class Event {

    private String id;
    private String name;
    private String level;
    private String type;
    private String begin;
    private String end;
    private String duration;
    private String remark;

    public String getId() {
        return id;
    }

    @BeanMethodAnnotation(order = 1)
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @BeanMethodAnnotation(order = 2)
    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    @BeanMethodAnnotation(order = 3)
    public void setLevel(String level) {
        this.level = level;
    }

    public String getType() {
        return type;
    }

    @BeanMethodAnnotation(order = 4)
    public void setType(String type) {
        this.type = type;
    }

    public String getBegin() {
        return begin;
    }

    @BeanMethodAnnotation(order = 5)
    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getEnd() {
        return end;
    }

    @BeanMethodAnnotation(order = 6)
    public void setEnd(String end) {
        this.end = end;
    }

    public String getDuration() {
        return duration;
    }

    @BeanMethodAnnotation(order = 7)
    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getRemark() {
        return remark;
    }

    @BeanMethodAnnotation(order = 8)
    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return super.toString()+"[name:"+name+" type:"+type+" days:"+duration+" remark:"+remark+"]";
    }
}
