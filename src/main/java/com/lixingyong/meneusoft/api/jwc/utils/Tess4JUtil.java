package com.lixingyong.meneusoft.api.jwc.utils;

import com.lixingyong.meneusoft.api.RestConfig;
import com.lixingyong.meneusoft.common.exception.WSExcetpion;
import com.lixingyong.meneusoft.common.utils.RedisUtils;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.awt.image.BufferedImage;
import java.io.*;

public class Tess4JUtil {
    private static RedisUtils redisUtils = RestConfig.getRedisUtils();
    private static String tess4jData = RestConfig.getTess4jData();
    public static boolean executeTess4J(InputStream code){
        String ocrResult = "";
        try {
            ITesseract instance = new Tesseract();
            instance.setDatapath(tess4jData);
            instance.setLanguage("eng");
            BufferedImage image =  ImgUtil.cleanLinesInImage(code);
            ocrResult = instance.doOCR(image).replaceAll(" ","");
            ocrResult = ocrResult.replaceAll("\n","");
            // 执行验证码校验
            if(ocrResult.isEmpty()){
                return false;
            }
            if(ocrResult.length() != 4){
                return false;
            }
            if(!isLetterDigit(ocrResult)){
                return false;
            }
            // 将code保存在redis中
            redisUtils.set("COURSE_CODE", ocrResult);
        } catch (TesseractException e){
            throw new WSExcetpion("转换出错");
        } catch (IOException e){
            throw new WSExcetpion("输入流错误");
        }
        return true;
    }

    /** 判断是否 */
    private static boolean isLetterDigit(String str) {
        String regex = "^[a-z0-9A-Z]+$";
        return str.matches(regex);
    }
}
