package com.lixingyong.meneusoft.api.ecard;

/**
 *  一卡通接口
 */
public final class EcardAPI {
    /** 一卡通地址 */
    public final static String ECARD = "https://ecard.neusoft.edu.cn/zh-hans";
    /** 获取一卡通cookie地址 */
    public final static String ECARDCOOKIE = ECARD + "/{ecardId}";
    /** 获取一卡通余额地址 */
    public final static String BALANCE = ECARD + "/user/balance";
    /** 获取一卡通流水 */
    public final static String ECATDDETAIL = ECARD + "/user/detail/{month}";
}
