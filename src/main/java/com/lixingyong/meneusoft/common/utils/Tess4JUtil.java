package com.lixingyong.meneusoft.common.utils;


import com.recognition.software.jdeskew.ImageDeskew;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.ImageHelper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @ClassName
 * @Description TODO
 * @Author lixingyong
 * @Date 2018/12/11 13:44
 * @Version 1.0
 */
public class Tess4JUtil {
    static final double MINIMUM_DESKEW_THRESHOLD = 0.05d;
    public static String executeTess4J(String imgUrl) throws IOException {
        String ocrResult = "";
        try{
            Tesseract instance = new Tesseract();
            instance.setDatapath("F:\\SpringBootProject\\meneusoft\\tessdata\\");
            instance.setLanguage("num");
            File imgDir = new File(imgUrl);
            BufferedImage bi = ImageIO.read(imgDir);
            ImageDeskew id = new ImageDeskew(bi);
            double imageSkewAngle = id.getSkewAngle(); // determine skew angle
            if ((imageSkewAngle > MINIMUM_DESKEW_THRESHOLD || imageSkewAngle < -(MINIMUM_DESKEW_THRESHOLD))) {
                bi = ImageHelper.rotateImage(bi, -imageSkewAngle); // deskew image
            }
            //long startTime = System.currentTimeMillis();
            ocrResult = instance.doOCR(bi).replaceAll(" ","");
        }catch (TesseractException e){
            e.printStackTrace();
        }
        return ocrResult;
    }

    public static String executeTess4J2(String imgUrl) throws IOException {
        String ocrResult = "";
        try{
            Tesseract instance = new Tesseract();
            instance.setDatapath("F:\\SpringBootProject\\meneusoft\\tessdata\\");
            instance.setLanguage("num");
            File imgDir = new File(imgUrl);
            //long startTime = System.currentTimeMillis();
            ocrResult = instance.doOCR(imgDir).replaceAll(" ","");
        }catch (TesseractException e){
            e.printStackTrace();
        }
        return ocrResult;
    }

    public static String executeTess4J3(String imgUrl) throws IOException {
        String ocrResult = "";
        try{
            Tesseract instance = new Tesseract();
            instance.setDatapath("F:\\SpringBootProject\\meneusoft\\tessdata\\");
            instance.setLanguage("eng");
            File imgDir = new File(imgUrl);
            //long startTime = System.currentTimeMillis();
            ocrResult = instance.doOCR(imgDir).replaceAll(" ","");
        }catch (TesseractException e){
            e.printStackTrace();
        }
        return ocrResult;
    }
}
