package com.lixingyong.meneusoft.common.utils;

import java.io.File;

/**
 * @ClassName ImagePreProcess
 * @Description TODO
 * @Author lixingyong
 * @Date 2018/12/7 13:58
 * @Version 1.0
 */
public class ImagePreProcess {


    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        //原始验证码地址
        String OriginalImg = "D:\\test1.jpg";
        //识别样本输出地址
        String ocrResult = "D:\\test2.jpg";
        //去噪点
        ImgUtils.cleanLinesInImage(OriginalImg, ocrResult);
        //OCR识别
        String code = Tess4J.executeTess4J(ocrResult);
        //输出识别结果
        System.out.println("Ocr识别结果: \n" + code);
    }
}
