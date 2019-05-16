package com.lixingyong.meneusoft.api.jwc;

/**
 * @ClassName JWCAPI
 * @Description TODO 教务处API接口
 * @Author lixingyong
 * @Date 2018/11/29 10:30
 * @Version 1.0
 */
public final class JWCAPI {
    /** VPN类型教务处首页 */
    public final static String JWC_API = "http://newjw.neusoft.edu.cn";
    /** VPN类型教务处首页 */
    public final static String JWC_VPN_API = "http/newjw.neusoft.edu.cn";
    /** 获取Session_Id */
    public final static String JWCSID = "/jwweb/";
    /** 获取验证码 */
    public final static String CODE = JWCSID + "sys/ValidateCode.aspx";
    /** 登录接口 */
    public final static String LOGIN = JWCSID + "_data/home_login.aspx";
    /** 教务处学业基本信息快查 */
    public final static String XYJZQK = JWCSID + "xsxj/Stu_xyjzqk_rpt.aspx";
    /**教务处获取校区信息*/
    public final static String XQ = JWCSID + "ZNPK/KBFB_RoomSel.aspx";
    /**教务处获取课程信息*/
    public final static String COURSE = JWCSID + "ZNPK/KBFB_LessonSel.aspx";
    /** 教务处获取详细课程信息 */
    public final static String COURSE_DETAIL = JWCSID + "ZNPK/KBFB_LessonSel_rpt.aspx";
    /** 教务处获取课程列表*/
    public final static String COURSE_LIST = JWCSID + "ZNPK/Private/List_XNXQKC.aspx?xnxq={termId}";
    /** 教务处获取教学楼信息 */
    public final static String JXL = JWCSID + "ZNPK/Private/List_JXL.aspx?w=150&id={id}";
    /**教务处获取教室信息*/
    public final static String ROOM = JWCSID + "ZNPK/Private/List_ROOM.aspx?w=150&id={id}";
}
