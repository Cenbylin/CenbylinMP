package cn.cenbylin.mp.wcapi.payment;

import cn.cenbylin.mp.common.utils.SignUtil;
import cn.cenbylin.mp.wcapi.baseinfo.InfoProvider;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

@Service
public class DoPayService {
	/**
	 * 给request注入支付信息
	 * @param request
	 * @param openid
	 * @param fee 以分为单位
	 * @param notifyUrl 回调Url
	 * @param param
	 * @throws Exception
	 */
	public void setPayRequestInfo(HttpServletRequest request, String openid, Integer fee, String notifyUrl, String param) throws Exception {
		//获得openid
		Random random = new Random();
		//统一下单
		TradeCreator tradeCreator = new TradeCreator();
		tradeCreator.setAppid(InfoProvider.getAppid());
		tradeCreator.setBody("online payment");
		tradeCreator.setOpenid(openid);
		tradeCreator.setAttach(param);
		tradeCreator.setMchId(InfoProvider.getMchId());
		tradeCreator.setNotifyUrl(notifyUrl);
		tradeCreator.setOutTradeNo(String.valueOf(Math.abs(random.nextInt())));
		tradeCreator.setSpbillCreateIp(request.getRemoteAddr());
		tradeCreator.setTotalFee(String.valueOf(fee));
		tradeCreator.setTradeType("JSAPI");
		//下单
		Map<String, String> res = tradeCreator.doTrade();
		Map<String, String> paramMap = new LinkedHashMap<String, String>();
		paramMap.put("appId", res.get("appid").toString());
		paramMap.put("timeStamp", String.valueOf(System.currentTimeMillis()));
		paramMap.put("nonceStr", res.get("nonce_str").toString());
		paramMap.put("package", "prepay_id=" + res.get("prepay_id").toString());
		paramMap.put("signType", "MD5");
		
		//用于签名的map
		Map<String, String> signMap = new HashMap<String, String>();
		signMap.put("appId", res.get("appid").toString());
		signMap.put("timeStamp", paramMap.get("timeStamp").toString());
		signMap.put("nonceStr", res.get("nonce_str").toString());
		signMap.put("package", "prepay_id=" + res.get("prepay_id").toString());
		signMap.put("signType", "MD5");
		String key = InfoProvider.getKey();
		//拿到签名
		paramMap.put("paySign", SignUtil.sign(signMap, key));
		GsonBuilder gb = new GsonBuilder();
		gb.disableHtmlEscaping();
		request.setAttribute("payParam", gb.create().toJson(paramMap));
		//System.out.println(gson.toJson(paramMap));
		//MVCControler.doRoute("/pay.jsp", request, response);

	}

}
