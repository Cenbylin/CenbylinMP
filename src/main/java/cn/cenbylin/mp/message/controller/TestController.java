package cn.cenbylin.mp.message.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Cenbylin on 2017/6/19.
 */
@Controller
@RequestMapping("/test")
public class TestController {
    @RequestMapping("/1.do")
    public @ResponseBody Map<String, Object> test(){
        Map<String, Object> res = new HashMap<String, Object>();
        res.put("1",1);
        return res;
    }
}
