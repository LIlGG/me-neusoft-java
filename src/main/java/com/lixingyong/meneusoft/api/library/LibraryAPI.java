package com.lixingyong.meneusoft.api.library;

/**
 * @ClassName com.lixingyong.meneusoft.api.library
 * @Description TODO 图书馆API接口
 * @Author mail@lixingyong.com
 * @Date 2019-03-16 15:22
 */
public final class LibraryAPI {
    /** 图书馆系统首页(无需登录VPN) */
    public static final String LIBRARYHOME = "http://library.neusoft.edu.cn";

    /** 图书馆公共检索系统 */
    public static final String LIBRARYREADER = "http/reader.library.neusoft.edu.cn";

    /** 图书馆用户登录 */
    public static final String LIBRARYLOGIN = LIBRARYREADER + "/reader/login.jsp?str_kind=login";

    /** 图书馆借阅历史查询*/
    public static final String LIBRARYHISTORY = LIBRARYREADER+ "/reader/readerHistory.jsp";

    /** 图书馆图书搜索 */
    public static final String SEARCH = LIBRARYREADER + "/book/queryOut.jsp";

    /** 图书信息详细信息查询*/
    public static final String DETAILS = LIBRARYREADER + "/book/detailBook.jsp?rec_ctrl_id={rid}";
}
