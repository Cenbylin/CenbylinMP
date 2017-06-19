package cn.cenbylin.mp.wcapi.auz;

import cn.cenbylin.mp.common.utils.HttpRequestTool;
import cn.cenbylin.mp.wcapi.baseinfo.IdentityInfo;
import cn.cenbylin.mp.wcapi.baseinfo.InfoProvider;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class WechatApiService {
	
	/**
	 * 网页授权--根据code获得openid等
	 * @param code
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public IdentityInfo getWechatIdentity(String code) {
		String appId = InfoProvider.getAppid();
		String secret = InfoProvider.getSecret();
		//获得授权后回调code
		String openid = null;
		String access_token = null;
		String str = null;
		//由code获取openid和access_token
		str = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appId + "&secret=" + secret + "&code=CODE&grant_type=authorization_code";
		String json = HttpRequestTool.sendGet(str.replace("CODE", code), "utf-8");
		Gson gson = new Gson();
		Map<String, Object> object = gson.fromJson(json, Map.class);
    	openid = object.get("openid").toString();
    	access_token = object.get("access_token").toString();
    	//获得信息
    	str = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
	    json = HttpRequestTool.sendGet(str.replace("ACCESS_TOKEN", access_token).replace("OPENID", openid), "utf-8");
	    gson = new Gson();
	    object = gson.fromJson(json, Map.class);
	    String nickname = object.get("nickname").toString();
	    String sex = object.get("sex").toString();
		String province = object.get("province").toString();
		String city = object.get("city").toString();
		String country = object.get("country").toString();
		String headimgurl = object.get("headimgurl").toString();
    	
    	return new IdentityInfo(openid, access_token, nickname, sex, province, city, country, headimgurl);
	}
	
	/**
	 * 判断是否关注
	 * @param code
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean judgeFollowState(String openid) {
		//由openid用户信息
		String url = new StringBuffer("https://api.weixin.qq.com/cgi-bin/user/info?access_token=")
		.append(InfoProvider.getAccessToken())
		.append("&openid=")
		.append(openid)
		.append("&lang=zh_CN")
		.toString();
		String json = HttpRequestTool.sendGet(url, "utf-8");
		Gson gson = new Gson();
		Map<String, Object> object = gson.fromJson(json, Map.class);
    	Object subscribe = object.get("subscribe");
    	boolean state = false;
    	// 判断已经关注
    	if(subscribe != null && Double.parseDouble(subscribe.toString())!=0){
    		state = true;
    	}
    	return state;
	}
}
