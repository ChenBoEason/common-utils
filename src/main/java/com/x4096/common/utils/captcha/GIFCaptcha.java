package com.x4096.common.utils.captcha;

import com.x4096.common.utils.captcha.gif.GIFEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @Author: peng.zhup
 * @Project: common-utils
 * @DateTime: 2019/5/19 11:14
 * @Description: GIF 验证码
 */
public class GIFCaptcha extends Captcha {

    private static final Logger LOGGER = LoggerFactory.getLogger(GIFCaptcha.class);

    public GIFCaptcha() {
    }

    public GIFCaptcha(int width, int height){
        this.width = width;
        this.height = height;
    }

    public GIFCaptcha(int width, int height, int len){
        this(width, height);
        this.captchaLength = len;
    }

    public GIFCaptcha(int width, int height, int len, int fontSize){
        this(width, height, len);
        font = new Font("Verdana", Font.ITALIC | Font.BOLD, fontSize);
    }

    public GIFCaptcha(int width, int height, int len, Font font){
        this(width, height, len);
        this.font = font;
    }


    /**
     * 重写 out
     *
     * @param os 输出流
     */
    @Override
    public void out(OutputStream os) {
        /* gif编码类，这个利用了洋人写的编码类 */
        GIFEncoder gifEncoder = new GIFEncoder();
        BufferedImage bufferedImage = null;
        /* 生成字符 */
        gifEncoder.start(os);
        gifEncoder.setQuality(180);
        gifEncoder.setDelay(100);
        gifEncoder.setRepeat(0);
        char[] rands = alphas();

        try{
            Color fontcolor[] = new Color[captchaLength];

            for(int i=0; i<captchaLength; i++){
                fontcolor[i] = new Color(20 + num(110), 20 + num(110), 20 + num(110));
            }

            for(int i=0; i<captchaLength; i++){
                bufferedImage = graphicsImage(fontcolor, rands, i);
                gifEncoder.addFrame(bufferedImage);
                bufferedImage.flush();
            }
            gifEncoder.finish();
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                LOGGER.error("输出流关闭异常", e);
            }
        }
    }

    /**
     * 画随机码图
     *
     * @param fontcolor 随机字体颜色
     * @param strs      字符数组
     * @param flag      透明度使用
     * @return BufferedImage
     */
    private BufferedImage graphicsImage(Color[] fontcolor, char[] strs, int flag){
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        /* 或得图形上下文 */
        Graphics2D g2d = (Graphics2D)image.getGraphics();
        //利用指定颜色填充背景
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);
        AlphaComposite ac3;
        int h  = height - ((height - font.getSize()) >>1);
        int w = width/captchaLength -2;
        g2d.setFont(font);

        for(int i=0; i<captchaLength; i++){
            ac3 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, getAlpha(flag, i));
            g2d.setComposite(ac3);
            g2d.setColor(fontcolor[i]);
            g2d.drawOval(num(width), num(height), 5+num(10), 5+num(10));
            g2d.drawString(strs[i]+"", (width-(captchaLength-i)*w)+(w-font.getSize())+1, h-4);
        }
        g2d.dispose();
        return image;
    }

    /**
     * 获取透明度,从0到1,自动计算步长
     *
     * @return float 透明度
     */
    private float getAlpha(int i,int j) {
        int num = i+j;
        float r = (float)1/captchaLength, s = (captchaLength + 1) * r;
        return num > captchaLength ? (num *r - s) : num * r;
    }

}
