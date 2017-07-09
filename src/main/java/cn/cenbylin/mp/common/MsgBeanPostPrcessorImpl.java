package cn.cenbylin.mp.common;

import cn.cenbylin.mp.dev.message.BasicMessageProcessor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.LinkedList;

/**
 * 消息处理器的注入
 * Created by Cenbylin on 2017/7/9.
 */
public class MsgBeanPostPrcessorImpl implements BeanPostProcessor {
    @Autowired
    LinkedList messageProcessorList;
    /**
     * 前处理
     * @param o
     * @param s
     * @return
     * @throws BeansException
     */
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        return o;
    }

    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
        if (o instanceof BasicMessageProcessor){
            System.err.println("拿到一个拿到一个");
            messageProcessorList.add(o);
        }
        return o;
    }
}
