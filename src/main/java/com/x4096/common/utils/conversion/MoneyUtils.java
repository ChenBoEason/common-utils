package com.x4096.common.utils.conversion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: common-utils
 * @DateTime: 2019-08-15 23:31
 * @Description: 金钱格式转换 元->分,分->元
 */
public class MoneyUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(MoneyUtils.class);

    /**
     * 元转分
     */
    private static Pattern y2FPattern = Pattern.compile("^[0-9]+(.[0-9]{0,2})?$");

    /**
     * 分转元
     */
    private static Pattern f2YPattern = Pattern.compile("^[0-9]+$");
    private static final int MULTIPLIER = 100;


    /**
     * 元转分
     * 支持以下格式
     * #    1
     * #.#  1.2
     * #.## 1.23
     *
     * @param yuan
     * @return
     */
    public static int changeF2Y(final String yuan) {
        Matcher matcher = y2FPattern.matcher(yuan);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("元转分,参数格式不正确!转换内容: " + yuan);
        }
        NumberFormat format = NumberFormat.getInstance();
        Number number;

        try {
            number = format.parse(yuan);
        } catch (ParseException e) {
            LOGGER.error("元转分,转换异常,转换内容: {}", yuan, e);
            throw new RuntimeException("元转分,转换异常,转换内容: " + yuan);
        }

        double temp = number.doubleValue() * 100.0;
        format.setGroupingUsed(false);
        format.setMaximumFractionDigits(0);
        return Integer.valueOf(format.format(temp));
    }


    /**
     * 元转分
     * 支付dubbo格式
     * #    1
     * #.#  1.2
     * #.## 1.23
     *
     * @param yuan
     * @return
     */
    public static int changeF2Y(final double yuan) {
        return changeF2Y(String.valueOf(yuan));
    }


    /**
     * 分转元
     *
     * @param fen
     * @return
     */
    public static String changeY2F(final String fen) {
        Matcher matcher = f2YPattern.matcher(fen);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("分转元,参数格式不正确!转换内容: " + fen);
        }
        return new BigDecimal(fen).divide(new BigDecimal(MULTIPLIER)).setScale(2).toString();
    }


    /**
     * 分转元
     *
     * @param fen
     * @return
     */
    public static String changeY2F(final int fen) {
        return changeY2F(String.valueOf(fen));
    }

}
