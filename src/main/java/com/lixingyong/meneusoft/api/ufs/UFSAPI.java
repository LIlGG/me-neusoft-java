package com.lixingyong.meneusoft.api.ufs;

public final class UFSAPI {
    public static final String UFS = "http://ufs.neusoft.edu.cn";
    /** 登录界面(用于获取cookie) */
    public static final String LOGIN = UFS+"/Login.jsp";
    /** 登录方法 */
    public static final String LOGIN_ACTION = UFS + "/loginAction.do";
    /** 登录后注销 */
    public static final String LOGOUT = UFS+"/jsp/global/removesession.jsp";
    /** 查询基本信息 */
    public static final String BASE_INFO = UFS+"/preViewOwnBaseInfo.do";
    /** 查询成绩界面 */
    public static final String QUERY_SCORE_PAGE = UFS+"/PreQPSQueryScoreAction.do";
    /** 查询成绩方法 */
    public static final String QUERY_SCORE = UFS+"/processQPSQueryScore.do";
}
