package com.x4096.common.utils.captcha;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: common-utils
 * @DateTime: 2019/5/19 15:02
 * @Description: 算数表达式验证码
 */
public class ArithmeticCaptcha extends Captcha {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArithmeticCaptcha.class);

    /**
     * 算法表达式
     */
    private static char[] ops = new char[]{'+', '-'};


    /**
     * 算法表达式计算结果
     */
    private int result;

    public ArithmeticCaptcha() {
    }

    public ArithmeticCaptcha(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public ArithmeticCaptcha(int width, int height, int fontSize) {
        this(width, height);
        font = new Font("Verdana", Font.ITALIC | Font.BOLD, fontSize);
    }

    public ArithmeticCaptcha(int width, int height, Font font) {
        this(width, height);
        this.font = font;
    }


    @Override
    public void out(OutputStream outputStream) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        Color color;
        /* 设置背景颜色 */
        g.fillRect(0, 0, width, height);
        g.setColor(Color.white);
        g.drawRect(0, 0, width - 1, height - 1);


        /* 生成干扰线 */
        for (int i = 0; i < 50; i++) {
            color = color(150, 250);
            g.setColor(color);
            /* 画蛋蛋，有蛋的生活才精彩 */
            g.drawOval(num(width), num(height), 5 + num(10), 5 + num(10));
        }

        /* 生成随机算数表达式 */
        String exp = generateVerifyCode();
        char[] expChars = exp.toCharArray();
        int len = expChars.length;

        this.setCaptcha(exp);
        this.result = calc(exp);

        g.setFont(font);
        int h = height - ((height - font.getSize()) >> 1),
                w = width / len - 2,
                size = w - font.getSize() + 1;

        /* 画字符串 */
        for (int i = 0; i < len; i++) {
            /* 对每个字符都用随机颜色 */
            color = new Color(20 + num(110), 20 + num(110), 20 + num(110));
            g.setColor(color);
            g.drawString(expChars[i] + "", (width - (len - i) * w) + size, h - 4);
        }
        g.dispose();

        try {
            ImageIO.write(image, "JPG", outputStream);
            outputStream.flush();
        } catch (IOException e) {
            LOGGER.error("数字表达式验证码输出流异常", e);
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                LOGGER.error("数字表达式输出流关闭异常", e);
            }

        }

    }


    /**
     * 引用 Script 引擎,计算结果
     *
     * @param exp
     * @return
     */
    private static int calc(String exp) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        try {
            return (Integer) engine.eval(exp);
        } catch (ScriptException e) {
            LOGGER.error("Script 异常", e);
        }
        return -1;
    }


    /**
     * 生成算数表达式
     *
     * @return
     */
    private String generateVerifyCode() {
        Random rdm = new Random();
        int num1 = rdm.nextInt(10);
        int num2 = rdm.nextInt(10);
        int num3 = rdm.nextInt(10);
        char op1 = ops[rdm.nextInt(2)];
        char op2 = ops[rdm.nextInt(2)];
        return "" + num1 + op1 + num2 + op2 + num3;
    }


    public int getResult() {
        return result;
    }
}
