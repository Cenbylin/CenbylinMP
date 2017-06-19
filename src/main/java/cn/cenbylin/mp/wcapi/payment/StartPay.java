package cn.cenbylin.mp.wcapi.payment;

import cn.cenbylin.mp.common.utils.HttpRequestTool;
import cn.cenbylin.mp.common.utils.MVCControler;
import cn.cenbylin.mp.wcapi.baseinfo.InfoProvider;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;


public class StartPay extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	@SuppressWarnings("unchecked")
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		String appId = InfoProvider.getAppid();
		String secret = InfoProvider.getSecret();
		// 获得授权后回调code
		String code = request.getParameter("code");
		String openid = null;
		String str = null;
		// 由code获取openid和access_token
		str = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
				+ appId + "&secret=" + secret
				+ "&code=CODE&grant_type=authorization_code";
		String json = HttpRequestTool.sendGet(str.replace("CODE", code),
				"utf-8");
		Gson gson = new Gson();
		Map<String, Object> object = gson.fromJson(json, Map.class);
		openid = object.get("openid").toString();

		request.setAttribute("openid", openid);
		MVCControler.doRoute("/input.jsp", request, response);
	}

}
