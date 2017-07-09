package cn.cenbylin.mp.message.processor;

import cn.cenbylin.mp.dev.message.AbstractMessageProcessor;
import org.springframework.stereotype.Component;

/**
 * Created by Cenbylin on 2017/7/9.
 */
@Component
public class SimpleProcessor extends AbstractMessageProcessor {
    @Override
    public Object doText(String openid, String text) {
        return "666";
    }
}
