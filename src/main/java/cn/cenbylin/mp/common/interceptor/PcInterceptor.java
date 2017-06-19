package cn.cenbylin.mp.common.interceptor;

import cn.cenbylin.mp.common.annotation.NoPcAuthorization;
import cn.cenbylin.mp.common.annotation.PcAuthorization;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


public class PcInterceptor implements HandlerInterceptor {
	private List<String> excludedUrls;

	public List<String> getExcludedUrls() {
		return excludedUrls;
	}

	public void setExcludedUrls(List<String> excludedUrls) {
		this.excludedUrls = excludedUrls;
	}

	/**
	 * 
	 * @Description: 在业务处理器处理请求之前被调用 如果返回false
	 *               从当前的拦截器往回执行所有拦截器的afterCompletion(), 再退出拦截器链, 如果返回true
	 *               执行下一个拦截器, 直到所有的拦截器都执行完毕 再执行被拦截的Controller 然后进入拦截器链,
	 *               从最后一个拦截器往回执行所有的postHandle()
	 *               接着再从最后一个拦截器往回执行所有的afterCompletion()
	 * @param request
	 * @param response
	 * @return: boolean
	 */
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		//PC授权验证
		//获得controller
		Class<?> clazz = null;
		if (handler instanceof HandlerMethod) {
			clazz = ((HandlerMethod)handler).getBean().getClass();
		}else {
			clazz = handler.getClass();
		}
		//拿到方法注解
		NoPcAuthorization methodAno = ((HandlerMethod)handler).getMethodAnnotation(NoPcAuthorization.class);
		//拿到注解
		PcAuthorization ano = clazz.getAnnotation(PcAuthorization.class);
		if(ano==null || methodAno!=null){//没有验证注解，放行
			return true;
		}
		System.out.println("拦截到PC端授权");
		//如果handle有该注解
		Object user = request.getSession().getAttribute("loginManager");
		if (user != null) {//已经登陆，放行
			return true;
		}
		//组装登陆地址
		StringBuffer newUrl = new StringBuffer("http://").append(request.getServerName());
		newUrl.append(request.getContextPath());
		newUrl.append("/pc/user/toLogin.do");
		//重定向到登陆页
		response.sendRedirect(newUrl.toString());
		return false;
	}
	
	// 在业务处理器处理请求执行完成后,生成视图之前执行的动作
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	/**
	 * 
	 * @Description: 在DispatcherServlet完全处理完请求后被调用 当有拦截器抛出异常时,
	 *               会从当前拦截器往回执行所有的拦截器的afterCompletion()
	 * @param request
	 * @param response
	 * @param handler
	 * @param ex
	 * @author: SongJia
	 * @date: 2016-6-27 下午4:27:51
	 */
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}
}
