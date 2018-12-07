package com.lixingyong.meneusoft.common.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @ClassName ImgUtils
 * @Description TODO
 * @Author lixingyong
 * @Date 2018/12/7 14:18
 * @Version 1.0
 */
public class ImgUtils {
//    public static void removeBackground(String imgUrl, String resUrl){
//        //定义一个临界阈值
//        int threshold = 600;
//        try{
//            BufferedImage img = ImageIO.read(new File(imgUrl));
//            int width = img.getWidth();
//            int height = img.getHeight();
//            for(int i = 1;i < width;i++){
//                for (int x = 0; x < width; x++){
//                    for (int y = 0; y < height; y++){
//                        Color color = new Color(img.getRGB(x, y));
//                        System.out.println("red:"+color.getRed()+" | green:"+color.getGreen()+" | blue:"+color.getBlue());
//                        int num = color.getRed()+color.getGreen()+color.getBlue();
//                        if(num >= threshold){
//                            img.setRGB(x, y, Color.WHITE.getRGB());
//                        }
//                    }
//                }
//            }
//            for(int i = 1;i<width;i++){
//                Color color1 = new Color(img.getRGB(i, 1));
//                int num1 = color1.getRed()+color1.getGreen()+color1.getBlue();
//                for (int x = 0; x < width; x++)
//                {
//                    for (int y = 0; y < height; y++)
//                    {
//                        Color color = new Color(img.getRGB(x, y));
//
//                        int num = color.getRed()+color.getGreen()+color.getBlue();
//                        if(num==num1){
//                            img.setRGB(x, y, Color.BLACK.getRGB());
//                        }else{
//                            img.setRGB(x, y, Color.WHITE.getRGB());
//                        }
//                    }
//                }
//            }
//
//            //去除干扰线条
////            for(int y = 1; y < height-1; y++){
////                for(int x = 1; x < width-1; x++){
////                    boolean flag = false ;
////                    if(isBlack(img.getRGB(x, y))){
////                        //左右均为空时，去掉此点
////                        if(isWhite(img.getRGB(x-1, y)) && isWhite(img.getRGB(x+1, y))){
////                            flag = true;
////                        }
////                        //上下均为空时，去掉此点
////                        if(isWhite(img.getRGB(x, y+1)) && isWhite(img.getRGB(x, y-1))){
////                            flag = true;
////                        }
////                        //斜上下为空时，去掉此点
////                        if(isWhite(img.getRGB(x-1, y+1)) && isWhite(img.getRGB(x+1, y-1))){
////                            flag = true;
////                        }
////                        if(isWhite(img.getRGB(x+1, y+1)) && isWhite(img.getRGB(x-1, y-1))){
////                            flag = true;
////                        }
////                        if(flag){
////                            img.setRGB(x,y,-1);
////                        }
////                    }
////                }
////            }
//            File file = new File(resUrl);
//            if (!file.exists())
//            {
//                File dir = file.getParentFile();
//                if (!dir.exists())
//                {
//                    dir.mkdirs();
//                }
//                try
//                {
//                    file.createNewFile();
//                }
//                catch (IOException e)
//                {
//                    e.printStackTrace();
//                }
//            }
//            ImageIO.write(img, "jpg", file);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//
//    public static BufferedImage cropImage(BufferedImage bufferedImage, int startX, int startY, int endX, int endY) {
//        int width = bufferedImage.getWidth();
//        int height = bufferedImage.getHeight();
//        if (startX == -1) {
//            startX = 0;
//        }
//        if (startY == -1) {
//            startY = 0;
//        }
//        if (endX == -1) {
//            endX = width - 1;
//        }
//        if (endY == -1) {
//            endY = height - 1;
//        }
//        BufferedImage result = new BufferedImage(endX - startX, endY - startY, 4);
//        for (int x = startX; x < endX; ++x) {
//            for (int y = startY; y < endY; ++y) {
//                int rgb = bufferedImage.getRGB(x, y);
//                result.setRGB(x - startX, y - startY, rgb);
//            }
//        }
//        return result;
//    }



    /**
     *
     * @param sfile
     *            需要去噪的图像
     * @param destDir
     *            去噪后的图像保存地址
     * @throws IOException
     */
    public static void cleanLinesInImage(String sfile, String destDir)  throws IOException{
        BufferedImage bufferedImage = ImageIO.read(new File(sfile));
        int h = bufferedImage.getHeight();
        int w = bufferedImage.getWidth();

        // 灰度化
        int[][] gray = new int[w][h];
        for (int x = 0; x < w; x++)
        {
            for (int y = 0; y < h; y++)
            {
                int argb = bufferedImage.getRGB(x, y);
                // 图像加亮（调整亮度识别率非常高）
                int r = (int) (((argb >> 16) & 0xFF) * 1.1 + 30);
                int g = (int) (((argb >> 8) & 0xFF) * 1.1 + 30);
                int b = (int) (((argb >> 0) & 0xFF) * 1.1 + 30);
                if (r >= 255)
                {
                    r = 255;
                }
                if (g >= 255)
                {
                    g = 255;
                }
                if (b >= 255)
                {
                    b = 255;
                }
                //此处根据实际需要进行设定阈值
                gray[x][y] = (int) Math
                        .pow((Math.pow(r, 2.2) * 0.2973 + Math.pow(g, 2.2)
                                * 0.6274 + Math.pow(b, 2.2) * 0.0753), 1 / 2.2);
            }
        }

        // 二值化
        int threshold = ostu(gray, w, h);
        BufferedImage binaryBufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_BINARY);
        for (int x = 0; x < w; x++)
        {
            for (int y = 0; y < h; y++)
            {
                if (gray[x][y] > threshold)
                {
                    gray[x][y] |= 0x00FFFF;
                } else
                {
                    gray[x][y] &= 0xFF0000;
                }
                binaryBufferedImage.setRGB(x, y, gray[x][y]);
            }
        }

        //去除干扰线条
        for (int y = 1; y < h - 1; y++) {
            for (int x = 1; x < w - 1; x++) {

                boolean lineFlag = false;//去除线判定
                int pointflagNum = 0;//去除点判定

                if (isBlack(binaryBufferedImage.getRGB(x, y))) {
                    //左右像素点为"白"即空时，去掉此点
                    if (isWhite(binaryBufferedImage.getRGB(x - 1, y)) && isWhite(binaryBufferedImage.getRGB(x + 1, y))) {
                        lineFlag = true;
                        pointflagNum += 2;
                    }
                    //上下像素点为"白"即空时，去掉此点
                    if (isWhite(binaryBufferedImage.getRGB(x, y + 1)) && isWhite(binaryBufferedImage.getRGB(x, y - 1))) {
                        lineFlag = true;
                        pointflagNum += 2;
                    }
                    //斜上像素点为"白"即空时，去掉此点
                    if (isWhite(binaryBufferedImage.getRGB(x - 1, y + 1)) && isWhite(binaryBufferedImage.getRGB(x + 1, y - 1))) {
                        lineFlag = true;
                        pointflagNum += 2;
                    }
                    if (isWhite(binaryBufferedImage.getRGB(x + 1, y + 1)) && isWhite(binaryBufferedImage.getRGB(x - 1, y - 1))) {
                        lineFlag = true;
                        pointflagNum += 2;
                    }
                    //去除干扰线
                    if (lineFlag) {
//                        /binaryBufferedImage.setRGB(x, y, -1);
                    }
                    //去除干扰点
                    if (pointflagNum > 3) {
                        binaryBufferedImage.setRGB(x, y, -1);
                    }
                }
            }
        }


        // 矩阵打印
        for (int y = 0; y < h; y++)
        {
            for (int x = 0; x < w; x++)
            {
                if (isBlack(binaryBufferedImage.getRGB(x, y)))
                {
                    System.out.print("*");
                } else
                {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
        File file = new File(destDir);
            if (!file.exists())
            {
                File dir = file.getParentFile();
                if (!dir.exists())
                {
                    dir.mkdirs();
                }
                try
                {
                    file.createNewFile();
               }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            ImageIO.write(binaryBufferedImage, "jpg", file);
    }

    public static boolean isBlack(int colorInt)
    {
        Color color = new Color(colorInt);
        if (color.getRed() + color.getGreen() + color.getBlue() <= 300)
        {
            return true;
        }
        return false;
    }

    public static boolean isWhite(int colorInt)
    {
        Color color = new Color(colorInt);
        if (color.getRed() + color.getGreen() + color.getBlue() > 300)
        {
            return true;
        }
        return false;
    }

    public static int isBlackOrWhite(int colorInt)
    {
        if (getColorBright(colorInt) < 30 || getColorBright(colorInt) > 730)
        {
            return 1;
        }
        return 0;
    }

    public static int getColorBright(int colorInt)
    {
        Color color = new Color(colorInt);
        return color.getRed() + color.getGreen() + color.getBlue();
    }

    public static int ostu(int[][] gray, int w, int h)
    {
        int[] histData = new int[w * h];
        // Calculate histogram
        for (int x = 0; x < w; x++)
        {
            for (int y = 0; y < h; y++)
            {
                int red = 0xFF & gray[x][y];
                histData[red]++;
            }
        }

        // Total number of pixels
        int total = w * h;

        float sum = 0;
        for (int t = 0; t < 256; t++)
            sum += t * histData[t];

        float sumB = 0;
        int wB = 0;
        int wF = 0;

        float varMax = 0;
        int threshold = 0;

        for (int t = 0; t < 256; t++)
        {
            wB += histData[t]; // Weight Background
            if (wB == 0)
                continue;

            wF = total - wB; // Weight Foreground
            if (wF == 0)
                break;

            sumB += (float) (t * histData[t]);

            float mB = sumB / wB; // Mean Background
            float mF = (sum - sumB) / wF; // Mean Foreground

            // Calculate Between Class Variance
            float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);

            // Check if new maximum found
            if (varBetween > varMax)
            {
                varMax = varBetween;
                threshold = t;
            }
        }

        return threshold;
    }

}
