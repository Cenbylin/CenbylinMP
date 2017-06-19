package cn.cenbylin.mp.common;

public class Constants {
	/**登陆对象的key（session）*/
	public static final String LOGIN_USER_KEY = "loginUser";
	/**PC端登陆对象的key（session*/
	public static final String LOGIN_MANAGER_KEY = "loginManager";
	/**微信授权重定向模板（APPID, URL）*/
	public static final String RED_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=URL&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect";
	/**历史链接的Key(session)*/
	public static final String URL_HISTORY_KEY = "historyUrl";
	
	/**活动-即将报名*/
	public static final int ACTIVITY_STATE_PRE = 1;
	/**活动-正在报名*/
	public static final int ACTIVITY_STATE_DOING = 2;
	/**活动-报名结束*/
	public static final int ACTIVITY_STATE_DONE = 3;
	/**
	 * 活动-即将报名
	 */
	public static final String ACTIVITY_STATE_PRE_TXT = "即将报名";
	/**
	 * 活动-正在报名
	 */
	public static final String ACTIVITY_STATE_DOING_TXT = "正在报名";
	/**
	 * 活动-报名结束
	 */
	public static final String ACTIVITY_STATE_DONE_TXT = "报名结束";
	
	/**活动-未签到*/
	public static final Integer ACTIVITY_SIGNUP_FALSE = 0;
	/**活动-已签到*/
	public static final Integer ACTIVITY_SIGNUP_TRUE = 1;
	/**活动-已支付*/
	public static final Integer ACTIVITY_PAYED_TRUE = 1;
	/**活动-未支付*/
	public static final Integer ACTIVITY_PAYED_FALSE = 0;
	/**活动-支付失败*/
	public static final Integer ACTIVITY_PAYED_FAIL = 2;
	
	/**活动-查询条件的key（session）*/
	public static final String ACTIVITY_CONDITION_KEY = "activityQueryCondition";
	/**成长记录类型-活动*/
	public static final Integer GROWN_TYPE_ACTIVITY = 1;
	/**成长记录类型-课程*/
	public static final Integer GROWN_TYPE_COURSE = 2;
	/**查询条件状态-未制定*/
	public static final Integer CONDITION_DEFAULT_TRUE = 1;
	/**查询条件状态-已制定*/
	public static final Integer CONDITION_DEFAULT_FALSE = 0;
	
	/**增加页面*/
	public static final int INPUTTYPE_ADD = 1 ;
	/**编辑页面*/
	public static final int INPUTTYPE_EDIT = 2 ;
	
	/**分页大小*/
	public static final Integer PAGER_SIZE = 20;
	/**管理员口令*/
	public static final String MANAGER_PASSWORD = "zhuchao";
}
