package cn.cenbylin.mp.wcapi.baseinfo;

import cn.cenbylin.mp.common.utils.HttpRequestTool;
import com.google.gson.Gson;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InfoProvider {
	private static String appid;
	private static String secret;
	private static String accessToken;
	private static String jsapiTicket;
	private static String mchid;
	private static String key;
	private static boolean isDev;
	private static String guideFollow;
	static{
		loadConfig();//加载
		//定时任务线程
		Runnable runnable = new Runnable() {
			@SuppressWarnings("unchecked")
			public void run() {
				int timeInterval = 7000000;//时间(毫秒)
				while (true) {
					String json = HttpRequestTool.sendGet(
							"https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
									+ appid + "&secret=" + secret, "utf-8");
					//解析数据
					Gson gson = new Gson();
					Map<String, Object> jsonObject = gson.fromJson(json, Map.class);
					try {
						// 调用一系列get方法获取object的直接子对象
						accessToken = jsonObject.get("access_token").toString();
						timeInterval = ((Double)jsonObject.get("expires_in")).intValue()*1000 - 10000;
						System.err.println("刷新token时间间隔："+timeInterval);
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("获取token出错"+json);
					}
					
					String json1 = HttpRequestTool.sendGet(
							"https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="
									+ accessToken + "&type=jsapi", "utf-8");
					//解析数据
					Map<String, Object> jsonObject1 = gson.fromJson(json1, Map.class);
					try {
						// 调用一系列get方法获取object的直接子对象
						jsapiTicket = jsonObject1.get("ticket").toString();
					} catch (Exception e) { }
					
					try {
						Thread.sleep(timeInterval);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		Thread thread = new Thread(runnable);
		thread.start();
	}
	public static String getAccessToken() {
		return accessToken;
	}
	public static String getAppid(){
		return appid;
	}
	public static String getSecret(){
		return secret;
	}
	public static String getMchId(){
		return mchid;
	}
	public static String getKey(){
		return key;
	}
	public static String getJsapiTicket(){
		return jsapiTicket;
	}
	public static boolean isDev() {
		return isDev;
	}
	public static String getGuideFollow() {
		return guideFollow;
	}
	@SuppressWarnings({ "unchecked", "unused" })
	/**
	 * 从配置文件中装载核心信息
	 */
	public static void loadConfig(){
		Element root = null;
		Map<String, String> map = new HashMap<String, String>();
		SAXReader reader = new SAXReader();
		try {
			InputStream xmlStream = InfoProvider.class.getClassLoader().getResourceAsStream("config/wechat-config.xml");
			Document doc = reader.read(xmlStream);	
			xmlStream.close();
			root = doc.getRootElement();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (DocumentException e2){
			
		}

		List<Element> list = root.elements();
		for (Element e : list) {
			if(e.getName().equals("appid")){
				appid = e.getStringValue();
			}else if(e.getName().equals("secret")){
				secret = e.getStringValue();
			}else if(e.getName().equals("mchid")){
				mchid = e.getStringValue();
			}else if(e.getName().equals("key")){
				key = e.getStringValue();
			}else if(e.getName().equals("dev")){
				isDev = Boolean.parseBoolean(e.getStringValue());
			}else if(e.getName().equals("guideFollow")){
				guideFollow = e.getStringValue();
			}
		}
	}

}
