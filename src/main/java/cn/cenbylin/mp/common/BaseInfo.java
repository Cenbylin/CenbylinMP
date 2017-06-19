package cn.cenbylin.mp.common;
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


public class BaseInfo {
	static{
		loadConfig();//加载
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
			InputStream xmlStream = BaseInfo.class.getClassLoader().getResourceAsStream("base-config.xml");
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
		for (Element element : list) {
			List<Element> userList = element.elements();
			String username = null;
			String password = null;
			int city = 0;
			for (Element e : userList) {
				if(e.getName().equals("username")){
					username = e.getStringValue();
				}else if(e.getName().equals("password")){
					password = e.getStringValue();
				}else if(e.getName().equals("city")){
					city = Integer.valueOf(e.getStringValue());
				}
			}

		}
	}

}
