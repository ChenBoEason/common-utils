package com.x4096.common.utils.captcha;

import com.x4096.common.utils.captcha.gif.Randoms;

import java.awt.*;
import java.io.OutputStream;
import java.util.UUID;

/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: common-utils
 * @DateTime: 2019/5/19 10:15
 * @Description:
 */
public abstract class Captcha extends Randoms {


    private static String[] chars = new String[] { "a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z" };

    /**
     * 字体
     */
    protected Font font = new Font("Verdana", Font.ITALIC | Font.BOLD, 24);

    /**
     * 验证码随机字符长度
     */
    protected int captchaLength = 6;

    /**
     * 验证码显示宽度
     */
    protected int width = 150;

    /**
     *  验证码显示高度
     */
    protected int height = 40;

    /**
     * 随机字符串
     */
    private String captcha = null;

    /**
     * 生成随机字符数组
     *
     * @return 字符数组
     */
    protected char[] alphas() {
        if(captchaLength > 8){
            throw new IllegalArgumentException("验证码字符个数限制少于 8");
        }
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < captchaLength; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        captcha = shortBuffer.toString();
        return shortBuffer.toString().toCharArray();
    }

    /**
     * 验证码输出,抽象方法，由子类实现
     *
     * @param os 输出流
     */
    public abstract void out(OutputStream os);


    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    /**
     * 获取随机字符串
     *
     * @return
     */
    public String getCaptcha() {
        return captcha;
    }

    /**
     * 给定范围获得随机颜色
     *
     * @return Color 随机颜色
     */
    protected Color color(int fc, int bc) {
        if (fc > 255)
            fc = 255;
        if (bc > 255)
            bc = 255;
        int r = fc + num(bc - fc);
        int g = fc + num(bc - fc);
        int b = fc + num(bc - fc);
        return new Color(r, g, b);
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public int getCaptchaLength() {
        return captchaLength;
    }

    public void setCaptchaLength(int captchaLength) {
        this.captchaLength = captchaLength;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
