package com.lixingyong.meneusoft.api.neusoft;


/**
 * @ClassName
 * @Description TODO 大连东软信息学院API
 * @Author lixingyong
 * @Date 2018/11/20 13:59
 * @Version 1.0
 */
public final class NeusoftAPI {
    /** 普通类型教务处 */
    public static final String JWC = "http://newjw.neusoft.edu.cn/jwweb";
    /** 教务处校历查询 */
    public static final String TERM = JWC+ "/_data/index_lookxl.aspx";
    /** 获取节假日的API接口 */
    public static final String YEAR_API = "https://sp0.baidu.com/8aQDcjqpAAV3otqbppnN2DJv/api.php?query={year}年{month}月&resource_id=6018&format=json";
}
