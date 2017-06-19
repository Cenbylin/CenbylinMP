package cn.cenbylin.mp.common.utils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * xml工具类
 * 
 * @author Cenbylin
 */
public class XMLUtil {
	/**
	 * xml转成Map集合
	 * 
	 * @param xml的输入流
	 * @return 解析后的Map
	 * @throws DocumentException
	 * @throws IOException
	 */
	public static Map<String, String> xmlToMap(InputStream xmlStream)
			throws DocumentException, IOException {
		Map<String, String> map = new HashMap<String, String>();
		SAXReader reader = new SAXReader();
		Document doc = reader.read(xmlStream);
		xmlStream.close();
		Element root = doc.getRootElement();
		@SuppressWarnings("unchecked")
		List<Element> list = root.elements();
		for (Element e : list) {
			map.put(e.getName(), e.getText());
		}
		return map;
	}

	/**
	 * javabean转XML
	 * 
	 * @param javabean
	 * @return
	 */
	public static String BeanToXml(Object javabean) {
		XStream xstream = new XStream(new DomDriver("UTF-8"));

		xstream.alias("xml", javabean.getClass());
		return xstream.toXML(javabean);
	}
	
	public static String map2xmlBody(Map<String, String> vo, String rootElement) {
        org.dom4j.Document doc = DocumentHelper.createDocument();
        Element body = DocumentHelper.createElement(rootElement);
        doc.add(body);
        __buildMap2xmlBody(body, vo);
        return doc.asXML();
    }
     
    @SuppressWarnings("unchecked")
	private static void __buildMap2xmlBody(Element body, Map<String, String> vo) {
        if (vo != null) {
            Iterator<String> it = vo.keySet().iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                if (StringUtils.isNotEmpty(key)) {
                    Object obj = vo.get(key);
                    Element element = DocumentHelper.createElement(key);
                    if (obj != null) {
                        if (obj instanceof String) {
                            element.setText((String) obj);
                        } else {
                            if (obj instanceof Character || obj instanceof Boolean || obj instanceof Number
                                    || obj instanceof java.math.BigInteger || obj instanceof java.math.BigDecimal) {
                                org.dom4j.Attribute attr = DocumentHelper.createAttribute(element, "type", obj.getClass().getCanonicalName());
                                element.add(attr);
                                element.setText(String.valueOf(obj));
                            } else if (obj instanceof Map) {
                                org.dom4j.Attribute attr = DocumentHelper.createAttribute(element, "type", Map.class.getCanonicalName());
                                element.add(attr);
                                __buildMap2xmlBody(element, (Map<String, String>) obj);
                            } else {
                            }
                        }
                    }
                    body.add(element);
                }
            }
        }
    }
}
