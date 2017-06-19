package cn.cenbylin.mp.wcapi.message;

import cn.cenbylin.mp.common.utils.XMLUtil;
import cn.cenbylin.mp.wcapi.baseinfo.InfoProvider;
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
@RequestMapping("/mp")
public class DoMessage {
	/**
	 * 微信公众号消息处理
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/doMessage")
	public void doMsg(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//判断是否初次接入
		if(InfoProvider.isDev()){
			PrintWriter out = response.getWriter();
			out.print(request.getParameter("echostr")); 
			out.flush();
			out.close();
		}
		//消息处理
		// 初始项
		response.setContentType("text/xml;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		Map<String, String> map = null;
		map = XMLUtil.xmlToMap(request.getInputStream());
		System.out.println(map);
	}
}
