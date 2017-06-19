package cn.cenbylin.mp.message.controller;

import cn.cenbylin.mp.common.utils.MVCControler;
import cn.cenbylin.mp.common.utils.MessageCreator;
import cn.cenbylin.mp.common.utils.XMLUtil;
import cn.cenbylin.mp.message.po.MessageBean;
import cn.cenbylin.mp.wcapi.baseinfo.InfoProvider;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
	MessageCreator messageCreator;
	Logger logger = Logger.getLogger(DoMessage.class);
	/**
	 * 微信公众号消息处理
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/doMessage.do")
	public void doMsg(HttpServletRequest request, HttpServletResponse response) throws Exception{
		logger.info("isdev:"+InfoProvider.isDev());
		//判断是否初次接入
		if(InfoProvider.isDev()){
			PrintWriter out = response.getWriter();
			out.print(request.getParameter("echostr")); 
			out.flush();
			out.close();
			return;
		}

		//消息处理
		// 初始项
		response.setContentType("text/xml;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		// 初始化消息模型，装载信息
		MessageBean msb = new MessageBean();
		Map<String, String> map = XMLUtil.xmlToMap(request.getInputStream());
		msb.loadMap(map);
		logger.info("got MSG:" + msb.getContent());
		// 直接回复
		String outStr = messageCreator.createTextMessage(msb.getToUserName(),
				msb.getFromUserName(),
				msb.getContent());
		// 输出
		MVCControler.writeString(response, outStr);
	}

	@RequestMapping("/test.do")
	@ResponseBody
	public String test(){
		return "12123123123";
	}
}
