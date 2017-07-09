package cn.cenbylin.mp.message.controller;

import cn.cenbylin.mp.common.utils.MVCControler;
import cn.cenbylin.mp.common.utils.XMLUtil;
import cn.cenbylin.mp.message.MessageJobExecutor;
import cn.cenbylin.mp.message.po.MessageBean;
import cn.cenbylin.mp.wcapi.baseinfo.InfoProvider;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

/**
 * 消息主处理器
 * @author Cenby7
 *
 */
@Controller
@RequestMapping("/msg")
public class DoMessage {
	@Autowired
    MessageJobExecutor messageJobExecutor;
    Logger logger = Logger.getLogger(DoMessage.class);

	/**
	 * 微信公众号消息处理
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/doMessage.do")
	public void doMsg(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//1、预处理
        preDoMsg(request, response);

	    //2、初始化消息模型，装载信息
		MessageBean msb = new MessageBean();
		Map<String, String> map = XMLUtil.xmlToMap(request.getInputStream());
		msb.loadMap(map);
		logger.info("got MSG:" + msb.getContent());

		//3、直接返回success(在客服接口异步处理)
        MVCControler.writeString(response, "success");

        //4、加入处理队列
        messageJobExecutor.addJob(msb);

        //5、唤醒
        if (messageJobExecutor.wait){
            synchronized (messageJobExecutor){
                messageJobExecutor.wait = false;
                messageJobExecutor.notify();
            }
        }
	}

    /**
     * 预处理（开发者接入等）
     * @param request
     * @param response
     * @throws Exception
     */
    private void preDoMsg(HttpServletRequest request, HttpServletResponse response) throws Exception{
        logger.info("isdev:"+InfoProvider.isDev());
        //判断是否初次接入
        if(InfoProvider.isDev()){
            PrintWriter out = response.getWriter();
            out.print(request.getParameter("echostr"));
            out.flush();
            out.close();
            return;
        }
        response.setContentType("text/xml;charset=utf-8");
        request.setCharacterEncoding("utf-8");
    }
}
