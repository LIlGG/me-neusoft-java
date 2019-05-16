package com.lixingyong.meneusoft.api.jwc;

import com.lixingyong.meneusoft.api.RestConfig;
import com.lixingyong.meneusoft.api.VPNInterface;
import com.lixingyong.meneusoft.api.vpn.VPNAPI;
import com.lixingyong.meneusoft.common.exception.WSExcetpion;
import com.lixingyong.meneusoft.modules.xcx.entity.Classroom;
import com.lixingyong.meneusoft.modules.xcx.service.ClassroomService;
import com.lixingyong.meneusoft.modules.xcx.service.CourseScheduleService;
import com.lixingyong.meneusoft.modules.xcx.service.CourseService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import java.util.ArrayList;
import java.util.List;

public abstract class JWCAbs  extends VPNInterface {
    /** 阿里云API的bucket名称 */
    protected static String bucketName = RestConfig.getBucketName();
    /** 阿里云API的文件夹名称 */
    protected static String folder = RestConfig.getFolder();
    /** 阿里云API的文件前缀 */
    protected static String codeFolder = RestConfig.getCodeFolder();
    /** 阿里云API的文件后缀 */
    protected static String suffix = RestConfig.getSuffix();

    protected static CourseService courseService = RestConfig.getCourseService();
    protected static CourseScheduleService courseScheduleService = RestConfig.getCourseScheduleService();

    protected static String URL(String url) throws WSExcetpion {
        return isVPN() ? VPNAPI.PROXY + JWCAPI.JWC_VPN_API + url :  JWCAPI.JWC_API + url;
    }

}
