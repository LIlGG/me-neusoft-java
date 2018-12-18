package com.lixingyong.meneusoft.common.utils;

import com.lixingyong.meneusoft.api.jwc.JWCUtil;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @ClassName ImagePreProcess
 * @Description TODO
 * @Author lixingyong
 * @Date 2018/12/7 13:58
 * @Version 1.0
 */
public class ImagePreProcess {
    private static int i = 0;

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) {
        //抓取图片
        Timer timer=new Timer();//实例化Timer类
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    ImagePreProcess.executes();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(task,1 * 1000,1*1000);

    }

    public static void executes() throws Exception {
        i++;
        //原始验证码地址
        // String OriginalImg = "F:\\code\\code"+i+".jpg";
        //识别样本输出地址
        String ocrResult = "F:\\codes\\code"+i+".jpg";
        //去噪点
       // ImgUtils.cleanLinesInImage(OriginalImg, ocrResult);
        //OCR识别
        String code = Tess4JUtil.executeTess4J(ocrResult);
        //输出识别结果
        System.out.println("纠正后的code"+i+"识别结果: " + code);
        String code2 = Tess4JUtil.executeTess4J2(ocrResult);
        System.out.println("不纠正的code"+i+"识别结果: " + code2);
        String code3 = Tess4JUtil.executeTess4J3(ocrResult);
        System.out.println("使用普通的code"+i+"识别结果: " + code3);
    }
}
