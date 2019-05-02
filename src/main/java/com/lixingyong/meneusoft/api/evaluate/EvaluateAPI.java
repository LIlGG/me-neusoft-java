package com.lixingyong.meneusoft.api.evaluate;

/**
 * 教学评估接口
 */
public final class EvaluateAPI {
    // 教学评教登录前主页（VPN状态）
    public static final String EVALUATE_VPN_HOME = "http/newtqe.neusoft.edu.cn";
    // 教学评估登录前主页
    public static final String EVALUATE_HOME = "http://newtqe.neusoft.edu.cn";
    // 教学评估登录页面(获取登录授权cookies)
    public static final String EVALUATE_LOGIN_PAGE = "/view/jsp/valuation/login.jsp";
    // 教学评估登录接口
    public static final String EVALUATE_LOGIN ="/view/jsp/valuation/LoginAction.action?method=login";
    // 教学评估注销接口
    public static final String EVALUATE_LOGOUT = "/view/jsp/valuation/LoginAction.action?method=logout";
    // 教学评估结果
    public static final String EVALUATE_RESULT = "/StatisticsAction.action?method=queryStuSuRecord";
    // 教学评估登录后首页信息获取接口
    public static final String EVALUATE_LOGIN_ACTION = "/view/jsp/valuation/ValIndexAction.action?method=queryValuationTask";
    // 教学评估任务列表请求后缀
    public static final String TASK_SUFFIX = "eisId={eisId}&academicYearNo={yearNo}&termNo={termNo}";
}
