package sysu.evteam.zyb.universalasynctask;

/**
 * <pre>
 *     @author: zyb
 *     email  : hbdxzyb@hotmail.com
 *     time   : 2017/11/23 下午4:45
 *     desc   :
 *     version: 1.0
 * </pre>
 */

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

    public String getGroup() {
        return group;
    }

    @BeanMethodAnnotation(order = 6)
    public void setGroup(String group) {
        this.group = group;
    }

    public String getAdmin() {
        return admin;
    }

    @BeanMethodAnnotation(order = 7)
    public void setAdmin(String admin) {
        this.admin = admin;
    }

    @Override
    public String toString() {
        return super.toString() + "[" + name + " psw:" + password + " tel:" + tel + " level:" + level + "]";
    }

}
