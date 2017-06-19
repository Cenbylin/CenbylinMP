package cn.cenbylin.mp.common.exception.commonresolver;

import cn.cenbylin.mp.common.exception.*;
import cn.cenbylin.mp.common.utils.MVCControler;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


/**
 * 统一异常处理器-简易版本
 * @author Cenby7
 *
 */
public class SimpleExceptionResolver implements HandlerExceptionResolver {

	public ModelAndView resolveException(HttpServletRequest request,
										 HttpServletResponse response, Object handler, Exception exception) {
		//返回结果
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> res = new HashMap<String, Object>();
		res.put("state", false);
		//异常捕捉
		if (exception instanceof NoDataException) {
			res.put("ErrorCode", 66001);
			res.put("ErrorMessage", exception.getMessage());
		}else if (exception instanceof ParamErrorException) {
			res.put("ErrorCode", 66002);
			res.put("ErrorMessage", exception.getMessage());
		}else if (exception instanceof InvalidTokenException) {
			res.put("ErrorCode", 66003);
			res.put("ErrorMessage", exception.getMessage());
		}else if (exception instanceof LoginException) {
			res.put("ErrorCode", 66004);
			res.put("ErrorMessage", exception.getMessage());
		}else if (exception instanceof JudgeException) {
			res.put("ErrorCode", 66005);
			res.put("ErrorMessage", exception.getMessage());
		}else if (exception instanceof OutOfTimeException) {
			res.put("ErrorCode", 66006);
			res.put("ErrorMessage", exception.getMessage());
		}else if (exception instanceof AuthorityException) {
			res.put("ErrorCode", 66007);
			res.put("ErrorMessage", exception.getMessage());
		}else {
			res.put("ErrorCode", 66000);
			res.put("ErrorMessage", "未知错误！");
		}

		//ajax统一返回application/json
		String jsonStr = null;
		try {
			jsonStr = mapper.writeValueAsString(res);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			MVCControler.ajax(jsonStr, "text/html", request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Logger logger = Logger.getLogger(getClass());
		logger.error("Type:"+ exception.getClass().getName() + exception.getMessage(), exception);
		return new ModelAndView();
	}

}
