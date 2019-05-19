package com.x4096.common.utils.captcha;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @Author: peng.zhup
 * @Project: common-utils
 * @DateTime: 2019/5/19 11:44
 * @Description: JPG 格式验证码
 */
public class JPGCaptcha extends Captcha {

    private static final Logger LOGGER = LoggerFactory.getLogger(JPGCaptcha.class);


    public JPGCaptcha() {
    }

    public JPGCaptcha(int width, int height){
        this.width = width;
        this.height = height;
    }



    public JPGCaptcha(int width, int height, int len){
        this(width, height);
        this.captchaLength = len;
    }

    public JPGCaptcha(int width, int height, int len, int fontSize){
        this(width, height, len);
        font = new Font("Verdana", Font.ITALIC | Font.BOLD, fontSize);
    }

    public JPGCaptcha(int width, int height, int len, Font font){
        this(width, height, len);
        this.font = font;
    }




    @Override
    public void out(OutputStream outputStream) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        char[] strs = alphas();
        Graphics2D g = (Graphics2D)image.getGraphics();
        AlphaComposite ac3;
        Color color ;
        int len = strs.length;
        g.setColor(Color.WHITE);
        g.fillRect(0,0,width ,height);

        /* 随机画干扰的蛋蛋 */
        for(int i=0; i<15; i++){
            color = color(150, 250);
            g.setColor(color);
            /* 画蛋蛋，有蛋的生活才精彩 */
            g.drawOval(num(width), num(height), 5+num(10), 5+num(10));
        }

        g.setFont(font);
        int h = height - ((height - font.getSize()) >>1),
            w = width/len - 2,
            size = w - font.getSize()+1;

        /* 画字符串 */
        for(int i=0; i<len; i++) {
            /* 指定透明度 */
            ac3 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f);
            g.setComposite(ac3);
            /* 对每个字符都用随机颜色 */
            color = new Color(20 + num(110), 20 + num(110), 20 + num(110));
            g.setColor(color);
            g.drawString(strs[i]+"",(width - (len - i) * w) + size, h-4);
        }
        g.dispose();

        try {
            ImageIO.write(image, "JPG", outputStream);
            outputStream.flush();
        }catch (IOException e){
            LOGGER.error("JPG 验证码生成异常", e);
        }finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                LOGGER.error("输出流关闭异常", e);
            }
        }
    }

}
