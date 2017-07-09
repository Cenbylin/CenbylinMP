package cn.cenbylin.mp.common.utils;

import java.util.Date;

/**
 * Created by Cenbylin on 2017/6/19.
 */
public class MessageCreator {
    /**
     * 生成公众号发出消息的xml文本
     * @param from
     * @param to
     * @param text
     * @return
     */
    public String createTextMessage(String from, String to, String text){
        StringBuffer sb = new StringBuffer("<xml><ToUserName>");
        sb.append(to).append("</ToUserName><FromUserName>").append(from)
                .append("</FromUserName><CreateTime>")
                .append(Long.toString(new Date().getTime()))
                .append("</CreateTime><MsgType>text</MsgType><Content>")
                .append(text).append("</Content></xml>");
        return sb.toString();
    }

    public static void main(String[] args) {
        P a = new C();
        System.out.println(a.doit());
    }
}
abstract class P{
    public abstract Object doit();
}
class C extends P{
    @Override
    public String doit() {
        return "asa";
    }
}
