package com.github.x4096.common.utils.life;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: common-utils
 * @DateTime: 2019-07-19 00:22
 * @Description: 中国汉字拼音工具类
 * 部分参考: https://github.com/0opslab/opslabJutil/blob/master/src/main/java/com/opslab/util/ChinesUtil.java
 */
public class PinYinUtils {

    private PinYinUtils() {
    }

    private static final Logger logger = LoggerFactory.getLogger(PinYinUtils.class);

    /**
     * 中文Unicode范围
     */
    private static final String REGEX = "[\\u4E00-\\u9FA5]+";


    /**
     * 获取输入中文字符串拼音
     *
     * @param inputString 汉字串
     * @return 获取输入中文字符串拼音
     */
    public static String getPinYin(String inputString) {
        if (StringUtils.isBlank(inputString)) {
            return "";
        }
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);

        char[] input = inputString.trim().toCharArray();
        StringBuilder output = new StringBuilder();

        try {
            for (char c : input) {
                if (Character.toString(c).matches(REGEX)) {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(c, format);
                    output.append(temp[0]);
                } else {
                    output.append(c);
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            logger.error("获取拼音异常,请求入参: {}", inputString, e);
        }
        return output.toString();
    }


    /**
     * 获取汉字串拼音首字母，英文字符不变
     *
     * @param chinese 汉字串
     * @return 汉语拼音首字母
     */
    public static String getFirstSpell(String chinese) {
        if (StringUtils.isBlank(chinese)) {
            return "";
        }
        char[] arr = chinese.toCharArray();
        StringBuilder pybf = new StringBuilder();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (char c : arr) {
            if (c > 128) {
                try {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(c, defaultFormat);
                    if (temp != null) {
                        pybf.append(temp[0].charAt(0));
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    logger.error("", e);
                }
            } else {
                pybf.append(c);
            }
        }
        return pybf.toString().replaceAll("\\W", "").trim();
    }


    /**
     * 获取汉字串拼音，英文字符不变
     *
     * @param chinese 汉字串
     * @return 汉语拼音
     */
    public static String getFullSpell(String chinese) {
        if (StringUtils.isBlank(chinese)) {
            return "";
        }
        StringBuilder pybf = new StringBuilder();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        char[] arr = chinese.toCharArray();
        for (char c : arr) {
            if (c > 128) {
                try {
                    pybf.append(PinyinHelper.toHanyuPinyinStringArray(c, defaultFormat)[0]);
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pybf.append(c);
            }
        }
        return pybf.toString();
    }

}
