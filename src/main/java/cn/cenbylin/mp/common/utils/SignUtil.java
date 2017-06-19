package cn.cenbylin.mp.common.utils;

import cn.cenbylin.mp.wcapi.baseinfo.InfoProvider;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;



public class SignUtil {
    public static void main(String[] args) {
        try {
			Class.forName("com.yysale.member.api.InnerInfo");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}//内置信息对象的加载
		while(StringUtils.isEmpty(InfoProvider.getJsapiTicket())){
			try{
				Thread.sleep(1000);//等待1秒
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		System.out.println(InfoProvider.getAccessToken());
		System.out.println(InfoProvider.getJsapiTicket());
        /*
        // 注意 URL 一定要动态获取，不能 hardcode
        String url = "http://example.com";
        Map<String, String> ret = sign(jsapi_ticket, url);
        for (Map.Entry entry : ret.entrySet()) {
            System.out.println(entry.getKey() + ", " + entry.getValue());
        }
        */
    };
    
    
    /**
     * JSSDK-获得所有验证参数
     * @param url
     * @return
     */
    public static Map<String, Object> sign(String url) {
    	while(StringUtils.isEmpty(InfoProvider.getJsapiTicket())){
			try{
				Thread.sleep(1000);//等待1秒
			}catch(Exception e){
				e.printStackTrace();
			}
		}
    	String jsapi_ticket = InfoProvider.getJsapiTicket();
    	
    	Map<String, Object> ret = new HashMap<String, Object>();
        String nonce_str = create_nonce_str();
        String timestamp = create_timestamp();
        String string1;
        String signature = "";

        //注意这里参数名必须全部小写，且必须有序
        string1 = "jsapi_ticket=" + jsapi_ticket +
                  "&noncestr=" + nonce_str +
                  "&timestamp=" + timestamp +
                  "&url=" + url;
        System.out.println(string1);

        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        ret.put("url", url);
        //ret.put("jsapi_ticket", jsapi_ticket);
        ret.put("appid", InfoProvider.getAppid());
        ret.put("nonceStr", nonce_str);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);

        return ret;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    private static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    private static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }
    /**
     * 微信商户，获得签名
     * @param appid
     * @param mch_id
     * @param device_info
     * @param body
     * @param nonce_str
     * @param key
     * @return
     */
    public static String sign(Map<String, String> paraMap, String key) {
        String string1 = null;
        
        //注意这里参数名必须全部小写，且必须有序
        string1 = formatParaMap(paraMap)+"&key=" + key;
        return  MD5Tool.getMD5(string1).toUpperCase();
    }

	/**
	 * 
	 * 方法用途: 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序），并且生成url参数串<br>
	 * 实现步骤: <br>
	 * 
	 * @param paraMap
	 *            要排序的Map对象
	 * @param urlEncode
	 *            是否需要URLENCODE
	 * @param keyToLower
	 *            是否需要将Key转换为全小写 true:key转化成小写，false:不转化
	 * @return
	 */
	public static String formatParaMap(Map<String, String> paraMap) {
		String buff = "";
		Map<String, String> tmpMap = paraMap;
		try {
			List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(
					tmpMap.entrySet());
			// 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
			Collections.sort(infoIds,
					new Comparator<Map.Entry<String, String>>() {

						public int compare(Map.Entry<String, String> o1,
								Map.Entry<String, String> o2) {
							return (o1.getKey()).toString().compareTo(
									o2.getKey());
						}
					});
			// 构造URL 键值对的格式
			StringBuilder buf = new StringBuilder();
			for (Map.Entry<String, String> item : infoIds) {
				if (StringUtils.isNotBlank(item.getKey())) {
					String key = item.getKey();
					String val = item.getValue();
					buf.append(key + "=" + val);
					buf.append("&");
				}

			}
			buff = buf.toString();
			if (buff.isEmpty() == false) {
				buff = buff.substring(0, buff.length() - 1);
			}
		} catch (Exception e) {
			
		}
		return buff;
	}
}
