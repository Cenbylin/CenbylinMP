package cn.cenbylin.mp.common.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


public class MVCControler {
	public static void doRoute(String route, HttpServletRequest request, HttpServletResponse response){
		//如果有路由就转发视图
		if(StringUtils.isNotEmpty(route)){
			try {
				request.getRequestDispatcher(route).forward(request, response);
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**  
     * AJAX输出，返回null  
     * @param content - 输出内容  
     * @return type - 输出类型  
     */  
    public static void ajax(String content, String type, HttpServletRequest request, HttpServletResponse response) {   
        try {   
            response.setContentType(type + ";charset=UTF-8");   
            response.setHeader("Pragma", "No-cache");   
            response.setHeader("Cache-Control", "no-cache");   
            response.setDateHeader("Expires", 0);   
            response.getWriter().write(content);   
            response.getWriter().flush();   
        } catch (IOException e) {   
            e.printStackTrace();   
        }  
    }

    /**
     * 直接写出字符流
     * @param response
     * @param content
     * @throws Exception
     */
    public static void writeString(HttpServletResponse response, String content) throws Exception{
        PrintWriter out = response.getWriter();
        out.write(content);
        out.flush();
        out.close();
    }
}
