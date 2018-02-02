package sysu.evteam.zyb.universalasynctask;

import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;

/**
 * <pre>
 *     author : zyb
 *     e-mail : hbdxzyb@hotmail.com
 *     time   : 2017/03/26
 *     desc   : 提供SoapHeader验证所需的请求头
 *     version: 1.0
 * </pre>
 */

public class SoapHeaderUtil {

    public static Element[] getHeader(){
        // 创建一个SoapHeader，后面赋值给envelope的headerOut
        String nameSpace = "http://tempuri.org/";
        Element[] header = new Element[1];
        header[0] = new Element().createElement(nameSpace,"MySoapHeader");

        Element username = new Element().createElement(nameSpace,"username");
        username.addChild(Node.TEXT,"EvTeam");
        header[0].addChild(Node.ELEMENT,username);//加到header中去

        Element password = new Element().createElement(nameSpace,"password");
        password.addChild(Node.TEXT,"bwYiPBo^bJWf3Xt");
        header[0].addChild(Node.ELEMENT,password);//加到header中去

        return header;
    }
}
