package cn.cenbylin.mp.wcapi.auz;

import cn.cenbylin.mp.common.Constants;
import cn.cenbylin.mp.common.utils.HttpRequestTool;
import cn.cenbylin.mp.common.utils.SignUtil;
import cn.cenbylin.mp.wcapi.baseinfo.IdentityInfo;
import cn.cenbylin.mp.wcapi.baseinfo.InfoProvider;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/wechat")
public class WechatApi {
	@Autowired
	WechatApiService wechatApiService;

	
	@SuppressWarnings("unchecked")
	@RequestMapping("/openid")
	public @ResponseBody Map<String, Object> getUserOpenid(String code) throws Exception{
		Map<String, Object> res = new HashMap<String, Object>();
		res.put("state", true);
		String appId = InfoProvider.getAppid();
		String secret = InfoProvider.getSecret();
		//获得授权后回调code
		String openid = null;
		String str = null;
		//由code获取openid和access_token
		str = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appId + "&secret=" + secret + "&code=CODE&grant_type=authorization_code";
		String json = HttpRequestTool.sendGet(str.replace("CODE", code), "utf-8");
		Gson gson = new Gson();
		Map<String, Object> object = gson.fromJson(json, Map.class);
    	openid = object.get("openid").toString();
    	System.err.println("openid:"+openid);
		//返回数据
    	res.put("openid", openid);
		return res;
	}
	/**
	 * JSSDK配置
	 * @param url
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/jsConfig")
	public @ResponseBody Map<String, Object> getJsConfig(String url) throws Exception{
		Map<String, Object> res = SignUtil.sign(url);
		return res;
	}
	
	/**
	 * 微信授权回调
	 * @param request
	 * @param code
	 *  授权完成跳转地址
	 * @throws Exception
	 */
	@RequestMapping("/wechatLogin.do")
	public void wechatLogin(HttpServletRequest request, HttpServletResponse response, String code) throws Exception{
		HttpSession session = request.getSession();
		
		// 获取code可能的情况：
		// (1)参数直接带来
		// (2)参数串被转义--反转义
		// (3)无参数串--重定向
		if (code == null) {
			// 重新授权
			Object failRoute1 = session.getAttribute(Constants.URL_HISTORY_KEY);
			String routeStr = failRoute1!=null ? failRoute1.toString() : request.getContextPath() + "/m/membCenter/toMyCourseList.do";
			response.sendRedirect(routeStr);
			Logger logger1 = Logger.getLogger(getClass());
			logger1.error("getCodeFail and send redirect to " + routeStr);
			return;
			/*
			String querySting = request.getQueryString();
			if (querySting==null) {
				//重新授权
				Object failRoute = session.getAttribute(Constants.URL_HISTORY_KEY);
				response.sendRedirect(failRoute!=null?failRoute.toString():"/");
				Logger logger = Logger.getLogger(getClass());
				logger.error("getCodeFail and send redirect to " + failRoute);
				return;
			}else {
				//反转义
				int tag = 0;//防止死循环
				while (querySting.indexOf("%") != -1 && (tag++ < 5)) {
					querySting = URLDecoder.decode(querySting, "utf-8");
				}
				//拿到真正的code
				int pos = querySting.indexOf("&state");
				code = querySting.substring(querySting.indexOf("code=")+"code=".length(), pos!=-1 ? pos:querySting.length());
				Logger logger = Logger.getLogger(getClass());
				logger.error("getCodeFail and get code(" + code + ") from queryString: " + querySting);
			}
			*/
		}
		IdentityInfo identityInfo = wechatApiService.getWechatIdentity(code);
		String openid = identityInfo.getOpenid();
		if (openid == null) {//没有拿到openid，跳转到失败页面并且刷新
			
		}
//		//尝试获得对象
//		FbParent bean = fbParentService.getParentByOpenid(openid);
//		if (bean==null) {
//			bean = new FbParent(openid, filterOffUtf8Mb4(identityInfo.getNickname()));
//			//新增对象
//			fbParentService.addParent(bean);
//		}
		//存session
//		session.setAttribute(Constants.LOGIN_USER_KEY, bean);
		Object route = session.getAttribute(Constants.URL_HISTORY_KEY);
		//登陆完成，回访
		response.sendRedirect(route!=null?route.toString():request.getContextPath() + "/m/membCenter/toMyCourseList.do");
	}
	
	
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		String querySting = "code%25253D011JwERo1D6eMm0JpVUo183KRo1JwERA%252526state%25253D1";
		//反转义
		int tag = 0;//防止死循环
		while (querySting.indexOf("%") != -1 && (tag++ < 5)) {
			querySting = URLDecoder.decode(querySting, "utf-8");
		}
		//拿到真正的code
		int pos = querySting.indexOf("&state");
		String code = querySting.substring(querySting.indexOf("code=")+"code=".length(), pos!=-1 ? pos:querySting.length());
		System.out.println(code);
	}
	static public String filterOffUtf8Mb4(String text) throws UnsupportedEncodingException {  
        byte[] bytes = text.getBytes("utf-8");  
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);  
        int i = 0;  
        while (i < bytes.length) {  
            short b = bytes[i];  
            if (b > 0) {  
                buffer.put(bytes[i++]);  
                continue;  
            }  
            b += 256;  
            if ((b ^ 0xC0) >> 4 == 0) {  
                buffer.put(bytes, i, 2);  
                i += 2;  
            }  
            else if ((b ^ 0xE0) >> 4 == 0) {  
                buffer.put(bytes, i, 3);  
                i += 3;  
            }  
            else if ((b ^ 0xF0) >> 4 == 0) {  
                i += 4;  
            }  
        }  
        buffer.flip();  
        return new String(buffer.array(), "utf-8");  
    }  
}
