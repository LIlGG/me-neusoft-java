package com.lixingyong.meneusoft.common.utils;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;

/**
 * @ClassName
 * @Description TODO
 * @Author lixingyong
 * @Date 2018/12/11 13:44
 * @Version 1.0
 */
public class Tess4JUtil {
    public static String executeTess4J(String imgUrl){
        String ocrResult = "";
        try{
            Tesseract instance = new Tesseract();
            instance.setLanguage("osd");
            File imgDir = new File(imgUrl);
            //long startTime = System.currentTimeMillis();
            ocrResult = instance.doOCR(imgDir).replaceAll(" ","");
        }catch (TesseractException e){
            e.printStackTrace();
        }
        return ocrResult;
    }
}
