package cn.cenbylin.mp.wcapi.payment;

import cn.cenbylin.mp.common.utils.HttpRequestTool;
import cn.cenbylin.mp.common.utils.MD5Tool;
import cn.cenbylin.mp.common.utils.SignUtil;
import cn.cenbylin.mp.common.utils.XMLUtil;
import cn.cenbylin.mp.wcapi.baseinfo.InfoProvider;
import org.dom4j.DocumentException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;




public class TradeCreator {
	private Map<String, String> map = new HashMap<String, String>();
	public TradeCreator(){
		map.put("nonce_str", MD5Tool.getMD5(UUID.randomUUID().toString()));
		//map.put("nonce_str", "ibuaiVcKdpRxkhJA");
		map.put("device_info", "WEB");
		//map.put("device_info", "1000");
	}
	public void setAppid(String appid){
		map.put("appid", appid);
	}
	public void setAttach(String attach){
		map.put("attach", attach);
	}
	public void setBody(String body){
		map.put("body", body);
	}
	public void setMchId(String mchId){
		map.put("mch_id", mchId);
	}
	public void setOpenid(String openid){
		map.put("openid", openid);
	}
	public void setNotifyUrl(String notifyUrl){
		map.put("notify_url", notifyUrl);
	}
	public void setOutTradeNo(String outTradeNo){
		map.put("out_trade_no", outTradeNo);
	}
	public void setSpbillCreateIp(String spbillCreateIp){
		map.put("spbill_create_ip", spbillCreateIp);
	}
	public void setTotalFee(String totalFee){
		map.put("total_fee", totalFee);
	}
	public void setTradeType(String tradeType){
		map.put("trade_type", tradeType);
	}
	
	public Map<String, String> doTrade() throws DocumentException, IOException{
		String key = InfoProvider.getKey();
		String sign = SignUtil.sign(map, key);
		map.put("sign", sign);
		String dataString = XMLUtil.map2xmlBody(map, "xml");
		String xmlString = HttpRequestTool.sendPost("https://api.mch.weixin.qq.com/pay/unifiedorder", dataString, "utf-8");
		InputStream in_withcode = new ByteArrayInputStream(xmlString.getBytes("UTF-8"));
		Map<String, String> resMap = XMLUtil.xmlToMap(in_withcode);
		return resMap;
	}
	
}
