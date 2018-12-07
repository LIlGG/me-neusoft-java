package com.lixingyong.meneusoft.common.utils;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.Tesseract1;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;

/**
 * @ClassName Tess4j
 * @Description TODO
 * @Author lixingyong
 * @Date 2018/12/7 14:37
 * @Version 1.0
 */
public class Tess4J {
    public static String executeTess4J(String imgUrl){
        String ocrResult = "";
        try{
            ITesseract instance = new Tesseract();
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
